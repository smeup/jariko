package com.smeup.dspfparser.linesprocessor

import kotlin.test.Test
import kotlin.test.assertEquals

internal class LineDeclarationTypeTest : LinesProcessorLoader("./src/test/resources/DECLARATIONS.dspf") {
    init {
        this.linesProcessor.createLines()
    }

    @Test
    fun isRecord() {
        assertEquals(this.linesProcessor.lines[0].type, LineType.RECORD)
    }

    @Test
    fun isHelp() {
        assertEquals(this.linesProcessor.lines[1].type, LineType.HELP)
    }

    @Test
    fun isField() {
        assertEquals(this.linesProcessor.lines[2].type, LineType.FIELD)
    }

    @Test
    fun isOther() {
        assertEquals(this.linesProcessor.lines[3].type, LineType.OTHER)
    }
}
