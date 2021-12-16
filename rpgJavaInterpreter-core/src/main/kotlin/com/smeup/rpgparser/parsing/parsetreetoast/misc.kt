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

package com.smeup.rpgparser.parsing.parsetreetoast

import com.smeup.rpgparser.RpgParser.*
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.parsing.ast.AssignmentOperator.*
import com.smeup.rpgparser.parsing.facade.findAllDescendants
import com.smeup.rpgparser.utils.ComparisonOperator
import com.smeup.rpgparser.utils.asIntOrNull
import com.smeup.rpgparser.utils.isEmptyTrim
import com.strumenta.kolasu.mapping.toPosition
import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Position
import com.strumenta.kolasu.model.ReferenceByName
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token
import java.util.*

data class ToAstConfiguration(
    val considerPosition: Boolean = true,
    val compileTimeInterpreter: CompileTimeInterpreter = CommonCompileTimeInterpreter
)

fun List<Node>.position(): Position? {
    val start = this.asSequence().map { it.position?.start }.filterNotNull().sorted().toList()
    val end = this.asSequence().map { it.position?.end }.filterNotNull().sorted().toList()
    return if (start.isEmpty() || end.isEmpty()) {
        null
    } else {
        Position(start.first(), end.last())
    }
}

internal interface DataDefinitionProvider {
    fun isReady(): Boolean
    fun toDataDefinition(): DataDefinition
}
private data class DataDefinitionHolder(val dataDefinition: DataDefinition) : DataDefinitionProvider {
    override fun isReady() = true
    override fun toDataDefinition() = dataDefinition
}
private data class DataDefinitionCalculator(val calculator: () -> DataDefinition) : DataDefinitionProvider {
    override fun isReady() = false
    override fun toDataDefinition() = calculator()
}

private fun RContext.getDataDefinitions(conf: ToAstConfiguration = ToAstConfiguration()): List<DataDefinition> {
    // We need to calculate first all the data definitions which do not contain the LIKE DS directives
    // then we calculate the ones with the LIKE DS clause, as they could have references to DS declared
    // after them
    val dataDefinitionProviders: MutableList<DataDefinitionProvider> = LinkedList()
    val knownDataDefinitions = mutableMapOf<String, DataDefinition>()

    // First pass ignore exception and all the know definitions
    dataDefinitionProviders.addAll(this.statement()
        .mapNotNull {
            when {
                it.dcl_ds() != null -> {
                    try {
                        it.dcl_ds()
                            .toAst(conf)
                            .updateKnownDataDefinitionsAndGetHolder(knownDataDefinitions)
                    } catch (e: Exception) {
                        null
                    }
                }
                else -> null
            }
        })

    // Second pass, everything, I mean everything
    dataDefinitionProviders.addAll(this.statement()
        .mapNotNull {
            kotlin.runCatching {
                when {
                    it.dspec() != null -> {
                        it.dspec()
                            .toAst(conf, knownDataDefinitions.values.toList())
                            .updateKnownDataDefinitionsAndGetHolder(knownDataDefinitions)
                    }
                    it.dcl_c() != null -> {
                        it.dcl_c()
                            .toAst(conf, knownDataDefinitions.values.toList())
                            .updateKnownDataDefinitionsAndGetHolder(knownDataDefinitions)
                    }
                    it.dcl_ds() != null && it.dcl_ds().useLikeDs(conf) -> {
                        DataDefinitionCalculator(it.dcl_ds().toAstWithLikeDs(conf, dataDefinitionProviders))
                    }
                    else -> null
                }
            }.onFailure { error ->
                it.error("Error on dataDefinitionProviders creation", error, conf)
            }.getOrThrow()
        })
    return dataDefinitionProviders.map { it.toDataDefinition() }
}

private fun DataDefinition.updateKnownDataDefinitionsAndGetHolder(
    knownDataDefinitions: MutableMap<String, DataDefinition>
): DataDefinitionHolder {
        knownDataDefinitions.addIfNotPresent(this)
    return DataDefinitionHolder(this)
}

private fun MutableMap<String, DataDefinition>.addIfNotPresent(dataDefinition: DataDefinition) {
    if (put(dataDefinition.name, dataDefinition) != null)
        dataDefinition.error("${dataDefinition.name} has been defined twice")
}

fun RContext.toAst(conf: ToAstConfiguration = ToAstConfiguration(), source: String? = null): CompilationUnit {
    val fileDefinitions = this.statement()
            .mapNotNull {
                when {
                    it.fspec_fixed() != null -> it.fspec_fixed().toAst(conf)
                    else -> null
                }
            }
    val dataDefinitions = getDataDefinitions(conf)

    val mainStmts = this.statement().mapNotNull {
        when {
            it.cspec_fixed() != null -> it.cspec_fixed().runParserRuleContext(conf) { context ->
                context.toAst(conf)
            }
            it.block() != null -> it.block().toAst(conf)
            it.free() != null -> it.free().toAst(conf)
            else -> null
        }
    }

    val subroutines = this.subroutine().map { it.toAst(conf) }
    val compileTimeArrays = this.endSourceBlock()?.endSource()?.map { it.toAst(conf) } ?: emptyList()
    val directives = this.findAllDescendants(Hspec_fixedContext::class).map { it.toAst(conf) }
    // if we have no procedures, the property procedure must be null because we decided it must be optional
    var procedures = this.procedure().map { it.toAst(conf) }.let {
        if (it.isEmpty()) null
        else it
    }

    // If none of 'rpg procedures', add only any 'fake procedures'.
    // If any 'rpg procedures' exists, add any 'fake procedures' too.
    var fakeProcedures = getFakeProcedures(rContext = this,
        conf = conf,
        dataDefinitions = dataDefinitions,
        mainStmts = mainStmts,
        procedures = procedures
    )

    if (null == procedures) {
        if (!fakeProcedures.isEmpty()) {
            procedures = fakeProcedures
        }
    } else {
        (procedures as ArrayList).addAll(fakeProcedures)
    }

    return CompilationUnit(
        fileDefinitions,
        dataDefinitions,
        MainBody(mainStmts, if (conf.considerPosition) mainStmts.position() else null),
        subroutines,
        compileTimeArrays,
        directives,
        position = this.toPosition(conf.considerPosition),
        apiDescriptors = this.statement().toApiDescriptors(conf),
        procedures = procedures,
        source = source
    ).let { compilationUnit ->
        // for each procedureUnit set compilationUnit as parent
        // in order to resolve global data references
        procedures?.onEach { procedureUnit ->
            procedureUnit.parent = compilationUnit
        }
        compilationUnit
    }.postProcess()
}

