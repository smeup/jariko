package com.smeup.rpgparser.ast

import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Position

abstract class MuteAnnotation(override val position: Position? = null) : Node(position)



data class MuteComparisonAnnotation(val val1: Expression, val val2: Expression, val comparison: String, override val position: Position? = null  ) : MuteAnnotation(position)