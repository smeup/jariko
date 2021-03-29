package com.smeup.rpgparser.interpreter

data class FunctionParam(val name: String, val type: Type)

interface Function {
    fun params(): List<FunctionParam>
    fun execute(systemInterface: SystemInterface, params: List<Value>, symbolTable: ISymbolTable): Value
}

abstract class JvmFunction(val name: String = "<UNNAMED>", val params: List<FunctionParam>) :
    Function {
    override fun params() = params
}

class RpgFunction(val name: String = "<UNNAMED FUNCTION>") : Function {
    override fun params(): List<FunctionParam> {
        TODO("Not yet implemented")
    }

    override fun execute(systemInterface: SystemInterface, params: List<Value>, symbolTable: ISymbolTable): Value {
        val changedInitialValue: List<Value> = emptyList()
        // TODO retrieve name "CALL1" from PROCEDURE_B program... CompilationUnit needed...
        // val interpreter = InternalInterpreter(systemInterface)
        // interpreter.execute(systemInterface.findProgram(this.name), emptyMap(), false)
        println("Hello world!")
        return changedInitialValue[0]
    }
}