package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.logging.configureLog
import com.smeup.rpgparser.logging.defaultLoggingConfiguration
import com.smeup.rpgparser.logging.loadLogConfiguration
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
    val db: DBInterface

    fun getAllLogHandlers() = (configureLog(this.loggingConfiguration() ?: defaultLoggingConfiguration()) + this.extraLogHandlers).toMutableList()
}

interface DBInterface {
    fun metadataOf(name: String): FileMetadata?
    fun open(name: String): DBFile?
}

interface DBFile {
    fun chain(key: Value): List<Pair<String, Value>>
    fun chain(keys: List<Pair<String, Value>>): List<Pair<String, Value>>
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
}

class SimpleSystemInterface(var loggingConfiguration: LoggingConfiguration? = null) : SystemInterface {
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
}
