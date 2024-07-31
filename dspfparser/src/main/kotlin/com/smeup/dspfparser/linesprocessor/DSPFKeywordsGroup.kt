package com.smeup.dspfparser.linesprocessor

import kotlinx.serialization.Serializable

@Serializable
internal data class DSPFKeywordsGroup private constructor(val group: MutableList<DSPFKeyword> = mutableListOf()) {
    companion object {
        fun fromString(text: String): DSPFKeywordsGroup {
            val constants = this.getConstant(text)

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

    fun areConstant(): Boolean {
        return this.group.size == 1 &&
                this.group[0].name != null &&
                this.group[0].parameters.isEmpty()
    }

    fun getConstantValue(): String {
        return this.group[0].name!!
    }
}
