/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smeup.rpgparser.db

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.db.utilities.isPostgresAvailable
import com.smeup.rpgparser.db.utilities.outputOfDBPgmPostgres
import com.smeup.rpgparser.interpreter.DbField
import com.smeup.rpgparser.interpreter.DecimalValue
import com.smeup.rpgparser.interpreter.FileMetadata
import com.smeup.rpgparser.interpreter.StringType
import org.junit.Assume.assumeTrue
import org.junit.Test
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/**
 * End-to-end coverage (real HSQLDB via outputOfDBPgm, so this exercises reload's actual
 * Result.rrn - see the companion smeuperp workspace's docs/plans/rrn-output-support-reload.md)
 * for the INFDS Relative Record Number subfield: docs/plans/infds-rrn-support-jariko.md.
 *
 * Every table here declares reload's convention `__RNN` identity column (see
 * SQLDialect.rrnSelectExpression's kdoc in reload), which every non-DB2 dialect - HSQLDB included -
 * projects as Result.rrn, keyed files and unkeyed ones alike (TestF is unkeyed in some tests,
 * accessFields=emptyList(), like ChainUnkeyedFormatDBTest, and keyed in others).
 */
open class InfdsRrnDBTest : AbstractTest() {
    @Test
    open fun chainPopulatesInfdsRrnSubfield() {
        val output =
            outputOfDBPgm(
                "db/INFDSRRN",
                listOf(createMetadata(accessFields = emptyList())),
                listOf(
                    sqlDropTestTable(),
                    sqlCreateTestTable(),
                    insertTestRecord("ABCDE", "FoundMe"),
                ),
                mapOf("rrn" to DecimalValue(BigDecimal.ONE)),
            )
        assertEquals(listOf("Found", "1"), output.map { it.trim() })
    }

    @Test
    open fun infdsNamingUndeclaredDsFailsFast() {
        // Fails during DBFileMap.add() (program init, before resolveInfdsDataDefinition even
        // opens a reload connection) - but AST conversion for an externally-described F-spec
        // needs real field metadata first (independent of this plan), so that much must be
        // supplied. No real SQL table is needed: resolution happens before reload is touched.
        val ex =
            assertFailsWith<IllegalStateException> {
                outputOfDBPgm("db/INFDSUNRESOLVABLE", listOf(createMetadata(accessFields = emptyList())), emptyList())
            }
        assertTrue(
            ex.message!!.contains("does not resolve", ignoreCase = true),
            "Expected message to mention the unresolved INFDS name, was: ${ex.message}",
        )
    }

    @Test
    open fun infdsDsWithFieldAtUnsupportedOffsetFailsFast() {
        val ex =
            assertFailsWith<IllegalArgumentException> {
                outputOfDBPgm("db/INFDSWRONGOFFSET", listOf(createMetadata(accessFields = emptyList())), emptyList())
            }
        assertTrue(
            ex.message!!.contains("BADFLD") && ex.message!!.contains("397"),
            "Expected message to mention the offending field and the supported offset, was: ${ex.message}",
        )
    }

    @Test
    open fun keyedChainPopulatesInfdsRrnSubfield() {
        // TESTF here is keyed (accessFields=["KEYTST"]): reload projects the table's __RNN as the
        // read's Result.rrn even though CHAIN positions by the real key - the C5C5I0.rpgle/
        // C5SER_39 production scenario (a keyed file whose INFDS RRN subfield is read back).
        // "1" is the single inserted row's real __RNN identity value.
        val output =
            outputOfDBPgm(
                "db/INFDSRRNKEYED",
                listOf(createMetadata(accessFields = listOf("KEYTST"))),
                listOf(
                    sqlDropTestTable(),
                    sqlCreateTestTable(),
                    insertTestRecord("ABCDE", "FoundMe"),
                ),
            )
        assertEquals(listOf("Found", "1"), output.map { it.trim() })
    }

    @Test
    open fun notFoundChainLeavesInfdsRrnSubfieldAtItsPreviousValue() {
        // A CHAIN that finds nothing has no row, hence Result.rrn == null: writeInfdsRrn must
        // ignore it - no crash, no overwrite. The program CHAINs a found key first (ABCDE, the
        // table's second row, so RRN 2 rather than a value that could pass by coincidence) and
        // then a missing one, and dsplys XXNREU after each: it must still read 2 after the miss,
        // not fall back to the DS's blank-initialized default (538976288).
        val output =
            outputOfDBPgm(
                "db/INFDSRRNNOTFOUND",
                listOf(createMetadata(accessFields = listOf("KEYTST"))),
                listOf(
                    sqlDropTestTable(),
                    sqlCreateTestTable(),
                    insertTestRecord("AAAAA", "FirstRow"),
                    insertTestRecord("ABCDE", "FoundMe"),
                ),
            )
        assertEquals(listOf("2", "Not found", "2"), output.map { it.trim() })
    }

