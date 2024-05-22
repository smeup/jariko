package com.smeup.rpgparser.logging

import com.smeup.rpgparser.interpreter.LazyLogEntry
import com.smeup.rpgparser.interpreter.LogEntry
import com.smeup.rpgparser.interpreter.LogSourceData
import com.smeup.rpgparser.interpreter.SymbolTableAction
import java.util.*
import kotlin.collections.HashMap
import kotlin.time.Duration

enum class ProgramUsageType {
    Statement,
    Expression,
    SymbolTable,
    Parsing,
    LogRendering,
    Interpretation
}

/**
 * Collects the usage information about the main parts of the program.
 */
class ProgramUsageStats {
    val statements: HashMap<String, UsageMeasurement> by lazy { hashMapOf() }
    val expressions: HashMap<String, UsageMeasurement> by lazy { hashMapOf() }
    val symbolTableActions: EnumMap<SymbolTableAction, UsageMeasurement> by lazy {
        EnumMap(SymbolTableAction::class.java)
    }
    val parsingSteps: HashMap<String, UsageMeasurement> by lazy { hashMapOf() }
//    val nestedStatements: HashMap<String, UsageMeasurement> by lazy { hashMapOf() }
//    val nestedExpressions: HashMap<String, UsageMeasurement> by lazy { hashMapOf() }
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
 * Records the execution of a nested statement.
 * @see ILoggableStatement
 */
// fun ProgramUsageTable.recordNestedStatement(program: String, scope: Stack<String>, entity: String, time: Duration) {
//    val programStats = this.getOrPut(program) { ProgramUsageStats() }
//    val path = "${scope.joinToString(separator = "/")}/$entity"
//    val measurement = programStats.nestedStatements.getOrDefault(path, UsageMeasurement.new())
//    programStats.nestedStatements[path] = measurement.hit(time)
// }

/**
 * Records the execution of a nested expression.
 * @see ILoggableExpression
 */
// fun ProgramUsageTable.recordNestedExpression(program: String, scope: Stack<String>, time: Duration) {
//    val programStats = this.getOrPut(program) { ProgramUsageStats() }
//    val path = scope.joinToString(separator = "->")
//    val measurement = programStats.nestedExpressions.getOrDefault(path, UsageMeasurement.new())
//    programStats.nestedExpressions[path] = measurement.hit(time)
// }

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
 * Records a parsing step.
 * @see SymbolTableAction
 */
fun ProgramUsageTable.recordParsing(program: String, step: String, time: Duration) {
    val programStats = this.getOrPut(program) { ProgramUsageStats() }
    val measurement = programStats.parsingSteps.getOrDefault(step, UsageMeasurement.new())
    programStats.parsingSteps[step] = measurement.hit(time)
}

/**
 * Generate an ANALYTICS statement usage report in the form of a sequence of LazyLogEntry.
 * @see LazyLogEntry
 */
fun ProgramUsageTable.generateStatementLogEntries(program: String): Sequence<LazyLogEntry> {
    val stats = this[program] ?: return emptySequence()

    return stats.statements.asSequence().map {
        val statementName = it.key
        val duration = it.value.duration
        val hit = it.value.hit

        val entry = LogEntry({ LogSourceData.fromProgram(program) }, LogChannel.ANALYTICS.getPropertyName(), "STMT TIME")
        LazyLogEntry(entry) { sep ->
            "$statementName$sep${duration.inWholeMicroseconds}${sep}$hit"
        }
    }
}

/**
 * Generate an ANALYTICS nested statement usage report in the form of a sequence of LazyLogEntry.
 * @see LazyLogEntry
// */
// fun ProgramUsageTable.generateNestedStatementLogEntries(program: String): Sequence<LazyLogEntry> {
//    val stats = this[program] ?: return emptySequence()
//
//    return stats.nestedStatements.asSequence().map {
//        val statementName = it.key
//        val duration = it.value.duration
//        val hit = it.value.hit
//
//        val entry = LogEntry({ LogSourceData.fromProgram(program) }, LogChannel.ANALYTICS.getPropertyName(), "NESTED STMT TIME")
//        LazyLogEntry(entry) { sep ->
//            "$statementName$sep${duration.inWholeMicroseconds}${sep}$hit"
//        }
//    }
// }

/**
 * Generate an ANALYTICS nested expression usage report in the form of a sequence of LazyLogEntry.
 * @see LazyLogEntry
 */
// fun ProgramUsageTable.generateNestedExpressionLogEntries(program: String): Sequence<LazyLogEntry> {
//    val stats = this[program] ?: return emptySequence()
//
//    return stats.nestedExpressions.asSequence().map {
//        val statementName = it.key
//        val duration = it.value.duration
//        val hit = it.value.hit
//
//        val entry = LogEntry({ LogSourceData.fromProgram(program) }, LogChannel.ANALYTICS.getPropertyName(), "NESTED EXPR TIME")
//        LazyLogEntry(entry) { sep ->
//            "$statementName$sep${duration.inWholeMicroseconds}${sep}$hit"
//        }
//    }
// }

/**
 * Generate an ANALYTICS expression usage report in the form of a sequence of LazyLogEntry.
 * @see LazyLogEntry
 */
fun ProgramUsageTable.generateExpressionLogEntries(program: String): Sequence<LazyLogEntry> {
    val stats = this[program] ?: return emptySequence()

    return stats.expressions.asSequence().map {
        val exprName = it.key
        val duration = it.value.duration
        val hit = it.value.hit

        val entry = LogEntry({ LogSourceData.fromProgram(program) }, LogChannel.ANALYTICS.getPropertyName(), "EXPR TIME")
        LazyLogEntry(entry) { sep ->
            "$exprName$sep${duration.inWholeMicroseconds}${sep}$hit"
        }
    }
}

/**
 * Generate an ANALYTICS symbol table usage report in the form of a sequence of LazyLogEntry.
 * @see LazyLogEntry
 */
fun ProgramUsageTable.generateSymbolTableLogEntries(program: String): Sequence<LazyLogEntry> {
    val stats = this[program] ?: return emptySequence()

    return stats.symbolTableActions.asSequence().map {
        val action = it.key
        val duration = it.value.duration
        val hit = it.value.hit

        val entry = LogEntry({ LogSourceData.fromProgram(program) }, LogChannel.ANALYTICS.getPropertyName(), "SYMTBL TIME")
        LazyLogEntry(entry) { sep ->
            "$action$sep${duration.inWholeMicroseconds}${sep}$hit"
        }
    }
}

/**
 * Generate an ANALYTICS parsing usage report in the form of a sequence of LazyLogEntry.
 * @see LazyLogEntry
 */
fun ProgramUsageTable.generateParsingLogEntries(program: String): Sequence<LazyLogEntry> {
    val stats = this[program] ?: return emptySequence()

    return stats.parsingSteps.asSequence().map {
        val step = it.key
        val duration = it.value.duration
        val hit = it.value.hit

        val entry = LogEntry({ LogSourceData.fromProgram(program) }, LogChannel.ANALYTICS.getPropertyName(), "PARS TIME")
        LazyLogEntry(entry) { sep ->
            "$step$sep${duration.inWholeMicroseconds}${sep}$hit"
        }
    }
}