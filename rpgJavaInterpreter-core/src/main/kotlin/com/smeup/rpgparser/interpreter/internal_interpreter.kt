package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.parsing.ast.AssignmentOperator.*
import com.smeup.rpgparser.parsing.ast.Comparison.EQ
import com.smeup.rpgparser.parsing.ast.Comparison.GE
import com.smeup.rpgparser.parsing.ast.Comparison.GT
import com.smeup.rpgparser.parsing.ast.Comparison.LE
import com.smeup.rpgparser.parsing.ast.Comparison.LT
import com.smeup.rpgparser.parsing.ast.Comparison.NE
import com.smeup.rpgparser.parsing.parsetreetoast.MuteAnnotationExecutionLogEntry
import com.smeup.rpgparser.utils.*
import java.lang.UnsupportedOperationException
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap

class LeaveException : Exception()
class IterException : Exception()

interface InterpretationContext {
    val name: String
    fun setDataWrapUpPolicy(dataWrapUpChoice: DataWrapUpChoice)
    fun shouldReinitialize(): Boolean
}

object DummyInterpretationContext : InterpretationContext {
    override val name: String
        get() = "<UNSPECIFIED>"

    override fun shouldReinitialize() = false

    override fun setDataWrapUpPolicy(dataWrapUpChoice: DataWrapUpChoice) {
        // nothing to do
    }
}

data class FileInformation(var found: Boolean)

class FileInformationMap {
    private val byFileName = TreeMap<String, FileInformation>(String.CASE_INSENSITIVE_ORDER)
    private val byFormatName = TreeMap<String, FileInformation>(String.CASE_INSENSITIVE_ORDER)

    fun add(fileDefinition: FileDefinition) {
        val fileInformation = FileInformation(false)
        byFileName[fileDefinition.name] = fileInformation
        val formatName = fileDefinition.formatName
        if (formatName != null && !fileDefinition.name.equals(formatName, ignoreCase = true)) {
            byFormatName[formatName] = fileInformation
        }
    }

    operator fun get(nameOrFormat: String): FileInformation? = byFileName[nameOrFormat] ?: byFormatName[nameOrFormat]
}

class InternalInterpreter(val systemInterface: SystemInterface) {
    private val globalSymbolTable = SymbolTable()
    private val predefinedIndicators = HashMap<Int, Value>()
    val executedAnnotation = HashMap<Int, MuteAnnotationExecuted>()
    var interpretationContext: InterpretationContext = DummyInterpretationContext
    /**
     * This is useful for debugging, so we can avoid infinite loops
     */
    var cycleLimit: Int? = null
    var logHandlers: List<InterpreterLogHandler> = emptyList()

    var lastFound = false
    private val fileInfos = FileInformationMap()

    private fun log(logEntry: LogEntry) {
        logHandlers.log(logEntry)
    }

    private fun exists(dataName: String) = globalSymbolTable.contains(dataName)

    private fun dataDefinitionByName(name: String) = globalSymbolTable.dataDefinitionByName(name)

    operator fun get(data: AbstractDataDefinition) = globalSymbolTable[data]
    operator fun get(dataName: String) = globalSymbolTable[dataName]

    operator fun set(data: AbstractDataDefinition, value: Value) {
        require(data.canBeAssigned(value)) {
            "Line ${data.position.line()}: $data cannot be assigned the value $value"
        }

        log(AssignmentLogEntry(data, value))
        globalSymbolTable[data] = coerce(value, data.type)
        // TODO add here the annotation evaluation ??
        // TODO CodeReview should we remove this comment?
    }

    private fun initialize(
        compilationUnit: CompilationUnit,
        initialValues: Map<String, Value>,
        reinitialization: Boolean = true
    ) {
        // TODO verify if these values should be reinitialised or not
        compilationUnit.fileDefinitions.forEach {
            fileInfos.add(it)
        }

        // Assigning initial values received from outside and consider INZ clauses
        if (reinitialization) {
            compilationUnit.dataDefinitions.forEach {
                set(it, coerce(when {
                    it.name in initialValues -> initialValues[it.name] ?: throw RuntimeException("Initial values for ${it.name} not found")
                    it.initializationValue != null -> interpret(it.initializationValue)
                    it.isCompileTimeArray() -> toArrayValue(compilationUnit.compileTimeArray(it.name), (it.type as ArrayType))
                    else -> blankValue(it)
                }, it.type))
                executeMutes(it.muteAnnotations)
            }
        } else {
            initialValues.forEach { iv ->
                val def = compilationUnit.allDataDefinitions.find { it.name.equals(iv.key, ignoreCase = true) }!!
                set(def, coerce(iv.value, def.type))
            }
        }
    }

