package com.smeup.rpgparser.logging

import com.smeup.rpgparser.interpreter.InterpreterLogHandler

object LogHandlerFactory {
    fun produce(channel: LogChannel, level: LogLevel, separator: String): InterpreterLogHandler = when (channel) {
        LogChannel.DATA -> DataLogHandler(level, separator)
        LogChannel.LOOP -> LoopLogHandler(level, separator)
        LogChannel.STATEMENT -> StatementLogHandler(level, separator)
        LogChannel.EXPRESSION -> ExpressionLogHandler(level, separator)
        LogChannel.PERFORMANCE -> PerformanceLogHandler(level, separator)
        LogChannel.RESOLUTION -> ResolutionLogHandler(level, separator)
        LogChannel.PARSING -> ParsingLogHandler(level, separator)
        LogChannel.ERROR -> ErrorLogHandler(level, separator)
        LogChannel.ANALYTICS -> AnalyticsLogHandler(level, separator)
    }
}
