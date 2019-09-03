package com.smeup.rpgparser.execution

import com.smeup.rpgparser.rgpinterop.DirRpgProgramFinder
import com.smeup.rpgparser.rgpinterop.RpgSystem
import org.junit.Test
import java.io.File
import com.smeup.rpgparser.execution.main as runnerMain

class RunnerTest {

    @Test
    fun executeExample() {
        RpgSystem.addProgramFinder(DirRpgProgramFinder(File("src/test/resources/logging")))
        runnerMain(arrayOf("--log-configuration", "../logging.config", "TEST_06.rpgle", "AA", "'ABCD'", "1**"))
    }
}