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

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

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
    fun replace(start: Int, end: Int, replacingString: String)

    /**
     * Returns a substring from the data structure string.
     *
     * @param start The start index of the substring.
     * @param end The end index of the substring.
     * @return The substring from start to end.
     */
    fun substring(start: Int, end: Int): String

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
         * Creates an instance of DataStructValueBuilder based on the given value and fields.
         *
         * @param value The initial value of the data structure string.
         * @param fields The number of fields to divide the string into.
         * @return An instance of DataStructValueBuilder. The algorithm to create the instance is chosen based on the value and fields.
         */
        fun create(value: String, fields: Int): DataStructValueBuilder {
            val stringSize = value.length

            return if (useIndexedStringBuilder(stringSize = stringSize, fields = fields)) {
                IndexedStringBuilder(value = value, chunksSize = stringSize / fields)
            } else {
                StringBuilderWrapper(value)
            }
        }

        /**
         * Determines whether to use IndexedStringBuilder based on the string size and number of fields.
         *
         * @param stringSize The size of the string.
         * @param fields The number of fields to divide the string into.
         * @return True if IndexedStringBuilder should be used, false otherwise.
         */
        private fun useIndexedStringBuilder(stringSize: Int, fields: Int): Boolean {
            if (stringSize >= 9000) return true
            if (stringSize >= 2000 && fields >= 5) return true
            return false
        }
    }
}

/**
 * A wrapper class for StringBuilder that implements the DataStructValueBuilder interface.
 *
 * @param value The initial value of the string builder.
 */
@Serializable
class StringBuilderWrapper(private val value: String) : DataStructValueBuilder {

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
    override fun replace(start: Int, end: Int, replacingString: String) {
        sb.replace(start, end, replacingString)
    }

    /**
     * Returns a substring from the string builder.
     *
     * @param start The start index of the substring.
     * @param end The end index of the substring.
     * @return The substring from start to end.
     */
    override fun substring(start: Int, end: Int): String {
        return sb.substring(start, end)
    }

    override fun forEach(action: (c: Char) -> Unit) {
        sb.forEach(action)
    }

    override fun isBlank(): Boolean {
        return sb.isBlank()
    }

    override fun chunked(size: Int): List<String> {
        return sb.chunked(size)
    }

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
    override fun toString(): String {
        return sb.toString()
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
@Serializable
class IndexedStringBuilder(private val value: String, val chunksSize: Int) : DataStructValueBuilder {

    // The string is divided into chunks of a fixed size
    @Contextual
    private val chunks: List<@Contextual StringBuilder> = List((value.length + chunksSize - 1) / chunksSize) { index ->
        StringBuilder(value.substring(index * chunksSize, minOf((index + 1) * chunksSize, value.length)))
    }

    override val length = value.length

    /***
     * Replace the substring from start to end with the replacing string.
     * The length of the replacing string must be equal to the length of the replaced string.
     * @param start The start index of the substring to replace
     * @param end The end index of the substring to replace
     */
    override fun replace(start: Int, end: Int, replacingString: String) {
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
    override fun substring(start: Int, end: Int): String {
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

    override fun isBlank(): Boolean {
        return chunks.all { it.isBlank() }
    }

    override fun chunked(size: Int): List<String> {
        return toString().chunked(size)
    }

    override fun replaceAll(value: String): DataStructValueBuilder {
        require(value.length == this.length) { "Value length must be the same of wrapped value." }
        chunks.forEachIndexed { index, chunk ->
            val start = index * chunksSize
            val end = minOf(start + chunksSize, this.length)
            chunk.replace(0, chunk.length, value.substring(start, end))
        }
        return this
    }

    override fun toString(): String {
        return chunks.joinToString(separator = "") { it.toString() }
    }
}