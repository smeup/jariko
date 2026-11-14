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

package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.execution.MainExecutionContext
import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Transient
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

/**
 * Interface representing a value of a data structure string.
 * This interface has been created to allow different implementations about how to manipulate the data structure content
 * in a more efficient way.
 */
@Serializable
sealed interface DataStructValueBuilder {
    /**
     * The length of the data structure string.
     */
    val length: Int

    /**
     * Replaces a substring within the data structure string.
     *
     * @param start The start index of the substring to replace.
     * @param end The end index of the substring to replace.
     * @param replacingString The string to replace the substring with, which must have the same length as the substring being replaced.
     */
    fun replace(
        start: Int,
        end: Int,
        replacingString: String,
    )

    /**
     * Returns a substring from the data structure string.
     *
     * @param start The start index of the substring.
     * @param end The end index of the substring.
     * @return The substring from start to end.
     */
    fun substring(
        start: Int,
        end: Int,
    ): String

    /**
     * Performs the given action on each character of the data structure string.
     *
     * @param action The action to be performed on each character.
     */
    fun forEach(action: (c: Char) -> Unit)

    /**
     * Checks if the data structure string is blank (i.e., empty or contains only whitespace characters).
     *
     * @return True if the data structure string is blank, false otherwise.
     */
    fun isBlank(): Boolean

    /**
     * Splits the data structure string into chunks of the specified size.
     *
     * @param size The size of each chunk.
     * @return A list of strings, each representing a chunk of the original string.
     */
    fun chunked(size: Int): List<String>

    /**
     * Replaces all the characters in the data structure string with the given value.
     * @param value The value to replace all the characters with.
     * @return An instance of DataStructValueBuilder.
     * @throws IllegalArgumentException if value is longer than the initial wrapped value.
     */
    fun replaceAll(value: String): DataStructValueBuilder

    companion object {
        /**
         * Creates an instance of DataStructValueBuilder.
         * This method delegates the creation of the instance to the `JarikoCallback.createDataStructValueBuilder` method.
         * @param value The initial value of the data structure string.
         * @param type The type of the data structure.
         * @return An instance of DataStructValueBuilder.
         * @see com.smeup.rpgparser.execution.JarikoCallback.createDataStructValueBuilder
         */
        fun create(
            value: String,
            type: DataStructureType,
        ) = MainExecutionContext
            .getConfiguration()
            .jarikoCallback
            .createDataStructValueBuilder(value, type)
    }
}

/**
 * A wrapper class for StringBuilder that implements the DataStructValueBuilder interface.
 *
 * It is serialized with its current content (see [StringBuilderWrapperSerializer]).
 *
 * @param value The initial value of the string builder.
 */
@Serializable(with = StringBuilderWrapperSerializer::class)
class StringBuilderWrapper(
    private val value: String,
) : DataStructValueBuilder {
    @Transient
    private val sb = StringBuilder(value)

    override val length = sb.length

    /**
     * Replaces a substring within the string builder.
     *
     * @param start The start index of the substring to replace.
     * @param end The end index of the substring to replace.
     * @param replacingString The string to replace the substring with.
     */
    override fun replace(
        start: Int,
        end: Int,
        replacingString: String,
    ) {
        sb.replace(start, end, replacingString)
    }

    /**
     * Returns a substring from the string builder.
     *
     * @param start The start index of the substring.
     * @param end The end index of the substring.
     * @return The substring from start to end.
     */
    override fun substring(
        start: Int,
        end: Int,
    ): String = sb.substring(start, end)

    override fun forEach(action: (c: Char) -> Unit) {
        sb.forEach(action)
    }

    override fun isBlank(): Boolean = sb.isBlank()

    override fun chunked(size: Int): List<String> = sb.chunked(size)

    override fun replaceAll(value: String): DataStructValueBuilder {
        require(value.length <= sb.length) { "Value is longer than the initial wrapped value." }
        sb.clear().append(value)
        return this
    }

    /**
     * Returns the string representation of the string builder.
     *
     * @return The string representation of the string builder.
     */
    override fun toString(): String = sb.toString()
}

