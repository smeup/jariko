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
import com.smeup.rpgparser.parsing.facade.CopyBlocks
import com.smeup.rpgparser.parsing.facade.findAllDescendants
import com.smeup.rpgparser.utils.ComparisonOperator
import com.smeup.rpgparser.utils.asIntOrNull
import com.smeup.rpgparser.utils.isEmptyTrim
import com.smeup.rpgparser.utils.mapNotNullOrError
import com.strumenta.kolasu.mapping.toPosition
import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Position
import com.strumenta.kolasu.model.ReferenceByName
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token
import java.util.*

enum class AstHandlingPhase {
    FileDefinitionsCreation,
    InputSpecificationsCreation,
    DataDefinitionsCreation,
    MainStatementsCreation,
    SubroutinesCreation,
    CompileTimeArraysCreation,
    DirectivesCreation,
    ProceduresCreation,
    Resolution
}

data class ToAstConfiguration(
    val considerPosition: Boolean = true,
    val compileTimeInterpreter: CompileTimeInterpreter = CommonCompileTimeInterpreter,
    /**
     * Called in case of error occurred after the AstHandlingPhase, it must be return
     * if to continue or not. Default continue excepts after Resolution
     * */
    var afterPhaseErrorContinue: (phase: AstHandlingPhase) -> Boolean = { phase -> phase != AstHandlingPhase.Resolution }
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

internal object KnownDataDefinition {
    fun getInstance(): KnownDataDefinitionInstance {
        return if (MainExecutionContext.getParsingProgramStack().empty()) {
            MainExecutionContext.getAttributes()
        } else {
            MainExecutionContext.getParsingProgramStack().peek().attributes
        }.computeIfAbsent("com.smeup.rpgparser.parsing.parsetreetoast.KnownDataDefinition") {
            mutableMapOf<String, DataDefinition>()
        } as MutableMap<String, DataDefinition>
    }
}

typealias KnownDataDefinitionInstance = MutableMap<String, DataDefinition>

private fun List<StatementContext?>.getDataDefinition(
    conf: ToAstConfiguration = ToAstConfiguration(),
    fileDefinitions: Map<FileDefinition, List<DataDefinition>>? = null,
    inputSpecifications: List<InputSpecificationGroup> = emptyList(),
    parentDataDefinitions: List<DataDefinition>? = null,
    useKnownDataDefinitionInstance: Boolean = false,
    procedureName: String? = null
): Pair<MutableList<DataDefinitionProvider>, KnownDataDefinitionInstance> {
    // We need to calculate first all the data definitions which do not contain the LIKE DS directives
    // then we calculate the ones with the LIKE DS clause, as they could have references to DS declared
    // after them
    val dataDefinitionProviders: MutableList<DataDefinitionProvider> = LinkedList()
    val knownDataDefinitions = if (useKnownDataDefinitionInstance) KnownDataDefinition.getInstance() else mutableMapOf()

    fileDefinitions?.let {
        val postProcessedFileDefinitions = it.processWithSpecifications(inputSpecifications)
        postProcessedFileDefinitions.filter { !it.key.justExtName }.values.flatten().removeDuplicatedDataDefinition().forEach { def ->
            dataDefinitionProviders.add(def.updateKnownDataDefinitionsAndGetHolder(knownDataDefinitions))
        }
    }

    /**
     * Statements sorting to accommodate processing needs
     * Step 1: Move the D specs with like because depending on other D specs definitions
     * Step 2: Split statements marked as CONST from the rest
     */
    val (constants, sortedStatements) = this.filterNotNull().moveLikeStatementToTheEnd(conf = conf).splitConstants()

    // Define constants
    val constantProviders = constants.getValidDataDefinitionHolders(
        conf = conf,
        knownDataDefinitions = knownDataDefinitions,
        fileDefinitions = fileDefinitions,
        parentDataDefinitions = parentDataDefinitions,
        procedureName = procedureName
    )
    dataDefinitionProviders.addAll(constantProviders)

    // First pass ignore exception and all the know definitions
    val firstPassProviders = sortedStatements.mapNotNull {
        it.toDataDefinitionProvider(
            conf = conf,
            knownDataDefinitions = knownDataDefinitions,
            parentDataDefinitions = parentDataDefinitions,
            fileDefinitions = fileDefinitions
        )
    }
    dataDefinitionProviders.addAll(firstPassProviders)

    // Second pass, everything, I mean everything
    val secondPassProviders = sortedStatements.getValidDataDefinitionHolders(
        conf = conf,
        knownDataDefinitions = knownDataDefinitions,
        fileDefinitions = fileDefinitions,
        parentDataDefinitions = parentDataDefinitions,
        procedureName = procedureName
    )
    dataDefinitionProviders.addAll(secondPassProviders)

    return Pair(dataDefinitionProviders, knownDataDefinitions)
}

private fun List<StatementContext>.getValidDataDefinitionHolders(
    conf: ToAstConfiguration = ToAstConfiguration(),
    knownDataDefinitions: KnownDataDefinitionInstance,
    fileDefinitions: Map<FileDefinition, List<DataDefinition>>? = null,
    parentDataDefinitions: List<DataDefinition>? = null,
    procedureName: String? = null
): List<DataDefinitionHolder> {
    return this.mapNotNullOrError {
        when {
            it.dspec() != null -> {
                it.dspec().toAst(
                    conf = conf,
                    knownDataDefinitions = knownDataDefinitions.values.toList(),
                    parentDataDefinitions = parentDataDefinitions,
                    fileDefinitions = fileDefinitions,
                    procedureName = procedureName
                ).updateKnownDataDefinitionsAndGetHolder(knownDataDefinitions)
            }
            it.dcl_c() != null -> it.dcl_c().toAst(conf).updateKnownDataDefinitionsAndGetHolder(knownDataDefinitions)
            else -> null
        }
    }
}

private fun RContext.getDataDefinitions(
    conf: ToAstConfiguration = ToAstConfiguration(),
    fileDefinitions: Map<FileDefinition, List<DataDefinition>>,
    inputSpecifications: List<InputSpecificationGroup> = emptyList()
): List<DataDefinition> {
    val (providers) = this.statement().getDataDefinition(
        conf = conf,
        fileDefinitions = fileDefinitions,
        inputSpecifications = inputSpecifications,
        useKnownDataDefinitionInstance = true
    )
    return providers.mapNotNull { kotlin.runCatching { it.toDataDefinition() }.getOrNull() }
}

private fun List<StatementContext>.moveLikeStatementToTheEnd(conf: ToAstConfiguration): List<StatementContext> {
    val likeStatements = this.filter {
        it.dcl_ds()?.useLikeDs(conf = conf) ?: it.dspec()?.useLike() ?: false
    }
    val otherStatements = this.filter {
        !(it.dcl_ds()?.useLikeDs(conf = conf) ?: it.dspec()?.useLike() ?: false)
    }
    return otherStatements + likeStatements
}

private fun List<StatementContext>.splitConstants(): Pair<List<StatementContext>, List<StatementContext>> {
    val constantStatements = this.filter { it.isConstant() }
    val otherStatements = this.filter { !it.isConstant() }

    return Pair(constantStatements, otherStatements)
}

private fun StatementContext.isConstant() = when {
    this.dcl_c() != null -> this.dcl_c().keyword_const() != null
    this.dspec() != null -> this.dspec().keyword().any { keyword -> keyword.keyword_const() != null } || this.dspec().dspecConstant() != null
    else -> false
}

private fun DataDefinition.updateKnownDataDefinitionsAndGetHolder(
    knownDataDefinitions: MutableMap<String, DataDefinition>
): DataDefinitionHolder {
        knownDataDefinitions.addIfNotPresent(this)
    return DataDefinitionHolder(this)
}

private fun MutableMap<String, DataDefinition>.addIfNotPresent(dataDefinition: DataDefinition) {
    val old = get(dataDefinition.name)
    if (old == null || (old.type is RecordFormatType && dataDefinition.type is DataStructureType)) {
        put(dataDefinition.name, dataDefinition)
    } else {
        dataDefinition.error("${dataDefinition.name} has been defined twice")
    }
}

private fun FileDefinition.loadMetadata(): FileMetadata {
    return when {
        fileType == FileType.PRINTER -> {
            FileMetadata(
                name = this.name,
                tableName = this.name,
                recordFormat = this.internalFormatName ?: this.name,
                fields = emptyList(),
                accessFields = emptyList()
            )
        }
        (fileType == FileType.DB || MainExecutionContext.getConfiguration().dspfConfig == null) -> {
            val reloadConfig = MainExecutionContext.getConfiguration()
                .reloadConfig
                ?: error("Not found metadata for $this because missing property reloadConfig in configuration")

            kotlin.runCatching {
                reloadConfig.metadataProducer.invoke(name)
            }.onFailure { error ->
                error("Not found metadata for $this", error)
            }.getOrNull() ?: error("Not found metadata for $this")
        }
        (fileType == FileType.VIDEO) -> {
            val dspfConfig = MainExecutionContext.getConfiguration()
                .dspfConfig ?: error("Not found metadata for $this because missing property dspfConfig in configuration")

            kotlin.runCatching {
                dspfConfig.metadataProducer.invoke(name)
            }.onFailure { error ->
                error("Not found metadata for $this", error)
            }.getOrNull() ?: error("Not found metadata for $this")
        }
        else -> error("Unhandled file type $fileType")
    }
}

internal fun FileDefinition.toDataDefinitions(): List<DataDefinition> {
    val dataDefinitions = mutableListOf<DataDefinition>()
    val metadata = loadMetadata()

    if (internalFormatName == null) internalFormatName = metadata.recordFormat.ifEmpty { metadata.name }

    dataDefinitions.addAll(
        metadata.fields.map { dbField ->
            dbField.toDataDefinition(prefix = prefix, position = position).apply {
                createDbFieldDataDefinitionRelation(dbField.fieldName, name)
            }
        }
    )

    // These are the fields related the record format, these fields will
    // be used in assignment operation to lookup for the DataDefinitions related these fields
    val fieldsDefinition = dataDefinitions.map {
        // explicitStartOffset and explicitEndOffsets set to zero are wanted
        FieldDefinition(name = it.name, type = it.type, explicitStartOffset = 0, explicitEndOffset = 0, position = it.position)
    }
    // record format possibly for file video is unuseful
    if (fileType == FileType.DB) {
        val recordFormatDefinition = DataDefinition(
            internalFormatName!!,
            type = RecordFormatType,
            position = position,
            fields = fieldsDefinition
        )

        dataDefinitions.add(recordFormatDefinition)
    }

    return dataDefinitions
}

fun RContext.toAst(conf: ToAstConfiguration = ToAstConfiguration(), source: String? = null, copyBlocks: CopyBlocks? = null): CompilationUnit {
    val fileDefinitions = this.statement()
        .mapNotNull { statement ->
            when {
                statement.fspec_fixed() != null -> statement.fspec_fixed().runParserRuleContext(conf) { context ->
                    kotlin.runCatching { context.toAst(conf).let { dataDefinition -> dataDefinition to dataDefinition.toDataDefinitions() } }.getOrNull()
                }
                statement.dcl_ds()?.useExtName() ?: false -> statement.dcl_ds().runParserRuleContext(conf) { context ->
                    kotlin.runCatching { context.toAstWithParameters(conf).let { extNameDefinition -> extNameDefinition to extNameDefinition.toDataDefinitions() } }.getOrNull()
                }
                else -> null
            }
        }.toMap()
    checkAstCreationErrors(phase = AstHandlingPhase.FileDefinitionsCreation)

    val inputSpecifications = this.findAllDescendants(Ispec_fixedContext::class).mapNotNull {
        it.runParserRuleContext(conf) { context -> kotlin.runCatching { context.toAst(conf) }.getOrNull() }
    }
    checkAstCreationErrors(phase = AstHandlingPhase.InputSpecificationsCreation)

    val dataDefinitions = getDataDefinitions(conf, fileDefinitions, inputSpecifications.grouped())
    checkAstCreationErrors(phase = AstHandlingPhase.DataDefinitionsCreation)

    val displayFiles = fileDefinitions.keys.toList().toDSPF()

    val mainStmts = this.statement().mapNotNull {
        when {
            it.cspec_fixed() != null -> it.cspec_fixed().runParserRuleContext(conf) { context ->
                kotlin.runCatching { context.toAst(conf) }.getOrNull()
            }
            it.block() != null -> it.block().runParserRuleContext(conf) { context ->
                kotlin.runCatching { context.toAst(conf) }.getOrNull()
            }
            it.free() != null -> it.free().runParserRuleContext(conf) { context ->
                kotlin.runCatching { context.toAst(conf) }.getOrNull()
            }
            else -> null
        }
    }
    checkAstCreationErrors(phase = AstHandlingPhase.MainStatementsCreation)

    val subroutines = this.subroutine().mapNotNull {
        it.runParserRuleContext(conf) { context -> kotlin.runCatching { context.toAst(conf) }.getOrNull() }
    }
    checkAstCreationErrors(phase = AstHandlingPhase.SubroutinesCreation)

    val compileTimeArrays = this.endSourceBlock()?.endSource()?.mapNotNull {
        it.runParserRuleContext(conf) { context -> kotlin.runCatching { context.toAst(conf) }.getOrNull() }
    } ?: emptyList()
    checkAstCreationErrors(phase = AstHandlingPhase.CompileTimeArraysCreation)

    val directives = this.findAllDescendants(Hspec_fixedContext::class).mapNotNull {
        it.runParserRuleContext(conf) { context -> kotlin.runCatching { context.toAst(conf) }.getOrNull() }
    }.flatten()
    checkAstCreationErrors(phase = AstHandlingPhase.DirectivesCreation)

    // if we have no procedures, the property procedure must be null because we decided it must be optional
    var procedures = this.procedure().mapNotNull {
        it.runParserRuleContext(conf) { context -> kotlin.runCatching { context.toAst(conf, dataDefinitions) }.getOrNull() }
    }.let {
        if (it.isEmpty()) null
        else it
    }
    checkAstCreationErrors(phase = AstHandlingPhase.ProceduresCreation)

    val procerurePrototypes = getProcerurePrototypes(rContext = this,
        conf = conf,
        dataDefinitions = dataDefinitions,
        procedures = procedures
    )

    if (null == procedures) {
        if (procerurePrototypes.isNotEmpty()) {
            procedures = procerurePrototypes
        }
    } else {
        (procedures as ArrayList).addAll(procerurePrototypes)
    }

    return CompilationUnit(
        // in fileDefinitions must go only FileDefinition related to F specs
        fileDefinitions = fileDefinitions.keys.filter { !it.justExtName },
        dataDefinitions = dataDefinitions,
        main = MainBody(mainStmts, if (conf.considerPosition) mainStmts.position() else null),
        subroutines = subroutines,
        compileTimeArrays = compileTimeArrays,
        directives = directives,
        position = this.toPosition(conf.considerPosition),
        apiDescriptors = this.statement().toApiDescriptors(conf),
        procedures = procedures,
        source = source,
        copyBlocks = copyBlocks,
        displayFiles = displayFiles
    ).let { compilationUnit ->
        // for each procedureUnit set compilationUnit as parent
        // in order to resolve global data references
        procedures?.onEach { procedureUnit ->
            procedureUnit.parent = compilationUnit
        }
        compilationUnit
    }.postProcess()
}

private fun getProcerurePrototypes(
    rContext: RContext,
    conf: ToAstConfiguration,
    dataDefinitions: List<DataDefinition>,
    procedures: List<CompilationUnit>?
): List<CompilationUnit> {
    /*
     * Could be prototype procedures definition. The construction is performed:
     * 1. filtering by procedures without concrete implementation;
     * 2. preparing fake procedure
     */
    val prototypeProcedures: Map<String, List<DataDefinition>> =
        rContext.children
            .filterIsInstance<Dcl_prContext>()
            .filter { procedures?.firstOrNull { p -> p.procedureName == it.getName() } == null }
            .associate { it ->
                val procedureName: String = it.children.firstOrNull { it is PrBeginContext }.let { (it as PrBeginContext).ds_name().NAME().text }
                val procedureDataDefinitions = it.children.filterIsInstance<Parm_fixedContext>().map {
                    val dataDefinition: DataDefinition = it.toAst(conf, dataDefinitions.toList())
                    dataDefinition.paramPassedBy = it.getParamPassedBy()
                    dataDefinition.paramOptions = it.getParamOptions()

                    dataDefinition
                }

                procedureName to procedureDataDefinitions
            }
            .toMap()

    return prototypeProcedures.map {
        CompilationUnit(
            fileDefinitions = emptyList(),
            dataDefinitions = emptyList(),
            main = MainBody(emptyList()),
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

/**
 * Gets the name of procedure
 */
private fun Dcl_prContext.getName(): String = this.children.firstOrNull { it is PrBeginContext }.let { (it as PrBeginContext).ds_name().NAME().text }

/**
 * Gets how the param is passed. If isn't set Const or Value, the param is passed by Reference.
 */
private fun Parm_fixedContext.getParamPassedBy(): ParamPassedBy {
    var paramPassedBy = ParamPassedBy.Reference
    this.keyword().forEach {
        if (it.keyword_const() != null) {
            paramPassedBy = ParamPassedBy.Const
        } else if (it.keyword_value() != null) {
            paramPassedBy = ParamPassedBy.Value
        }
    }
    return paramPassedBy
}

/**
 * Get a list of param options, if they exist.
 */
private fun Parm_fixedContext.getParamOptions(): List<ParamOption> {
    return this.keyword().filter { it.keyword_options() != null }.flatMap { it ->
        it.keyword_options().identifier().map {
            val keyword = it.free_identifier().idOrKeyword().ID().toString()
            ParamOption.getByKeyword(keyword)
        }
    }
}

private fun Dcl_dsContext.useLikeDs(conf: ToAstConfiguration): Boolean {
    val keywordLikeDs = this.keyword_likeds()
    if (keywordLikeDs != null) {
        todo(conf = conf)
    }
    return (this.keyword().any { it.keyword_likeds() != null })
}

private fun DspecContext.useLike(): Boolean {
    return this.keyword().any { it.keyword_like() != null }
}

private fun Dcl_dsContext.useExtName(): Boolean {
    return this.keyword().any { it.keyword_extname() != null }
}

internal fun EndSourceContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): CompileTimeArray {
    fun cName(s: String) =
        if (s.trim().matches(Regex("^\\*\\*[\\s]*(CTDATA)*([\\s]+[\\S]+)\$"))) {
            s.substringAfter("**").trim().substringAfter("CTDATA").trim()
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
    val statements = this.statement().mapNotNull {
        kotlin.runCatching {
            it.runParserRuleContext(conf) { context -> context.toAst(conf) }
        }.getOrNull()
    }
    return Subroutine(
        this.begsr().csBEGSR().factor1.text,
        statements,
        this.endsr().csENDSR().factor1.text,
        toPosition(conf.considerPosition)
    )
}

internal fun ProcedureContext.toAst(conf: ToAstConfiguration = ToAstConfiguration(), parentDataDefinitions: List<DataDefinition>): CompilationUnit {
    val procedureName = this.beginProcedure().psBegin().ps_name().text
    MainExecutionContext.getParsingProgramStack().peek().parsingFunctionNameStack.push(procedureName)

    // TODO FileDefinitions

    // DataDefinitions
    val dataDefinitions = getDataDefinitions(
        conf = conf,
        parentDataDefinitions = parentDataDefinitions,
        procedureName = procedureName
    )

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
        fileDefinitions = emptyList(),
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
                .forEach { keywordContext ->
                        if (keywordContext.keyword_const() != null) {
                            dataDefinition.const = true
                            dataDefinition.paramPassedBy = ParamPassedBy.Const
                        } else if (keywordContext.keyword_value() != null) {
                            dataDefinition.paramPassedBy = ParamPassedBy.Value
                        }
                        if (keywordContext.keyword_options() != null) {
                            keywordContext.keyword_options().identifier().forEach {
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

private fun StatementContext.toDataDefinitionProvider(
    conf: ToAstConfiguration = ToAstConfiguration(),
    knownDataDefinitions: MutableMap<String, DataDefinition>,
    parentDataDefinitions: List<DataDefinition>?,
    fileDefinitions: Map<FileDefinition, List<DataDefinition>>?
): DataDefinitionProvider? {
    return when {
        this.dcl_ds() != null -> {
            kotlin.runCatching {
                try {
                    this.dcl_ds()
                        .toAst(
                            conf = conf,
                            knownDataDefinitions = knownDataDefinitions.values,
                            parentDataDefinitions = parentDataDefinitions,
                            fileDefinitions = fileDefinitions
                        ).updateKnownDataDefinitionsAndGetHolder(knownDataDefinitions)
                    // these errors can be caught because they don't introduce sneaky errors
                } catch (e: CannotRetrieveDataStructureElementSizeException) {
                    null
                } catch (e: ParseTreeToAstError) {
                    null
                } catch (e: Exception) {
                    throw e.fireErrorEvent(this.dcl_ds().toPosition(conf.considerPosition))
                }
            }.getOrNull()
        }
        else -> null
    }
}

private fun ProcedureContext.getDataDefinitions(conf: ToAstConfiguration = ToAstConfiguration(), parentDataDefinitions: List<DataDefinition>, procedureName: String): List<DataDefinition> {
    val (providers, knownDataDefinitions) = this.subprocedurestatement()
        .map { it.statement() }
        .getDataDefinition(
            conf = conf,
            parentDataDefinitions = parentDataDefinitions,
            procedureName = procedureName
        )

    // PROCEDURE PARAMETERS pass
    val paramProviders = this.dcl_pi().pi_parm_fixed()
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
        }
    providers.addAll(paramProviders)

    return providers.map { it.toDataDefinition() }
}

internal fun FunctionContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Expression {
    return when (this.functionName().text.uppercase(Locale.getDefault())) {
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

internal fun String.isDecimal() = this.toDoubleOrNull() != null

internal fun String.toDecimal() = this.toDouble()

internal fun String.isNumber() = this.isInt() || this.isDecimal()

internal fun Boolean.toInt() = if (this) 1 else 0

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
): NumberLiteral {
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
            value.toIntLiteral(position)
        }
    }
}

internal fun SymbolicConstantsContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Expression {
    val position = toPosition(conf.considerPosition)
    return when {
        this.SPLAT_HIVAL() != null -> HiValExpr(position)
        this.SPLAT_LOVAL() != null -> LowValExpr(position)
        this.SPLAT_START() != null -> StartValExpr(position)
        this.SPLAT_END() != null -> EndValExpr(position)
        this.SPLAT_BLANKS() != null -> BlanksRefExpr(position)
        this.SPLAT_ZEROS() != null -> ZeroExpr(position)
        this.SPLAT_OFF() != null -> OffRefExpr(position)
        this.SPLAT_ON() != null -> OnRefExpr(position)
        this.SPLAT_ISO() != null -> IsoFormatExpr(position)
        this.SPLAT_JUL() != null -> JulFormatExpr(position)
        this.SPLAT_INDICATOR() != null -> {
            IndicatorExpr(
                index = children[0].text.uppercase().replace("*IN", "").toIndicatorKey(),
                position = position
            )
        }
        this.SPLAT_ALL_INDICATORS() != null -> {
            GlobalIndicatorExpr(
                position = position
            )
        }
        this.SPLAT_ALL() != null -> {
            val content: LiteralContext = this.parent.getChild(1) as LiteralContext
            AllExpr(content.toAst(conf), position)
        }
        this.UDATE() != null -> UDateRefExpr(position)
        this.UYEAR() != null -> UYearRefExpr(position)
        this.UMONTH() != null -> UMonthRefExpr(position)
        this.UDAY() != null -> UDayRefExpr(position)
        else -> todo(conf = conf)
    }
}

internal fun Cspec_fixedContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Statement {
    return when {
        this.cspec_fixed_standard() != null ->
            // we need capture error inside runParserRuleContext in order
            // to avoid that some errors pass silently
            this.cspec_fixed_standard().runParserRuleContext(conf) { standardContext ->
                standardContext
                    .toAst(conf)
                    .also {
                        it.buildIndicatorsFlags(this, conf)
                    }
            }
        this.cspec_fixed_x2() != null ->
            this.cspec_fixed_x2().runParserRuleContext(conf) {
                it.toAst()
            }
        else -> todo(conf = conf)
    }
}

internal fun Cspec_fixed_standardContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Statement {
    return when {
        this.csEXSR() != null -> this.csEXSR()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csEVAL() != null -> this.csEVAL().toAst(conf)

        this.csCALL() != null -> this.csCALL()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csSETON() != null -> this.csSETON()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csSETOFF() != null -> this.csSETOFF()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csPLIST() != null -> this.csPLIST()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csCLEAR() != null -> this.csCLEAR()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csLEAVE() != null -> LeaveStmt(toPosition(conf.considerPosition))

        this.csLEAVESR() != null -> LeaveSrStmt(toPosition(conf.considerPosition))

        this.csITER() != null -> IterStmt(toPosition(conf.considerPosition))

        this.csOTHER() != null -> OtherStmt(toPosition(conf.considerPosition))

        this.csDSPLY() != null -> this.csDSPLY()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csMOVE() != null -> this.csMOVE()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csMOVEA() != null -> this.csMOVEA()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csMOVEL() != null -> this.csMOVEL()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csTIME() != null -> this.csTIME()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csSUBDUR() != null -> this.csSUBDUR()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csZ_ADD() != null -> this.csZ_ADD()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csADD() != null -> this.csADD()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csZ_SUB() != null -> this.csZ_SUB()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csSUB() != null -> this.csSUB()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csCHAIN() != null -> this.csCHAIN()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csCHECK() != null -> this.csCHECK()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csCHECKR() != null -> this.csCHECKR()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csKLIST() != null -> this.csKLIST()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csSETLL() != null -> this.csSETLL()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csSETGT() != null -> this.csSETGT()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csREAD() != null -> this.csREAD()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csREADP() != null -> this.csREADP()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csREADE() != null -> this.csREADE()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csREADPE() != null -> this.csREADPE()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csWRITE() != null -> this.csWRITE()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csUPDATE() != null -> this.csUPDATE()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csDELETE() != null -> this.csDELETE()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csCOMP() != null -> this.csCOMP()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csMULT() != null -> this.csMULT()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csDIV() != null -> this.csDIV()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csRETURN() != null -> this.csRETURN().toAst(conf)

        this.csTAG() != null -> this.csTAG()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csGOTO() != null -> this.csGOTO()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csSORTA() != null -> this.csSORTA().toAst(conf)

        this.csDEFINE() != null -> this.csDEFINE()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csCAT() != null -> this.csCAT()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csLOOKUP() != null -> this.csLOOKUP()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csCAB() != null -> this.csCAB()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csCABLE() != null -> this.csCABLE()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csCABLT() != null -> this.csCABLT()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csCABEQ() != null -> this.csCABEQ()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csCABNE() != null -> this.csCABNE()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csCABGE() != null -> this.csCABGE()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csCABGT() != null -> this.csCABGT()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csXFOOT() != null -> this.csXFOOT()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csSCAN() != null -> this.csSCAN()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csSUBST() != null -> this.csSUBST()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csOCCUR() != null -> this.csOCCUR()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csOPEN() != null -> this.csOPEN()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csCLOSE() != null -> this.csCLOSE()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csXLATE() != null -> this.csXLATE()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csRESET() != null -> this.csRESET()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csEXFMT() != null -> this.csEXFMT()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csREADC() != null -> this.csREADC()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csUNLOCK() != null -> this.csUNLOCK()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csEXCEPT() != null -> this.csEXCEPT()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csFEOD() != null -> this.csFEOD()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csBITON() != null -> this.csBITON()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csBITOFF() != null -> this.csBITOFF()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csTESTN() != null -> this.csTESTN()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        this.csDEALLOC() != null -> this.csDEALLOC()
            .let { it.cspec_fixed_standard_parts().validate(stmt = it.toAst(conf), conf = conf) }

        else -> todo(conf = conf)
    }
}

internal fun Cspec_fixed_standard_partsContext.validate(stmt: Statement, conf: ToAstConfiguration): Statement {
    val position = toPosition(conf.considerPosition)
    if (result.text.isNotBlank()) {
        toDataDefinition(result.text, position, conf)?.let {
            if (stmt !is StatementThatCanDefineData) result.todo(
                message = "${stmt::class.java.name} must implement ${StatementThatCanDefineData::class.java.name} ",
                conf = conf
            )
        }
    }
    return stmt
}

internal fun Cspec_fixed_sqlContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Statement {
    return when {
        this.CS_ExecSQL() != null -> ExecSqlStmt(toPosition(conf.considerPosition))
        this.CSQL_TEXT() != null -> CsqlTextStmt(toPosition(conf.considerPosition))
        this.CSQL_END() != null -> CsqlEndStmt(toPosition(conf.considerPosition))
        else -> todo(conf = conf)
    }
}

private fun annidatedReferenceExpression(
    text: String,
    position: Position?
): AssignableExpression {
    // FIXME: This is very, very, very ugly. It should be fixed by parsing this properly
    //        in the grammar
    if (text.uppercase(Locale.getDefault()) == "*IN") {
        return GlobalIndicatorExpr(position)
    }
    if (text.uppercase(Locale.getDefault()).startsWith("*IN(") && text.endsWith(")")) {
        val index = text.uppercase(Locale.getDefault()).removePrefix("*IN(").removeSuffix(")")
        return when {
            index.isInt() -> IndicatorExpr(index.toInt(), position)
            else -> IndicatorExpr(DataRefExpr(ReferenceByName(index), position), position)
        }
    }
    if (text.uppercase(Locale.getDefault()).startsWith("*IN")) {
        val index = text.uppercase(Locale.getDefault()).removePrefix("*IN")
        return IndicatorExpr(index.toIndicatorKey(), position)
    }

    var expr: Expression
    if (text.contains(".")) {
            val parts = text.split(".")
            require(parts.isNotEmpty())
            val varName = if (parts.size == 1) {
                parts[0]
            } else {
                parts.last()
            }
            expr = DataRefExpr(ReferenceByName(varName), position)
    } else {
        expr = text.indexOf("(").let {
            val varName = if (it == -1) text else text.substring(0, it)
            DataRefExpr(ReferenceByName(varName), position)
        }
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
        expr = ArrayAccessExpr(expr, indexExpression, position)
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

    val dataDefinitions = this.csKFLD().mapNotNull {
        val parts = it.cspec_fixed_standard_parts()
        val name = parts.result.text
        parts.toDataDefinition(name, position, conf)
    }

    return KListStmt(factor1, fields, dataDefinitions, position)
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

    return if (text.contains('.')) {
        handleParsingOfTargets(text, position)
    } else {
        annidatedReferenceExpression(text, position)
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
    var resultName = this.cspec_fixed_standard_parts().result.text
    if (resultName.contains(".")) {
        val parts = resultName.split(".")
        require(parts.isNotEmpty())
        resultName = parts.last()
    }
    val resultPosition = this.cspec_fixed_standard_parts().result.toPosition()

    val factor1Text = this.factor1.text.trim()
    val factor1Position = this.factor1.toPosition(conf.considerPosition)

    val factor1Expression = if (factor1Text.isNotEmpty()) annidatedReferenceExpression(factor1Text, factor1Position) else null
    val factor2Expression = this.cspec_fixed_standard_parts().factor2Expression(conf)

    /*
     * In accord to documentation (see https://www.ibm.com/docs/en/i/7.5?topic=codes-plist-identify-parameter-list):
     * - when `CALL` is processed, the content of Factor 2 is placed in the Result field. So. is considered Factor 2 value;
     * - when control transfers to called program, the contents of the Result field is placed in
     *    the Factor 1 field. So, is considered Result Value.
     */
    val initializationValue = if (this.parent is CsCALLContext && this.cspec_fixed_standard_parts().len.asInt() == null) {
        factor2Expression
    } else if (this.parent is CsPLISTContext && this.cspec_fixed_standard_parts().len.asInt() == null) {
        annidatedReferenceExpression(resultName, resultPosition)
    } else {
        null
    }

    val position = toPosition(conf.considerPosition)
    return PlistParam(
        factor1Expression,
        factor2Expression,
        ReferenceByName(resultName),
        this.cspec_fixed_standard_parts().toDataDefinition(
            resultName,
            position,
            conf
        ),
        position,
        initializationValue
    )
}

internal fun CsTIMEContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): TimeStmt {
    val name = this.cspec_fixed_standard_parts().result.text
    val position = toPosition(conf.considerPosition)

    val result = this.cspec_fixed_standard_parts().result
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(result.text, position, conf)

    return TimeStmt(annidatedReferenceExpression(name, toPosition(conf.considerPosition)), dataDefinition, position)
}

fun Cspec_fixed_standard_partsContext.factor2Expression(conf: ToAstConfiguration): Expression? {
    // Exception catching is wanted to not make blocking the creating ast exceptions
    factor2?.symbolicConstants()?.let {
        return kotlin.runCatching { it.toAst() }.getOrNull()
    }
    return kotlin.runCatching { factor2?.content?.toAst(conf) }.getOrNull()
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
    val len = this.len.asInt() ?: return null
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
            .map(String::uppercase)
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
    val extenders = this.operationExtender?.extender?.text?.uppercase(Locale.getDefault())?.toCharArray() ?: CharArray(0)
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
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(target.first(), position, conf)

    return SubDurStmt(left, DataRefExpr(ReferenceByName(target[0]), position), factor2, durationCode, dataDefinition, position)
}

private fun String.toDuration(): DurationCode =
    when (uppercase(Locale.getDefault())) {
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
    val hi = this.cspec_fixed_standard_parts().hi.toIndicatorKey()
    val lo = this.cspec_fixed_standard_parts().lo.toIndicatorKey()
    return ChainStmt(factor1, factor2, hi, lo, position)
}

internal fun CsREADContext.toAst(conf: ToAstConfiguration): Statement {
    val position = toPosition(conf.considerPosition)
    // TODO implement DS in result field
    val factor2 = this.cspec_fixed_standard_parts().factor2.text ?: throw UnsupportedOperationException("READ operation requires factor 2: ${this.text} - ${position.atLine()}")

    val loIndicator = this.cspec_fixed_standard_parts().lo.toIndicatorKey()
    val eqIndicator = this.cspec_fixed_standard_parts().eq.toIndicatorKey()
    return ReadStmt(factor2, loIndicator, eqIndicator, position)
}

internal fun CsREADPContext.toAst(conf: ToAstConfiguration): Statement {
    val position = toPosition(conf.considerPosition)
    // TODO implement DS in result field
    val factor2 = this.cspec_fixed_standard_parts().factor2.text ?: throw UnsupportedOperationException("READP operation requires factor 2: ${this.text} - ${position.atLine()}")

    val loIndicator = this.cspec_fixed_standard_parts().lo.toIndicatorKey()
    val eqIndicator = this.cspec_fixed_standard_parts().eq.toIndicatorKey()
    return ReadPreviousStmt(factor2, loIndicator, eqIndicator, position)
}

internal fun CsREADEContext.toAst(conf: ToAstConfiguration): Statement {
    val position = toPosition(conf.considerPosition)
    // TODO implement DS in result field
    val factor1 = this.factor1Context()?.content?.toAst(conf)
    val factor2 = this.cspec_fixed_standard_parts().factor2.text ?: throw UnsupportedOperationException("READE operation requires factor 2: ${this.text} - ${position.atLine()}")

    val lo = this.cspec_fixed_standard_parts().lo.toIndicatorKey()
    val eq = this.cspec_fixed_standard_parts().eq.toIndicatorKey()
    return ReadEqualStmt(factor1, factor2, lo, eq, position)
}

internal fun CsREADPEContext.toAst(conf: ToAstConfiguration): Statement {
    val position = toPosition(conf.considerPosition)
    // TODO implement DS in result field
    val factor1 = this.factor1Context()?.content?.toAst(conf)
    val factor2 = this.cspec_fixed_standard_parts().factor2.text ?: throw UnsupportedOperationException("READPE operation requires factor 2: ${this.text} - ${position.atLine()}")

    val lo = this.cspec_fixed_standard_parts().lo.toIndicatorKey()
    val eq = this.cspec_fixed_standard_parts().eq.toIndicatorKey()
    return ReadPreviousEqualStmt(factor1, factor2, lo, eq, position)
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

    val factor2 = this.cspec_fixed_standard_parts().factor2

    val result = this.cspec_fixed_standard_parts().result
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(result.text, position, conf)

    val rightIndicators = cspec_fixed_standard_parts().rightIndicators()
    val target = if (result.text.isNotBlank()) result.toAst(conf) else null

    val baseExpression = factor2.factorContent(0).toAst(conf)
    val positionExpression =
        if (factor2.factorContent().size > 1) {
            factor2.factorContent(1).toAst(conf)
        } else {
            null
        }

    return ScanStmt(
        left = compareExpression,
        leftLength = compareLength,
        right = baseExpression,
        startPosition = positionExpression,
        target = target,
        rightIndicators = rightIndicators,
        dataDefinition = dataDefinition,
        position = position
    )
}

internal fun CsCHECKContext.toAst(conf: ToAstConfiguration): Statement {
    val position = toPosition(conf.considerPosition)
    val factor1 = this.factor1Context()?.content?.toAst(conf) ?: throw UnsupportedOperationException("CHECK operation requires factor 1: ${this.text} - ${position.atLine()}")
    val (expression, startPosition) = this.cspec_fixed_standard_parts().factor2.toIndexedExpression(conf)

    val result = this.cspec_fixed_standard_parts().result
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(result.text, position, conf)

    val eqIndicator = this.cspec_fixed_standard_parts().resultIndicator(2)?.text

    val wrongCharExpression = when {
        result != null && result.text.isNotBlank() -> result.toAst(conf)
        !eqIndicator.isNullOrBlank() -> IndicatorExpr(eqIndicator.toIndicatorKey(), position)
        else -> null
    }

    return CheckStmt(
        factor1,
        expression,
        startPosition ?: 1,
        wrongCharExpression,
        dataDefinition,
        position
    )
}

internal fun CsCHECKRContext.toAst(conf: ToAstConfiguration): Statement {
    val position = toPosition(conf.considerPosition)
    val factor1 = this.factor1Context()?.content?.toAst(conf) ?: throw UnsupportedOperationException("CHECKR operation requires factor 1: ${this.text} - ${position.atLine()}")
    val (expression, startPosition) = this.cspec_fixed_standard_parts().factor2.toIndexedExpression(conf)

    val result = this.cspec_fixed_standard_parts().result
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(result.text, position, conf)

    val eqIndicator = this.cspec_fixed_standard_parts().resultIndicator(2)?.text

    val wrongCharExpression = when {
        result != null && result.text.isNotBlank() -> result.toAst(conf)
        !eqIndicator.isNullOrBlank() -> IndicatorExpr(eqIndicator.toIndicatorKey(), position)
        else -> null
    }

    return CheckrStmt(
        factor1,
        expression,
        startPosition ?: 1,
        wrongCharExpression,
        dataDefinition,
        position
    )
}

private fun FactorContext.toDoubleExpression(conf: ToAstConfiguration, index: Int): Expression =
    if (this.text.contains(":")) this.text.toDoubleExpression(toPosition(conf.considerPosition), index) else this.content.toAst(conf)

private fun String.toDoubleExpression(position: Position?, index: Int): Expression {
    val quoteAwareSplitPattern = Regex(""":(?=([^']*'[^']*')*[^']*$)""")
    val baseStringTokens = this.split(quoteAwareSplitPattern)
    var reference = baseStringTokens[index]

    val regexp = Regex("'(.*?)'")
    val ret = if (reference.matches(regexp)) {
        reference = reference.replace("'", "")
        StringLiteral(reference, position)
    } else {
        DataRefExpr(ReferenceByName(reference), position)
    }
    return ret
}

private fun FactorContext.toIndexedExpression(conf: ToAstConfiguration): Pair<Expression, Int?> =
    if (this.text.contains(":")) this.text.toIndexedExpression(toPosition(conf.considerPosition)) else this.content.toAst(conf) to null

private fun String.toIndexedExpression(position: Position?): Pair<Expression, Int?> {
    val quoteAwareSplitPattern = Regex(""":(?=([^']*'[^']*')*[^']*$)""")
    val baseStringTokens = this.split(quoteAwareSplitPattern)

    val startPosition =
        when (baseStringTokens.size) {
            !in 1..2 -> throw UnsupportedOperationException("Wrong base string expression at line ${position?.line()}: $this")
            2 -> baseStringTokens[1].toInt()
            else -> null
        }
    val reference = baseStringTokens[0]
    return when {
        reference.isStringLiteral() -> StringLiteral(reference.trim('\''), position)
        reference.contains('(') && reference.endsWith(")") -> {
            annidatedReferenceExpression(this, position)
        }
        else -> DataRefExpr(ReferenceByName(reference), position)
    } to startPosition
}

internal fun CsMOVEAContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): MoveAStmt {
    val position = toPosition(conf.considerPosition)
    val expression = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("MOVEA operation requires factor 2: ${this.text} - ${position.atLine()}")
    val resultExpression = this.cspec_fixed_standard_parts().resultExpression(conf) as AssignableExpression
    val result = this.cspec_fixed_standard_parts().result.text
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(result, position, conf)
    return MoveAStmt(
        operationExtender = this.operationExtender?.text,
        target = resultExpression,
        expression = expression,
        dataDefinition = dataDefinition,
        position = position)
}

internal fun CsMOVEContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): MoveStmt {
    val operationExtender = this.operationExtender?.text
    val position = toPosition(conf.considerPosition)
    val expression = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("MOVE operation requires factor 2: ${this.text} - ${position.atLine()}")
    val resultExpression = this.cspec_fixed_standard_parts().resultExpression(conf) as AssignableExpression
    val result = this.cspec_fixed_standard_parts().result.text
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(result, position, conf)
    return MoveStmt(
        operationExtender,
        target = resultExpression,
        expression = expression,
        dataDefinition = dataDefinition,
        position = position
    )
}

internal fun CsMOVELContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): MoveLStmt {
    val position = toPosition(conf.considerPosition)
    val expression = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("MOVE operation requires factor 2: ${this.text} - ${position.atLine()}")
    val resultExpression = this.cspec_fixed_standard_parts().resultExpression(conf) as AssignableExpression
    val result = this.cspec_fixed_standard_parts().result.text
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(result, position, conf)
    val dataAttributes = leftExpr(conf)
    return MoveLStmt(
        operationExtender = this.operationExtender?.text,
        target = resultExpression,
        dataDefinition = dataDefinition,
        value = expression,
        dataAttributes = dataAttributes,
        position = position
    )
}

internal fun CsZ_ADDContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): ZAddStmt {
    val position = toPosition(conf.considerPosition)
    val name = this.cspec_fixed_standard_parts().result.text
    val expression = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: this.todo(message = "Z-ADD operation requires factor 2: ${this.text} - ${position.atLine()}", conf)
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(name, position, conf)
    return ZAddStmt(this.cspec_fixed_standard_parts().result.toAst(conf), dataDefinition, expression, position)
}

internal fun CsMULTContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): MultStmt {
    val position = toPosition(conf.considerPosition)
    val result = this.cspec_fixed_standard_parts().result.text
    val factor1 = leftExpr(conf)
    val factor2 = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: this.todo(message = "SUB operation requires factor 2: ${this.text} - ${position.atLine()}", conf)
    this.cspec_fixed_standard_parts().toDataDefinition(result, position, conf)
    val extenders = this.operationExtender?.extender?.text?.uppercase(Locale.getDefault())?.toCharArray() ?: CharArray(0)
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(result, position, conf)
    return MultStmt(
        target = this.cspec_fixed_standard_parts().result.toAst(conf),
        halfAdjust = 'H' in extenders,
        factor1 = factor1,
        factor2 = factor2,
        dataDefinition = dataDefinition,
        position = position
    )
}

internal fun CsDIVContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): DivStmt {
    val position = toPosition(conf.considerPosition)
    val result = this.cspec_fixed_standard_parts().result.text
    val factor1 = leftExpr(conf)
    val factor2 = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: this.todo(message = "SUB operation requires factor 2: ${this.text} - ${position.atLine()}", conf)
    this.cspec_fixed_standard_parts().toDataDefinition(result, position, conf)
    val extenders = this.operationExtender?.extender?.text?.uppercase(Locale.getDefault())?.toCharArray() ?: CharArray(0)
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(result, position, conf)
    return DivStmt(
        target = this.cspec_fixed_standard_parts().result.toAst(conf),
        halfAdjust = 'H' in extenders,
        factor1 = factor1,
        factor2 = factor2,
        mvrStatement = this.csMVR()?.toAst(conf),
        dataDefinition = dataDefinition,
        position = position
    )
}

internal fun CsMVRContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): MvrStmt {
    val result = this.cspec_fixed_standard_parts().result.text
    val target = result?.let {
        DataRefExpr(ReferenceByName(it), toPosition(conf.considerPosition))
    }
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(
        name = result,
        position = toPosition(conf.considerPosition),
        conf = conf
    )
    return MvrStmt(
        target = target,
        dataDefinition = dataDefinition
    )
}

