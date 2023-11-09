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

package com.smeup.rpgparser.logging

import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.ast.LogicalAndExpr
import com.smeup.rpgparser.parsing.ast.LogicalOrExpr
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class ExpressionLogHandler(level: LogLevel, sep: String) : LogHandler(level, sep), InterpreterLogHandler {
    val logger = LogManager.getLogger(EXPRESSION_LOGGER)

    override fun render(logEntry: LogEntry): String {
        val fileName = extractFilename(logEntry.programName)
        return logEntry.renderExpression("EXPR", fileName, this.sep)
    }
    override fun handle(logEntry: LogEntry) {
        if (logger.checkChannelLoggingEnabled()) {
            when (logEntry) {
                is ExpressionEvaluationLogEntry -> {
                    // Avoid expression
                    if (logEntry.expression.parent !is LogicalAndExpr && logEntry.expression.parent !is LogicalOrExpr) {
                        logger.fireLogInfo(render(logEntry))
                    }
                }
            }
        }
    }
}

class PerformanceLogHandler(level: LogLevel, sep: String) : LogHandler(level, sep), InterpreterLogHandler {
    private val logger = LogManager.getLogger(PERFORMANCE_LOGGER)

    override fun render(logEntry: LogEntry): String {
        val fileName = extractFilename(logEntry.programName)
        return logEntry.renderPerformance("PERF", fileName, this.sep)
    }

    override fun handle(logEntry: LogEntry) {

        if (logger.checkChannelLoggingEnabled()) {
            when (logEntry) {
                is SubroutineExecutionLogEnd -> logger.fireLogInfo(render(logEntry))
                is ForStatementExecutionLogEnd -> logger.fireLogInfo(render(logEntry))
                is DoStatemenExecutionLogEnd -> logger.fireLogInfo(render(logEntry))
                is DowStatemenExecutionLogEnd -> logger.fireLogInfo(render(logEntry))
                is CallEndLogEntry -> logger.fireLogInfo(render(logEntry))
                is ProgramInterpretationLogEnd -> logger.fireLogInfo(render(logEntry))
                is SymbolTableIniLogEnd -> logger.fireLogInfo(render(logEntry))
                is SymbolTableLoadLogEnd -> logger.fireLogInfo(render(logEntry))
                is SymbolTableStoreLogEnd -> logger.fireLogInfo(render(logEntry))
                is SetLogEnd -> logger.fireLogInfo(render(logEntry))
                is ReadLogEnd -> logger.fireLogInfo(render(logEntry))
                is ReadEqualLogEnd -> logger.fireLogInfo(render(logEntry))
                is StoreLogEnd -> logger.fireLogInfo(render(logEntry))
            }
        }
    }
}

class StatementLogHandler(level: LogLevel, sep: String) : LogHandler(level, sep), InterpreterLogHandler {
    val logger = LogManager.getLogger(STATEMENT_LOGGER)
    var inLoop: Int = 0

    override fun render(logEntry: LogEntry): String {
        val fileName = extractFilename(logEntry.programName)
        return logEntry.renderStatement("STMT", fileName, this.sep)
    }

