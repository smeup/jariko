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

package com.smeup.rpgparser.parsing

import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.ast.SourceProgram
import com.smeup.rpgparser.parsing.facade.*
import org.junit.Assert
import org.junit.Test
import java.io.File

class CopyTest {

    // Test CopyBlock contains function
    @Test
    fun copyBlockContains() {
        val copyBlock1 = CopyBlock(CopyId(member = "1"), start = 1, end = 5)
        val copyBlock2 = CopyBlock(CopyId(member = "2"), start = 10, end = 20)
        val copyBlock3 = CopyBlock(CopyId(member = "2.1"), start = 11, end = 14)
        Assert.assertFalse(copyBlock1.contains(copyBlock2))
        Assert.assertFalse(copyBlock1.contains(copyBlock3))
        Assert.assertFalse(copyBlock2.contains(copyBlock1))
        Assert.assertTrue(copyBlock2.contains(copyBlock3))
        Assert.assertFalse(copyBlock3.contains(copyBlock1))
        Assert.assertFalse(copyBlock3.contains(copyBlock2))
    }

    // Test CopyBlocks getCopyBlock function
    @Test
    fun copyBlocksGetCopyBlock() {
        MainExecutionContext.execute(systemInterface = JavaSystemInterface()) {
            it.executionProgramName = "PGM"
            val copyBlocks = CopyBlocks()
            val cpy1 = CopyBlock(CopyId(member = "CPY1"), start = 4, end = 6)
            val cpy2 = CopyBlock(CopyId(member = "CPY2"), start = 9, end = 11)
            val cpy21 = CopyBlock(CopyId(member = "CPY21"), start = 10, end = 11)
            copyBlocks.onStartCopyBlock(cpy1.copyId, cpy1.start)
            copyBlocks.onEndCopyBlock(cpy1.end)
            copyBlocks.onStartCopyBlock(cpy2.copyId, cpy2.start)
            copyBlocks.onStartCopyBlock(cpy21.copyId, cpy21.start)
            copyBlocks.onEndCopyBlock(cpy21.end)
            copyBlocks.onEndCopyBlock(cpy2.end)
            for (line in 1..3) {
                val actual = copyBlocks.getCopyBlock(line)
                println("line: $line, actual: $actual")
                Assert.assertNull(actual)
            }
            for (line in 4..5) {
                val actual = copyBlocks.getCopyBlock(line)
                println("line: $line, actual: $actual")
                Assert.assertEquals(cpy1, actual)
            }
            for (line in 6..8) {
                val actual = copyBlocks.getCopyBlock(line)
                println("line: $line, actual: $actual")
                Assert.assertNull(actual)
            }
            for (line in 9..9) {
                val actual = copyBlocks.getCopyBlock(line)
                println("line: $line, actual: $actual")
                Assert.assertEquals(cpy2, actual)
            }
            for (line in 10..10) {
                val actual = copyBlocks.getCopyBlock(line)
                println("line: $line, actual: $actual")
                Assert.assertEquals(cpy21, actual)
            }
            for (line in 11..12) {
                val actual = copyBlocks.getCopyBlock(line)
                println("line: $line, actual: $actual")
                Assert.assertNull(actual)
            }
        }
    }

    // Verify if the lines position of statement returned by copy blocks match with the related
    // lines of code contained in post-processed program
    // In this case program and copies are inline
    @Test
    fun absoluteLinesMatchingRelativeLine() {
        val pgm = """
2    D Msg             S             30      
3    C                   EVAL      Msg = 'Include COPY1'
4    C      Msg          DSPLY
5    I/COPY COPY1
6    C                   EVAL      Msg = 'Include COPY2'
7    C      Msg          DSPLY
8    I/COPY COPY2     
9    C                   EVAL      Msg = 'Exit from COPY2'
10   C      Msg          DSPLY
        """.trimEnd()
        val copy1 = """
2    D Msg1            S             30
3    C                   EVAL      Msg1 = 'I AM COPY1'
4    C      Msg1         DSPLY
        """
        val copy2 = """
2    D Msg2            S             30
3    C                   EVAL      Msg2 = 'I AM COPY2'
4    C      Msg2         DSPLY
5    C                   EVAL      Msg2 = 'Include COPY21'
6    C      Msg2         DSPLY
7     /COPY COPY21     
8    C      Msg2         EVAL      Msg2 = 'After COPY21'
9    C      Msg2         DSPLY
10   C                   EVAL      Msg2 = 'Include COPY22'
11   C      Msg2         DSPLY
12    /COPY COPY22     
        """
        val copy21 = """
2    D Msg21           S             30
3    C                   EVAL      Msg21 = 'I AM COPY21'
4    C      Msg21        DSPLY
        """
        val copy22 = """
2    D Msg22           S             30
3    C                   EVAL      Msg21 = 'I AM COPY22'
4    C      Msg22        DSPLY
        """
        val id1 = CopyId(member = "COPY1")
        val id2 = CopyId(member = "COPY2")
        val id21 = CopyId(member = "COPY21")
        val id22 = CopyId(member = "COPY22")
        val copyDefinitions = mapOf(
            id1 to copy1,
            id2 to copy2,
            id21 to copy21,
            id22 to copy22
        )
        absoluteLinesMatchingRelativeLine(pgm = pgm, copyDefinitions = copyDefinitions)
    }

    // Verify if the lines position of statement returned by copy blocks match with the related
    // lines of code contained in post-processed program
    // In this case the program is LOSER_PR and the copies are all those referenced by LOSER_PR
    @Test
    fun absoluteLinesMatchingRelativeLineInLOSER_PR() {
        absoluteLinesMatchingRelativeLine(pgmName = "LOSER_PR")
    }

    private fun absoluteLinesMatchingRelativeLine(pgmName: String) {
        val srcRoot = "src/test/resources"
        val pgm = File("$srcRoot/$pgmName.rpgle").readText()
        val copyDefinitions = mutableMapOf<CopyId, String>()
        pgm.byteInputStream().preprocess(
            findCopy = { copyId ->
                File("$srcRoot/${copyId.key(SourceProgram.RPGLE)}").readText(charset = Charsets.UTF_8)
                    .apply { copyDefinitions[copyId] = this }
            }
        )
        absoluteLinesMatchingRelativeLine(pgm = pgm, copyDefinitions)
    }

    private fun absoluteLinesMatchingRelativeLine(pgm: String, copyDefinitions: Map<CopyId, String>) {
        val copyBlocks = CopyBlocks()
        val preprocessed = pgm.byteInputStream().preprocess(
            findCopy = { copyId -> copyDefinitions[copyId] },
            onStartInclusion = { copyId, start -> copyBlocks.onStartCopyBlock(copyId, start) },
            onEndInclusion = { end -> copyBlocks.onEndCopyBlock(end) }
        )
        preprocessed.lines().forEachIndexed { index, expectedLineOfCode ->
            println("index: $index, expectedLineOfCode:$expectedLineOfCode")
            if (!expectedLineOfCode.startsWith("**********")) {
                val relativeLine = copyBlocks.relativeLine(index + 1)
                println("relativeLine: $relativeLine")
                val actualLineOfCode = relativeLine.second?.let { copyBlock ->
                    copyDefinitions[copyBlock.copyId]!!.lines()[relativeLine.first - 1]
                } ?: pgm.lines()[relativeLine.first - 1]
                if (!actualLineOfCode.contains("/COPY")) {
                    println("expectedLineOfCode: $expectedLineOfCode, actualLineOfCode: $actualLineOfCode")
                    Assert.assertEquals(expectedLineOfCode, actualLineOfCode)
                }
            } else println("skip")
        }
    }
}