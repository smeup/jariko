package com.smeup.rpgparser.ast

import com.strumenta.kolasu.model.Position

// *00..*99
data class PredefinedIndicatorExpr(val index: Int, override val position: Position? = null) :
    Expression(position)
