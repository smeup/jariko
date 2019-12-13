package com.smeup.rpgparser.overlay

import com.smeup.rpgparser.assertASTCanBeProduced
import com.smeup.rpgparser.assertCanBeParsed
import com.smeup.rpgparser.executeAnnotations
import com.smeup.rpgparser.interpreter.DummyDBInterface
import com.smeup.rpgparser.interpreter.InternalInterpreter
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.parsetreetoast.resolve
import com.smeup.rpgparser.rgpinterop.DirRpgProgramFinder
import com.smeup.rpgparser.rgpinterop.RpgSystem
import org.junit.Ignore
import org.junit.Test
import java.io.File


public class RpgParserOverlayTest03 {

    @Test
    fun parseMUTE03_09_syntax() {
        assertCanBeParsed("overlay/MUTE03_09", withMuteSupport = true)
    }

    @Test
    fun parseMUTE03_09_ast() {
        assertASTCanBeProduced("overlay/MUTE03_09", considerPosition = true, withMuteSupport = true)
    }

    @Test
    fun parseMUTE03_09_runtime() {
        RpgSystem.addProgramFinder(DirRpgProgramFinder(File("src/test/resources/overlay")))
        val cu = assertASTCanBeProduced("overlay/MUTE03_09", considerPosition = true, withMuteSupport = true)
        cu.resolve(DummyDBInterface)

        val interpreter = InternalInterpreter(JavaSystemInterface())
        interpreter.execute(cu, mapOf())
        val annotations = interpreter.systemInterface.getExecutedAnnotation().toSortedMap()
        var failed: Int = executeAnnotations(annotations)
        if (failed > 0) {
            throw AssertionError("$failed/${annotations.size} failed annotation(s) ")
        }
    }
}
