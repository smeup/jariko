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
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import kotlin.test.assertEquals

/**
 * READ after CHAIN, and file state after CLOSE/OPEN and across calls.
 * IBM i semantics followed (NOT yet proved on IBM i, see docs/ibmi-proof): READ after a successful CHAIN returns the
 * record following the chained one; CLOSE+OPEN, or the end of a program that exits with LR, resets the file position
 * so the next READ returns the first record.
 */
open class ReadAfterChainTest : AbstractTest() {
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

    private fun run(program: String) = outputOfDBPgm(program, listOf(createEmployeeMetadata()), emptyList(), emptyMap())

    @Test
    fun readAfterChainReturnsNextRecord() {
        assertEquals(listOf("000030", "000050"), run("db/READAFTCH1"))
    }

    @Test
    fun closeAndOpenResetThePositionAfterChain() {
        assertEquals(listOf("000030", "000010"), run("db/READAFTCH2"))
    }

    @Test
    fun chainOfPreviousCallDoesNotLeakIntoNextCall() {
        assertEquals(listOf("000030", "000010"), run("db/READAFTCH3"))
    }
}
