package com.smeup.rpgparser.parsing.ast

import com.smeup.rpgparser.interpreter.Evaluator
import com.smeup.rpgparser.interpreter.Value
import com.smeup.rpgparser.parsing.parsetreetoast.isInt
import com.strumenta.kolasu.model.Position
import kotlinx.serialization.Serializable

// *IN01..*IN99 and *INLR *INRT
@Serializable
data class IndicatorExpr(
    val index: IndicatorKey,
    override val position: Position? = null,
    val indexExpression: Expression? = null
) : AssignableExpression(position) {

    constructor(dataWrapUpChoice: DataWrapUpChoice, position: Position? = null) :
            this(index = dataWrapUpChoice.name.toIndicatorKey(), position = position)

    /**
     * Constructor for *IN where the index is an expression
     * */
    constructor(index: Expression, position: Position?) :
            this(index = 0, position = position, indexExpression = index)

    override fun size(): Int = 1
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// *IN
@Serializable
data class GlobalIndicatorExpr(override val position: Position? = null) :
        AssignableExpression(position) {
    override fun size(): Int = IndicatorType.Predefined.range.last
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

typealias IndicatorKey = Int

/**
 * Managed indicator types
 * */
enum class IndicatorType(val range: IntRange) {
    Predefined(1..99),
    LR(100..100),
    RT(101..101),
    OC(102..102),
    OF(103..103),
    OV(104..104),
    KA(105..105),
    KB(106..106),
    KC(107..107),
    KD(108..108),
    KE(109..109),
    KF(110..110),
    KG(111..111),
    KH(112..112),
    KI(113..113),
    KJ(114..114),
    KK(115..115),
    KL(116..116),
    KM(117..117),
    KN(118..118),
    KP(119..119),
    KQ(120..120),
    KR(121..121),
    KS(122..122),
    KT(123..123),
    KU(124..124),
    KV(125..125),
    KW(126..126),
    KX(127..127),
    KY(128..128),
    ;

    companion object {

        val STATELESS_INDICATORS: List<IndicatorKey> by lazy {
            arrayListOf<IndicatorKey>().apply {
                values().filter { indicatorType ->
                    indicatorType.range.last > 99
                }.map { indicatorType ->
                    indicatorType.range.forEach {
                        add(it)
                    }
                }
            }
        }
    }
}

/**
 * Convert a string in format [0-9] [0-9] or [a-zA-Z] [a-zA-Z] to IndicatorKey
 * */
fun String.toIndicatorKey(): IndicatorKey {
    return when {
        this.isInt() -> this.let {
            require(IndicatorType.Predefined.range.contains(it.toInt()))
            it.toInt()
        }
        else -> IndicatorType.valueOf(this).range.first
    }
}

@Serializable
data class IndicatorCondition(val key: IndicatorKey, val negate: Boolean)
@Serializable
data class ContinuedIndicator(val key: IndicatorKey, val negate: Boolean, val level: String)