internal fun CsTAGContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): TagStmt {
    return TagStmt(this.factor1Context()?.content?.text!!, toPosition(conf.considerPosition))
}

/**
 * If FactorContext contains symbolicConstants convert it to expression
 * @return When possible an instance of Expression achieved by symbolicConstants
 * */
internal fun FactorContext.toAstIfSymbolicConstant(): Expression? {
    // Exception catching is wanted to not make blocking the creating ast exceptions
    return this.symbolicConstants()?.let { kotlin.runCatching { it.toAst() }.getOrNull() }
}

private fun ParserRuleContext.leftExpr(conf: ToAstConfiguration): Expression? {
    this.factor1Context().toAstIfSymbolicConstant()?.let {
        return it
    }
    return if (this.factor1Context()?.content?.text?.isNotBlank() == true) {
        // Exception catching is wanted to not make blocking the creating ast exceptions
        kotlin.runCatching { this.factor1Context().content.toAst(conf) }.getOrNull()
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

internal fun CsCABNEContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): CabStmt {
    return cabStatement(ComparisonOperator.NE, this.cspec_fixed_standard_parts(), conf)
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
    val right = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: this.todo(message = "ADD operation requires factor 2: ${this.text} - ${position.atLine()}", conf)
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(result, position, conf)
    return AddStmt(left, this.cspec_fixed_standard_parts().result.toAst(conf), dataDefinition, right, position)
}

internal fun CsZ_SUBContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): ZSubStmt {
    val position = toPosition(conf.considerPosition)
    val expression = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: this.todo(message = "Z-SUB operation requires factor 2: ${this.text} - ${position.atLine()}", conf)
    val name = this.cspec_fixed_standard_parts().result.text
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(name, position, conf)
    return ZSubStmt(this.cspec_fixed_standard_parts().result.toAst(conf), dataDefinition, expression, position)
}

