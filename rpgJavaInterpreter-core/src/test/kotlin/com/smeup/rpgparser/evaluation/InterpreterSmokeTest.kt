package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.assertASTCanBeProduced
import com.smeup.rpgparser.execute
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import org.junit.Test

class InterpreterSmokeTest {

    @Test
    fun executeJD_001() {
        val cu = assertASTCanBeProduced("JD_001", true)
        cu.resolveAndValidate()
        execute(cu, mapOf())
        // We need JD_URL in Kotlin. We want it to print its third parameter
    }

    @Test
    fun executeJD_002() {
        val cu = assertASTCanBeProduced("JD_002")
        cu.resolveAndValidate()
        execute(cu, mapOf())
    }

    @Test
    fun executeJD_003() {
        val cu = assertASTCanBeProduced("JD_003")
        cu.resolveAndValidate()
        execute(cu, mapOf())
    }

    @Test
    fun executeMOVEA01() {
        val cu = assertASTCanBeProduced("MOVEA01")
        cu.resolveAndValidate()
        execute(cu, mapOf())
    }
}
