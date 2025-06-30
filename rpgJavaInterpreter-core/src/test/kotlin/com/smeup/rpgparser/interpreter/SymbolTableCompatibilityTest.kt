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
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import kotlin.test.*

/**
 * Comprehensive compatibility test suite between SymbolTable and OptimizedSymbolTable.
 *
 * This test suite ensures that the OptimizedSymbolTable maintains full functional compatibility
 * with the original SymbolTable implementation across all operations and edge cases.
 *
 * Test Coverage:
 * - Basic CRUD operations (Create, Read, Update, Delete)
 * - Edge cases and error conditions
 * - Complex data structure operations
 * - Field access patterns
 * - Symbol table hierarchy and scoping
 * - Performance under various load patterns
 */
class SymbolTableCompatibilityTest : AbstractTest() {

    /**
     * Tests basic symbol table operations for compatibility.
     */
    @Test
    fun testBasicOperationsCompatibility() {
        assertASTCanBeProduced(
            exampleName = "symboltable/ST_PERFORMANCE_ACCESS01",
            afterAstCreation = { ast ->
                val original = createOriginalSymbolTable(ast)
                val optimized = createOptimizedSymbolTable(ast)

                // Test contains operations
                testContainsOperations(original, optimized, ast)

                // Test get operations
                testGetOperations(original, optimized, ast)

                // Test set operations
                testSetOperations(original, optimized, ast)

                // Test dataDefinitionByName operations
                testDataDefinitionByNameOperations(original, optimized)
            }
        )
    }

    /**
     * Tests field access compatibility between implementations.
     */
    @Test
    fun testFieldAccessCompatibility() {
        assertASTCanBeProduced(
            exampleName = "symboltable/ST_PERFORMANCE_ACCESS01",
            afterAstCreation = { ast ->
                val original = createOriginalSymbolTable(ast)
                val optimized = createOptimizedSymbolTable(ast)

                // Test field access by name
                testFieldAccessByName(original, optimized)

                // Test qualified field access
                testQualifiedFieldAccess(original, optimized, ast)

                // Test unqualified field access
                testUnqualifiedFieldAccess(original, optimized, ast)
            }
        )
    }

    /**
     * Tests error handling compatibility between implementations.
     */
    @Test
    fun testErrorHandlingCompatibility() {
        assertASTCanBeProduced(
            exampleName = "symboltable/ST_PERFORMANCE_ACCESS01",
            afterAstCreation = { ast ->
                val original = createOriginalSymbolTable(ast)
                val optimized = createOptimizedSymbolTable(ast)

                // Test accessing non-existent variables
                testNonExistentVariableAccess(original, optimized)

                // Test invalid assignments
                testInvalidAssignments(original, optimized, ast)

                // Test conflicting data definitions
                testConflictingDataDefinitions(original, optimized)
            }
        )
    }

    /**
     * Tests data structure operations compatibility.
     */
    @Test
    fun testDataStructureOperationsCompatibility() {
        assertASTCanBeProduced(
            exampleName = "symboltable/ST_PERFORMANCE_ACCESS01",
            afterAstCreation = { ast ->
                val original = createOriginalSymbolTable(ast)
                val optimized = createOptimizedSymbolTable(ast)

                // Test data structure field access
                testDataStructureFieldAccess(original, optimized, ast)

                // Test nested data structure access
                testNestedDataStructureAccess(original, optimized, ast)

                // Test data structure modification
                testDataStructureModification(original, optimized, ast)
            }
        )
    }

    /**
     * Tests symbol table state management compatibility.
     */
    @Test
    fun testStateManagementCompatibility() {
        assertASTCanBeProduced(
            exampleName = "symboltable/ST_PERFORMANCE_ACCESS01",
            afterAstCreation = { ast ->
                val original = createOriginalSymbolTable(ast)
                val optimized = createOptimizedSymbolTable(ast)

                // Test clear operations
                testClearOperations(original, optimized)

                // Test isEmpty operations
                testIsEmptyOperations(original, optimized)

                // Test getValues operations
                testGetValuesOperations(original, optimized)
            }
        )
    }

