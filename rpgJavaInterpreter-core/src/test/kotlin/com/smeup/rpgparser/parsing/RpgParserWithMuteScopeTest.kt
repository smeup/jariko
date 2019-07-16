package com.smeup.rpgparser.parsing



import com.smeup.rpgparser.facade.RpgParserFacade
import com.smeup.rpgparser.facade.RpgParserResult
import com.smeup.rpgparser.inputStreamFor
import com.smeup.rpgparser.parsetreetoast.injectMuteAnnotation
import com.smeup.rpgparser.parsetreetoast.toAst
import org.junit.Test;
import kotlin.test.assertEquals
import kotlin.test.assertTrue

public class RpgParserWithMuteScopeTest {

    // Temporary replacement to return RpgParserResult
    private fun assertCanBeParsed(exampleName: String, withMuteSupport: Boolean = false) : RpgParserResult {
        val result = RpgParserFacade()
                .apply { this.muteSupport = withMuteSupport }
                .parse(inputStreamFor(exampleName))
        assertTrue(result.correct,
                message = "Errors: ${result.errors.joinToString(separator = ", ")}")

        return result
    }


    @Test
    fun parseMUTE01_scope() {
        val result = assertCanBeParsed("mute/MUTE01_SCOPE",withMuteSupport = true)

        val cu = result.root!!.rContext.toAst().apply {
            this.injectMuteAnnotation(result.root!!.rContext, result.root!!.muteContexts!!)
        }
        // Data decalaration annotation are attachd at the first statement in main ??
        //assertTrue( cu.main.stmts[0].muteAnnotations.size == 2 )
        //assertTrue( cu.main.stmts[0].position!!.start.line == 11 )

        // Main annotation are attached to the closest next statement (line 15)
        assertTrue( cu.main.stmts[2].muteAnnotations.size == 2 )
        assertTrue( cu.main.stmts[2].position!!.start.line == 16 )


        // Subroutine annotation are attached to closest next statement
        assertTrue ( cu.subroutines[0].stmts[0].muteAnnotations.size == 1)
        assertTrue ( cu.subroutines[0].stmts[0].position!!.start.line == 23)

    }
}