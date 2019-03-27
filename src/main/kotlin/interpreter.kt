package com.smeup.rpgparser

/**
 * This represent the interface to the external world.
 * Printing, accessing databases, all sort of interactions should go through this interface.
 */
interface SystemInterface {

}

abstract class Value

data class StringValue(val value: String) : Value()
data class IntValue(val value: Long) : Value()

class SymbolTable() {
    private val values = HashMap<String, Value>()

    operator fun get(name: String) : Value {
        return values[name]!!
    }

    operator fun set(name: String, value: Value) {
        values[name] = value
    }
}

class Interpreter(val systemInterface: SystemInterface) {
    private val globalSymbolTable = SymbolTable()

    fun setGlobalValue(name: String, value: Value) {
        globalSymbolTable[name] = value
    }

    fun execute(compilationUnit: CompilationUnit) {
        compilationUnit.main.stmts.forEach {

        }
    }

    private fun execute(statement: Statement) {
        TODO()
    }

    fun interpret(expression: Expression) : Value {
        return when (expression) {
            is StringLiteral -> StringValue(expression.value)
            is IntLiteral -> IntValue(expression.value)
            else -> TODO(expression.toString())
        }
    }
}