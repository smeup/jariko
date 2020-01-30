package com.smeup.rpgparser.overlay

import com.smeup.rpgparser.assertASTCanBeProduced
import com.smeup.rpgparser.executeAnnotations
import com.smeup.rpgparser.interpreter.DummyDBInterface
import com.smeup.rpgparser.interpreter.InternalInterpreter
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import org.junit.Test

class RpgDeceditTest09 {

    @Test
    fun parseMUTE09_02() {
        val cu = assertASTCanBeProduced("overlay/MUTE09_02", considerPosition = true, withMuteSupport = true)
        cu.resolveAndValidate(DummyDBInterface)

        val interpreter = InternalInterpreter(JavaSystemInterface())

        interpreter.execute(cu, mapOf())
        val annotations = interpreter.systemInterface.getExecutedAnnotation().toSortedMap()
        var failed: Int = executeAnnotations(annotations)
        if (failed > 0) {
            throw AssertionError("$failed/${annotations.size} failed annotation(s) ")
        }
    }

    @Test
    fun parseMUTE09_02_comma() {
        val cu = assertASTCanBeProduced("overlay/MUTE09_02_COMMA", considerPosition = true, withMuteSupport = true)
        cu.resolveAndValidate(DummyDBInterface)

        val interpreter = InternalInterpreter(JavaSystemInterface())
        // Changes the default decedit
        interpreter.decedit = ","
        interpreter.execute(cu, mapOf())
        val annotations = interpreter.systemInterface.getExecutedAnnotation().toSortedMap()
        var failed: Int = executeAnnotations(annotations)
        if (failed > 0) {
            throw AssertionError("$failed/${annotations.size} failed annotation(s) ")
        }
    }

    @Test
    fun parseMUTE09_02A() {
        val cu = assertASTCanBeProduced("overlay/MUTE09_02A", considerPosition = true, withMuteSupport = true)
        cu.resolveAndValidate(DummyDBInterface)

        val interpreter = InternalInterpreter(JavaSystemInterface())
        interpreter.decedit = "0,"
        // Changes the default decedit
        interpreter.execute(cu, mapOf())
        val annotations = interpreter.systemInterface.getExecutedAnnotation().toSortedMap()
        var failed: Int = executeAnnotations(annotations)
        if (failed > 0) {
            throw AssertionError("$failed/${annotations.size} failed annotation(s) ")
        }
    }
}
