package com.smeup.rpgparser.video.snapshot

import com.smeup.rpgparser.interpreter.RuntimeInterpreterSnapshot

internal interface StackTraceStorage : AutoCloseable {
    fun store(snapshot: RuntimeInterpreterSnapshot, stackTrace: StackTrace)
    fun load(snapshot: RuntimeInterpreterSnapshot): StackTrace
}