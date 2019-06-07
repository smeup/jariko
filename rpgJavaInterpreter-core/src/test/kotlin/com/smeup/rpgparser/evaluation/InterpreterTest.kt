@file:Suppress("DEPRECATION")
package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.*
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.jvminterop.JvmProgramRaw
import com.smeup.rpgparser.parsetreetoast.resolve
import junit.framework.Assert.format
import org.junit.Ignore
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class InterpreterTest {

    @Test
    fun executeCALCFIB_initialDeclarations_dec() {
        val cu = assertASTCanBeProduced("CALCFIB_1", true)
        cu.resolve()
        val interpreter = execute(cu, mapOf("ppdat" to StringValue("3")))
        assertIsIntValue(interpreter["NBR"], 3)
    }

    @Test
    fun executeCALCFIB_initialDeclarations_inz() {
        val cu = assertASTCanBeProduced("CALCFIB_1", true)
        cu.resolve()

        assertTrue(cu.getDataDefinition("ppdat").initializationValue == null)
        assertTrue(cu.getDataDefinition("NBR").initializationValue == null)
        assertTrue(cu.getDataDefinition("RESULT").initializationValue != null)
        assertTrue(cu.getDataDefinition("COUNT").initializationValue == null)
        assertTrue(cu.getDataDefinition("A").initializationValue != null)
        assertTrue(cu.getDataDefinition("B").initializationValue != null)

        val interpreter = execute(cu, mapOf("ppdat" to StringValue("3")))
        assertIsIntValue(interpreter["RESULT"], 0)
        assertIsIntValue(interpreter["A"], 0)
        assertIsIntValue(interpreter["B"], 1)
    }

    @Test
    fun executeCALCFIB_otherClauseOfSelect() {
        val cu = assertASTCanBeProduced("CALCFIB_2", true)
        cu.resolve()
        val si = CollectorSystemInterface()
        val interpreter = execute(cu, mapOf("ppdat" to StringValue("10")), si)
        val assignments = interpreter.getAssignments()
        assertEquals(assignments[0].value, StringValue("10"))
        assertIsIntValue(interpreter["NBR"], 10)
        assertEquals(listOf("10"), si.displayed)
    }

    private fun assertFibonacci(input: String, output: String) {
        val cu = assertASTCanBeProduced("CALCFIB", true)
        cu.resolve()
        val si = CollectorSystemInterface()
        val interpreter = execute(cu, mapOf("ppdat" to StringValue(input)), si)
        assertEquals(listOf("FIBONACCI OF: $input IS: $output"), si.displayed)
        assertEquals(interpreter.getExecutedSubroutineNames()[0], "FIB")
    }

    @Test
    fun executeCALCFIB_for_value_0() {
        assertFibonacci("0", "0")
    }

    @Test
    fun executeCALCFIB_for_value_1() {
        assertFibonacci("1", "1")
    }

    @Test
    fun executeCALCFIB_for_value_2() {
        assertFibonacci("2", "1")
    }

    @Test
    fun executeCALCFIB_for_value_3() {
        assertFibonacci("3", "2")
    }

    @Test
    fun executeCALCFIB_for_value_4() {
        assertFibonacci("4", "3")
    }

    @Test
    fun executeCALCFIB_for_value_10() {
        assertFibonacci("10", "55")
    }

    @Test
    fun executeHELLO() {
        val cu = assertASTCanBeProduced("HELLO", true)
        cu.resolve()
        val si = CollectorSystemInterface()
        val interpreter = execute(cu, mapOf(), si)
        assertEquals(listOf("Hello World!"), si.displayed)
        assertEquals(interpreter.getExecutedSubroutines().size, 0)
    }


    @Test
    fun executeCallToFibonacciWrittenInRpg() {
        val cu = assertASTCanBeProduced("CALCFIBCAL", true)
        cu.resolve()
        val si = CollectorSystemInterface()
        si.programs["CALCFIB"] = rpgProgram("CALCFIB")
        val interpreter = execute(cu, mapOf("ppdat" to StringValue("10")), si)
        assertEquals(listOf("FIBONACCI OF: 10 IS: 55"), si.displayed)
        assertEquals(interpreter.getExecutedSubroutines().size, 0)
    }

    @Test
    fun executeCallToFibonacciWrittenOnTheJvm() {
        val cu = assertASTCanBeProduced("CALCFIBCAL", true)
        cu.resolve()
        val si = CollectorSystemInterface()
        si.programs["CALCFIB"] = object : JvmProgramRaw("CALCFIB", listOf(ProgramParam("ppdat", StringType(8)))) {
            override fun execute(systemInterface: SystemInterface, params: Map<String, Value>) : List<Value> {
                val n = params["ppdat"]!!.asString().valueWithoutPadding.toInt()
                var t1 = 0
                var t2 = 1

                for (i in 1..n) {
                    val sum = t1 + t2
                    t1 = t2
                    t2 = sum
                }
                systemInterface.display("FIBONACCI OF: $n IS: $t1")
                return listOf(params["ppdat"]!!)
            }
        }
        val interpreter = execute(cu, mapOf("ppdat" to StringValue("10")), si)
        assertEquals(listOf("FIBONACCI OF: 10 IS: 55"), si.displayed)
        assertEquals(interpreter.getExecutedSubroutines().size, 0)
    }

    @Test
    fun executeFibonacciWrittenInRpgAsProgram() {
        val cu = assertASTCanBeProduced("CALCFIB", true)
        cu.resolve()
        val si = CollectorSystemInterface()
        val rpgProgram = RpgProgram(cu)
        rpgProgram.execute(si, mapOf("ppdat" to StringValue("10")))
        assertEquals(1, rpgProgram.params().size)
        assertEquals(ProgramParam("ppdat", StringType(8)), rpgProgram.params()[0])
        assertEquals(listOf("FIBONACCI OF: 10 IS: 55"), si.displayed)
    }

    @Test
    fun executeHELLOCASE() {
        assertEquals(outputOf("HELLOCASE"), listOf("Hello World!"))
    }


    @Test
    fun executeHELLOPLIST() {
        val msg = "Hello World!"
        val parms :  Map<String, Value> = mapOf("msG" to StringValue(msg))
        assertEquals(outputOf("HELLOPLIST", parms), listOf(msg))
    }

    @Test
    fun executeHELLOTRIM() {
        assertEquals(outputOf("HELLOTRIM"), listOf("Hello World!"))
    }


    @Test
    fun executeHELLO1() {
        assertEquals(outputOf("HELLO1"), listOf("Hello World"))
    }

    //TODO
    @Test @Ignore
    fun executeHELLOCHARS() {
        assertEquals(outputOf("HELLOCHARS"), listOf("OK"))
    }

    //TODO
    @Test @Ignore
    fun executeHELLOEQU() {
        assertEquals(outputOf("HELLOEQU"), listOf("Cb is equal to C and Cb does not differ from C"))
    }

    //TODO
    @Test @Ignore
    fun executeHELLOPAD() {
        assertEquals(outputOf("HELLOPAD"), listOf("X padded"))
    }

    //TODO
    @Test @Ignore
    fun executeHELLOVARST() {
        assertEquals(outputOf("HELLOVARST"), listOf("Eq", "Hello-World", "Hello-World"))
    }

    @Test
    fun executeCLEARDEC() {
        assertStartsWith(outputOf("CLEARDEC"), "Counter:")
    }

    @Test
    fun executeTIMESTDIFF() {
        assertStartsWith(outputOf("TIMESTDIFF"), "Elapsed time:")
    }

    @Test
    fun executeCALCFIBCA5() {
        assertEquals(outputOf("CALCFIBCA5"), listOf("FIBONACCI OF: 10 IS: 55"))
    }

    @Test @Ignore
    fun executeJD_000() {
        assertEquals(outputOf("JD_000"), listOf("", "", "Url", "http://xxx.smaup.com"))
    }

    private fun assertStartsWith(lines: List<String>, value: String) {
        if (lines.isEmpty()) {
            fail("Empty output")
        }
        assertTrue (lines.get(0).startsWith(value), format("Output not matching", value, lines))
    }

    private fun outputOf(programName: String, initialValues: Map<String, Value> = mapOf()): LinkedList<String> {
        val cu = assertASTCanBeProduced(programName, true)
        cu.resolve()
        val si = ExtendedCollectorSystemInterface(CollectorSystemInterface())
        execute(cu, initialValues, si)
        return si.collectorSI.displayed
    }


    class ExtendedCollectorSystemInterface(val collectorSI: CollectorSystemInterface): SystemInterface by collectorSI {
        override fun findProgram(name: String): Program? {
            return collectorSI.findProgram(name) ?: rpgProgram(name)
        }
    }
}

private fun rpgProgram(name: String) : RpgProgram {
    return RpgProgram.fromInputStream(Dummy::class.java.getResourceAsStream("/$name.rpgle"), name)
}

