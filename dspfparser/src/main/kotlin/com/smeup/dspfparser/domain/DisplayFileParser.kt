package com.smeup.dspfparser.domain

import com.smeup.dspfparser.linesclassifier.DSPFSpecifications
import com.smeup.dspfparser.linesprocessor.LinesProcessor
import java.io.BufferedReader
import java.io.InputStream

/**
 * Entry point to request a display file parse.
 */
abstract class DisplayFileParser {
    companion object {
        /**
         * Parse a source display file given an input stream.
         * @param stream
         * @return a [DSPF] instance representing the display file
         */
        fun parse(stream: InputStream): DSPF {
            val buffer = stream.bufferedReader()
            val processor = LinesProcessor(buffer)
            processor.createLines()
            val specifications = DSPFSpecifications.fromLines(processor.lines)
            return specifications
        }

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
