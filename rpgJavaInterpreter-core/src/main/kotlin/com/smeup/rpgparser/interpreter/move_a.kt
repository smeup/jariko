package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.*
import java.lang.Math.pow
import java.math.BigDecimal
import kotlin.math.pow

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
    val targetArray = interpreterCore.get(target.variable.referred!!).asArray()
    val newValue = interpreterCore.toArray(value, targetArray.elementType)
    val arrayValue = createArrayValue(baseType(target.type()), target.type().numberOfElements()) {
        if (it < (startIndex - 1)) {
            targetArray.getElement(it + 1)
        } else {
            val newValueIndex = it - startIndex + 1
            var elementValue = if (newValueIndex < newValue.arrayLength()) {
                newValue.getElement(newValueIndex + 1)
            } else {
                if (operationExtenter == null) {
                    targetArray.getElement(it + 1)
                } else {
                    IntValue.ZERO
                }
            }
            // Proper conversion between a left side decimal to right side integer
            if (elementValue is DecimalValue && targetArray.elementType is NumberType && (targetArray.elementType as NumberType).decimalDigits == 0) {
                elementValue = DecimalValue(elementValue.value * BigDecimal(10.0.pow(((targetArray.elementType as NumberType).entireDigits - (newValue.elementType as NumberType).entireDigits).toDouble())))
            }
            elementValue
        }
    }
    return arrayValue
}

private fun InterpreterCore.toArray(expression: Expression, targetType: Type): ArrayValue =
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
                eval(expression) as ArrayValue
            } else {
                ConcreteArrayValue(mutableListOf(eval(expression)), expression.type())
            }
        }
        is IntLiteral -> {
            val value = eval(expression)
            if (targetType is NumberType && targetType.decimalDigits > 0) {
                val decimalValue = DecimalValue((value as IntValue).value.toDouble().div((10).pow(targetType.decimalDigits)).toBigDecimal())
                ConcreteArrayValue(mutableListOf(decimalValue), targetType)
            } else {
                ConcreteArrayValue(mutableListOf(value), targetType)
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
                val valueSize = when (val valueType = value.type()) {
                    is StringType -> valueType.length
                    else -> valueType.size
                }
                StringValue(" ".repeat((realSize - valueSize)))
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