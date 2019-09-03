package com.smeup.rpgparser.logging

import com.smeup.rpgparser.interpreter.InterpreterLogHandler
import com.smeup.rpgparser.interpreter.LogEntry
import com.smeup.rpgparser.interpreter.LoggingConfiguration
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.Appender
import org.apache.logging.log4j.core.LoggerContext
import java.io.File
import java.io.FileInputStream
import java.util.*

const val DATA_LOGGER: String = "data"
const val LOOP_LOGGER: String = "loop"
const val STATEMENT_LOGGER: String = "statement"
const val EXPRESSION_LOGGER: String = "expression"
const val PERFOMANCE_LOGGER: String = "performance"
const val RESOLUTUION_LOGGER: String = "resolution"

abstract class LogHandler(val level: LogLevel, val sep: String) {
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

fun loadLogConfiguration(configFile: File) : LoggingConfiguration {
    // Load the logging config file
    val properties = Properties()
    val inputStream = FileInputStream(configFile)
    properties.load(inputStream)
    return properties
}

/**
 * Read the configuration file, configure the logger and return a
 * list of log handlers
 */

fun configureLog(configFile: File): List<InterpreterLogHandler> {
    return configureLog(loadLogConfiguration(configFile))
}

fun defaultLoggingConfiguration(): LoggingConfiguration {
    val props = Properties()
    return props
}

enum class LogLevel {
    OFF,
    ALL;
    companion object {
        fun find(name: String) : LogLevel? {
            return values().find { it.name.toLowerCase() == name.toLowerCase() }
        }
    }
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
            val logLevelStr = config.getProperty("$it.level") ?: LogLevel.OFF.name
            val logLevel = LogLevel.find(logLevelStr) ?: {
                System.err.println("Unknown log level: $logLevelStr, for channel $it")
               LogLevel.OFF
            }()
            if (logLevel != LogLevel.OFF) {
                when (it) {
                    DATA_LOGGER -> {
                        configureLogChannel(ctx, it, config)
                        handlers.add(DataLogHandler(logLevel, dataSeparator))
                    }
                    LOOP_LOGGER -> {
                        configureLogChannel(ctx, it, config)
                        handlers.add(LoopLogHandler(logLevel, dataSeparator))
                    }
                    EXPRESSION_LOGGER -> {
                        configureLogChannel(ctx, it, config)
                        handlers.add(ExpressionLogHandler(logLevel, dataSeparator))
                    }
                    STATEMENT_LOGGER -> {
                        configureLogChannel(ctx, it, config)
                        handlers.add(StatementLogHandler(logLevel, dataSeparator))
                    }
                    PERFOMANCE_LOGGER -> {
                        configureLogChannel(ctx, it, config)
                        handlers.add(PerformanceLogHandler(logLevel, dataSeparator))
                    }
                    RESOLUTUION_LOGGER -> {
                        configureLogChannel(ctx, it, config)
                        handlers.add(ResolutionLogHandler(logLevel, dataSeparator))
                    }
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
