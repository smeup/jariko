package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.*
import java.math.BigDecimal

private fun clear(value: String, type: Type): String {
    return when (type) {
        is NumberType -> "0".repeat(type.numberOfDigits)
        is StringType -> {
            if (type.varying) {
                // return the actual length of the string
                " ".repeat(value.length)
            } else {
                " ".repeat(type.size)
            }
        }

        else -> " ".repeat(type.size)
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
    if (value is FigurativeConstantRef) return interpreterCore.assign(target, interpreterCore.eval(value))

    val valueType = value.type()
    if (valueType is ArrayType) {
        throw UnsupportedOperationException("Cannot set an array as factor 2 in MOVE/MOVE(P) statement")
    }

    val valueToMove = valueToString(interpreterCore.eval(value), valueType)
    return when (val targetType = target.type()) {
        is ArrayType -> {
            // for each element of array apply move
            val arrayValue = interpreterCore.eval(target) as ConcreteArrayValue
            val valueToApplyMoveElementType = targetType.element
            arrayValue.elements.forEachIndexed { index, el ->
                arrayValue.setElement(
                    index + 1, stringToValue(
                        move(
                            valueToMove,
                            valueToString(el, valueToApplyMoveElementType),
                            valueToApplyMoveElementType,
                            operationExtender != null
                        ),
                        valueToApplyMoveElementType
                    )
                )
            }

            interpreterCore.assign(target, arrayValue)
        }
        else -> {
            val valueToApplyMove = valueToString(interpreterCore.eval(target), targetType)
            interpreterCore.assign(
                target,
                stringToValue(
                    move(valueToMove, valueToApplyMove, target.type(), operationExtender != null),
                    targetType
                )
            )
        }
    }
}

private fun movel(
    valueToMove: String,
    valueToApplyMove: String,
    valueToApplyMoveType: Type,
    withClear: Boolean = false
): String {
    val canContainValue = valueToMove.length <= valueToApplyMove.length
    return when {
        canContainValue -> {
            var result = valueToApplyMove
            if (withClear) {
                result = clear(result, valueToApplyMoveType)
            }
            // overwrite valueToMove from left to right to valueToApplyMove
            valueToMove + result.substring(valueToMove.length)
        }
        else -> {
            // overwrite valueToMove to valueToApplyMove
            valueToMove.substring(0, valueToApplyMove.length)
        }
    }
}

private fun move(
    valueToMove: String,
    valueToApplyMove: String,
    valueToApplyMoveType: Type,
    withClear: Boolean = false
): String {
    val canContainValue = valueToMove.length <= valueToApplyMove.length
    return when {
        canContainValue -> {
            var result = valueToApplyMove
            if (withClear) {
                result = clear(result, valueToApplyMoveType)
            }
            // overwrite valueToMove from left to right to valueToApplyMove
            result.substring(0, result.length - valueToMove.length) + valueToMove
        }
        else -> {
            // overwrite valueToMove to valueToApplyMove
            valueToMove.substring(valueToMove.length - valueToApplyMove.length)
        }
    }
}

private fun valueToString(value: Value, type: Type): String {
    var s = value.asString().value
    return when (type) {
        is StringType -> {
            return if (type.varying) {
                s
            } else {
                s + " ".repeat(type.length - s.length)
            }
        }

        is CharacterType -> return s

        is NumberType -> {
            if (value is NumberValue) {
                if (value.isNegative()) {
                    throw UnsupportedOperationException("MOVEL/MOVE for negative numbers not supported")
                }
                if (value is IntValue) {
                    val zeros = "0".repeat(type.numberOfDigits - s.length)
                    zeros + s
                } else {
                    s = s.replace(".", "")
                    val zeros = "0".repeat(type.numberOfDigits - s.length)
                    zeros + s
                }
            } else {
                throw UnsupportedOperationException("MOVE/MOVEL not supported for the type: $type")
            }
        }

        else -> throw UnsupportedOperationException("MOVE/MOVEL not supported for the type: $type")
    }
}

private fun stringToValue(value: String, type: Type): Value {
    return when (type) {
        is StringType -> {
            if (!type.varying) {
                val newValue = if (value.length < type.length) {
                    // fill with blank space
                    value + " ".repeat(type.length - value.length)
                } else value
                StringValue(newValue, false)
            } else StringValue(value, true)
        }
        is CharacterType -> StringValue(value)
        is NumberType -> {
            if (!type.integer) {
                // decimal
                val integerPart = value.substring(0, type.entireDigits)
                val decimalPart = value.substring(type.entireDigits, value.length)
                DecimalValue(BigDecimal("$integerPart.$decimalPart"))
            } else {
                // integer
                IntValue(value.toLong())
            }
        }
        else -> throw UnsupportedOperationException("MOVE/MOVEL not supported for the type: $type")
    }
}