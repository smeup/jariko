package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.*
import java.math.BigDecimal

private fun clear(type: Type): String {
    return if (type is NumberType) {
        "0".repeat(type.numberOfDigits)
    } else {
        " ".repeat(type.size)
    }
}

fun movel(
    operationExtender: String?,
    target: AssignableExpression,
    value: Expression,
    interpreterCore: InterpreterCore
): Value {
    if (value !is FigurativeConstantRef) {
        if (value.type() is ArrayType) {
            throw UnsupportedOperationException("Cannot set an array as factor 2 in MOVEL/MOVEL(P) statement")
        }
        val valueToMove: String = valueToString(interpreterCore.eval(value), value.type())
        if (target.type() is ArrayType) {
            // for each element of array apply move
            val arrayValue: ConcreteArrayValue = interpreterCore.eval(target) as ConcreteArrayValue
            val valueToApplyMoveElementType: Type = (target.type() as ArrayType).element
            arrayValue.elements.forEachIndexed { index, el ->
                arrayValue.setElement(
                    index + 1, stringToValue(
                        movel(
                            valueToMove,
                            valueToString(el, valueToApplyMoveElementType),
                            valueToApplyMoveElementType,
                            operationExtender != null
                        ),
                        valueToApplyMoveElementType
                    )
                )
            }
            return interpreterCore.assign(target, arrayValue)
        } else {
            val valueToApplyMove: String = valueToString(interpreterCore.eval(target), target.type())
            return interpreterCore.assign(
                target,
                stringToValue(
                    movel(valueToMove, valueToApplyMove, target.type(), operationExtender != null),
                    target.type()
                )
            )
        }
    } else {
        return interpreterCore.assign(target, interpreterCore.eval(value))
    }
}

fun move(
    operationExtender: String?,
    target: AssignableExpression,
    value: Expression,
    interpreterCore: InterpreterCore
): Value {
    if (value !is FigurativeConstantRef) {
        if (value.type() is ArrayType) {
            throw UnsupportedOperationException("Cannot set an array as factor 2 in MOVE/MOVE(P) statement")
        }
        val valueToMove: String = valueToString(interpreterCore.eval(value), value.type())
        if (target.type() is ArrayType) {
            // for each element of array apply move
            val arrayValue: ConcreteArrayValue = interpreterCore.eval(target) as ConcreteArrayValue
            val valueToApplyMoveElementType: Type = (target.type() as ArrayType).element
            arrayValue.elements.forEachIndexed { index, el ->
                arrayValue.setElement(
                    index + 1, stringToValue(
                        movel(
                            valueToMove,
                            valueToString(el, valueToApplyMoveElementType),
                            valueToApplyMoveElementType,
                            operationExtender != null
                        ),
                        valueToApplyMoveElementType
                    )
                )
            }
            return interpreterCore.assign(target, arrayValue)
        } else {
            val valueToApplyMove: String = valueToString(interpreterCore.eval(target), target.type())
            return interpreterCore.assign(
                target,
                stringToValue(
                    move(valueToMove, valueToApplyMove, target.type(), operationExtender != null),
                    target.type()
                )
            )
        }
    } else {
        return interpreterCore.assign(target, interpreterCore.eval(value))
    }
}

private fun movel(
    valueToMove: String,
    valueToApplyMove: String,
    valueToApplyMoveType: Type,
    withClear: Boolean = false
): String {
    return if (valueToMove.length <= valueToApplyMove.length) {
        var result: String = valueToApplyMove
        if (withClear) {
            result = clear(valueToApplyMoveType)
        }
        // overwrite valueToMove from left to right to valueToApplyMove
        valueToMove + result.substring(valueToMove.length)
    } else {
        // overwrite valueToMove to valueToApplyMove
        valueToMove.substring(0, valueToApplyMove.length)
    }
}

private fun move(
    valueToMove: String,
    valueToApplyMove: String,
    valueToApplyMoveType: Type,
    withClear: Boolean = false
): String {
    return if (valueToMove.length <= valueToApplyMove.length) {
        var result: String = valueToApplyMove
        if (withClear) {
            result = clear(valueToApplyMoveType)
        }
        // overwrite valueToMove from left to right to valueToApplyMove
        result.substring(0, result.length - valueToMove.length) + valueToMove
    } else {
        // overwrite valueToMove to valueToApplyMove
        valueToMove.substring(valueToMove.length - valueToApplyMove.length)
    }
}

private fun valueToString(value: Value, type: Type): String {
    var s = value.asString().value
    when (type) {
        is StringType -> {
            return if (type.varying) {
                s
            } else {
                s + " ".repeat(type.length - s.length)
            }
        }

        is CharacterType -> {
            return s
        }

        is NumberType -> {
            when (value) {
                is IntValue -> {
                    val zeros = "0".repeat(type.numberOfDigits - s.length)
                    return zeros + s
                }

                is DecimalValue -> {
                    s = s.replace(".", "")
                    val zeros = "0".repeat(type.numberOfDigits - s.length)
                    return zeros + s
                }
            }
        }

        else -> {
            throw UnsupportedOperationException("Unable to stringify the type: $type")
        }
    }
    return value.asString().toString()
}

private fun stringToValue(value: String, type: Type): Value {
    when (type) {
        is StringType -> {
            return if (type.varying) {
                StringValue(value, true)
            } else {
                StringValue(value, false)
            }
        }

        is CharacterType -> {
            return StringValue(value)
        }

        is NumberType -> {
            return if (type.integer) {
                // integer
                IntValue(value.toLong())
            } else {
                // decimal
                val integerPart = value.substring(0, type.entireDigits)
                val decimalPart = value.substring(type.entireDigits, value.length)
                DecimalValue(BigDecimal("$integerPart.$decimalPart"))
            }
        }

        else -> {
            throw UnsupportedOperationException("Unable to convert string to value the type: $type")
        }
    }
}