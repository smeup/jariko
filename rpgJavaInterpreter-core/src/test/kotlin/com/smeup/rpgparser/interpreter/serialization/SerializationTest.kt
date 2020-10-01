package com.smeup.rpgparser.interpreter.serialization

import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.parsetreetoast.RpgType
import com.smeup.rpgparser.test.doubles
import com.smeup.rpgparser.test.forAll
import com.smeup.rpgparser.test.longs
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.math.BigDecimal
import java.util.*
import kotlin.test.*

class SerializationTest {

    private inline fun <reified T> checkValueSerialization(value: T, printValues: Boolean = false): Boolean {
        val string = SerializationOption.serializer.encodeToString(value)
        if (printValues) println(string)
        val deserializedValue = SerializationOption.serializer.decodeFromString<T>(string)
        assertEquals(value, deserializedValue)
        return true
    }

    @Test
    fun `IntValue can be serialized to Json`() {
        forAll(longs) {
            checkValueSerialization(IntValue(this))
        }
    }

    @Test
    fun `DecimalValue can be serialized to Json`() {
        forAll(doubles) {
            checkValueSerialization(DecimalValue(BigDecimal.valueOf(this)))
        }
    }

    @Test
    fun `TimeStampValue can be serialized to Json`() {
        val aDate = GregorianCalendar(2020, Calendar.JANUARY, 15).time
        checkValueSerialization(TimeStampValue(aDate), true)
    }

    @Test
    fun `StringValue can be serialized to Json`() {
        val hindiHelloWorld = "नमस्ते दुनिया"
        checkValueSerialization(StringValue(hindiHelloWorld))
    }

    @Test
    fun `CharacterValue can be serialized to Json`() {
        val characterValue = CharacterValue("Hello world".toCharArray().toTypedArray())
        checkValueSerialization(characterValue)
    }

    @Test
    fun `BooleanValue can be serialized to Json`() {
        checkValueSerialization(BooleanValue.TRUE)
        checkValueSerialization(BooleanValue.FALSE)
    }

    @Test
    fun `ConcreteArrayValue can be serialized to Json`() {
        val arrayValue =
            ConcreteArrayValue(
                mutableListOf<Value>(IntValue(1), IntValue(2), IntValue(3)),
                NumberType(3, 0, RpgType.INTEGER)
            )
        checkValueSerialization(arrayValue)
    }

    @Test
    fun `DataStructValue can be serialized to Json`() {
        val rawStringValue = " Hello world 123 "
        val dsValue = DataStructValue(rawStringValue)
        checkValueSerialization(dsValue, true)
    }

    @Test
    fun `a map with Values can be serialized to Json`() {
        val aLongNumber = 6969L
        val decimalValue = DecimalValue(BigDecimal(aLongNumber))
        val intValue = IntValue(aLongNumber)
        val stringValue = StringValue(aLongNumber.toString())
        val booleanValue = BooleanValue.TRUE
        val timeStampValue = TimeStampValue(Date())
        val characterValue = CharacterValue("Hello world".toCharArray().toTypedArray())
        val arrayValue =
            ConcreteArrayValue(
                mutableListOf<Value>(IntValue(1), IntValue(2), IntValue(3)),
                NumberType(3, 0, RpgType.INTEGER)
            )
        val dsValue = DataStructValue(" test 11233 ")

        val originalMap = mapOf<String, Value>(
            "one" to decimalValue,
            "two" to intValue,
            "three" to stringValue,
            "four" to booleanValue,
            "five" to timeStampValue,
            "six" to characterValue,
            "seven" to arrayValue,
            "eighth" to dsValue
        )
        checkValueSerialization(originalMap, printValues = true)
    }
}
