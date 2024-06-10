package com.smeup.dspfparser.linesclassifier

import com.smeup.dspfparser.linesprocessor.LinesProcessorLoader

internal open class DSPFSpecificationsLoader : LinesProcessorLoader {
    protected val specifications: DSPFSpecifications

    constructor(source: String) : super(source) {
        this.linesProcessor.createLines()
        this.specifications = DSPFSpecifications.fromLines(this.linesProcessor.lines)
    }
}
