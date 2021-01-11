package com.smeup.rpgparser.db

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.db.utilities.execute
import com.smeup.rpgparser.interpreter.*
import org.junit.BeforeClass
import org.junit.Test
import kotlin.test.assertEquals

open class Chain2KeysDBTest : AbstractTest() {

    companion object {

        @BeforeClass
        @JvmStatic
        fun init() {
            execute(listOf(sqlCreateTestTable(), insertRecords()))
        }

        private fun sqlCreateTestTable() =
            """
        CREATE TABLE MYFILE2 (
            KY1TST CHAR(5) DEFAULT '' NOT NULL,
            KY2TST DECIMAL(2, 0) DEFAULT 0 NOT NULL,
            DESTST CHAR(40) DEFAULT '' NOT NULL,
            PRIMARY KEY(KY1TST, KY2TST) )
        """.trimIndent()

        private fun insertRecords() =
            "INSERT INTO MYFILE2 (KY1TST, KY2TST, DESTST) VALUES('ABA', 1, 'ABA1'), ('ABC', 1, 'ABC1'), ('ABC', 12, 'ABC12'), ('XYZ', 1, 'XYZ1')"
    }

    @Test
    open fun findsExistingRecord() {
        assertEquals(
            listOf("Found: ABC12"),
            outputOfDBPgm(
                "db/CHAIN2KEYS",
                listOf(createMetadata()),
                emptyList(),
                mapOf("toFind1" to StringValue("ABC"), "toFind2" to StringValue("12"))
            )
        )
    }

    private fun createMetadata() = FileMetadata(
        "MYFILE2",
        "TS2REC",
        listOf(
            DbField("KY1TST", StringType(5)),
            DbField("KY2TST", NumberType(2, 0)),
                DbField("DESTST", StringType(40))
        ),
        listOf("KY1TST", "KY2TST")
    )

    @Test
    open fun doesntFindNonExistingRecord() {
        assertEquals(
            listOf("Not found"),
            outputOfDBPgm(
                "db/CHAIN2KEYS",
                listOf(createMetadata()),
                emptyList(),
                mapOf("toFind1" to StringValue("ZZZ"), "toFind2" to StringValue("99"))
            )
        )
    }
}
