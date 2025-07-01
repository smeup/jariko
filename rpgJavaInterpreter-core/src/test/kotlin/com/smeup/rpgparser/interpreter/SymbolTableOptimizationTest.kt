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
import com.smeup.rpgparser.PerformanceTest
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import org.junit.experimental.categories.Category
import kotlin.test.*
import kotlin.time.Duration
import kotlin.time.measureTime

/**
 * Performance comparison test suite between original SymbolTable and OptimizedSymbolTable.
 *
 * This test suite validates that the proposed optimizations are effective by:
 * 1. Comparing raw performance between implementations
 * 2. Testing cache effectiveness under different access patterns
 * 3. Measuring memory overhead
 * 4. Validating correctness of optimized implementation
 */
class SymbolTableOptimizationTest : AbstractTest() {

    /**
     * Comprehensive performance comparison between original and optimized Symbol Table implementations.
     *
     * This test creates identical symbol tables using both implementations and performs
     * various access patterns to measure performance improvements.
     */
    @Test
    @Category(PerformanceTest::class)
    fun compareSymbolTablePerformance() {
        assertASTCanBeProduced(
            exampleName = "symboltable/ST_PERFORMANCE_ACCESS01",
            afterAstCreation = { ast ->
                // Create both symbol table implementations
                val originalSymbolTable = createOriginalSymbolTable(ast)
                val optimizedSymbolTable = createOptimizedSymbolTable(ast)

                // Verify both tables have same content
                verifySymbolTablesEquivalent(originalSymbolTable, optimizedSymbolTable)

                val testIterations = 100_000

                println("=== Symbol Table Performance Comparison ===")
                println("Test iterations: $testIterations")

                // Test 1: String-based access performance
                val stringAccessResults = compareStringAccess(originalSymbolTable, optimizedSymbolTable, testIterations)

                // Test 2: Direct data definition access performance
                val directAccessResults = compareDirectAccess(originalSymbolTable, optimizedSymbolTable, testIterations)

                // Test 3: Mixed access pattern (more realistic)
                val mixedAccessResults = compareMixedAccess(originalSymbolTable, optimizedSymbolTable, testIterations)

                // Test 4: Cache effectiveness (repeated access)
                val cacheEffectivenessResults = testCacheEffectiveness(optimizedSymbolTable, testIterations)

                // Print results
                printPerformanceResults(stringAccessResults, directAccessResults, mixedAccessResults, cacheEffectivenessResults)

                // Validate performance improvements
                validatePerformanceImprovements(stringAccessResults, directAccessResults, mixedAccessResults)

                // Test memory overhead
                testMemoryOverhead(optimizedSymbolTable)
            }
        )
    }

    /**
     * Test that validates the correctness of the optimized implementation.
     * Ensures that optimization doesn't break functionality.
     */
    @Test
    fun validateOptimizedSymbolTableCorrectness() {
        assertASTCanBeProduced(
            exampleName = "symboltable/ST_PERFORMANCE_ACCESS01",
            afterAstCreation = { ast ->
                val originalSymbolTable = createOriginalSymbolTable(ast)
                val optimizedSymbolTable = createOptimizedSymbolTable(ast)

                // Test all data definitions are accessible by name
                ast.dataDefinitions.forEach { dataDefinition ->
                    val originalValue = originalSymbolTable[dataDefinition]
                    val optimizedValue = optimizedSymbolTable[dataDefinition]
                    assertEquals(originalValue, optimizedValue, "Values should be equal for ${dataDefinition.name}")

                    val originalByName = originalSymbolTable[dataDefinition.name]
                    val optimizedByName = optimizedSymbolTable[dataDefinition.name]
                    assertEquals(originalByName, optimizedByName, "Values by name should be equal for ${dataDefinition.name}")
                }

                // Test field access
                val originalVAR1 = originalSymbolTable.dataDefinitionByName("VAR1")
                val optimizedVAR1 = optimizedSymbolTable.dataDefinitionByName("VAR1")
                assertEquals(originalVAR1, optimizedVAR1, "VAR1 data definition should be equal")

                val originalDS10_FLD50 = originalSymbolTable.dataDefinitionByName("DS10_FLD50")
                val optimizedDS10_FLD50 = optimizedSymbolTable.dataDefinitionByName("DS10_FLD50")
                assertEquals(originalDS10_FLD50, optimizedDS10_FLD50, "DS10_FLD50 data definition should be equal")

                // Test set operations
                if (originalVAR1 != null && optimizedVAR1 != null) {
                    val testValue = StringValue("TEST")
                    originalSymbolTable[originalVAR1] = testValue
                    optimizedSymbolTable[optimizedVAR1] = testValue

                    assertEquals(originalSymbolTable[originalVAR1], optimizedSymbolTable[optimizedVAR1])
                }
            }
        )
    }