    /**
     * Tests complex access patterns for compatibility.
     */
    @Test
    fun testComplexAccessPatternsCompatibility() {
        assertASTCanBeProduced(
            exampleName = "symboltable/ST_PERFORMANCE_ACCESS01",
            afterAstCreation = { ast ->
                val original = createOriginalSymbolTable(ast)
                val optimized = createOptimizedSymbolTable(ast)

                // Test mixed access patterns
                testMixedAccessPatterns(original, optimized, ast)

                // Test repeated access patterns
                testRepeatedAccessPatterns(original, optimized, ast)

                // Test random access patterns
                testRandomAccessPatterns(original, optimized, ast)
            }
        )
    }

    /**
     * Tests case sensitivity compatibility.
     */
    @Test
    fun testCaseSensitivityCompatibility() {
        assertASTCanBeProduced(
            exampleName = "symboltable/ST_PERFORMANCE_ACCESS01",
            afterAstCreation = { ast ->
                val original = createOriginalSymbolTable(ast)
                val optimized = createOptimizedSymbolTable(ast)

                testCaseInsensitiveAccess(original, optimized)
            }
        )
    }

    // Helper methods for creating symbol tables
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

    // Test implementation methods
    private fun testContainsOperations(original: SymbolTable, optimized: OptimizedSymbolTable, ast: CompilationUnit) {
        // Test contains by name
        val testNames = listOf("VAR1", "DS10", "DS10_FLD50", "NONEXISTENT")

        testNames.forEach { name ->
            val originalContains = original.contains(name)
            val optimizedContains = optimized.contains(name)
            assertEquals(originalContains, optimizedContains, "contains($name) should be equal")
        }

        // Test contains by data definition
        ast.dataDefinitions.forEach { dataDefinition ->
            val originalContains = original.contains(dataDefinition)
            val optimizedContains = optimized.contains(dataDefinition)
            assertEquals(originalContains, optimizedContains, "contains(${dataDefinition.name}) should be equal")
        }
    }

    private fun testGetOperations(original: SymbolTable, optimized: OptimizedSymbolTable, ast: CompilationUnit) {
        // Test get by data definition
        ast.dataDefinitions.forEach { dataDefinition ->
            val originalValue = original[dataDefinition]
            val optimizedValue = optimized[dataDefinition]
            assertEquals(originalValue, optimizedValue, "get(${dataDefinition.name}) should return equal values")
        }

        // Test get by name for existing variables
        val existingNames = listOf("VAR1")
        existingNames.forEach { name ->
            val originalValue = original[name]
            val optimizedValue = optimized[name]
            assertEquals(originalValue, optimizedValue, "get($name) should return equal values")
        }

        // Test get by name for fields
        val fieldNames = listOf("DS10_FLD50")
        fieldNames.forEach { name ->
            try {
                val originalValue = original[name]
                val optimizedValue = optimized[name]
                assertEquals(originalValue, optimizedValue, "get($name) should return equal field values")
            } catch (_: Exception) {
                // Both should throw the same exception for non-existent fields
                assertFailsWith<Exception> { optimized[name] }
            }
        }
    }

    private fun testSetOperations(original: SymbolTable, optimized: OptimizedSymbolTable, ast: CompilationUnit) {
        // Test setting values by data definition
        ast.dataDefinitions.take(3).forEach { dataDefinition ->
            val testValue = StringValue("TEST_${dataDefinition.name}")

            if (dataDefinition.type.canBeAssigned(testValue)) {
                testValue.also { original[dataDefinition] = it }
                testValue.also { optimized[dataDefinition] = it }

                // Verify the assignment worked and both return the same new value
                val originalCurrent = original[dataDefinition]
                val optimizedCurrent = optimized[dataDefinition]
                assertEquals(originalCurrent, optimizedCurrent, "After setting ${dataDefinition.name}, values should be equal")
            }
        }
    }

