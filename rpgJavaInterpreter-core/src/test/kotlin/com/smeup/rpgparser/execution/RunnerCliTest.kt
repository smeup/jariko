package com.smeup.rpgparser.execution

import com.smeup.rpgparser.utils.StringOutputStream
import org.apache.commons.io.input.ReaderInputStream
import org.junit.Test
import java.io.PrintStream
import java.io.StringReader
import java.nio.charset.Charset
import kotlin.test.assertTrue
import com.smeup.rpgparser.execution.main as runnerMain

class RunnerCliTest {

    @Test
    fun withNoArgsReplIsStarted() {
        SimpleShell.inputStream = ReaderInputStream(StringReader("signoff"), Charset.defaultCharset())
        val out = StringOutputStream()
        System.setOut(PrintStream(out))

        runnerMain(arrayOf())

        assertTrue(out.written)
        assertTrue(out.toString().contains("Goodbye"))
    }
}