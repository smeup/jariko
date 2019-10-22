package com.smeup.rpgparser.db.sql

import com.smeup.rpgparser.interpreter.*
import org.junit.Test
import java.math.BigDecimal
import kotlin.test.*

class DBSQLTest {

    @Test
    fun dbSQLSmokeTest() {
        connectionForTest()
    }

    @Test
    fun dbMetaDataTest() {
        val tableName = "TSTTAB01"
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
        assertEquals(fields, metadata.fields)
    }

    @Test
    fun dbChainTest() {
        val tableName = "TSTTAB02"
        val formatName = "TSTRECF"
        val fields = listOf(
                "TSTFLDCHR" primaryKeyWithType StringType(3),
                "TSTFLDNBR" withType NumberType(5, 2))
        val bigDecimalValue = BigDecimal("123.45").setScale(2)
        val fileMetadata = FileMetadata(tableName, formatName, fields)
        val db = connectionForTest(listOf(fileMetadata))
        val values: List<Pair<String, Value>> =
            listOf(
                "TSTFLDCHR" to StringValue("XXX"),
                "TSTFLDNBR" to DecimalValue(bigDecimalValue))
        db.insertRow(tableName, values)
        val dbFile = db.open(tableName)
        assertTrue(dbFile!!.chain(StringValue("ABC")).isEmpty())
        val chainedRecord = dbFile.chain(StringValue("XXX"))
        assertEquals(2, chainedRecord.size)
        val fieldsIterator = chainedRecord.iterator()
        assertEquals(StringValue("XXX"), fieldsIterator.next().value)
        assertEquals(DecimalValue(bigDecimalValue), fieldsIterator.next().value)
    }
}
