package com.smeup.rpgparser.db

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.db.utilities.execute
import com.smeup.rpgparser.interpreter.StringValue
import org.junit.BeforeClass
import org.junit.Test
import kotlin.test.assertEquals

class EmployeesTest : AbstractTest() {

    companion object {
        @BeforeClass
        @JvmStatic
        fun init() {
            execute(listOf(createEMPLOYEE(), insertRecords(), createXEMP2(), createXEMP2Index()))
        }
    }

    /**
     * Test a %EQUAL func call without previous SETLL
     */
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

    /**
     * Test for READ and EOF
     *
     * Read all record froma table until EOF
     */
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

    /*
     *  Test read of not existing record. Test CHAIN and not FOUND. Table metadata is defined as a VIEW of preexistent
     *  table.
     */
    @Test
    fun doesNotFindNonExistingRecordOnView() {
        assertEquals(
            listOf("Not found"),
            outputOfDBPgm(
                "db/CHAINREADE",
                listOf(createXEMP2Metadata()),
                emptyList(),
                mapOf("toFind" to StringValue("XXX"))
            )
        )
    }

    /**
     *  Test read of existing records with CHAIN and READE. Table metadata is defined as a VIEW of preexistent table.
     */
    @Test
    fun findsExistingRecordsOnView() {
        assertEquals(
            listOf("SALLY KWAN", "DELORES QUINTANA", "HEATHER NICHOLLS", "KIM NATZ"),
            outputOfDBPgm(
                "db/CHAINREADE",
                listOf(createXEMP2Metadata()),
                emptyList(),
                mapOf("toFind" to StringValue("C01"))
            )
        )
    }

    /**
     * Point to last element key of a table with HIVAL and read it with a READP command. Test for HIVAL and READP.
     */
    @Test
    fun hivalSetllAndRead() {
        val actualOutput = outputOfDBPgm(
            "db/HIVAL",
            listOf(createEmployeeMetadata()),
            emptyList(),
            emptyMap()
        )

        assertEquals(42, actualOutput.size)
        assertEquals("200340", actualOutput[0])
        assertEquals("200330", actualOutput[1])
        assertEquals("000010", actualOutput[41])
    }

    /**
     * Point to last element key of a table with LOVAL and read it with a READ command. Test for LOVAL and READ.
     */
    @Test
    fun lovalSetllAndRead() {
        val actualOutput = outputOfDBPgm(
            "db/LOVAL",
            listOf(createEmployeeMetadata()),
            emptyList(),
            emptyMap()
        )

        assertEquals(42, actualOutput.size)
        assertEquals("000010", actualOutput[0])
        assertEquals("200340", actualOutput[41])
    }

    /**
     * Test a SETLL to *HIVAL followed by 5 READP.
     */
    @Test
    fun setllAndReadP() {
        val outputLines = outputOfDBPgm(
            "db/SETLLREADP",
            listOf(createEmployeeMetadata()),
            emptyList(),
            emptyMap()
        )

        assertEquals(listOf("200340", "200330", "200310", "200280", "200240"), outputLines)
    }
}
