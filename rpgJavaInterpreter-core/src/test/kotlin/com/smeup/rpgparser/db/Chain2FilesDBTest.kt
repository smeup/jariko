package com.smeup.rpgparser.db

import com.smeup.dbnative.model.CharacterType
import com.smeup.dbnative.model.DecimalType
import com.smeup.dbnative.model.FileMetadata
import com.smeup.dbnative.utils.fieldByType
import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.interpreter.StringValue
import org.junit.Test
import kotlin.test.assertEquals

class Chain2FilesDBTest : AbstractTest() {

    @Test
    fun executeCHAIN2FILE() {
        assertEquals(
            listOf("Not found in First", "2nd: SomeDescription"),
            outputOfDBPgm(
                "db/CHAIN2FILE",
                listOf(createMetadata("FIRST"), createMetadata("SECOND")),
                listOf(
                    sqlCreateTestTable("FIRST"), recordFormatTestTable("FIRST"),
                    sqlCreateTestTable("SECOND"), recordFormatTestTable("SECOND"), insertTestRecords("SECOND")
                ),
                mapOf("toFind" to StringValue("ABCDE"))
            )
        )
    }

    private fun createMetadata(name: String) = FileMetadata(
        name,
        "TSTREC",
        listOf(
            "KEYTST" fieldByType CharacterType(5),
            "DESTST" fieldByType CharacterType(40),
            "NBRTST" fieldByType DecimalType(2, 0)
        ),
        listOf("KEYTST"),
        true
    )

    private fun sqlCreateTestTable(name: String) =
        """
        CREATE TABLE $name (
           KEYTST CHAR(5) DEFAULT '' NOT NULL,
           DESTST CHAR(40) DEFAULT '' NOT NULL,      
           NBRTST DECIMAL(2, 0) DEFAULT 0 NOT NULL,   
           PRIMARY KEY(KEYTST) )
        """.trimIndent()

    private fun recordFormatTestTable(tableName: String) = "COMMENT ON TABLE $tableName IS 'TSTREC'"

    private fun insertTestRecords(tableName: String) =
        "INSERT INTO $tableName (KEYTST, DESTST) VALUES('ABCDE', 'SomeDescription'), ('XYZ', 'OtherDescription')"
}
