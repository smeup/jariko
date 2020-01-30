package com.smeup.rpgparser.overlay

import com.smeup.rpgparser.executeAnnotations
import com.smeup.rpgparser.execution.ResourceProgramFinder
import com.smeup.rpgparser.interpreter.DummyDBInterface
import com.smeup.rpgparser.interpreter.InternalInterpreter
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import com.smeup.rpgparser.rpginterop.RpgSystem
import org.junit.Test

class RpgEvalTest {

    @Test
    fun EVAL_runtime() {
        RpgSystem.addProgramFinder(ResourceProgramFinder("/"))
        val cu = com.smeup.rpgparser.assertASTCanBeProduced("overlay/EVALH", considerPosition = true, withMuteSupport = true)
        cu.resolveAndValidate(DummyDBInterface)

        val interpreter = InternalInterpreter(JavaSystemInterface())
        interpreter.execute(cu, mapOf())

        val annotations = interpreter.systemInterface.getExecutedAnnotation().toSortedMap()
        var failed: Int = executeAnnotations(annotations)
        if (failed > 0) {
            throw AssertionError("$failed/${annotations.size} failed annotation(s) ")
        }
    }
}