    @Test
    open fun keyedChainOnRealPostgresPopulatesInfdsRrnSubfield() {
        // PostgreSQL smoke test of keyedChainPopulatesInfdsRrnSubfield: same scenario, but through
        // PostgreSQLDialect (whose RRN parameter needs an explicit CAST) instead of the Default
        // dialect HSQLDB uses. Runs only when a real PostgreSQL is reachable (see
        // isPostgresAvailable's kdoc) - skipped, not failed, otherwise.
        assumeTrue("No PostgreSQL container reachable at localhost:5432 - skipping", isPostgresAvailable())
        val output =
            outputOfDBPgmPostgres(
                "db/INFDSRRNKEYED",
                listOf(createMetadata(accessFields = listOf("KEYTST"))),
                listOf(
                    "DROP TABLE IF EXISTS \"TESTF\"",
                    """
                    CREATE TABLE "TESTF" (
                       "__RNN" BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       "KEYTST" CHAR(5) DEFAULT '' NOT NULL,
                       "DESTST" CHAR(40) DEFAULT '' NOT NULL,
                       UNIQUE("KEYTST")
                    )
                    """.trimIndent(),
                    "INSERT INTO \"TESTF\" (\"KEYTST\", \"DESTST\") VALUES ('ABCDE', 'FoundMe')",
                ),
                configuration =
                    com.smeup.rpgparser.execution
                        .Configuration(),
            )
        // "1": the single inserted row's real __RNN identity value, not a fallback/default - the
        // whole point of this test.
        assertEquals(listOf("Found", "1"), output.map { it.trim() })
    }

    @Test
    open fun infdsResolvesPerFSpecNotPerSharedRenameFormat() {
        // Unkeyed and Keyed share record format TSTFMT via Keyed's RENAME (like
        // ChainUnkeyedFormatDBTest's C5RATER regression setup), but each declares its own INFDS.
        // Unkeyed's CHAIN-by-RRN and Keyed's CHAIN-by-key each get their own real Result.rrn (1,
        // the single row inserted in each table) - the two outputs being independent confirms
        // per-F-spec resolution.
        val output =
            outputOfDBPgm(
                "db/INFDSRRNRENAME",
                listOf(
                    createMetadata(name = "UNKEYED", accessFields = emptyList()),
                    createMetadata(name = "KEYED", accessFields = listOf("KEYTST")),
                ),
                listOf(
                    sqlDropTestTable("UNKEYED"),
                    sqlCreateTestTable("UNKEYED"),
                    insertTestRecord("ABCDE", "FoundMe", "UNKEYED"),
                    sqlDropTestTable("KEYED"),
                    sqlCreateTestTable("KEYED"),
                    insertTestRecord("ABCDE", "FoundMe", "KEYED"),
                ),
                mapOf("rrn" to DecimalValue(BigDecimal.ONE)),
            )
        assertEquals(listOf("1", "1"), output.map { it.trim() })
    }

    private fun createMetadata(
        name: String = "TESTF",
        accessFields: List<String>,
    ) = FileMetadata(
        name = name,
        tableName = name,
        recordFormat = "TSTFMT",
        fields =
            listOf(
                DbField("KEYTST", StringType(5)),
                DbField("DESTST", StringType(40)),
            ),
        accessFields = accessFields,
    )

    private fun sqlCreateTestTable(tableName: String = "TESTF") =
        """
        CREATE TABLE $tableName (
           "__RNN" BIGINT GENERATED ALWAYS AS IDENTITY (START WITH 1) PRIMARY KEY,
           KEYTST CHAR(5) DEFAULT '' NOT NULL,
           DESTST CHAR(40) DEFAULT '' NOT NULL,
           UNIQUE(KEYTST) )
        """.trimIndent()

    // The underlying HSQLDB server (DBServer) is a singleton shared across every test method in
    // this class, so a table created by an earlier test is still there - drop it first.
    private fun sqlDropTestTable(tableName: String = "TESTF") = "DROP TABLE IF EXISTS $tableName"

    private fun insertTestRecord(
        key: String,
        description: String,
        tableName: String = "TESTF",
    ) = "INSERT INTO $tableName (KEYTST, DESTST) VALUES('$key', '$description')"
}
