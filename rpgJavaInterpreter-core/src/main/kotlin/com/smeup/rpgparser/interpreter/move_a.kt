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

fun movea(target: AssignableExpression, value: Expression, interpreterCoreHelper: InterpreterCoreHelper): Value {
    return if (target is DataRefExpr) {
        moveaFullArray(target, value, 1, interpreterCoreHelper)
    } else {
        require(target is ArrayAccessExpr) {
            "Result must be an Array element"
        }
        moveaFullArray(target.array as DataRefExpr, value, (interpreterCoreHelper.interpret(target.index) as IntValue).value.toInt(), interpreterCoreHelper)
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
            else -> TODO()
        }
        interpreterCoreHelper.assign(target, arrayValue)
    }
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
    val arrayValue = createArrayValue(target.type(), target.type().numberOfElements()) {
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
