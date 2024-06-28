package com.smeup.rpgparser.video.snapshot

import com.smeup.rpgparser.interpreter.RuntimeInterpreterSnapshot

internal class StackTraceStorageMock {
    var snapshot: RuntimeInterpreterSnapshot? = null
    private var stackTrace: StackTrace = StackTrace()

    fun store(stackTrace: StackTrace) {
        this.stackTrace = stackTrace
    }

    fun load(): StackTrace {
        return this.stackTrace
    }

    fun close() {
        TODO("Not yet implemented")
    }
}