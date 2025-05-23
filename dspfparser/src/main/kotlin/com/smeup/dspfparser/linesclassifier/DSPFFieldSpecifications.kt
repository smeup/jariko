/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smeup.dspfparser.linesclassifier

import com.smeup.dspfparser.linesprocessor.DSPFLine
import com.smeup.dspfparser.linesprocessor.LineType
import com.smeup.dspfparser.positionals.FieldType
import kotlinx.serialization.Serializable

@Serializable
internal data class DSPFFieldSpecifications(
    override val x: Int?,
    override val y: Int?
) : DSPFField {

    companion object {
        fun fromLine(declaration: DSPFLine): DSPFField {
            if (declaration.type == LineType.CONSTANT) {
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
