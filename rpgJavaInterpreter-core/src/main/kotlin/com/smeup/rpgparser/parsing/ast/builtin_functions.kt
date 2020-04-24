package com.smeup.rpgparser.parsing.ast

import com.smeup.rpgparser.interpreter.Evaluator
import com.smeup.rpgparser.interpreter.Value
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
) : Expression(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %SCAN
data class ScanExpr(
    var value: Expression,
    val source: Expression,
    val start: Expression? = null,
    override val position: Position? = null
) : Expression(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %XLATE
data class TranslateExpr(
    var from: Expression,
    var to: Expression,
    var string: Expression,
    val startPos: Expression,
    override val position: Position? = null
) : Expression(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %TRIM
data class TrimExpr(
    var value: Expression,
    val charactersToTrim: Expression? = null,
    override val position: Position? = null
) : Expression(position) {
    override fun render(): String {
        val toTrim = if (this.charactersToTrim != null) ": ${this.charactersToTrim.render()}" else ""
        return "%TRIM(${this.value.render()} $toTrim)"
    }
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %TRIMR
data class TrimrExpr(
    var value: Expression,
    val charactersToTrim: Expression? = null,
    override val position: Position? = null
) : Expression(position) {

    override fun render(): String {
        val toTrim = if (this.charactersToTrim != null) ": ${this.charactersToTrim.render()}" else ""
        return "%TRIMR(${this.value.render()} $toTrim)"
    }
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %TRIML
data class TrimlExpr(
    var value: Expression,
    val charactersToTrim: Expression? = null,
    override val position: Position? = null
) : Expression(position) {

    override fun render(): String {
        val toTrim = if (this.charactersToTrim != null) ": ${this.charactersToTrim.render()}" else ""
        return "%TRIMR(${this.value.render()} $toTrim)"
    }
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %SUBST
data class SubstExpr(
    var string: Expression,
    val start: Expression,
    val length: Expression? = null,
    override val position: Position? = null
) :
        AssignableExpression(position) {
    override fun render(): String {
        val len = if (length != null) ": ${length.render()}" else ""
        return "%SUBST(${this.string.render()} : ${start.render()} $len)"
    }
    override fun size(): Int {
        TODO("size")
    }
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %LEN
data class LenExpr(var value: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render(): String {
        return "%LEN(${this.value.render()})"
    }
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %REM
data class RemExpr(
    val dividend: Expression,
    val divisor: Expression,
    override val position: Position? = null
) : Expression(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %DEC
data class DecExpr(
    var value: Expression,
    var intDigits: Expression,
    val decDigits: Expression,
    override val position: Position? = null
) : Expression(position) {
    override fun render(): String {
        return "${this.value.render()}"
    }
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %INT
data class IntExpr(
    var value: Expression,
    override val position: Position? = null
) :
    Expression(position) {
    override fun render(): String {
        return "${this.value.render()}"
    }
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %SQRT
data class SqrtExpr(var value: Expression, override val position: Position? = null) :
        Expression(position) {
    override fun render(): String {
        return "${this.value.render()}"
    }
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %EDITC
// TODO add other parameters
data class EditcExpr(
    var value: Expression,
    val format: Expression,
    override val position: Position? = null
) : Expression(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %EDITW
// TODO add other parameters
data class EditwExpr(
    var value: Expression,
    val format: Expression,
    override val position: Position? = null
) : Expression(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %FOUND
data class FoundExpr(
    var name: String? = null,
    override val position: Position? = null
) : Expression(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %EOF
data class EofExpr(
    var name: String? = null,
    override val position: Position? = null
) : Expression(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %EQUAL
data class EqualExpr(
    var name: String? = null,
    override val position: Position? = null
) : Expression(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %ABS
data class AbsExpr(
    var value: Expression,
    override val position: Position? = null
) : Expression(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %CHAR
data class CharExpr(var value: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render(): String {
        return "%CHAR(${value.render()})"
    }
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %TIMESTAMP
data class TimeStampExpr(val value: Expression?, override val position: Position? = null) :
    Expression(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %DIFF
data class DiffExpr(
    var value1: Expression,
    var value2: Expression,
    val durationCode: Expression,
    override val position: Position? = null
) :
    Expression(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %REPLACE
data class ReplaceExpr(
    val replacement: Expression,
    val source: Expression,
    val start: Expression? = null,
    val length: Expression? = null,
    override val position: Position? = null
) :
    Expression(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// TODO Move and handle different types of duration
// TODO document what a duration code is
data class DurationCodeExpr(override var position: Position? = null) :
    Expression(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}
