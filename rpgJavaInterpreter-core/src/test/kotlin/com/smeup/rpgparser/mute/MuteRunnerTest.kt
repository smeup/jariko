package com.smeup.rpgparser.mute

import com.smeup.rpgparser.rgpinterop.DirRpgProgramFinder
import com.smeup.rpgparser.rgpinterop.RpgProgramFinder
import org.junit.Ignore
import org.junit.Test
import java.io.File
import kotlin.test.assertTrue

class MuteRunnerTest {
    @Test
    fun muteRunerCanCallRPGsInTheSameDirectory() {
        val aSource = """
     C                   call      'B'
     C                   SETON                                          LR            
"""
        val bSource = """
     C     'Hello'       dsply
     C                   SETON                                          LR            
"""
        val tempDir = createTempDir()
        val aPgm = File(tempDir, "A.rpgle")
        aPgm.writeText(aSource)
        File(tempDir, "B.rpgle").writeText(bSource)
        val programFinders = listOf<RpgProgramFinder>(DirRpgProgramFinder(aPgm.parentFile))
        val result = executeWithMutes(aPgm.toPath(), true, null, programFinders = programFinders)
        assertTrue(result.success())
    }
}
