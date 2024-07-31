package com.smeup.dspfparser.linesclassifier

import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule

/**
 * Unlike variable fields, a constant field should be initialized with its value in the
 * parser phase since it is hardwired into the display file.
 * This [DSPFValue] implementation allows the value to be passed into Jariko.
 * Importing Jariko's [DSPFValue] implementations is not a good choice because Jariko depends on this
 * module and not the vice versa.
 * To make serialization / deserialization work proper declarations in [SerializersModule] is needed
 * because [DSPFValue] is not sealed (its implementers are in another package,
 * in rpgJavaInterpreter-core module).
 */
@Serializable
data class ConstantValue(val value: String) : DSPFValue