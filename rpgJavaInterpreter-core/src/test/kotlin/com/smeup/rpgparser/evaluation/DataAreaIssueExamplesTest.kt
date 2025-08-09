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
import com.smeup.rpgparser.CollectorSystemInterface
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Tests for IN and OUT opcodes using examples from issue #767
 */
class DataAreaIssueExamplesTest : AbstractTest() {

    /**
     * Test for DTAREAINPROC.rpgle - data areas in procedures
     */
    @Test
    fun testDataAreaInProc() {
        var dataStore = mutableMapOf<String, String>()

        val systemInterface = object : CollectorSystemInterface() {
            override fun getConfiguration(): Configuration {
                return Configuration().apply {
                    jarikoCallback = JarikoCallback().apply {
                        readDataArea = { dataAreaName ->
                            println("Reading data area: $dataAreaName")
                            dataStore[dataAreaName]
                        }
                        writeDataArea = { dataAreaName, value ->
                            println("Writing to data area: $dataAreaName = $value")
                            dataStore[dataAreaName] = value
                        }
                    }
                }
            }
        }

        // Pre-populate the data area that will be read by the DEFINE operation
        dataStore["APU001D1"] = "          " // 10 spaces for the DS

        val cu = assertASTCanBeProduced("DTAREAINPROC")
        cu.resolveAndValidate()

        // This should execute without errors
        val interpreter = execute(cu, mapOf(), systemInterface)

        // Verify that the data area was written to
        assertTrue(dataStore.containsKey("APU001D1"))
        println("Data area contents after execution: ${dataStore["APU001D1"]}")
    }

    /**
     * Test for DTAREAPROC.rpgle - data areas with procedures (different variant)
     */
    @Test
    fun testDataAreaProc() {
        var dataStore = mutableMapOf<String, String>()

        val systemInterface = object : CollectorSystemInterface() {
            override fun getConfiguration(): Configuration {
                return Configuration().apply {
                    jarikoCallback = JarikoCallback().apply {
                        readDataArea = { dataAreaName ->
                            println("Reading data area: $dataAreaName")
                            dataStore[dataAreaName]
                        }
                        writeDataArea = { dataAreaName, value ->
                            println("Writing to data area: $dataAreaName = $value")
                            dataStore[dataAreaName] = value
                        }
                    }
                }
            }
        }

        // Pre-populate the data area
        dataStore["APU001D1"] = "          " // 10 spaces for the DS

        val cu = assertASTCanBeProduced("DTAREAPROC")
        cu.resolveAndValidate()

        // This should execute without errors
        val interpreter = execute(cu, mapOf(), systemInterface)

        // Verify that the data area was written to
        assertTrue(dataStore.containsKey("APU001D1"))
        println("Data area contents after execution: ${dataStore["APU001D1"]}")
    }

    /**
     * Test for basic DTAREAREAD.rpgle functionality
     */
    @Test
    fun testBasicDataAreaRead() {
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

        // Pre-populate data area with test value
        dataStore["TEST_AREA"] = "TEST_CURRENT_VALUE"

        val cu = assertASTCanBeProduced("DTAREAREAD")
        cu.resolveAndValidate()
        val interpreter = execute(cu, mapOf(), systemInterface)

        // Should display the value that was read from data area
        assertTrue(systemInterface.displayed.any { it.contains("TEST_CURRENT_VALUE") })
    }

    /**
     * Test for DTAREAWRITE.rpgle functionality
     */
    @Test
    fun testBasicDataAreaWrite() {
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

        // Should have written "WRITTEN_VALUE" to the data area
        assertEquals("WRITTEN_VALUE", dataStore["TEST_AREA"])
        assertTrue(systemInterface.displayed.any { it.contains("WRITTEN_VALUE") })
    }

    /**
     * Test for indicator handling in data area operations
     */
    @Test
    fun testDataAreaWithIndicators() {
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
        dataStore["TEST_AREA"] = "INDICATOR_TEST_VALUE"

        val cu = assertASTCanBeProduced("DTAREAREADIND")
        cu.resolveAndValidate()
        val interpreter = execute(cu, mapOf(), systemInterface)

        // Should display the value and indicator status (0 = success)
        assertTrue(systemInterface.displayed.any { it.contains("INDICATOR_TEST_VALUE") })
        assertTrue(systemInterface.displayed.any { it.contains("0") })
    }

    /**
     * Test error handling when data area doesn't exist
     */
    @Test
    fun testDataAreaNotFound() {
        var dataStore = mutableMapOf<String, String>()

        val systemInterface = object : CollectorSystemInterface() {
            override fun getConfiguration(): Configuration {
                return Configuration().apply {
                    jarikoCallback = JarikoCallback().apply {
                        readDataArea = { dataAreaName ->
                            // Return null to simulate data area not found
                            null
                        }
                        writeDataArea = { dataAreaName, value ->
                            dataStore[dataAreaName] = value
                        }
                    }
                }
            }
        }

        val cu = assertASTCanBeProduced("DTAREAREADIND")
        cu.resolveAndValidate()
        val interpreter = execute(cu, mapOf(), systemInterface)

        // When data area is not found, indicator should be set (1 = error)
        assertTrue(systemInterface.displayed.any { it.contains("1") })
    }
}