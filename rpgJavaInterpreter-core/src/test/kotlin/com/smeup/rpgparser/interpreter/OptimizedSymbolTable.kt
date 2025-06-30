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

import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.logging.ProgramUsageType
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import kotlin.time.Duration.Companion.nanoseconds

/**
 * Optimized Symbol Table implementation with two-tier caching architecture.
 *
 * This implementation provides significant performance improvements through:
 * - Dual hot caches for both AbstractDataDefinition and String-based access
 * - Frequency-based cache promotion
 * - Optimized name resolution caching
 * - Reduced logging overhead
 */
class OptimizedSymbolTable : ISymbolTable {
    // Hot cache for frequently accessed symbols by name (String -> Value mapping)
    private val hotCacheByName = ConcurrentHashMap<String, Value>(64)

    // Hot cache for frequently accessed symbols by data definition (AbstractDataDefinition -> Value mapping)
    private val hotCacheByData = ConcurrentHashMap<AbstractDataDefinition, Value>(64)

    // Regular storage (same as original implementation)
    private val values = LinkedHashMap<AbstractDataDefinition, Value>()
    private val names = mutableMapOf<String, AbstractDataDefinition>()

    // Access frequency tracking for both access patterns
    private val accessCountsByName = ConcurrentHashMap<String, AtomicInteger>()
    private val accessCountsByData = ConcurrentHashMap<AbstractDataDefinition, AtomicInteger>()

    // Cache for dataDefinitionByName lookups
    private val definitionCache = ConcurrentHashMap<String, AbstractDataDefinition?>(128)

    // Threshold for promotion to hot cache
    private val HOT_THRESHOLD = 5

    // Performance metrics tracking
    private var operationCount = 0
    private val LOG_BATCH_SIZE = 100
    private val performanceMetrics = HashMap<SymbolTableAction, Long>()

    override var parentSymbolTable: ISymbolTable? = null

    override operator fun contains(dataName: String): Boolean = dataDefinitionByName(dataName) != null
    override operator fun contains(data: AbstractDataDefinition): Boolean = data in values

    override operator fun get(data: AbstractDataDefinition): Value {
        return if (MainExecutionContext.isLoggingEnabled) {
            measureAndLog(SymbolTableAction.GET) { getOptimized(data) }
        } else {
            getOptimized(data)
        }
    }

    override operator fun get(dataName: String): Value {
        return if (MainExecutionContext.isLoggingEnabled) {
            measureAndLog(SymbolTableAction.GET) { getOptimized(dataName) }
        } else {
            getOptimized(dataName)
        }
    }

    private fun getOptimized(data: AbstractDataDefinition): Value {
        // Regular path - hot cache is handled in getLocal/getInternal
        return getInternal(data)
    }

    private fun getOptimized(dataName: String): Value {
        // Fast path: check hot cache first for name-based lookups
        val upperCaseName = dataName.uppercase()
        val hotValue = hotCacheByName[upperCaseName]
        if (hotValue != null) {
            return hotValue
        }

        // Regular path
        val value = getInternal(dataName)

        // Track access and potentially promote to hot cache
        val count = accessCountsByName.computeIfAbsent(upperCaseName) { AtomicInteger(0) }
        if (count.incrementAndGet() >= HOT_THRESHOLD) {
            hotCacheByName[upperCaseName] = value
        }

        return value
    }

    override fun dataDefinitionByName(dataName: String): AbstractDataDefinition? {
        val upperCaseName = dataName.uppercase()

        // Check cache first
        return definitionCache.computeIfAbsent(upperCaseName) { key ->
            // Original lookup logic, but with cached result
            names.compute(key) { _, value ->
                value ?: names
                    .asSequence()
                    .filter { name ->
                        name.value.type is DataStructureType &&
                                !(name.value.type as AbstractDataStructureType).isQualified &&
                                name.value is DataDefinition
                    }
                    .map { it.value }
                    .flatMap { dataStructure -> (dataStructure as DataDefinition).fields }
                    .firstOrNull { field -> field.name.equals(key, ignoreCase = true) } as AbstractDataDefinition?
            } ?: parentSymbolTable?.dataDefinitionByName(dataName)
        }
    }

