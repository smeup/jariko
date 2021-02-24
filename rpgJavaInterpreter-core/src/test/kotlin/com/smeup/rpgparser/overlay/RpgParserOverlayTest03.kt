package com.smeup.rpgparser.overlay

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.assertCanBeParsed
import com.smeup.rpgparser.executeAnnotations
import com.smeup.rpgparser.interpreter.InternalInterpreter
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import com.smeup.rpgparser.rpginterop.RpgSystem
import org.junit.Test
import java.io.File

open class RpgParserOverlayTest03 : AbstractTest() {

    @Test
    fun parseMUTE03_09_syntax() {
        assertCanBeParsed("overlay/MUTE03_09", withMuteSupport = true)
    }

    @Test
    fun parseMUTE03_09_ast() {
        assertASTCanBeProduced("overlay/MUTE03_09", considerPosition = true, withMuteSupport = true)
    }

    @Test
    fun parseMUTE03_09B_ast() {
        assertASTCanBeProduced("overlay/MUTE03_09B", considerPosition = true, withMuteSupport = true)
    }

    @Test
    fun parseMUTE03_09_runtime() {
        val cu = assertASTCanBeProduced("overlay/MUTE03_09", considerPosition = true, withMuteSupport = true)
        cu.resolveAndValidate()

        val interpreter = InternalInterpreter(JavaSystemInterface().apply {
            rpgSystem = RpgSystem().apply {
                addProgramFinder(DirRpgProgramFinder(File("src/test/resources/overlay")))
            }
        })
        interpreter.execute(cu, mapOf())
        val annotations = interpreter.systemInterface.getExecutedAnnotation().toSortedMap()
        var failed: Int = executeAnnotations(annotations)
        if (failed > 0) {
            throw AssertionError("$failed/${annotations.size} failed annotation(s) ")
        }
    }
}
