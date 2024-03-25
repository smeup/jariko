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

package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.parsing.parsetreetoast.RpgType
import com.smeup.rpgparser.parsing.parsetreetoast.error
import com.smeup.rpgparser.parsing.parsetreetoast.todo
import com.strumenta.kolasu.model.specificProcess
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import kotlin.math.ceil
import kotlin.math.max

// Supported data types:
// * Character Format
// * Numeric Data Type
// * UCS-2 Format
// * Date Data Type
// * Time Data Type
// * Timestamp Data Type
// * Object Data Type
// * Basing Pointer Data Type
// * Procedure Pointer Data Type

@Serializable
sealed class Type {
    open fun numberOfElements(): Int {
        return 1
    }
    open fun elementSize(): Int {
        return size
    }

    open fun canBeAssigned(value: Value): Boolean {
        return value.assignableTo(this)
    }

    open fun canBeAssigned(type: Type): Boolean {
        return this == type
    }

    abstract val size: Int

    fun toArray(nElements: Int) = ArrayType(this, nElements)
    fun isArray() = this is ArrayType
    open fun asArray(): ArrayType {
        throw IllegalStateException("Not an ArrayType")
    }
    open fun hasVariableSize() = false
}

@Serializable
object FigurativeType : Type() {
    override val size: Int
        get() = 0

    override fun canBeAssigned(value: Value): Boolean = true
}

@Serializable
object RecordFormatType : Type() {
    override val size: Int
        get() = 0

    override fun canBeAssigned(value: Value) = value is BlanksValue

    override fun toString(): String {
        return "RecordFormatType"
    }
}

@Serializable
object KListType : Type() {
    override val size: Int
        get() = 0

    override fun canBeAssigned(value: Value): Boolean = false
}

@Serializable
data class DataStructureType(val fields: List<FieldType>, val elementSize: Int) : Type() {
    override val size: Int
        get() = elementSize
}

/**
 * This type models a DS with OCCURS keyword
 * @param dataStructureType DS type
 * @param occurs Occurrences number
 * */
@Serializable
data class OccurableDataStructureType(val dataStructureType: DataStructureType, val occurs: Int) : Type() {
    override val size: Int
        get() = dataStructureType.size
}

@Serializable
data class StringType(val length: Int, val varying: Boolean = false) : Type() {
    override val size: Int
        get() = if (varying) length + 2 else length // varying type get 2 additional bytes containing the size of the data

    /**
     * Creates an instance of StringType in according to [FeatureFlag.UnlimitedStringTypeFlag]
     * */
    internal companion object {
        internal fun createInstance(length: Int, varying: Boolean = false): Type {
            return MainExecutionContext.getSystemInterface()?.let {
                it.getFeaturesFactory().createStringType {
                    StringType(length = length, varying = varying)
                }
            } ?: StringType(length = length, varying = varying)
        }
    }

    override fun hasVariableSize(): Boolean {
        return varying
    }
}

@Serializable
object UnlimitedStringType : Type() {
    override val size: Int
        get() = -1
}

@Serializable
object BooleanType : Type() {
    override val size: Int
        get() = 1

    override fun toString() = this.javaClass.simpleName
}

@Serializable
object HiValType : Type() {
    override val size: Int
        get() = throw IllegalStateException("Has variable size")

    override fun hasVariableSize() = true
}

@Serializable
object LowValType : Type() {
    override val size: Int
        get() = throw IllegalStateException("Has variable size")

    override fun hasVariableSize() = true
}

@Serializable
object TimeStampType : Type() {
    override val size: Int
        get() = 26
}

/**
 * A CharacterType is basically very similar to an array of characters
 * and very similar to a string.
 */
@Serializable
data class CharacterType(val nChars: Int) : Type() {
    override val size: Int
        get() = nChars
}

infix fun Int.pow(exponent: Int): Long {
    require(exponent >= 0)
    return if (exponent == 0) {
        1
    } else {
        this * this.pow(exponent - 1)
    }
}

infix fun Long.log(base: Int): Double {
    return (Math.log(this.toDouble()) / Math.log(base.toDouble()))
}

@Serializable
data class NumberType(val entireDigits: Int, val decimalDigits: Int, val rpgType: String? = "") : Type() {

    constructor(entireDigits: Int, decimalDigits: Int, rpgType: RpgType) : this(entireDigits, decimalDigits, rpgType.rpgType)

    init {
        if (rpgType == RpgType.INTEGER.rpgType || rpgType == RpgType.UNSIGNED.rpgType) {
            require(entireDigits <= 20) { "Integer or Unsigned integer can have only length up to 20. Value specified: $this" }
            require(decimalDigits == 0)
        }
    }

    override val size: Int
        get() {
            return when (rpgType) {
                RpgType.PACKED.rpgType -> ceil((numberOfDigits + 1).toDouble() / 2.toFloat()).toInt()
                RpgType.INTEGER.rpgType, RpgType.UNSIGNED.rpgType -> {
                    when (entireDigits) {
                        in 1..3 -> 1
                        in 4..5 -> 2
                        in 6..10 -> 4
                        in 11..20 -> 8
                        else -> throw IllegalStateException("Only predefined length allowed for integer, signed or unsigned")
                    }
                }
                RpgType.BINARY.rpgType -> {
                    when (entireDigits) {
                        in 1..4 -> 2
                        in 5..9 -> 4
                        else -> throw IllegalStateException("Only predefined length allowed binary ")
                    }
                }
                else -> numberOfDigits
            }
        }

