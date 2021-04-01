package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.execution.defaultProgramFinders
import com.smeup.rpgparser.execution.getProgram
import com.smeup.rpgparser.interpreter.RpgProgram
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.ast.Api
import com.smeup.rpgparser.parsing.ast.ApiDescriptor
import com.smeup.rpgparser.parsing.ast.ApiId
import com.smeup.rpgparser.parsing.ast.SourceProgram
import com.smeup.rpgparser.parsing.facade.Copy
import com.smeup.rpgparser.parsing.facade.CopyId
import com.smeup.rpgparser.rpginterop.RpgProgramFinder
import com.smeup.rpgparser.testCompiledDir
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

private class BinProgramFinder : RpgProgramFinder {

    override fun findRpgProgram(nameOrSource: String): RpgProgram? {
        val f = File(testCompiledDir, "$nameOrSource.bin")
        return if (f.exists()) {
            println("Loading $f")
            RpgProgram.fromInputStream(f.inputStream(), nameOrSource, SourceProgram.BINARY)
        } else {
            null
        }
    }

    override fun findCopy(copyId: CopyId): Copy? {
        return null
    }

    override fun findApiDescriptor(apiId: ApiId): ApiDescriptor? {
        return null
    }

    override fun findApi(apiId: ApiId): Api? {
        return null
    }
}

class InterpreterTestCompiled : InterpreterTest() {

    override fun useCompiledVersion() = true

    @Test
    override fun executeDSOVERL() {
        val si = JavaSystemInterface()
        // test compiled version using a customized programfinder
        val programFinder = mutableListOf(BinProgramFinder()) + defaultProgramFinders
        getProgram("DSOVERL", si, programFinder).singleCall(emptyList())
        assertEquals(
            expected = "AAAA,BBBB".split(","),
            actual = si.consoleOutput
        )
    }
}