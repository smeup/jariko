package com.smeup.rpgparser.execution

import com.smeup.rpgparser.logging.*
import com.smeup.rpgparser.rgpinterop.DirRpgProgramFinder
import com.smeup.rpgparser.rgpinterop.RpgSystem
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import org.apache.logging.log4j.LogManager
import org.junit.Test
import java.io.File
import kotlin.test.assertNotNull
import com.smeup.rpgparser.execution.main as runnerMain
import org.apache.logging.log4j.Logger as L4JLogger

class RunnerTest {

    @Test
    fun executeExample() {
        mockkStatic(LogManager::class)

        RpgSystem.addProgramFinder(DirRpgProgramFinder(File("src/test/resources/logging")))

        val slot = slot<String>()

        val dataLogger = mockk<L4JLogger>()
        val perfLogger = mockk<L4JLogger>()
        val loopLogger = mockk<L4JLogger>()
        val stmtLogger = mockk<L4JLogger>()
        val exprLogger = mockk<L4JLogger>()

        val dataLogs = mutableListOf<String>()
        val perfLogs = mutableListOf<String>()
        val loopLogs = mutableListOf<String>()
        val stmtLogs = mutableListOf<String>()
        val exprLogs = mutableListOf<String>()

        every { LogManager.getLogger(DATA_LOGGER) } answers { dataLogger }
        every { dataLogger.isInfoEnabled } answers { true }
        every { dataLogger.info(capture(slot)) } answers { dataLogs.add(slot.captured) }

        every { LogManager.getLogger(PERFOMANCE_LOGGER) } answers { perfLogger }
        every { perfLogger.isInfoEnabled } answers { true }
        every { perfLogger.info(capture(slot)) } answers { perfLogs.add(slot.captured) }

        every { LogManager.getLogger(LOOP_LOGGER) } answers { loopLogger }
        every { loopLogger.isInfoEnabled } answers { true }
        every { loopLogger.info(capture(slot)) } answers { loopLogs.add(slot.captured) }

        every { LogManager.getLogger(STATEMENT_LOGGER) } answers { stmtLogger }
        every { stmtLogger.isInfoEnabled } answers { true }
        every { stmtLogger.info(capture(slot)) } answers { stmtLogs.add(slot.captured) }

        every { LogManager.getLogger(EXPRESSION_LOGGER) } answers { exprLogger }
        every { exprLogger.isInfoEnabled } answers { true }
        every { exprLogger.info(capture(slot)) } answers { exprLogs.add(slot.captured) }

        runnerMain(arrayOf("--log-configuration", "../logging.config", "TEST_06.rpgle", "AA", "'ABCD'", "1**"))

        assertNotNull(exprLogs.find { it.endsWith("TEST_06.rpgle\t73\tEXPR\t%ELEM(WORDS)\t128") })

        assertNotNull(perfLogs.find { it.contains("TEST_06.rpgle\t44\tPERF\tENDFOR J") })
        assertNotNull(perfLogs.find { it.contains("TEST_06.rpgle\t61\tPERF\tENDFOR I") })
        assertNotNull(perfLogs.find { it.contains("TEST_06.rpgle\t80\tPERF\tSUBROUTINE END\tPRINT") })
        assertNotNull(perfLogs.find { it.contains("TEST_06.rpgle\t\tPERF\tEND TEST_06.rpgle") })
    }

    @Test
    fun executeExampleWithCall() {
        mockkStatic(LogManager::class)

        RpgSystem.addProgramFinder(DirRpgProgramFinder(File("src/test/resources")))

        val slot = slot<String>()

        val dataLogger = mockk<L4JLogger>()
        val perfLogger = mockk<L4JLogger>()
        val loopLogger = mockk<L4JLogger>()
        val stmtLogger = mockk<L4JLogger>()
        val exprLogger = mockk<L4JLogger>()

        val dataLogs = mutableListOf<String>()
        val perfLogs = mutableListOf<String>()
        val loopLogs = mutableListOf<String>()
        val stmtLogs = mutableListOf<String>()
        val exprLogs = mutableListOf<String>()

        every { LogManager.getLogger(DATA_LOGGER) } answers { dataLogger }
        every { dataLogger.isInfoEnabled } answers { true }
        every { dataLogger.info(capture(slot)) } answers { dataLogs.add(slot.captured) }

        every { LogManager.getLogger(PERFOMANCE_LOGGER) } answers { perfLogger }
        every { perfLogger.isInfoEnabled } answers { true }
        every { perfLogger.info(capture(slot)) } answers { perfLogs.add(slot.captured) }

        every { LogManager.getLogger(LOOP_LOGGER) } answers { loopLogger }
        every { loopLogger.isInfoEnabled } answers { true }
        every { loopLogger.info(capture(slot)) } answers { loopLogs.add(slot.captured) }

        every { LogManager.getLogger(STATEMENT_LOGGER) } answers { stmtLogger }
        every { stmtLogger.isInfoEnabled } answers { true }
        every { stmtLogger.info(capture(slot)) } answers { stmtLogs.add(slot.captured) }

        every { LogManager.getLogger(EXPRESSION_LOGGER) } answers { exprLogger }
        every { exprLogger.isInfoEnabled } answers { true }
        every { exprLogger.info(capture(slot)) } answers { exprLogs.add(slot.captured) }

        runnerMain(arrayOf("--log-configuration", "../logging.config", "CALCFIBCA5.rpgle", "AA", "'ABCD'", "1**"))

        assertNotNull(dataLogs.find { it.contains("CALCFIBCA5.rpgle\t\tDATA\tppdat = N/D\t10") })
        assertNotNull(dataLogs.find { it.contains("CALCFIB.CALCFIB\t\tDATA\tppdat = N/D\t10") })
    }
}