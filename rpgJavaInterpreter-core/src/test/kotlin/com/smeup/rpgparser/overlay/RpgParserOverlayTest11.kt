package com.smeup.rpgparser.overlay

import com.smeup.rpgparser.DummyProgramFinder
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
import com.smeup.rpgparser.rgpinterop.RpgSystem
import kotlin.test.Ignore
import org.junit.Test
import java.lang.StringBuilder
import kotlin.test.assertTrue

class RpgParserOverlayTest11 {
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
    fun parseMUTE11_11C_syntax() {
        assertCanBeParsed("overlay/MUTE11_11C", withMuteSupport = true)
    }

    @Test
    fun parseMUTE11_11C_ast() {
        assertASTCanBeProduced("overlay/MUTE11_11C", considerPosition = true, withMuteSupport = true)
    }

    @Test
    fun parseMUTE11_11C_runtime() {
        assertMutesInExampleSucceed("overlay/MUTE11_11C")
    }

    @Test
    fun parseMUTE11_15_syntax() {
        assertCanBeParsed("overlay/MUTE11_15", withMuteSupport = true)
    }

    @Test
    fun parseMUTE11_15_ast() {
        assertASTCanBeProduced("overlay/MUTE11_15", considerPosition = true, withMuteSupport = true)
    }

    @Test
    fun parseMUTE11_15_runtime() {
        assertMutesInExampleSucceed("overlay/MUTE11_15")
    }

    @Test @Ignore
    fun parseMUTE11_16_runtime() {
        // Flaky test
        assertMutesInExampleSucceed("overlay/MUTE11O16")
    }

    private fun assertMutesInExampleSucceed(exampleName: String) {
        RpgSystem.addProgramFinder(DummyProgramFinder("/overlay/"))
        val cu = assertASTCanBeProduced(exampleName, considerPosition = true, withMuteSupport = true)
        cu.resolve()

        var failed: Int = 0

        val interpreter = InternalInterpreter(JavaSystemInterface())
        interpreter.execute(cu, mapOf())
        val annotations = interpreter.systemInterface.getExecutedAnnotation().toSortedMap()
        val messages = StringBuilder()
        annotations.forEach { (line, annotation) ->
            try {
                assertTrue(annotation.succeeded())
            } catch (e: AssertionError) {
                val msg = "${annotation.programName}: $line ${annotation.headerDescription()} ${annotation.succeeded()}"
                println(msg)
                messages.appendln(msg)
                failed++
            }
        }
        if (failed > 0) {
            throw AssertionError("$failed/${annotations.size} failed annotation(s) $messages")
        }
    }
}
