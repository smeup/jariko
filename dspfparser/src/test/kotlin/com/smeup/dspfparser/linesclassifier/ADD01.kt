package com.smeup.dspfparser.linesclassifier

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class ADD01 : DSPFSpecificationsLoader("./src/test/resources/ADD01.dspf") {
    @Test
    fun records() {
        val first = this.specifications.records[0]
        assertEquals("FMT01", first.name)
    }

    @Test
    fun fields() {
        val fields = this.specifications.records[0].fields
        val names = fields.map { it.name }
        assertEquals(4, fields.size)
        assertTrue { names.contains("A") }
        assertTrue { names.contains("B") }
        assertTrue { names.contains("RESULT") }
        assertTrue { names.contains("PGMCTL") }
    }

    @Test
    fun constants() {
        val constants = this.specifications.records[0].constants
        val values = constants.map { it.value }
        assertEquals(5, constants.size)
        assertTrue { values.contains(ConstantValue("Sum of A and B")) }
        assertTrue { values.contains(ConstantValue("A:")) }
        assertTrue { values.contains(ConstantValue("B:")) }
        assertTrue { values.contains(ConstantValue("RESULT:")) }
        assertTrue { values.contains(ConstantValue("PGMCTL:")) }
    }
}
