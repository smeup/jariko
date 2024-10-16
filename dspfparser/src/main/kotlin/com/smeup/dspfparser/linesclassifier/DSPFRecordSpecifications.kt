package com.smeup.dspfparser.linesclassifier

import com.smeup.dspfparser.linesprocessor.DSPFLine
import kotlinx.serialization.Serializable

@Serializable
internal data class DSPFRecordSpecifications private constructor(
    override val name: String
) : DSPFRecord {
    override val mutables: MutableList<MutableField> = mutableListOf()
    override val constants: MutableList<ConstantField> = mutableListOf()

    companion object {
        fun fromLine(declaration: DSPFLine): DSPFRecordSpecifications {
            return DSPFRecordSpecifications(
                name = declaration.fieldName
            )
        }
    }
}
