package com.smeup.rpgparser.overlay

import com.smeup.rpgparser.executeAnnotations
import com.smeup.rpgparser.interpreter.*
import org.junit.Test
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.parsetreetoast.resolve
import com.smeup.rpgparser.rgpinterop.DirRpgProgramFinder
import com.smeup.rpgparser.rgpinterop.RpgSystem
import java.io.File

class RpgEvalTest {

    @Test
    fun EVAL_runtime() {
        RpgSystem.addProgramFinder(DirRpgProgramFinder(File("src/test/resources")))
        val cu = com.smeup.rpgparser.assertASTCanBeProduced("overlay/EVALH", considerPosition = true, withMuteSupport = true)
        cu.resolve()

        val interpreter = InternalInterpreter(JavaSystemInterface())
        interpreter.execute(cu, mapOf())

        val annotations = interpreter.systemInterface.getExecutedAnnotation().toSortedMap()
        var failed: Int = executeAnnotations(annotations)
        if (failed > 0) {
            throw AssertionError("$failed/${annotations.size} failed annotation(s) ")
        }
    }
}
