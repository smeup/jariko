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

import com.smeup.dbnative.file.Record
import com.smeup.rpgparser.execution.ErrorEvent
import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.parsing.facade.SourceReference
import com.smeup.rpgparser.utils.asNonNullString
import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Position
import org.antlr.v4.runtime.CharStream
import java.io.PrintStream
import java.util.*
import java.util.regex.Pattern

abstract class LogEntry(open val programName: String) {

    fun renderHeader(channel: String, filename: String, line: String, sep: String): String {
        return "${sep}$filename${sep}${line}${sep}$channel$sep"
    }

    open fun renderStatement(channel: String, filename: String, sep: String): String {
        return "$channel NOT IMPLEMENTED"
    }
    open fun renderPerformance(channel: String, filename: String, sep: String): String {
        return "$channel NOT IMPLEMENTED"
    }

    open fun renderLoop(channel: String, filename: String, sep: String): String {
        return "$channel NOT IMPLEMENTED"
    }
    open fun renderData(channel: String, filename: String, sep: String): String {
        return "$channel NOT IMPLEMENTED"
    }
    open fun renderExpression(channel: String, filename: String, sep: String): String {
        return "$channel NOT IMPLEMENTED"
    }
    open fun renderResolution(channel: String, filename: String, sep: String): String {
        return "$channel NOT IMPLEMENTED"
    }
    open fun renderErrorEvent(channel: String, filename: String, sep: String): String {
        return "$channel NOT IMPLEMENTED"
    }
}

data class LineLogEntry(override val programName: String, val stmt: Statement) : LogEntry(programName) {
    override fun toString(): String {
        return "Line ${stmt.position.line()}"
    }
}

data class CallExecutionLogEntry(override val programName: String, val callStmt: CallStmt) : LogEntry(programName) {
    override fun toString(): String {
        return "calling $callStmt"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "CALL START${sep}${callStmt.expression.render()}"

        return renderHeader(channel, filename, callStmt.startLine(), sep) + data
    }

    override fun renderResolution(channel: String, filename: String, sep: String): String {
        val data = "CALL ${sep}${callStmt.expression.render()}"

        return renderHeader(channel, filename, callStmt.startLine(), sep) + data
    }
}

class CallEndLogEntry(programName: String, val callStmt: CallStmt, val elapsed: Long, val exception: Exception? = null) : LogEntry(programName) {
    override fun toString(): String {
        return if (exception == null) {
            "end of $callStmt"
        } else {
            "exception $exception in calling $callStmt"
        }
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "CALL END${sep}${callStmt.expression.render()}"

        return renderHeader(channel, filename, callStmt.endLine(), sep) + data
    }
    override fun renderPerformance(channel: String, filename: String, sep: String): String {
        val data = "CALL END${sep}${callStmt.expression.render()}${sep}$elapsed${sep}ms"

        return renderHeader(channel, filename, callStmt.endLine(), sep) + data
    }
}

data class CallPExecutionLogEntry(override val programName: String, val callStmt: CallPStmt) : LogEntry(programName) {
    override fun toString(): String {
        return "calling $callStmt"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "CALLP START${sep}${callStmt.functionCall.render()}"

        return renderHeader(channel, filename, callStmt.startLine(), sep) + data
    }

    override fun renderResolution(channel: String, filename: String, sep: String): String {
        val data = "CALLP ${sep}${callStmt.functionCall.render()}"

        return renderHeader(channel, filename, callStmt.startLine(), sep) + data
    }
}

class CallPEndLogEntry(programName: String, val callStmt: CallPStmt, val elapsed: Long, val exception: Exception? = null) : LogEntry(programName) {
    override fun toString(): String {
        return if (exception == null) {
            "end of $callStmt"
        } else {
            "exception $exception in calling $callStmt"
        }
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "CALLP END${sep}${callStmt.functionCall.render()}"

        return renderHeader(channel, filename, callStmt.endLine(), sep) + data
    }
    override fun renderPerformance(channel: String, filename: String, sep: String): String {
        val data = "CALLP END${sep}${callStmt.functionCall.render()}${sep}$elapsed${sep}ms"

        return renderHeader(channel, filename, callStmt.endLine(), sep) + data
    }
}

data class FindProgramLogEntry(override val programName: String) : LogEntry(programName) {
    override fun renderResolution(channel: String, filename: String, sep: String): String {
        return renderHeader(channel, filename, "", sep)
    }
}

data class RpgProgramFinderLogEntry(override val programName: String) : LogEntry(programName) {
    override fun renderResolution(channel: String, filename: String, sep: String): String {
        return renderHeader(channel, "", "", sep) + programName
    }
}