    private fun toArrayValue(compileTimeArray: CompileTimeArray, arrayType: ArrayType): Value {
        val l: MutableList<Value> =
            compileTimeArray.lines.chunkAs(arrayType.compileTimeRecordsPerLine!!, arrayType.element.size.toInt())
                    .map {
                        coerce(StringValue(it), arrayType.element)
                    }
                    .resizeTo(arrayType.nElements, blankValue(arrayType.element))
                    .toMutableList()

        return ConcreteArrayValue(l, arrayType.element)
    }

    fun simplyInitialize(compilationUnit: CompilationUnit, initialValues: Map<String, Value>) {
        initialize(compilationUnit, initialValues)
    }

    fun execute(
        compilationUnit: CompilationUnit,
        initialValues: Map<String, Value>,
        reinitialization: Boolean = true
    ) {
        initialize(compilationUnit, caseInsensitiveMap(initialValues), reinitialization)
        compilationUnit.main.stmts.forEach {
            executeWithMute(it)
        }
    }

    private fun caseInsensitiveMap(aMap: Map<String, Value>): Map<String, Value> {
        val result = TreeMap<String, Value>(String.CASE_INSENSITIVE_ORDER)
        result.putAll(aMap)
        return result
    }

    private fun execute(statements: List<Statement>) {
        statements.forEach { executeWithMute(it) }
    }

    private fun executeWithMute(statement: Statement) {
        log(LineLogEntry(statement))
        execute(statement)
        executeMutes(statement.muteAnnotations)
    }

    private fun executeMutes(muteAnnotations: MutableList<MuteAnnotation>) {
        muteAnnotations.forEach {
            when (it) {
                is MuteComparisonAnnotation -> {
                    val exp: Expression = when (it.comparison) {
                        EQ -> EqualityExpr(it.val1, it.val2, it.position)
                        NE -> DifferentThanExpr(it.val1, it.val2, it.position)
                        GT -> GreaterThanExpr(it.val1, it.val2, it.position)
                        GE -> GreaterEqualThanExpr(it.val1, it.val2, it.position)
                        LT -> LessThanExpr(it.val1, it.val2, it.position)
                        LE -> LessEqualThanExpr(it.val1, it.val2, it.position)
                    }
                    val value1 = interpretConcrete(it.val1)
                    val value2 = interpretConcrete(it.val2)
                    // TODO use value1 and value2 without re-evaluate them as they could have side-effects
                    val value = interpretConcrete(exp)
                    log(MuteAnnotationExecutionLogEntry(it, value))
                    executedAnnotation[it.position!!.start.line] = MuteAnnotationExecuted(exp, it.val1, it.val2, value, value1, value2)
                }
                is MuteTypeAnnotation -> {
                    // Skip
                }
                else -> throw UnsupportedOperationException("Unknown type of annotation: $it")
            }
        }
    }

