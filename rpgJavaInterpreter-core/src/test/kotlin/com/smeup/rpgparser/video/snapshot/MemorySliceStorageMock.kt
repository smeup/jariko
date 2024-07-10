package com.smeup.rpgparser.video.snapshot

import com.smeup.rpgparser.interpreter.IMemorySliceStorage
import com.smeup.rpgparser.interpreter.MemorySliceId
import com.smeup.rpgparser.interpreter.RuntimeInterpreterSnapshot
import com.smeup.rpgparser.interpreter.Value

internal class MemorySliceStorageMock : IMemorySliceStorage {
    var snapshot: RuntimeInterpreterSnapshot? = null
    private val storage = mutableMapOf<MemorySliceId, Map<String, Value>>()
    private var lastLoadedId: MemorySliceId? = null

    override fun open() {}

    override fun load(memorySliceId: MemorySliceId): Map<String, Value> {
        return this.storage[memorySliceId] ?: emptyMap()
    }

    override fun store(memorySliceId: MemorySliceId, values: Map<String, Value>) {
        this.lastLoadedId = memorySliceId
        this.storage[memorySliceId] = values
    }

    override fun beginTrans() {}

    override fun commitTrans() {}

    override fun rollbackTrans() {}

    override fun close() {}

    fun loadFromLastCalledProgram(): Map<String, Value> {
        // at this point CALL was already performed and such not NULL
        return this.load(this.lastLoadedId!!)
    }

    fun reset() {
        this.snapshot = null
        this.storage.clear()
    }
}