class SubroutineExecutionLogStart(programName: String, val subroutine: Subroutine) : LogEntry(programName) {
    override fun toString(): String {
        return "executing ${subroutine.name}"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "SUBROUTINE ${sep}${subroutine.name}"

        return renderHeader(channel, filename, subroutine.startLine(), sep) + data
    }

    override fun renderResolution(channel: String, filename: String, sep: String): String {
        val data = "SUBROUTINE START${sep}${subroutine.name}"
        return renderHeader(channel, filename, subroutine.startLine(), sep) + data
    }
}

class SubroutineExecutionLogEnd(programName: String, val subroutine: Subroutine, val elapsed: Long) : LogEntry(programName) {
    override fun toString(): String {
        return "executing ${subroutine.name}"
    }

    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "SUBROUTINE END${sep}${subroutine.name}"

        return renderHeader(channel, filename, subroutine.endLine(), sep) + data
    }
    override fun renderPerformance(channel: String, filename: String, sep: String): String {
        val data = "SUBROUTINE END${sep}${subroutine.name}${sep}$elapsed${sep}ms"

        return renderHeader(channel, filename, subroutine.endLine(), sep) + data
    }
}

class ForStatementExecutionLogStart(programName: String, val statement: ForStmt) : LogEntry(programName) {
    override fun toString(): String {
        return "executing FOR LOOP"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val downward = if (statement.downward == true) "DOWNTO" else "TO"
        val byValue = if (statement.byValue.render() == "1") "" else "BY ${statement.byValue.render()}"
        val data = "FOR ${statement.init.render()} $byValue $downward ${statement.endValue.render()}"

        return renderHeader(channel, filename, statement.startLine(), sep) + data
    }
    override fun renderLoop(channel: String, filename: String, sep: String): String {
        val downward = if (statement.downward == true) "DOWNTO" else "TO"
        val byValue = if (statement.byValue.render() == "1") "" else "BY ${statement.byValue.render()}"
        val data = "FOR ${statement.init.render()} $byValue $downward ${statement.endValue.render()} "

        return renderHeader(channel, filename, statement.startLine(), sep) + data
    }
}

class ForStatementExecutionLogEnd(programName: String, val statement: ForStmt, val elapsed: Long, val loopCounter: Long) : LogEntry(programName) {
    override fun toString(): String {
        return "ending FOR LOOP"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val reference = statement.init.render().substringBefore("=")
        val data = "ENDFOR $reference"

        return renderHeader(channel, filename, statement.endLine(), sep) + data
    }

    override fun renderPerformance(channel: String, filename: String, sep: String): String {
        val reference = statement.init.render().substringBefore("=")
        val data = "ENDFOR ${reference}${sep}${elapsed}${sep}ms"

        return renderHeader(channel, filename, statement.endLine(), sep) + data
    }
    override fun renderLoop(channel: String, filename: String, sep: String): String {
        val reference = statement.init.render().substringBefore("=")
        val data = "ENDFOR ${reference}${sep}${loopCounter}${sep}iterations"

        return renderHeader(channel, filename, statement.endLine(), sep) + data
    }
}
class DoStatemenExecutionLogStart(programName: String, val statement: DoStmt) : LogEntry(programName) {
    override fun toString(): String {
        return "executing DO LOOP"
    }

    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "DO LOOP START${sep}${statement.startLimit.render()} ${statement.endLimit.render()} "

        return renderHeader(channel, filename, statement.startLine(), sep) + data
    }

    override fun renderLoop(channel: String, filename: String, sep: String): String {
        val data = "DO LOOP START${sep}${statement.startLimit.render()} ${statement.endLimit.render()} "

        return renderHeader(channel, filename, statement.startLine(), sep) + data
    }
}
class DoStatemenExecutionLogEnd(programName: String, val statement: DoStmt, val elapsed: Long, val loopCounter: Long) : LogEntry(programName) {
    override fun toString(): String {
        return "ending DO LOOP"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        return renderHeader(channel, filename, statement.endLine(), sep) + "DO LOOP END"
    }

    override fun renderPerformance(channel: String, filename: String, sep: String): String {
        val data = "DO LOOP END${sep}${elapsed}${sep}ms"
        return renderHeader(channel, filename, statement.endLine(), sep) + data
    }
    override fun renderLoop(channel: String, filename: String, sep: String): String {
        val data = "DO LOOP END${sep}$loopCounter "
        return renderHeader(channel, filename, statement.endLine(), sep) + data
    }
}

class DOWxxStatementExecutionLogStart(programName: String, val statement: DOWxxStmt) : LogEntry(programName) {
    override fun toString(): String {
        return "executing DOWxx LOOP"
    }

    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "DOW${statement.comparisonOperator.symbol} LOOP START${sep}${statement.comparisonOperator}${sep}LEFT: ${statement.factor1.render()}/RIGHT ${statement.factor2.render()}"

