package com.smeup.rpgparser.overlay

import com.smeup.rpgparser.assertASTCanBeProduced
import com.smeup.rpgparser.assertCanBeParsedResult
import com.smeup.rpgparser.executeAnnotations
import com.smeup.rpgparser.inputStreamFor
import com.smeup.rpgparser.interpreter.InternalInterpreter
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.parsing.facade.RpgParserFacade
import com.smeup.rpgparser.parsing.facade.RpgParserResult
import com.smeup.rpgparser.parsing.parsetreetoast.ToAstConfiguration
import com.smeup.rpgparser.parsing.parsetreetoast.injectMuteAnnotation
import com.smeup.rpgparser.parsing.parsetreetoast.resolve
import com.smeup.rpgparser.parsing.parsetreetoast.toAst
import org.junit.Test
import kotlin.test.assertTrue

class RpgDeceditTest09 {

    @Test
    fun parseMUTE09_02() {
        val cu = assertASTCanBeProduced("overlay/MUTE09_02", considerPosition = true, withMuteSupport = true)
        cu.resolve()

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
        cu.resolve()

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
        cu.resolve()

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
