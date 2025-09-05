/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smeup.rpgparser.smeup

import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertTrue
import java.util.*
import kotlin.test.assertEquals

open class MULANGT03IndicatorTest : MULANGTTest() {
    /**
     * Executes IF statement by checking indicator state.
     * @see #LS24004236
     */
    @Test
    fun executeMUDRNRAPU00121() {
        val expected = listOf("Hello there")
        assertEquals(expected, "smeup/MUDRNRAPU00121".outputOf())
    }

    /**
     * Executes DOWxx (in this case, DOWNE) statement by checking indicator state.
     * @see #LS24004236
     */
    @Test
    fun executeMUDRNRAPU00122() {
        val expected = listOf("10", "10")
        assertEquals(expected, "smeup/MUDRNRAPU00122".outputOf())
    }

    /**
     * Executes DOUxx (in this case, DOUEQ) statement by checking indicator state.
     * @see #LS24004236
     */
    @Test
    fun executeMUDRNRAPU00123() {
        val expected = listOf("10", "10")
        assertEquals(expected, "smeup/MUDRNRAPU00123".outputOf())
    }

    /**
     * Executes DO statement by checking indicator state.
     * @see #LS24004236
     */
    @Test
    fun executeMUDRNRAPU00124() {
        val expected = listOf("10", "10")
        assertEquals(expected, "smeup/MUDRNRAPU00124".outputOf())
    }

    /**
     * Executes FOR statement by checking indicator state.
     * @see #LS24004236
     */
    @Test
    fun executeMUDRNRAPU00125() {
        val expected = listOf("10", "10")
        assertEquals(expected, "smeup/MUDRNRAPU00125".outputOf())
    }

    /**
     * Executes DOU statement by checking indicator state.
     * @see #LS24004236
     */
    @Test
    fun executeMUDRNRAPU00129() {
        val expected = listOf("10", "10")
        assertEquals(expected, "smeup/MUDRNRAPU00129".outputOf())
    }

    /**
     * Executes DOW statement by checking indicator state.
     * @see #LS24004236
     */
    @Test
    fun executeMUDRNRAPU00130() {
        val expected = listOf("10", "10")
        assertEquals(expected, "smeup/MUDRNRAPU00130".outputOf())
    }

    /**
     * Execute a CALL with error state bound to an indicator where the error is originated externally
     * @see #LS25002734
     */
    @Test
    fun executeMUDRNRAPU00292() {
        val expected = listOf("ok")

        class MySystemInterface : JavaSystemInterface() {
            val displayed = LinkedList<String>()

            override fun findProgram(name: String): Program? =
                if (name == "DOPEDPGM") {
                    object : Program {
                        override fun params() = emptyList<ProgramParam>()

                        override fun execute(
                            systemInterface: SystemInterface,
                            params: LinkedHashMap<String, Value>,
                        ): List<Value> {
                            error("todo")
                        }
                    }
                } else {
                    super.findProgram(name)
                }

            override fun display(value: String) {
                displayed.add(value)
                println(value)
            }
        }

        val systemInterface = MySystemInterface()
        var myInterpreter: InterpreterCore? = null
        val configuration =
            Configuration().apply {
                jarikoCallback.onInterpreterCreation = { interpreter ->
                    myInterpreter = interpreter
                }
            }
        executePgm(
            programName = "smeup/MUDRNRAPU00292",
            systemInterface = systemInterface,
            configuration = configuration,
        )
        assertEquals(expected, systemInterface.displayed)
        assertTrue(myInterpreter!!.getIndicators()[37]?.value == true) {
            "Indicator 37 should be set to true, but it is ${myInterpreter.getIndicators()[37]?.value}"
        }
    }
}
