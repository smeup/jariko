package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.*
import com.smeup.rpgparser.interpreter.StringValue
import com.smeup.rpgparser.parsing.parsetreetoast.resolve
import org.junit.Ignore
import org.junit.Test

class InterpreterSmokeTest {

    @Test
    fun executeJD_001() {
        val cu = assertASTCanBeProduced("JD_001", true)
        cu.resolve()
        execute(cu, mapOf())
        // We need JD_URL in Kotlin. We want it to print its third parameter
    }

    @Test
    fun executeJD_002() {
        val cu = assertASTCanBeProduced("JD_002")
        cu.resolve()
        execute(cu, mapOf())
    }

    @Test
    fun executeJD_003() {
        val cu = assertASTCanBeProduced("JD_003")
        cu.resolve()
        execute(cu, mapOf())
    }

    @Test @Ignore
    fun executeCHAINHOSTS() {
        val cu = assertASTCanBeProduced("CHAINHOSTS")
        cu.resolve()
        execute(cu, mapOf("ipToFind" to StringValue("127.0.0.1")))
    }
}
