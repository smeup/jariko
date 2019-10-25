package com.smeup.rpgparser.interpreter

import org.junit.Test
import java.math.BigDecimal
import kotlin.test.assertEquals

class FormattingTest {

    @Test
    fun formatAsWithDecEdit1() {
        val dv = DecimalValue(BigDecimal.valueOf(-9010.54897))
        assertEquals("9,010.548970",
                dv.formatAs("1", NumberType(entireDigits = 4, decimalDigits = 6, rpgType = ""),
                decedit = ".").value)
    }
}