private fun getFakeProcedures(
    rContext: RContext,
    conf: ToAstConfiguration,
    dataDefinitions: List<DataDefinition>,
    mainStmts: List<Statement>,
    procedures: List<CompilationUnit>?
): List<CompilationUnit> {
    // If any of the 'non rpgle standard' procedures (only prototype definition 'PR' and missing
    // the procedure implementation) should be the 'doped java procedure' case.
    // Needed to create a 'fake procedure' to be able to get 'ProcedureParameterDataDefinition'.
    // 1. get names of all prototype definitions
    // 2. match names with related procedure implementation to remove any 'not real fake prototype'
    // 3. any missing match, will generate a fake procedure
    val fakePrototypeNames = mutableMapOf<String, ArrayList<DataDefinition>>()
    rContext.children.forEach {
        if (it is Dcl_prContext) {
            var fakePrototypeName: String = ""
            var fakePrototypeDataDefinitions: ArrayList<DataDefinition> = arrayListOf<DataDefinition>()
            if (rContext.children[0] is Dcl_prContext) {
                (rContext.children[0] as Dcl_prContext).children.forEachIndexed { index, element ->
                    var fakePrototypeDataDefinition: DataDefinition
                    if (index == 0) {
                        fakePrototypeName =
                            (element as PrBeginContext).ds_name().NAME().text
                    } else {
                        var parmFixed = ((rContext.children[0] as Dcl_prContext).children[index] as Parm_fixedContext)
                        var paramPassedBy = ParamPassedBy.Reference
                        var paramOptions = mutableListOf<ParamOption>()
                        parmFixed
                            .keyword()
                            .forEach { it ->
                                if (it.keyword_const() != null) {
                                    paramPassedBy = ParamPassedBy.Const
                                } else if (it.keyword_value() != null) {
                                    paramPassedBy = ParamPassedBy.Value
                                }
                                if (it.keyword_options() != null) {
                                    it.keyword_options().identifier().forEach {
                                        val keyword = it.free_identifier().idOrKeyword().ID().toString()
                                        val paramOption = ParamOption.getByKeyword(keyword)
                                        if (null != paramOption) {
                                            (paramOptions as ArrayList).add(paramOption)
                                        }
                                    }
                                }
                            }
                        fakePrototypeDataDefinition =
                            parmFixed.toAst(conf, dataDefinitions.toList())
                        fakePrototypeDataDefinition.paramPassedBy = paramPassedBy
                        fakePrototypeDataDefinition.paramOptions = paramOptions
                        fakePrototypeDataDefinitions.add(fakePrototypeDataDefinition)
                    }
                }
                // Add only 'real fake prototype', if any RPG procedure exists yet
                // the 'fake prototype' with same name mustn't be added.
                if (null == procedures || (null != procedures && !procedures.contains(fakePrototypeName))) {
                    fakePrototypeNames.put(fakePrototypeName, fakePrototypeDataDefinitions)
                }
            }
        }
    }

    // Create 'fake procedures' related only to 'fake prototype names'
    return fakePrototypeNames.map {
        CompilationUnit(
            fileDefinitions = emptyList(),
            dataDefinitions = emptyList(),
            MainBody(mainStmts, if (conf.considerPosition) mainStmts.position() else null),
            subroutines = emptyList(),
            compileTimeArrays = emptyList(),
            directives = emptyList(),
            position = null,
            apiDescriptors = null,
            procedureName = it.key,
            proceduresParamsDataDefinitions = it.value
        )
    }
}

private fun Dcl_dsContext.useLikeDs(conf: ToAstConfiguration): Boolean {
    val keywordLikeDs = this.keyword_likeds()
    if (keywordLikeDs != null) {
        todo(conf = conf)
    }
    return (this.keyword().any { it.keyword_likeds() != null })
}

internal fun EndSourceContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): CompileTimeArray {

        fun cName(s: String) =
            if (s.contains("**CTDATA")) {
                s.substringAfter("**CTDATA ").replace("\\s".toRegex(), "")
            } else {
                s.replace(s, "")
            }

    return CompileTimeArray(
        cName(this.endSourceHead().text), // TODO: change grammar to get **CTDATA name
        this.endSourceLine().map { it.endSourceLineText().text },
        toPosition(conf.considerPosition)
    )
}

internal fun SubroutineContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Subroutine {
    return Subroutine(
        this.begsr().csBEGSR().factor1.text,
        this.statement().map { it.toAst(conf) },
        this.endsr().csENDSR().factor1.text,
        toPosition(conf.considerPosition)
    )
}

internal fun ProcedureContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): CompilationUnit {

    val procedureName = this.beginProcedure().psBegin().ps_name().text
    MainExecutionContext.getParsingProgramStack().peek().parsingFunctionNameStack.push(procedureName)

    // TODO FileDefinitions

    // DataDefinitions
    val dataDefinitions = getDataDefinitions(conf)

    // Procedure Parameters DataDefinitions
    val proceduresParamsDataDefinitions = getProceduresParamsDataDefinitions(dataDefinitions)

    // MainBody (list of Statements)
    val mainStmts = this.subprocedurestatement().mapNotNull {
        if (null != it.statement()) {
            when {
                it.statement().cspec_fixed() != null -> it.statement().cspec_fixed().toAst(conf)
                it.statement().block() != null -> it.statement().block().toAst(conf)
                it.statement().free() != null -> it.statement().free().toAst(conf)
                else -> null
            }
        } else {
            null
        }
    }

    val subroutines = this.subprocedurestatement().mapNotNull {
        when {
            it.subroutine() != null -> it.subroutine().toAst(conf)
            else -> null
        }
    }

    // TODO CompileTimeArrays

    // TODO Directives

    // TODO Procedures

    return CompilationUnit(
        fileDefinitions = mutableListOf(),
        dataDefinitions,
        main = MainBody(mainStmts, null),
        subroutines,
        compileTimeArrays = mutableListOf(),
        directives = mutableListOf(),
        position = toPosition(conf.considerPosition),
        apiDescriptors = null,
        procedures = null,
        procedureName = procedureName,
        proceduresParamsDataDefinitions
    ).apply { MainExecutionContext.getParsingProgramStack().peek().parsingFunctionNameStack.pop() }
}

fun ProcedureContext.getProceduresParamsDataDefinitions(dataDefinitions: List<DataDefinition>): List<DataDefinition> {
    val proceduresParamsDataDefinitions: MutableList<DataDefinition> = LinkedList()

    // Get parmDefinition matching from internal (Procedure scope) dataDefinitions and PI parm definition.
    dataDefinitions.forEach { dataDefinition ->
        this.dcl_pi().pi_parm_fixed()
            .asSequence()
            .filter {
                it.parm_fixed().ds_name().NAME().text == dataDefinition.name
            }
            .forEach {
            it.parm_fixed()
                .keyword()
                .forEach { it ->
                        if (it.keyword_const() != null) {
                            dataDefinition.const = true
                            dataDefinition.paramPassedBy = ParamPassedBy.Const
                        } else if (it.keyword_value() != null) {
                            dataDefinition.paramPassedBy = ParamPassedBy.Value
                        }
                        if (it.keyword_options() != null) {
                            it.keyword_options().identifier().forEach {
                                val keyword = it.free_identifier().idOrKeyword().ID().toString()
                                val paramOption = ParamOption.getByKeyword(keyword)
                                (dataDefinition.paramOptions as ArrayList).add(paramOption)
                            }
                        }
                    }
                    proceduresParamsDataDefinitions.add(dataDefinition)
                }
        }

    return proceduresParamsDataDefinitions
}