    private fun execute(statement: Statement) {
        try {
            when (statement) {
                is ExecuteSubroutine -> {
                    log(SubroutineExecutionLogEntry(statement.subroutine.referred!!))
                    execute(statement.subroutine.referred!!.stmts)
                }
                is EvalStmt -> {
                    assign(statement.target, statement.expression, statement.operator)
                }
                is MoveStmt -> move(statement.target, statement.expression)
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
                is SetOnStmt -> {
                    statement.choices.forEach {
                        interpretationContext.setDataWrapUpPolicy(it)
                    }
                }
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
                is ZAddStmt -> {
                    assign(statement.target, eval(statement.expression))
                }
                is TimeStmt -> {
                    return when (statement.value) {
                        is DataRefExpr -> {
                            assign(statement.value, TimeStampValue(Date()))
                            Unit
                        }
                        else -> throw UnsupportedOperationException("I do not know how to set TIME to ${statement.value}")
                    }
                }
                is DisplayStmt -> {
                    val values = mutableListOf<Value>()
                    statement.factor1?.let { values.add(interpret(it)) }
                    statement.response?.let { values.add(interpret(it)) }
                    // TODO: receive input from systemInterface and assign value to response
                    systemInterface.display(render(values))
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
                    log(CallExecutionLogEntry(statement))
                    val programToCall = eval(statement.expression).asString().value
                    val program = systemInterface.findProgram(programToCall) ?: throw RuntimeException("Program $programToCall cannot be found")

                    val params = statement.params.mapIndexed { index, it ->
                        if (it.dataDefinition != null) {
                            if (it.dataDefinition.initializationValue != null) {
                                if (!exists(it.param.name)) {
                                    assign(it.dataDefinition, eval(it.dataDefinition.initializationValue))
                                } else {
                                    assign(dataDefinitionByName(it.param.name)!!, eval(it.dataDefinition.initializationValue))
                                }
                            } else {
                                if (!exists(it.param.name)) {
                                    assign(it.dataDefinition, eval(BlanksRefExpr()))
                                }
                            }
                        }
                        program.params()[index].name to get(it.param.name)
                    }.toMap(LinkedHashMap())

                    val paramValuesAtTheEnd =
                        try {
                            program.execute(systemInterface, params).apply {
                                log(CallEndLogEntry(statement))
                            }
                        } catch (e: Exception) { // TODO Catch a more specific exception?
                            log(CallEndLogEntry(statement))
                            if (statement.errorIndicator == null) {
                                throw e
                            }
                            predefinedIndicators[statement.errorIndicator] = BooleanValue.TRUE
                            null
                        }
                    paramValuesAtTheEnd?.forEachIndexed { index, value ->
                        assign(statement.params[index].param.referred!!, value)
                    }
                }
                is ForStmt -> {
                    eval(statement.init)
                    try {
                        while (enterCondition(this[statement.iterDataDefinition()], eval(statement.endValue), statement.downward)) {
                            execute(statement.body)
                            increment(statement.iterDataDefinition(), step(statement.byValue, statement.downward))
                        }
                    } catch (e: LeaveException) {
                        // leaving
                    }
                }
                is DoStmt -> {
                    if (statement.index == null) {
                        var myIterValue = eval(statement.startLimit).asInt()
                        try {
                            while ((cycleLimit == null || (cycleLimit as Int) >= myIterValue.value) &&
                                    isEqualOrSmaller(myIterValue, eval(statement.endLimit))) {
                                try {
                                    execute(statement.body)
                                } catch (e: IterException) {
                                    // nothing to do here
                                }
                                myIterValue = myIterValue.increment()
                            }
                        } catch (e: LeaveException) {
                            // nothing to do here
                        }
                    } else {
                        assign(statement.index, statement.startLimit)
                        try {
                            while ((cycleLimit == null || (cycleLimit as Int) >= eval(statement.index).asInt().value) &&
                                    isEqualOrSmaller(eval(statement.index), eval(statement.endLimit))) {
                                try {
                                    execute(statement.body)
                                } catch (e: IterException) {
                                    // nothing to do here
                                }
                                assign(statement.index, PlusExpr(statement.index, IntLiteral(1)))
                            }
                        } catch (e: LeaveException) {
                            // nothing to do here
                        }
                    }
                }
                is DowStmt -> {
                    try {
                        while (eval(statement.endExpression).asBoolean().value) {
                            execute(statement.body)
                        }
                    } catch (e: LeaveException) {
                        // nothing to do here
                    }
                }
                is SubDurStmt -> {
                    when (statement.target) {
                        is DataRefExpr -> {
                            // TODO: partial implementation just for *MS - Add more cases
                            val minuend = if (statement.factor1 == null) {
                                interpret(statement.target)
                            } else {
                                interpret(statement.factor1)
                            }
                            val subtrahend = interpret(statement.factor2)
                            val newValue = (minuend.asTimeStamp().value.time - subtrahend.asTimeStamp().value.time) * 1000
                            assign(statement.target, IntValue(newValue))
                        }
                        else -> throw UnsupportedOperationException("Data reference required: " + statement)
                    }
                }
                is LeaveStmt -> throw LeaveException()
                is IterStmt -> throw IterException()
                is CheckStmt -> {
                    var baseString = interpret(statement.baseString).asString().value.removeNullChars()
                    if (statement.baseString is DataRefExpr) {
                        baseString = baseString.padEnd(statement.baseString.size().toInt())
                    }
                    val charSet = interpret(statement.comparatorString).asString().value
                    val wrongIndex = statement.wrongCharPosition
                    lastFound = false
                    if (wrongIndex != null) {
                        assign(wrongIndex, IntValue.ZERO)
                    }
                    baseString.substring(statement.start - 1).forEachIndexed {
                        i, c -> if (!charSet.contains(c)) {
                            if (wrongIndex != null) {
                                assign(wrongIndex, IntValue((i + statement.start).toLong()))
                            }
                            lastFound = true
                            return
                        }
                    }
                }
                is ChainStmt -> {
                    // -- TODO handle record formats
                    require(fileInfos[statement.name] != null) {
                        "Line: ${statement.position.line()} - File definition ${statement.name} not found"
                    }

                    // --
                    val record = systemInterface.db.chain(statement.name, eval(statement.searchArg))
                    if (record != null) {
                        lastFound = true
                        record.forEach { assign(it.first, it.second) }
                    } else {
                        lastFound = false
                    }
                    fileInfos[statement.name]!!.found = lastFound
                }
                else -> TODO(statement.toString())
            }
        } catch (e: InterruptForDebuggingPurposes) {
            throw e
        } catch (e: RuntimeException) {
            throw RuntimeException("Issue executing statement $statement", e)
        }
    }

