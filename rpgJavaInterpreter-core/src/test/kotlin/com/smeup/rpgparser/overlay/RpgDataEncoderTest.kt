package com.smeup.rpgparser.overlay

import com.smeup.rpgparser.interpreter.decodeBinary
import com.smeup.rpgparser.interpreter.decodeFromDS
import com.smeup.rpgparser.interpreter.encodeBinary
import com.smeup.rpgparser.interpreter.encodeToDS
import org.junit.Test

import java.math.MathContext
import kotlin.test.assertTrue

class RpgDataEncoderTest {

    @Test
    fun encodeDecodePacked() {

        for (i in -9999..9999) {
            val binary2 = i.toBigDecimal()
            val encoded2 = encodeBinary(binary2, 2)
            assertTrue(encoded2.length == 2)
            val decoded2 = decodeBinary(encoded2, 2)
            assertTrue(binary2.compareTo(decoded2) == 0)
        }

        for (i in -9999999..9999999) {
            val binary4 = i.toBigDecimal()
            val encoded4 = encodeBinary(binary4, 4)
            assertTrue(encoded4.length == 4)
            val decoded4 = decodeBinary(encoded4, 4)
            assertTrue(binary4.compareTo(decoded4) == 0)
        }

        for (i in -999999..999999) {
            val packed50 = i.toBigDecimal(MathContext(0))
            val encoded50 = encodeToDS(packed50, 5, 0)
            assertTrue(encoded50.length <= 7)
            val decoded50 = decodeFromDS(encoded50, 5, 0)
            assertTrue(packed50.compareTo(decoded50) == 0)
        }
    }
}
