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
import com.smeup.rpgparser.parsing.parsetreetoast.acceptBody
import com.smeup.rpgparser.parsing.parsetreetoast.toAst
import com.smeup.rpgparser.utils.ComparisonOperator
import com.smeup.rpgparser.utils.resizeTo
import com.smeup.rpgparser.utils.substringOfLength
import com.strumenta.kolasu.model.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.*
import kotlin.system.measureTimeMillis

interface StatementThatCanDefineData {
    fun dataDefinition(): List<InStatementDataDefinition>
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
) : Node(position) {
    open fun accept(mutes: MutableMap<Int, MuteParser.MuteLineContext>, start: Int = 0, end: Int): MutableList<MuteAnnotationResolved> {

        // List of mutes successfully attached to the statements
        val mutesAttached: MutableList<MuteAnnotationResolved> = mutableListOf()

        // Extracts the annotation declared before the statement
        val muteToProcess = mutes.filterKeys {
            it < this.position!!.start.line
        }

        muteToProcess.forEach { (line, mute) ->
            this.muteAnnotations.add(mute.toAst(
                    position = pos(line, this.position!!.start.column, line, this.position!!.end.column))
            )
            mutesAttached.add(MuteAnnotationResolved(line, this.position!!.start.line))
        }

        return mutesAttached
    }
    open fun simpleDescription() = "Issue executing ${javaClass.simpleName} at line ${startLine()}."

    var indicatorCondition: IndicatorCondition? = null
    var continuedIndicators: HashMap<IndicatorKey, ContinuedIndicator> = HashMap<IndicatorKey, ContinuedIndicator>()

    @Throws(ControlFlowException::class, IllegalArgumentException::class, NotImplementedError::class, RuntimeException::class)
    abstract fun execute(interpreter: InterpreterCore)
}

interface CompositeStatement {
    val body: List<Statement>
}

fun List<Statement>.explode(): List<Statement> {
    val result = mutableListOf<Statement>()
    forEach {
        if (it is CompositeStatement) {
            result.addAll(it.body.explode())
        } else {
            result.add(it)
        }
    }
    return result
}

@Serializable
data class ExecuteSubroutine(var subroutine: ReferenceByName<Subroutine>, override val position: Position? = null) : Statement(position) {
    override fun execute(interpreter: InterpreterCore) {
        interpreter.log {
            SubroutineExecutionLogStart(
                    interpreter.getInterpretationContext().currentProgramName,
                    subroutine.referred!!
            )
        }
        val elapsed = measureTimeMillis {
            try {
                interpreter.execute(subroutine.referred!!.stmts)
            } catch (e: LeaveSrException) {
                // Nothing to do here
            } catch (e: GotoException) {
                if (!e.tag.equals(subroutine.referred!!.tag, true)) throw e
            }
        }
        interpreter.log {
            SubroutineExecutionLogEnd(
                    interpreter.getInterpretationContext().currentProgramName,
                    subroutine.referred!!,
                    elapsed
            )
        }
    }
}

@Serializable
data class SelectStmt(
    var cases: List<SelectCase>,
    var other: SelectOtherClause? = null,
    override val position: Position? = null
) : Statement(position), CompositeStatement {
    override fun accept(mutes: MutableMap<Int, MuteParser.MuteLineContext>, start: Int, end: Int): MutableList<MuteAnnotationResolved> {

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

    override fun execute(interpreter: InterpreterCore) {
        for (case in this.cases) {
            val result = interpreter.eval(case.condition)

            interpreter.log { SelectCaseExecutionLogEntry(interpreter.getInterpretationContext().currentProgramName, case, result) }
            if (result.asBoolean().value) {
                interpreter.execute(case.body)
                return
            }
        }
        if (this.other != null) {
            interpreter.log {
                SelectOtherExecutionLogEntry(
                        interpreter.getInterpretationContext().currentProgramName,
                        this.other!!
                )
            }
            interpreter.execute(this.other!!.body)
        }
    }

    @Derived
    override val body: List<Statement>
        get() {
            val result = mutableListOf<Statement>()
            cases.forEach { case ->
                result.addAll(case.body.explode())
            }
            if (other?.body != null) result.addAll(other!!.body.explode())
            return result
        }
}

@Serializable
data class SelectOtherClause(override val body: List<Statement>, override val position: Position? = null) : Node(position), CompositeStatement

@Serializable
data class SelectCase(val condition: Expression, override val body: List<Statement>, override val position: Position? = null) : Node(position), CompositeStatement

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
    override fun execute(interpreter: InterpreterCore) {
            // Should I assign it one by one?
            val result = if (target.type().isArray() &&
                    target.type().asArray().element.canBeAssigned(expression.type())) {
                interpreter.assignEachElement(target, expression, operator)
            } else {
                interpreter.assign(target, expression, operator)
            }
        interpreter.log { EvaluationLogEntry(interpreter.getInterpretationContext().currentProgramName, this, result) }
    }
}

