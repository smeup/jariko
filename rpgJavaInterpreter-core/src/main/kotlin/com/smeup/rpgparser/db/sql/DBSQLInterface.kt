package com.smeup.rpgparser.db.sql

import com.smeup.rpgparser.interpreter.AbstractDataDefinition
import com.smeup.rpgparser.interpreter.DBInterface
import com.smeup.rpgparser.interpreter.FileMetadata
import com.smeup.rpgparser.interpreter.Value
import org.jetbrains.exposed.sql.Database

class DBSQLInterface(private val dbConfiguration: DBConfiguration) : DBInterface {
    val db by lazy {
        Database.connect(dbConfiguration.url, dbConfiguration.driver, dbConfiguration.user, dbConfiguration.password)
    }

    override fun metadataOf(name: String): FileMetadata? {
        TODO("not implemented")
    }

    override fun chain(name: String, key: Value): Collection<Pair<AbstractDataDefinition, Value>>? {
        TODO("not implemented")
    }
}

data class DBConfiguration(var url: String, val driver: String, val user: String = "", val password: String = "")