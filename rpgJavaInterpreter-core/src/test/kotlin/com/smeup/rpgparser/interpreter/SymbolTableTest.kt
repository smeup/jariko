package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.PerformanceTest
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import org.junit.experimental.categories.Category
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.time.measureTime

/**
 * The purpose of this test suite is to validate the behaviour around Symbol Table.
 * The resources about this suite starts with `ST` (Symbol Table), followed by any string which describes the purpose.
 */
class SymbolTableTest : AbstractTest() {
    /**
     * Performance test for accessing standalone fields and data structure fields in a symbol table.
     *
     * This test measures the execution time required to perform repeated lookups of standalone fields and
     * data structure fields in a symbol table created from an Abstract Syntax Tree (AST). It verifies that
     * the AST can be produced successfully and evaluates the performance of symbol table lookups for
     * specific fields.
     *
     * Steps:
     * 1. Produce an AST from the specified example program (`symboltable/ST_PERFORMANCE_ACCESS01`).
     * 2. Create a symbol table (`ISymbolTable`) from the AST.
     * 3. Perform 1,000,000 lookups for the standalone field `VAR1`.
     * 4. Perform 1,000,000 lookups for the last field of the data structure `DS10_FLD50`.
     * 5. Measure and log the total execution time for these operations.
     *
     * The goal is to evaluate the efficiency of symbol table lookups and ensure performance is within an acceptable range.
     *
     * @throws TimeoutException if the test does not complete within 6 seconds
     * @see ISymbolTable
     */
    @Test(timeout = 3_000)
//    @Category(PerformanceTest::class)
    fun executeST_F_WITH_DS_UNQUALIFIED1_PERFORMANCE() {
        assertASTCanBeProduced(
            exampleName = "symboltable/ST_PERFORMANCE_ACCESS01",
            afterAstCreation = { ast ->
                val symbolTable: ISymbolTable = ast.createSymbolTable()

                val timeVAR1 = measureTime {
                    for (i in 1..1_000_000) {
                        symbolTable.dataDefinitionByName("VAR1")
                    }
                }.also { time ->
                    println("Time execution during the resolution of VAR1: $time")
                }

                val timeDS10_FLD50 = measureTime {
                    for (i in 1..1_000_000) {
                        symbolTable.dataDefinitionByName("DS10_FLD50")
                    }
                }.also { time ->
                    println("Time execution during the resolution of DS10_FLD50: $time")
                }

                println("Ratio execution during the resolution of Standalone and DS field: ${timeVAR1 / timeDS10_FLD50}")
            }
        )
    }

    /**
     * Test for validating symbol table lookups of standalone fields and data structure fields.
     *
     * This test verifies the correctness of symbol table entries for:
     * 1. A standalone field (`VAR1`).
     * 2. Fields within a data structure (`DS10_FLD50` and `DS10_FLD51`).
     *
     * Steps:
     * 1. Produce an Abstract Syntax Tree (AST) from the example program `symboltable/ST_PERFORMANCE_ACCESS01`.
     * 2. Create a symbol table (`ISymbolTable`) from the AST.
     * 3. Perform lookups for:
     *    - `VAR1`: A standalone field expected to be found as a `DataDefinition`.
     *    - `DS10_FLD50`: A field in the data structure `DS1`, expected to be found as a `FieldDefinition`.
     *    - `DS10_FLD51`: Another field, expected not to exist in the symbol table (returns `null`).
     * 4. Assert the types and properties of the retrieved definitions:
     *    - Verify `VAR1` is a `DataDefinition`.
     *    - Verify `DS10_FLD50` is a `FieldDefinition` and belongs to the parent data structure `DS1`.
     *    - Verify `DS10_FLD51` is not found in the symbol table (`null`).
     *
     * Assertions:
     * - The type and parent relationships of the retrieved definitions are validated.
     * - The name of the parent data structure for `DS10_FLD50` is confirmed as `DS1`.
     * - It is asserted that `DS10_FLD51` does not exist in the symbol table.
     *
     * @see ISymbolTable
     * @see DataDefinition
     * @see FieldDefinition
     */
    @Test
    fun executeST_F_WITH_DS_UNQUALIFIED1() {
        assertASTCanBeProduced(
            exampleName = "symboltable/ST_PERFORMANCE_ACCESS01",
            afterAstCreation = { ast ->
                val symbolTable: ISymbolTable = ast.createSymbolTable()

                val dataDefinition = symbolTable.dataDefinitionByName("VAR1")
                val fieldDefinition1 = symbolTable.dataDefinitionByName("DS10_FLD50")
                val fieldDefinition2 = symbolTable.dataDefinitionByName("DS10_FLD51")

                assertIs<DataDefinition>(dataDefinition)
                assertIs<FieldDefinition>(fieldDefinition1)
                assertIs<DataDefinition>(fieldDefinition1.parent)
                assertEquals("DS1", (fieldDefinition1.parent as DataDefinition).name, "DS10_FLD50 is field DS1.")
                assertNull(fieldDefinition2, "DS10_FLD51 field not found.")
            }
        )
    }

    /**
     * Creates a symbol table for the current `CompilationUnit`, mapping each `DataDefinition` to its corresponding `Value`.
     *
     * The symbol table (`ISymbolTable`) acts as a container for `DataDefinition`-to-`Value` mappings.
     * For each `DataDefinition` in the `CompilationUnit`, this function generates a pair consisting of:
     * - The `DataDefinition` as the key.
     * - The associated `Value`, which is either the default value of the `DataDefinition` or a blank value based on its type.
     *
     * @return an `ISymbolTable` containing mappings of all `DataDefinition` objects in the `CompilationUnit`
     */
    private fun CompilationUnit.createSymbolTable(): ISymbolTable {
        val symbolTable: ISymbolTable = SymbolTable()
        for (pair in this.dataDefinitions.map { dataDefinition -> makePairDataDefinitionValue(dataDefinition) }) {
            symbolTable[pair.first] = pair.second
        }

        return symbolTable
    }

    /**
     * Creates a key-value pair for a `DataDefinition` and its associated `Value`.
     *
     * This function takes a `DataDefinition` and generates a pair consisting of:
     * - The `DataDefinition` itself as the key.
     * - The associated `Value`, which is determined as follows:
     *   - If the `DataDefinition` has a `defaultValue`, that is used.
     *   - Otherwise, a blank value is generated based on the `DataDefinition`'s type.
     *
     * @param dataDefinition the `DataDefinition` for which the key-value pair is created
     * @return a `Pair` where the key is the `DataDefinition` and the value is the associated `Value`
     */
    private fun makePairDataDefinitionValue(dataDefinition: DataDefinition): Pair<DataDefinition, Value> {
        return Pair(
            dataDefinition,
            dataDefinition.defaultValue ?: dataDefinition.type.blank()
        )
    }
}