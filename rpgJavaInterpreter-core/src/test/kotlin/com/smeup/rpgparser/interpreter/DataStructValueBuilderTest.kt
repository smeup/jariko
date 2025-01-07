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

import com.smeup.rpgparser.parsing.ast.json
import kotlin.random.Random
import kotlin.test.*
import kotlin.time.measureTime

class DataStructValueBuilderTest {

    private fun replaceAllFields(
        dataStructValueBuilder: DataStructValueBuilder,
        replacingChar: Char,
        fieldsLen: List<Int>,
        iteration: Int
    ) {
        for (i in 0 until iteration) {
            var start = 0
            var end = 0
            for (fieldLen in fieldsLen) {
                end += fieldLen
                dataStructValueBuilder.replace(start, end, "$replacingChar".repeat(fieldLen))
                start = end
            }
        }
    }

    @Test
    fun dataStructureUsesStringBuilderWrapper() {
        val fields = List(10) { FieldType(name = "name$it", type = StringType(10, false)) }
        val type = DataStructureType(fields, fields.sumOf { it.type.size })
        val dataStructValueBuilder = DataStructValueBuilder.create(value = "a".repeat(type.size), type = type)
        assertIs<StringBuilderWrapper>(
            value = dataStructValueBuilder,
            message = "Data structure with total fields less than 100 uses StringBuilderWrapper"
        )
    }

    @Test
    fun arrayDataStructureUsesStringBuilderWrapper() {
        val arrayType = ArrayType(StringType(10, false), 99)
        val fields = List(1) { FieldType(name = "name$it", type = arrayType) }
        val type = DataStructureType(fields, fields.sumOf { it.type.size })
        val dataStructValueBuilder = DataStructValueBuilder.create(value = "a".repeat(type.size), type = type)
        assertIs<StringBuilderWrapper>(
            value = dataStructValueBuilder,
            message = "Array data structure with total fields less than 100 uses StringBuilderWrapper"
        )
    }

    @Test
    fun arrayDataStructureUsesIndexedStringBuilder() {
        val arrayType = ArrayType(StringType(10, false), 100)
        val fields = List(1) { FieldType(name = "name$it", type = arrayType) }
        val type = DataStructureType(fields, fields.sumOf { it.type.size })
        val dataStructValueBuilder = DataStructValueBuilder.create(value = "a".repeat(type.size), type = type)
        assertIs<IndexedStringBuilder>(
            value = dataStructValueBuilder,
            message = "Array data structure with total fields equals or greater than 100 uses IndexedStringBuilder"
        )
    }

    @Test
    fun littleDSBetterStringBuilderWrapper() {
        val littleDS = listOf(10, 20, 20, 4, 5)
        val value = "a".repeat(littleDS.sum())
        val stringBuilderWrapper = StringBuilderWrapper(value = value)
        val indexedStringBuilder = IndexedStringBuilder(value = value, chunksSize = value.length / littleDS.size)
        val replacingChar = 'b'
        val checkBuiltString = "$replacingChar".repeat(littleDS.sum())
        val iteration = 10_000
        println("In case of little DS StringBuilderWrapper performance must be better than IndexedStringBuilder")
        val stringBuilderWrapperDuration = measureTime {
            replaceAllFields(
                dataStructValueBuilder = stringBuilderWrapper,
                replacingChar = replacingChar,
                fieldsLen = littleDS,
                iteration = iteration
            )
        }
        val indexedStringBuilderDuration = measureTime {
            replaceAllFields(
                dataStructValueBuilder = indexedStringBuilder,
                replacingChar = replacingChar,
                fieldsLen = littleDS,
                iteration = iteration
            )
        }
        println("stringBuilderWrapperDuration: $stringBuilderWrapperDuration")
        println("indexedStringBuilderDuration: $indexedStringBuilderDuration")
        assertEquals(checkBuiltString, stringBuilderWrapper.toString())
        assertEquals(checkBuiltString, indexedStringBuilder.toString())
        assertTrue { stringBuilderWrapperDuration < indexedStringBuilderDuration }
    }

