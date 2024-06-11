package com.smeup.dspfparser.linesclassifier

import kotlinx.serialization.Serializable

/**
 * Value assigned to a [DSPFField] object.
 */
@Serializable
sealed interface DSPFValue<T> {
    /**
     * Real value that a [DSPFValue] instance holds.
     */
    var primitive: T
}
