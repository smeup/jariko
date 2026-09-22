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
import com.smeup.rpgparser.interpreter.DbField
import com.smeup.rpgparser.interpreter.DecimalValue
import com.smeup.rpgparser.interpreter.FileMetadata
import com.smeup.rpgparser.interpreter.StringType
import org.junit.Test
import java.math.BigDecimal
import kotlin.test.assertEquals

/**
 * Regression for the C5RATER bug: an unkeyed F-spec with no RENAME (UNKEYED, native format
 * TSTFMT) shares its format name with a keyed F-spec that RENAMEs its own format to it (KEYED,
 * rename(TSTFMT:TSTFM2)). `CHAIN TSTFMT` (the bare, shared format name) must deterministically
 * resolve to UNKEYED - the first F-spec declaring TSTFMT - and, since UNKEYED has no access
 * fields, be treated as a Relative Record Number chain rather than crashing
 * (`DBFileMap`/`createKList`, see Fix 1/Fix 2) or being rejected by reload (RRN support).
 */
open class ChainUnkeyedFormatDBTest : AbstractTest() {
    @Test
    open fun executeCHAINUNKEYEDFORMAT() {
        assertEquals(
            listOf("Found: FoundMe"),
            outputOfDBPgm(
                "db/CHAINUNKEYEDFORMAT",
                listOf(createMetadata("UNKEYED", accessFields = emptyList()), createMetadata("KEYED", accessFields = listOf("KEYTST"))),
                listOf(
                    sqlCreateTestTable("UNKEYED"),
                    recordFormatTestTable("UNKEYED"),
                    insertTestRecord("UNKEYED", "ABCDE", "FoundMe"),
                    sqlCreateTestTable("KEYED"),
                    recordFormatTestTable("KEYED"),
                ),
                mapOf("rrn" to DecimalValue(BigDecimal.ONE)),
            ),
        )
    }

    private fun createMetadata(
        name: String,
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
        accessFields,
    )

    // A physical PRIMARY KEY is kept on the "unkeyed" table too: reload's Relative Record Number
    // support derives row order from the table's real SQL primary key, independently of whether
    // the RPG side declares any access fields (accessFields=emptyList() is what makes UNKEYED
    // unkeyed from jariko's point of view).
    private fun sqlCreateTestTable(name: String) =
        """
        CREATE TABLE $name (
           "__RNN" BIGINT GENERATED ALWAYS AS IDENTITY (START WITH 1) PRIMARY KEY,
           KEYTST CHAR(5) DEFAULT '' NOT NULL,
           DESTST CHAR(40) DEFAULT '' NOT NULL,
           UNIQUE(KEYTST) )
        """.trimIndent()

    private fun recordFormatTestTable(tableName: String) = "COMMENT ON TABLE $tableName IS 'TSTFMT'"

    private fun insertTestRecord(
        tableName: String,
        key: String,
        description: String,
    ) = "INSERT INTO $tableName (KEYTST, DESTST) VALUES('$key', '$description')"
}
