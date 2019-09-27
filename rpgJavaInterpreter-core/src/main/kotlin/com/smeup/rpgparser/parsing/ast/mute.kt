package com.smeup.rpgparser.parsing.ast

import com.smeup.rpgparser.interpreter.Value
import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Position

abstract class MuteAnnotation(override val position: Position? = null) : Node(position)

enum class Comparison {
    EQ,
    NE,
    GT,
    GE,
    LT,
    LE
}

data class MuteComparisonAnnotation(
    var val1: Expression,
    var val2: Expression,
    val comparison: Comparison,
    override val position: Position? = null
) : MuteAnnotation(position)

/**
 * This type is supported for retro-compatibility but it is never processed
 */
data class MuteTypeAnnotation(override var position: Position? = null) : MuteAnnotation(position)

/**
 * A Mute annotation associated to a statement
 */
data class MuteAnnotationResolved(val muteLine: Int, val statementLine: Int)

/**
 * The result of executing a mute annotation. Note that currently we have only annotations with two values.
 * This could change in the future.
 */
data class MuteAnnotationExecuted(val programName: String, val expression: Expression, val value1Expression: Expression, val value2Expression: Expression, val result: Value, val value1Result: Value, val value2Result: Value)
