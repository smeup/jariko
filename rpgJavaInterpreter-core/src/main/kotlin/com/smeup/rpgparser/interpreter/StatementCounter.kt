package com.smeup.rpgparser.interpreter

import java.util.Stack

private enum class StatementCounterState {
    EX_NOVO,
    RESUME,
//    MIDDLE_STATEMENTS,
    CONTINUE
}

internal object StatementCounter : Stack<Int>() {
    private var pointer: Int = -1
    private var state: StatementCounterState = StatementCounterState.EX_NOVO

    fun forceSet(stack: List<Int>, pointer: Int) {
        this.removeAllElements()
        stack.forEach {
            // Should be independent to state
            this.pointer++
            super.push(it)
        }
        this.pointer = pointer
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

    fun reset() {
        this.state = StatementCounterState.EX_NOVO
        this.forceSet(emptyList(), -1)
    }

    fun prepareForRestore() {
        this.state = StatementCounterState.RESUME
    }

    fun prepare() {
        if (this.state == StatementCounterState.EX_NOVO || this.state == StatementCounterState.CONTINUE) {
            this.push(0)
        }
    }

    fun peekPointer(): Int {
        if (this.state == StatementCounterState.EX_NOVO || this.state == StatementCounterState.CONTINUE) {
            return 0
        }

        // this.state == StatementCounterState.RESUME
        // suppose pointer is already set to 0 (watch then increment)

        if (this.pointer == -1) {
            // this equals to start from first statement of the program
            return 0
        }

        return this[this.pointer++]


//
//        if (this.pointer < this.size - 1) {
//            this.pointer++
//            return this[this.pointer - 1]
//        }
//
//        this.state = StatementCounterState.CONTINUE
//        return this[this.pointer]
    }
}