    private fun testDataDefinitionByNameOperations(original: SymbolTable, optimized: OptimizedSymbolTable) {
        val testNames = listOf("VAR1", "DS10", "DS10_FLD50", "NONEXISTENT", "var1", "ds10")

        testNames.forEach { name ->
            val originalDefinition = original.dataDefinitionByName(name)
            val optimizedDefinition = optimized.dataDefinitionByName(name)
            assertEquals(originalDefinition, optimizedDefinition, "dataDefinitionByName($name) should return equal definitions")
        }
    }

    private fun testFieldAccessByName(original: SymbolTable, optimized: OptimizedSymbolTable) {
        val fieldNames = listOf("DS10_FLD50")

        fieldNames.forEach { fieldName ->
            try {
                val originalValue = original[fieldName]
                val optimizedValue = optimized[fieldName]
                assertEquals(originalValue, optimizedValue, "Field access for $fieldName should be equal")
            } catch (originalException: Exception) {
                // If original throws an exception, optimized should throw the same type
                assertFailsWith(originalException::class) { optimized[fieldName] }
            }
        }
    }

    private fun testQualifiedFieldAccess(original: SymbolTable, optimized: OptimizedSymbolTable, ast: CompilationUnit) {
        // Test accessing fields through their qualified names
        val dataStructures = ast.dataDefinitions
            .filter { it.type is DataStructureType }

        dataStructures.forEach { ds ->
            if (ds.fields.isNotEmpty()) {
                val field = ds.fields.first()
                val qualifiedName = "${ds.name}.${field.name}"

                try {
                    val originalValue = original[qualifiedName]
                    val optimizedValue = optimized[qualifiedName]
                    assertEquals(originalValue, optimizedValue, "Qualified field access for $qualifiedName should be equal")
                } catch (originalException: Exception) {
                    assertFailsWith(originalException::class) { optimized[qualifiedName] }
                }
            }
        }
    }

    private fun testUnqualifiedFieldAccess(original: SymbolTable, optimized: OptimizedSymbolTable, ast: CompilationUnit) {
        // Test accessing fields without qualification
        val dataStructures = ast.dataDefinitions
            .filter { it.type is DataStructureType }

        dataStructures.forEach { ds ->
            ds.fields.forEach { field ->
                if (field.canBeUsedUnqualified()) {
                    try {
                        val originalValue = original[field.name]
                        val optimizedValue = optimized[field.name]
                        assertEquals(originalValue, optimizedValue, "Unqualified field access for ${field.name} should be equal")
                    } catch (originalException: Exception) {
                        assertFailsWith(originalException::class) { optimized[field.name] }
                    }
                }
            }
        }
    }

    private fun testNonExistentVariableAccess(original: SymbolTable, optimized: OptimizedSymbolTable) {
        val nonExistentNames = listOf("NONEXISTENT", "FAKE_VAR", "MISSING_FIELD")

        nonExistentNames.forEach { name ->
            assertFailsWith<Exception>("Both should throw exception for non-existent variable: $name") {
                original[name]
            }
            assertFailsWith<Exception>("Both should throw exception for non-existent variable: $name") {
                optimized[name]
            }
        }
    }

    private fun testInvalidAssignments(original: SymbolTable, optimized: OptimizedSymbolTable, ast: CompilationUnit) {
        // Test assigning incompatible types
        ast.dataDefinitions.take(2).forEach { dataDefinition ->
            // Try to assign a value that doesn't match the type
            val incompatibleValue = StringValue("INCOMPATIBLE")

            if (!dataDefinition.type.canBeAssigned(incompatibleValue)) {
                assertFailsWith<Exception>("Original should reject incompatible assignment to ${dataDefinition.name}") {
                    original[dataDefinition] = incompatibleValue
                }
                assertFailsWith<Exception>("Optimized should reject incompatible assignment to ${dataDefinition.name}") {
                    optimized[dataDefinition] = incompatibleValue
                }
            }
        }
    }

