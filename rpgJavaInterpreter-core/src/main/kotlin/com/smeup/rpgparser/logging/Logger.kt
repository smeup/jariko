package com.smeup.rpgparser.logging

import com.smeup.rpgparser.interpreter.AbstractDataDefinition
import com.smeup.rpgparser.interpreter.Value
import com.smeup.rpgparser.parsing.ast.*
import com.strumenta.kolasu.model.Node
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.Appender
import org.apache.logging.log4j.core.LoggerContext
import org.apache.logging.log4j.kotlin.Logging
import java.io.FileInputStream
import java.util.*

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