    val integer: Boolean
        get() = decimalDigits == 0
    val decimal: Boolean
        get() = !integer
    val numberOfDigits: Int
        get() = entireDigits + decimalDigits

    override fun canBeAssigned(type: Type): Boolean {
        if (type is NumberType) {
            return type.entireDigits <= this.entireDigits && type.decimalDigits <= this.decimalDigits
        } else {
            return false
        }
    }

    override fun equals(other: Any?): Boolean {
        return if (other is NumberType) {
            val resultDigits = this.entireDigits == other.entireDigits && this.decimalDigits == other.decimalDigits
            if (rpgType?.isNotBlank()!! && other.rpgType?.isNotEmpty()!!) {
                return resultDigits && rpgType == other.rpgType
            }

            return resultDigits
        } else {
            false
        }
    }
}

@Serializable
data class ArrayType(val element: Type, val nElements: Int, val compileTimeRecordsPerLine: Int? = null) : Type() {
    var ascend: Boolean? = null

    override val size: Int
        get() = element.size * nElements

    override fun numberOfElements(): Int {
        return nElements
    }

    override fun elementSize(): Int {
        return element.size
    }

    override fun asArray(): ArrayType {
        return this
    }

    fun compileTimeArray(): Boolean = compileTimeRecordsPerLine != null
}

@Serializable
data class FieldType(val name: String, val type: Type)

fun Expression.type(): Type {
    return when (this) {
        is DataRefExpr -> {
            this.variable.referred!!.type
        }
        is StringLiteral -> {
            StringType(this.value.length, true) // TODO verify if varying has to be true or false here
        }
        is IntLiteral -> {
            NumberType(BigDecimal.valueOf(this.value).precision(), decimalDigits = 0)
        }
        is RealLiteral -> {
            NumberType(this.precision - this.value.scale(), this.value.scale())
        }
        is ArrayAccessExpr -> {
            val type = this.array.type().asArray()
            return type.element
        }
        is IndicatorExpr -> {
            return BooleanType
        }
        is GlobalIndicatorExpr -> {
            return ArrayType(BooleanType, 99)
        }
        is HiValExpr -> {
            return HiValType
        }
        is LowValExpr -> {
            return LowValType
        }
        is SubstExpr -> {
            return this.string.type()
        }
        is SubarrExpr -> {
            return this.array.type()
        }
        is QualifiedAccessExpr -> {
            return this.field.referred!!.type
        }
        is OnRefExpr, is OffRefExpr -> return BooleanType
        is FigurativeConstantRef -> {
            FigurativeType
        }
        is PlusExpr -> {
            val leftType = this.left.type()
            val rightType = this.right.type()
            return when {
                leftType is NumberType && rightType is NumberType -> {
                    NumberType(max(leftType.entireDigits, rightType.entireDigits), max(leftType.decimalDigits, rightType.decimalDigits))
                }
                leftType is ArrayType && rightType is ArrayType -> {
                    leftType
                }
                else -> TODO("We do not know the type of a sum of types $leftType and $rightType")
            }
        }
        is MinusExpr -> {
            val leftType = this.left.type()
            val rightType = this.right.type()
            if (leftType is NumberType && rightType is NumberType) {
                return NumberType(max(leftType.entireDigits, rightType.entireDigits), max(leftType.decimalDigits, rightType.decimalDigits))
            } else {
                TODO("We do not know the type of a subtraction of types $leftType and $rightType")
            }
        }
        // insert is for LenExpr and FunctionCall
        is LenExpr -> {
            val size = when (this.value) {
                // If len argument is a dataref, we have to evaluate it
                // But is absolutely not clear why if the argument is a string of 1-9 chars the len expression type is 1
                // while if is a string of 10-99 chars the len expression type is 2
                // This way the tests don't fail but to me is not clear why
                is DataRefExpr -> (this.value as DataRefExpr).size().toString().length
                // else we need to find the dataref inside the expression
                else -> {
                    var dataRefInsideExpr: DataRefExpr? = null
                    this.value.specificProcess(klass = DataRefExpr::class.java) {
                        dataRefInsideExpr = it
                    }
                    dataRefInsideExpr?.size()?.toString()?.length
                        ?: this.value.error(message = "I don't know how to calculate the length of this expression")
                }
            }
            return NumberType(size, decimalDigits = 0)
        }
        is FunctionCall -> {
            if (this.parent is EvalStmt) {
                (this.parent as EvalStmt).target.type()
            } else {
                todo("Unable to establish FunctionCall '${this.function.name}' return type of which '${this.parent}'.")
            }
        }
        // in all cases the type is computed by analyzing the variables inside the expression
        else -> {
            var size = 0L
            this.specificProcess(klass = DataRefExpr::class.java) {
                size += it.size().toString().length
            }
            if (size == 0L) error("Unable to establish type of expression '$this'.")
            return NumberType(size.toInt(), decimalDigits = 0)
        }
    }
}

fun baseType(type: Type): Type = if (type is ArrayType) type.element else type