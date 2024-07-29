package com.smeup.dspfparser.linesclassifier

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class DSPFSpecificationsTest : DSPFSpecificationsLoader("./src/test/resources/RECORDS.dspf") {

    @Test
    fun recordsByName() {
        assertEquals("REC01", this.specifications.getRecord("REC01").name)
        assertEquals("REC01", this.specifications.getRecord("rec01").name)
        assertEquals("REC11", this.specifications.getRecord("REC11").name)
        assertEquals("REC11", this.specifications.getRecord("rec11").name)
        assertEquals("REC21", this.specifications.getRecord("REC21").name)
        assertEquals("REC21", this.specifications.getRecord("rec21").name)
    }

    @Test
    fun fieldsFromRecordByName() {
        assertEquals(2, this.specifications.getFieldsFromRecord("REC01").size)
        assertEquals(1, this.specifications.getFieldsFromRecord("REC11").size)
        assertEquals(1, this.specifications.getFieldsFromRecord("REC21").size)
    }

    @Test
    fun fields_1() {
        val first = this.specifications.records[0].fields[0]
        assertEquals("FLD01", first.name)
        assertFalse { first.isNumeric }
        assertTrue { first.type == DSPFFieldType.OUTPUT }
        assertEquals(5, first.length)
        assertEquals(1, first.y)
        assertEquals(2, first.x)

        val second = this.specifications.records[0].fields[1]
        assertEquals("FLD02", second.name)
        assertTrue { second.isNumeric }
        assertTrue { second.type == DSPFFieldType.INPUT }
        assertEquals(15, second.length)
        assertEquals(1, second.y)
        assertEquals(4, second.x)
    }

    @Test
    fun fields_2() {
        val first = this.specifications.records[1].fields[0]
        assertEquals("FLD11", first.name)
        assertFalse { first.isNumeric }
        assertTrue { first.type == DSPFFieldType.INPUT_OUTPUT }
        assertEquals(3, first.length)
        assertEquals(10, first.y)
        assertEquals(8, first.x)
    }


}
