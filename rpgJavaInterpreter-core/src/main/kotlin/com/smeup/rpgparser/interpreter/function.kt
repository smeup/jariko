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
// todo in general all warning have to be resolved
package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.parsing.parsetreetoast.error
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
    val paramOptions: List<ParamOption> = emptyList(),
    override val position: Position? = null
) : Node(position)

data class FunctionValue(
    val variableName: String? = null,
    var value: Value,
    override val position: Position? = null
) : Node(position = position)

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
        return compilationUnit.getFunctionParams()
    }

    override fun execute(
        systemInterface: SystemInterface,
        params: List<FunctionValue>,
        symbolTable: ISymbolTable
    ): Value {

        val interpreter = FunctionInterpreter(
            systemInterface = systemInterface,
            procedureName = compilationUnit.procedureName).apply {
            getGlobalSymbolTable().parentSymbolTable = symbolTable
        }

        // values passed to function in format argumentName to argumentValue
        // every argumentName will be a variable scoped function
        val functionParamNameToValue = mutableMapOf<String, Value>()

        // arguments are parameters expected by function
        val functionParams = this.params()

        functionParams.forEachIndexed { index, functionParam ->
            if (index < params.size) {
                functionParamNameToValue[functionParam.name] = params[index].value
            }
        }
        interpreter.execute(this.compilationUnit, functionParamNameToValue, false)
        params.forEachIndexed { index, functionValue ->
            functionValue.variableName?.apply {
                val functionParam = functionParams[index]
                if (functionParam.paramPassedBy == ParamPassedBy.Reference) {
                    functionValue.value = interpreter[functionParam.name]
                }
            }
        }
        interpreter.doSomethingAfterExecution()
        return interpreter.getStatus().returnValue ?: VoidValue
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
                    it.procedureName.equals(name, ignoreCase = true)
                }
            )
        }
    }
}

class FunctionWrapper(private val function: Function, private val functionName: String, private val functionNode: Node) : Function {

    private val expectedParams: List<FunctionParam> by lazy {
        when (function) {
            is JavaFunction -> getProcedureUnit(functionName).getFunctionParams()
            else -> function.params()
        }
    }

    override fun params() = expectedParams

    private fun checkParamsSize(params: List<FunctionValue>) {
        if (params.size > expectedParams.size) {
            functionNode.error("You are passing to $functionName is ${params.size} but params expected is: $expectedParams")
        }
        if (params.size < expectedParams.size) {
            expectedParams.forEachIndexed { index, functionParam ->
                if (index >= params.size) {
                    // Any missing parm must be optional ('*NOPASS' keyword)
                    if (expectedParams[index].paramOptions.isEmpty() ||
                        !expectedParams[index].paramOptions.contains(ParamOption.NoPass)
                    ) {
                        functionParam.todo("Parameter '${expectedParams[index].name}' must be defined as optional (use '*NOPASS' keyword)")
                    }
                }
            }
        }
    }

    override fun execute(systemInterface: SystemInterface, params: List<FunctionValue>, symbolTable: ISymbolTable): Value {
        checkParamsSize(params)
        params.forEachIndexed { index, functionValue ->
            if (!functionValue.value.assignableTo(expectedParams[index].type)) {
                functionValue.error("$functionValue is not assignable to parameter: ${expectedParams[index].name}")
            }
        }
        val previousValues = params.map { it.value }
        return function.execute(systemInterface, params, symbolTable).apply {
            params.forEachIndexed { index, functionValue ->
                functionValue.variableName?.apply {
                    val functionParam = expectedParams[index]
                    if (functionParam.paramPassedBy == ParamPassedBy.Reference && functionValue.value != previousValues[index]) {
                        symbolTable[symbolTable.dataDefinitionByName(this)!!] = functionValue.value
                    }
                }
            }
        }
    }
}

fun CompilationUnit.getFunctionParams(): List<FunctionParam> {
    val arguments = mutableListOf<FunctionParam>()
    this.proceduresParamsDataDefinitions!!.forEach {
        arguments.add(
            FunctionParam(
                name = it.name,
                type = it.type,
                paramPassedBy = it.paramPassedBy,
                paramOptions = it.paramOptions,
                it.position
            )
        )
    }
    return arguments
}

private fun getProcedureUnit(functionName: String): CompilationUnit {
    return MainExecutionContext.getProgramStack().peek().cu.procedures!!.first {
        it.procedureName == functionName
    }
}

private class FunctionInterpreter(systemInterface: SystemInterface, private val procedureName: String?) : InternalInterpreter(systemInterface = systemInterface) {

    override fun getMemorySliceId(): MemorySliceId? {
        val memorySliceId = super.getMemorySliceId()
        return memorySliceId?.copy(programName = "${memorySliceId.programName}.$procedureName")
    }

    override fun getMemorySliceMgr(): MemorySliceMgr? = null

    override fun fireOnEnterPgmCallBackFunction() {
        // here I do nothing because I am not a program
    }
}