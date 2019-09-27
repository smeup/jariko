package com.smeup.rpgparser.db.sql

import com.smeup.rpgparser.CollectorSystemInterface
import com.smeup.rpgparser.assertASTCanBeProduced
import com.smeup.rpgparser.execute
import com.smeup.rpgparser.interpreter.FileMetadata
import com.smeup.rpgparser.interpreter.StringValue
import com.smeup.rpgparser.parsing.parsetreetoast.resolve
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

fun outputOfDBPgm(programName: String, initialSQL: List<String>, inputParms: Map<String, StringValue> = mapOf(), printTree: Boolean = false): List<String> {
    val cu = assertASTCanBeProduced(programName, printTree = printTree)
    val dbInterface = connectionForTest()
    dbInterface.execute(initialSQL)
    cu.resolve(dbInterface)
    val si = CollectorSystemInterface()
    si.databaseInterface = dbInterface
    execute(cu, inputParms, si)
    return si.displayed
}