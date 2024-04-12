package com.smeup.rpgparser.compilerDirectives

import com.smeup.rpgparser.AbstractTest
import org.junit.Test
import kotlin.test.assertFails

class CompilerDirectivesTest: AbstractTest() {
    @Test
    fun executeError01() {
        assertFails{
            "compilerDirectives/error01".outputOf()
        }.printStackTrace()
    }

    @Test
    fun executeError02() {
        assertFails{
            "compilerDirectives/error02".outputOf()
        }.printStackTrace()
    }

    @Test
    fun executeError03() {
        assertFails{
            "compilerDirectives/error03".outputOf()
        }.printStackTrace()
    }
}