package com.smeup.dspfparser.linesclassifier

import kotlinx.serialization.Serializable

/**
 * Models a [DSPFRecord] field (that can be seen as a variable, with its record as scope).
 */
@Serializable
sealed interface DSPFField {
    val name: String
    var value: DSPFValue?
    val isNumeric: Boolean
    val length: Int?
    val precision: Int?
    val type: DSPFFieldType
    val x: Int?
    val y: Int?
    val hasError: Boolean
}
