package com.smeup.rpgparser.compilerDirectives

import com.smeup.rpgparser.AbstractTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class CompilerDirectivesTest : AbstractTest() {
    @Test
    fun executeError01() {
        assertFails {
            "compilerDirectives/ERROR01".outputOf()
        }.printStackTrace()
    }

    @Test
    fun executeError02() {
        assertFails {
            "compilerDirectives/ERROR02".outputOf()
        }.printStackTrace()
    }

    @Test
    fun executeError03() {
        assertFails {
            "compilerDirectives/ERROR03".outputOf()
        }.printStackTrace()
    }

    @Test
    fun executeError04() {
        assertFails {
            "compilerDirectives/ERROR04".outputOf()
        }.printStackTrace()
    }

    /**
     * Duplicate definition in two different copies
     * @see #LS24004074
     */
    @Test
    fun executeDUPDEF() {
        val expected = listOf("ok")
        assertEquals(expected, "compilerDirectives/DUPDEF".outputOf())
    }
}