internal fun CsSUBContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): SubStmt {
    val position = toPosition(conf.considerPosition)
    val result = this.cspec_fixed_standard_parts().result.text
    val left = leftExpr(conf)
    val right = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: this.todo(message = "SUB operation requires factor 2: ${this.text} - ${position.atLine()}", conf)
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(result, position, conf)
    return SubStmt(left, this.cspec_fixed_standard_parts().result.toAst(conf), dataDefinition, right, position)
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
        is SubarrTargetContext -> this.bif_subarr().toAst(conf)
        is LenTargetContext -> this.bif_len().toAst(conf)
        is QualifiedTargetContext -> QualifiedAccessExpr(
            DataRefExpr(ReferenceByName(this.container.text), this.container!!.toPosition(conf.considerPosition)),
            ReferenceByName(this.getFieldName()),
            toPosition(conf.considerPosition)
        )
        is IndicatorTargetContext -> IndicatorExpr(
            this.indic.text.indicatorIndex()!!,
            toPosition(conf.considerPosition)
        )
        is IndexedIndicatorTargetContext -> {
            val index: Expression = this.index.toAst(conf = conf)
            return IndicatorExpr(index = index, position = toPosition(conf.considerPosition))
        }
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
    val operationExtender = this.operationExtender?.text
    val position = toPosition(conf.considerPosition)
    val left = leftExpr(conf)
    val right = this.cspec_fixed_standard_parts().factor2Expression(conf) ?: throw UnsupportedOperationException("COMP operation requires factor 2: ${this.text} - ${position.atLine()}")

    val blanksInBetweenExpression: Expression? =
        if (this.cspec_fixed_standard_parts().factor2.factorContent().size > 1) {
            this.cspec_fixed_standard_parts().factor2.factorContent(1).toAst(conf)
        } else {
            null
        }

    val target = this.cspec_fixed_standard_parts().resultExpression(conf) as AssignableExpression
    val result = this.cspec_fixed_standard_parts().result.text
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(result, position, conf)
    return CatStmt(
        left = left,
        right = right,
        target = target,
        blanksInBetween = blanksInBetweenExpression,
        position = position,
        dataDefinition = dataDefinition,
        operationExtender = operationExtender
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

internal fun CsSUBSTContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): SubstStmt {
    val position = toPosition(conf.considerPosition)

    // Left expression contain length
    val length = leftExpr(conf)

    // factor2 is formed by TEXT:B
    // where "TEXT" is the content to be substringed
    val stringExpression = this.cspec_fixed_standard_parts().factor2.factorContent(0).toAst(conf)
    // and  "B" is the start position to substring, if not specified it returns null
    val positionExpression =
        if (this.cspec_fixed_standard_parts().factor2.factorContent().size > 1) {
            this.cspec_fixed_standard_parts().factor2.factorContent(1).toAst(conf)
        } else {
            null
        }

    val result = this.cspec_fixed_standard_parts().result.text
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(result, position, conf)

    return SubstStmt(
        length = length,
        value = stringExpression,
        startPosition = positionExpression,
        target = this.cspec_fixed_standard_parts()!!.result!!.toAst(conf),
        operationExtender = this.operationExtender?.text,
        position = position,
        dataDefinition = dataDefinition
    )
}

internal fun CsOCCURContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): OccurStmt {

    val position = toPosition(conf.considerPosition)
    val result = this.cspec_fixed_standard_parts().result.text
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(result, position, conf)
    val resultExpression = if (!result.isBlank()) this.cspec_fixed_standard_parts().result.toAst(conf) else null

    return OccurStmt(
        occurenceValue = leftExpr(conf),
        dataStructure = this.cspec_fixed_standard_parts().factor2.text,
        result = resultExpression,
        operationExtender = this.operationExtender?.text,
        position = position,
        dataDefinition = dataDefinition,
        errorIndicator = this.cspec_fixed_standard_parts().lo.asIndex()
    )
}

