package com.smeup.rpgparser.video.snapshot

import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.interpreter.InterpreterCore
import com.smeup.rpgparser.interpreter.MemorySliceId
import com.smeup.rpgparser.interpreter.MemorySliceMgr
import com.smeup.rpgparser.interpreter.RuntimeInterpreterSnapshot
import com.smeup.rpgparser.interpreter.RuntimeInterpreterSnapshotManager
import com.smeup.rpgparser.interpreter.Value
import com.smeup.rpgparser.parsing.ast.Statement
import java.util.EmptyStackException
import kotlin.collections.LinkedHashMap

internal class SnapshotManager(
    private val memorySliceStorage: MemorySliceStorageMock,
    // snapshot will be deserialized and passed here
    var snapshot: Snapshot? = null
) : RuntimeInterpreterSnapshotManager {
    // there is no sense to create an instance of this class without a memory slice manager!
    // by the way when initializing this class manager could be not ready yet
    private val memorySliceMgr: MemorySliceMgr?
        get() = MainExecutionContext.getMemorySliceMgr()
    private val stackTraceStorage: StackTraceStorageMock = StackTraceStorageMock()
    private var stackTrace: StackTrace = this.stackTraceStorage.load(this.programName)
    private val programName: String
        get() = try {
            MainExecutionContext.getProgramStack().peek().name
        } catch (e: EmptyStackException) {
            ""
        }
    private val activationGroup: String
        get() = try {
            MainExecutionContext.getProgramStack().peek().activationGroup.assignedName
        } catch (e: EmptyStackException) {
            ""
        }

    private fun setUpStorages() {
        if (this.snapshot == null) {
            this.snapshot = Snapshot()
        }
        this.memorySliceStorage.use(this.snapshot!!)
        this.stackTraceStorage.use(this.snapshot!!)
    }

    override fun take(): RuntimeInterpreterSnapshot {
        this.setUpStorages()
        return this.snapshot!!
    }

    override fun store() {
        this.setUpStorages()
        this.stackTraceStorage.store(this.programName, this.stackTrace)
        this.memorySliceMgr!!.saveBeforeExfmtSuspend()
        // here we can serialize snapshot
    }

    override fun load() {
        this.setUpStorages()
        // the load operation for memory slice is not needed because it is already
        // executed after interpreter initialization before this call
        if (!allowStackReadTest) {
            this.stackTrace = this.stackTraceStorage.load(this.programName)
        }
    }

    private var doIndex: Long = 0
    override fun beforeDOCycle(): Long { return this.doIndex }
    override fun beforeDOIteration(i: Long) { this.doIndex = i }
    override fun afterDOIteration() {}

    override fun beforeCall() {
        this.load()
        this.stackTrace.unblockCall()
    }

    override fun onCallSuspend(interpreter: InterpreterCore, params: LinkedHashMap<String, Value>) {
        // this procedure uses symbol table from last called program to update symbol table of the
        // caller (through the interpreter, since each program has its own)
        val id = MemorySliceId(this.activationGroup, this.programName)
        val values = this.memorySliceStorage.load(id)

        values.filter { params.keys.contains(it.key) }.forEach {
            val dataDefinition = interpreter.dataDefinitionByName(it.key)!!
            val value = it.value
            interpreter.assign(dataDefinition, value)
        }

        this.store()
    }

    override fun afterCall() {
        this.load()
        this.stackTrace.blockCall()
    }

    override fun isOnRestore(): Boolean {
        return this.stackTrace.isOnRestore()
    }

    override fun beforeExecuteCycle(statements: List<Statement>): Int {
        return this.stackTrace.peek(statements)
    }

    override fun beforeStatementExecution(i: Int) {
        this.stackTrace.push(i)
    }

    override fun afterStatementExecution() {
        this.stackTrace.pop()
    }

    /**
     * Test only: allows test for stack read
     */
    var allowStackReadTest: Boolean = false

    /**
     * Test only: stack info
     */
    fun getStackInfo(): StackInfo {
        return this.stackTrace.getInfo()
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
        this.setUpStorages()
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