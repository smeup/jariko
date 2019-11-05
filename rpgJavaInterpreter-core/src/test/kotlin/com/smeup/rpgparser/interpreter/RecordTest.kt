package com.smeup.rpgparser.interpreter

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RecordTest {

    @Test
    fun matchesOK() {
        val record = Record(
            RecordField("F1", StringValue("A")),
            RecordField("F2", IntValue(8)),
            RecordField("F3", StringValue("W"))
        )
        val keyFieldsOK = listOf(
            RecordField("F2", IntValue(8)),
            RecordField("F3", StringValue("W"))
        )
        assertTrue(record.matches((keyFieldsOK)))
    }

    @Test
    fun matchesKO() {
        val record = Record(
            RecordField("F1", StringValue("A")),
            RecordField("F2", IntValue(8)),
            RecordField("F3", StringValue("W"))
        )
        val keyFieldsKO = listOf(
            RecordField("F2", IntValue(8)),
            RecordField("F3", StringValue("XXXXXXX"))
        )
        assertFalse(record.matches((keyFieldsKO)))
    }
}