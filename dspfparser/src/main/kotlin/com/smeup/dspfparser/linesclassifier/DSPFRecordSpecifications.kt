package com.smeup.dspfparser.linesclassifier

import com.smeup.dspfparser.domain.DSPFRecord
import com.smeup.dspfparser.linesprocessor.DSPFLine
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
internal data class DSPFRecordSpecifications private constructor(
    override val name: String,
    @Transient val declaration: DSPFLine = DSPFLine.fake()
) : DSPFRecord {
    override val fields: MutableList<DSPFFieldSpecifications> = mutableListOf()
    val related: MutableList<DSPFLine> = mutableListOf()

    companion object {
        fun fromLine(declaration: DSPFLine): DSPFRecordSpecifications {
            return DSPFRecordSpecifications(
                name = declaration.fieldName,
                declaration = declaration
            )
        }
    }
}
