package com.smeup.rpgparser.logging

import com.smeup.rpgparser.interpreter.*

interface ILoggable {
    val loggableEntityName: String
}

interface ILoggableExpression : ILoggable {
    override val loggableEntityName: String
        get() = this.javaClass.simpleName
}

interface ILoggableStatement : ILoggable {
    override val loggableEntityName: String
        get() = this.javaClass.simpleName

    fun getStatementLogRenderer(
        source: LogSourceProvider,
        action: String
    ): LazyLogEntry {
        val entry = LogEntry(source, LogChannel.STATEMENT.getPropertyName(), action)
        return LazyLogEntry.produceMessage(entry, this.loggableEntityName)
    }

    fun getResolutionLogRenderer(source: LogSourceProvider): LazyLogEntry? = null
}