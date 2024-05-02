package com.smeup.rpgparser.logging

import com.smeup.rpgparser.interpreter.LazyLogEntry
import com.smeup.rpgparser.interpreter.LogEntry
import com.smeup.rpgparser.interpreter.LogSourceData
import com.smeup.rpgparser.interpreter.SymbolTableAction
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.DurationUnit

/**
 * Object used to store metadata for ANALYTICS log channel.
 * @see LogChannel.ANALYTICS
 */
class AnalyticsLoggingContext {
    private val programUsageTable = ProgramUsageTable()
    private var renderingTimeMeasurement = UsageMeasurement.new()

    private val initTimestamp = System.nanoTime()
    private val totalTime
        get() = (System.nanoTime() - initTimestamp).nanoseconds

    private val durationUnit = DurationUnit.MICROSECONDS

    /**
     * Records log rendering duration.
     */
    fun recordRenderingDuration(time: Duration) {
        renderingTimeMeasurement = renderingTimeMeasurement.hit(time)
    }

    /**
     * Records the execution of an expression.
     * @see ILoggableExpression
     */
    fun recordExpressionExecution(program: String, entity: String, time: Duration) =
        programUsageTable.recordExpression(program, entity, time)

    /**
     * Records an interaction with the symbol table.
     * @see SymbolTableAction
     */
    fun recordSymbolTableAccess(program: String, action: SymbolTableAction, time: Duration) =
        programUsageTable.recordSymbolTableAction(program, action, time)

    /**
     * Records the execution of a statement.
     * @see ILoggableStatement
     */
    fun recordStatementExecution(program: String, entity: String, time: Duration) =
        programUsageTable.recordStatement(program, entity, time)

    /**
     * Generate an ANALYTICS report based on currently collected metadata in the form
     * of a list of LazyLogEntry.
     * @see LazyLogEntry
     */
    fun generateCompleteReport(): List<LazyLogEntry> {
        val statementEntries = mutableListOf<LazyLogEntry>()
        val expressionEntries = mutableListOf<LazyLogEntry>()
        val symbolTableEntries = mutableListOf<LazyLogEntry>()

        programUsageTable.asSequence().forEach {
            val program = it.key
            val statement = programUsageTable.generateStatementLogEntries(program, durationUnit)
            val expression = programUsageTable.generateExpressionLogEntries(program, durationUnit)
            val symTable = programUsageTable.generateSymbolTableLogEntries(program, durationUnit)

            statementEntries.addAll(statement)
            expressionEntries.addAll(expression)
            symbolTableEntries.addAll(symTable)
        }

        val logTimeEntry = generateLogTimeReportEntry()
        val programExecutionEntry = generateProgramReportEntry()

        return statementEntries + expressionEntries + symbolTableEntries + logTimeEntry + programExecutionEntry
    }

    private fun generateLogTimeReportEntry(): LazyLogEntry {
        val duration = renderingTimeMeasurement.duration
        val hit = renderingTimeMeasurement.hit

        val entry = LogEntry({ LogSourceData.UNKNOWN }, LogChannel.ANALYTICS.getPropertyName(), "LOG TIME")
        return LazyLogEntry(entry) { sep ->
            "${duration.toString(durationUnit)}$sep$hit"
        }
    }

    private fun generateProgramReportEntry(): LazyLogEntry {
        val entry = LogEntry({ LogSourceData.UNKNOWN }, LogChannel.ANALYTICS.getPropertyName(), "PROGRAM TIME")
        return LazyLogEntry(entry) { totalTime.toString(durationUnit) }
    }
}