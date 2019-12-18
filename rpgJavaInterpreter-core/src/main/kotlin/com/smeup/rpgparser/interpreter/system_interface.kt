package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.logging.configureLog
import com.smeup.rpgparser.logging.defaultLoggingConfiguration
import com.smeup.rpgparser.logging.loadLogConfiguration
import com.smeup.rpgparser.parsing.ast.MuteAnnotationExecuted
import com.smeup.rpgparser.rgpinterop.RpgProgramFinder
import java.io.File
import java.io.PrintStream
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap

typealias LoggingConfiguration = Properties

fun consoleVerboseConfiguration(): LoggingConfiguration {
    val props = Properties()
    props.setProperty("logger.data.separator", "\t")
    props.setProperty("logger.date.pattern", "HH:mm:ss.SSS")
    props.setProperty("data.level", "all")
    props.setProperty("data.output", "console")
    props.setProperty("loop.level", "all")
    props.setProperty("loop.output", "console")

    props.setProperty("expression.level", "all")
    props.setProperty("expression.output", "console")

    props.setProperty("statement.level", "all")
    props.setProperty("statement.output", "console")

    props.setProperty("performance.level", "all")
    props.setProperty("performance.output", "console")

    props.setProperty("resolution.level", "all")
    props.setProperty("resolution.output", "console")
    return LoggingConfiguration(props)
}

/**
 * This represent the interface to the external world.
 * Printing, accessing databases, all sort of interactions should go through this interface.
 */
interface SystemInterface {
    fun display(value: String)
    fun findProgram(name: String): Program?
    fun findFunction(globalSymbolTable: SymbolTable, name: String): Function?

    val db: DBInterface

    fun loggingConfiguration(): LoggingConfiguration?
    fun addExtraLogHandlers(logHandlers: List<InterpreterLogHandler>): SystemInterface {
        extraLogHandlers.addAll(logHandlers)
        return this
    }
    val extraLogHandlers: MutableList<InterpreterLogHandler>
    fun getAllLogHandlers() = (configureLog(this.loggingConfiguration() ?: defaultLoggingConfiguration()) + this.extraLogHandlers).toMutableList()

    val executedAnnotationInternal: HashMap<Int, MuteAnnotationExecuted>
    fun getExecutedAnnotation(): HashMap<Int, MuteAnnotationExecuted>
    fun addExecutedAnnotation(line: Int, annotation: MuteAnnotationExecuted)
}

interface DBInterface {
    fun metadataOf(name: String): FileMetadata?
    fun open(name: String): DBFile?
}

data class RecordField(val name: String, val value: Value)

class Record(vararg fields: RecordField) : LinkedHashMap<String, Value>() {
    init {
        fields.forEach {
            add(it)
        }
    }

    fun matches(keyFields: List<RecordField>) = keyFields.all { this[it.name] == it.value }

    fun add(field: RecordField) {
        put(field.name, field.value)
    }
}

interface DBFile {
    fun chain(key: Value): Record
    fun chain(keys: List<RecordField>): Record
    fun setll(key: Value): Boolean
    fun setll(keys: List<RecordField>): Boolean
    fun readEqual(): Record
    fun readEqual(key: Value): Record
    fun readEqual(keys: List<RecordField>): Record
    fun readPrevious(): Record
    fun readPrevious(key: Value): Record
    fun readPrevious(keys: List<RecordField>): Record
    fun eof(): Boolean
    fun equal(): Boolean
    fun read(): Record
}

data class DBField(val name: String, val type: Type, val primaryKey: Boolean = false) {
    fun toDataDefinition() = DataDefinition(name, type)
}

data class FileMetadata(val tableName: String, val formatName: String, val fields: List<DBField>)

object DummyDBInterface : DBInterface {
    override fun metadataOf(name: String): FileMetadata? = null
    override fun open(name: String): DBFile? = null
}

object DummySystemInterface : SystemInterface {
    override var executedAnnotationInternal: HashMap<Int, MuteAnnotationExecuted> = HashMap<Int, MuteAnnotationExecuted>()
    override var extraLogHandlers: MutableList<InterpreterLogHandler> = mutableListOf()

    override fun loggingConfiguration(): LoggingConfiguration? = null

    override val db: DBInterface
        get() = DummyDBInterface

    override fun findFunction(globalSymbolTable: SymbolTable, name: String): Function? {
        return null
    }

    override fun findProgram(name: String): Program? {
        return null
    }

    override fun display(value: String) {
        // doing nothing
    }
    override fun addExecutedAnnotation(line: Int, annotation: MuteAnnotationExecuted) {
        executedAnnotationInternal[line] = annotation
    }

    override fun getExecutedAnnotation(): HashMap<Int, MuteAnnotationExecuted> {
        return executedAnnotationInternal
    }
}

class SimpleSystemInterface(var loggingConfiguration: LoggingConfiguration? = null, val programFinders: List<RpgProgramFinder> = emptyList(), val output: PrintStream? = null) : SystemInterface {
    override var executedAnnotationInternal: HashMap<Int, MuteAnnotationExecuted> = HashMap<Int, MuteAnnotationExecuted>()
    override var extraLogHandlers: MutableList<InterpreterLogHandler> = mutableListOf()

    override fun loggingConfiguration(): LoggingConfiguration? = this.loggingConfiguration

    override val db: DBInterface
        get() = DummyDBInterface

    override fun findFunction(globalSymbolTable: SymbolTable, name: String): Function? {
        return null
    }

    private val programs = java.util.HashMap<String, Program?>()

    override fun findProgram(name: String): Program? {
        programs.computeIfAbsent(name) {
            programFinders.asSequence().mapNotNull {
                it.findRpgProgram(name, db)
            }.firstOrNull()
        }
        return programs[name]
    }

    override fun display(value: String) {
        output?.println(value)
    }

    fun useConfigurationFile(configurationFile: File?): SystemInterface {
        if (configurationFile == null) {
            this.loggingConfiguration = defaultLoggingConfiguration()
        } else {
            this.loggingConfiguration = loadLogConfiguration(configurationFile)
        }
        return this
    }

    override fun addExecutedAnnotation(line: Int, annotation: MuteAnnotationExecuted) {
        executedAnnotationInternal[line] = annotation
    }

    override fun getExecutedAnnotation(): HashMap<Int, MuteAnnotationExecuted> {
        return executedAnnotationInternal
    }
}