    override operator fun set(data: AbstractDataDefinition, value: Value): Value? {
        val start = System.nanoTime()

        val output = when (data.scope.visibility) {
            Visibility.Program -> (programSymbolTable as OptimizedSymbolTable).setLocal(data, value)
            Visibility.Local -> setLocal(data, value)
            Visibility.Static -> (getStaticSymbolTable(data.scope.reference!!) as OptimizedSymbolTable).setLocal(data, value)
        }

        // Only update name-based cache if this name is already being tracked (has been accessed)
        // This prevents initial setup operations from polluting the hot cache
        val upperCaseName = data.name.uppercase()
        if (accessCountsByName.containsKey(upperCaseName)) {
            hotCacheByName[upperCaseName] = value
        }

        val elapsed = System.nanoTime() - start
        if (MainExecutionContext.isLoggingEnabled) {
            measureAndLog(SymbolTableAction.SET) { }
        }

        return output
    }

    private fun setLocal(data: AbstractDataDefinition, value: Value): Value? {
        // Same logic as original SymbolTable but with cache updates
        require(!(data !in this && data.name in names)) {
            "This data definition would conflict with an existing data definition with the same name. This data definition: $data. Existing data definition: ${this[data.name]}"
        }
        require(data.type.canBeAssigned(value)) {
            "Value $value cannot be assigned to data: $data"
        }

        val result = if (data is FieldDefinition) {
            val containerValue = get(data.container)
            if (data.container.isArray()) {
                throw IllegalStateException("We do not yet handle an array container")
            } else if (data.declaredArrayInLine != null) {
                ProjectedArrayValue.forData(containerValue as DataStructValue, data).container.set(data, value.forType(data.type))
                ProjectedArrayValue.forData(containerValue as DataStructValue, data)
            } else {
                when (containerValue) {
                    is DataStructValue -> {
                        containerValue.set(data, value.forType(data.type))
                        coerce(containerValue[data], data.type)
                    }
                    is OccurableDataStructValue -> {
                        containerValue.value().set(data, value.forType(data.type))
                        coerce(containerValue.value()[data], data.type)
                    }
                    else -> {
                        throw IllegalStateException("The container value is expected to be a DataStructValue, instead it is $containerValue")
                    }
                }
            }
        } else {
            names[data.name.uppercase()] = data
            values.put(data, value.forType(data.type))
        }

        // Only update hot cache if this data definition is already being tracked (has been accessed)
        // This prevents initial setup operations from polluting the hot cache
        if (accessCountsByData.containsKey(data)) {
            hotCacheByData[data] = value.forType(data.type)
        }

        return result
    }

    override fun getValues(): Map<AbstractDataDefinition, Value> {
        return values
    }

    override fun clear() {
        values.clear()
        names.clear()
        hotCacheByName.clear()
        hotCacheByData.clear()
        accessCountsByName.clear()
        accessCountsByData.clear()
        definitionCache.clear()
        performanceMetrics.clear()
        operationCount = 0
    }

    override fun isEmpty() = values.isEmpty()

    // Cache invalidation when symbol table structure changes
    private fun invalidateHotCaches() {
        hotCacheByName.clear()
        hotCacheByData.clear()
        accessCountsByName.clear()
        accessCountsByData.clear()
        definitionCache.clear()
    }

    private fun getInternal(data: AbstractDataDefinition): Value = when (data.scope.visibility) {
        Visibility.Program -> (programSymbolTable as OptimizedSymbolTable).getLocal(data)
        Visibility.Local -> getLocal(data)
        Visibility.Static -> (getStaticSymbolTable(data.scope.reference!!) as OptimizedSymbolTable).getLocal(data)
    }

    private fun getInternal(dataName: String): Value {
        val data = dataDefinitionByName(dataName)
        if (data != null) {
            return get(data)
        }
        return findInFields(dataName)
    }

