package com.smeup.rpgparser.interpreter.serialization

import com.smeup.rpgparser.interpreter.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.*
import java.math.BigDecimal
import java.util.*
import kotlin.test.*

class SerializationTest {

    private val module = SerializersModule {
        contextual(BigDecimalSerializer)
        contextual(DateAsLongSerializer)
        polymorphic(Value::class) {
            subclass(IntValue::class)
            subclass(DecimalValue::class)
        }
    }

    val format = Json {
        serializersModule = module
    }

    private inline fun <reified T> checkValueSerialization(value: T) {
        val string = format.encodeToString(value)
        println(string)
        val deserializedValue = format.decodeFromString<T>(string)
        assertEquals(value, deserializedValue)
    }

    @Test
    fun `IntValue can be serialized to Json`() {
        val numericValue: Long = 6969
        val intValue = IntValue(numericValue)
        checkValueSerialization(IntValue(numericValue))
    }

    @Test
    fun `DecimalValue can be serialized to Json`() {
        val numericValue = BigDecimal(6969.3333)
        checkValueSerialization(DecimalValue(numericValue))
    }

    @Test
    fun `TimeStampValue can be serialized to Json`() {
        val aDate = GregorianCalendar(2020, Calendar.JANUARY, 15).time
        checkValueSerialization(TimeStampValue(aDate))
    }

    @Test
    fun `StringValue can be serialized to Json`() {
        val hindiHelloWorld = "नमस्ते दुनिया"
        checkValueSerialization(StringValue(hindiHelloWorld))
    }

    @Test
    fun `BooleanValue can be serialized to Json`() {
        checkValueSerialization(BooleanValue.TRUE)
        checkValueSerialization(BooleanValue.FALSE)
    }

    @Test
    fun `a map with IntValue and DecimalValue can be serialized to Json`() {
        val aLongNumber = 6969L
        val decimalValue = DecimalValue(BigDecimal(aLongNumber))
        val intValue = IntValue(aLongNumber)

        val originalMap = mapOf<String, Value>(
            "first" to decimalValue,
            "second" to intValue
        )
        checkValueSerialization(originalMap)
    }
}
