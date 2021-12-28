package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.JarikoCallback
import com.smeup.rpgparser.execution.Options
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.facade.Copy
import com.smeup.rpgparser.parsing.facade.CopyId
import com.strumenta.kolasu.model.Position
import org.junit.Assert
import kotlin.test.Test

/**
 * Test suite to test Jariko callback features
 * */
class JarikoCallbackTest : AbstractTest() {

    @Test
    fun onEnterStatement() {
        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        var enteredTimes = 0
        executePgm(systemInterface = systemInterface, programName = "HELLO", configuration = Configuration().apply {
            jarikoCallback.onEnterStatement = { position: Position?, _: CopyId? -> enteredTimes++ }
        })
        Assert.assertEquals(3, enteredTimes)
    }

    @Test
    fun programLifeCycle() {
        val enteredTimes = mutableMapOf("CAL04" to 0, "CAL02" to 0)
        val exitedTimes = mutableMapOf("CAL04" to 0, "CAL02" to 0)
        val systemInterface = JavaSystemInterface().apply { onDisplay = { _, _ -> run {} } }
        executePgm(systemInterface = systemInterface, programName = "CAL04", configuration = Configuration().apply {
            jarikoCallback.onEnterPgm = {
                    programName: String, symbolTable: ISymbolTable ->
                println("onEnterPgm - programName: $programName, symbolTable: $symbolTable")
                enteredTimes[programName] = enteredTimes[programName]!! + 1
            }
            jarikoCallback.onExitPgm = {
                    programName: String, symbolTable: ISymbolTable, error: Throwable? ->
                println("onExitPgm - programName: $programName, symbolTable: $symbolTable")
                exitedTimes[programName] = exitedTimes[programName]!! + 1
            }
        })
        Assert.assertEquals(1, enteredTimes["CAL04"])
        Assert.assertEquals(1, exitedTimes["CAL04"])
        Assert.assertEquals(5, enteredTimes["CAL02"])
        Assert.assertEquals(5, exitedTimes["CAL02"])
    }

    @Test
    fun onEnterStatementWithCopyBlock() {
        val pgm = """
     D Msg             S             30      
     C                   EVAL      Msg = 'Include COPY1'
     C      Msg          DSPLY
     I/COPY COPY1
     C                   EVAL      Msg = 'Include COPY2'
     C      Msg          DSPLY
     I/COPY COPY2     
     C                   EVAL      Msg = 'Exit from COPY2'
     C      Msg          DSPLY
        """
        val copy1 = """
     D Msg1            S             30
     C                   EVAL      Msg1 = 'I AM COPY1'
     C      Msg1         DSPLY
        """
        val copy2 = """
     D Msg2            S             30
     C                   EVAL      Msg2 = 'I AM COPY2'
     C      Msg2         DSPLY
     C                   EVAL      Msg2 = 'Include COPY21'
     C      Msg2         DSPLY
      /COPY COPY21     
        """
        val copy21 = """
     D Msg21           S             30
     C                   EVAL      Msg21 = 'I AM COPY21'
     C      Msg21        DSPLY
        """
        val id1 = CopyId(member = "COPY1")
        val id2 = CopyId(member = "COPY2")
        val id21 = CopyId(member = "COPY21")
        val copyDefinitions = mapOf(
            id1 to copy1,
            id2 to copy2,
            id21 to copy21
        )
        val systemInterface = object : JavaSystemInterface() {
            override fun findCopy(copyId: CopyId): Copy? {
                return copyDefinitions[copyId]?.let { Copy.fromInputStream(it.byteInputStream(charset("UTF-8"))) }
            }
            override fun display(value: String) {
                println(value)
            }
        }
        lateinit var postprocessed: String
        // statements per copy
        val statements = HashMap<CopyId?, Int>().apply {
            put(null, 6)
            put(id1, 2)
            put(id2, 4)
            put(id21, 2)
        }
        val configuration = Configuration().apply {
            options = Options().apply { dumpSourceOnExecutionError = true }
            jarikoCallback = JarikoCallback().apply {
                afterAstCreation = { ast -> postprocessed = ast.source!! }
                onEnterStatement = { position: Position?, copyId: CopyId? ->
                    println("copyId: $copyId, line: ${position!!.start.line}")
                    val src = copyId?.let { copyDefinitions[copyId] } ?: postprocessed
                    val code = src.lines()[position!!.start.line - 1]
                    println("code: $code")
                    // for each statement remove statement for that copy
                    // test has succeeded if statements become empty
                    statements[copyId] = statements[copyId]!! - 1
                    if (statements[copyId]!! == 0) {
                        statements.remove(copyId)
                    }
                }
            }
        }
        executePgm(programName = pgm, systemInterface = systemInterface, configuration = configuration)
        Assert.assertTrue(statements.isEmpty())
    }
}