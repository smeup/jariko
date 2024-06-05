package com.smeup.dspfparser.linesprocessor

import kotlinx.serialization.Serializable

@Serializable
internal data class DSPFKeywordsGroup private constructor(val group: MutableList<DSPFKeyword> = mutableListOf()) {
    companion object {
        fun fromString(text: String): DSPFKeywordsGroup {
            return DSPFKeywordsGroup(this.getParametrized(text))
        }

        private fun getParametrized(text: String): MutableList<DSPFKeyword> {
            val group: MutableList<DSPFKeyword> = mutableListOf()
            Regex("[a-zA-Z]{1,}\\([^(^)]*\\)").find(text)?.groupValues?.forEach {
                group.add(DSPFKeyword.fromString(it))
            }
            return group
        }
    }
}
