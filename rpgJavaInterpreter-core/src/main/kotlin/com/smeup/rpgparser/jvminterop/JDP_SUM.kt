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
import com.smeup.rpgparser.interpreter.Function

/*
  JDP_SUM - Java Doped Procedure
  A sample of a 'doped' procedure, acting like an RPG-procedure
  but its implementation is done in kotlin and not in rpgle."
  Procedure is called by 'CALLP' statement invoked by rpgle program.
 */

class JDP_SUM() : Function {

    override fun params(): List<FunctionParam> {
        val arguments = mutableListOf<FunctionParam>()
        arguments.add(FunctionParam("NUM1", NumberType(entireDigits = 3, decimalDigits = 0), paramPassedBy = ParamPassedBy.Reference))
        arguments.add(FunctionParam("NUM2", NumberType(entireDigits = 3, decimalDigits = 0), paramPassedBy = ParamPassedBy.Reference))
        arguments.add(FunctionParam("RES", NumberType(entireDigits = 3, decimalDigits = 0), paramPassedBy = ParamPassedBy.Reference))
        return arguments
    }

    override fun execute(
        systemInterface: SystemInterface,
        params: List<FunctionValue>,
        symbolTable: ISymbolTable
    ): Value {

        val argumentNameToValue = mutableMapOf<String, Value>()
        val arguments = this.params()

        arguments.forEachIndexed { index, functionParam ->
            if (index < params.size) {
                argumentNameToValue[functionParam.name] = params[index].value
            }
        }

        val result = (argumentNameToValue["NUM1"] as IntValue).value + (argumentNameToValue["NUM2"] as IntValue).value
        argumentNameToValue["RES"] = IntValue(result)

        params.forEachIndexed { index, functionValue ->
            functionValue.variableName?.let { variableName ->
                if (arguments[index].paramPassedBy == ParamPassedBy.Reference) {
                    symbolTable[symbolTable.dataDefinitionByName(variableName)!!] = argumentNameToValue[variableName]!!
                }
            }
        }

        return VoidValue
    }
}