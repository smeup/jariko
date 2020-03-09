package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.Expression
import com.smeup.rpgparser.utils.ComparisonOperator
import java.nio.charset.Charset

enum class Comparison {
    SMALLER,
    EQUAL,
    GREATER
}

fun isEqualOrSmaller(value1: Value, value2: Value, charset: Charset): Boolean {
    val cmp = compare(value1, value2, charset)
    return cmp == Comparison.SMALLER || cmp == Comparison.EQUAL
}

fun isEqualOrGreater(value1: Value, value2: Value, charset: Charset): Boolean {
    val cmp = compare(value1, value2, charset)
    return cmp == Comparison.GREATER || cmp == Comparison.EQUAL
}

fun isGreaterThan(value1: Value, value2: Value, charset: Charset): Boolean {
    val cmp = compare(value1, value2, charset)
    return cmp == Comparison.GREATER
}

fun isSmallerThan(value1: Value, value2: Value, charset: Charset): Boolean {
    val cmp = compare(value1, value2, charset)
    return cmp == Comparison.SMALLER
}

fun compare(value1: Value, value2: Value, charset: Charset): Comparison {
    return when {
        value1 is IntValue && value2 is IntValue -> when {
            value1.value == value2.value -> Comparison.EQUAL
            value1.value < value2.value -> Comparison.SMALLER
            else -> Comparison.GREATER
        }
        value1 is IntValue && value2 is StringValue -> throw RuntimeException("Cannot compare int and string")
        value2 is HiValValue -> Comparison.SMALLER
        value1 is NumberValue && value2 is NumberValue -> when {
            value1.bigDecimal == value2.bigDecimal -> Comparison.EQUAL
            value1.bigDecimal < value2.bigDecimal -> Comparison.SMALLER
            else -> Comparison.GREATER
        }
        value1 is StringValue && value2 is StringValue -> {
            stringComparison(value1, value2, charset)
        }
        else -> TODO("Unable to compare: value 1 is $value1, Value 2 is $value2")
    }
}

fun stringComparison(value1: Value, value2: Value, charset: Charset): Comparison {
    if (value1 == value2) {
        return Comparison.EQUAL
    }
    if (value1.compare(value2, charset) < 0) {
        return Comparison.SMALLER
    }
    return Comparison.GREATER
}

fun ComparisonOperator?.isVerifiedFor(factor1: Expression, factor2: Expression, interpreter: InterpreterCoreHelper, charset: Charset): Boolean {
    if (this == null) return true
    interpreter.compareExpressions(factor1, factor2, charset)
    TODO()
}

fun InterpreterCoreHelper.compareExpressions(left: Expression, right: Expression, charset: Charset): Comparison {
    val factor1 = interpret(left)
    if (factor1 !is NumberValue && factor1 !is StringValue) {
        throw UnsupportedOperationException("Unable to compare: Factor1 datatype ($factor1) is not yet supported.")
    }

    val factor2 = interpret(right)
    if (factor2 !is NumberValue && factor2 !is StringValue) {
        throw UnsupportedOperationException("Unable to compare: Factor2 datatype ($factor2) is not yet supported.")
    }

    return compare(factor1, factor2, charset)
}
