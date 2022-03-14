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

package com.smeup.rpgparser.execution

import com.smeup.dbnative.DBNativeAccessConfig
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.parsing.facade.CopyId
import com.smeup.rpgparser.parsing.facade.SourceReference
import com.smeup.rpgparser.parsing.parsetreetoast.ToAstConfiguration
import java.io.File

const val DEFAULT_ACTIVATION_GROUP_NAME: String = "*DFTACTGRP"

/**
 * Configuration object
 * @param memorySliceStorage Allows to implement a symbol table storaging.
 * If null, symbol table persistence will be skipped
 * @param jarikoCallback Several callback.
 * @param defaultActivationGroupName Default activation group. If not specified it assumes "*DEFACTGRP"
 * */

data class Configuration(
    val memorySliceStorage: IMemorySliceStorage? = null,
    var jarikoCallback: JarikoCallback = JarikoCallback(),
    var reloadConfig: ReloadConfig? = null,
    val defaultActivationGroupName: String = DEFAULT_ACTIVATION_GROUP_NAME,
    var options: Options? = Options()
) {
    constructor(memorySliceStorage: IMemorySliceStorage?) :
            this(memorySliceStorage, JarikoCallback(), null, DEFAULT_ACTIVATION_GROUP_NAME, Options())

    constructor(memorySliceStorage: IMemorySliceStorage?, defaultActivationGroupName: String) :
            this(memorySliceStorage, JarikoCallback(), null, defaultActivationGroupName, Options())
}

/**
 * Reload configuration
 * @param nativeAccessConfig DB Native Accesso config
 * @param metadataProducer Produce metadata for a dbFile, if returns null, FileMetadata are searched using default lookup method
 * provided by reload
 * */
data class ReloadConfig(
    val nativeAccessConfig: DBNativeAccessConfig,
    val metadataProducer: (dbFile: String) -> FileMetadata
)

/**
 * Options object
 * @param muteSupport Used to enable/disable scan execution of mute annotations into rpg sources)
 * @param compiledProgramsDir If specified Jariko searches compiled program in this directory
 * @param muteVerbose If true increases mute logging granularity
 * @param toAstConfiguration Creating ast configuration
 * @param callProgramHandler If specified allows to override program call handling logic.
 * @param dumpSourceOnExecutionError If true, program source is dumped on execution error. Default false.
 * @param debuggingInformation If true add debugging information, for example program source will be dumped in case of errors.
 * Setting this property to true causes a little overhead in AST serialization and deserialization due the fact
 * the source is CompilationUnit property
 * */
data class Options(
    var muteSupport: Boolean = false,
    var compiledProgramsDir: File? = null,
    var muteVerbose: Boolean = false,
    var toAstConfiguration: ToAstConfiguration = ToAstConfiguration(),
    var callProgramHandler: CallProgramHandler? = null,
    @Deprecated(replaceWith = ReplaceWith(expression = "debuggingInformation"), message = "This property will be deleted in the next releases")
    var dumpSourceOnExecutionError: Boolean? = false,
    var debuggingInformation: Boolean? = false
) {
    internal fun mustDumpSource() = dumpSourceOnExecutionError == true || debuggingInformation == true
    internal fun mustCreateCopyBlocks() = debuggingInformation == true
    internal fun mustInvokeOnStatementCallback() = debuggingInformation == true
}

/**
 * Sometimes we have to gain control of Jariko, this is the right place.
 * It was primarily intended for future use.
 * @param getActivationGroup If specified, it overrides the activation group associated to the program.
 * Default null it means by Configuration.
 * Parameter programName is program for which we are getting activation group, associatedActivationGroup is the current
 * activation group associated to the program.
 * @param exitInRT If specified, it overrides the exit mode established in program. Default null (nei seton rt od lr mode)
 * @param onEnterPgm It is invoked on program enter after symboltable initialization.
 * @param onExitPgm It is invoked on program exit. In case of error it is no longer called, then even error parameter is no longer significant
 * @param afterAstCreation It is invoked after ast creation
 * @param onEnterCopy It is invoked on copy enter.
 * **This callback will be called only if [Options.debuggingInformation] is set to true**.
 * @param onEnterCopy It is invoked on copy exit.
 * **This callback will be called only if [Options.debuggingInformation] is set to true**.
 * @param onEnterStatement It is invoked before statement execution.
 * **This callback will be called only if [Options.debuggingInformation] is set to true**.
 * See [JarikoCallback.onEnterStatement] for further information
 * @param onEnterFunction It is invoked on function enter after symboltable initialization.
 * @param onExitFunction It is invoked on function exit, only if the function does not throw any error
 * */
data class JarikoCallback(
    var getActivationGroup: (programName: String, associatedActivationGroup: ActivationGroup?) -> ActivationGroup? = { _: String, _: ActivationGroup? ->
            null
    },
    var exitInRT: (programName: String) -> Boolean? = { null },
    var onEnterPgm: (programName: String, symbolTable: ISymbolTable) -> Unit = { _: String, _: ISymbolTable -> },
    var onExitPgm: (programName: String, symbolTable: ISymbolTable, error: Throwable?) -> Unit = { _: String, _: ISymbolTable, _: Throwable? -> },
    var afterAstCreation: (ast: CompilationUnit) -> Unit = { },
    var onEnterCopy: (copyId: CopyId) -> Unit = { },
    var onExitCopy: (copyId: CopyId) -> Unit = { },
    /**
     * @param lineNumber is the absolute position of the statement in the post-processed program.
     * In case of programs with copy, the absolute position usually is different from the position of the statement
     * inside the source.
     * The position of the statement inside the source is accessible through sourceReference parameter.
     * @param sourceReference The source type where the statement is
     * */
    var onEnterStatement: (lineNumber: Int, sourceReference: SourceReference) -> Unit = { _: Int, _: SourceReference -> },
    var onEnterFunction: (functionName: String, params: List<FunctionValue>, symbolTable: ISymbolTable)
    -> Unit = { _: String, _: List<FunctionValue>, _: ISymbolTable -> },
    var onExitFunction: (functionName: String, params: List<FunctionValue>, symbolTable: ISymbolTable, returnValue: Value)
    -> Unit = { _: String, _: List<FunctionValue>, _: ISymbolTable, _: Value -> }
)

/**
 * Through this class, we can customize program calling. Typical scenario is program calling "out of process".
 * @param handleCall Handles programName calling. Returns null to preserve jariko default call program handling
 * */
data class CallProgramHandler(
    val handleCall: (programName: String, systemInterface: SystemInterface, params: LinkedHashMap<String, Value>) -> List<Value>?
)