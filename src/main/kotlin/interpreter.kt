package com.smeup.rpgparser

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

    fun execute(compilationUnit: CompilationUnit, initialValues: Map<String, Value>) {
        compilationUnit.dataDefinitons.forEach {
            if (it.name in initialValues) {
                globalSymbolTable[it.name] = initialValues[it.name]!!
            } else {
                globalSymbolTable[it.name] = blankValue(it)
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