package com.smeup.dspfparser.linesclassifier

import kotlinx.serialization.Serializable

@Serializable
data class MutableField(
    val name: String,
    var value: DSPFValue?,
    val isNumeric: Boolean,
    val length: Int?,
    val precision: Int?,
    val type: DSPFFieldType,
    override val x: Int?,
    override val y: Int?,
) : DSPFField
