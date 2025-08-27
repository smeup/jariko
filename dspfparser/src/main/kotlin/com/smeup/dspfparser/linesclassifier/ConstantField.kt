package com.smeup.dspfparser.linesclassifier

import kotlinx.serialization.Serializable

@Serializable
data class ConstantField(
    val value: ConstantValue?,
    override val x: Int?,
    override val y: Int?,
) : DSPFField
