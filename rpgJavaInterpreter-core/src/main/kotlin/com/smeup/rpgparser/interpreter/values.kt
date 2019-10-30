package com.smeup.rpgparser.interpreter

import java.lang.Exception
import java.lang.RuntimeException
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import kotlin.streams.toList

const val PAD_CHAR = '\u0000'
const val PAD_STRING = PAD_CHAR.toString()

abstract class Value {
    open fun asInt(): IntValue = throw UnsupportedOperationException("${this.javaClass.simpleName} cannot be seen as an Int")
    open fun asDecimal(): DecimalValue = throw UnsupportedOperationException("${this.javaClass.simpleName} cannot be seen as an Decimal")
    open fun asString(): StringValue = throw UnsupportedOperationException()
    open fun asBoolean(): BooleanValue = throw UnsupportedOperationException()
    open fun asTimeStamp(): TimeStampValue = throw UnsupportedOperationException()
    abstract fun assignableTo(expectedType: Type): Boolean
    open fun takeLast(n: Int): Value = TODO("takeLast not yet implemented for ${this.javaClass.simpleName}")
    open fun takeFirst(n: Int): Value = TODO("takeFirst not yet implemented for ${this.javaClass.simpleName}")
    open fun concatenate(other: Value): Value = TODO("concatenate not yet implemented for ${this.javaClass.simpleName}")
    open fun asArray(): ArrayValue = throw UnsupportedOperationException()
    open fun render(): String = "Nope"
}

interface NumberValue {
    fun negate(): Value
}

// TODO Should we change value to a val in order tho share instances?
data class StringValue(var value: String) : Value() {
    override fun assignableTo(expectedType: Type): Boolean {
        return when (expectedType) {
            is StringType -> expectedType.length >= value.length.toLong()
            is DataStructureType -> expectedType.elementSize == value.length // Check for >= ???
            else -> false
        }
    }

    override fun takeLast(n: Int): Value {
        return StringValue(value.takeLast(n))
    }

    override fun takeFirst(n: Int): Value {
        return StringValue(value.take(n))
    }

    override fun concatenate(other: Value): Value {
        require(other is StringValue)
        return StringValue(value + other.value)
    }

    val valueWithoutPadding: String
        get() = value.removeNullChars()

    companion object {
        fun blank(length: Int) = StringValue(PAD_STRING.repeat(length))
        fun padded(value: String, size: Int) = StringValue(value.padEnd(size, PAD_CHAR))
    }

    override fun equals(other: Any?): Boolean {
        return if (other is StringValue) {
            this.valueWithoutPadding == other.valueWithoutPadding
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return valueWithoutPadding.hashCode()
    }

    fun setSubstring(startOffset: Int, endOffset: Int, substringValue: StringValue) {
        require(startOffset >= 0)
        require(startOffset <= value.length)
        require(endOffset >= startOffset)
        require(endOffset <= value.length) { "Asked startOffset=$startOffset, endOffset=$endOffset on string of length ${value.length}" }
        require(endOffset - startOffset == substringValue.value.length)
        val newValue = value.substring(0, startOffset) + substringValue.value + value.substring(endOffset)
        value = newValue.replace('\u0000', ' ')
    }

    fun getSubstring(startOffset: Int, endOffset: Int): StringValue {
        require(startOffset >= 0)
        require(startOffset <= value.length)
        require(endOffset >= startOffset)
        require(endOffset <= value.length) { "Asked startOffset=$startOffset, endOffset=$endOffset on string of length ${value.length}" }
        val s = value.substring(startOffset, endOffset)
        return StringValue(s)
    }

    override fun toString(): String {
        return "StringValue[${value.length}]($valueWithoutPadding)"
    }

    override fun asString() = this
    fun isBlank(): Boolean {
        return this.valueWithoutPadding.isBlank()
    }

    override fun render(): String {
        return valueWithoutPadding.toString()
    }
}

fun String.removeNullChars(): String {
    val firstNullChar = this.chars().toList().indexOfFirst { it == 0 }
    return if (firstNullChar == -1) {
        this
    } else {
        this.substring(0, firstNullChar)
    }
}

data class IntValue(val value: Long) : NumberValue, Value() {
    override fun negate(): Value = IntValue(-value)

    override fun assignableTo(expectedType: Type): Boolean {
        // TODO check decimals
        return expectedType is NumberType
    }

    override fun asInt() = this
    // TODO Verify conversion
    override fun asDecimal(): DecimalValue = DecimalValue(BigDecimal(value))

    fun increment() = IntValue(value + 1)

    override fun takeLast(n: Int): Value {
        return IntValue(lastDigits(value, n))
    }

    private fun lastDigits(n: Long, digits: Int): Long {
        return (n % Math.pow(10.0, digits.toDouble())).toLong()
    }

