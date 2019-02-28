package com.smeup.rpgparser

/**
 * This represent the interface to the external world.
 * Printing, accessing databases, all sort of interactions should go through this interface.
 */
interface SystemInterface {

}

abstract class Value

class Interpreter(val systemInterface: SystemInterface) {
    fun execute(compilationUnit: CompilationUnit) {
        TODO()
    }
    fun interpret(expression: Expression) : Value {
        TODO()
    }
}