package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.DataRefExpr
import com.smeup.rpgparser.parsing.ast.Expression
import com.smeup.rpgparser.parsing.parsetreetoast.RpgType
import java.lang.IllegalStateException
import kotlin.math.ceil
import com.smeup.rpgparser.parsing.ast.*
import java.math.BigDecimal

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

sealed class Type {
    open fun numberOfElements(): Int {
        return 1
    }
    open fun elementSize(): Long {
        return size
    }

    open fun canBeAssigned(value: Value): Boolean {
        return value.assignableTo(this)
    }

    abstract val size: Long

    fun toArray(nElements: Int) = ArrayType(this, nElements)
    fun isArray() = this is ArrayType
    open fun asArray(): ArrayType {
        throw IllegalStateException("Not an ArrayType")
    }
}

object KListType : Type() {
    override val size: Long
        get() = 0

    override fun canBeAssigned(value: Value): Boolean = false
}

data class DataStructureType(val fields: List<FieldType>, val elementSize: Int) : Type() {
    override val size: Long
        get() = elementSize.toLong()
}

data class StringType(val length: Long,val varying: Boolean = false) : Type() {
    override val size: Long
        get() = length

}

object BooleanType : Type() {
    override val size: Long
        get() = 1
}

object TimeStampType : Type() {
    override val size: Long
        get() = 26
}

/**
 * A CharacterType is basically very similar to an array of characters
 * and very similar to a string.
 */
data class CharacterType(val nChars: Int) : Type() {
    override val size: Long
        get() = nChars.toLong()
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

data class NumberType(val entireDigits: Int, val decimalDigits: Int, val rpgType: String? = "") : Type() {

    constructor(entireDigits: Int, decimalDigits: Int, rpgType: RpgType) : this(entireDigits, decimalDigits, rpgType.rpgType)

    init {
        if (rpgType == RpgType.INTEGER.rpgType || rpgType == RpgType.UNSIGNED.rpgType) {
            require(entireDigits <= 20) { "Integer or Unsigned integer can have only length up to 20. Value specified: $this" }
            require(decimalDigits == 0)
        }
    }

    override val size: Long
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
            }.toLong()
        }

    val integer: Boolean
        get() = decimalDigits == 0
    val decimal: Boolean
        get() = !integer
    val numberOfDigits: Int
        get() = entireDigits + decimalDigits
}

data class ArrayType(val element: Type, val nElements: Int, val compileTimeRecordsPerLine: Int? = null) : Type() {
    override val size: Long
        get() = element.size * nElements

    override fun numberOfElements(): Int {
        return nElements
    }

    override fun elementSize(): Long {
        return element.size
    }

    override fun asArray(): ArrayType {
        return this
    }

    fun compileTimeArray(): Boolean = compileTimeRecordsPerLine != null
}

data class FieldType(val name: String, val type: Type)

fun Expression.type(): Type {
    return when (this) {
        is DataRefExpr -> {
            this.variable.referred!!.type
        }
        is StringLiteral -> {
            StringType(this.value.length.toLong())
        }
        is IntLiteral -> {
            NumberType(BigDecimal.valueOf(this.value).precision(), decimalDigits = 0)
        }
        is RealLiteral -> {
            NumberType(this.value.precision() - this.value.scale(), this.value.scale())
        }
        is ArrayAccessExpr -> {
            val type = this.array.type().asArray()
            return type.element
        }
        else -> TODO("We do not know how to calculate the type of $this (${this.javaClass.canonicalName})")
    }
}