/**
 * Serializes a [StringBuilderWrapper] as its current content without trailing blanks, plus its length.
 * Data structures are mostly blanks: writing them all made the serialized program state much bigger and slower.
 *
 * The generated serializer wrote the constructor `value`, the initial content, while every change goes into the
 * transient builder: a data structure modified after its creation was restored with its initial content.
 * Serial name and `value` element are unchanged, so values serialized before can still be read.
 */
object StringBuilderWrapperSerializer : KSerializer<StringBuilderWrapper> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("com.smeup.rpgparser.interpreter.StringBuilderWrapper") {
            element<String>("value")
            element<Int>("length", isOptional = true)
        }

    override fun serialize(
        encoder: Encoder,
        value: StringBuilderWrapper,
    ) {
        encoder.encodeStructure(descriptor) {
            val content = value.toString()
            encodeStringElement(descriptor, 0, content.trimEnd(' '))
            encodeIntElement(descriptor, 1, content.length)
        }
    }

    override fun deserialize(decoder: Decoder): StringBuilderWrapper =
        decoder.decodeStructure(descriptor) {
            var value: String? = null
            var length: Int? = null
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> value = decodeStringElement(descriptor, 0)
                    1 -> length = decodeIntElement(descriptor, 1)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> throw SerializationException("Unexpected element index $index")
                }
            }
            val content = value ?: throw SerializationException("Missing 'value'")
            StringBuilderWrapper(if (length != null) content.padEnd(length) else content)
        }
}

/**
 * Creates a string builder that allows to replace substrings in a more efficient way.
 * The string is divided into chunks of a fixed size.
 * When replacing a substring, only the affected chunks are modified.
 * This is useful when the string is very large and only a small part of it is modified.
 * @param value The initial value of the string builder
 * @param chunksSize The size of the chunks
 */
@Serializable(with = IndexedStringBuilderSerializer::class)
class IndexedStringBuilder(
    private val value: String,
    val chunksSize: Int,
) : DataStructValueBuilder {
    // The string is divided into chunks of a fixed size
    @Contextual
    private val chunks: List<@Contextual StringBuilder> =
        List((value.length + chunksSize - 1) / chunksSize) { index ->
            StringBuilder(value.substring(index * chunksSize, minOf((index + 1) * chunksSize, value.length)))
        }

    override val length = value.length

    /***
     * Replace the substring from start to end with the replacing string.
     * The length of the replacing string must be equal to the length of the replaced string.
     * @param start The start index of the substring to replace
     * @param end The end index of the substring to replace
     */
    override fun replace(
        start: Int,
        end: Int,
        replacingString: String,
    ) {
        require(end > start) { "End index must be greater than start index." }
        require(replacingString.length == end - start) { "Replacing string length must match the length of the substring being replaced." }

        val firstChunkIndex = start / chunksSize
        val lastChunkIndex = (end / chunksSize).let { if (it >= chunks.size) it - 1 else it }
        val subChunks = chunks.subList(firstChunkIndex, lastChunkIndex + 1)
        var remaining = replacingString
        var currentIndex = firstChunkIndex * chunksSize

        for (chunk in subChunks) {
            val chunkStart = currentIndex
            val chunkEnd = currentIndex + chunk.length
            if (start < chunkEnd && end > chunkStart) {
                val relativeStart = maxOf(0, start - chunkStart)
                val relativeEnd = minOf(chunk.length, end - chunkStart)
                val replaceStart = maxOf(0, start - chunkStart - relativeStart)
                val replaceEnd = replaceStart + (relativeEnd - relativeStart)

                chunk.replace(relativeStart, relativeEnd, remaining.substring(replaceStart, replaceEnd))

                remaining = remaining.substring(replaceEnd - replaceStart)
            }

            currentIndex += chunk.length
        }
    }

    /***
     * Returns the substring from start to end.
     * @param start The start index of the substring
     * @param end The end index of the substring
     * @return The substring from start to end
     */
    override fun substring(
        start: Int,
        end: Int,
    ): String {
        require(end >= start) { "End index must be greater than or equal to start index." }

        if (start == end) return ""

        val firstChunkIndex = start / chunksSize
        val lastChunkIndex = (end / chunksSize).let { if (it >= chunks.size) it - 1 else it }
        val subChunks = chunks.subList(firstChunkIndex, lastChunkIndex + 1)

        val result = StringBuilder()
        var currentIndex = firstChunkIndex * chunksSize
        for (chunk in subChunks) {
            val chunkStart = currentIndex
            val chunkEnd = currentIndex + chunk.length

            if (start < chunkEnd && end > chunkStart) {
                val relativeStart = maxOf(0, start - chunkStart)
                val relativeEnd = minOf(chunk.length, end - chunkStart)
                result.append(chunk.substring(relativeStart, relativeEnd))
            }

            currentIndex += chunk.length
        }

        return result.toString()
    }

    override fun forEach(action: (c: Char) -> Unit) {
        chunks.forEach { it.forEach(action) }
    }

    override fun isBlank(): Boolean = chunks.all { it.isBlank() }

    override fun chunked(size: Int): List<String> = toString().chunked(size)

    override fun replaceAll(value: String): DataStructValueBuilder {
        require(value.length == this.length) { "Value length must be the same of wrapped value." }
        chunks.forEachIndexed { index, chunk ->
            val start = index * chunksSize
            val end = minOf(start + chunksSize, this.length)
            chunk.replace(0, chunk.length, value.substring(start, end))
        }
        return this
    }

    override fun toString(): String = chunks.joinToString(separator = "") { it.toString() }
}

