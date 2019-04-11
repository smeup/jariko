package com.smeup.rpgparser

import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.lang.UnsupportedOperationException
import java.math.BigDecimal
import java.util.*

/**
 * This represent the interface to the external world.
 * Printing, accessing databases, all sort of interactions should go through this interface.
 */
interface SystemInterface {
    fun display(value: String)
}

class SymbolTable {
    private val values = HashMap<AbstractDataDefinition, Value>()

    operator fun get(data: AbstractDataDefinition) : Value {
        if (data is FieldDefinition) {
            val containerValue = get(data.container!!)
            return if (data.container!!.isArray()) {
                ProjectedArrayValue(containerValue as ArrayValue, data)
            } else {
                (containerValue as StructValue).elements[data]!!
            }
        }
        return values[data] ?: throw IllegalArgumentException("Cannot find value for $data")
    }

    operator fun get(dataName: String) : Value {
        val data = values.keys.firstOrNull { it.name == dataName }
        if (data != null) {
            return values[data] ?: throw IllegalArgumentException("Cannot find value for $data")
        }
        for (e in values) {
            val field = (e.key as DataDefinition).fields?.firstOrNull { it.name == dataName }
            if (field != null) {
                return ProjectedArrayValue(e.value as ArrayValue, field)
            }
        }
        throw IllegalArgumentException("Cannot find value for $dataName")
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

    private fun initialize(compilationUnit: CompilationUnit, initialValues: Map<String, Value>) {
        // Assigning initial values received from outside and consider INZ clauses
        compilationUnit.dataDefinitions.forEach {
            globalSymbolTable[it] = when {
                it.name in initialValues -> initialValues[it.name]!!
                it.initializationValue != null -> interpret(it.initializationValue)
                else -> blankValue(it)
            }
        }
    }

    fun execute(compilationUnit: CompilationUnit, initialValues: Map<String, Value>) {
        initialize(compilationUnit, initialValues)
        compilationUnit.main.stmts.forEach {
            execute(it)
        }
    }

    private fun execute(statements: List<Statement>) {
        statements.forEach { execute(it) }
    }

    private fun execute(statement: Statement) {
        try {
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
                is PlistStmt -> null /* Nothing to do here */
                is ClearStmt -> {
                    return when (statement.value) {
                        is DataRefExpr -> {
                            assign(statement.value, BlanksRefExpr())
                            Unit
                        }
                        else -> throw UnsupportedOperationException("I do not know how to clear ${statement.value}")
                    }
                }
                is DisplayStmt -> {
                    val value = interpret(statement.value)
                    systemInterface.display(render(value))
                }
                is ForStmt -> {
                    eval(statement.init)
                    // TODO consider DOWNTO
                    while (isEqualOrSmaller(this[statement.iterDataDefinition()], eval(statement.endValue))) {
                        execute(statement.body)
                        increment(statement.iterDataDefinition())
                    }
                }
                else -> TODO(statement.toString())
            }
        } catch (e : RuntimeException) {
            throw RuntimeException("Issue executing statement $statement", e)
        }
    }

    private fun isEqualOrSmaller(value1: Value, value2: Value) : Boolean {
        return when {
            value1 is IntValue && value2 is IntValue -> value1.value <= value2.value
            else -> TODO()
        }
    }

    private fun increment(dataDefinition: AbstractDataDefinition) {
        val value = this[dataDefinition]
        if (value is IntValue) {
            println("incrementing ${dataDefinition.name} from ${value}")
            this[dataDefinition] = IntValue(value.value + 1)
        } else {
            throw UnsupportedOperationException()
        }
    }

    private fun areEquals(value1: Value, value2: Value) : Boolean {
        return value1 == value2
    }

    private fun render(value: Value) : String {
        return when (value) {
            is StringValue -> value.value
            is BooleanValue -> value.value.toString()
            is IntValue -> value.value.toString()
            else -> TODO(value.javaClass.canonicalName)
        }
    }

    private fun eval(expression: Expression) : Value {
        return when (expression) {
            is AssignmentExpr -> {
                assign(expression.target, expression.value)
            }
            else -> interpret(expression)
        }
    }

    private fun assign(target: AssignableExpression, value: Expression) : Value {
        when (target) {
            is DataRefExpr -> {
                val l = target as DataRefExpr
                val value = coerce(interpret(value), l.variable.referred!!.type())
                globalSymbolTable[target.variable.referred!!] = value
                return value
            }
            is ArrayAccessExpr -> {
                val arrayValue = interpret(target.array) as ArrayValue
                val indexValue = interpret(target.index)
                val value = coerce(interpret(value), (target.array.type() as ArrayType).element)
                arrayValue.setElement(indexValue.asInt().value.toInt(), value)
                return value
            }
            else -> TODO(target.toString())
        }
    }

    private fun coerce(value: Value, type: Type) : Value {
        // TODO to be completed
        return when (value) {
            is BlanksValue -> {
                when (type) {
                    is DataDefinitionType -> {
                        blankValue(type.dataDefinition as DataDefinition)
                    }
                    is RawType -> {
                        blankValue(type.size!!)
                    }
                    else -> TODO(type.toString())
                }
            }
            is StringValue -> {
                when (type) {
                    is RawType -> {
                        val missingLength = type.size!! - value.value.length
                        StringValue(value.value + " ".repeat(missingLength))
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
                    is ArrayValue -> value.size().asValue()
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
            is DecExpr -> {
                val decDigits = interpret(expression.decDigits).asInt().value
                val intDigits = interpret(expression.intDigits).asInt().value
                val valueAsString = interpret(expression.value).asString().value
                return if (decDigits == 0L) {
                    IntValue(valueAsString.toLong())
                } else {
                    DecimalValue(BigDecimal(valueAsString))
                }
            }
            is PlusExpr -> {
                val left = interpret(expression.left)
                val right = interpret(expression.right)
                when {
                    left is StringValue && right is StringValue -> StringValue(left.value + right.value)
                    left is IntValue && right is IntValue -> IntValue(left.value + right.value)
                    else -> throw UnsupportedOperationException("I do not know how to sum $left and $right at ${expression.position}")
                }
            }
            is CharExpr -> {
                val value = interpret(expression.value)
                return StringValue(render(value))
            }
            else -> TODO(expression.toString())
        }
    }

    fun blankValue(size: Int) = StringValue(" ".repeat(size))

    fun blankValue(dataDefinition: DataDefinition, forceElement: Boolean = false): Value {
        if (dataDefinition.arrayLength != null && !forceElement) {
            val nElements : Int = interpret(dataDefinition.arrayLength).asInt().value.toInt()
            return ConcreteArrayValue(Array(nElements) { blankValue(dataDefinition, true) }.toMutableList())
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
