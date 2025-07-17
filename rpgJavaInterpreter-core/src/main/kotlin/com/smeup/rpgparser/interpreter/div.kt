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

import com.smeup.rpgparser.parsing.ast.AssignableExpression
import com.smeup.rpgparser.parsing.ast.Expression
import com.strumenta.kolasu.model.Position
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

/**
 * Divides the value of the dividend expression by the value of the divisor expression and optionally adjusts
 * the result rounding based on the `isHalfAdjust` flag. If `mvrTarget` is specified, it also calculates the remainder (rest)
 * and assigns it to the specified target expression.
 *
 * @param dividendExpr The expression representing the dividend.
 * @param divisorExpr The expression representing the divisor.
 * @param targetType The type to which the result should be cast or scaled.
 * @param isHalfAdjust A flag indicating whether to use half-up rounding for the result.
 * @param interpreterCore The core of the interpreter used for evaluating expressions and handling assignments.
 * @param position The position in the source code associated with this operation, or null if not applicable.
 * @param mvrTarget An optional assignable expression to store the remainder of the division; null if not required.
 * @return The result of the division as a scaled and rounded Value, depending on `targetType` and `isHalfAdjust`.
 */
fun div(
    dividendExpr: Expression,
    divisorExpr: Expression,
    targetType: Type,
    isHalfAdjust: Boolean,
    interpreterCore: InterpreterCore,
    position: Position? = null,
    mvrTarget: AssignableExpression? = null
): Value {
    // TODO When will pass my PR for more robustness replace Value.render with NumericValue.bigDecimal
    val dividend = BigDecimal(interpreterCore.eval(dividendExpr).render())
    val divisor = BigDecimal(interpreterCore.eval(divisorExpr).render())
    val quotient = dividend.divide(divisor, MathContext.DECIMAL128)

    require(targetType is NumberType)

    // calculation of rest
    // NB. rest based on type of quotient
    if (mvrTarget != null) {
        val restType = mvrTarget.type()
        require(restType is NumberType)
        val truncatedQuotient: BigDecimal = quotient.setScale(targetType.decimalDigits, RoundingMode.DOWN)
        val rest: BigDecimal = dividend.subtract(truncatedQuotient.multiply(divisor))
        interpreterCore.assign(
            mvrTarget,
            DecimalValue(rest.setScale(restType.decimalDigits, RoundingMode.DOWN))
        )
    }

    return if (isHalfAdjust) {
        DecimalValue(quotient.setScale(targetType.decimalDigits, RoundingMode.HALF_UP))
    } else {
        DecimalValue(quotient.setScale(targetType.decimalDigits, RoundingMode.DOWN))
    }
}