package com.smeup.dspfparser.linesclassifier

import kotlinx.serialization.Serializable

/**
 * Value assigned to a [DSPFField] object. At the contrary of [DSPFValue] this is immutable.
 */
interface DSPFConstantValue : DSPFValue