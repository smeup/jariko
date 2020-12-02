package com.smeup.rpgparser.db

import com.smeup.rpgparser.db.utilities.execute
import com.smeup.rpgparser.db.utilities.outputOfDBPgm
import com.smeup.rpgparser.interpreter.StringValue
import org.hsqldb.Server
import org.junit.BeforeClass
import org.junit.Test
import kotlin.test.Ignore
import kotlin.test.assertEquals

class ReadTest {

    companion object {

        lateinit var server: Server

        @BeforeClass
        @JvmStatic
        fun init() {
            execute(listOf(createEMPLOYEE(), insertRecords()))
        }
    }

    @Test
    fun equalWithNoSetllReturnsFalse() {
        val outputLines = outputOfDBPgm(
                "db/EQUALNOSET",
                listOf(createEmployeeMetadata()),
                emptyList(),
                emptyMap()
        )
        assertEquals(listOf("EQUAL=0"), outputLines)
    }

    @Test
    fun findsAndCountExistingRecords() {
        val outputLines = outputOfDBPgm(
            "db/READ01",
                listOf(createEmployeeMetadata()),
            emptyList(),
            emptyMap()
        )
        assertEquals("000010", outputLines.first())
        assertEquals("200340", outputLines.last())
        assertEquals(42, outputLines.size)
    }

    @Test
    fun doesNotFindNonExistingRecord() {
        assertEquals(
                listOf("Not found"),
                outputOfDBPgm(
                        "db/CHAINREADE",
                        listOf(createEmployeeMetadata()),
                        emptyList(),
                        mapOf("toFind" to StringValue("XXX"))
                )
        )
    }

    @Test @Ignore
    fun findsExistingRecords2() {
        assertEquals(
                // TODO is the order of results mandatory?
                listOf("SALLY KWAN", "DELORES QUINTANA", "HEATHER NICHOLLS", "KIM NATZ"),
                outputOfDBPgm(
                        "db/CHAINREADE",
                        listOf(createEmployeeMetadata()),
                        emptyList(),
                        mapOf("toFind" to StringValue("C01"))
                )
        )
    }

}
