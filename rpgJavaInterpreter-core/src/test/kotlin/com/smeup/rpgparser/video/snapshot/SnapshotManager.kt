package com.smeup.rpgparser.video.snapshot

import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.interpreter.MemorySliceMgr
import com.smeup.rpgparser.interpreter.RuntimeInterpreterSnapshot
import com.smeup.rpgparser.interpreter.RuntimeInterpreterSnapshotManager
import java.util.Stack

internal class SnapshotManager(
    private val memorySliceStorage: MemorySliceStorageMock,
    override var snapshot: RuntimeInterpreterSnapshot? = null
) : RuntimeInterpreterSnapshotManager {
    // there is no sense to create an instance of this class without a memory slice manager!
    // by the way when initializing this class manager could be not ready yet
    private val memorySliceMgr: MemorySliceMgr? = MainExecutionContext.getMemorySliceMgr()
    private val stackTraceStorage: StackTraceStorageMock = StackTraceStorageMock()
    private var stackTrace: StackTrace = StackTrace()

    override fun take(): RuntimeInterpreterSnapshot {
        this.snapshot = RuntimeInterpreterSnapshot("")
        this.stackTraceStorage.snapshot = this.snapshot
        this.memorySliceStorage.snapshot = this.snapshot
        return this.snapshot!!
    }

    override fun store() {
        this.stackTraceStorage.store(this.stackTrace)
        this.memorySliceMgr!!.saveBeforeExfmtSuspend()
    }

    override fun load() {
        // the load operation for memory slice is not needed because it is already
        // executed after interpreter initialization
        this.stackTrace = this.stackTraceStorage.load()
    }

    override fun isOnRestore(): Boolean {
        return this.stackTrace.isOnRestore()
    }

    override fun beforeExecuteCycle(): Int {
        return this.stackTrace.peek()
    }

    override fun beforeStatementExecution(i: Int) {
        this.stackTrace.push(i)
    }

    override fun afterStatementExecution() {
        this.stackTrace.pop()
    }

    /**
     * Test only: returns a copy of the stack to preventing the program any way to mutate a private attribute
     */
    fun getStack(): Stack<Int> {
        return this.stackTrace.clone()
    }
}