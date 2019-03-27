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

class Interpreter(val systemInterface: SystemInterface) {
    fun execute(compilationUnit: CompilationUnit) {
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