package com.smeup.rpgparser.video

import com.smeup.rpgparser.interpreter.IMemorySliceStorage
import com.smeup.rpgparser.interpreter.IStatementCounterStorage
import com.smeup.rpgparser.interpreter.MemorySliceId
import com.smeup.rpgparser.interpreter.RuntimeInterpreterSnapshot
import com.smeup.rpgparser.interpreter.StatementCounter
import com.smeup.rpgparser.interpreter.Value

class MemorySliceStorageMock : IMemorySliceStorage {

    private val storage = mutableMapOf<MemorySliceId, Map<String, Value>>()
    override var snapshot: RuntimeInterpreterSnapshot = RuntimeInterpreterSnapshot.blank()

    override fun open() {}

    override fun load(memorySliceId: MemorySliceId): Map<String, Value> {
        return this.storage[memorySliceId] ?: emptyMap()
    }

    override fun store(memorySliceId: MemorySliceId, values: Map<String, Value>) {
        this.storage[memorySliceId] = values
    }

    override fun beginTrans() {}

    override fun commitTrans() {}

    override fun rollbackTrans() {}

    override fun close() {}
}

class StatementCounterStorageMock : IStatementCounterStorage {
    private var statementCounter = StatementCounter()
    override var snapshot: RuntimeInterpreterSnapshot = RuntimeInterpreterSnapshot.blank()

    override fun load(): StatementCounter {
        return this.statementCounter
    }

    override fun store(statementCounter: StatementCounter) {
        this.statementCounter = statementCounter
    }

    override fun close() {}
}