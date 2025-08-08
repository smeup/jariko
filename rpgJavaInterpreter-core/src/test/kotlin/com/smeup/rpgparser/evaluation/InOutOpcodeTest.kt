package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.outputOf
import kotlin.test.assertEquals
import org.junit.Test as test

class InOutOpcodeTest : AbstractTest() {

    @test
    fun executeINOUT01() {
        val expected = listOf(
            "OUT: 111",
            "IN: 1010"
        )
        assertEquals(expected, "INOUT01".outputOf())
    }
}