private fun ProcedureContext.getDataDefinitions(conf: ToAstConfiguration = ToAstConfiguration()): List<DataDefinition> {
    // We need to calculate first all the data definitions which do not contain the LIKE DS directives
    // then we calculate the ones with the LIKE DS clause, as they could have references to DS declared
    // after them
    val dataDefinitionProviders: MutableList<DataDefinitionProvider> = LinkedList()
    val knownDataDefinitions = mutableMapOf<String, DataDefinition>()

    // First pass ignore exception and all the know definitions
    dataDefinitionProviders.addAll(this.subprocedurestatement()
        .mapNotNull {
            if (null != it.statement()) {
                when {
                    it.statement().dcl_ds() != null -> {
                        try {
                            it.statement().dcl_ds()
                                .toAst(conf)
                                .updateKnownDataDefinitionsAndGetHolder(knownDataDefinitions)
                        } catch (e: Exception) {
                            null
                        }
                    }
                    else -> null
                }
            } else {
                null
            }
        })

    // Second pass, everything, I mean everything
    dataDefinitionProviders.addAll(this.subprocedurestatement()
        .mapNotNull {
            kotlin.runCatching {
                if (null != it.statement()) {
                    when {
                        it.statement().dspec() != null -> {
                            it.statement().dspec()
                                .toAst(conf, knownDataDefinitions.values.toList())
                                .updateKnownDataDefinitionsAndGetHolder(knownDataDefinitions)
                        }
                        it.statement().dcl_c() != null -> {
                            it.statement().dcl_c()
                                .toAst(conf, knownDataDefinitions.values.toList())
                                .updateKnownDataDefinitionsAndGetHolder(knownDataDefinitions)
                        }
                        it.statement().dcl_ds() != null && it.statement().dcl_ds().useLikeDs(conf) -> {
                            DataDefinitionCalculator(
                                it.statement().dcl_ds().toAstWithLikeDs(conf, dataDefinitionProviders)
                            )
                        }
                        else -> null
                    }
                } else {
                    null
                }
            }.onFailure { error ->
                it.error("Error on dataDefinitionProviders creation", error, conf)
            }.getOrThrow()
        })

    // PROCEDURE PARAMETERS
    // Second pass, everything, I mean everything
    dataDefinitionProviders.addAll(this.dcl_pi().pi_parm_fixed()
        .mapNotNull {
            kotlin.runCatching {
                when {
                    it.parm_fixed() != null -> {
                        it.parm_fixed()
                            .toAst(conf, knownDataDefinitions.values.toList())
                            .updateKnownDataDefinitionsAndGetHolder(knownDataDefinitions)
                    }
                    else -> null
                }
            }.onFailure { error ->
                it.error("Error on dataDefinitionProviders creation", error, conf)
            }.getOrThrow()
        })
    return dataDefinitionProviders.map { it.toDataDefinition() }
}

internal fun FunctionContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Expression {
    return when (this.functionName().text.toUpperCase()) {
        "NOT" -> {
            if (this.args().expression().size != 1) {
                throw IllegalStateException("Not should have just one parameter")
            }
            NotExpr(this.args().expression()[0].toAst(conf), toPosition(conf.considerPosition))
        }
        else -> FunctionCall(
            ReferenceByName(this.functionName().text), this.args().expression().map { it.toAst(conf) }, toPosition(
                conf.considerPosition
            )
        )
    }
}

internal fun String.isInt() = this.toIntOrNull() != null

internal fun ParserRuleContext.rContext(): RContext {
    return if (this.parent == null) {
        this as RContext
    } else {
        (this.parent as ParserRuleContext).rContext()
    }
}

internal fun FactorContentContext.toAst(conf: ToAstConfiguration): Expression {
    val position = this.toPosition(conf.considerPosition)

    val l = this.literal()
    if (l != null) {
        return l.toAst(conf)
    }
    val text = this.CS_FactorContent().text
    val regexp = Regex("([+\\-])?(\\d|,|\\.)+([+\\-])?")
    return if (text.matches(regexp)) {
        literalToNumber(text, position)
    } else if (text.startsWith("\'")) {
        StringLiteral(text, position)
    } else {
        annidatedReferenceExpression(text, position)
    }
}

fun literalToNumber(
    text: String,
    position: Position?
): Expression {
    // fix minus at right
    val value = if (text.endsWith('-')) {
        "-" + text.replaceFirst("-", "")
    } else {
        text
    }
    return when {
        // When assigning a value to a numeric field we could either use
        // a comma or a dot as decimal separators
        value.contains('.') -> {
            value.toRealLiteral(position, Locale.US)
        }
        value.contains(',') -> {
            value.toRealLiteral(position, Locale.ITALIAN)
        }
        else -> {
            IntLiteral(value.toLong(), position)
        }
    }
}

internal fun SymbolicConstantsContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Expression {
    val position = toPosition(conf.considerPosition)
    return when {
        this.SPLAT_HIVAL() != null -> HiValExpr(position)
        this.SPLAT_LOVAL() != null -> LowValExpr(position)
        this.SPLAT_BLANKS() != null -> BlanksRefExpr(position)
        this.SPLAT_ZEROS() != null -> ZeroExpr(position)
        this.SPLAT_ALL() != null -> {
            val content: LiteralContext = this.parent.getChild(1) as LiteralContext
            AllExpr(content.toAst(conf), position)
        }
        else -> todo(conf = conf)
    }
}

internal fun Cspec_fixedContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Statement {
    return when {
        this.cspec_fixed_standard() != null ->
            this.cspec_fixed_standard().toAst(conf)
                .also {
                    it.indicatorCondition = this.toIndicatorCondition(conf)
                    if (it.indicatorCondition != null) {
                        val continuedIndicators = this.cspec_continuedIndicators()
                        // loop over continued indicators (WARNING: continuedIndicators not contains inline indicator)
                        for (i in 0 until continuedIndicators.size) {
                            val indicator = continuedIndicators[i].indicators.children[0].toString().toIndicatorKey()
                            var onOff = false
                            if (!continuedIndicators[i].indicatorsOff.children[0].toString().isEmptyTrim()) {
                                onOff = true
                            }
                            val controlLevel = when (continuedIndicators[i].start.type) {
                                AndIndicator -> "AND"
                                OrIndicator -> "OR"
                                else -> ""
                            }
                            val continuedIndicator = ContinuedIndicator(indicator, onOff, controlLevel)
                            it.continuedIndicators.put(indicator, continuedIndicator)
                        }

                        // Add indicatorCondition (inline indicator) also
                        var controlLevel = (this.children[continuedIndicators.size + 1] as Cs_controlLevelContext).children[0].toString()
                        if (controlLevel == "AN") {
                            controlLevel = "AND"
                        }
                        var onOff = false
                        if (!(this.children[continuedIndicators.size + 2] as OnOffIndicatorsFlagContext).children[0].toString().isEmptyTrim()) {
                            onOff = true
                        }
                        val indicator = (this.children[continuedIndicators.size + 3] as Cs_indicatorsContext).children[0].toString().toIndicatorKey()
                        val continuedIndicator = ContinuedIndicator(indicator, onOff, controlLevel)
                        it.continuedIndicators.put(indicator, continuedIndicator)
                    }
                }
        this.cspec_fixed_x2() != null ->
            this.cspec_fixed_x2().toAst()
        else -> todo(conf = conf)
    }
}

internal fun Cspec_fixedContext.toIndicatorCondition(conf: ToAstConfiguration): IndicatorCondition? =
    if (this.indicators.text.isEmptyTrim()) {
        null
    } else {
        try {
            IndicatorCondition(this.indicators.text.toIndicatorKey(), " " != this.indicatorsOff.text)
        } catch (e: NumberFormatException) {
            error("Non numeric indicators", e, conf)
        }
    }

