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

package com.smeup.rpgparser.parsing.ast

import com.smeup.dbnative.file.DBFile
import com.smeup.dbnative.file.Record
import com.smeup.dbnative.file.RecordField
import com.smeup.dbnative.file.Result
import com.smeup.rpgparser.MuteParser
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.logging.ILoggableStatement
import com.smeup.rpgparser.logging.LogChannel
import com.smeup.rpgparser.parsing.parsetreetoast.acceptBody
import com.smeup.rpgparser.parsing.parsetreetoast.error
import com.smeup.rpgparser.parsing.parsetreetoast.isInt
import com.smeup.rpgparser.parsing.parsetreetoast.toAst
import com.smeup.rpgparser.utils.ComparisonOperator
import com.smeup.rpgparser.utils.divideAtIndex
import com.smeup.rpgparser.utils.resizeTo
import com.smeup.rpgparser.utils.substringOfLength
import com.strumenta.kolasu.model.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.time.format.DateTimeFormatter
import java.util.*

interface StatementThatCanDefineData {
    fun dataDefinition(): List<InStatementDataDefinition>
}

interface LoopStatement : ILoggableStatement {
    val loopSubject: String
    val iterations: Long
}

enum class AssignmentOperator(val text: String) {
    NORMAL_ASSIGNMENT("="),
    PLUS_ASSIGNMENT("+="),
    MINUS_ASSIGNMENT("-="),
    MULT_ASSIGNMENT("*="),
    DIVIDE_ASSIGNMENT("/="),
    EXP_ASSIGNMENT("**=");
}

@Serializable
abstract class Statement(
    @Transient override val position: Position? = null,
    var muteAnnotations: MutableList<MuteAnnotation> = mutableListOf()
) : Node(position), ILoggableStatement {

    open fun accept(
        mutes: MutableMap<Int, MuteParser.MuteLineContext>,
        start: Int = 0,
        end: Int
    ): MutableList<MuteAnnotationResolved> {

        // List of mutes successfully attached to the statements
        val mutesAttached: MutableList<MuteAnnotationResolved> = mutableListOf()

        // Extracts the annotation declared before the statement
        val muteToProcess = mutes.filterKeys {
            it < this.position!!.start.line
        }

        muteToProcess.forEach { (line, mute) ->
            this.muteAnnotations.add(
                mute.toAst(
                    position = pos(line, this.position!!.start.column, line, this.position!!.end.column)
                )
            )
            mutesAttached.add(MuteAnnotationResolved(line, this.position!!.start.line))
        }

        return mutesAttached
    }

    open fun simpleDescription() = "Issue executing ${javaClass.simpleName} at line ${startLine()}."

    var indicatorCondition: IndicatorCondition? = null
    var continuedIndicators: HashMap<IndicatorKey, ContinuedIndicator> = HashMap<IndicatorKey, ContinuedIndicator>()

    @Throws(
        ControlFlowException::class,
        IllegalArgumentException::class,
        NotImplementedError::class,
        RuntimeException::class
    )
    abstract fun execute(interpreter: InterpreterCore)
}

/**
 * For statements with this interface there isn't execution but will be called the callback `onMockStatement`.
 */
interface MockStatement

interface CompositeStatement {
    val body: List<Statement>
}

fun List<Statement>.explode(preserveCompositeStatement: Boolean = false): List<Statement> {
    val result = mutableListOf<Statement>()
    forEach {
        if (it is CompositeStatement) {
            if (preserveCompositeStatement) result.add(it)
            result.addAll(it.body.explode(preserveCompositeStatement))
        } else {
            result.add(it)
        }
    }
    return result
}

@Serializable
data class ExecuteSubroutine(var subroutine: ReferenceByName<Subroutine>, override val position: Position? = null) :
    Statement(position) {
    override val loggableEntityName: String
        get() = "EXSR"

    override fun execute(interpreter: InterpreterCore) {
        val programName = interpreter.getInterpretationContext().currentProgramName
        val logSource = { LogSourceData(programName, subroutine.referred!!.position.line()) }
        interpreter.renderLog { LazyLogEntry.produceSubroutineStart(logSource, subroutine.referred!!) }
        try {
            interpreter.execute(subroutine.referred!!.stmts)
        } catch (e: LeaveSrException) {
            // Nothing to do here
        } catch (e: GotoException) {
            if (!e.tag.equals(subroutine.referred!!.tag, true)) throw e
        }
    }

    override fun getStatementLogRenderer(source: LogSourceProvider, action: String): LazyLogEntry {
        val entry = LogEntry(source, LogChannel.STATEMENT.getPropertyName(), action)
        return LazyLogEntry(entry) {
                sep -> "${this.loggableEntityName}$sep${subroutine.name}"
        }
    }

    override fun getResolutionLogRenderer(source: LogSourceProvider): LazyLogEntry {
        val entry = LogEntry(source, LogChannel.RESOLUTION.getPropertyName())
        return LazyLogEntry(entry) {
                sep -> "${this.loggableEntityName}$sep${subroutine.name}"
        }
    }
}

@Serializable
data class SelectStmt(
    var cases: List<SelectCase>,
    var other: SelectOtherClause? = null,
    @Derived val dataDefinition: InStatementDataDefinition? = null,
    override val position: Position? = null
) : Statement(position), CompositeStatement, StatementThatCanDefineData {
    override val loggableEntityName: String
        get() = "SELECT"

    override fun accept(
        mutes: MutableMap<Int, MuteParser.MuteLineContext>,
        start: Int,
        end: Int
    ): MutableList<MuteAnnotationResolved> {

        val muteAttached: MutableList<MuteAnnotationResolved> = mutableListOf()

        cases.forEach {
            muteAttached.addAll(
                acceptBody(it.body, mutes, it.position!!.start.line, it.position.end.line)
            )
        }

        if (other != null) {
            muteAttached.addAll(
                acceptBody(other!!.body, mutes, other!!.position!!.start.line, other!!.position!!.end.line)
            )
        }

        return muteAttached
    }

    override fun dataDefinition(): List<InStatementDataDefinition> = dataDefinition?.let { listOf(it) } ?: emptyList()

    override fun execute(interpreter: InterpreterCore) {
        for (case in this.cases) {
            val result = interpreter.eval(case.condition)

            if (result.asBoolean().value) {
                interpreter.execute(case.body)
                return
            }
        }
        if (this.other != null) {
            interpreter.execute(this.other!!.body)
        }
    }

    @Derived
    override val body: List<Statement>
        get() {
            val result = mutableListOf<Statement>()
            cases.forEach { case ->
                result.addAll(case.body.explode(preserveCompositeStatement = true))
            }
            if (other?.body != null) result.addAll(other!!.body.explode(preserveCompositeStatement = true))
            return result
        }
}

@Serializable
data class CaseStmt(
    var cases: List<CaseClause>,
    var other: CaseOtherClause? = null,
    @Derived val dataDefinition: InStatementDataDefinition? = null,
    override val position: Position? = null
) : Statement(position), StatementThatCanDefineData {
    override val loggableEntityName: String
        get() = "CASE"

    override fun dataDefinition(): List<InStatementDataDefinition> = dataDefinition?.let { listOf(it) } ?: emptyList()

    override fun execute(interpreter: InterpreterCore) {
        for (case in this.cases) {
            val result = interpreter.eval(case.condition)

            if (result.asBoolean().value) {
                executeSubProcedure(interpreter, case.function)
                return
            }
        }
        if (this.other != null) {
            executeSubProcedure(interpreter, other!!.function)
        }
    }

    private fun executeSubProcedure(interpreter: InterpreterCore, subProcedureName: String) {
        val containingCU = this.ancestor(CompilationUnit::class.java)
            ?: throw IllegalStateException("Not contained in a CU")
        containingCU.subroutines.firstOrNull { subroutine ->
            subroutine.name.equals(other = subProcedureName, ignoreCase = true)
        }?.let { subroutine ->
            ExecuteSubroutine(
                subroutine = ReferenceByName(subProcedureName, subroutine),
                position = subroutine.position
            ).execute(interpreter)
        }
    }
}

@Serializable
data class SelectOtherClause(override val body: List<Statement>, override val position: Position? = null) :
    Node(position), CompositeStatement

@Serializable
data class CaseOtherClause(override val position: Position? = null, val function: String) : Node(position)

@Serializable
data class SelectCase(
    val condition: Expression,
    override val body: List<Statement>,
    override val position: Position? = null
) : Node(position), CompositeStatement

@Serializable
data class CaseClause(val condition: Expression, override val position: Position? = null, val function: String) :
    Node(position)

@Serializable
data class EvalFlags(
    val halfAdjust: Boolean = false,
    val maximumNumberOfDigitsRule: Boolean = false,
    val resultDecimalPositionRule: Boolean = false
)

@Serializable
data class EvalStmt(
    val target: AssignableExpression,
    var expression: Expression,
    val operator: AssignmentOperator = AssignmentOperator.NORMAL_ASSIGNMENT,
    val flags: EvalFlags = EvalFlags(),
    override val position: Position? = null
) :
    Statement(position) {
    override val loggableEntityName: String
        get() = "EVAL"

    override fun execute(interpreter: InterpreterCore) {
        // Should I assign it one by one?
        if (target.type().isArray() &&
            target.type().asArray().element.canBeAssigned(expression.type()) && expression.type() !is ArrayType
        ) {
            interpreter.assignEachElement(target, expression, operator)
        } else {
            interpreter.assign(target, expression, operator)
        }
    }

    override fun getStatementLogRenderer(source: LogSourceProvider, action: String): LazyLogEntry {
        val entry = LogEntry(source, LogChannel.STATEMENT.getPropertyName(), action)
        return LazyLogEntry(entry) {
            sep -> "${this.loggableEntityName}$sep${target.render()} ${operator.text} ${expression.render()}$sep"
        }
    }
}

