package com.smeup.rpgparser.interpreter

import org.junit.Test
import kotlin.test.assertEquals

class TypeTest {

    @Test
    fun aSmallerIntIsAssignableToALargerInt() {
        val valueType = NumberType(1, 0)
        val targetType = NumberType(8, 0)
        assertEquals(true, targetType.canBeAssigned(valueType))
    }

    @Test
    fun twoSameNumberTypesAreAssignableToEachOther() {
        val valueType = NumberType(8, 3)
        val targetType = NumberType(8, 3)
        assertEquals(true, targetType.canBeAssigned(valueType))
    }
}