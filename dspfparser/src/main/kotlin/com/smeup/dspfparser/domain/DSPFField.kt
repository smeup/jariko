package com.smeup.dspfparser.domain

/**
 * Models a [DSPFRecord] field (that can be seen as a variable, with its record as scope).
 */
interface DSPFField {
    val value: DSPFValue<String>
    val isNumeric: Boolean
    val length: Int?
    val precision: Int?
    val type: DSPFFieldType
    val x: Int
    val y: Int
    val hasError: Boolean
}
