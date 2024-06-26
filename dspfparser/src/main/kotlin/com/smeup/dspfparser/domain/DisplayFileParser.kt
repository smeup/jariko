package com.smeup.dspfparser.domain

import com.smeup.dspfparser.linesclassifier.DSPF
import com.smeup.dspfparser.linesclassifier.DSPFSpecifications
import com.smeup.dspfparser.linesprocessor.LinesProcessor
import java.io.BufferedReader

/**
 * Entry point to request a display file parse.
 */
abstract class DisplayFileParser {
    companion object {
        /**
         * Parse a source display file given a buffered reader.
         * @param buffer
         * @return a [DSPF] instance representing the display file
         */
        fun parse(buffer: BufferedReader): DSPF {
            val processor = LinesProcessor(buffer)
            processor.createLines()
            val specifications = DSPFSpecifications.fromLines(processor.lines)
            return specifications
        }
    }
}
