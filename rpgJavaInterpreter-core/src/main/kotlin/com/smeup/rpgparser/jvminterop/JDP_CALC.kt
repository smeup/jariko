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
import java.lang.RuntimeException

/*
  JDP_CALC - Java Doped Procedure
  A sample of a 'doped' procedure, acting like an RPG-procedure
  but its implementation is done in kotlin and not in rpgle."
  Procedure is called by 'CALLP' and or 'EVAL' rpgle statement.
 */

class JDP_CALC() : Function {

    override fun params(): List<FunctionParam> {
        val arguments = mutableListOf<FunctionParam>()
        arguments.add(FunctionParam("N1", NumberType(entireDigits = 3, decimalDigits = 0), paramPassedBy = ParamPassedBy.Value))
        arguments.add(FunctionParam("N2", NumberType(entireDigits = 3, decimalDigits = 0), paramPassedBy = ParamPassedBy.Value))
        arguments.add(FunctionParam("OP", StringType(length = 1), paramPassedBy = ParamPassedBy.Value))
        arguments.add(FunctionParam("R1", NumberType(entireDigits = 3, decimalDigits = 0), paramPassedBy = ParamPassedBy.Reference))
        return arguments
    }

    override fun execute(
        systemInterface: SystemInterface,
        params: List<FunctionValue>,
        symbolTable: ISymbolTable
    ): Value {

        var returnValue: IntValue
        val argumentNameToValue = mutableMapOf<String, Value>()
        val arguments = this.params()

        // Create map of 'VariableName, Value'
        arguments.forEachIndexed { index, functionParam ->
            if (index < params.size) {
                argumentNameToValue[functionParam.name] = params[index].value
            }
        }

        // Logic
        val num1 = argumentNameToValue["N1"] as IntValue
        val num2 = argumentNameToValue["N2"] as IntValue
        val operator = argumentNameToValue["OP"] as StringValue
        val result = when (operator.value) {
            "+" -> num1.value + num2.value
            "-" -> num1.value - num2.value
            "*" -> num1.value * num2.value
            "/" -> num1.value / num2.value
            else -> throw RuntimeException("Unsupported math operator: $operator")
        }

        argumentNameToValue["R1"] = result.asValue()
        returnValue = result.asValue()

        // Values could/couldn't return due to 'ParamPassedBy' policy
        params.forEachIndexed { index, functionValue ->
            functionValue.variableName?.let { variableName ->
                if (arguments[index].paramPassedBy == ParamPassedBy.Reference) {
                    symbolTable[symbolTable.dataDefinitionByName(variableName)!!] = argumentNameToValue[variableName]!!
                }
            }
        }

        return returnValue
    }
}