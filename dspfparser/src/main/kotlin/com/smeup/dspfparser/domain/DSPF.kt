package com.smeup.dspfparser.domain

import com.smeup.rpgparser.interpreter.DbField

/**
 * Models a display file as a whole logical unit.
 */
interface DSPF {
    val name: String?

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

    /**
     * Retrieve a list containing all file video fields as a [DbField] instances
     * @return [List]<[DbField]>
     */
    fun getDbFields(): List<DbField>
}
