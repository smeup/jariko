package com.smeup.rpgparser.overlay

import com.smeup.rpgparser.AbstractTestCase
import com.smeup.rpgparser.executeAnnotations
import com.smeup.rpgparser.interpreter.DecEdit
import com.smeup.rpgparser.interpreter.DummyDBInterface
import com.smeup.rpgparser.interpreter.InternalInterpreter
import com.smeup.rpgparser.interpreter.LocalizationContext
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import org.junit.Test

open class RpgDeceditTest09 : AbstractTestCase() {

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

        val localizationContext = LocalizationContext(decedit = DecEdit.COMMA)
        val interpreter = InternalInterpreter(JavaSystemInterface(), localizationContext)
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
        val localizationContext = LocalizationContext(decedit = DecEdit.ZERO_COMMA)
        val interpreter = InternalInterpreter(JavaSystemInterface(), localizationContext)
        // Changes the default decedit
        interpreter.execute(cu, mapOf())
        val annotations = interpreter.systemInterface.getExecutedAnnotation().toSortedMap()
        var failed: Int = executeAnnotations(annotations)
        if (failed > 0) {
            throw AssertionError("$failed/${annotations.size} failed annotation(s) ")
        }
    }
}
