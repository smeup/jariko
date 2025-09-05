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

import com.smeup.rpgparser.execution.ErrorEvent
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.logging.LogChannel
import com.smeup.rpgparser.logging.ProgramUsageType
import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.parsing.facade.SourceReference
import com.smeup.rpgparser.utils.asNonNullString
import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Position
import java.io.PrintStream
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.system.measureNanoTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.DurationUnit

/**
 * A data class holding information about the source of the log
 * @see LogEntry
 */
data class LogSourceData(
    val programName: String,
    val line: String,
) {
    companion object {
        val UNKNOWN get() = LogSourceData("", "")

        fun fromProgram(name: String) = LogSourceData(name, "")
    }

    val filename get() = programName.replace('\\', '/').substringAfterLast("/").substringBeforeLast(".")

    fun projectLine(newLine: String) = LogSourceData(programName, newLine)
}

/**
 * A function returning a LogSourceData
 * @see LogSourceData
 */
typealias LogSourceProvider = () -> LogSourceData

/**
 * A representation of a single log entry
 */
data class LogEntry(
    val source: LogSourceProvider,
    val scope: String,
    val action: String? = null,
)

/**
 * A wrapper on LogEntry used to render its content dynamically only when needed
 * @see LogEntry
 */
class LazyLogEntry(
    val entry: LogEntry,
    val renderContent: (sep: String) -> String,
) {
    companion object {
        /**
         * Create a new LazyLogEntry providing an empty message
         */
        fun fromEntry(entry: LogEntry): LazyLogEntry = produceMessage(entry, "")

        /**
         * Create a new LazyLogEntry providing a prerendered message
         */
        fun produceMessage(
            entry: LogEntry,
            message: String,
        ): LazyLogEntry = LazyLogEntry(entry) { message }

        /**
         * Create a new LazyLogEntry for line logging
         * @see LinesLogHandler
         */
        fun produceLine(source: LogSourceProvider): LazyLogEntry {
            val entry = LogEntry(source, LinesLogHandler.SCOPE)
            return produceMessage(entry, "Line ${source().line}")
        }

        /**
         * Create a new LazyLogEntry for the DATA channel
         * @see LogChannel
         */
        fun produceData(
            source: LogSourceProvider,
            data: AbstractDataDefinition,
            value: Value,
            previous: Value?,
        ): LazyLogEntry {
            val entry = LogEntry(source, LogChannel.DATA.getPropertyName(), "ASSIGN")
            return LazyLogEntry(entry) { sep ->
                val prevValue = previous?.render() ?: "N/D"
                "${data.name} = ${value.render()}${sep}was: $prevValue"
            }
        }

        /**
         * Create a new LazyLogEntry for the EXPR channel
         * @see LogChannel
         */
        fun produceExpression(
            source: LogSourceProvider,
            expression: Expression,
            value: Value,
        ): LazyLogEntry {
            val actualProvider = {
                val computedSource = source()
                when {
                    expression.position != null -> computedSource.projectLine(expression.startLine())
                    expression.parent != null && expression.parent!!.position != null ->
                        computedSource.projectLine(
                            expression.parent!!.startLine(),
                        )
                    else -> computedSource
                }
            }

            val entry = LogEntry(actualProvider, LogChannel.EXPRESSION.getPropertyName(), "EVAL")

            return LazyLogEntry(entry) { sep ->
                val content =
                    buildString {
                        append(expression.render())
                        append(sep)
                        append(value.render())
                    }
                content
            }
        }

        /**
         * Create a new LazyLogEntry for the EvalLogHandler
         * @see EvalLogHandler
         */
        fun produceEval(
            source: LogSourceProvider,
            expression: Expression,
            value: Value,
        ): LazyLogEntry {
            val entry = LogEntry(source, EvalLogHandler.SCOPE)
            return produceMessage(
                entry,
                "Evaluating ${expression.type()} = $value -- Line: ${expression.position.line()}",
            )
        }

        /**
         * Create a new LazyLogEntry for Mutes
         * @see MuteAnnotation
         */
        fun produceMute(
            annotation: MuteAnnotation,
            source: LogSourceProvider,
            result: Value,
        ): LazyLogEntry {
            val message: String by lazy {
                when (annotation) {
                    is MuteComparisonAnnotation -> "executing MuteComparisonAnnotation: ${annotation.position} $result ${annotation.val1} ${annotation.comparison} ${annotation.val2} "
                    is MuteFailAnnotation -> "executing MuteFail: ${annotation.position} - ${result.render()}"
                    else -> this.toString()
                }
            }

            val entry = LogEntry(source, "MUTE")
            return produceMessage(entry, message)
        }

        /**
         * Create a new LazyLogEntry for RPG profiling
         * @see ProfilingAnnotation
         */
        fun produceProfiling(
            annotation: ProfilingAnnotation,
            source: LogSourceProvider,
        ): LazyLogEntry {
            val message: String by lazy {
                when (annotation) {
                    is ProfilingSpanStartAnnotation -> "opening RPG trace: ${annotation.name}"
                    is ProfilingSpanEndAnnotation -> "closing RPG trace"
                    else -> this.toString()
                }
            }

            val entry = LogEntry(source, "PROF")
            return produceMessage(entry, message)
        }

        /**
         * Create a new LazyLogEntry for the AssignmentsLogHandler
         * @see AssignmentsLogHandler
         */
        fun produceAssignment(
            source: LogSourceProvider,
            data: AbstractDataDefinition,
            value: Value,
        ): LazyLogEntry {
            val entry = LogEntry(source, AssignmentsLogHandler.SCOPE, value.render())
            return produceMessage(entry, "assigning to ${data.name} value $value")
        }

        /**
         * Create a new LazyLogEntry for the AssignmentsLogHandler describing array element assignments
         * @see AssignmentsLogHandler
         */
        fun produceAssignmentOfElement(
            source: LogSourceProvider,
            array: Expression,
            index: Int,
            value: Value,
        ): LazyLogEntry {
            val entry = LogEntry(source, AssignmentsLogHandler.SCOPE, "ARRAY")
            return produceMessage(entry, "assigning to $array[$index] value $value")
        }

        /**
         * Create a new LazyLogEntry from LogEntry data
         */
        fun produceInformational(
            source: LogSourceProvider,
            scope: String,
            action: String? = null,
        ): LazyLogEntry {
            val entry = LogEntry(source, scope, action)
            return fromEntry(entry)
        }

        /**
         * Create a new LazyLogEntry for the PERF channel
         * @see LogChannel
         */
//        fun producePerformance(source: LogSourceProvider, entity: String, elapsed: Duration): LazyLogEntry {
//            val entry = LogEntry(source, LogChannel.PERFORMANCE.getPropertyName(), entity)
//            return LazyLogEntry(entry) {
//                elapsed.inWholeMicroseconds.toString()
//            }
//        }

        /**
         * Create a new LazyLogEntry for the PERF channel and updates AnalyticsContext with its data
         * @see LogChannel
         */
        fun producePerformanceAndUpdateAnalytics(
            source: LogSourceProvider,
            type: ProgramUsageType,
            entity: String,
            elapsed: Duration,
        ): LazyLogEntry {
            val entry = LogEntry(source, LogChannel.PERFORMANCE.getPropertyName(), entity)

            val loggingContext = MainExecutionContext.getAnalyticsLoggingContext()
            val programName = MainExecutionContext.getExecutionProgramName()
            loggingContext?.recordUsage(programName, type, entity, elapsed)

            return LazyLogEntry(entry) {
                elapsed.inWholeMicroseconds.toString()
            }
        }

        /**
         * Create a new LazyLogEntry for the ANALYTICS channel
         * @see LogChannel
         */
        fun produceAnalytics(
            source: LogSourceProvider,
            action: String,
            message: String,
        ): LazyLogEntry {
            val entry = LogEntry(source, LogChannel.ANALYTICS.getPropertyName(), action)
            return produceMessage(entry, message)
        }

        /**
         * Create a new LazyLogEntry for the STMT channel
         * @see LogChannel
         */
        fun produceStatement(
            source: LogSourceProvider,
            statementName: String,
            action: String,
        ): LazyLogEntry {
            val entry = LogEntry(source, LogChannel.STATEMENT.getPropertyName(), action)
            return LazyLogEntry(entry) { statementName }
        }

        /**
         * Create a new LazyLogEntry for the PARS channel on parsing start
         * @see LogChannel
         */
        fun produceParsingStart(
            source: LogSourceProvider,
            entity: String,
        ): LazyLogEntry {
            val entry = LogEntry(source, LogChannel.PARSING.getPropertyName(), "START")
            return LazyLogEntry(entry) { entity }
        }

        /**
         * Create a new LazyLogEntry for the PARS channel on parsing end
         * @see LogChannel
         */
        fun produceParsingEnd(
            source: LogSourceProvider,
            entity: String,
            elapsed: Duration,
        ): LazyLogEntry {
            val entry = LogEntry(source, LogChannel.PARSING.getPropertyName(), "END")
            return LazyLogEntry(entry) { sep ->
                buildString {
                    append(entity)
                    append(sep)
                    append("elapsed ")
                    append(elapsed.toString(DurationUnit.MICROSECONDS))
                }
            }
        }

        /**
         * Create a new LazyLogEntry for the RESL channel
         * @see LogChannel
         */
        fun produceResolution(
            source: LogSourceProvider,
            resolution: String? = null,
        ): LazyLogEntry {
            val entry = LogEntry(source, LogChannel.RESOLUTION.getPropertyName())
            resolution ?: return fromEntry(entry)
            return produceMessage(entry, resolution)
        }

        /**
         * Create a new LazyLogEntry for the ERR channel
         * @see LogChannel
         */
        fun produceError(errorEvent: ErrorEvent): LazyLogEntry {
            val source =
                { LogSourceData(errorEvent.sourceReference?.sourceId ?: "", errorEvent.absoluteLine?.toString() ?: "") }
            val entry = LogEntry(source, LogChannel.ERROR.getPropertyName())
            return LazyLogEntry(entry) { errorEvent.toString() }
        }

        /**
         * Create a new LazyLogEntry for subroutine start detection
         * @see ListLogHandler
         */
        fun produceSubroutineStart(
            source: LogSourceProvider,
            subroutine: Subroutine,
        ): LazyLogEntry {
            val entry = LogEntry(source, "SUBROUTINE START", subroutine.name)
            return fromEntry(entry)
        }

        /**
         * Create a new LazyLogEntry for the LOOP channel on loop start
         * @see LogChannel
         */
        fun produceLoopStart(
            source: LogSourceProvider,
            entity: String,
            subject: String,
        ): LazyLogEntry {
            val entry = LogEntry(source, LogChannel.LOOP.getPropertyName(), "START")
            return LazyLogEntry(entry) { "$entity $subject" }
        }

        /**
         * Create a new LazyLogEntry for the LOOP channel on loop end
         * @see LogChannel
         */
        fun produceLoopEnd(
            source: LogSourceProvider,
            entity: String,
            subject: String,
            iterations: Long,
        ): LazyLogEntry {
            val entry = LogEntry(source, LogChannel.LOOP.getPropertyName(), "END")
            return LazyLogEntry(entry) { "$entity $subject $iterations" }
        }
    }

    private fun renderHeader(sep: String) =
        buildString {
            val source = entry.source()
            append(source.filename)
            append(sep)
            append(source.line)
        }

    fun render(
        sep: String,
        withHeader: Boolean = false,
        withScope: Boolean = true,
    ) = buildString {
        if (withHeader) {
            val header = renderHeader(sep)
            append(header)
            append(sep)
        }

        if (withScope) {
            append(entry.scope)
            append(sep)
        }

        if (entry.action != null) {
            append(entry.action)
            append(sep)
        }

        val content = renderContent(sep)
        append(content)
    }

    fun renderScoped(): String {
        val rendered = render("\t", withHeader = true, withScope = false)
        val timestamp =
            DateTimeFormatter
                .ofPattern("HH:mm:ss.SSS")
                .withZone(ZoneId.systemDefault())
                .format(Instant.now())
        return "$timestamp\t${entry.scope}\t$rendered"
    }
}

