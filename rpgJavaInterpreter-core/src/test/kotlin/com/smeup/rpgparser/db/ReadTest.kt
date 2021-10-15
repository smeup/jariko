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
import com.smeup.rpgparser.db.utilities.execute
import com.smeup.rpgparser.interpreter.StringValue
import org.hsqldb.Server
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import kotlin.test.Ignore
import kotlin.test.assertEquals

open class ReadTest : AbstractTest() {

    companion object {

        lateinit var server: Server

        @BeforeClass
        @JvmStatic
        fun init() {
            execute(listOf(createEMPLOYEE(), insertRecords()))
        }

        @AfterClass
        @JvmStatic
        fun tearOff() {
            execute(listOf(dropEMPLOYEE()))
        }
    }

    @Test
    fun equalWithNoSetllReturnsFalse() {
        val outputLines = outputOfDBPgm(
            "db/EQUALNOSET",
            listOf(createEmployeeMetadata()),
            emptyList(),
            emptyMap()
        )
        assertEquals(listOf("EQUAL=0"), outputLines)
    }

    @Test
    fun doesNotFindNonExistingRecord() {
        assertEquals(
            listOf("Not found"),
            outputOfDBPgm(
                "db/CHAINREADE",
                listOf(createEmployeeMetadata()),
                emptyList(),
                mapOf("toFind" to StringValue("XXX"))
            )
        )
    }

    @Test
    @Ignore
    fun findsExistingRecords2() {
        assertEquals(
            // TODO is the order of results mandatory?
            listOf("SALLY KWAN", "DELORES QUINTANA", "HEATHER NICHOLLS", "KIM NATZ"),
            outputOfDBPgm(
                "db/CHAINREADE",
                listOf(createEmployeeMetadata()),
                emptyList(),
                mapOf("toFind" to StringValue("C01"))
            )
        )
    }
}
