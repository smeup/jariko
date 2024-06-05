package com.smeup.dspfparser.linesprocessor

import kotlin.test.Test
import kotlin.test.assertEquals

internal class ParametersTest {
    @Test
    fun parameters() {
        // see class to know why there is no empty parameters test
        val multiple = DSPFParameters.fromString("23 'Hello' HI")
        assertEquals(mutableListOf("23", "Hello", "HI"), multiple.arguments)
    }
}
