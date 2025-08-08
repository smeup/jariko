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

package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execute
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.JarikoCallback
import com.smeup.rpgparser.interpreter.CollectorSystemInterface
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Tests for IN and OUT opcodes (data area operations)
 */
class DataAreaTest : AbstractTest() {

    @Test
    fun testDataAreaRead() {
        var dataStore = mutableMapOf<String, String>()

        val systemInterface = object : CollectorSystemInterface() {
            override fun getConfiguration(): Configuration {
                return Configuration().apply {
                    jarikoCallback = JarikoCallback().apply {
                        readDataArea = { dataAreaName ->
                            dataStore[dataAreaName]
                        }
                        writeDataArea = { dataAreaName, value ->
                            dataStore[dataAreaName] = value
                        }
                    }
                }
            }
        }

        // Pre-populate data area for read test
        dataStore["C£C£E00D"] = "TEST_VALUE"

        val cu = assertASTCanBeProduced("DTAREAREAD")
        cu.resolveAndValidate()
        val interpreter = execute(cu, mapOf(), systemInterface)

        // Should display the value that was read from data area
        assertTrue(systemInterface.displayed.any { it.contains("TEST_VALUE") })
    }

    @Test
    fun testDataAreaWrite() {
        var dataStore = mutableMapOf<String, String>()

        val systemInterface = object : CollectorSystemInterface() {
            override fun getConfiguration(): Configuration {
                return Configuration().apply {
                    jarikoCallback = JarikoCallback().apply {
                        readDataArea = { dataAreaName ->
                            dataStore[dataAreaName]
                        }
                        writeDataArea = { dataAreaName, value ->
                            dataStore[dataAreaName] = value
                        }
                    }
                }
            }
        }

        val cu = assertASTCanBeProduced("DTAREAWRITE")
        cu.resolveAndValidate()
        val interpreter = execute(cu, mapOf(), systemInterface)

        // Should have written "WRITTEN" to the data area
        assertEquals("WRITTEN", dataStore["C£C£E00D"])
        assertTrue(systemInterface.displayed.any { it.contains("WRITTEN") })
    }

    @Test
    fun testDataAreaReadWithIndicator() {
        var dataStore = mutableMapOf<String, String>()

        val systemInterface = object : CollectorSystemInterface() {
            override fun getConfiguration(): Configuration {
                return Configuration().apply {
                    jarikoCallback = JarikoCallback().apply {
                        readDataArea = { dataAreaName ->
                            dataStore[dataAreaName]
                        }
                        writeDataArea = { dataAreaName, value ->
                            dataStore[dataAreaName] = value
                        }
                    }
                }
            }
        }

        // Pre-populate data area
        dataStore["C£C£E00D"] = "INDICATOR_TEST"

        val cu = assertASTCanBeProduced("DTAREAREADIND")
        cu.resolveAndValidate()
        val interpreter = execute(cu, mapOf(), systemInterface)

        // Should display the value that was read and the indicator status
        assertTrue(systemInterface.displayed.any { it.contains("INDICATOR_TEST") })
        // Indicator 50 should be OFF (0) since read was successful
        assertTrue(systemInterface.displayed.any { it.contains("0") })
    }
}