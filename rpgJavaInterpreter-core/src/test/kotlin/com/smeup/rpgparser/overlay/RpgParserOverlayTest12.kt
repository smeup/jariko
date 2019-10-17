package com.smeup.rpgparser.overlay

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
import kotlin.test.assertEquals
import kotlin.test.assertTrue

public class RpgParserOverlayTest12 {
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
    fun parseMUTE12_01_syntax() {
        val result = assertCanBeParsed("overlay/MUTE12_01", withMuteSupport = true)
    }

    @Test
    fun parseMUTE12_01_ast() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_01", considerPosition = true, withMuteSupport = true)
        val f1 = cu.dataDefinitions.find { it.name == "§F1" }!!
        val uibds = cu.dataDefinitions.find { it.name == "£UIBDS" }!!
        assertEquals(36, f1.fields.size)
        assertEquals(36, uibds.fields.size)
        assertEquals(uibds.type, f1.type)
    }

    @Test
    @Ignore // clear is not supported, yet
    fun parseMUTE12_01_runtime() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_01", considerPosition = true, withMuteSupport = true)
        cu.resolve()

        var failed: Int = 0

        val interpreter = InternalInterpreter(JavaSystemInterface())

        interpreter.execute(cu, mapOf())
        val annotations = interpreter.systemInterface.getExecutedAnnotation().toSortedMap()
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
    fun parseMUTE12_02_syntax() {
        assertCanBeParsed("overlay/MUTE12_02", withMuteSupport = true)
    }

    @Test
    fun parseMUTE12_02_ast() {
        assertASTCanBeProduced("overlay/MUTE12_02", considerPosition = true, withMuteSupport = true)
    }

    @Test
    @Ignore // PACKEVEN not supported
    fun parseMUTE12_02_runtime() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_02", considerPosition = true, withMuteSupport = true)
        cu.resolve()
        val interpreter = InternalInterpreter(JavaSystemInterface())
        interpreter.execute(cu, mapOf())
    }

    @Test
    fun parseMUTE12_03_syntax() {
        assertCanBeParsed("overlay/MUTE12_03", withMuteSupport = true)
    }

    @Test
    @Ignore // fix automatic calculation of DS size if not present
    fun parseMUTE12_03_ast() {
        assertASTCanBeProduced("overlay/MUTE12_03", considerPosition = true, withMuteSupport = true)
    }

    @Test
    @Ignore // fix data structure arrays
    fun parseMUTE12_03_runtime() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_03", considerPosition = true, withMuteSupport = true)
        cu.resolve()
        val interpreter = InternalInterpreter(JavaSystemInterface())
        interpreter.execute(cu, mapOf())
    }

    @Test
    fun parseMUTE12_04_syntax() {
        assertCanBeParsed("overlay/MUTE12_04", withMuteSupport = true)
    }

    @Test
    fun parseMUTE12_04_ast() {
        assertASTCanBeProduced("overlay/MUTE12_04", considerPosition = true, withMuteSupport = true)
    }

    @Test
    fun parseMUTE12_04_runtime() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_04", considerPosition = true, withMuteSupport = true)
        cu.resolve()
        val interpreter = InternalInterpreter(JavaSystemInterface())
        interpreter.execute(cu, mapOf())
    }

    @Test
    fun parseMUTE12_05_syntax() {
        val result = assertCanBeParsed("overlay/MUTE12_05", withMuteSupport = true)
    }

    @Test
    fun parseMUTE12_05_ast() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_05", considerPosition = true, withMuteSupport = true)
    }

    @Test
    fun parseMUTE12_05_runtime() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_05", considerPosition = true, withMuteSupport = true)
        cu.resolve()

        var failed: Int = 0

        val interpreter = InternalInterpreter(JavaSystemInterface())

        interpreter.execute(cu, mapOf())
        val annotations = interpreter.systemInterface.getExecutedAnnotation().toSortedMap()
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
    @Ignore // Not yet implemented
    fun parseMUTE12_06_syntax() {
        assertCanBeParsed("overlay/MUTE12_06", withMuteSupport = true)
    }

    @Test
    @Ignore // Not yet implemented
    fun parseMUTE12_06_ast() {

        assertASTCanBeProduced("overlay/MUTE12_06", considerPosition = true, withMuteSupport = true)
    }

    @Test
    @Ignore // Not yet implemented
    fun parseMUTE12_06_runtime() {
        val cu = assertASTCanBeProduced("overlay/MUTE12_06", considerPosition = true, withMuteSupport = true)
        cu.resolve()
        val interpreter = InternalInterpreter(JavaSystemInterface())
        interpreter.execute(cu, mapOf())
    }
}
