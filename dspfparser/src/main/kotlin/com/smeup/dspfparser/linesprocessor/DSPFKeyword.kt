package com.smeup.dspfparser.linesprocessor

import com.smeup.dspfparser.utils.removeFirst
import com.smeup.dspfparser.utils.removeLast
import kotlinx.serialization.Serializable

@Serializable
internal data class DSPFKeyword private constructor(
    val name: String? = null,
    val parameters: MutableList<DSPFParameters> = mutableListOf(),
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
            return Regex("^'[^']*'\$").find(text.trim())?.value?.removeFirst('\'')?.removeLast('\'')
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
