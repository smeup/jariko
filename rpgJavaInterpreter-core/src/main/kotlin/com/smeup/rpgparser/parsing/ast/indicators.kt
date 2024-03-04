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
    KA(1..1),
    KB(2..2),
    KC(3..3),
    KD(4..4),
    KE(5..5),
    KF(6..6),
    KG(7..7),
    KH(8..8),
    KI(9..9),
    KJ(10..10),
    KK(11..11),
    KL(12..12),
    KM(13..13),
    KN(14..14),
    KP(15..15),
    KQ(16..16),
    KR(17..17),
    KS(18..18),
    KT(19..19),
    KU(20..20),
    KV(21..21),
    KW(22..22),
    KX(23..23),
    KY(24..24),
    Predefined(25..99),
    LR(100..100),
    RT(101..101),
    OC(102..102),
    OF(103..103),
    OV(104..104),
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
