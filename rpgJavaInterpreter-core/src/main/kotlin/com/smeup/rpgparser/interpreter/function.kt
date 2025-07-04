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
import com.smeup.rpgparser.parsing.ast.DataWrapUpChoice
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
    fun execute(systemInterface: SystemInterface, params: List<FunctionValue>, interpreterStatus: InterpreterStatus): Value
}

interface JavaFunction : Function {
    override fun params(): List<FunctionParam> {
        TODO("'JavaFunction.params' not yet implemented")
    }
    override fun execute(systemInterface: SystemInterface, params: List<FunctionValue>, interpreterStatus: InterpreterStatus): Value
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
        interpreterStatus: InterpreterStatus
    ): Value {
        val interpreter = FunctionInterpreter(
            systemInterface = systemInterface,
            procedureName = compilationUnit.procedureName!!
        ).apply {
            getStatus().indicators = interpreterStatus.indicators
            getStatus().klists = interpreterStatus.klists
            getStatus().dbFileMap = interpreterStatus.dbFileMap
            getStatus().lastDBFile = interpreterStatus.lastDBFile
            getStatus().inzsrExecuted = interpreterStatus.inzsrExecuted
            getStatus().displayFiles = interpreterStatus.displayFiles
            getStatus().lastFound = interpreterStatus.lastFound

            getGlobalSymbolTable().parentSymbolTable = interpreterStatus.symbolTable
            setIndicators(interpreterStatus.indicators)
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
        MainExecutionContext.getConfiguration().jarikoCallback.onEnterFunction(compilationUnit.procedureName!!, params, interpreter.getGlobalSymbolTable())
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
        return (interpreter.getStatus().returnValue ?: VoidValue).apply {
            MainExecutionContext.getConfiguration().jarikoCallback.onExitFunction(compilationUnit.procedureName, this)
        }
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

    override fun execute(systemInterface: SystemInterface, params: List<FunctionValue>, interpreterStatus: InterpreterStatus): Value {
        checkParamsSize(params)
        params.forEachIndexed { index, functionValue ->
            val expectedType = expectedParams[index].type
            val value = coerce(functionValue.value, expectedType)
            if (!value.assignableTo(expectedType)) {
                functionValue.error("$functionValue is not assignable to parameter: ${expectedParams[index].name}")
            }
        }
        val previousValues = params.map { it.value }
        return function.execute(systemInterface, params, interpreterStatus).apply {
            params.forEachIndexed { index, functionValue ->
                functionValue.variableName?.apply {
                    val functionParam = expectedParams[index]
                    if (functionParam.paramPassedBy == ParamPassedBy.Reference && functionValue.value != previousValues[index]) {
                        interpreterStatus.symbolTable[interpreterStatus.symbolTable.dataDefinitionByName(this)!!] = functionValue.value
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

private class FunctionInterpreter(systemInterface: SystemInterface, private val procedureName: String) : InternalInterpreter(systemInterface = systemInterface) {

    private var initializing = false

    private val staticSymbolTable = getGlobalSymbolTable().getStaticSymbolTable(procedureName)

    private val staticMemorySliceId = MemorySliceId(
        activationGroup = "*STATIC",
        programName = functionName
    )

    private val functionName: String get() = "FunctionInterpreter.$procedureName.static"

    override fun getMemorySliceId(): MemorySliceId? {
        val memorySliceId = super.getMemorySliceId()
        return memorySliceId?.copy(programName = "${memorySliceId.programName}.$procedureName")
    }

    override fun getInterpretationContext(): InterpretationContext {
        return object : InterpretationContext {
            private var iDataWrapUpChoice: DataWrapUpChoice? = null
            override val currentProgramName: String
                get() = functionName

            override fun shouldReinitialize() = false
            override var dataWrapUpChoice: DataWrapUpChoice?
                get() = iDataWrapUpChoice
                set(value) {
                    iDataWrapUpChoice = value
                }
        }
    }

    /**
     * This way I can avoid to persist the memory slice of the function
     * */
    override fun getMemorySliceMgr(): MemorySliceMgr? = null

    override fun beforeInitialization() {
        initializing = true
        super.beforeInitialization()
    }

    override fun afterInitialization(initialValues: Map<String, Value>) {
        super.afterInitialization(initialValues)
        // I restore static symbol table from memory slice only if it is the first execution.
        // This is because the current state of memory slice will be persisted at the end of execution of
        // the main program
        if (isFirstExecution()) {
            staticSymbolTable.restoreFromMemorySlice(
                memorySliceMgr = MainExecutionContext.getMemorySliceMgr(),
                memorySliceId = staticMemorySliceId
            )
        }
        initializing = false
    }

    override fun set(data: AbstractDataDefinition, value: Value) {
        // if jariko is initializing and data is static, I must check if data is already present in static symbol table
        // if not, I can set it else not because static data must be initialized only once
        if (initializing && data.static) {
            if (!staticSymbolTable.contains(data.name)) {
                super.set(data, value)
            }
        } else {
            super.set(data, value)
        }
    }

    override fun doSomethingAfterExecution() {
        super.doSomethingAfterExecution()
        MainExecutionContext.getAttributes()[staticMemorySliceId.getAttributeKey()]?.let {
            (it as MemorySlice).persist = true
        }
    }

    private fun isFirstExecution(): Boolean {
        return MainExecutionContext.getAttributes()[staticMemorySliceId.getAttributeKey()] == null
    }
}
