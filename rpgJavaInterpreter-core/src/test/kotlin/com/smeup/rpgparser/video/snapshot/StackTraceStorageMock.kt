package com.smeup.rpgparser.video.snapshot

import com.smeup.rpgparser.interpreter.RuntimeInterpreterSnapshot

internal class StackTraceStorageMock {
    var snapshot: RuntimeInterpreterSnapshot? = null
    private var stackTrace: StackTrace = StackTrace()

    fun store(stackTrace: StackTrace) {
        // set on restore before save if snapshot is defined
        if (this.snapshot != null) this.stackTrace.prepareForRestore()
        this.stackTrace = stackTrace
    }

    fun load(): StackTrace {
        // set on restore because also state is saved
        if (this.snapshot != null) this.stackTrace.prepareForRestore()
        return this.stackTrace
    }

    fun close() {}
}