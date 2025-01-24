package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.AbstractTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

/**
 * The purpose of this test suite is to validate the behaviour around Symbol Table, directly or not.
 * The resources about this suite starts with `ST` (Symbol Table), followed by any string which describes the purpose.
 * Could be a Data Struct (DS), Standalone (S), File (F) or Inline (I) with an operation on this.
 */
class SymbolTableTest : AbstractTest() {
//    /**
//     * In this test we have a Data Structure declared as not `QUALIFIED` by using `EXTNAME` keyword.
//     * When I use a set (by `EVAL`) for a field without dot notation, the parent DS must be find in parent
//     *  of Symbol Table.
//     */
//    @Test
//    fun executeSTDSUNQUALIFIED1() {
//        val expected = listOf("FOO")
//        assertEquals(expected, "symboltable/STDSUNQUALIFIED1".outputOf(configuration = smeupConfig))
//    }

    /**
     * In this test we have a Data Structure declared as not `QUALIFIED` by using `EXTNAME` keyword and
     *  a File to the same resource declared for DS `EXTNAME`. In this case the File fields are removed from parent.
     * The purpose of test is to check if DS field is resolved without dot notation and refers to DS, and not to File.
     */
    @Test
    fun executeST_F_WITH_DS_UNQUALIFIED1() {
        assertASTCanBeProduced(
            exampleName = "symboltable/ST_F_WITH_DS_UNQUALIFIED1",
            afterAstCreation = { ast ->
                val symbolTable: ISymbolTable = SymbolTable()
                for (pair in ast.dataDefinitions.map { dataDefinition -> makePairDataDefinitionValue(dataDefinition) }) {
                    symbolTable[pair.first] = pair.second
                }

                val field = symbolTable.dataDefinitionByName("ST01_KEY")

                assertIs<FieldDefinition>(field, "ST01_KEY is a FieldDefinition.")
                assertIs<DataDefinition>(field.parent, "ST01_KEY parent is a DataDefinition.")
                assertEquals((field.parent as DataDefinition).name, "DS1", "The ST01_KEY parent is called DS1.")
                assertNull((field.parent as DataDefinition).parent, "DS1 hasn't parent.")
            }
        )
    }

//    /**
//     * In this test we have a Fil and all fields are placed on parent.
//     * So, the resolution must be in this place.
//     */
//    @Test
//    fun executeSTFCHAIN1() {
//        ST01DbMock().usePopulated({
//            val expected = listOf("1", "FOO", "BAR")
//            assertEquals(expected, "symboltable/STFCHAIN1".outputOf(configuration = smeupConfig))
//        },
//            listOf(mapOf("ST01_KEY" to "1", "ST01_COL1" to "FOO", "ST01_COL2" to "BAR"))
//        )
//    }

    private fun makePairDataDefinitionValue(dataDefinition: DataDefinition): Pair<DataDefinition, Value> {
        return Pair(
            dataDefinition,
            dataDefinition.defaultValue ?: dataDefinition.type.blank()
        )
    }
}