package com.smeup.dspfparser.domain

/**
 * Represents a display file record.
 */
interface DSPFRecord {
    val name: String
    val fields: List<DSPFField>
}
