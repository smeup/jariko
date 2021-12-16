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

/*
  JDP_OPTIONAL - Java Doped Procedure
  A sample of a 'doped' procedure, acting like an RPG-procedure
  but its implementation is done in kotlin and not in rpgle."
  Procedure is called by 'CALLP' and or 'EVAL' rpgle statement.
  Check right handle of optional parameters.
 */

class JDP_OPTIONAL() : JavaFunction {

    override fun execute(
        systemInterface: SystemInterface,
        params: List<FunctionValue>,
        symbolTable: ISymbolTable
    ): Value {

        val returnValue = when (params.size) {
            1 -> {
                val n1 = params.filter { fv -> fv.variableName == "N1" }.first().value as IntValue
                params.filter { fv -> fv.variableName == "N1" }.first().value = n1
                n1
            }
            2 -> {
                var n1 = params.filter { fv -> fv.variableName == "N1" }.first().value as IntValue
                val n2 = params.filter { fv -> fv.variableName == "N2" }.first().value as IntValue
                n1 += n2
                params.filter { fv -> fv.variableName == "N1" }.first().value = n1
                params.filter { fv -> fv.variableName == "N2" }.first().value = n2
                n1
            } else -> VoidValue
        }

        return returnValue
    }
}