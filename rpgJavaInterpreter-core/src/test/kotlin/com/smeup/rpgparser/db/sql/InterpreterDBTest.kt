package com.smeup.rpgparser.db.sql

import com.smeup.rpgparser.CollectorSystemInterface
import com.smeup.rpgparser.assertASTCanBeProduced
import com.smeup.rpgparser.execute
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.parsetreetoast.resolve
import org.junit.Test
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

    private fun outputOfDBPgm(programName: String, initialSQL: List<String>, initialValues: Map<String, StringValue> = mapOf()): List<String> {
        val cu = assertASTCanBeProduced(programName)
        val dbInterface = connectionForTest()
        dbInterface.execute(initialSQL)
        cu.resolve(dbInterface)
        val si = CollectorSystemInterface()
        si.databaseInterface = dbInterface
        val interpreter = execute(cu, initialValues, si)
        return si.displayed
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
