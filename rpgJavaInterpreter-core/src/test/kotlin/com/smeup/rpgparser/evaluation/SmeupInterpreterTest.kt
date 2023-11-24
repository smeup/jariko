package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.AbstractTest
import org.junit.Test

open class SmeupInterpreterTest : AbstractTest() {

    @Test
    fun executeT15_A80() {
        "smeup/T15_A80".outputOf()
    }

    @Test
    fun executeT15_A90() {
        "smeup/T15_A90".outputOf()
    }
}