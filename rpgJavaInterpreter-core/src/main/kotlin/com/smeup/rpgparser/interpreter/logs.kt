package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.ast.CallStmt
import com.smeup.rpgparser.ast.Expression
import com.smeup.rpgparser.ast.Subroutine
import com.strumenta.kolasu.model.Position
import java.io.PrintStream
import java.util.*

abstract class LogEntry

data class CallExecutionLogEntry(val callStmt: CallStmt) : LogEntry() {
    override fun toString(): String {
        return "calling $callStmt"
    }
}

data class CallEndLogEntry(val callStmt: CallStmt, val exception: Exception? = null) : LogEntry() {
    override fun toString(): String {
        if (exception == null) {
            return "end of $callStmt"
        } else {
            return "exception $exception in calling $callStmt"
        }
    }
}

data class SubroutineExecutionLogEntry(val subroutine: Subroutine) : LogEntry() {
    override fun toString(): String {
        return "executing ${subroutine.name}"
    }
}

data class ExpressionEvaluationLogEntry(val expression: Expression, val value: Value) : LogEntry() {
    override fun toString(): String {
        return "evaluating $expression as $value"
    }
}

data class AssignmentLogEntry(val data: AbstractDataDefinition, val value: Value) : LogEntry() {
    override fun toString(): String {
        return "assigning to $data value $value"
    }
}

data class AssignmentOfElementLogEntry(val array: Expression, val index: Int, val value: Value) : LogEntry() {
    override fun toString(): String {
        return "assigning to $array[$index] value $value"
    }
}

data class StartProgramLog(val programName: String, val initialValues: Map<String, Value>) : LogEntry() {
    override fun toString(): String {
        return "calling $programName with initial values $initialValues"
    }
}

interface InterpreterLogHandler {
    fun handle(logEntry: LogEntry): Unit
}

class AssignmentsLogHandler(private val printStream: PrintStream = System.out) : InterpreterLogHandler {
    override fun handle(logEntry: LogEntry) {
        if (logEntry is AssignmentLogEntry) {
            printStream.println("[LOG] ${logEntry.data.name} = ${logEntry.value}")
        }
    }
}

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
    private val logs = LinkedList<LogEntry>()

    override fun handle(logEntry: LogEntry) {
        logs.add(logEntry)
    }

    fun getLogs() = logs
    fun getExecutedSubroutines() = logs.asSequence().filterIsInstance(SubroutineExecutionLogEntry::class.java).map { it.subroutine }.toList()
    fun getExecutedSubroutineNames() = getExecutedSubroutines().map { it.name }
    fun getEvaluatedExpressions() = logs.filterIsInstance(ExpressionEvaluationLogEntry::class.java)
    fun getAssignments() = logs.filterIsInstance(AssignmentLogEntry::class.java)
    /**
     * Remove an expression if the last time the same expression was evaluated it had the same searchedValued
     */
    fun getEvaluatedExpressionsConcise(): List<ExpressionEvaluationLogEntry> {
        val base = logs.asSequence().filterIsInstance(ExpressionEvaluationLogEntry::class.java).toMutableList()
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
        }
    }
}

fun Position?.line() = this?.start?.line?.toString() ?: ""
