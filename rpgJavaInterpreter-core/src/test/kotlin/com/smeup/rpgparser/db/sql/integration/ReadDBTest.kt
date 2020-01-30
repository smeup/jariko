package com.smeup.rpgparser.db.sql.integration

import com.smeup.rpgparser.db.sql.outputOfDBPgm
import org.junit.Test
import kotlin.test.Ignore
import kotlin.test.assertEquals

class ReadDBTest {

    @Test @Ignore
    fun findsExistingRecords() {
        val outputLines = outputOfDBPgm(
            "db/SETLLREADP",
            listOf(createEMPLOYEE(), createXEMP2(), createXEMP2Index(), insertRecords()),
            emptyMap()
        )
        assertEquals(listOf("200340", "200330", "200310", "200280", "200240"), outputLines)
    }
}
