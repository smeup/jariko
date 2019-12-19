package com.smeup.rpgparser.db.sql.integration

import com.smeup.rpgparser.db.sql.outputOfDBPgm
import org.junit.Ignore
import org.junit.Test
import kotlin.test.assertEquals

class ReadPreviousDBTest {

    @Test @Ignore
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
}