    private fun enterCondition(index: Value, end: Value, downward: Boolean): Boolean =
        if (downward) {
            isEqualOrGreater(index, end)
        } else {
            isEqualOrSmaller(index, end)
        }

    private fun step(byValue: Expression, downward: Boolean): Long {
        val sign = if (downward) {
            -1
        } else {
            1
        }
        return eval(byValue).asInt().value * sign
    }

    enum class Comparison {
        SMALLER,
        EQUAL,
        GREATER
    }

    private fun isEqualOrSmaller(value1: Value, value2: Value): Boolean {
        val cmp = compare(value1, value2)
        return cmp == Comparison.SMALLER || cmp == Comparison.EQUAL
    }

    private fun isEqualOrGreater(value1: Value, value2: Value): Boolean {
        val cmp = compare(value1, value2)
        return cmp == Comparison.GREATER || cmp == Comparison.EQUAL
    }

    private fun isGreaterThan(value1: Value, value2: Value): Boolean {
        val cmp = compare(value1, value2)
        return cmp == Comparison.GREATER
    }

    private fun isSmallerThan(value1: Value, value2: Value): Boolean {
        val cmp = compare(value1, value2)
        return cmp == Comparison.SMALLER
    }

    private fun compare(value1: Value, value2: Value): Comparison {
        return when {
            value1 is IntValue && value2 is IntValue -> when {
                value1.value == value2.value -> Comparison.EQUAL
                value1.value < value2.value -> Comparison.SMALLER
                else -> Comparison.GREATER
            }
            value1 is IntValue && value2 is StringValue -> throw RuntimeException("Cannot compare int and string")
            value2 is HiValValue -> Comparison.SMALLER
            else -> TODO("Unable to compare: value 1 is $value1, Value 2 is $value2")
        }
    }

    private fun increment(dataDefinition: AbstractDataDefinition, amount: Long = 1) {
        val value = this[dataDefinition]
        if (value is IntValue) {
            this[dataDefinition] = IntValue(value.value + amount)
        } else {
            throw UnsupportedOperationException()
        }
    }

    private fun areEquals(value1: Value, value2: Value): Boolean {
        return when {
            value1 is BlanksValue && value2 is StringValue -> value2.isBlank()
            value2 is BlanksValue && value1 is StringValue -> value1.isBlank()
            value1 is StringValue && value2 is StringValue -> {
                val v1 = value1.value.trimEnd().removeNullChars()
                val v2 = value2.value.trimEnd().removeNullChars()
                v1 == v2
            }
            else -> value1 == value2
        }
    }

    private fun render(values: List<Value>) = values.map { render(it) }.joinToString("")

    private fun render(value: Value): String {
        return when (value) {
            is StringValue -> value.valueWithoutPadding
            is BooleanValue -> value.asString().value // TODO check if it's the best solution
            is IntValue -> value.value.toString()
            is DecimalValue -> value.value.toString() // TODO: formatting rules
            else -> TODO("Unable to render value $value (${value.javaClass.canonicalName})")
        }
    }

    private fun eval(expression: Expression): Value {
        return when (expression) {
            is AssignmentExpr -> {
                assign(expression.target, expression.value)
            }
            else -> interpret(expression)
        }
    }

    private fun assign(dataDefinition: AbstractDataDefinition, value: Value): Value {
        val coercedValue = coerce(value, dataDefinition.type)
        set(dataDefinition, coercedValue)
        return coercedValue
    }

