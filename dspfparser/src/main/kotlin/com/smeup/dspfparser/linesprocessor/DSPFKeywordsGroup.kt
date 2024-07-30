package com.smeup.dspfparser.linesprocessor

import kotlinx.serialization.Serializable

@Serializable
internal data class DSPFKeywordsGroup private constructor(val group: MutableList<DSPFKeyword> = mutableListOf()) {
    companion object {
        fun fromString(text: String): DSPFKeywordsGroup {
            val constants = this.getConstants(text)

            if (constants.isNotEmpty()) return DSPFKeywordsGroup(constants)
            return DSPFKeywordsGroup()
        }

        private fun getConstants(text: String): MutableList<DSPFKeyword> {
            val group: MutableList<DSPFKeyword> = mutableListOf()
            Regex("^'.*'\$").find(text)?.groupValues?.forEach {
                group.add(DSPFKeyword.fromString(it))
            }
            return group
        }
    }

    fun areConstantField(): Boolean {
        return this.group.size == 1
                && this.group[0].name != null
                && this.group[0].parameters.isEmpty()
    }

    fun getConstantFieldValue(): String {
        return this.group[0].name!!
    }
}
