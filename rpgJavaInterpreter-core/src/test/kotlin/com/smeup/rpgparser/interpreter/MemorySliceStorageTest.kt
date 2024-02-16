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

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.JarikoCallback
import com.smeup.rpgparser.experimental.PropertiesFileStorage
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

lateinit var simpleStorageTempDir: File
val DEMO: Boolean = System.getenv("DEMO")?.toBoolean() ?: false

private class SillySymbolTable : ISymbolTable {

    private val values = mutableMapOf<AbstractDataDefinition, Value>()

    /**
     * @return true if SymbolTable contains a variable named dataName
     * */
    override fun contains(dataName: String): Boolean {
        return dataDefinitionByName(dataName) != null
    }

    /**
     * @return true if SymbolTable contains the datadefinition
     * */
    override fun contains(data: AbstractDataDefinition) = values.containsKey(key = data)

    /**
     * @return value associated to data
     * */
    override fun get(data: AbstractDataDefinition): Value = values.get(key = data)!!

    /**
     * @return value associated to variable named dataName
     * */
    override fun get(dataName: String): Value {
        val data = dataDefinitionByName(dataName)
        require(data != null) {
            "Cannot find $dataName"
        }
        return get(data)
    }

    /**
     * @return if exists a datadefinition by variable name
     * */
    override fun dataDefinitionByName(dataName: String) =
        values.keys.firstOrNull { it.name.equals(dataName, ignoreCase = true) }

    /**
     * Set a value for data.
     * @return old value if presents
     * */
    override fun set(data: AbstractDataDefinition, value: Value) = values.put(key = data, value = value)

    /**
     * @return All symbol table values
     * */
    override fun getValues(): Map<AbstractDataDefinition, Value> = values

    fun setValue(name: String, value: String) {
        val fieldDefinition = FieldDefinition(name = name, type = StringType(200), explicitStartOffset = 10,
            explicitEndOffset = 30)
        this[fieldDefinition] = StringValue(value = value)
    }

    /**
     * Clear symbol table
     * */
    override fun clear() = values.clear()

    /**
     * @return if is empty
     * */
    override fun isEmpty() = values.isEmpty()

    override var parentSymbolTable: ISymbolTable?
        get() = TODO("Not yet implemented")
        set(value) {}
}

open class MemorySliceStorageTest : AbstractTest() {
    @Before
    fun before() {
        val lastPathComponent = if (DEMO) {
            "demo"
        } else {
            "${System.currentTimeMillis()}"
        }
        simpleStorageTempDir = File(System.getProperty("java.io.tmpdir"), "/teststorage/$lastPathComponent")
    }

    @Test
    fun propertiesFileStorageTest() {
        val memorySlicePodProducer = MemorySliceMgr(PropertiesFileStorage(simpleStorageTempDir))
        val symbolTableOut = SillySymbolTable()
        symbolTableOut.setValue(name = "myVar", value = "VAL1")
        val memorySliceId = MemorySliceId("group", "programName")
        val memorySlice = memorySlicePodProducer.associate(memorySliceId, symbolTableOut)
        // jariko changes myVar value
        symbolTableOut.setValue(name = "myVar", value = "VAL2")
        memorySlice.persist = true
        // will be stored only memory slices with persist property set to true
        memorySlicePodProducer.afterMainProgramInterpretation()
        val symbolTableIn = SillySymbolTable()
        symbolTableIn.setValue(name = "myVar", value = "Pippo")
        // here have to be different
        assert(symbolTableOut.getValues() != symbolTableIn.getValues())
        val memorySlicePodConsumer = MemorySliceMgr(PropertiesFileStorage(simpleStorageTempDir))
        memorySlicePodConsumer.associate(memorySliceId, symbolTableIn)
        // here have to be the same
        assert(symbolTableOut.getValues() == symbolTableIn.getValues())
    }

