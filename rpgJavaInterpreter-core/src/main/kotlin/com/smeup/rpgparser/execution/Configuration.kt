package com.smeup.rpgparser.execution

import com.smeup.rpgparser.interpreter.ActivationGroup
import com.smeup.rpgparser.interpreter.IMemorySliceStorage

/**
 * Configuration object
 * @param memorySliceStorage Allows to implement a symbol table storaging.
 * If null, symbol table persistence will be skipped
 * @param jarikoCallback Several callback.
 * */
data class Configuration(
    val memorySliceStorage: IMemorySliceStorage? = null,
    val jarikoCallback: JarikoCallback = JarikoCallback()
)

/**
 * Sometimes we have to gain control of Jariko, this is the right place.
 * It was primarily intended for future use.
 * @param getActivationGroup If specified, it overrides the activation group associated to the program. Default null (no activation group)
 * @param exitInRT If specified, it overrides the exit mode established in program. Default null (no seton rt od lr mode)
 * */
data class JarikoCallback(
    val getActivationGroup: (programName: String) -> ActivationGroup? = { null },
    val exitInRT: (programName: String) -> Boolean? = { null }
)