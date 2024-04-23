package com.smeup.rpgparser.logging

import com.smeup.rpgparser.interpreter.InterpreterLogHandler

/**
 * A factory providing the different types of LogHandler by its corresponding LogChannel
 * @see LogChannel
 * @see LogHandler
 */
object LogHandlerFactory {

    /**
     * Get a new instance of InterpreterLogHandler by its corresponding LogChannel
     * @return A new InterpreterLogHandler
     * @see LogChannel
     * @see InterpreterLogHandler
     */
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
