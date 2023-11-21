package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.*

private fun Type.length(): Int {
    return if (this is NumberType) {
        this.numberOfDigits
    } else {
        this.size
    }
}

private fun Value.clearAsString(type: Type): StringValue {
    return if (type is NumberType) {
        StringValue("0".repeat(type.numberOfDigits), type.hasVariableSize())
    } else {
        StringValue(" ".repeat(type.size), type.hasVariableSize())
    }
}

fun movel(
    operationExtender: String?,
    target: AssignableExpression,
    value: Expression,
    interpreterCore: InterpreterCore
): Value {
    if (value !is FigurativeConstantRef) {
        val valueToMoveLength = value.type().length()
        val valueToApplyMoveLength = target.type().length()
        val valueToMove: StringValue = coerce(
            interpreterCore.eval(value), StringType(valueToMoveLength, value.type().hasVariableSize())
        ).asString()
        var valueToApplyMove: StringValue = coerce(
            interpreterCore.eval(target),
            StringType(valueToApplyMoveLength, target.type().hasVariableSize())
        ).asString()
        if (valueToMove.length() <= valueToApplyMove.length()) {
            var result: StringValue = valueToMove
            if (operationExtender != null) {
                // MOVEL(P): If factor 2 is shorter than the length of the result field,
                // a P specified in the operation extender position causes the result
                // clear valueToApplyMove
                valueToApplyMove = valueToApplyMove.clearAsString(target.type())
            }
            // overwrite valueToMove from left to right to valueToApplyMove
            result = StringValue(valueToMove.value + valueToApplyMove.value.substring(valueToMove.length()))
            // cast result to real value
            return interpreterCore.assign(target, coerce(result, target.type()))
        } else {
            // overwrite valueToMove to valueToApplyMove
            val result = StringValue(valueToMove.value.substring(0, valueToApplyMove.length()))
            // cast result to real value
            return interpreterCore.assign(target, coerce(result, target.type()))
        }
    } else {
        return interpreterCore.assign(target, interpreterCore.eval(value))
    }
}
