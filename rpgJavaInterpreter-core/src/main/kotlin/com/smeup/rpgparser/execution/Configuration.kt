package com.smeup.rpgparser.execution

import com.smeup.rpgparser.interpreter.IMemorySliceStorage

/**
 * Creates a configuration object
 * @param memorySliceStorage Allows to implement a symbol table storaging.
 * If null, symbol table persistence will be skipped
 * @param debugOptions Several debug options
 * */
data class Configuration(
    val memorySliceStorage: IMemorySliceStorage? = null,
    val debugOptions: DebugOptions = DebugOptions()
)

/**
 * Debug options
 * @param #getActivationGroup If specified, it overrides the activation group associated to the program. Default blank
 * @param exitInRT If specified, it overrides the exit mode established in program. Default true
 * */
data class DebugOptions(
    val getActivationGroup: (programName: String) -> String = { "" },
    val exitInRT: (programName: String) -> Boolean = { true }
)