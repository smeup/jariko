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
import org.hsqldb.Server
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

open class StoreTest() : AbstractTest() {

    companion object {
        lateinit var server: Server
    }

    @Before
    fun init() {
        execute(listOf(createEMPLOYEE()))
    }

    @After
    fun tearOff() {
        execute(listOf(dropEMPLOYEE()))
    }

    @Test
    fun testWrite() {
        outputOfDBPgm(
            programName = "db/WRITE01",
            metadata = listOf(createEmployeeMetadata())
        )
    }

    @Test
    fun testWriteTableNameDifferentByName() {
        outputOfDBPgm(
            programName = "db/WRITE01",
            metadata = listOf(createEmployeeMetadata(tableName = "EMPLOYTN"))
        )
    }

    @Test
    fun testWriteRecordFormatDifferentByName() {
        outputOfDBPgm(
            programName = "db/WRITE01",
            metadata = listOf(createEmployeeMetadata(recordFormat = "EMPLOYRF"))
        )
    }

    // TODO Waiting for evaluation about reload potential issue
    @Ignore
    @Test
    fun testUpdate() {
        outputOfDBPgm(
            programName = "db/UPDATE01",
            metadata = listOf(createEmployeeMetadata())
        )
    }
}