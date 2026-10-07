package com.smeup.rpgparser.db

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.interpreter.DbField
import com.smeup.rpgparser.interpreter.FileMetadata
import com.smeup.rpgparser.interpreter.StringType
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Regression for C5C6M0: an F-spec `RENAME(TSTFMT:TSTFM2)` declared before the F-spec that owns
 * the format name TSTFMT. The record is read through the owner (CHAIN by file name) and written
 * back with `UPDATE TSTFMT` (by format name), which must resolve to the owner and not to the
 * renamed F-spec that registered TSTFMT first only as the record's native name (it had no current
 * record: "Positioning required before update"). See [DBFileMap]. The two files sit on different
 * tables to tell them apart.
 */
open class UpdateRenamedFirstDBTest : AbstractTest() {
    @Test
    open fun executeUPDATERENAMEDFIRST() {
        assertEquals(
            listOf("Updated"),
            outputOfDBPgm(
                "db/UPDATERENAMEDFIRST",
                listOf(createMetadata("RENAMED"), createMetadata("PLAIN")),
                listOf(
                    sqlCreateTestTable("RENAMED"),
                    "INSERT INTO RENAMED (KEYTST, DESTST) VALUES('ABCDE', 'FromRenamed')",
                    sqlCreateTestTable("PLAIN"),
                    "INSERT INTO PLAIN (KEYTST, DESTST) VALUES('ABCDE', 'FromPlain')",
                ),
            ).map { it.trim() },
        )
    }

    private fun createMetadata(name: String) =
        FileMetadata(
            name = name,
            tableName = name,
            recordFormat = "TSTFMT",
            fields = listOf(DbField("KEYTST", StringType(5)), DbField("DESTST", StringType(40))),
            accessFields = listOf("KEYTST"),
        )

    private fun sqlCreateTestTable(name: String) =
        """
        CREATE TABLE $name (
           "__RNN" BIGINT GENERATED ALWAYS AS IDENTITY (START WITH 1) PRIMARY KEY,
           KEYTST CHAR(5) DEFAULT '' NOT NULL,
           DESTST CHAR(40) DEFAULT '' NOT NULL,
           UNIQUE(KEYTST) )
        """.trimIndent()
}
