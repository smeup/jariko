package com.github.rpgjavainterpreter.gatecontroller

import com.smeup.rpgparser.execution.ResourceProgramFinder
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import org.junit.Ignore
import org.junit.Test
import kotlin.test.assertEquals

class GateControllerTest {

    // TODO fix call by reflection
    @Test @Ignore
    fun runExample1() {
        val javaSystemInterface = JavaSystemInterface().apply {
            rpgSystem.addProgramFinders(listOf(ResourceProgramFinder("/"), ResourceProgramFinder("/rpg/")))
        }
        javaSystemInterface.addJavaInteropPackage("com.github.rpgjavainterpreter.gatecontroller")
        JD_001(javaSystemInterface).call("https://xxx.myurl.com", "x", "w")
        assertEquals(listOf("Invoked  , URL=https://www.myurl.com"), javaSystemInterface.consoleOutput)
    }
}
