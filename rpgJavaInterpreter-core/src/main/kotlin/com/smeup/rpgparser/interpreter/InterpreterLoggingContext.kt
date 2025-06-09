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

data class InterpreterLoggingContext(
    var logHandlers: List<InterpreterLogHandler> = emptyList()
) {
    val logsEnabled get() = logHandlers.isNotEmpty()

    fun doLog(renderer: LazyLogEntry) {
        logHandlers.renderLog(renderer)
    }

    fun openLoggingScope(statement: Statement, sourceProducer: LogSourceProvider) {
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
                    statement.loopSubject
                )
            }
        }
    }

    fun closeLoggingScope(
        statement: Statement,
        programName: String,
        sourceProducer: LogSourceProvider,
        executionTime: Duration
    ) {
        val loggingContext = MainExecutionContext.getAnalyticsLoggingContext()
        loggingContext?.recordStatementExecution(programName, statement.loggableEntityName, executionTime)

        if (statement is LoopStatement) {
            renderLog {
                LazyLogEntry.produceLoopEnd(
                    sourceProducer,
                    statement.loggableEntityName,
                    statement.loopSubject,
                    statement.iterations
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
                executionTime
            )
        }
    }

    inline fun renderLog(producer: () -> LazyLogEntry?) {
        if (!logsEnabled) return

        val entry = producer()
        entry ?: return
        doLog(entry)
    }
}
