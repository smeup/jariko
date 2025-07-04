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

package com.smeup.rpgparser.interpreter

import kotlin.test.Test
import kotlin.test.assertEquals
import com.smeup.dbnative.sql.SQLDBFile
import com.smeup.dbnative.model.FileMetadata
import com.smeup.rpgparser.db.utilities.DBServer
import java.sql.DriverManager
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

class InterpreterStatusTest {

    lateinit var connection: java.sql.Connection
    lateinit var mainSymbolTable: SymbolTable
    lateinit var interpreterStatus: InterpreterStatus
    lateinit var functionStatus: InterpreterStatus

    @BeforeTest
    fun setup() {
        DBServer.startDB()
        Class.forName("org.hsqldb.jdbc.JDBCDriver")
        connection = DriverManager.getConnection("jdbc:hsqldb:hsql://127.0.0.1:9001/mainDb", "SA", "")
        mainSymbolTable = SymbolTable()
        interpreterStatus = InterpreterStatus(symbolTable = mainSymbolTable, indicators = HashMap())
        functionStatus = InterpreterStatus(symbolTable = SymbolTable(), indicators = HashMap()).apply {
            indicators = interpreterStatus.indicators
            klists = interpreterStatus.klists
            dbFileMap = interpreterStatus.dbFileMap
            lastDBFile = interpreterStatus.lastDBFile
            inzsrExecuted = interpreterStatus.inzsrExecuted
            displayFiles = interpreterStatus.displayFiles
            lastFound = interpreterStatus.lastFound
            mainSymbolTable.parentSymbolTable = interpreterStatus.symbolTable
        }
    }

    @AfterTest
    fun tearDown() {
        connection.close()
        DBServer.stopDB()
    }

    @Test
    fun indicatorsShouldBeShared() {
        // If I set an indicator in the interpreter status, it should be reflected in the function status
        interpreterStatus.indicators[1] = BooleanValue.TRUE
        assertEquals(interpreterStatus.indicators, functionStatus.indicators)

        // If I change the indicator in the function status, it should be reflected in the interpreter status
        functionStatus.indicators[1] = BooleanValue.FALSE
        assertEquals(functionStatus.indicators, interpreterStatus.indicators)
    }

    @Test
    fun keyListsShouldBeShared() {
        // If I set a key list in the interpreter status, it should be reflected in the function status
        interpreterStatus.klists["a"] = listOf("value1", "value2")
        assertEquals(interpreterStatus.klists, functionStatus.klists)

        // If I change the key list in the function status, it should be reflected in the interpreter status
        functionStatus.klists["b"] = listOf("value3", "value4")
        assertEquals(functionStatus.klists, interpreterStatus.klists)
    }

    @Test
    fun lastDbFileShouldBeShared() {
        val lastDBFile = SQLDBFile(
            name = "TEST",
            fileMetadata = FileMetadata(name = "MYFILE", tableName = "MYTABLE", fields = emptyList(), fileKeys = emptyList()),
            connection = connection
        )
        // If I set the last DB file in the interpreter status, it should be reflected in the function status
        interpreterStatus.lastDBFile = lastDBFile
        assertEquals(lastDBFile, functionStatus.lastDBFile)

        functionStatus.lastDBFile = null
        assertEquals(functionStatus.lastDBFile, interpreterStatus.lastDBFile)
    }

    @Test
    fun inzsrExecutedShouldBeShared() {
        // If I set inzsrExecuted in the interpreter status, it should be reflected in the function status
        interpreterStatus.inzsrExecuted = true
        assertEquals(interpreterStatus.inzsrExecuted, functionStatus.inzsrExecuted)

        // If I change inzsrExecuted in the function status, it should be reflected in the interpreter status
        functionStatus.inzsrExecuted = false
        assertEquals(functionStatus.inzsrExecuted, interpreterStatus.inzsrExecuted)
    }

    @Test
    fun lastFoundShouldBeShared() {
        // If I set lastFound in the interpreter status, it should be reflected in the function status
        interpreterStatus.lastFound = true
        assertEquals(interpreterStatus.lastFound, functionStatus.lastFound)

        // If I change lastFound in the function status, it should be reflected in the interpreter status
        functionStatus.lastFound = false
        assertEquals(functionStatus.lastFound, interpreterStatus.lastFound)
    }
}