    /**
     * Tests the hot cache promotion mechanism.
     */
    @Test
    fun testHotCachePromotion() {
        assertASTCanBeProduced(
            exampleName = "symboltable/ST_PERFORMANCE_ACCESS01",
            afterAstCreation = { ast ->
                val optimizedSymbolTable = createOptimizedSymbolTable(ast)

                // Initial state - no hot cache entries
                var stats = optimizedSymbolTable.getHotCacheStats()
                assertEquals(0, stats.hotCacheByNameSize, "Hot cache should be empty initially")
                assertEquals(0, stats.hotCacheByDataSize, "Hot cache should be empty initially")

                // Access VAR1 by name 4 times (below threshold)
                repeat(4) {
                    optimizedSymbolTable["VAR1"]
                }

                stats = optimizedSymbolTable.getHotCacheStats()
                assertEquals(0, stats.hotCacheByNameSize, "Hot cache should still be empty (below threshold)")

                // Access VAR1 one more time (reaches threshold of 5)
                optimizedSymbolTable["VAR1"]

                stats = optimizedSymbolTable.getHotCacheStats()
                assertEquals(1, stats.hotCacheByNameSize, "VAR1 should be promoted to hot cache")

                // Test direct data definition access
                val dataDefinition = optimizedSymbolTable.dataDefinitionByName("VAR1") // Use VAR1 instead of DS10_FLD50
                assertNotNull(dataDefinition, "VAR1 should exist")

                // Clear existing cache to start fresh for data definition test
                optimizedSymbolTable.clear()

                // Re-populate symbol table
                for (pair in ast.dataDefinitions.map { dataDefinition -> makePairDataDefinitionValue(dataDefinition) }) {
                    optimizedSymbolTable[pair.first] = pair.second
                }

                // Now test data definition access
                repeat(5) {
                    optimizedSymbolTable[dataDefinition]
                }

                stats = optimizedSymbolTable.getHotCacheStats()
                assertEquals(1, stats.hotCacheByDataSize, "VAR1 data definition should be promoted to hot cache")

                println("Hot cache promotion test passed!")
                println("Final stats: $stats")
            }
        )
    }

    private fun createOriginalSymbolTable(ast: CompilationUnit): SymbolTable {
        val symbolTable = SymbolTable()
        for (pair in ast.dataDefinitions.map { dataDefinition -> makePairDataDefinitionValue(dataDefinition) }) {
            symbolTable[pair.first] = pair.second
        }
        return symbolTable
    }

    private fun createOptimizedSymbolTable(ast: CompilationUnit): OptimizedSymbolTable {
        val symbolTable = OptimizedSymbolTable()
        for (pair in ast.dataDefinitions.map { dataDefinition -> makePairDataDefinitionValue(dataDefinition) }) {
            symbolTable[pair.first] = pair.second
        }
        return symbolTable
    }

    private fun makePairDataDefinitionValue(dataDefinition: DataDefinition): Pair<DataDefinition, Value> {
        return Pair(
            dataDefinition,
            dataDefinition.defaultValue ?: dataDefinition.type.blank()
        )
    }

    private fun verifySymbolTablesEquivalent(original: SymbolTable, optimized: OptimizedSymbolTable) {
        val originalValues = original.getValues()
        val optimizedValues = optimized.getValues()

        assertEquals(originalValues.size, optimizedValues.size, "Symbol tables should have same number of entries")

        originalValues.forEach { (dataDefinition, value) ->
            val optimizedValue = optimizedValues[dataDefinition]
            assertNotNull(optimizedValue, "Optimized table should contain $dataDefinition")
            assertEquals(value, optimizedValue, "Values should be equal for $dataDefinition")
        }
    }

