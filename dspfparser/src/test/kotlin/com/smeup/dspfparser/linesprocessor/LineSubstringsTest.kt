package com.smeup.dspfparser.linesprocessor

import kotlin.test.Test
import kotlin.test.assertEquals

internal class LineSubstringsTest : LinesProcessorLoader("./src/test/resources/_fake.dspf") {
    init {
        this.linesProcessor.createLinesSubstrings()
    }

    @Test
    fun line_1() {
        val line = this.linesProcessor.linesSubstrings[0]
        // this scope does not check types so everything will be accepted as substring
        assertEquals(1, line.count)
        assertEquals("00101", line.sequenceNumber)
        assertEquals("A", line.a)
        assertEquals(" N01 02N03", line.condition)
        assertEquals("R", line.typeOfName)
        assertEquals(" ", line.reserved)
        assertEquals("RECORD0001", line.fieldName)
        assertEquals("R", line.reference)
        assertEquals("   45", line.length)
        assertEquals("W", line.dataTypeKeyboardShift)
        assertEquals("10", line.decimalsPositions)
        assertEquals("B", line.fieldType)
        assertEquals(" 12", line.y)
        assertEquals(" 22", line.x)
        assertEquals("TEXT('Hello')", line.keywords!!)
    }

    @Test
    fun line_2() {
        val line = this.linesProcessor.linesSubstrings[1]
        assertEquals("* This is a line with a comment!", line.comment)
    }

    @Test
    fun line_3() {
        val line = this.linesProcessor.linesSubstrings[2]
        assertEquals("'* Test with line break starting at column 45 *'", line.keywords)
    }

    @Test
    fun line_4() {
        val line = this.linesProcessor.linesSubstrings[3]
        assertEquals("'* Test with line break starting at any column *'", line.keywords)
    }

    @Test
    fun line_5() {
        val line = this.linesProcessor.linesSubstrings[4]
        assertEquals(" 12", line.y)
        assertEquals(" 22", line.x)
        assertEquals("'Line break with something before'", line.keywords)
    }

    @Test
    fun line_6() {
        val line = this.linesProcessor.linesSubstrings[5]
        assertEquals(" N01 02 03  07      ", line.condition)
        assertEquals("'Line break with conditions'", line.keywords)
    }
}
