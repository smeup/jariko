package com.smeup.rpgparser.interpreter

import org.junit.Test
import kotlin.test.assertEquals

class ValueTest {
    @Test
    fun takeFromIntValue() {
        assertEquals(IntValue(2345), IntValue(1234567).take(2, 5))
    }
}