        return renderHeader(channel, filename, statement.startLine(), sep) + data
    }

    override fun renderLoop(channel: String, filename: String, sep: String): String {
        val data = "DOW${statement.comparisonOperator.symbol} LOOP START${sep}LEFT: ${statement.factor1.render()}/RIGHT ${statement.factor2.render()}"

        return renderHeader(channel, filename, statement.startLine(), sep) + data
    }
}

class DOWxxStatementExecutionLogEnd(programName: String, val statement: DOWxxStmt, val elapsed: Long) : LogEntry(programName) {
    override fun toString(): String {
        return "ending DOWxx LOOP"
    }

    override fun renderStatement(channel: String, filename: String, sep: String): String {
        return renderHeader(channel, filename, statement.endLine(), sep) + "DOWxx LOOP END"
    }

    override fun renderPerformance(channel: String, filename: String, sep: String): String {
        val data = "DOW${statement.comparisonOperator.symbol} LOOP END${sep}${statement.comparisonOperator}${sep}LEFT: ${statement.factor1.render()}/RIGHT ${statement.factor2.render()}${sep}${elapsed}${sep}ms"

        return renderHeader(channel, filename, statement.endLine(), sep) + data
    }
    override fun renderLoop(channel: String, filename: String, sep: String): String {
        val data = "DOW${statement.comparisonOperator.symbol} LOOP END"

        return renderHeader(channel, filename, statement.endLine(), sep) + data
    }
}

class DouStatemenExecutionLogStart(programName: String, val statement: DouStmt) : LogEntry(programName) {
    override fun toString(): String {
        return "executing DOU LOOP"
    }

    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "DOU LOOP START${sep}${statement.endExpression.render()} ${statement.endExpression.render()} "

        return renderHeader(channel, filename, statement.startLine(), sep) + data
    }

    override fun renderLoop(channel: String, filename: String, sep: String): String {
        val data = "DOU LOOP START${sep}${statement.endExpression.render()} ${statement.endExpression.render()} "

        return renderHeader(channel, filename, statement.endLine(), sep) + data
    }
}
class DouStatemenExecutionLogEnd(programName: String, val statement: DouStmt, val elapsed: Long, val loopCounter: Long) : LogEntry(programName) {
    override fun toString(): String {
        return "ending DOU LOOP"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        return renderHeader(channel, filename, statement.endLine(), sep) + "DO LOOP END"
    }

    override fun renderPerformance(channel: String, filename: String, sep: String): String {
        val data = "DO LOOP END${sep}${elapsed}${sep}ms"
        return renderHeader(channel, filename, statement.endLine(), sep) + data
    }
    override fun renderLoop(channel: String, filename: String, sep: String): String {
        val data = "DO LOOP END${sep}$loopCounter "
        return renderHeader(channel, filename, statement.endLine(), sep) + data
    }
}

class DowStatemenExecutionLogStart(programName: String, val statement: DowStmt) : LogEntry(programName) {
    override fun toString(): String {
        return "executing DOW LOOP"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "DOW LOOP START${sep}${statement.endExpression.render()} "

        return renderHeader(channel, filename, statement.startLine(), sep) + data
    }
    override fun renderLoop(channel: String, filename: String, sep: String): String {
        val data = "DOW LOOP START${sep}${statement.endExpression.render()} "

        return renderHeader(channel, filename, statement.startLine(), sep) + data
    }
}
class DowStatemenExecutionLogEnd(programName: String, val statement: DowStmt, val elapsed: Long, val loopCounter: Long) : LogEntry(programName) {
    override fun toString(): String {
        return "ending DOW LOOP"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        return renderHeader(channel, filename, statement.endLine(), sep) + "DOW LOOP END"
    }

    override fun renderPerformance(channel: String, filename: String, sep: String): String {
        val data = "DOW LOOP END${sep}${sep}${elapsed}${sep}ms"

        return renderHeader(channel, filename, statement.endLine(), sep) + data
    }
    override fun renderLoop(channel: String, filename: String, sep: String): String {
        val data = "DOW LOOP END${sep}${sep}${loopCounter}${sep}iterations"

        return renderHeader(channel, filename, statement.endLine(), sep) + data
    }
}

class SelectCaseExecutionLogEntry(programName: String, val case: SelectCase, val result: Value) : LogEntry(programName) {
    override fun toString(): String {
        return "executing SELECT CASE"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "SELECT WHEN${sep}${case.condition.render()}$sep(${result.asBoolean().value})"

        return renderHeader(channel, filename, case.startLine(), sep) + data
    }
}

class SelectOtherExecutionLogEntry(programName: String, val other: SelectOtherClause, val duration: Long = -1) : LogEntry(programName) {
    override fun toString(): String {
        return "executing SELECT OTHER"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        return renderHeader(channel, filename, other.startLine(), sep) + "SELECT OTHER"
    }
}

