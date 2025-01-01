package com.smeup.rpgparser.interpreter

import kotlin.random.Random
import kotlin.test.*
import kotlin.time.measureTime

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
    fun substringWithinSingleChunk() {
        val builder = IndexedStringBuilder("HelloWorld", 5)
        val result = builder.substring(0, 5)
        assertEquals("Hello", result)
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
}

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
    fun inCaseOfLittleDSUseStringBuilderWrapper() {
        val littleDS = listOf(10, 20, 20, 4, 5)
        val dataStructValueBuilder = DataStructValueBuilder.create(value = "a".repeat(littleDS.sum()), fields = littleDS.size)
        assertIs<StringBuilderWrapper>(dataStructValueBuilder)
    }

    @Test
    fun inCaseOfBigDSUseIndexedStringBuilder() {
        val bigDS = listOf(1000, 2000, 3000, 4000, 5000)
        val dataStructValueBuilder = DataStructValueBuilder.create(value = "a".repeat(bigDS.sum()), fields = bigDS.size)
        assertIs<IndexedStringBuilder>(dataStructValueBuilder)
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