/**
 * A log handler specifically intended for usage by the interpreter
 */
interface InterpreterLogHandler {
    /**
     * Checks if the log entry should be accepted by this handler
     * @see LogEntry
     */
    fun accepts(entry: LogEntry): Boolean

    /**
     * Handles the log entry provided by the renderer
     * @see LazyLogEntry
     * @see LogEntry
     */
    fun handle(renderer: LazyLogEntry)
}

// TODO remove used in Test only
class LinesLogHandler(
    private val printStream: PrintStream = System.out,
) : InterpreterLogHandler {
    companion object {
        const val SCOPE = "LINE"
    }

    override fun accepts(entry: LogEntry) = entry.scope == SCOPE

    override fun handle(renderer: LazyLogEntry) {
        printStream.println("[LOG] ${renderer.render("\t", withHeader = false)}")
    }
}

class AssignmentsLogHandler(
    private val printStream: PrintStream = System.out,
) : InterpreterLogHandler {
    companion object {
        const val SCOPE = "ASSIGNMENT"
    }

    override fun accepts(entry: LogEntry) = entry.scope == SCOPE

    override fun handle(renderer: LazyLogEntry) {
        printStream.println("[LOG] ${renderer.render("\t", withHeader = false)}")
    }
}

// TODO remove used in Test only
class EvalLogHandler(
    private val printStream: PrintStream = System.out,
) : InterpreterLogHandler {
    companion object {
        const val SCOPE = "EVAL"
    }

    override fun accepts(entry: LogEntry) = entry.scope == SCOPE

    override fun handle(renderer: LazyLogEntry) {
        printStream.println("[LOG] ${renderer.render("\t", withHeader = false)}")
    }
}

