package com.smeup.rpgparser.db

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.db.utilities.execute
import com.smeup.rpgparser.interpreter.*
import org.hsqldb.Server
import org.junit.BeforeClass
import org.junit.Test
import kotlin.test.assertEquals

class ChainHostsDBTest : AbstractTest() {

    companion object {

        lateinit var server: Server

        @BeforeClass
        @JvmStatic
        fun init() {
            execute(listOf(sqlCreateQATOCHOSTS(), insertRecordsQATOCHOSTS()))
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

        private fun insertRecordsQATOCHOSTS() =
            "INSERT INTO QATOCHOST (INTERNET, HOSTNME1) VALUES('127.0.0.1', 'LOOPBACK')"
    }

    @Test
    fun findsExistingRecord() {
        assertEquals(
            listOf("LOOPBACK"),
            outputOfDBPgm(
                "db/CHAINHOSTS",
                listOf(createMetadata()),
                emptyList(),
                mapOf("ipToFind" to StringValue("127.0.0.1"))
            )
        )
    }

    @Test
    fun doesNotFindNonExistingRecord() {
        assertEquals(
            listOf("Not found"),
            outputOfDBPgm(
                "db/CHAINHOSTS",
                listOf(createMetadata()),
                emptyList(),
                mapOf("ipToFind" to StringValue("1.2.3.4"))
            )
        )
    }

    private fun createMetadata() = FileMetadata(
        "QATOCHOST",
        "QHOSTS",
        listOf(
            DbField("INTERNET", StringType(15)),
            DbField("HOSTNME1", StringType(255)),
            DbField("HOSTNME2", StringType(255)),
            DbField("HOSTNME3", StringType(255)),
            DbField("HOSTNME4", StringType(255)),
            DbField("IPINTGER", NumberType(10, 0)),
            DbField("TXTDESC", CharacterType(64)),
            DbField("RESERVED", CharacterType(49))
        ),
        listOf("INTERNET")
    )
}
