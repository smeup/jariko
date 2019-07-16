package com.smeup.rpgparser.parsing



import com.smeup.rpgparser.facade.RpgParserFacade
import com.smeup.rpgparser.facade.RpgParserResult
import com.smeup.rpgparser.inputStreamFor
import com.smeup.rpgparser.parsetreetoast.injectMuteAnnotation
import com.smeup.rpgparser.parsetreetoast.toAst
import org.junit.Test;
import kotlin.test.assertEquals
import kotlin.test.assertTrue

public class RpgParserWithMutePositionTest {

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
    fun parseMUTE01_position_for() {
        val result = assertCanBeParsed("mute/MUTE01_POSITION",withMuteSupport = true)

        val cu = result.root!!.rContext.toAst().apply {
            this.injectMuteAnnotation(result.root!!.rContext, result.root!!.muteContexts!!)
        }
        // Subroutine annotation are attached to closest next statement
        assertTrue ( cu.main.stmts[0].muteAnnotations.size == 0)



    }
}