package com.smeup.rpgparser.interpreter

class LeaveSrException : Exception()
class LeaveException : Exception()
class IterException : Exception()

// Useful to interrupt infinite cycles in tests
class InterruptForDebuggingPurposes : RuntimeException()
class ReturnException(val returnValue: Value?) : RuntimeException()
class GotoException(val tag: String) : RuntimeException()
