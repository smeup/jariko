package com.smeup.rpgparser.parsing.ast

import com.strumenta.kolasu.model.Position

// %LOOKUP
// To be supported:
// * %LOOKUPLT
// * %LOOKUPLE
// * %LOOKUPGT
// * %LOOKUPGE
data class LookupExpr(
    var searchedValued: Expression,
    val array: Expression,
    override val position: Position? = null
) : Expression(position)

// %SCAN
data class ScanExpr(
    var value: Expression,
    val source: Expression,
    val start: Expression? = null,
    override val position: Position? = null
) : Expression(position)

// %XLATE
data class TranslateExpr(
    var from: Expression,
    var to: Expression,
    var string: Expression,
    val startPos: Expression,
    override val position: Position? = null
) :
    Expression(position)

// %TRIM
data class TrimExpr(
    var value: Expression,
    val charactersToTrim: Expression? = null,
    override val position: Position? = null
) : Expression(position)

// %TRIMR
data class TrimrExpr(
    var value: Expression,
    val charactersToTrim: Expression? = null,
    override val position: Position? = null
) : Expression(position)

// %SUBST
data class SubstExpr(
    var string: Expression,
    val start: Expression,
    val length: Expression? = null,
    override val position: Position? = null
) : AssignableExpression(position) {
    override fun size(): Long {
        TODO("size")
    }
}

// %LEN
data class LenExpr(var value: Expression, override val position: Position? = null) :
    Expression(position)

// %DEC
data class DecExpr(
    var value: Expression,
    var intDigits: Expression,
    val decDigits: Expression,
    override val position: Position? = null
) :
    Expression(position)

// %EDITC
// TODO add other parameters
data class EditcExpr(
    var value: Expression,
    val format: Expression,
    override val position: Position? = null
) :
    Expression(position)

// %FOUND
data class FoundExpr(
    var name: String? = null,
    override val position: Position? = null
) :
        Expression(position)

// %CHAR
data class CharExpr(var value: Expression, override val position: Position? = null) :
    Expression(position)

// %TIMESTAMP
data class TimeStampExpr(val value: Expression?, override val position: Position? = null) :
    Expression(position)

// %DIFF
data class DiffExpr(
    var value1: Expression,
    var value2: Expression,
    val durationCode: Expression,
    override val position: Position? = null
) :
    Expression(position)

// TODO Move and handle different types of duration
// TODO document what a duration code is
data class DurationCodeExpr(override var position: Position? = null) :
    Expression(position)