object SimpleLogHandler : InterpreterLogHandler {
    override fun accepts(entry: LogEntry) = true

    override fun handle(renderer: LazyLogEntry) {
        println("[LOG] ${renderer.render("\t", withHeader = false)}")
    }

    fun fromFlag(flag: Boolean) =
        if (flag) {
            listOf(this)
        } else {
            emptyList()
        }
}

class ListLogHandler : InterpreterLogHandler {
    private val _logs = LinkedList<LogEntry>()

    override fun accepts(entry: LogEntry) = true

    override fun handle(renderer: LazyLogEntry) {
        _logs.add(renderer.entry)
    }

    // Immutable view of the internal mutable list
    val logs: List<LogEntry>
        get() = _logs

    fun getExecutedSubroutines() = _logs.asSequence().filter { it.scope == "SUBROUTINE START" }.toList()

    fun getExecutedSubroutineNames() = getExecutedSubroutines().map { it.action }

//    fun getEvaluatedExpressions() = _logs.filterIsInstance<ExpressionEvaluationLogEntry>()
    fun getAssignments() = _logs.filter { it.scope == AssignmentsLogHandler.SCOPE }
    /**
     * Remove an expression if the last time the same expression was evaluated it had the same searchedValued
     */
//    fun getEvaluatedExpressionsConcise(): List<LogEntry2> {
//        val base = _logs.asSequence().filter { it.scope == EvalLogHandler.SCOPE}.toMutableList()
//        var i = 0
//        while (i < base.size) {
//            val current = base[i]
//            val found = base.subList(0, i).reversed().firstOrNull {
//                it. == current.expression
//            }?.value == current.value
//            if (found) {
//                base.removeAt(i)
//            } else {
//                i++
//            }
//        }
//        return base
//    }
}

