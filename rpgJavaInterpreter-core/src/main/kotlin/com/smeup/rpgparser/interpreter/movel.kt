package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.parsing.parsetreetoast.DateFormat
import com.smeup.rpgparser.parsing.parsetreetoast.isDecimal
import com.smeup.rpgparser.parsing.parsetreetoast.isInt
import com.smeup.rpgparser.parsing.parsetreetoast.toDecimal
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
    dataAttributes: Expression?,
    interpreterCore: InterpreterCore
): Value {
    if (value !is FigurativeConstantRef) {
        if (value.type() is ArrayType) {
            throw UnsupportedOperationException("Cannot set an array as factor 2 in MOVEL/MOVEL(P) statement")
        }

        if (value.type() is DateType) {
            return interpreterCore.assign(target, dateToString(value, dataAttributes, interpreterCore))
        }

        val valueToMove: String = getStringOfValue(target, interpreterCore, value)
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
        val valueToMove: String = getStringOfValue(target, interpreterCore, value)
        if (target.type() is ArrayType) {
            // for each element of array apply move
            val arrayValue: ConcreteArrayValue = interpreterCore.eval(target) as ConcreteArrayValue
            val valueToApplyMoveElementType: Type = (target.type() as ArrayType).element
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

private fun getStringOfValue(
    target: AssignableExpression,
    interpreterCore: InterpreterCore,
    value: Expression
): String = when {
    target.type() is BooleanType -> {
        val valueInterpreted: String = (interpreterCore.eval(value)).asString().value
        when {
            (valueInterpreted.isInt() && valueInterpreted.toInt() in 0..1) -> valueInterpreted
            (valueInterpreted.isDecimal() && valueInterpreted.toDecimal() in 0.0..1.0) -> "0"
            valueInterpreted.isBlank() -> "0"
            else -> throw UnsupportedOperationException("MOVE/MOVEL for ${target.type()} have to be 0, 1 or blank")
        }
    }

    else -> valueToString(interpreterCore.eval(value), value.type())
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
            result = clear(result, valueToApplyMoveType)
        }
        // overwrite valueToMove from left to right to valueToApplyMove
        if (valueToApplyMoveType is BooleanType) valueToMove
            else valueToMove + result.substring(valueToMove.length)
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
            result = clear(result, valueToApplyMoveType)
        }
        // overwrite valueToMove from left to right to valueToApplyMove
        if (valueToApplyMoveType is BooleanType) valueToMove
            else result.substring(0, result.length - valueToMove.length) + valueToMove
    } else {
        // overwrite valueToMove to valueToApplyMove
        valueToMove.substring(valueToMove.length - valueToApplyMove.length)
    }
}

private fun dateToString(source: Expression, destinationFormat: Expression?, interpreterCore: InterpreterCore): Value {
    val sourceEvaluated: DateValue = interpreterCore.eval(source) as DateValue
    if (destinationFormat == null) {
        return sourceEvaluated.asString()
    }

    return when (destinationFormat) {
        is JulFormatExpr -> StringValue(sourceEvaluated.adapt(DateFormat.JUL))
        is IsoFormatExpr -> StringValue(sourceEvaluated.adapt(DateFormat.ISO))
        else -> throw UnsupportedOperationException("Unable to convert to $destinationFormat")
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

        is BooleanType -> return s

        else -> throw UnsupportedOperationException("MOVE/MOVEL not supported for the type: $type")
    }
}

private fun stringToValue(value: String, type: Type): Value {
    when (type) {
        is StringType -> {
            return if (type.varying) {
                StringValue(value, true)
            } else {
                var newValue = value
                if (value.length < type.length) {
                    // fill with blank space
                    newValue += " ".repeat(type.length - value.length)
                }
                StringValue(newValue, false)
            }
        }

        is CharacterType -> return StringValue(value)

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

        is BooleanType -> return StringValue(value)

        else -> throw UnsupportedOperationException("MOVE/MOVEL not supported for the type: $type")
    }
}