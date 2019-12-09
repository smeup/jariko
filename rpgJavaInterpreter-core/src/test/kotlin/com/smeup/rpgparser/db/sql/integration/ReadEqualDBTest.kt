package com.smeup.rpgparser.db.sql.integration

import com.smeup.rpgparser.db.sql.outputOfDBPgm
import com.smeup.rpgparser.interpreter.StringValue
import org.junit.Assert.assertFalse
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ReadEqualDBTest {

    @Test
    fun doesNotFindNonExistingRecord() {
        assertEquals(
            listOf("Not found"),
            outputOfDBPgm(
                "db/CHAINREADE",
                listOf(createEMPLOYEE(), createXEMP2(), createXEMP2Index(), insertRecords()),
                mapOf("toFind" to StringValue("XXX"))
            )
        )
    }

    @Test
    fun findsExistingRecords() {
        assertEquals(
            // TODO is the order of results mandatory?
            listOf("SALLY KWAN", "DELORES QUINTANA", "HEATHER NICHOLLS", "KIM NATZ"),
            outputOfDBPgm(
                "db/CHAINREADE",
                listOf(createEMPLOYEE(), createXEMP2(), createXEMP2Index(), insertRecords()),
                mapOf("toFind" to StringValue("C01"))
            )
        )
    }

    @Test
    fun throwsExceptionIfNoSearchedBefore() {
        // TODO better error assertion
        assertFailsWith(RuntimeException::class) {
            outputOfDBPgm(
                "db/CHAINREDE0",
                listOf(createEMPLOYEE(), createXEMP2(), createXEMP2Index(), insertRecords()),
                mapOf("toFind" to StringValue("C01")),
                printTree = false
            )
        }
    }

    @Test
    fun findsExistingRecordsIfReadWithKeyStartingFromFirstKey() {
        assertEquals(
            listOf("CHRISTINE HAAS", "VINCENZO LUCCHESSI", "SEAN O'CONNELL", "DIAN HEMMINGER", "GREG ORLANDO"),
            outputOfDBPgm(
                "db/READENOCHN",
                listOf(createEMPLOYEE(), createXEMP2(), createXEMP2Index(), insertRecords()),
                mapOf("toFind" to StringValue("A00"))
            )
        )
    }

    @Test
    fun setllReadeFindExistingRecord() {
        assertEquals(
            listOf("CHRISTINE HAAS", "VINCENZO LUCCHESSI", "SEAN O'CONNELL", "DIAN HEMMINGER", "GREG ORLANDO"),
            outputOfDBPgm(
                "db/SETLLOK01",
                listOf(createEMPLOYEE(), createXEMP2(), createXEMP2Index(), insertRecords()),
                mapOf("toFind" to StringValue("A00"))
            )
        )
    }

    @Test
    fun setllSetdFoundFlag() {
        assertEquals(
            listOf("SALLY KWAN", "DELORES QUINTANA", "HEATHER NICHOLLS", "KIM NATZ"),
            outputOfDBPgm(
                "db/SETLLOK01",
                listOf(createEMPLOYEE(), createXEMP2(), createXEMP2Index(), insertRecords()),
                mapOf("toFind" to StringValue("C01"))
            )
        )
    }

    @Test
    fun findsAtLeastExistingRecordsIfReadWithKeyStartingFromFirstKey() {
        assertTrue(
            outputOfDBPgm(
                "db/READENOCHN",
                listOf(createEMPLOYEE(), createXEMP2(), createXEMP2Index(), insertRecords()),
                mapOf("toFind" to StringValue("A00"))
            ).containsAll(listOf("CHRISTINE HAAS", "VINCENZO LUCCHESSI", "DIAN HEMMINGER", "GREG ORLANDO"))
        )
    }

    @Test
    fun doesNotFindExistingRecordsIfReadWithKeyStartingFromAnotherKey() {
        assertEquals(
            emptyList(),
            outputOfDBPgm(
                "db/READENOCHN",
                listOf(createEMPLOYEE(), createXEMP2(), createXEMP2Index(), insertRecords()),
                mapOf("toFind" to StringValue("B01"))
            )
        )
    }

    @Test
    fun findsEmployees() {
        val lines = outputOfDBPgm(
            "db/EMPBYDEPT",
            listOf(createEMPLOYEE(), createXEMP2(), createXEMP2Index(), insertRecords()),
            mapOf("toFind" to StringValue("A00"))
        )
        assertEquals("_##_ENDROW", lines.last())
        assertTrue(lines.joinToString().contains("LUCCHESSI"))
        assertFalse(lines.joinToString().contains("YAMAMOTO"))
    }

    @Test
    fun findsEmployeesSecondDept() {
        val lines = outputOfDBPgm(
            "db/EMPBYDEPT",
            listOf(createEMPLOYEE(), createXEMP2(), createXEMP2Index(), insertRecords()),
            mapOf("toFind" to StringValue("D11"))
        )
        assertEquals("_##_ENDROW", lines.last())
        assertFalse(lines.joinToString().contains("LUCCHESSI"))
        assertTrue(lines.joinToString().contains("YAMAMOTO"))
    }
}