    @Test
    fun bigDSBetterIndexedStringBuilder() {
        val bigDS = List(100) { Random.nextInt(100, 1000) }
        val value = "a".repeat(bigDS.sum())
        val stringBuilderWrapper = StringBuilderWrapper(value = value)
        val indexedStringBuilder = IndexedStringBuilder(value = value, chunksSize = value.length / bigDS.size)
        val replacingChar = 'b'
        val checkBuiltString = "$replacingChar".repeat(bigDS.sum())
        val iteration = 1000
        println("In case of big DS IndexedStringBuilder performance must be better than StringBuilderWrapper")
        val stringBuilderWrapperDuration = measureTime {
            replaceAllFields(
                dataStructValueBuilder = stringBuilderWrapper,
                replacingChar = replacingChar,
                fieldsLen = bigDS,
                iteration = iteration
            )
        }
        val indexedStringBuilderDuration = measureTime {
            replaceAllFields(
                dataStructValueBuilder = indexedStringBuilder,
                replacingChar = replacingChar,
                fieldsLen = bigDS,
                iteration = iteration
            )
        }
        println("stringBuilderWrapperDuration: $stringBuilderWrapperDuration")
        println("indexedStringBuilderDuration: $indexedStringBuilderDuration")
        assertEquals(checkBuiltString, stringBuilderWrapper.toString())
        assertEquals(checkBuiltString, indexedStringBuilder.toString())
        assertTrue { indexedStringBuilderDuration < stringBuilderWrapperDuration }
    }

    @Test
    fun overlayingDSMuchBetterIndexedStringBuilder() {
        val dsWithOverlay = List(10_000) { Random.nextInt(10, 101) }
        val value = "a".repeat(dsWithOverlay.sum())
        val stringBuilderWrapper = StringBuilderWrapper(value = value)
        val indexedStringBuilder = IndexedStringBuilder(value = value, chunksSize = value.length / dsWithOverlay.size)
        val replacingChar = 'b'
        val checkBuiltString = "$replacingChar".repeat(dsWithOverlay.sum())
        val iteration = 5
        println("In case of DS with overlay IndexedStringBuilder performance must better than StringBuilderWrapper at least one order of magnitude.\"")
        println("Measure StringBuilderWrapper performance")
        val stringBuilderWrapperDuration = measureTime {
            replaceAllFields(
                dataStructValueBuilder = stringBuilderWrapper,
                replacingChar = replacingChar,
                fieldsLen = dsWithOverlay,
                iteration = iteration
            )
        }
        println("Measure IndexedStringBuilder performance")
        val indexedStringBuilderDuration = measureTime {
            replaceAllFields(
                dataStructValueBuilder = indexedStringBuilder,
                replacingChar = replacingChar,
                fieldsLen = dsWithOverlay,
                iteration = iteration
            )
        }
        println("stringBuilderWrapperDuration: $stringBuilderWrapperDuration")
        println("indexedStringBuilderDuration: $indexedStringBuilderDuration")
        assertEquals(checkBuiltString, stringBuilderWrapper.toString())
        assertEquals(checkBuiltString, indexedStringBuilder.toString())
        val ratio = stringBuilderWrapperDuration.div(indexedStringBuilderDuration)
        assertTrue { ratio > 10 }
    }
}

class IndexedStringBuilderTest {

    @Test
    fun replaceWithinSingleChunkFirst() {
        val builder = IndexedStringBuilder("HelloWorld", 5)
        builder.replace(0, 5, "12345")
        assertEquals("12345World", builder.toString())
    }

    @Test
    fun replaceWithinSingleChunkMiddle() {
        val builder = IndexedStringBuilder("HelloWorldHello", 5)
        builder.replace(5, 10, "12345")
        assertEquals("Hello12345Hello", builder.toString())
    }

