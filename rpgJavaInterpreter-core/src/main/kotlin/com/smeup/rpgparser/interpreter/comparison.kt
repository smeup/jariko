package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.Expression
import com.smeup.rpgparser.utils.ComparisonOperator
import java.nio.charset.Charset

typealias Comparison = Int

const val EQUAL = 0
const val SMALLER = -1
const val GREATER = 1

fun isEqualOrSmaller(value1: Value, value2: Value, charset: Charset): Boolean {
    val cmp = compare(value1, value2, charset)
    return cmp == SMALLER || cmp == EQUAL
}

fun isEqualOrGreater(value1: Value, value2: Value, charset: Charset): Boolean {
    val cmp = compare(value1, value2, charset)
    return cmp == GREATER || cmp == EQUAL
}

fun isGreaterThan(value1: Value, value2: Value, charset: Charset): Boolean {
    val cmp = compare(value1, value2, charset)
    return cmp == GREATER
}

fun isSmallerThan(value1: Value, value2: Value, charset: Charset): Boolean {
    val cmp = compare(value1, value2, charset)
    return cmp == SMALLER
}

fun compare(value1: Value, value2: Value, charset: Charset): Comparison =
    when {
        value1 is StringValue && value2 is StringValue -> stringComparison(value1, value2, charset)
        else -> value1.compareTo(value2)
    }

fun stringComparison(value1: StringValue, value2: StringValue, charset: Charset): Comparison {
    if (value1.value == value2.value) {
        return EQUAL
    }
    if (value1.compare(value2, charset) < 0) {
        return SMALLER
    }
    return GREATER
}

data class ComparisonResult(val isVerified: Boolean, val comparison: Comparison)

fun ComparisonOperator?.verify(factor1: Expression, factor2: Expression, interpreter: InterpreterCore, charset: Charset): ComparisonResult =
    when (val comparison = interpreter.compareExpressions(factor1, factor2, charset)) {
        EQUAL -> ComparisonResult(this == null || this == ComparisonOperator.EQ || this == ComparisonOperator.GE || this == ComparisonOperator.LE, comparison)
        SMALLER -> ComparisonResult(this == null || this == ComparisonOperator.LE || this == ComparisonOperator.LT || this == ComparisonOperator.NE, comparison)
        else -> ComparisonResult(this == null || this == ComparisonOperator.GE || this == ComparisonOperator.GT || this == ComparisonOperator.NE, comparison)
    }

fun InterpreterCore.compareExpressions(left: Expression, right: Expression, charset: Charset): Comparison =
    compare(eval(left), eval(right), charset)
