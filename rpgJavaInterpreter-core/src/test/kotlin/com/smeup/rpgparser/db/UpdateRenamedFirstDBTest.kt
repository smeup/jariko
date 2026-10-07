package com.smeup.rpgparser.db

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.interpreter.DbField
import com.smeup.rpgparser.interpreter.FileMetadata
import com.smeup.rpgparser.interpreter.StringType
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Regression for C5C6M0.
 *
 * Two F-spec files share the native record format name TSTFMT; the first one declares
 * `RENAME(TSTFMT:TSTFM2)`, so in RPG IV `TSTFMT` can only refer to the second one. The program
 * reads a record through the second file (CHAIN by file name) and writes it back with
 * `UPDATE TSTFMT` (by format name).
 *
 * [DBFileMap] used to register the native name `TSTFMT` for the renamed (first) file as well, and
 * as the first registration wins, `UPDATE TSTFMT` resolved to the renamed file, which had never
 * read anything: "Positioning required before update". Now the file that really declares the
 * name takes the alias over.
 *
 * The two files sit on different tables (RENAMED, PLAIN) only to make it observable which one is
 * updated: the test passes when the row of PLAIN is the one changed. Without the fix it fails with
 * the error above.
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
