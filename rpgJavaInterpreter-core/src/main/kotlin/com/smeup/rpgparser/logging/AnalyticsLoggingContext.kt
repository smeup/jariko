package com.smeup.rpgparser.logging

import com.smeup.rpgparser.interpreter.LazyLogEntry
import com.smeup.rpgparser.interpreter.LogEntry
import com.smeup.rpgparser.interpreter.LogSourceData
import com.smeup.rpgparser.interpreter.SymbolTableAction
import java.util.Stack
import kotlin.time.Duration

/**
 * Object used to store metadata for ANALYTICS log channel.
 * @see LogChannel.ANALYTICS
 */
class AnalyticsLoggingContext {
    private val programUsageTable = ProgramUsageTable()
    private var renderingTimeMeasurement = UsageMeasurement.new()
    private var interpretationTimeMeasurement = UsageMeasurement.new()

    private val statementScope = Stack<String>()
    private val expressionScope = Stack<String>()

    /**
     * Checks whether we are in a CompositeStatement scope or not.
     */
    val isExecutingCompositeStatement get() = statementScope.isNotEmpty()

    /**
     * Checks whether we are executing an expression or not.
     */
    val isExecutingExpression get() = expressionScope.isNotEmpty()

    /**
     * Records the beginning of the execution of a CompositeStatement.
     */
    fun enterCompositeStatement(entity: String) { statementScope.push(entity) }

    /**
     * Records the end of the execution of a CompositeStatement.
     */
    fun exitCompositeStatement() { statementScope.pop() }

    /**
     * Records the beginning of the execution of an expression.
     */
    fun enterExpression(entity: String) { expressionScope.push(entity) }

    /**
     * Records the end of the execution of an expression.
     */
    fun exitExpression() { expressionScope.pop() }

    /**
     * Records log rendering duration.
     */
    fun recordRenderingDuration(time: Duration) {
        renderingTimeMeasurement = renderingTimeMeasurement.hit(time)
    }

    /**
     * Records interpretation duration.
     */
    fun recordInterpretationDuration(time: Duration) {
        interpretationTimeMeasurement = interpretationTimeMeasurement.hit(time)
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
     * Records the execution of a nested statement.
     * @see ILoggableStatement
     */
    fun recordNestedStatementExecution(program: String, entity: String, time: Duration) =
        programUsageTable.recordNestedStatement(program, statementScope, entity, time)

    /**
     * Records the execution of a nested expression from the current expression scope state.
     * @see ILoggableExpression
     */
    fun recordNestedExpressionExecutionFromScope(program: String, time: Duration) =
        programUsageTable.recordNestedExpression(program, expressionScope, time)

    /**
     * Generate an ANALYTICS report based on currently collected metadata in the form
     * of a list of LazyLogEntry.
     * @see LazyLogEntry
     */
    fun generateCompleteReport(): List<LazyLogEntry> {
        val statementEntries = mutableListOf<LazyLogEntry>()
        val expressionEntries = mutableListOf<LazyLogEntry>()
        val symbolTableEntries = mutableListOf<LazyLogEntry>()
        val nestedStatementEntries = mutableListOf<LazyLogEntry>()
        val nestedExpressionEntries = mutableListOf<LazyLogEntry>()

        programUsageTable.asSequence().forEach {
            val program = it.key
            val statement = programUsageTable.generateStatementLogEntries(program)
            val expression = programUsageTable.generateExpressionLogEntries(program)
            val symTable = programUsageTable.generateSymbolTableLogEntries(program)
            val nestedStatement = programUsageTable.generateNestedStatementLogEntries(program)
            val nestedExpression = programUsageTable.generateNestedExpressionLogEntries(program)

            statementEntries.addAll(statement)
            expressionEntries.addAll(expression)
            symbolTableEntries.addAll(symTable)
            nestedStatementEntries.addAll(nestedStatement)
            nestedExpressionEntries.addAll(nestedExpression)
        }

        val logTimeEntry = generateLogTimeReportEntry()
        val interpretationTimeEntry = generateInterpretationReportEntry()

        return statementEntries + expressionEntries + symbolTableEntries + nestedStatementEntries + nestedExpressionEntries + logTimeEntry + interpretationTimeEntry
    }

    private fun generateLogTimeReportEntry(): LazyLogEntry {
        val duration = renderingTimeMeasurement.duration
        val hit = renderingTimeMeasurement.hit

        val entry = LogEntry({ LogSourceData.UNKNOWN }, LogChannel.ANALYTICS.getPropertyName(), "LOG TIME")
        return LazyLogEntry(entry) { sep ->
            "$sep${duration.inWholeMicroseconds}$sep$hit"
        }
    }

    private fun generateInterpretationReportEntry(): LazyLogEntry {
        val duration = interpretationTimeMeasurement.duration
        val hit = interpretationTimeMeasurement.hit

        val entry = LogEntry({ LogSourceData.UNKNOWN }, LogChannel.ANALYTICS.getPropertyName(), "INTERPRETATION TIME")
        return LazyLogEntry(entry) { sep ->
            "$sep${duration.inWholeMicroseconds}$sep$hit"
        }
    }
}