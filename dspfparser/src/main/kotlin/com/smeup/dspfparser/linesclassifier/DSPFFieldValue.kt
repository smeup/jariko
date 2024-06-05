package com.smeup.dspfparser.linesclassifier

import com.smeup.dspfparser.domain.DSPFValue
import kotlinx.serialization.Serializable

@Serializable
internal data class DSPFFieldValue<T>(override var primitive: T) : DSPFValue<T>
