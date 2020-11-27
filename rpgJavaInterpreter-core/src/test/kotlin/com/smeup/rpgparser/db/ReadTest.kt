package com.smeup.rpgparser.db

import com.smeup.rpgparser.db.utilities.outputOfDBPgm
import org.junit.Test
import kotlin.test.Ignore
import kotlin.test.assertEquals

class ReadTest {

    @Test @Ignore
    fun findsExistingRecords() {
        val outputLines = outputOfDBPgm(
            "db/READ01",
                listOf(createEmployeeMetadata()),
            listOf(createEMPLOYEE(), createXEMP2(), createXEMP2Index(), insertRecords()),
            emptyMap()
        )
        assertEquals("000010", outputLines.first())
        assertEquals("200340", outputLines.last())
        assertEquals(42, outputLines.size)
    }
}
