package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.utils.runIfNotEmpty
import java.io.File

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

class InterpreterTimeoutException(val programName: String, val elapsed: Long, val expected: Long) : ControlFlowException() {
    fun ratio(): Double = if (elapsed <= 0) 0.0 else expected.toDouble() / elapsed.toDouble()
    override fun toString(): String {
        writeDataToCSV()
        return "$programName TIMEOUT. Execution took $elapsed millis, but there was a $expected millis timeout. expected/elapsed ratio: ${ratio()}"
    }

    private fun writeDataToCSV() {
        System.getProperty("exportCsvFile").runIfNotEmpty {
            try {
                val file = File(this)
                println("Printing results to $this")
                file.appendText("$programName,$elapsed,$expected,${ratio()}\n")
            } catch (e: Exception) {
                System.err.println("Problems with csv file $this : $e")
            }
        }
    }
}
