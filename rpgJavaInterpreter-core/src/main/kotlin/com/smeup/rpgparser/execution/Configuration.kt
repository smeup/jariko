package com.smeup.rpgparser.execution

import com.smeup.dbnative.DBManagerBaseImpl
import com.smeup.dbnative.DBNativeAccessConfig
import com.smeup.dbnative.model.FileMetadata
import com.smeup.rpgparser.interpreter.ActivationGroup
import com.smeup.rpgparser.interpreter.IMemorySliceStorage
import com.smeup.rpgparser.interpreter.ISymbolTable
import com.smeup.rpgparser.parsing.ast.CompilationUnit
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
    val reloadConfig: ReloadConfig? = null,
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
    val metadataProducer: (dbFile: String) -> FileMetadata? = {
        DBManagerBaseImpl.register.getMetadata(it.toUpperCase())
    }
)

/**
 * Options object
 * @param muteSupport Used to enable/disable scan execution of mute annotations into rpg sources)
 * @param compiledProgramsDir If specified Jariko searches compiled program in this directory
 * @param muteVerbose If true increases mute logging granularity
 * @param toAstConfiguration Creating ast configuration
 * */
data class Options(
    var muteSupport: Boolean = false,
    var compiledProgramsDir: File? = null,
    var muteVerbose: Boolean = false,
    var toAstConfiguration: ToAstConfiguration = ToAstConfiguration()
)

/**
 * Sometimes we have to gain control of Jariko, this is the right place.
 * It was primarily intended for future use.
 * @param getActivationGroup If specified, it overrides the activation group associated to the program.
 * Default null it means by Configuration.
 * Parameter programName is program for which we are getting activation group, associatedActivationGroup is the current
 * activation group associated to the program.
 * @param exitInRT If specified, it overrides the exit mode established in program. Default null (nei seton rt od lr mode)
 * @param onEnterPgm It is invoked on program enter after symboltable initialization.
 * @param onExitPgm It is invoked on program exit
 * @param afterAstCreation It is invoked after ast creation
 * */
data class JarikoCallback(
    var getActivationGroup: (programName: String, associatedActivationGroup: ActivationGroup?) -> ActivationGroup? = { _: String, _: ActivationGroup? ->
            null
    },
    var exitInRT: (programName: String) -> Boolean? = { null },
    var onEnterPgm: (programName: String, symbolTable: ISymbolTable) -> Unit = { _: String, _: ISymbolTable -> },
    var onExitPgm: (programName: String, symbolTable: ISymbolTable, error: Throwable?) -> Unit = { _: String, _: ISymbolTable, _: Throwable? -> },
    var afterAstCreation: (ast: CompilationUnit) -> Unit = { }
)
