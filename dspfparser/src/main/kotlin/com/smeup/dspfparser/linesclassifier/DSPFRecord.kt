package com.smeup.dspfparser.linesclassifier

import kotlinx.serialization.Serializable

/**
 * Represents a display file record.
 */
@Serializable
sealed interface DSPFRecord {
    val name: String
    val fields: List<DSPFField>
    val constants: List<DSPFField>
}
