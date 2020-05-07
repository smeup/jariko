package com.smeup.rpgparser.parsing.ast

import com.smeup.rpgparser.MuteParser
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.parsetreetoast.acceptBody
import com.smeup.rpgparser.parsing.parsetreetoast.toAst
import com.smeup.rpgparser.utils.ComparisonOperator
import com.strumenta.kolasu.model.*
import kotlin.system.measureTimeMillis
import java.util.*

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

typealias IndicatorKey = Int
data class IndicatorCondition(val key: IndicatorKey, val negate: Boolean)
data class ContinuedIndicator(val key: IndicatorKey, val negate: Boolean, val level: String)

abstract class Statement(
    override val position: Position? = null,
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
    var continuedIndicators: HashMap<Int, ContinuedIndicator> = HashMap<Int, ContinuedIndicator>()

    @Throws(ControlFlowException::class, IllegalArgumentException::class, NotImplementedError::class, RuntimeException::class)
    abstract fun execute(internal_interpreter: InternalInterpreter)
}

data class ExecuteSubroutine(var subroutine: ReferenceByName<Subroutine>, override val position: Position? = null) : Statement(position) {
    override fun execute(internal_interpreter: InternalInterpreter) {
        internal_interpreter.log {
            SubroutineExecutionLogStart(
                    internal_interpreter.interpretationContext.currentProgramName,
                    subroutine.referred!!
            )
        }
        val elapsed = measureTimeMillis {
            try {
                internal_interpreter.execute(subroutine.referred!!.stmts)
            } catch (e: LeaveSrException) {
                // Nothing to do here
            } catch (e: GotoException) {
                if (!e.tag.equals(subroutine.referred!!.tag, true)) throw e
            }
        }
        internal_interpreter.log {
            SubroutineExecutionLogEnd(
                    internal_interpreter.interpretationContext.currentProgramName,
                    subroutine.referred!!,
                    elapsed
            )
        }
    }
}

data class SelectStmt(
    var cases: List<SelectCase>,
    var other: SelectOtherClause? = null,
    override val position: Position? = null
) : Statement(position) {
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

    override fun execute(internal_interpreter: InternalInterpreter) {
        for (case in this.cases) {
            val result = internal_interpreter.eval(case.condition)

            internal_interpreter.log { SelectCaseExecutionLogEntry(internal_interpreter.interpretationContext.currentProgramName, case, result) }
            if (result.asBoolean().value) {
                internal_interpreter.execute(case.body)
                return
            }
        }
        if (this.other != null) {
            internal_interpreter.log {
                SelectOtherExecutionLogEntry(
                        internal_interpreter.interpretationContext.currentProgramName,
                        this.other!!
                )
            }
            internal_interpreter.execute(this.other!!.body)
        }
    }
}

data class SelectOtherClause(val body: List<Statement>, override val position: Position? = null) : Node(position)

data class SelectCase(val condition: Expression, val body: List<Statement>, override val position: Position? = null) : Node(position)

data class EvalFlags(
    val halfAdjust: Boolean = false,
    val maximumNumberOfDigitsRule: Boolean = false,
    val resultDecimalPositionRule: Boolean = false
)

data class EvalStmt(
    val target: AssignableExpression,
    var expression: Expression,
    val operator: AssignmentOperator = AssignmentOperator.NORMAL_ASSIGNMENT,
    val flags: EvalFlags = EvalFlags(),
    override val position: Position? = null
) :
    Statement(position) {
    override fun execute(internal_interpreter: InternalInterpreter) {
            // Should I assign it one by one?
            val result = if (target.type().isArray() &&
                    target.type().asArray().element.canBeAssigned(expression.type())) {
                internal_interpreter.assignEachElement(target, expression, operator)
            } else {
                internal_interpreter.assign(target, expression, operator)
            }
        internal_interpreter.log { EvaluationLogEntry(internal_interpreter.interpretationContext.currentProgramName, this, result) }
    }
}

