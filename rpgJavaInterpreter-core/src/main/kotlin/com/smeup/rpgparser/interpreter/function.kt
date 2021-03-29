package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.parsing.ast.CompilationUnit

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
        // println("Hello world")
        val interpreter = InternalInterpreter(systemInterface)
        interpreter.execute(this.compilationUnit, emptyMap(), false)
        return VoidValue
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