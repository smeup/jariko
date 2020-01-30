package com.smeup.rpgparser.db.sql.integration

import com.smeup.rpgparser.db.sql.outputOfDBPgm
import org.junit.Test
import kotlin.test.assertEquals

class EqualDBTest {

    @Test
    fun equalWithNoSetllReturnsFalse() {
        val outputLines = outputOfDBPgm(
            "db/EQUALNOSET",
            listOf(createEMPLOYEE()),
            emptyMap()
        )
        assertEquals(listOf("EQUAL=0"), outputLines)
    }
}
