package com.smeup.dspfparser.linesclassifier

import kotlinx.serialization.Serializable

/**
 * Models a [DSPFRecord] field, that can be seen as a variable or a constant, with its record as scope.
 */
@Serializable
sealed interface DSPFField {
    val x: Int?
    val y: Int?
}
