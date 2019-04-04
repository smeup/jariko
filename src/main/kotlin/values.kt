package com.smeup.rpgparser

abstract class Value {
    open fun asInt() : IntValue = throw UnsupportedOperationException()
    open fun asBoolean() : BooleanValue = throw UnsupportedOperationException()
}

data class StringValue(var value: String) : Value() {
    fun setSubstring(startOffset: Int, endOffset: Int, substringValue: StringValue) {
        require(startOffset >= 0)
        require(startOffset <= value.length)
        require(endOffset >= startOffset)
        require(endOffset <= value.length)
        value = value.substring(0, startOffset) + substringValue + value.substring(endOffset)
    }
}

data class IntValue(val value: Long) : Value() {
    override fun asInt() = this
}
data class BooleanValue(val value: Boolean) : Value() {
    override fun asBoolean() = this
}
abstract class ArrayValue : Value() {
    abstract fun size() : Int
    abstract fun setElement(index: Int, value: Value)
    abstract fun getElement(index: Int) : Value
}
data class ConcreteArrayValue(val elements: MutableList<Value>) : ArrayValue() {
    override fun size() = elements.size

    override fun setElement(index: Int, value: Value) {
        elements[index] = value
    }

    override fun getElement(index: Int) = elements[index]

}
object BlanksValue : Value()
class StructValue(val elements: MutableMap<FieldDefinition, Value>) : Value()

class ProjectedArrayValue(val container: ArrayValue, val field: FieldDefinition) : ArrayValue() {
    override fun size() = container.size()

    override fun setElement(index: Int, value: Value) {
        val containerElement =  container.getElement(index)
        if (containerElement is StringValue) {
            if (value is StringValue) {
                containerElement.setSubstring(field.startOffset, field.endOffset, value)
            } else {
                TODO()
            }
        } else {
            TODO()
        }
    }

    override fun getElement(index: Int) = (container.getElement(index) as StructValue).elements[field]!!

}

fun createArrayValue(n: Int, creator: (Int) -> Value) = ConcreteArrayValue(Array(n, creator).toMutableList())

fun blankString(length: Int) = StringValue(" ".repeat(length))
