package com.smeup.rpgparser.db.sql.integration

import com.smeup.rpgparser.AbstractTestCase
import com.smeup.rpgparser.interpreter.StringValue
import org.junit.Test
import kotlin.test.assertEquals

class SetllDBTest : AbstractTestCase() {

    @Test
    fun setllHigherMatch() {
        assertSETLLMATCHPgmReturns("000000", "HIGHER MATCH")
    }

    @Test
    fun setllExactMatch() {
        assertSETLLMATCHPgmReturns("000060", "EXACT MATCH")
    }

    @Test
    fun setllNoRecords() {
        assertSETLLMATCHPgmReturns("999999", "NO RECORDS")
    }

    @Test
    fun setllEndOfFile() {
        assertSETLLMATCHPgmReturns("999999", "NO RECORDS", listOf(createEMPLOYEE()))
    }

    private fun assertSETLLMATCHPgmReturns(toFind: String, result: String, initialSQL: List<String> = listOf(createEMPLOYEE(), createXEMP2(), createXEMP2Index(), insertRecords())) {
        val outputLines = outputOfDBPgm(
            "db/SETLLMATCH",
            initialSQL,
            mapOf("toFind" to StringValue(toFind))
        )
        assertEquals(listOf(result), outputLines)
    }
}
