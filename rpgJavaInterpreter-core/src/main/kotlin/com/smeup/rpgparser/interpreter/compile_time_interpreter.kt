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
import com.smeup.rpgparser.RpgParser.*
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
    fun evaluateElementSizeOf(rContext: RpgParser.RContext, expression: Expression, conf: ToAstConfiguration, procedureName: String? = null): Int
    fun evaluateTypeOf(rContext: RpgParser.RContext, expression: Expression, conf: ToAstConfiguration, procedureName: String? = null): Type
    fun evaluateNumberOfElementsOf(rContext: RpgParser.RContext, declName: String): Int
}

object CommonCompileTimeInterpreter : BaseCompileTimeInterpreter(emptyList())

class InjectableCompileTimeInterpreter(
    knownDataDefinitions: List<DataDefinition> = emptyList(),
    fileDefinitions: Map<FileDefinition, List<DataDefinition>>? = null,
    delegatedCompileTimeInterpreter: CompileTimeInterpreter? = null
) : BaseCompileTimeInterpreter(knownDataDefinitions, fileDefinitions, delegatedCompileTimeInterpreter) {
    override fun evaluateNumberOfElementsOf(rContext: RpgParser.RContext, declName: String): Int {
        return mockedDecls[declName]?.numberOfElements() ?: super.evaluateNumberOfElementsOf(rContext, declName)
    }

    override fun evaluateElementSizeOf(rContext: RpgParser.RContext, declName: String, conf: ToAstConfiguration, procedureName: String?): Int {
        return mockedDecls[declName]?.elementSize() ?: super.evaluateElementSizeOf(rContext, declName, conf, procedureName)
    }

    override fun evaluateTypeOf(rContext: RpgParser.RContext, declName: String, conf: ToAstConfiguration, procedureName: String?): Type {
        return mockedDecls[declName] ?: super.evaluateTypeOf(rContext, declName, conf, procedureName)
    }

    private val mockedDecls = HashMap<String, Type>()

    fun overrideDecl(name: String, type: Type) {
        mockedDecls[name] = type
    }
}

class NotFoundAtCompileTimeException(declName: String) : ParseTreeToAstError("Unable to calculate element size of $declName")

