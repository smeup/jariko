package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.getProgram
import com.smeup.rpgparser.utils.asBigDecimal
import org.junit.Ignore
import org.junit.Test
import kotlin.test.assertEquals

class TypeTest : AbstractTest() {

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

    @Test
    @Ignore
    fun integers() {
        // Variable 'NUM' is defined as '1' integer digit and '0' decimal digit.
        // Due to its definition, 'NUM' variable can store values from -9 to 9.
        // This test shows how numeric variable declaration doesn't work properly
        // cause the 'NUM' variable can store 9999 value.
        val myProgram = """
     H ACTGRP('MyAct')
     D NUM             S              1  0
     D NEGNUM          S              1  0
     C                   EVAL      NUM = 9999
     C                   EVAL      NEGNUM = -9999
     C     NUM           DSPLY
     C     NEGNUM        DSPLY
     C                   SETON                                          RT
     """
        val commandLineProgram = getProgram(nameOrSource = myProgram)
        val memoryStorage = MemoryStorage()
        val configuration = Configuration(memorySliceStorage = memoryStorage)
        val numberException = kotlin.runCatching {
            commandLineProgram.singleCall(emptyList(), configuration)
        }.exceptionOrNull()
        require(numberException != null)
    }

    @Test
    @Ignore
    fun integersHigherLowerVal() {
        // Variable 'NUM' is defined as '1' integer digit and '0' decimal digit.
        // Due to its definition, 'NUM' variable can store values from -9 to 9.
        // This test shows how numeric variable declaration doesn't work properly
        // cause the 'NUM' variable can store 9999 value.
        val myProgram = """
     H ACTGRP('MyAct')
     D NUM             S              1  0
     D NEGNUM          S              1  0
     C                   EVAL      NUM = *HIVAL
     C                   EVAL      NEGNUM = *LOVAL
     C     NUM           DSPLY
     C     NEGNUM        DSPLY
     C                   SETON                                          RT
     """
        val commandLineProgram = getProgram(nameOrSource = myProgram)
        val memoryStorage = MemoryStorage()
        val configuration = Configuration(memorySliceStorage = memoryStorage)
        commandLineProgram.singleCall(emptyList(), configuration)
        val variables = memoryStorage.storage[MemorySliceId("MyAct".toUpperCase(), programName = myProgram)]
        require(variables != null)
        assertEquals(
                expected = IntValue(9),
                actual = variables["NUM"] ?: error("Not found NUM")
        )
        assertEquals(
                expected = IntValue(-9),
                actual = variables["NEGNUM"] ?: error("Not found NEGNUM")
        )
    }

    @Test
    @Ignore
    fun decimalsHigherLowerVal() {
        // Variable 'NUM' is defined as '2' integer digit and '1' decimal digit.
        // Due to its definition, 'NUM' variable can store values from -99.9 to 99.9 .
        // This test shows how numeric variable declaration doesn't work properly cause,
        // cause the 'NUM' variable can store 9999 value.
        val myProgram = """
     H ACTGRP('MyAct')
     D NUM             S              3  1
     D NEGNUM          S              3  1
     C                   EVAL      NUM = *HIVAL
     C                   EVAL      NEGNUM = *LOVAL
     C     NUM           DSPLY
     C     NEGNUM        DSPLY
     C                   SETON                                          RT
     """
        val commandLineProgram = getProgram(nameOrSource = myProgram)
        val memoryStorage = MemoryStorage()
        val configuration = Configuration(memorySliceStorage = memoryStorage)
        commandLineProgram.singleCall(emptyList(), configuration)
        val variables = memoryStorage.storage[MemorySliceId("MyAct".toUpperCase(), programName = myProgram)]
        require(variables != null)

        assertEquals(
                expected = "99.9".asBigDecimal(),
                actual = (variables["NUM"] as DecimalValue).value ?: error("Not found NUM")
        )
        assertEquals(
                expected = "-99.9".asBigDecimal(),
                actual = (variables["NEGNUM"] as DecimalValue).value ?: error("Not found NUM")
        )
    }

    @Test
    fun strings() {
        // Variable 'STR' is defined as '2' bytes length.
        // Due to its definition, 'STR' variable can store values of size of '2' bytes.
        // This test shows how string variable works properly.
        val myProgram = """
     H ACTGRP('MyAct')
     D STR             S              2  
     D MSG             S             12
     C                   EVAL      STR = '  '
     C                   EVAL      STR = %TRIM(STR) + 'A'
     C                   EVAL      STR = %TRIM(STR) + 'B'
     C                   EVAL      STR = %TRIM(STR) + 'C'
     C                   EVAL      STR = %TRIM(STR) + 'D'
     C     STR           DSPLY
     C                   SETON                                          RT
     """
        val commandLineProgram = getProgram(nameOrSource = myProgram)
        val memoryStorage = MemoryStorage()
        val configuration = Configuration(memorySliceStorage = memoryStorage)
        commandLineProgram.singleCall(emptyList(), configuration)
        val variables = memoryStorage.storage[MemorySliceId("MyAct".toUpperCase(), programName = myProgram)]
        require(variables != null)
        assertEquals(
                expected = "AB",
                actual = (variables["STR"] as StringValue).value ?: error("Not found STR")
        )
    }
}
