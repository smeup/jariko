package com.smeup.rpgparser.parsing

import com.smeup.rpgparser.assertCanBeParsedResult
import com.smeup.rpgparser.parsing.ast.MuteAnnotationResolved
import com.smeup.rpgparser.parsing.parsetreetoast.injectMuteAnnotation
import com.smeup.rpgparser.parsing.parsetreetoast.toAst
import kotlin.test.assertTrue
import org.junit.Test

class RpgParserWithMutePositionTest {

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
    fun parseMUTE01_position_for() {
        val resolved: List<MuteAnnotationResolved>
        val result = assertCanBeParsedResult("mute/MUTE01_POSITION", withMuteSupport = true)

        result.root!!.rContext.toAst().apply {
            resolved = this.injectMuteAnnotation(result.root!!.muteContexts!!)
        }

        showResults(resolved)

        assertTrue(resolved.size == 3)
        var annotation = getResolvedAnnotation(11, resolved)
        assertTrue(annotation != null)
        assertTrue(annotation.statementLine == 12)

        annotation = getResolvedAnnotation(14, resolved)
        assertTrue(annotation != null)
        assertTrue(annotation.statementLine == 15)

        annotation = getResolvedAnnotation(17, resolved)
        assertTrue(annotation != null)
        assertTrue(annotation.statementLine == 18)
    }

    @Test
    fun parseMUTE02_position_select() {
        val resolved: List<MuteAnnotationResolved>
        val result = assertCanBeParsedResult("mute/MUTE02_POSITION", withMuteSupport = true)
        result.root!!.rContext.toAst().apply {
            resolved = this.injectMuteAnnotation(result.root!!.muteContexts!!)
        }

        showResults(resolved)
        // Subroutine annotation are attached to closest next statement
        assertTrue(resolved.size == 4)

        var annotation = getResolvedAnnotation(12, resolved)
        assertTrue(annotation != null)
        assertTrue(annotation.statementLine == 13)

        annotation = getResolvedAnnotation(16, resolved)
        assertTrue(annotation != null)
        assertTrue(annotation.statementLine == 17)

        annotation = getResolvedAnnotation(21, resolved)
        assertTrue(annotation != null)
        assertTrue(annotation.statementLine == 23)

        annotation = getResolvedAnnotation(22, resolved)
        assertTrue(annotation != null)
        assertTrue(annotation.statementLine == 23)
    }

    @Test
    fun parseMUTE02_position_if() {
        val resolved: List<MuteAnnotationResolved>
        val result = assertCanBeParsedResult("mute/MUTE03_POSITION", withMuteSupport = true)
        result.root!!.rContext.toAst().apply {
            resolved = this.injectMuteAnnotation(result.root!!.muteContexts!!)
        }

        showResults(resolved)

        assertTrue(resolved.size == 4)
        var annotation = getResolvedAnnotation(13, resolved)
        assertTrue(annotation != null)
        assertTrue(annotation.statementLine == 14)

        annotation = getResolvedAnnotation(16, resolved)
        assertTrue(annotation != null)
        assertTrue(annotation.statementLine == 17)

        annotation = getResolvedAnnotation(20, resolved)
        assertTrue(annotation != null)
        assertTrue(annotation.statementLine == 21)

        annotation = getResolvedAnnotation(24, resolved)
        assertTrue(annotation != null)
        assertTrue(annotation.statementLine == 25)
    }

    @Test
    fun parseMUTE02_position_do() {
        val resolved: List<MuteAnnotationResolved>
        val result = assertCanBeParsedResult("mute/MUTE04_POSITION", withMuteSupport = true)
        result.root!!.rContext.toAst().apply {
            resolved = this.injectMuteAnnotation(result.root!!.muteContexts!!)
        }

        showResults(resolved)
        // Subroutine annotation are attached to closest next statement
        assertTrue(resolved.size == 2)

        var annotation = getResolvedAnnotation(11, resolved)
        assertTrue(annotation != null)
        assertTrue(annotation.statementLine == 12)

        annotation = getResolvedAnnotation(15, resolved)
        assertTrue(annotation != null)
        assertTrue(annotation.statementLine == 16)
    }
}
