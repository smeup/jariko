package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.experimental.PropertiesFileStorage
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File

lateinit var simpleStorageTempDir: File

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
        simpleStorageTempDir = File(System.getProperty("java.io.tmpdir"), "/teststorage/${System.currentTimeMillis()}")
    }

    @Test
    fun propertiesFileStorageTest() {
        val memorySlicePodProducer = MemorySliceMgr(PropertiesFileStorage(simpleStorageTempDir))
        val symbolTableOut = SillySymbolTable()
        symbolTableOut.setValue(name = "myVar", value = "Ciao")
        val memorySliceId = MemorySliceId("sessionId", "group", "programName")
        val memorySlice = memorySlicePodProducer.associate(memorySliceId, symbolTableOut)
        memorySlice.persist = true
        memorySlicePodProducer.store()
        val symbolTableIn = SillySymbolTable()
        symbolTableIn.setValue(name = "myVar", value = "Pippo")
        // here have to be different
        assert(symbolTableOut.getValues() != symbolTableIn.getValues())
        val memorySlicePodConsumer = MemorySliceMgr(PropertiesFileStorage(simpleStorageTempDir))
        memorySlicePodConsumer.associate(memorySliceId, symbolTableIn)
        // here have to be the same
        assert(symbolTableOut.getValues() == symbolTableIn.getValues())
    }

    @After
    fun after() {
        simpleStorageTempDir.deleteRecursively()
    }
}
