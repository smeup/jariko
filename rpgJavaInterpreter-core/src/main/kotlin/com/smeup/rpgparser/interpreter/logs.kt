package com.smeup.rpgparser.interpreter

import java.io.PrintStream
import java.util.*

interface InterpreterLogHandler {
    fun handle(logEntry: LogEntry): Unit
}

class AssignmentsLogHandler(private val printStream: PrintStream = System.out) : InterpreterLogHandler {
    override fun handle(logEntry: LogEntry) {
        if (logEntry is AssignmentLogEntry) {
            printStream.println("[LOG] ${logEntry.data.name} = ${logEntry.value} -- ${logEntry.data.position}")
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

class ListLogHandler: InterpreterLogHandler {
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