package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.parsing.parsetreetoast.DateFormat
import com.smeup.rpgparser.parsing.parsetreetoast.isDecimal
import com.smeup.rpgparser.parsing.parsetreetoast.isInt
import com.smeup.rpgparser.parsing.parsetreetoast.toDecimal
import java.math.BigDecimal
import kotlin.math.min

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

        is UnlimitedStringType -> " ".repeat(value.length)

        else -> " ".repeat(type.size)
    }
}

/**
 * Performs RPGLE `MOVEL` operation by assigning `value` (Factor 2) to `target`,
 * handling both scalar and array types for `value`.
 *
 * If `value` is of type `ArrayType`, it delegates to `movelFactorAsArray` to handle array
 * operations. If `value` is a scalar, it delegates to `movelFactorAsScalar`. For figurative constants,
 * it directly assigns the evaluated constant to the `target`.
 *
 * @param operationExtender Optional modifier specifying an extender for the `MOVEL` operation.
 *        When non-null, enables the "clear" functionality, which clears existing target values
 *        before assignment.
 * @param target The target `AssignableExpression` where the result of the `MOVEL` operation
 *        will be assigned.
 * @param value The `Expression` representing Factor 2 in RPGLE. This can be a figurative constant,
 *        scalar, or array.
 * @param dataAttributes Optional `Expression` containing additional attributes, in example for date format.
 *        Can be null.
 * @param interpreterCore The `InterpreterCore` instance handling expression evaluation and assignment.
 *
 * @return The result `Value` of the `MOVEL` operation after assigning `value` to `target`.
 *
 * @throws IllegalArgumentException if the type of `target` or `value` does not support the `MOVEL` operation.
 */
fun movel(
    operationExtender: String?,
    target: AssignableExpression,
    value: Expression,
    dataAttributes: Expression?,
    interpreterCore: InterpreterCore
): Value {
    return if (value !is FigurativeConstantRef) {
        if (value.type() is ArrayType) {
            movelFactorAsArray(operationExtender, target, value, dataAttributes, interpreterCore)
        } else {
            movelFactorAsScalar(operationExtender, target, value, dataAttributes, interpreterCore)
        }
    } else {
        interpreterCore.assign(target, interpreterCore.eval(value))
    }
}

/**
 * Handles `MOVEL` operation when `source` (Factor 2) is an array type, assigning its elements
 * to `target` (Result). Elements are transferred up to the minimum length between `source` and
 * `target` arrays, applying any `operationExtender` if specified.
 *
 * @param operationExtender Optional modifier specifying an extender for the `MOVEL` operation.
 *        When non-null, enables the "clear" functionality, which clears existing target values
 *        before assignment.
 * @param target The `AssignableExpression` target array where elements from `source` will be
 *        assigned.
 * @param source The `Expression` source array (Factor 2) providing elements to transfer to `target`.
 * @param dataAttributes Optional `Expression` containing additional attributes, in example for date format.
 *        Can be null.
 * @param interpreterCore The `InterpreterCore` instance for evaluating, retrieving, and assigning
 *        expressions.
 *
 * @return The resulting `Value` after assigning elements from `source` to `target`.
 *
 * @throws UnsupportedOperationException If `target` is not an array type or if `source` and `target`
 *         have incompatible types for `MOVEL` operation.
 */
fun movelFactorAsArray(
    operationExtender: String?,
    target: AssignableExpression,
    source: Expression,
    dataAttributes: Expression?,
    interpreterCore: InterpreterCore
): Value {
    return when (target.type()) {
        is ArrayType -> {
            val arraySourceValue: ConcreteArrayValue = interpreterCore.eval(source) as ConcreteArrayValue
            val arrayTargetValue: ConcreteArrayValue = interpreterCore.eval(target) as ConcreteArrayValue
            val arraySourceType: Type = (source.type() as ArrayType).element
            val arrayTargetType: Type = (target.type() as ArrayType).element

            val maxSize = min(arraySourceValue.elements.size, arrayTargetValue.elements.size)
            for (i in 1 until maxSize + 1) {
                arrayTargetValue.setElement(
                    i,
                    valueToString(arraySourceValue.getElement(i), arraySourceType),
                    valueToString(arrayTargetValue.getElement(i), arrayTargetType),
                    arrayTargetType,
                    operationExtender
                )
            }
            interpreterCore.assign(target, arrayTargetValue)
        }
        else -> {
            throw UnsupportedOperationException("Not implemented MOVEL/MOVEL(P) statement between Factor 2 as ${source.type()} to Result as ${target.type()}.")
        }
    }
}

/**
 * Performs `MOVEL` operation when `value` (Factor 2) is a scalar, assigning it to `target`.
 * Handles specific transformations if `value` is a date type, and applies the value to each
 * element if `target` is an array. If `operationExtender` is specified, enables clearing
 * behavior during assignment.
 *
 * @param operationExtender Optional modifier specifying an extender for the `MOVEL` operation.
 *        When non-null, enables the "clear" functionality, which clears existing target values
 *        before assignment.
 * @param target The `AssignableExpression` where the transformed `value` will be assigned.
 * @param value The scalar `Expression` (Factor 2) to be moved to `target`.
 * @param dataAttributes Optional `Expression` containing additional attributes, in example for date format.
 *        Can be null.
 * @param interpreterCore The `InterpreterCore` instance used for expression evaluation and assignment.
 *
 * @return The resulting `Value` after the `MOVEL` operation, representing the assigned target value.
 *
 * @throws UnsupportedOperationException if `target` is of an incompatible type for `MOVEL`.
 */