class IfExecutionLogEntry(programName: String, val statement: IfStmt, val result: Value) : LogEntry(programName) {
    override fun toString(): String {
        return "executing IF"
    }

    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "IF ${statement.condition.render()}${sep}${result.asBoolean().value}"
        return renderHeader(channel, filename, statement.startLine(), sep) + data
    }
}

class ElseIfExecutionLogEntry(programName: String, val statement: ElseIfClause, val result: Value) : LogEntry(programName) {
    override fun toString(): String {
        return "executing ELSE IF"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "ELSE IF {statement.condition.render()}$sep(${result.asBoolean().value})"
        return renderHeader(channel, filename, statement.startLine(), sep) + data
    }
}

class ElseExecutionLogEntry(programName: String, val statement: ElseClause, val result: Value) : LogEntry(programName) {
    override fun toString(): String {
        return "executing ELSE "
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "ELSE"
        return renderHeader(channel, filename, statement.startLine(), sep) + data
    }
}

class EvaluationLogEntry(programName: String, val statement: EvalStmt, val value: Value?) : LogEntry(programName) {
    override fun toString(): String {
        return "evaluating $statement as $value"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "EVAL ${statement.target.render()} ${statement.operator.text} ${statement.expression.render()}${sep}${value!!.render()}"
        return renderHeader(channel, filename, statement.startLine(), sep) + data
    }
}
class ExpressionEvaluationLogEntry(programName: String, val expression: Expression, val value: Value) : LogEntry(programName) {
    override fun toString(): String {
        return "evaluating $expression as $value"
    }

    override fun renderExpression(channel: String, filename: String, sep: String): String {
        var header = "${sep}$filename${sep}${sep}$channel$sep"
        if (expression.position != null) {
            header = renderHeader(channel, filename, expression.startLine(), sep)
        } else if (expression.parent != null && expression.parent!!.position != null) {
            header = renderHeader(channel, filename, expression.parent!!.startLine(), sep)
        }

        val data = "${expression.render()}${sep}${value.render()}"
        return header + data
    }
}

class AssignmentLogEntry(programName: String, val data: AbstractDataDefinition, val value: Value, val previous: Value?) : LogEntry(programName) {
    override fun toString(): String {
        return "assigning to $data value $value"
    }
    override fun renderData(channel: String, filename: String, sep: String): String {
        val pvalue = if (previous == null) "N/D" else previous.render()
        val data = "${data.name} = ${pvalue}${sep}${value.render()}"

        return renderHeader(channel, filename, "", sep) + data
    }
}

class AssignmentOfElementLogEntry(programName: String, val array: Expression, val index: Int, val value: Value) : LogEntry(programName) {
    override fun toString(): String {
        return "assigning to $array[$index] value $value"
    }
}

class ProgramInterpretationLogStart(programName: String, val initialValues: Map<String, Value>) : LogEntry(programName) {
    override fun toString(): String {
        return "calling $programName with initial values $initialValues"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "INTERPRETATION START${sep}$filename"

        return renderHeader(channel, filename, "", sep) + data
    }
}

class ProgramInterpretationLogEnd(programName: String, val elapsed: Long = -1) : LogEntry(programName) {
    override fun toString(): String {
        return "ending $programName"
    }
    override fun renderPerformance(channel: String, filename: String, sep: String): String {
        val data = "INTERPRETATION END $filename${sep}${elapsed}${sep}ms"

        return renderHeader(channel, filename, "", sep) + data
    }

    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "INTERPRETATION END${sep}$filename"

        return renderHeader(channel, filename, "", sep) + data
    }
}

class MoveLStatemenExecutionLog(programName: String, val statement: MoveLStmt, val result: Value) : LogEntry(programName) {
    override fun toString(): String {
        return "MOVEL"
    }

    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "MOVEL${sep}${statement.expression.render()} TO ${statement.target.render()}${sep}${result.render()}"
        return renderHeader(channel, filename, statement.startLine(), sep) + data
    }
}

class MoveStatemenExecutionLog(programName: String, val statement: MoveStmt, val result: Value) : LogEntry(programName) {
    override fun toString(): String {
        return "MOVE"
    }

    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "MOVE${sep}${statement.expression.render()} TO ${statement.target.render()}${sep}${result.render()}"
        return renderHeader(channel, filename, statement.startLine(), sep) + data
    }
}

class MoveAStatemenExecutionLog(programName: String, val statement: MoveAStmt, val result: Value) : LogEntry(programName) {
    override fun toString(): String {
        return "MOVEA"
    }

    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "MOVEA${sep}${statement.expression.render()} TO ${statement.target.render()}${sep}${result.render()}"
        return renderHeader(channel, filename, statement.startLine(), sep) + data
    }
}

