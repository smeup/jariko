package com.smeup.rpgparser.mute

import com.smeup.rpgparser.rgpinterop.DirRpgProgramFinder
import com.smeup.rpgparser.rgpinterop.RpgProgramFinder
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MuteRunnerTest {
    @Test
    fun muteRunnerCanCallRPGsInTheSameDirectory() {
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

    @Test
    fun muteRunnerCanCountAnnotations() {
        val aSource = """
     D x               S             50    inz('Hello world!')
    MU* VAL1('Hello world!') VAL2(x) COMP(EQ) 
     C                   seton                                        lr         
"""
        val tempDir = createTempDir()
        val aPgm = File(tempDir, "A.rpgle")
        aPgm.writeText(aSource)
        val result = executeWithMutes(aPgm.toPath(), true, null)
        assertTrue(result.success())
        assertEquals(1, result.resolved)
        assertEquals(1, result.executed)
        assertEquals(0, result.failed)
    }

    @Test
    fun muteRunnerCanCountFailingAnnotations() {
        val aSource = """
    MU* VAL1(1) VAL2(2) COMP(EQ) 
     C                   seton                                        lr     
"""
        val tempDir = createTempDir()
        val aPgm = File(tempDir, "A.rpgle")
        aPgm.writeText(aSource)
        val result = executeWithMutes(aPgm.toPath(), true, null)
        assertFalse(result.success())
        assertEquals(1, result.resolved)
        assertEquals(1, result.executed)
        assertEquals(1, result.failed)
    }
}
