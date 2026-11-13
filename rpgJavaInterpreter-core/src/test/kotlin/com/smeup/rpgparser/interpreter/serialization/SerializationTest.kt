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
import com.strumenta.kolasu.model.Point
import com.strumenta.kolasu.model.Position
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToHexString
import kotlinx.serialization.encodeToString
import java.math.BigDecimal
import java.time.ZoneId
import java.util.Calendar
import java.util.GregorianCalendar
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SerializationTest {
    private val valueMapSerializer: KSerializer<Map<String, Value>> =
        MapSerializer(String.serializer(), PolymorphicSerializer(Value::class))

    private fun ByteArray.toHex() = joinToString("") { "%02x".format(it) }

    private inline fun <reified T> checkValueSerialization(
        value: T,
        printValues: Boolean = false,
    ): Boolean {
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
                NumberType(3, 0, RpgType.INTEGER),
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
                NumberType(3, 0, RpgType.INTEGER),
            )
        val dsValue = DataStructValue(" test 11233 ")

        val originalMap =
            mapOf(
                "one" to decimalValue,
                "two" to intValue,
                "three" to stringValue,
                "four" to booleanValue,
                "five" to timeStampValue,
                "six" to characterValue,
                "seven" to arrayValue,
                "eighth" to dsValue,
            )
        checkValueSerialization(originalMap, printValues = true)
    }

    @Test
    fun `a map with Values can be round-tripped via ValueMapSerializer`() {
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
                NumberType(3, 0, RpgType.INTEGER),
            )
        val dsValue = DataStructValue(" test 11233 ")

        val originalMap =
            mapOf(
                "one" to decimalValue,
                "two" to intValue,
                "three" to stringValue,
                "four" to booleanValue,
                "five" to timeStampValue,
                "six" to characterValue,
                "seven" to arrayValue,
                "eighth" to dsValue,
            )

        val encoded = ValueMapSerializer.encode(originalMap)
        val decoded = ValueMapSerializer.decode(encoded)
        assertEquals(originalMap, decoded)
    }

    @Test
    fun `binary serialization keeps the kotlinx hex text and decodes it in either case`() {
        val binary = SerializationOption.binarySerializer
        val serializer: KSerializer<Map<String, Value>> =
            MapSerializer(String.serializer(), PolymorphicSerializer(Value::class))
        val map: Map<String, Value> =
            mapOf("a" to IntValue(42), "b" to StringValue("hello"), "c" to DataStructValue(" ds 1 "))

        val encoded = binary.encodeToString(serializer, map)
        assertEquals(binary.implementer.encodeToHexString(serializer, map), encoded)
        assertEquals(map, binary.decodeFromString(serializer, encoded))
        assertEquals(map, binary.decodeFromString(serializer, encoded.uppercase()))
    }

    @Test
    fun `ValueMapSerializer compresses a mostly blank state and still reads the plain hex format`() {
        val map: Map<String, Value> =
            mapOf("big" to DataStructValue(" ".repeat(500_000)), "n" to IntValue(7), "s" to StringValue("abc"))

        val encoded = ValueMapSerializer.encode(map)
        assertTrue(encoded.startsWith("~"), "expected the deflated format")
        assertTrue(encoded.length < 10_000, "500 KB of blanks should deflate to a few KB, got ${encoded.length} chars")
        assertEquals(map, ValueMapSerializer.decode(encoded))

        val serializer: KSerializer<Map<String, Value>> =
            MapSerializer(String.serializer(), PolymorphicSerializer(Value::class))
        val plainHex = SerializationOption.binarySerializer.encodeToString(serializer, map)
        assertEquals(map, ValueMapSerializer.decode(plainHex))
    }

    @Test
    fun `IndexedStringBuilder is serialized once with its current content`() {
        val builder = IndexedStringBuilder("AAAAAAAAAABBBBBBBBBB", 8)
        builder.replace(2, 5, "xyz")
        val map: Map<String, Value> = mapOf("ds" to DataStructValue(builder))

        val encoded = SerializationOption.binarySerializer.encodeToString(valueMapSerializer, map)
        assertTrue("AAAAAAAAAABBBBBBBBBB".toByteArray().toHex() !in encoded, "the stale initial content must not be written")
        val decoded = SerializationOption.binarySerializer.decodeFromString(valueMapSerializer, encoded)
        assertEquals("AAxyzAAAAABBBBBBBBBB", (decoded["ds"] as DataStructValue).value.toString())
        assertEquals(map, decoded)
        assertEquals(map, ValueMapSerializer.decode(ValueMapSerializer.encode(map)))
    }

    @Test
    fun `IndexedStringBuilder trailing blanks are not serialized but restored`() {
        val builder = IndexedStringBuilder(" ".repeat(100_000), 500)
        builder.replace(10, 13, "abc")
        val map: Map<String, Value> =
            mapOf(
                "ds" to DataStructValue(builder),
                "blank" to DataStructValue(IndexedStringBuilder(" ".repeat(50_000), 500)),
            )

        val encoded = SerializationOption.binarySerializer.encodeToString(valueMapSerializer, map)
        assertTrue(encoded.length < 1_000, "trailing blanks must not be written, got ${encoded.length} hex chars")
        val decoded = SerializationOption.binarySerializer.decodeFromString(valueMapSerializer, encoded)
        assertEquals(100_000, (decoded["ds"] as DataStructValue).value.length)
        assertEquals(map, decoded)
    }

    @Test
    fun `StringValue trailing blanks are not serialized but restored`() {
        val map: Map<String, Value> =
            mapOf(
                "fixed" to StringValue("abc" + " ".repeat(5_000)),
                "blank" to StringValue(" ".repeat(5_000)),
                "varying" to StringValue("ab  ", varying = true),
                "plain" to StringValue("xyz"),
            )

        val encoded = SerializationOption.binarySerializer.encodeToString(valueMapSerializer, map)
        assertTrue(encoded.length < 1_000, "trailing blanks must not be written, got ${encoded.length} hex chars")
        assertEquals(map, SerializationOption.binarySerializer.decodeFromString(valueMapSerializer, encoded))
        assertEquals(
            map,
            SerializationOption.stringSerializer.decodeFromString(
                valueMapSerializer,
                SerializationOption.stringSerializer.encodeToString(valueMapSerializer, map),
            ),
        )
    }

    @Test
    fun `StringValue serialized without length is read as it is`() {
        val json = """{"#class":"com.smeup.rpgparser.interpreter.StringValue","value":"ab  ","varying":true}"""
        assertEquals(StringValue("ab  ", varying = true), SerializationOption.stringSerializer.decodeFromString<Value>(json))
    }

    @Test
    fun `IndexedStringBuilder serialized with value and chunks reads the chunks`() {
        // Written by the generated serializer before IndexedStringBuilderSerializer: stale 'value' plus current 'chunks'
        val oldFormat =
            "bf6264739f782f636f6d2e736d6575702e7270677061727365722e696e7465727072657465722e4461746153747275637456616c7565" +
                "bf6576616c75659f7834636f6d2e736d6575702e7270677061727365722e696e7465727072657465722e496e646578656453747269" +
                "6e674275696c646572bf6576616c75657441414141414141414141424242424242424242426a6368756e6b7353697a6508666368756e" +
                "6b739f68414178797a4141416841414242424242426442424242ffffffffffff"
        val decoded = ValueMapSerializer.decode(oldFormat)
        assertEquals("AAxyzAAAAABBBBBBBBBB", (decoded["ds"] as DataStructValue).value.toString())
    }

    @Test
    fun `DataStructValue with UnlimitedStringType can be serialized to Json`() {
        val rawStringValue = " Hello world 123 "
        val dsValue = DataStructValue(rawStringValue)
        val fieldDefinition =
            FieldDefinition(name = "myField", type = UnlimitedStringType, explicitStartOffset = -1, explicitEndOffset = -1)
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

    @Test
    fun `StartValValue to Json`() {
        checkValueSerialization(StartValValue)
    }

    @Test
    fun `AllValue to Json`() {
        checkValueSerialization(AllValue("F"))
    }

    @Test
    fun `EndValValue to Json`() {
        checkValueSerialization(EndValValue)
    }

    @Test
    @Ignore("TODO: Is necessary to remove `@Transient` from `FieldDefinition.overriddenContainer` and fix `JFTCPR.rpgle` test.")
    fun `ProjectedArrayValue to Json`() {
        val ds1Fl1Type =
            FieldType(
                name = "DS1_FL1",
                type =
                    ArrayType(
                        element = NumberType(entireDigits = 1, decimalDigits = 0, rpgType = RpgType.ZONED),
                        nElements = 9,
                    ),
            )

        val ds1Fl1Definition =
            FieldDefinition(
                name = ds1Fl1Type.name,
                type =
                    ArrayType(
                        element = NumberType(entireDigits = 1, decimalDigits = 0, rpgType = RpgType.ZONED),
                        nElements = 9,
                    ),
                explicitStartOffset = 0,
                explicitEndOffset = 1,
                position = Position(start = Point(2, 5), end = Point(2, 85)),
                declaredArrayInLineOnThisField = 9,
            )
        ds1Fl1Definition.overriddenContainer =
            DataDefinition(
                "DS1",
                type =
                    DataStructureType(
                        fields = listOf(ds1Fl1Type),
                        elementSize = 9,
                    ),
            )

        val projectedArrayValue =
            ProjectedArrayValue(
                container = DataStructValue("111111111"),
                field = ds1Fl1Definition,
                startOffset = 0,
                step = 1,
                arrayLength = 9,
            )

        checkValueSerialization(projectedArrayValue)
    }
}
