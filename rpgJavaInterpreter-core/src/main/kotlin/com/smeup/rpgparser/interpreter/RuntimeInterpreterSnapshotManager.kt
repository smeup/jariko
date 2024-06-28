package com.smeup.rpgparser.interpreter

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
    fun isOnRestore(): Boolean
    fun beforeExecuteCycle(): Int
    fun beforeStatementExecution(i: Int)
    fun afterStatementExecution()
}