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
import com.smeup.rpgparser.serialization.BigDecimalSerializer
import com.smeup.rpgparser.serialization.DateAsLongSerializer
import kotlinx.serialization.*
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

private val module = SerializersModule {
    contextual(BigDecimalSerializer)
    contextual(DateAsLongSerializer)
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
    }
}

interface InterpreterSerialization : BinaryFormat, StringFormat

class BinaryInterpreterSerialization(val implementer: BinaryFormat) : InterpreterSerialization,
    BinaryFormat by implementer {
    override fun <T> decodeFromString(deserializer: DeserializationStrategy<T>, string: String): T =
        implementer.decodeFromHexString<T>(deserializer, string)

    override fun <T> encodeToString(serializer: SerializationStrategy<T>, value: T): String =
        implementer.encodeToHexString(serializer, value)
}

class StringInterpreterSerialization(val implementer: StringFormat) : InterpreterSerialization,
    StringFormat by implementer {
    override fun <T> decodeFromByteArray(deserializer: DeserializationStrategy<T>, bytes: ByteArray): T =
        implementer.decodeFromString(deserializer, bytes.toString())

    override fun <T> encodeToByteArray(serializer: SerializationStrategy<T>, value: T): ByteArray =
        implementer.encodeToString(serializer, value).toByteArray()
}

// TODO read the configuration from an external source
const val BINARY_SERIALIZATION = false
const val CLASS_DISCRIMINATOR_TAG = "#class"

object SerializationOption {
    private fun stringFormat() = Json {
        serializersModule = module
        classDiscriminator = CLASS_DISCRIMINATOR_TAG
        // See how to set this option
        prettyPrint = false
    }

    private fun binaryFormat() = Cbor {
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
    fun encode(value: Value) = SerializationOption.stringSerializer.encodeToString(value)

    /**
    * Decodes string in a value instance
    * */
    @JvmStatic
    fun decode(string: String): Value = SerializationOption.stringSerializer.decodeFromString(string)
}
