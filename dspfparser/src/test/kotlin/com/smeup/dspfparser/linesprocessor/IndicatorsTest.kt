package com.smeup.dspfparser.linesprocessor

import com.smeup.dspfparser.positionals.BooleanUnaryOperator
import kotlin.test.Test
import kotlin.test.assertEquals

internal class IndicatorsTest {
    @Test
    fun indicator() {
        val default = DSPFIndicator.fromString(" 02")
        val not = DSPFIndicator.fromString("N12")
        assertEquals(BooleanUnaryOperator.BLANK, default.op)
        assertEquals(2, default.id)
        assertEquals(BooleanUnaryOperator.N, not.op)
        assertEquals(12, not.id)
    }
}
