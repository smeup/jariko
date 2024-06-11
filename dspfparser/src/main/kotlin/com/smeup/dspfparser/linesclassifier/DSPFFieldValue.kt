package com.smeup.dspfparser.linesclassifier

import kotlinx.serialization.Serializable

@Serializable
internal data class DSPFFieldValue<T>(override var primitive: T) : DSPFValue<T>
