package com.smeup.rpgparser.video

import com.smeup.rpgparser.interpreter.StatementsExecuteTrace
import java.util.Stack

private enum class TraceState {
    EX_NOVO,
    RESUME,
    CONTINUE
}

class StackTrace : StatementsExecuteTrace, Stack<Int> {
    private var pointer: Int = -1
    private var state: TraceState = TraceState.EX_NOVO

    constructor() : super()

    constructor(stack: List<Int>, pointer: Int) {
        this.forceSet(stack, pointer)
    }

    fun isOnRestore(): Boolean {
        return this.state == TraceState.RESUME
    }

//    fun restoreFrom(manager: StatementCounterMgr?) {
//        val statementCounter = manager?.load()
//        if (statementCounter == null || statementCounter.empty()) {
//            // if stack is empty (just created for example with empty constructor)
//            // then no need to resume, just start ex novo
//            this.reset()
//        } else {
//            // pointer is 0, when restore pointer should start over again
//            this.prepareForRestore()
//            this.forceSet(statementCounter, 0)
//        }
//    }

    private fun forceSet(stack: List<Int>, pointer: Int) {
        this.removeAllElements()
        stack.forEach {
            // Should be independent to state
            this.pointer++
            super.push(it)
        }
        this.pointer = pointer
    }

    fun reset() {
        this.forceSet(emptyList(), -1)
    }

    private fun prepareForRestore() {
        this.state = TraceState.RESUME
    }

    override fun clone(): Stack<Int> {
        val stack = Stack<Int>()
        stack.addAll(this)
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
            // otherwise if last is an EXFMT another exception is thrown (loops)
            // should also handle properly if there is no last + 1 statement
            this.state = TraceState.CONTINUE
            return this[this.pointer] + 1
        }

        return this[this.pointer++]
    }

    companion object {
        fun restoredFrom(stack: List<Int>, pointer: Int): StackTrace {
            val statementCounter = StackTrace(stack, pointer)
            statementCounter.prepareForRestore()
            return statementCounter
        }
    }
}