internal fun Cspec_fixed_standardContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Statement {
    return when {
        this.csEXSR() != null -> this.csEXSR().toAst(conf)
        this.csEVAL() != null -> this.csEVAL().toAst(conf)
        this.csCALL() != null -> this.csCALL().toAst(conf)
        this.csSETON() != null -> this.csSETON().toAst(conf)
        this.csSETOFF() != null -> this.csSETOFF().toAst(conf)
        this.csPLIST() != null -> this.csPLIST().toAst(conf)
        this.csCLEAR() != null -> this.csCLEAR().toAst(conf)
        this.csLEAVE() != null -> LeaveStmt(toPosition(conf.considerPosition))
        this.csLEAVESR() != null -> LeaveSrStmt(toPosition(conf.considerPosition))
        this.csITER() != null -> IterStmt(toPosition(conf.considerPosition))
        this.csOTHER() != null -> OtherStmt(toPosition(conf.considerPosition))
        this.csDSPLY() != null -> this.csDSPLY().toAst(conf)
        this.csMOVE() != null -> this.csMOVE().toAst(conf)
        this.csMOVEA() != null -> this.csMOVEA().toAst(conf)
        this.csMOVEL() != null -> this.csMOVEL().toAst(conf)
        this.csTIME() != null -> this.csTIME().toAst(conf)
        this.csSUBDUR() != null -> this.csSUBDUR().toAst(conf)
        this.csZ_ADD() != null -> this.csZ_ADD().toAst(conf)
        this.csADD() != null -> this.csADD().toAst(conf)
        this.csZ_SUB() != null -> this.csZ_SUB().toAst(conf)
        this.csSUB() != null -> this.csSUB().toAst(conf)
        this.csCHAIN() != null -> this.csCHAIN().toAst(conf)
        this.csCHECK() != null -> this.csCHECK().toAst(conf)
        this.csKLIST() != null -> this.csKLIST().toAst(conf)
        this.csSETLL() != null -> this.csSETLL().toAst(conf)
        this.csSETGT() != null -> this.csSETGT().toAst(conf)
        this.csREAD() != null -> this.csREAD().toAst(conf)
        this.csREADP() != null -> this.csREADP().toAst(conf)
        this.csREADE() != null -> this.csREADE().toAst(conf)
        this.csREADPE() != null -> this.csREADPE().toAst(conf)
        this.csWRITE() != null -> this.csWRITE().toAst(conf)
        this.csUPDATE() != null -> this.csUPDATE().toAst(conf)
        this.csDELETE() != null -> this.csDELETE().toAst(conf)
        this.csCOMP() != null -> this.csCOMP().toAst(conf)
        this.csMULT() != null -> this.csMULT().toAst(conf)
        this.csDIV() != null -> this.csDIV().toAst(conf)
        this.csRETURN() != null -> this.csRETURN().toAst(conf)
        this.csTAG() != null -> this.csTAG().toAst(conf)
        this.csGOTO() != null -> this.csGOTO().toAst(conf)
        this.csSORTA() != null -> this.csSORTA().toAst(conf)
        this.csDEFINE() != null -> this.csDEFINE().toAst(conf)
        this.csCAT() != null -> this.csCAT().toAst(conf)
        this.csLOOKUP() != null -> this.csLOOKUP().toAst(conf)
        this.csCAB() != null -> this.csCAB().toAst(conf)
        this.csCABLE() != null -> this.csCABLE().toAst(conf)
        this.csCABLT() != null -> this.csCABLT().toAst(conf)
        this.csCABEQ() != null -> this.csCABEQ().toAst(conf)
        this.csCABGE() != null -> this.csCABGE().toAst(conf)
        this.csCABGT() != null -> this.csCABGT().toAst(conf)
        this.csXFOOT() != null -> this.csXFOOT().toAst(conf)
        this.csSCAN() != null -> this.csSCAN().toAst(conf)
        else -> todo(conf = conf)
    }
}

private fun annidatedReferenceExpression(
    text: String,
    position: Position?
): AssignableExpression {
    // FIXME: This is very, very, very ugly. It should be fixed by parsing this properly
    //        in the grammar
    if (text.toUpperCase() == "*IN") {
        return GlobalIndicatorExpr(position)
    }
    if (text.toUpperCase().startsWith("*IN(") && text.endsWith(")")) {
        val index = text.toUpperCase().removePrefix("*IN(").removeSuffix(")").toInt()
        return IndicatorExpr(index, position)
    }
    if (text.toUpperCase().startsWith("*IN")) {
        val index = text.toUpperCase().removePrefix("*IN").toInt()
        return IndicatorExpr(index, position)
    }
    var expr: Expression = text.indexOf("(").let {
        val varName = if (it == -1) text else text.substring(0, it)
        DataRefExpr(ReferenceByName(varName), position)
    }
    if (text.contains("(")) {
        // TODO support annidated parenthesis, if necessary
        if (text.substring(text.indexOf("(") + 1).contains("(")) {
            TODO("Support annidated parenthesis")
        }
        val indexText = text.substring(text.indexOf("(") + 1, text.lastIndexOf(")"))
        val indexValue = indexText.toLongOrNull()
        val indexExpression =
            if (indexValue == null) {
                DataRefExpr(ReferenceByName(indexText), computeNewPosition(position, text))
            } else {
                IntLiteral(indexValue, computeNewPosition(position, text))
            }
        expr = ArrayAccessExpr(expr, indexExpression)
    }
    return expr as AssignableExpression
}

private fun computeNewPosition(position: Position?, text: String) =
    if (position == null) {
        null
    } else {
        Position(
            position.start.plus(text.substring(0, text.indexOf("("))),
            position.start.plus(text.substring(0, text.lastIndexOf(")")))
        )
    }

fun ParserRuleContext.factor1Context() = ((this.parent as Cspec_fixed_standardContext).parent as Cspec_fixedContext).factor()

internal fun CsKLISTContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): KListStmt {
    val position = toPosition(conf.considerPosition)
    val factor1 = this.factor1Context()?.content?.text ?: throw UnsupportedOperationException("Line ${position?.line()}: KLIST operation requires factor 1: ${this.text}")
    val fields = this.csKFLD().map {
        it.cspec_fixed_standard_parts().result.text
    }
    return KListStmt(factor1, fields, position)
}

internal fun CsDSPLYContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): DisplayStmt {
    val left = leftExpr(conf)
    val right = if (this.cspec_fixed_standard_parts()?.result?.text?.isNotBlank() ?: false) {
        this.cspec_fixed_standard_parts().result.toAst(conf)
    } else {
        null
    }
    require(left != null || right != null)
    return DisplayStmt(left, right, toPosition(conf.considerPosition))
}

internal fun ResultTypeContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): AssignableExpression {
    // this should have been parsed differently because here we have to figure out
    // what kind of expression is this
    val position = toPosition(conf.considerPosition)

    if (text.contains('.')) {
        return handleParsingOfTargets(text, position)
    } else {
        return annidatedReferenceExpression(text, position)
    }
}

private fun handleParsingOfTargets(code: String, position: Position?): AssignableExpression {
    require(!code.contains("(") && !code.contains(")"))
    val parts = code.split(".")
    require(parts.isNotEmpty())
    return if (parts.size == 1) {
        DataRefExpr(ReferenceByName(parts[0]), position)
    } else {
        val containerCode = parts.dropLast(1).joinToString(separator = ".")
        QualifiedAccessExpr(
            container = handleParsingOfTargets(containerCode, position),
            field = ReferenceByName(parts.last()),
            position = position
        )
    }
}

internal fun CsPLISTContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): PlistStmt {
    val isEntry = this.factor1Context().symbolicConstants().SPLAT_ENTRY() != null
    return PlistStmt(
        this.csPARM().map { it.toAst(conf) },
        isEntry,
        toPosition(conf.considerPosition)
    )
}

