package com.smeup.dspfparser.linesprocessor

import com.smeup.dspfparser.positionals.CONDITION_LENGTH
import kotlinx.serialization.Serializable

@Serializable
internal data class DSPFConditionsGroup private constructor(
    val group: MutableList<DSPFCondition> = mutableListOf()
) {
    companion object {
        fun fromString(text: String): DSPFConditionsGroup {
            return DSPFConditionsGroup(this.getConditions(text))
        }

        private fun getConditions(text: String): MutableList<DSPFCondition> {
            val group: MutableList<DSPFCondition> = mutableListOf()
            var i = 0
            while (true) {
                try {
                    group.add(DSPFCondition.fromString(text.substring(i, i + CONDITION_LENGTH)))
                } catch (exception: Exception) {
                    break
                }
                i += CONDITION_LENGTH
            }
            return group
        }
    }
}
