/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smeup.rpgparser.interpreter

import com.smeup.dbnative.file.DBFile
import com.smeup.dbnative.file.Record
import com.smeup.rpgparser.execution.ErrorEvent
import com.smeup.rpgparser.execution.ErrorEventSource
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.logging.*
import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.parsing.ast.AssignmentOperator.*
import com.smeup.rpgparser.parsing.facade.SourceReference
import com.smeup.rpgparser.parsing.facade.dumpSource
import com.smeup.rpgparser.parsing.facade.relative
import com.smeup.rpgparser.parsing.parsetreetoast.RpgType
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import com.smeup.rpgparser.parsing.parsetreetoast.todo
import com.smeup.rpgparser.utils.ComparisonOperator.*
import com.smeup.rpgparser.utils.chunkAs
import com.smeup.rpgparser.utils.resizeTo
import com.strumenta.kolasu.model.Position
import com.strumenta.kolasu.model.ReferenceByName
import com.strumenta.kolasu.model.ancestor
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.min
import kotlin.system.measureTimeMillis

object InterpreterConfiguration {
    /**
     * Enable runtime checks during assignments
     */
    var enableRuntimeChecksOnAssignement = false
}

val ALL_PREDEFINED_INDEXES = IndicatorType.Predefined.range

private const val MEMORY_SLICE_ATTRIBUTE = "com.smeup.rpgparser.interpreter.memorySlice"
private const val PREV_STMT_EXEC_LINE_ATTRIBUTE = "com.smeup.rpgparser.interpreter.prevStmtExecLine"

typealias StatementReference = Pair<Int, SourceReference>

class InterpreterStatus(
    val symbolTable: ISymbolTable,
    val indicators: HashMap<IndicatorKey, BooleanValue>,
    var returnValue: Value? = null,
    var params: Int = 0
) {
    var inzsrExecuted = false
    var lastFound = false
    var lastDBFile: DBFile? = null
    val dbFileMap = DBFileMap()
    fun indicator(key: IndicatorKey) = indicators[key] ?: BooleanValue.FALSE
    fun getVar(abstractDataDefinition: AbstractDataDefinition): Value {
        val tmpValue = symbolTable[abstractDataDefinition]
        if (tmpValue is NullValue) {
            throw IllegalArgumentException("Void value for ${abstractDataDefinition.name}")
        }
        return tmpValue
    }
}