@Serializable
data class SubDurStmt(
    val factor1: Expression?,
    val target: AssignableExpression,
    val factor2: Expression,
    val durationCode: DurationCode,
    @Derived val dataDefinition: InStatementDataDefinition? = null,
    override val position: Position? = null
) :
    Statement(position), StatementThatCanDefineData {
    override val loggableEntityName: String
        get() = "SUBDUR"

    override fun execute(interpreter: InterpreterCore) {
        when (target) {
            is DataRefExpr -> {
                val minuend = factor1 ?: target
                val subtrahend = factor2
                interpreter.assign(target, interpreter.eval(DiffExpr(minuend, subtrahend, durationCode)))
            }

            else -> throw UnsupportedOperationException("Data reference required: $this")
        }
    }

    override fun dataDefinition(): List<InStatementDataDefinition> = dataDefinition?.let { listOf(it) } ?: emptyList()
}

@Serializable
data class MoveStmt(
    val operationExtender: String?,
    val target: AssignableExpression,
    var expression: Expression,
    @Derived val dataDefinition: InStatementDataDefinition? = null,
    override val position: Position? = null
) :
    Statement(position), StatementThatCanDefineData {
    override val loggableEntityName: String
        get() = "MOVE"

    override fun execute(interpreter: InterpreterCore) {
        move(operationExtender, target, expression, interpreter)
    }

    override fun dataDefinition() = dataDefinition?.let { listOf(it) } ?: emptyList()
}

@Serializable
data class MoveAStmt(
    val operationExtender: String?,
    val target: AssignableExpression,
    var expression: Expression,
    @Derived val dataDefinition: InStatementDataDefinition? = null,
    override val position: Position? = null
) :
    Statement(position), StatementThatCanDefineData {
    override val loggableEntityName: String
        get() = "MOVEA"

    override fun execute(interpreter: InterpreterCore) {
        movea(operationExtender, target, expression, interpreter)
    }

    override fun dataDefinition() = dataDefinition?.let { listOf(it) } ?: emptyList()

    override fun getStatementLogRenderer(source: LogSourceProvider, action: String): LazyLogEntry {
        val entry = LogEntry(source, LogChannel.STATEMENT.getPropertyName(), action)
        return LazyLogEntry(entry) {
                sep -> "${this.loggableEntityName}${sep}${expression.render()} TO ${target.render()}"
        }
    }
}

@Serializable
data class MoveLStmt(
    val operationExtender: String?,
    val target: AssignableExpression,
    @Derived val dataDefinition: InStatementDataDefinition? = null,
    var expression: Expression,
    override val position: Position? = null
) : Statement(position), StatementThatCanDefineData {
    override val loggableEntityName: String
        get() = "MOVEL"

    override fun dataDefinition(): List<InStatementDataDefinition> {
        if (dataDefinition != null) {
            return listOf(dataDefinition)
        }
        return emptyList()
    }

    override fun execute(interpreter: InterpreterCore) {
        movel(operationExtender, target, expression, interpreter)
    }

    override fun getStatementLogRenderer(source: LogSourceProvider, action: String): LazyLogEntry {
        val entry = LogEntry(source, LogChannel.STATEMENT.getPropertyName(), action)
        return LazyLogEntry(entry) {
            sep -> "${this.loggableEntityName}${sep}${expression.render()} TO ${target.render()}"
        }
    }
}

@Serializable
abstract class AbstractReadEqualStmt(
    @Transient open val searchArg: Expression? = null, // Factor1
    @Transient open val name: String = "", // Factor 2
    @Transient override val position: Position? = null

) : Statement(position) {
    override fun execute(interpreter: InterpreterCore) {
        val dbFile = interpreter.dbFile(name, this)
        val kList: List<String> = when (searchArg) {
            null -> emptyList()
            else -> searchArg!!.createKList(dbFile.jarikoMetadata, interpreter)
        }

        val result = when (searchArg) {
            null -> read(dbFile)
            else -> read(dbFile, kList)
        }
        interpreter.fillDataFrom(dbFile, result.record)
    }

    abstract fun read(dbFile: DBFile, kList: List<String>? = null): Result
}

@Serializable
abstract class AbstractReadStmt(
    @Transient open val name: String = "", // Factor 2
    @Transient override val position: Position? = null
) : Statement(position) {
    override fun execute(interpreter: InterpreterCore) {
        val dbFile = interpreter.dbFile(name, this)
        val result = readOp(dbFile)
        interpreter.fillDataFrom(dbFile, result.record)
    }

    abstract fun readOp(dbFile: DBFile): Result
}

@Serializable
abstract class AbstractStoreStmt(
    @Transient open val name: String = "", // Factor 2
    @Transient override val position: Position? = null
) : Statement(position) {

    override fun execute(interpreter: InterpreterCore) {
        val dbFile = interpreter.dbFile(name, this)
        val record = Record()
        dbFile.fileMetadata.fields.forEach { field ->
            interpreter.dataDefinitionByName(field.name)?.let { dataDefinition ->
                RecordField(field.name, interpreter[dataDefinition].asString().value)
            }?.apply {
                record.add(this)
            } ?: error("Not found in SymbolTable dbFieldName: ${field.name}")
        }
        store(dbFile, record)
    }

    abstract fun store(dbFile: DBFile, record: Record): Result
}

@Serializable
abstract class AbstractSetStmt(
    // this one is a dummy expression needed to initialize because of "transient" annotation
    @Transient open val searchArg: Expression = StringLiteral(""), // Factor1
    @Transient open val name: String = "", // Factor 2
    @Transient override val position: Position? = null
) : Statement(position) {
    override fun execute(interpreter: InterpreterCore) {
        val dbFile = interpreter.dbFile(name, this)
        val kList: List<String> = searchArg.createKList(dbFile.jarikoMetadata, interpreter)
        interpreter.getStatus().lastFound = set(dbFile, kList)
    }

    abstract fun set(dbFile: DBFile, kList: List<String>): Boolean
}

// TODO add other parameters
@Serializable
data class ChainStmt(
    override val searchArg: Expression, // Factor1
    override val name: String, // Factor 2
    override val position: Position? = null
) : AbstractReadEqualStmt(searchArg, name, position) {
    override val loggableEntityName: String
        get() = "CHAIN"

    override fun read(dbFile: DBFile, kList: List<String>?): Result = dbFile.chain(kList!!)
}

@Serializable
data class ReadEqualStmt(
    override val searchArg: Expression?,
    override val name: String,
    override val position: Position? = null
) : AbstractReadEqualStmt(searchArg = searchArg, name = name, position = position) {
    override val loggableEntityName: String
        get() = "READE"

    override fun read(dbFile: DBFile, kList: List<String>?): Result {
        return if (kList == null) {
            dbFile.readEqual()
        } else {
            dbFile.readEqual(kList)
        }
    }
}

@Serializable
data class ReadPreviousEqualStmt(
    override val searchArg: Expression?,
    override val name: String,
    override val position: Position? = null
) : AbstractReadEqualStmt(searchArg = searchArg, name = name, position = position) {
    override val loggableEntityName: String
        get() = "READPE"

    override fun read(dbFile: DBFile, kList: List<String>?): Result {
        return if (kList == null) {
            dbFile.readPreviousEqual()
        } else {
            dbFile.readPreviousEqual(kList)
        }
    }
}

@Serializable
data class ReadStmt(override val name: String, override val position: Position?) : AbstractReadStmt(name, position) {
    override val loggableEntityName: String
        get() = "READ"

    override fun readOp(dbFile: DBFile) = dbFile.read()
}

@Serializable
data class ReadPreviousStmt(override val name: String, override val position: Position?) :
    AbstractReadStmt(name, position) {
    override val loggableEntityName: String
        get() = "READP"

    override fun readOp(dbFile: DBFile) = dbFile.readPrevious()
}

@Serializable
data class WriteStmt(override val name: String, override val position: Position?) :
    AbstractStoreStmt(name = name, position = position) {
    override val loggableEntityName: String
        get() = "WRITE"

    override fun store(dbFile: DBFile, record: Record) = dbFile.write(record)
}

@Serializable
data class UpdateStmt(override val name: String, override val position: Position?) :
    AbstractStoreStmt(name = name, position = position) {
    override val loggableEntityName: String
        get() = "UPDATE"

    override fun store(dbFile: DBFile, record: Record) = dbFile.update(record)
}

@Serializable
data class DeleteStmt(override val name: String, override val position: Position?) :
    AbstractStoreStmt(name = name, position = position) {
    override val loggableEntityName: String
        get() = "DELETE"

    override fun store(dbFile: DBFile, record: Record) = dbFile.delete(record)
}

@Serializable
data class SetllStmt(
    override val searchArg: Expression,
    override val name: String,
    override val position: Position?
) : AbstractSetStmt(
    searchArg = searchArg,
    name = name,
    position = position
) {
    override val loggableEntityName: String
        get() = "SETLL"

    override fun set(dbFile: DBFile, kList: List<String>) = dbFile.setll(kList)
}

