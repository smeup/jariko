package com.smeup.rpgparser.interpreter

import java.lang.RuntimeException

interface IMemorySliceStorage : AutoCloseable {

    fun open()
    override fun close()
    /**
     * Load memory associated to memorySliceId
     * */
    fun load(memorySliceId: MemorySliceId): Map<String, Value>
    fun beginTrans()
    fun store(memorySliceId: MemorySliceId, values: Map<String, Value>)
    fun commitTrans()
    fun rollbackTrans()
}

/**
 * Memory slice identifier.
 * */
data class MemorySliceId(val sessionId: String, val activationGroup: String, val programName: String)

/**
 * Memory slice is that portion of program memory associated to the SymbolTable
 * */
data class MemorySlice(val memorySliceId: MemorySliceId, val symbolTable: ISymbolTable) {

    /**
     * Flag to indicate that memory slice has to be persist.
     * This property is optional because, if for some reason is not set, the serializer will throw an exception as warranty
     * that something has been wrong, and it needs insights.
     * */
    var persist: Boolean? = null
}

/**
 * Create a memory slice manager
 * */
class MemorySliceMgr(private val storage: IMemorySliceStorage) {

    private var memorySlices = mutableMapOf<MemorySliceId, MemorySlice>()

    private fun getDataDefinition(name: String, symbolTable: ISymbolTable): AbstractDataDefinition? {
        return symbolTable.dataDefinitionByName(name)
    }

    private fun encodeDataDefinition(dataDefinition: AbstractDataDefinition): String {
        return dataDefinition.name
    }

    /**
     * Associate a symbol table to a memory slice.
     * At first time association, symbol table will be initialized by loading of persistent data through IMemorySliceStorage
     * */
    fun associate(memorySliceId: MemorySliceId, symbolTable: ISymbolTable): MemorySlice {
        return memorySlices.computeIfAbsent(memorySliceId) {
            storage.load(it).forEach() { nameToValue ->
                getDataDefinition(nameToValue.key, symbolTable).let { dataDef ->
                    symbolTable[dataDef!!] = nameToValue.value
                }
            }
            MemorySlice(memorySliceId = memorySliceId, symbolTable = symbolTable)
        }
    }

    fun store() {
        val slicesNotConfigured = memorySlices.values.filter {
            it.persist == null
        }
        if (slicesNotConfigured.isNotEmpty()) {
            throw RuntimeException("persist property not set for these slices: $slicesNotConfigured")
        }
        memorySlices.values.forEach { slice ->
            val result = storage.runCatching {
                beginTrans()
                if (slice.persist!!) {
                    val values = slice.symbolTable.getValues().map {
                        encodeDataDefinition(it.key) to it.value
                    }.toMap()
                    storage.store(memorySliceId = slice.memorySliceId, values = values)
                }
                commitTrans()
            }
            if (result.isFailure) {
                storage.rollbackTrans()
                throw RuntimeException(result.exceptionOrNull())
            }
        }
    }
}
