package com.smeup.rpgparser.parsing.ast

import com.smeup.rpgparser.interpreter.Evaluator
import com.smeup.rpgparser.interpreter.Value
import com.strumenta.kolasu.model.Position
import kotlinx.serialization.Serializable

// *IN01..*IN99 and *INLR *INRT
@Serializable
data class IndicatorExpr(val index: IndicatorKey, override val position: Position? = null) :
    AssignableExpression(position) {

    constructor(dataWrapUpChoice: DataWrapUpChoice, position: Position? = null) :
            this(index = dataWrapUpChoice.name.toIndicatorKey(), position = position)

    override fun size(): Int = 1
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// *IN
@Serializable
data class GlobalIndicatorExpr(override val position: Position? = null) :
        AssignableExpression(position) {
    override fun size(): Int {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}
