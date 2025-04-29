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

package com.smeup.rpgparser.interpreter.serialization

import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.parsetreetoast.RpgType
import com.smeup.rpgparser.test.doubles
import com.smeup.rpgparser.test.forAll
import com.smeup.rpgparser.test.longs
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.math.BigDecimal
import java.time.ZoneId
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

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
        checkValueSerialization(TimeStampValue(aDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()), true)
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
    fun `DataStructValue with IndexedStringBuilder can be serialized to Json`() {
        val rawStringValue = " Hello world 123 "
        val dsValue = DataStructValue(value = IndexedStringBuilder(rawStringValue, 2))
        checkValueSerialization(dsValue, true)
    }

    @Test
    fun `DataStructValue with StringBuilderWrapper can be serialized to Json`() {
        val rawStringValue = " Hello world 123 "
        val dsValue = DataStructValue(value = StringBuilderWrapper(rawStringValue))
        checkValueSerialization(dsValue, true)
    }

    @Test
    fun `a map with Values can be serialized to Json`() {
        val aLongNumber = 6969L
        val decimalValue = DecimalValue(BigDecimal(aLongNumber))
        val intValue = IntValue(aLongNumber)
        val stringValue = StringValue(aLongNumber.toString())
        val booleanValue = BooleanValue.TRUE
        val timeStampValue = TimeStampValue.now()
        val characterValue = CharacterValue("Hello world".toCharArray().toTypedArray())
        val arrayValue =
            ConcreteArrayValue(
                mutableListOf<Value>(IntValue(1), IntValue(2), IntValue(3)),
                NumberType(3, 0, RpgType.INTEGER)
            )
        val dsValue = DataStructValue(" test 11233 ")

        val originalMap = mapOf(
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

    @Test
    fun `DataStructValue with UnlimitedStringType can be serialized to Json`() {
        val rawStringValue = " Hello world 123 "
        val dsValue = DataStructValue(rawStringValue)
        val fieldDefinition = FieldDefinition(name = "myField", type = UnlimitedStringType, explicitStartOffset = -1, explicitEndOffset = -1)
        val value = UnlimitedStringValue("myValue")
        dsValue.set(fieldDefinition, value)
        checkValueSerialization(dsValue, true)
    }

    @Test
    fun `BlanksValue to Json`() {
        checkValueSerialization(BlanksValue)
    }

    @Test
    fun `NullValue to Json`() {
        checkValueSerialization(NullValue)
    }

    @Test
    fun `ZeroValue to Json`() {
        checkValueSerialization(ZeroValue)
    }

    @Test
    fun `PointerValue to Json`() {
        checkValueSerialization(PointerValue(10))
    }

    @Test
    fun `IsoValue to Json`() {
        checkValueSerialization(IsoValue)
    }

    @Test
    fun `HiValValue to Json`() {
        checkValueSerialization(HiValValue)
    }

    @Test
    fun `LowValValue to Json`() {
        checkValueSerialization(LowValValue)
    }

    @Test
    fun `JulValue to Json`() {
        checkValueSerialization(JulValue)
    }
}
