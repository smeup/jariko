package com.smeup.rpgparser.video.snapshot

internal class StackTraceStorageMock {
    private var snapshot: Snapshot? = null

    fun use(snapshot: Snapshot) {
        this.snapshot = snapshot
    }

    fun load(id: String): StackTrace {
        if (this.snapshot == null) {
            return StackTrace()
        }
        if (!this.snapshot!!.stackTraces.containsKey(id)) {
            this.snapshot!!.stackTraces[id] = StackTrace()
            return this.snapshot!!.stackTraces[id]!!
        }
        this.snapshot!!.stackTraces[id]!!.prepareForRestore()
        return this.snapshot!!.stackTraces[id]!!
    }

    fun store(id: String, stackTrace: StackTrace) {
        if (this.snapshot != null) {
            this.snapshot!!.stackTraces[id] = stackTrace
            this.snapshot!!.stackTraces[id]!!.prepareForRestore()
        }
    }

    fun reset(id: String) {
        this.snapshot!!.stackTraces.clear()
    }

    fun close() {}
}