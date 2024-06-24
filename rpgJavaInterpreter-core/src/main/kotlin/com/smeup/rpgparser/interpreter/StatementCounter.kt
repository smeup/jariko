package com.smeup.rpgparser.interpreter

import java.util.Stack

private enum class StatementCounterState {
    EX_NOVO,
    RESUME,
    CONTINUE
}

interface IStatementCounterStorage {

}

class StatementCounter : Stack<Int> {
    private var pointer: Int = -1
    private var state: StatementCounterState = StatementCounterState.EX_NOVO

    constructor() : super()

    constructor(stack: List<Int>, pointer: Int) {
        this.removeAllElements()
        stack.forEach {
            // Should be independent to state
            this.pointer++
            super.push(it)
        }
        this.pointer = pointer
    }

    fun prepareForRestore() {
        this.state = StatementCounterState.RESUME
    }

    override fun clone(): Stack<Int> {
        val stack = Stack<Int>()
        stack.addAll(this)
        return stack
    }

    override fun push(item: Int?): Int {
        if (this.state == StatementCounterState.EX_NOVO || this.state == StatementCounterState.CONTINUE) {
            this.pointer++
            return super.push(item)
        }
        return 0
    }

    override fun pop(): Int {
        if (this.state == StatementCounterState.EX_NOVO || this.state == StatementCounterState.CONTINUE) {
            this.pointer--
            return super.pop()
        }
        return 0
    }

    override fun peek(): Int {
        if (this.state == StatementCounterState.EX_NOVO || this.state == StatementCounterState.CONTINUE) {
            return 0
        }

        // At this point this.state == StatementCounterState.RESUME

        if (this.pointer == -1) {
            // this equals to start from first statement of the program
            return 0
        }

        if (this.pointer == this.size - 1) {
            this.state = StatementCounterState.CONTINUE
            return this[this.pointer]
        }

        return this[this.pointer++]
    }

    companion object {
        fun restoreFrom(stack: List<Int>, pointer: Int): StatementCounter {
            val statementCounter = StatementCounter(stack, pointer)
            statementCounter.prepareForRestore()
            return statementCounter
        }
    }
}