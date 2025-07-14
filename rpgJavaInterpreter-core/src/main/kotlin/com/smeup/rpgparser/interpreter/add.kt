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
        addendOneValue is IntValue && addendTwoValue is IntValue -> IntValue(addendOneValue.asInt().value.plus(addendTwoValue.asInt().value))
        addendOneValue is IntValue && addendTwoValue is DecimalValue -> DecimalValue(addendOneValue.asDecimal().value.plus(addendTwoValue.value))
        addendOneValue is DecimalValue && addendTwoValue is IntValue -> DecimalValue(addendOneValue.value.plus(addendTwoValue.asDecimal().value))
        addendOneValue is DecimalValue && addendTwoValue is DecimalValue -> DecimalValue(addendOneValue.value.plus(addendTwoValue.value))
        else -> throw UnsupportedOperationException("I do not know how to sum $addendOneValue and $addendTwoValue at $position")
    }
}