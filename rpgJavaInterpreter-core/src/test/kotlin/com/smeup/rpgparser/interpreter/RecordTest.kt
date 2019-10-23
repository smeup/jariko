package com.smeup.rpgparser.interpreter

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RecordTest {

    @Test
    fun matchesOK() {
        val record = Record(
            Field("F1", StringValue("A")),
            Field("F2", IntValue(8)),
            Field("F3", StringValue("W"))
        )
        val keyFieldsOK = listOf(
            Field("F2", IntValue(8)),
            Field("F3", StringValue("W"))
        )
        assertTrue(record.matches((keyFieldsOK)))
    }

    @Test
    fun matchesKO() {
        val record = Record(
            Field("F1", StringValue("A")),
            Field("F2", IntValue(8)),
            Field("F3", StringValue("W"))
        )
        val keyFieldsKO = listOf(
            Field("F2", IntValue(8)),
            Field("F3", StringValue("XXXXXXX"))
        )
        assertFalse(record.matches((keyFieldsKO)))
    }
}