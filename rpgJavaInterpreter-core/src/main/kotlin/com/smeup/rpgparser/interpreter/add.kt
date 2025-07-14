/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.Expression
import com.strumenta.kolasu.model.Position

/**
 * Adds two numerical expressions and returns the resulting value.
 *
 * @param addendOneExpr the first expression to be evaluated and added
 * @param addendTwoExpr the second expression to be evaluated and added
 * @param interpreterCore the interpreter core used to evaluate the expressions
 * @param position the position in the code where this operation occurs, can be null
 * @return the result of the addition as a Value; throws an exception if the addition cannot be performed
 */
fun add(
    addendOneExpr: Expression,
    addendTwoExpr: Expression,
    interpreterCore: InterpreterCore,
    position: Position? = null
): Value {
    val addendOneValue: Value = interpreterCore.eval(addendOneExpr)
    require(addendOneValue is NumberValue || (addendOneValue is ArrayValue && addendOneValue.elementType is NumberType)) {
        "$addendOneValue should be a number"
    }
    val addendTwoValue: Value = interpreterCore.eval(addendTwoExpr)
    require(addendTwoValue is NumberValue || (addendTwoValue is ArrayValue && addendTwoValue.elementType is NumberType)) {
        "$addendTwoValue should be a number"
    }

    return when {
        addendOneValue is ArrayValue && addendTwoValue is ArrayValue -> makeArrayValue(addendOneValue, addendTwoValue, position)
        addendOneValue is ArrayValue && addendTwoValue is NumberValue -> TODO()
        else -> makeSingleValue(addendOneValue, addendTwoValue, position)
    }
}

/**
 * Combines the values of two `Value` objects into a single `Value`,
 * performing addition based on their types.
 *
 * @param addendOneValue the first `Value` to be added; must be of type `IntValue` or `DecimalValue`
 * @param addendTwoValue the second `Value` to be added; must be of type `IntValue` or `DecimalValue`
 * @param position optional `Position` for contextual information during operation; used in exception message if applicable
 * @return a newly created `Value` resulting from the addition of `addendOneValue` and `addendTwoValue`,
 *         which will be either an `IntValue` or a `DecimalValue` depending on the input types
 * @throws UnsupportedOperationException if the provided `Value` types cannot be summed or are unsupported
 */
private fun makeSingleValue(addendOneValue: Value, addendTwoValue: Value, position: Position?): Value {
    return when {
        addendOneValue is IntValue && addendTwoValue is IntValue -> IntValue(addendOneValue.asInt().value.plus(addendTwoValue.asInt().value))
        addendOneValue is IntValue && addendTwoValue is DecimalValue -> DecimalValue(addendOneValue.asDecimal().value.plus(addendTwoValue.value))
        addendOneValue is DecimalValue && addendTwoValue is IntValue -> DecimalValue(addendOneValue.value.plus(addendTwoValue.asDecimal().value))
        addendOneValue is DecimalValue && addendTwoValue is DecimalValue -> DecimalValue(addendOneValue.value.plus(addendTwoValue.value))
        else -> throw UnsupportedOperationException("I do not know how to sum $addendOneValue and $addendTwoValue at $position")
    }
}

/**
 * Creates a new `ArrayValue` by combining the elements of two input `ArrayValue` objects.
 * The operation is element-wise and applies up to the minimum size of the two arrays.
 *
 * @param addendOneValue the first `ArrayValue` instance whose elements will be processed
 * @param addendTwoValue the second `ArrayValue` instance whose elements will be combined
 * @param position an optional `Position` object providing context for error reporting
 * @return a new `ArrayValue` containing the combined elements
 */
private fun makeArrayValue(addendOneValue: ArrayValue, addendTwoValue: ArrayValue, position: Position?): ArrayValue {
    val minSize = minOf(addendOneValue.arrayLength(), addendTwoValue.arrayLength())
    val newArrayValue = addendTwoValue.copy()

    1.rangeTo(minSize)
        .forEach { index ->
            newArrayValue.setElement(
                index,
                makeSingleValue(addendOneValue.getElement(index), newArrayValue.getElement(index), position)
            )
        }

    return newArrayValue
}