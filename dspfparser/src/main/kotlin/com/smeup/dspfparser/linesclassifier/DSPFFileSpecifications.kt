package com.smeup.dspfparser.linesclassifier

import com.smeup.dspfparser.linesprocessor.DSPFLine
import kotlinx.serialization.Serializable

@Serializable
internal data class DSPFFileSpecifications private constructor(
    val related: MutableList<DSPFLine> = mutableListOf(),
) {
    companion object {
        fun create(): DSPFFileSpecifications {
            return DSPFFileSpecifications()
        }
    }
}
