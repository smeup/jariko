package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.UnwrappedStatementData
import com.smeup.rpgparser.utils.indexOfTag
import com.smeup.rpgparser.utils.runIfNotEmpty
import java.io.File
import java.util.*

open class ControlFlowException() : Exception() {
    override fun fillInStackTrace(): Throwable = this
}

class LeaveSrException : ControlFlowException()
class LeaveException : ControlFlowException()
class IterException : ControlFlowException()

/**
 * Exception similar to [GotoException] but thrown when we are trying to goto a TAG
 * at top level.
 *
 * The distinction is needed in order to allow catching it in different places and therefore
 * executing different logic in different execution flows.
 *
 * @param tag The tag of the destination label
 */
class GotoTopLevelException(val tag: String) : ControlFlowException() {
    internal fun indexOfTaggedStatement(statements: List<UnwrappedStatementData>) = statements.indexOfTag(tag)
}

// Useful to interrupt infinite cycles in tests
class InterruptForDebuggingPurposes : ControlFlowException()
class ReturnException(val returnValue: Value?) : ControlFlowException()

/**
 * Exception thrown whenever we execute a GOTO-like instruction
 * @param tag The tag of the destination label
 */
class GotoException private constructor(val tag: String) : ControlFlowException() {
    companion object {
        operator fun invoke(tag: String): GotoException = GotoException(tag.uppercase(Locale.getDefault()))
    }

    internal fun indexOfTaggedStatement(statements: List<UnwrappedStatementData>) = statements.indexOfTag(tag)
}

class InterpreterTimeoutException(val programName: String, val elapsed: Long, val expected: Long) : ControlFlowException() {
    fun ratio(): Double = if (elapsed <= 0) 0.0 else elapsed.toDouble() / expected.toDouble()
    override fun toString(): String {
        writeDataToCSV()
        return "$programName TIMEOUT. Execution took $elapsed millis, but there was a $expected millis timeout. elapsed/expected ratio: ${ratio()}"
    }

    private fun writeDataToCSV() {
        System.getProperty("exportCsvFile").runIfNotEmpty {
            try {
                val file = File(this)
                println("Printing results to $this")
                if (!file.exists()) {
                    file.appendText("Program Name;Elapsed;Expected;Ratio\n")
                }
                file.appendText("$programName;$elapsed;$expected;${ratio()}\n")
            } catch (e: Exception) {
                System.err.println("Problems with csv file $this : $e")
            }
        }
    }
}
