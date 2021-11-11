/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smeup.rpgparser.parsing.ast

import com.smeup.rpgparser.interpreter.Evaluator
import com.smeup.rpgparser.interpreter.Value
import com.strumenta.kolasu.model.Position
import kotlinx.serialization.Serializable

// %LOOKUP
// To be supported:
// * %LOOKUPLT
// * %LOOKUPLE
// * %LOOKUPGT
// * %LOOKUPGE
@Serializable
data class LookupExpr(
    var searchedValued: Expression,
    val array: Expression,
    val start: Expression? = null,
    val length: Expression? = null,
    override val position: Position? = null
) : Expression(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %SCAN
@Serializable
data class ScanExpr(
    var value: Expression,
    val source: Expression,
    val start: Expression? = null,
    override val position: Position? = null
) : Expression(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %XLATE
@Serializable
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
@Serializable
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
@Serializable
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
@Serializable
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
@Serializable
data class SubstExpr(
    var string: Expression,
    // i don't know but fix: Error com.strumenta.kolasu.model.ImmutablePropertyException: Cannot mutate property 'start' of node FunctionCall(
    var start: Expression,
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
@Serializable
data class LenExpr(var value: Expression, override val position: Position? = null) :
    Expression(position) {
    override fun render(): String {
        return "%LEN(${this.value.render()})"
    }
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %REM
@Serializable
data class RemExpr(
    val dividend: Expression,
    val divisor: Expression,
    override val position: Position? = null
) : Expression(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %DEC
@Serializable
data class DecExpr(
    var value: Expression,
    var intDigits: Expression,
    val decDigits: Expression,
    override val position: Position? = null
) : Expression(position) {
    override fun render(): String {
        return this.value.render()
    }
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %INT
@Serializable
data class IntExpr(
    var value: Expression,
    override val position: Position? = null
) :
    Expression(position) {
    override fun render(): String = "${this.value.render()}"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %SQRT
@Serializable
data class SqrtExpr(var value: Expression, override val position: Position? = null) :
        Expression(position) {
    override fun render(): String {
        return this.value.render()
    }
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %EDITC
// TODO add other parameters
@Serializable
data class EditcExpr(
    var value: Expression,
    val format: Expression,
    override val position: Position? = null
) : Expression(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %EDITW
// TODO add other parameters
@Serializable
data class EditwExpr(
    var value: Expression,
    val format: Expression,
    override val position: Position? = null
) : Expression(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %FOUND
@Serializable
data class FoundExpr(
    var name: String? = null,
    override val position: Position? = null
) : Expression(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %EOF
@Serializable
data class EofExpr(
    var name: String? = null,
    override val position: Position? = null
) : Expression(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %PARMS
@Serializable
data class ParmsExpr(
    var name: String,
    override val position: Position? = null
) : Expression(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %EQUAL
@Serializable
data class EqualExpr(
    var name: String? = null,
    override val position: Position? = null
) : Expression(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %ABS
@Serializable
data class AbsExpr(
    var value: Expression,
    override val position: Position? = null
) : Expression(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %CHAR
@Serializable
data class CharExpr(var value: Expression, val format: String?, override val position: Position? = null) :
    Expression(position) {
    override fun render(): String {
        return "%CHAR(${value.render()})"
    }
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %TIMESTAMP
@Serializable
data class TimeStampExpr(val value: Expression?, override val position: Position? = null) :
    Expression(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %DIFF
@Serializable
data class DiffExpr(
    var value1: Expression,
    var value2: Expression,
    val durationCode: DurationCode,
    override val position: Position? = null
) :
    Expression(position) {
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %REPLACE
@Serializable
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

@Serializable
sealed class DurationCode
@Serializable
object DurationInMSecs : DurationCode()
@Serializable
object DurationInDays : DurationCode()
@Serializable
object DurationInSecs : DurationCode()
@Serializable
object DurationInMinutes : DurationCode()
@Serializable
object DurationInHours : DurationCode()
@Serializable
object DurationInMonths : DurationCode()
@Serializable
object DurationInYears : DurationCode()
