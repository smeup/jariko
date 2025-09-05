package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.PerformanceTest
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import org.junit.experimental.categories.Category
import kotlin.test.*
import kotlin.time.DurationUnit
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
    @Test
    @Category(PerformanceTest::class)
    fun executeST_F_WITH_DS_UNQUALIFIED1_PERFORMANCE() {
        assertASTCanBeProduced(
            exampleName = "symboltable/ST_PERFORMANCE_ACCESS01",
            afterAstCreation = { ast ->
                val symbolTable: ISymbolTable = ast.createSymbolTable()

                val timeVAR1 =
                    measureTime {
                        for (i in 1..1_000_000) {
                            symbolTable.dataDefinitionByName("VAR1")
                        }
                    }.also { time ->
                        println("Time execution during the resolution of VAR1: $time")
                    }

                val timeDS10_FLD50 =
                    measureTime {
                        for (i in 1..1_000_000) {
                            symbolTable.dataDefinitionByName("DS10_FLD50")
                        }
                    }.also { time ->
                        println("Time execution during the resolution of DS10_FLD50: $time")
                    }

                println("Ratio execution during the resolution of Standalone and DS field: ${timeVAR1 / timeDS10_FLD50}")

                assertTrue((timeVAR1 + timeDS10_FLD50).toLong(DurationUnit.MILLISECONDS) < 3000)
            },
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
                assertEquals("DS10", (fieldDefinition1.parent as DataDefinition).name, "DS10_FLD50 is field DS1.")
                assertNull(fieldDefinition2, "DS10_FLD51 field not found.")
            },
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
    private fun makePairDataDefinitionValue(dataDefinition: DataDefinition): Pair<DataDefinition, Value> =
        Pair(
            dataDefinition,
            dataDefinition.defaultValue ?: dataDefinition.type.blank(),
        )

    /**
     * Tests the interaction between a program with a data structure and another program using standalone variables,
     * validating symbol table entries and execution behavior.
     *
     * This test simulates the execution of a program (`symboltable/ST_CALL01`) that interacts with a called program
     * (`ST_CALL01C`). Both programs pass data between them, and the symbol table entries for data structures and standalone
     * variables are validated for correctness.
     *
     * Key Validations:
     * 1. For the called program (`ST_CALL01C`):
     *    - Verifies the type of the data definition `£G90WK` as `StringValue`.
     * 2. For the calling program (`ST_CALL01`):
     *    - Verifies the type of the data definition `£G90DS` as `DataStructValue`.
     *
     * Additional Assertions:
     * - Captures system outputs during the execution using a mocked system interface.
     * - Asserts expected output messages in the sequence: ["CALLED", "CALLER"].
     */
    @Test
    fun callWithDataStructureToStandalone() {
        val messages = emptyList<String>().toMutableList()
        val systemInterface =
            JavaSystemInterface().apply {
                onDisplay = { message, printStream ->
                    run {
                        messages.add(message)
                    }
                }
            }
        executePgm(
            systemInterface = systemInterface,
            programName = "symboltable/ST_CALL01",
            configuration =
                Configuration().apply {
                    jarikoCallback.onExitPgm = { programName: String, symbolTable: ISymbolTable, _: Throwable? ->
                        if (programName.equals("ST_CALL01C", ignoreCase = true)) {
                            val dataDefinition = symbolTable.get("£G90WK")
                            assertIs<StringValue>(dataDefinition)
                        }

                        if (programName.equals("ST_CALL01", ignoreCase = true)) {
                            val dataDefinition = symbolTable.get("£G90DS")
                            assertIs<DataStructValue>(dataDefinition)
                        }
                    }
                },
        )

        assertEquals(listOf("CALLED", "CALLER"), messages)
    }

    /**
     * Tests the interaction between standalone variables of different sizes across programs using a symbol table.
     *
     * The test simulates the execution of a program that calls another program, validating the behavior and properties
     * of standalone variables (`VARSTD`) in both the calling and called programs. A mocked system interface captures
     * output messages for validation.
     *
     * Key Validations:
     * 1. For the standalone variable (`VARSTD`) in the called program:
     *    - Confirms the type is `StringValue`.
     *    - Verifies the length of the variable is 5.
     * 2. For the standalone variable (`VARSTD`) in the calling program:
     *    - Confirms the type is `StringValue`.
     *    - Verifies the length of the variable is 6.
     *
     * Additional Assertions:
     * - Verifies the system output messages during the execution sequence:
     *   - Ensures expected outputs are captured in the order: ["CALLER", "FOOBAR", "CALLED", "FOOBA"].
     */
    @Test
    fun callWithStandaloneToStandaloneWithDifferentSizes() {
        val messages = emptyList<String>().toMutableList()
        val systemInterface =
            JavaSystemInterface().apply {
                onDisplay = { message, printStream ->
                    run {
                        messages.add(message)
                    }
                }
            }
        executePgm(
            systemInterface = systemInterface,
            programName = "symboltable/ST_CALL02",
            configuration =
                Configuration().apply {
                    jarikoCallback.onExitPgm = { programName: String, symbolTable: ISymbolTable, _: Throwable? ->
                        if (programName.equals("ST_CALL02C", ignoreCase = true)) {
                            val dataDefinition = symbolTable.get("VARSTD")
                            assertIs<StringValue>(dataDefinition)
                            assertEquals(5, dataDefinition.length())
                        }

                        if (programName.equals("ST_CALL02", ignoreCase = true)) {
                            val dataDefinition = symbolTable.get("VARSTD")
                            assertIs<StringValue>(dataDefinition)
                            assertEquals(6, dataDefinition.length())
                        }
                    }
                },
        )

        assertEquals(listOf("CALLER", "FOOBAR", "CALLED", "FOOBA"), messages)
    }

    /**
     * Tests the behavior of calling a program with an array of data structures, verifying its interaction
     * with a standalone field using a symbol table.
     *
     * The test simulates program execution using a mocked system interface and verifies the following:
     * 1. An array of data structures (`DS1_ARR`) is passed, checked for its properties such as array length
     *    and element type (`StringType`).
     * 2. A standalone variable (`VARSTD`) is validated in a called program for its type (`StringValue`)
     *    and length.
     *
     * Assertions:
     * - Verifies the system output messages during execution.
     * - Confirms the symbol table contains the expected values and types for both standalone and array data.
     */
    @Test
    fun callWithArrayOfDataStructureToStandalone() {
        val messages = emptyList<String>().toMutableList()
        val systemInterface =
            JavaSystemInterface().apply {
                onDisplay = { message, printStream ->
                    run {
                        messages.add(message)
                    }
                }
            }
        executePgm(
            systemInterface = systemInterface,
            programName = "symboltable/ST_CALL03",
            configuration =
                Configuration().apply {
                    jarikoCallback.onExitPgm = { programName: String, symbolTable: ISymbolTable, _: Throwable? ->
                        if (programName.equals("ST_CALL03C", ignoreCase = true)) {
                            val dataDefinition = symbolTable.get("VARSTD")
                            assertIs<StringValue>(dataDefinition)
                            assertEquals(2, dataDefinition.length())
                        }

                        if (programName.equals("ST_CALL03", ignoreCase = true)) {
                            val dataDefinition = symbolTable.get("DS1_ARR")
                            assertIs<ArrayValue>(dataDefinition)
                            assertIs<StringType>(dataDefinition.elementType)
                            assertEquals(5, dataDefinition.arrayLength())
                        }
                    }
                },
        )

        assertEquals(listOf("CALLER", "5", "5", "5", "5", "5", "CALLED", "55"), messages)
    }
}
