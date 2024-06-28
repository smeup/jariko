package com.smeup.rpgparser.video.snapshot

import com.smeup.rpgparser.interpreter.RuntimeInterpreterSnapshot

internal class StackTraceStorageMock : StackTraceStorage {
    override var snapshot: RuntimeInterpreterSnapshot? = null

    override fun store(stackTrace: StackTrace) {
        TODO("Not yet implemented")
    }

    override fun load(): StackTrace {
        TODO("Not yet implemented")
    }


    override fun close() {
        TODO("Not yet implemented")
    }
}