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
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Multiplies the values of two expressions, formats the result according to the specified target type,
 * and applies optional half adjustment rounding.
 *
 * @param leftExpr the left-hand side expression to be evaluated and multiplied.
 * @param rightExpr the right-hand side expression to be evaluated and multiplied.
 * @param targetType the numeric type that dictates how the result should be formatted.
 * @param isHalfAdjust a flag specifying whether to apply half adjustment (rounding mode HALF_UP) or truncate (rounding mode DOWN).
 * @param interpreterCore the core interpreter used to evaluate the expressions.
 * @param position an optional position in the source code for logging or error reporting.
 * @return a Value containing the result of the multiplication formatted according to the specified target type.
 */
fun mult(
    leftExpr: Expression,
    rightExpr: Expression,
    targetType: Type,
    isHalfAdjust: Boolean,
    interpreterCore: InterpreterCore,
    position: Position? = null
): Value {
    // TODO When will pass my PR for more robustness replace Value.render with NumericValue.bigDecimal
    val leftValue = BigDecimal(interpreterCore.eval(leftExpr).render())
    val rightValue = BigDecimal(interpreterCore.eval(rightExpr).render())
    val result = rightValue.multiply(leftValue)

    require(targetType is NumberType)

    return if (isHalfAdjust) {
        DecimalValue(result.setScale(targetType.decimalDigits, RoundingMode.HALF_UP))
    } else {
        DecimalValue(result.setScale(targetType.decimalDigits, RoundingMode.DOWN))
    }
}