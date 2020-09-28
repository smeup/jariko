package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.executePgmWithStringArgs
import com.smeup.rpgparser.experimental.PropertiesFileStorage
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File

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
}

class MemorySliceStorageTest {
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

    @Test
    fun podSimulationTest() {
        // testing program
        val programName = "ACTGRP_FIX.rpgle"
        val path = javaClass.getResource("/$programName")
        // here I set the path from where jariko will search for the rpg sources
        val rpgProgramFinders = listOf(DirRpgProgramFinder(File(path.path).parentFile))
        // here I set configuration
        val configuration = Configuration(PropertiesFileStorage(simpleStorageTempDir))

        // simulate pod execution
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

    @After
    fun after() {
        if (!DEMO) {
            simpleStorageTempDir.deleteRecursively()
        }
    }
}
