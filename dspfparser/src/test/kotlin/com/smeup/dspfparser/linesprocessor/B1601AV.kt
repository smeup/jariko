package com.smeup.dspfparser.linesprocessor

import kotlin.test.Test

internal class B1601AV : LinesProcessorLoader("./src/test/resources/B£1601AV.dspf") {
    @Test
    fun doesNotThrowErrors() {
        this.linesProcessor.createLines()
    }
}
