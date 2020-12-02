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
