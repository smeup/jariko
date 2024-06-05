package com.smeup.dspfparser.linesprocessor

import kotlin.test.Test

internal class B1601BV : LinesProcessorLoader("./src/test/resources/B£1601BV.dspf") {
    @Test
    fun doesNotThrowErrors() {
        this.linesProcessor.createLines()
    }
}
