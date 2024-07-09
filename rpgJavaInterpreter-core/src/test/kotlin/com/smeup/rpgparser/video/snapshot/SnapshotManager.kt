package com.smeup.rpgparser.video.snapshot

import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.interpreter.ExfmtSuspendException
import com.smeup.rpgparser.interpreter.MemorySliceMgr
import com.smeup.rpgparser.interpreter.RuntimeInterpreterSnapshot
import com.smeup.rpgparser.interpreter.RuntimeInterpreterSnapshotManager
import com.smeup.rpgparser.parsing.ast.CallStmt
import com.smeup.rpgparser.parsing.ast.Statement
import java.util.*

internal class SnapshotManager(
    private val memorySliceStorage: MemorySliceStorageMock,
    override var snapshot: RuntimeInterpreterSnapshot? = null
) : RuntimeInterpreterSnapshotManager {
    // there is no sense to create an instance of this class without a memory slice manager!
    // by the way when initializing this class manager could be not ready yet
    private val memorySliceMgr: MemorySliceMgr?
        get() = MainExecutionContext.getMemorySliceMgr()
    private val programName: String
        get() = try { MainExecutionContext.getProgramStack().peek().name } catch (e: EmptyStackException) { "" }
    private val stackTraceStorage: StackTraceStorageMock = StackTraceStorageMock()
    private var stackTrace: StackTrace = this.stackTraceStorage.load(this.programName)
    private var doIndex: Long = 0

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
        this.stackTraceStorage.store(this.programName, this.stackTrace)
        this.memorySliceMgr!!.saveBeforeExfmtSuspend()
    }

    override fun load() {
        this.setUpStorages()
        // the load operation for memory slice is not needed because it is already
        // executed after interpreter initialization
        this.stackTrace = this.stackTraceStorage.load(this.programName)
    }

    override fun beforeDOCycle(): Long {
        return this.doIndex
    }

    override fun beforeDOIteration(i: Long) {
        this.doIndex = i
    }

    override fun onCallEnterPgm() {
        this.load()
    }

    override fun onCallExitPgm() {
        this.load()
    }

    override fun isOnRestore(): Boolean {
        return this.stackTrace.isOnRestore()
    }

    override fun beforeExecuteCycle(statements: List<Statement>): Int {
//        var i = this.stackTrace.peek()
//        try {
//            if (statements[i - 1] is CallStmt) {
//                i--
//                this.stackTrace.pop()
//                this.stackTrace.push(i)
//            }
//        } catch (e: IndexOutOfBoundsException) {
//            this.stackTrace.reset()
//        }
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
        this.stackTraceStorage.store(this.programName, StackTrace.restoredFrom(list))
        this.stackTrace = this.stackTraceStorage.load(this.programName)
    }

    /**
     * Test only: resets the current stack
     */
    fun resetStack() {
        this.snapshot = null
        this.stackTraceStorage.reset(this.programName)
        this.stackTrace = this.stackTraceStorage.load(this.programName)
    }

    /**
     * Test only: resets memory slice storage
     */
    fun resetMemory() {
        this.memorySliceStorage.reset()
    }
}