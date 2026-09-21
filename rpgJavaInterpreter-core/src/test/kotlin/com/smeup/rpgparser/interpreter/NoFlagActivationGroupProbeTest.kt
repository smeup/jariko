package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.adaptForTestCase
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.getProgram
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import com.smeup.rpgparser.rpginterop.RpgProgramFinder
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class NoFlagActivationGroupProbeTest : AbstractTest() {
    @Test
    fun noFlagProgramCalledTwiceWithinSameRequest() {
        val systemInterface = JavaSystemInterface()
        val displayed = mutableListOf<String>()
        systemInterface.onDisplay = { value, _ -> displayed.add(value) }
        val rpgDir = File("src/test/resources/")
        val programFinders: List<RpgProgramFinder> = listOf(DirRpgProgramFinder(rpgDir))

        val jariko = getProgram("ACTGRPNF1", systemInterface, programFinders)
        val configuration = Configuration()
        configuration.adaptForTestCase(this)

        jariko.singleCall(emptyList(), configuration)

        println("Displayed: $displayed")
        // Expected if persistence matches real IBM i (LR-off => persist, regardless of RT): "0", "1".
        // ACTGRPNF2 never sets LR or RT explicitly, but RETURN sets RT on, so its storage persists across calls.
        assertEquals(listOf("0", "1"), displayed.map { it.trim() })
    }
}
