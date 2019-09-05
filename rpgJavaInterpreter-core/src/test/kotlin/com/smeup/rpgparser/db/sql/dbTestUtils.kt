package com.smeup.rpgparser.db.sql

import com.smeup.rpgparser.interpreter.FileMetadata
import java.util.concurrent.ThreadLocalRandom

// Using random DB name in order to have different dbs for each test
// TODO verify if this strategy could create performance issues
fun randomHsqlMemDB() =
        DBConfiguration("jdbc:hsqldb:mem:testmemdb" + ThreadLocalRandom.current().nextInt(1, 99_999), "SA")

fun connectionForTest(tables: List<FileMetadata> = emptyList()): DBSQLInterface {
    val db = DBSQLInterface(randomHsqlMemDB())
    db.setSQLLog(true)
    db.create(tables)
    return db
}