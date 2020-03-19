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
