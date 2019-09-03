package com.smeup.rpgparser.interpreter

/**
 * This represent the interface to the external world.
 * Printing, accessing databases, all sort of interactions should go through this interface.
 */
interface SystemInterface {
    fun display(value: String)
    fun findProgram(name: String): Program?
    fun findFunction(globalSymbolTable: SymbolTable, name: String): Function?
    val db: DBInterface
}

interface DBInterface {
    fun metadataOf(name: String): FileMetadata?
    fun chain(name: String, key: Value): Collection<Pair<AbstractDataDefinition, Value>>?
}

data class FileMetadata(val tableName: String, val formatName: String, val fields: Collection<AbstractDataDefinition>)

object DummyDBInterface : DBInterface {
    override fun metadataOf(name: String): FileMetadata? = null
    override fun chain(name: String, key: Value): Collection<Pair<AbstractDataDefinition, Value>>? = null
}

object DummySystemInterface : SystemInterface {
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
