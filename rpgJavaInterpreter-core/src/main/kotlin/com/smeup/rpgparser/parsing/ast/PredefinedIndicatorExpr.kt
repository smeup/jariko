package com.smeup.rpgparser.parsing.ast

import com.strumenta.kolasu.model.Position

// *IN00..*IN99
data class PredefinedIndicatorExpr(val index: Int, override val position: Position? = null) :
    AssignableExpression(position) {
    override fun size(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

// *IN
data class PredefinedGlobalIndicatorExpr(override val position: Position? = null) :
        AssignableExpression(position) {
    override fun size(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