open class BaseCompileTimeInterpreter(
    private val knownDataDefinitions: List<DataDefinition>,
    private val fileDefinitions: Map<FileDefinition, List<DataDefinition>>? = null,
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
                                return it.dspec().toAst(
                                    conf = conf,
                                    knownDataDefinitions = knownDataDefinitions,
                                    fileDefinitions = fileDefinitions
                                ).let { dataDefinition ->
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

    open fun evaluateElementSizeOf(rContext: RpgParser.RContext, declName: String, conf: ToAstConfiguration, procedureName: String?): Int {
        knownDataDefinitions.forEach {
            if (it.name.equals(declName, ignoreCase = true)) {
                return it.elementSize()
            }
            val field = it.fields.find { field -> field.name.equals(declName, ignoreCase = true) }
            if (field != null) return (field.elementSize() /*/ field.declaredArrayInLine!!*/)
        }

        return findSize(rContext.getStatements(procedureName), declName, conf, false)!!
    }

    private fun findSize(statements: List<RpgParser.StatementContext>, declName: String, conf: ToAstConfiguration, innerBlock: Boolean = true): Int? {
        statements.forEach {
            kotlin.runCatching {
                when {
                    it.fspec_fixed() != null -> {
                        val size = it.fspec_fixed().runParserRuleContext(conf) { context ->
                            kotlin.runCatching { context.toAst(conf).toDataDefinitions() }.getOrNull()
                        }?.find { dataDefinition -> dataDefinition.name.equals(declName, ignoreCase = true) }
                            ?.elementSize()
                        if (size != null) return size
                    }
                    it.dspec() != null -> {
                        val name = it.dspec().ds_name()?.text ?: it.dspec().dspecConstant().ds_name()?.text
                        if (declName.equals(name, ignoreCase = true)) {
                            /**
                             * If this is a D-Spec based on a LIKE, recursively find and return its size
                             * else-wise return explicit size
                             */
                            val likeSize = it.dspec().keyword().firstOrNull { it.keyword_like() != null }?.let {
                                val target = it.keyword_like()!!.name.text.trim()
                                findSize(statements, target, conf, innerBlock)
                            }
                            return likeSize ?: it.dspec().TO_POSITION().text.asInt()
                        }
                    }
                    it.cspec_fixed() != null -> {
                        val size = it.cspec_fixed().findType(
                            declName = declName,
                            lookupIn = statements,
                            conf = conf
                        )?.size
                        if (size != null) return size
                    }
                    it.dcl_ds() != null -> {
                        val name = it.dcl_ds().name
                        val fields = fileDefinitions?.let { defs -> it.dcl_ds().getExtnameFields(defs, conf) } ?: emptyList()
                        if (name.equals(declName, ignoreCase = true)) {
                            return it.dcl_ds().elementSizeOf(knownDataDefinitions, fields)
                        }
                    }
                    it.block() != null -> {
                        val size = it.block().findType(declName, conf)?.size
                        if (size != null) return size
                    }
                    it.directive() != null -> {
                        // API Directives
                        if (it.directive().dir_api() != null) {
                            val apiDirective = it.directive().dir_api()
                            val apiId = apiDirective.toApiId(conf)
                            val size = apiId.loadAndUse { api ->
                                api.let {
                                    it.compilationUnit.dataDefinitions.firstOrNull { def ->
                                        def.name.equals(declName, ignoreCase = true)
                                    }
                                }?.type?.size
                            }
                            if (size != null) return size
                        }
                    }
                }
            }
        }
        if (innerBlock)
            return null
        else
            throw NotFoundAtCompileTimeException(declName)
    }

    override fun evaluateElementSizeOf(rContext: RpgParser.RContext, expression: Expression, conf: ToAstConfiguration, procedureName: String?): Int {
        return when (expression) {
            is DataRefExpr -> {
                try {
                    evaluateElementSizeOf(rContext, expression.variable.name, conf, procedureName)
                } catch (e: NotFoundAtCompileTimeException) {
                    if (delegatedCompileTimeInterpreter != null) {
                        return delegatedCompileTimeInterpreter.evaluateElementSizeOf(rContext, expression, conf, procedureName)
                    } else {
                        expression.error(message = e.message, cause = e)
                    }
                }
            }
            else -> TODO(expression.toString())
        }
    }

    override fun evaluateTypeOf(rContext: RpgParser.RContext, expression: Expression, conf: ToAstConfiguration, procedureName: String?): Type {
        return when (expression) {
            is DataRefExpr -> {
                try {
                    evaluateTypeOf(rContext, expression.variable.name, conf, procedureName)
                } catch (e: NotFoundAtCompileTimeException) {
                    if (delegatedCompileTimeInterpreter != null) {
                        return delegatedCompileTimeInterpreter.evaluateTypeOf(rContext, expression, conf, procedureName)
                    } else {
                        throw RuntimeException(e)
                    }
                }
            }
            else -> TODO(expression.toString())
        }
    }

    open fun evaluateTypeOf(rContext: RpgParser.RContext, declName: String, conf: ToAstConfiguration, procedureName: String?): Type {
        knownDataDefinitions.forEach {
            if (it.name.equals(declName, ignoreCase = true)) {
                return it.type
            }
            val field = it.fields.find { it.name.equals(declName, ignoreCase = true) }
            if (field != null) {
                return field.type
            }
        }

        return findType(rContext.getStatements(procedureName), declName, conf, false)!!
    }

    private fun findType(statements: List<RpgParser.StatementContext>, declName: String, conf: ToAstConfiguration, innerBlock: Boolean = true): Type? {
        statements
            .forEach { it ->
                kotlin.runCatching {
                    when {
                        it.fspec_fixed() != null -> {
                            val type = it.fspec_fixed().runParserRuleContext(conf) { context ->
                                kotlin.runCatching { context.toAst(conf).toDataDefinitions() }.getOrNull()
                            }?.find { dataDefinition -> dataDefinition.name.equals(declName, ignoreCase = true) }?.type
                            if (type != null) return type
                        }
                        it.dcl_ds() != null -> {
                            // First look for the entire DS, if no match search in each of its fields
                            val type = if (it.dcl_ds().name.equals(declName, ignoreCase = true)) {
                                it.dcl_ds().toAst(
                                    conf = conf,
                                    knownDataDefinitions = knownDataDefinitions,
                                    fileDefinitions = fileDefinitions,
                                    parentDataDefinitions = emptyList()
                                )?.type
                            } else {
                                it.dcl_ds().parm_fixed().find {
                                    it.ds_name().text.equals(declName, ignoreCase = true)
                                }?.findType(conf)
                            }
                            if (type != null) return type
                        }
                        it.dspec() != null -> {
                            val name = it.dspec().ds_name()?.text ?: it.dspec().dspecConstant().ds_name()?.text
                            if (declName.equals(name, ignoreCase = true)) {
                                return it.dspec().toAst(conf = conf, knownDataDefinitions = knownDataDefinitions).type
                            }
                        }
                        it.cspec_fixed() != null -> {
                            val type = it.cspec_fixed().findType(
                                declName = declName,
                                lookupIn = statements,
                                conf = conf
                            )
                            if (type != null) return type
                        }
                        it.block() != null -> {
                            val type = it.block().findType(declName, conf)
                            if (type != null) return type
                        }
                        it.directive() != null -> {
                            // API Directives
                            if (it.directive().dir_api() != null) {
                                val apiDirective = it.directive().dir_api()
                                val apiId = apiDirective.toApiId(conf)
                                val type = apiId.loadAndUse { api ->
                                    api.let {
                                        it.compilationUnit.dataDefinitions.firstOrNull { def ->
                                            def.name.equals(declName, ignoreCase = true)
                                        }
                                    }?.type
                                }
                                if (type != null) return type
                            }
                        }
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

    private fun Cspec_fixedContext.findType(
        declName: String,
        lookupIn: List<StatementContext>? = null,
        conf: ToAstConfiguration = ToAstConfiguration()
    ): Type? {
        val targetStatementsPool = lookupIn ?: rContext().getUnwrappedStatements()
        return when (val ast = this.toAst(conf)) {
            is DefineStmt -> {
                if (!declName.equals(ast.newVarName, ignoreCase = true)) return null
                val type = findType(targetStatementsPool, ast.originalName, conf)
                type
            }
            is StatementThatCanDefineData -> {
                val dataDefinition = ast.dataDefinition()
                dataDefinition.firstOrNull { it.name.equals(declName, ignoreCase = true) }?.type
            }
            else -> null
        }
    }

    private fun Parm_fixedContext.findType(conf: ToAstConfiguration): Type? {
        return this.toAst(conf, emptyList()).type
    }

    private fun RpgParser.RContext.getStatements(procedureName: String?): List<StatementContext> {
        val statements: MutableList<StatementContext> = mutableListOf()
        if (procedureName != null) {
            val procedureContext: RpgParser.ProcedureContext? = this.procedure().firstOrNull { it.beginProcedure().psBegin().ps_name().text.equals(procedureName, ignoreCase = true) }
            if (procedureContext != null) {
                statements.addAll(
                    procedureContext.subprocedurestatement().mapNotNull { it.subroutine() }.flatMap { it.statement() } +
                            procedureContext.subprocedurestatement().mapNotNull { it.statement() })
            }
        }
        statements.addAll(this.getUnwrappedStatements())

        return statements.toList()
    }

    /**
     * Retrieve the list of all statements including those contained in subroutines
     */
    private fun RContext.getUnwrappedStatements() = this.statement() + this.subroutine().flatMap { it.statement() }
}