    @Test
    fun replaceWithinSingleChunkLast() {
        val builder = IndexedStringBuilder("HelloWorldHello", 5)
        builder.replace(10, 15, "12345")
        assertEquals("HelloWorld12345", builder.toString())
    }

    @Test
    fun replaceWithinSubChunkFirst() {
        val builder = IndexedStringBuilder("HelloWorld", 5)
        builder.replace(0, 1, "1")
        assertEquals("1elloWorld", builder.toString())
        builder.replace(1, 2, "1")
        assertEquals("11lloWorld", builder.toString())
    }

    @Test
    fun replaceAllChunks() {
        val builder = IndexedStringBuilder("HelloWorld", 5)
        builder.replace(0, 10, "1234567890")
        assertEquals("1234567890", builder.toString())
    }

    @Test
    fun replaceAcrossMultipleChunks() {
        val builder = IndexedStringBuilder("HelloWorld", 2)
        builder.replace(3, 8, "12345")
        assertEquals("Hel12345ld", builder.toString())
    }

    @Test
    fun replaceWithReplacingStringGreaterThanReplacedRangeThrowsException() {
        val builder = IndexedStringBuilder("HelloWorld", 5)
        assertFailsWith<IllegalArgumentException> {
            builder.replace(0, 5, "HelloWorld")
        }
    }

    @Test
    fun replaceWithReplacingStringLowerThanReplacedRangeThrowsException() {
        val builder = IndexedStringBuilder("HelloWorld", 5)
        assertFailsWith<IllegalArgumentException> {
            builder.replace(0, 5, "H")
        }
    }

    @Test
    fun substringWithinSingleChunkStart() {
        val builder = IndexedStringBuilder("HelloWorld", 5)
        val result = builder.substring(0, 5)
        assertEquals("Hello", result)
    }

    @Test
    fun substringWithinSingleChunkMiddle() {
        val builder = IndexedStringBuilder("HelloWorld", 2)
        val result = builder.substring(2, 4)
        assertEquals("ll", result)
    }

    @Test
    fun substringWithinSingleChunkEnd() {
        val builder = IndexedStringBuilder("HelloWorld", 4)
        val result = builder.substring(5, 10)
        assertEquals("World", result)
    }

    @Test
    fun substringAcrossMultipleChunks() {
        val builder = IndexedStringBuilder("HelloWorld", 5)
        val result = builder.substring(3, 8)
        assertEquals("loWor", result)
    }

    @Test
    fun substringWithExactLength() {
        val builder = IndexedStringBuilder("HelloWorld", 5)
        val result = builder.substring(0, 10)
        assertEquals("HelloWorld", result)
    }

    @Test
    fun substringWithEmptyResult() {
        val builder = IndexedStringBuilder("HelloWorld", 5)
        val result = builder.substring(5, 5)
        assertEquals("", result)
    }

    @Test
    fun forEachExecutesActionOnEachCharacter() {
        val builder = IndexedStringBuilder("HelloWorld", 5)
        val result = StringBuilder()
        builder.forEach { result.append(it) }
        assertEquals("HelloWorld", result.toString())
    }

    @Test
    fun forEachExecutesActionOnEmptyString() {
        val builder = IndexedStringBuilder("", 5)
        val result = StringBuilder()
        builder.forEach { result.append(it) }
        assertEquals("", result.toString())
    }

    @Test
    fun forEachExecutesActionOnSingleCharacter() {
        val builder = IndexedStringBuilder("A", 5)
        val result = StringBuilder()
        builder.forEach { result.append(it) }
        assertEquals("A", result.toString())
    }

    @Test
    fun forEachExecutesActionOnWhitespaceCharacters() {
        val builder = IndexedStringBuilder(" \t\n", 5)
        val result = StringBuilder()
        builder.forEach { result.append(it) }
        assertEquals(" \t\n", result.toString())
    }