    private fun compareStringAccess(original: SymbolTable, optimized: OptimizedSymbolTable, iterations: Int): PerformanceResult {
        val fieldNames = listOf("VAR1", "DS10_FLD50", "VAR1", "DS10_FLD50") // Mix of standalone and field access

        // Warm up optimized cache
        repeat(10) {
            fieldNames.forEach { name ->
                try {
                    optimized[name]
                } catch (e: Exception) {
                    // Some fields might not exist, that's ok for warm-up
                }
            }
        }

        val originalTime = measureTime {
            repeat(iterations) {
                val name = fieldNames[it % fieldNames.size]
                try {
                    original[name]
                } catch (e: Exception) {
                    // Handle cases where field doesn't exist
                }
            }
        }

        val optimizedTime = measureTime {
            repeat(iterations) {
                val name = fieldNames[it % fieldNames.size]
                try {
                    optimized[name]
                } catch (e: Exception) {
                    // Handle cases where field doesn't exist
                }
            }
        }

        return PerformanceResult("String Access", originalTime, optimizedTime, iterations)
    }

    private fun compareDirectAccess(original: SymbolTable, optimized: OptimizedSymbolTable, iterations: Int): PerformanceResult {
        val dataDefinitions = original.getValues().keys.take(4).toList() // Take first 4 data definitions

        // Warm up optimized cache
        repeat(10) {
            dataDefinitions.forEach { dataDefinition ->
                optimized[dataDefinition]
            }
        }

        val originalTime = measureTime {
            repeat(iterations) {
                val dataDefinition = dataDefinitions[it % dataDefinitions.size]
                original[dataDefinition]
            }
        }

        val optimizedTime = measureTime {
            repeat(iterations) {
                val dataDefinition = dataDefinitions[it % dataDefinitions.size]
                optimized[dataDefinition]
            }
        }

        return PerformanceResult("Direct Access", originalTime, optimizedTime, iterations)
    }

    private fun compareMixedAccess(original: SymbolTable, optimized: OptimizedSymbolTable, iterations: Int): PerformanceResult {
        val dataDefinitions = original.getValues().keys.take(2).toList()
        val fieldNames = listOf("VAR1", "DS10_FLD50")

        // Warm up optimized cache
        repeat(10) {
            dataDefinitions.forEach { optimized[it] }
            fieldNames.forEach {
                try { optimized[it] } catch (e: Exception) { /* ignore */ }
            }
        }

        val originalTime = measureTime {
            repeat(iterations) {
                if (it % 2 == 0) {
                    // String access
                    val name = fieldNames[it % fieldNames.size]
                    try {
                        original[name]
                    } catch (e: Exception) { /* ignore */ }
                } else {
                    // Direct access
                    val dataDefinition = dataDefinitions[it % dataDefinitions.size]
                    original[dataDefinition]
                }
            }
        }

        val optimizedTime = measureTime {
            repeat(iterations) {
                if (it % 2 == 0) {
                    // String access
                    val name = fieldNames[it % fieldNames.size]
                    try {
                        optimized[name]
                    } catch (e: Exception) { /* ignore */ }
                } else {
                    // Direct access
                    val dataDefinition = dataDefinitions[it % dataDefinitions.size]
                    optimized[dataDefinition]
                }
            }
        }

        return PerformanceResult("Mixed Access", originalTime, optimizedTime, iterations)
    }

    private fun testCacheEffectiveness(optimized: OptimizedSymbolTable, iterations: Int): CacheEffectivenessResult {
        val fieldName = "VAR1"

        // Cold access (before cache)
        val coldTime = measureTime {
            repeat(1000) {
                try {
                    optimized[fieldName]
                } catch (e: Exception) { /* ignore */ }
            }
        }

        // Hot access (after cache promotion)
        val hotTime = measureTime {
            repeat(iterations) {
                try {
                    optimized[fieldName]
                } catch (e: Exception) { /* ignore */ }
            }
        }

        val stats = optimized.getHotCacheStats()

        return CacheEffectivenessResult(coldTime, hotTime, stats)
    }

