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

import com.smeup.dspfparser.utils.removeFirst
import com.smeup.dspfparser.utils.removeLast
import kotlinx.serialization.Serializable

@Serializable
internal data class DSPFParameters(
    val arguments: MutableList<String> = mutableListOf()
) {
    companion object {
        fun fromString(text: String): DSPFParameters {
            return DSPFParameters(this.getArguments(text))
        }

        private fun removeBrackets(text: String): String {
            return text.removeFirst('(').removeLast(')')
        }

        private fun removeSingleQuotes(text: String): String {
            return text.removeFirst('\'').removeLast('\'')
        }

        private fun getArguments(text: String): MutableList<String> {
            val arguments: MutableList<String> = mutableListOf()
            this.removeBrackets(text).split(' ').forEach {
                arguments.add(this.removeSingleQuotes(it))
            }
            return arguments
        }
    }
}
