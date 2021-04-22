/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smeup.rpgparser.jvminterop

import com.smeup.rpgparser.interpreter.*
import java.lang.RuntimeException

/*
  JDP_CALC - Java Doped Procedure
  A sample of a 'doped' procedure, acting like an RPG-procedure
  but its implementation is done in kotlin and not in rpgle."
  Procedure is called by 'CALLP' and or 'EVAL' rpgle statement.
 */

class JDP_CALC() : JavaFunction {

    override fun execute(
        systemInterface: SystemInterface,
        params: List<FunctionValue>,
        symbolTable: ISymbolTable
    ): Value {

        // Create local variables from received parameters
        var n1 = params.filter { fv -> fv.variableName == "N1" }.first().value as IntValue
        var n2 = params.filter { fv -> fv.variableName == "N2" }.first().value as IntValue
        var op = params.filter { fv -> fv.variableName == "OP" }.first().value as StringValue

        // Compute requested operation
        val returnValue = when (op.value) {
            "+" -> n1.value + n2.value
            "-" -> n1.value - n2.value
            "*" -> n1.value * n2.value
            "/" -> n1.value / n2.value
            else -> throw RuntimeException("Unsupported math operator: ${op.value}")
        }.asValue()

        // Change value of "N1" and "R1" parameter.
        // This doesn't mean it will affect value of variable in symboltable caller scope,
        // cause it depends on 'ParamPassedBy' policy (reference, value...)
        // In this case, only "R1" value will be changed in symboltable cause "N1" is
        // passed by value
        params.filter { fv -> fv.variableName == "N1" }.first().value = returnValue
        params.filter { fv -> fv.variableName == "R1" }.first().value = returnValue

        return returnValue
    }
}