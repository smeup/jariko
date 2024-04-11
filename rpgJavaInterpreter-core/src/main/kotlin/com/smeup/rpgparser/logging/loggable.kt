package com.smeup.rpgparser.logging

import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.ast.Expression

data class PerformanceLogMetadata(
    val elapsedMs: Long
)

data class DataLogMetadata(
    val data: AbstractDataDefinition,
    val value: Value,
    val previous: Value?
)

data class ExpressionLogMetadata(
    val expression: Expression,
    val value: Value
)

interface ILoggable {
    val loggableEntityName: String
}

interface ILoggableExpression : ILoggable {
    override val loggableEntityName: String
        get() = "EXPR"

    fun getExpressionLogRenderer(entry: LogEntry, metadata: ExpressionLogMetadata): LazyLogEntry? {
        return LazyLogEntry(entry) { sep ->
            val content = buildString {
                append(metadata.expression.render())
                append(sep)
                append(metadata.value.render())
            }
            content
        }
    }
}