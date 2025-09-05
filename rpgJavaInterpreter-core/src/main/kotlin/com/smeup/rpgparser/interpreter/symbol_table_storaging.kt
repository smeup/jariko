package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.logging.ProgramUsageType
import kotlin.system.measureNanoTime
import kotlin.time.Duration.Companion.nanoseconds

interface IMemorySliceStorage : AutoCloseable {
    /**
     * Open the storage
     * */
    fun open()

    /**
     * Load memory associated to memorySliceId
     * */
    fun load(memorySliceId: MemorySliceId): Map<String, Value>

    /**
     * Notify transaction start. Is called before all memory slices storing
     * */
    fun beginTrans()

    /**
     * Store map associated to memory slices
     * */
    fun store(
        memorySliceId: MemorySliceId,
        values: Map<String, Value>,
    )

    /**
     * Called if all memory slices storing process is succesfully completed
     * */
    fun commitTrans()

    /**
     * Called in case of failure
     * */
    fun rollbackTrans()

    /**
     * Close the storage. If do not has been called commitTrans, it could be needed to implement rollback mechanisms
     * */
    override fun close()

    companion object {
        /**
         * Creates (just for tests) an instance of storage wrapped in a map
         * */
        fun createMemoryStorage(map: MutableMap<MemorySliceId, Map<String, Value>>): IMemorySliceStorage =
            object : IMemorySliceStorage {
                override fun open() {
                }

                override fun load(memorySliceId: MemorySliceId) = map.getOrDefault(memorySliceId, mutableMapOf())

                override fun beginTrans() {
                }

                override fun store(
                    memorySliceId: MemorySliceId,
                    values: Map<String, Value>,
                ) {
                    map[memorySliceId] = values
                }

                override fun commitTrans() {
                }

                override fun rollbackTrans() {
                }

                override fun close() {
                }
            }
    }
}

/**
 * Memory slice identifier.
 * */
data class MemorySliceId(
    val activationGroup: String,
    val programName: String,
)

/**
 * Memory slice is that portion of program memory associated to the SymbolTable
 * */
data class MemorySlice(
    val memorySliceId: MemorySliceId,
    val symbolTable: ISymbolTable,
) {
    /**
     * Flag to indicate that memory slice has to be persisted.
     * This property is optional because, if for some reason is not set, the serializer will throw an exception as warranty
     * that something has been wrong, and it needs insights.
     * */
    var persist: Boolean? = null
}

/**
 * Create a memory slice manager
 * */
class MemorySliceMgr(
    private val storage: IMemorySliceStorage,
) {
    private var memorySlices = mutableMapOf<MemorySliceId, MemorySlice>()

    init {
        storage.open()
    }

    private fun getDataDefinition(
        name: String,
        symbolTable: ISymbolTable,
    ): AbstractDataDefinition? = symbolTable.dataDefinitionByName(name)

    private fun encodeDataDefinition(dataDefinition: AbstractDataDefinition): String = dataDefinition.name

    fun afterMainProgramInterpretation(ok: Boolean = true) {
        storage.use {
            if (ok) {
                store()
            }
        }
    }

    /**
     * Associates a symbol table to a memory slice.
     * In every case the association (and initialization of symbol table) is always followed by storage load invocation, for this reason, caching optimization
     * should be handled in IMemorySliceStorage implementation.
     * @param memorySliceId memory identifier
     * @param symbolTable Symbol table associated to the memory slice
     * @param initSymbolTableEntry Contains initialization logic for a single symbol table entry
     * */
    fun associate(
        memorySliceId: MemorySliceId,
        symbolTable: ISymbolTable,
        initSymbolTableEntry: (dataDefinition: AbstractDataDefinition, storedValue: Value) -> Unit = { dataDefinition, storedValue ->
            symbolTable[dataDefinition] = storedValue
        },
    ): MemorySlice {
        val memorySlice = MemorySlice(memorySliceId = memorySliceId, symbolTable = symbolTable)
        memorySlices[memorySliceId] = memorySlice
        storage.load(memorySliceId).forEach { nameToValue ->
            getDataDefinition(nameToValue.key, symbolTable).let { dataDef ->
                initSymbolTableEntry.invoke(dataDef!!, nameToValue.value)
            }
        }
        return memorySlice
    }

    /**
     * Store all memory slices.
     * @throws RuntimeException if something go wrong
     * */
    private fun store() {
        val slicesNotConfigured =
            memorySlices.values.filter {
                it.persist == null
            }
        if (slicesNotConfigured.isNotEmpty()) {
            throw RuntimeException("persist property not set for these slices: $slicesNotConfigured")
        }
        storage.beginTrans()
        memorySlices.values.forEach { slice ->
            val result =
                storage.runCatching {
                    if (slice.persist!!) {
                        val logSource = { LogSourceData(slice.memorySliceId.programName, "") }
                        MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "SYMTBLSTORE", "START"))
                        val values =
                            slice.symbolTable
                                .getValues()
                                .map {
                                    encodeDataDefinition(it.key) to it.value
                                }.toMap()
                        val elapsed =
                            measureNanoTime {
                                storage.store(memorySliceId = slice.memorySliceId, values = values)
                            }.nanoseconds
                        MainExecutionContext.log(LazyLogEntry.produceStatement(logSource, "SYMTBLSTORE", "END"))
                        MainExecutionContext.log(
                            LazyLogEntry.producePerformanceAndUpdateAnalytics(
                                logSource,
                                ProgramUsageType.SymbolTable,
                                SymbolTableAction.STORE.name,
                                elapsed,
                            ),
                        )
                    }
                }
            if (result.isFailure) {
                storage.rollbackTrans()
                throw RuntimeException(result.exceptionOrNull())
            }
        }
        storage.commitTrans()
    }
}