internal fun CsXLATEContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): XlateStmt {
    val position = toPosition(conf.considerPosition)
    val from = this.factor1Context().toDoubleExpression(conf, 0)
    val to = this.factor1Context().toDoubleExpression(conf, 1)
    val (string, startPosition) = this.cspec_fixed_standard_parts().factor2.toIndexedExpression(conf)
    val rightIndicators = cspec_fixed_standard_parts().rightIndicators()
    val result = this.cspec_fixed_standard_parts().result.text
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(result, position, conf)
    return XlateStmt(
        from = from,
        to = to,
        string = string,
        startPos = startPosition ?: 1,
        target = this.cspec_fixed_standard_parts()!!.result!!.toAst(conf),
        rightIndicators = rightIndicators,
        dataDefinition = dataDefinition,
        position = position
    )
}

internal fun CsOPENContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Statement {
    val position = toPosition(conf.considerPosition)
    val factor2 = this.cspec_fixed_standard_parts().factor2.text ?: throw UnsupportedOperationException("READ operation requires factor 2: ${this.text} - ${position.atLine()}")
    return OpenStmt(
        name = factor2,
        position = position,
        operationExtender = this.operationExtender?.text,
        errorIndicator = this.cspec_fixed_standard_parts().lo.asIndex()
    )
}