    // Test flow:
    // Step 0 - Executing ACTGRP_FIX that exits in RT
    // Step 1 - Executing ACTGRP_FIX that exits in RT
    // Assertions for each step:
    // Step 0 - X = 1
    // Step 1 - X = 2
    // Final assertion:
    // X in storage = 2
    @Test
    fun podSimulationTest() {
        // testing program
        val programName = "ACTGRP_FIX.rpgle"
        val path = javaClass.getResource("/$programName")
        // here I set the path from where jariko will search for the rpg sources
        val rpgProgramFinders = listOf(DirRpgProgramFinder(File(path.path).parentFile))
        var currentStep = 0
        // here I set configuration
        val configuration = Configuration(
            PropertiesFileStorage(simpleStorageTempDir),
            JarikoCallback(
                onExitPgm = { _: String, symbolTable: ISymbolTable, _: Throwable? ->
                    val x = symbolTable["X"]
                    assertEquals(currentStep + 1, x.asInt().value.toInt())
                }

            )
        )
        val times = 2
        repeat(times) {
            // simulate pod execution
            currentStep = it
            println("Executing $programName")
            executePgmWithStringArgs(
                programName = programName,
                programFinders = rpgProgramFinders,
                programArgs = listOf<String>(),
                configuration = configuration
            )
            configuration.memorySliceStorage?.let { memorySliceStorage ->
                if (memorySliceStorage is PropertiesFileStorage) {
                    memorySliceStorage.dumpPropertiesFile()
                }
            }
        }
        val memorySliceId = MemorySliceId(activationGroup = "MYACT", programName = programName)
        val x = configuration.memorySliceStorage!!.load(memorySliceId)["X"]!!
        assertEquals(times, x.asInt().value.toInt())
    }

    // Test flow:
    // Step 0 - Executing ACTGRP_FIX that exits in RT
    // Step 1 - Executing ACTGRP_FIX that exits in LR (forced LR programmatically via callback)
    // Step 2 - Executing ACTGRP_FIX that exits in LR (forced LR programmatically via callback)
    // Assertions for each step:
    // Step 0 - X = 1
    // Step 1 - X = 2
    // Step 2 - X = 2
    // Variable evaluation via JarikoCallback.onExitPgm
    @Test
    fun simulateRTFollowedByLR() {
        val programName = "ACTGRP_FIX.rpgle"
        val path = javaClass.getResource("/$programName")
        // here I set the path from where jariko will search for the rpg sources
        val rpgProgramFinders = listOf(DirRpgProgramFinder(File(path.path).parentFile))
        val memorySliceId = MemorySliceId(activationGroup = "MYACT", programName = programName)
        // I work with same instance of MemorySliceStorage for each test
        val memorySliceStorage = PropertiesFileStorage(simpleStorageTempDir)
        repeat(3) {
            val exitInRT = it == 0
            val configuration = Configuration(
                memorySliceStorage = memorySliceStorage,
                // at the first iteration force RT in order to store a SymbolTable
                jarikoCallback = JarikoCallback(
                    exitInRT = { exitInRT },
                    onExitPgm = { _: String, symbolTable: ISymbolTable, _: Throwable? ->
                        val x = symbolTable["X"]
                        if (it == 0) {
                            assertEquals(1, x.asInt().value.toInt())
                        } else {
                            assertEquals(2, x.asInt().value.toInt())
                        }
                    }
                )
            )
            println("Executing $programName")
            executePgmWithStringArgs(
                programName = programName,
                programFinders = rpgProgramFinders,
                programArgs = listOf<String>(),
                configuration = configuration
            )
            configuration.memorySliceStorage?.let { memorySliceStorage ->
                if (memorySliceStorage is PropertiesFileStorage) {
                    memorySliceStorage.dumpPropertiesFile()
                }
            }
        }
    }

    @After
    fun after() {
        if (!DEMO) {
            simpleStorageTempDir.deleteRecursively()
        }
    }
}
