package com.smeup.dspfparser.linesprocessor

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import kotlin.test.Test

internal class B1601av : LinesProcessorLoader("./src/test/resources/b£1601av.dspf") {
    @Test
    fun doesNotThrowErrors() {
        assertDoesNotThrow { this.linesProcessor.createLines() }
    }
}
