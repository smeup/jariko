package com.smeup.rpgparser.logging

import com.smeup.rpgparser.interpreter.*

data class DataLogMetadata(
    val data: AbstractDataDefinition,
    val value: Value,
    val previous: Value?
)

interface ILoggable {
    val loggableEntityName: String
}

interface ILoggableStatement : ILoggable {
    override val loggableEntityName: String
        get() = "STMT"

    fun getStatementLogRenderer(
        source: LogSourceData,
        action: String
    ): LazyLogEntry {
        val entry = LogEntry(source, LogChannel.STATEMENT.getPropertyName(), action)
        return LazyLogEntry.produceMessage(entry, this.loggableEntityName)
    }

    fun getResolutionLogRenderer(source: LogSourceData): LazyLogEntry? = null
}