package com.smeup.rpgparser

import com.smeup.rpgparser.ast.DataRefExpr
import com.smeup.rpgparser.ast.Expression

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

    abstract val size: Long
}
data class DataStructureType(val fields: List<FieldType>, val elementSize: Int) : Type() {

    override val size: Long
        get()= elementSize.toLong()
}

data class StringType(val length: Long) : Type() {
    override val size: Long
        get()= length
}
object BooleanType : Type() {
    override val size: Long
        get()= 1
}
data class NumberType(val entireDigits: Int, val decimalDigits: Int) : Type() {
    override val size: Long
        get()= (entireDigits + decimalDigits).toLong()
}
data class ArrayType(val element: Type, val nElements: Int) : Type() {
    override val size: Long
        get()= element.size * nElements

    override fun numberOfElements(): Int {
        return nElements
    }

    override fun elementSize(): Long {
        return element.size
    }
}

data class FieldType(val name: String, val type: Type)

fun Expression.type() : Type {
    return when (this) {
        is DataRefExpr -> this.variable.referred!!.type
        else -> TODO(this.javaClass.canonicalName)
    }
}
