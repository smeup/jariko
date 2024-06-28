package com.smeup.rpgparser.video.snapshot

import com.smeup.rpgparser.interpreter.RuntimeInterpreterSnapshot

internal interface StackTraceStorage : AutoCloseable {
    var snapshot: RuntimeInterpreterSnapshot?

    fun store(stackTrace: StackTrace)
    fun load(): StackTrace
}