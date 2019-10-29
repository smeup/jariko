package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.RpgParser
import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.parsing.parsetreetoast.*
import com.smeup.rpgparser.utils.asInt

/**
 * This is a very limited interpreter used at compile time, mainly
 * while examining data definitions.
 *
 * It should consider only statically evaluatable elements.
 */
interface CompileTimeInterpreter {
    fun evaluate(rContext: RpgParser.RContext, expression: Expression): Value
    fun evaluateElementSizeOf(rContext: RpgParser.RContext, expression: Expression): Int
}

object CommonCompileTimeInterpreter : BaseCompileTimeInterpreter()

class InjectableCompileTimeInterpreter : BaseCompileTimeInterpreter() {
    override fun evaluateNumberOfElementsOf(rContext: RpgParser.RContext, declName: String): Int {
        return mockedDecls[declName]?.numberOfElements() ?: super.evaluateNumberOfElementsOf(rContext, declName)
    }

    override fun evaluateElementSizeOf(rContext: RpgParser.RContext, declName: String): Int {
        return mockedDecls[declName]?.elementSize()?.toInt() ?: super.evaluateElementSizeOf(rContext, declName)
    }

    private val mockedDecls = HashMap<String, Type>()

    fun overrideDecl(name: String, type: Type) {
        mockedDecls[name] = type
    }
}

open class BaseCompileTimeInterpreter : CompileTimeInterpreter {
    override fun evaluate(rContext: RpgParser.RContext, expression: Expression): Value {
        return when (expression) {
            is NumberOfElementsExpr -> IntValue(evaluateNumberOfElementsOf(rContext, expression.value).toLong())
            is IntLiteral -> IntValue(expression.value)
            is StringLiteral -> StringValue(expression.value)
            else -> TODO(expression.toString())
        }
    }
    private fun evaluateNumberOfElementsOf(rContext: RpgParser.RContext, expression: Expression): Int {
        return when (expression) {
            is DataRefExpr -> evaluateNumberOfElementsOf(rContext, expression.variable.name)
            else -> TODO(expression.toString())
        }
    }

    protected open fun evaluateNumberOfElementsOf(rContext: RpgParser.RContext, declName: String): Int {
        rContext.statement()
                .forEach {
                    when {
                        it.dspec() != null -> {
                            val name = it.dspec().ds_name().text
                            if (name == declName) {
                                TODO()
                            }
                        }
                        it.dcl_ds() != null -> {
                            val name = it.dcl_ds().name
                            if (name == declName) {
                                val fieldsInfo: List<FieldInfo> = it.dcl_ds().calculateFieldInfos()
                                return it.dcl_ds().type(
                                        it.dcl_ds().declaredSize(),
                                        fieldsInfo).numberOfElements()
                            }
                        }
                    }
                }
        TODO("Not found: $declName")
    }

    open fun evaluateElementSizeOf(rContext: RpgParser.RContext, declName: String): Int {
        rContext.statement()
                .forEach {
                    when {
                        it.dspec() != null -> {
                            val name = it.dspec().ds_name().text
                            if (name == declName) {
                                // TODO verify...
                                return it.dspec().TO_POSITION().text.asInt()
                            }
                        }
                        it.dcl_ds() != null -> {
                            val name = it.dcl_ds().name
                            if (name == declName) {
                                return it.dcl_ds().elementSizeOf()
                            }
                        }
                    }
                }
        TODO("Not found: $declName")
    }

    override fun evaluateElementSizeOf(rContext: RpgParser.RContext, expression: Expression): Int {
        return when (expression) {
            is DataRefExpr -> evaluateElementSizeOf(rContext, expression.variable.name)
            else -> TODO(expression.toString())
        }
    }
}
