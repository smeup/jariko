package com.smeup.dspfparser.linesclassifier

import com.smeup.dspfparser.domain.DSPFFieldType
import com.smeup.dspfparser.linesprocessor.DSPFKeywordsGroup
import com.smeup.dspfparser.linesprocessor.SHOULD_GET_CONDITIONS_AND_KEYWORDS
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class DSPFSpecificationsTest : DSPFSpecificationsLoader("./src/test/resources/_d2.dspf") {
    @Test
    fun file() {
        if (SHOULD_ADD_RELATED_AND_FILE) {
            val first = this.specifications.file.related[0]
            assertEquals(1, this.specifications.file.related.size)

            if (SHOULD_GET_CONDITIONS_AND_KEYWORDS) {
                assertEquals(DSPFKeywordsGroup.fromString("PRINT"), first.keywords)
            }
        }
    }

    @Test
    fun records() {
        val first = this.specifications.records[0]
        val second = this.specifications.records[1]
        assertEquals(2, this.specifications.records.size)
        assertEquals("REC01", first.declaration.fieldName)
        assertEquals("REC11", second.declaration.fieldName)

        if (SHOULD_ADD_RELATED_AND_FILE) {
            assertEquals(emptyList(), second.related)
        }

        if (SHOULD_GET_CONDITIONS_AND_KEYWORDS) {
            assertEquals(
                DSPFKeywordsGroup.fromString("DSPATR(RI)"),
                first.related[0].keywords
            )
        }
    }

    @Test
    fun firstRecordFields() {
        val first = this.specifications.records[0].fields[0]
        val second = this.specifications.records[0].fields[1]
        assertEquals(2, this.specifications.records[0].fields.size)
        assertEquals("FLD01", first.declaration.fieldName)
        assertEquals("FLD02", second.declaration.fieldName)

        if (SHOULD_ADD_RELATED_AND_FILE) {
            assertEquals(emptyList(), second.related)
        }

        if (SHOULD_GET_CONDITIONS_AND_KEYWORDS) {
            assertEquals(
                DSPFKeywordsGroup.fromString("CHGINPDFT(HI CS)"),
                first.related[0].keywords
            )
        }
    }

    @Test
    fun secondRecordFields() {
        val first = this.specifications.records[1].fields[0]
        assertEquals(1, this.specifications.records[1].fields.size)
        assertEquals("FLD11", first.declaration.fieldName)

        if (SHOULD_GET_CONDITIONS_AND_KEYWORDS) {
            assertEquals(
                DSPFKeywordsGroup.fromString("COLOR(RED)"),
                first.related[0].keywords
            )
        }
    }

    @Test
    fun getRecordsByName() {
        assertEquals("REC01", this.specifications.getRecord("REC01").declaration.fieldName)
        assertEquals("REC11", this.specifications.getRecord("REC11").declaration.fieldName)
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
}
