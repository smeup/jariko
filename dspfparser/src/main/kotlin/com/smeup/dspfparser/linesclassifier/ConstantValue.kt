package com.smeup.dspfparser.linesclassifier

import kotlinx.serialization.Serializable

@Serializable
data class ConstantValue(val value: String) : DSPFValue