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

import com.smeup.dspfparser.positionals.BooleanNAryOperator
import kotlinx.serialization.Serializable

@Serializable
internal data class DSPFCondition(
    val op: BooleanNAryOperator,
    val indicators: MutableList<DSPFIndicator> = mutableListOf(),
) {
    companion object {
        fun fromString(text: String): DSPFCondition = DSPFCondition(this.getOp(text), this.getIndicators(text))

        private fun getOp(text: String): BooleanNAryOperator = BooleanNAryOperator.entries.first { it.value == text[0] }

        private fun getIndicators(text: String): MutableList<DSPFIndicator> {
            val indicators: MutableList<DSPFIndicator> = mutableListOf()
            val first = text.substring(1, 4)
            val second = text.substring(4, 7)
            val third = text.substring(7, 10)
            if (first != " ".repeat(3)) indicators.add(DSPFIndicator.fromString(first))
            if (second != " ".repeat(3)) indicators.add(DSPFIndicator.fromString(second))
            if (third != " ".repeat(3)) indicators.add(DSPFIndicator.fromString(third))
            return indicators
        }
    }
}
