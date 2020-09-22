package com.smeup.rpgparser.execution

import com.smeup.rpgparser.interpreter.IMemorySliceStorage

/**
 * Creates a configuration object
 * @param memorySliceStorage Allow to implement a symbol table storaging.
 * If null symbol table persistence behaviour will be skipped
 * @param debugOptions Several debug options
 * */
data class Configuration(
    val memorySliceStorage: IMemorySliceStorage? = null,
    val debugOptions: DebugOptions = DebugOptions()
)

/**
 * Debug options
 * @param #getActivationGroup If specified, interpreter retrieve the activation group name from lambda expression returned value
 * */
data class DebugOptions(val getActivationGroup: () -> String = { "" })