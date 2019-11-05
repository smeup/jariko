package com.smeup.rpgparser.parsing

import com.smeup.rpgparser.assertCanBeParsedResult
import com.smeup.rpgparser.parsing.ast.MuteAnnotationResolved
import com.smeup.rpgparser.parsing.parsetreetoast.injectMuteAnnotation
import com.smeup.rpgparser.parsing.parsetreetoast.toAst
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.Test

public class RpgParserWithMuteScopeTest {

    var printResults: Boolean = true

    // Useful display function for debugging
    private fun showResults(resolved: List<MuteAnnotationResolved>) {
        if (this.printResults) {
            val sorted = resolved.sortedWith(compareBy({ it.muteLine }))
            sorted.forEach {
                println("Mute at line ${it.muteLine} attached to statement ${it.statementLine}")
            }
        }
    }

    private fun getResolvedAnnotation(line: Int, annotations: List<MuteAnnotationResolved>): MuteAnnotationResolved? {
        annotations.forEach {
            if (it.muteLine == line) {
                return it
            }
        }
        return null
    }

    @Test
    fun parseMUTE01_scope() {
        val resolved: List<MuteAnnotationResolved>
        val result = assertCanBeParsedResult("mute/MUTE01_SCOPE", withMuteSupport = true)

        result.root!!.rContext.toAst().apply {
            resolved = this.injectMuteAnnotation(result.root!!.muteContexts!!)
        }

        showResults(resolved)

        assertEquals(resolved.size, 5)

        // Data definitions
        var annotation = getResolvedAnnotation(3, resolved)
        assertTrue(actual = annotation != null)
        assertEquals(annotation.statementLine, 4)

        annotation = getResolvedAnnotation(10, resolved)
        assertTrue(actual = annotation != null)
        assertEquals(annotation.statementLine, 9)

        annotation = getResolvedAnnotation(15, resolved)
        assertTrue(actual = annotation != null)
        assertEquals(annotation.statementLine, 17)

        annotation = getResolvedAnnotation(16, resolved)
        assertTrue(actual = annotation != null)
        assertEquals(annotation.statementLine, 17)

        annotation = getResolvedAnnotation(23, resolved)
        assertTrue(actual = annotation != null)
        assertEquals(annotation.statementLine, 24)
    }
}
