package com.smeup.rpgparser.interpreter

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.time.DurationUnit
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

    private fun compareStringBuilderAndIndexedStringBuilderPerformance(descriptionTest: String, stringSize: Int, iterations: Int) {
        println("Test: $descriptionTest")
        require(stringSize % 10 == 0) { "stringSize must be a multiple of 10" }
        val replacingString = "b".repeat(stringSize / 10)
        val sbDuration = measureTime {
            for (i in 0 until iterations) {
                val sb = StringBuilder("a".repeat(stringSize))
                val step = stringSize / 10
                for (j in 0 until step) {
                    val start: Int = j * step
                    val end: Int = start + step
                    sb.replace(start, end, replacingString)
                }
            }
        }

        val indexedSbDuration = measureTime {
            for (i in 0 until iterations) {
                val indexedSb = IndexedStringBuilder("a".repeat(stringSize), stringSize / 10)
                val step = stringSize / 10
                for (j in 0 until step) {
                    val start: Int = j*step
                    val end: Int = start + step
                    indexedSb.replace(start, end, replacingString)
                }
            }
        }

        println("StringBuilder: ${sbDuration.toLong(DurationUnit.MILLISECONDS)}ms")
        println("IndexedStringBuilder: ${indexedSbDuration.toLong(DurationUnit.MILLISECONDS)}ms")
    }

    @Test
    fun littleStringFewLoopPerformance() {
        val stringSize = 100_000
        val iterations = 100
        compareStringBuilderAndIndexedStringBuilderPerformance("littleStringFewLoopPerformance", stringSize, iterations)
    }

//    @Test
//    fun evalStringBuilderPerformances() {
//        // DS elements
//        val elements = 1_000
//
//        // Fields in each element
//        val fields = 100
//
//        // Field size
//        val fieldSize = 100
//
//        // DS value as StringBuilder
//        val sb = StringBuilder("a".repeat(elements*fields*fieldSize))
//
//        // DS value as Indexed StringBuilder
//        val indexedSb = MutableList(elements) { StringBuilder("a".repeat(fields*fieldSize)) }
//
//        val replacingString = "b".repeat(fieldSize)
//
//        val randomField = List(fields) { (0..<fields).random() }
//
//        measureTime {
//            // For each element of the list
//            for (i in 0 until elements) {
//                val elementOffset = i * fieldSize * fields
//                for (field in 0 until fields) {
//                    val start = elementOffset + (randomField[field] * fieldSize)
//                    val end = start + fieldSize
//                    sb.replace(start, end, replacingString)
//                }
//            }
//        }.also { println("StringBuilder: ${it.toLong(DurationUnit.MILLISECONDS)}ms") }
//
//        measureTime {
//            // For each element of the list
//            for (i in 0 until elements) {
//                for (field in 0 until fields) {
//                    val start = randomField[field] * fieldSize
//                    val end = start + fieldSize
//                    indexedSb[i].replace(start, end, replacingString)
//                }
//            }
//        }.also { println("Indexed StringBuilder: ${it.toLong(DurationUnit.MILLISECONDS)}ms") }
//
//        assertEquals(sb.toString(), indexedSb.joinToString(separator = ""))
//    }
}