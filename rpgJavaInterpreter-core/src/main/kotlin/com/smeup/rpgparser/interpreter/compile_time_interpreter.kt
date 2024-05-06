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
import com.smeup.rpgparser.RpgParser.Cspec_fixedContext
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.parsing.facade.findAllDescendants
import com.smeup.rpgparser.parsing.parsetreetoast.*
import com.smeup.rpgparser.utils.asInt
import com.strumenta.kolasu.model.tryToResolve

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
            is DataRefExpr -> if (expression.variable.tryToResolve(knownDataDefinitions))
                return this.evaluate(rContext, (expression.variable.referred as? DataDefinition)?.initializationValue as Expression)
                    else
                TODO(expression.toString())
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
                                val fieldsList = it.dcl_ds().calculateFieldInfos(knownDataDefinitions)
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

        return findSize(rContext.statement() + rContext.subroutine().flatMap { it.statement() }, declName, conf, false)!!
    }

    private fun findSize(statements: List<RpgParser.StatementContext>, declName: String, conf: ToAstConfiguration, innerBlock: Boolean = true): Int? {
        statements.forEach {
            when {
                it.fspec_fixed() != null -> {
                    val size = it.fspec_fixed().runParserRuleContext(conf) { context ->
                        kotlin.runCatching { context.toAst(conf).let { dataDefinition -> dataDefinition to dataDefinition.toDataDefinitions() } }.getOrNull()
                    }?.second?.find { it.name.equals(declName, ignoreCase = true) }?.elementSize()
                    if (size != null) return size
                }
                it.dspec() != null -> {
                    val name = it.dspec().ds_name()?.text ?: it.dspec().dspecConstant().ds_name()?.text
                    if (declName.equals(name, ignoreCase = true)) {
                        return it.dspec().TO_POSITION().text.asInt()
                    }
                }
                it.cspec_fixed() != null -> {
                    val size = it.cspec_fixed().findType(declName, conf)?.size
                    if (size != null) return size
                }
                it.dcl_ds() != null -> {
                    val name = it.dcl_ds().name
                    if (name == declName) {
                        return it.dcl_ds().elementSizeOf(knownDataDefinitions)
                    }
                }
                it.block() != null -> {
                    val size = it.block().findType(declName, conf)?.size
                    if (size != null) return size
                }
            }
        }
        if (innerBlock)
            return null
        else
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

        return findType(rContext.statement() + rContext.subroutine().flatMap { it.statement() }, declName, conf, false)!!
    }

    private fun findType(statements: List<RpgParser.StatementContext>, declName: String, conf: ToAstConfiguration, innerBlock: Boolean = true): Type? {
        statements
            .forEach { it ->
                when {
//                    it.dcl_ds() != null -> {
//                        val type = it.dcl_ds().toAst(conf = conf, knownDataDefinitions = knownDataDefinitions).fields
//                            .firstOrNull { it.name.equals(declName, ignoreCase = true) }?.type
//                        if (type != null) return type
//                    }
                    it.fspec_fixed() != null -> {
                        val type = it.fspec_fixed().runParserRuleContext(conf) { context ->
                            kotlin.runCatching { context.toAst(conf).let { dataDefinition -> dataDefinition to dataDefinition.toDataDefinitions() } }.getOrNull()
                        }?.second?.find { it.name.equals(declName, ignoreCase = true) }?.type
                        if (type != null) return type
                    }
                    it.dspec() != null -> {
                        val name = it.dspec().ds_name()?.text ?: it.dspec().dspecConstant().ds_name()?.text
                        if (declName.equals(name, ignoreCase = true)) {
                            return it.dspec().toAst(conf = conf, knownDataDefinitions = knownDataDefinitions).type
                        }
                    }
                    it.cspec_fixed() != null -> {
                        val type = it.cspec_fixed().findType(declName, conf)
                        if (type != null) return type
                    }
                    it.block() != null -> {
                        val type = it.block().findType(declName, conf)
                        if (type != null) return type
                    }
                }
            }
        if (innerBlock) {
            return null
        } else {
            throw NotFoundAtCompileTimeException(declName)
        }
    }

    private fun RpgParser.BlockContext.findType(declName: String, conf: ToAstConfiguration): Type? {
        return this.findAllDescendants(type = RpgParser.StatementContext::class, includingMe = false).let { descendants ->
            findType(descendants, declName, conf)
        }
    }

    private fun Cspec_fixedContext.findType(declName: String, conf: ToAstConfiguration): Type? {
        val ast = this.toAst(conf)
        if (ast is StatementThatCanDefineData) {
            val dataDefinition = ast.dataDefinition()
            dataDefinition.forEach {
                if (it.name.asValue().value == declName) {
                    return it.type
                }
            }
        }
        return null
    }
}