open class InternalInterpreter(
    private val systemInterface: SystemInterface,
    private val localizationContext: LocalizationContext = LocalizationContext()
) : InterpreterCore {
    override fun getSystemInterface(): SystemInterface {
        return systemInterface
    }

    override fun getLocalizationContext(): LocalizationContext {
        return localizationContext
    }

    private val globalSymbolTable = systemInterface.getFeaturesFactory().createSymbolTable()
    override fun getGlobalSymbolTable(): ISymbolTable {
        return globalSymbolTable
    }

    private val indicators = HashMap<IndicatorKey, BooleanValue>()
    override fun getIndicators(): HashMap<IndicatorKey, BooleanValue> {
        return indicators
    }

    private var interpretationContext: InterpretationContext = DummyInterpretationContext
    override fun getInterpretationContext(): InterpretationContext {
        return interpretationContext
    }

    fun setInterpretationContext(interpretationContext: InterpretationContext) {
        this.interpretationContext = interpretationContext
    }

    private val klists = HashMap<String, List<String>>()
    override fun getKlists(): HashMap<String, List<String>> {
        return klists
    }

    private var logHandlers: List<InterpreterLogHandler> = emptyList()

    private fun logsEnabled() = logHandlers.isNotEmpty()

    private val status = InterpreterStatus(globalSymbolTable, indicators)
    override fun getStatus(): InterpreterStatus {
        return status
    }

    private val expressionEvaluation = ExpressionEvaluation(systemInterface, localizationContext, status)

    override fun renderLog(producer: () -> LazyLogEntry) {
        if (logsEnabled()) doLog(producer())
    }

    private fun doLog(renderer: LazyLogEntry) {
        logHandlers.renderLog(renderer)
    }

    override fun exists(dataName: String) = globalSymbolTable.contains(dataName)

    override fun dataDefinitionByName(name: String) = globalSymbolTable.dataDefinitionByName(name)

    override operator fun get(data: AbstractDataDefinition) = globalSymbolTable[data]

    override operator fun get(dataName: String) = globalSymbolTable[dataName]

    open operator fun set(data: AbstractDataDefinition, value: Value) {
        require(data.canBeAssigned(value)) {
            "${data.name} of type ${data.type} defined at line ${data.position.line()} cannot be assigned the value $value"
        }

        val logSource = LogSourceData(this.interpretationContext.currentProgramName, data.startLine())

        when (data) {
            // Field are stored within the Data Structure definition
            is FieldDefinition -> {
                val ds = data.parent as DataDefinition
                if (data.declaredArrayInLine != null) {
                    val dataStructValue = get(ds.name) as DataStructValue
                    var startOffset = data.startOffset
                    val size = data.endOffset - data.startOffset

                    // for (i in 1..data.declaredArrayInLine!!) {
                    // If the size of the arrays are different
                    val maxElements = min(value.asArray().arrayLength(), data.declaredArrayInLine!!)
                    for (i in 1..maxElements) {
                        // Added coerce
                        val valueToAssign = coerce(value.asArray().getElement(i), data.type.asArray().element)
                        dataStructValue.setSubstring(
                            startOffset, startOffset + size,
                            data.type.asArray().element.toDataStructureValue(valueToAssign)
                        )
                        startOffset += data.stepSize
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
                        is OccurableDataStructValue -> {
                            containerValue.value().set(data, value)
                        }
                        else -> TODO()
                    }
                }
                // TODO: Add DATA log entry for data structs
            }
            else -> {
                renderLog {
                    val previous = if (data.name in globalSymbolTable) {
                        globalSymbolTable[data.name]
                    } else null
                    LazyLogEntry.produceData(logSource, data, value, previous)
                }
                renderLog { LazyLogEntry.produceAssignment(logSource, data, value) }
                // deny reassignment if data is a constant
                globalSymbolTable.set(data, coerce(value, data.type))?.let {
                    if (data.const) error("${data.name} is a const and cannot be assigned")
                }
            }
        }
    }

    private fun initialize(
        compilationUnit: CompilationUnit,
        initialValues: Map<String, Value>,
        reinitialization: Boolean = true
    ) {
        val start = System.currentTimeMillis()
        val logSource =
            LogSourceData(programName = interpretationContext.currentProgramName, line = compilationUnit.startLine())

        renderLog { LazyLogEntry.produceInformational(logSource, "SYMTBLINI", "START") }
        renderLog { LazyLogEntry.produceStatement(logSource, "SYMTBLINI", "START") }

        // TODO verify if these values should be reinitialised or not
        compilationUnit.fileDefinitions.filter { it.fileType == FileType.DB }.forEach {
            status.dbFileMap.add(it)
        }

        var index = 0
        // Assigning initial values received from outside and consider INZ clauses
        // symboltable goes empty when program exits in LR mode so, it is always needed reinitialize, in these
        // circumstances is correct reinitialization
        if (reinitialization || globalSymbolTable.isEmpty()) {
            beforeInitialization()
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
                            compilationUnit.compileTimeArray(index++),
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
                        when (it.name) {
                            in initialValues -> initialValues[it.name]
                                ?: throw RuntimeException("Initial values for ${it.name} not found")

                            else -> if ((it.parent as PlistParam).dataDefinition().isNotEmpty()) {
                                it.type.blank()
                            } else {
                                null
                            }
                        }
                    } else {
                        // TODO check this during the process of revision of DB access
                        if (it.type is KListType) null else it.type.blank()
                    }
                }
                // Fix issue on CTDATA
                val ctdata = compilationUnit.compileTimeArray(it.name)
                if (ctdata.name == it.name) {
                    value = toArrayValue(
                        compilationUnit.compileTimeArray(it.name),
                        (it.type as ArrayType)
                    )
                    set(it, value)
                }

                if (value != null) {
                    set(it, coerce(value, it.type))
                    if (it is DataDefinition) {
                        try {
                            val tmpValue = globalSymbolTable[it]
                            if (tmpValue !is NullValue) {
                                it.defaultValue = tmpValue.copy()
                            }
                        } catch (exc: IllegalArgumentException) {
                            it.defaultValue = null
                        }
                    }
                    executeMutes(it.muteAnnotations, compilationUnit, "(data definition)")
                }
            }
        } else {
            initialValues.forEach { iv ->
                val def = compilationUnit.allDataDefinitions.find { it.name.equals(iv.key, ignoreCase = true) }!!
                set(def, coerce(iv.value, def.type))
            }
        }

        val initElapsed = System.currentTimeMillis() - start

        renderLog { LazyLogEntry.produceInformational(logSource, "SYMTBLINI", "END") }
        renderLog { LazyLogEntry.produceStatement(logSource, "SYMTBLINI", "END") }
        renderLog { LazyLogEntry.producePerformance(logSource, "SYMTBLINI", initElapsed) }

        renderLog { LazyLogEntry.produceInformational(logSource, "SYMTBLLOAD", "START") }
        renderLog { LazyLogEntry.produceStatement(logSource, "SYMTBLLOAD", "START") }

        val loadElapsed = measureTimeMillis { afterInitialization(initialValues = initialValues) }

        renderLog { LazyLogEntry.produceInformational(logSource, "SYMTBLLOAD", "END") }
        renderLog { LazyLogEntry.produceStatement(logSource, "SYMTBLLOAD", "END") }
        renderLog { LazyLogEntry.producePerformance(logSource, "SYMTBLLOAD", loadElapsed) }
    }

    private fun toArrayValue(compileTimeArray: CompileTimeArray, arrayType: ArrayType): Value {
        // It is not clear why the compileTimeRecordsPerLine on the array type is null
        // probably it is an error during the ast processing.
        // as workaround, if null assumes the number of lines in the compile compileTimeArray
        // as value for compileTimeRecordsPerLine
        val lines = arrayType.compileTimeRecordsPerLine ?: compileTimeArray.lines.size
        val l: MutableList<Value> =
            compileTimeArray.lines.chunkAs(lines, arrayType.element.size)
                .map {
                    // force rpgle to zoned if is number type
                    val elementType = if (arrayType.element is NumberType) {
                        arrayType.element.copy(rpgType = RpgType.ZONED.rpgType)
                    } else {
                        arrayType.element
                    }
                    coerce(StringValue(it), elementType)
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
        kotlin.runCatching {
            configureLogHandlers()

            status.params = initialValues.size
            initialize(compilationUnit, caseInsensitiveMap(initialValues), reinitialization)
            execINZSR(compilationUnit)
            if (compilationUnit.minTimeOut == null) {
                execute(compilationUnit.main.stmts)
            } else {
                val elapsedTime = measureTimeMillis {
                    execute(compilationUnit.main.stmts)
                }

                if (elapsedTime > compilationUnit.minTimeOut!!) {
                    throw InterpreterTimeoutException(
                        interpretationContext.currentProgramName,
                        elapsedTime,
                        compilationUnit.minTimeOut!!
                    )
                }
            }

            val logSource = LogSourceData(this.interpretationContext.currentProgramName, "")
            val logEntry = LogEntry(logSource, LogChannel.PERFORMANCE.getPropertyName(), "RECAP")
            val message = "Execution time is distributed as following:\n" +
                    MainExecutionContext.getLoggingContext()?.generateTimeUsageByStatementReport()

            renderLog { LazyLogEntry.produceMessage(logEntry, message) }
        }.onFailure {
            if (it is ReturnException) {
                status.returnValue = it.returnValue
            } else {
                if (!MainExecutionContext.getProgramStack().isEmpty()) {
                    MainExecutionContext.getProgramStack().peek().cu.source?.apply {
                        System.err.println(it.message)
                        System.err.println(this.dumpSource())
                    }
                }
                throw it
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
        var i = 0
        while (i < statements.size) {
            try {
                executeWithMute(statements[i++])
            } catch (e: GotoException) {
                i = e.indexOfTaggedStatement(statements)
                if (i < 0 || i >= statements.size) throw e
            }
        }
    }

    @Deprecated(message = "No longer used")
    open fun fireOnEnterPgmCallBackFunction() {
    }

    private fun executeWithMute(statement: Statement) {
        val logSource = LogSourceData(
            programName = this.interpretationContext.currentProgramName,
            line = statement.position.line()
        )

        renderLog { LazyLogEntry.produceLine(logSource) }

        try {
            if (statement.isStatementExecutable(getMapOfORs(statement.solveIndicatorValues()))) {
                statement.position?.let { fireCopyObservingCallback(it.start.line) }
                if (MainExecutionContext.getConfiguration().options.mustInvokeOnStatementCallback()) {
                    statement.position?.relative()?.let {
                        MainExecutionContext.getConfiguration().jarikoCallback.onEnterStatement(it.first, it.second)
                    }
                }

                if (statement is MockStatement) {
                    MainExecutionContext.getConfiguration().jarikoCallback.onMockStatement
                } else {
                    executeWithLogging(statement, logSource)
                }
            }
        } catch (e: ControlFlowException) {
            throw e
        } catch (e: IllegalArgumentException) {
            val message = e.toString()
            if (!message.contains(statement.position.line())) {
                throw IllegalArgumentException(errorDescription(statement, e), e).fireErrorEvent(statement.position)
            }
            throw e
        } catch (e: NotImplementedError) {
            throw RuntimeException(errorDescription(statement, e), e).fireErrorEvent(statement.position)
        } catch (e: RuntimeException) {
            throw RuntimeException(errorDescription(statement, e), e).fireErrorEvent(statement.position)
        } catch (t: Throwable) {
            throw RuntimeException(errorDescription(statement, t), t).fireErrorEvent(statement.position)
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

    private fun Throwable.fireErrorEvent(position: Position?): Throwable {
        val errorEvent =
            ErrorEvent(this, ErrorEventSource.Interpreter, position?.start?.line, position?.relative()?.second)
        errorEvent.pushRuntimeErrorEvent()
        MainExecutionContext.getConfiguration().jarikoCallback.onError.invoke(errorEvent)
        return this
    }

    // I use a chain of names, so I am sure that this attribute name depends on program stack too
    private fun prevStmtAttributeMame(): String {
        return "$PREV_STMT_EXEC_LINE_ATTRIBUTE${
            MainExecutionContext.getProgramStack().joinToString(separator = "->") { it.name }
        }"
    }

    private fun fireCopyObservingCallback(currentStatementLine: Int) {
        if (!MainExecutionContext.getProgramStack()
                .empty() && MainExecutionContext.getConfiguration().options.mustCreateCopyBlocks()
        ) {
            val copyBlocks = MainExecutionContext.getProgramStack().peek().cu.copyBlocks!!
            val previousStatementLine = (MainExecutionContext.getAttributes()[prevStmtAttributeMame()] ?: 0) as Int
            copyBlocks.observeTransitions(
                from = previousStatementLine + if (currentStatementLine > previousStatementLine) 1 else -1,
                to = currentStatementLine,
                onEnter = { copyBlock ->
                    MainExecutionContext.getConfiguration().jarikoCallback.onEnterCopy.invoke(
                        copyBlock.copyId
                    )
                },
                onExit = { copyBlock ->
                    MainExecutionContext.getConfiguration().jarikoCallback.onExitCopy.invoke(
                        copyBlock.copyId
                    )
                }
            )
            MainExecutionContext.getAttributes()[prevStmtAttributeMame()] = currentStatementLine
        }
    }

    private fun executeMutes(
        muteAnnotations: MutableList<MuteAnnotation>,
        compilationUnit: CompilationUnit,
        line: String
    ) {
        muteAnnotations.forEach {
            it.resolveAndValidate(compilationUnit)
            val logSource = LogSourceData(
                this.interpretationContext.currentProgramName,
                it.startLine()
            )
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

                    renderLog { LazyLogEntry.produceMute(it, logSource, value) }

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
                            line
                        )
                    )
                }

                is MuteFailAnnotation -> {
                    val message = it.message.evalWith(expressionEvaluation)
                    renderLog { LazyLogEntry.produceMute(it, logSource, message) }
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
            val condition: Boolean = if (continuedIndicator.negate) !indicator else indicator
            val solvedIndicatorCondition = SolvedIndicatorCondition(indicatorKey, condition, continuedIndicator.level)
            solvedIndicatorCondition
        }

    private fun getMapOfORs(indicatorValues: List<SolvedIndicatorCondition>): ArrayList<ArrayList<Boolean>> {
        val mapOfORs = ArrayList<ArrayList<Boolean>>()
        val reversed = indicatorValues.reversed()
        var previousOperator = ""
        var loops = 0
        var idxOfMapOfANDs = 0
        reversed.forEach { solvedIndicator ->
            if (loops == 0) {
                mapOfORs.add(ArrayList<Boolean>())
                mapOfORs[idxOfMapOfANDs].add(solvedIndicator.value)
            } else {
                if (previousOperator == "AND") {
                    mapOfORs[idxOfMapOfANDs].add(solvedIndicator.value)
                } else {
                    mapOfORs.add(ArrayList<Boolean>())
                    idxOfMapOfANDs++
                    mapOfORs[idxOfMapOfANDs].add(solvedIndicator.value)
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

    override fun setIndicators(statement: WithRightIndicators, hi: BooleanValue, lo: BooleanValue, eq: BooleanValue) {
        statement.hi?.let {
            indicators[it] = hi
        }
        statement.lo?.let {
            indicators[it] = lo
        }
        statement.eq?.let {
            indicators[it] = eq
        }
    }

    private fun errorDescription(statement: Statement, throwable: Throwable) =
        "Program ${interpretationContext.currentProgramName} - ${statement.simpleDescription()} ${throwable.message}"

    override fun fillDataFrom(dbFile: EnrichedDBFile, record: Record) {
        if (!record.isEmpty()) {
            status.lastFound = true
            record.forEach { field ->
                // dbFieldName could be different by dataDefinition name if file definition has a prefix property
                dbFile.getDataDefinitionName(field.key)?.let { name ->
                    dataDefinitionByName(name)
                }?.apply {
                    assign(this, StringValue(field.value))
                }
                    ?: System.err.println("Field: ${field.key} not found in Symbol Table. Probably reload returns more fields than required")
            }
        } else {
            status.lastFound = false
        }
    }

    override fun dbFile(name: String, statement: Statement): EnrichedDBFile {

        // Nem could be file name or format name
        val dbFile = status.dbFileMap[name]

        require(dbFile != null) {
            "Line: ${statement.position.line()} - File definition $name not found"
        }
        status.lastDBFile = dbFile
        return dbFile
    }

    override fun toSearchValues(searchArgExpression: Expression, fileMetadata: FileMetadata): List<String> {
        val kListName = searchArgExpression.render().uppercase(Locale.getDefault())
        return klists[kListName]!!.mapIndexed { index, name ->
            get(name).asString(fileMetadata.accessFieldsType[index])
        }
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

    override fun rawRender(values: List<Value>) = values.joinToString("") { rawRender(it) }

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

        val logSource = LogSourceData(
            this.interpretationContext.currentProgramName,
            expression.startLine()
        )

        expression.produceExpressionLogRenderer(logSource, expression, value)?.let { renderLog { it } }

        return value
    }

    override fun mult(statement: MultStmt): Value {
        // TODO When will pass my PR for more robustness replace Value.render with NumericValue.bigDecimal
        val rightValue = BigDecimal(eval(statement.left).render())
        val leftValue = BigDecimal(eval(statement.right).render())
        val result = rightValue.multiply(leftValue)
        val type = statement.target.type()
        require(type is NumberType)
        return if (statement.halfAdjust) {
            DecimalValue(result.setScale(type.decimalDigits, RoundingMode.HALF_UP))
        } else {
            DecimalValue(result.setScale(type.decimalDigits, RoundingMode.DOWN))
        }
    }

    override fun div(statement: DivStmt): Value {
        // TODO When will pass my PR for more robustness replace Value.render with NumericValue.bigDecimal
        val dividend = BigDecimal(eval(statement.dividend).render())
        val divisor = BigDecimal(eval(statement.divisor).render())
        val quotient = dividend.divide(divisor, MathContext.DECIMAL128)
        val type = statement.target.type()
        require(type is NumberType)
        // calculation of rest
        // NB. rest based on type of quotient
        if (statement.mvrStatement != null) {
            val restType = statement.mvrStatement.target?.type()
            require(restType is NumberType)
            val truncatedQuotient: BigDecimal = quotient.setScale(type.decimalDigits, RoundingMode.DOWN)
            val rest: BigDecimal = dividend.subtract(truncatedQuotient.multiply(divisor))
            assign(
                statement.mvrStatement.target,
                DecimalValue(rest.setScale(restType.decimalDigits, RoundingMode.DOWN))
            )
        }
        return if (statement.halfAdjust) {
            DecimalValue(quotient.setScale(type.decimalDigits, RoundingMode.HALF_UP))
        } else {
            DecimalValue(quotient.setScale(type.decimalDigits, RoundingMode.DOWN))
        }
    }

    override fun assign(dataDefinition: AbstractDataDefinition, value: Value): Value {
        // if I am working with a record format
        if (dataDefinition.type is RecordFormatType) {
            // currently the only assignable value for a record format type is blank
            if (value is BlanksValue) {
                // I iterate over all the fields of the record format and assign them the blank value
                (dataDefinition as DataDefinition).fields.forEach { field ->
                    assign(globalSymbolTable.dataDefinitionByName(field.name)!!, value)
                }
                return value
            } else {
                error("Cannot assign $value to $dataDefinition")
            }
        } else {
            val coercedValue = coerce(value, dataDefinition.type)
            set(dataDefinition, coercedValue)
            return coercedValue
        }
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

                val logSource = LogSourceData(interpretationContext.currentProgramName, target.array.startLine())
                renderLog { LazyLogEntry.produceAssignmentOfElement(logSource, target.array, index, value) }

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
            is SubarrExpr -> {
                require(value is ArrayValue)
                // replace portion of array with another array
                val start: Int = eval(target.start).asInt().value.toInt()
                val numberOfElement: Int? =
                    if (target.numberOfElements != null) eval(target.numberOfElements).asInt().value.toInt() else null
                val array: ArrayValue = eval(target.array).asArray().copy()
                val to: Int = if (numberOfElement == null) {
                    array.arrayLength()
                } else {
                    (start) + numberOfElement
                } - 1
                // replace elements
                var j = 1
                for (i in start..to) {
                    array.setElement(i, coerce(value.getElement(j), target.type().asArray().element))
                    j++
                }
                return assign(target.array as AssignableExpression, array)
            }
            is LenExpr -> {
                require((target.value as? DataRefExpr)?.variable?.referred is DataDefinition)
                val dataDefinition: DataDefinition = (target.value as DataRefExpr).variable.referred!! as DataDefinition

                when {
                    dataDefinition.type is StringType -> dataDefinition.resizeStringSize(value.asInt().value.toInt())
                    else -> target.todo("Implements redefinition of ${dataDefinition.type.javaClass.name}")
                }
                return value
            }
            is QualifiedAccessExpr -> {
                when (val container = eval(target.container)) {
                    is DataStructValue -> {
                        container[target.field.referred!!]
                        container.set(target.field.referred!!, coerce(value, target.field.referred!!.type))
                    }

                    is OccurableDataStructValue -> {
                        container.value()[target.field.referred!!]
                        container.value().set(target.field.referred!!, coerce(value, target.field.referred!!.type))
                    }
                }
                return value
            }
            is IndicatorExpr -> {
                val index = target.indexExpression?.let { eval(it).asInt().value.toInt() } ?: target.index
                val coercedValue = coerce(value, BooleanType)
                indicators[index] = coercedValue.asBoolean()
                return coercedValue
            }
            is GlobalIndicatorExpr -> {
                return if (value.assignableTo(BooleanType)) {
                    val coercedValue = coerce(value, BooleanType)
                    for (index in ALL_PREDEFINED_INDEXES) {
                        indicators[index] = coercedValue.asBoolean()
                    }
                    coercedValue
                } else {
                    val coercedValue = coerce(value, ArrayType(BooleanType, 100)).asArray()
                    for (index in ALL_PREDEFINED_INDEXES) {
                        indicators[index] = coercedValue.getElement(index).asBoolean()
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

    private fun blankValue(dataDefinition: DataDefinition, forceElement: Boolean = false): Value {
        if (forceElement) TODO()
        return when (dataDefinition.type) {
            is DataStructureType -> createBlankFor(dataDefinition)
            else -> dataDefinition.type.blank()
        }
    }

    private fun getActivationGroupAssignedName(): String? {
        // for some reason, the run with mute annotation do not use RpgProgram.execute then, for now,
        // i need test whether programstack is empty or not
        return when {
            MainExecutionContext.getProgramStack().isEmpty() -> null
            else -> {
                val associatedActivationGroup = MainExecutionContext.getProgramStack().peek()?.activationGroup
                val activationGroup = associatedActivationGroup?.assignedName
                return MainExecutionContext.getConfiguration().jarikoCallback.getActivationGroup.invoke(
                    interpretationContext.currentProgramName, associatedActivationGroup
                )?.assignedName ?: activationGroup
            }
        }
    }

    open fun getMemorySliceId(): MemorySliceId? {
        return getActivationGroupAssignedName()?.let {
            MemorySliceId(activationGroup = it, interpretationContext.currentProgramName)
        }
    }

    /**
     * @return an instance of MemorySliceMgr, return null to disable serialization/deserialization
     * of symboltable
     * */
    open fun getMemorySliceMgr(): MemorySliceMgr? = MainExecutionContext.getMemorySliceMgr()

    /**
     * This function is called before the initialization of the interpreter.
     * */
    open fun beforeInitialization() {}

    /**
     * This function is called after the initialization of the interpreter.
     * */
    open fun afterInitialization(initialValues: Map<String, Value>) {
        globalSymbolTable.restoreFromMemorySlice(getMemorySliceId(), getMemorySliceMgr(), initialValues)
    }

    private fun isExitingInRTMode(): Boolean {

        // LR indicator 'ON' means stateless, doesn't matter if RT is 'ON' too, LR wins!
        // RT indicator 'ON' means statefull (ONLY if LR indicator is 'OFF', as described above)

        val isLROn = indicators[IndicatorType.LR.name.toIndicatorKey()]?.value
        val isRTOn = indicators[IndicatorType.RT.name.toIndicatorKey()]?.value ?: false

        val exitRT = isRTOn && (isLROn == null || !isLROn)

        return MainExecutionContext.getConfiguration().jarikoCallback.exitInRT.invoke(
            interpretationContext.currentProgramName
        ) ?: exitRT
    }

    // I had to introduce this function, which will be called from external, because symbol table cleaning before exits
    // could make failing RpgProgram.execute.
    // The failure depends on whether that the initialvalues are searched in symboltable
    open fun doSomethingAfterExecution() {
        val exitingRT = isExitingInRTMode()
        MainExecutionContext.getAttributes()[getMemorySliceId()?.getAttributeKey()]?.let {
            (it as MemorySlice).persist = exitingRT
        }
        if (!exitingRT) {
            globalSymbolTable.clear()
            // if I exit in LR have to mark inzsrExecuted to false
            status.inzsrExecuted = false
        }
        indicators.clearStatelessIndicators()
    }

    private fun execINZSR(compilationUnit: CompilationUnit) {
        if (!status.inzsrExecuted) {
            val name = "*INZSR"
            status.inzsrExecuted = true
            compilationUnit.subroutines.firstOrNull { subroutine ->
                subroutine.name.equals(other = name, ignoreCase = true)
            }?.let { subroutine ->
                ExecuteSubroutine(
                    subroutine = ReferenceByName(name, subroutine),
                    position = subroutine.position
                ).execute(this)
            }
        }
    }

    private fun executeWithLogging(statement: Statement, source: LogSourceData) {
        if (statement is CompositeStatement) {
            renderLog { LazyLogEntry.produceStatement(source, statement.loggableEntityName, "START") }
        } else {
            renderLog { LazyLogEntry.produceStatement(source, statement.loggableEntityName, "EXEC") }
        }

        if (statement is LoopStatement) {
            renderLog { LazyLogEntry.produceLoopStart(source, statement.loggableEntityName, statement.loopSubject) }
        }

        val executionTime = measureTimeMillis {
            statement.execute(this)
        }

        MainExecutionContext.getLoggingContext()?.recordStatementDuration(statement.loggableEntityName, executionTime)

        if (statement is LoopStatement) {
            renderLog {
                LazyLogEntry.produceLoopEnd(
                    source,
                    statement.loggableEntityName,
                    statement.loopSubject,
                    statement.iterations
                )
            }
        }

        if (statement is CompositeStatement) {
            renderLog { LazyLogEntry.produceStatement(source, statement.loggableEntityName, "END") }
        }

        renderLog { LazyLogEntry.producePerformance(source, statement.loggableEntityName, executionTime) }
    }
}

fun MutableMap<IndicatorKey, BooleanValue>.clearStatelessIndicators() {
    IndicatorType.STATELESS_INDICATORS.forEach {
        this.remove(it)
    }
}

/**
 * @return An instance of StatementReference related to position.
 * */
internal fun Position.relative(): StatementReference {
    val programName =
        if (MainExecutionContext.getProgramStack().empty()) null else MainExecutionContext.getProgramStack().peek().name
    val copyBlocks = programName?.let { MainExecutionContext.getProgramStack().peek().cu.copyBlocks }
    return this.relative(programName, copyBlocks)
}

/**
 * Memory slice context attribute name must to be also string representation of MemorySliceId
 * */
internal fun MemorySliceId.getAttributeKey() = "${MEMORY_SLICE_ATTRIBUTE}_$this"

/**
 * Restores the symbol table from a memory slice.
 *
 * This function is used to restore the state of the symbol table from a previously saved memory slice.
 * This is useful in scenarios where the state of the symbol table needs to be preserved across different
 * executions of the same program, for example in case of stateful programs.
 *
 * @param memorySliceId The ID of the memory slice to restore from. This ID is used to look up the memory slice in the memory slice manager.
 * @param memorySliceMgr The memory slice manager that is used to manage memory slices. It provides functions to create, retrieve and delete memory slices.
 * @param initialValues A map of initial values to be set in the symbol table. These values will not be overwritten by the values from the memory slice.
 */
internal fun ISymbolTable.restoreFromMemorySlice(
    memorySliceId: MemorySliceId?,
    memorySliceMgr: MemorySliceMgr?,
    initialValues: Map<String, Value> = emptyMap()
) {
    memorySliceId?.let { myMemorySliceId ->
        memorySliceMgr?.let {
            MainExecutionContext.getAttributes()[myMemorySliceId.getAttributeKey()] = it.associate(
                memorySliceId = memorySliceId,
                symbolTable = this,
                initSymbolTableEntry = { dataDefinition, storedValue ->
                    // initial values have not to be overwritten
                    if (!initialValues.containsKey(dataDefinition.name)) {
                        this[dataDefinition] = storedValue
                    }
                }
            )
        }
    }
}

private fun ILoggableExpression.produceExpressionLogRenderer(
    source: LogSourceData,
    expression: Expression,
    value: Value
): LazyLogEntry? {
    val metadata = ExpressionLogMetadata(expression, value)

    val actualSource = when {
        metadata.expression.position != null -> source.projectLine(metadata.expression.startLine())
        metadata.expression.parent != null && metadata.expression.parent!!.position != null -> source.projectLine(
            metadata.expression.parent!!.startLine()
        )

        else -> source
    }

    val entry = LogEntry(actualSource, LogChannel.EXPRESSION.getPropertyName(), "EVAL")
    return this.getExpressionLogRenderer(entry, metadata)
}