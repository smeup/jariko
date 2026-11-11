package com.smeup.rpgparser.parsing.parsetreetoast

import org.junit.Test
import kotlin.test.assertEquals

/**
 * [isInt] no longer uses a regular expression: it must give the same answer as the pattern it replaced.
 */
class NumericStringChecksTest {
    private val previousIsInt = Regex("^-?[0-9]+$")

    private val samples =
        (
            "|-|--1|0|-0|7|-7|00|123|-123|99999999999999999999|+1|1-|1.0|1,0| 1|1 |1a|a1|*ON|LR|IN|١٢|１"
                .split("|") + listOf("1\n", "\n1")
        )

    @Test
    fun isIntGivesTheSameResultAsTheRegexItReplaced() {
        samples.forEach { sample ->
            assertEquals(sample.matches(previousIsInt), sample.isInt(), "isInt(\"$sample\")")
        }
    }

    @Test
    fun precompiledChecksKeepTheirResults() {
        assertEquals(true, "-1.5".isDecimal())
        assertEquals(true, ".5".isDecimal())
        assertEquals(false, "1".isDecimal())
        assertEquals(true, "1e10".isFloatingPointNumber())
        assertEquals(false, "e10".isFloatingPointNumber())
        assertEquals(true, "12".isNumber())
        assertEquals(false, "1a".isNumber())
    }
}
