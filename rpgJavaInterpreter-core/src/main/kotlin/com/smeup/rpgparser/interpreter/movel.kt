package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.AssignableExpression
import com.smeup.rpgparser.parsing.ast.DataRefExpr
import com.smeup.rpgparser.parsing.ast.Expression
import java.math.BigDecimal

private fun assignStringToString(operationExtender: String?, target: DataRefExpr, factor2: Expression, interpreterCoreHelper: InterpreterCoreHelper): Value {
    var newValue = interpreterCoreHelper.interpret(factor2)
    if (factor2.type().size > target.size()) {
        newValue = newValue.takeFirst(target.size().toInt())
    } else if (factor2.type().size < target.size()) {
        val append = if (operationExtender == null) {
            interpreterCoreHelper.get(target.variable.referred!!).takeLast(target.size().toInt() - factor2.type().size.toInt())
        } else {
            StringValue.blank(target.size().toInt() - factor2.type().size.toInt())
        }
        newValue = newValue.concatenate(append)
    }
    return interpreterCoreHelper.assign(target, newValue)
}

// for future use
// map conversion 1 -> J, 2 -> K, ..., R -> 9
private fun Char.numberToLetter(): CharSequence {
    val offset = 'J'.toInt() - '1'.toInt()
    return (this.toInt() + offset).toChar().toString()
}

private fun NumberValue.numberToString(): Value {
    val value = this.bigDecimal.abs().toString().replaceFirst(".", "")
    require(this.bigDecimal >= BigDecimal.ZERO) {
        "negative factor 2 not allowed"
    }
    return StringValue(value)
}

private fun assignNumberToString(target: DataRefExpr, factor2: Expression, interpreterCoreHelper: InterpreterCoreHelper): Value {
    var newValue = (interpreterCoreHelper.interpret(factor2) as NumberValue).numberToString()
    if (factor2.type().size > target.size()) {
        newValue = newValue.takeFirst(target.size().toInt())
    } else if (factor2.type().size < target.size()) {
        newValue = newValue.concatenate(interpreterCoreHelper.get(target.variable.referred!!).takeLast(target.size().toInt() - factor2.type().size.toInt()))
    }
    return interpreterCoreHelper.assign(target, newValue)
}

fun movel(operationExtender: String?, target: AssignableExpression, value: Expression, interpreterCoreHelper: InterpreterCoreHelper): Value {
    require(target is DataRefExpr)
    return if (value.type() is StringType && target.type() is StringType) {
        assignStringToString(operationExtender, target, value, interpreterCoreHelper)
    } else if (value.type() is NumberType && target.type() is StringType) {
        assignNumberToString(target, value, interpreterCoreHelper)
    } else {
        throw IllegalArgumentException(
                "Cannot assign ${value.type()::class.qualifiedName} to ${target.type()::class.qualifiedName}")
    }
}