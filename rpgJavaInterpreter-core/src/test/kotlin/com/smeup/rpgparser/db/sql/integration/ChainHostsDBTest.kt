package com.smeup.rpgparser.db.sql.integration

import com.smeup.rpgparser.db.sql.outputOfDBPgm
import com.smeup.rpgparser.interpreter.*
import org.junit.*
import kotlin.test.assertEquals

class ChainHostsDBTest {

    @Test
    fun findsExistingRecord() {
        assertEquals(
                listOf("LOOPBACK"),
                outputOfDBPgm(
                        "db/CHAINHOSTS",
                        listOf(sqlCreateQATOCHOSTS(), recordFormatNameQATOCHOSTS(), insertRecordsQATOCHOSTS()),
                        mapOf("ipToFind" to StringValue("127.0.0.1"))))
    }

    @Test
    fun doesNotFindNonExistingRecord() {
        assertEquals(
            listOf("Not found"),
                outputOfDBPgm(
                        "db/CHAINHOSTS",
                        listOf(sqlCreateQATOCHOSTS(), recordFormatNameQATOCHOSTS(), insertRecordsQATOCHOSTS()),
                        mapOf("ipToFind" to StringValue("1.2.3.4"))))
    }

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
