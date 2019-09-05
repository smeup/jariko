package com.smeup.rpgparser.db.sql

import com.smeup.rpgparser.CollectorSystemInterface
import com.smeup.rpgparser.assertASTCanBeProduced
import com.smeup.rpgparser.execute
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.parsetreetoast.resolve
import org.junit.Ignore
import org.junit.Test
import kotlin.test.assertEquals

class InterpreterDBTest {
    @Test @Ignore
    fun executeCHAINHOSTS_onDB() {
        val cu = assertASTCanBeProduced("CHAINHOSTS")
        val dbInterface = connectionForTest()
        dbInterface.execute(listOf<String>("CREATE TABLE QATOCHOST (\n" +
                "INTERNET CHAR(15) DEFAULT '' NOT NULL, \n" +
                "HOSTNME1 CHAR(255) DEFAULT '' NOT NULL, \n" +
                "HOSTNME2 CHAR(255) DEFAULT '' NOT NULL, \n" +
                "HOSTNME3 CHAR(255) DEFAULT '' NOT NULL, \n" +
                "HOSTNME4 CHAR(255) DEFAULT '' NOT NULL, \n" +
                "IPINTGER INTEGER DEFAULT 0 NOT NULL, \n" +
                "TXTDESC CHAR(64) DEFAULT '' NOT NULL, \n" +
                "RESERVED CHAR(49) DEFAULT '' NOT NULL, \n" +
                "PRIMARY KEY(INTERNET) )  ",
                "COMMENT ON TABLE QATOCHOST IS 'QHOSTS'",
                "INSERT INTO QATOCHOST (INTERNET, HOSTNME1) VALUES('127.0.0.1', 'LOOPBACK')"))
        cu.resolve(dbInterface)
        val si = CollectorSystemInterface()
        si.databaseInterface = dbInterface
        val interpreter = execute(cu, mapOf("ipToFind" to StringValue("127.0.0.1")), si)
        assertEquals(listOf("LOOPBACK"), si.displayed)
    }
}