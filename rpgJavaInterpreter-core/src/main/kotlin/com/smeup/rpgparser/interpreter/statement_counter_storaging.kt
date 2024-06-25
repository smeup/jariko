package com.smeup.rpgparser.interpreter

interface IStatementCounterStorage : AutoCloseable {
    var snapshot: RuntimeInterpreterSnapshot
    fun load(): StatementCounter
    fun store(statementCounter: StatementCounter)
}

class StatementCounterMgr(private val storage: IStatementCounterStorage) {
    fun load(): StatementCounter = storage.load()
    fun store(statementCounter: StatementCounter) = storage.store(statementCounter)
}