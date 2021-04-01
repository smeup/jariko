package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate

data class FunctionParam(val name: String, val type: Type)

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
        var plistParams = mutableListOf<FunctionParam>()
        this.compilationUnit.proceduresParamsDataDefinitions!!.forEach {
            plistParams.add(FunctionParam(it.name, it.type))
        }
        return plistParams
    }

    override fun execute(systemInterface: SystemInterface, params: List<FunctionValue>, symbolTable: ISymbolTable): Value {
        val interpreter: InternalInterpreter by lazy {
            InternalInterpreter(systemInterface)
        }

        var parameters = mutableMapOf<String, Value>()
        this.params().forEachIndexed { i, _ ->
            parameters[params[i].variableName.toString()] = params[i].value
        }

        interpreter.execute(this.compilationUnit, parameters, false)

        // TODO Update value of caller parameter (due to call by reference)
        this.params().forEachIndexed { i, e ->
            var a = parameters.keys.toList()[i]
            var p = e.name
            // symbolTable[a] = interpreter.globalSymbolTable[p]
        }

        return VoidValue
    }

    init {
        compilationUnit.resolveAndValidate()
    }

    companion object {
        /**
         * Create an RpgFunction instance achieved by current program
         * @param name Funtion name
         * */
        fun fromCurrentProgram(name: String): RpgFunction {
            return RpgFunction(
                compilationUnit = MainExecutionContext.getProgramStack().peek().cu.procedures!!.first {
                    it.name == name
                }
            )
        }
    }
}