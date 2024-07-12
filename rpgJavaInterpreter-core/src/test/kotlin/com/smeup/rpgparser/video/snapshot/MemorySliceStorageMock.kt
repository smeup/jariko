package com.smeup.rpgparser.video.snapshot

import com.smeup.rpgparser.interpreter.IMemorySliceStorage
import com.smeup.rpgparser.interpreter.MemorySliceId
import com.smeup.rpgparser.interpreter.Value

internal class MemorySliceStorageMock : IMemorySliceStorage {
    private var snapshot: Snapshot? = null

    fun use(snapshot: Snapshot) {
        this.snapshot = snapshot
    }

    override fun open() {}

    override fun load(memorySliceId: MemorySliceId): Map<String, Value> {
        val values = this.snapshot?.symbolTables?.get(memorySliceId)
        if (values == null) return emptyMap()
        else return values
    }

    override fun store(memorySliceId: MemorySliceId, values: Map<String, Value>) {
        this.snapshot!!.symbolTables[memorySliceId] = values
    }

    override fun beginTrans() {}

    override fun commitTrans() {}

    override fun rollbackTrans() {}

    override fun close() {}

    fun reset() {
        this.snapshot!!.symbolTables.clear()
    }
}