package com.smeup.rpgparser.video.snapshot

import com.smeup.rpgparser.interpreter.RuntimeInterpreterSnapshot

internal class StackTraceStorageMock {
    var snapshot: RuntimeInterpreterSnapshot? = null
    private var stackTrace: MutableMap<String, StackTrace> = mutableMapOf()

    fun store(id: String, stackTrace: StackTrace) {
        // set on restore before save if snapshot is defined
        if (this.snapshot != null) this.stackTrace[id]!!.prepareForRestore()
        this.stackTrace[id] = stackTrace
    }

    fun load(id: String): StackTrace {
        if (!this.stackTrace.containsKey(id)) {
            this.stackTrace[id] = StackTrace()
            return this.stackTrace[id]!!
        }

        // set on restore because also state is saved
        if (this.snapshot != null) this.stackTrace[id]!!.prepareForRestore()
        return this.stackTrace[id]!!
    }

    fun reset(id: String) {
        this.snapshot = null
        this.stackTrace[id] = StackTrace()
    }

    fun close() {}
}