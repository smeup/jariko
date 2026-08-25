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

    // storage.store() only ever runs once, at the end of the whole request (afterMainProgramInterpretation),
    // so nothing in the backing storage can change between two loads of the same slice within one request.
    // Caching here means repeat/nested calls to the same program reuse the first load instead of re-hitting
    // storage each time.
    private val loadedValues = mutableMapOf<MemorySliceId, Map<String, Value>>()

    init {
        storage.open()
    }

    fun getSize() = memorySlices.size

    fun remove(memorySliceId: MemorySliceId) = memorySlices.remove(memorySliceId)

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
     * Associates a symbol table to a memory slice. This registration always happens, regardless of [load]:
     * it is what makes the slice eligible for the batched store() at the end of the request, and what lets
     * doSomethingAfterExecution find it again to mark it persist-worthy.
     * When [load] is true, the association is additionally followed by a storage load - but the physical load
     * only happens once per memorySliceId per MemorySliceMgr instance (i.e. once per request); subsequent
     * associations for the same id reuse the first load's result. Callers pass load=false when the symbol
     * table already holds this program's authoritative in-memory state (e.g. a program re-entered within the
     * same request without having cleared its table), so a reload would overwrite it with stale data.
     * @param memorySliceId memory identifier
     * @param symbolTable Symbol table associated to the memory slice
     * @param load whether to also perform (or reuse the cached result of) the physical storage load
     * @param initSymbolTableEntry Contains initialization logic for a single symbol table entry
     * */
    fun associate(
        memorySliceId: MemorySliceId,
        symbolTable: ISymbolTable,
        load: Boolean = true,
        initSymbolTableEntry: (dataDefinition: AbstractDataDefinition, storedValue: Value) -> Unit = { dataDefinition, storedValue ->
            symbolTable[dataDefinition] = storedValue
        },
    ): MemorySlice {
        val memorySlice = MemorySlice(memorySliceId = memorySliceId, symbolTable = symbolTable)
        memorySlices[memorySliceId] = memorySlice
        if (load) {
            val values = loadedValues.getOrPut(memorySliceId) { storage.load(memorySliceId) }
            values.forEach { nameToValue ->
                getDataDefinition(nameToValue.key, symbolTable).let { dataDef ->
                    initSymbolTableEntry.invoke(dataDef!!, nameToValue.value)
                }
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