@Serializable
data class SetgtStmt(
    override val searchArg: Expression,
    override val name: String,
    override val position: Position?
) : AbstractSetStmt(searchArg = searchArg, name = name, position = position) {
    override val loggableEntityName: String
        get() = "SETGT"

    override fun set(dbFile: DBFile, kList: List<String>) = dbFile.setgt(kList)
}

@Serializable
data class CheckStmt(
    val comparatorString: Expression, // Factor1
    val baseString: Expression,
    val start: Int = 1,
    val wrongCharPosition: AssignableExpression?,
    @Derived val dataDefinition: InStatementDataDefinition? = null,
    override val position: Position? = null
) : Statement(position), StatementThatCanDefineData {
    override val loggableEntityName: String
        get() = "CHECK"

    override fun execute(interpreter: InterpreterCore) {
        var baseString = interpreter.eval(this.baseString).asString().value
        if (this.baseString is DataRefExpr) {
            baseString = baseString.padEnd(this.baseString.size())
        }
        val charSet = interpreter.eval(comparatorString).asString().value
        val wrongIndex = wrongCharPosition
        interpreter.getStatus().lastFound = false
        if (wrongIndex != null) {
            interpreter.assign(wrongIndex, IntValue.ZERO)
        }
        baseString.substring(start - 1).forEachIndexed { i, c ->
            if (!charSet.contains(c)) {
                if (wrongIndex != null) {
                    interpreter.assign(wrongIndex, IntValue((i + start).toLong()))
                }
                interpreter.getStatus().lastFound = true
                return
            }
        }
    }

    override fun dataDefinition(): List<InStatementDataDefinition> = dataDefinition?.let { listOf(it) } ?: emptyList()
}

@Serializable
data class CallStmt(
    val expression: Expression,
    val params: List<PlistParam>,
    val errorIndicator: IndicatorKey? = null,
    override val position: Position? = null
) : Statement(position), StatementThatCanDefineData {
    override val loggableEntityName: String
        get() = "CALL"

    override fun dataDefinition(): List<InStatementDataDefinition> {
        return params.mapNotNull() {
            it.dataDefinition
        }
    }

    override fun execute(interpreter: InterpreterCore) {
        val programToCall = interpreter.eval(expression).asString().value.trim()
        MainExecutionContext.setExecutionProgramName(programToCall)
        val program: Program?
        try {
            program = interpreter.getSystemInterface().findProgram(programToCall)
            if (errorIndicator != null) {
                interpreter.getIndicators()[errorIndicator] = BooleanValue.FALSE
            }
        } catch (e: Exception) {
            if (errorIndicator == null) {
                throw e
            }
            interpreter.getIndicators()[errorIndicator] = BooleanValue.TRUE
            return
        }

        require(program != null) {
            "Line: ${this.position.line()} - Program '$programToCall' cannot be found"
        }

        val params = this.params.mapIndexed { index, it ->
            if (it.dataDefinition != null) {
                // handle declaration of new variable
                if (it.dataDefinition.initializationValue != null) {
                    if (!interpreter.exists(it.param.name)) {
                        interpreter.assign(it.dataDefinition, interpreter.eval(it.dataDefinition.initializationValue))
                    } else {
                        interpreter.assign(
                            interpreter.dataDefinitionByName(it.param.name)!!,
                            interpreter.eval(it.dataDefinition.initializationValue)
                        )
                    }
                } else {
                    if (!interpreter.exists(it.param.name)) {
                        interpreter.assign(it.dataDefinition, interpreter.eval(BlanksRefExpr()))
                    }
                }
            } else {
                // handle initialization value without declaration of new variables
                // change the value of parameter with initialization value
                if (it.initializationValue != null) {
                    interpreter.assign(
                        interpreter.dataDefinitionByName(it.param.name)!!,
                        interpreter.eval(it.initializationValue)
                    )
                }
            }
            require(program.params().size > index) {
                "Line: ${this.position.line()} - Parameter nr. ${index + 1} can't be found"
            }
            program.params()[index].name to interpreter[it.param.name]
        }.toMap(LinkedHashMap())

        val paramValuesAtTheEnd =
            try {
                interpreter.getSystemInterface().registerProgramExecutionStart(program, params)
                kotlin.run {
                    val callProgramHandler = MainExecutionContext.getConfiguration().options.callProgramHandler
                    // call program.execute only if callProgramHandler.handleCall do nothing
                    callProgramHandler?.handleCall?.invoke(programToCall, interpreter.getSystemInterface(), params)
                        ?: program.execute(interpreter.getSystemInterface(), params)
                }.apply {
                    if (errorIndicator != null) {
                        interpreter.getIndicators()[errorIndicator] = BooleanValue.FALSE
                    }
                }
            } catch (e: Exception) { // TODO Catch a more specific exception?
                if (errorIndicator == null) {
                    throw e
                }
                interpreter.getIndicators()[errorIndicator] = BooleanValue.TRUE
                MainExecutionContext.getConfiguration().jarikoCallback.onCallPgmError.invoke(popRuntimeErrorEvent())
                null
            }
        paramValuesAtTheEnd?.forEachIndexed { index, value ->
            if (this.params.size > index) {
                interpreter.assign(this.params[index].param.referred!!, value)
            }
        }
    }

    override fun getStatementLogRenderer(source: LogSourceProvider, action: String): LazyLogEntry {
        val entry = LogEntry(source, LogChannel.STATEMENT.getPropertyName(), action)
        return LazyLogEntry(entry) {
            sep -> "${this.loggableEntityName}$sep${expression.render()}"
        }
    }

    override fun getResolutionLogRenderer(source: LogSourceProvider): LazyLogEntry {
        val entry = LogEntry(source, LogChannel.RESOLUTION.getPropertyName())
        return LazyLogEntry(entry) {
                sep -> "${this.loggableEntityName}$sep${expression.render()}"
        }
    }
}

@Serializable
data class CallPStmt(
    val functionCall: FunctionCall,
    val errorIndicator: IndicatorKey? = null,
    override val position: Position? = null
) : Statement(position), StatementThatCanDefineData {
    override val loggableEntityName: String
        get() = "CALLP"

    override fun dataDefinition(): List<InStatementDataDefinition> {
        return emptyList()
    }

    override fun execute(interpreter: InterpreterCore) {
        runCatching {
            val expressionEvaluation = ExpressionEvaluation(
                interpreter.getSystemInterface(),
                LocalizationContext(),
                interpreter.getStatus()
            )
            expressionEvaluation.eval(functionCall)
        }.onFailure { e ->
            // TODO Catch a more specific exception?
            errorIndicator ?: throw e
            interpreter.getIndicators()[errorIndicator] = BooleanValue.TRUE
        }
    }

    override fun getStatementLogRenderer(source: LogSourceProvider, action: String): LazyLogEntry {
        val entry = LogEntry(source, LogChannel.STATEMENT.getPropertyName(), action)
        return LazyLogEntry(entry) {
                sep -> "${this.loggableEntityName}$sep${functionCall.render()}"
        }
    }

    override fun getResolutionLogRenderer(source: LogSourceProvider): LazyLogEntry {
        val entry = LogEntry(source, LogChannel.RESOLUTION.getPropertyName())
        return LazyLogEntry(entry) {
                sep -> "${this.loggableEntityName}$sep${functionCall.render()}"
        }
    }
}

@Serializable
data class KListStmt(
    val name: String,
    val fields: List<String>,
    val dataDefinitions: List<InStatementDataDefinition>? = null,
    override val position: Position?
) : Statement(position),
    StatementThatCanDefineData {
    override val loggableEntityName: String
        get() = "KLIST"

    private val normalizedName: String = name.uppercase(Locale.getDefault())

    override fun dataDefinition(): List<InStatementDataDefinition> {
        val klistDefinition = InStatementDataDefinition(normalizedName, KListType)
        val innerDefinitions = dataDefinitions ?: emptyList()
        return listOf(klistDefinition) + innerDefinitions
    }

    override fun execute(interpreter: InterpreterCore) {
        interpreter.getKlists()[normalizedName] = fields
    }
}

@Serializable
data class MonitorStmt(
    @SerialName("body")
    val monitorBody: List<Statement>,
    val onErrorClauses: List<OnErrorClause> = emptyList(),
    override val position: Position? = null
) : Statement(position), CompositeStatement {
    override val loggableEntityName: String
        get() = "MONITOR"

    // Since this property is a collection built up from multiple parts, this annotation
    // is necessary to avoid that the same node is processed more than ones, thing that it would cause that the same
    // ErrorEvent is fired more times
    @Derived
    @Transient
    override val body: List<Statement> = mutableListOf<Statement>().apply {
        addAll(monitorBody)
        onErrorClauses.forEach { addAll(it.body) }
    }

    override fun accept(
        mutes: MutableMap<Int, MuteParser.MuteLineContext>,
        start: Int,
        end: Int
    ): MutableList<MuteAnnotationResolved> {
        // check if the annotation is just before the ELSE
        val muteAttached: MutableList<MuteAnnotationResolved> = mutableListOf()

        // Process the body statements
        muteAttached.addAll(
            acceptBody(monitorBody, mutes, this.position!!.start.line, this.position.end.line)
        )

        // Process the ON ERROR
        onErrorClauses.forEach {
            muteAttached.addAll(
                acceptBody(it.body, mutes, it.position!!.start.line, it.position.end.line)
            )
        }

        return muteAttached
    }

    override fun execute(interpreter: InterpreterCore) {
        try {
            interpreter.execute(this.monitorBody)
        } catch (_: Exception) {
            onErrorClauses.forEach {
                interpreter.execute(it.body)
            }
        }
    }
}

