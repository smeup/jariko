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
import com.smeup.rpgparser.db.utilities.createEMPLOYEE
import com.smeup.rpgparser.db.utilities.createEmployeeMetadata
import com.smeup.rpgparser.db.utilities.dropEMPLOYEE
import com.smeup.rpgparser.db.utilities.execute
import com.smeup.rpgparser.db.utilities.insertRecords
import com.smeup.rpgparser.interpreter.StringValue
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import kotlin.test.assertEquals

/**
 * The EQ resulting indicator (columns 75-76) of SETLL must be set on when a record with a key equal to the
 * search argument exists, and set off otherwise.
 */
open class SetllEqIndicatorTest : AbstractTest() {
    companion object {
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

    private fun setllOutput(key: String) =
        outputOfDBPgm(
            "db/SETLLEQIND",
            listOf(createEmployeeMetadata()),
            emptyList(),
            mapOf("toFind" to StringValue(key)),
        )

    @Test
    fun eqIndicatorIsOnWhenTheKeyExists() {
        assertEquals(listOf("EQ=1"), setllOutput("000030"))
    }

    @Test
    fun eqIndicatorIsOffWhenTheKeyDoesNotExist() {
        assertEquals(listOf("EQ=0"), setllOutput("000031"))
    }
}
