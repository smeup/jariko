package com.smeup.rpgparser.logging

import com.smeup.rpgparser.interpreter.InterpreterLogHandler
import com.smeup.rpgparser.interpreter.LogEntry
import com.smeup.rpgparser.interpreter.LoggingConfiguration
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.Appender
import org.apache.logging.log4j.core.LoggerContext
import org.apache.logging.log4j.core.appender.ConsoleAppender
import org.apache.logging.log4j.core.appender.FileAppender
import org.apache.logging.log4j.core.config.AppenderRef
import org.apache.logging.log4j.core.config.Configuration
import org.apache.logging.log4j.core.config.LoggerConfig
import org.apache.logging.log4j.core.layout.PatternLayout
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.util.*

enum class LogChannel {
    DATA,
    LOOP,
    STATEMENT,
    EXPRESSION,
    PERFORMANCE,
    RESOLUTION,
    PARSING,
    ERROR;

    fun getPropertyName() = this.name.lowercase()
}

abstract class LogHandler(val level: LogLevel, val sep: String) {
    // as this method is for registration only, I think it is incorrect to extract the extension as well
    fun extractFilename(name: String): String {
        return name.replace('\\', '/').substringAfterLast("/").substringBeforeLast(".")
    }

    open fun render(logEntry: LogEntry): String {
        return "NOT IMPLEMENTED"
    }
}

fun loadLogConfiguration(configFile: File): LoggingConfiguration {
    // Load the logging config file
    val properties = Properties()
    val inputStream = FileInputStream(configFile)
    properties.load(InputStreamReader(inputStream, Charset.defaultCharset()))
    return properties
}

/**
 * Read the configuration file, configure the logger and return a
 * list of log handlers
 */

fun configureLog(configFile: File): List<InterpreterLogHandler> {
    return configureLog(loadLogConfiguration(configFile))
}

fun defaultLoggingConfiguration(): LoggingConfiguration = LoggingConfiguration()

enum class LogLevel {
    OFF,
    ALL;
    companion object {
        fun find(name: String): LogLevel? {
            return values().find { it.name.lowercase() == name.lowercase() }
        }
    }
}

fun configureLog(config: LoggingConfiguration): List<InterpreterLogHandler> {
    val ctx: LoggerContext by lazy {
        LogManager.getContext(false) as LoggerContext
    }

    val dataSeparator = config.getProperty("logger.data.separator")
    try {
        val handlers = LogChannel.values().mapNotNull {
            val logLevelStr = config.getProperty("${it.getPropertyName()}.level") ?: LogLevel.OFF.name
            val logLevel = LogLevel.find(logLevelStr) ?: run {
                System.err.println("Unknown log level: $logLevelStr, for channel $it")
                LogLevel.OFF
            }

            return@mapNotNull if (logLevel != LogLevel.OFF) {
                configureLogChannel(ctx, it, config)
                LogHandlerFactory.produce(it, logLevel, dataSeparator)
            } else null
        }
        return handlers
    } catch (e: Exception) {
        println("Configuration WARNING: ${e.message!!}")
    }

    return emptyList()
}

/**
 * Create a File Appender programmatically
 */
fun createFileAppender(name: String, config: Configuration, properties: Properties): Appender? {
    /* already created ?*/
    if (config.appenders[name] != null) {
        return config.appenders[name]
    }

    val pattern = if (properties.getProperty("logger.date.pattern") != null) properties.getProperty("logger.date.pattern") else "HH:mm:ss.SSS"
    val layout = PatternLayout.newBuilder()
            .withConfiguration(config)
            .withPattern("%d{$pattern} %msg%n")
            .build()

    var filepath = if (properties.getProperty("logger.file.path") != null) properties.getProperty("logger.file.path") else "."

    if (!File(filepath).exists()) {
        System.err.println("logger.file.path : $filepath, does not exists using default path .")
        filepath = "."
    }

    val filename = if (properties.getProperty("logger.file.name") != null) properties.getProperty("logger.file.name") else "log.log"
    val builder = FileAppender::class.java.getMethod("newBuilder").invoke(null) as FileAppender.Builder<*>
    val appender = builder.apply {
            setName(name)
            withFileName("$filepath/$filename")
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

fun createConsoleAppender(name: String, config: Configuration, properties: Properties): Appender? {
    /* already created ?*/
    if (config.appenders[name] != null) {
        return config.appenders[name]
    }
    val pattern = if (properties.getProperty("logger.date.pattern") != null) properties.getProperty("logger.date.pattern") else "HH:mm:ss.SSS"

    val layout = PatternLayout.newBuilder()
            .withConfiguration(config)
            .withPattern("%d{$pattern} %msg%n")
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

fun configureLogChannel(ctx: LoggerContext, channel: LogChannel, properties: Properties) {
    val channelName = channel.getPropertyName()
    val level = properties.getProperty("$channelName.level")
    when (val output = properties.getProperty("$channelName.output")) {
        "console" -> {
            // Creates and add the logger
            val console = createConsoleAppender("console", ctx.configuration, properties)
            val ref = AppenderRef.createAppenderRef("console", null, null)
            val refs = arrayOf(ref)

            val loggerConfig = LoggerConfig
                    .createLogger(false, Level.getLevel(level.uppercase()), channelName, "true", refs, null, ctx.configuration, null)

            loggerConfig.addAppender(console, null, null)
            ctx.configuration.addLogger(channelName, loggerConfig)
        }
        "file" -> {
            // Creates and add the logger
            val file = createFileAppender("file", ctx.configuration, properties)
            val ref = AppenderRef.createAppenderRef("file", null, null)
            val refs = arrayOf(ref)

            val loggerConfig = LoggerConfig
                    .createLogger(false, Level.getLevel(level.uppercase()), channelName, "true", refs, null, ctx.configuration, null)

            loggerConfig.addAppender(file, null, null)
            ctx.configuration.addLogger(channelName, loggerConfig)
        }
        else -> throw RuntimeException("Unknown log output value: $output")
    }
    ctx.updateLoggers()
}

private fun loggingConfiguration(output: String, vararg types: LogChannel): LoggingConfiguration {
    val configuration = LoggingConfiguration()
    configuration.setProperty("logger.data.separator", "\t")
    for (t in types) {
        val channelName = t.getPropertyName()
        configuration.setProperty("$channelName.level", "all")
        configuration.setProperty("$channelName.output", output)
    }
    return configuration
}

fun consoleLoggingConfiguration(vararg types: LogChannel): LoggingConfiguration {
    return loggingConfiguration("console", *types)
}

fun fileLoggingConfiguration(file: File, vararg types: LogChannel): LoggingConfiguration {
    val configuration = loggingConfiguration("file", *types)
    configuration.setProperty("logger.file.path", file.parent)
    configuration.setProperty("logger.file.name", file.name)
    return configuration
}
