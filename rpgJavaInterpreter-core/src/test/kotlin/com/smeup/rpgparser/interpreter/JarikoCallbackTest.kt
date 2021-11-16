package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execution.Configuration
import com.strumenta.kolasu.model.Position
import kotlin.test.Test

/**
 * Test suite for test Jariko callback features
 * */
class JarikoCallbackTest : AbstractTest(){

    @Test
    fun onEnterStatement() {
        executePgm(programName = "HELLO", configuration = Configuration().apply {
            jarikoCallback.onEnterStatement = {
                    position: Position? ->
                println(position)
            }
        })
    }

}