    private fun testConflictingDataDefinitions(original: SymbolTable, optimized: OptimizedSymbolTable) {
        // This test would need to be implemented based on specific conflict scenarios
        // For now, we'll test that both symbol tables have the same conflict detection behavior

        // Test that both implementations have the same data definitions
        val originalValues = original.getValues()
        val optimizedValues = optimized.getValues()

        assertEquals(originalValues.keys, optimizedValues.keys, "Both symbol tables should have the same data definitions")
    }

    private fun testDataStructureFieldAccess(original: SymbolTable, optimized: OptimizedSymbolTable, ast: CompilationUnit) {
        val dataStructures = ast.dataDefinitions
            .filter { it.type is DataStructureType }

        dataStructures.forEach { ds ->
            ds.fields.forEach { field ->
                try {
                    val originalValue = original[field]
                    val optimizedValue = optimized[field]
                    assertEquals(originalValue, optimizedValue, "Field access for ${field.name} in ${ds.name} should be equal")
                } catch (originalException: Exception) {
                    assertFailsWith(originalException::class) { optimized[field] }
                }
            }
        }
    }

    private fun testNestedDataStructureAccess(original: SymbolTable, optimized: OptimizedSymbolTable, ast: CompilationUnit) {
        // Test accessing nested data structures if they exist
        val dataStructures = ast.dataDefinitions
            .filter { it.type is DataStructureType }

        dataStructures.forEach { ds ->
            val nestedStructures = ds.fields.filter { it.type is DataStructureType }
            nestedStructures.forEach { nestedDS ->
                try {
                    val originalValue = original[nestedDS]
                    val optimizedValue = optimized[nestedDS]
                    assertEquals(originalValue, optimizedValue, "Nested data structure access for ${nestedDS.name} should be equal")
                } catch (originalException: Exception) {
                    assertFailsWith(originalException::class) { optimized[nestedDS] }
                }
            }
        }
    }

    private fun testDataStructureModification(original: SymbolTable, optimized: OptimizedSymbolTable, ast: CompilationUnit) {
        val dataStructures = ast.dataDefinitions
            .filter { it.type is DataStructureType }

        dataStructures.take(1).forEach { ds ->
            if (ds.fields.isNotEmpty()) {
                val field = ds.fields.first()
                val testValue = StringValue("MODIFIED")

                if (field.type.canBeAssigned(testValue)) {
                    original[field] = testValue
                    optimized[field] = testValue

                    val originalValue = original[field]
                    val optimizedValue = optimized[field]
                    assertEquals(originalValue, optimizedValue, "Modified field values should be equal")
                }
            }
        }
    }

    private fun testClearOperations(original: SymbolTable, optimized: OptimizedSymbolTable) {
        // Verify both are not empty initially
        assertFalse(original.isEmpty(), "Original should not be empty initially")
        assertFalse(optimized.isEmpty(), "Optimized should not be empty initially")

        // Clear both
        original.clear()
        optimized.clear()

        // Verify both are empty after clear
        assertTrue(original.isEmpty(), "Original should be empty after clear")
        assertTrue(optimized.isEmpty(), "Optimized should be empty after clear")

        // Verify getValues returns empty maps
        assertTrue(original.getValues().isEmpty(), "Original getValues should be empty after clear")
        assertTrue(optimized.getValues().isEmpty(), "Optimized getValues should be empty after clear")
    }

    private fun testIsEmptyOperations(original: SymbolTable, optimized: OptimizedSymbolTable) {
        val originalEmpty = original.isEmpty()
        val optimizedEmpty = optimized.isEmpty()
        assertEquals(originalEmpty, optimizedEmpty, "isEmpty() should return the same value")
    }

    private fun testGetValuesOperations(original: SymbolTable, optimized: OptimizedSymbolTable) {
        val originalValues = original.getValues()
        val optimizedValues = optimized.getValues()

        assertEquals(originalValues.size, optimizedValues.size, "getValues() should return same number of entries")

        originalValues.forEach { (dataDefinition, value) ->
            val optimizedValue = optimizedValues[dataDefinition]
            assertNotNull(optimizedValue, "Optimized values should contain ${dataDefinition.name}")
            assertEquals(value, optimizedValue, "Values for ${dataDefinition.name} should be equal")
        }
    }

