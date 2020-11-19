package com.smeup.rpgparser.parsing.ast

import com.smeup.rpgparser.interpreter.Evaluator
import com.smeup.rpgparser.interpreter.Value
import com.strumenta.kolasu.model.Position
import kotlinx.serialization.Serializable

// *IN01..*IN99
data class PredefinedIndicatorExpr(val index: Int, override val position: Position? = null) :
    AssignableExpression(position) {
    init {
        require(index in 1..99) { "Indicator not in range 01 to 99 at $position" }
    }
    override fun size(): Int = 1
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// *IN
data class PredefinedGlobalIndicatorExpr(override val position: Position? = null) :
        AssignableExpression(position) {
    override fun size(): Int {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

@Serializable
data class DataWrapUpIndicatorExpr(val dataWrapUpChoice: DataWrapUpChoice, override val position: Position? = null) :
        AssignableExpression(position) {
    override fun size(): Int = 1
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}
