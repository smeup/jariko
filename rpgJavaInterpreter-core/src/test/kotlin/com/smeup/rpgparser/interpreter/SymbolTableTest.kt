package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.PerformanceTest
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import org.junit.experimental.categories.Category
import kotlin.test.Test
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
     * 4. Perform 1,000,000 lookups for the last field of the data structure `DS1_FLD25`.
     * 5. Measure and log the total execution time for these operations.
     *
     * The goal is to evaluate the efficiency of symbol table lookups and ensure performance is within an acceptable range.
     *
     * @throws TimeoutException if the test does not complete within 6 seconds
     * @see ISymbolTable
     */
    @Test(timeout = 3_000)
    @Category(PerformanceTest::class)
    fun executeST_F_WITH_DS_UNQUALIFIED1_PERFORMANCE() {
        measureTime {
            assertASTCanBeProduced(
                exampleName = "symboltable/ST_PERFORMANCE_ACCESS01",
                afterAstCreation = { ast ->
                    val symbolTable: ISymbolTable = ast.createSymbolTable()

                    for (i in 1..1_000_000) {
                        symbolTable.dataDefinitionByName("VAR1")
                    }

                    for (i in 1..1_000_000) {
                        symbolTable.dataDefinitionByName("DS1_FLD25")
                    }
                }
            )
        }.also { time ->
            println("Time for accessing to Standalone and Data Struct last field: $time")
        }
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