package com.smeup.rpgparser.db.sql

import com.smeup.rpgparser.interpreter.AbstractDataDefinition
import com.smeup.rpgparser.interpreter.DBInterface
import com.smeup.rpgparser.interpreter.FileMetadata
import com.smeup.rpgparser.interpreter.StringType
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
                listOf(AbstractDataDefinition("TSTFIELD", StringType(5))))
        val db = connectionForTest(listOf(fileMetadata))
    }

    private fun connectionForTest(tables: List<FileMetadata> = emptyList()): DBInterface {
        val db = DBSQLInterface(DBConfiguration("jdbc:hsqldb:mem:testmemdb", "SA"))
        db.create(tables)
        return db
    }
}