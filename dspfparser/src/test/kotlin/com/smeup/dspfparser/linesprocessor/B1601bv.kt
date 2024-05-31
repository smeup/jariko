package com.smeup.dspfparser.linesprocessor

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import kotlin.test.Test

internal class B1601bv : LinesProcessorLoader("./src/test/resources/b£1601bv.dspf") {
    @Test
    fun doesNotThrowErrors() {
        assertDoesNotThrow { this.linesProcessor.createLines() }
    }
}