internal fun CsCLOSEContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Statement {
    val position = toPosition(conf.considerPosition)
    val factor2 = this.cspec_fixed_standard_parts().factor2.text ?: throw UnsupportedOperationException("READ operation requires factor 2: ${this.text} - ${position.atLine()}")
    return CloseStmt(
        name = factor2,
        position = position,
        operationExtender = this.operationExtender?.text,
        errorIndicator = this.cspec_fixed_standard_parts().lo.asIndex()
    )
}

internal fun CsRESETContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Statement {
    val position = toPosition(conf.considerPosition)

    require(this.cspec_fixed_standard_parts().factor().text.isEmptyTrim()) {
        "RESET operation does not support factor1"
    }
    require(this.cspec_fixed_standard_parts().factor2.text.isEmptyTrim()) {
        "RESET operation does not support factor2"
    }
    val result = this.cspec_fixed_standard_parts().result.text
    require(!result.isEmptyTrim()) {
        "RESET operation requires result"
    }

    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(result, position, conf)

    return ResetStmt(
        name = result,
        dataDefinition = dataDefinition,
        position = position
    )
}

// TODO
internal fun CsEXFMTContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Statement {
    val position = toPosition(conf.considerPosition)
    val factor2 = this.cspec_fixed_standard_parts().factor2.text ?: throw UnsupportedOperationException("Line ${position?.line()}: EXFMT operation requires factor 2: ${this.text}")
    return ExfmtStmt(position, factor2)
}

