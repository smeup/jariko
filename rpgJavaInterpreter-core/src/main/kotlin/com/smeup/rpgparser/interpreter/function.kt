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
import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import com.smeup.rpgparser.parsing.parsetreetoast.todo
import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Position

enum class ParamPassedBy {
    // Reference: any value change of variable, is reflected to variable parent
    // Value: a 'copy' of value is created, so any value change will not be reflected to variable parent
    // Const: like 'Reference' but 'read-only', so any value change will not be reflected to variable parent
    Reference, Value, Const
}

data class FunctionParam(
    val name: String,
    val type: Type,
    val paramPassedBy: ParamPassedBy = ParamPassedBy.Reference,
    override val position: Position? = null
) : Node(position)

data class FunctionValue(var variableName: String? = null, var value: Value, val type: Type? = null)

interface Function {
    fun params(): List<FunctionParam>
    fun execute(systemInterface: SystemInterface, params: List<FunctionValue>, symbolTable: ISymbolTable): Value
}

interface JavaFunction : Function {
    override fun params(): List<FunctionParam> {
        TODO("Not yet implemented")
    }
    override fun execute(systemInterface: SystemInterface, params: List<FunctionValue>, symbolTable: ISymbolTable): Value
}

/**
 * This class models a generic function, "generic" because could be a procedure if return a VoidValue else
 * a function if return something else.
 * */
open class RpgFunction(private val compilationUnit: CompilationUnit) : Function {

    override fun params(): List<FunctionParam> {
        val arguments = mutableListOf<FunctionParam>()
        this.compilationUnit.proceduresParamsDataDefinitions!!
            .forEach { arguments.add(FunctionParam(it.name, it.type, it.paramPassedBy, it.position)) }
        return arguments
    }

    override fun execute(
        systemInterface: SystemInterface,
        params: List<FunctionValue>,
        symbolTable: ISymbolTable
    ): Value {

        val interpreter = InternalInterpreter(systemInterface).apply {
            globalSymbolTable.parentSymbolTable = symbolTable
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
                    !paramsDataDefinition[index].paramOptions.contains(ParamOption.NoPass)
                ) {
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

class FunctionExecutor {
    companion object {
        fun execute(expressionEvaluator: Evaluator, expression: FunctionCall, systemInterface: SystemInterface, symbolTable: ISymbolTable): Value {

            val procedureParmsDataDefinition = mutableListOf<DataDefinition>()
            var compilationUnit: CompilationUnit

            // If recursive procedure calls
            if (expression.parent!!.parent!!.parent!!.parent is CompilationUnit) {
                compilationUnit = (expression.parent!!.parent!!.parent!!.parent as CompilationUnit)
            } else {
                compilationUnit = (expression.parent!!.parent!!.parent as CompilationUnit)
            }

            // If recursive procedure calls
            if (null != compilationUnit.procedures) {
                compilationUnit
                    .procedures
                    ?.filter {
                            p -> p.procedureName == expression.function.name
                    }
                    ?.forEach {
                        it.proceduresParamsDataDefinitions?.forEach {
                            procedureParmsDataDefinition.add(it)
                        }
                    }
            } else {
                procedureParmsDataDefinition.addAll(compilationUnit.proceduresParamsDataDefinitions!!)
            }

            // Check number and type of params
            expression.args.forEachIndexed { index, functionParam ->
                if (index < procedureParmsDataDefinition.size) {
                    if (functionParam.type() != procedureParmsDataDefinition[index].type) {
                        functionParam.todo("Procedure parameter '${procedureParmsDataDefinition[index].name}' of type '${procedureParmsDataDefinition[index].type}' must be as same type of varialble '${(functionParam as DataRefExpr).variable.name}' of type ${functionParam.type()}")
                    }
                } else {
                    // Any missing parm must be optional ('*NOPASS' keyword)
                    if (procedureParmsDataDefinition[index].paramOptions.isEmpty() ||
                        !procedureParmsDataDefinition[index].paramOptions.contains(ParamOption.NoPass)
                    ) {
                        functionParam.todo("Procedure parameter '${procedureParmsDataDefinition[index].name}' must be defined as optional (use '*NOPASS' keyword)")
                    }
                }
            }

            // Load list of variables name related to parameters passed by Reference
            val nameOfVariablesPassedByReference = mutableListOf<String>()
            expression.args.forEachIndexed { index, variable ->
                if (procedureParmsDataDefinition[index].paramPassedBy == ParamPassedBy.Reference) {
                    nameOfVariablesPassedByReference.add((variable as DataRefExpr).variable.name)
                }
            }

            val functionToCall = expression.function.name
            val function = systemInterface.findFunction(symbolTable, functionToCall)
                ?: throw RuntimeException("Function $functionToCall cannot be found (${expression.position.line()})")
            var paramsValues: List<FunctionValue>

            val returnValue = when (function) {
                is JavaFunction -> {
                    paramsValues = expression.args.map {
                        if (it is DataRefExpr) {
                            FunctionValue(variableName = it.variable.name, value = it.evalWith(expressionEvaluator), type = it.variable.referred!!.type)
                        } else {
                            FunctionValue(value = it.evalWith(expressionEvaluator))
                        }
                    }
                    val returnValue = function.execute(systemInterface, paramsValues, symbolTable)
                    // Set new params values if passing by Reference
                    paramsValues.forEach {
                        it.variableName?.let { variableName ->
                            if (nameOfVariablesPassedByReference.contains(variableName)) {
                                symbolTable[symbolTable.dataDefinitionByName(variableName)!!] = it.value
                            }
                        }
                    }
                    returnValue
                }
                else -> {
                        paramsValues = expression.args.map {
                            if (it is DataRefExpr) {
                                FunctionValue(variableName = it.variable.name, value = it.evalWith(expressionEvaluator))
                            } else {
                                FunctionValue(value = it.evalWith(expressionEvaluator))
                            }
                        }
                        function.execute(systemInterface, paramsValues, symbolTable)
                    }
                }

            return returnValue ?: VoidValue
            }
        }
    }