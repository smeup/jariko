package com.smeup.rpgparser.execution

import com.smeup.rpgparser.interpreter.ActivationGroup
import com.smeup.rpgparser.interpreter.IMemorySliceStorage
import com.smeup.rpgparser.interpreter.ISymbolTable

/**
 * Configuration object
 * @param memorySliceStorage Allows to implement a symbol table storaging.
 * If null, symbol table persistence will be skipped
 * @param jarikoCallback Several callback.
 * @param defaultActivationGroupName Default activation group. If not specified it assumes "*DEFACTGRP"
 * */

const val DEFAULT_ACTIVATION_GROUP_NAME = "*DFTACTGRP"

data class Configuration(
    val memorySliceStorage: IMemorySliceStorage? = null,
    val jarikoCallback: JarikoCallback = JarikoCallback(),
    val defaultActivationGroupName: String = DEFAULT_ACTIVATION_GROUP_NAME,
    val options: Map<String, String>? = HashMap()
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
 * */
data class JarikoCallback(
    val getActivationGroup: (programName: String, associatedActivationGroup: ActivationGroup?) -> ActivationGroup? = {
        _: String, _: ActivationGroup? ->
        null
    },
    val exitInRT: (programName: String) -> Boolean? = { null },
    val onEnterPgm: (programName: String, symbolTable: ISymbolTable) -> Unit = { _: String, _: ISymbolTable -> },
    val onExitPgm: (programName: String, symbolTable: ISymbolTable, error: Throwable?) -> Unit = { _: String, _: ISymbolTable, _: Throwable? -> }
)