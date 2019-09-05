package com.smeup.rpgparser.db.sql

import org.junit.*
import java.sql.Connection
import java.util.concurrent.ThreadLocalRandom
import kotlin.test.assertEquals

class JDBCUtilsTest {
    @Test
    fun primaryKeysTest() {
        connectionForTest().use {
            it.createStatement().execute("CREATE TABLE TSTTAB00 (TSTFLDCHR CHAR (5) NOT NULL, TSTFLDNBR DECIMAL (7, 2) NOT NULL, TSTFLDNB2 DECIMAL (2, 0) NOT NULL, PRIMARY KEY(TSTFLDCHR, TSTFLDNBR))")
            assertEquals(listOf("TSTFLDCHR", "TSTFLDNBR"), it.primaryKeys("TSTTAB00"))
        }
    }

    private fun connectionForTest(): Connection =
        randomHsqlMemDB().getConnection()
}

// Using random DB name in order to have different dbs for each test
// TODO verify if this strategy could create performance issues
fun randomHsqlMemDB() =
    DBConfiguration("jdbc:hsqldb:mem:testmemdb" + ThreadLocalRandom.current().nextInt(1, 99_999), "SA")
