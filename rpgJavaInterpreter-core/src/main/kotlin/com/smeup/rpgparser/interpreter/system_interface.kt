package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.logging.configureLog
import com.smeup.rpgparser.logging.defaultLoggingConfiguration
import com.smeup.rpgparser.logging.loadLogConfiguration
import com.smeup.rpgparser.parsing.ast.MuteAnnotationExecuted
import java.io.File
import java.util.*

typealias LoggingConfiguration = Properties

/**
 * This represent the interface to the external world.
 * Printing, accessing databases, all sort of interactions should go through this interface.
 */
interface SystemInterface {
    fun display(value: String)
    fun findProgram(name: String): Program?
    fun findFunction(globalSymbolTable: SymbolTable, name: String): Function?
    fun loggingConfiguration(): LoggingConfiguration?
    fun addExtraLogHandlers(logHandlers: List<InterpreterLogHandler>): SystemInterface {
        extraLogHandlers.addAll(logHandlers)
        return this
    }

    val extraLogHandlers: MutableList<InterpreterLogHandler>
    val executedAnnotationInternal: HashMap<Int, MuteAnnotationExecuted>
    val db: DBInterface

    fun getAllLogHandlers() = (configureLog(this.loggingConfiguration() ?: defaultLoggingConfiguration()) + this.extraLogHandlers).toMutableList()
    fun getExecutedAnnotation(): HashMap<Int, MuteAnnotationExecuted>
    fun addExecutedAnnotation(line: Int, annotation: MuteAnnotationExecuted)
}

interface DBInterface {
    fun metadataOf(name: String): FileMetadata?
    fun open(name: String): DBFile?
}

data class Field(val name: String, val value: Value)

class Record(vararg fields: Field) : ArrayList<Field>(fields.asList()) {
    private val fieldMap by lazy {
        fields.associate { it.name to it.value }
    }
    fun matches(keyFields: List<Field>): Boolean {
        return keyFields.all { fieldMap[it.name] == it.value }
    }
}

interface DBFile {
    fun chain(key: Value): Record
    fun chain(keys: List<Field>): Record
    fun readEqual(): Record
    fun readEqual(key: Value): Record
    fun readEqual(keys: List<Field>): Record
    fun eof(): Boolean
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

class SimpleSystemInterface(var loggingConfiguration: LoggingConfiguration? = null) : SystemInterface {
    override var executedAnnotationInternal: HashMap<Int, MuteAnnotationExecuted> = HashMap<Int, MuteAnnotationExecuted>()
    override var extraLogHandlers: MutableList<InterpreterLogHandler> = mutableListOf()

    override fun loggingConfiguration(): LoggingConfiguration? = this.loggingConfiguration

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
