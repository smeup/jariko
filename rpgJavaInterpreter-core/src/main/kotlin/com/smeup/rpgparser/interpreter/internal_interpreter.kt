package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.parsing.ast.AssignmentOperator.*
import com.smeup.rpgparser.parsing.parsetreetoast.MuteAnnotationExecutionLogEntry
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import com.smeup.rpgparser.utils.*
import com.smeup.rpgparser.utils.ComparisonOperator.*
import com.strumenta.kolasu.model.ancestor
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.util.*
import kotlin.collections.HashMap
import kotlin.system.measureTimeMillis

object InterpreterConfiguration {
    /**
     * Enable runtime checks during assignments
     */
    var enableRuntimeChecksOnAssignement = false
}

val ALL_PREDEFINED_INDEXES = 1..99

class InterpreterStatus(
    val symbolTable: ISymbolTable,
    val predefinedIndicators: HashMap<IndicatorKey, BooleanValue>
) {
    var lastFound = false
    var lastDBFile: DBFile? = null
    fun indicator(key: IndicatorKey) = predefinedIndicators[key] ?: BooleanValue.FALSE
    fun getVar(abstractDataDefinition: AbstractDataDefinition): Value = symbolTable.get(abstractDataDefinition)
}

class InternalInterpreter(
    override val systemInterface: SystemInterface,
    override val localizationContext: LocalizationContext = LocalizationContext(),
    override val interpretationContext: InterpretationContext = SimpleInterpretationContext()
) : InterpreterCore {
    override val globalSymbolTable = systemInterface.getFeaturesFactory().createSymbolTable()
    override val predefinedIndicators = HashMap<IndicatorKey, BooleanValue>()

    override val klists = HashMap<String, List<String>>()

    private var logHandlers: List<InterpreterLogHandler> = emptyList()

    fun logsEnabled() = logHandlers.isNotEmpty()

    override val status = InterpreterStatus(globalSymbolTable, predefinedIndicators)

    private val dbFileMap = DBFileMap(systemInterface.db)

    private val expressionEvaluation = ExpressionEvaluation(systemInterface, localizationContext, status)

    override inline fun log(logEntry: () -> LogEntry) {
        if (logsEnabled()) doLog(logEntry())
    }

    fun doLog(entry: LogEntry) {
        logHandlers.log(entry)
    }

    override fun exists(dataName: String) = globalSymbolTable.contains(dataName)

    override fun dataDefinitionByName(name: String) = globalSymbolTable.dataDefinitionByName(name)

    override operator fun get(data: AbstractDataDefinition): Value {
        return globalSymbolTable[data]
    }

    override operator fun get(dataName: String) = globalSymbolTable[dataName]

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
                log {
                    var previous: Value? = null
                    if (data.name in globalSymbolTable) {
                        previous = globalSymbolTable[data.name]
                    }
                    AssignmentLogEntry(this.interpretationContext.currentProgramName, data, value, previous)
                }
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
                        it.initializationValue != null -> eval(it.initializationValue)
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
                                val fieldValue = coerce(eval(field.initializationValue), field.type)
                                (value as DataStructValue).set(field, fieldValue)
                            }
                        }
                    }
                } else if (it is InStatementDataDefinition) {
                    value = if (it.parent is PlistParam) {
                        when {
                            it.name in initialValues -> initialValues[it.name]
                                ?: throw RuntimeException("Initial values for ${it.name} not found")
                            else -> null
                        }
                    } else {
                        // TODO check this during the process of revision of DB access
                        if (it.type is KListType) null else it.type.blank()
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
            if (elapsedTime > compilationUnit.minTimeOut!!) {
                throw InterpreterTimeoutException(interpretationContext.currentProgramName, elapsedTime, compilationUnit.minTimeOut!!)
            }
        }
    }

    private fun GotoException.indexOfTaggedStatement(statements: List<Statement>): Int =
        statements.indexOfFirst {
            it is TagStmt && it.tag == tag
        }

    private fun caseInsensitiveMap(aMap: Map<String, Value>): Map<String, Value> {
        val result = TreeMap<String, Value>(String.CASE_INSENSITIVE_ORDER)
        result.putAll(aMap)
        return result
    }

    override fun execute(statements: List<Statement>) {
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
            if (statement.isStatementExecutable(getMapOfORs(statement.solveIndicatorValues()))) {
                statement.execute(this)
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
        } finally {
            if (statement.muteAnnotations.size > 0) {
                executeMutes(
                    statement.muteAnnotations,
                    statement.ancestor(CompilationUnit::class.java)!!,
                    statement.position.line()
                )
            }
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
                    val value1 = it.val1.evalWith(expressionEvaluation)
                    val value2 = it.val2.evalWith(expressionEvaluation)
                    // TODO use value1 and value2 without re-evaluate them as they could have side-effects
                    val value = exp.evalWith(expressionEvaluation)
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
                    systemInterface.addExecutedAnnotation(
                        it.position!!.start.line,
                        MuteTimeoutAnnotationExecuted(
                            this.interpretationContext.currentProgramName,
                            it.timeout,
                            line)
                    )
                }
                is MuteFailAnnotation -> {
                    val message = it.message.evalWith(expressionEvaluation)
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
        val indicator = status.indicator(key).value
        return if (negate) !indicator else indicator
    }

    data class SolvedIndicatorCondition(val key: Int, val value: Boolean, val operator: String)

    private fun Statement.solveIndicatorValues(): List<SolvedIndicatorCondition> =
        this.continuedIndicators.map { (indicatorKey, continuedIndicator) ->
            val indicator = status.indicator(indicatorKey).value
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

    override fun optimizedIntExpression(expression: Expression): () -> Long =
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

    override fun fillDataFrom(record: Record) {
        if (!record.isEmpty()) {
            status.lastFound = true
            record.forEach { assign(dataDefinitionByName(it.key)!!, it.value) }
        } else {
            status.lastFound = false
        }
    }

    override fun dbFile(name: String, statement: Statement): DBFile {
        val dbFile = dbFileMap[name]
        require(dbFile != null) {
            "Line: ${statement.position.line()} - File definition $name not found"
        }
        status.lastDBFile = dbFile
        return dbFile
    }

    override fun toSearchValues(searchArgExpression: Expression): List<RecordField> {
        val kListName = searchArgExpression.render().toUpperCase()
        val parms = klists[kListName]
        return parms!!.map { RecordField(it, get(it)) }
    }

    override fun enterCondition(index: Value, end: Value, downward: Boolean): Boolean =
        if (downward) {
            isEqualOrGreater(index, end, localizationContext.charset)
        } else {
            isEqualOrSmaller(index, end, localizationContext.charset)
        }

    override fun increment(dataDefinition: AbstractDataDefinition, amount: Long): Value {
        val value = this[dataDefinition]
        if (value is NumberValue) {
            val newValue = value.increment(amount)
            globalSymbolTable[dataDefinition] = newValue
            return newValue
        } else {
            TODO("Incrementing of ${value.javaClass}")
        }
    }

    override fun rawRender(values: List<Value>) = values.map { rawRender(it) }.joinToString("")

    private fun rawRender(value: Value): String {
        return when (value) {
            is NumberValue -> if (value.isNegative()) "${value.abs().render()}-" else value.render()
            else -> value.stringRepresentation()
        }
    }

    override fun eval(expression: Expression): Value {
        val value = when (expression) {
            is AssignmentExpr -> {
                assign(expression.target, expression.value)
            }
            else -> expression.evalWith(expressionEvaluation)
        }
        if (expression !is StringLiteral && expression !is IntLiteral &&
            expression !is DataRefExpr && expression !is BlanksRefExpr) {
            log { ExpressionEvaluationLogEntry(this.interpretationContext.currentProgramName, expression, value) }
        }
        return value
    }

    override fun mult(statement: MultStmt): Value {
        // TODO When will pass my PR for more robustness replace Value.render with NumericValue.bigDecimal
        require(statement.target is DataRefExpr)
        val rightValue: BigDecimal = if (statement.factor1 != null) {
            BigDecimal(eval(statement.factor1).render())
        } else {
            BigDecimal(get(statement.target.variable.referred!!).render())
        }
        val leftValue = BigDecimal(eval(statement.factor2).render())
        val result = rightValue.multiply(leftValue)
        val type = statement.target.variable.referred!!.type
        require(type is NumberType)
        return if (statement.halfAdjust) {
            DecimalValue(result.setScale(type.decimalDigits, RoundingMode.HALF_UP))
        } else {
            DecimalValue(result.setScale(type.decimalDigits, RoundingMode.DOWN))
        }
    }

    override fun div(statement: DivStmt): Value {
        // TODO When will pass my PR for more robustness replace Value.render with NumericValue.bigDecimal
        require(statement.target is DataRefExpr)
        val dividend: BigDecimal = if (statement.factor1 != null) {
            BigDecimal(eval(statement.factor1).render())
        } else {
            BigDecimal(get(statement.target.variable.referred!!).render())
        }
        val divisor = BigDecimal(eval(statement.factor2).render())
        val quotient = dividend.divide(divisor, MathContext.DECIMAL128)
        val type = statement.target.variable.referred!!.type
        require(type is NumberType)
        return if (statement.halfAdjust) {
            DecimalValue(quotient.setScale(type.decimalDigits, RoundingMode.HALF_UP))
        } else {
            DecimalValue(quotient.setScale(type.decimalDigits, RoundingMode.DOWN))
        }
    }

    override fun assign(dataDefinition: AbstractDataDefinition, value: Value): Value {
        val coercedValue = coerce(value, dataDefinition.type)
        set(dataDefinition, coercedValue)
        return coercedValue
    }

    override fun assignEachElement(target: AssignableExpression, value: Value): Value {
        val arrayType = target.type().asArray()
        return assign(target, value.toArray(arrayType.nElements, arrayType.element))
    }

    override fun assign(target: AssignableExpression, value: Value): Value {
        when (target) {
            is DataRefExpr -> {
                return assign(target.variable.referred!!, value)
            }
            is ArrayAccessExpr -> {
                val arrayValue = eval(target.array) as ArrayValue
                val targetType = target.array.type()
                // Before assigning the single element we do a sanity check:
                // is the value we have for the array compatible with the type
                // we expect for such array?
                if (InterpreterConfiguration.enableRuntimeChecksOnAssignement) {
                    require(arrayValue.assignableTo(targetType)) {
                        "The value $arrayValue is not assignable to $targetType"
                    }
                }
                val indexValue = eval(target.index)
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

    override fun assign(
        target: AssignableExpression,
        value: Expression,
        operator: AssignmentOperator
    ): Value {
        return when (operator) {
            NORMAL_ASSIGNMENT -> {
                if (target is DataRefExpr && value is PlusExpr && value.left.render() == target.render() && value.right is IntLiteral) {
                    val amount = (value.right as IntLiteral).value
                    increment(target.variable.referred!!, amount)
                } else {
                    assign(target, eval(value))
                }
            }
            PLUS_ASSIGNMENT ->
                if (target is DataRefExpr && value is IntLiteral) {
                    increment(target.variable.referred!!, value.value)
                } else {
                    assign(target, eval(PlusExpr(target, value)))
                }
            MINUS_ASSIGNMENT -> assign(target, eval(MinusExpr(target, value)))
            MULT_ASSIGNMENT -> assign(target, eval(MultExpr(target, value)))
            DIVIDE_ASSIGNMENT -> assign(target, eval(DivExpr(target, value)))
            EXP_ASSIGNMENT -> assign(target, eval(ExpExpr(target, value)))
        }
    }

    override fun assignEachElement(
        target: AssignableExpression,
        value: Expression,
        operator: AssignmentOperator
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

    override fun add(statement: AddStmt): Value {
        val addend1 = eval(statement.addend1)
        require(addend1 is NumberValue) {
            "$addend1 should be a number"
        }
        val addend2 = eval(statement.right)
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

    override fun sub(statement: SubStmt): Value {
        val minuend = eval(statement.minuend)
        require(minuend is NumberValue) {
            "$minuend should be a number"
        }
        val subtrahend = eval(statement.right)
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

    fun blankValue(dataDefinition: DataDefinition, forceElement: Boolean = false): Value {
        if (forceElement) TODO()
        return when (dataDefinition.type) {
            is DataStructureType -> createBlankFor(dataDefinition)
            else -> dataDefinition.type.blank()
        }
    }
}
