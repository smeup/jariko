package com.smeup.rpgparser.db.sql.integration

import com.smeup.rpgparser.db.sql.outputOfDBPgm
import org.junit.Ignore
import org.junit.Test
import kotlin.test.assertEquals

class ReadPreviousDBTest {

    @Test
    fun readsTheWholeFileBackwards() {
        val actualOutput = outputOfDBPgm(
            "db/READP",
            listOf(createEMPLOYEE(), createXEMP2(), createXEMP2Index(), insertRecords()),
            emptyMap()
        )
        assertEquals(42, actualOutput.size)
        assertEquals("200340", actualOutput[0])
        assertEquals("200330", actualOutput[1])
        assertEquals("000010", actualOutput[41])
    }

    @Test @Ignore
    fun readingPreviousFromTopDoesntFindAnything() {
        val actualOutput = outputOfDBPgm(
            "db/READPLOVAL",
            listOf(createEMPLOYEE(), createXEMP2(), createXEMP2Index(), insertRecords()),
            emptyMap()
        )
        assertEquals(emptyList(), actualOutput)
    }
}
