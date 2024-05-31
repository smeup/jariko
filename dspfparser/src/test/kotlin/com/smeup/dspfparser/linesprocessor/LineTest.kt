package com.smeup.dspfparser.linesprocessor

import com.smeup.dspfparser.positionals.DTKBS
import com.smeup.dspfparser.positionals.FieldType
import com.smeup.dspfparser.positionals.Reference
import com.smeup.dspfparser.positionals.Reserved
import com.smeup.dspfparser.positionals.TypeOfName
import kotlin.test.Test
import kotlin.test.assertEquals

internal class LineTest : LinesProcessorLoader("./src/test/resources/_fake.dspf") {
    init {
        this.linesProcessor.createLines()
    }

    @Test
    fun line_1() {
        val line = this.linesProcessor.lines[0]
        assertEquals(1, line.count)
        assertEquals("00101", line.sequenceNumber)
        assertEquals('A', line.a)
        assertEquals(TypeOfName.R, line.typeOfName)
        assertEquals(Reserved.BLANK, line.reserved)
        assertEquals("RECORD0001", line.fieldName)
        assertEquals(Reference.R, line.reference)
        assertEquals(45, line.length)
        assertEquals(DTKBS.W, line.dataTypeKeyboardShift)
        assertEquals(10, line.decimalsPositions)
        assertEquals(FieldType.B, line.fieldType)
        assertEquals(12, line.y)
        assertEquals(22, line.x)

        if (SHOULD_GET_CONDITIONS_AND_KEYWORDS) {
            assertEquals(
                DSPFConditionsGroup.fromString(" N01 02N03"),
                line.conditions
            )
            assertEquals(
                DSPFKeywordsGroup.fromString("TEXT('Hello')"),
                line.keywords
            )
        }
    }

    @Test
    fun line_2() {
        val line = this.linesProcessor.lines[1]

        if (SHOULD_GET_CONDITIONS_AND_KEYWORDS) {
            assertEquals(
                DSPFKeywordsGroup.fromString("'* Test with line break starting at column 45 *'"),
                line.keywords
            )
        }
    }

    @Test
    fun line_3() {
        val line = this.linesProcessor.lines[2]

        if (SHOULD_GET_CONDITIONS_AND_KEYWORDS) {
            assertEquals(
                DSPFKeywordsGroup.fromString("'* Test with line break starting at any column *'"),
                line.keywords
            )
        }
    }

    @Test
    fun line_4() {
        val line = this.linesProcessor.lines[3]
        assertEquals(12, line.y)
        assertEquals(22, line.x)

        if (SHOULD_GET_CONDITIONS_AND_KEYWORDS) {
            assertEquals(
                DSPFKeywordsGroup.fromString("'Line break with something before'"),
                line.keywords
            )
        }
    }

    @Test
    fun line_5() {
        val line = this.linesProcessor.lines[4]

        if (SHOULD_GET_CONDITIONS_AND_KEYWORDS) {
            assertEquals(
                DSPFConditionsGroup.fromString(" N01 02 03  07      "),
                line.conditions
            )
            assertEquals(
                DSPFKeywordsGroup.fromString("'Line break with conditions'"),
                line.keywords
            )
        }
    }
}