@Serializable
data class IfStmt(
    val condition: Expression,
    @SerialName("body")
    val thenBody: List<Statement>,
    val elseIfClauses: List<ElseIfClause> = emptyList(),
    val elseClause: ElseClause? = null,
    override val position: Position? = null
) : Statement(position), CompositeStatement {
    override val loggableEntityName: String
        get() = "IF"

    // Since that this property is a collection achieved from thenBody + elseIfClauses + elseClause, this annotation
    // is necessary to avoid that the same node is processed more than ones, thing that it would cause that the same
    // ErrorEvent is fired more times
    @Derived
    @Transient
    override val body: List<Statement> = mutableListOf<Statement>().apply {
        addAll(thenBody)
        elseIfClauses.forEach { addAll(it.body) }
        elseClause?.body?.let { addAll(it) }
    }

    override fun accept(
        mutes: MutableMap<Int, MuteParser.MuteLineContext>,
        start: Int,
        end: Int
    ): MutableList<MuteAnnotationResolved> {
        // check if the annotation is just before the ELSE
        val muteAttached: MutableList<MuteAnnotationResolved> = mutableListOf()

        // Process the body statements
        muteAttached.addAll(
            acceptBody(thenBody, mutes, this.position!!.start.line, this.position.end.line)
        )

        // Process the ELSE IF
        elseIfClauses.forEach {
            muteAttached.addAll(
                acceptBody(it.body, mutes, it.position!!.start.line, it.position.end.line)
            )
        }

        // Process the ELSE
        if (elseClause != null) {
            muteAttached.addAll(
                acceptBody(elseClause.body, mutes, elseClause.position!!.start.line, elseClause.position.end.line)
            )
        }

        return muteAttached
    }

    override fun execute(interpreter: InterpreterCore) {
        val condition = interpreter.eval(condition)
        if (condition.asBoolean().value) {
            interpreter.execute(this.thenBody)
        } else {
            for (elseIfClause in elseIfClauses) {
                val c = interpreter.eval(elseIfClause.condition)
                if (c.asBoolean().value) {
                    interpreter.execute(elseIfClause.body)
                    return
                }
            }
            if (elseClause != null) {
                interpreter.execute(elseClause.body)
            }
        }
    }

    override fun getStatementLogRenderer(source: LogSourceProvider, action: String): LazyLogEntry {
        val entry = LogEntry(source, LogChannel.STATEMENT.getPropertyName(), action)
        return LazyLogEntry(entry) {
            sep -> "${this.loggableEntityName}${sep}${condition.render()}"
        }
    }
}

@Serializable
data class OnErrorClause(override val body: List<Statement>, override val position: Position? = null) : Node(position),
    CompositeStatement

@Serializable
data class ElseClause(override val body: List<Statement>, override val position: Position? = null) : Node(position),
    CompositeStatement

@Serializable
data class ElseIfClause(
    val condition: Expression,
    override val body: List<Statement>,
    override val position: Position? = null
) : Node(position), CompositeStatement

@Serializable
data class SetStmt(
    val valueSet: ValueSet,
    val indicators: List<AssignableExpression>,
    override val position: Position? = null
) : Statement(position) {
    override val loggableEntityName: String
        get() = "SET"

    enum class ValueSet {
        ON,
        OFF
    }

    override fun execute(interpreter: InterpreterCore) {
        indicators.forEach {
            when (it) {
                is IndicatorExpr -> interpreter.getIndicators()[it.index] = BooleanValue(valueSet == ValueSet.ON)
                else -> TODO()
            }
        }
    }
}

@Serializable
data class ReturnStmt(val expression: Expression?, override val position: Position? = null) : Statement(position) {
    override val loggableEntityName: String
        get() = "RETURN"

    override fun execute(interpreter: InterpreterCore) {
        // set the RT indicator always on
        interpreter.getIndicators()[IndicatorType.RT.name.toIndicatorKey()] = BooleanValue.TRUE
        val returnValue = expression?.let { interpreter.eval(expression) }
        throw ReturnException(returnValue)
    }
}

// A Plist is a list of parameters
@Serializable
data class PlistStmt(
    val params: List<PlistParam>,
    val isEntry: Boolean,
    override val position: Position? = null
) : Statement(position), StatementThatCanDefineData {
    override val loggableEntityName: String
        get() = "PLIST"

    override fun dataDefinition(): List<InStatementDataDefinition> {
        val allDataDefinitions = params.mapNotNull { it.dataDefinition }
        // We do not want params in plist to shadow existing data definitions
        // They are implicit data definitions only when explicit data definitions are not present
        val filtered = allDataDefinitions.filter { paramDataDef ->
            val containingCU = this.ancestor(CompilationUnit::class.java)
                ?: throw IllegalStateException("Not contained in a CU")
            containingCU.dataDefinitions.none { it.name == paramDataDef.name }
        }
        return filtered
    }

    override fun execute(interpreter: InterpreterCore) {
        params.forEach {
            if (interpreter.getGlobalSymbolTable().contains(it.param.name)) {
                interpreter.getGlobalSymbolTable()[it.param.name]
            }
        }
    }

    override fun getStatementLogRenderer(source: LogSourceProvider, action: String): LazyLogEntry {
        val entry = LogEntry(source, LogChannel.STATEMENT.getPropertyName(), action)
        return LazyLogEntry(entry) {
            sep -> "${this.loggableEntityName}${sep}${params.mapNotNull { it.dataDefinition?.name }.joinToString() }}"
        }
    }
}

@Serializable
data class PlistParam(
    val param: ReferenceByName<AbstractDataDefinition>,
    // TODO @Derived????
    @Derived val dataDefinition: InStatementDataDefinition? = null,
    override val position: Position? = null,
    val initializationValue: Expression? = null
) : Node(position), StatementThatCanDefineData {
    override fun dataDefinition(): List<InStatementDataDefinition> {
        if (dataDefinition != null) {
            return listOf(dataDefinition)
        }
        return emptyList()
    }
}

@Serializable
data class ClearStmt(
    val value: Expression,
    @Derived val dataDefinition: InStatementDataDefinition? = null,
    override val position: Position? = null
) : Statement(position), StatementThatCanDefineData {
    override val loggableEntityName: String
        get() = "CLEAR"

    override fun dataDefinition(): List<InStatementDataDefinition> {
        if (dataDefinition != null) {
            return listOf(dataDefinition)
        }
        return emptyList()
    }

    override fun execute(interpreter: InterpreterCore) {
        when (value) {
            is DataRefExpr -> {
                val newValue: Value
                /*
                 If DataRef is referred to an OccurableDataStructureType read the
                 last cursor position and assisgn it to new blanck object
                 */
                if (this.value.variable.referred?.type is OccurableDataStructureType) {
                    val origValue = interpreter.eval(value) as OccurableDataStructValue
                    newValue = interpreter.assign(value, BlanksRefExpr()) as OccurableDataStructValue
                    newValue.pos(origValue.occurrence)
                } else {
                    interpreter.assign(value, BlanksRefExpr())
                }
            }

            is IndicatorExpr -> {
                interpreter.assign(value, BlanksRefExpr())
            }

            is ArrayAccessExpr -> {
                interpreter.assign(value, BlanksRefExpr())
            }

            else -> throw UnsupportedOperationException("I do not know how to clear ${this.value}")
        }
    }

    override fun getStatementLogRenderer(source: LogSourceProvider, action: String): LazyLogEntry {
        val entry = LogEntry(source, LogChannel.STATEMENT.getPropertyName(), action)
        return LazyLogEntry(entry) {
            sep -> "${this.loggableEntityName}${sep}${value.render()}"
        }
    }
}