class ParamListStatemenExecutionLog(programName: String, val statement: PlistStmt, val name: String, val value: Value) : LogEntry(programName) {
    override fun toString(): String {
        return "PLIST"
    }

    override fun renderStatement(channel: String, filename: String, sep: String): String {

        val data = "PARAM${sep}${name}${sep}${value.render()}"
        return renderHeader(channel, filename, statement.startLine(), sep) + data
    }
}

class ClearStatemenExecutionLog(programName: String, val statement: ClearStmt, val result: Value) : LogEntry(programName) {
    override fun toString(): String {
        return "CLEAR"
    }

    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "CLEAR${sep}${statement.value.render()}${sep}${result.render()}"
        return renderHeader(channel, filename, statement.startLine(), sep) + data
    }
}

class LeaveStatemenExecutionLog(programName: String, val statement: LeaveStmt) : LogEntry(programName) {
    override fun toString(): String {
        return "LEAVE"
    }

    override fun renderStatement(channel: String, filename: String, sep: String): String {
        return renderHeader(channel, filename, statement.startLine(), sep) + "LEAVE"
    }
}

class LeaveSrStatemenExecutionLog(programName: String, val statement: LeaveSrStmt) : LogEntry(programName) {
    override fun toString(): String {
        return "LEAVESR"
    }

    override fun renderStatement(channel: String, filename: String, sep: String): String {
        return renderHeader(channel, filename, statement.startLine(), sep) + "LEAVESR"
    }
}

class IterStatemenExecutionLog(programName: String, val statement: IterStmt) : LogEntry(programName) {
    override fun toString(): String {
        return "ITER"
    }

    override fun renderStatement(channel: String, filename: String, sep: String): String {
        return renderHeader(channel, filename, statement.startLine(), sep) + "ITER"
    }
}

interface InterpreterLogHandler {
    fun handle(logEntry: LogEntry)
}

// TODO remove used in Test only
class LinesLogHandler(private val printStream: PrintStream = System.out) : InterpreterLogHandler {
    override fun handle(logEntry: LogEntry) {
        if (logEntry is LineLogEntry) {
            printStream.println("[LOG] $logEntry")
        }
    }
}

class AssignmentsLogHandler(private val printStream: PrintStream = System.out) : InterpreterLogHandler {
    override fun handle(logEntry: LogEntry) {
        if (logEntry is AssignmentLogEntry) {
            printStream.println("[LOG] ${logEntry.data.name} = ${logEntry.value}")
        }
    }
}

// TODO remove used in Test only
class EvalLogHandler(private val printStream: PrintStream = System.out) : InterpreterLogHandler {
    override fun handle(logEntry: LogEntry) {
        if (logEntry is ExpressionEvaluationLogEntry) {
            printStream.println("[LOG] Evaluating ${logEntry.expression.type()} = ${logEntry.value} -- Line: ${logEntry.expression.position.line()}")
        }
    }
}

object SimpleLogHandler : InterpreterLogHandler {
    override fun handle(logEntry: LogEntry) {
        println("[LOG] $logEntry")
    }

    fun fromFlag(flag: Boolean) = if (flag) {
        listOf(this)
    } else {
        emptyList()
    }
}

class ListLogHandler : InterpreterLogHandler {
    private val _logs = LinkedList<LogEntry>()

    override fun handle(logEntry: LogEntry) {
        _logs.add(logEntry)
    }

    // Immutable view of the internal mutable list
    val logs: List<LogEntry>
        get() = _logs

    fun getExecutedSubroutines() = _logs.asSequence().filterIsInstance(SubroutineExecutionLogStart::class.java).map { it.subroutine }.toList()
    fun getExecutedSubroutineNames() = getExecutedSubroutines().map { it.name }
    fun getEvaluatedExpressions() = _logs.filterIsInstance(ExpressionEvaluationLogEntry::class.java)
    fun getAssignments() = _logs.filterIsInstance(AssignmentLogEntry::class.java)
    /**
     * Remove an expression if the last time the same expression was evaluated it had the same searchedValued
     */
    fun getEvaluatedExpressionsConcise(): List<ExpressionEvaluationLogEntry> {
        val base = _logs.asSequence().filterIsInstance(ExpressionEvaluationLogEntry::class.java).toMutableList()
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
}

fun List<InterpreterLogHandler>.log(logEntry: LogEntry) {
    this.forEach {
        try {
            it.handle(logEntry)
        } catch (t: Throwable) {
            // TODO: how should we handle exceptions?
            t.printStackTrace()
        }
    }
}

fun Position?.line() = this?.relative()?.second?.renderStartLine().asNonNullString()
fun Position?.atLine() = this?.relative()?.second?.renderStartLine()?.let { "line $it " } ?: ""
fun Node?.startLine() = this?.position?.relative()?.second?.renderStartLine().asNonNullString()
fun Node?.endLine() = this?.position?.relative()?.second?.renderEndLine().asNonNullString()
fun SourceReference.renderStartLine() = "${this.relativeLine}"
fun SourceReference.renderEndLine() = "${this.position?.end?.line}"
class CatStatementExecutionLog(programName: String, val statement: CatStmt, val result: Value) : LogEntry(programName) {
    override fun toString(): String {
        return "CAT"
    }

