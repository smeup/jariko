package com.smeup.rpgparser.logging

import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.ast.*
import com.strumenta.kolasu.model.Node
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.Appender
import org.apache.logging.log4j.core.LoggerContext
import org.apache.logging.log4j.kotlin.Logging
import java.io.File
import java.io.FileInputStream
import java.util.*

const val DATA_LOGGER: String = "data"
const val LOOP_LOGGER: String = "loop"
const val STATEMENT_LOGGER: String = "statement"
const val EXPRESSION_LOGGER: String = "expression"
const val PERFOMANCE_LOGGER: String = "performance"
const val RESOLUTUION_LOGGER: String = "resolution"

abstract class LogHandler(val level: String, val sep: String) {
    fun extractFilename(name: String): String {
        val fullName = name.substringAfterLast("/")
        val fileName = fullName.substringBeforeLast(".")
        val extension = fullName.substringAfterLast(".")

        return "$fileName.$extension"
    }

    open fun render(logEntry: LogEntry): String {
        return "NOT IMPLEMENTED"
    }
}

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

/**
 * Read the configuration file, configure the logger and return a
 * list of log handlers
 */

fun configureLog(configFile: File): List<InterpreterLogHandler> {
    // Load the logging config file
    val properties = Properties()
    val inputStream = FileInputStream(configFile)
    properties.load(inputStream)

    return configureLog(properties)
}

fun defaultLoggingConfiguration() : LoggingConfiguration {
    val props = Properties()
    return props
}

/**
 * Read the configuration file, configure the logger and return a
 * list of log handlers
 */

fun configureLog(config: LoggingConfiguration): List<InterpreterLogHandler> {
    val names = listOf(LOOP_LOGGER, EXPRESSION_LOGGER, STATEMENT_LOGGER, DATA_LOGGER, PERFOMANCE_LOGGER, RESOLUTUION_LOGGER)
    val handlers: MutableList<InterpreterLogHandler> = mutableListOf()
    val ctx = LogManager.getContext(false) as LoggerContext

    try {

        val dataSeparator = config.getProperty("logger.data.separator")
        // TODO error

        names.forEach {
            val logLevel = config.getProperty("$it.level")
            if (logLevel != "off") {

                if (it == DATA_LOGGER) {
                    configureLogChannel(ctx, it, config)
                    handlers.add(DataLogHandler(logLevel, dataSeparator))
                }

                if (it == LOOP_LOGGER) {
                    configureLogChannel(ctx, it, config)
                    handlers.add(LoopLogHandler(logLevel, dataSeparator))
                }

                if (it == EXPRESSION_LOGGER) {
                    configureLogChannel(ctx, it, config)
                    handlers.add(ExpressionLogHandler(logLevel, dataSeparator))
                }

                if (it == STATEMENT_LOGGER) {
                    configureLogChannel(ctx, it, config)
                    handlers.add(StatementLogHandler(logLevel, dataSeparator))
                }

                if (it == PERFOMANCE_LOGGER) {
                    configureLogChannel(ctx, it, config)
                    handlers.add(PerformanceLogHandler(logLevel, dataSeparator))
                }

                if (it == RESOLUTUION_LOGGER) {
                    configureLogChannel(ctx, it, config)
                    handlers.add(ResolutionLogHandler(logLevel, dataSeparator))
                }
            }
        }
    } catch (e: Exception) {
        println("Configuration WARNING: ${e.message!!}")
    }

    return handlers
}

fun configureLogChannel(ctx: LoggerContext, channel: String, properties: Properties) {
    val cfg = ctx.configuration.getLoggerConfig(channel)
    // Check if the configuration is available in the default config file

    if (cfg.name == channel) {
        val level = properties.getProperty("$channel.level")
        val output = properties.getProperty("$channel.output")

        cfg.level = Level.getLevel(level.toUpperCase())

        if (output == "console") {

//
//            val builder = ConfigurationBuilderFactory.newConfigurationBuilder()
//
//            builder.setStatusLevel(Level.getLevel(level.toUpperCase()))
//            builder.setConfigurationName(channel)
//
        }

        if (output == "file") {
            cfg.removeAppender("STDOUT")
            var file = ctx.configuration.appenders["file"] as Appender

            cfg.addAppender(file, Level.getLevel(level.toUpperCase()), null)
        }
        ctx.updateLoggers()
    }
}

class Logger {

