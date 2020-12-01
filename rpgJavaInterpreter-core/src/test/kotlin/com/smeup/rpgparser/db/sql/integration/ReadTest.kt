package com.smeup.rpgparser.db.sql.integration

import com.smeup.rpgparser.AbstractTestCase
import org.junit.Test
import kotlin.test.Ignore
import kotlin.test.assertEquals

class ReadTest : AbstractTestCase() {

    @Test @Ignore
    fun findsExistingRecords() {
        val outputLines = outputOfDBPgm(
            "db/READ01",
            listOf(createEMPLOYEE(), createXEMP2(), createXEMP2Index(), insertRecords()),
            emptyMap()
        )
        assertEquals("000010", outputLines.first())
        assertEquals("200340", outputLines.last())
        assertEquals(42, outputLines.size)
    }
}
