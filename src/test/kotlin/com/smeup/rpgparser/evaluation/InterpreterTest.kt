package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.*
import org.junit.Test
import kotlin.test.assertEquals

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
        interpreter.getEvaluatedExpressionsConcise().forEach {
            println("${it.expression.render()} -> ${it.value}")
        }
        assertEquals(8, interpreter.getEvaluatedExpressionsConcise().size)
        assertEquals(listOf("IMP0", "FINZ", "FIN0"), interpreter.getExecutedSubroutineNames())
        // Initialized inside IMP0
        assertEquals(createArrayValue(200) { blankString(1050) }, interpreter["\$\$SVAR"])
        // Assigned inside FINZ
        assertEquals(createArrayValue(200) { blankString(1050) }, interpreter["U\$SVARSK_INI"])
        assertEquals(StringValue(" "), interpreter["U\$IN35"])
    }
}
