package com.smeup.rpgparser

import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.util.*

/**
 * This represent the interface to the external world.
 * Printing, accessing databases, all sort of interactions should go through this interface.
 */
interface SystemInterface {

}

abstract class Value {
    open fun asInt() : IntValue = throw UnsupportedOperationException()
    open fun asBoolean() : BooleanValue = throw UnsupportedOperationException()
}

data class StringValue(val value: String) : Value()
data class IntValue(val value: Long) : Value() {
    override fun asInt() = this
}
data class BooleanValue(val value: Boolean) : Value() {
    override fun asBoolean() = this
}
data class ArrayValue(val elements: MutableList<Value>) : Value()
object BlanksValue : Value()

fun createArrayValue(n: Int, creator: (Int) -> Value) = ArrayValue(Array(n, creator).toMutableList())
fun blankString(length: Int) = StringValue(" ".repeat(length))

class SymbolTable {
    private val values = HashMap<AbstractDataDefinition, Value>()

    operator fun get(data: AbstractDataDefinition) : Value {
        return values[data] ?: throw IllegalArgumentException("Cannot find value for $data")
    }

    operator fun get(dataName: String) : Value {
        val data = values.keys.firstOrNull { it.name == dataName } ?: throw IllegalArgumentException("Cannot find value for $dataName")
        return values[data] ?: throw IllegalArgumentException("Cannot find value for $data")
    }

    operator fun set(data: AbstractDataDefinition, value: Value) {
        values[data] = value
    }

}

abstract class LogEntry
data class SubroutineExecutionLogEntry(val subroutine: Subroutine) : LogEntry()
data class ExpressionEvaluationLogEntry(val expression: Expression, val value: Value) : LogEntry()

class Interpreter(val systemInterface: SystemInterface) {
    private val globalSymbolTable = SymbolTable()
    private val logs = LinkedList<LogEntry>()

    fun getLogs() = logs
    fun getExecutedSubroutines() = logs.filterIsInstance(SubroutineExecutionLogEntry::class.java).map { it.subroutine }
    fun getExecutedSubroutineNames() = getExecutedSubroutines().map { it.name }
    fun getEvaluatedExpressions() = logs.filterIsInstance(ExpressionEvaluationLogEntry::class.java)
    /**
     * Remove an expression if the last time the same expression was evaluated it had the same value
     */
    fun getEvaluatedExpressionsConcise() : List<ExpressionEvaluationLogEntry> {
        val base= logs.filterIsInstance(ExpressionEvaluationLogEntry::class.java).toMutableList()
        var i = 0
        while (i < base.size) {
            val current = base[i]
            val found = base.subList(0, i).reversed().firstOrNull {
                it.expression == current.expression
            }?.value == current.value
            if (found) {
                base.removeAt(i)
            } else {
                i++
            }
        }
        return base
    }

    operator fun get(data: AbstractDataDefinition) = globalSymbolTable[data]
    operator fun get(dataName: String) = globalSymbolTable[dataName]
    operator fun set(data: AbstractDataDefinition, value: Value) {
        globalSymbolTable[data] = value
    }

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

    private fun execute(statements: List<Statement>) {
        statements.forEach { execute(it) }
    }

    private fun execute(statement: Statement) {
        when (statement) {
            is ExecuteSubroutine -> {
                logs.add(SubroutineExecutionLogEntry(statement.subroutine.referred!!))
                execute(statement.subroutine.referred!!.stmts)
            }
            is EvalStmt -> eval(statement.expression)
            is SelectStmt -> {
                for (case in statement.cases) {
                    if (interpret(case.condition).asBoolean().value) {
                        execute(case.body)
                        return
                    }
                }
                if (statement.other != null) {
                    execute(statement.other!!.body)
                }
            }
            is SetOnStmt -> null /* Nothing to do here */
            else -> TODO(statement.toString())
        }
    }

    private fun eval(expression: Expression) {
        when (expression) {
            is AssignmentExpr -> {
                assign(expression.target, expression.value)
            }
            else -> TODO(expression.toString())
        }
    }

    private fun assign(target: AssignableExpression, value: Expression) {
        when (target) {
            is DataRefExpr -> {
                val l = target as DataRefExpr
                globalSymbolTable[target.variable.referred!!] = coerce(interpret(value), l.variable.referred!!.type())
            }
            is ArrayAccessExpr -> {
                val arrayValue = interpret(target.array) as ArrayValue
                val indexValue = interpret(target.index)
                arrayValue.elements[indexValue.asInt().value.toInt()] = coerce(interpret(value), ((target.array as ArrayAccessExpr).array.type() as ArrayType).element)
            }
            else -> TODO(target.toString())
        }
    }

    private fun coerce(value: Value, type: Type) : Value {
        // TODO to be completed
        return when {
            value is BlanksValue -> {
                when (type) {
                    is DataDefinitionType -> {
                        blankValue(type.dataDefinition as DataDefinition)
                    }
                    else -> TODO(type.toString())
                }
            }
            else -> value
        }
    }

    fun interpret(expression: Expression) : Value {
        val value = interpretConcrete(expression)
        logs.add(ExpressionEvaluationLogEntry(expression, value))
        return value
    }

    private fun interpretConcrete(expression: Expression) : Value {
        return when (expression) {
            is StringLiteral -> StringValue(expression.value)
            is IntLiteral -> IntValue(expression.value)
            is NumberOfElementsExpr -> {
                val value = interpret(expression.value)
                when (value) {
                    is ArrayValue -> value.elements.size.asValue()
                    else -> throw IllegalStateException("Cannot ask number of elements of $value")
                }
            }
            is DataRefExpr -> globalSymbolTable[expression.variable.referred!!]
            is EqualityExpr -> {
                val left = interpret(expression.left)
                val right = interpret(expression.right)
                return (left == right).asValue()
            }
            is BlanksRefExpr -> {
                return BlanksValue
            }
            else -> TODO(expression.toString())
        }
    }

    fun blankValue(dataDefinition: DataDefinition, forceElement: Boolean = false): Value {
        if (dataDefinition.arrayLength != null && !forceElement) {
            val nElements : Int = interpret(dataDefinition.arrayLength).asInt().value.toInt()
            return ArrayValue(Array(nElements) { blankValue(dataDefinition, true) }.toMutableList())
        }
        return when {
            dataDefinition.dataType == DataType.SINGLE -> StringValue(" ".repeat(dataDefinition.actualSize(this).value.toInt()))
            dataDefinition.dataType == DataType.BOOLEAN -> BooleanValue(false)
            // TODO: to be revised
            dataDefinition.dataType == DataType.DATA_STRUCTURE -> StringValue(" ".repeat(dataDefinition.actualSize(this).value.toInt()))
            else -> TODO(dataDefinition.toString())
        }
    }
}

private fun Int.asValue() = IntValue(this.toLong())
private fun Boolean.asValue() = BooleanValue(this)

private fun DataDefinition.actualSize(interpreter: Interpreter) : IntValue {
    return when {
        this.size != null -> this.size.asValue()
        this.like != null -> interpreter.interpret(NumberOfElementsExpr(this.like)).asInt()
        else -> throw IllegalStateException("No actual size can be calculated")
    }
}

private fun DataDefinition.actualArrayLength(interpreter: Interpreter) : IntValue {
    return when {
        this.arrayLength != null -> interpreter.interpret(this.arrayLength).asInt()
        else -> IntValue(1)
    }
}
