package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.Statement

/**
 * Manages [SymbolTable] through [IMemorySliceStorage] to allow saving and restoring state of the interpreter
 */
interface RuntimeInterpreterSnapshotManager {
    /**
     * It is used as key to store or load a specific snapshot.
     */
    var snapshot: RuntimeInterpreterSnapshot?

    fun take(): RuntimeInterpreterSnapshot
    fun store()
    fun load()
    fun beforeDOCycle(): Long
    fun beforeDOIteration(i: Long)
    fun onCallEnterPgm()
    fun onCallExitPgm()
    fun isOnRestore(): Boolean
    fun beforeExecuteCycle(statement: List<Statement>): Int
    fun beforeStatementExecution(i: Int)
    fun afterStatementExecution()
}