/**
 * Serializes an [IndexedStringBuilder] as its current content without trailing blanks, its length and chunk size.
 * Data structures are mostly blanks (fields and arrays never written), so the trailing blanks were most of
 * the serialized program state.
 *
 * The generated serializer wrote the constructor `value` (the initial content, stale after any replace)
 * together with the `chunks` (the current content): every data structure was saved twice. The `chunks`
 * element is still accepted when decoding, and wins over `value`, so values serialized before keep their
 * current content.
 */
object IndexedStringBuilderSerializer : KSerializer<IndexedStringBuilder> {
    private val chunksSerializer = ListSerializer(String.serializer())

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("com.smeup.rpgparser.interpreter.IndexedStringBuilder") {
            element<String>("value")
            element<Int>("chunksSize")
            element("chunks", chunksSerializer.descriptor, isOptional = true)
            element<Int>("length", isOptional = true)
        }

    override fun serialize(
        encoder: Encoder,
        value: IndexedStringBuilder,
    ) {
        encoder.encodeStructure(descriptor) {
            val content = value.toString()
            encodeStringElement(descriptor, 0, content.trimEnd(' '))
            encodeIntElement(descriptor, 1, value.chunksSize)
            encodeIntElement(descriptor, 3, content.length)
        }
    }

    override fun deserialize(decoder: Decoder): IndexedStringBuilder =
        decoder.decodeStructure(descriptor) {
            var value: String? = null
            var chunksSize: Int? = null
            var chunks: List<String>? = null
            var length: Int? = null
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> value = decodeStringElement(descriptor, 0)
                    1 -> chunksSize = decodeIntElement(descriptor, 1)
                    2 -> chunks = decodeSerializableElement(descriptor, 2, chunksSerializer)
                    3 -> length = decodeIntElement(descriptor, 3)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> throw SerializationException("Unexpected element index $index")
                }
            }
            val content = chunks?.joinToString(separator = "") ?: value ?: throw SerializationException("Missing 'value'")
            IndexedStringBuilder(
                if (length != null) content.padEnd(length) else content,
                chunksSize ?: throw SerializationException("Missing 'chunksSize'"),
            )
        }
}

/**
 * Checks if the data structure type contains only array fields.
 *
 * @return `true` if all fields in the data structure type are arrays, `false` otherwise.
 */
internal fun DataStructureType.containsOnlyArrays(): Boolean = fields.all { it.type is ArrayType }

/**
 * Counts the number of fields in the data structure type.
 *
 * @return The total number of fields, including elements of array fields.
 */
internal fun DataStructureType.totalFields(): Int = fields.sumOf { if (it.type is ArrayType) it.type.nElements else 1 }
