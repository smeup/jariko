package com.smeup.dspfparser.linesclassifier

import com.smeup.dspfparser.linesprocessor.DSPFLine
import com.smeup.dspfparser.positionals.FieldType
import kotlinx.serialization.Serializable

@Serializable
internal data class DSPFFieldSpecifications private constructor(
    override val name: String,
    override var value: DSPFValue? = null,
    override val isNumeric: Boolean,
    override val length: Int? = null,
    override val precision: Int? = null,
    override val type: DSPFFieldType,
    override val x: Int?,
    override val y: Int?,
    override var hasError: Boolean = false
) : DSPFField {

    companion object {
        fun fromLine(declaration: DSPFLine): DSPFFieldSpecifications {
            val isNumeric = declaration.decimalsPositions != null
            val value = if (declaration.isConstant()) {
                ConstantValue(declaration.keywords!!.getConstantValue())
            } else {
                null
            }

            return DSPFFieldSpecifications(
                name = declaration.fieldName,
                value = value,
                isNumeric = isNumeric,
                length = declaration.length,
                precision = declaration.decimalsPositions,
                type = this.getType(declaration),
                x = declaration.x,
                y = declaration.y
            )
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
