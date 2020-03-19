package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.parsing.ast.AssignmentOperator.*
import com.smeup.rpgparser.parsing.parsetreetoast.LogicalCondition
import com.smeup.rpgparser.parsing.parsetreetoast.MuteAnnotationExecutionLogEntry
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import com.smeup.rpgparser.utils.*
import com.smeup.rpgparser.utils.ComparisonOperator.*
import com.strumenta.kolasu.model.ancestor
import java.lang.System.currentTimeMillis
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeoutException
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap
import kotlin.system.measureTimeMillis

object InterpreterConfiguration {
    /**
     * Enable runtime checks during assignments
     */
    var enableRuntimeChecksOnAssignement = false
}

interface InterpretationContext {
    val currentProgramName: String
    fun setDataWrapUpPolicy(dataWrapUpChoice: DataWrapUpChoice)
    fun shouldReinitialize(): Boolean
}

object DummyInterpretationContext : InterpretationContext {
    override val currentProgramName: String
        get() = "<UNSPECIFIED>"

    override fun shouldReinitialize() = false

    override fun setDataWrapUpPolicy(dataWrapUpChoice: DataWrapUpChoice) {
        // nothing to do
    }
}

val ALL_PREDEFINED_INDEXES = 1..99

class InternalInterpreter(val systemInterface: SystemInterface) : InterpreterCore {
    private val globalSymbolTable = SymbolTable()
    private val predefinedIndicators = HashMap<IndicatorKey, BooleanValue>()
    // TODO default value DECEDIT can be changed
    var decedit: String = "."
    // TODO default value CHARSET can be changed
    val charset = Charset.forName("Cp037")

    var interpretationContext: InterpretationContext = DummyInterpretationContext
    private val klists = HashMap<String, List<String>>()

    private var logHandlers: List<InterpreterLogHandler> = emptyList()

    private var lastFound = false
    private var lastDBFile: DBFile? = null

    private val dbFileMap = DBFileMap(systemInterface.db)

    override fun log(logEntry: () -> LogEntry) {
        if (!logHandlers.isEmpty()) logHandlers.log(logEntry())
    }

    private fun exists(dataName: String) = globalSymbolTable.contains(dataName)

    private fun dataDefinitionByName(name: String) = globalSymbolTable.dataDefinitionByName(name)

    override operator fun get(data: AbstractDataDefinition): Value {
        return globalSymbolTable[data]
    }

    operator fun get(dataName: String) = globalSymbolTable[dataName]

    operator fun set(data: AbstractDataDefinition, value: Value) {
        require(data.canBeAssigned(value)) {
            "${data.name} of type ${data.type} defined at line ${data.position.line()} cannot be assigned the value $value"
        }

        when (data) {
            // Field are stored within the Data Structure definition
            is FieldDefinition -> {
                val ds = data.parent as DataDefinition
                if (data.declaredArrayInLine != null) {
                    val dataStructValue = get(ds.name) as DataStructValue
                    var startOffset = data.startOffset
                    var size = data.endOffset - data.startOffset

                    // for (i in 1..data.declaredArrayInLine!!) {
                    // If the size of the arrays are different
                    val maxElements = Math.min(value.asArray().arrayLength(), data.declaredArrayInLine!!)
                    for (i in 1..maxElements) {
                        // Added coerce
                        val valueToAssign = coerce(value.asArray().getElement(i), data.type.asArray().element)
                        dataStructValue.setSubstring(startOffset, startOffset + size,
                                data.type.asArray().element.toDataStructureValue(valueToAssign))
                        startOffset += data.stepSize.toInt()
                    }
                } else {
                    when (val containerValue = get(ds.name)) {
                        is ArrayValue -> {
                            val valuesToAssign = value as ArrayValue
                            require(containerValue.arrayLength() == valuesToAssign.arrayLength())
                            // The container value is an array of datastructurevalues
                            // we assign to each data structure the corresponding field value
                            for (i in 1..containerValue.arrayLength()) {
                                val dataStructValue = containerValue.getElement(i) as DataStructValue
                                dataStructValue.setSingleField(data, valuesToAssign.getElement(i))
                            }
                        }
                        is DataStructValue -> {
                            containerValue.set(data, value)
                        }
                        else -> TODO()
                    }
                }
            }
            else -> {
                var previous: Value? = null
                if (data.name in globalSymbolTable) {
                    previous = globalSymbolTable[data.name]
                }
                log { AssignmentLogEntry(this.interpretationContext.currentProgramName, data, value, previous) }
                globalSymbolTable[data] = coerce(value, data.type)
            }
        }
    }

    private fun initialize(
        compilationUnit: CompilationUnit,
        initialValues: Map<String, Value>,
        reinitialization: Boolean = true
    ) {
        // TODO verify if these values should be reinitialised or not
        compilationUnit.fileDefinitions.forEach {
            dbFileMap.add(it)
        }

        // Assigning initial values received from outside and consider INZ clauses
        if (reinitialization) {
            compilationUnit.allDataDefinitions.forEach {
                var value: Value? = null
                if (it is DataDefinition) {
                    value = when {
                        it.name in initialValues -> {
                            val initialValue = initialValues[it.name]
                                    ?: throw RuntimeException("Initial values for ${it.name} not found")
                            if (InterpreterConfiguration.enableRuntimeChecksOnAssignement) {
                                require(initialValue.assignableTo(it.type)) {
                                    "Initial value for ${it.name} is not compatible. Passed $initialValue, type: ${it.type}"
                                }
                            }
                            initialValue
                        }
                        it.initializationValue != null -> interpret(it.initializationValue)
                        it.isCompileTimeArray() -> toArrayValue(
                            compilationUnit.compileTimeArray(it.name),
                            (it.type as ArrayType)
                        )
                        else -> blankValue(it)
                    }
                    if (it.name !in initialValues) {
                        blankValue(it)
                        it.fields.forEach { field ->
                            if (field.initializationValue != null) {
                                val fieldValue = coerce(interpret(field.initializationValue), field.type)
                                (value as DataStructValue).set(field, fieldValue)
                            }
                        }
                    }
                } else if (it is InStatementDataDefinition && it.parent is PlistParam) {
                    value = when {
                        it.name in initialValues -> initialValues[it.name]
                            ?: throw RuntimeException("Initial values for ${it.name} not found")
                        else -> null
                    }
                }
                // Fix issue on CTDATA
                val ctdata = compilationUnit.compileTimeArray(it.name)
                if (ctdata.name == it.name) {
                    val xx = toArrayValue(
                            compilationUnit.compileTimeArray(it.name),
                            (it.type as ArrayType))
                    set(it, xx)
                }

                if (value != null) {
                    set(it, coerce(value, it.type))
                    executeMutes(it.muteAnnotations, compilationUnit, "(data definition)")
                }
            }
        } else {
            initialValues.forEach { iv ->
                val def = compilationUnit.allDataDefinitions.find { it.name.equals(iv.key, ignoreCase = true) }!!
                set(def, coerce(iv.value, def.type))
            }
        }
    }