    private fun assign(target: AssignableExpression, value: Value): Value {
        when (target) {
            is DataRefExpr -> {
                return assign(target.variable.referred!!, value)
            }
            is ArrayAccessExpr -> {
                val arrayValue = interpret(target.array) as ArrayValue
                require(arrayValue.assignableTo(target.array.type()))
                val indexValue = interpret(target.index)
                val elementType = (target.array.type() as ArrayType).element
                val evaluatedValue = coerce(value, elementType)
                val index = indexValue.asInt().value.toInt()
                log(AssignmentOfElementLogEntry(target.array, index, evaluatedValue))
                arrayValue.setElement(index, evaluatedValue)
                return evaluatedValue
            }
            is SubstExpr -> {
                val oldValue = eval(target.string).asString().value
                val length = if (target.length != null) eval(target.length).asInt().value.toInt() else null
                val start = eval(target.start).asInt().value.toInt() - 1

                val newValue = if (length == null) {
                    StringValue(oldValue.replaceRange(start, oldValue.length, value.asString().value))
                } else {
                    val paddedValue = value.asString().value.padEnd(length)
                    StringValue(oldValue.replaceRange(start, start + length, paddedValue))
                }

                return assign(target.string as AssignableExpression, newValue)
            }
            else -> TODO(target.toString())
        }
    }

    private fun assign(target: AssignableExpression, value: Expression, operator: AssignmentOperator = NORMAL_ASSIGNMENT): Value {
        return when (operator) {
            NORMAL_ASSIGNMENT -> assign(target, eval(value))
            PLUS_ASSIGNMENT -> assign(target, eval(PlusExpr(target, value)))
            MINUS_ASSIGNMENT -> assign(target, eval(MinusExpr(target, value)))
            MULT_ASSIGNMENT -> assign(target, eval(MultExpr(target, value)))
            DIVIDE_ASSIGNMENT -> assign(target, eval(DivExpr(target, value)))
            EXP_ASSIGNMENT -> assign(target, eval(ExpExpr(target, value)))
        }
    }

    private fun move(target: AssignableExpression, value: Expression): Value {
        when (target) {
            is DataRefExpr -> {
                var newValue = interpret(value).takeLast(target.size().toInt())
                if (value.type().size < target.size()) {
                    newValue = get(target.variable.referred!!).takeFirst((target.size() - value.type().size).toInt()).concatenate(newValue)
                }
                return assign(target, newValue)
            }
            else -> TODO()
        }
    }

    // TODO put it outside InternalInterpreter
    fun coerce(value: Value, type: Type): Value {
        // TODO to be completed
        return when (value) {
            is BlanksValue -> {
                when (type) {
                    is StringType -> {
                        blankValue(type.length.toInt())
                    }
                    is ArrayType -> {
                        createArrayValue(type.element, type.nElements) {
                            blankValue(type.element)
                        }
                    }
                    is NumberType -> {
                        if (type.integer) {
                            IntValue.ZERO
                        } else {
                            DecimalValue.ZERO
                        }
                    }
                    else -> TODO("Converting BlanksValue to $type")
                }
            }
            is StringValue -> {
                when (type) {
                    is StringType -> {
                        var s = value.value.padEnd(type.length.toInt(), PAD_CHAR)
                        if (value.value.length > type.length) {
                            s = s.substring(0, type.length.toInt())
                        }
                        return StringValue(s)
                    }
                    is ArrayType -> {
                        createArrayValue(type.element, type.nElements) {
                            // TODO
                            blankValue(type.element)
                        }
                    }
                    // TODO
                    is NumberType -> {
                        if (type.integer) {
                            IntValue(value.value.asLong())
                        } else {
                            TODO(DecimalValue(BigDecimal.valueOf(value.value.asLong(), type.decimalDigits)).toString())
                        }
                    }
                    is BooleanType -> {
                        if ("1" == value.value.trim()) {
                            BooleanValue.TRUE
                        } else {
                            BooleanValue.FALSE
                        }
                    }
                    is DataStructureType -> {
                        TODO("Converting String to $type")
                    }
                    else -> TODO("Converting String to $type")
                }
            }
            is ArrayValue -> {
                when (type) {
                    is StringType -> {
                        return value.asString()
                    }
                    is ArrayType -> {
                        return value
                    }
                    else -> TODO("Converting ArrayValue to $type")
                }
            }
            else -> value
        }
    }

    fun interpret(expression: Expression): Value {
        val value = interpretConcrete(expression)
        log(ExpressionEvaluationLogEntry(expression, value))
        return value
    }