    override fun handle(logEntry: LogEntry) {

        if (logger.checkChannelLoggingEnabled()) {
            when (logEntry) {
                is RpgLoadLogStart -> logger.fireLogInfo(render(logEntry))
                is RpgLoadLogEnd -> logger.fireLogInfo(render(logEntry))
                is PreprocessingLogStart -> logger.fireLogInfo(render(logEntry))
                is PreprocessingLogEnd -> logger.fireLogInfo(render(logEntry))
                is LexerLogStart -> logger.fireLogInfo(render(logEntry))
                is LexerLogEnd -> logger.fireLogInfo(render(logEntry))
                is ParserLogStart -> logger.fireLogInfo(render(logEntry))
                is ParserLogEnd -> logger.fireLogInfo(render(logEntry))
                is RContextLogStart -> logger.fireLogInfo(render(logEntry))
                is RContextLogEnd -> logger.fireLogInfo(render(logEntry))
                is CheckParseTreeLogStart -> logger.fireLogInfo(render(logEntry))
                is CheckParseTreeLogEnd -> logger.fireLogInfo(render(logEntry))
                is FindMutesLogStart -> logger.fireLogInfo(render(logEntry))
                is FindMutesLogEnd -> logger.fireLogInfo(render(logEntry))
                is AstLogStart -> logger.fireLogInfo(render(logEntry))
                is AstLogEnd -> logger.fireLogInfo(render(logEntry))
                is ParamListStatemenExecutionLog -> logger.fireLogInfo(render(logEntry))
                is SelectCaseExecutionLogEntry -> logger.fireLogInfo(render(logEntry))
                is SelectOtherExecutionLogEntry -> logger.fireLogInfo(render(logEntry))
                is SubroutineExecutionLogStart -> logger.fireLogInfo(render(logEntry))
                is SubroutineExecutionLogEnd -> logger.fireLogInfo(render(logEntry))
                is ProgramInterpretationLogStart -> logger.fireLogInfo(render(logEntry))
                is ProgramInterpretationLogEnd -> logger.fireLogInfo(render(logEntry))
                is IfExecutionLogEntry -> logger.fireLogInfo(render(logEntry))
                is ElseIfExecutionLogEntry -> logger.fireLogInfo(render(logEntry))
                is ClearStatemenExecutionLog -> logger.fireLogInfo(render(logEntry))
                is MoveStatemenExecutionLog -> logger.fireLogInfo(render(logEntry))
                is LeaveStatemenExecutionLog -> logger.fireLogInfo(render(logEntry))
                is IterStatemenExecutionLog -> logger.fireLogInfo(render(logEntry))
                is ElseExecutionLogEntry -> logger.fireLogInfo(render(logEntry))
                is CallExecutionLogEntry -> logger.fireLogInfo(render(logEntry))
                is CallEndLogEntry -> logger.fireLogInfo(render(logEntry))
                is EvaluationLogEntry -> logger.fireLogInfo(render(logEntry))
                is ForStatementExecutionLogStart -> {
                    logger.fireLogInfo(render(logEntry))
                    this.inLoop++
                }
                is ForStatementExecutionLogEnd -> {
                    logger.fireLogInfo(render(logEntry))
                    this.inLoop--
                }
                is DoStatemenExecutionLogStart -> {
                    logger.fireLogInfo(render(logEntry))
                    this.inLoop++
                }
                is DoStatemenExecutionLogEnd -> {
                    logger.fireLogInfo(render(logEntry))
                    this.inLoop--
                }
                is DowStatemenExecutionLogStart -> {
                    logger.fireLogInfo(render(logEntry))
                    this.inLoop++
                }
                is DowStatemenExecutionLogEnd -> {
                    logger.fireLogInfo(render(logEntry))
                    this.inLoop--
                }
                is SymbolTableIniLogStart -> logger.fireLogInfo(render(logEntry))
                is SymbolTableIniLogEnd -> logger.fireLogInfo(render(logEntry))
                is SymbolTableLoadLogStart -> logger.fireLogInfo(render(logEntry))
                is SymbolTableLoadLogEnd -> logger.fireLogInfo(render(logEntry))
                is SymbolTableStoreLogStart -> logger.fireLogInfo(render(logEntry))
                is SymbolTableStoreLogEnd -> logger.fireLogInfo(render(logEntry))
                is SetLogStart -> logger.fireLogInfo(render(logEntry))
                is SetLogEnd -> logger.fireLogInfo(render(logEntry))
                is ReadLogStart -> logger.fireLogInfo(render(logEntry))
                is ReadLogEnd -> logger.fireLogInfo(render(logEntry))
                is ReadEqualLogStart -> logger.fireLogInfo(render(logEntry))
                is ReadEqualLogEnd -> logger.fireLogInfo(render(logEntry))
                is StoreLogStart -> logger.fireLogInfo(render(logEntry))
                is StoreLogEnd -> logger.fireLogInfo(render(logEntry))
            }
        }
    }
}

class DataLogHandler(level: LogLevel, sep: String) : LogHandler(level, sep), InterpreterLogHandler {
    val logger = LogManager.getLogger(DATA_LOGGER)

    override fun render(logEntry: LogEntry): String {
        val fileName = extractFilename(logEntry.programName)
        return logEntry.renderData("DATA", fileName, this.sep)
    }

    override fun handle(logEntry: LogEntry) {
        if (logger.checkChannelLoggingEnabled()) {
            when (logEntry) {
                is AssignmentLogEntry -> logger.fireLogInfo(render(logEntry))
            }
        }
    }
}

