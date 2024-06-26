package com.smeup.dspfparser.linesclassifier

import kotlinx.serialization.Serializable

/**
 * Models a display file as a whole logical unit.
 */
@Serializable
sealed interface DSPF {
    val records: List<DSPFRecord>

    /**
     * Retrieve a [DSPFRecord] given its name.
     * @param name the name of the record
     * @return [DSPFRecord]
     */
    fun getRecord(name: String): DSPFRecord

    /**
     * Retrieve a list of [DSPFField] given its record's name.
     * @param name the name of the record
     * @return [List]<[DSPFField]>
     */
    fun getFieldsFromRecord(name: String): List<DSPFField>
}
