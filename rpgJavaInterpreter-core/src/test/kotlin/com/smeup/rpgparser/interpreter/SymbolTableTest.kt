package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

/**
 * The purpose of this test suite is to validate the behaviour around Symbol Table.
 * The resources about this suite starts with `ST` (Symbol Table), followed by any string which describes the purpose.
 * Could be a Data Struct (DS), Standalone (S), File (F) or Inline (I) with an operation on this.
 */
class SymbolTableTest : AbstractTest() {
    /**
     * In this test we have a Data Structure declared as not `QUALIFIED` by using `EXTNAME` keyword and
     *  a File to the same resource declared for DS `EXTNAME`. In this case the File fields are removed from root.
     * The purpose of test is to check if DS field is resolved without dot notation and refers to DS, and not to File.
     */
    @Test
    fun executeST_F_WITH_DS_UNQUALIFIED1() {
        assertASTCanBeProduced(
            exampleName = "symboltable/ST_F_WITH_DS_UNQUALIFIED1",
            afterAstCreation = { ast ->
                val symbolTable: ISymbolTable = ast.createSymbolTable()

                val field = symbolTable.dataDefinitionByName("ST01_KEY")

                assertIs<FieldDefinition>(field, "ST01_KEY is a FieldDefinition.")
                assertIs<DataDefinition>(field.parent, "ST01_KEY parent is a DataDefinition.")
                assertEquals((field.parent as DataDefinition).name, "DS1", "The ST01_KEY parent is called DS1.")
                assertNull((field.parent as DataDefinition).parent, "DS1 hasn't parent.")
            }
        )
    }

    /**
     * In this test we have a Data Structure declared as not `QUALIFIED` and a File.
     * In this case the File fields are present in root.
     * The purpose of test is to check File field resolution in right place, that is in root.
     */
    @Test
    fun executeST_F_WITH_DS_UNQUALIFIED2() {
        assertASTCanBeProduced(
            exampleName = "symboltable/ST_F_WITH_DS_UNQUALIFIED2",
            afterAstCreation = { ast ->
                val symbolTable: ISymbolTable = ast.createSymbolTable()

                val field = symbolTable.dataDefinitionByName("ST01_KEY")

                assertIs<DataDefinition>(field, "ST01_KEY is a DataDefinition.")
                assertNull(field.parent, "ST01_KEY hasn't parent.")
            }
        )
    }

    /**
     * In this test we have only File declaration. The fields are placed on root.
     * The purpose of test is to check File field resolution.
     */
    @Test
    fun executeST_F_WITHOUT_DS1() {
        assertASTCanBeProduced(
            exampleName = "symboltable/ST_F_WITHOUT_DS1",
            afterAstCreation = { ast ->
                val symbolTable: ISymbolTable = ast.createSymbolTable()

                val field = symbolTable.dataDefinitionByName("ST01_KEY")

                assertIs<DataDefinition>(field, "ST01_KEY is a DataDefinition.")
                assertNull(field.parent, "ST01_KEY hasn't parent.")
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