package com.smeup.rpgparser.interpreter

/**
 * This represent the interface to the external world.
 * Printing, accessing databases, all sort of interactions should go through this interface.
 */
interface SystemInterface {
    fun display(value: String)
    fun findProgram(name: String): Program?
    fun findFunction(globalSymbolTable: SymbolTable, name: String): Function?
    val db: DatabaseInterface
}

interface DatabaseInterface {
    abstract fun fieldsOf(name: String): Collection<AbstractDataDefinition>
}

object DummyDatabaseInterface : DatabaseInterface {
    override fun fieldsOf(name: String): Collection<AbstractDataDefinition> = emptyList()
}

object DummySystemInterface : SystemInterface {
    override val db: DatabaseInterface
        get() = DummyDatabaseInterface

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
