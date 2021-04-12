package com.smeup.rpgparser.overlay

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.executeAnnotations
import com.smeup.rpgparser.interpreter.InternalInterpreter
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import org.junit.Test

open class RpgEvalTest : AbstractTest() {

    @Test
    fun EVAL_runtime() {
        val cu = assertASTCanBeProduced("overlay/EVALH", considerPosition = true, withMuteSupport = true)
        cu.resolveAndValidate()

        val interpreter = InternalInterpreter(JavaSystemInterface())
        interpreter.execute(cu, mapOf())

        val annotations = interpreter.systemInterface.getExecutedAnnotation().toSortedMap()
        var failed: Int = executeAnnotations(annotations)
        if (failed > 0) {
            throw AssertionError("$failed/${annotations.size} failed annotation(s) ")
        }
    }
}
