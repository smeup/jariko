package com.smeup.rpgparser.logging

import com.smeup.rpgparser.interpreter.LazyLogEntry
import com.smeup.rpgparser.interpreter.LogEntry
import com.smeup.rpgparser.interpreter.LogSourceData
import com.smeup.rpgparser.interpreter.LogSourceProvider
import java.util.*
import kotlin.collections.HashMap
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.DurationUnit

class AnalyticsLoggingContext {
    private val timeUsageByStatement: HashMap<String, UsageMeasurement> = hashMapOf()
    private val symbolTableTimeUsage: EnumMap<SymbolTableAction, UsageMeasurement> = EnumMap(SymbolTableAction::class.java)
    private var renderingTimeMeasurement = UsageMeasurement.new()
    private var expressionTimeMeasurement = UsageMeasurement.new()

    private val initTimestamp = System.nanoTime()

    private val totalTime
        get() = (System.nanoTime() - initTimestamp).nanoseconds

    enum class SymbolTableAction {
        INIT,
        LOAD,
        STORE
    }

    data class UsageMeasurement(val duration: Duration, val hit: Long) {
        companion object {
            fun new(): UsageMeasurement = UsageMeasurement(
                duration = Duration.ZERO,
                hit = 0
            )
        }
    }

    fun recordRenderingDuration(executionTime: Duration) {
        renderingTimeMeasurement = UsageMeasurement(
            duration = renderingTimeMeasurement.duration + executionTime,
            hit = renderingTimeMeasurement.hit + 1
        )
    }

    fun recordExpressionDuration(executionTime: Duration) {
        expressionTimeMeasurement = UsageMeasurement(
            duration = renderingTimeMeasurement.duration + executionTime,
            hit = renderingTimeMeasurement.hit + 1
        )
    }

    fun recordSymbolTableDuration(action: SymbolTableAction, executionTime: Duration) {
        val entry = symbolTableTimeUsage.getOrPut(action) { UsageMeasurement.new() }
        symbolTableTimeUsage[action] = UsageMeasurement(
            duration = entry.duration + executionTime,
            hit = entry.hit + 1
        )
    }

    fun recordStatementDuration(name: String, executionTime: Duration) {
        val entry = timeUsageByStatement.getOrPut(name) { UsageMeasurement.new() }
        timeUsageByStatement[name] = UsageMeasurement(
            duration = entry.duration + executionTime,
            hit = entry.hit + 1
        )
    }

    fun generateCompleteReport(source: LogSourceProvider): List<LazyLogEntry> {
        val timeUsageEntries = generateTimeUsageByStatementReportEntries(source)
        val symTableEntries = generateSymbolTableTimeUsageReportEntries(source)
        val expressionEntry = generateExpressionReportEntry(source)
        val logTimeEntry = generateLogTimeReportEntry(source)
        val programExecutionEntry = generateProgramReportEntry(source)

        return timeUsageEntries + symTableEntries + expressionEntry + logTimeEntry + programExecutionEntry
    }

    private fun generateTimeUsageByStatementReportEntries(source: LogSourceProvider): List<LazyLogEntry> {
        return timeUsageByStatement.toList().map {
            val statementName = it.first
            val duration = it.second.duration
            val hit = it.second.hit

            val entry = LogEntry(source, LogChannel.ANALYTICS.getPropertyName(), "STMT TIME")
            LazyLogEntry(entry) { sep ->
                "$statementName$sep${duration.toString(DurationUnit.MICROSECONDS)}${sep}$hit"
            }
        }
    }

    private fun generateSymbolTableTimeUsageReportEntries(source: LogSourceProvider): List<LazyLogEntry> {
        return symbolTableTimeUsage.toList().map {
            val action = it.first
            val duration = it.second.duration
            val hit = it.second.hit

            val entry = LogEntry(source, LogChannel.ANALYTICS.getPropertyName(), "SYMTBL TIME")
            LazyLogEntry(entry) { sep ->
                "${action.name}$sep${duration.toString(DurationUnit.MICROSECONDS)}${sep}$hit"
            }
        }
    }

    private fun generateExpressionReportEntry(source: LogSourceProvider): LazyLogEntry {
        val duration = expressionTimeMeasurement.duration
        val hit = expressionTimeMeasurement.hit

        val entry = LogEntry(source, LogChannel.ANALYTICS.getPropertyName(), "EXPR TIME")
        return LazyLogEntry(entry) { sep ->
            "${duration.toString(DurationUnit.MICROSECONDS)}$sep$hit"
        }
    }

    private fun generateLogTimeReportEntry(source: LogSourceProvider): LazyLogEntry {
        val duration = renderingTimeMeasurement.duration
        val hit = renderingTimeMeasurement.hit

        val entry = LogEntry(source, LogChannel.ANALYTICS.getPropertyName(), "LOG TIME")
        return LazyLogEntry(entry) { sep ->
            "${duration.toString(DurationUnit.MICROSECONDS)}$sep$hit"
        }
    }

    private fun generateProgramReportEntry(source: LogSourceProvider): LazyLogEntry {
        val entry = LogEntry(source, LogChannel.ANALYTICS.getPropertyName(), "PROGRAM TIME")
        return LazyLogEntry(entry) { totalTime.toString(DurationUnit.MICROSECONDS) }
    }
}