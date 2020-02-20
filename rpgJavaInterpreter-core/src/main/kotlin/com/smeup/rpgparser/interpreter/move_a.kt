package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.*

fun move(target: AssignableExpression, value: Expression, interpreterCoreHelper: InterpreterCoreHelper): Value {
    when (target) {
        is DataRefExpr -> {
            var newValue = interpreterCoreHelper.interpret(value)
            if (value !is FigurativeConstantRef) {
                newValue = newValue.takeLast(target.size())
                if (value.type().size < target.size()) {
                    newValue =
                        interpreterCoreHelper.get(target.variable.referred!!).takeFirst((target.size() - value.type().size))
                            .concatenate(newValue)
                }
            }
            return interpreterCoreHelper.assign(target, newValue)
        }
        else -> TODO()
    }
}

fun movea(target: AssignableExpression, valueExpression: Expression, interpreterCoreHelper: InterpreterCoreHelper): Value {
    if (target.position?.start?.line == 244) {
        println("xx")
    }
    return if (target is DataRefExpr) {
        moveaFullArray(target, valueExpression, 1, interpreterCoreHelper)
    } else if (target is PredefinedGlobalIndicatorExpr) {
        interpreterCoreHelper.assign(target, interpreterCoreHelper.interpret(valueExpression))
    } else if (target is PredefinedIndicatorExpr) {
        val value = interpreterCoreHelper.interpret(valueExpression)
        for (index in target.index..ALL_PREDEFINED_INDEXES.last) {
            interpreterCoreHelper.assign(PredefinedIndicatorExpr(index), value)
        }
        value
    } else {
        require(target is ArrayAccessExpr) {
            "Result must be an Array element"
        }
        moveaFullArray(target.array as DataRefExpr, valueExpression, (interpreterCoreHelper.interpret(target.index) as IntValue).value.toInt(), interpreterCoreHelper)
    }
}

private fun moveaFullArray(target: DataRefExpr, value: Expression, startIndex: Int, interpreterCoreHelper: InterpreterCoreHelper): Value {
    val targetType = target.type()
    require(targetType is ArrayType) {
        "Result must be an Array"
    }
    return if (value is FigurativeConstantRef) {
        interpreterCoreHelper.assign(target, interpreterCoreHelper.interpret(value))
    } else {
        val arrayValue = when (targetType.element) {
            is StringType -> moveaString(target, startIndex, interpreterCoreHelper, value)
            is NumberType -> moveaNumber(target, startIndex, interpreterCoreHelper, value)
            else -> TODO()
        }
        interpreterCoreHelper.assign(target, arrayValue)
    }
}

private fun moveaNumber(
    target: DataRefExpr,
    startIndex: Int,
    interpreterCoreHelper: InterpreterCoreHelper,
    value: Expression
): ConcreteArrayValue {
    var newValue = interpreterCoreHelper.toArray(value)
    val targetArray = interpreterCoreHelper.get(target.variable.referred!!).asArray()
    val arrayValue = createArrayValue(baseType(target.type()), target.type().numberOfElements()) {
        if (it < (startIndex - 1)) {
            targetArray.getElement(it + 1)
        } else {
            val newValueIndex = it - startIndex + 1
            if (newValueIndex < newValue.elements.size) {
                newValue.elements[newValueIndex]
            } else {
                targetArray.getElement(it + 1)
            }
        }
    }
    return arrayValue
}

private fun InterpreterCoreHelper.toArray(expression: Expression): ConcreteArrayValue =
    when (expression) {
        is ArrayAccessExpr -> {
            val arrayValueRaw = interpret(expression.array)
            val arrayValue = arrayValueRaw as? ArrayValue
                ?: throw IllegalStateException("Array access to something that does not look like an array: ${expression.render()} (${expression.position})")
            val indexValue = interpret(expression.index).asInt().value.toInt()
            arrayValue
                .elements()
                .slice((indexValue - 1)..arrayValue.arrayLength())
                .asConcreteArrayValue(arrayValue.elementType)
        }
        is DataRefExpr -> {
            if (expression.type() is ArrayType) {
                interpret(expression) as ConcreteArrayValue
            } else {
                ConcreteArrayValue(mutableListOf(interpret(expression)), expression.type())
            }
        }
        else -> ConcreteArrayValue(mutableListOf(interpret(expression)), expression.type())
    }

private fun moveaString(
    target: DataRefExpr,
    startIndex: Int,
    interpreterCoreHelper: InterpreterCoreHelper,
    value: Expression
): ConcreteArrayValue {
    val realSize = target.type().elementSize() * (target.type().numberOfElements() - startIndex + 1)
    var newValue = interpreterCoreHelper.interpret(value).takeFirst(realSize)
    if (value.type().size < realSize) {
        newValue = newValue.concatenate(
            interpreterCoreHelper.get(target.variable.referred!!).takeLast((realSize - value.type().size))
        )
    }
    val arrayValue = createArrayValue(baseType(target.type()), target.type().numberOfElements()) {
        if (it < (startIndex - 1)) {
            interpreterCoreHelper.get(target.variable.referred!!).asArray().getElement(it + 1)
        } else {
            val index = it - startIndex + 1
            val startValue = (index * target.type().elementSize()) + 1
            newValue.take(startValue, startValue + target.type().elementSize() - 1)
        }
    }
    return arrayValue
}
