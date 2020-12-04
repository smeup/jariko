package com.smeup.rpgparser.db

import com.smeup.dbnative.model.CharacterType
import com.smeup.dbnative.model.FileMetadata
import com.smeup.dbnative.model.IntegerType
import com.smeup.dbnative.utils.fieldByType
import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.db.utilities.execute
import com.smeup.rpgparser.interpreter.StringValue
import org.hsqldb.Server
import org.junit.BeforeClass
import org.junit.Test
import kotlin.test.assertEquals

class ChainHostsDBTest {

    companion object : AbstractTest() {

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
            "INTERNET" fieldByType CharacterType(15),
            "HOSTNME1" fieldByType CharacterType(255),
            "HOSTNME2" fieldByType CharacterType(255),
            "HOSTNME3" fieldByType CharacterType(255),
            "HOSTNME4" fieldByType CharacterType(255),
            "IPINTGER" fieldByType IntegerType,
            "TXTDESC" fieldByType CharacterType(64),
            "RESERVED" fieldByType CharacterType(49)
        ),
        listOf("INTERNET"),
        true
    )
}
