package com.smeup.rpgparser.parsing



import com.smeup.rpgparser.ast.MuteAnnotationResolved
import com.smeup.rpgparser.facade.RpgParserFacade
import com.smeup.rpgparser.facade.RpgParserResult
import com.smeup.rpgparser.inputStreamFor
import com.smeup.rpgparser.parsetreetoast.injectMuteAnnotation
import com.smeup.rpgparser.parsetreetoast.toAst
import org.junit.Test;
import kotlin.test.assertEquals
import kotlin.test.assertTrue

public class RpgParserWithMuteScopeTest {

    var printResults : Boolean = true

    // Useful display function for debugging
    private fun showResults(resolved: List<MuteAnnotationResolved> ) {
        if( this.printResults ) {
            val sorted  = resolved.sortedWith(compareBy({ it.muteLine }))
            sorted.forEach {
                println("Mute at line ${it.muteLine} attached to statement ${it.statementLine}" )
            }

        }
    }

    private fun getResolvedAnnotation(line: Int,  annotations: List<MuteAnnotationResolved> ) : MuteAnnotationResolved? {
        annotations.forEach {
            if(it.muteLine == line) {
                return it
            }
        }
        return null

    }

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
        val resolved : List<MuteAnnotationResolved>
        val result = assertCanBeParsed("mute/MUTE01_SCOPE",withMuteSupport = true)

        val cu = result.root!!.rContext.toAst().apply {
            resolved  = this.injectMuteAnnotation(result.root!!.rContext, result.root!!.muteContexts!!)
        }

        showResults(resolved)

        assertEquals(resolved.size, 3)
        var annotation = getResolvedAnnotation(14,resolved)
        assertTrue (actual = annotation != null)
        assertEquals(annotation.statementLine, 16)

        annotation = getResolvedAnnotation(15,resolved)
        assertTrue (actual = annotation != null)
        assertEquals(annotation.statementLine, 16)

        annotation = getResolvedAnnotation(22,resolved)
        assertTrue (actual = annotation != null)
        assertEquals(annotation.statementLine, 23)



    }
}