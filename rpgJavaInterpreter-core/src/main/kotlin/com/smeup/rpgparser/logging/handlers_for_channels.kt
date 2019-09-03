package com.smeup.rpgparser.logging

import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.ast.LogicalAndExpr
import com.smeup.rpgparser.parsing.ast.LogicalOrExpr
import org.apache.logging.log4j.LogManager

class ExpressionLogHandler(level: String, sep: String) : LogHandler(level, sep), InterpreterLogHandler {
    val logger = LogManager.getLogger(EXPRESSION_LOGGER)

    override fun render(logEntry: LogEntry): String {
        val fileName = extractFilename(logEntry.programName)
        return logEntry.renderExpression("EXPR", fileName, this.sep)
    }
    override fun handle(logEntry: LogEntry) {
        if (logger.isInfoEnabled) {
            when (logEntry) {
                is ExpressionEvaluationLogEntry -> {
                    // Avoid expression
                    if (logEntry.expression.parent !is LogicalAndExpr && logEntry.expression.parent !is LogicalOrExpr) {
                        logger.info(render(logEntry))
                    }
                }
            }
        }
    }
}

class PerformanceLogHandler(level: String, sep: String) : LogHandler(level, sep), InterpreterLogHandler {
    private val logger = LogManager.getLogger(PERFOMANCE_LOGGER)

    override fun render(logEntry: LogEntry): String {
        val fileName = extractFilename(logEntry.programName)
        return logEntry.renderPerformance("PERF", fileName, this.sep)
    }

    override fun handle(logEntry: LogEntry) {

        if (logger.isInfoEnabled) {
            when (logEntry) {
                is SubroutineExecutionLogEnd -> logger.info(render(logEntry))
                is ForStatementExecutionLogEnd -> logger.info(render(logEntry))
                is DoStatemenExecutionLogEnd -> logger.info(render(logEntry))
                is DowStatemenExecutionLogEnd -> logger.info(render(logEntry))
                is CallEndLogEntry -> logger.info(render(logEntry))
                is EndProgramLog -> logger.info(render(logEntry))
            }
        }
    }
}

class StatementLogHandler(level: String, sep: String) : LogHandler(level, sep), InterpreterLogHandler {
    val logger = LogManager.getLogger(STATEMENT_LOGGER)
    var inLoop: Int = 0

    override fun render(logEntry: LogEntry): String {
        val fileName = extractFilename(logEntry.programName)
        return logEntry.renderStatement("STMT", fileName, this.sep)
    }

    fun renderParamList(logEntry: LogEntry): List<String> {
        val fileName = extractFilename(logEntry.programName)
        return logEntry.renderParamList("STMT", fileName, this.sep)
    }

    override fun handle(logEntry: LogEntry) {

        if (logger.isInfoEnabled) {
            when (logEntry) {
                is ParamListStatemenExecutionLog -> {
                    val params = renderParamList(logEntry)
                    params.forEach {
                        logger.info(it)
                    }
                }
                is SelectCaseExecutionLogEntry -> logger.info(render(logEntry))
                is SelectOtherExecutionLogEntry -> logger.info(render(logEntry))
                is SubroutineExecutionLogStart -> logger.info(render(logEntry))
                is SubroutineExecutionLogEnd -> logger.info(render(logEntry))
                is IfExecutionLogEntry -> logger.info(render(logEntry))
                is ElseIfExecutionLogEntry -> logger.info(render(logEntry))
                is ClearStatemenExecutionLog -> logger.info(render(logEntry))
                is MoveStatemenExecutionLog -> logger.info(render(logEntry))
                is LeaveStatemenExecutionLog -> logger.info(render(logEntry))
                is IterStatemenExecutionLog -> logger.info(render(logEntry))
                is ElseExecutionLogEntry -> logger.info(render(logEntry))
                is CallExecutionLogEntry -> logger.info(render(logEntry))
                is CallEndLogEntry -> logger.info(render(logEntry))
                is EvaluationLogEntry -> logger.info(render(logEntry))
                is ForStatementExecutionLogStart -> {
                    logger.info(render(logEntry))
                    this.inLoop++
                }
                is ForStatementExecutionLogEnd -> {
                    logger.info(render(logEntry))
                    this.inLoop--
                }
                is DoStatemenExecutionLogStart -> {
                    logger.info(render(logEntry))
                    this.inLoop++
                }
                is DoStatemenExecutionLogEnd -> {
                    logger.info(render(logEntry))
                    this.inLoop--
                }
                is DowStatemenExecutionLogStart -> {
                    logger.info(render(logEntry))
                    this.inLoop++
                }
                is DowStatemenExecutionLogEnd -> {
                    logger.info(render(logEntry))
                    this.inLoop--
                }
            }
        }
    }
}

class DataLogHandler(level: String, sep: String) : LogHandler(level, sep), InterpreterLogHandler {
    val logger = LogManager.getLogger(DATA_LOGGER)

    override fun render(logEntry: LogEntry): String {
        val fileName = extractFilename(logEntry.programName)
        return logEntry.renderData("DATA", fileName, this.sep)
    }

    override fun handle(logEntry: LogEntry) {
        if (logger.isInfoEnabled) {
            when (logEntry) {
                is AssignmentLogEntry -> logger.info(render(logEntry))
            }
        }
    }
}

class LoopLogHandler(level: String, sep: String) : LogHandler(level, sep), InterpreterLogHandler {
    val logger = LogManager.getLogger(LOOP_LOGGER)

    override fun render(logEntry: LogEntry): String {
        val fileName = extractFilename(logEntry.programName)
        return logEntry.renderLoop("LOOP", fileName, this.sep)
    }

    override fun handle(logEntry: LogEntry) {
        if (logger.isInfoEnabled) {
            when (logEntry) {
                is ForStatementExecutionLogStart -> logger.info(render(logEntry))
                is ForStatementExecutionLogEnd -> logger.info(render(logEntry))
                is DoStatemenExecutionLogStart -> logger.info(render(logEntry))
                is DoStatemenExecutionLogEnd -> logger.info(render(logEntry))
                is DowStatemenExecutionLogStart -> logger.info(render(logEntry))
                is DowStatemenExecutionLogEnd -> logger.info(render(logEntry))
            }
        }
    }
}

class ResolutionLogHandler(level: String, sep: String) : LogHandler(level, sep), InterpreterLogHandler {
    val logger = LogManager.getLogger(RESOLUTUION_LOGGER)
    override fun handle(logEntry: LogEntry) {
    }
}