internal fun CsPARMContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): PlistParam {
    val paramName = this.cspec_fixed_standard_parts().result.text
    val position = toPosition(conf.considerPosition)
    return PlistParam(
        ReferenceByName(paramName), this.cspec_fixed_standard_parts().toDataDefinition(
            paramName,
            position,
            conf
        ), position
    )
}

internal fun CsTIMEContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): TimeStmt {
    val name = this.cspec_fixed_standard_parts().result.text
    val position = toPosition(conf.considerPosition)
    return TimeStmt(annidatedReferenceExpression(name, toPosition(conf.considerPosition)), position)
}

fun Cspec_fixed_standard_partsContext.factor2Expression(conf: ToAstConfiguration): Expression? {
    factor2?.symbolicConstants()?.let {
        return it.toAst()
    }
    return factor2?.content?.toAst(conf)
}

fun Cspec_fixed_standard_partsContext.resultExpression(conf: ToAstConfiguration): Expression {
    if (result?.symbolicConstants() != null) {
        return result.symbolicConstants().toAst()
    }
    return result.toAst(conf)
}

internal fun Cspec_fixed_standard_partsContext.toDataDefinition(
    name: String,
    position: Position?,
    conf: ToAstConfiguration
): InStatementDataDefinition? {
    val len = this.len.asInt()
    if (len == null) {
        return null
    }
    val decimals = this.decimalPositions.asInt()
    val initialValue = this.factor2Expression(conf)
    return InStatementDataDefinition(name, dataType(len, decimals), position, initializationValue = initialValue)
}

private fun dataType(len: Int, decimals: Int?): Type =
    if (decimals == null) {
        StringType(len, false)
    } else {
        NumberType(len - decimals, decimals)
    }

internal fun Token.asInt(): Int? {
    val tokenString = this.text.trim()
    return if (tokenString.isNotBlank()) {
        tokenString.toIntOrNull()
    } else {
        null
    }
}

internal fun CsSETONContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): SetStmt {
    try {
        return SetStmt(
            SetStmt.ValueSet.ON, indicators(this.cspec_fixed_standard_parts(), conf.considerPosition), toPosition(
                conf.considerPosition
            )
        )
    } catch (e: Exception) {
        throw RuntimeException("Problem translating ${this.text} at ${this.toPosition(true)}", e)
    }
}

internal fun CsSETOFFContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): SetStmt {
    try {
        return SetStmt(
            SetStmt.ValueSet.OFF, indicators(this.cspec_fixed_standard_parts(), conf.considerPosition), toPosition(
                conf.considerPosition
            )
        )
    } catch (e: Exception) {
        throw RuntimeException("Problem translating ${this.text} at ${this.toPosition(true)}", e)
    }
}

internal fun indicators(cspecs: Cspec_fixed_standard_partsContext, considerPosition: Boolean = true): List<AssignableExpression> {
    return listOf(cspecs.hi, cspecs.lo, cspecs.eq)
            .asSequence()
            .map { it.text }
            .filter { !it.isNullOrBlank() }
            .map(String::toUpperCase)
            .map {
                IndicatorExpr(it.toIndicatorKey(), cspecs.toPosition(considerPosition))
            }
            .toList()
}

internal fun CsEXSRContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): ExecuteSubroutine {
    val subroutineName = this.cspec_fixed_standard_parts().factor2.text
    require(this.cspec_fixed_standard_parts().decimalPositions.text.isBlank())
    require(this.cspec_fixed_standard_parts().eq.text.isBlank())
    require(this.cspec_fixed_standard_parts().hi.text.isBlank())
    require(this.cspec_fixed_standard_parts().len.text.isBlank())
    require(this.cspec_fixed_standard_parts().lo.text.isBlank())
    require(this.cspec_fixed_standard_parts().result.text.isBlank())
    return ExecuteSubroutine(ReferenceByName(subroutineName), toPosition(conf.considerPosition))
}

internal fun CsRETURNContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): ReturnStmt {
    return ReturnStmt(this.fixedexpression?.expression()?.toAst(conf), toPosition(conf.considerPosition))
}

internal fun CsEVALContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): EvalStmt {
    val extenders = this.operationExtender?.extender?.text?.toUpperCase()?.toCharArray() ?: CharArray(0)
    val flags = EvalFlags(
        halfAdjust = 'H' in extenders,
        maximumNumberOfDigitsRule = 'M' in extenders,
        resultDecimalPositionRule = 'R' in extenders
    )
    return EvalStmt(
        this.target().toAst(conf),
        this.fixedexpression.expression().toAst(conf),
        operator = this.operator.toAssignmentOperator(),
        flags = flags,
        position = toPosition(conf.considerPosition)
    )
}

internal fun CsSUBDURContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): SubDurStmt {
    val position = toPosition(conf.considerPosition)
    val left = leftExpr(conf)
    val factor2 = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("SUBDUR operation requires factor 2: ${this.text} - ${position.atLine()}")
    // TODO handle duration code after the :
    val target = this.cspec_fixed_standard_parts().result.text.split(":")
    val durationCode = if (target.size > 1) target[1].toDuration() else DurationInMSecs
    return SubDurStmt(left, DataRefExpr(ReferenceByName(target[0]), position), factor2, durationCode, position)
}

private fun String.toDuration(): DurationCode =
    when (toUpperCase()) {
        "*D", "*DAYS" -> DurationInDays
        "*MS", "*MSECONDS" -> DurationInMSecs
        "*S", "*SECONDS" -> DurationInSecs
        "*MN", "*MINUTES" -> DurationInMinutes
        "*H", "*HOURS" -> DurationInHours
        "*M", "*MONTHS" -> DurationInMonths
        "*Y", "*YEARS" -> DurationInYears
        else -> TODO("Implement conversion to DurationCode for $this")
    }

internal fun CsCHAINContext.toAst(conf: ToAstConfiguration): Statement {
    val position = toPosition(conf.considerPosition)
    val factor1 = this.factor1Context()?.content?.toAst(conf) ?: throw UnsupportedOperationException("CHAIN operation requires factor 1: ${this.text} - ${position.atLine()}")
    val factor2 = this.cspec_fixed_standard_parts().factor2.text ?: throw UnsupportedOperationException("CHAIN operation requires factor 2: ${this.text} - ${position.atLine()}")
    return ChainStmt(factor1, factor2, position)
}

internal fun CsREADContext.toAst(conf: ToAstConfiguration): Statement {
    val position = toPosition(conf.considerPosition)
    // TODO implement DS in result field
    val factor2 = this.cspec_fixed_standard_parts().factor2.text ?: throw UnsupportedOperationException("READ operation requires factor 2: ${this.text} - ${position.atLine()}")
    return ReadStmt(factor2, position)
}

internal fun CsREADPContext.toAst(conf: ToAstConfiguration): Statement {
    val position = toPosition(conf.considerPosition)
    // TODO implement DS in result field
    val factor2 = this.cspec_fixed_standard_parts().factor2.text ?: throw UnsupportedOperationException("READP operation requires factor 2: ${this.text} - ${position.atLine()}")
    return ReadPreviousStmt(factor2, position)
}

