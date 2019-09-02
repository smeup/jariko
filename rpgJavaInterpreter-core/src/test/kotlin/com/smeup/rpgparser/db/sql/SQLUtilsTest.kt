package com.smeup.rpgparser.db.sql

import com.smeup.rpgparser.interpreter.*
import org.junit.*
import kotlin.test.assertEquals

class SQLUtilsTest {
    @Test
    fun sqlForCreateTableTest() {
        val fileMetadata = FileMetadata("TSTTAB", "TSTTAB",
            listOf(
                "TSTFLDCHR" withType StringType(5),
                "TSTFLDNBR" withType NumberType(5, 2)))
        assertEquals(
            "CREATE TABLE TSTTAB (TSTFLDCHR CHAR (5), TSTFLDNBR DECIMAL (7, 2))",
            fileMetadata.toSQL())
    }
}