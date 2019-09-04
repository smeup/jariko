package com.smeup.rpgparser.db.sql

import org.junit.*
import java.sql.Connection
import kotlin.test.assertEquals

class JDBCUtilsTest {
    @Test
    fun primaryKeysTest() {
        connectionForTest().use {
            it.createStatement().execute("CREATE TABLE TSTTAB01 (TSTFLDCHR CHAR (5) NOT NULL, TSTFLDNBR DECIMAL (7, 2) NOT NULL, TSTFLDNB2 DECIMAL (2, 0) NOT NULL, PRIMARY KEY(TSTFLDCHR, TSTFLDNBR))")
            assertEquals(listOf("TSTFLDCHR", "TSTFLDNBR"), it.primaryKeys("TSTTAB01"))
        }
    }

    // TODO Many tests using the same DB? How about cleaning DB objects?
    private fun connectionForTest(): Connection =
        DBConfiguration("jdbc:hsqldb:mem:testmemdb", "SA").getConnection()
}