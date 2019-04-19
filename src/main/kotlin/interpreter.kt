package com.smeup.rpgparser

import com.smeup.rpgparser.ast.*
import java.io.InputStream
import java.math.BigDecimal
import java.util.*
import kotlin.collections.HashMap

interface Program {
    fun execute(systemInterface: SystemInterface, params: Map<String, Value>)
}

class RpgProgram(val inputStream: InputStream, val name: String = "<UNNAMED>") : Program {
    override fun execute(systemInterface: SystemInterface, params: Map<String, Value>) {
        val interpreter = Interpreter(systemInterface, programName = name)
        val cu = RpgParserFacade().parseAndProduceAst(inputStream)
        cu.resolve()
        interpreter.execute(cu, params)
    }
}

/**
 * This represent the interface to the external world.
 * Printing, accessing databases, all sort of interactions should go through this interface.
 */
interface SystemInterface {
    fun display(value: String)
    fun findProgram(name: String) : Program?
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
        return values[data] ?: throw IllegalArgumentException("Cannot find searchedValued for $data")
    }

    operator fun get(dataName: String) : Value {
        val data = values.keys.firstOrNull { it.name == dataName }
        if (data != null) {
            return values[data] ?: throw IllegalArgumentException("Cannot find searchedValued for $data")
        }
        for (e in values) {
            val field = (e.key as DataDefinition).fields?.firstOrNull { it.name == dataName }
            if (field != null) {
                return ProjectedArrayValue(e.value as ArrayValue, field)
            }
        }
        throw IllegalArgumentException("Cannot find searchedValued for $dataName")
    }

    operator fun set(data: AbstractDataDefinition, value: Value) {
        values[data] = value
    }

}

abstract class LogEntry
data class SubroutineExecutionLogEntry(val subroutine: Subroutine) : LogEntry()
data class ExpressionEvaluationLogEntry(val expression: Expression, val value: Value) : LogEntry()
data class AssignmentLogEntry(val data: AbstractDataDefinition, val value: Value) : LogEntry()

class Interpreter(val systemInterface: SystemInterface, val programName : String = "<UNNAMED>") {
    private val globalSymbolTable = SymbolTable()
    private val logs = LinkedList<LogEntry>()

