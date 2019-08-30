package com.smeup.rpgparser.parsing.ast

import com.strumenta.kolasu.model.Position

// *00..*99
data class PredefinedIndicatorExpr(val index: Int, override val position: Position? = null) :
    AssignableExpression(position) {
    override fun size(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