    override fun renderStatement(channel: String, filename: String, sep: String): String {
        var data = "CAT${sep}FACTOR2${sep}${statement.right.render()}${sep}RESULT${sep}${result.render()}"
        if (null != statement.left) {
            data = "CAT${sep}FACTOR1${sep}${statement.left.render()}${sep}FACTOR2${sep}${statement.right.render()}${sep}RESULT${sep}${result.render()}"
        }
        return renderHeader(channel, filename, statement.startLine(), sep) + data
    }
}

class CompStatementExecutionLog(programName: String, val statement: CompStmt, val result: Value) : LogEntry(programName) {
    override fun toString(): String {
        return "COMP"
    }

    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "COMP${sep}FACTOR1${sep}${statement.left.render()}${sep}FACTOR2${sep}${statement.right.render()}${sep}HI${sep}${statement.hi}${sep}LO${sep}${statement.lo}${sep}EQ${sep}${statement.eq}"
        return renderHeader(channel, filename, statement.startLine(), sep) + data
    }
}

class LookupStatementExecutionLog(programName: String, val statement: LookupStmt, val hi: Int?, val lo: Int?, val eq: Int?) : LogEntry(programName) {
    override fun toString(): String {
        return "LOOKUP"
    }

    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "LOOKUP${sep}FACTOR1${sep}${statement.left.render()}${sep}FACTOR2${sep}${statement.right.render()}${sep}HI${sep}$hi${sep}LO${sep}$lo${sep}EQ${sep}$eq"
        return renderHeader(channel, filename, statement.startLine(), sep) + data
    }
}

class AstLogStart(programName: String) : LogEntry(programName) {
    override fun toString(): String {
        return "ast start $programName"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "AST START$sep"

        return renderHeader(channel, filename, "", sep) + data
    }
}

class AstLogEnd(programName: String, val elapsed: Long) : LogEntry(programName) {

    override fun toString(): String {
        return "ast $programName"
    }

    override fun renderPerformance(channel: String, filename: String, sep: String): String {
        val data = "AST END $filename${sep}${elapsed}${sep}ms"

        return renderHeader(channel, filename, "", sep) + data
    }

    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "AST END${sep}$filename"

        return renderHeader(channel, filename, "", sep) + data
    }
}

class RpgLoadLogStart(programName: String) : LogEntry(programName) {
    override fun toString(): String {
        return "rpgload start $programName"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "RPGLOAD START$sep"

        return renderHeader(channel, filename, "", sep) + data
    }
}

class RpgLoadLogEnd(programName: String, val elapsed: Long, private val programSouce: CharStream?) : LogEntry(programName) {

    private val lines: Int by lazy {
        programSouce?.toString()?.split(Pattern.compile("\\r\\n|\\r|\\n"))?.size ?: 0
    }

    override fun toString(): String {
        return "rpgload $programName"
    }

    override fun renderPerformance(channel: String, filename: String, sep: String): String {
        val data = "RPGLOAD END $filename${sep}${elapsed}${sep}ms"

        return renderHeader(channel, filename, "$lines", sep) + data
    }

    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "RPGLOAD END${sep}$filename"

        return renderHeader(channel, filename, "$lines", sep) + data
    }
}

class PreprocessingLogStart(programName: String) : LogEntry(programName) {
    override fun toString(): String {
        return "preprop start $programName"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "PREPROP START$sep"

        return renderHeader(channel, filename, "", sep) + data
    }
}

class PreprocessingLogEnd(programName: String, val elapsed: Long, private val programSouce: String) : LogEntry(programName) {

    private val lines: Int by lazy {
        programSouce.lines().size
    }

    override fun toString(): String {
        return "preprop $programName"
    }

    override fun renderPerformance(channel: String, filename: String, sep: String): String {
        val data = "PREPROP END $filename${sep}${elapsed}${sep}ms"

        return renderHeader(channel, filename, "$lines", sep) + data
    }

    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "PREPROP END${sep}$filename"

        return renderHeader(channel, filename, "$lines", sep) + data
    }
}

class LexerLogStart(programName: String) : LogEntry(programName) {
    override fun toString(): String {
        return "lexer start $programName"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "LEXER START$sep"

        return renderHeader(channel, filename, "", sep) + data
    }
}