@Serializable
data class DefineStmt(
    val originalName: String,
    val newVarName: String,
    override val position: Position? = null
) : Statement(position), StatementThatCanDefineData {
    override val loggableEntityName: String
        get() = "DEFINE"

    override fun dataDefinition(): List<InStatementDataDefinition> {
        val containingCU = this.ancestor(CompilationUnit::class.java)
            ?: return emptyList()

        val originalDataDefinition = containingCU.dataDefinitions.find { it.name == originalName }
        // If definition was not found as a 'standalone' 'D spec' declaration,
        // maybe it can be found as a sub-field of DS in 'D specs' declarations
        containingCU.dataDefinitions.forEach {
            it.fields.forEach {
                if (it.name == originalName) {
                    return listOf(InStatementDataDefinition(newVarName, it.type, position))
                }
            }
        }

        if (originalDataDefinition != null) {
            return listOf(InStatementDataDefinition(newVarName, originalDataDefinition.type, position))
        } else {
            if (!this.enterInStack()) {
                // This check is necessary to avoid infinite recursion
                throw Error("Data reference $originalName not resolved")
            }
            /* Checking if the context is main or subroutine, to execute the right search about the InStatementsDataDefinition. */
            val inStatementDataDefinition = when (this.parent) {
                is Subroutine -> {
                    this.findInLocalScope(originalName)
                        ?: this.findInGlobalScope(containingCU, originalName)
                        ?: throw Error("Data reference $originalName not resolved")
                }

                is MainBody -> {
                    this.findInGlobalScope(containingCU, originalName) ?: throw Error("Data reference $originalName not resolved")
                }

                else -> throw Error("Data reference $originalName not resolved")
            }

            this.exitFromStack()
            return listOf(InStatementDataDefinition(newVarName, inStatementDataDefinition.type, position))
        }
    }

    override fun execute(interpreter: InterpreterCore) {
        // Nothing to do here
    }

    private fun findInGlobalScope(cu: CompilationUnit, search: String): InStatementDataDefinition? {
        return cu.main.stmts.findInStatementDataDefinition(originalName, this) ?: cu.subroutines.firstNotNullOf {
            it.stmts.findInStatementDataDefinition(search, this)
        }
    }

    private fun findInLocalScope(search: String): InStatementDataDefinition? {
        return (this.parent as Subroutine).stmts.findInStatementDataDefinition(search, this)
    }

    /**
     * Get the list of stack. This could be necessary, in example, to avoid recursive calls.
     * @return List of `DefineStmt` or empty list.
     */
    fun getStack(): List< DefineStmt
> {
        return (MainExecutionContext.getAttributes().computeIfAbsent("DefineStmt.callStack") {
        mutableSetOf<DefineStmt>()
        } as MutableSet<DefineStmt>).toList()
    }

    /**
     * Receiver wants to enter in call stack
     * @return false if the receiver cannot enter
     */
    private fun enterInStack(): Boolean {
        val stack = MainExecutionContext.getAttributes().computeIfAbsent("DefineStmt.callStack") {
            mutableSetOf<DefineStmt>()
        } as MutableSet<DefineStmt>
        return stack.add(this)
    }

    /**
     * Receiver will exit from call stack
     */
    private fun exitFromStack(): Boolean {
        val stack = MainExecutionContext.getAttributes().computeIfAbsent("DefineStmt.callStack") {
            mutableSetOf<DefineStmt>()
        } as MutableSet<DefineStmt>
        return stack.remove(this)
    }
}

/**
 * From a list of Statements, finds an inline definition (InStatementDataDefinition) based of `originalName`.
 */
private fun List<Statement>.findInStatementDataDefinition(originalName: String, contextToExclude: DefineStmt): InStatementDataDefinition? {
    return this.filterIsInstance<StatementThatCanDefineData>()
                .filter { !contextToExclude.getStack().contains(it) }
                .asSequence()
                .map(StatementThatCanDefineData::dataDefinition)
                .flatten()
                .firstOrNull { it.name == originalName }
}

interface WithRightIndicators {
    fun allPresent(): Boolean = hi != null && lo != null && eq != null

    val hi: IndicatorKey?
    val lo: IndicatorKey?
    val eq: IndicatorKey?
}

@Serializable
data class RightIndicators(
    override val hi: IndicatorKey?,
    override val lo: IndicatorKey?,
    override val eq: IndicatorKey?
) : WithRightIndicators

@Serializable
data class CompStmt(
    val left: Expression,
    val right: Expression,
    val rightIndicators: WithRightIndicators,
    override val position: Position? = null
) : Statement(position), WithRightIndicators by rightIndicators {
    override val loggableEntityName: String
        get() = "COMP"

    override fun execute(interpreter: InterpreterCore) {
        when (interpreter.compareExpressions(left, right, interpreter.getLocalizationContext().charset)) {
            GREATER -> interpreter.setIndicators(this, BooleanValue.TRUE, BooleanValue.FALSE, BooleanValue.FALSE)
            SMALLER -> interpreter.setIndicators(this, BooleanValue.FALSE, BooleanValue.TRUE, BooleanValue.FALSE)
            else -> interpreter.setIndicators(this, BooleanValue.FALSE, BooleanValue.FALSE, BooleanValue.TRUE)
        }
    }

    override fun getStatementLogRenderer(source: LogSourceProvider, action: String): LazyLogEntry {
        val entry = LogEntry(source, LogChannel.STATEMENT.getPropertyName(), action)
        return LazyLogEntry(entry) {
            sep -> "${this.loggableEntityName}${sep}FACTOR1$sep${left.render()}${sep}FACTOR2$sep${right.render()}${sep}HI$sep$hi${sep}LO$sep$lo${sep}EQ$sep$eq"
        }
    }
}

@Serializable
data class ZAddStmt(
    val target: AssignableExpression,
    @Derived val dataDefinition: InStatementDataDefinition? = null,
    var expression: Expression,
    override val position: Position? = null
) :
    Statement(position), StatementThatCanDefineData {
    override val loggableEntityName: String
        get() = "ZADD"

    override fun dataDefinition(): List<InStatementDataDefinition> {
        if (dataDefinition != null) {
            return listOf(dataDefinition)
        }
        return emptyList()
    }

    override fun execute(interpreter: InterpreterCore) {
        interpreter.assign(target, interpreter.eval(expression))
    }
}

@Serializable
data class MultStmt(
    val target: AssignableExpression,
    val halfAdjust: Boolean = false,
    val factor1: Expression?,
    val factor2: Expression,
    @Derived val dataDefinition: InStatementDataDefinition? = null,
    override val position: Position? = null
) : Statement(position), StatementThatCanDefineData {
    override val loggableEntityName: String
        get() = "MULT"

    @Derived
    val left: Expression
        get() = factor1 ?: target

    @Derived
    val right: Expression
        get() = factor2

    override fun execute(interpreter: InterpreterCore) {
        interpreter.assign(target, interpreter.mult(this))
    }

    override fun dataDefinition() = dataDefinition?.let { listOf(it) } ?: emptyList()
}

@Serializable
data class DivStmt(
    val target: AssignableExpression,
    val halfAdjust: Boolean = false,
    val factor1: Expression?,
    val factor2: Expression,
    val mvrStatement: MvrStmt? = null,
    @Derived val dataDefinition: InStatementDataDefinition? = null,
    override val position: Position? = null
) : Statement(position), CompositeStatement, StatementThatCanDefineData {
    override val loggableEntityName: String
        get() = "DIV"

    @Derived
    val dividend: Expression
        get() = factor1 ?: target

    @Derived
    val divisor: Expression
        get() = factor2

    override fun execute(interpreter: InterpreterCore) {
        interpreter.assign(target, interpreter.div(this))
    }

    override fun dataDefinition() = dataDefinition?.let { listOf(it) } ?: emptyList()

    @Derived
    override val body: List<Statement>
        get() = mvrStatement?.let { listOf(it) } ?: emptyList()
}

@Serializable
data class MvrStmt(
    val target: AssignableExpression?,
    @Derived val dataDefinition: InStatementDataDefinition? = null,
    override val position: Position? = null
) : Statement(position), StatementThatCanDefineData {
    override val loggableEntityName: String
        get() = "MVR"

    override fun dataDefinition() = dataDefinition?.let { listOf(it) } ?: emptyList()
    override fun execute(interpreter: InterpreterCore) {}
}

@Serializable
data class AddStmt(
    val left: Expression?,
    val result: AssignableExpression,
    @Derived val dataDefinition: InStatementDataDefinition? = null,
    val right: Expression,
    override val position: Position? = null
) : Statement(position), StatementThatCanDefineData {
    override val loggableEntityName: String
        get() = "ADD"

    override fun dataDefinition(): List<InStatementDataDefinition> {
        if (dataDefinition != null) {
            return listOf(dataDefinition)
        }
        return emptyList()
    }

    @Derived
    val addend1: Expression
        get() = left ?: result

    override fun execute(interpreter: InterpreterCore) {
        interpreter.assign(result, interpreter.add(this))
    }
}

@Serializable
data class ZSubStmt(
    val target: AssignableExpression,
    @Derived val dataDefinition: InStatementDataDefinition? = null,
    var expression: Expression,
    override val position: Position? = null
) : Statement(position), StatementThatCanDefineData {
    override val loggableEntityName: String
        get() = "ZSUB"

    override fun dataDefinition(): List<InStatementDataDefinition> {
        if (dataDefinition != null) {
            return listOf(dataDefinition)
        }
        return emptyList()
    }

    override fun execute(interpreter: InterpreterCore) {
        val value = interpreter.eval(expression)
        require(value is NumberValue) {
            "$value should be a number"
        }
        interpreter.assign(target, value.negate())
    }
}

@Serializable
data class SubStmt(
    val left: Expression?,
    val result: AssignableExpression,
    @Derived val dataDefinition: InStatementDataDefinition? = null,
    val right: Expression,
    override val position: Position? = null
) : Statement(position), StatementThatCanDefineData {
    override val loggableEntityName: String
        get() = "SUB"

    override fun dataDefinition(): List<InStatementDataDefinition> {
        if (dataDefinition != null) {
            return listOf(dataDefinition)
        }
        return emptyList()
    }

    @Derived
    val minuend: Expression
        get() = left ?: result

    override fun execute(interpreter: InterpreterCore) {
        interpreter.assign(result, interpreter.sub(this))
    }
}

