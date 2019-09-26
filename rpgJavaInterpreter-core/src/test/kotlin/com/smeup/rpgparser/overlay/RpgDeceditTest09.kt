package com.smeup.rpgparser.overlay

import com.smeup.rpgparser.assertASTCanBeProduced
import com.smeup.rpgparser.assertCanBeParsed
import com.smeup.rpgparser.execute
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
import org.junit.Ignore
import org.junit.Test
import kotlin.test.assertTrue

@Ignore
class RpgDeceditTest09 {

    // Temporary replacement to return RpgParserResult
    private fun assertCanBeParsed(exampleName: String, withMuteSupport: Boolean = true): RpgParserResult {
        val result = RpgParserFacade()
                .apply { this.muteSupport = withMuteSupport }
                .parse(inputStreamFor(exampleName))
        assertTrue(result.correct,
                message = "Errors: ${result.errors.joinToString(separator = ", ")}")

        return result
    }
    // Temporary replacement
    private fun assertASTCanBeProduced(
        exampleName: String,
        considerPosition: Boolean = false,
        withMuteSupport: Boolean = true
    ): CompilationUnit {
        val parseTreeRoot = assertCanBeParsed(exampleName, withMuteSupport)
        val ast = parseTreeRoot.root!!.rContext.toAst(ToAstConfiguration(
                considerPosition = considerPosition))
        if (withMuteSupport) {
            if (!considerPosition) {
                throw IllegalStateException("Mute annotations can be injected only when retaining the position")
            }
        }
        if (withMuteSupport) {
            ast.injectMuteAnnotation(parseTreeRoot.root!!.muteContexts!!)
        }
        return ast
    }

    @Test
    fun parseMUTE09_02_default() {
        val cu = assertASTCanBeProduced("overlay/MUTE09_02_DEFAULT", considerPosition = true, withMuteSupport = true)
        cu.resolve()

        var failed: Int = 0

        val interpreter = InternalInterpreter(JavaSystemInterface())

        interpreter.execute(cu, mapOf())
        val annotations = interpreter.systemInterface.getExceutedAnnotation().toSortedMap()
        annotations.forEach { (line, annotation) ->
            try {
                assertTrue(annotation.result.asBoolean().value)
            } catch (e: AssertionError) {
                println("${annotation.programName}: $line ${annotation.expression.render()} ${annotation.result.asBoolean().value}")
                failed++
            }
        }
        if (failed > 0) {
            throw AssertionError("$failed/${annotations.size} failed annotation(s) ")
        }
    }
    @Test
    fun parseMUTE09_02_comma() {
        val cu = assertASTCanBeProduced("overlay/MUTE09_02_COMMA", considerPosition = true, withMuteSupport = true)
        cu.resolve()

        var failed: Int = 0

        val interpreter = InternalInterpreter(JavaSystemInterface())
        // Changes the default decedit
        interpreter.decedit = ","
        interpreter.execute(cu, mapOf())
        val annotations = interpreter.systemInterface.getExceutedAnnotation().toSortedMap()
        annotations.forEach { (line, annotation) ->
            try {
                assertTrue(annotation.result.asBoolean().value)
            } catch (e: AssertionError) {
                println("${annotation.programName}: $line ${annotation.expression.render()} ${annotation.result.asBoolean().value}")
                failed++
            }
        }
        if (failed > 0) {
            throw AssertionError("$failed/${annotations.size} failed annotation(s) ")
        }
    }

    @Test
    fun parseMUTE09_02A() {
        val cu = assertASTCanBeProduced("overlay/MUTE09_02A", considerPosition = true, withMuteSupport = true)
        cu.resolve()

        var failed: Int = 0

        val interpreter = InternalInterpreter(JavaSystemInterface())
        interpreter.decedit = "0,"
        // Changes the default decedit
        interpreter.execute(cu, mapOf())
        val annotations = interpreter.systemInterface.getExceutedAnnotation().toSortedMap()
        annotations.forEach { (line, annotation) ->
            try {
                assertTrue(annotation.result.asBoolean().value)
            } catch (e: AssertionError) {
                println("${annotation.programName}: $line ${annotation.expression.render()} ${annotation.result.asBoolean().value}")
                failed++
            }
        }
        if (failed > 0) {
            throw AssertionError("$failed/${annotations.size} failed annotation(s) ")
        }
    }
}