internal fun CsREADEContext.toAst(conf: ToAstConfiguration): Statement {
    val position = toPosition(conf.considerPosition)
    // TODO implement DS in result field
    val factor1 = this.factor1Context()?.content?.toAst(conf)
    val factor2 = this.cspec_fixed_standard_parts().factor2.text ?: throw UnsupportedOperationException("READE operation requires factor 2: ${this.text} - ${position.atLine()}")
    return ReadEqualStmt(factor1, factor2, position)
}

internal fun CsREADPEContext.toAst(conf: ToAstConfiguration): Statement {
    val position = toPosition(conf.considerPosition)
    // TODO implement DS in result field
    val factor1 = this.factor1Context()?.content?.toAst(conf)
    val factor2 = this.cspec_fixed_standard_parts().factor2.text ?: throw UnsupportedOperationException("READPE operation requires factor 2: ${this.text} - ${position.atLine()}")
    return ReadPreviousEqualStmt(factor1, factor2, position)
}

internal fun CsSETLLContext.toAst(conf: ToAstConfiguration): Statement {
    val position = toPosition(conf.considerPosition)
    // TODO implement indicators handling
    val factor1 = this.factor1Context()?.content?.toAst(conf) ?: this.factor1Context()?.constant?.toAst(conf)
    require(factor1 != null) {
        "SETLL operation requires factor 1: ${this.text} - ${position.atLine()}"
    }
    val factor2 = this.cspec_fixed_standard_parts().factor2.text ?: throw UnsupportedOperationException("SETLL operation requires factor 2: ${this.text} - ${position.atLine()}")
    return SetllStmt(factor1, factor2, position)
}

internal fun CsSETGTContext.toAst(conf: ToAstConfiguration): Statement {
    val position = toPosition(conf.considerPosition)
    // TODO implement indicators handling
    val factor1 = this.factor1Context()?.content?.toAst(conf) ?: this.factor1Context()?.constant?.toAst(conf)
    require(factor1 != null) {
        "SETGT operation requires factor 1: ${this.text} - ${position.atLine()}"
    }
    val factor2 = this.cspec_fixed_standard_parts().factor2.text ?: throw UnsupportedOperationException("SETGT operation requires factor 2: ${this.text} - ${position.atLine()}")
    return SetgtStmt(factor1, factor2, position)
}

internal fun CsWRITEContext.toAst(conf: ToAstConfiguration): Statement {
    val position = toPosition(conf.considerPosition)
    val factor2 = this.cspec_fixed_standard_parts().factor2.text ?: throw UnsupportedOperationException("WRITE operation requires factor 2: ${this.text} - ${position.atLine()}")
    return WriteStmt(factor2, position)
}

internal fun CsUPDATEContext.toAst(conf: ToAstConfiguration): Statement {
    val position = toPosition(conf.considerPosition)
    val factor2 = this.cspec_fixed_standard_parts().factor2.text ?: throw UnsupportedOperationException("WRITE operation requires factor 2: ${this.text} - ${position.atLine()}")
    return UpdateStmt(factor2, position)
}

internal fun CsDELETEContext.toAst(conf: ToAstConfiguration): Statement {
    val position = toPosition(conf.considerPosition)
    val factor2 = this.cspec_fixed_standard_parts().factor2.text ?: throw UnsupportedOperationException("DELETE operation requires factor 2: ${this.text} - ${position.atLine()}")
    return DeleteStmt(factor2, position)
}

internal fun CsSCANContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): ScanStmt {
    val position = toPosition(conf.considerPosition)
    val (compareExpression, compareLength) = this.factor1Context().toIndexedExpression(conf)
    val (baseExpression, startPosition) = this.cspec_fixed_standard_parts().factor2.toIndexedExpression(conf)
    val rightIndicators = cspec_fixed_standard_parts().rightIndicators()
    return ScanStmt(
        compareExpression,
        compareLength,
        baseExpression,
        startPosition ?: 1,
        this.cspec_fixed_standard_parts()!!.result!!.toAst(conf),
        rightIndicators,
        position
    )
}

internal fun CsCHECKContext.toAst(conf: ToAstConfiguration): Statement {
    val position = toPosition(conf.considerPosition)
    val factor1 = this.factor1Context()?.content?.toAst(conf) ?: throw UnsupportedOperationException("CHECK operation requires factor 1: ${this.text} - ${position.atLine()}")
    val (expression, startPosition) = this.cspec_fixed_standard_parts().factor2.toIndexedExpression(conf)
    return CheckStmt(
        factor1,
        expression,
        startPosition ?: 1,
        this.cspec_fixed_standard_parts()?.result?.toAst(conf),
        position
    )
}

private fun FactorContext.toIndexedExpression(conf: ToAstConfiguration): Pair<Expression, Int?> =
    if (this.text.contains(":")) this.text.toIndexedExpression(toPosition(conf.considerPosition)) else this.content.toAst(conf) to null

private fun String.toIndexedExpression(position: Position?): Pair<Expression, Int?> {
    val baseStringTokens = this.split(":")
    val startPosition =
        when (baseStringTokens.size) {
            !in 1..2 -> throw UnsupportedOperationException("Wrong base string expression at line ${position?.line()}: $this")
            2 -> baseStringTokens[1].toInt()
            else -> null
        }
    val reference = baseStringTokens[0]
    return DataRefExpr(ReferenceByName(reference), position) to startPosition
}

internal fun CsMOVEAContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): MoveAStmt {
    val position = toPosition(conf.considerPosition)
    val expression = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("MOVEA operation requires factor 2: ${this.text} - ${position.atLine()}")
    val resultExpression = this.cspec_fixed_standard_parts().resultExpression(conf) as AssignableExpression
    return MoveAStmt(this.operationExtender?.text, resultExpression, expression, position)
}

internal fun CsMOVEContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): MoveStmt {
    val position = toPosition(conf.considerPosition)
    val expression = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("MOVE operation requires factor 2: ${this.text} - ${position.atLine()}")
    val resultExpression = this.cspec_fixed_standard_parts().resultExpression(conf) as AssignableExpression
    return MoveStmt(resultExpression, expression, position)
}

internal fun CsMOVELContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): MoveLStmt {
    val position = toPosition(conf.considerPosition)
    val expression = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("MOVE operation requires factor 2: ${this.text} - ${position.atLine()}")
    val resultExpression = this.cspec_fixed_standard_parts().resultExpression(conf) as AssignableExpression
    val result = this.cspec_fixed_standard_parts().result.text
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(result, position, conf)
    return MoveLStmt(this.operationExtender?.text, resultExpression, dataDefinition, expression, position)
}

internal fun CsZ_ADDContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): ZAddStmt {
    val position = toPosition(conf.considerPosition)
    val expression = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("Z-ADD operation requires factor 2: ${this.text} - ${position.atLine()}")
    val name = this.cspec_fixed_standard_parts().result.text
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(name, position, conf)
    return ZAddStmt(DataRefExpr(ReferenceByName(name), position), dataDefinition, expression, position)
}

internal fun CsMULTContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): MultStmt {
    val position = toPosition(conf.considerPosition)
    val result = this.cspec_fixed_standard_parts().result.text
    val factor1 = leftExpr(conf)
    val factor2 = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("SUB operation requires factor 2: ${this.text} - ${position.atLine()}")
    this.cspec_fixed_standard_parts().toDataDefinition(result, position, conf)
    val extenders = this.operationExtender?.extender?.text?.toUpperCase()?.toCharArray() ?: CharArray(0)
    return MultStmt(DataRefExpr(ReferenceByName(result), position), 'H' in extenders, factor1, factor2, position)
}