    private fun toArrayValue(compileTimeArray: CompileTimeArray, arrayType: ArrayType): Value {
        // It is not clear why the compileTimeRecordsPerLine on the array type is null
        // probably it is an error during the ast processing.
        // as workaround, if null assumes the number of lines in the compile compileTimeArray
        // as value for compileTimeRecordsPerLine
        var lines = if (arrayType.compileTimeRecordsPerLine == null) compileTimeArray.lines.size else arrayType.compileTimeRecordsPerLine
        val l: MutableList<Value> =
            compileTimeArray.lines.chunkAs(lines, arrayType.element.size)
                .map {
                    coerce(StringValue(it), arrayType.element)
                }
                .resizeTo(arrayType.nElements, arrayType.element.blank())
                .toMutableList()

        return ConcreteArrayValue(l, arrayType.element)
    }

    fun simplyInitialize(compilationUnit: CompilationUnit, initialValues: Map<String, Value>) {
        initialize(compilationUnit, initialValues)
    }

    private fun configureLogHandlers() {
        logHandlers = systemInterface.getAllLogHandlers()
    }

    fun execute(
        compilationUnit: CompilationUnit,
        initialValues: Map<String, Value>,
        reinitialization: Boolean = true
    ) {
        configureLogHandlers()

        initialize(compilationUnit, caseInsensitiveMap(initialValues), reinitialization)

        if (compilationUnit.minTimeOut == null) {
            execute(compilationUnit.main.stmts)
        } else {
            val elapsedTime = measureTimeMillis {
                execute(compilationUnit.main.stmts)
            }
            if (elapsedTime > compilationUnit.minTimeOut!!) throw TimeoutException("Execution took $elapsedTime millis, but there was a ${compilationUnit.minTimeOut} millis timeout")
        }
    }

    private fun GotoException.indexOfTaggedStatement(statements: List<Statement>): Int =
        statements.indexOfFirst {
            it is TagStmt && it.tag.equals(tag, true)
        }

    private fun caseInsensitiveMap(aMap: Map<String, Value>): Map<String, Value> {
        val result = TreeMap<String, Value>(String.CASE_INSENSITIVE_ORDER)
        result.putAll(aMap)
        return result
    }

    private fun execute(statements: List<Statement>) {
        try {
            var i = 0
            while (i < statements.size) {
                try {
                    executeWithMute(statements[i++])
                } catch (e: GotoException) {
                    i = e.indexOfTaggedStatement(statements)
                    if (i < 0 || i >= statements.size) throw e
                }
            }
        } catch (e: ReturnException) {
            // TODO use return value
        }
    }

    private fun executeWithMute(statement: Statement) {
        log { LineLogEntry(this.interpretationContext.currentProgramName, statement) }
        try {
            execute(statement)
        } finally {
            executeMutes(
                statement.muteAnnotations,
                statement.ancestor(CompilationUnit::class.java)!!,
                statement.position.line()
            )
        }
    }

