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
import com.smeup.rpgparser.interpreter.*
import org.junit.Test
import kotlin.test.assertEquals

open class Chain2FilesDBTest : AbstractTest() {

    @Test
    open fun executeCHAIN2FILE() {
        assertEquals(
            listOf("Not found in First", "2nd: SomeDescription"),
            outputOfDBPgm(
                "db/CHAIN2FILE",
                listOf(createMetadata("FIRST"), createMetadata("SECOND")),
                listOf(
                    sqlCreateTestTable("FIRST"), recordFormatTestTable("FIRST"),
                    sqlCreateTestTable("SECOND"), recordFormatTestTable("SECOND"), insertTestRecords("SECOND")
                ),
                mapOf("toFind" to StringValue("ABCDE"))
            )
        )
    }

    private fun createMetadata(name: String) = FileMetadata(
        name = name,
        tableName = name,
        recordFormat = "TSTREC",
        fields = listOf(
            DbField("KEYTST", StringType(5)),
            DbField("DESTST", StringType(40)),
            DbField("NBRTST", NumberType(2, 0))
        ),
        listOf("KEYTST")
    )

    private fun sqlCreateTestTable(name: String) =
        """
        CREATE TABLE $name (
           KEYTST CHAR(5) DEFAULT '' NOT NULL,
           DESTST CHAR(40) DEFAULT '' NOT NULL,      
           NBRTST DECIMAL(2, 0) DEFAULT 0 NOT NULL,   
           PRIMARY KEY(KEYTST) )
        """.trimIndent()

    private fun recordFormatTestTable(tableName: String) = "COMMENT ON TABLE $tableName IS 'TSTREC'"

    private fun insertTestRecords(tableName: String) =
        "INSERT INTO $tableName (KEYTST, DESTST) VALUES('ABCDE', 'SomeDescription'), ('XYZ', 'OtherDescription')"
}