    private fun interpretConcrete(expression: Expression): Value {
        return when (expression) {
            is StringLiteral -> StringValue(expression.value)
            is IntLiteral -> IntValue(expression.value)
            is RealLiteral -> DecimalValue(expression.value)
            is NumberOfElementsExpr -> {
                val value = interpret(expression.value)
                when (value) {
                    is ArrayValue -> value.arrayLength().asValue()
                    else -> throw IllegalStateException("Cannot ask number of elements of $value")
                }
            }
            is DataRefExpr -> get(expression.variable.referred
                    ?: throw IllegalStateException("[${interpretationContext.name}] Unsolved reference ${expression.variable.name} at ${expression.position}"))
            is EqualityExpr -> {
                val left = interpret(expression.left)
                val right = interpret(expression.right)
                return areEquals(left, right).asValue()
            }
            is DifferentThanExpr -> {
                val left = interpret(expression.left)
                val right = interpret(expression.right)
                return (!areEquals(left, right)).asValue()
            }
            is GreaterThanExpr -> {
                val left = interpret(expression.left)
                val right = interpret(expression.right)
                return isGreaterThan(left, right).asValue()
            }
            is GreaterEqualThanExpr -> {
                val left = interpret(expression.left)
                val right = interpret(expression.right)
                return (isGreaterThan(left, right) || areEquals(left, right)).asValue()
            }
            is LessEqualThanExpr -> {
                val left = interpret(expression.left)
                val right = interpret(expression.right)
                return (isEqualOrSmaller(left, right)).asValue()
            }
            is LessThanExpr -> {
                val left = interpret(expression.left)
                val right = interpret(expression.right)
                return isSmallerThan(left, right).asValue()
            }
            is BlanksRefExpr -> {
                return BlanksValue
            }
            is DecExpr -> {
                val decDigits = interpret(expression.decDigits).asInt().value
                val valueAsString = interpret(expression.value).asString().value
                return if (decDigits == 0L) {
                    IntValue(valueAsString.removeNullChars().asLong())
                } else {
                    DecimalValue(BigDecimal(valueAsString))
                }
            }
            is PlusExpr -> {
                val left = interpret(expression.left)
                val right = interpret(expression.right)
                when {
                    left is StringValue && right is StringValue -> {
                        val s = left.valueWithoutPadding + right.valueWithoutPadding
                        StringValue(s)
                    }
                    left is IntValue && right is IntValue -> IntValue(left.value + right.value)
                    else -> throw UnsupportedOperationException("I do not know how to sum $left and $right at ${expression.position}")
                }
            }
            is MinusExpr -> {
                val left = interpret(expression.left)
                val right = interpret(expression.right)
                when {
                    left is IntValue && right is IntValue -> IntValue(left.value - right.value)
                    else -> throw UnsupportedOperationException("I do not know how to sum $left and $right at ${expression.position}")
                }
            }
            is MultExpr -> {
                val left = interpret(expression.left)
                val right = interpret(expression.right)
                when {
                    left is IntValue && right is IntValue -> IntValue(left.value * right.value)
                    else -> throw UnsupportedOperationException("I do not know how to multiply $left and $right at ${expression.position}")
                }
            }
            is CharExpr -> {
                val value = interpret(expression.value)
                return StringValue(render(value).trim())
            }
            is LookupExpr -> {
                val searchValued = interpret(expression.searchedValued)
                val array = interpret(expression.array) as ArrayValue
                val index = array.elements().indexOfFirst {
                    areEquals(it, searchValued)
                }
                return if (index == -1) 0.asValue() else (index + 1).asValue()
            }
            is ArrayAccessExpr -> {
                val arrayValue = interpret(expression.array) as ArrayValue
                val indexValue = interpret(expression.index)
                return arrayValue.getElement(indexValue.asInt().value.toInt())
            }
            is HiValExpr -> return HiValValue
            is TranslateExpr -> {
                val originalChars = eval(expression.from).asString().valueWithoutPadding
                val newChars = eval(expression.to).asString().valueWithoutPadding
                val start = eval(expression.startPos).asInt().value.toInt()
                val s = eval(expression.string).asString().valueWithoutPadding
                val pair = s.divideAtIndex(start - 1)
                var right = pair.second
                val substitutionMap = mutableMapOf<Char, Char>()
                originalChars.forEachIndexed { i, c ->
                    if (newChars.length > i) {
                        substitutionMap[c] = newChars[i]
                    }
                }
                substitutionMap.forEach {
                    right = right.replace(it.key, it.value)
                }
                return StringValue(pair.first + right)
            }
            is LogicalAndExpr -> {
                val left = eval(expression.left).asBoolean().value
                return if (left) {
                    eval(expression.right)
                } else {
                    BooleanValue(false)
                }
            }
            is LogicalOrExpr -> {
                val left = eval(expression.left).asBoolean().value
                return if (left) {
                    BooleanValue(true)
                } else {
                    eval(expression.right)
                }
            }
            is OnRefExpr -> {
                return BooleanValue(true)
            }
            is NotExpr -> {
                return BooleanValue(!eval(expression.base).asBoolean().value)
            }
            is ScanExpr -> {
                var startIndex = 0
                if (expression.start != null) {
                    startIndex = eval(expression.start).asInt().value.toInt()
                }
                val value = eval(expression.value).asString().valueWithoutPadding
                val source = eval(expression.source).asString().valueWithoutPadding
                val result = source.indexOf(value, startIndex)
                return IntValue(if (result == -1) 0 else result.toLong() + 1)
            }
            is SubstExpr -> {
                val length = if (expression.length != null) eval(expression.length).asInt().value.toInt() else null
                val start = eval(expression.start).asInt().value.toInt() - 1
                val originalString = eval(expression.string).asString().value
                return if (length == null) {
                    StringValue(originalString.substring(start))
                } else {
                    StringValue(originalString.substring(start, start + length))
                }
            }
            is LenExpr -> {
                val value = eval(expression.value)
                return when (value) {
                    is StringValue -> value.value.length.asValue()
                    else -> TODO(value.toString())
                }
            }
            is OffRefExpr -> {
                return BooleanValue(false)
            }
            is PredefinedIndicatorExpr -> {
                return predefinedIndicators[expression.index] ?: BooleanValue.FALSE
            }
            is FunctionCall -> {
                val functionToCall = expression.function.name
                val function = systemInterface.findFunction(globalSymbolTable, functionToCall)
                        ?: throw RuntimeException("Function $functionToCall cannot be found")
                // TODO check number and types of params
                val paramsValues = expression.args.map { eval(it) }
                return function.execute(systemInterface, paramsValues, globalSymbolTable)
            }
            is TimeStampExpr -> {
                if (expression.value == null) {
                    return TimeStampValue(Date())
                } else {
                    val evaluated = eval(expression.value)
                    if (evaluated is StringValue) {
                        return TimeStampValue(evaluated.value.asIsoDate())
                    }
                    TODO("TimeStamp parsing: " + evaluated)
                }
            }
            is EditcExpr -> {
                val n = eval(expression.value)
                val format = eval(expression.format)
                if (format !is StringValue) throw UnsupportedOperationException("Required string value, but got $format at ${expression.position}")
                return n.asDecimal().formatAs(format.value, expression.value.type())
            }
            is DiffExpr -> {
                // TODO expression.durationCode
                val v1 = eval(expression.value1)
                val v2 = eval(expression.value2)
                return DecimalValue(BigDecimal(v1.asTimeStamp().value.time - v2.asTimeStamp().value.time))
            }
            is DivExpr -> {
                val v1 = eval(expression.left)
                val v2 = eval(expression.right)
                // TODO check type
                return DecimalValue(BigDecimal(v1.asInt().value / v2.asInt().value))
            }
            is ExpExpr -> {
                val v1 = eval(expression.left)
                val v2 = eval(expression.right)
                return DecimalValue(BigDecimal(Math.pow(v1.asInt().value.toDouble(), v2.asInt().value.toDouble())))
            }
            is TrimrExpr -> {
                // TODO expression.charactersToTrim
                return StringValue(eval(expression.value).asString().value.removeNullChars().trimEnd())
            }
            is TrimExpr -> {
                return StringValue(eval(expression.value).asString().value.removeNullChars().trim())
            }
            is FoundExpr -> {
                if (expression.name == null) {
                    return BooleanValue(lastFound)
                }
                TODO("Line ${expression.position?.line()} - %FOUND expression with file names is not implemented yet")
            }
            else -> TODO(expression.toString())
        }
    }