class LexerLogEnd(programName: String, val elapsed: Long) : LogEntry(programName) {

    override fun toString(): String {
        return "lexer $programName"
    }

    override fun renderPerformance(channel: String, filename: String, sep: String): String {
        val data = "LEXER END $filename${sep}${elapsed}${sep}ms"

        return renderHeader(channel, filename, "", sep) + data
    }

    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "LEXER END${sep}$filename"

        return renderHeader(channel, filename, "", sep) + data
    }
}

class ParserLogStart(programName: String) : LogEntry(programName) {
    override fun toString(): String {
        return "parser start $programName"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "PARSER START$sep"

        return renderHeader(channel, filename, "", sep) + data
    }
}

class ParserLogEnd(programName: String, val elapsed: Long) : LogEntry(programName) {

    override fun toString(): String {
        return "parser $programName"
    }

    override fun renderPerformance(channel: String, filename: String, sep: String): String {
        val data = "PARSER END $filename${sep}${elapsed}${sep}ms"

        return renderHeader(channel, filename, "", sep) + data
    }

    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "PARSER END${sep}$filename"

        return renderHeader(channel, filename, "", sep) + data
    }
}

class RContextLogStart(programName: String) : LogEntry(programName) {
    override fun toString(): String {
        return "rcontext start $programName"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "RCONTEXT START$sep"

        return renderHeader(channel, filename, "", sep) + data
    }
}

class RContextLogEnd(programName: String, val elapsed: Long) : LogEntry(programName) {

    override fun toString(): String {
        return "rcontext $programName"
    }

    override fun renderPerformance(channel: String, filename: String, sep: String): String {
        val data = "RCONTEXT END $filename${sep}${elapsed}${sep}ms"

        return renderHeader(channel, filename, "", sep) + data
    }

    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "RCONTEXT END${sep}$filename"

        return renderHeader(channel, filename, "", sep) + data
    }
}

class CheckParseTreeLogStart(programName: String) : LogEntry(programName) {
    override fun toString(): String {
        return "chkptree start $programName"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "CHKPTREE START$sep"

        return renderHeader(channel, filename, "", sep) + data
    }
}

class CheckParseTreeLogEnd(programName: String, val elapsed: Long) : LogEntry(programName) {

    override fun toString(): String {
        return "chkptree $programName"
    }

    override fun renderPerformance(channel: String, filename: String, sep: String): String {
        val data = "CHKPTREE END $filename${sep}${elapsed}${sep}ms"

        return renderHeader(channel, filename, "", sep) + data
    }

    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "CHKPTREE END${sep}$filename"

        return renderHeader(channel, filename, "", sep) + data
    }
}

class FindMutesLogStart(programName: String) : LogEntry(programName) {
    override fun toString(): String {
        return "findmutes start $programName"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "FINDMUTES START$sep"

        return renderHeader(channel, filename, "", sep) + data
    }
}

class FindMutesLogEnd(programName: String, val elapsed: Long) : LogEntry(programName) {

    override fun toString(): String {
        return "findmutes $programName"
    }

    override fun renderPerformance(channel: String, filename: String, sep: String): String {
        val data = "FINDMUTES END $filename${sep}${elapsed}${sep}ms"

        return renderHeader(channel, filename, "", sep) + data
    }

    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "FINDMUTES END${sep}$filename"

        return renderHeader(channel, filename, "", sep) + data
    }
}

class SymbolTableIniLogStart(programName: String) : LogEntry(programName) {
    override fun toString(): String {
        return "symtblini start $programName"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "SYMTBLINI START$sep"

        return renderHeader(channel, filename, "", sep) + data
    }
}

class SymbolTableIniLogEnd(programName: String, val elapsed: Long) : LogEntry(programName) {

    override fun toString(): String {
        return "symtblini $programName"
    }

    override fun renderPerformance(channel: String, filename: String, sep: String): String {
        val data = "SYMTBLINI END $filename${sep}${elapsed}${sep}ms"

        return renderHeader(channel, filename, "", sep) + data
    }

    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "SYMTBLINI END${sep}$filename"

        return renderHeader(channel, filename, "", sep) + data
    }
}

class SymbolTableLoadLogStart(programName: String) : LogEntry(programName) {
    override fun toString(): String {
        return "symtblload start $programName"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "SYMTBLLOAD START$sep"

        return renderHeader(channel, filename, "", sep) + data
    }
}

class SymbolTableLoadLogEnd(programName: String, val elapsed: Long) : LogEntry(programName) {

    override fun toString(): String {
        return "symtblload $programName"
    }

    override fun renderPerformance(channel: String, filename: String, sep: String): String {
        val data = "SYMTBLLOAD END $filename${sep}${elapsed}${sep}ms"

        return renderHeader(channel, filename, "", sep) + data
    }

    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "SYMTBLLOAD END${sep}$filename"

