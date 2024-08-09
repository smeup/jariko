package com.smeup.dspfparser.linesprocessor

import kotlinx.serialization.Serializable

@Serializable
internal data class DSPFKeywordsGroup private constructor(val group: MutableList<DSPFKeyword> = mutableListOf()) {
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

    fun areConstant(): Boolean {
        return this.group.size == 1 &&
                this.group[0].name != null &&
                this.group[0].parameters.isEmpty()
    }

    fun getConstantValue(): String {
        return this.group[0].name!!
    }
}