@Serializable
data class TimeStmt(
    val value: Expression,
    @Derived val dataDefinition: InStatementDataDefinition? = null,
    override val position: Position? = null
) : Statement(position), StatementThatCanDefineData {
    override val loggableEntityName: String
        get() = "TIME"

    override fun execute(interpreter: InterpreterCore) {
        when (value) {
            is DataRefExpr -> {
                val t = TimeStampValue.now()
                when (val valueType = value.type()) {
                    is TimeStampType -> interpreter.assign(value, t)
                    is NumberType -> {
                        val timestampFormatted: String = when (valueType.elementSize()) {
                            6 -> DateTimeFormatter.ofPattern("HHmmss").format(t.value)
                            12 -> DateTimeFormatter.ofPattern("HHmmssddMMyy").format(t.value)
                            14 -> DateTimeFormatter.ofPattern("HHmmssddMMyyyy").format(t.value)
                            else -> throw UnsupportedOperationException("TIME Statement only supports 6, 12, and 14 as the length of the Integer data type")
                        }
                        interpreter.assign(value, IntValue(timestampFormatted.toLong()))
                    }

                    else -> throw UnsupportedOperationException("TIME Statement only supports Timestamp or Integer data type")
                }
            }

            else -> throw UnsupportedOperationException("I do not know how to set TIME to ${this.value}")
        }
    }

    override fun dataDefinition(): List<InStatementDataDefinition> = dataDefinition?.let { listOf(it) } ?: emptyList()
}

@Serializable
data class DisplayStmt(val factor1: Expression?, val response: Expression?, override val position: Position? = null) :
    Statement(position) {
    override val loggableEntityName: String
        get() = "DISPLAY"

    override fun execute(interpreter: InterpreterCore) {
        val values = mutableListOf<Value>()
        factor1?.let { values.add(interpreter.eval(it)) }
        response?.let { values.add(interpreter.eval(it)) }
        // TODO: receive input from systemInterface and assign value to response
        interpreter.getSystemInterface().display(interpreter.rawRender(values))
    }
}

@Serializable
data class DOWxxStmt(
    val comparisonOperator: ComparisonOperator,
    val factor1: Expression,
    val factor2: Expression,
    override val body: List<Statement>,
    override val position: Position? = null
) : Statement(position), CompositeStatement, LoopStatement {
    override val loggableEntityName: String
        get() = "DOWxx"

    private var _iterations: Long = 0
    override val iterations: Long
        get() = _iterations

    override val loopSubject: String
        get() = ""

    override fun execute(interpreter: InterpreterCore) {
        try {
            while (comparisonOperator.verify(
                    factor1,
                    factor2,
                    interpreter,
                    interpreter.getLocalizationContext().charset
                ).isVerified
            ) {
                ++_iterations
                interpreter.execute(body)
            }
        } catch (e: LeaveException) {
            // nothing to do here
        }
    }

    override fun getStatementLogRenderer(source: LogSourceProvider, action: String): LazyLogEntry {
        val entry = LogEntry(source, LogChannel.STATEMENT.getPropertyName(), action)
        return LazyLogEntry(entry) {
            sep -> "${this.loggableEntityName}${comparisonOperator.symbol}${sep}LEFT: ${factor1.render()}/RIGHT: ${factor2.render()}"
        }
    }
}

@Serializable
data class DoStmt(
    val endLimit: Expression,
    val index: AssignableExpression?,
    override val body: List<Statement>,
    val startLimit: Expression = IntLiteral(1),
    @Derived val dataDefinition: InStatementDataDefinition? = null,
    override val position: Position? = null
) : Statement(position), CompositeStatement, StatementThatCanDefineData, LoopStatement {
    override val loggableEntityName: String
        get() = "DO"

    private var _iterations: Long = 0
    override val iterations: Long
        get() = _iterations

    override val loopSubject: String
        get() = ""

    override fun accept(
        mutes: MutableMap<Int, MuteParser.MuteLineContext>,
        start: Int,
        end: Int
    ): MutableList<MuteAnnotationResolved> {
        // TODO check if the annotation is the last statement
        return acceptBody(body, mutes, start, end)
    }

    override fun dataDefinition(): List<InStatementDataDefinition> = dataDefinition?.let { listOf(it) } ?: emptyList()

    override fun execute(interpreter: InterpreterCore) {
        var loopCounter: Long = 0
        val endLimitExpression = endLimit
        val endLimit: () -> Long = interpreter.optimizedIntExpression(endLimitExpression)
        if (index == null) {
            var myIterValue = interpreter.eval(startLimit).asInt().value
            try {
                while (myIterValue <= endLimit()) {
                    try {
                        ++_iterations
                        interpreter.execute(body)
                    } catch (e: IterException) {
                        // nothing to do here
                    }
                    loopCounter++
                    myIterValue++
                }
            } catch (e: LeaveException) {
                // nothing to do here
            }
        } else {
            interpreter.assign(index, startLimit)
            try {
                val indexExpression = interpreter.optimizedIntExpression(index)
                while (indexExpression() <= endLimit()) {
                    try {
                        interpreter.execute(body)
                    } catch (e: IterException) {
                        // nothing to do here
                    }
                    interpreter.assign(index, PlusExpr(index, IntLiteral(1)))
                }
            } catch (e: LeaveException) {
                // nothing to do here
            }
        }
    }

    override fun getStatementLogRenderer(source: LogSourceProvider, action: String): LazyLogEntry {
        val entry = LogEntry(source, LogChannel.STATEMENT.getPropertyName(), action)
        return LazyLogEntry(entry) {
            sep -> "${this.loggableEntityName}$sep${startLimit.render()} ${endLimit.render()}"
        }
    }
}

@Serializable
data class DowStmt(
    val endExpression: Expression,
    override val body: List<Statement>,
    override val position: Position? = null
) : Statement(position), CompositeStatement, LoopStatement {
    override val loggableEntityName: String
        get() = "DOW"

    override val loopSubject: String
        get() = ""

    private var _iterations: Long = 0
    override val iterations: Long
        get() = _iterations

    override fun execute(interpreter: InterpreterCore) {
        var loopCounter: Long = 0
        try {
            while (interpreter.eval(endExpression).asBoolean().value) {
                ++_iterations
                interpreter.execute(body)
                loopCounter++
            }
        } catch (_: LeaveException) {
        }
    }

    override fun getStatementLogRenderer(source: LogSourceProvider, action: String): LazyLogEntry {
        val entry = LogEntry(source, LogChannel.STATEMENT.getPropertyName(), action)
        return LazyLogEntry(entry) {
            sep -> "${this.loggableEntityName}${sep}${endExpression.render()}"
        }
    }
}

@Serializable
data class DouStmt(
    val endExpression: Expression,
    override val body: List<Statement>,
    override val position: Position? = null
) : Statement(position), CompositeStatement, LoopStatement {
    override val loggableEntityName: String
        get() = "DOU"

    override val loopSubject: String
        get() = ""

    private var _iterations: Long = 0
    override val iterations: Long
        get() = _iterations

    override fun execute(interpreter: InterpreterCore) {
        var loopCounter: Long = 0
        try {
            do {
                ++_iterations
                interpreter.execute(body)
                loopCounter++
            } while (!interpreter.eval(endExpression).asBoolean().value)
        } catch (_: LeaveException) {
        }
    }

    override fun getStatementLogRenderer(source: LogSourceProvider, action: String): LazyLogEntry {
        val entry = LogEntry(source, LogChannel.STATEMENT.getPropertyName(), action)
        return LazyLogEntry(entry) {
            sep -> "${this.loggableEntityName}${sep}${endExpression.render()}"
        }
    }
}

@Serializable
data class LeaveSrStmt(override val position: Position? = null) : Statement(position) {
    override val loggableEntityName: String
        get() = "LEAVESR"

    override fun execute(interpreter: InterpreterCore) {
        throw LeaveSrException()
    }
}

@Serializable
data class LeaveStmt(override val position: Position? = null) : Statement(position) {
    override val loggableEntityName: String
        get() = "LEAVE"

    override fun execute(interpreter: InterpreterCore) {
        throw LeaveException()
    }
}

@Serializable
data class IterStmt(override val position: Position? = null) : Statement(position) {
    override val loggableEntityName: String
        get() = "ITER"

    override fun execute(interpreter: InterpreterCore) {
        throw IterException()
    }
}

@Serializable
data class OtherStmt(override val position: Position? = null) : Statement(position) {
    override val loggableEntityName: String
        get() = "OTHER"

    override fun execute(interpreter: InterpreterCore) {
        TODO("Not yet implemented")
    }
}

@Serializable
data class TagStmt private constructor(val tag: String, override val position: Position? = null) : Statement(position) {
    override val loggableEntityName: String
        get() = "TAG"

    companion object {
        operator fun invoke(tag: String, position: Position? = null): TagStmt =
            TagStmt(tag.uppercase(Locale.getDefault()), position)
    }

    override fun execute(interpreter: InterpreterCore) {
        // Nothing to do here
    }
}

@Serializable
data class GotoStmt(val tag: String, override val position: Position? = null) : Statement(position) {
    override val loggableEntityName: String
        get() = "GOTO"

    override fun execute(interpreter: InterpreterCore) {
        throw GotoException(tag)
    }
}