    companion object : Logging {

        /**
         * Read the configuration file, configure the logger and return a
         * list of messages/errors
         */
        fun configure(configFilePath: String): List<String> {
            val messages: MutableList<String> = mutableListOf()

            try {
                // Load the logging config file
                val properties = Properties()
                val inputStream = FileInputStream(configFilePath)
                properties.load(inputStream)

                // Detects the log file(s) path
                var logFilesPath = properties.getProperty("logger.file.path")
                if (logFilesPath != null) {
                    messages.add("Configuration: log files path set to: $logFilesPath")
                } else {
                    logFilesPath = ""
                    messages.add("Configuration WARNING: logger.file.path not set, log files path set to: .")
                }

                // Logger configuration
                val ctx = LogManager.getContext(false) as LoggerContext
                messages.addAll(configureLevel(ctx, "data", properties, logFilesPath))
                messages.addAll(configureLevel(ctx, "loop", properties, logFilesPath))
                messages.addAll(configureLevel(ctx, "expression", properties, logFilesPath))
                messages.addAll(configureLevel(ctx, "statement", properties, logFilesPath))
                messages.addAll(configureLevel(ctx, "performance", properties, logFilesPath))
                messages.addAll(configureLevel(ctx, "resolution", properties, logFilesPath))
            } catch (e: Exception) {
                messages.add("Configuration WARNING: ${e.message!!}")
            }
            return messages
        }

        private fun configureLevel(ctx: LoggerContext, channel: String, properties: Properties, path: String): List<String> {
            val messages: MutableList<String> = mutableListOf()

            val rootCfg = ctx.configuration.getLoggerConfig("")
            val cfg = ctx.configuration.getLoggerConfig(channel)
            // Check if the configuration is available in the default config file
            if (cfg.name == channel) {
                val level = properties.getProperty("$channel.level")
                val output = properties.getProperty("$channel.output")

                cfg.level = Level.getLevel(level.toUpperCase())

                if (output == "file") {
                    cfg.removeAppender("STDOUT")
                    var file = ctx.configuration.appenders["file"] as Appender

                    cfg.addAppender(file, Level.getLevel(level.toUpperCase()), null)
                }
            } else {
                messages.add("Configuration WARNING: channel '$channel' configuration not found")
            }
            ctx.updateLoggers()
            return messages
        }

        private fun extractFilename(name: String): String {
            val fullName = name.substringAfterLast("/")
            val fileName = fullName.substringBeforeLast(".")
            val extension = fullName.substringAfterLast(".")

            return "$fileName.$extension"
        }

        fun expressionDump(name: String, left: Expression, right: Expression, operator: String, result: Value) {
            val expressionLogger = LogManager.getLogger("expression")

            val fullName = name.substringAfterLast("/")
            val fileName = fullName.substringBeforeLast(".")
            val extension = fullName.substringAfterLast(".")
            val fullname = "$fileName.$extension"

            expressionLogger.info("$fullname ${left.position!!.start.line} EXPR ${left.render()} $operator ${right.render()} -> ${result.asBoolean().value}")
        }

        fun statementDump(name: String, statement: Node, duration: Long = -1, result: Value? = null) {

            val statementLogger = LogManager.getLogger("statement")
            val performanceLogger = LogManager.getLogger("performance")

            val fileName = extractFilename(name)

            if (statementLogger.isInfoEnabled) {
                when (statement) {
                    is SelectCase -> {

                        statementLogger.info("$fileName ${statement.position!!.start.line} STMT SELECT WHEN ${statement.condition.render()} -> ${result!!.asBoolean().value}")
                    }
                    is SelectOtherClause -> statementLogger.info("$fileName ${statement.position!!.start.line} STMT SELECT OTHER ")
                    is ExecuteSubroutine -> statementLogger.info("$fileName ${statement.position!!.start.line} STMT SUBROUTINE ${statement.subroutine!!.name}")
                    is EvalStmt -> {
                        statementLogger.info("$fileName ${statement.position!!.start.line} STMT EVAL ${statement.target.render()} ${statement.operator.text} ${statement.expression.render()} -> ${result!!.render()}")
                    }
                    is ForStmt -> {
                        if (duration < 0) {
                            statementLogger.info("$fileName ${statement.position!!.start.line} STMT FOR ${statement.init.render()} TO ${statement.endValue.render()}")
                        }
                    }
                }
            }

            if (performanceLogger.isInfoEnabled) {
                if (duration >= 0) {

                    when (statement) {
                        is ExecuteSubroutine -> performanceLogger.info("$fileName ${statement.position!!.start.line} PERF FOR SUBROUTINE ${statement.subroutine!!.name} $duration ms")
                        is ForStmt -> performanceLogger.info("$fileName ${statement.position!!.start.line} PERF FOR LOOP $duration ms")
                    }
                }
            }
        }

        fun assignDump(name: String, variable: AbstractDataDefinition, value: Value) {
            val dataLogger = LogManager.getLogger("data")
            val fileName = extractFilename(name)
            if (dataLogger.isInfoEnabled) {
                dataLogger.info("$fileName    DATA ${variable.name} = ${value.render()}")
            }
        }
    }
}
