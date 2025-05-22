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
internal data class DSPFKeyword(
    val name: String? = null,
    val parameters: MutableList<DSPFParameters> = mutableListOf()
) {
    companion object {
        fun fromString(text: String): DSPFKeyword {
            val message = this.getConstantMessage(text)

            if (message == null) {
                val name = this.getName(text)
                val parameters = this.getParameters(text)
                return DSPFKeyword(name, parameters)
            }

            return DSPFKeyword(message)
        }

        private fun getConstantMessage(text: String): String? {
            return Regex("^'.*'\$").find(text.trim())?.value?.removeFirst('\'')?.removeLast('\'')
        }

        private fun getName(text: String): String? {
            return Regex("^[a-zA-Z]*").find(text)?.value
        }

        private fun getParameters(text: String): MutableList<DSPFParameters> {
            val parameters =
                Regex("\\([^(^)]*\\)").find(text)?.groupValues?.map {
                    it.replace("(", "")
                    it.replace(")", "")
                    DSPFParameters.fromString(it)
                }

            if (parameters != null) return parameters.toMutableList()
            return mutableListOf()
        }
    }
}
