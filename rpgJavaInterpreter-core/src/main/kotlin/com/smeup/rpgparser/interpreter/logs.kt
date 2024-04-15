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
import com.smeup.rpgparser.logging.DataLogMetadata
import com.smeup.rpgparser.logging.LogChannel
import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.parsing.facade.SourceReference
import com.smeup.rpgparser.utils.asNonNullString
import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Position
import java.io.PrintStream
import java.util.*
import kotlin.time.Duration

data class LogSourceData(
    val programName: String,
    val line: String
) {
    val filename = programName.replace('\\', '/').substringAfterLast("/").substringBeforeLast(".")
    fun projectLine(newLine: String) = LogSourceData(programName, newLine)
}

data class LogEntry(
    val source: LogSourceData,
    val scope: String,
    val action: String? = null
)

class LazyLogEntry(val entry: LogEntry, val renderContent: (sep: String) -> String) {
    companion object {
        fun fromEntry(entry: LogEntry): LazyLogEntry {
            return produceMessage(entry, "")
        }

        fun produceMessage(entry: LogEntry, message: String): LazyLogEntry {
            return LazyLogEntry(entry) { message }
        }

        fun produceLine(source: LogSourceData): LazyLogEntry {
            val entry = LogEntry(source, LinesLogHandler.SCOPE)
            return produceMessage(entry, "Line ${source.line}")
        }

        fun produceData(
            source: LogSourceData,
            data: AbstractDataDefinition,
            value: Value,
            previous: Value?
        ): LazyLogEntry {
            val metadata = DataLogMetadata(data, value, previous)
            val entry = LogEntry(source, LogChannel.DATA.getPropertyName(), "ASSIGN")
            return LazyLogEntry(entry) { sep ->
                val prevValue = if (metadata.previous == null) "N/D" else metadata.previous.render()
                "${metadata.data.name} = ${metadata.value.render()}${sep}was: $prevValue"
            }
        }

        fun produceExpression(
            source: LogSourceData,
            expression: Expression,
            value: Value
        ): LazyLogEntry {

            val actualSource = when {
                expression.position != null -> source.projectLine(expression.startLine())
                expression.parent != null && expression.parent!!.position != null -> source.projectLine(
                    expression.parent!!.startLine()
                )

                else -> source
            }

            val entry = LogEntry(actualSource, LogChannel.EXPRESSION.getPropertyName(), "EVAL")

            return LazyLogEntry(entry) { sep ->
                val content = buildString {
                    append(expression.render())
                    append(sep)
                    append(value.render())
                }
                content
            }
        }

        fun produceEval(
            source: LogSourceData,
            expression: Expression,
            value: Value
        ): LazyLogEntry {
            val entry = LogEntry(source, EvalLogHandler.SCOPE)
            return produceMessage(
                entry, "Evaluating ${expression.type()} = $value -- Line: ${expression.position.line()}"
            )
        }

        fun produceMute(
            annotation: MuteAnnotation,
            source: LogSourceData,
            result: Value
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

        fun produceAssignment(
            source: LogSourceData,
            data: AbstractDataDefinition,
            value: Value
        ): LazyLogEntry {
            val entry = LogEntry(source, AssignmentsLogHandler.SCOPE, value.render())
            return produceMessage(entry, "assigning to ${data.name} value $value")
        }

        fun produceAssignmentOfElement(
            source: LogSourceData,
            array: Expression,
            index: Int,
            value: Value
        ): LazyLogEntry {
            val entry = LogEntry(source, AssignmentsLogHandler.SCOPE, "ARRAY")
            return produceMessage(entry, "assigning to $array[$index] value $value")
        }

        fun produceInformational(
            source: LogSourceData,
            scope: String,
            action: String? = null
        ): LazyLogEntry {
            val entry = LogEntry(source, scope, action)
            return fromEntry(entry)
        }

        fun producePerformance(source: LogSourceData, entity: String, elapsed: Duration): LazyLogEntry {
            val entry = LogEntry(source, LogChannel.PERFORMANCE.getPropertyName(), entity)
            return LazyLogEntry(entry) {
                buildString {
                    append("elapsed ")
                    append(elapsed.toString())
                }
            }
        }

        fun produceStatement(
            source: LogSourceData,
            statementName: String,
            action: String
        ): LazyLogEntry {
            val entry = LogEntry(source, LogChannel.STATEMENT.getPropertyName(), action)
            return LazyLogEntry(entry) {
                statementName
            }
        }

        fun produceResolution(
            source: LogSourceData
        ): LazyLogEntry {
            val entry = LogEntry(source, LogChannel.RESOLUTION.getPropertyName())
            return fromEntry(entry)
        }

        fun produceError(
            errorEvent: ErrorEvent
        ): LazyLogEntry {
            val source = LogSourceData(errorEvent.sourceReference?.sourceId ?: "", errorEvent.absoluteLine?.toString() ?: "")
            val entry = LogEntry(source, LogChannel.ERROR.getPropertyName())
            return LazyLogEntry(entry) { errorEvent.toString() }
        }

        fun produceSubroutineStart(source: LogSourceData, subroutine: Subroutine): LazyLogEntry {
            val entry = LogEntry(source, "SUBROUTINE START", subroutine.name)
            return fromEntry(entry)
        }

        fun produceLoopStart(source: LogSourceData, entity: String, subject: String): LazyLogEntry {
            val entry = LogEntry(source, LogChannel.LOOP.getPropertyName(), "START")
            return LazyLogEntry(entry) { "$entity $subject" }
        }

        fun produceLoopEnd(source: LogSourceData, entity: String, subject: String, iterations: Long): LazyLogEntry {
            val entry = LogEntry(source, LogChannel.LOOP.getPropertyName(), "END")
            return LazyLogEntry(entry) { "$entity $subject $iterations" }
        }
    }

    private fun renderHeader(sep: String) = buildString {
        append(entry.source.filename)
        append(sep)
        append(entry.source.line)
    }

    fun render(sep: String, withHeader: Boolean = false, withScope: Boolean = true) = buildString {
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
}

interface InterpreterLogHandler {
    fun accepts(entry: LogEntry): Boolean
    fun handle(renderer: LazyLogEntry)
}

// TODO remove used in Test only
class LinesLogHandler(private val printStream: PrintStream = System.out) : InterpreterLogHandler {
    companion object {
        const val SCOPE = "LINE"
    }

    override fun accepts(entry: LogEntry) = entry.scope == SCOPE
    override fun handle(renderer: LazyLogEntry) {
        printStream.println("[LOG] ${renderer.render("\t", withHeader = false)}")
    }
}

class AssignmentsLogHandler(private val printStream: PrintStream = System.out) : InterpreterLogHandler {
    companion object {
        const val SCOPE = "ASSIGNMENT"
    }

    override fun accepts(entry: LogEntry) = entry.scope == SCOPE
    override fun handle(renderer: LazyLogEntry) {
        printStream.println("[LOG] ${renderer.render("\t", withHeader = false)}")
    }
}

// TODO remove used in Test only
class EvalLogHandler(private val printStream: PrintStream = System.out) : InterpreterLogHandler {
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

    fun fromFlag(flag: Boolean) = if (flag) {
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
    this.forEach {
        try {
            if (it.accepts(renderer.entry))
                it.handle(renderer)
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