fun List<InterpreterLogHandler>.renderLog(renderer: LazyLogEntry) {
    val time =
        measureNanoTime {
            this.forEach {
                try {
                    if (it.accepts(renderer.entry)) {
                        it.handle(renderer)
                    }
                } catch (t: Throwable) {
                    // TODO: how should we handle exceptions?
                    t.printStackTrace()
                }
            }
        }.nanoseconds

    val programName = MainExecutionContext.getExecutionProgramName()
    MainExecutionContext.getAnalyticsLoggingContext()?.recordUsage(programName, ProgramUsageType.LogRendering, "", time)
}

fun Position?.line() =
    this
        ?.relative()
        ?.second
        ?.renderStartLine()
        .asNonNullString()

fun Position?.atLine() =
    this
        ?.relative()
        ?.second
        ?.renderStartLine()
        ?.let { "line $it " } ?: ""

fun Node?.startLine() =
    this
        ?.position
        ?.relative()
        ?.second
        ?.renderStartLine()
        .asNonNullString()

fun Node?.endLine() =
    this
        ?.position
        ?.relative()
        ?.second
        ?.renderEndLine()
        .asNonNullString()

fun SourceReference.renderStartLine() = "${this.relativeLine}"

fun SourceReference.renderEndLine() = "${this.position?.end?.line}"