fun movelFactorAsScalar(
    operationExtender: String?,
    target: AssignableExpression,
    value: Expression,
    dataAttributes: Expression?,
    interpreterCore: InterpreterCore
): Value {
    if (value.type() is DateType) {
        return interpreterCore.assign(target, dateToString(value, dataAttributes, interpreterCore))
    }

    val valueToMove: String = getStringOfValueBaseOfTarget(target, interpreterCore, value)
    return when (target.type()) {
        is ArrayType -> {
            // For each element of array apply MOVEL
            val arrayValue: ConcreteArrayValue = interpreterCore.eval(target) as ConcreteArrayValue
            val valueToApplyMoveElementType: Type = (target.type() as ArrayType).element
            arrayValue.elements.forEachIndexed { index, el ->
                arrayValue.setElement(
                    index + 1,
                    valueToMove,
                    valueToString(el, valueToApplyMoveElementType),
                    valueToApplyMoveElementType,
                    operationExtender
                )
            }
            interpreterCore.assign(target, arrayValue)
        }
        else -> {
            val valueToApplyMove: String = valueToString(interpreterCore.eval(target), target.type())
            return interpreterCore.assign(
                target,
                stringToValue(
                    movel(valueToMove, valueToApplyMove, target.type(), operationExtender != null),
                    target.type()
                )
            )
        }
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
            throw UnsupportedOperationException("Not implemented MOVEL/MOVEL(P) statement between Factor 2 as ${value.type()} to Result as ${target.type()}.")
        }
        val valueToMove: String = getStringOfValueBaseOfTarget(target, interpreterCore, value)
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

private fun getStringOfValueBaseOfTarget(
    target: AssignableExpression,
    interpreterCore: InterpreterCore,
    value: Expression
): String = when (target.type()) {
    is BooleanType -> {
        val valueInterpreted: String = (interpreterCore.eval(value)).asString().value
        when {
            (valueInterpreted.isInt() && valueInterpreted.toInt() in 0..1) -> valueInterpreted
            (valueInterpreted.isDecimal() && valueInterpreted.toDecimal() in 0.0..1.0) -> "0"
            valueInterpreted.isBlank() -> "0"
            else -> throw UnsupportedOperationException("MOVE/MOVEL for ${target.type()} have to be 0, 1 or blank")
        }
    }
    is DataStructureType -> {
        value.type().toDataStructureValue(interpreterCore.eval(value)).value
    }
    else -> valueToString(interpreterCore.eval(value), value.type())
}

private fun movel(
    valueToMove: String,
    valueToApplyMove: String,
    valueToApplyMoveType: Type,
    withClear: Boolean = false
): String {
    return if (valueToApplyMoveType is UnlimitedStringType) {
        valueToMove
    } else if (valueToMove.length <= valueToApplyMove.length) {
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

        is UnlimitedStringType -> return s

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

        is DataStructureType -> return s

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

        is UnlimitedStringType -> return UnlimitedStringValue(value)

        is CharacterType -> return StringValue(value)

        is NumberType -> {
            return if (type.integer) {
                // integer
                if (value.isBlank()) IntValue.ZERO else value.toLong().asValue()
            } else {
                // decimal
                val integerPart = value.substring(0, type.entireDigits)
                val decimalPart = value.substring(type.entireDigits, value.length)
                if (value.isBlank()) DecimalValue.ZERO else DecimalValue(BigDecimal("$integerPart.$decimalPart"))
            }
        }

        is BooleanType -> return StringValue(value)

        is DataStructureType -> {
            return DataStructValue(value)
        }

        else -> throw UnsupportedOperationException("MOVE/MOVEL not supported for the type: $type")
    }
}

/**
 * Sets an element in the `ConcreteArrayValue` array at the specified `index`, transforming
 * `sourceValueAsString` and applying it to the target based on `MOVEL` operation rules and
 * `targetType`. If `operationExtender` is provided, it enables the "clear" functionality in the
 * `MOVEL` operation, allowing existing values to be cleared before setting the new value.
 *
 * @param index The position in the array where the element will be set.
 * @param sourceValueAsString The new source value as a string to be assigned after transformation.
 * @param targetValueAsString The current value at `index`, used as a basis for `MOVEL` transformation.
 * @param targetType The `Type` of the target element, ensuring type compatibility during the assignment.
 * @param operationExtender Optional flag that, if non-null, enables the "clear" functionality within
 *        `MOVEL`, allowing `targetValueAsString` to be cleared before `sourceValueAsString` is assigned.
 *
 * @throws IndexOutOfBoundsException if `index` is outside the bounds of the array.
 */
private fun ConcreteArrayValue.setElement(index: Int, sourceValueAsString: String, targetValueAsString: String, targetType: Type, operationExtender: String?) {
    this.setElement(
        index, stringToValue(
            movel(
                sourceValueAsString,
                targetValueAsString,
                targetType,
                operationExtender != null
            ),
            targetType
        )
    )
}