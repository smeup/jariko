package com.smeup.rpgparser.interpreter

interface StatementsExecuteTraceManager : AutoCloseable {
    fun store()
    fun load()
}