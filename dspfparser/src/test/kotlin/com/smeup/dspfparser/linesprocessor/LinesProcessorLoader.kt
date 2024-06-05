package com.smeup.dspfparser.linesprocessor

import java.io.File

internal open class LinesProcessorLoader {
    private val source: String
    protected val linesProcessor: LinesProcessor

    constructor(source: String) {
        this.source = source
        this.linesProcessor = LinesProcessor(File(source).bufferedReader())
    }
}
