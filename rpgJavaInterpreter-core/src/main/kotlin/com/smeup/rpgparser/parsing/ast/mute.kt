package com.smeup.rpgparser.parsing.ast

import com.smeup.rpgparser.interpreter.BooleanValue
import com.smeup.rpgparser.interpreter.Value
import com.smeup.rpgparser.utils.ComparisonOperator
import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Position

abstract class MuteAnnotation(override val position: Position? = null) : Node(position)

data class MuteComparisonAnnotation(
    var val1: Expression,
    var val2: Expression,
    val comparison: ComparisonOperator,
    override val position: Position? = null
) : MuteAnnotation(position)

/**
 * This type is supported for retro-compatibility but it is never processed
 */
data class MuteTypeAnnotation(override var position: Position? = null) : MuteAnnotation(position)

/**
 * A timeout annotation
 */
data class MuteTimeoutAnnotation(val timeout: Long, override var position: Position? = null) : MuteAnnotation(position)

/**
 * A Fail annotation
 */
data class MuteFailAnnotation(val message: Expression, override val position: Position? = null) :
    MuteAnnotation(position)

/**
 * A Mute annotation associated to a statement
 */
data class MuteAnnotationResolved(val muteLine: Int, val statementLine: Int)

/**
 * The result of executing a mute annotation.
 */
abstract class MuteAnnotationExecuted() {
    abstract val programName: String
    abstract val result: BooleanValue
    fun succeeded(): Boolean = result.value
    fun failed(): Boolean = !succeeded()
    fun resultAsString() = if (succeeded()) {
        "succeded"
    } else {
        "failed"
    }
    abstract fun headerDescription(): String
}

/**
 * The result of executing a mute comparison annotation.
 * Note that currently we have only annotations with two values.
 * This could change in the future.
 */
data class MuteComparisonAnnotationExecuted(
    override val programName: String,
    val expression: Expression,
    val value1Expression: Expression,
    val value2Expression: Expression,
    override val result: BooleanValue,
    val value1Result: Value,
    val value2Result: Value,
    val line: String
) : MuteAnnotationExecuted() {
    override fun headerDescription(): String =
        "${expression.render()} - Left value ${value1Result.render()}  - right value ${value2Result.render()} - Line $line"
}

/**
 * The result of a failing mute annotation.
 */
data class MuteFailAnnotationExecuted(
    override val programName: String,
    val message: Value,
    val line: String
) : MuteAnnotationExecuted() {
    override fun headerDescription(): String = message.render() + " - Line $line"
    override val result = BooleanValue.FALSE
}

/**
 * The result of a passing Timeout mute annotation.
 */
data class MuteTimeoutAnnotationExecuted(
    override val programName: String,
    val timeout: Long,
    val line: String
) : MuteAnnotationExecuted() {
    override fun headerDescription(): String = "Timeout $timeout - Line $line"
    override val result = BooleanValue.TRUE
}

fun HashMap<Int, MuteAnnotationExecuted>.failed(): List<MuteAnnotationExecuted> =
    this.toSortedMap().filter { it.value.failed() }.map { it.value }
