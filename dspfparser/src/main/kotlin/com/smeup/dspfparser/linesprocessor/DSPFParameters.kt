package com.smeup.dspfparser.linesprocessor

import com.smeup.dspfparser.utils.removeFirst
import com.smeup.dspfparser.utils.removeLast
import kotlinx.serialization.Serializable

@Serializable
internal data class DSPFParameters private constructor(
    val arguments: MutableList<String> = mutableListOf(),
) {
    companion object {
        fun fromString(text: String): DSPFParameters {
            return DSPFParameters(this.getArguments(text))
        }

        private fun removeBrackets(text: String): String {
            return text.removeFirst('(').removeLast(')')
        }

        private fun tryRemoveSurroundingSingleQuotes(text: String): String {
            return text.removeFirst('\'').removeLast('\'')
        }

        private fun getArguments(text: String): MutableList<String> {
            val arguments: MutableList<String> = mutableListOf()
            this.removeBrackets(text).split(' ')?.forEach {
                arguments.add(this.tryRemoveSurroundingSingleQuotes(it))
            }
            return arguments
        }
    }
}
