package com.smeup.rpgparser.video.snapshot

import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.interpreter.IMemorySliceStorage
import com.smeup.rpgparser.interpreter.MemorySliceId
import com.smeup.rpgparser.interpreter.RuntimeInterpreterSnapshot
import com.smeup.rpgparser.interpreter.Value

internal class MemorySliceStorageMock : IMemorySliceStorage {
    var snapshot: RuntimeInterpreterSnapshot? = null
    private val storage = mutableMapOf<MemorySliceId, Map<String, Value>>()

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

    fun reset() {
        this.snapshot = null
        this.storage.clear()
    }
}