// TODO
internal fun CsREADCContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Statement {
    val position = toPosition(conf.considerPosition)
    return ReadcStmt(position)
}

// TODO
internal fun CsUNLOCKContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Statement {
    val position = toPosition(conf.considerPosition)
    return UnlockStmt(position)
}

// TODO
internal fun CsEXCEPTContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Statement {
    val position = toPosition(conf.considerPosition)
    return ExceptStmt(position)
}

internal fun CsTESTNContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): TestnStmt {
    val position = toPosition(conf.considerPosition)
    val resultExpression = this.cspec_fixed_standard_parts().resultExpression(conf) as AssignableExpression
    val result = this.cspec_fixed_standard_parts().result.text
    val dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(result, position, conf)
    return TestnStmt(resultExpression, dataDefinition, cspec_fixed_standard_parts().rightIndicators(), position)
}

// TODO
internal fun CsFEODContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Statement {
    val position = toPosition(conf.considerPosition)
    return FeodStmt(position)
}

// TODO
internal fun CsBITONContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Statement {
    val position = toPosition(conf.considerPosition)
    return BitOnStmt(
        dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(this.cspec_fixed_standard_parts().result.text, position, conf),
        position = position
    )
}

// TODO
internal fun CsBITOFFContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Statement {
    val position = toPosition(conf.considerPosition)
    return BitOffStmt(
        dataDefinition = this.cspec_fixed_standard_parts().toDataDefinition(this.cspec_fixed_standard_parts().result.text, position, conf),
        position = position
    )
}