internal fun CsDIVContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): DivStmt {
    val position = toPosition(conf.considerPosition)
    val result = this.cspec_fixed_standard_parts().result.text
    val factor1 = leftExpr(conf)
    val factor2 = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("SUB operation requires factor 2: ${this.text} - ${position.atLine()}")
    this.cspec_fixed_standard_parts().toDataDefinition(result, position, conf)
    val extenders = this.operationExtender?.extender?.text?.toUpperCase()?.toCharArray() ?: CharArray(0)
    return DivStmt(DataRefExpr(ReferenceByName(result), position), 'H' in extenders, factor1, factor2, position)
}

internal fun CsTAGContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): TagStmt {
    return TagStmt(this.factor1Context()?.content?.text!!, toPosition(conf.considerPosition))
}

private fun ParserRuleContext.leftExpr(conf: ToAstConfiguration): Expression? {
    return if (this.factor1Context()?.content?.text?.isNotBlank() == true) {
        this.factor1Context().content.toAst(conf)
    } else {
        null
    }
}

internal fun CsGOTOContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): GotoStmt {
    return GotoStmt(this.cspec_fixed_standard_parts().factor2.text, toPosition(conf.considerPosition))
}

internal fun CsCABContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): CabStmt {
    return cabStatement(null, this.cspec_fixed_standard_parts(), conf)
}

internal fun CsCABLEContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): CabStmt {
    return cabStatement(ComparisonOperator.LE, this.cspec_fixed_standard_parts(), conf)
}

internal fun CsCABLTContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): CabStmt {
    return cabStatement(ComparisonOperator.LT, this.cspec_fixed_standard_parts(), conf)
}

internal fun CsCABEQContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): CabStmt {
    return cabStatement(ComparisonOperator.EQ, this.cspec_fixed_standard_parts(), conf)
}

internal fun CsCABGTContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): CabStmt {
    return cabStatement(ComparisonOperator.GT, this.cspec_fixed_standard_parts(), conf)
}

internal fun CsCABGEContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): CabStmt {
    return cabStatement(ComparisonOperator.GE, this.cspec_fixed_standard_parts(), conf)
}

fun Cspec_fixed_standard_partsContext.rightIndicators() =
    RightIndicators(
        hi.asIntOrNull(),
        lo.asIntOrNull(),
        eq.asIntOrNull()
    )

fun ParserRuleContext.cabStatement(
    comparison: ComparisonOperator?,
    cspecFixedStandardParts: Cspec_fixed_standard_partsContext,
    conf: ToAstConfiguration
): CabStmt {
    val position = toPosition(conf.considerPosition)
    val left = leftExpr(conf) ?: throw UnsupportedOperationException("CAB operation requires factor 1 - $text")
    val right = cspecFixedStandardParts.factor2Expression(conf) ?: throw UnsupportedOperationException("CAB operation requires factor 2 - $text - ${position.atLine()}")
    return CabStmt(
        left,
        right,
        comparison,
        cspecFixedStandardParts.result.text,
        cspecFixedStandardParts.rightIndicators(),
        position
    )
}

internal fun CsADDContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): AddStmt {
    val position = toPosition(conf.considerPosition)
    val result = this.cspec_fixed_standard_parts().result.text
    val left = leftExpr(conf)
    val right = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("ADD operation requires factor 2: ${this.text} - ${position.atLine()}")
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(result, position, conf)
    return AddStmt(left, DataRefExpr(ReferenceByName(result), position), dataDefinition, right, position)
}

internal fun CsZ_SUBContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): ZSubStmt {
    val position = toPosition(conf.considerPosition)
    val expression = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("Z-SUB operation requires factor 2: ${this.text} - ${position.atLine()}")
    val name = this.cspec_fixed_standard_parts().result.text
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(name, position, conf)
    return ZSubStmt(DataRefExpr(ReferenceByName(name), position), dataDefinition, expression, position)
}

internal fun CsSUBContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): SubStmt {
    val position = toPosition(conf.considerPosition)
    val result = this.cspec_fixed_standard_parts().result.text
    val left = leftExpr(conf)
    val right = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("SUB operation requires factor 2: ${this.text} - ${position.atLine()}")
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(result, position, conf)
    return SubStmt(left, DataRefExpr(ReferenceByName(result), position), dataDefinition, right, position)
}

internal fun ResultIndicatorContext?.asIntOrNull(): Int? = this?.text?.asIntOrNull()

internal fun CsCOMPContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): CompStmt {
    val position = toPosition(conf.considerPosition)
    val left = leftExpr(conf) ?: throw UnsupportedOperationException("COMP operation requires factor 1: ${this.text}")
    val right = cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("COMP operation requires factor 2: ${this.text} - ${position.atLine()}")

    return CompStmt(
        left,
        right,
        cspec_fixed_standard_parts().rightIndicators(),
        position
    )
}

internal fun CsCLEARContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): ClearStmt {
    val name = this.cspec_fixed_standard_parts().result.text
    val position = toPosition(conf.considerPosition)
    return ClearStmt(
        annidatedReferenceExpression(name, toPosition(conf.considerPosition)),
        this.cspec_fixed_standard_parts().toDataDefinition(name, position, conf),
        position
    )
}

internal fun CsDEFINEContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): DefineStmt {
    val originalVarName = this.cspec_fixed_standard_parts().factor2.text
    val newVarName = this.cspec_fixed_standard_parts().result.text
    val position = toPosition(conf.considerPosition)
    return DefineStmt(originalVarName, newVarName, position)
}

private fun QualifiedTargetContext.getFieldName(): String {
    return if (this.fieldName != null) {
        this.fieldName.text
    } else {
        this.field.ID().text
    }
}

internal fun TargetContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): AssignableExpression {
    return when (this) {
        is SimpleTargetContext -> DataRefExpr(ReferenceByName(this.name.text), toPosition(conf.considerPosition))
        is IndexedTargetContext -> ArrayAccessExpr(
            array = this.base.toAst(conf),
            index = this.index.toAst(conf),
            position = toPosition(conf.considerPosition)
        )
        is SubstTargetContext -> this.bif_subst().toAst(conf)
        is QualifiedTargetContext -> QualifiedAccessExpr(
            DataRefExpr(ReferenceByName(this.container.text), this.container!!.toPosition(conf.considerPosition)),
            ReferenceByName(this.getFieldName()),
            toPosition(conf.considerPosition)
        )
        is IndicatorTargetContext -> IndicatorExpr(
            this.indic.text.indicatorIndex()!!,
            toPosition(conf.considerPosition)
        )
        is GlobalIndicatorTargetContext -> GlobalIndicatorExpr(
            toPosition(conf.considerPosition)
        )
        else -> todo(conf = conf)
    }
}

internal fun AssignmentOperatorIncludingEqualContext.toAssignmentOperator(): AssignmentOperator {
    return when {
        this.EQUAL() != null -> NORMAL_ASSIGNMENT
        this.CPLUS() != null -> PLUS_ASSIGNMENT
        this.CMINUS() != null -> MINUS_ASSIGNMENT
        this.CMULT() != null -> MULT_ASSIGNMENT
        this.CDIV() != null -> DIVIDE_ASSIGNMENT
        this.CEXP() != null -> EXP_ASSIGNMENT
        else -> throw UnsupportedOperationException(this.text)
    }
}

