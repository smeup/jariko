package com.smeup.rpgparser.interpreter

open class ControlFlowException() : Exception() {
    override fun fillInStackTrace(): Throwable = this
}

class LeaveSrException : ControlFlowException()
class LeaveException : ControlFlowException()
class IterException : ControlFlowException()

// Useful to interrupt infinite cycles in tests
class InterruptForDebuggingPurposes : ControlFlowException()
class ReturnException(val returnValue: Value?) : ControlFlowException()
class GotoException(val tag: String) : ControlFlowException()

class InterpreterTimeoutException(val elapsed: Long, val expected: Long) : ControlFlowException() {
    fun ratio(): Double = if (elapsed <= 0) 0.0 else expected.toDouble() / elapsed.toDouble()
    override fun toString(): String {
        return "TIMEOUT. Execution took $elapsed millis, but there was a $expected millis timeout. expected/elapsed ratio: ${ratio()}"
    }
}
