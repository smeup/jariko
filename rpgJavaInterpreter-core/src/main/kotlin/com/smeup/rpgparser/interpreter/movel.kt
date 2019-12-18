package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.AllExpr
import com.smeup.rpgparser.parsing.ast.AssignableExpression
import com.smeup.rpgparser.parsing.ast.DataRefExpr
import com.smeup.rpgparser.parsing.ast.Expression
import java.math.BigDecimal

private fun assignStringToString(operationExtender: String?, target: DataRefExpr, factor2: Expression, interpreterCoreHelper: InterpreterCoreHelper): Value {
    var newValue = interpreterCoreHelper.interpret(factor2)
    if (factor2 is AllExpr) {
        return interpreterCoreHelper.assign(target, newValue)
    }
    if (newValue is NumberValue) {
        newValue = newValue.numberToString()
    }
    if (factor2.type().size > target.size()) {
        newValue = newValue.takeFirst(target.size())
    } else if (factor2.type().size < target.size()) {
        val append = if (operationExtender == null) {
            val value = interpreterCoreHelper.get(target.variable.referred!!)
            require(value is StringValue)
            StringValue.padded(value.value, target.size()).takeLast(target.size() - factor2.type().size)
        } else {
            StringValue.blank(target.size() - factor2.type().size)
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

fun movel(operationExtender: String?, target: AssignableExpression, value: Expression, interpreterCoreHelper: InterpreterCoreHelper): Value {
    require(target is DataRefExpr)
    val valueType = value.type()
    require(target.type() is StringType && (valueType is StringType || valueType is NumberType || valueType is FigurativeType)) {
        "Cannot assign ${valueType::class.qualifiedName} to ${target.type()::class.qualifiedName}"
    }
    return assignStringToString(operationExtender, target, value, interpreterCoreHelper)
}
