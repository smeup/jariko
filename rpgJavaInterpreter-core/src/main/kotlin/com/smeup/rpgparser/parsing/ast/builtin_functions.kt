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

import com.smeup.rpgparser.interpreter.*
import com.strumenta.kolasu.model.Position
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * For expressions with this interface there isn't resolution but will be called the callback `onMockExpression` and returned a null value.
 */
interface MockExpression {
    val defaultValue get(): Value = NullValue
}

// %LOOKUP
@Serializable
data class LookupExpr(
    var searchedValued: Expression,
    val array: Expression,
    val start: Expression? = null,
    val length: Expression? = null,
    override val position: Position? = null
) : Expression(position) {
    override val loggableEntityName: String
        get() = "%LOOKUP"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %LOOKUPGT
@Serializable
data class LookupGtExpr(
    var searchedValue: Expression,
    val array: Expression,
    val start: Expression? = null,
    val length: Expression? = null,
    override val position: Position? = null
) : Expression(position) {
    override val loggableEntityName: String
        get() = "%LOOKUPGT"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %LOOKUPGE
@Serializable
data class LookupGeExpr(
    var searchedValue: Expression,
    val array: Expression,
    val start: Expression? = null,
    val length: Expression? = null,
    override val position: Position? = null
) : Expression(position) {
    override val loggableEntityName: String
        get() = "%LOOKUPGE"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %LOOKUPLT
@Serializable
data class LookupLtExpr(
    var searchedValue: Expression,
    val array: Expression,
    val start: Expression? = null,
    val length: Expression? = null,
    override val position: Position? = null
) : Expression(position) {
    override val loggableEntityName: String
        get() = "%LOOKUPLT"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %LOOKUPLE
@Serializable
data class LookupLeExpr(
    var searchedValue: Expression,
    val array: Expression,
    val start: Expression? = null,
    val length: Expression? = null,
    override val position: Position? = null
) : Expression(position) {
    override val loggableEntityName: String
        get() = "%LOOKUPLE"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %SCAN
@Serializable
data class ScanExpr(
    var value: Expression,
    var source: Expression,
    val start: Expression? = null,
    val length: Expression? = null,
    override val position: Position? = null
) : Expression(position) {
    override val loggableEntityName: String
        get() = "%SCAN"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %CHECK
@Serializable
data class CheckExpr(
    var value: Expression,
    val source: Expression,
    val start: Expression? = null,
    override val position: Position? = null
) : Expression(position) {
    override val loggableEntityName: String
        get() = "%CHECK"
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
    override val loggableEntityName: String
        get() = "%XLATE"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %TRIM
@Serializable
data class TrimExpr(
    var value: Expression,
    val charactersToTrim: Expression? = null,
    override val position: Position? = null
) : Expression(position) {
    override val loggableEntityName: String
        get() = "%TRIM"
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
    override val loggableEntityName: String
        get() = "%TRIMR"
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
    override val loggableEntityName: String
        get() = "%TRIML"
    override fun render(): String {
        val toTrim = if (this.charactersToTrim != null) ": ${this.charactersToTrim.render()}" else ""
        return "%TRIML(${this.value.render()} $toTrim)"
    }
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %SUBST
@Serializable
data class SubstExpr(
    var string: Expression,
    // i don't know but fix: Error com.strumenta.kolasu.model.ImmutablePropertyException: Cannot mutate property 'start' of node FunctionCall(
    var start: Expression,
    var length: Expression? = null,
    override val position: Position? = null
) : AssignableExpression(position) {
    override val loggableEntityName: String
        get() = "%SUBST"
    override fun render(): String {
        val len = length?.let { ": ${it.render()}" } ?: ""
        return "%SUBST(${this.string.render()} : ${start.render()} $len)"
    }
    override fun size(): Int {
        TODO("size")
    }
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %SUBARR
@Serializable
data class SubarrExpr(
    var array: Expression,
    var start: Expression,
    val numberOfElements: Expression? = null,
    override val position: Position? = null
) : AssignableExpression(position) {
    override val loggableEntityName: String
        get() = "%SUBARR"
    override fun render(): String {
        val len = if (numberOfElements != null) ": ${numberOfElements.render()}" else ""
        return "%SUBARR(${this.array.render()} : ${start.render()} $len)"
    }
    override fun size(): Int {
        TODO("size")
    }
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %LEN
@Serializable
data class LenExpr(var value: Expression, override val position: Position? = null) : AssignableExpression(position) {
    override val loggableEntityName: String
        get() = "%LEN"
    override fun render(): String {
        return "%LEN(${this.value.render()})"
    }

    override fun size(): Int {
        TODO("Not yet implemented")
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
    override val loggableEntityName: String
        get() = "%REM"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// Abstract %DEC definition
@Serializable
abstract class DecExpr(
    override val position: Position? = null
) : Expression(position) {
    override val loggableEntityName: String
        get() = "%DEC"
}

// %DEC with numeric value
@Serializable
data class DecNumericExpr(
    var value: Expression,
    var intDigits: Expression,
    val decDigits: Expression,
    @Transient override val position: Position? = null
) : DecExpr(position) {
    override fun render(): String {
        return this.value.render()
    }
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %DEC with timestamp value
@Serializable
data class DecTimeExpr(
    var timestamp: Expression,
    var format: Expression?,
    @Transient override val position: Position? = null
) : DecExpr(position) {
    override fun render() = timestamp.render()
    override fun evalWith(evaluator: Evaluator) = evaluator.eval(this)
}

// %INT
@Serializable
data class IntExpr(
    var value: Expression,
    override val position: Position? = null
) : Expression(position) {
    override val loggableEntityName: String
        get() = "%INT"
    override fun render(): String = this.value.render()
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %INTH
@Serializable
data class InthExpr(
    var value: Expression,
    override val position: Position? = null
) : Expression(position) {
    override val loggableEntityName: String
        get() = "%INTH"
    override fun render(): String = this.value.render()
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %SQRT
@Serializable
data class SqrtExpr(var value: Expression, override val position: Position? = null) : Expression(position) {
    override val loggableEntityName: String
        get() = "%SQRT"
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
    override val loggableEntityName: String
        get() = "%EDITC"
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
    override val loggableEntityName: String
        get() = "%EDITW"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %FOUND
@Serializable
data class FoundExpr(
    var name: String? = null,
    override val position: Position? = null
) : Expression(position) {
    override val loggableEntityName: String
        get() = "%FOUND"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %EOF
@Serializable
data class EofExpr(
    var name: String? = null,
    override val position: Position? = null
) : Expression(position) {
    override val loggableEntityName: String
        get() = "%EOF"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %PARMS
@Serializable
data class ParmsExpr(
    var name: String,
    override val position: Position? = null
) : Expression(position) {
    override val loggableEntityName: String
        get() = "%PARAMS"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %EQUAL
@Serializable
data class EqualExpr(
    var name: String? = null,
    override val position: Position? = null
) : Expression(position) {
    override val loggableEntityName: String
        get() = "%EQUAL"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %ABS
@Serializable
data class AbsExpr(
    var value: Expression,
    override val position: Position? = null
) : Expression(position) {
    override val loggableEntityName: String
        get() = "%ABS"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %CHAR
@Serializable
data class CharExpr(var value: Expression, val format: String?, override val position: Position? = null) :
    Expression(position) {
    override val loggableEntityName: String
        get() = "%CHAR"
    override fun render(): String {
        return "%CHAR(${value.render()})"
    }
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %TIMESTAMP
@Serializable
data class TimeStampExpr(val value: Expression?, override val position: Position? = null) : Expression(position) {
    override val loggableEntityName: String
        get() = "%TIMESTAMP"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %DIFF
@Serializable
data class DiffExpr(
    var value1: Expression,
    var value2: Expression,
    val durationCode: DurationCode,
    override val position: Position? = null
) : Expression(position) {
    override val loggableEntityName: String
        get() = "%DIFF"
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
) : Expression(position) {
    override val loggableEntityName: String
        get() = "%REPLACE"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %OPEN
@Serializable
data class OpenExpr(
    var name: String? = null,
    override val position: Position? = null
) : Expression(position) {
    override val loggableEntityName: String
        get() = "%OPEN"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %SIZE
@Serializable
data class SizeExpr(var value: Expression, override val position: Position? = null) :
    Expression(position) {

    override fun render(): String {
        return "%SIZE(${this.value.render()})"
    }

    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %XFOOT
@Serializable
data class XFootExpr(var value: Expression, override val position: Position? = null) : Expression(position) {
    override val loggableEntityName get() = "%XFOOT"
    override fun render() = "%XFOOT(${this.value.render()})"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %ADDR
@Serializable
data class AddrExpr(override val position: Position? = null) : Expression(position), MockExpression {
    override val defaultValue: Value get() = PointerValue.NULL
    override val loggableEntityName get() = "%ADDR"
    override fun render() = "%ADDR"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %ALLOC
@Serializable
data class AllocExpr(override val position: Position? = null) : Expression(position), MockExpression {
    override val defaultValue: Value get() = PointerValue.NULL
    override val loggableEntityName get() = "%ALLOC"
    override fun render() = "%ALLOC"
    override fun evalWith(evaluator: Evaluator): Value = evaluator.eval(this)
}

// %REALLOC
@Serializable
data class ReallocExpr(val value: Expression, override val position: Position? = null) : Expression(position), MockExpression {
    override val defaultValue: Value get() = PointerValue.NULL
    override val loggableEntityName get() = "%REALLOC"
    override fun render() = "%REALLOC"
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
