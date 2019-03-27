package com.smeup.rpgparser

import java.lang.IllegalArgumentException

/**
 * This represent the interface to the external world.
 * Printing, accessing databases, all sort of interactions should go through this interface.
 */
interface SystemInterface {

}

abstract class Value {
    open fun asInt() : IntValue = throw UnsupportedOperationException()
}

data class StringValue(val value: String) : Value()
data class IntValue(val value: Long) : Value() {
    override fun asInt() = this
}
data class ArrayValue(val elements: List<Value>) : Value()

class SymbolTable() {
    private val values = HashMap<AbstractDataDefinition, Value>()

    operator fun get(data: AbstractDataDefinition) : Value {
        return values[data] ?: throw IllegalArgumentException("Cannot find value for $data")
    }

    operator fun set(data: AbstractDataDefinition, value: Value) {
        values[data] = value
    }

}

class Interpreter(val systemInterface: SystemInterface) {
    private val globalSymbolTable = SymbolTable()

    fun execute(compilationUnit: CompilationUnit, initialValues: Map<String, Value>) {
        compilationUnit.dataDefinitions.forEach {
            if (it.name in initialValues) {
                globalSymbolTable[it] = initialValues[it.name]!!
            } else {
                globalSymbolTable[it] = blankValue(it)
            }
        }
        compilationUnit.main.stmts.forEach {
            execute(it)
        }
    }

    private fun execute(statement: Statement) {
        TODO()
    }

    fun interpret(expression: Expression) : Value {
        return when (expression) {
            is StringLiteral -> StringValue(expression.value)
            is IntLiteral -> IntValue(expression.value)
            is NumberOfElementsExpr -> {
                val value = interpret(expression.value)
                TODO(value.toString())
            }
            is DataRefExpr -> globalSymbolTable[expression.variable.referred!!]
            else -> TODO(expression.toString())
        }
    }

    fun blankValue(dataDefinition: DataDefinition, forceElement: Boolean = false): Value {
        if (dataDefinition.arrayLength != null && !forceElement) {
            val nElements : Int = interpret(dataDefinition.arrayLength).asInt().value.toInt()
            return ArrayValue(Array(nElements) { blankValue(dataDefinition, true) }.toList())
        }
        return when {
            dataDefinition.dataType == DataType.SINGLE -> StringValue(" ".repeat(dataDefinition.size!!))
            else -> TODO(this.toString())
        }
    }
}