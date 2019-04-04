package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InterpreterTest {

    @Test
    fun executeJD_001_plist() {
        val cu = assertASTCanBeProduced("JD_001", true)
        cu.resolve()
        val interpreter = execute(cu, mapOf(
                "U\$FUNZ" to StringValue("Foo"),
                "U\$METO" to StringValue("Bar"),
                "U\$SVARSK" to createArrayValue(200) { blankString(1050) },
                "U\$IN35" to blankString(1)))
        assertEquals(listOf("IMP0", "FIN0"), interpreter.getExecutedSubroutineNames())
        assertEquals(StringValue("Foo"), interpreter["U\$FUNZ"])
        assertEquals(StringValue("Bar"), interpreter["U\$METO"])
        assertEquals(createArrayValue(200) { blankString(1050) }, interpreter["U\$SVARSK"])
        assertEquals(StringValue(" "), interpreter["U\$IN35"])
    }

    @Test
    fun executeJD_001_settingVars() {
        val cu = assertASTCanBeProduced("JD_001", true)
        cu.resolve()
        val interpreter = execute(cu, mapOf(
                "U\$FUNZ" to StringValue("Foo"),
                "U\$METO" to StringValue("Bar"),
                "U\$SVARSK" to createArrayValue(200) { blankString(1050) },
                "U\$IN35" to blankString(1)))
        assertEquals(listOf("IMP0", "FIN0"), interpreter.getExecutedSubroutineNames())
        // Initialized inside IMP0
        assertEquals(createArrayValue(200) { blankString(1050) }, interpreter["\$\$SVAR"])
    }

    @Test
    fun executeJD_001_inzFunz() {
        val cu = assertASTCanBeProduced("JD_001", true)
        cu.resolve()
        val interpreter = execute(cu, mapOf(
                "U\$FUNZ" to StringValue("INZ"),
                "U\$METO" to StringValue("Bar"),
                "U\$SVARSK" to createArrayValue(200) { blankString(1050) },
                "U\$IN35" to StringValue("X")))
        assertEquals(11, interpreter.getEvaluatedExpressionsConcise().size)
        assertEquals(listOf("IMP0", "FINZ", "FIN0"), interpreter.getExecutedSubroutineNames())
        // Initialized inside IMP0
        assertEquals(createArrayValue(200) { blankString(1050) }, interpreter["\$\$SVAR"])
        // Assigned inside FINZ
        assertEquals(createArrayValue(200) { blankString(1050) }, interpreter["U\$SVARSK_INI"])
        assertEquals(StringValue(" "), interpreter["U\$IN35"])
    }

    @Test
    fun executeJD_000_datainit() {
        val cu = assertASTCanBeProduced("JD_000_datainit", true)
        cu.resolve()

        assertEquals("U\$SVARSK", cu.dataDefinitonsAndFields[0].name)
        assertEquals("\$\$SVARCD", cu.dataDefinitonsAndFields[1].name)
        assertEquals("\$\$SVARVA", cu.dataDefinitonsAndFields[2].name)
        assertEquals(0, (cu.dataDefinitonsAndFields[1] as FieldDefinition).startOffset)
        assertEquals(50, (cu.dataDefinitonsAndFields[1] as FieldDefinition).endOffset)
        assertEquals(50, (cu.dataDefinitonsAndFields[2] as FieldDefinition).startOffset)
        assertEquals(1050, (cu.dataDefinitonsAndFields[2] as FieldDefinition).endOffset)

        val interpreter = execute(cu, mapOf())

        val svarsk = interpreter["U\$SVARSK"]
        assertTrue(svarsk is ArrayValue)
        assertEquals(200, (svarsk as ArrayValue).size())
        val svarskElement = (svarsk as ArrayValue).getElement(0)
        assertEquals(blankString(1050), svarskElement)

        val svarcd = interpreter["\$\$SVARCD"]
        assertTrue(svarcd is ArrayValue)
        assertEquals(200, (svarcd as ArrayValue).size())
        val svarcdElement = (svarcd as ArrayValue).getElement(0)
        assertEquals(blankString(50), svarcdElement)

        val svarva = interpreter["\$\$SVARVA"]
        assertTrue(svarva is ArrayValue)
        assertEquals(200, (svarva as ArrayValue).size())
        val svarvaElement = (svarva as ArrayValue).getElement(0)
        assertEquals(blankString(1000), svarvaElement)
    }

    @Test
    fun executeJD_000_base() {
        val cu = assertASTCanBeProduced("JD_000_base", true)
        cu.resolve()
        val interpreter = execute(cu, mapOf())
    }

//    @Test
//    fun executeJD_000() {
//        val cu = assertASTCanBeProduced("JD_000", true)
//        cu.resolve()
//        val interpreter = execute(cu, mapOf())
//    }

    @Test
    fun executeCALCFIB() {
        val cu = assertASTCanBeProduced("CALCFIB", true)
        cu.resolve()
        val interpreter = execute(cu, mapOf("ppdat" to StringValue("3")))
    }

}
