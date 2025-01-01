package com.smeup.rpgparser.interpreter

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

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
