package com.smeup.dspfparser.linesclassifier

import com.smeup.dspfparser.domain.DSPFFieldType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class DSPFSpecificationsTest : DSPFSpecificationsLoader("./src/test/resources/D2.dspf") {

    @Test
    fun records() {
        val first = this.specifications.records[0]
        val second = this.specifications.records[1]
        assertEquals(2, this.specifications.records.size)
        assertEquals("REC01", first.name)
        assertEquals("REC11", second.name)
    }

    @Test
    fun firstRecordFields() {
        val first = this.specifications.records[0].fields[0]
        val second = this.specifications.records[0].fields[1]
        assertEquals(2, this.specifications.records[0].fields.size)
        assertEquals("FLD01", first.name)
        assertEquals("FLD02", second.name)
    }

    @Test
    fun secondRecordFields() {
        val first = this.specifications.records[1].fields[0]
        assertEquals(1, this.specifications.records[1].fields.size)
        assertEquals("FLD11", first.name)
    }

    @Test
    fun getRecordsByName() {
        assertEquals("REC01", this.specifications.getRecord("REC01").name)
        assertEquals("REC11", this.specifications.getRecord("REC11").name)
        assertEquals("REC01", this.specifications.getRecord("rec01").name)
        assertEquals("REC11", this.specifications.getRecord("rec11").name)
    }

    @Test
    fun firstRecordFirstFields() {
        val field = this.specifications.getRecord("REC01").fields.first()
        assertFalse { field.isNumeric }
        assertTrue { field.type == DSPFFieldType.OUTPUT }
        assertEquals(5, field.length)
        assertEquals(1, field.y)
        assertEquals(2, field.x)
    }

    @Test
    fun firstRecordSecondField() {
        val field = this.specifications.getRecord("REC01").fields[1]
        assertTrue { field.isNumeric }
        assertTrue { field.type == DSPFFieldType.INPUT }
        assertEquals(15, field.length)
        assertEquals(1, field.y)
        assertEquals(4, field.x)
    }

    @Test
    fun secondRecordFirstField() {
        val field = this.specifications.getRecord("REC11").fields[0]
        assertFalse { field.isNumeric }
        assertTrue { field.type == DSPFFieldType.INPUT_OUTPUT }
        assertEquals(3, field.length)
        assertEquals(10, field.y)
        assertEquals(8, field.x)
    }

    @Test
    fun getFieldsFromRecordName() {
        assertEquals(2, this.specifications.getFieldsFromRecord("REC01").size)
        assertEquals(1, this.specifications.getFieldsFromRecord("REC11").size)
    }
}
