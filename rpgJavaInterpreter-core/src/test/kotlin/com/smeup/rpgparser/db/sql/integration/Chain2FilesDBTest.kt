package com.smeup.rpgparser.db.sql.integration

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
                    programName = "db/CHAIN2FILE",
                    initialSQL = listOf(sqlCreateTestTable("FIRST"), recordFormatTestTable("FIRST"),
                                sqlCreateTestTable("SECOND"), recordFormatTestTable("SECOND"), insertTestRecords("SECOND")),
                    inputParms = mapOf("toFind" to StringValue("ABCDE"))
                ))
    }

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
