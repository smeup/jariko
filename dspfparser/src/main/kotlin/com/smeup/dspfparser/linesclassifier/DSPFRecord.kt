package com.smeup.dspfparser.linesclassifier

import kotlinx.serialization.Serializable

/**
 * Represents a display file record.
 */
@Serializable
sealed interface DSPFRecord {
    val name: String
    val mutables: List<MutableField>
    val constants: List<ConstantField>
}
