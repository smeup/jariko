package com.smeup.dspfparser.linesprocessor

import kotlin.test.Test
import kotlin.test.assertTrue

internal class LineDeclarationTypeTest : LinesProcessorLoader("./src/test/resources/D1.dspf") {
    init {
        this.linesProcessor.createLines()
    }

    @Test
    fun isRecord() {
        assertTrue { this.linesProcessor.lines[0].isRecord() }
    }

    @Test
    fun isHelp() {
        assertTrue { this.linesProcessor.lines[1].isHelp() }
    }

    @Test
    fun isField() {
        assertTrue { this.linesProcessor.lines[2].isField() }
    }

    @Test
    fun isTheSameAsPreviousLine() {
        assertTrue { this.linesProcessor.lines[3].isNone() }
    }
}
