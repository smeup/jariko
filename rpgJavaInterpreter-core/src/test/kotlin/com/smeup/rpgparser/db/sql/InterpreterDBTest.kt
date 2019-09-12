package com.smeup.rpgparser.db.sql

import com.smeup.rpgparser.interpreter.*
import org.junit.*
import kotlin.test.assertEquals

class InterpreterDBTest {

    @Test
    fun executeCHAINHOSTS_onDBFindsExistingRecord() {
        assertEquals(
                listOf("LOOPBACK"),
                outputOfDBPgm(
                        "CHAINHOSTS",
                        listOf(sqlCreateQATOCHOSTS(), recordFormatNameQATOCHOSTS(), insertRecordsQATOCHOSTS()),
                        mapOf("ipToFind" to StringValue("127.0.0.1"))))
    }

    @Test
    fun executeCHAINHOSTS_onDBDoesntFindNonExistingRecord() {
        assertEquals(
            listOf("Not found"),
            outputOfDBPgm(
                    "CHAINHOSTS",
                    listOf(sqlCreateQATOCHOSTS(), recordFormatNameQATOCHOSTS(), insertRecordsQATOCHOSTS()),
                    mapOf("ipToFind" to StringValue("1.2.3.4"))))
    }

    @Test
    fun executeCHAIN2FILE() {
        assertEquals(
                listOf("Not found in First", "2nd: SomeDescription"),
                outputOfDBPgm(
                        "CHAIN2FILE",
                        listOf(sqlCreateTestTable("FIRST"), recordFormatTestTable("FIRST"),
                                sqlCreateTestTable("SECOND"), recordFormatTestTable("SECOND"), insertTestRecords("SECOND")),
                        mapOf("toFind" to StringValue("ABCDE"))))
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

    private fun sqlCreateQATOCHOSTS() =
            """
        CREATE TABLE QATOCHOST (
            INTERNET CHAR(15) DEFAULT '' NOT NULL,
            HOSTNME1 CHAR(255) DEFAULT '' NOT NULL,
            HOSTNME2 CHAR(255) DEFAULT '' NOT NULL,
            HOSTNME3 CHAR(255) DEFAULT '' NOT NULL,
            HOSTNME4 CHAR(255) DEFAULT '' NOT NULL,
            IPINTGER INTEGER DEFAULT 0 NOT NULL,
            TXTDESC CHAR(64) DEFAULT '' NOT NULL,
            RESERVED CHAR(49) DEFAULT '' NOT NULL,
            PRIMARY KEY(INTERNET) )
        """.trimIndent()

    private fun recordFormatNameQATOCHOSTS() = "COMMENT ON TABLE QATOCHOST IS 'QHOSTS'"

    private fun insertRecordsQATOCHOSTS() = "INSERT INTO QATOCHOST (INTERNET, HOSTNME1) VALUES('127.0.0.1', 'LOOPBACK')"
}
