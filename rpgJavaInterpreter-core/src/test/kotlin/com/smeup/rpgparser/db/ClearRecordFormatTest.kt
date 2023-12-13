package com.smeup.rpgparser.db

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.interpreter.DbField
import com.smeup.rpgparser.interpreter.FileMetadata
import com.smeup.rpgparser.interpreter.NumberType
import com.smeup.rpgparser.interpreter.StringType
import org.junit.Test
import kotlin.test.assertEquals

class ClearRecordFormatTest : AbstractTest() {

    private fun createBRARTI0FMetadata(name: String = "BRARTI0F", recordFormat: String = "BRARTIR"): FileMetadata =
        FileMetadata(
            name = name,
            tableName = name,
            recordFormat = recordFormat,
            fields = listOf(
                DbField("A§ARTI", StringType(15)),
                DbField("A§PESO", NumberType(12, 5, "P")),
                DbField("A§DT01", NumberType(8, 0, "P"))
            ),
            accessFields = listOf("A§ARTI")
        )

    @Test
    fun clearRecordFormatTest() {
        val expected = listOf(
            "A§ARTI(               ) A§PESO(.00000) A§DT01(0)",
            "A§ARTI(123456789012345) A§PESO(123.45600) A§DT01(12345678)",
            "A§ARTI(               ) A§PESO(.00000) A§DT01(0)"
        )
        assertEquals(
            expected, outputOfDBPgm(
                "db/CLEARRECF",
                listOf(createBRARTI0FMetadata()),
                emptyList(),
                emptyMap()
            )
        )
    }
}