data class SubDurStmt(
    val factor1: Expression?,
    val target: AssignableExpression,
    val factor2: Expression,
    override val position: Position? = null
) :
    Statement(position) {
    override fun execute(internal_interpreter: InternalInterpreter) {
            when (target) {
                is DataRefExpr -> {
                    // TODO: partial implementation just for *MS - Add more cases
                    val minuend = if (factor1 == null) {
                        internal_interpreter.eval(target)
                    } else {
                        internal_interpreter.eval(factor1)
                    }
                    val subtrahend = internal_interpreter.eval(factor2)
                    val newValue =
                            (minuend.asTimeStamp().value.time - subtrahend.asTimeStamp().value.time) * 1000
                    internal_interpreter.assign(target, IntValue(newValue))
                }
                else -> throw UnsupportedOperationException("Data reference required: " + this)
            }
        }
    }

data class MoveStmt(
    val target: AssignableExpression,
    var expression: Expression,
    override val position: Position? = null
) :
    Statement(position) {
    override fun execute(internal_interpreter: InternalInterpreter) {
        val value = move(target, expression, internal_interpreter)
        internal_interpreter.log { MoveStatemenExecutionLog(internal_interpreter.interpretationContext.currentProgramName, this, value) }
    }
}

data class MoveAStmt(
    val operationExtender: String?,
    val target: AssignableExpression,
    var expression: Expression,
    override val position: Position? = null
) :
    Statement(position) {
    override fun execute(internal_interpreter: InternalInterpreter) {
        val value = movea(operationExtender, target, expression, internal_interpreter)
        internal_interpreter.log {
            MoveAStatemenExecutionLog(internal_interpreter.interpretationContext.currentProgramName, this, value)
        }
    }
}

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

        override fun execute(internal_interpreter: InternalInterpreter) {
            val value = movel(operationExtender, target, expression, internal_interpreter)
            internal_interpreter.log { MoveLStatemenExecutionLog(internal_interpreter.interpretationContext.currentProgramName, this, value) }
        }
    }

    // TODO add other parameters
    data class ChainStmt(
        val searchArg: Expression, // Factor1
        val name: String, // Factor 2
        override val position: Position? = null
    ) :
            Statement(position) {
        override fun execute(internal_interpreter: InternalInterpreter) {
            val dbFile = internal_interpreter.dbFile(name, this)
            val record = if (searchArg.type() is KListType) {
                dbFile.chain(internal_interpreter.toSearchValues(searchArg))
            } else {
                dbFile.chain(internal_interpreter.eval(searchArg))
            }
            internal_interpreter.fillDataFrom(record)
        }
    }

    data class ReadEqualStmt(
        val searchArg: Expression?, // Factor1
        val name: String, // Factor 2
        override val position: Position? = null
    ) :
            Statement(position) {
        override fun execute(internal_interpreter: InternalInterpreter) {
            val dbFile = internal_interpreter.dbFile(name, this)
            val record = when {
                searchArg == null -> dbFile.readEqual()
                searchArg.type() is KListType -> dbFile.readEqual(internal_interpreter.toSearchValues(searchArg))
                else -> dbFile.readEqual(internal_interpreter.eval(searchArg))
            }
            internal_interpreter.fillDataFrom(record)
        }
    }

    data class ReadPreviousStmt(
        val searchArg: Expression?, // Factor1
        val name: String, // Factor 2
        override val position: Position? = null
    ) :
            Statement(position) {
        override fun execute(internal_interpreter: InternalInterpreter) {
            val dbFile = internal_interpreter.dbFile(name, this)
            val record = when {
                searchArg == null -> dbFile.readPrevious()
                searchArg.type() is KListType -> dbFile.readPrevious(internal_interpreter.toSearchValues(searchArg))
                else -> dbFile.readPrevious(internal_interpreter.eval(searchArg))
            }
            internal_interpreter.fillDataFrom(record)
        }
    }

    data class ReadStmt(
        val name: String, // Factor 2
        override val position: Position? = null
    ) :
            Statement(position) {
        override fun execute(internal_interpreter: InternalInterpreter) {
            val dbFile = internal_interpreter.dbFile(name, this)
            val record = dbFile.read()
            internal_interpreter.fillDataFrom(record)
        }
    }

    data class SetllStmt(
        val searchArg: Expression, // Factor1
        val name: String, // Factor 2
        override val position: Position? = null
    ) : Statement(position) {
        override fun execute(internal_interpreter: InternalInterpreter) {
            val dbFile = internal_interpreter.dbFile(name, this)
            internal_interpreter.status.lastFound = if (searchArg.type() is KListType) {
                dbFile.setll(internal_interpreter.toSearchValues(searchArg))
            } else {
                dbFile.setll(internal_interpreter.eval(searchArg))
            }
        }
    }

    data class CheckStmt(
        val comparatorString: Expression, // Factor1
        val baseString: Expression,
        val start: Int = 1,
        val wrongCharPosition: AssignableExpression?,
        override val position: Position? = null
    ) : Statement(position) {
        override fun execute(internal_interpreter: InternalInterpreter) {
            var baseString = internal_interpreter.eval(this.baseString).asString().value
            if (this.baseString is DataRefExpr) {
                baseString = baseString.padEnd(this.baseString.size())
            }
            val charSet = internal_interpreter.eval(comparatorString).asString().value
            val wrongIndex = wrongCharPosition
            internal_interpreter.status.lastFound = false
            if (wrongIndex != null) {
                internal_interpreter.assign(wrongIndex, IntValue.ZERO)
            }
            baseString.substring(start - 1).forEachIndexed { i, c ->
                if (!charSet.contains(c)) {
                    if (wrongIndex != null) {
                        internal_interpreter.assign(wrongIndex, IntValue((i + start).toLong()))
                    }
                    internal_interpreter.status.lastFound = true
                    return
                }
            }
        }
    }

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

        override fun execute(internal_interpreter: InternalInterpreter) {
            internal_interpreter.log { CallExecutionLogEntry(internal_interpreter.interpretationContext.currentProgramName, this) }
            val callStatement = this
            val programToCall = internal_interpreter.eval(expression).asString().value
            val program = internal_interpreter.systemInterface.findProgram(programToCall)
            require(program != null) {
                "Line: ${this.position.line()} - Program $programToCall cannot be found"
            }

            val params = this.params.mapIndexed { index, it ->
                if (it.dataDefinition != null) {
                    if (it.dataDefinition.initializationValue != null) {
                        if (!internal_interpreter.exists(it.param.name)) {
                            internal_interpreter.assign(it.dataDefinition, internal_interpreter.eval(it.dataDefinition.initializationValue))
                        } else {
                            internal_interpreter.assign(
                                    internal_interpreter.dataDefinitionByName(it.param.name)!!,
                                    internal_interpreter.eval(it.dataDefinition.initializationValue)
                            )
                        }
                    } else {
                        if (!internal_interpreter.exists(it.param.name)) {
                            internal_interpreter.assign(it.dataDefinition, internal_interpreter.eval(BlanksRefExpr()))
                        }
                    }
                }
                require(program.params().size > index) {
                    "Line: ${this.position.line()} - Parameter nr. ${index + 1} can't be found"
                }
                program.params()[index].name to internal_interpreter.get(it.param.name)
            }.toMap(LinkedHashMap())

            val startTime = System.currentTimeMillis()
            val paramValuesAtTheEnd =
                    try {
                        internal_interpreter.systemInterface.registerProgramExecutionStart(program, params)
                        program.execute(internal_interpreter.systemInterface, params).apply {
                            internal_interpreter.log { CallEndLogEntry("", callStatement, System.currentTimeMillis() - startTime) }
                        }
                    } catch (e: Exception) { // TODO Catch a more specific exception?
                        internal_interpreter.log { CallEndLogEntry("", callStatement, System.currentTimeMillis() - startTime) }
                        if (errorIndicator == null) {
                            throw e
                        }
                        internal_interpreter.predefinedIndicators[errorIndicator] = BooleanValue.TRUE
                        null
                    }
            paramValuesAtTheEnd?.forEachIndexed { index, value ->
                internal_interpreter.assign(this.params[index].param.referred!!, value)
            }
        }
    }

    data class KListStmt
    private constructor(val name: String, val fields: List<String>, override val position: Position?) : Statement(position), StatementThatCanDefineData {
        companion object {
            operator fun invoke(name: String, fields: List<String>, position: Position? = null): KListStmt {
                return KListStmt(name.toUpperCase(), fields, position)
            }
        }

        override fun dataDefinition(): List<InStatementDataDefinition> = listOf(InStatementDataDefinition(name, KListType))

        override fun execute(internal_interpreter: InternalInterpreter) {
            // TODO Add logging as for PlistStmt
            internal_interpreter.klists[name] = fields
        }
    }

    data class IfStmt(
        val condition: Expression,
        val body: List<Statement>,
        val elseIfClauses: List<ElseIfClause> = emptyList(),
        val elseClause: ElseClause? = null,
        override val position: Position? = null
    ) : Statement(position) {

        override fun accept(mutes: MutableMap<Int, MuteParser.MuteLineContext>, start: Int, end: Int): MutableList<MuteAnnotationResolved> {
            // check if the annotation is just before the ELSE
            val muteAttached: MutableList<MuteAnnotationResolved> = mutableListOf()

            // Process the body statements
            muteAttached.addAll(
                    acceptBody(body, mutes, this.position!!.start.line, this.position.end.line)
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

        override fun execute(internal_interpreter: InternalInterpreter) {
            val condition = internal_interpreter.eval(condition)
            internal_interpreter.log { IfExecutionLogEntry(internal_interpreter.interpretationContext.currentProgramName, this, condition) }
            if (condition.asBoolean().value) {
                internal_interpreter.execute(this.body)
            } else {
                for (elseIfClause in elseIfClauses) {
                    val c = internal_interpreter.eval(elseIfClause.condition)
                    internal_interpreter.log { ElseIfExecutionLogEntry(internal_interpreter.interpretationContext.currentProgramName, elseIfClause, c) }
                    if (c.asBoolean().value) {
                        internal_interpreter.execute(elseIfClause.body)
                        return
                    }
                }
                if (elseClause != null) {
                    internal_interpreter.log {
                        ElseExecutionLogEntry(
                                internal_interpreter.interpretationContext.currentProgramName,
                                elseClause,
                                condition
                        )
                    }
                    internal_interpreter.execute(elseClause.body)
                }
            }
        }
    }

    data class ElseClause(val body: List<Statement>, override val position: Position? = null) : Node(position)

    data class ElseIfClause(val condition: Expression, val body: List<Statement>, override val position: Position? = null) : Node(position)

    data class SetStmt(val valueSet: ValueSet, val indicators: List<AssignableExpression>, override val position: Position? = null) : Statement(position) {
        enum class ValueSet {
            ON,
            OFF
        }

        override fun execute(internal_interpreter: InternalInterpreter) {
            indicators.forEach {
                when (it) {
                    is DataWrapUpIndicatorExpr -> internal_interpreter.interpretationContext.setDataWrapUpPolicy(it.dataWrapUpChoice)
                    is PredefinedIndicatorExpr -> {
                        if (valueSet.name == "ON") {
                            internal_interpreter.predefinedIndicators[it.index] = BooleanValue.TRUE
                        } else {
                            internal_interpreter.predefinedIndicators[it.index] = BooleanValue.FALSE
                        }
                    }
                    else -> TODO()
                }
            }
        }
    }

    data class ReturnStmt(val expression: Expression?, override val position: Position? = null) : Statement(position) {
        override fun execute(internal_interpreter: InternalInterpreter) {
            val returnValue = expression?.let { internal_interpreter.eval(expression) }
            throw ReturnException(returnValue)
        }
    }

    // A Plist is a list of parameters
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

        override fun execute(internal_interpreter: InternalInterpreter) {
            params.forEach {
                if (internal_interpreter.globalSymbolTable.contains(it.param.name)) {
                    val value = internal_interpreter.globalSymbolTable[it.param.name]
                    internal_interpreter.log {
                        ParamListStatemenExecutionLog(
                                internal_interpreter.interpretationContext.currentProgramName,
                                this,
                                it.param.name,
                                value
                        )
                    }
                }
            }
        }
    }

    data class PlistParam(
        val param: ReferenceByName<AbstractDataDefinition>,
        // TODO @Derived????
        @Derived val dataDefinition: InStatementDataDefinition? = null,
        override val position: Position? = null
    ) : Node(position)

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

        override fun execute(internal_interpreter: InternalInterpreter) {
            return when (value) {
                is DataRefExpr -> {
                    val value = internal_interpreter.assign(value, BlanksRefExpr())
                    internal_interpreter.log {
                        ClearStatemenExecutionLog(
                                internal_interpreter.interpretationContext.currentProgramName,
                                this,
                                value
                        )
                    }
                    Unit
                }
                is PredefinedIndicatorExpr -> {
                    val value = internal_interpreter.assign(value, BlanksRefExpr())
                    internal_interpreter.log {
                        ClearStatemenExecutionLog(
                                internal_interpreter.interpretationContext.currentProgramName,
                                this,
                                value
                        )
                    }
                }
                else -> throw UnsupportedOperationException("I do not know how to clear ${this.value}")
            }
        }
    }

    data class DefineStmt(
        val originalName: String,
        val newVarName: String,
        override val position: Position? = null
    ) : Statement(position), StatementThatCanDefineData {
        override fun dataDefinition(): List<InStatementDataDefinition> {
            val containingCU = this.ancestor(CompilationUnit::class.java)
                    ?: throw IllegalStateException("Not contained in a CU")
            val originalDataDefinition = containingCU.dataDefinitions.find { it.name == originalName }
            if (originalDataDefinition != null) {
                return listOf(InStatementDataDefinition(newVarName, originalDataDefinition.type, position))
            } else {
                val inStatementDataDefinition =
                        containingCU.main.stmts
                                .filterIsInstance(StatementThatCanDefineData::class.java)
                                .asSequence()
                                .map(StatementThatCanDefineData::dataDefinition)
                                .flatten()
                                .find { it.name == originalName }
                return listOf(InStatementDataDefinition(newVarName, inStatementDataDefinition!!.type, position))
            }
        }

        override fun execute(internal_interpreter: InternalInterpreter) {
            // Nothing to do here
        }
    }

    interface WithRightIndicators {
        fun allPresent(): Boolean = hi != null && lo != null && eq != null

        val hi: IndicatorKey?
        val lo: IndicatorKey?
        val eq: IndicatorKey?
    }

    data class RightIndicators(
        override val hi: IndicatorKey?,
        override val lo: IndicatorKey?,
        override val eq: IndicatorKey?
    ) : WithRightIndicators

    data class CompStmt(
        val left: Expression,
        val right: Expression,
        val rightIndicators: WithRightIndicators,
        override val position: Position? = null
    ) : Statement(position), WithRightIndicators by rightIndicators {
        override fun execute(internal_interpreter: InternalInterpreter) {
            when (internal_interpreter.compareExpressions(left, right, internal_interpreter.localizationContext.charset)) {
                Comparison.GREATER -> internal_interpreter.setPredefinedIndicators(this, BooleanValue.TRUE, BooleanValue.FALSE, BooleanValue.FALSE)
                Comparison.SMALLER -> internal_interpreter.setPredefinedIndicators(this, BooleanValue.FALSE, BooleanValue.TRUE, BooleanValue.FALSE)
                Comparison.EQUAL -> internal_interpreter.setPredefinedIndicators(this, BooleanValue.FALSE, BooleanValue.FALSE, BooleanValue.TRUE)
            }
        }
    }

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

        override fun execute(internal_interpreter: InternalInterpreter) {
            internal_interpreter.assign(target, internal_interpreter.eval(expression))
        }
    }

    data class MultStmt(
        val target: AssignableExpression,
        val halfAdjust: Boolean = false,
        val factor1: Expression?,
        val factor2: Expression,
        override val position: Position? = null
    ) : Statement(position) {
        override fun execute(internal_interpreter: InternalInterpreter) {
            internal_interpreter.assign(target, internal_interpreter.mult(this))
        }
    }

    data class DivStmt(
        val target: AssignableExpression,
        val halfAdjust: Boolean = false,
        val factor1: Expression?,
        val factor2: Expression,
        override val position: Position? = null
    ) : Statement(position) {
        override fun execute(internal_interpreter: InternalInterpreter) {
            internal_interpreter.assign(target, internal_interpreter.div(this))
        }
    }

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

        override fun execute(internal_interpreter: InternalInterpreter) {
            internal_interpreter.assign(result, internal_interpreter.add(this))
        }
    }

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

        override fun execute(internal_interpreter: InternalInterpreter) {
            val value = internal_interpreter.eval(expression)
            require(value is NumberValue) {
                "$value should be a number"
            }
            internal_interpreter.assign(target, value.negate())
        }
    }

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

        override fun execute(internal_interpreter: InternalInterpreter) {
            internal_interpreter.assign(result, internal_interpreter.sub(this))
        }
    }

    data class TimeStmt(
        val value: Expression,
        override val position: Position? = null
    ) : Statement(position) {
        override fun execute(internal_interpreter: InternalInterpreter) {
            when (value) {
                is DataRefExpr -> {
                    internal_interpreter.assign(value, TimeStampValue(Date()))
                }
                else -> throw UnsupportedOperationException("I do not know how to set TIME to ${this.value}")
            }
        }
    }

    data class DisplayStmt(val factor1: Expression?, val response: Expression?, override val position: Position? = null) : Statement(position) {
        override fun execute(internal_interpreter: InternalInterpreter) {
            val values = mutableListOf<Value>()
            factor1?.let { values.add(internal_interpreter.eval(it)) }
            response?.let { values.add(internal_interpreter.eval(it)) }
            // TODO: receive input from systemInterface and assign value to response
            internal_interpreter.systemInterface.display(internal_interpreter.rawRender(values))
        }
    }

    data class DoStmt(
        val endLimit: Expression,
        val index: AssignableExpression?,
        val body: List<Statement>,
        val startLimit: Expression = IntLiteral(1),
        override val position: Position? = null
    ) : Statement(position) {
        override fun accept(mutes: MutableMap<Int, MuteParser.MuteLineContext>, start: Int, end: Int): MutableList<MuteAnnotationResolved> {
            // TODO check if the annotation is the last statement
            return acceptBody(body, mutes, start, end)
        }

        override fun execute(internal_interpreter: InternalInterpreter) {
            var loopCounter: Long = 0
            val startTime = System.currentTimeMillis()
            val endLimitExpression = endLimit
            val endLimit: () -> Long = internal_interpreter.optimizedIntExpression(endLimitExpression)
            if (index == null) {
                var myIterValue = internal_interpreter.eval(startLimit).asInt().value
                try {
                    internal_interpreter.log { DoStatemenExecutionLogStart(internal_interpreter.interpretationContext.currentProgramName, this) }
                    while (myIterValue <= endLimit()) {
                        try {
                            internal_interpreter.execute(body)
                        } catch (e: IterException) {
                            // nothing to do here
                        }
                        loopCounter++
                        myIterValue++
                    }
                    internal_interpreter.log {
                        val elapsed = System.currentTimeMillis() - startTime
                        DoStatemenExecutionLogEnd(
                                internal_interpreter.interpretationContext.currentProgramName,
                                this,
                                elapsed,
                                loopCounter
                        )
                    }
                } catch (e: LeaveException) {
                    // nothing to do here
                    internal_interpreter.log {
                        val elapsed = System.currentTimeMillis() - startTime
                        DoStatemenExecutionLogEnd(
                                internal_interpreter.interpretationContext.currentProgramName,
                                this,
                                elapsed,
                                loopCounter
                        )
                    }
                }
            } else {
                internal_interpreter.assign(index, startLimit)
                try {
                    val indexExpression = internal_interpreter.optimizedIntExpression(index)
                    while (indexExpression() <= endLimit()) {
                        try {
                            internal_interpreter.execute(body)
                        } catch (e: IterException) {
                            // nothing to do here
                        }
                        internal_interpreter.assign(index, PlusExpr(index, IntLiteral(1)))
                    }
                } catch (e: LeaveException) {
                    // nothing to do here
                }
            }
        }
    }

    data class DowStmt(
        val endExpression: Expression,
        val body: List<Statement>,
        override val position: Position? = null
    ) : Statement(position) {
        override fun execute(internal_interpreter: InternalInterpreter) {
            var loopCounter: Long = 0
            val startTime = System.currentTimeMillis()
            try {
                internal_interpreter.log { DowStatemenExecutionLogStart(internal_interpreter.interpretationContext.currentProgramName, this) }
                while (internal_interpreter.eval(endExpression).asBoolean().value) {
                    internal_interpreter.execute(body)
                    loopCounter++
                }
                internal_interpreter.log {
                    val elapsed = System.currentTimeMillis() - startTime
                    DowStatemenExecutionLogEnd(
                            internal_interpreter.interpretationContext.currentProgramName,
                            this,
                            elapsed,
                            loopCounter
                    )
                }
            } catch (e: LeaveException) {
                internal_interpreter.log {
                    val elapsed = System.currentTimeMillis() - startTime
                    DowStatemenExecutionLogEnd(
                            internal_interpreter.interpretationContext.currentProgramName,
                            this,
                            elapsed,
                            loopCounter
                    )
                }
            }
        }
    }

    data class DouStmt(
        val endExpression: Expression,
        val body: List<Statement>,
        override val position: Position? = null
    ) : Statement(position) {
        override fun execute(internal_interpreter: InternalInterpreter) {
            var loopCounter: Long = 0
            val startTime = System.currentTimeMillis()
            try {
                internal_interpreter.log { DouStatemenExecutionLogStart(internal_interpreter.interpretationContext.currentProgramName, this) }
                do {
                    internal_interpreter.execute(body)
                    loopCounter++
                } while (!internal_interpreter.eval(endExpression).asBoolean().value)
                internal_interpreter.log {
                    val elapsed = System.currentTimeMillis() - startTime
                    DouStatemenExecutionLogEnd(
                            internal_interpreter.interpretationContext.currentProgramName,
                            this,
                            elapsed,
                            loopCounter
                    )
                }
            } catch (e: LeaveException) {
                internal_interpreter.log {
                    val elapsed = System.currentTimeMillis() - startTime
                    DouStatemenExecutionLogEnd(
                            internal_interpreter.interpretationContext.currentProgramName,
                            this,
                            elapsed,
                            loopCounter
                    )
                }
            }
        }
    }

    data class LeaveSrStmt(override val position: Position? = null) : Statement(position) {
        override fun execute(internal_interpreter: InternalInterpreter) {
            internal_interpreter.log { LeaveSrStatemenExecutionLog(internal_interpreter.interpretationContext.currentProgramName, this) }
            throw LeaveSrException()
        }
    }

    data class LeaveStmt(override val position: Position? = null) : Statement(position) {
        override fun execute(internal_interpreter: InternalInterpreter) {
            internal_interpreter.log { LeaveStatemenExecutionLog(internal_interpreter.interpretationContext.currentProgramName, this) }
            throw LeaveException()
        }
    }

    data class IterStmt(override val position: Position? = null) : Statement(position) {
        override fun execute(internal_interpreter: InternalInterpreter) {
            internal_interpreter.log { IterStatemenExecutionLog(internal_interpreter.interpretationContext.currentProgramName, this) }
            throw IterException()
        }
    }

    data class OtherStmt(override val position: Position? = null) : Statement(position) {
        override fun execute(internal_interpreter: InternalInterpreter) {
            TODO("Not yet implemented")
        }
    }

    data class TagStmt private constructor(val tag: String, override val position: Position? = null) : Statement(position) {
        companion object {
            operator fun invoke(tag: String, position: Position? = null): TagStmt = TagStmt(tag.toUpperCase(), position)
        }
        override fun execute(internal_interpreter: InternalInterpreter) {
            // Nothing to do here
        }
    }

    data class GotoStmt(val tag: String, override val position: Position? = null) : Statement(position) {
        override fun execute(internal_interpreter: InternalInterpreter) {
            throw GotoException(tag)
        }
    }

    data class CabStmt(
        val factor1: Expression,
        val factor2: Expression,
        val comparison: ComparisonOperator?,
        val tag: String,
        val rightIndicators: WithRightIndicators,
        override val position: Position? = null
    ) : Statement(position), WithRightIndicators by rightIndicators {
        override fun execute(internal_interpreter: InternalInterpreter) {
            val comparisonResult = comparison.verify(factor1, factor2, internal_interpreter, internal_interpreter.localizationContext.charset)
            when (comparisonResult.comparison) {
                Comparison.GREATER -> internal_interpreter.setPredefinedIndicators(this, BooleanValue.TRUE, BooleanValue.FALSE, BooleanValue.FALSE)
                Comparison.SMALLER -> internal_interpreter.setPredefinedIndicators(this, BooleanValue.FALSE, BooleanValue.TRUE, BooleanValue.FALSE)
                Comparison.EQUAL -> internal_interpreter.setPredefinedIndicators(this, BooleanValue.FALSE, BooleanValue.FALSE, BooleanValue.TRUE)
            }
            if (comparisonResult.isVerified) throw GotoException(tag)
        }
    }

    data class ForStmt(
        var init: Expression,
        val endValue: Expression,
        val byValue: Expression,
        val downward: Boolean = false,
        val body: List<Statement>,
        override val position: Position? = null
    ) : Statement(position) {
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

        override fun execute(internal_interpreter: InternalInterpreter) {
            var loopCounter: Long = 0
            val startTime = System.currentTimeMillis()

            internal_interpreter.eval(init)
            val iterVar = iterDataDefinition()
            try {
                internal_interpreter.log { ForStatementExecutionLogStart(internal_interpreter.interpretationContext.currentProgramName, this) }
                var step = internal_interpreter.eval(byValue).asInt().value
                if (downward) {
                    step *= -1
                }
                while (internal_interpreter.enterCondition(internal_interpreter[iterVar], internal_interpreter.eval(endValue), downward)) {
                    try {
                        internal_interpreter.execute(body)
                    } catch (e: IterException) {
                        // nothing to do here
                    }

                    internal_interpreter.increment(iterVar, step)
                    loopCounter++
                }
                internal_interpreter.log {
                    val elapsed = System.currentTimeMillis() - startTime
                    ForStatementExecutionLogEnd(
                            internal_interpreter.interpretationContext.currentProgramName,
                            this,
                            elapsed,
                            loopCounter
                    )
                }
            } catch (e: LeaveException) {
                // leaving
                internal_interpreter.log {
                    val elapsed = System.currentTimeMillis() - startTime
                    ForStatementExecutionLogEnd(
                            internal_interpreter.interpretationContext.currentProgramName,
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
    data class SortAStmt(val target: Expression, override val position: Position? = null) : Statement(position) {
        override fun execute(internal_interpreter: InternalInterpreter) {
            sortA(internal_interpreter.eval(target), internal_interpreter.localizationContext.charset)
        }
    }

    data class CatStmt(val left: Expression?, val right: Expression, val target: AssignableExpression, val blanksInBetween: Int, override val position: Position? = null) : Statement(position) {
        override fun execute(internal_interpreter: InternalInterpreter) {
            val blanksInBetween = blanksInBetween
            val blanks = StringValue.blank(blanksInBetween)
            val factor2 = internal_interpreter.eval(right)
            var result = internal_interpreter.eval(target)
            val resultLen = result.asString().length()
            var concatenatedFactors: Value

            if (null != left) {
                val factor1 = internal_interpreter.eval(left)
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

            internal_interpreter.assign(target, result)
            internal_interpreter.log { CatStatementExecutionLog(internal_interpreter.interpretationContext.currentProgramName, this, internal_interpreter.eval(target)) }
        }
    }

    data class LookupStmt(
        val left: Expression,
        val right: Expression,
        val rightIndicators: WithRightIndicators,
        override val position: Position? = null
    ) : Statement(position), WithRightIndicators by rightIndicators {
        override fun execute(internal_interpreter: InternalInterpreter) {
            lookUp(this, internal_interpreter, internal_interpreter.localizationContext.charset)
        }
    }

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

        override fun execute(internal_interpreter: InternalInterpreter) {
            xfoot(this, internal_interpreter)
        }
    }