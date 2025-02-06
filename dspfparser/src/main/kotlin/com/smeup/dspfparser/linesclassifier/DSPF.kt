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
     * Retrieve a list of [MutableField] given its record's name.
     * @param name the name of the record
     * @return [List]<[MutableField]>
     */
    fun getMutableFieldsFromRecord(name: String): List<MutableField>

    /**
     * Retrieve a list of [ConstantField] given its record's name.
     * @param name the name of the record
     * @return [List]<[ConstantField]>
     */
    fun getConstantFieldsFromRecord(name: String): List<ConstantField>
}
