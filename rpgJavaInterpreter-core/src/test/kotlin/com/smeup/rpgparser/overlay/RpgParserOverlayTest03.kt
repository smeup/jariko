package com.smeup.rpgparser.overlay

import com.smeup.rpgparser.assertASTCanBeProduced
import com.smeup.rpgparser.assertCanBeParsed
import com.smeup.rpgparser.execute
import com.smeup.rpgparser.interpreter.InternalInterpreter
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.parsetreetoast.resolve
import com.smeup.rpgparser.rgpinterop.DirRpgProgramFinder
import com.smeup.rpgparser.rgpinterop.RpgSystem
import org.junit.Test
import java.io.File
import kotlin.test.assertFalse
import kotlin.test.assertTrue


public class RpgParserOverlayTest03 {

    @Test
    fun parseMUTE03_09_syntax() {
        val result = assertCanBeParsed("overlay/MUTE03_09", withMuteSupport = true)
    }

    @Test
    fun parseMUTE03_09_ast() {
        val cu = assertASTCanBeProduced("overlay/MUTE03_09", considerPosition = true, withMuteSupport = true)
    }

    @Test
    fun parseMUTE03_09_runtime() {
        RpgSystem.addProgramFinder(DirRpgProgramFinder(File("src/test/resources/overlay")))
        val cu = assertASTCanBeProduced("overlay/MUTE03_09", considerPosition = true, withMuteSupport = true)
        cu.resolve()
        var failed : Int = 0

        val interpreter = InternalInterpreter(JavaSystemInterface())
        interpreter.execute(cu, mapOf())
        val annotations = interpreter.systemInterface.getExceutedAnnotation()
        annotations.forEach { (line, annotation) ->
            try {
                    assertTrue(annotation.result.asBoolean().value)

            } catch (e:AssertionError) {
                println("$line ${annotation.expression.render()} ${annotation.result.asBoolean().value}")
                failed++
            }
        }
        if(failed > 0) {
            throw AssertionError("$failed failed annotation(s)")
        }

    }








}
