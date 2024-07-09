package com.smeup.dspfparser.linesprocessor

import com.smeup.dspfparser.positionals.DTKBS
import com.smeup.dspfparser.positionals.FieldType
import com.smeup.dspfparser.positionals.Reference
import com.smeup.dspfparser.positionals.Reserved
import com.smeup.dspfparser.positionals.TypeOfName
import kotlin.test.Test
import kotlin.test.assertEquals

internal class LineTest : LinesProcessorLoader("./src/test/resources/FAKE.dspf") {
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
    }

    @Test
    fun line_4() {
        val line = this.linesProcessor.lines[3]
        assertEquals(12, line.y)
        assertEquals(22, line.x)
    }
}
