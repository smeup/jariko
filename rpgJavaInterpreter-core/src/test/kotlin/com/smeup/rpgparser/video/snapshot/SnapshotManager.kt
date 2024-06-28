package com.smeup.rpgparser.video.snapshot

import com.smeup.rpgparser.interpreter.IMemorySliceStorage
import com.smeup.rpgparser.interpreter.RuntimeInterpreterSnapshot
import com.smeup.rpgparser.interpreter.RuntimeInterpreterSnapshotManager

class SnapshotManager(
    private val memorySliceStorage: IMemorySliceStorage,
    override var snapshot: RuntimeInterpreterSnapshot? = null
) : RuntimeInterpreterSnapshotManager {
    private val stackTraceStorage: StackTraceStorage? = null

    override fun take(): RuntimeInterpreterSnapshot {
        TODO("Not yet implemented")
    }

    override fun store() {
        TODO("Not yet implemented")
    }

    override fun load() {
        TODO("Not yet implemented")
    }

    override fun peekStatement(): Int {
        TODO("Not yet implemented")
    }

    override fun beforeStatementExecution(i: Int) {
        TODO("Not yet implemented")
    }

    override fun afterStatementExecution() {
        TODO("Not yet implemented")
    }

}