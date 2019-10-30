package com.smeup.rpgparser.parsing

import com.smeup.rpgparser.assertCanBeParsedResult
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.parsing.parsetreetoast.ToAstConfiguration
import com.smeup.rpgparser.parsing.parsetreetoast.injectMuteAnnotation
import com.smeup.rpgparser.parsing.parsetreetoast.toAst
import java.lang.IllegalStateException
import org.junit.Test

public class RpgParserWithMuteSyntaxtTest {

    // Temporary replacement
    private fun assertASTCanBeProduced(
        exampleName: String,
        considerPosition: Boolean = false,
        withMuteSupport: Boolean = false
    ): CompilationUnit {
        val parseTreeRoot = assertCanBeParsedResult(exampleName, withMuteSupport)
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
    fun parseMUTE01_syntax() {
        assertCanBeParsedResult("mute/MUTE01_SYNTAX", withMuteSupport = true)
    }

    @Test
    fun parseMUTE01_ast() {
        assertASTCanBeProduced("mute/MUTE01_SYNTAX", considerPosition = true, withMuteSupport = true)
    }
}
