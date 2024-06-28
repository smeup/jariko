package com.smeup.rpgparser.interpreter

interface StatementsExecuteTraceManager : AutoCloseable {
    val trace: StatementsExecuteTrace

    fun store()
    fun load()
}