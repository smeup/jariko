package com.smeup.dspfparser.linesprocessor

import com.smeup.dspfparser.positionals.BooleanNAryOperator
import kotlinx.serialization.Serializable

@Serializable
internal data class DSPFCondition private constructor(
    val op: BooleanNAryOperator,
    val indicators: MutableList<DSPFIndicator> = mutableListOf()
) {
    companion object {
        fun fromString(text: String): DSPFCondition {
            return DSPFCondition(this.getOp(text), this.getIndicators(text))
        }

        private fun getOp(text: String): BooleanNAryOperator {
            return BooleanNAryOperator.values().first { it.value == text[0] }
        }

        private fun getIndicators(text: String): MutableList<DSPFIndicator> {
            val indicators: MutableList<DSPFIndicator> = mutableListOf()
            val first = text.substring(1, 4)
            val second = text.substring(4, 7)
            val third = text.substring(7, 10)
            if (first != " ".repeat(3)) indicators.add(DSPFIndicator.fromString(first))
            if (second != " ".repeat(3)) indicators.add(DSPFIndicator.fromString(second))
            if (third != " ".repeat(3)) indicators.add(DSPFIndicator.fromString(third))
            return indicators
        }
    }
}