    @Test
    fun isBlankReturnsTrueForEmptyString() {
        val builder = IndexedStringBuilder("", 5)
        assertTrue(builder.isBlank())
    }

    @Test
    fun isBlankReturnsTrueForWhitespaceString() {
        val builder = IndexedStringBuilder(" \t\n", 5)
        assertTrue(builder.isBlank())
    }

    @Test
    fun isBlankReturnsFalseForNonEmptyString() {
        val builder = IndexedStringBuilder("HelloWorld", 5)
        assertFalse(builder.isBlank())
    }

    @Test
    fun isBlankReturnsFalseForMixedString() {
        val builder = IndexedStringBuilder(" \t\nHello", 5)
        assertFalse(builder.isBlank())
    }

    @Test
    fun chunkedSplitsStringIntoEqualChunks() {
        val builder = IndexedStringBuilder("HelloWorld", 5)
        val result = builder.chunked(2)
        assertEquals(listOf("He", "ll", "oW", "or", "ld"), result)
    }

    @Test
    fun chunkedSplitsStringWithRemainingCharacters() {
        val builder = IndexedStringBuilder("HelloWorld", 5)
        val result = builder.chunked(3)
        assertEquals(listOf("Hel", "loW", "orl", "d"), result)
    }

    @Test
    fun chunkedSplitsEmptyString() {
        val builder = IndexedStringBuilder("", 5)
        val result = builder.chunked(3)
        assertEquals(emptyList<String>(), result)
    }

    @Test
    fun chunkedSplitsStringWithSingleCharacterChunks() {
        val builder = IndexedStringBuilder("Hello", 5)
        val result = builder.chunked(1)
        assertEquals(listOf("H", "e", "l", "l", "o"), result)
    }

    @Test
    fun chunkedSplitsStringWithChunkSizeGreaterThanStringLength() {
        val builder = IndexedStringBuilder("Hello", 5)
        val result = builder.chunked(10)
        assertEquals(listOf("Hello"), result)
    }

    @Test
    fun replaceAllReplacesEntireStringWithSameLength() {
        val builder = IndexedStringBuilder("HelloWorld", 5)
        builder.replaceAll("1234567890")
        assertEquals("1234567890", builder.toString())
    }

    @Test
    fun replaceAllReplacesStringWithDifferentChunks() {
        val builder = IndexedStringBuilder("HelloWorldHelloWorld", 5)
        builder.replaceAll("12345123451234512345")
        assertEquals("12345123451234512345", builder.toString())
    }

    @Test
    fun replaceAllThrowsExceptionWhenValueIsShorter() {
        val builder = IndexedStringBuilder("HelloWorld", 5)
        assertFailsWith<IllegalArgumentException> {
            builder.replaceAll("12345")
        }
    }

    @Test
    fun replaceAllThrowsExceptionWhenValueIsLonger() {
        val builder = IndexedStringBuilder("HelloWorld", 5)
        assertFailsWith<IllegalArgumentException> {
            builder.replaceAll("12345678901")
        }
    }

    @Test
    fun replaceAllWithEmptyStringThrowsException() {
        val builder = IndexedStringBuilder("HelloWorld", 5)
        assertFailsWith<IllegalArgumentException> {
            builder.replaceAll("")
        }
    }

    @Test
    fun serializationWhenValueIsStringBuilderWrapper() {
        val dataStruct = DataStructValue(value = StringBuilderWrapper("HelloWorld"))
        val serialized = json.encodeToString(DataStructValue.serializer(), dataStruct)
        assertEquals(dataStruct, json.decodeFromString(DataStructValue.serializer(), serialized))
    }

    @Test
    fun serializationWhenValueIsIndexedStringBuilder() {
        val dataStruct = DataStructValue(value = IndexedStringBuilder("HelloWorld", 5))
        val serialized = json.encodeToString(DataStructValue.serializer(), dataStruct)
        assertEquals(dataStruct, json.decodeFromString(DataStructValue.serializer(), serialized))
    }
}
