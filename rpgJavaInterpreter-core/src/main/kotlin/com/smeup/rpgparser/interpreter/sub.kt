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
 * Subtracts the evaluated `subtrahendExpr` from the evaluated `minuendExpr`.
 * The method supports operations between integer and decimal values.
 *
 * @param minuendExpr The expression representing the minuend.
 * @param subtrahendExpr The expression representing the subtrahend.
 * @param interpreterCore The interpreter core used to evaluate the expressions.
 * @param position Optional positional information for error reporting.
 * @return The result of the subtraction operation as a `Value`.
 * @throws IllegalArgumentException If either the evaluated `minuendExpr` or `subtrahendExpr` is not a number.
 * @throws UnsupportedOperationException If the operation cannot be performed on the provided values.
 */
fun sub(
    minuendExpr: Expression,
    subtrahendExpr: Expression,
    interpreterCore: InterpreterCore,
    position: Position? = null,
): Value {
    val minuendValue = interpreterCore.eval(minuendExpr)
    require(minuendValue is NumberValue) {
        "$minuendValue should be a number"
    }
    val subtrahendValue = interpreterCore.eval(subtrahendExpr)
    require(subtrahendValue is NumberValue) {
        "$subtrahendValue should be a number"
    }

    return when {
        minuendValue is IntValue && subtrahendValue is IntValue -> IntValue(minuendValue.asInt().value.minus(subtrahendValue.asInt().value))
        minuendValue is IntValue && subtrahendValue is DecimalValue ->
            DecimalValue(
                minuendValue.asDecimal().value.minus(subtrahendValue.value),
            )
        minuendValue is DecimalValue && subtrahendValue is IntValue ->
            DecimalValue(
                minuendValue.value.minus(subtrahendValue.asDecimal().value),
            )
        minuendValue is DecimalValue && subtrahendValue is DecimalValue -> DecimalValue(minuendValue.value.minus(subtrahendValue.value))
        else -> throw UnsupportedOperationException("I do not know how to sum $minuendValue and $subtrahendValue at $position")
    }
}
