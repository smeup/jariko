package com.smeup.rpgparser.interpreter.serialization

import com.smeup.rpgparser.interpreter.DecimalValue
import com.smeup.rpgparser.interpreter.IntValue
import com.smeup.rpgparser.interpreter.TimeStampValue
import com.smeup.rpgparser.interpreter.Value
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

    @Test
    fun `IntValue can be serialized to Json`() {
        val numericValue: Long = 6969
        val intValue = IntValue(numericValue)
        val string = Json.encodeToString(intValue)

        println(string)
        assertTrue(string.contains("$numericValue"))

        val deserializedIntValue = Json.decodeFromString<IntValue>(string)
        assertEquals(intValue, deserializedIntValue)
    }

    @Test
    fun `DecimalValue can be serialized to Json`() {
        val numericValue = BigDecimal(6969.3333)
        val decimalValue = DecimalValue(numericValue)
        val string = format.encodeToString(decimalValue)

        println(string)
        assertTrue(string.contains("$numericValue"))

        val deserializedDecimalValue = format.decodeFromString<DecimalValue>(string)
        assertEquals(decimalValue, deserializedDecimalValue)
    }

    @Test
    fun `TimeStampValue can be serialized to Json`() {
        val aDate = GregorianCalendar(2020, Calendar.JANUARY, 15).time
        val timeStampValue = TimeStampValue(aDate)
        val string = format.encodeToString(timeStampValue)

        println(string)

        val deserializedTimeStampValue = format.decodeFromString<TimeStampValue>(string)
        assertEquals(timeStampValue, deserializedTimeStampValue)
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

        val string = format.encodeToString(originalMap)

        println(string)

        val deserializedMap = format.decodeFromString<Map<String, Value>>(string)
        assertEquals(originalMap, deserializedMap)
    }
}
