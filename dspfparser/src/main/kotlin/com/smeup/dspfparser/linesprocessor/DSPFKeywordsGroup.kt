package com.smeup.dspfparser.linesprocessor

import kotlinx.serialization.Serializable

@Serializable
internal data class DSPFKeywordsGroup private constructor(val group: MutableList<DSPFKeyword> = mutableListOf()) {
    companion object {
        fun fromString(text: String): DSPFKeywordsGroup {
            val constants = this.getConstants(text)
            val parametrized = this.getParametrized(text)

            if (parametrized.isNotEmpty()) return DSPFKeywordsGroup(parametrized)
            return DSPFKeywordsGroup(constants)
        }

        private fun getConstants(text: String): MutableList<DSPFKeyword> {
            val group: MutableList<DSPFKeyword> = mutableListOf()
            Regex("^'.*'\$").find(text)?.groupValues?.forEach {
                group.add(DSPFKeyword.fromString(it))
            }
            return group
        }

        private fun getParametrized(text: String): MutableList<DSPFKeyword> {
            val group: MutableList<DSPFKeyword> = mutableListOf()
            Regex("[a-zA-Z]{1,}\\([^(^)]*\\)").find(text)?.groupValues?.forEach {
                group.add(DSPFKeyword.fromString(it))
            }
            return group
        }
    }

    fun hasConstantField(): Boolean {
        return this.group.size == 1
                && this.group[0].name != null
                && this.group[0].parameters.isEmpty()
    }
}
