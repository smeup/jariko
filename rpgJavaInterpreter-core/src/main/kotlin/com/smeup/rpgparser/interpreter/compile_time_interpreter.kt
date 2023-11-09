/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.RpgParser
import com.smeup.rpgparser.execution.MainExecutionContext
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
    fun evaluateTypeOf(rContext: RpgParser.RContext, expression: Expression, conf: ToAstConfiguration): Type
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

    override fun evaluateTypeOf(rContext: RpgParser.RContext, declName: String, conf: ToAstConfiguration): Type {
        return mockedDecls[declName] ?: super.evaluateTypeOf(rContext, declName, conf)
    }

    private val mockedDecls = HashMap<String, Type>()

    fun overrideDecl(name: String, type: Type) {
        mockedDecls[name] = type
    }
}

class NotFoundAtCompileTimeException(declName: String) : ParseTreeToAstError("Unable to calculate element size of $declName")

open class BaseCompileTimeInterpreter(
    private val knownDataDefinitions: List<DataDefinition>,
    private val delegatedCompileTimeInterpreter: CompileTimeInterpreter? = null
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
        val conf = MainExecutionContext.getConfiguration().options.toAstConfiguration
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
                                return it.dspec().toAst(conf = conf, knownDataDefinitions = listOf()).let { dataDefinition ->
                                    if (dataDefinition.type is ArrayType) {
                                        dataDefinition.numberOfElements()
                                    } else throw it.dspec().ds_name().error("D spec is not an array", conf = conf)
                                }
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
            if (it.name.equals(declName, ignoreCase = true)) {
                return it.elementSize()
            }
            val field = it.fields.find { it.name.equals(declName, ignoreCase = true) }
            if (field != null) return (field.elementSize() /*/ field.declaredArrayInLine!!*/)
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
                            val statement = it.cspec_fixed().cspec_fixed_standard().toAst(conf)
                            if (statement is StatementThatCanDefineData) {
                                val dataDefinition = (statement as StatementThatCanDefineData).dataDefinition()
                                dataDefinition.forEach {
                                    if (it.name.asValue().value == declName) {
                                        return it.type.size
                                    }
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
                        throw expression.error(message = e.message, cause = e)
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
            if (it.name.equals(declName, ignoreCase = true)) {
                return it.type
            }
            val field = it.fields.find { it.name.equals(declName, ignoreCase = true) }
            if (field != null) {
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
