/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.smeup.rpgparser.interpreter

import com.smeup.dbnative.file.Record
import com.smeup.rpgparser.execution.ErrorEvent
import com.smeup.rpgparser.execution.ErrorEventSource
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.logging.ProgramUsageType
import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.parsing.ast.AssignmentOperator.*
import com.smeup.rpgparser.parsing.facade.dumpSource
import com.smeup.rpgparser.parsing.parsetreetoast.RpgType
import com.smeup.rpgparser.parsing.parsetreetoast.error
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
import kotlin.math.min
import kotlin.system.measureNanoTime
import kotlin.time.Duration.Companion.nanoseconds

object InterpreterConfiguration {
    /**
     * Enable runtime checks during assignments
     */
    var enableRuntimeChecksOnAssignement = false
}

val ALL_PREDEFINED_INDEXES = IndicatorType.Predefined.range

private const val PREV_STMT_EXEC_LINE_ATTRIBUTE = "com.smeup.rpgparser.interpreter.prevStmtExecLine"

open class InternalInterpreter(
    private val systemInterface: SystemInterface,
    private val localizationContext: LocalizationContext = LocalizationContext()
) : InterpreterCore {
    private val configuration = MainExecutionContext.getConfiguration()
    override fun getConfiguration() = configuration

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

    /**
     * NOTE: This should never be accessed as is. Is [getInterpretationContext] to get its value.
     */
    private var _interpretationContext: InterpretationContext = DummyInterpretationContext
    override fun getInterpretationContext(): InterpretationContext {
        return _interpretationContext
    }

    fun setInterpretationContext(interpretationContext: InterpretationContext) {
        this._interpretationContext = interpretationContext
    }

    private val klists = HashMap<String, List<String>>()
    override fun getKlists(): HashMap<String, List<String>> {
        return klists
    }

    private val status = InterpreterStatus(globalSymbolTable, indicators)
    override fun getStatus(): InterpreterStatus {
        return status
    }

    private val loggingContext = InterpreterLoggingContext()

    private val expressionEvaluation = ExpressionEvaluation(systemInterface, localizationContext, status)

    override fun renderLog(producer: () -> LazyLogEntry?) = loggingContext.renderLog(producer)

    override fun exists(dataName: String) = globalSymbolTable.contains(dataName)

    override fun dataDefinitionByName(name: String) = globalSymbolTable.dataDefinitionByName(name)

    override operator fun get(data: AbstractDataDefinition) = globalSymbolTable[data]
    override operator fun get(dataName: String) = globalSymbolTable[dataName]

    open operator fun set(data: AbstractDataDefinition, value: Value) {
        require(data.canBeAssigned(value)) {
            "${value.render()} cannot be assigned to ${data.name} of type ${data.type}"
        }

        val programName = getInterpretationContext().currentProgramName

        renderLog {
            val logSource = { LogSourceData(programName, data.startLine()) }
            val previous = if (data.name in globalSymbolTable) {
                globalSymbolTable[data.name]
            } else null
            LazyLogEntry.produceData(logSource, data, value, previous)
        }

        when (data) {
            // Field are stored within the Data Structure definition
            is FieldDefinition -> {
                val ds = data.parent as DataDefinition
                if (data.declaredArrayInLine != null) {
                    val dataStructValue = get(ds.name) as DataStructValue
                    var startOffset = data.startOffset
                    val size = data.endOffset - data.startOffset

                    fun assignValueToArray(value: Value, data: FieldDefinition) {
                        // Added coerce
                        val valueToAssign = coerce(value, data.type.asArray().element)
                        dataStructValue.setSubstring(
                            startOffset, startOffset + size,
                            data.type.asArray().element.toDataStructureValue(valueToAssign)
                        )
                        startOffset += data.stepSize
                    }

                    if (value is ArrayValue) {
                        // If the size of the arrays are different
                        for (i in 1..min(value.asArray().arrayLength(), data.declaredArrayInLine!!)) {
                            assignValueToArray(value.asArray().getElement(i), data)
                        }
                    } else {
                        for (i in 1..data.declaredArrayInLine!!) {
                            assignValueToArray(value, data)
                        }
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
            }
            else -> {
                renderLog {
                    val logSource = { LogSourceData(programName, data.startLine()) }
                    LazyLogEntry.produceAssignment(logSource, data, value)
                }
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
        val callback = configuration.jarikoCallback
        val initTrace = JarikoTrace(JarikoTraceKind.SymbolTable, "INIT")
        val programName = getInterpretationContext().currentProgramName
        val logSourceProducer = { LogSourceData(programName = programName, line = compilationUnit.startLine()) }

        callback.traceBlock(initTrace) {
            val start = System.nanoTime()

            renderLog { LazyLogEntry.produceInformational(logSourceProducer, "SYMTBLINI", "START") }
            renderLog { LazyLogEntry.produceStatement(logSourceProducer, "SYMTBLINI", "START") }

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
                    when (it) {
                        is DataDefinition -> {
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
                                /*
                                 * In accord to documentation (see https://www.ibm.com/docs/en/i/7.5?topic=codes-plist-identify-parameter-list):
                                 *  when control transfers to called program, at the beginning, the contents of the Result field is placed in
                                 *  the Factor 1 field.
                                 */
                                it.isInPlist(compilationUnit) -> {
                                    val resultName = it.getResultNameByFactor1(compilationUnit)
                                    if (resultName == null || initialValues[resultName] is NullValue) {
                                        blankValue(it)
                                    } else {
                                        initialValues[resultName]
                                    }
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
                                        when (value) {
                                            is DataStructValue -> value.set(field, fieldValue)
                                            is OccurableDataStructValue -> value.initializeField(field, fieldValue)
                                            else -> throw RuntimeException("Expected value to be a DataStructure")
                                        }
                                    }
                                }
                            }
                        }
                        is FieldDefinition -> {
                            if (it.isCompileTimeArray()) {
                                value = toArrayValue(
                                    compilationUnit.compileTimeArray(index++),
                                    (it.type as ArrayType)
                                )
                            }
                        }
                        is InStatementDataDefinition -> {
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

            val initElapsed = (System.nanoTime() - start).nanoseconds

            renderLog { LazyLogEntry.produceInformational(logSourceProducer, "SYMTBLINI", "END") }
            renderLog { LazyLogEntry.produceStatement(logSourceProducer, "SYMTBLINI", "END") }
            renderLog { LazyLogEntry.producePerformanceAndUpdateAnalytics(logSourceProducer, ProgramUsageType.SymbolTable, SymbolTableAction.INIT.name, initElapsed) }
        }

        val loadTrace = JarikoTrace(JarikoTraceKind.SymbolTable, "LOAD")
        callback.traceBlock(loadTrace) {
            renderLog { LazyLogEntry.produceInformational(logSourceProducer, "SYMTBLLOAD", "START") }
            renderLog { LazyLogEntry.produceStatement(logSourceProducer, "SYMTBLLOAD", "START") }

            val loadElapsed = measureNanoTime { afterInitialization(initialValues = initialValues) }.nanoseconds

            renderLog { LazyLogEntry.produceInformational(logSourceProducer, "SYMTBLLOAD", "END") }
            renderLog { LazyLogEntry.produceStatement(logSourceProducer, "SYMTBLLOAD", "END") }
            renderLog { LazyLogEntry.producePerformanceAndUpdateAnalytics(logSourceProducer, ProgramUsageType.SymbolTable, SymbolTableAction.LOAD.name, loadElapsed) }
        }
    }

    /**
     * Retrieves the result name associated with the current `AbstractDataDefinition` instance
     * from the parameter list (PList) of the specified `CompilationUnit`.
     *
     * This function searches the PList for the first parameter where `factor1` is of type `DataRefExpr`
     * and its variable name matches the name of the current `AbstractDataDefinition` (case-insensitively).
     * If such a parameter is found, its associated result name is returned.
     *
     * @param compilationUnit the compilation unit whose entry PList is to be checked
     * @return the result name associated with the matching parameter, or `null` if no match is found
     */
    private fun AbstractDataDefinition.getResultNameByFactor1(compilationUnit: CompilationUnit): String? {
        val resultName = compilationUnit.entryPlist?.params
            ?.filter { plistParam -> plistParam.factor1 is DataRefExpr }
            ?.firstOrNull { plistParamFiltered ->
                (plistParamFiltered.factor1 as DataRefExpr).variable.name.equals(
                    this.name,
                    true
                )
            }
            ?.result?.name
        return resultName
    }

    /**
     * Checks if the current `AbstractDataDefinition` instance is present in the parameter list (PList)
     * of the specified `CompilationUnit`.
     *
     * This function evaluates whether the `AbstractDataDefinition` matches any parameter in the PList
     * by comparing their names (case-insensitively). Parameters in the PList are filtered to include
     * only those with a `factor1` of type `DataRefExpr`.
     *
     * @param compilationUnit the compilation unit whose entry PList is to be checked
     * @return `true` if the `AbstractDataDefinition` is present in the PList, otherwise `false`
     */
    private fun AbstractDataDefinition.isInPlist(compilationUnit: CompilationUnit) = compilationUnit.entryPlist?.params
            ?.filter { plistParam -> plistParam.factor1 is DataRefExpr }
            ?.any { plistParamFiltered -> (plistParamFiltered.factor1 as DataRefExpr).variable.name.equals(this.name, true) } == true

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
        val logHandlers = systemInterface.getAllLogHandlers()
        loggingContext.logHandlers = logHandlers
    }

    /**
     * Execute a [MainBody]
     * @param main The [MainBody]
     */
    private fun execute(main: MainBody) {
        // Execute the main body
        var throwable = kotlin.runCatching {
            execute(main.stmts)
        }.exceptionOrNull()

        val unwrappedStatement = main.stmts.unwrap()

        // Recursive deal with top level goto flow
        while (throwable is GotoTopLevelException || throwable is GotoException) {
            // We need to know the statement unwrapped in order to jump directly into a nested tag
            val (offset, tag) = when (throwable) {
                is GotoException -> Pair(throwable.indexOfTaggedStatement(unwrappedStatement), throwable.tag)
                is GotoTopLevelException -> Pair(throwable.indexOfTaggedStatement(unwrappedStatement), throwable.tag)
                else -> Pair(-1, "")
            }
            if (unwrappedStatement.size <= offset || offset < 0)
                main.error("GOTO offset $offset is not valid. Cannot find TAG '$tag'")
            throwable = kotlin.runCatching {
                executeUnwrappedAt(unwrappedStatement, offset)
            }.exceptionOrNull()
        }

        // If the GOTO-flow threw a different exception, dispatch it to the parent flow
        throwable?.let { throw it }
    }

    fun execute(
        compilationUnit: CompilationUnit,
        initialValues: Map<String, Value>,
        reinitialization: Boolean = true,
        callerParams: Map<String, Value> = initialValues
    ) {
        kotlin.runCatching {
            configureLogHandlers()

            this.status.displayFiles = compilationUnit.displayFiles
            status.callerParams = callerParams.size
            status.params = initialValues.size
            initialize(compilationUnit, caseInsensitiveMap(initialValues), reinitialization)
            execINZSR(compilationUnit)
            if (compilationUnit.minTimeOut == null) {
                execute(compilationUnit.main)
            } else {
                val elapsed = measureNanoTime {
                    execute(compilationUnit.main)
                }.nanoseconds

                if (elapsed.inWholeMilliseconds > compilationUnit.minTimeOut!!) {
                    throw InterpreterTimeoutException(
                        getInterpretationContext().currentProgramName,
                        elapsed.inWholeMilliseconds,
                        compilationUnit.minTimeOut!!
                    )
                }
            }
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

    private fun caseInsensitiveMap(aMap: Map<String, Value>): Map<String, Value> {
        val result = TreeMap<String, Value>(String.CASE_INSENSITIVE_ORDER)
        result.putAll(aMap)
        return result
    }

    override fun execute(statements: List<Statement>) = executeAt(statements, 0)

    /**
     * Execute a list of statements starting for an arbitrary offset.
     * @param statements The list of statements to execute.
     * @param offset The starting offset.
     */
    private fun executeAt(statements: List<Statement>, offset: Int) {
        /*
         * FIXME:
         * This method has a lot of code duplicated with [executeUnwrappedAt] because it can be called
         * passing only a part of statement to execute (like when executing statement bodies).
         * if you manage to refactor this, please also remove this comment.
         */

        var i = offset
        try {
            while (i < statements.size) {
                if (Thread.currentThread().isInterrupted) {
                    throw InterruptedException()
                }
                executeWithMute(statements[i++])
            }
        } catch (e: InterpreterProgramStatusErrorException) {
            /**
             * Program status error not caught from MONITOR statements should end up here
             * We should treat these cases as common Jariko runtime errors
             */
            throw e.fireErrorEvent(e.position)
        }
    }

    /**
     * Execute an unwrapped list of statement.
     * @param unwrappedStatements The unwrapped list of statements. It is up to the caller to ensure statements are unwrapped.
     * @param offset Offset to start the execution from.
     */
    override fun executeUnwrappedAt(unwrappedStatements: List<UnwrappedStatementData>, offset: Int) {
        var index = offset
        while (index < unwrappedStatements.size) {
            val data = unwrappedStatements[index]
            executeWithMute(data.statement)
            index += data.nextOperationOffset + 1
        }
    }

    @Deprecated(message = "No longer used")
    open fun fireOnEnterPgmCallBackFunction() {
    }

    private fun executeWithMute(statement: Statement) {
        val programName = getInterpretationContext().currentProgramName
        renderLog {
            val logSource = { LogSourceData(programName, statement.position.line()) }
            LazyLogEntry.produceLine(logSource)
        }

        try {
            if (statement.isStatementExecutable(getMapOfORs(statement.solveIndicatorValues()))) {
                statement.position?.let { fireCopyObservingCallback(it.start.line) }
                if (configuration.options.mustInvokeOnStatementCallback()) {
                    statement.position?.relative()?.let {
                        configuration.jarikoCallback.onEnterStatement(it.first, it.second)
                    }
                }

                if (statement is MockStatement) {
                    configuration.jarikoCallback.onMockStatement(statement)
                    execute(statement)
                } else {
                    execute(statement)
                }
            }
        } catch (e: ControlFlowException) {
            throw e
        } catch (e: InterpreterProgramStatusErrorException) {
            // If position is not set, consider it to be the statement position
            if (e.position == null) e.position = statement.position
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
            if (statement.muteAnnotations.isNotEmpty()) {
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
        configuration.jarikoCallback.onError.invoke(errorEvent)
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
                .empty() && configuration.options.mustCreateCopyBlocks()
        ) {
            val copyBlocks = MainExecutionContext.getProgramStack().peek().cu.copyBlocks!!
            val previousStatementLine = (MainExecutionContext.getAttributes()[prevStmtAttributeMame()] ?: 0) as Int
            copyBlocks.observeTransitions(
                from = previousStatementLine + if (currentStatementLine > previousStatementLine) 1 else -1,
                to = currentStatementLine,
                onEnter = { copyBlock ->
                    configuration.jarikoCallback.onEnterCopy.invoke(
                        copyBlock.copyId
                    )
                },
                onExit = { copyBlock ->
                    configuration.jarikoCallback.onExitCopy.invoke(
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
        val programName = getInterpretationContext().currentProgramName
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

                    renderLog {
                        val logSource = { LogSourceData(programName, it.startLine()) }
                        LazyLogEntry.produceMute(it, logSource, value)
                    }

                    systemInterface.addExecutedAnnotation(
                        it.position!!.start.line,
                        MuteComparisonAnnotationExecuted(
                            this.getInterpretationContext().currentProgramName,
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
                            this.getInterpretationContext().currentProgramName,
                            it.timeout,
                            line
                        )
                    )
                }

                is MuteFailAnnotation -> {
                    val message = it.message.evalWith(expressionEvaluation)
                    renderLog {
                        val logSource = { LogSourceData(programName, it.startLine()) }
                        LazyLogEntry.produceMute(it, logSource, message)
                    }
                    systemInterface.addExecutedAnnotation(
                        it.position!!.start.line,
                        MuteFailAnnotationExecuted(
                            this.getInterpretationContext().currentProgramName,
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
        "Program ${getInterpretationContext().currentProgramName} - ${statement.simpleDescription()} ${throwable.message}"

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
        // Name could be file name or format name
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
            val programName = this.getInterpretationContext().currentProgramName
            renderLog {
                val logSource = { LogSourceData(programName, dataDefinition.startLine()) }
                LazyLogEntry.produceData(logSource, dataDefinition, newValue, value)
            }
            set(data = dataDefinition, value = newValue)
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

        val programName = this.getInterpretationContext().currentProgramName
        val sourceProvider = { LogSourceData(programName, expression.startLine()) }
        renderLog { LazyLogEntry.produceExpression(sourceProvider, expression, value) }

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
            /*
             * If the source (from `value`) and target (from `dataDefinition`) are two arrays with size of source smaller than target,
             *  copies the last missed values from target to new `coercedValue`.
             */
            if (value is ArrayValue && coercedValue is ArrayValue && dataDefinition.type is ArrayType) {
                if (value.arrayLength() < dataDefinition.type.numberOfElements()) {
                    val targetValue = (globalSymbolTable[dataDefinition] as ArrayValue)
                    for (i in (value.arrayLength() + 1)..targetValue.arrayLength()) {
                        coercedValue.setElement(i, targetValue.getElement(i))
                    }
                }
            }
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

                renderLog {
                    val logSource =
                        { LogSourceData(getInterpretationContext().currentProgramName, target.array.startLine()) }
                    LazyLogEntry.produceAssignmentOfElement(logSource, target.array, index, value)
                }

                arrayValue.setElement(index, evaluatedValue)
                return evaluatedValue
            }
            is SubstExpr -> {
                val oldValue = eval(target.string).asString().value
                val length = target.length?.let { eval(it).asInt().value.toInt() }
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
                    assign(target, eval(PlusExpr(target, value, target.position)))
                }
            MINUS_ASSIGNMENT -> assign(target, eval(MinusExpr(target, value, target.position)))
            MULT_ASSIGNMENT -> assign(target, eval(MultExpr(target, value, target.position)))
            DIVIDE_ASSIGNMENT -> assign(target, eval(DivExpr(target, value, target.position)))
            EXP_ASSIGNMENT -> assign(target, eval(ExpExpr(target, value, target.position)))
        }
    }

    override fun assignEachElement(
        target: AssignableExpression,
        value: Expression,
        operator: AssignmentOperator
    ): Value {
        return when (operator) {
            NORMAL_ASSIGNMENT -> assignEachElement(target, eval(value))
            PLUS_ASSIGNMENT -> assignEachElement(target, eval(PlusExpr(target, value, target.position)))
            MINUS_ASSIGNMENT -> assignEachElement(target, eval(MinusExpr(target, value, target.position)))
            MULT_ASSIGNMENT -> assignEachElement(target, eval(MultExpr(target, value, target.position)))
            DIVIDE_ASSIGNMENT -> assignEachElement(target, eval(DivExpr(target, value, target.position)))
            EXP_ASSIGNMENT -> assignEachElement(target, eval(ExpExpr(target, value, target.position)))
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
                return configuration.jarikoCallback.getActivationGroup.invoke(
                    getInterpretationContext().currentProgramName, associatedActivationGroup
                )?.assignedName ?: activationGroup
            }
        }
    }

    open fun getMemorySliceId(): MemorySliceId? {
        return getActivationGroupAssignedName()?.let {
            MemorySliceId(activationGroup = it, getInterpretationContext().currentProgramName)
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

        return configuration.jarikoCallback.exitInRT.invoke(
            getInterpretationContext().currentProgramName
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

    /**
     * Execute a statement keeping track of its state for observability purposes
     */
    private inline fun execute(statement: Statement) {
        val programName = this.getInterpretationContext().currentProgramName
        val callback = configuration.jarikoCallback
        val trace = toTracePoint(statement)

        val internalExecute = {
            val sourceProducer = if (loggingContext.logsEnabled) {
                { LogSourceData(programName, statement.position.line()) }
            } else null
            sourceProducer?.let { loggingContext.openLoggingScope(statement, it) }
            val attachBeforeProfilingAnnotations = statement.getProfilingAnnotations(ProfilingAnnotationAttachStrategy.AttachToNext)
            val attachAfterProfilingAnnotations = statement.getProfilingAnnotations(ProfilingAnnotationAttachStrategy.AttachToPrevious)

            executeProfiling(attachBeforeProfilingAnnotations)
            val executionTime = measureNanoTime { statement.execute(this) }.nanoseconds
            sourceProducer?.let { loggingContext.closeLoggingScope(statement, programName, sourceProducer, executionTime) }
            executeProfiling(attachAfterProfilingAnnotations)
        }

        trace?.let { callback.traceBlock(it) { internalExecute() } } ?: internalExecute()
    }

    override fun onInterpretationEnd() {
        loggingContext.emitAnalyticsReport()
    }
}