internal fun CsCALLContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): CallStmt {
    val position = this.toPosition(true)
    require(this.cspec_fixed_standard_parts().factor().factorContent().size == 1) {
        "Missing factor 1 in call statement at line ${position.line()}"
    }
    val literal = this.cspec_fixed_standard_parts().factor().factorContent()[0].literal()
    val functionCalled: Expression?
    functionCalled = literal?.toAst(conf) ?: this.cspec_fixed_standard_parts().factor2.content.toAst(conf)
    return CallStmt(
        functionCalled,
        this.csPARM().map { it.toAst(conf) },
        this.cspec_fixed_standard_parts().lo.asIndex(),
        toPosition(conf.considerPosition)
    )
}

internal fun Cspec_fixed_x2Context.toAst(conf: ToAstConfiguration = ToAstConfiguration()): CallPStmt {
    val position = toPosition(conf.considerPosition)
    require(this.c_free().expression().function().functionName() != null) {
        "Missing functionName in callp statement at line ${position.line()}"
    }

    val literal = this.c_free().expression().literal()
    val functionCalled: FunctionCall = (literal?.toAst(conf) ?: this.c_free().expression().toAst(conf)) as FunctionCall
    val errorIndicator: IndicatorKey? = null

    return CallPStmt(
        functionCalled,
        errorIndicator,
        position
    )
}

internal fun CsSORTAContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): SortAStmt {
    val expr = this.fixedexpression.expression().toAst(conf)
    return SortAStmt(expr, toPosition(conf.considerPosition))
}
internal fun ResultIndicatorContext.asIndex(): Int? {
    // TODO: verify if we should cover other cases (e.g. external indicators)
    return this.GeneralIndicator()?.text?.toIntOrNull()
}

internal fun CsCATContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): CatStmt {
    val position = toPosition(conf.considerPosition)
    val left = leftExpr(conf)
    val right = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("COMP operation requires factor 2: ${this.text} - ${position.atLine()}")
    var blanksInBetween = 0

    if (null != this.cspec_fixed_standard_parts().factor2.content2 &&
            this.cspec_fixed_standard_parts().factor2.content2.children.size > 0) {
        blanksInBetween = this.cspec_fixed_standard_parts().factor2.content2.children[0].toString().toInt()
    }
    val target = this.cspec_fixed_standard_parts().resultExpression(conf) as AssignableExpression
    return CatStmt(
        left,
        right,
        target,
        blanksInBetween,
        position
    )
}

internal fun CsLOOKUPContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): LookupStmt {
    val position = toPosition(conf.considerPosition)
    val left = leftExpr(conf) ?: throw UnsupportedOperationException("LOOKUP operation requires factor 1: ${this.text}")
    val right = cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("LOOKUP operation requires factor 2: ${this.text} - ${position.atLine()}")

    val rightIndicators = cspec_fixed_standard_parts().rightIndicators()
    require(!rightIndicators.allPresent()) {
        "You cant use all three indicators with Lookup Statement"
    }
    return LookupStmt(
        left,
        right,
        rightIndicators,
        position
    )
}

internal fun CsXFOOTContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): XFootStmt {
    val position = toPosition(conf.considerPosition)
    val array2sumup = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("XFOOT operation requires factor 2: ${this.text} - ${position.atLine()}")
    val resultExpression = this.cspec_fixed_standard_parts().resultExpression(conf) as AssignableExpression
    val name = this.cspec_fixed_standard_parts().result.text
    /**
     *
    XFOOT adds the elements of an array together and places the sum into the field
    specified as the result field. Factor 2 contains the name of the array.
    If half-adjust is specified, the rounding occurs after all elements are summed and
    before the results are moved into the result field. If the result field is an element of
    the array specified in factor 2, the value of the element before the XFOOT
    operation is used to calculate the total of the array.
    If the array is float, XFOOT will be performed as follows: When the array is in
    descending sequence, the elements will be added together in reverse order.
    Otherwise, the elements will be added together starting with the first elements of
    the array.
     *
    https://docs.asna.com/documentation/Help120/AVR/_HTML/XFOOT.htm
    0017.00      C                   xfoot     arr2          sum               4 0
     *
    Pos
    Optional.  Turned on if the value of Result is a positive number.
    Neg
    Optional.  Turned on if the value of Result is a negative number.
    Zero
    Optional.  Turned on if the value of Result is zero.
    */
    val rightIndicators = cspec_fixed_standard_parts().rightIndicators()
    return XFootStmt(
        array2sumup, resultExpression, rightIndicators, this.cspec_fixed_standard_parts().toDataDefinition(
            name,
            position,
            conf
        ), position
    )
}

internal fun FreeContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Statement {
    return when {
        this.baseExpression().op().op_dsply() != null -> this.baseExpression().op().op_dsply().toAst(conf)
        this.baseExpression().op().op_eval() != null -> this.baseExpression().op().op_eval().toAst(conf)
        else -> todo(conf = conf)
    }
}

internal fun Op_dsplyContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Statement {
    val expression = this.expression()[0].toAst(conf = conf)
    return DisplayStmt(expression, null, toPosition(conf.considerPosition))
}

internal fun Op_evalContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Statement {
    return this.evalExpression().toAst(conf = conf)
}

internal fun EvalExpressionContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Statement {
    return if (assignmentExpression() != null) {
        assignmentExpression().toAst(conf = conf)
    } else {
        todo(conf = conf)
    }
}

internal fun AssignmentExpressionContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Statement {
    val target = when {
        this.simpleExpression() != null -> this.simpleExpression().toAst(conf = conf)
        this.expression() != null -> this.expression().toAst(conf = conf)
        else -> todo(conf = conf)
    }
    require(target is AssignableExpression)
    return EvalStmt(
        target = target,
        expression = expression().toAst(conf = conf),
        operator = NORMAL_ASSIGNMENT
    )
}

fun ParserRuleContext.todo(message: String? = null, conf: ToAstConfiguration): Nothing {
    val pref = message?.let {
        "$message at"
    } ?: "Error at"
    TODO("$pref ${toPosition(conf.considerPosition)} ${this.javaClass.name}")
}

fun ParserRuleContext.error(message: String? = null, cause: Throwable? = null, conf: ToAstConfiguration): Nothing {
    val pref = message?.let {
        "$message at: "
    } ?: "Error at: "
    throw IllegalStateException(
        "$pref${toPosition(conf.considerPosition)} ${this.javaClass.name}",
        cause
    )
}

/**
 * Run a block. In case of error throws an error encapsulating useful information
 * like node position
 */
fun <T : ParserRuleContext, R> T.runParserRuleContext(conf: ToAstConfiguration, block: (T) -> R): R {
    return kotlin.runCatching {
        block.invoke(this)
    }.onFailure {
        this.error(it.message, it, conf)
    }.getOrThrow()
}

fun Node.error(message: String? = null, cause: Throwable? = null): Nothing {
    throw IllegalStateException(
        message?.let { "$message at: ${this.position}" } ?: "Error at: ${this.position}",
        cause?.let { cause }
    )
}

fun Node.todo(message: String? = null): Nothing {
    val pref = message?.let {
        "$message at "
    } ?: "Error at "
    TODO("${pref}Position: ${this.position}")
}

/**
 * Run a block related AST node. In case of error throws an error encapsulating useful information
 * like node position
 */
fun <T : Node, R> T.runNode(block: (T) -> R): R {
    return kotlin.runCatching {
        block.invoke(this)
    }.onFailure {
        this.error(it.message, it)
    }.getOrThrow()
}