    private fun executeMutes(muteAnnotations: MutableList<MuteAnnotation>, compilationUnit: CompilationUnit, line: String) {
        muteAnnotations.forEach {
            it.resolveAndValidate(compilationUnit)
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
                    require(value is BooleanValue) {
                        "Expected BooleanValue, but found $value"
                    }

                    log { MuteAnnotationExecutionLogEntry(this.interpretationContext.currentProgramName, it, value) }
                    systemInterface.addExecutedAnnotation(
                        it.position!!.start.line,
                        MuteComparisonAnnotationExecuted(
                            this.interpretationContext.currentProgramName,
                            exp,
                            it.val1,
                            it.val2,
                            value,
                            value1,
                            value2,
                            line
                        )
                    )
                }
                is MuteTypeAnnotation -> {
                    // Skip
                }
                is MuteTimeoutAnnotation -> {
                    // Skip
                }
                is MuteFailAnnotation -> {
                    val message = interpretConcrete(it.message)
                    log { MuteAnnotationExecutionLogEntry(this.interpretationContext.currentProgramName, it, message) }
                    systemInterface.addExecutedAnnotation(
                        it.position!!.start.line,
                        MuteFailAnnotationExecuted(
                            this.interpretationContext.currentProgramName,
                            message,
                            line
                        )
                    )
                }
                else -> throw UnsupportedOperationException("Unknown type of annotation: $it")
            }
        }
    }

    private fun IndicatorCondition?.shouldExecuteStatement(): Boolean {
        if (this == null) return true
        val indicator = indicator(key).value
        return if (negate) !indicator else indicator
    }

    data class SolvedIndicatorCondition(val key: Int, val value: Boolean, val operator: String)

    private fun Statement.solveIndicatorValues(): List<SolvedIndicatorCondition> =
        this.continuedIndicators.map { (indicatorKey, continuedIndicator) ->
            val indicator = indicator(indicatorKey).value
            var condition: Boolean = if (continuedIndicator.negate) !indicator else indicator
            val solvedIndicatorCondition = SolvedIndicatorCondition(indicatorKey, condition, continuedIndicator.level)
            solvedIndicatorCondition
        }

    private fun getMapOfORs(indicatorValues: List<SolvedIndicatorCondition>): ArrayList<ArrayList<Boolean>> {
        val mapOfORs = ArrayList<ArrayList<Boolean>>()
        val reversed = indicatorValues.reversed()
        var previousOperator: String = ""
        var loops = 0
        var idxOfMapOfANDs = 0
        reversed.forEach { solvedIndicator ->
            if (loops == 0) {
                mapOfORs.add(ArrayList<Boolean>())
                mapOfORs.get(idxOfMapOfANDs).add(solvedIndicator.value)
            } else {
                if (previousOperator == "AND") {
                    mapOfORs.get(idxOfMapOfANDs).add(solvedIndicator.value)
                } else {
                    mapOfORs.add(ArrayList<Boolean>())
                    idxOfMapOfANDs++
                    mapOfORs.get(idxOfMapOfANDs).add(solvedIndicator.value)
                }
            }
            previousOperator = solvedIndicator.operator
            loops++
        }
        return mapOfORs
    }

    private fun Statement.shouldBeExecuted(): Boolean = this.indicatorCondition.shouldExecuteStatement()

    private fun Statement.isStatementExecutable(mapOfORs: ArrayList<ArrayList<Boolean>>): Boolean {
        var isExecutable = false
        // True if at least one of "mapOfANDs" relations contains only true values
        // loop through "mapOfORs" relations
        for (mapOfANDs in mapOfORs) {
            // loop through map of "AND" relations
            for (b in mapOfANDs) {
                if (!b) {
                    isExecutable = false
                    break
                }
                isExecutable = true
            }
            if (isExecutable) break
        }
        // Empty mapOfORs means no leftIndicator
        if (mapOfORs.isEmpty()) {
            isExecutable = this.shouldBeExecuted()
        }
        return isExecutable
    }

    private fun execute(statement: Statement) {
        try {
            if (!statement.isStatementExecutable(getMapOfORs(statement.solveIndicatorValues()))) return
            when (statement) {
                is ExecuteSubroutine -> {
                    log {
                        SubroutineExecutionLogStart(
                            this.interpretationContext.currentProgramName,
                            statement.subroutine.referred!!
                        )
                    }
                    val elapsed = measureTimeMillis {
                        try {
                            execute(statement.subroutine.referred!!.stmts)
                        } catch (e: LeaveSrException) {
                            // Nothing to do here
                        } catch (e: GotoException) {
                            if (!e.tag.equals(statement.subroutine.referred!!.tag, true)) throw e
                        }
                    }
                    log {
                        SubroutineExecutionLogEnd(
                            this.interpretationContext.currentProgramName,
                            statement.subroutine.referred!!,
                            elapsed
                        )
                    }
                }
                is EvalStmt -> {
                    // Should I assign it one by one?
                    val result = if (statement.target.type().isArray() &&
                            statement.target.type().asArray().element.canBeAssigned(statement.expression.type())) {
                        assignEachElement(statement.target, statement.expression, statement.operator)
                    } else {
                        assign(statement.target, statement.expression, statement.operator)
                    }
                    log { EvaluationLogEntry(this.interpretationContext.currentProgramName, statement, result) }
                }
                is MoveStmt -> {
                    val value = move(statement.target, statement.expression, this)
                    log { MoveStatemenExecutionLog(this.interpretationContext.currentProgramName, statement, value) }
                }
                is MoveAStmt -> {
                    val value = movea(statement.operationExtender, statement.target, statement.expression, this)
                    log { MoveAStatemenExecutionLog(this.interpretationContext.currentProgramName, statement, value) }
                }
                is MoveLStmt -> {
                    val value = movel(statement.operationExtender, statement.target, statement.expression, this)
                    log { MoveLStatemenExecutionLog(this.interpretationContext.currentProgramName, statement, value) }
                }
                is SelectStmt -> {
                    for (case in statement.cases) {
                        val result = interpret(case.condition)

                        log { SelectCaseExecutionLogEntry(this.interpretationContext.currentProgramName, case, result) }
                        if (result.asBoolean().value) {
                            execute(case.body)
                            return
                        }
                    }
                    if (statement.other != null) {
                        log {
                            SelectOtherExecutionLogEntry(
                                this.interpretationContext.currentProgramName,
                                statement.other!!
                            )
                        }
                        execute(statement.other!!.body)
                    }
                }
                is SetStmt -> {
                    statement.indicators.forEach {
                        when (it) {
                            is DataWrapUpIndicatorExpr -> interpretationContext.setDataWrapUpPolicy(it.dataWrapUpChoice)
                            is PredefinedIndicatorExpr -> {
                                if (statement.valueSet.name == "ON") {
                                    predefinedIndicators[it.index] = BooleanValue.TRUE
                                } else {
                                    predefinedIndicators[it.index] = BooleanValue.FALSE
                                }
                            }
                            else -> TODO()
                        }
                    }
                }
                is PlistStmt -> {
                    statement.params.forEach {
                        if (globalSymbolTable.contains(it.param.name)) {
                            val value = globalSymbolTable[it.param.name]
                            log {
                                ParamListStatemenExecutionLog(
                                    this.interpretationContext.currentProgramName,
                                    statement,
                                    it.param.name,
                                    value
                                )
                            }
                        }
                    }
                } /* Nothing to do here */
                is KListStmt -> {
                    // TODO Add logging as for PlistStmt
                    klists[statement.name] = statement.fields
                }
                is ClearStmt -> {
                    return when (statement.value) {
                        is DataRefExpr -> {
                            val value = assign(statement.value, BlanksRefExpr())
                            log {
                                ClearStatemenExecutionLog(
                                    this.interpretationContext.currentProgramName,
                                    statement,
                                    value
                                )
                            }
                            Unit
                        }
                        is PredefinedIndicatorExpr -> {
                            val value = assign(statement.value, BlanksRefExpr())
                            log {
                                ClearStatemenExecutionLog(
                                    this.interpretationContext.currentProgramName,
                                    statement,
                                    value
                                )
                            }
                        }
                        else -> throw UnsupportedOperationException("I do not know how to clear ${statement.value}")
                    }
                }
                is ZAddStmt -> {
                    assign(statement.target, eval(statement.expression))
                }
                is AddStmt -> {
                    assign(statement.result, add(statement))
                }
                is ZSubStmt -> {
                    val value = eval(statement.expression)
                    require(value is NumberValue) {
                        "$value should be a number"
                    }
                    assign(statement.target, value.negate())
                }
                is MultStmt -> {
                    assign(statement.target, mult(statement))
                }
                is DivStmt -> {
                    assign(statement.target, div(statement))
                }
                is SubStmt -> {
                    assign(statement.result, sub(statement))
                }
                is TimeStmt -> {
                    when (statement.value) {
                        is DataRefExpr -> {
                            assign(statement.value, TimeStampValue(Date()))
                        }
                        else -> throw UnsupportedOperationException("I do not know how to set TIME to ${statement.value}")
                    }
                }
                is DisplayStmt -> {
                    val values = mutableListOf<Value>()
                    statement.factor1?.let { values.add(interpret(it)) }
                    statement.response?.let { values.add(interpret(it)) }
                    // TODO: receive input from systemInterface and assign value to response
                    systemInterface.display(rawRender(values))
                }
                is IfStmt -> {
                    val condition = eval(statement.condition)
                    log { IfExecutionLogEntry(this.interpretationContext.currentProgramName, statement, condition) }
                    if (condition.asBoolean().value) {
                        execute(statement.body)
                    } else {
                        for (elseIfClause in statement.elseIfClauses) {
                            val c = eval(elseIfClause.condition)
                            log { ElseIfExecutionLogEntry(this.interpretationContext.currentProgramName, elseIfClause, c) }
                            if (c.asBoolean().value) {
                                execute(elseIfClause.body)
                                return
                            }
                        }
                        if (statement.elseClause != null) {
                            log {
                                ElseExecutionLogEntry(
                                    this.interpretationContext.currentProgramName,
                                    statement.elseClause,
                                    condition
                                )
                            }
                            execute(statement.elseClause.body)
                        }
                    }
                }
                is CallStmt -> {
                    log { CallExecutionLogEntry(this.interpretationContext.currentProgramName, statement) }
                    val programToCall = eval(statement.expression).asString().value
                    val program = systemInterface.findProgram(programToCall)
                    require(program != null) {
                        "Line: ${statement.position.line()} - Program $programToCall cannot be found"
                    }

                    val params = statement.params.mapIndexed { index, it ->
                        if (it.dataDefinition != null) {
                            if (it.dataDefinition.initializationValue != null) {
                                if (!exists(it.param.name)) {
                                    assign(it.dataDefinition, eval(it.dataDefinition.initializationValue))
                                } else {
                                    assign(
                                            dataDefinitionByName(it.param.name)!!,
                                            eval(it.dataDefinition.initializationValue)
                                    )
                                }
                            } else {
                                if (!exists(it.param.name)) {
                                    assign(it.dataDefinition, eval(BlanksRefExpr()))
                                }
                            }
                        }
                        require(program.params().size > index) {
                            "Line: ${statement.position.line()} - Parameter nr. ${index + 1} can't be found"
                        }
                        program.params()[index].name to get(it.param.name)
                    }.toMap(LinkedHashMap())

                    val startTime = currentTimeMillis()
                    val paramValuesAtTheEnd =
                            try {
                                systemInterface.registerProgramExecutionStart(program, params)
                                program.execute(systemInterface, params).apply {
                                    log { CallEndLogEntry("", statement, currentTimeMillis() - startTime) }
                                }
                            } catch (e: Exception) { // TODO Catch a more specific exception?
                                log { CallEndLogEntry("", statement, currentTimeMillis() - startTime) }
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
                    var loopCounter: Long = 0
                    val startTime = currentTimeMillis()

                    eval(statement.init)

                    try {
                        log { ForStatementExecutionLogStart(this.interpretationContext.currentProgramName, statement) }
                        while (enterCondition(
                                        this[statement.iterDataDefinition()],
                                        eval(statement.endValue),
                                        statement.downward
                                )
                        ) {
                            try {
                                execute(statement.body)
                            } catch (e: IterException) {
                                // nothing to do here
                            }

                            increment(statement.iterDataDefinition(), step(statement.byValue, statement.downward))
                            loopCounter++
                        }
                        val elapsed = currentTimeMillis() - startTime
                        log {
                            ForStatementExecutionLogEnd(
                                this.interpretationContext.currentProgramName,
                                statement,
                                elapsed,
                                loopCounter
                            )
                        }
                    } catch (e: LeaveException) {
                        // leaving
                        val elapsed = currentTimeMillis() - startTime
                        log {
                            ForStatementExecutionLogEnd(
                                this.interpretationContext.currentProgramName,
                                statement,
                                elapsed,
                                loopCounter
                            )
                        }
                    }
                }
                is DoStmt -> {
                    var loopCounter: Long = 0
                    val startTime = currentTimeMillis()
                    val endLimitExpression = statement.endLimit
                    val endLimit: () -> Long = optimizedIntExpression(endLimitExpression)
                    if (statement.index == null) {
                        var myIterValue = eval(statement.startLimit).asInt().value
                        try {
                            log { DoStatemenExecutionLogStart(this.interpretationContext.currentProgramName, statement) }
                            while (myIterValue <= endLimit()) {
                                try {
                                    execute(statement.body)
                                } catch (e: IterException) {
                                    // nothing to do here
                                }
                                loopCounter++
                                myIterValue++
                            }
                            val elapsed = currentTimeMillis() - startTime
                            log {
                                DoStatemenExecutionLogEnd(
                                    this.interpretationContext.currentProgramName,
                                    statement,
                                    elapsed,
                                    loopCounter
                                )
                            }
                        } catch (e: LeaveException) {
                            // nothing to do here
                            val elapsed = currentTimeMillis() - startTime
                            log {
                                DoStatemenExecutionLogEnd(
                                    this.interpretationContext.currentProgramName,
                                    statement,
                                    elapsed,
                                    loopCounter
                                )
                            }
                        }
                    } else {
                        assign(statement.index, statement.startLimit)
                        try {
                            val indexExpression = optimizedIntExpression(statement.index)
                            while (indexExpression() <= endLimit()) {
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
                    var loopCounter: Long = 0
                    val startTime = currentTimeMillis()
                    try {
                        log { DowStatemenExecutionLogStart(this.interpretationContext.currentProgramName, statement) }
                        while (eval(statement.endExpression).asBoolean().value) {
                            execute(statement.body)
                            loopCounter++
                        }
                        val elapsed = currentTimeMillis() - startTime
                        log {
                            DowStatemenExecutionLogEnd(
                                this.interpretationContext.currentProgramName,
                                statement,
                                elapsed,
                                loopCounter
                            )
                        }
                    } catch (e: LeaveException) {
                        val elapsed = currentTimeMillis() - startTime
                        log {
                            DowStatemenExecutionLogEnd(
                                this.interpretationContext.currentProgramName,
                                statement,
                                elapsed,
                                loopCounter
                            )
                        }
                    }
                }
                is DouStmt -> {
                    var loopCounter: Long = 0
                    val startTime = currentTimeMillis()
                    try {
                        log { DouStatemenExecutionLogStart(this.interpretationContext.currentProgramName, statement) }
                        do {
                            execute(statement.body)
                            loopCounter++
                        } while (!eval(statement.endExpression).asBoolean().value)
                        val elapsed = currentTimeMillis() - startTime
                        log {
                            DouStatemenExecutionLogEnd(
                                this.interpretationContext.currentProgramName,
                                statement,
                                elapsed,
                                loopCounter
                            )
                        }
                    } catch (e: LeaveException) {
                        val elapsed = currentTimeMillis() - startTime
                        log {
                            DouStatemenExecutionLogEnd(
                                this.interpretationContext.currentProgramName,
                                statement,
                                elapsed,
                                loopCounter
                            )
                        }
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
                            val newValue =
                                    (minuend.asTimeStamp().value.time - subtrahend.asTimeStamp().value.time) * 1000
                            assign(statement.target, IntValue(newValue))
                        }
                        else -> throw UnsupportedOperationException("Data reference required: " + statement)
                    }
                }
                is LeaveStmt -> {
                    log { LeaveStatemenExecutionLog(this.interpretationContext.currentProgramName, statement) }
                    throw LeaveException()
                }
                is LeaveSrStmt -> {
                    log { LeaveSrStatemenExecutionLog(this.interpretationContext.currentProgramName, statement) }
                    throw LeaveSrException()
                }
                is IterStmt -> {
                    log { IterStatemenExecutionLog(this.interpretationContext.currentProgramName, statement) }
                    throw IterException()
                }
                is CheckStmt -> {
                    var baseString = interpret(statement.baseString).asString().value
                    if (statement.baseString is DataRefExpr) {
                        baseString = baseString.padEnd(statement.baseString.size())
                    }
                    val charSet = interpret(statement.comparatorString).asString().value
                    val wrongIndex = statement.wrongCharPosition
                    lastFound = false
                    if (wrongIndex != null) {
                        assign(wrongIndex, IntValue.ZERO)
                    }
                    baseString.substring(statement.start - 1).forEachIndexed { i, c ->
                        if (!charSet.contains(c)) {
                            if (wrongIndex != null) {
                                assign(wrongIndex, IntValue((i + statement.start).toLong()))
                            }
                            lastFound = true
                            return
                        }
                    }
                }
                is ChainStmt -> {
                    val dbFile = dbFile(statement.name, statement)
                    val record = if (statement.searchArg.type() is KListType) {
                        dbFile.chain(toSearchValues(statement.searchArg))
                    } else {
                        dbFile.chain(eval(statement.searchArg))
                    }
                    fillDataFrom(record)
                }
                is SetllStmt -> {
                    val dbFile = dbFile(statement.name, statement)
                    lastFound = if (statement.searchArg.type() is KListType) {
                        dbFile.setll(toSearchValues(statement.searchArg))
                    } else {
                        dbFile.setll(eval(statement.searchArg))
                    }
                }
                is ReadEqualStmt -> {
                    val dbFile = dbFile(statement.name, statement)
                    val record = when {
                        statement.searchArg == null -> dbFile.readEqual()
                        statement.searchArg.type() is KListType -> dbFile.readEqual(toSearchValues(statement.searchArg))
                        else -> dbFile.readEqual(eval(statement.searchArg))
                    }
                    fillDataFrom(record)
                }
                is ReadPreviousStmt -> {
                    val dbFile = dbFile(statement.name, statement)
                    val record = when {
                        statement.searchArg == null -> dbFile.readPrevious()
                        statement.searchArg.type() is KListType -> dbFile.readPrevious(toSearchValues(statement.searchArg))
                        else -> dbFile.readPrevious(eval(statement.searchArg))
                    }
                    fillDataFrom(record)
                }
                is ReadStmt -> {
                    val dbFile = dbFile(statement.name, statement)
                    val record = dbFile.read()
                    fillDataFrom(record)
                }
                is ReturnStmt -> {
                    val returnValue = statement.expression?.let { eval(statement.expression) }
                    throw ReturnException(returnValue)
                }
                is DefineStmt -> {
                    // Nothing to do here
                }
                is TagStmt -> {
                    // Nothing to do here
                }
                is GotoStmt -> {
                    throw GotoException(statement.tag)
                }
                is CabStmt -> {
                    val comparisonResult = statement.comparison.verify(statement.factor1, statement.factor2, this, charset)
                    when (comparisonResult.comparison) {
                        Comparison.GREATER -> setPredefinedIndicators(statement, BooleanValue.TRUE, BooleanValue.FALSE, BooleanValue.FALSE)
                        Comparison.SMALLER -> setPredefinedIndicators(statement, BooleanValue.FALSE, BooleanValue.TRUE, BooleanValue.FALSE)
                        Comparison.EQUAL -> setPredefinedIndicators(statement, BooleanValue.FALSE, BooleanValue.FALSE, BooleanValue.TRUE)
                    }
                    if (comparisonResult.isVerified) throw GotoException(statement.tag)
                }
                is SortAStmt -> {
                    sortA(interpret(statement.target), charset)
                }
                is CatStmt -> {
                    val blanksInBetween = statement.blanksInBetween
                    val blanks = StringValue.blank(blanksInBetween)
                    val factor2 = interpret(statement.right)
                    var result = interpret(statement.target)
                    val resultLen = result.asString().length()
                    var concatenatedFactors: Value

                    if (null != statement.left) {
                        val factor1 = interpret(statement.left)
                        val f1Trimmed = (factor1 as StringValue).value.trim()
                        val factor1Trimmed = StringValue(f1Trimmed)
                        concatenatedFactors = if (blanksInBetween > 0) {
                            factor1Trimmed.concatenate(blanks).concatenate(factor2)
                        } else {
                            factor1.concatenate(factor2)
                        }
                    } else {
                        concatenatedFactors = if (!result.asString().isBlank()) {
                            result
                        } else if (blanksInBetween > 0) {
                            if (blanksInBetween >= resultLen) {
                                result
                            } else {
                                blanks.concatenate(factor2)
                            }
                        } else {
                            result
                        }
                    }
                    val concatenatedFactorsLen = concatenatedFactors.asString().length()
                    result = if (concatenatedFactorsLen >= resultLen) {
                        concatenatedFactors.asString().getSubstring(0, resultLen)
                    } else {
                        concatenatedFactors.concatenate(result.asString().getSubstring(concatenatedFactorsLen, resultLen))
                    }

                    assign(statement.target, result)
                    log { CatStatementExecutionLog(this.interpretationContext.currentProgramName, statement, eval(statement.target)) }
                }
                is CompStmt -> {
                    when (this.compareExpressions(statement.left, statement.right, charset)) {
                        Comparison.GREATER -> setPredefinedIndicators(statement, BooleanValue.TRUE, BooleanValue.FALSE, BooleanValue.FALSE)
                        Comparison.SMALLER -> setPredefinedIndicators(statement, BooleanValue.FALSE, BooleanValue.TRUE, BooleanValue.FALSE)
                        Comparison.EQUAL -> setPredefinedIndicators(statement, BooleanValue.FALSE, BooleanValue.FALSE, BooleanValue.TRUE)
                    }
                }
                is LookupStmt -> {
                    lookUp(statement, this, charset)
                }
                is XFootStmt -> {
                    xfoot(statement, this)
                }

                else -> TODO(statement.toString())
            }
        } catch (e: ControlFlowException) {
            throw e
        } catch (e: IllegalArgumentException) {
            val message = e.toString()
            if (!message.contains(statement.position.line())) {
                throw IllegalArgumentException(errorDescription(statement, e), e)
            }
            throw e
        } catch (e: NotImplementedError) {
            throw RuntimeException(errorDescription(statement, e), e)
        } catch (e: RuntimeException) {
            throw RuntimeException(errorDescription(statement, e), e)
        }
    }

    private fun optimizedIntExpression(expression: Expression): () -> Long =
        if (expression is IntLiteral || expression is FigurativeConstantRef) {
            val constValue = eval(expression).asInt().value
            { constValue }
        } else {
            { eval(expression).asInt().value }
        }

    override fun setPredefinedIndicators(statement: WithRightIndicators, hi: BooleanValue, lo: BooleanValue, eq: BooleanValue) {
        statement.hi?.let {
            predefinedIndicators[it] = hi
        }
        statement.lo?.let {
            predefinedIndicators[it] = lo
        }
        statement.eq?.let {
            predefinedIndicators[it] = eq
        }
    }

    private fun errorDescription(statement: Statement, throwable: Throwable) =
        "Program ${interpretationContext.currentProgramName} - ${statement.simpleDescription()} ${throwable.message}"

    private fun fillDataFrom(record: Record) {
        if (!record.isEmpty()) {
            lastFound = true
            record.forEach { assign(dataDefinitionByName(it.key)!!, it.value) }
        } else {
            lastFound = false
        }
    }

    private fun dbFile(name: String, statement: Statement): DBFile {
        val dbFile = dbFileMap[name]
        require(dbFile != null) {
            "Line: ${statement.position.line()} - File definition $name not found"
        }
        lastDBFile = dbFile
        return dbFile
    }

    private fun toSearchValues(searchArgExpression: Expression): List<RecordField> {
        val kListName = searchArgExpression.render().toUpperCase()
        val parms = klists[kListName]
        return parms!!.map { RecordField(it, get(it)) }
    }

    private fun enterCondition(index: Value, end: Value, downward: Boolean): Boolean =
        if (downward) {
            isEqualOrGreater(index, end, charset)
        } else {
            isEqualOrSmaller(index, end, charset)
        }

    private fun step(byValue: Expression, downward: Boolean): Long {
        val sign = if (downward) {
            -1
        } else {
            1
        }
        return eval(byValue).asInt().value * sign
    }

    private fun increment(dataDefinition: AbstractDataDefinition, amount: Long = 1) {
        val value = this[dataDefinition]
        if (value is IntValue) {
            this[dataDefinition] = IntValue(value.value + amount)
        } else {
            throw UnsupportedOperationException()
        }
    }

    private fun rawRender(values: List<Value>) = values.map { rawRender(it) }.joinToString("")

    private fun rawRender(value: Value): String {
        return when (value) {
            is NumberValue -> if (value.isNegative()) "${value.abs().render()}-" else value.render()
            else -> render(value)
        }
    }

    private fun render(value: Value): String {
        return when (value) {
            is StringValue -> value.value.trimEnd()
            is BooleanValue -> value.asString().value // TODO check if it's the best solution
            is NumberValue -> value.render()
            is ArrayValue -> "[${value.elements().map { render(it) }.joinToString(", ")}]"
            is TimeStampValue -> SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").format(value.value)
            is DataStructValue -> value.value.trimEnd()
            is ZeroValue -> "0"
            is AllValue -> value.charsToRepeat
            else -> TODO("Unable to render value $value (${value.javaClass.canonicalName})")
        }
    }

    override fun eval(expression: Expression): Value {
        return when (expression) {
            is AssignmentExpr -> {
                assign(expression.target, expression.value)
            }
            else -> interpret(expression)
        }
    }

    private fun mult(statement: MultStmt): Value {
        // TODO When will pass my PR for more robustness replace Value.render with NumericValue.bigDecimal
        require(statement.target is DataRefExpr)
        val rightValue: BigDecimal = if (statement.factor1 != null) {
            BigDecimal(interpret(statement.factor1).render())
        } else {
            BigDecimal(get(statement.target.variable.referred!!).render())
        }
        val leftValue = BigDecimal(interpret(statement.factor2).render())
        val result = rightValue.multiply(leftValue)
        val type = statement.target.variable.referred!!.type
        require(type is NumberType)
        return if (statement.halfAdjust) {
            DecimalValue(result.setScale(type.decimalDigits, RoundingMode.HALF_UP))
        } else {
            DecimalValue(result.setScale(type.decimalDigits, RoundingMode.DOWN))
        }
    }

    private fun div(statement: DivStmt): Value {
        // TODO When will pass my PR for more robustness replace Value.render with NumericValue.bigDecimal
        require(statement.target is DataRefExpr)
        val dividend: BigDecimal = if (statement.factor1 != null) {
            BigDecimal(interpret(statement.factor1).render())
        } else {
            BigDecimal(get(statement.target.variable.referred!!).render())
        }
        val divisor = BigDecimal(interpret(statement.factor2).render())
        val quotient = dividend.divide(divisor, MathContext.DECIMAL128)
        val type = statement.target.variable.referred!!.type
        require(type is NumberType)
        return if (statement.halfAdjust) {
            DecimalValue(quotient.setScale(type.decimalDigits, RoundingMode.HALF_UP))
        } else {
            DecimalValue(quotient.setScale(type.decimalDigits, RoundingMode.DOWN))
        }
    }

    private fun assign(dataDefinition: AbstractDataDefinition, value: Value): Value {
        val coercedValue = coerce(value, dataDefinition.type)
        set(dataDefinition, coercedValue)
        return coercedValue
    }

    fun assignEachElement(target: AssignableExpression, value: Value): Value {
        val arrayType = target.type().asArray()
        return assign(target, value.toArray(arrayType.nElements, arrayType.element))
    }

    override fun assign(target: AssignableExpression, value: Value): Value {
        when (target) {
            is DataRefExpr -> {
                return assign(target.variable.referred!!, value)
            }
            is ArrayAccessExpr -> {
                val arrayValue = interpret(target.array) as ArrayValue
                val targetType = target.array.type()
                // Before assigning the single element we do a sanity check:
                // is the value we have for the array compatible with the type
                // we expect for such array?
                if (InterpreterConfiguration.enableRuntimeChecksOnAssignement) {
                    require(arrayValue.assignableTo(targetType)) {
                        "The value $arrayValue is not assignable to $targetType"
                    }
                }
                val indexValue = interpret(target.index)
                val elementType = (targetType as ArrayType).element
                val evaluatedValue = coerce(value, elementType)
                val index = indexValue.asInt().value.toInt()
                log {
                    AssignmentOfElementLogEntry(
                        this.interpretationContext.currentProgramName,
                        target.array,
                        index,
                        evaluatedValue
                    )
                }
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
            is QualifiedAccessExpr -> {
                val container = eval(target.container) as DataStructValue
                container[target.field.referred!!]
                container.set(target.field.referred!!, coerce(value, target.field.referred!!.type))
                return value
            }
            is PredefinedIndicatorExpr -> {
                val coercedValue = coerce(value, BooleanType)
                predefinedIndicators[target.index] = coercedValue.asBoolean()
                return coercedValue
            }
            is PredefinedGlobalIndicatorExpr -> {
                return if (value.assignableTo(BooleanType)) {
                    val coercedValue = coerce(value, BooleanType)
                    for (index in ALL_PREDEFINED_INDEXES) {
                        predefinedIndicators[index] = coercedValue.asBoolean()
                    }
                    coercedValue
                } else {
                    val coercedValue = coerce(value, ArrayType(BooleanType, 100)).asArray()
                    for (index in ALL_PREDEFINED_INDEXES) {
                        predefinedIndicators[index] = coercedValue.getElement(index).asBoolean()
                    }
                    coercedValue
                }
            }
            else -> TODO(target.toString())
        }
    }

    private fun assign(
        target: AssignableExpression,
        value: Expression,
        operator: AssignmentOperator = NORMAL_ASSIGNMENT
    ): Value {
        return when (operator) {
            NORMAL_ASSIGNMENT -> assign(target, eval(value))
            PLUS_ASSIGNMENT -> assign(target, eval(PlusExpr(target, value)))
            MINUS_ASSIGNMENT -> assign(target, eval(MinusExpr(target, value)))
            MULT_ASSIGNMENT -> assign(target, eval(MultExpr(target, value)))
            DIVIDE_ASSIGNMENT -> assign(target, eval(DivExpr(target, value)))
            EXP_ASSIGNMENT -> assign(target, eval(ExpExpr(target, value)))
        }
    }

    private fun assignEachElement(
        target: AssignableExpression,
        value: Expression,
        operator: AssignmentOperator = NORMAL_ASSIGNMENT
    ): Value {
        return when (operator) {
            NORMAL_ASSIGNMENT -> assignEachElement(target, eval(value))
            PLUS_ASSIGNMENT -> assignEachElement(target, eval(PlusExpr(target, value)))
            MINUS_ASSIGNMENT -> assignEachElement(target, eval(MinusExpr(target, value)))
            MULT_ASSIGNMENT -> assignEachElement(target, eval(MultExpr(target, value)))
            DIVIDE_ASSIGNMENT -> assignEachElement(target, eval(DivExpr(target, value)))
            EXP_ASSIGNMENT -> assignEachElement(target, eval(ExpExpr(target, value)))
        }
    }

    private fun add(statement: AddStmt): Value {
        val addend1 = interpret(statement.addend1)
        require(addend1 is NumberValue) {
            "$addend1 should be a number"
        }
        val addend2 = interpret(statement.right)
        require(addend2 is NumberValue) {
            "$addend2 should be a number"
        }
        return when {
            addend1 is IntValue && addend2 is IntValue -> IntValue(addend1.asInt().value.plus(addend2.asInt().value))
            addend1 is IntValue && addend2 is DecimalValue -> DecimalValue(addend1.asDecimal().value.plus(addend2.value))
            addend1 is DecimalValue && addend2 is IntValue -> DecimalValue(addend1.value.plus(addend2.asDecimal().value))
            addend1 is DecimalValue && addend2 is DecimalValue -> DecimalValue(addend1.value.plus(addend2.value))
            else -> throw UnsupportedOperationException("I do not know how to sum $addend1 and $addend2 at ${statement.position}")
        }
    }

    private fun sub(statement: SubStmt): Value {
        val minuend = interpret(statement.minuend)
        require(minuend is NumberValue) {
            "$minuend should be a number"
        }
        val subtrahend = interpret(statement.right)
        require(subtrahend is NumberValue) {
            "$subtrahend should be a number"
        }
        return when {
            minuend is IntValue && subtrahend is IntValue -> IntValue(minuend.asInt().value.minus(subtrahend.asInt().value))
            minuend is IntValue && subtrahend is DecimalValue -> DecimalValue(minuend.asDecimal().value.minus(subtrahend.value))
            minuend is DecimalValue && subtrahend is IntValue -> DecimalValue(minuend.value.minus(subtrahend.asDecimal().value))
            minuend is DecimalValue && subtrahend is DecimalValue -> DecimalValue(minuend.value.minus(subtrahend.value))
            else -> throw UnsupportedOperationException("I do not know how to sum $minuend and $subtrahend at ${statement.position}")
        }
    }

    override fun interpret(expression: Expression): Value {
        val value = interpretConcrete(expression)
        if (expression !is StringLiteral && expression !is IntLiteral &&
            expression !is DataRefExpr && expression !is BlanksRefExpr) {
            log { ExpressionEvaluationLogEntry(this.interpretationContext.currentProgramName, expression, value) }
        }
        return value
    }

    private fun evalAsBoolean(expression: Expression): Boolean = eval(expression).asBoolean().value

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
            is DataRefExpr -> get(
                expression.variable.referred
                    ?: throw IllegalStateException("[${interpretationContext.currentProgramName}] Unsolved reference ${expression.variable.name} at ${expression.position}")
            )
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
                return isGreaterThan(left, right, charset).asValue()
            }
            is GreaterEqualThanExpr -> {
                val left = interpret(expression.left)
                val right = interpret(expression.right)
                return (isGreaterThan(left, right, charset) || areEquals(left, right)).asValue()
            }
            is LessEqualThanExpr -> {
                val left = interpret(expression.left)
                val right = interpret(expression.right)
                return (isEqualOrSmaller(left, right, charset)).asValue()
            }
            is LessThanExpr -> {
                val left = interpret(expression.left)
                val right = interpret(expression.right)
                return isSmallerThan(left, right, charset).asValue()
            }
            is BlanksRefExpr -> {
                return BlanksValue
            }
            is DecExpr -> {
                val decDigits = interpret(expression.decDigits).asInt().value
                val valueAsString = interpret(expression.value).asString().value
                val valueAsBigDecimal = valueAsString.asBigDecimal()
                require(valueAsBigDecimal != null) {
                    "Line ${expression.position?.line()} - %DEC can't understand '$valueAsString'"
                }
                return if (decDigits == 0L) {
                    IntValue(valueAsBigDecimal.toLong())
                } else {
                    DecimalValue(valueAsBigDecimal)
                }
            }
            is PlusExpr -> {
                val left = interpret(expression.left)
                val right = interpret(expression.right)
                when {
                    left is StringValue && right is StringValue -> {
                        if (left.varying) {
                            val s = left.value.trimEnd() + right.value
                            StringValue(s)
                        } else {
                            val s = left.value + right.value
                            StringValue(s)
                        }
                    }
                    left is IntValue && right is IntValue -> IntValue(left.value + right.value)
                    left is NumberValue && right is NumberValue -> DecimalValue(left.bigDecimal + right.bigDecimal)
                    else -> throw UnsupportedOperationException("I do not know how to sum $left and $right at ${expression.position}")
                }
            }
            is MinusExpr -> {
                val left = interpret(expression.left)
                val right = interpret(expression.right)
                when {
                    left is IntValue && right is IntValue -> IntValue(left.value - right.value)
                    left is NumberValue && right is NumberValue -> DecimalValue(left.bigDecimal - right.bigDecimal)
                    else -> throw UnsupportedOperationException("I do not know how to sum $left and $right at ${expression.position}")
                }
            }
            is MultExpr -> {
                val left = interpret(expression.left)
                val right = interpret(expression.right)
                when {
                    left is IntValue && right is IntValue -> IntValue(left.value * right.value)
                    left is NumberValue && right is NumberValue -> DecimalValue(left.bigDecimal * right.bigDecimal)
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
                val arrayValueRaw = interpret(expression.array)
                val arrayValue = arrayValueRaw as? ArrayValue
                        ?: throw IllegalStateException("Array access to something that does not look like an array: ${expression.render()} (${expression.position})")
                val indexValue = interpret(expression.index)
                return arrayValue.getElement(indexValue.asInt().value.toInt())
            }
            is HiValExpr -> return HiValValue
            is LowValExpr -> return LowValValue
            is ZeroExpr -> return ZeroValue
            is AllExpr -> return AllValue(eval(expression.charsToRepeat).asString().value)
            is TranslateExpr -> {
                val originalChars = eval(expression.from).asString().value
                val newChars = eval(expression.to).asString().value
                val start = eval(expression.startPos).asInt().value.toInt()
                val s = eval(expression.string).asString().value
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
            is LogicalCondition -> {
                if (expression.ands.any { !evalAsBoolean(it) }) {
                    return BooleanValue.FALSE
                }

                if (evalAsBoolean(expression.expression)) {
                    return BooleanValue.TRUE
                }

                return BooleanValue(expression.ors.any { evalAsBoolean(it) })
            }
            is OnRefExpr -> {
                return BooleanValue.TRUE
            }
            is NotExpr -> {
                return BooleanValue(!evalAsBoolean(expression.base))
            }
            is ScanExpr -> {
                var startIndex = 0
                if (expression.start != null) {
                    startIndex = eval(expression.start).asInt().value.toInt()
                }
                val value = eval(expression.value).asString().value
                val source = eval(expression.source).asString().value
                val result = source.indexOf(value, startIndex)
                return IntValue(if (result == -1) 0 else result.toLong() + 1)
            }
            is SubstExpr -> {
                val length = if (expression.length != null) eval(expression.length).asInt().value.toInt() else 0
                val start = eval(expression.start).asInt().value.toInt() - 1
                val originalString = eval(expression.string).asString().value
                return if (length == 0) {
                    StringValue(originalString.padEnd(start + 1).substring(start))
                } else {
                    StringValue(originalString.padEnd(start + length + 1).substring(start, start + length))
                }
            }
            is LenExpr -> {
                val value = eval(expression.value)
                return when (value) {
                    is StringValue -> {
                        when (expression.value) {
                            is DataRefExpr -> {
                                val type = expression.value.type()
                                when (type) {
                                    is StringType -> {
                                        value.length(type.varying).asValue()
                                    }
                                    else -> {
                                        value.value.length.asValue()
                                    }
                                }
                            }
                            is ArrayAccessExpr -> {
                                value.value.length.asValue()
                                // value.elementSize().asValue()
                            }
                            else -> {
                                return value.length().asValue()
                            }
                        }
                    }
                    is DataStructValue -> {
                        value.value.length.asValue() }
                    is ArrayValue -> {
                        // Incorrect data structure size calculation #28
                        when (expression.value) {
                            is DataRefExpr -> value.totalSize().asValue()
                            is ArrayAccessExpr -> value.elementSize().asValue()
                            else -> {
                                TODO("Invalid LEN parameter $value")
                            }
                        }
                    }
                    else -> {
                        TODO("Invalid LEN parameter $value")
                    }
                }
            }
            is OffRefExpr -> {
                return BooleanValue(false)
            }
            is PredefinedIndicatorExpr -> {
                return indicator(expression.index)
            }
            is FunctionCall -> {
                val functionToCall = expression.function.name
                val function = systemInterface.findFunction(globalSymbolTable, functionToCall)
                    ?: throw RuntimeException("Function $functionToCall cannot be found (${interpretationContext.currentProgramName}:${expression.position.line()})")
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
                return n.asDecimal().formatAs(format.value, expression.value.type(), this.decedit)
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
                // Check the type and select the correct operation
                if (v1 is IntValue && v2 is IntValue) {
                    return DecimalValue(BigDecimal(v1.asInt().value / v2.asInt().value))
                }
                require(v1 is NumberValue && v2 is NumberValue)
                val res = v1.bigDecimal.toDouble() / v2.bigDecimal.toDouble()
                // Detects what kind of eval must be evaluated
                if (expression.parent is EvalStmt) {
                    val parent = expression.parent as EvalStmt
                    val targetType = parent.target.type() as NumberType
                    // EVAL(H)
                    if (parent.flags.halfAdjust) {
                        // perform the calculation, adjust the operand scale to the target
                        return DecimalValue(v1.bigDecimal.setScale(targetType.decimalDigits).divide(v2.bigDecimal.setScale(targetType.decimalDigits), RoundingMode.HALF_UP))
                    }
                    // Eval(M)
                    if (parent.flags.maximumNumberOfDigitsRule) {
                        TODO("EVAL(M) not supported yet")
                    }
                    // Eval(R)
                    if (parent.flags.resultDecimalPositionRule) {
                        TODO("EVAL(R) not supported yet")
                    }
                    return DecimalValue(BigDecimal(res).setScale(targetType.decimalDigits, RoundingMode.DOWN))
                }
                // TODO rounding and scale???
                return DecimalValue(BigDecimal(res))
            }
            is ExpExpr -> {
                val v1 = eval(expression.left)
                val v2 = eval(expression.right)
                return DecimalValue(BigDecimal(Math.pow(v1.asInt().value.toDouble(), v2.asInt().value.toDouble())))
            }
            is TrimrExpr -> {
                return if (expression.charactersToTrim == null) {
                    StringValue(eval(expression.value).asString().value.trimEnd())
                } else {
                    val suffix = eval(expression.charactersToTrim).asString().value
                    var result = eval(expression.value).asString().value
                    while (result.endsWith(suffix)) {
                        result = result.substringBefore(suffix)
                    }
                    StringValue(result)
                }
            }
            is TrimlExpr -> {
                if (expression.charactersToTrim == null) {
                    return StringValue(eval(expression.value).asString().value.trimStart())
                } else {
                    val prefix = eval(expression.charactersToTrim).asString().value
                    var result = eval(expression.value).asString().value
                    while (result.startsWith(prefix)) {
                        result = result.substringAfter(prefix)
                    }
                    return StringValue(result)
                }
            }
            is TrimExpr -> {
                return StringValue(eval(expression.value).asString().value.trim())
            }
            is FoundExpr -> {
                // TODO fix this bad implementation
                if (expression.name == null) {
                    return BooleanValue(lastFound)
                }
                TODO("Line ${expression.position?.line()} - %FOUND expression with file names is not implemented yet")
            }
            is EofExpr -> {
                // TODO fix this bad implementation
                if (expression.name == null) {
                    return BooleanValue(lastDBFile?.eof() ?: false)
                }
                TODO("Line ${expression.position?.line()} - %EOF expression with file names is not implemented yet")
            }
            is EqualExpr -> {
                // TODO fix this bad implementation
                if (expression.name == null) {
                    return BooleanValue(lastDBFile?.equal() ?: false)
                }
                TODO("Line ${expression.position?.line()} - %EQUAL expression with file names is not implemented yet")
            }
            is AbsExpr -> {
                val value = interpret(expression.value)
                return DecimalValue(BigDecimal.valueOf(Math.abs(value.asDecimal().value.toDouble())))
            }
            is EditwExpr -> {
                val n = eval(expression.value)
                val format = eval(expression.format)
                require(format is StringValue) { "Required string value, but got $format at ${expression.position}" }
                return n.asDecimal().formatAsWord(format.value, expression.value.type())
            }
            is IntExpr -> {
                return when (val value = interpret(expression.value)) {
                    is StringValue ->
                        IntValue(cleanNumericString(value.value).asLong())
                    is DecimalValue ->
                        value.asInt()
                    else -> throw UnsupportedOperationException("I do not know how to handle $value with %INT")
                }
            }
            is RemExpr -> {
                val n = eval(expression.dividend).asInt().value
                val m = eval(expression.divisor).asInt().value
                return IntValue(n % m)
            }
            is QualifiedAccessExpr -> {
                val containerValue = eval(expression.container)
                val dataStringValue = containerValue as DataStructValue
                return dataStringValue[expression.field.referred ?: throw IllegalStateException("Referenced to field not resolved: ${expression.field.name}")]
            }
            is ReplaceExpr -> {
                val replString = eval(expression.replacement).asString().value
                val sourceString = eval(expression.source).asString().value
                val replStringLength: Int = replString.length
                // case of %REPLACE(stringToReplaceWith:stringSource)
                if (expression.start == null) {
                    return StringValue(sourceString.replaceRange(0..replStringLength - 1, replString))
                }
                // case of %REPLACE(stringToReplaceWith:stringSource:startIndex)
                if (expression.length == null) {
                    val startNr = eval(expression.start).asInt().value.toInt()
                    return StringValue(sourceString.replaceRange((startNr - 1)..(startNr + replStringLength - 2), replString))
                } else {
                // case of %REPLACE(stringToReplaceWith:stringSource:startIndex:nrOfCharsToReplace)
                    val startNr = eval(expression.start).asInt().value.toInt() - 1
                    val nrOfCharsToReplace = eval(expression.length).asInt().value.toInt()
                    return StringValue(sourceString.replaceRange(startNr, (startNr + nrOfCharsToReplace), replString))
                }
            }
            is SqrtExpr -> {
                val value = interpret(expression.value)
                return DecimalValue(BigDecimal.valueOf(Math.sqrt(value.asDecimal().value.toDouble())))
            }
            else -> TODO(expression.toString())
        }
    }

    private fun indicator(key: IndicatorKey) = predefinedIndicators[key] ?: BooleanValue.FALSE

    fun blankValue(dataDefinition: DataDefinition, forceElement: Boolean = false): Value {
        if (forceElement) TODO()
        return when (dataDefinition.type) {
            is DataStructureType -> dataDefinition.type.blank(dataDefinition)
            else -> dataDefinition.type.blank()
        }
    }
}

private fun cleanNumericString(s: String): String {
    val result = s.moveEndingString("-")
    return when {
        result.contains(".") -> result.substringBefore(".")
        result.contains(",") -> result.substringBefore(",")
        else -> result
    }
}
