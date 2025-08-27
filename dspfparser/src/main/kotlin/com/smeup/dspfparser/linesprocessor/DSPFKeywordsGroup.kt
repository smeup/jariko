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

import kotlinx.serialization.Serializable

@Serializable
internal data class DSPFKeywordsGroup(
    val group: MutableList<DSPFKeyword> = mutableListOf(),
) {
    companion object {
        fun fromString(text: String): DSPFKeywordsGroup {
            val constants = this.getConstant(text)

            // no other keywords are allowed than constant ones
            // so if constant is not found simply return an empty group
            // E.G. keywords like DATSEP(''), SFL, DSPATR(HI), ... are not supported yet
            if (constants.isNotEmpty()) return DSPFKeywordsGroup(constants)
            return DSPFKeywordsGroup()
        }

        private fun getConstant(text: String): MutableList<DSPFKeyword> {
            val group: MutableList<DSPFKeyword> = mutableListOf()
            Regex("^'.*'\$").find(text)?.groupValues?.forEach {
                group.add(DSPFKeyword.fromString(it))
            }
            return group
        }
    }

    fun areConstant(): Boolean =
        this.group.size == 1 &&
            this.group[0].name != null &&
            this.group[0].parameters.isEmpty()

    fun getConstantValue(): String = this.group[0].name!!
}
