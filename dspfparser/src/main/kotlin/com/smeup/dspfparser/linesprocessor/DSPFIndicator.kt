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

import com.smeup.dspfparser.positionals.BooleanUnaryOperator
import kotlinx.serialization.Serializable

@Serializable
internal data class DSPFIndicator(
    val op: BooleanUnaryOperator = BooleanUnaryOperator.BLANK,
    val id: Int
) {
    companion object {
        fun fromString(text: String): DSPFIndicator {
            return DSPFIndicator(this.getOp(text), this.getId(text))
        }

        private fun getOp(text: String): BooleanUnaryOperator {
            return BooleanUnaryOperator.values().first { it.value == text[0] }
        }

        private fun getId(text: String): Int {
            val id = text.substring(1).toInt()
            if (id <= 0 || id > 99) throw Exception("Indicator value not allowed")
            return id
        }
    }
}