    fun getLogs() = logs
    fun getExecutedSubroutines() = logs.filterIsInstance(SubroutineExecutionLogEntry::class.java).map { it.subroutine }
    fun getExecutedSubroutineNames() = getExecutedSubroutines().map { it.name }
    fun getEvaluatedExpressions() = logs.filterIsInstance(ExpressionEvaluationLogEntry::class.java)
    fun getAssignments() = logs.filterIsInstance(AssignmentLogEntry::class.java)
    /**
     * Remove an expression if the last time the same expression was evaluated it had the same searchedValued
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
        logs.add(AssignmentLogEntry(data, value))
        globalSymbolTable[data] = coerce(value, data.type)
    }

    private fun initialize(compilationUnit: CompilationUnit, initialValues: Map<String, Value>, reinitialization : Boolean = true) {
        // Assigning initial values received from outside and consider INZ clauses
        if (reinitialization) {
            compilationUnit.dataDefinitions.forEach {
                set(it, when {
                    it.name in initialValues -> initialValues[it.name]!!
                    it.initializationValue != null -> interpret(it.initializationValue)
                    else -> blankValue(it)
                })
            }
        } else {
            initialValues.forEach { iv ->
                set(compilationUnit.allDataDefinitions.find { it.name == iv.key }!!, iv.value)
            }
        }
    }

    fun simplyInitialize(compilationUnit: CompilationUnit, initialValues: Map<String, Value>) {
        initialize(compilationUnit, initialValues)
    }

    fun execute(compilationUnit: CompilationUnit, initialValues: Map<String, Value>, reinitialization : Boolean = true) {
        initialize(compilationUnit, initialValues, reinitialization)
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
                is EvalStmt -> assign(statement.target, statement.expression)
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
                is IfStmt -> {
                    val condition = eval(statement.condition).asBoolean().value
                    if (condition) {
                        execute(statement.body)
                    } else {
                        for (elseIfClause in statement.elseIfClauses) {
                            val c = eval(elseIfClause.condition).asBoolean().value
                            if (c) {
                                execute(elseIfClause.body)
                                return
                            }
                        }
                        if (statement.elseClause != null) {
                            execute(statement.elseClause.body)
                        }
                    }
                }
                is CallStmt -> {
                    val programToCall = eval(statement.expression).asString().value
                    val program = systemInterface.findProgram(programToCall) ?: throw RuntimeException("Program $programToCall cannot be found")
                    val params = statement.params.map { it.paramName to get(it.paramName) }.toMap()
                    program.execute(systemInterface, params)
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
            value1 is IntValue && value2 is StringValue -> throw RuntimeException("Cannot compare int and string")
            else -> TODO("Value 1 is $value1, Value 2 is $value2")
        }
    }

    private fun increment(dataDefinition: AbstractDataDefinition) {
        val value = this[dataDefinition]
        if (value is IntValue) {
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
            is StringValue -> value.valueWithoutPadding
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
                val value = coerce(interpret(value), l.variable.referred!!.type)
                set(target.variable.referred!!, value)
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
                    is StringType -> {
                        blankValue(type.length.toInt())
                    }
                    else -> TODO(type.toString())
                }
            }
            is StringValue -> {
                when (type) {
                    is StringType -> {
                        val missingLength = type.length - value.value.length
                        StringValue(value.value + "\u0000".repeat(missingLength.toInt()))
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
                    is ArrayValue -> value.arrayLength().asValue()
                    else -> throw IllegalStateException("Cannot ask number of elements of $value")
                }
            }
            is DataRefExpr -> get(expression.variable.referred ?: throw IllegalStateException("[$programName] Unsolved reference ${expression.variable.name} at ${expression.position}"))
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
                    IntValue(valueAsString.removeNullChars().toLong())
                } else {
                    DecimalValue(BigDecimal(valueAsString))
                }
            }
            is PlusExpr -> {
                val left = interpret(expression.left)
                val right = interpret(expression.right)
                when {
                    left is StringValue && right is StringValue -> StringValue(left.valueWithoutPadding + right.valueWithoutPadding)
                    left is IntValue && right is IntValue -> IntValue(left.value + right.value)
                    else -> throw UnsupportedOperationException("I do not know how to sum $left and $right at ${expression.position}")
                }
            }
            is CharExpr -> {
                val value = interpret(expression.value)
                return StringValue(render(value))
            }
            is LookupExpr -> {
                val searchValued = interpret(expression.searchedValued)
                val array = interpret(expression.array) as ArrayValue
                val index = array.elements().indexOfFirst { it == searchValued }
                return if (index == -1) 0.asValue() else (index + 1).asValue()
            }
            else -> TODO(expression.toString())
        }
    }

    fun blankValue(size: Int) = StringValue(" ".repeat(size))

    fun blankValue(type: Type): Value {
        return when (type){
            is ArrayType -> createArrayValue(type.element, type.nElements) {
                blankValue(type.element)
            }
            is DataStructureType -> StringValue.blank(type.size.toInt())
            is StringType ->  StringValue.blank(type.size.toInt())
            is NumberType -> IntValue(0)
            is BooleanType -> BooleanValue(false)
            else -> TODO(type.toString())
        }
    }

    fun blankValue(dataDefinition: DataDefinition, forceElement: Boolean = false): Value {
        if (forceElement) TODO()
        return blankValue(dataDefinition.type)
    }
}

private fun Int.asValue() = IntValue(this.toLong())
private fun Boolean.asValue() = BooleanValue(this)

object DummySystemInterface : SystemInterface {
    override fun findProgram(name: String): Program? {
        return null
    }

    override fun display(value: String) {
        // doing nothing
    }

}
