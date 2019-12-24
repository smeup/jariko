package com.smeup.rpgparser.db.orient

import com.smeup.rpgparser.db.sql.withType
import com.smeup.rpgparser.interpreter.*
import org.junit.Test
import kotlin.test.*

class DBOrientTest {

    @Test @Ignore
    fun dbOrientSmokeTest() {
        connectionForTest()
    }

    @Test @Ignore
    fun dbMetaDataTest() {
        val tableName = "TSTTAB01"
        val formatName = "TSTRECF"
        val fields = listOf(
            "TSTFLDCHR" withType StringType(5, false),
            "TSTFLDNBR" withType NumberType(5, 2))
        val fileMetadata = FileMetadata(tableName, formatName, fields)
        val db = connectionForTest(listOf(fileMetadata))
        val metadata = db.metadataOf(tableName)
        assertNotNull(metadata)
        assertEquals(tableName, metadata.tableName)
        assertEquals(formatName, metadata.formatName)
        assertEquals(fields.size, metadata.fields.size)
        assertEquals(fields, metadata.fields)
    }
}
