package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.DataRefExpr
import com.smeup.rpgparser.parsing.ast.Expression

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

    fun canBeAssigned(value: Value): Boolean {
        return value.assignableTo(this)
    }

    abstract val size: Long
}
data class DataStructureType(val fields: List<FieldType>, val elementSize: Int) : Type() {

    override val size: Long
        get() = elementSize.toLong()
}

data class StringType(val length: Long) : Type() {
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

object CharacterType : Type() {
    override val size: Long
        get() = 26
}

data class NumberType(val entireDigits: Int, val decimalDigits: Int) : Type() {
    override val size: Long
        get() = (entireDigits + decimalDigits).toLong()
    val integer: Boolean
        get() = decimalDigits == 0
    val decimal: Boolean
        get() = !integer
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

    fun compileTimeArray(): Boolean = compileTimeRecordsPerLine != null
}

data class FieldType(val name: String, val type: Type)

fun Expression.type(): Type {
    return when (this) {
        is DataRefExpr -> this.variable.referred!!.type
        else -> TODO("We do not know how to calculate the type of $this (${this.javaClass.canonicalName})")
    }
}
