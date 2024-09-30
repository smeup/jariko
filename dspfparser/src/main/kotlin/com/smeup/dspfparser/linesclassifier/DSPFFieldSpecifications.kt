package com.smeup.dspfparser.linesclassifier

import com.smeup.dspfparser.linesprocessor.DSPFLine
import com.smeup.dspfparser.positionals.FieldType
import kotlinx.serialization.Serializable

@Serializable
internal data class DSPFFieldSpecifications private constructor(
    override val x: Int?,
    override val y: Int?
) : DSPFField {

    companion object {
        fun fromLine(declaration: DSPFLine): DSPFField {
            if (declaration.isConstant()) {
                return ConstantField(
                    value = ConstantValue(declaration.keywords!!.getConstantValue()),
                    x = declaration.x,
                    y = declaration.y
                )
            } else {
                val isNumeric = declaration.decimalsPositions != null

                return MutableField(
                    name = declaration.fieldName,
                    value = null,
                    isNumeric = isNumeric,
                    length = declaration.length,
                    precision = declaration.decimalsPositions,
                    type = this.getType(declaration),
                    x = declaration.x,
                    y = declaration.y
                )
            }
        }

        private fun getType(declaration: DSPFLine): DSPFFieldType {
            return when (declaration.fieldType) {
                FieldType.I -> DSPFFieldType.INPUT
                FieldType.O -> DSPFFieldType.OUTPUT
                FieldType.B -> DSPFFieldType.INPUT_OUTPUT
                FieldType.H -> DSPFFieldType.HIDDEN
                FieldType.M -> DSPFFieldType.MESSAGE
                FieldType.P -> DSPFFieldType.PROGRAM_TO_SYSTEM
                FieldType.BLANK -> DSPFFieldType.DEFAULT
            }
        }
    }
}
