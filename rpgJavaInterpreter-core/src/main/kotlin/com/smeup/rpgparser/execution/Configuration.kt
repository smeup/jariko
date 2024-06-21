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
import com.smeup.dspfparser.linesclassifier.DSPF
import com.smeup.dspfparser.linesclassifier.DSPFField
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.parsing.facade.CopyBlocks
import com.smeup.rpgparser.parsing.facade.CopyId
import com.smeup.rpgparser.parsing.facade.SourceReference
import com.smeup.rpgparser.parsing.parsetreetoast.ToAstConfiguration
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import java.io.File

const val DEFAULT_ACTIVATION_GROUP_NAME: String = "*DFTACTGRP"

/**
 * Configuration object
 * @param memorySliceStorage Allows to implement a symbol table storaging.
 * If null, symbol table persistence will be skipped
 * @param jarikoCallback Several callback.
 * @param reloadConfig Reload configuration, it is mandatory for rpgle programs containing RLA operations
 * @param dspfConfig DSPF parser configuration, if null metadata information related to display files will be loaded
 * from reloadConfig
 * @param defaultActivationGroupName Default activation group. If not specified it assumes "*DEFACTGRP"
 * */

data class Configuration(
    val memorySliceStorage: IMemorySliceStorage? = null,
    var jarikoCallback: JarikoCallback = JarikoCallback(),
    var reloadConfig: ReloadConfig? = null,
    var dspfConfig: DspfConfig? = null,
    var snapshotConfig: SnapshotConfig? = null,
    val defaultActivationGroupName: String = DEFAULT_ACTIVATION_GROUP_NAME,
    var options: Options = Options()
) {
    constructor(memorySliceStorage: IMemorySliceStorage?) :
            this(memorySliceStorage, JarikoCallback(), null, null, null, DEFAULT_ACTIVATION_GROUP_NAME, Options())

    constructor(memorySliceStorage: IMemorySliceStorage?, defaultActivationGroupName: String) :
            this(memorySliceStorage, JarikoCallback(), null, null, null, defaultActivationGroupName, Options())
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
 * DSPF parser configuration
 * @param metadataProducer Produce metadata for a displayFile
 * */
data class DspfConfig(
    val metadataProducer: (displayFile: String) -> FileMetadata,
    val dspfProducer: (displayFile: String) -> DSPF
)

/**
 * Snapshot configuration
 * @param save saves snapshot
 * */
data class SnapshotConfig(
    val save: (snapshotFile: String, interpreter: InterpreterCore) -> Unit
)

/**
 * Options object
 * @param muteSupport Used to enable/disable scan execution of mute annotations into rpg sources
 * @param compiledProgramsDir If specified Jariko searches compiled program in this directory.
 * This property should be used just for debug, because in production environment, in which the compiled programs
 * could be found in different paths, it is preferable to use a program finder for every path
 * @param muteVerbose If true increases mute logging granularity
 * @param toAstConfiguration Creating ast configuration
 * @param callProgramHandler If specified allows to override program call handling logic.
 * @param dumpSourceOnExecutionError If true, program source is dumped on execution error. Default false.
 * @param debuggingInformation If true, adds debugging information. Default false.
 * This property is necessary to enable some features useful when jariko must be debugged, for example some callback functions
 * such as onEnter and onExit copies or statements, just for performance reasons, will be invoked only when this property
 * is true.
 * */
data class Options(
    var muteSupport: Boolean = false,
    var compiledProgramsDir: File? = null,
    var muteVerbose: Boolean = false,
    var toAstConfiguration: ToAstConfiguration = ToAstConfiguration(),
    var callProgramHandler: CallProgramHandler? = null,
    var dumpSourceOnExecutionError: Boolean? = false,
    var debuggingInformation: Boolean? = false
) {
    internal fun mustDumpSource() = dumpSourceOnExecutionError == true
    internal fun mustCreateCopyBlocks() = debuggingInformation == true
    internal fun mustInvokeOnStatementCallback() = debuggingInformation == true
}

/**
 * Through this class, we can customize Jariko behavior.
 * */
data class JarikoCallback(
    /**
     * If specified, it overrides the activation group associated to the program.
     * Default null it means by Configuration.
     * Parameter programName is a program for which we are getting activation group, associatedActivationGroup is the current
     * activation group associated with the program.
     * */
    var getActivationGroup: (programName: String, associatedActivationGroup: ActivationGroup?) -> ActivationGroup? = { _: String, _: ActivationGroup? ->
            null
    },

    /**
     * It is invoked before than the copy is included in the source, the default implementation
     * will return the copy source itself
     */
    var beforeCopyInclusion: (copyId: CopyId, source: String?) -> String? = { _, source -> source },

    /**
     * It is invoked after that all copies has been included in the source.
     * **This callback will be called only if [Options.debuggingInformation] is set to true**.
     * */
    var afterCopiesInclusion: (copyBlocks: CopyBlocks) -> Unit = { },

    /**
     * It is invoked before the parsing.
     * It is passed the source that will be parsed after all copy inclusions, the default implementation
     * will return the source itself
     * */
    var beforeParsing: (source: String) -> String = { source -> source },

    /**
     * If specified, it overrides the exit mode established in the program. Default null to preserve default behavior.
     * */
    var exitInRT: (programName: String) -> Boolean? = { null },

    /**
     * It is invoked on Interpreter creation
     * */
    var onInterpreterCreation: (interpreter: InterpreterCore) -> Unit = { },

    /**
     * It is invoked on program entered after symbol table initialization.
     * */
    var onEnterPgm: (programName: String, symbolTable: ISymbolTable) -> Unit = { _: String, _: ISymbolTable -> },

    /**
     * It is invoked on program exit.
     * In case of error it is no longer called, then even error parameter is no longer significant
     * */
    var onExitPgm: (programName: String, symbolTable: ISymbolTable, error: Throwable?) -> Unit = { _: String, _: ISymbolTable, _: Throwable? -> },

    /**
     * It is invoked after ast creation.
     * */
    var afterAstCreation: (ast: CompilationUnit) -> Unit = { },

    /**
     * It is invoked on copy entered.
     * **This callback will be called only if [Options.debuggingInformation] is set to true**.
     * */
    var onEnterCopy: (copyId: CopyId) -> Unit = { },

    /**
     * It is invoked on copy exited.
     * **This callback will be called only if [Options.debuggingInformation] is set to true**.
     * */
    var onExitCopy: (copyId: CopyId) -> Unit = { },

    /**
     * It is invoked on EXFMT execution.
     */
    var onExfmt: (fields: List<DSPFField>, runtimeInterpreterSnapshot: RuntimeInterpreterSnapshot) -> OnExfmtResponse? = {
        _, _ -> null
    },

    /**
     * It is invoked before statement execution.
     * **This callback will be called only if [Options.debuggingInformation] is set to true**.
     * @see [JarikoCallback.onEnterStatement] for further information
     * @param absoluteLine is the absolute position of the statement in the post-processed program.
     * In the case of programs with copy, the absolute position usually is different from the position of the statement
     * inside the source.
     * @param sourceReference The position of the statement inside the source is accessible through sourceReference parameter.
     * sourceReference The source type where the statement is
     * */
    var onEnterStatement: (absoluteLine: Int, sourceReference: SourceReference) -> Unit = { _: Int, _: SourceReference -> },

    /**
     * It is invoked on function entered after symbol table initialization.
     * */
    var onEnterFunction: (functionName: String, params: List<FunctionValue>, symbolTable: ISymbolTable)
    -> Unit = { _: String, _: List<FunctionValue>, _: ISymbolTable -> },

    /**
     * It is invoked on function exit, only if the function does not throw any error
     * */
    var onExitFunction: (functionName: String, returnValue: Value) -> Unit = { _: String, _: Value -> },

    /**
     * It is invoked in case of errors. The default implementation writes error event in stderr
     * */
    var onError: (errorEvent: ErrorEvent) -> Unit = { errorEvent ->
        // If SystemInterface is not in the main execution context or in the SystemInterface there is no
        // logging configuration, the error event must be shown as before, else we run the risk to miss very helpful information
        MainExecutionContext.getSystemInterface()?.apply {
            if (MainExecutionContext.isErrorChannelConfigured) {
                MainExecutionContext.log(LazyLogEntry.produceError(errorEvent))
            } else {
                System.err.println(errorEvent)
            }
        } ?: System.err.println(errorEvent)
    },

    /***
     * It is invoked in case of runtime errors occurred inside the program called, only if the error indicator
     * at column 73-74 is specified.
     * The default implementation does nothing
     */
    var onCallPgmError: (errorEvent: ErrorEvent) -> Unit = { },

    /**
     * If specified, it is invoked to log information messages, for all enabled channels
     * */
    var logInfo: ((channel: String, message: String) -> Unit)? = null,

    /**
     * If specified, it allows to enable programmatically the channel logging.
     * For instance, you can enable all channels by using [consoleVerboseConfiguration] but you can decide, through
     * the implementation of this callback, which channel you want to log.
     * */
    var channelLoggingEnabled: ((channel: String) -> Boolean)? = null,

    /**
     * If specified, it allows customizing the behavior of the mock statements.
     * Default implementation provides a simple println with the name of the mock statement.
     * @param mockStatement The mock statement
     */
    var onMockStatement: ((mockStatement: MockStatement) -> Unit) = { System.err.println("Executing mock statement: ${it.javaClass.simpleName}") },

    /**
     * If specified, it allows customizing the behavior of the mock statements.
     * Default implementation provides a simple println with the name of the mock expression.
     * @param mockExpression The mock expression
     */
    var onMockExpression: ((mockExpression: MockExpression) -> Unit) = { System.err.println("Executing mock expression: ${it.javaClass.simpleName}") },

    /**
     * If specified, it allows overriding the default mechanism of API validation.
     * It is called before the Api is included in the main program.
     * Default implementation resolves and validates the compilationUnit related the API.
     * @param apiId The id of the API
     * @param api The API to include
     * */
    var onApiInclusion: ((apiId: ApiId, api: Api) -> Unit) = { _, api ->
        api.compilationUnit.resolveAndValidate()
    },

    /**
     * It allows overriding the feature flag check.
     * @param featureFlag The feature flag
     * @return true if the feature flag is on, false otherwise - default implementation returns the feature flag default value
     * @see FeatureFlag.on
     * */
    var featureFlagIsOn: ((featureFlag: FeatureFlag) -> Boolean) = { featureFlag -> featureFlag.on }
)

/**
 * Through this class, we can customize program calling. Typical scenario is program calling "out of process".
 * @param handleCall Handles programName calling. Returns null to preserve jariko default call program handling
 * */
data class CallProgramHandler(
    val handleCall: (programName: String, systemInterface: SystemInterface, params: LinkedHashMap<String, Value>) -> List<Value>?
)

/**
 * This class models an error event
 * @param error The error
 * @param errorEventSource The source of event
 * @param absoluteLine The line number of post processed file from which the error was thrown
 * @param sourceReference The source reference
 * */
data class ErrorEvent(val error: Throwable, val errorEventSource: ErrorEventSource, val absoluteLine: Int?, val sourceReference: SourceReference?) {

    /**
     * The source code line from which the error event has been fired.
     * Could be null
     * */
    val fragment = absoluteLine?.let { line ->
        when (errorEventSource) {
            ErrorEventSource.Parser -> MainExecutionContext.getParsingProgramStack().takeIf { it.isNotEmpty() }?.peek()?.sourceLines?.get(line - 1)
            ErrorEventSource.Interpreter -> MainExecutionContext.getProgramStack().takeIf { it.isNotEmpty() }?.peek()?.cu?.source?.split("\\r\\n|\\n".toRegex())?.get(line - 1)
        }
    }

    override fun toString(): String {
        return "ErrorEvent(error=$error, errorEventSource=$errorEventSource, absoluteLine=$absoluteLine, sourceReference=$sourceReference, fragment=$fragment)"
    }
}

enum class ErrorEventSource {
    Parser, Interpreter
}