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
 * Performs addition of two numerical expressions. The method evaluates the provided expressions
 * and computes the sum based on their types.
 *
 * @param value The first expression to be added. This should evaluate to either a single numerical value
 *              or an array of numerical values.
 * @param target The second expression to be added. This should evaluate to either a single numerical value
 *               or an array of numerical values.
 * @param interpreterCore The interpreter core responsible for evaluating the expressions.
 * @param position The position in the source code where this addition is invoked. Used for error reporting
 *                 and debugging. Nullable.
 * @return The result of the addition as a single numerical value.
 * @throws IllegalArgumentException If either of the evaluated expressions is not a number or an array of numbers.
 * @throws UnsupportedOperationException If the addition operation is not supported for the given evaluated types.
 */
fun add(
    value: Expression,
    target: Expression,
    interpreterCore: InterpreterCore,
    position: Position? = null
): Value {
    val addend1: Value = interpreterCore.eval(value)
    require(addend1 is NumberValue || (addend1 is ArrayValue && addend1.elementType is NumberType)) {
        "$addend1 should be a number"
    }
    val addend2: Value = interpreterCore.eval(target)
    require(addend2 is NumberValue || (addend2 is ArrayValue && addend2.elementType is NumberType)) {
        "$addend2 should be a number"
    }

    return when {
        addend1 is IntValue && addend2 is IntValue -> IntValue(addend1.asInt().value.plus(addend2.asInt().value))
        addend1 is IntValue && addend2 is DecimalValue -> DecimalValue(addend1.asDecimal().value.plus(addend2.value))
        addend1 is DecimalValue && addend2 is IntValue -> DecimalValue(addend1.value.plus(addend2.asDecimal().value))
        addend1 is DecimalValue && addend2 is DecimalValue -> DecimalValue(addend1.value.plus(addend2.value))
        else -> throw UnsupportedOperationException("I do not know how to sum $addend1 and $addend2 at $position")
    }
}