internal fun CsDEALLOCContext.toAst(conf: ToAstConfiguration = ToAstConfiguration()): Statement {
    val position = toPosition(conf.considerPosition)
    return DeallocStmt(position = position)
}

/**
 * Run a block. In case of error throws an error encapsulating useful information
 * like node position
 */
fun <T : ParserRuleContext, R> T.runParserRuleContext(conf: ToAstConfiguration, block: (T) -> R): R {
    return kotlin.runCatching {
        block.invoke(this)
    }.onFailure {
        // to avoid duplicated errors
        if (it !is ParseTreeToAstError) this.error(it.message, it, conf)
    }.getOrThrow()
}

/**
 * Run a block related AST node. In case of error throws an error encapsulating useful information
 * like node position
 */
fun <T : Node, R> T.runNode(block: (T) -> R): R {
    return kotlin.runCatching {
        block.invoke(this)
    }.onFailure {
        // to avoid duplicated errors
        if (it !is AstResolutionError) this.error(it.message, it)
    }.getOrThrow()
}

private typealias ProgramNameToCopyBlocks = Pair<String?, CopyBlocks?>

internal fun getProgramNameToCopyBlocks(): ProgramNameToCopyBlocks {
    val programName = if (MainExecutionContext.getParsingProgramStack().empty()) null else MainExecutionContext.getParsingProgramStack().peek().name
    val copyBlocks = programName?.let { MainExecutionContext.getParsingProgramStack().peek().copyBlocks }
    return ProgramNameToCopyBlocks(programName, copyBlocks)
}

