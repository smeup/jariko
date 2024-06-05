package com.smeup.dspfparser.linesprocessor

import com.smeup.dspfparser.positionals.BooleanUnaryOperator
import kotlinx.serialization.Serializable

@Serializable
internal data class DSPFIndicator private constructor(
    val op: BooleanUnaryOperator = BooleanUnaryOperator.BLANK,
    val id: Int
) {
    companion object {
        fun fromString(text: String): DSPFIndicator {
            return DSPFIndicator(this.getOp(text), this.getId(text))
        }

        private fun getOp(text: String): BooleanUnaryOperator {
            return BooleanUnaryOperator.values().first { it.value == text[0] }
        }

        private fun getId(text: String): Int {
            val id = text.substring(1).toInt()
            if (id <= 0 || id > 99) throw Exception("Indicator value not allowed")
            return id
        }
    }
}
