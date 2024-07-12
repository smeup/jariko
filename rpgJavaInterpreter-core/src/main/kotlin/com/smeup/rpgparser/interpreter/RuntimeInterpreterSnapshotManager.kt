package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.Statement

/**
 * Manages [SymbolTable] through [IMemorySliceStorage] to allow saving and restoring state of the interpreter
 */
interface RuntimeInterpreterSnapshotManager {
    fun take(): RuntimeInterpreterSnapshot
    fun store()
    fun load()
    fun beforeDOCycle(): Long
    fun beforeDOIteration(i: Long)
    fun afterDOIteration()
    fun beforeCall()
    fun onCallSuspend(interpreter: InterpreterCore, params: LinkedHashMap<String, Value>)
    fun afterCall()
    fun isOnRestore(): Boolean
    fun beforeExecuteCycle(statements: List<Statement>): Int
    fun beforeStatementExecution(i: Int)
    fun afterStatementExecution()
}