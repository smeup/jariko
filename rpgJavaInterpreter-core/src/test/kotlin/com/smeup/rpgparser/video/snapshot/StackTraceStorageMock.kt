package com.smeup.rpgparser.video.snapshot

import com.smeup.rpgparser.interpreter.RuntimeInterpreterSnapshot

internal class StackTraceStorageMock : StackTraceStorage {
    override fun store(
        snapshot: RuntimeInterpreterSnapshot,
        stackTrace: StackTrace
    ) {
        TODO("Not yet implemented")
    }

    override fun load(snapshot: RuntimeInterpreterSnapshot): StackTrace {
        TODO("Not yet implemented")
    }


    override fun close() {
        TODO("Not yet implemented")
    }
}