package com.smeup.rpgparser.ast

import com.smeup.rpgparser.RpgParser
import com.strumenta.kolasu.model.Position

// %LOOKUP
// To be supported:
// * %LOOKUPLT
// * %LOOKUPLE
// * %LOOKUPGT
// * %LOOKUPGE
data class LookupExpr(val searchedValued: Expression, val array: Expression,
                      override val position: Position? = null) : Expression(position)

// %SCAN
data class ScanExpr(val value: Expression, val source: Expression, val start: Expression? = null,
                    override val position: Position? = null) : Expression(position)

// %XLATE
data class TranslateExpr(var from: Expression, var to: Expression, var string: Expression,
                         val startPos: Expression? = null, override val position: Position? = null)
    : Expression(position)

// %TRIM
data class TrimExpr(val value: Expression, val charactersToTrim: Expression? = null,
                    override val position: Position? = null) : Expression(position)

// %SUBST
data class SubstExpr(val string: Expression, val start: Expression,
                     val length: Expression? = null, override val position: Position? = null)
    : Expression(position)

// %LEN
data class LenExpr(val value: Expression, override val position: Position? = null)
    : Expression(position)

// %DEC
data class DecExpr(val value: Expression, var intDigits : Expression, val decDigits: Expression,
                   override val position: Position? = null)
    : Expression(position)

// %CHAR
data class CharExpr(val value: Expression, override val position: Position? = null)
    : Expression(position)

// %TIMESTAMP
data class TimeStampExpr(val value: Expression?, override val position: Position? = null)
    : Expression(position)

// %DIFF
data class DiffExpr(val value1: Expression, var value2 : Expression, val durationCode: Expression,
                   override val position: Position? = null)
    : Expression(position)

// TODO Move and handle different types of duration
data class DurationCodeExpr(override val position: Position? = null)
    : Expression(position)