    private fun getLocal(data: AbstractDataDefinition): Value {
        // Fast path: check hot cache first for direct data definition access
        val hotValue = hotCacheByData[data]
        if (hotValue != null) {
            return hotValue
        }

        val value = if (data is FieldDefinition) {
            val containerValue = get(data.container)
            when {
                data.container.isArray() -> TODO("We do not yet handle an array container")
                containerValue is OccurableDataStructValue -> coerce(containerValue.value()[data], data.type)
                data.declaredArrayInLine != null -> ProjectedArrayValue.forData(containerValue as DataStructValue, data)
                else -> {
                    when (containerValue) {
                        is DataStructValue -> coerce(containerValue[data], data.type)
                        else -> {
                            throw IllegalStateException("The container value is expected to be a DataStructValue, instead it is $containerValue")
                        }
                    }
                }
            }
        } else {
            values[data] ?: throw IllegalArgumentException("Cannot find searched value for $data")
        }

        // Track access and potentially promote to hot cache
        val count = accessCountsByData.computeIfAbsent(data) { AtomicInteger(0) }
        if (count.incrementAndGet() >= HOT_THRESHOLD) {
            hotCacheByData[data] = value
        }

        return value
    }

    private fun findInFields(dataName: String): Value {
        values
            .filterKeys { it is DataDefinition }
            .forEach {
                val field = (it.key as DataDefinition).fields.firstOrNull {
                        field -> field.name.equals(dataName, ignoreCase = true) && field.canBeUsedUnqualified()
                }
                if (field != null) {
                    return if (it.key.type is ArrayType) {
                        TODO("We do not yet handle top level values of array type")
                    } else {
                        (it.value as DataStructValue)[field]
                    }
                }
            }

        throw IllegalArgumentException("Cannot find searchedValued for $dataName")
    }

    private inline fun <T> measureAndLog(action: SymbolTableAction, block: () -> T): T {
        val start = System.nanoTime()
        val result = block()
        val elapsed = System.nanoTime() - start

        // Accumulate metrics instead of logging every operation
        performanceMetrics[action] = (performanceMetrics[action] ?: 0L) + elapsed

        // Only log periodically
        if (++operationCount % LOG_BATCH_SIZE == 0) {
            flushPerformanceMetrics()
        }

        return result
    }

    private fun flushPerformanceMetrics() {
        val programName = MainExecutionContext.getExecutionProgramName()
        performanceMetrics.forEach { (action, totalTime) ->
            MainExecutionContext.log(
                LazyLogEntry.producePerformanceAndUpdateAnalytics(
                    { LogSourceData.fromProgram(programName) },
                    ProgramUsageType.SymbolTable,
                    action.name,
                    totalTime.nanoseconds
                )
            )
        }
        performanceMetrics.clear()
    }

    // Statistics methods for testing
    fun getHotCacheStats(): HotCacheStats {
        return HotCacheStats(
            hotCacheByNameSize = hotCacheByName.size,
            hotCacheByDataSize = hotCacheByData.size,
            totalAccessCountsByName = accessCountsByName.values.sumOf { it.get() },
            totalAccessCountsByData = accessCountsByData.values.sumOf { it.get() },
            definitionCacheSize = definitionCache.size
        )
    }

    // Batch operations for improved performance
    fun setBatch(values: Map<AbstractDataDefinition, Value>): Map<AbstractDataDefinition, Value?> {
        val previousValues = HashMap<AbstractDataDefinition, Value?>()

        // Pre-validate all values first to avoid partial updates
        for ((data, value) in values) {
            require(data.type.canBeAssigned(value)) {
                "Value $value cannot be assigned to data: $data"
            }
        }

        // Then perform the actual updates
        for ((data, value) in values) {
            previousValues[data] = set(data, value)
        }

        return previousValues
    }
}

/**
 * Statistics data class for hot cache performance analysis
 */
data class HotCacheStats(
    val hotCacheByNameSize: Int,
    val hotCacheByDataSize: Int,
    val totalAccessCountsByName: Int,
    val totalAccessCountsByData: Int,
    val definitionCacheSize: Int
)