    private fun firstDigits(n: Long, digits: Int): Long {
        var localNr = n
        if (n < 0) {
            localNr = n * -1
        }
        val div = Math.pow(10.0, digits.toDouble()).toInt()
        while (localNr / div > 0) {
            localNr /= 10
        }
        return localNr * java.lang.Long.signum(n)
    }

    override fun takeFirst(n: Int): Value {
        return IntValue(firstDigits(value, n))
    }

    override fun concatenate(other: Value): Value {
        require(other is IntValue)
        return IntValue((value.toString() + other.value.toString()).toLong())
    }

    companion object {
        val ZERO = IntValue(0)
    }

    override fun render(): String {
        return value.toString()
    }
}

data class DecimalValue(val value: BigDecimal) : NumberValue, Value() {
    override fun negate(): Value = DecimalValue(-value)
    override fun asInt(): IntValue {

        return IntValue(value.toLong())
    }

    override fun asDecimal(): DecimalValue = this

    override fun assignableTo(expectedType: Type): Boolean {
        // TODO check decimals
        return expectedType is NumberType
    }

    fun isPositive(): Boolean {
        return value >= BigDecimal.ZERO
    }

    companion object {
        val ZERO = DecimalValue(BigDecimal.ZERO)
    }
}

data class BooleanValue(val value: Boolean) : Value() {
    override fun assignableTo(expectedType: Type): Boolean {
        return expectedType is BooleanType
    }

    override fun asBoolean() = this

    override fun asString() = StringValue(if (value) "1" else "0")

    companion object {
        val FALSE = BooleanValue(false)
        val TRUE = BooleanValue(true)
    }
    override fun render(): String {
        return value.toString()
    }
}

data class CharacterValue(val value: Array<Char>) : Value() {
    override fun assignableTo(expectedType: Type): Boolean {
        return expectedType is CharacterType
    }
}

data class TimeStampValue(val value: Date) : Value() {
    override fun assignableTo(expectedType: Type): Boolean {
        return expectedType is TimeStampType
    }

    override fun asTimeStamp() = this

    companion object {
        val LOVAL = TimeStampValue(GregorianCalendar(0, Calendar.JANUARY, 0).time)
    }
}

abstract class ArrayValue : Value() {
    abstract fun arrayLength(): Int
    abstract fun elementSize(): Int
    abstract fun setElement(index: Int, value: Value)
    abstract fun getElement(index: Int): Value
    fun elements(): List<Value> {
        val elements = LinkedList<Value>()
        for (i in 0 until (arrayLength())) {
            elements.add(getElement(i + 1))
        }
        return elements
    }

    override fun asString(): StringValue {
        return StringValue(elements().map { it.asString() }.joinToString(""))
    }

    override fun assignableTo(expectedType: Type): Boolean {
        if (expectedType is DataStructureType) {
            // FIXME
            return true
        }
        if (expectedType is ArrayType) {
            return elements().all { it.assignableTo(expectedType.element) }
        }
        if (expectedType is StringType) {
            return expectedType.length >= arrayLength() * elementSize()
        }
        return false
    }
    override fun render(): String {
        return "Array(${elements().size})"
    }
    override fun asArray() = this
}
data class ConcreteArrayValue(val elements: MutableList<Value>, val elementType: Type) : ArrayValue() {
    override fun elementSize() = elementType.size.toInt()

    override fun arrayLength() = elements.size

    override fun setElement(index: Int, value: Value) {
        require(index >= 1)
        require(index <= arrayLength())
        require(value.assignableTo(elementType))
        elements[index - 1] = value
    }

    override fun getElement(index: Int): Value {
        require(index >= 1)
        require(index <= arrayLength())
        return elements[index - 1]
    }
}

object BlanksValue : Value() {
    override fun toString(): String {
        return "BlanksValue"
    }

    override fun assignableTo(expectedType: Type): Boolean {
        // FIXME
        return true
    }
}

object HiValValue : Value() {
    override fun toString(): String {
        return "HiValValue"
    }

    override fun assignableTo(expectedType: Type): Boolean {
        // FIXME
        return true
    }
}
object LowValValue : Value() {
    override fun toString(): String {
        return "LowValValue"
    }

