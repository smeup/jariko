package com.github.rpgjavainterpreter.gatecontroller

import com.smeup.rpgparser.execution.ResourceProgramFinder
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.rgpinterop.RpgSystem
import org.junit.Ignore
import org.junit.Test
import kotlin.test.assertEquals

class GateControllerTest {

    // TODO fix call by reflection
    @Test @Ignore
    fun runExample1() {
        RpgSystem.addProgramFinder(ResourceProgramFinder("/"))
        RpgSystem.addProgramFinder(ResourceProgramFinder("/rpg/"))
        val javaSystemInterface = JavaSystemInterface()
        javaSystemInterface.addJavaInteropPackage("com.github.rpgjavainterpreter.gatecontroller")
        JD_001(javaSystemInterface).call("https://xxx.myurl.com", "x", "w")
        assertEquals(listOf("Invoked  , URL=https://www.myurl.com"), javaSystemInterface.consoleOutput)
    }
}
