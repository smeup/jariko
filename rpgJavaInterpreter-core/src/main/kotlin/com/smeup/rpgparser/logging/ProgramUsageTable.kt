package com.smeup.rpgparser.logging

import com.smeup.rpgparser.interpreter.LazyLogEntry
import com.smeup.rpgparser.interpreter.LogEntry
import com.smeup.rpgparser.interpreter.LogSourceData
import com.smeup.rpgparser.interpreter.SymbolTableAction
import java.util.*
import kotlin.collections.HashMap
import kotlin.time.Duration
import kotlin.time.DurationUnit

/**
 * Collects the usage information about the main parts of the program.
 */
class ProgramUsageStats {
    val statements: HashMap<String, UsageMeasurement> by lazy { hashMapOf() }
    val expressions: HashMap<String, UsageMeasurement> by lazy { hashMapOf() }
    val symbolTableActions: EnumMap<SymbolTableAction, UsageMeasurement> by lazy {
        EnumMap(SymbolTableAction::class.java)
    }
}

typealias ProgramUsageTable = HashMap<String, ProgramUsageStats>

/**
 * Records the execution of a statement.
 * @see ILoggableStatement
 */
fun ProgramUsageTable.recordStatement(program: String, entity: String, time: Duration) {
    val programStats = this.getOrPut(program) { ProgramUsageStats() }
    val measurement = programStats.statements.getOrDefault(entity, UsageMeasurement.new())
    programStats.statements[entity] = measurement.hit(time)
}

/**
 * Records the execution of an expression.
 * @see ILoggableExpression
 */
fun ProgramUsageTable.recordExpression(program: String, entity: String, time: Duration) {
    val programStats = this.getOrPut(program) { ProgramUsageStats() }
    val measurement = programStats.expressions.getOrDefault(entity, UsageMeasurement.new())
    programStats.expressions[entity] = measurement.hit(time)
}

/**
 * Records an interaction with the symbol table.
 * @see SymbolTableAction
 */
fun ProgramUsageTable.recordSymbolTableAction(program: String, action: SymbolTableAction, time: Duration) {
    val programStats = this.getOrPut(program) { ProgramUsageStats() }
    val measurement = programStats.symbolTableActions.getOrDefault(action, UsageMeasurement.new())
    programStats.symbolTableActions[action] = measurement.hit(time)
}

/**
 * Generate an ANALYTICS statement usage report in the form of a sequence of LazyLogEntry.
 * @see LazyLogEntry
 */
fun ProgramUsageTable.generateStatementLogEntries(program: String, unit: DurationUnit = DurationUnit.NANOSECONDS): Sequence<LazyLogEntry> {
    val stats = this[program] ?: return emptySequence()

    return stats.statements.asSequence().map {
        val statementName = it.key
        val duration = it.value.duration
        val hit = it.value.hit

        val entry = LogEntry({ LogSourceData.UNKNOWN }, LogChannel.ANALYTICS.getPropertyName(), "STMT TIME")
        LazyLogEntry(entry) { sep ->
            "$program$sep$statementName$sep${duration.toString(unit)}${sep}$hit"
        }
    }
}

/**
 * Generate an ANALYTICS expression usage report in the form of a sequence of LazyLogEntry.
 * @see LazyLogEntry
 */
fun ProgramUsageTable.generateExpressionLogEntries(program: String, unit: DurationUnit = DurationUnit.NANOSECONDS): Sequence<LazyLogEntry> {
    val stats = this[program] ?: return emptySequence()

    return stats.expressions.asSequence().map {
        val exprName = it.key
        val duration = it.value.duration
        val hit = it.value.hit

        val entry = LogEntry({ LogSourceData.UNKNOWN }, LogChannel.ANALYTICS.getPropertyName(), "EXPR TIME")
        LazyLogEntry(entry) { sep ->
            "$program$sep$exprName$sep${duration.toString(unit)}${sep}$hit"
        }
    }
}

/**
 * Generate an ANALYTICS symbol table usage report in the form of a sequence of LazyLogEntry.
 * @see LazyLogEntry
 */
fun ProgramUsageTable.generateSymbolTableLogEntries(program: String, unit: DurationUnit = DurationUnit.NANOSECONDS): Sequence<LazyLogEntry> {
    val stats = this[program] ?: return emptySequence()

    return stats.symbolTableActions.asSequence().map {
        val action = it.key
        val duration = it.value.duration
        val hit = it.value.hit

        val entry = LogEntry({ LogSourceData.UNKNOWN }, LogChannel.ANALYTICS.getPropertyName(), "SYMTBL TIME")
        LazyLogEntry(entry) { sep ->
            "$program$sep$action$sep${duration.toString(unit)}${sep}$hit"
        }
    }
}