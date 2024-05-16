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
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

/**
 * Log handler for the EXPR channel
 */
class ExpressionLogHandler(level: LogLevel, sep: String) : LogHandler(level, sep), InterpreterLogHandler {
    private val logger = LogManager.getLogger(LogChannel.EXPRESSION.getPropertyName())

    override fun render(renderer: LazyLogEntry): String {
        return buildString {
            append("EXPR")
            append(sep)
            append(renderer.render(sep, withHeader = true, withScope = false))
        }
    }

    override fun accepts(entry: LogEntry) = logger.checkChannelLoggingEnabled() && entry.scope == LogChannel.EXPRESSION.getPropertyName()
    override fun handle(renderer: LazyLogEntry) = logger.fireLogInfo(render(renderer))
}

/**
 * Log handler for the PERF channel
 */
class PerformanceLogHandler(level: LogLevel, sep: String) : LogHandler(level, sep), InterpreterLogHandler {
    private val logger = LogManager.getLogger(LogChannel.PERFORMANCE.getPropertyName())

    override fun render(renderer: LazyLogEntry): String {
        return buildString {
            append("PERF")
            append(sep)
            append(renderer.render(sep, withHeader = true, withScope = false))
        }
    }

    override fun accepts(entry: LogEntry) = logger.checkChannelLoggingEnabled() && entry.scope == LogChannel.PERFORMANCE.getPropertyName()
    override fun handle(renderer: LazyLogEntry) = logger.fireLogInfo(render(renderer))
}

/**
 * Log handler for the STMT channel
 */
class StatementLogHandler(level: LogLevel, sep: String) : LogHandler(level, sep), InterpreterLogHandler {
    private val logger = LogManager.getLogger(LogChannel.STATEMENT.getPropertyName())

    override fun render(renderer: LazyLogEntry): String {
        return buildString {
            append("STMT")
            append(sep)
            append(renderer.render(sep, withHeader = true, withScope = false))
        }
    }

    override fun accepts(entry: LogEntry) = logger.checkChannelLoggingEnabled() && entry.scope == LogChannel.STATEMENT.getPropertyName()
    override fun handle(renderer: LazyLogEntry) = logger.fireLogInfo(render(renderer))
}

/**
 * Log handler for the DATA channel
 */
class DataLogHandler(level: LogLevel, sep: String) : LogHandler(level, sep), InterpreterLogHandler {
    private val logger = LogManager.getLogger(LogChannel.DATA.getPropertyName())

    override fun render(renderer: LazyLogEntry): String {
        return buildString {
            append("DATA")
            append(sep)
            append(renderer.render(sep, withHeader = true, withScope = false))
        }
    }

    override fun accepts(entry: LogEntry) = logger.checkChannelLoggingEnabled() && entry.scope == LogChannel.DATA.getPropertyName()
    override fun handle(renderer: LazyLogEntry) = logger.fireLogInfo(render(renderer))
}

/**
 * Log handler for the LOOP channel
 */
class LoopLogHandler(level: LogLevel, sep: String) : LogHandler(level, sep), InterpreterLogHandler {
    private val logger = LogManager.getLogger(LogChannel.LOOP.getPropertyName())
    override fun render(renderer: LazyLogEntry): String {
        return buildString {
            append("LOOP")
            append(sep)
            append(renderer.render(sep, withHeader = true, withScope = false))
        }
    }

    override fun accepts(entry: LogEntry) = logger.checkChannelLoggingEnabled() && entry.scope == LogChannel.LOOP.getPropertyName()
    override fun handle(renderer: LazyLogEntry) = logger.fireLogInfo(render(renderer))
}

/**
 * Log handler for the RESL channel
 */
class ResolutionLogHandler(level: LogLevel, sep: String) : LogHandler(level, sep), InterpreterLogHandler {
    private val logger = LogManager.getLogger(LogChannel.RESOLUTION.getPropertyName())

    override fun render(renderer: LazyLogEntry): String {
        return buildString {
            append("RESL")
            append(sep)
            append(renderer.render(sep, withHeader = true, withScope = false))
        }
    }

    override fun accepts(entry: LogEntry) = logger.checkChannelLoggingEnabled() && entry.scope == LogChannel.RESOLUTION.getPropertyName()
    override fun handle(renderer: LazyLogEntry) = logger.fireLogInfo(render(renderer))
}

/**
 * Log handler for the PARS channel
 */
class ParsingLogHandler(level: LogLevel, sep: String) : LogHandler(level, sep), InterpreterLogHandler {
    private val logger = LogManager.getLogger(LogChannel.PARSING.getPropertyName())

    override fun render(renderer: LazyLogEntry): String {
        return buildString {
            append("PARS")
            append(sep)
            append(renderer.render(sep, withHeader = true, withScope = false))
        }
    }

    override fun accepts(entry: LogEntry) = logger.checkChannelLoggingEnabled() && entry.scope == LogChannel.PARSING.getPropertyName()
    override fun handle(renderer: LazyLogEntry) = logger.fireLogInfo(render(renderer))
}

/**
 * Log handler for the ERR channel
 */
class ErrorLogHandler(level: LogLevel, sep: String) : LogHandler(level, sep), InterpreterLogHandler {
    private val logger = LogManager.getLogger(LogChannel.ERROR.getPropertyName())

    override fun render(renderer: LazyLogEntry): String {
        return buildString {
            append("ERR")
            append(sep)
            append(renderer.render(sep, withHeader = true, withScope = false))
        }
    }

    override fun accepts(entry: LogEntry) = logger.checkChannelLoggingEnabled() && entry.scope == LogChannel.ERROR.getPropertyName()
    override fun handle(renderer: LazyLogEntry) = logger.fireLogInfo(render(renderer))
}

/**
 * Log handler for the ANALYTICS channel
 */
class AnalyticsLogHandler(level: LogLevel, sep: String) : LogHandler(level, sep), InterpreterLogHandler {
    private val logger = LogManager.getLogger(LogChannel.ANALYTICS.getPropertyName())

    override fun render(renderer: LazyLogEntry): String {
        return buildString {
            append("ANALYTICS")
            append(sep)
            append(renderer.render(sep, withHeader = true, withScope = false))
        }
    }

    override fun accepts(entry: LogEntry) = logger.checkChannelLoggingEnabled() && entry.scope == LogChannel.ANALYTICS.getPropertyName()
    override fun handle(renderer: LazyLogEntry) = logger.fireLogInfo(render(renderer))
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
