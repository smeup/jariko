package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.*
import org.junit.Test
import kotlin.test.assertEquals

class InterpreterSmokeTest {

    @Test
    fun executeJD_001() {
        val cu = assertASTCanBeProduced("JD_001")
        execute(cu)
        // We need JD_URL in Kotlin. We want it to print its third parameter
    }

    @Test
    fun executeJD_002() {
        val cu = assertASTCanBeProduced("JD_002")
        execute(cu)
    }

    @Test
    fun executeJD_003() {
        val cu = assertASTCanBeProduced("JD_003")
        execute(cu)
    }

    private fun execute(cu: CompilationUnit) {
        val systemInterface = object : SystemInterface {

        }
        return Interpreter(systemInterface).execute(cu)
    }

}
