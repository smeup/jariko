package com.smeup.rpgparser.interpreter

data class FunctionParam(val name: String, val type: Type)

interface Function {
    fun params() : List<FunctionParam>
    fun execute(systemInterface: SystemInterface, params: List<Value>, symbolTable: SymbolTable) : Value
}

abstract class JvmFunction(val name: String = "<UNNAMED>", val params: List<FunctionParam>)
    : Function {
    override fun params() = params
}
