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

import com.smeup.dspfparser.positionals.LAST_COLUMN
import com.smeup.dspfparser.positionals.LineInfo
import com.smeup.dspfparser.utils.removeNewLineAndExtend

/*
    substring trims here are allowed only in some cases for performance reasons. Do not trim randomly!
    trimming blank spaces to "" can cause errors when parsing numbers
    further trims are performed at the next step

    for comments and keywords:
    trimming is needed to prevent holding in memory as much as 5000 blank characters
    padding is needed because a "" string will throw errors, and that
    occurs when trimming a completely blank line (no keywords)
*/
internal data class DSPFLineSubstrings(
    val count: Int,
    val sequenceNumber: String,
    val a: String,
    val comment: String,
    var condition: String,
    val reserved: String,
    val typeOfName: String,
    val fieldName: String,
    val reference: String,
    val length: String,
    val dataTypeKeyboardShift: String,
    val decimalsPositions: String,
    val fieldType: String,
    val y: String,
    val x: String,
    val keywords: String,
    val line: String,
    val continuationChar: Char? = null,
) {
    companion object {
        fun from(
            count: Int,
            line: String,
        ): DSPFLineSubstrings {
            // simulates multiline by extending writable range
            val extendedLine = line.removeNewLineAndExtend(' ', LAST_COLUMN)

            return DSPFLineSubstrings(
                count,
                this.getSequenceNumber(extendedLine),
                this.getA(extendedLine),
                this.getComment(extendedLine),
                this.getCondition(extendedLine),
                this.getReserved(extendedLine),
                this.getTypeOfName(extendedLine),
                this.getFieldName(extendedLine),
                this.getReference(extendedLine),
                this.getLength(extendedLine),
                this.getDataTypeKeyboardShift(extendedLine),
                this.getDecimalsPositions(extendedLine),
                this.getFieldType(extendedLine),
                this.getY(extendedLine),
                this.getX(extendedLine),
                this.getKeywords(extendedLine),
                line,
                this.getContinuationChar(extendedLine),
            )
        }

        private fun getContinuationChar(line: String): Char? {
            if (line[7] != '*' && line.trim().last() == '+') return '+'
            if (line[7] != '*' && line.trim().last() == '-') return '-'
            return null
        }

        private fun getSequenceNumber(line: String): String = line.substring(LineInfo.SEQUENCE_NUMBER.from, LineInfo.SEQUENCE_NUMBER.to)

        private fun getA(line: String): String = line.substring(LineInfo.A.from, LineInfo.A.to)

        private fun getComment(line: String): String = line.substring(LineInfo.COMMENT.from, LineInfo.COMMENT.to).trim()

        private fun getCondition(line: String): String = line.substring(LineInfo.CONDITION.from, LineInfo.CONDITION.to)

        private fun getReserved(line: String): String = line.substring(LineInfo.RESERVED.from, LineInfo.RESERVED.to)

        private fun getTypeOfName(line: String): String = line.substring(LineInfo.TYPE_OF_NAME.from, LineInfo.TYPE_OF_NAME.to)

        private fun getFieldName(line: String): String = line.substring(LineInfo.FIELD_NAME.from, LineInfo.FIELD_NAME.to)

        private fun getReference(line: String): String = line.substring(LineInfo.REFERENCE.from, LineInfo.REFERENCE.to)

        private fun getLength(line: String): String = line.substring(LineInfo.LENGTH.from, LineInfo.LENGTH.to)

        private fun getDataTypeKeyboardShift(line: String): String = line.substring(LineInfo.DTKBS.from, LineInfo.DTKBS.to)

        private fun getDecimalsPositions(line: String): String =
            line.substring(LineInfo.DECIMALS_POSITIONS.from, LineInfo.DECIMALS_POSITIONS.to)

        private fun getFieldType(line: String): String = line.substring(LineInfo.FIELD_TYPE.from, LineInfo.FIELD_TYPE.to)

        private fun getY(line: String): String = line.substring(LineInfo.Y.from, LineInfo.Y.to)

        private fun getX(line: String): String = line.substring(LineInfo.X.from, LineInfo.X.to)

        private fun getKeywords(line: String): String =
            line
                .substring(LineInfo.KEYWORDS.from, LineInfo.KEYWORDS.to)
                .trim()
                .padEnd(1)
                .padStart(1)
    }

    fun isComment(): Boolean = this.comment[0] == '*'

    fun doesContinue(): Boolean = this.continuationChar != null

    fun continuesAtColumn45(): Boolean = this.continuationChar == '-'

    fun continuesAtAnyColumn(): Boolean = this.continuationChar == '+'
}