@Serializable
data class CabStmt(
    val factor1: Expression,
    val factor2: Expression,
    val comparison: ComparisonOperator?,
    val tag: String,
    val rightIndicators: WithRightIndicators,
    override val position: Position? = null
) : Statement(position), WithRightIndicators by rightIndicators {
    override val loggableEntityName: String
        get() = "CAB"

    override fun execute(interpreter: InterpreterCore) {
        val comparisonResult =
            comparison.verify(factor1, factor2, interpreter, interpreter.getLocalizationContext().charset)
        when (comparisonResult.comparison) {
            GREATER -> interpreter.setIndicators(this, BooleanValue.TRUE, BooleanValue.FALSE, BooleanValue.FALSE)
            SMALLER -> interpreter.setIndicators(this, BooleanValue.FALSE, BooleanValue.TRUE, BooleanValue.FALSE)
            else -> interpreter.setIndicators(this, BooleanValue.FALSE, BooleanValue.FALSE, BooleanValue.TRUE)
        }
        if (comparisonResult.isVerified) throw GotoException(tag)
    }
}

@Serializable
data class ForStmt(
    var init: Expression,
    val endValue: Expression,
    val byValue: Expression,
    val downward: Boolean = false,
    override val body: List<Statement>,
    override val position: Position? = null
) : Statement(position), CompositeStatement, LoopStatement {
    override val loggableEntityName: String
        get() = "FOR"

    override val loopSubject: String
        get() {
            return if (init is AssignmentExpr) {
                val assignment = init as AssignmentExpr
                if (assignment.target is DataRefExpr) {
                    val target = assignment.target as DataRefExpr
                    target.variable.referred?.name ?: ""
                } else ""
            } else ""
        }

    override var iterations: Long = 0

    fun iterDataDefinition(): AbstractDataDefinition {
        if (init is AssignmentExpr) {
            if ((init as AssignmentExpr).target is DataRefExpr) {
                return ((init as AssignmentExpr).target as DataRefExpr).variable.referred!!
            } else {
                throw UnsupportedOperationException()
            }
        } else {
            throw UnsupportedOperationException()
        }
    }

    override fun accept(
        mutes: MutableMap<Int, MuteParser.MuteLineContext>,
        start: Int,
        end: Int
    ): MutableList<MuteAnnotationResolved> {
        // TODO check if the annotation is the last statement
        return acceptBody(body, mutes, start, end)
    }

    override fun execute(interpreter: InterpreterCore) {
        var loopCounter: Long = 0

        interpreter.eval(init)
        val iterVar = iterDataDefinition()
        try {
            var step = interpreter.eval(byValue).asInt().value
            if (downward) {
                step *= -1
            }
            while (interpreter.enterCondition(interpreter[iterVar], interpreter.eval(endValue), downward)) {
                try {
                    interpreter.execute(body)
                    ++iterations
                } catch (e: IterException) {
                    // nothing to do here
                }

                interpreter.increment(iterVar, step)
                loopCounter++
            }
        } catch (e: LeaveException) {
            // leaving
        }
    }

    override fun getStatementLogRenderer(source: LogSourceProvider, action: String): LazyLogEntry {
        val downward = if (downward) "DOWNTO" else "TO"
        val byValue = if (byValue.render() == "1") "" else "BY ${byValue.render()}"

        val entry = LogEntry(source, LogChannel.STATEMENT.getPropertyName(), action)
        return LazyLogEntry(entry) {
            sep -> "${this.loggableEntityName}$sep${init.render()} $byValue $downward ${endValue.render()}"
        }
    }
}

/*
* For an array data structure, the keyed-ds-array operand is a qualified name consisting
* of the array to be sorted followed by the subfield to be used as a key for the sort.
*/
@Serializable
data class SortAStmt(val target: Expression, override val position: Position? = null) : Statement(position) {
    override val loggableEntityName: String
        get() = "SORTA"

    override fun execute(interpreter: InterpreterCore) {
        sortA(
            value = interpreter.eval(target),
            arrayType = target.type() as ArrayType
        )
    }
}

@Serializable
data class CatStmt(
    val left: Expression?,
    val right: Expression,
    val target: AssignableExpression,
    val blanksInBetween: Expression?,
    @Derived val dataDefinition: InStatementDataDefinition? = null,
    override val position: Position? = null,
    val operationExtender: String? = null
) : Statement(position), StatementThatCanDefineData {
    override val loggableEntityName: String
        get() = "CAT"

    override fun execute(interpreter: InterpreterCore) {
        val factor1: String = if (left != null) {
            interpreter.eval(left).asString().value
        } else {
            // set result as factor 1
            interpreter.eval(target).asString().value
        }
        val factor2: String = if (right.type() is StringType) {
            interpreter.eval(right).asString().value
        } else {
            throw UnsupportedOperationException("Factor 2 of CAT Statement must be a StringValue")
        }
        val blanks: String? = if (blanksInBetween != null) {
            " ".repeat(interpreter.eval(blanksInBetween).asInt().value.toInt())
        } else {
            null
        }
        require(target.type() is StringType) {
            "Result expression of CAT Statement must be a StringValue"
        }
        var result: String = interpreter.eval(target).asString().value

        val concatenatedString: String = if (blanks == null) {
            // concatenate factor 1 with factor 2
            (factor1 + factor2)
        } else {
            // if blanks aren't null trim factor 1
            (factor1.trim() + blanks + factor2)
        }

        result = if (result.length > concatenatedString.length) {
            // handle CAT(P)
            if (operationExtender != null) {
                concatenatedString + " ".repeat(result.length - concatenatedString.length)
            } else {
                (concatenatedString + result.substring(concatenatedString.length))
            }
        } else {
            (concatenatedString.substring(0, result.length))
        }

        interpreter.assign(target, StringValue(result))
    }

    override fun dataDefinition(): List<InStatementDataDefinition> = dataDefinition?.let { listOf(it) } ?: emptyList()

    override fun getStatementLogRenderer(source: LogSourceProvider, action: String): LazyLogEntry {
        val entry = LogEntry(source, LogChannel.STATEMENT.getPropertyName(), action)

        return LazyLogEntry(entry) { sep ->
            buildString {
                append(loggableEntityName)
                append(sep)
                if (left != null) {
                    append("FACTOR1${sep}${left.render()}")
                    append(sep)
                }
                append("FACTOR2${sep}${right.render()}")
            }
        }
    }
}

@Serializable
data class LookupStmt(
    val left: Expression,
    val right: Expression,
    val rightIndicators: WithRightIndicators,
    override val position: Position? = null
) : Statement(position), WithRightIndicators by rightIndicators {
    override val loggableEntityName: String
        get() = "LOOKUP"

    override fun execute(interpreter: InterpreterCore) {
        lookUp(this, interpreter, interpreter.getLocalizationContext().charset)
    }

    override fun getStatementLogRenderer(source: LogSourceProvider, action: String): LazyLogEntry {
        val entry = LogEntry(source, LogChannel.STATEMENT.getPropertyName(), action)
        return LazyLogEntry(entry) {
            sep -> "${this.loggableEntityName}${sep}FACTOR1${sep}${left.render()}${sep}FACTOR2${sep}${right.render()}${sep}HI${sep}$hi${sep}LO${sep}$lo${sep}EQ${sep}$eq"
        }
    }
}

@Serializable
data class ScanStmt(
    val left: Expression,
    val leftLength: Int?,
    val right: Expression,
    val startPosition: Expression?,
    val target: AssignableExpression?,
    val rightIndicators: WithRightIndicators,
    @Derived val dataDefinition: InStatementDataDefinition? = null,
    override val position: Position? = null
) : Statement(position), WithRightIndicators by rightIndicators, StatementThatCanDefineData {
    override val loggableEntityName: String
        get() = "SCAN"

    override fun execute(interpreter: InterpreterCore) {
        val start = startPosition?.let { interpreter.eval(it).asString().value.toInt() } ?: 1

        val stringToSearch = interpreter.eval(left).asString().value.substringOfLength(leftLength)
        val searchInto = interpreter.eval(right).asString().value.substring(start - 1)
        val occurrences = mutableListOf<Value>()
        var index = -1
        do {
            index = searchInto.indexOf(stringToSearch, index + 1)
            if (index >= 0) occurrences.add(IntValue((index + start).toLong()))
        } while (index >= 0)
        if (occurrences.isEmpty()) {
            interpreter.setIndicators(this, BooleanValue.FALSE, BooleanValue.FALSE, BooleanValue.FALSE)
            target?.let { interpreter.assign(it, IntValue(0)) }
        } else {
            interpreter.setIndicators(this, BooleanValue.FALSE, BooleanValue.FALSE, BooleanValue.TRUE)
            target?.let {
                if (it.type().isArray()) {
                    val fullOccurrences =
                        occurrences.resizeTo(it.type().numberOfElements(), IntValue.ZERO).toMutableList()
                    interpreter.assign(it, ConcreteArrayValue(fullOccurrences, it.type().asArray().element))
                } else {
                    interpreter.assign(it, occurrences[0])
                }
            }
        }
    }

    override fun dataDefinition() = dataDefinition?.let { listOf(it) } ?: emptyList()
}

@Serializable
data class XFootStmt(
    val left: Expression,
    val result: AssignableExpression,
    val rightIndicators: WithRightIndicators,
    @Derived val dataDefinition: InStatementDataDefinition? = null,
    override val position: Position? = null
) : Statement(position), WithRightIndicators by rightIndicators, StatementThatCanDefineData {
    override val loggableEntityName: String
        get() = "XFOOT"

    override fun dataDefinition(): List<InStatementDataDefinition> {
        if (dataDefinition != null) {
            return listOf(dataDefinition)
        }
        return emptyList()
    }

    override fun execute(interpreter: InterpreterCore) {
        xfoot(this, interpreter)
    }
}