    fun blankValue(size: Int) = StringValue(" ".repeat(size))

    fun blankValue(dataDefinition: DataDefinition, forceElement: Boolean = false): Value {
        if (forceElement) TODO()
        return blankValue(dataDefinition.type)
    }
}

private fun AbstractDataDefinition.canBeAssigned(value: Value): Boolean {
    return type.canBeAssigned(value)
}

private fun Int.asValue() = IntValue(this.toLong())
private fun Boolean.asValue() = BooleanValue(this)

private fun DecimalValue.formatAs(format: String, type: Type): StringValue {
    fun signumChar() = (if (this.value < BigDecimal.ZERO) "-" else " ")

    fun commas(t: NumberType) = if (t.entireDigits <= 3) 0 else t.entireDigits / 3
    fun points(t: NumberType) = if (t.decimalDigits > 0) 1 else 0

    fun nrOfPunctuationsIn(t: NumberType): Int {
        return commas(t) + points(t)
    }

    fun decimalsFormatString(t: NumberType) = if (t.decimalDigits == 0) "" else "." + "".padEnd(t.decimalDigits, '0')

    fun f1(): String {
        if (type !is NumberType) throw UnsupportedOperationException("Unsupported type for %EDITC: $type")
        val s = DecimalFormat("#,###" + decimalsFormatString(type), DecimalFormatSymbols(Locale.US)).format(this.value.abs())
        return s.padStart(type.size.toInt() + nrOfPunctuationsIn(type))
    }

    fun f2(): String = if (this.value.isZero()) "".padStart(type.size.toInt() + nrOfPunctuationsIn(type as NumberType)) else f1()

    fun f3(): String {
        if (type !is NumberType) throw UnsupportedOperationException("Unsupported type for %EDITC: $type")
        val s = DecimalFormat("#" + decimalsFormatString(type), DecimalFormatSymbols(Locale.US)).format(this.value.abs())
        return s.padStart(type.size.toInt() + points(type))
    }

    fun f4(): String = if (this.value.isZero()) "".padStart(type.size.toInt() + points(type as NumberType)) else f3()

    fun fJ(): String = f1() + signumChar()

    fun fK(): String = f2() + signumChar()

    fun fL(): String = f3() + signumChar()

    fun fM(): String = f4() + signumChar()

    fun fN(): String = signumChar() + f1()

    fun fO(): String = signumChar() + f2()

    fun fP(): String = signumChar() + f3()

    fun fQ(): String = signumChar() + f4()

    fun toBlnk(c: Char) = if (c == '0') ' ' else c

    fun fY(): String {
        var stringN = this.value.abs().unscaledValue().toString().trim()
        return if (stringN.length <= 6) {
            stringN = stringN.padStart(6, '0')
            "${toBlnk(stringN[0])}${stringN[1]}/${stringN[2]}${stringN[3]}/${stringN[4]}${stringN[5]}".padStart(type.size.toInt() + 2)
        } else {
            stringN = stringN.padStart(8, '0')
            "${toBlnk(stringN[0])}${stringN[1]}/${stringN[2]}${stringN[3]}/${stringN[4]}${stringN[5]}${stringN[6]}${stringN[7]}".padStart(type.size.toInt() + 2)
        }
    }

    fun fZ(): String {
        val s = if (this.value.isZero()) {
            ""
        } else {
            this.value.abs().unscaledValue().toString()
        }
        return s.padStart(type.size.toInt())
    }

    return when (format) {
        "1" -> StringValue(f1())
        "2" -> StringValue(f2())
        "3" -> StringValue(f3())
        "4" -> StringValue(f4())
        "J" -> StringValue(fJ())
        "K" -> StringValue(fK())
        "L" -> StringValue(fL())
        "M" -> StringValue(fM())
        "N" -> StringValue(fN())
        "O" -> StringValue(fO())
        "P" -> StringValue(fP())
        "Q" -> StringValue(fQ())
        "Y" -> StringValue(fY())
        "Z" -> StringValue(fZ())
        else -> throw UnsupportedOperationException("Unsupported format for %EDITC: $format")
    }
}

// Useful to interrupt infinite cycles in tests
class InterruptForDebuggingPurposes : RuntimeException()

fun blankValue(type: Type): Value {
    return when (type) {
        is ArrayType -> createArrayValue(type.element, type.nElements) {
            blankValue(type.element)
        }
        is DataStructureType -> StringValue.blank(type.size.toInt())
        is StringType -> StringValue.blank(type.size.toInt())
        is NumberType -> IntValue(0)
        is BooleanType -> BooleanValue(false)
        is TimeStampType -> TimeStampValue.LOVAL
    }
}