    override fun assignableTo(expectedType: Type): Boolean {
        // FIXME
        return true
    }
}

class StructValue(val elements: MutableMap<FieldDefinition, Value>) : Value() {
    override fun assignableTo(expectedType: Type): Boolean {
        // FIXME
        return true
    }
}

class ProjectedArrayValue(val container: ArrayValue, val field: FieldDefinition) : ArrayValue() {
    override fun elementSize(): Int {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun arrayLength() = container.arrayLength()

    override fun setElement(index: Int, value: Value) {
        require(index >= 1)
        require(index <= arrayLength())
        require(value.assignableTo((field.type as ArrayType).element)) { "Assigning to field $field incompatible value $value" }
        val containerElement = container.getElement(index)
        // TODO to review
        if (containerElement is StringValue) {
            if (value is StringValue) {
                containerElement.setSubstring(field.startOffset, field.endOffset, value)
            } else if (value is IntValue) {
                var s = value.value.toString()
                val pad = s.padStart(field.endOffset - field.startOffset)
                containerElement.setSubstring(field.startOffset, field.endOffset, StringValue(pad))
            } else {
                TODO()
            }
        } else if (containerElement is DataStructValue) {
            if (value is StringValue) {
                containerElement.setSubstring(field.startOffset, field.endOffset, value)
            } else if (value is IntValue) {
                var s = value.value.toString()
                val pad = s.padStart(field.endOffset - field.startOffset)
                containerElement.setSubstring(field.startOffset, field.endOffset, StringValue(pad))
            } else {
                TODO()
            }
        }
    }

    override fun getElement(index: Int): Value {
        val containerElement = container.getElement(index)
        if (containerElement is StringValue) {
            return containerElement.getSubstring(field.startOffset, field.endOffset)
        } else if (containerElement is DataStructValue) {
            return containerElement.getSubstring(field.startOffset, field.endOffset)
        } else {
            TODO()
        }
    }
}

fun createArrayValue(elementType: Type, n: Int, creator: (Int) -> Value) = ConcreteArrayValue(Array(n, creator).toMutableList(), elementType)

fun blankString(length: Int) = StringValue(PAD_STRING.repeat(length))

fun Long.asValue() = IntValue(this)

fun String.asValue() = StringValue(this)

private const val FORMAT_DATE_ISO = "yyyy-MM-dd-HH.mm.ss.SSS"

fun String.asIsoDate(): Date {
    return SimpleDateFormat(FORMAT_DATE_ISO).parse(this.take(FORMAT_DATE_ISO.length))
}

fun Type.blank(): Value {
    return when (this) {
        is ArrayType -> createArrayValue(this.element, this.nElements) {
            this.element.blank()
        }
        is DataStructureType -> DataStructValue.blank(this.size.toInt())
        is StringType -> StringValue.blank(this.size.toInt())
        is NumberType -> IntValue(0)
        is BooleanType -> BooleanValue(false)
        is TimeStampType -> TimeStampValue.LOVAL
        is KListType -> throw UnsupportedOperationException("Blank value not supported for KList")
        is CharacterType -> CharacterValue(Array(this.nChars) { ' ' })
        else -> TODO("I do not know how to produce a blank $this")
    }
}

// StringValue wrapper
data class DataStructValue(var value: String) : Value() {
    override fun assignableTo(expectedType: Type): Boolean {
        return when (expectedType) {
            is DataStructureType -> expectedType.elementSize == value.length // Check for >= ???
            is StringType -> expectedType.size == this.value.length.toLong()
            else -> false
        }
    }

    fun set(field: FieldDefinition, value: Value) {
        val v = field.toDataStructureValue(value)
        val startIndex = field.startOffset
        val endIndex = field.startOffset + field.size.toInt()
        try {
            this.setSubstring(startIndex, endIndex, v)
        } catch (e: Exception) {
            throw RuntimeException("Issue arose while setting field ${field.name}. Indexes: $startIndex to $endIndex. Field size: ${field.size}", e)
        }
    }

    operator fun get(data: FieldDefinition): Value {
        return coerce(this.getSubstring(data.startOffset, data.endOffset), data.type)
    }

    val valueWithoutPadding: String
        get() = value.removeNullChars()

    fun setSubstring(startOffset: Int, endOffset: Int, substringValue: StringValue) {
        require(startOffset >= 0)
        require(startOffset <= value.length)
        require(endOffset >= startOffset)
        require(endOffset <= value.length) { "Asked startOffset=$startOffset, endOffset=$endOffset on string of length ${value.length}" }
        require(endOffset - startOffset == substringValue.value.length)
        val newValue = value.substring(0, startOffset) + substringValue.value + value.substring(endOffset)
        value = newValue.replace('\u0000', ' ')
    }

    fun getSubstring(startOffset: Int, endOffset: Int): StringValue {
        require(startOffset >= 0)
        require(startOffset <= value.length)
        require(endOffset >= startOffset)
        require(endOffset <= value.length) { "Asked startOffset=$startOffset, endOffset=$endOffset on string of length ${value.length}" }
        val s = value.substring(startOffset, endOffset)
        return StringValue(s)
    }

    companion object {
        fun blank(length: Int) = DataStructValue(PAD_STRING.repeat(length))
    }
    override fun toString(): String {
        return "StringValue[${value.length}]($valueWithoutPadding)"
    }

    override fun asString() = StringValue(this.value)
    fun isBlank(): Boolean {
        return this.valueWithoutPadding.isBlank()
    }
}
