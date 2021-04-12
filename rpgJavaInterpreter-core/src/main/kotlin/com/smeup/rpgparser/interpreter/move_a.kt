package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.*

fun move(target: AssignableExpression, value: Expression, interpreterCore: InterpreterCore): Value {
    when (target) {
        is DataRefExpr -> {
            var newValue = interpreterCore.eval(value)
            if (value !is FigurativeConstantRef) {
                newValue = newValue.takeLast(target.size())
                if (value.type().size < target.size()) {
                    newValue =
                        interpreterCore.get(target.variable.referred!!).takeFirst((target.size() - value.type().size))
                            .concatenate(newValue)
                }
            }
            return interpreterCore.assign(target, newValue)
        }
        else -> TODO()
    }
}

fun movea(operationExtenter: String?, target: AssignableExpression, valueExpression: Expression, interpreterCore: InterpreterCore): Value {
    return when (target) {
        is DataRefExpr -> {
            moveaFullArray(operationExtenter, target, valueExpression, 1, interpreterCore)
        }
        is GlobalIndicatorExpr -> {
            interpreterCore.assign(target, interpreterCore.eval(valueExpression))
        }
        is IndicatorExpr -> {
            val value = interpreterCore.eval(valueExpression)
            for (index in target.index..ALL_PREDEFINED_INDEXES.last) {
                interpreterCore.assign(IndicatorExpr(index), value)
            }
            value
        }
        else -> {
            require(target is ArrayAccessExpr) {
                "Result must be an Array element"
            }
            moveaFullArray(operationExtenter, target.array as DataRefExpr, valueExpression, (interpreterCore.eval(target.index) as IntValue).value.toInt(), interpreterCore)
        }
    }
}

private fun moveaFullArray(operationExtenter: String?, target: DataRefExpr, value: Expression, startIndex: Int, interpreterCore: InterpreterCore): Value {
    val targetType = target.type()
    require(targetType is ArrayType || targetType is StringType) {
        "Result must be an Array or a String"
    }
    return if (value is FigurativeConstantRef) {
        interpreterCore.assign(target, interpreterCore.eval(value))
    } else {
        val type = if (targetType is ArrayType) {
            targetType.element
        } else {
            targetType
        }
        val computedValue = when (type) {
            is StringType -> moveaString(operationExtenter, target, startIndex, interpreterCore, value)
            is NumberType -> moveaNumber(operationExtenter, target, startIndex, interpreterCore, value)
            else -> TODO()
        }
        interpreterCore.assign(target, computedValue)
    }
}

private fun moveaNumber(
    operationExtenter: String?,
    target: DataRefExpr,
    startIndex: Int,
    interpreterCore: InterpreterCore,
    value: Expression
): ConcreteArrayValue {
    var newValue = interpreterCore.toArray(value)
    val targetArray = interpreterCore.get(target.variable.referred!!).asArray()
    val arrayValue = createArrayValue(baseType(target.type()), target.type().numberOfElements()) {
        if (it < (startIndex - 1)) {
            targetArray.getElement(it + 1)
        } else {
            val newValueIndex = it - startIndex + 1
            if (newValueIndex < newValue.elements.size) {
                newValue.elements[newValueIndex]
            } else {
                if (operationExtenter == null) {
                    targetArray.getElement(it + 1)
                } else {
                    IntValue.ZERO
                }
            }
        }
    }
    return arrayValue
}

private fun InterpreterCore.toArray(expression: Expression): ConcreteArrayValue =
    when (expression) {
        is ArrayAccessExpr -> {
            val arrayValueRaw = eval(expression.array)
            val arrayValue = arrayValueRaw as? ArrayValue
                ?: throw IllegalStateException("Array access to something that does not look like an array: ${expression.render()} (${expression.position})")
            val indexValue = eval(expression.index).asInt().value.toInt()
            arrayValue
                .elements()
                .slice((indexValue - 1)..arrayValue.arrayLength())
                .asConcreteArrayValue(arrayValue.elementType)
        }
        is DataRefExpr -> {
            if (expression.type() is ArrayType) {
                eval(expression) as ConcreteArrayValue
            } else {
                ConcreteArrayValue(mutableListOf(eval(expression)), expression.type())
            }
        }
        else -> ConcreteArrayValue(mutableListOf(eval(expression)), expression.type())
    }

private fun moveaString(
    operationExtenter: String?,
    target: DataRefExpr,
    startIndex: Int,
    interpreterCore: InterpreterCore,
    value: Expression
): Value {
    val realSize = target.type().elementSize() * (target.type().numberOfElements() - startIndex + 1)
    var newValue = valueFromSourceExpression(interpreterCore, value).takeFirst(realSize).asString()
    if (newValue.value.length < realSize) {
        val other =
            if (operationExtenter == null) {
                interpreterCore.get(target.variable.referred!!).takeLast((realSize - newValue.value.length))
            } else {
                StringValue(" ".repeat((realSize - value.type().size)))
            }
        newValue = newValue.concatenate(other).asString()
    }
    if (target.type() is ArrayType) {
        return createArrayValue(baseType(target.type()), target.type().numberOfElements()) {
            if (it < (startIndex - 1)) {
                interpreterCore.get(target.variable.referred!!).asArray().getElement(it + 1)
            } else {
                val index = it - startIndex + 1
                val startValue = (index * target.type().elementSize()) + 1
                newValue.take(startValue, startValue + target.type().elementSize() - 1)
            }
        }
    } else {
        return newValue
    }
}

private fun valueFromSourceExpression(interpreterCore: InterpreterCore, valueExpression: Expression): Value {
    return if (valueExpression is ArrayAccessExpr) {
        val arrayValueRaw = interpreterCore.eval(valueExpression.array) as ArrayValue
        val index = (interpreterCore.eval(valueExpression.index) as NumberValue).bigDecimal.toInt()
        arrayValueRaw.concatenateElementsFrom(index)
    } else {
        interpreterCore.eval(valueExpression)
    }
}
