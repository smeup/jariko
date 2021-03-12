package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.RpgParser
import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.parsing.parsetreetoast.*
import com.smeup.rpgparser.utils.asInt

/**
 * This is a very limited interpreter used at compile time, mainly
 * while examining data definitions.
 *
 * It should consider only statically evaluable elements.
 */
interface CompileTimeInterpreter {
    fun evaluate(rContext: RpgParser.RContext, expression: Expression): Value
    fun evaluateElementSizeOf(rContext: RpgParser.RContext, expression: Expression, conf: ToAstConfiguration): Int
    fun evaluateTypeOf(rContext: RpgParser.RContext, expression: Expression, cond: ToAstConfiguration): Type
    fun evaluateNumberOfElementsOf(rContext: RpgParser.RContext, declName: String): Int
}

object CommonCompileTimeInterpreter : BaseCompileTimeInterpreter(emptyList())

class InjectableCompileTimeInterpreter(
    knownDataDefinitions: List<DataDefinition> = emptyList(),
    delegatedCompileTimeInterpreter: CompileTimeInterpreter? = null
) : BaseCompileTimeInterpreter(knownDataDefinitions, delegatedCompileTimeInterpreter) {
    override fun evaluateNumberOfElementsOf(rContext: RpgParser.RContext, declName: String): Int {
        return mockedDecls[declName]?.numberOfElements() ?: super.evaluateNumberOfElementsOf(rContext, declName)
    }

    override fun evaluateElementSizeOf(rContext: RpgParser.RContext, declName: String, conf: ToAstConfiguration): Int {
        return mockedDecls[declName]?.elementSize() ?: super.evaluateElementSizeOf(rContext, declName, conf)
    }

    private val mockedDecls = HashMap<String, Type>()

    fun overrideDecl(name: String, type: Type) {
        mockedDecls[name] = type
    }
}

class NotFoundAtCompileTimeException(val declName: String) : RuntimeException("Unable to calculate element size of $declName")

open class BaseCompileTimeInterpreter(
    val knownDataDefinitions: List<DataDefinition>,
    val delegatedCompileTimeInterpreter: CompileTimeInterpreter? = null
) : CompileTimeInterpreter {

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
            is DataRefExpr -> {
                try {
                    evaluateNumberOfElementsOf(rContext, expression.variable.name)
                } catch (e: NotFoundAtCompileTimeException) {
                    if (delegatedCompileTimeInterpreter != null) {
                        return delegatedCompileTimeInterpreter.evaluateNumberOfElementsOf(rContext, expression.variable.name)
                    } else {
                        throw RuntimeException(e)
                    }
                }
            }
            else -> TODO(expression.toString())
        }
    }

    override fun evaluateNumberOfElementsOf(rContext: RpgParser.RContext, declName: String): Int {
        knownDataDefinitions.forEach {
            if (it.name == declName) {
                return it.numberOfElements()
            }
            val field = it.fields.find { it.name == declName }
            if (field != null) {
                return field.numberOfElements()
            }
        }
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
                                val fieldsList = it.dcl_ds().calculateFieldInfos()
                                return it.dcl_ds().type(
                                        it.dcl_ds().declaredSize(),
                                        fieldsList).numberOfElements()
                            }
                        }
                    }
                }
        throw NotFoundAtCompileTimeException(declName)
    }

    open fun evaluateElementSizeOf(rContext: RpgParser.RContext, declName: String, conf: ToAstConfiguration): Int {
        knownDataDefinitions.forEach {
            if (it.name == declName) {
                return it.elementSize().toInt()
            }
            val field = it.fields.find { it.name == declName }
            if (field != null) {
                if (field.declaredArrayInLine != null) {
                    return (field.elementSize() /*/ field.declaredArrayInLine!!*/).toInt()
                }
                return field.elementSize().toInt()
            }
        }
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
                        it.cspec_fixed() != null -> {
                            val dataDefinition = (it.cspec_fixed().cspec_fixed_standard().toAst(conf) as StatementThatCanDefineData).dataDefinition()
                            dataDefinition.forEach {
                                if (it.name.asValue().value == declName) {
                                    return it.type.size.toInt()
                                }
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
        throw NotFoundAtCompileTimeException(declName)
    }

    override fun evaluateElementSizeOf(rContext: RpgParser.RContext, expression: Expression, conf: ToAstConfiguration): Int {
        return when (expression) {
            is DataRefExpr -> {
                try {
                    evaluateElementSizeOf(rContext, expression.variable.name, conf)
                } catch (e: NotFoundAtCompileTimeException) {
                    if (delegatedCompileTimeInterpreter != null) {
                        return delegatedCompileTimeInterpreter.evaluateElementSizeOf(rContext, expression, conf)
                    } else {
                        throw RuntimeException(e)
                    }
                }
            }
            else -> TODO(expression.toString())
        }
    }

    override fun evaluateTypeOf(rContext: RpgParser.RContext, expression: Expression, conf: ToAstConfiguration): Type {
        return when (expression) {
            is DataRefExpr -> {
                try {
                    evaluateTypeOf(rContext, expression.variable.name, conf)
                } catch (e: NotFoundAtCompileTimeException) {
                    if (delegatedCompileTimeInterpreter != null) {
                        return delegatedCompileTimeInterpreter.evaluateTypeOf(rContext, expression, conf)
                    } else {
                        throw RuntimeException(e)
                    }
                }
            }
            else -> TODO(expression.toString())
        }
    }

    open fun evaluateTypeOf(rContext: RpgParser.RContext, declName: String, conf: ToAstConfiguration): Type {
        knownDataDefinitions.forEach {
            if (it.name == declName) {
                return it.type
            }
            val field = it.fields.find { it.name == declName }
            if (field != null) {
                if (field.declaredArrayInLine != null) {
                    return field.type
                }
                return field.type
            }
        }
        rContext.statement()
            .forEach {
                when {
                    it.cspec_fixed() != null -> {
                        val dataDefinition = (it.cspec_fixed().cspec_fixed_standard().toAst(conf) as StatementThatCanDefineData).dataDefinition()
                        dataDefinition.forEach {
                            if (it.name.asValue().value == declName) {
                                return it.type
                            }
                        }
                    }
                }
            }
        throw NotFoundAtCompileTimeException(declName)
    }
}
