package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.DebugOptions
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
        val programName = "ACTGRP_FIX.rpgle"
        val path = javaClass.getResource("/$programName")
        // Debug options: I force for each program the activation group name as ACTVGRP and RT mode exit set to true
        val debugOptions = DebugOptions(
            getActivationGroup = { "ACTGRP" },
            exitInRT = { false }
        )
        // Create test configuration
        // Set as storage a simple implementation properties file based (PropertiesFileStorage)
        val memorySliceStorage = PropertiesFileStorage(simpleStorageTempDir)
        val configuration = Configuration(
            memorySliceStorage = memorySliceStorage,
            debugOptions = debugOptions
        )
        // finally I set the RPG program finder needed to interpret programName sited in a specific path
        val rpgProgramFinders = listOf(DirRpgProgramFinder(File(path.path).parentFile))

        // simulate five execution in POD
        // for each executePgmWithStringArg invocation, which we are using to simulate a like-POD execution environment, it will be created
        // a new instance of InternalInterpreter, and every InternalInterpreter instance has an own SymbolTable instance
        // for this reason I believe that test is trust enough
        repeat(5) {
            println("POD($it) execution")
            executePgmWithStringArgs(programName = programName, programFinders = rpgProgramFinders, programArgs = listOf<String>(), configuration = configuration)
            memorySliceStorage.dumpPropertiesFile()
        }
    }

    @After
    fun after() {
        if (!DEMO) {
            simpleStorageTempDir.deleteRecursively()
        }
    }
}
