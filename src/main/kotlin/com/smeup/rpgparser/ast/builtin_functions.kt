package com.smeup.rpgparser.ast

import com.strumenta.kolasu.model.Position

data class LookupExpr(val searchedValued: Expression, val array: Expression, override val position: Position? = null) : Expression(position)

data class ScanExpr(val value: Expression, val source: Expression, val start: Expression? = null, override val position: Position? = null) : Expression(position)

data class TranslateExpr(var from: Expression, var to: Expression, var string: Expression,
                         val startPos: Expression? = null, override val position: Position? = null) : Expression(position)

data class TrimExpr(val value: Expression, val charactersToTrim: Expression? = null,
                    override val position: Position? = null) : Expression(position)

data class SubstExpr(val string: Expression, val start: Expression,
                     val length: Expression? = null, override val position: Position? = null)
    : Expression(position)

data class LenExpr(val value: Expression, override val position: Position? = null)
    : Expression(position)

data class PredefinedIndicatorExpr(val index: Int, override val position: Position? = null)
    : Expression(position)

data class DecExpr(val value: Expression, var intDigits : Expression, val decDigits: Expression, override val position: Position? = null)
    : Expression(position)

data class CharExpr(val value: Expression, override val position: Position? = null)
    : Expression(position)
