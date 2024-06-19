package com.smeup.rpgparser.interpreter

import java.util.Stack

private enum class StatementCounterState {
    EX_NOVO,
    RESTORED,
    RESUMED
}

internal object StatementCounter : Stack<Int>() {
    private var pointer: Int = -1
    private var state: StatementCounterState = StatementCounterState.EX_NOVO

    override fun push(item: Int?): Int {
        this.pointer++
        return super.push(item)
    }

    override fun pop(): Int {
        this.pointer--
        return super.pop()
    }

    fun reset() {
        this.useAsExNovo()
        this.set(emptyList(), -1)
    }

    private fun useAsExNovo() {
        this.state = StatementCounterState.EX_NOVO
    }

    fun useAsRestored() {
        this.state = StatementCounterState.RESTORED
    }

    fun set(stack: List<Int>, pointer: Int) {
        this.removeAllElements()
        stack.forEach { this.push(it) }
        this.pointer = pointer
    }

    fun prepare() {
        if (this.state == StatementCounterState.EX_NOVO) {
            this.push(0)
        }
    }

    fun peekPointer(): Int {
        if (this.state == StatementCounterState.EX_NOVO) {
            return 0
        }

        if (this.state == StatementCounterState.RESUMED) {
            return 0
        }

        this.state = StatementCounterState.RESUMED
        if (this.pointer == -1) {
            return 0
        }

        if (this.pointer < this.size - 1) {
            this.pointer++
            return this[this.pointer - 1]
        }

        return this[this.pointer]
    }
}