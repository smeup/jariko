package com.smeup.rpgparser.db.sql

import com.smeup.rpgparser.interpreter.*
import org.junit.Test

class DBSQLTest {

    @Test
    fun dbSQLSmokeTest() {
        val db: DBInterface = connectionForTest()
    }

    @Test
    fun dbMetaDataTest() {
        val fileMetadata = FileMetadata("TSTTAB",
                "TSTTAB",
                listOf("TSTFIELD" withType StringType(5)))
        val db = connectionForTest(listOf(fileMetadata))
    }

    private fun connectionForTest(tables: List<FileMetadata> = emptyList()): DBInterface {
        val db = DBSQLInterface(DBConfiguration("jdbc:hsqldb:mem:testmemdb", "SA"))
        db.create(tables)
        return db
    }
}