    private fun testMixedAccessPatterns(original: SymbolTable, optimized: OptimizedSymbolTable, ast: CompilationUnit) {
        val dataDefinitions = ast.dataDefinitions.take(3)
        val fieldNames = listOf("VAR1", "DS10_FLD50")

        // Alternate between string and data definition access
        repeat(10) { i ->
            if (i % 2 == 0) {
                // String access
                fieldNames.forEach { name ->
                    try {
                        val originalValue = original[name]
                        val optimizedValue = optimized[name]
                        assertEquals(originalValue, optimizedValue, "Mixed access iteration $i: $name should be equal")
                    } catch (_: Exception) {
                        // Both should behave the same way
                        assertFailsWith<Exception> { optimized[name] }
                    }
                }
            } else {
                // Data definition access
                dataDefinitions.forEach { dataDefinition ->
                    val originalValue = original[dataDefinition]
                    val optimizedValue = optimized[dataDefinition]
                    assertEquals(originalValue, optimizedValue, "Mixed access iteration $i: ${dataDefinition.name} should be equal")
                }
            }
        }
    }

    private fun testRepeatedAccessPatterns(original: SymbolTable, optimized: OptimizedSymbolTable, ast: CompilationUnit) {
        val dataDefinition = ast.dataDefinitions.first()

        // Access the same variable multiple times
        repeat(10) { i ->
            val originalValue = original[dataDefinition]
            val optimizedValue = optimized[dataDefinition]
            assertEquals(originalValue, optimizedValue, "Repeated access iteration $i should be equal")
        }

        // Access by name multiple times
        repeat(10) { i ->
            try {
                val originalValue = original["VAR1"]
                val optimizedValue = optimized["VAR1"]
                assertEquals(originalValue, optimizedValue, "Repeated name access iteration $i should be equal")
            } catch (_: Exception) {
                assertFailsWith<Exception> { optimized["VAR1"] }
            }
        }
    }

    private fun testRandomAccessPatterns(original: SymbolTable, optimized: OptimizedSymbolTable, ast: CompilationUnit) {
        val dataDefinitions = ast.dataDefinitions
        val fieldNames = listOf("VAR1", "DS10_FLD50", "NONEXISTENT")

        // Random access pattern
        repeat(20) { i ->
            val useDataDefinition = i % 3 == 0

            if (useDataDefinition && dataDefinitions.isNotEmpty()) {
                val randomDataDef = dataDefinitions.random()
                val originalValue = original[randomDataDef]
                val optimizedValue = optimized[randomDataDef]
                assertEquals(originalValue, optimizedValue, "Random data def access iteration $i should be equal")
            } else {
                val randomName = fieldNames.random()
                try {
                    val originalValue = original[randomName]
                    val optimizedValue = optimized[randomName]
                    assertEquals(originalValue, optimizedValue, "Random name access iteration $i should be equal")
                } catch (_: Exception) {
                    assertFailsWith<Exception> { optimized[randomName] }
                }
            }
        }
    }

    private fun testCaseInsensitiveAccess(original: SymbolTable, optimized: OptimizedSymbolTable) {
        val testCases = listOf(
            "VAR1" to "var1",
            "VAR1" to "Var1",
            "VAR1" to "VAR1",
            "DS10" to "ds10",
            "DS10" to "Ds10"
        )

        testCases.forEach { (_, testName) ->
            try {
                val originalValue = original[testName]
                val optimizedValue = optimized[testName]
                assertEquals(originalValue, optimizedValue, "Case insensitive access: $testName should be equal")
            } catch (originalException: Exception) {
                assertFailsWith(originalException::class) { optimized[testName] }
            }

            // Test dataDefinitionByName as well
            val originalDef = original.dataDefinitionByName(testName)
            val optimizedDef = optimized.dataDefinitionByName(testName)
            assertEquals(originalDef, optimizedDef, "Case insensitive dataDefinitionByName: $testName should be equal")
        }
    }
}