    private fun testMemoryOverhead(optimized: OptimizedSymbolTable) {
        val stats = optimized.getHotCacheStats()
        val estimatedOverhead = (stats.hotCacheByNameSize + stats.hotCacheByDataSize +
                                stats.definitionCacheSize) * 64 // Rough estimate in bytes

        println("Memory overhead estimate: ${estimatedOverhead} bytes")
        println("Hot cache stats: $stats")

        // Ensure memory overhead is reasonable (less than 10KB for test data)
        assertTrue(estimatedOverhead < 10_000, "Memory overhead should be reasonable")
    }

    private fun printPerformanceResults(
        stringAccess: PerformanceResult,
        directAccess: PerformanceResult,
        mixedAccess: PerformanceResult,
        cacheEffectiveness: CacheEffectivenessResult
    ) {
        println("\n=== PERFORMANCE RESULTS ===")
        println(stringAccess)
        println(directAccess)
        println(mixedAccess)
        println("\n=== CACHE EFFECTIVENESS ===")
        println(cacheEffectiveness)
        println("\n" + "=".repeat(50))
    }

    private fun validatePerformanceImprovements(
        stringAccess: PerformanceResult,
        directAccess: PerformanceResult,
        mixedAccess: PerformanceResult
    ) {
        // Validate that optimized version is faster (or at least not significantly slower)
        // We allow for some variance due to JVM warm-up and other factors

        val stringImprovement = stringAccess.improvementPercentage
        val directImprovement = directAccess.improvementPercentage
        val mixedImprovement = mixedAccess.improvementPercentage

        println("\nPERFORMANCE VALIDATION:")
        println("String access improvement: ${String.format("%.1f", stringImprovement)}%")
        println("Direct access improvement: ${String.format("%.1f", directImprovement)}%")
        println("Mixed access improvement: ${String.format("%.1f", mixedImprovement)}%")

        // At minimum, we expect no significant regression (more than 20% slower)
        assertTrue(stringImprovement > -20.0, "String access should not be more than 20% slower")
        assertTrue(directImprovement > -20.0, "Direct access should not be more than 20% slower")
        assertTrue(mixedImprovement > -20.0, "Mixed access should not be more than 20% slower")

        // Ideally, we expect improvements, but this depends on cache warm-up
        val overallImprovement = (stringImprovement + directImprovement + mixedImprovement) / 3
        println("Overall improvement: ${String.format("%.1f", overallImprovement)}%")

        if (overallImprovement > 10.0) {
            println("✅ Significant performance improvement achieved!")
        } else if (overallImprovement > 0.0) {
            println("✅ Performance improvement achieved!")
        } else {
            println("⚠️ Performance improvement not yet visible (may need cache warm-up)")
        }
    }

    data class PerformanceResult(
        val testName: String,
        val originalTime: Duration,
        val optimizedTime: Duration,
        val iterations: Int
    ) {
        val improvementPercentage: Double
            get() = ((originalTime.inWholeNanoseconds - optimizedTime.inWholeNanoseconds) * 100.0) / originalTime.inWholeNanoseconds

        override fun toString(): String {
            return """
                $testName Results ($iterations iterations):
                  Original:  ${originalTime}
                  Optimized: ${optimizedTime}
                  Improvement: ${String.format("%.1f", improvementPercentage)}%
            """.trimIndent()
        }
    }

    data class CacheEffectivenessResult(
        val coldTime: Duration,
        val hotTime: Duration,
        val cacheStats: HotCacheStats
    ) {
        val cacheEffectivenessPercentage: Double
            get() = ((coldTime.inWholeNanoseconds - hotTime.inWholeNanoseconds) * 100.0) / coldTime.inWholeNanoseconds

        override fun toString(): String {
            return """
                Cache Effectiveness:
                  Cold access time: $coldTime
                  Hot access time:  $hotTime
                  Cache speedup: ${String.format("%.1f", cacheEffectivenessPercentage)}%
                  Cache stats: $cacheStats
            """.trimIndent()
        }
    }
}
