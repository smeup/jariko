package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.AbstractTest
import org.junit.Test
import kotlin.test.assertEquals

open class SmeupInterpreterTest : AbstractTest() {

    @Test
    fun executeT15_A80() {
        // TODO When we will have more clear idea about the expected result, we will add the assert
        println("executeT15_A80: " + "smeup/T15_A80".outputOf())
    }

    @Test
    fun executeT15_A90() {
        // TODO When we will have more clear idea about the expected result, we will add the assert
        println("executeT15_A90: " + "smeup/T15_A90".outputOf())
    }

    @Test
    fun executeT02_A30() {
        val len = 100
        val expected = listOf(
            buildString {
                append("AAAAA".padEnd(len, ' '))
                append("BBBBB".padEnd(len, ' '))
                append("CCCCC".padEnd(len, ' '))
                append("DDDDD".padEnd(len, ' '))
                append("EEEEE".padEnd(len, ' '))
                append("FFFFF".padEnd(len, ' '))
                append("GGGGG".padEnd(len, ' '))
                append("HHHHH".padEnd(len, ' '))
                // Here I don't padEnd because the display messages are trimmed
                append("IIIII")
            }
        )
        assertEquals(expected, "smeup/T02_A30".outputOf())
    }

    @Test
    fun executeT02_A40() {
        val expected = listOf("DS4_FL1(NNNNNFFFFFMMMMMGGGGGAAAAAZZZZZ)", "DS4_FL2(AAAAAZZZZZMMMMMGGGGGNNNNNFFFFF)")
        assertEquals(expected, "smeup/T02_A40".outputOf())
    }

    @Test
    fun executeT04_A40() {
        val expected = listOf("A40_P1(122469.88)A40_P2(987.22)A40_P3(123456.10)A40_P4(121028170.03)")
        assertEquals(expected, "smeup/T04_A40".outputOf())
    }

    @Test
    fun executeT10_A20() {
        val expected = listOf("172.670-22146.863-.987000000")
        assertEquals(expected, "smeup/T10_A20".outputOf())
    }
}