/**
 *  The SUBST statements returns a substring from factor 2, starting at the
 *  location specified in 'startPosition' for the length specified in 'length',
 *  and places this substring in the 'target' field.
 *
 *  @property length length to extract.
 *  @property value source string.
 *  @property startPosition starting position.
 *  @property target result string.
 *  @property operationExtender indicate extender position.
 */
@Serializable
data class SubstStmt(
    val length: Expression?,
    val value: Expression,
    val startPosition: Expression?,
    val target: AssignableExpression,
    val operationExtender: String?,
    @Derived val dataDefinition: InStatementDataDefinition? = null,
    override val position: Position? = null
) : Statement(position), StatementThatCanDefineData {
    override val loggableEntityName: String
        get() = "SUBST"

    override fun execute(interpreter: InterpreterCore) {
        val start = startPosition?.let { interpreter.eval(it).asString().value.toInt() } ?: 1

        /**
         * Length is 1-based which is a specific prerogative of rpg,
         * not like modern programming languages whose indices are all zero-based
         */
        val substring: String = length?.let {
            val end = interpreter.eval(it).asString().value.toInt()
            val endPosition = start + end
            interpreter.eval(value).asString().value.substring(startIndex = start - 1, endIndex = endPosition - 1)
        } ?: interpreter.eval(value).asString().value.substring(startIndex = start - 1)

        /** Extended operations on opcode:
         * in case of SUBST replace range of string,
         * in case of SUBST(P) replace string
         */
        val newSubstr = operationExtender?.let { substring } ?: let {
            val targetValue = interpreter.eval(target).asString().value
            if (targetValue.length > substring.length) {
                targetValue.replaceRange(startIndex = 0, endIndex = substring.length, replacement = substring)
            } else {
                targetValue.replace(targetValue, substring)
            }
        }
        interpreter.assign(target, newSubstr.asValue())
    }

    override fun dataDefinition(): List<InStatementDataDefinition> = dataDefinition?.let { listOf(it) } ?: emptyList()
}

/**
 * Implements [OCCUR](https://www.ibm.com/docs/en/i/7.4?topic=codes-occur-setget-occurrence-data-structure)
 * */
@Serializable
data class OccurStmt(
    val occurenceValue: Expression?,
    val dataStructure: String,
    val result: AssignableExpression?,
    val operationExtender: String?,
    @Derived val dataDefinition: InStatementDataDefinition? = null,
    val errorIndicator: IndicatorKey?,
    override val position: Position? = null
) : Statement(position), StatementThatCanDefineData {
    override val loggableEntityName: String
        get() = "OCCUR"

    init {
        require(operationExtender == null) {
            "Operation extender not supported"
        }
    }

    override fun dataDefinition(): List<InStatementDataDefinition> = dataDefinition?.let { listOf(it) } ?: emptyList()

    override fun execute(interpreter: InterpreterCore) {
        val dataStructureValue = interpreter[dataStructure]
        require(dataStructureValue is OccurableDataStructValue) {
            "OCCUR not supported. $dataStructure must be a DS defined with OCCURS keyword"
        }
        occurenceValue?.let {
            val evaluatedValue = interpreter.eval(it)
            if (evaluatedValue is OccurableDataStructValue) {
                dataStructureValue.pos(
                    occurrence = evaluatedValue.occurrence,
                    interpreter = interpreter,
                    errorIndicator = errorIndicator
                )
            } else if (evaluatedValue.asString().value.isInt()) {
                dataStructureValue.pos(
                    occurrence = evaluatedValue.asString().value.toInt(),
                    interpreter = interpreter,
                    errorIndicator = errorIndicator
                )
            } else {
                throw IllegalArgumentException("$evaluatedValue must be an occurrence or a reference to a multiple occurrence data structure")
            }
        }
        result?.let { result -> interpreter.assign(result, dataStructureValue.occurrence.asValue()) }
    }
}

fun OccurableDataStructValue.pos(occurrence: Int, interpreter: InterpreterCore, errorIndicator: IndicatorKey?) {
    try {
        this.pos(occurrence)
    } catch (e: ArrayIndexOutOfBoundsException) {
        if (errorIndicator == null) throw e else interpreter.getIndicators()[errorIndicator] = BooleanValue.TRUE
    }
}

@Serializable
data class OpenStmt(
    val name: String = "", // Factor 2
    override val position: Position? = null,
    val operationExtender: String?,
    val errorIndicator: IndicatorKey?
) : Statement(position) {
    override val loggableEntityName: String
        get() = "OPEN"

    init {
        require(operationExtender == null) {
            "Operation extender not supported"
        }
    }

    override fun execute(interpreter: InterpreterCore) {
        val dbFile = interpreter.dbFile(name, this)
        dbFile.open = true
    }
}

@Serializable
data class CloseStmt(
    val name: String = "", // Factor 2
    override val position: Position? = null,
    val operationExtender: String?,
    val errorIndicator: IndicatorKey?
) : Statement(position) {
    override val loggableEntityName: String
        get() = "CLOSE"

    init {
        require(operationExtender == null) {
            "Operation extender not supported"
        }
    }

    override fun execute(interpreter: InterpreterCore) {
        val dbFile = interpreter.dbFile(name, this)
        dbFile.open = false
    }
}

/**
 *  XLATE operation Code: all characters in the source string (factor 2) are translated according
 *  to the From and To strings (both in factor 1) and put into a receiver
 *  field (result field). Source characters with a match in the From string
 *  are translated to corresponding characters in the To string.
 *
 *  @property from characters to replace
 *  @property to replacement characters.
 *  @property string source string.
 *  @property startPos starting position in the source string.
 *  @property target result string.
 */
@Serializable
data class XlateStmt(
    val from: Expression,
    val to: Expression,
    val string: Expression,
    val startPos: Int,
    val target: AssignableExpression,
    val rightIndicators: WithRightIndicators,
    @Derived val dataDefinition: InStatementDataDefinition? = null,
    override val position: Position? = null
) : Statement(position), WithRightIndicators by rightIndicators, StatementThatCanDefineData {
    override val loggableEntityName: String
        get() = "XLATE"

    override fun execute(interpreter: InterpreterCore) {
        val originalChars = interpreter.eval(from).asString().value
        val newChars = interpreter.eval(to).asString().value
        val start = startPos
        val s = interpreter.eval(string).asString().value
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
        interpreter.assign(target, StringValue(pair.first + right))
    }

    override fun dataDefinition() = dataDefinition?.let { listOf(it) } ?: emptyList()
}

@Serializable
data class ResetStmt(
    val name: String,
    @Derived val dataDefinition: InStatementDataDefinition? = null,
    override val position: Position? = null
) : Statement(position), StatementThatCanDefineData {
    override val loggableEntityName: String
        get() = "RESET"

    override fun execute(interpreter: InterpreterCore) {
        when (val dataDefinition = interpreter.dataDefinitionByName(name)) {
            null -> this.error("Data definition $name not found")
            is DataDefinition -> {
                require(dataDefinition.defaultValue != null) {
                    this.error("Data definition $name has no default value")
                }
                interpreter.assign(dataDefinition, dataDefinition.defaultValue!!)
            }

            is InStatementDataDefinition -> {
                interpreter.assign(dataDefinition, dataDefinition.type.blank())
            }

            else -> this.error("Data definition $name is not a valid instance of DataDefinition")
        }
    }

    override fun dataDefinition(): List<InStatementDataDefinition> = dataDefinition?.let { listOf(it) } ?: emptyList()
}

@Serializable
data class ExfmtStmt(
    override val position: Position? = null
) : Statement(position), MockStatement {
    override val loggableEntityName: String
        get() = "EXFMT"

    override fun execute(interpreter: InterpreterCore) {}
}

@Serializable
data class ReadcStmt(
    override val position: Position? = null
) : Statement(position), MockStatement {
    override val loggableEntityName: String
        get() = "READC"

    override fun execute(interpreter: InterpreterCore) {}
}

@Serializable
data class UnlockStmt(
    override val position: Position? = null
) : Statement(position), MockStatement {
    override val loggableEntityName: String
        get() = "UNLOCK"

    override fun execute(interpreter: InterpreterCore) {}
}

@Serializable
data class FeodStmt(
    override val position: Position? = null
) : Statement(position), MockStatement {
    override val loggableEntityName: String
        get() = "FEOD"

    override fun execute(interpreter: InterpreterCore) {}
}

@Serializable
data class BitOnStmt(
    override val position: Position? = null,
    @Derived val dataDefinition: InStatementDataDefinition? = null
) : Statement(position), StatementThatCanDefineData, MockStatement {
    override fun execute(interpreter: InterpreterCore) { }
    override fun dataDefinition(): List<InStatementDataDefinition> = dataDefinition?.let { listOf(it) } ?: emptyList()
}

@Serializable
data class BitOffStmt(
    override val position: Position? = null,
    @Derived val dataDefinition: InStatementDataDefinition? = null
) : Statement(position), StatementThatCanDefineData, MockStatement {
    override fun execute(interpreter: InterpreterCore) { }
    override fun dataDefinition(): List<InStatementDataDefinition> = dataDefinition?.let { listOf(it) } ?: emptyList()
}