        return renderHeader(channel, filename, "", sep) + data
    }
}

class SymbolTableStoreLogStart(programName: String) : LogEntry(programName) {
    override fun toString(): String {
        return "symtblStore start $programName"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "SYMTBLSTORE START$sep"

        return renderHeader(channel, filename, "", sep) + data
    }
}

class SymbolTableStoreLogEnd(programName: String, val elapsed: Long) : LogEntry(programName) {

    override fun toString(): String {
        return "symtblstore $programName"
    }

    override fun renderPerformance(channel: String, filename: String, sep: String): String {
        val data = "SYMTBLSTORE END $filename${sep}${elapsed}${sep}ms"

        return renderHeader(channel, filename, "", sep) + data
    }

    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "SYMTBLSTORE END${sep}$filename"

        return renderHeader(channel, filename, "", sep) + data
    }
}

class SetLogStart(programName: String, val statement: Statement, private val logPref: String, val kList: List<String>) : LogEntry(programName) {
    override fun toString(): String {
        return "executing $logPref"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "$logPref START$sep$kList}"

        return renderHeader(channel, filename, statement.startLine(), sep) + data
    }
}

class SetLogEnd(programName: String, val statement: Statement, private val logPref: String, val elapsed: Long) : LogEntry(programName) {
    override fun toString(): String {
        return "ending $logPref"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        return renderHeader(channel, filename, statement.endLine(), sep) + "$logPref END"
    }
    override fun renderPerformance(channel: String, filename: String, sep: String): String {
        val data = "$logPref END${sep}${elapsed}${sep}ms"
        return renderHeader(channel, filename, statement.endLine(), sep) + data
    }
}

class ReadLogStart(programName: String, val statement: Statement, private val logPref: String) : LogEntry(programName) {
    override fun toString(): String {
        return "executing $logPref"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "$logPref $sep"

        return renderHeader(channel, filename, statement.startLine(), sep) + data
    }
}

class ReadLogEnd(programName: String, val statement: Statement, private val logPref: String, private val result: com.smeup.dbnative.file.Result, val elapsed: Long) : LogEntry(programName) {
    override fun toString(): String {
        return "ending $logPref"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        return renderHeader(channel, filename, statement.endLine(), sep) + "$logPref END$sep$result"
    }
    override fun renderPerformance(channel: String, filename: String, sep: String): String {
        val data = "$logPref END${sep}${elapsed}${sep}ms"
        return renderHeader(channel, filename, statement.endLine(), sep) + data
    }
}

class ReadEqualLogStart(programName: String, val statement: Statement, private val logPref: String, val kList: List<String>) : LogEntry(programName) {
    override fun toString(): String {
        return "executing $logPref"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "$logPref START$sep$kList"

        return renderHeader(channel, filename, statement.startLine(), sep) + data
    }
}

class ReadEqualLogEnd(programName: String, val statement: Statement, private val logPref: String, val result: com.smeup.dbnative.file.Result, val elapsed: Long) : LogEntry(programName) {
    override fun toString(): String {
        return "ending $logPref"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        return renderHeader(channel, filename, statement.endLine(), sep) + "$logPref END$sep$result"
    }
    override fun renderPerformance(channel: String, filename: String, sep: String): String {
        val data = "$logPref END${sep}${elapsed}${sep}ms"
        return renderHeader(channel, filename, statement.endLine(), sep) + data
    }
}

class StoreLogStart(programName: String, val statement: Statement, private val logPref: String, val record: Record? = null) : LogEntry(programName) {
    override fun toString(): String {
        return "executing $logPref"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        val data = "$logPref $sep$record"

        return renderHeader(channel, filename, statement.startLine(), sep) + data
    }
}

class StoreLogEnd(programName: String, val statement: Statement, private val logPref: String, val result: com.smeup.dbnative.file.Result, val elapsed: Long) : LogEntry(programName) {
    override fun toString(): String {
        return "ending $logPref"
    }
    override fun renderStatement(channel: String, filename: String, sep: String): String {
        return renderHeader(channel, filename, statement.endLine(), sep) + "$logPref END$sep$result"
    }
    override fun renderPerformance(channel: String, filename: String, sep: String): String {
        val data = "$logPref END${sep}${elapsed}${sep}ms"
        return renderHeader(channel, filename, statement.endLine(), sep) + data
    }
}

class ErrorEventLogEntry(private val errorEvent: ErrorEvent) : LogEntry(errorEvent.sourceReference?.sourceId ?: "") {
    override fun toString(): String {
        return "error"
    }

    override fun renderErrorEvent(channel: String, filename: String, sep: String): String {
        val line = errorEvent.absoluteLine?.toString() ?: ""
        return renderHeader(channel, filename, line, sep) + errorEvent
    }
}