class LoopLogHandler(level: LogLevel, sep: String) : LogHandler(level, sep), InterpreterLogHandler {
    val logger = LogManager.getLogger(LOOP_LOGGER)

    override fun render(logEntry: LogEntry): String {
        val fileName = extractFilename(logEntry.programName)
        return logEntry.renderLoop("LOOP", fileName, this.sep)
    }

    override fun handle(logEntry: LogEntry) {
        if (logger.checkChannelLoggingEnabled()) {
            when (logEntry) {
                is ForStatementExecutionLogStart -> logger.fireLogInfo(render(logEntry))
                is ForStatementExecutionLogEnd -> logger.fireLogInfo(render(logEntry))
                is DoStatemenExecutionLogStart -> logger.fireLogInfo(render(logEntry))
                is DoStatemenExecutionLogEnd -> logger.fireLogInfo(render(logEntry))
                is DowStatemenExecutionLogStart -> logger.fireLogInfo(render(logEntry))
                is DowStatemenExecutionLogEnd -> logger.fireLogInfo(render(logEntry))
            }
        }
    }
}

class ResolutionLogHandler(level: LogLevel, sep: String) : LogHandler(level, sep), InterpreterLogHandler {
    val logger = LogManager.getLogger(RESOLUTION_LOGGER)

    override fun render(logEntry: LogEntry): String {
        val fileName = extractFilename(logEntry.programName)
        return logEntry.renderResolution("RESL", fileName, this.sep)
    }

    override fun handle(logEntry: LogEntry) {
        if (logger.checkChannelLoggingEnabled()) {
            when (logEntry) {
                is SubroutineExecutionLogStart -> logger.fireLogInfo(render(logEntry))
                is CallExecutionLogEntry -> logger.fireLogInfo(render(logEntry))
                is FindProgramLogEntry -> logger.fireLogInfo(render(logEntry))
                is RpgProgramFinderLogEntry -> logger.fireLogInfo(render(logEntry))
            }
        }
    }
}

class ParsingLogHandler(level: LogLevel, sep: String) : LogHandler(level, sep), InterpreterLogHandler {
    private val logger = LogManager.getLogger(PARSING_LOGGER)

    override fun render(logEntry: LogEntry): String {
        val fileName = extractFilename(logEntry.programName)
        return logEntry.renderPerformance("PARS", fileName, this.sep)
    }

    override fun handle(logEntry: LogEntry) {

        if (logger.checkChannelLoggingEnabled()) {
            when (logEntry) {
                is RpgLoadLogEnd -> logger.fireLogInfo(render(logEntry))
                is PreprocessingLogEnd -> logger.fireLogInfo(render(logEntry))
                is LexerLogEnd -> logger.fireLogInfo(render(logEntry))
                is ParserLogEnd -> logger.fireLogInfo(render(logEntry))
                is RContextLogEnd -> logger.fireLogInfo(render(logEntry))
                is CheckParseTreeLogEnd -> logger.fireLogInfo(render(logEntry))
                is FindMutesLogEnd -> logger.fireLogInfo(render(logEntry))
                is AstLogEnd -> logger.fireLogInfo(render(logEntry))
            }
        }
    }
}

class ErrorLogHandler(level: LogLevel, sep: String) : LogHandler(level, sep), InterpreterLogHandler {
    private val logger = LogManager.getLogger(ERROR_LOGGER)

    override fun render(logEntry: LogEntry): String {
        val fileName = extractFilename(logEntry.programName)
        return logEntry.renderErrorEvent("ERR", fileName, this.sep)
    }

    override fun handle(logEntry: LogEntry) {

        if (logger.checkChannelLoggingEnabled()) {
            when (logEntry) {
                is ErrorEventLogEntry -> logger.fireLogInfo(render(logEntry))
            }
        }
    }
}

private fun Logger.fireLogInfo(message: String) {
    val channel = this.name
    MainExecutionContext.getConfiguration().jarikoCallback.logInfo?.let {
        it.invoke(channel, message)
        true
    } ?: this.info(message)
}

private fun Logger.checkChannelLoggingEnabled(): Boolean {
    return MainExecutionContext.getConfiguration().jarikoCallback.channelLoggingEnabled?.invoke(this.name) ?: this.isInfoEnabled
}
