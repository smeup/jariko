/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * imitations under the License.
 *
 */

package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.logging.ProgramUsageType
import com.smeup.rpgparser.parsing.ast.CompositeStatement
import com.smeup.rpgparser.parsing.ast.LoopStatement
import com.smeup.rpgparser.parsing.ast.Statement
import kotlin.time.Duration

/**
 * Object holding information about the interpreter logging configuration and state.
 */
data class InterpreterLoggingContext(
    var logHandlers: List<InterpreterLogHandler> = emptyList(),
) {
    /**
     * Determine whether logs are enabled or not.
     */
    val logsEnabled get() = logHandlers.isNotEmpty()

    /**
     * Log a [LazyLogEntry].
     */
    fun log(renderer: LazyLogEntry) {
        logHandlers.renderLog(renderer)
    }

    /**
     * Emit analytics report.
     */
    fun emitAnalyticsReport() {
        val ctx = MainExecutionContext.getAnalyticsLoggingContext() ?: return
        ctx.generateCompleteReport().forEach { entry -> renderLog { entry } }
    }

    /**
     * Open a logging scope.
     *
     * @param statement The statement connected to the scope.
     * @param sourceProducer The log source provider.
     */
    fun openLoggingScope(
        statement: Statement,
        sourceProducer: LogSourceProvider,
    ) {
        renderLog { statement.getResolutionLogRenderer(sourceProducer) }
        if (statement is CompositeStatement) {
            renderLog { statement.getStatementLogRenderer(sourceProducer, "START") }
        } else {
            renderLog { statement.getStatementLogRenderer(sourceProducer, "EXEC") }
        }

        if (statement is LoopStatement) {
            renderLog {
                LazyLogEntry.produceLoopStart(
                    sourceProducer,
                    statement.loggableEntityName,
                    statement.loopSubject,
                )
            }
        }
    }

    /**
     * Close a logging scope.
     *
     * @param statement The statement connected to the scope.
     * @param programName The name of the program in which the log had origin.
     * @param sourceProducer The log source provider.
     * @param executionTime The duration of the execution of the scope.
     */
    fun closeLoggingScope(
        statement: Statement,
        programName: String,
        sourceProducer: LogSourceProvider,
        executionTime: Duration,
    ) {
        val loggingContext = MainExecutionContext.getAnalyticsLoggingContext()
        loggingContext?.recordStatementExecution(programName, statement.loggableEntityName, executionTime)

        if (statement is LoopStatement) {
            renderLog {
                LazyLogEntry.produceLoopEnd(
                    sourceProducer,
                    statement.loggableEntityName,
                    statement.loopSubject,
                    statement.iterations,
                )
            }
        }

        if (statement is CompositeStatement) {
            renderLog { statement.getStatementLogRenderer(sourceProducer, "END") }
        }

        renderLog {
            LazyLogEntry.producePerformanceAndUpdateAnalytics(
                sourceProducer,
                ProgramUsageType.Statement,
                statement.loggableEntityName,
                executionTime,
            )
        }
    }

    /**
     * Render a log lazily.
     *
     * @param producer The log producer.
     */
    inline fun renderLog(producer: () -> LazyLogEntry?) {
        if (!logsEnabled) return

        val entry = producer()
        entry ?: return
        log(entry)
    }
}
