package com.smeup.rpgparser.db.sql

import com.smeup.rpgparser.interpreter.*
import org.junit.*
import kotlin.test.assertEquals

class SQLUtilsTest {
    @Test
    fun sqlForCreateTableTest() {
        val fileMetadata = FileMetadata("TSTTAB", "TSTRECF",
            listOf(
                "TSTFLDCHR" withType StringType(5),
                "TSTFLDNBR" withType NumberType(5, 2)))

        assertEquals(
            listOf("CREATE TABLE TSTTAB (TSTFLDCHR CHAR (5) NOT NULL, TSTFLDNBR DECIMAL (7, 2) NOT NULL)",
                    "COMMENT ON TABLE TSTTAB IS 'TSTRECF'"),
            fileMetadata.toSQL())
    }

    @Test
    fun sqlForInsertTest() {
        val values = listOf(
            "TSTFLDCHR" to StringValue("ABC"),
            "TSTFLDNBR" to IntValue(5))
        assertEquals("INSERT INTO TSTTAB (TSTFLDCHR, TSTFLDNBR) VALUES(?, ?)", "TSTTAB".insertSQL(values))
    }

    @Test
    fun sqlForWhereTest() {
        val values = listOf(
                "TSTFLDCHR" to StringValue("ABC"),
                "TSTFLDNBR" to IntValue(5))
        assertEquals(" WHERE TSTFLDCHR = ? AND TSTFLDNBR = ?", values.whereSQL())
    }
}