internal fun <T : AbstractDataDefinition> List<T>.removeDuplicatedDataDefinition(): List<T> {
    // NOTE: With current logic when type matches on duplications the first definition wins
    val dataDefinitionMap = mutableMapOf<String, AbstractDataDefinition>()
    return removeUnnecessaryRecordFormat().filter {
        val dataDefinition = dataDefinitionMap[it.name]
        if (dataDefinition == null) {
            dataDefinitionMap[it.name] = it
            true
        } else {
            it.require(it.matchType(dataDefinition)) {
                "Incongruous definitions of ${it.name}: ${dataDefinition.type} vs ${it.type}"
            }
            false
        }
    }
}

internal fun AbstractDataDefinition.matchType(dataDefinition: AbstractDataDefinition): Boolean {
    fun Type.matchType(other: Any?): Boolean {
        // TODO: Improve logic for StringType/UnlimitedStringType matching
        return when {
            this is NumberType && other is NumberType -> {
                val resultDigits = this.entireDigits == other.entireDigits && this.decimalDigits == other.decimalDigits
                if (rpgType?.isNotBlank()!! && other.rpgType?.isNotEmpty()!!) {
                    return resultDigits && rpgType == other.rpgType
                }
                resultDigits
            }
            this is UnlimitedStringType && other is StringType -> true
            else -> this == other
        }
    }

    return when {
        dataDefinition.type.matchType(this.type) -> true
        dataDefinition.elementSize() == this.elementSize() -> {
            dataDefinition.type is StringType && this.type is DataStructureType ||
            dataDefinition.type is DataStructureType && this.type is StringType ||
            dataDefinition.type is BooleanType && this.type is StringType ||
            dataDefinition.type is StringType && this.type is BooleanType
        }
        else -> false
    }
}

/**
  * This function is used to remove unnecessary record formats from a list of data definitions.
  * Data definitions of type RecordFormatType are necessary where in the AST we don't have a DS
  * named like the record format, and we have operations such as CLEAR RECORD_FORMAT_NAME.
  * In that case the record format in the data definitions list is necessary just to retrieve the fields that must be clean.
  *
  * @return A list of data definitions excluding the ones of type `RecordFormat`.
 */
private fun <T : AbstractDataDefinition> List<T>.removeUnnecessaryRecordFormat(): List<T> {
    return this.filterNot { dataDef ->
        dataDef.type is RecordFormatType && this.any { it.type is DataStructureType && it.name.uppercase() == dataDef.name.uppercase() }
    }
}

private fun List<DataDefinition>.renameFields(externalFieldSpecs: List<ExternalFieldInputSpecification>): List<DataDefinition> {
    return this.map {
        val match = externalFieldSpecs.find { spec -> spec.originalName == it.name }
        match ?: return@map it
        it.copy(name = match.newName)
    }
}

private fun List<InputSpecification>.grouped(): List<InputSpecificationGroup> {
    if (this.isEmpty()) return emptyList()
    val files = this.withIndex().filter { it.value is FileNameInputSpecification }
    val output: MutableList<InputSpecificationGroup> = ArrayList<InputSpecificationGroup>(files.size)

    var lastFile = files.first()
    fun addGroup(index: Int) {
        val nestedSpecifications = this.slice(lastFile.index + 1 until index)
        val group = InputSpecificationGroup(lastFile.value as FileNameInputSpecification, nestedSpecifications)
        output.add(group)
    }

    files.slice(1..files.lastIndex).forEach {
        addGroup(it.index)
        lastFile = it
    }

    // Last pass if necessary
    if (output.size < files.size) addGroup(this.size)

    return output
}

private fun Map<FileDefinition, List<DataDefinition>>.processWithSpecifications(specifications: List<InputSpecificationGroup>): Map<FileDefinition, List<DataDefinition>> {
    val buffer = this.toMutableMap()
    specifications.forEach {
        val target = buffer.entries.firstOrNull { entry -> (entry.key.internalFormatName ?: entry.key.name) == it.fileName.name }
        target ?: error("No FileDefinition named ${it.fileName.name}")
        val externalFieldSpecifications = it.specifications.filterIsInstance<ExternalFieldInputSpecification>()
        buffer[target.key] = target.value.renameFields(externalFieldSpecifications)
    }
    return buffer
}

private fun String.isStringLiteral(): Boolean = startsWith('\'') && endsWith('\'')

private fun ResultIndicatorContext.toIndicatorKey() = text.trim().takeIf { it.isNotBlank() }?.toIndicatorKey()
