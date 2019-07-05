package com.smeup.rpgparser.ast

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

data class MuteComparisonAnnotation(val val1: Expression, val val2: Expression, val comparison: Comparison,
                                    override val position: Position? = null  ) : MuteAnnotation(position)
