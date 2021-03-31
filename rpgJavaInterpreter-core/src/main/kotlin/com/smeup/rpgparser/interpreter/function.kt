package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate

data class FunctionParam(val name: String, val type: Type)

interface Function {
    fun params(): List<FunctionParam>
    fun execute(systemInterface: SystemInterface, params: List<Value>, symbolTable: ISymbolTable): Value
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
        return emptyList()
    }

    override fun execute(systemInterface: SystemInterface, params: List<Value>, symbolTable: ISymbolTable): Value {
        val interpreter: InternalInterpreter by lazy {
            InternalInterpreter(systemInterface)
        }

        var parms = mutableMapOf<String, Value>()
        repeat(params.size) {
            var parmName = this.compilationUnit.parmDefinitions!!.get(it).name
            var parmValue = params[it]
            parms.put(parmName, parmValue)
        }

        interpreter.execute(this.compilationUnit, parms, false)
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