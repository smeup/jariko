package com.smeup.rpgparser.db.sql.integration

import com.smeup.rpgparser.db.sql.outputOfDBPgm
import org.junit.Test
import kotlin.test.assertEquals

class ReadDBTest {

    @Test
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
