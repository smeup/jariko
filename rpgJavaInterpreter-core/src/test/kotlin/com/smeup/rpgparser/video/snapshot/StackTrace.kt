package com.smeup.rpgparser.video.snapshot

import com.smeup.rpgparser.parsing.ast.CallStmt
import com.smeup.rpgparser.parsing.ast.Statement
import java.util.Stack

private enum class TraceState {
    EX_NOVO,
    RESUME,
    CONTINUE
}

internal class StackTrace : Stack<Int> {
    private var pointer: Int = -1
    private var state: TraceState = TraceState.EX_NOVO

    constructor() : super()

    constructor(stack: List<Int>) {
        this.forceSet(stack, 0)
    }

    fun isOnRestore(): Boolean {
        return this.state == TraceState.RESUME
    }

    private fun forceSet(list: List<Int>, pointer: Int) {
        this.removeAllElements()
        list.forEach {
            // Should be independent to state
            this.pointer++
            super.push(it)
        }
        this.pointer = pointer
    }

    fun reset() {
        this.forceSet(emptyList(), -1)
    }

    fun prepareForRestore() {
        this.pointer = 0
        this.state = TraceState.RESUME
    }

    override fun clone(): StackTrace {
        val stack = StackTrace()
        stack.addAll(this)
        stack.pointer = this.pointer
        return stack
    }

    override fun push(item: Int?): Int {
        if (this.state == TraceState.EX_NOVO || this.state == TraceState.CONTINUE) {
            this.pointer++
            return super.push(item)
        }
        return 0
    }

    override fun pop(): Int {
        if (this.state == TraceState.EX_NOVO || this.state == TraceState.CONTINUE) {
            this.pointer--
            return super.pop()
        }
        return 0
    }

    override fun peek(): Int {
        if (this.state == TraceState.EX_NOVO || this.state == TraceState.CONTINUE) {
            return 0
        }

        // At this point this.state == StatementCounterState.RESUME

        if (this.pointer == -1) {
            // this equals to start from first statement of the program
            return 0
        }

        if (this.pointer == this.size - 1) {
            // when last statement on stack is reached, next statement to resume is last + 1
            // but a pop is needed here because if EXFMT is executed, an exception is thrown
            // and no pop will occur in interpreter;
            // otherwise, program restarts from last EXFMT and another exception is thrown
            // leading to an infinite loop
            this.state = TraceState.CONTINUE
            val i = this[this.pointer] + 1
            this.pop()
            return i
        }

        return this[this.pointer++]
    }

    private var isCallBlocked: Boolean = false
    fun unblockCall() {
        this.isCallBlocked = false
    }
    fun blockCall() {
        this.isCallBlocked = true
    }

    fun peek(statements: List<Statement>): Int {
        val i = this.peek()
        try {
            if (statements[i - 1] is CallStmt && !this.isCallBlocked) {
                return i - 1
            }
            if (statements[i] is CallStmt && !this.isCallBlocked) {
                return i
            }
            if (this.isCallBlocked) {
                this.push(i + 1)
                return i + 1
            }
//            if (statements[i - 1] is CallStmt && !this.block) {
//                i--
//                this.push(i)
//            } else if (statements[i] is CallStmt) {
//                println()
////                this.state = TraceState.CONTINUE
////                this.pointer--
//                return i + 1
//            }
//            println()
//            if (statements[i - 1] is CallStmt && !this.block) {
//                i--
//                // if last - 1 is a call statement then pop will be already occured
//                this.push(i)
//            } else if (statements[i] is CallStmt && this.block) {
//                i++
//                this.state = TraceState.CONTINUE
//                this.push(i)
//            } else if (this.block) {
//                i++
//                this.state = TraceState.CONTINUE
//                this.push(i)
////
////                this.state = TraceState.CONTINUE
////                val i = this[this.pointer] + 1
////                this.pop()
////                return i
//            }
        } catch (e: IndexOutOfBoundsException) {
            return i
        }
        // do not peek it again
        return i
    }

    companion object {
        fun restoredFrom(list: List<Int>): StackTrace {
            val statementCounter = StackTrace(list)
            statementCounter.prepareForRestore()
            return statementCounter
        }
    }

    /**
     * Test only: stack info
     */
    fun getInfo(): StackInfo {
        return StackInfo(this.clone(), this.pointer, this.state.name)
    }
}

/**
 * Test only: stack info
 */
data class StackInfo(
    val list: List<Int>,
    val pointer: Int,
    val state: String,
)