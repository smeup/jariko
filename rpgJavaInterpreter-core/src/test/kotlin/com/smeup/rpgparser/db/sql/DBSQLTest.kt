package com.smeup.rpgparser.db.sql

import com.smeup.rpgparser.interpreter.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DBSQLTest {

    @Test
    fun dbSQLSmokeTest() {
        val db: DBInterface = connectionForTest()
    }

    @Test
    fun dbMetaDataTest() {
        val tableName = "TSTTAB"
        val formatName = "TSTRECF"
        val fields = listOf(
            "TSTFLDCHR" withType StringType(5),
            "TSTFLDNBR" withType NumberType(5, 2))
        val fileMetadata = FileMetadata(tableName, formatName, fields)
        val db = connectionForTest(listOf(fileMetadata))
        val metadata = db.metadataOf(tableName)
        assertNotNull(metadata)
        assertEquals(tableName, metadata.tableName)
        assertEquals(formatName, metadata.formatName)
        assertEquals(fields.size, metadata.fields.size)
//        assertEquals(fields, metadata.fields)
    }

    private fun connectionForTest(tables: List<FileMetadata> = emptyList()): DBSQLInterface {
        val db = DBSQLInterface(DBConfiguration("jdbc:hsqldb:mem:testmemdb", "SA"))
        db.setSQLLog(true)
        db.create(tables)
        return db
    }
}