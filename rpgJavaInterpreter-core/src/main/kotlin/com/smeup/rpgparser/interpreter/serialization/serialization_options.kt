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

import com.smeup.dspfparser.linesclassifier.ConstantValue
import com.smeup.dspfparser.linesclassifier.DSPFValue
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.serialization.BigDecimalSerializer
import com.smeup.rpgparser.serialization.LocalDateTimeSerializer
import com.smeup.rpgparser.serialization.StringBuilderSerializer
import kotlinx.serialization.*
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

private val module =
    SerializersModule {
        contextual(BigDecimalSerializer)
        contextual(LocalDateTimeSerializer)
        contextual(StringBuilderSerializer)
        polymorphic(DataStructValueBuilder::class) {
            subclass(StringBuilderWrapper::class)
            subclass(IndexedStringBuilder::class)
        }
        polymorphic(Value::class) {
            subclass(IntValue::class)
            subclass(DecimalValue::class)
            subclass(StringValue::class)
            subclass(BooleanValue::class)
            subclass(TimeStampValue::class)
            subclass(CharacterValue::class)
            subclass(ConcreteArrayValue::class)
            subclass(DataStructValue::class)
            subclass(OccurableDataStructValue::class)
            subclass(UnlimitedStringValue::class)
            subclass(BlanksValue::class)
            subclass(NullValue::class)
            subclass(ZeroValue::class)
            subclass(PointerValue::class)
            subclass(IsoValue::class)
            subclass(HiValValue::class)
            subclass(LowValValue::class)
            subclass(JulValue::class)
            subclass(StartValValue::class)
            subclass(AllValue::class)
            subclass(EndValValue::class)
//        subclass(ProjectedArrayValue::class) TODO: See `ProjectedArrayValue to Json` test for reason.
        }
        polymorphic(DSPFValue::class) {
            subclass(ConstantValue::class)
        }
    }

interface InterpreterSerialization :
    BinaryFormat,
    StringFormat

class BinaryInterpreterSerialization(
    val implementer: BinaryFormat,
) : InterpreterSerialization,
    BinaryFormat by implementer {
    override fun <T> decodeFromString(
        deserializer: DeserializationStrategy<T>,
        string: String,
    ): T = implementer.decodeFromByteArray(deserializer, parseHex(string))

    // Same lowercase hex text as kotlinx encodeToHexString, but HexFormat writes it in one pass into a
    // pre-sized array, while kotlinx appends char by char to a StringBuilder and then lowercases the copy.
    override fun <T> encodeToString(
        serializer: SerializationStrategy<T>,
        value: T,
    ): String = HEX.formatHex(implementer.encodeToByteArray(serializer, value))

    private companion object {
        val HEX: java.util.HexFormat = java.util.HexFormat.of()

        /** Value of each hex digit (either case) by char code, -1 for any other char. */
        val HEX_DIGITS =
            IntArray(128) { -1 }.apply {
                for (c in '0'..'9') this[c.code] = c - '0'
                for (c in 'a'..'f') this[c.code] = c - 'a' + 10
                for (c in 'A'..'F') this[c.code] = c - 'A' + 10
            }

        // Table lookup: about 3x faster than HexFormat.parseHex on the large states saved at every program exit.
        fun parseHex(string: String): ByteArray {
            require(string.length % 2 == 0) { "Hex string must have an even length, it has ${string.length}" }
            val bytes = ByteArray(string.length / 2)
            for (i in bytes.indices) {
                val high = hexDigit(string, 2 * i)
                val low = hexDigit(string, 2 * i + 1)
                bytes[i] = ((high shl 4) or low).toByte()
            }
            return bytes
        }

        private fun hexDigit(
            string: String,
            index: Int,
        ): Int {
            val c = string[index].code
            val digit = if (c < 128) HEX_DIGITS[c] else -1
            require(digit >= 0) { "Invalid hex digit '${string[index]}' at index $index" }
            return digit
        }
    }
}

class StringInterpreterSerialization(
    val implementer: StringFormat,
) : InterpreterSerialization,
    StringFormat by implementer {
    override fun <T> decodeFromByteArray(
        deserializer: DeserializationStrategy<T>,
        bytes: ByteArray,
    ): T = implementer.decodeFromString(deserializer, bytes.toString())

    override fun <T> encodeToByteArray(
        serializer: SerializationStrategy<T>,
        value: T,
    ): ByteArray = implementer.encodeToString(serializer, value).toByteArray()
}

// TODO read the configuration from an external source
const val BINARY_SERIALIZATION = true
const val CLASS_DISCRIMINATOR_TAG = "#class"

object SerializationOption {
    private fun stringFormat() =
        Json {
            serializersModule = module
            classDiscriminator = CLASS_DISCRIMINATOR_TAG
            // See how to set this option
            prettyPrint = false
        }

    private fun binaryFormat() =
        Cbor {
            serializersModule = module
        }

    val serializer by lazy {
        getSerializer(BINARY_SERIALIZATION)
    }

    fun getSerializer(binary: Boolean) = if (binary) binarySerializer else stringSerializer

    val stringSerializer by lazy {
        StringInterpreterSerialization(stringFormat())
    }

    val binarySerializer by lazy {
        BinaryInterpreterSerialization(binaryFormat())
    }
}

/**
 * Value serializer helper
 * */
object ValueSerializer {
    /**
     * Encodes value in json format
     * */
    @JvmStatic
    fun encode(value: Value) = SerializationOption.serializer.encodeToString(value)

    /**
     * Decodes string in a value instance
     * */
    @JvmStatic
    fun decode(string: String): Value = SerializationOption.serializer.decodeFromString(string)
}

/**
 * Map<String, Value> serializer helper.
 * Exposed separately from [ValueSerializer] (rather than as an overload) because Java
 * callers need explicit, non-reified entry points to batch-encode/decode a whole memory
 * slice in a single call, instead of one call per entry.
 * */
object ValueMapSerializer {
    private val serializer: KSerializer<Map<String, Value>> by lazy {
        MapSerializer(String.serializer(), PolymorphicSerializer(Value::class))
    }

    @JvmStatic
    fun encode(values: Map<String, Value>): String = SerializationOption.serializer.encodeToString(serializer, values)

    @JvmStatic
    fun decode(string: String): Map<String, Value> = SerializationOption.serializer.decodeFromString(serializer, string)
}
