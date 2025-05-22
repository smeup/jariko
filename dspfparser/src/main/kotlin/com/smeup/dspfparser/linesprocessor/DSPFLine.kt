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

package com.smeup.dspfparser.linesprocessor

import com.smeup.dspfparser.positionals.DTKBS
import com.smeup.dspfparser.positionals.FieldType
import com.smeup.dspfparser.positionals.Reference
import com.smeup.dspfparser.positionals.Reserved
import com.smeup.dspfparser.positionals.TypeOfName
import kotlinx.serialization.Serializable

enum class LineType {
    NULL,
    OTHER,
    HELP,
    RECORD,
    FIELD,
    CONSTANT
}

@Serializable
internal data class DSPFLine(
    val count: Int,
    val sequenceNumber: String,
    val a: Char,
    val reserved: Reserved,
    val typeOfName: TypeOfName,
    val fieldName: String,
    val reference: Reference,
    val length: Int? = null,
    val dataTypeKeyboardShift: DTKBS,
    val decimalsPositions: Int? = null,
    val fieldType: FieldType,
    val y: Int? = null,
    val x: Int? = null,
    val keywords: DSPFKeywordsGroup? = null,
    var type: LineType = LineType.NULL
) {
    companion object {
        fun from(lineSubstrings: DSPFLineSubstrings): DSPFLine {
            val line = DSPFLine(
                this.getCount(lineSubstrings),
                this.getSequenceNumber(lineSubstrings),
                this.getA(lineSubstrings),
                this.getReserved(lineSubstrings),
                this.getTypeOfName(lineSubstrings),
                this.getFieldName(lineSubstrings),
                this.getReference(lineSubstrings),
                this.getLength(lineSubstrings),
                this.getDataTypeKeyboardShift(lineSubstrings),
                this.getDecimalsPositions(lineSubstrings),
                this.getFieldType(lineSubstrings),
                this.getY(lineSubstrings),
                this.getX(lineSubstrings),
                this.getKeywords(lineSubstrings)
            )
            return line
        }

        private fun getCount(lineSubstrings: DSPFLineSubstrings): Int {
            return lineSubstrings.count
        }

        private fun getSequenceNumber(lineSubstrings: DSPFLineSubstrings): String {
            return lineSubstrings.sequenceNumber.trim()
        }

        private fun getA(lineSubstrings: DSPFLineSubstrings): Char {
            return lineSubstrings.a[0]
        }

        // Could-Have: getCondition

        private fun getReserved(lineSubstrings: DSPFLineSubstrings): Reserved {
            return Reserved.values().first { it.value == lineSubstrings.reserved[0] }
        }

        private fun getTypeOfName(lineSubstrings: DSPFLineSubstrings): TypeOfName {
            return TypeOfName.values().first { it.value == lineSubstrings.typeOfName[0] }
        }

        private fun getFieldName(lineSubstrings: DSPFLineSubstrings): String {
            return lineSubstrings.fieldName.trim()
        }

        private fun getReference(lineSubstrings: DSPFLineSubstrings): Reference {
            return Reference.values().first { it.value == lineSubstrings.reference[0] }
        }

        private fun getLength(lineSubstrings: DSPFLineSubstrings): Int? {
            if (lineSubstrings.length.trim().isBlank()) return null
            return lineSubstrings.length.trim().toInt()
        }

        private fun getDataTypeKeyboardShift(lineSubstrings: DSPFLineSubstrings): DTKBS {
            return DTKBS.values().first { it.value == lineSubstrings.dataTypeKeyboardShift[0] }
        }

        private fun getDecimalsPositions(lineSubstrings: DSPFLineSubstrings): Int? {
            if (lineSubstrings.decimalsPositions.trim().isBlank()) return null
            return lineSubstrings.decimalsPositions.trim().toInt()
        }

        private fun getFieldType(lineSubstrings: DSPFLineSubstrings): FieldType {
            return FieldType.values().first { it.value == lineSubstrings.fieldType[0] }
        }

        private fun getY(lineSubstrings: DSPFLineSubstrings): Int? {
            if (lineSubstrings.y.trim().isBlank()) return null
            return lineSubstrings.y.trim().toInt()
        }

        private fun getX(lineSubstrings: DSPFLineSubstrings): Int? {
            if (lineSubstrings.x.trim().isBlank()) return null
            return lineSubstrings.x.trim().toInt()
        }

        private fun getKeywords(lineSubstrings: DSPFLineSubstrings): DSPFKeywordsGroup? {
            if (lineSubstrings.keywords.isBlank()) return null
            return DSPFKeywordsGroup.fromString(lineSubstrings.keywords)
        }
    }

    init {
        when {
            this.typeOfName == TypeOfName.H && this.fieldName.isBlank() -> this.type = LineType.HELP
            this.typeOfName == TypeOfName.R && this.fieldName.isNotBlank() -> this.type = LineType.RECORD
            this.typeOfName == TypeOfName.BLANK && this.fieldName.isNotBlank() -> this.type = LineType.FIELD
            this.typeOfName == TypeOfName.BLANK &&
                this.fieldName.isBlank() &&
                this.reference == Reference.BLANK &&
                this.length == null &&
                this.dataTypeKeyboardShift == DTKBS.BLANK &&
                this.decimalsPositions == null &&
                this.fieldType == FieldType.BLANK &&
                this.y != null &&
                this.x != null &&
                this.keywords?.areConstant() ?: false
                    -> this.type = LineType.CONSTANT
            else -> this.type = LineType.OTHER
        }
    }
}
