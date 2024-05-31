package com.smeup.dspfparser.domain

/**
 * Value assigned to a [DSPFField] object.
 */
interface DSPFValue<T> {
    /**
     * Real value that a [DSPFValue] instance holds.
     */
    var primitive: T
}
