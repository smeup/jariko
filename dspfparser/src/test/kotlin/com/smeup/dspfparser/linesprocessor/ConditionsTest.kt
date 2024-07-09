package com.smeup.dspfparser.linesprocessor

import com.smeup.dspfparser.positionals.BooleanNAryOperator
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ConditionsTest {
    @Test
    fun withDefaultBooleanOperator() {
        val default = DSPFCondition.fromString(" N15 14 89")
        assertEquals(BooleanNAryOperator.BLANK, default.op)
        assertEquals(
            mutableListOf(DSPFIndicator.fromString("N15"), DSPFIndicator.fromString(" 14"), DSPFIndicator.fromString(" 89")),
            default.indicators
        )
    }

    @Test
    fun withAndBooleanOperator() {
        val and = DSPFCondition.fromString("AN15 17 90")
        assertEquals(BooleanNAryOperator.A, and.op)
        assertEquals(
            mutableListOf(DSPFIndicator.fromString("N15"), DSPFIndicator.fromString(" 17"), DSPFIndicator.fromString(" 90")),
            and.indicators
        )
    }

    @Test
    fun withOrBooleanOperator() {
        val or = DSPFCondition.fromString("ON15 11 04")
        assertEquals(BooleanNAryOperator.O, or.op)
        assertEquals(
            mutableListOf(DSPFIndicator.fromString("N15"), DSPFIndicator.fromString(" 11"), DSPFIndicator.fromString(" 04")),
            or.indicators
        )
    }

    @Test
    fun partial() {
        val partial = DSPFCondition.fromString("ON18      ")
        assertEquals(BooleanNAryOperator.O, partial.op)
        assertEquals(mutableListOf(DSPFIndicator.fromString("N18")), partial.indicators)
    }

    @Test
    fun empty() {
        val empty = DSPFCondition.fromString("          ")
        assertEquals(BooleanNAryOperator.BLANK, empty.op)
        assertEquals(emptyList(), empty.indicators)
    }

    @Test
    fun partialGroup() {
        val conditions = DSPFConditionsGroup.fromString(" N01 02 23A 38      ")
        assertEquals(DSPFCondition.fromString(" N01 02 23"), conditions.group[0])
        assertEquals(DSPFCondition.fromString("A 38      "), conditions.group[1])
    }

    @Test
    fun fullGroup() {
        val conditions = DSPFConditionsGroup.fromString(" N01 02N03ON88 19 90")
        assertEquals(DSPFCondition.fromString(" N01 02N03"), conditions.group[0])
        assertEquals(DSPFCondition.fromString("ON88 19 90"), conditions.group[1])
    }
}
