package com.smeup.rpgparser.db

import com.smeup.rpgparser.db.utilities.outputOfDBPgm
import org.junit.Test
import kotlin.test.assertEquals

class EqualDBTest {

    @Test
    fun equalWithNoSetllReturnsFalse() {
        val outputLines = outputOfDBPgm(
            "db/EQUALNOSET",
            listOf(createEmployeeMetadata()),
            listOf(createEMPLOYEE()),
            emptyMap()
        )
        assertEquals(listOf("EQUAL=0"), outputLines)
    }
}
