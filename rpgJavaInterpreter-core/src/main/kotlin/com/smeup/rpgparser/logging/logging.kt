package com.smeup.rpgparser.logging

import com.smeup.rpgparser.interpreter.InterpreterLogHandler
import com.smeup.rpgparser.interpreter.LogEntry
import com.smeup.rpgparser.interpreter.LoggingConfiguration
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.Appender
import org.apache.logging.log4j.core.LoggerContext
import org.apache.logging.log4j.core.appender.ConsoleAppender
import org.apache.logging.log4j.core.config.Configuration
import java.io.File
import java.io.FileInputStream
import java.util.*
import org.apache.logging.log4j.core.layout.PatternLayout
import org.apache.logging.log4j.core.appender.FileAppender
import org.apache.logging.log4j.core.config.LoggerConfig
import org.apache.logging.log4j.core.config.AppenderRef







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

fun loadLogConfiguration(configFile: File): LoggingConfiguration {
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
        fun find(name: String): LogLevel? {
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

/**
 * Create a File Appender programmatically
 */
fun createFileAppender(name: String, config: Configuration, properties: Properties)  : Appender?  {

    /* already created ?*/
    if(config.appenders[name] != null) {
        return  config.appenders[name]
    }
    val pattern = if (properties.getProperty("logger.date.pattern") != null) properties.getProperty("logger.date.pattern") else "HH:mm:ss.SSS"
    val layout = PatternLayout.newBuilder()
            .withConfiguration(config)
            .withPattern("%d{${pattern}} %msg%n")
            .build()

    var filepath = if (properties.getProperty("logger.file.path") != null) properties.getProperty("logger.file.path") else "."

    if(!File(filepath).exists()) {
        System.err.println("logger.file.path : $filepath, does not exists using default path .")
        filepath = "."
    }

    val filename = if (properties.getProperty("logger.file.name") != null) properties.getProperty("logger.file.name") else "log.log"
    val builder = FileAppender::class.java.getMethod("newBuilder").invoke(null) as FileAppender.Builder<*>
    val appender = builder.apply {
            setName(name)
            withFileName("${filepath}/${filename}")
            setLayout(layout)
            setConfiguration(config)
    }.build()

    appender.start()
    config.addAppender(appender)
    return appender
}

/**
 * Create a console appender programmatically
 */

fun createConsoleAppender(name : String,config: Configuration, properties: Properties) : Appender? {
    /* already created ?*/
    if(config.appenders[name] != null) {
        return  config.appenders[name]
    }
    val pattern = if (properties.getProperty("logger.date.pattern") != null) properties.getProperty("logger.date.pattern") else "HH:mm:ss.SSS"

    val layout = PatternLayout.newBuilder()
            .withConfiguration(config)
            .withPattern("%d{${pattern}} %msg%n")
            .build()

    val builder = ConsoleAppender::class.java.getMethod("newBuilder").invoke(null) as ConsoleAppender.Builder<*>
        val appender = builder.apply {
        setName(name)
        setLayout(layout)
        setTarget(ConsoleAppender.Target.SYSTEM_OUT)
    }.build()

    appender.start()
    config.addAppender(appender)

    return appender
}


fun configureLogChannel(ctx: LoggerContext, channel: String, properties: Properties) {
    val level = properties.getProperty("$channel.level")
    val output = properties.getProperty("$channel.output")

    if (output == "console") {
        // Creates and add the logger
        val console = createConsoleAppender("console",ctx.configuration,properties);
        val ref = AppenderRef.createAppenderRef("console", null, null)
        val refs = arrayOf(ref)

        val loggerConfig = LoggerConfig
                .createLogger(false, Level.getLevel(level.toUpperCase()), channel, "true", refs, null, ctx.configuration, null)

        loggerConfig.addAppender(console, null, null);
        ctx.configuration.addLogger(channel, loggerConfig)
    }
    if (output == "file") {
        // Creates and add the logger
        val file = createFileAppender("file",ctx.configuration,properties);
        val ref = AppenderRef.createAppenderRef("file", null, null)
        val refs = arrayOf(ref)

        val loggerConfig = LoggerConfig
                .createLogger(false, Level.getLevel(level.toUpperCase()), channel, "true", refs, null, ctx.configuration, null)

        loggerConfig.addAppender(file, null, null);
        ctx.configuration.addLogger(channel, loggerConfig)
    }
    ctx.updateLoggers()
}