@Serializable
data class SubDurStmt(
    val factor1: Expression?,
    val target: AssignableExpression,
    val factor2: Expression,
    val durationCode: DurationCode,
    override val position: Position? = null
) :
    Statement(position) {
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
}

@Serializable
data class MoveStmt(
    val target: AssignableExpression,
    var expression: Expression,
    override val position: Position? = null
) :
    Statement(position) {
    override fun execute(interpreter: InterpreterCore) {
        val value = move(target, expression, interpreter)
        interpreter.log { MoveStatemenExecutionLog(interpreter.getInterpretationContext().currentProgramName, this, value) }
    }
}

@Serializable
data class MoveAStmt(
    val operationExtender: String?,
    val target: AssignableExpression,
    var expression: Expression,
    override val position: Position? = null
) :
    Statement(position) {
    override fun execute(interpreter: InterpreterCore) {
        val value = movea(operationExtender, target, expression, interpreter)
        interpreter.log {
            MoveAStatemenExecutionLog(interpreter.getInterpretationContext().currentProgramName, this, value)
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
    override fun dataDefinition(): List<InStatementDataDefinition> {
        if (dataDefinition != null) {
            return listOf(dataDefinition)
        }
        return emptyList()
    }

    override fun execute(interpreter: InterpreterCore) {
        val value = movel(operationExtender, target, expression, interpreter)
        interpreter.log { MoveLStatemenExecutionLog(interpreter.getInterpretationContext().currentProgramName, this, value) }
    }
}

@Serializable
abstract class AbstractReadEqualStmt(
    @Transient open val searchArg: Expression? = null, // Factor1
    @Transient open val name: String = "", // Factor 2
    @Transient override val position: Position? = null,
    private val logPref: String

) : Statement(position) {
    override fun execute(interpreter: InterpreterCore) {
        val dbFile = interpreter.dbFile(name, this)
        val kList: List<String> = when (searchArg) {
            null -> emptyList()
            else -> searchArg!!.createKList(dbFile.jarikoMetadata, interpreter)
        }
        interpreter.log {
            ReadEqualLogStart(
                programName = interpreter.getInterpretationContext().currentProgramName,
                statement = this,
                logPref = logPref,
                kList = kList
            )
        }
        val result: Result
        val elapsed = measureTimeMillis {
            result = when (searchArg) {
                null -> read(dbFile)
                else -> read(dbFile, kList)
            }
            interpreter.fillDataFrom(dbFile, result.record)
        }
        interpreter.log {
            ReadEqualLogEnd(
                programName = interpreter.getInterpretationContext().currentProgramName,
                statement = this,
                logPref = logPref,
                result = result,
                elapsed = elapsed
            )
        }
    }

    abstract fun read(dbFile: DBFile, kList: List<String>? = null): Result
}

@Serializable
abstract class AbstractReadStmt(
    @Transient open val name: String = "", // Factor 2
    @Transient override val position: Position? = null,
    private val logPref: String
) : Statement(position) {
    override fun execute(interpreter: InterpreterCore) {
        interpreter.log {
            ReadLogStart(
                programName = interpreter.getInterpretationContext().currentProgramName,
                statement = this,
                logPref = logPref
            )
        }
        val result: Result
        val elapsed = measureTimeMillis {
            val dbFile = interpreter.dbFile(name, this)
            result = readOp(dbFile)
            interpreter.fillDataFrom(dbFile, result.record)
        }
        interpreter.log {
            ReadLogEnd(
                programName = interpreter.getInterpretationContext().currentProgramName,
                this,
                logPref = logPref,
                result = result,
                elapsed = elapsed
            )
        }
    }

    abstract fun readOp(dbFile: DBFile): Result
}

@Serializable
abstract class AbstractStoreStmt(
    @Transient open val name: String = "", // Factor 2
    @Transient override val position: Position? = null,
    private val logPref: String
) : Statement(position) {

    override fun execute(interpreter: InterpreterCore) {
        val dbFile = interpreter.dbFile(name, this)
        val record = Record()
        val result: Result
        val elapsed = measureTimeMillis {
            interpreter.log {
                StoreLogStart(
                    programName = interpreter.getInterpretationContext().currentProgramName,
                    statement = this,
                    logPref = "$logPref CREATE RECORD"
                )
            }
            dbFile.fileMetadata.fields.forEach { field ->
                interpreter.dataDefinitionByName(field.name)?.let { dataDefinition ->
                    RecordField(field.name, interpreter[dataDefinition].asString().value)
                }?.apply {
                    record.add(this)
                } ?: error("Not found in SymbolTable dbFieldName: ${field.name}")
            }
            interpreter.log {
                StoreLogStart(
                    programName = interpreter.getInterpretationContext().currentProgramName,
                    statement = this,
                    logPref = "$logPref STORE"
                )
            }
            result = store(dbFile, record)
        }
        interpreter.log {
            StoreLogEnd(
                programName = interpreter.getInterpretationContext().currentProgramName,
                statement = this,
                logPref = logPref,
                result = result,
                elapsed = elapsed
            )
        }
    }

    abstract fun store(dbFile: DBFile, record: Record): Result
}

@Serializable
abstract class AbstractSetStmt(
    // this one is a dummy expression needed to initialize because of "transient" annotation
    @Transient open val searchArg: Expression = StringLiteral(""), // Factor1
    @Transient open val name: String = "", // Factor 2
    @Transient override val position: Position? = null,
    private val logPref: String = ""
) : Statement(position) {
    override fun execute(interpreter: InterpreterCore) {
        val dbFile = interpreter.dbFile(name, this)
        val kList: List<String> = searchArg.createKList(dbFile.jarikoMetadata, interpreter)
        interpreter.log {
            SetLogStart(
                programName = interpreter.getInterpretationContext().currentProgramName,
                statement = this,
                kList = kList,
                logPref = logPref
            )
        }
        val elapsed = measureTimeMillis {
            interpreter.getStatus().lastFound = set(dbFile, kList)
        }
        interpreter.log {
            SetLogEnd(
                programName = interpreter.getInterpretationContext().currentProgramName,
                statement = this,
                logPref = logPref,
                elapsed = elapsed
            )
        }
    }

    abstract fun set(dbFile: DBFile, kList: List<String>): Boolean
}

// TODO add other parameters
@Serializable
data class ChainStmt(
    override val searchArg: Expression, // Factor1
    override val name: String, // Factor 2
    override val position: Position? = null
) : AbstractReadEqualStmt(searchArg, name, position, "CHAIN") {
    override fun read(dbFile: DBFile, kList: List<String>?): Result = dbFile.chain(kList!!)
}

@Serializable
data class ReadEqualStmt(
    override val searchArg: Expression?,
    override val name: String,
    override val position: Position? = null
) : AbstractReadEqualStmt(searchArg = searchArg, name = name, position = position, logPref = "READE") {

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
) : AbstractReadEqualStmt(searchArg = searchArg, name = name, position = position, logPref = "READPE") {

    override fun read(dbFile: DBFile, kList: List<String>?): Result {
        return if (kList == null) {
            dbFile.readPreviousEqual()
        } else {
            dbFile.readPreviousEqual(kList)
        }
    }
}

@Serializable
data class ReadStmt(override val name: String, override val position: Position?) : AbstractReadStmt(name, position, "READ") {
    override fun readOp(dbFile: DBFile) = dbFile.read()
}

@Serializable
data class ReadPreviousStmt(override val name: String, override val position: Position?) : AbstractReadStmt(name, position, "READP") {
    override fun readOp(dbFile: DBFile) = dbFile.readPrevious()
}

@Serializable
data class WriteStmt(override val name: String, override val position: Position?) : AbstractStoreStmt(name = name, position = position, logPref = "WRITE") {
    override fun store(dbFile: DBFile, record: Record) = dbFile.write(record)
}

@Serializable
data class UpdateStmt(override val name: String, override val position: Position?) : AbstractStoreStmt(name = name, position = position, logPref = "UPDATE") {
    override fun store(dbFile: DBFile, record: Record) = dbFile.update(record)
}

@Serializable
data class DeleteStmt(override val name: String, override val position: Position?) : AbstractStoreStmt(name = name, position = position, logPref = "DELETE") {
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
    position = position,
    logPref = "SETLL"
) {
    override fun set(dbFile: DBFile, kList: List<String>) = dbFile.setll(kList)
}

@Serializable
data class SetgtStmt(
    override val searchArg: Expression,
    override val name: String,
    override val position: Position?
) : AbstractSetStmt(searchArg = searchArg, name = name, position = position, logPref = "SETGT") {

    override fun set(dbFile: DBFile, kList: List<String>) = dbFile.setgt(kList)
}

@Serializable
data class CheckStmt(
    val comparatorString: Expression, // Factor1
    val baseString: Expression,
    val start: Int = 1,
    val wrongCharPosition: AssignableExpression?,
    override val position: Position? = null
) : Statement(position) {
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
}

@Serializable
data class CallStmt(
    val expression: Expression,
    val params: List<PlistParam>,
    val errorIndicator: IndicatorKey? = null,
    override val position: Position? = null
) : Statement(position), StatementThatCanDefineData {
    override fun dataDefinition(): List<InStatementDataDefinition> {
        return params.mapNotNull() {
            it.dataDefinition
        }
    }

    override fun execute(interpreter: InterpreterCore) {
        interpreter.log { CallExecutionLogEntry(interpreter.getInterpretationContext().currentProgramName, this) }
        val startTime = System.currentTimeMillis()
        val callStatement = this
        val programToCall = interpreter.eval(expression).asString().value.trim()
        MainExecutionContext.setExecutionProgramName(programToCall)
        val program = interpreter.getSystemInterface().findProgram(programToCall)
        require(program != null) {
            "Line: ${this.position.line()} - Program '$programToCall' cannot be found"
        }

        val params = this.params.mapIndexed { index, it ->
            if (it.dataDefinition != null) {
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
                    interpreter.log {
                        CallEndLogEntry(
                            interpreter.getInterpretationContext().currentProgramName,
                            callStatement,
                            System.currentTimeMillis() - startTime
                        )
                    }
                }
            } catch (e: Exception) { // TODO Catch a more specific exception?
                interpreter.log {
                    CallEndLogEntry(
                        interpreter.getInterpretationContext().currentProgramName,
                        callStatement,
                        System.currentTimeMillis() - startTime
                    )
                }
                if (errorIndicator == null) {
                    throw e
                }
                interpreter.getIndicators()[errorIndicator] = BooleanValue.TRUE
                null
            }
        paramValuesAtTheEnd?.forEachIndexed { index, value ->
            interpreter.assign(this.params[index].param.referred!!, value)
        }
    }
}

@Serializable
data class CallPStmt(
    val functionCall: FunctionCall,
    val errorIndicator: IndicatorKey? = null,
    override val position: Position? = null
) : Statement(position), StatementThatCanDefineData {

    override fun dataDefinition(): List<InStatementDataDefinition> {
        return emptyList()
    }

    override fun execute(interpreter: InterpreterCore) {
        interpreter.log { CallPExecutionLogEntry(interpreter.getInterpretationContext().currentProgramName, this) }
        val startTime = System.currentTimeMillis()
        val callStatement = this
        try {
            kotlin.run {
                val expressionEvaluation = ExpressionEvaluation(interpreter.getSystemInterface(), LocalizationContext(), interpreter.getStatus())
                expressionEvaluation.eval(functionCall)
            }.apply {
                interpreter.log {
                    CallPEndLogEntry(
                        interpreter.getInterpretationContext().currentProgramName,
                        callStatement,
                        System.currentTimeMillis() - startTime
                    )
                }
            }
        } catch (e: Exception) { // TODO Catch a more specific exception?
            interpreter.log {
                CallPEndLogEntry(
                    interpreter.getInterpretationContext().currentProgramName,
                    callStatement,
                    System.currentTimeMillis() - startTime
                )
            }
            if (errorIndicator == null) {
                throw e
            }
            interpreter.getIndicators()[errorIndicator] = BooleanValue.TRUE
        }
    }
}

@Serializable
data class KListStmt
private constructor(val name: String, val fields: List<String>, override val position: Position?) : Statement(position), StatementThatCanDefineData {
    companion object {
        operator fun invoke(name: String, fields: List<String>, position: Position? = null): KListStmt {
            return KListStmt(name.toUpperCase(), fields, position)
        }
    }

    override fun dataDefinition(): List<InStatementDataDefinition> = listOf(InStatementDataDefinition(name, KListType))

    override fun execute(interpreter: InterpreterCore) {
        // TODO Add logging as for PlistStmt
        interpreter.getKlists()[name] = fields
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

    override fun accept(mutes: MutableMap<Int, MuteParser.MuteLineContext>, start: Int, end: Int): MutableList<MuteAnnotationResolved> {
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
        interpreter.log { IfExecutionLogEntry(interpreter.getInterpretationContext().currentProgramName, this, condition) }
        if (condition.asBoolean().value) {
            interpreter.execute(this.thenBody)
        } else {
            for (elseIfClause in elseIfClauses) {
                val c = interpreter.eval(elseIfClause.condition)
                interpreter.log { ElseIfExecutionLogEntry(interpreter.getInterpretationContext().currentProgramName, elseIfClause, c) }
                if (c.asBoolean().value) {
                    interpreter.execute(elseIfClause.body)
                    return
                }
            }
            if (elseClause != null) {
                interpreter.log {
                    ElseExecutionLogEntry(
                            interpreter.getInterpretationContext().currentProgramName,
                            elseClause,
                            condition
                    )
                }
                interpreter.execute(elseClause.body)
            }
        }
    }
}

@Serializable
data class ElseClause(override val body: List<Statement>, override val position: Position? = null) : Node(position), CompositeStatement

@Serializable
data class ElseIfClause(val condition: Expression, override val body: List<Statement>, override val position: Position? = null) : Node(position), CompositeStatement

@Serializable
data class SetStmt(val valueSet: ValueSet, val indicators: List<AssignableExpression>, override val position: Position? = null) : Statement(position) {
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
    override fun execute(interpreter: InterpreterCore) {
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
                val value = interpreter.getGlobalSymbolTable()[it.param.name]
                interpreter.log {
                    ParamListStatemenExecutionLog(
                            interpreter.getInterpretationContext().currentProgramName,
                            this,
                            it.param.name,
                            value
                    )
                }
            }
        }
    }
}

@Serializable
data class PlistParam(
    val param: ReferenceByName<AbstractDataDefinition>,
    // TODO @Derived????
    @Derived val dataDefinition: InStatementDataDefinition? = null,
    override val position: Position? = null
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
    override fun dataDefinition(): List<InStatementDataDefinition> {
        if (dataDefinition != null) {
            return listOf(dataDefinition)
        }
        return emptyList()
    }

    override fun execute(interpreter: InterpreterCore) {
        return when (value) {
            is DataRefExpr -> {
                val value = interpreter.assign(value, BlanksRefExpr())
                interpreter.log {
                    ClearStatemenExecutionLog(
                            interpreter.getInterpretationContext().currentProgramName,
                            this,
                            value
                    )
                }
            }
            is IndicatorExpr -> {
                val value = interpreter.assign(value, BlanksRefExpr())
                interpreter.log {
                    ClearStatemenExecutionLog(
                            interpreter.getInterpretationContext().currentProgramName,
                            this,
                            value
                    )
                }
            }
            is ArrayAccessExpr -> {
                val value = interpreter.assign(value, BlanksRefExpr())
                interpreter.log {
                    ClearStatemenExecutionLog(
                        interpreter.getInterpretationContext().currentProgramName,
                        this,
                        value
                    )
                }
            }
            else -> throw UnsupportedOperationException("I do not know how to clear ${this.value}")
        }
    }
}

@Serializable
data class DefineStmt(
    val originalName: String,
    val newVarName: String,
    override val position: Position? = null
) : Statement(position), StatementThatCanDefineData {
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
            val inStatementDataDefinition =
                containingCU.main.stmts
                    .filterIsInstance(StatementThatCanDefineData::class.java)
                    .filter { it != this }
                    .asSequence()
                    .map(StatementThatCanDefineData::dataDefinition)
                    .flatten()
                    .find { it.name == originalName } ?: return emptyList()

            return listOf(InStatementDataDefinition(newVarName, inStatementDataDefinition.type, position))
        }
    }

    override fun execute(interpreter: InterpreterCore) {
        // Nothing to do here
    }
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
    override fun execute(interpreter: InterpreterCore) {
        when (interpreter.compareExpressions(left, right, interpreter.getLocalizationContext().charset)) {
            GREATER -> interpreter.setIndicators(this, BooleanValue.TRUE, BooleanValue.FALSE, BooleanValue.FALSE)
            SMALLER -> interpreter.setIndicators(this, BooleanValue.FALSE, BooleanValue.TRUE, BooleanValue.FALSE)
            else -> interpreter.setIndicators(this, BooleanValue.FALSE, BooleanValue.FALSE, BooleanValue.TRUE)
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
    override val position: Position? = null
) : Statement(position) {
    override fun execute(interpreter: InterpreterCore) {
        interpreter.assign(target, interpreter.mult(this))
    }
}

@Serializable
data class DivStmt(
    val target: AssignableExpression,
    val halfAdjust: Boolean = false,
    val factor1: Expression?,
    val factor2: Expression,
    override val position: Position? = null
) : Statement(position) {
    override fun execute(interpreter: InterpreterCore) {
        interpreter.assign(target, interpreter.div(this))
    }
}

@Serializable
data class AddStmt(
    val left: Expression?,
    val result: AssignableExpression,
    @Derived val dataDefinition: InStatementDataDefinition? = null,
    val right: Expression,
    override val position: Position? = null
) : Statement(position), StatementThatCanDefineData {
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
    override val position: Position? = null
) : Statement(position) {
    override fun execute(interpreter: InterpreterCore) {
        when (value) {
            is DataRefExpr -> {
                interpreter.assign(value, TimeStampValue(Date()))
            }
            else -> throw UnsupportedOperationException("I do not know how to set TIME to ${this.value}")
        }
    }
}

@Serializable
data class DisplayStmt(val factor1: Expression?, val response: Expression?, override val position: Position? = null) : Statement(position) {
    override fun execute(interpreter: InterpreterCore) {
        val values = mutableListOf<Value>()
        factor1?.let { values.add(interpreter.eval(it)) }
        response?.let { values.add(interpreter.eval(it)) }
        // TODO: receive input from systemInterface and assign value to response
        interpreter.getSystemInterface().display(interpreter.rawRender(values))
    }
}

@Serializable
data class DoStmt(
    val endLimit: Expression,
    val index: AssignableExpression?,
    override val body: List<Statement>,
    val startLimit: Expression = IntLiteral(1),
    override val position: Position? = null
) : Statement(position), CompositeStatement {
    override fun accept(mutes: MutableMap<Int, MuteParser.MuteLineContext>, start: Int, end: Int): MutableList<MuteAnnotationResolved> {
        // TODO check if the annotation is the last statement
        return acceptBody(body, mutes, start, end)
    }

    override fun execute(interpreter: InterpreterCore) {
        var loopCounter: Long = 0
        val startTime = System.currentTimeMillis()
        val endLimitExpression = endLimit
        val endLimit: () -> Long = interpreter.optimizedIntExpression(endLimitExpression)
        if (index == null) {
            var myIterValue = interpreter.eval(startLimit).asInt().value
            try {
                interpreter.log { DoStatemenExecutionLogStart(interpreter.getInterpretationContext().currentProgramName, this) }
                while (myIterValue <= endLimit()) {
                    try {
                        interpreter.execute(body)
                    } catch (e: IterException) {
                        // nothing to do here
                    }
                    loopCounter++
                    myIterValue++
                }
                interpreter.log {
                    val elapsed = System.currentTimeMillis() - startTime
                    DoStatemenExecutionLogEnd(
                            interpreter.getInterpretationContext().currentProgramName,
                            this,
                            elapsed,
                            loopCounter
                    )
                }
            } catch (e: LeaveException) {
                // nothing to do here
                interpreter.log {
                    val elapsed = System.currentTimeMillis() - startTime
                    DoStatemenExecutionLogEnd(
                            interpreter.getInterpretationContext().currentProgramName,
                            this,
                            elapsed,
                            loopCounter
                    )
                }
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
}

@Serializable
data class DowStmt(
    val endExpression: Expression,
    override val body: List<Statement>,
    override val position: Position? = null
) : Statement(position), CompositeStatement {
    override fun execute(interpreter: InterpreterCore) {
        var loopCounter: Long = 0
        val startTime = System.currentTimeMillis()
        try {
            interpreter.log { DowStatemenExecutionLogStart(interpreter.getInterpretationContext().currentProgramName, this) }
            while (interpreter.eval(endExpression).asBoolean().value) {
                interpreter.execute(body)
                loopCounter++
            }
            interpreter.log {
                val elapsed = System.currentTimeMillis() - startTime
                DowStatemenExecutionLogEnd(
                        interpreter.getInterpretationContext().currentProgramName,
                        this,
                        elapsed,
                        loopCounter
                )
            }
        } catch (e: LeaveException) {
            interpreter.log {
                val elapsed = System.currentTimeMillis() - startTime
                DowStatemenExecutionLogEnd(
                        interpreter.getInterpretationContext().currentProgramName,
                        this,
                        elapsed,
                        loopCounter
                )
            }
        }
    }
}

@Serializable
data class DouStmt(
    val endExpression: Expression,
    override val body: List<Statement>,
    override val position: Position? = null
) : Statement(position), CompositeStatement {
    override fun execute(interpreter: InterpreterCore) {
        var loopCounter: Long = 0
        val startTime = System.currentTimeMillis()
        try {
            interpreter.log { DouStatemenExecutionLogStart(interpreter.getInterpretationContext().currentProgramName, this) }
            do {
                interpreter.execute(body)
                loopCounter++
            } while (!interpreter.eval(endExpression).asBoolean().value)
            interpreter.log {
                val elapsed = System.currentTimeMillis() - startTime
                DouStatemenExecutionLogEnd(
                        interpreter.getInterpretationContext().currentProgramName,
                        this,
                        elapsed,
                        loopCounter
                )
            }
        } catch (e: LeaveException) {
            interpreter.log {
                val elapsed = System.currentTimeMillis() - startTime
                DouStatemenExecutionLogEnd(
                        interpreter.getInterpretationContext().currentProgramName,
                        this,
                        elapsed,
                        loopCounter
                )
            }
        }
    }
}

@Serializable
data class LeaveSrStmt(override val position: Position? = null) : Statement(position) {
    override fun execute(interpreter: InterpreterCore) {
        interpreter.log { LeaveSrStatemenExecutionLog(interpreter.getInterpretationContext().currentProgramName, this) }
        throw LeaveSrException()
    }
}

@Serializable
data class LeaveStmt(override val position: Position? = null) : Statement(position) {
    override fun execute(interpreter: InterpreterCore) {
        interpreter.log { LeaveStatemenExecutionLog(interpreter.getInterpretationContext().currentProgramName, this) }
        throw LeaveException()
    }
}

@Serializable
data class IterStmt(override val position: Position? = null) : Statement(position) {
    override fun execute(interpreter: InterpreterCore) {
        interpreter.log { IterStatemenExecutionLog(interpreter.getInterpretationContext().currentProgramName, this) }
        throw IterException()
    }
}

@Serializable
data class OtherStmt(override val position: Position? = null) : Statement(position) {
    override fun execute(interpreter: InterpreterCore) {
        TODO("Not yet implemented")
    }
}

@Serializable
data class TagStmt private constructor(val tag: String, override val position: Position? = null) : Statement(position) {
    companion object {
        operator fun invoke(tag: String, position: Position? = null): TagStmt = TagStmt(tag.toUpperCase(), position)
    }
    override fun execute(interpreter: InterpreterCore) {
        // Nothing to do here
    }
}

@Serializable
data class GotoStmt(val tag: String, override val position: Position? = null) : Statement(position) {
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
    override fun execute(interpreter: InterpreterCore) {
        val comparisonResult = comparison.verify(factor1, factor2, interpreter, interpreter.getLocalizationContext().charset)
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
) : Statement(position), CompositeStatement {
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

    override fun accept(mutes: MutableMap<Int, MuteParser.MuteLineContext>, start: Int, end: Int): MutableList<MuteAnnotationResolved> {
        // TODO check if the annotation is the last statement
        return acceptBody(body, mutes, start, end)
    }

    override fun execute(interpreter: InterpreterCore) {
        var loopCounter: Long = 0
        val startTime = System.currentTimeMillis()

        interpreter.eval(init)
        val iterVar = iterDataDefinition()
        try {
            interpreter.log { ForStatementExecutionLogStart(interpreter.getInterpretationContext().currentProgramName, this) }
            var step = interpreter.eval(byValue).asInt().value
            if (downward) {
                step *= -1
            }
            while (interpreter.enterCondition(interpreter[iterVar], interpreter.eval(endValue), downward)) {
                try {
                    interpreter.execute(body)
                } catch (e: IterException) {
                    // nothing to do here
                }

                interpreter.increment(iterVar, step)
                loopCounter++
            }
            interpreter.log {
                val elapsed = System.currentTimeMillis() - startTime
                ForStatementExecutionLogEnd(
                        interpreter.getInterpretationContext().currentProgramName,
                        this,
                        elapsed,
                        loopCounter
                )
            }
        } catch (e: LeaveException) {
            // leaving
            interpreter.log {
                val elapsed = System.currentTimeMillis() - startTime
                ForStatementExecutionLogEnd(
                        interpreter.getInterpretationContext().currentProgramName,
                        this,
                        elapsed,
                        loopCounter
                )
            }
        }
    }
}

/*
* For an array data structure, the keyed-ds-array operand is a qualified name consisting
* of the array to be sorted followed by the subfield to be used as a key for the sort.
*/
@Serializable
data class SortAStmt(val target: Expression, override val position: Position? = null) : Statement(position) {
    override fun execute(interpreter: InterpreterCore) {
        sortA(interpreter.eval(target), interpreter.getLocalizationContext().charset)
    }
}

@Serializable
data class CatStmt(
    val left: Expression?,
    val right: Expression,
    val target: AssignableExpression,
    val blanksInBetween: Int,
    @Derived val dataDefinition: InStatementDataDefinition? = null,
    override val position: Position? = null
) : Statement(position), StatementThatCanDefineData {

    override fun execute(interpreter: InterpreterCore) {
        val blanksInBetween = blanksInBetween
        val blanks = StringValue.blank(blanksInBetween)
        val factor2 = interpreter.eval(right)
        var result = interpreter.eval(target)
        val resultLen = result.asString().length()
        val concatenatedFactors: Value

        if (null != left) {
            val factor1 = interpreter.eval(left)
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

        interpreter.assign(target, result)
        interpreter.log { CatStatementExecutionLog(interpreter.getInterpretationContext().currentProgramName, this, interpreter.eval(target)) }
    }

    override fun dataDefinition(): List<InStatementDataDefinition> = dataDefinition?.let { listOf(it) } ?: emptyList()
}

@Serializable
data class LookupStmt(
    val left: Expression,
    val right: Expression,
    val rightIndicators: WithRightIndicators,
    override val position: Position? = null
) : Statement(position), WithRightIndicators by rightIndicators {
    override fun execute(interpreter: InterpreterCore) {
        lookUp(this, interpreter, interpreter.getLocalizationContext().charset)
    }
}

@Serializable
data class ScanStmt(
    val left: Expression,
    val leftLength: Int?,
    val right: Expression,
    val startPosition: Int,
    val target: AssignableExpression,
    val rightIndicators: WithRightIndicators,
    override val position: Position? = null
) : Statement(position), WithRightIndicators by rightIndicators {

    override fun execute(interpreter: InterpreterCore) {
        val stringToSearch = interpreter.eval(left).asString().value.substringOfLength(leftLength)
        val searchInto = interpreter.eval(right).asString().value.substring(startPosition - 1)
        val occurrences = mutableListOf<Value>()
        var index = -1
        do {
            index = searchInto.indexOf(stringToSearch, index + 1)
            if (index >= 0) occurrences.add(IntValue((index + startPosition).toLong()))
        } while (index >= 0)
        if (occurrences.isEmpty()) {
            interpreter.setIndicators(this, BooleanValue.FALSE, BooleanValue.FALSE, BooleanValue.FALSE)
        } else {
            interpreter.setIndicators(this, BooleanValue.FALSE, BooleanValue.FALSE, BooleanValue.TRUE)
            if (target.type().isArray()) {
                val fullOccurrences = occurrences.resizeTo(target.type().numberOfElements(), IntValue.ZERO).toMutableList()
                interpreter.assign(target, ConcreteArrayValue(fullOccurrences, target.type().asArray().element))
            } else {
                interpreter.assign(target, occurrences[0])
            }
        }
    }
}

@Serializable
data class XFootStmt(
    val left: Expression,
    val result: AssignableExpression,
    val rightIndicators: WithRightIndicators,
    @Derived val dataDefinition: InStatementDataDefinition? = null,
    override val position: Position? = null
) : Statement(position), WithRightIndicators by rightIndicators, StatementThatCanDefineData {
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
