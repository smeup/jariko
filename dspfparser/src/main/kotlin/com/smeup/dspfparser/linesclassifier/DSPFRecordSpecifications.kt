package com.smeup.dspfparser.linesclassifier

import com.smeup.dspfparser.domain.DSPFRecord
import com.smeup.dspfparser.linesprocessor.DSPFLine
import kotlinx.serialization.Serializable

@Serializable
internal data class DSPFRecordSpecifications private constructor(
    override val name: String,
    val declaration: DSPFLine,
) : DSPFRecord {
    override val fields: MutableList<DSPFFieldSpecifications> = mutableListOf()
    val related: MutableList<DSPFLine> = mutableListOf()

    companion object {
        fun fromLine(declaration: DSPFLine): DSPFRecordSpecifications {
            return DSPFRecordSpecifications(
                name = declaration.fieldName,
                declaration = declaration,
            )
        }
    }
}
