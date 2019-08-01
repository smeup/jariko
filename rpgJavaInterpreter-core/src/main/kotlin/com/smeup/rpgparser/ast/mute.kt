package com.smeup.rpgparser.ast

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

data class MuteComparisonAnnotation(var val1: Expression, var val2: Expression, val comparison: Comparison,
                                    override val position: Position? = null  ) : MuteAnnotation(position)


data class MuteAnnotationResolved(val muteLine : Int, val statementLine: Int )
data class MuteAnnotationExecuted(val expression: Expression, val result : Value)

