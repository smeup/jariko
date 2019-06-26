package com.github.rpgjavainterpreter.gatecontroller

import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.rgpinterop.DirRpgProgramFinder
import com.smeup.rpgparser.rgpinterop.RpgSystem
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class GateControllerTest {

    @Test fun runExample1() {
        RpgSystem.addProgramFinder(DirRpgProgramFinder(File("examples/rpg")))
        RpgSystem.addProgramFinder(DirRpgProgramFinder(File("rpg")))
        val javaSystemInterface = JavaSystemInterface()
        javaSystemInterface.addJavaInteropPackage("com.github.rpgjavainterpreter.gatecontroller")
        JD_001(javaSystemInterface).call("https://xxx.myurl.com", "x", "w")
        assertEquals(listOf("Invoked  , URL=https://www.myurl.com"), javaSystemInterface.consoleOutput)
    }
}