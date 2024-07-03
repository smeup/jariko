package com.smeup.rpgparser.video.snapshot

import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.interpreter.ExfmtSuspendException
import com.smeup.rpgparser.interpreter.MemorySliceMgr
import com.smeup.rpgparser.interpreter.RuntimeInterpreterSnapshot
import com.smeup.rpgparser.interpreter.RuntimeInterpreterSnapshotManager

internal class SnapshotManager(
    private val memorySliceStorage: MemorySliceStorageMock,
    override var snapshot: RuntimeInterpreterSnapshot? = null
) : RuntimeInterpreterSnapshotManager {
    // there is no sense to create an instance of this class without a memory slice manager!
    // by the way when initializing this class manager could be not ready yet
    private val memorySliceMgr: MemorySliceMgr?
        get() = MainExecutionContext.getMemorySliceMgr()
    private val stackTraceStorage: StackTraceStorageMock = StackTraceStorageMock()
    private var stackTrace: StackTrace = StackTrace()

    private fun setUpStorages() {
        this.stackTraceStorage.snapshot = this.snapshot
        this.memorySliceStorage.snapshot = this.snapshot
    }

    override fun take(): RuntimeInterpreterSnapshot {
        this.snapshot = RuntimeInterpreterSnapshot("")
        return this.snapshot!!
    }

    override fun store() {
        this.setUpStorages()
        this.stackTraceStorage.store(this.stackTrace)
        this.memorySliceMgr!!.saveBeforeExfmtSuspend()
        throw ExfmtSuspendException()
    }

    override fun load() {
        this.setUpStorages()
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
     * Test only: returns the copy of the stack as list
     */
    fun getStackAsList(): List<Int> {
        return this.stackTrace.clone()
    }

    /**
     * Test only: sets the current stack for restore from list
     */
    fun setStackWithList(list: List<Int>) {
        // always 0 for start correctly
        this.stackTrace = StackTrace.restoredFrom(list)
        this.stackTraceStorage.store(this.stackTrace)
    }

    /**
     * Test only: resets the current stack
     */
    fun resetStack() {
        this.stackTrace = StackTrace()
        this.stackTraceStorage.store(this.stackTrace)
    }

    /**
     * Test only: resets memory slice storage
     */
    fun resetMemory() {
        this.memorySliceStorage.reset()
    }
}