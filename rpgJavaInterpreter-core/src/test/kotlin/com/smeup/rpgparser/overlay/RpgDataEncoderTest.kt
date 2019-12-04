package com.smeup.rpgparser.overlay

import com.smeup.rpgparser.PerformanceTest
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.test.forAll
import com.smeup.rpgparser.test.integerGenerator
import org.junit.Test
import org.junit.experimental.categories.Category
import java.math.MathContext
import kotlin.random.Random.Default.nextInt
import kotlin.random.Random.Default.nextLong
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RpgDataEncoderTest {

    @Test
    fun encodeDecodeInteger1() {
        for (i in -128..127) {
            val integer1 = i.toBigDecimal()
            val encoded1 = encodeInteger(integer1, 1)
            assertTrue(encoded1.length == 1)
            val decoded1 = decodeInteger(encoded1, 1)
            assertTrue(integer1.compareTo(decoded1) == 0)
        }
    }

    @Test
    fun encodeDecodeInteger2() {
        for (i in -32768..32767) {
            val integer2 = i.toBigDecimal()
            val encoded2 = encodeInteger(integer2, 2)
            assertTrue(encoded2.length == 2)
            val decoded2 = decodeInteger(encoded2, 2)
            assertTrue(integer2.compareTo(decoded2) == 0)
        }
    }

    @Test
    fun encodeDecodeInteger4() {
        // To speed up the test, is generated a random value
        // uniformly distributed between Int.MIN_VALUE
        // and Int.MAX_VALUE (inclusive).
        for (i in 0..1048576) {
            val integer4 = nextInt().toBigDecimal()
            val encoded4 = encodeInteger(integer4, 4)
            assertTrue(encoded4.length == 4)
            val decoded4 = decodeInteger(encoded4, 4)
            assertTrue(integer4.compareTo(decoded4) == 0)
        }
    }

    @Test
    fun encodeDecodeInteger8() {
        // To speed up the test, is generated a random value
        // uniformly distributed between Long.MIN_VALUE
        // and Long.MAX_VALUE (inclusive).
        for (i in 0..1048576) {
            val integer8 = nextLong().toBigDecimal()
            val encoded8 = encodeInteger(integer8, 8)
            assertTrue(encoded8.length == 8)
            val decoded8 = decodeInteger(encoded8, 8)
            assertTrue(integer8.compareTo(decoded8) == 0)
        }
    }

    @Test
    fun encodeDecodeUnsigned1() {
        for (i in 0..255) {
            val unsigned1 = i.toBigDecimal()
            val encoded1 = encodeUnsigned(unsigned1, 1)
            assertTrue(encoded1.length == 1)
            val decoded1 = decodeUnsigned(encoded1, 1)
            assertTrue(unsigned1.compareTo(decoded1) == 0)
        }
    }

    @Test
    fun encodeDecodeUnsigned2() {
        for (i in 0..255) {
            val unsigned2 = i.toBigDecimal()
            val encoded2 = encodeUnsigned(unsigned2, 2)
            assertTrue(encoded2.length == 2)
            val decoded1 = decodeUnsigned(encoded2, 2)
            assertTrue(unsigned2.compareTo(decoded1) == 0)
        }
    }

    @Test
    fun encodeDecodeBinary2() {
        for (i in -9999..9999) {
            val binary2 = i.toBigDecimal()
            val encoded2 = encodeBinary(binary2, 2)
            assertTrue(encoded2.length == 2)
            val decoded2 = decodeBinary(encoded2, 2)
            assertTrue(binary2.compareTo(decoded2) == 0)
        }
    }

    private val encodeDecodeBinary4Lambda: Int.() -> Boolean = {
        val binary4 = toBigDecimal()
        val encoded4 = encodeBinary(binary4, 4)
        assertTrue(encoded4.length == 4)
        val decoded4 = decodeBinary(encoded4, 4)
        assertTrue(binary4.compareTo(decoded4) == 0)
        true
    }

    @Test @Category(PerformanceTest::class)
    fun encodeDecodeBinary4Slow() {
        for (i in -9999999..9999999) {
            i.encodeDecodeBinary4Lambda()
        }
    }

    @Test
    fun encodeDecodeBinary4() {
        forAll(integerGenerator(-9999999..9999999), property = encodeDecodeBinary4Lambda)
    }

    private val encodeDecodePackedLambda: Int.() -> Boolean = {
        val packed50 = toBigDecimal(MathContext(0))
        val encoded50 = encodeToDS(packed50, 5, 0)
        assertTrue(encoded50.length <= 7)
        val decoded50 = decodeFromDS(encoded50, 5, 0)
        assertEquals(packed50.compareTo(decoded50), 0)
        true
    }

    @Test @Category(PerformanceTest::class)
    fun encodeDecodePackedSlow() {
        for (i in -999999..999999) {
            i.encodeDecodePackedLambda()
        }
    }

    @Test
    fun encodeDecodePacked() {
        forAll(integerGenerator(-999999..999999), property = encodeDecodePackedLambda)
    }

    @Test
    fun encodeDecodeZoned() {
        for (i in 0..1048576) {
            val zoned153 = (nextInt().toString() + ".999").toBigDecimal()
            val encoded153 = encodeToZoned(zoned153, 15, 3)
            assertEquals(encoded153.length, 15)
            val decoded153 = decodeFromZoned(encoded153, 15, 3)
            assertEquals(zoned153.compareTo(decoded153), 0)
        }
    }
}
