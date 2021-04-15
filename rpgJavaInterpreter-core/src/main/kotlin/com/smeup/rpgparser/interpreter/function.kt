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

package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import com.smeup.rpgparser.parsing.parsetreetoast.todo
import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Position

enum class ParamPassedBy {
    Reference, Value
}

data class FunctionParam(
    val name: String,
    val type: Type,
    val paramPassedBy: ParamPassedBy = ParamPassedBy.Reference,
    override val position: Position? = null
) : Node(position)

data class FunctionValue(val variableName: String? = null, val value: Value)

interface Function {
    fun params(): List<FunctionParam>
    fun execute(systemInterface: SystemInterface, params: List<FunctionValue>, symbolTable: ISymbolTable): Value
}

abstract class JvmFunction(val name: String = "<UNNAMED>", val params: List<FunctionParam>) :
    Function {
    override fun params() = params
}

/**
 * This class models a generic function, "generic" because could be a procedure if return a VoidValue else
 * a function if return something else.
 * */
class RpgFunction(private val compilationUnit: CompilationUnit) : Function {

    override fun params(): List<FunctionParam> {
        val arguments = mutableListOf<FunctionParam>()
        this.compilationUnit.proceduresParamsDataDefinitions!!
            .forEach { arguments.add(FunctionParam(it.name, it.type, it.paramPassedBy, it.position)) }
        return arguments
    }

    private var interpreter: InternalInterpreter? = null

    override fun execute(systemInterface: SystemInterface, params: List<FunctionValue>, symbolTable: ISymbolTable): Value {

        if (interpreter == null) {
            interpreter = InternalInterpreter(systemInterface)
        }

        // values passed to function in format argumentName to argumentValue
        // every argumentName will be a variable scoped function
        val argumentNameToValue = mutableMapOf<String, Value>()

        // arguments are parameters expected by function
        val arguments = this.params()

        // Params size could be smaller than arguments size, due to any optional parameter ('*NOPASS' keyword)
        val paramsDataDefinition = this.compilationUnit.proceduresParamsDataDefinitions!!
        arguments.forEachIndexed { index, functionParam ->
            if (index < params.size) {
                argumentNameToValue[functionParam.name] = params[index].value
            } else {
                // Any missing parm must be optional ('*NOPASS' keyword)
                if (paramsDataDefinition[index].paramOptions.isEmpty() ||
                    !paramsDataDefinition[index].paramOptions.contains(ParamOption.NoPass)) {
                    functionParam.todo("Parameter '${paramsDataDefinition[index].name}' must be defined as optional (use '*NOPASS' keyword)")
                }
            }
        }

        interpreter!!.execute(this.compilationUnit, argumentNameToValue, false)

        params.forEachIndexed { index, functionValue ->
            // if passed param contains variable name and parameter is passed by reference
            functionValue.variableName?.let { variableName ->
                if (arguments[index].paramPassedBy == ParamPassedBy.Reference) {
                    symbolTable[symbolTable.dataDefinitionByName(variableName)!!] = interpreter!![arguments[index].name]
                }
            }
        }

        return interpreter!!.status.returnValue ?: VoidValue
    }

    init {
        compilationUnit.resolveAndValidate()
    }

    companion object {
        /**
         * Create a RpgFunction instance achieved by current program
         * @param name Function name
         * */
        fun fromCurrentProgram(name: String): RpgFunction {
            return RpgFunction(
                compilationUnit = MainExecutionContext.getProgramStack().peek().cu.procedures!!.first {
                    it.procedureName == name
                }
            )
        }
    }
}