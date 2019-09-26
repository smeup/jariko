package com.smeup.rpgparser.parsing

import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.execute
import com.smeup.rpgparser.parsing.facade.RpgParserFacade
import com.smeup.rpgparser.parsing.facade.RpgParserResult
import com.smeup.rpgparser.inputStreamFor
import com.smeup.rpgparser.parsing.parsetreetoast.ToAstConfiguration
import com.smeup.rpgparser.parsing.parsetreetoast.injectMuteAnnotation
import com.smeup.rpgparser.parsing.parsetreetoast.resolve
import com.smeup.rpgparser.parsing.parsetreetoast.toAst
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.Test

public class RpgParserWithMuteRuntimeTest {

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
    fun parseMUTE01_runtime() {
        val cu = assertASTCanBeProduced("mute/MUTE01_RUNTIME", true)
        cu.resolve()
        val interpreter = execute(cu, mapOf())

        assertEquals(interpreter.systemInterface.getExecutedAnnotation().size, 8)

        // VAL1(FIELD1) VAL2('AAAA') COMP(EQ)
        var annotation = interpreter.systemInterface.getExecutedAnnotation()[3]
        assertTrue(actual = annotation != null)
        assertTrue(annotation.result.asBoolean().value)

        // VAL1(NBR) VAL2(11) COMP(LT)
        annotation = interpreter.systemInterface.getExecutedAnnotation()[7]
        assertTrue(actual = annotation != null)
        assertTrue(annotation.result.asBoolean().value)

        // VAL1(FIELD1) VAL2('A ' + ' B') COMP(NE)
        annotation = interpreter.systemInterface.getExecutedAnnotation()[14]
        assertTrue(actual = annotation != null)
        assertFalse(annotation.result.asBoolean().value)

        // VAL1(B) VAL2(1) COMP(GE)
        annotation = interpreter.systemInterface.getExecutedAnnotation()[16]
        assertTrue(actual = annotation != null)
        assertTrue(annotation.result.asBoolean().value)

        // VAL1(B) VAL2(1) COMP(LE)
        annotation = interpreter.systemInterface.getExecutedAnnotation()[17]
        assertTrue(actual = annotation != null)
        assertTrue(annotation.result.asBoolean().value)

        // VAL1(B) VAL2(1) COMP(GT)
        annotation = interpreter.systemInterface.getExecutedAnnotation()[19]
        assertTrue(actual = annotation != null)
        assertFalse(annotation.result.asBoolean().value)

        // VAL1(B) VAL2(1) COMP(LT)
        annotation = interpreter.systemInterface.getExecutedAnnotation()[20]
        assertTrue(actual = annotation != null)
        assertFalse(annotation.result.asBoolean().value)

        // VAL1(COUNT) VAL2(4) COMP(LE)
        annotation = interpreter.systemInterface.getExecutedAnnotation()[28]
        assertTrue(actual = annotation != null)
        assertTrue(annotation.result.asBoolean().value)
    }

    @Test
    fun parseMUTE02_runtime() {
        val cu = assertASTCanBeProduced("mute/MUTE02_RUNTIME", true)
        cu.resolve()
        val interpreter = execute(cu, mapOf())

        assertEquals(interpreter.systemInterface.getExecutedAnnotation().size, 5)

        // VAL1(VAR1) VAL2(%TRIM(' AAAA ')) COMP(EQ)
        var annotation = interpreter.systemInterface.getExecutedAnnotation()[3]
        assertTrue(actual = annotation != null)
        assertTrue(annotation.result.asBoolean().value)

        //  VAL1(VALUE1) VAL2('AAA:') COMP(EQ)
        annotation = interpreter.systemInterface.getExecutedAnnotation()[10]
        assertTrue(actual = annotation != null)
        assertTrue(annotation.result.asBoolean().value)

        //  VAL1(VALUE1) VAL2('  AAA:') COMP(EQ)
        annotation = interpreter.systemInterface.getExecutedAnnotation()[12]
        assertTrue(actual = annotation != null)
        // this one fail, as expected
        assertFalse(annotation.result.asBoolean().value)

        //  VAL1(%TRIMR(VAR1) +':') VAL2('  AAA:') COMP(EQ)
        annotation = interpreter.systemInterface.getExecutedAnnotation()[14]
        assertTrue(actual = annotation != null)
        assertTrue(annotation.result.asBoolean().value)

        //  VAL1(VALUE1) VAL2('AAA                         :') COMP(NE)
        annotation = interpreter.systemInterface.getExecutedAnnotation()[15]
        assertTrue(actual = annotation != null)
        assertTrue(annotation.result.asBoolean().value)
    }

    @Test
    fun parseMUTE02_runtimeWithArray() {
        val cu = assertASTCanBeProduced("mute/MUTE02_RUNTIME_array", true)
        cu.resolve()
        val interpreter = execute(cu, mapOf())

        assertEquals(interpreter.systemInterface.getExecutedAnnotation().size, 1)

        // VAL1(AR(1)) VAL2(4) COMP(NE)
        val annotation = interpreter.systemInterface.getExecutedAnnotation()[2]
        assertTrue(actual = annotation != null)
        assertTrue(annotation.result.asBoolean().value)
    }
}
