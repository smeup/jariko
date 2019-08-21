@file:Suppress("DEPRECATION")
package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.*
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.jvminterop.JvmProgramRaw
import com.smeup.rpgparser.parsing.parsetreetoast.resolve
import com.smeup.rpgparser.utils.asInt
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import org.junit.Ignore
import org.junit.Test

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
        val logHandler = ListLogHandler()
        val interpreter = execute(cu, mapOf("ppdat" to StringValue("10")), si, listOf(logHandler))
        val assignments = logHandler.getAssignments()
        assertEquals(assignments[0].value, StringValue("10"))
        assertIsIntValue(interpreter["NBR"], 10)
        assertEquals(listOf("10"), si.displayed)
    }

    private fun assertFibonacci(input: String, output: String) {
        val cu = assertASTCanBeProduced("CALCFIB", true)
        cu.resolve()
        val si = CollectorSystemInterface()
        val logHandler = ListLogHandler()
        execute(cu, mapOf("ppdat" to StringValue(input)), si, listOf(logHandler))
        assertEquals(listOf("FIBONACCI OF: $input IS: $output"), si.displayed)
        assertEquals(logHandler.getExecutedSubroutineNames()[0], "FIB")
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
        val logHandler = ListLogHandler()
        execute(cu, mapOf(), si, listOf(logHandler))
        assertEquals(listOf("Hello World!"), si.displayed)
        assertEquals(logHandler.getExecutedSubroutines().size, 0)
    }

    @Test
    fun executeCallToFibonacciWrittenInRpg() {
        val cu = assertASTCanBeProduced("CALCFIBCAL", true)
        cu.resolve()
        val si = CollectorSystemInterface()
        val logHandler = ListLogHandler()
        si.programs["CALCFIB"] = rpgProgram("CALCFIB")
        execute(cu, mapOf("ppdat" to StringValue("10")), si, listOf(logHandler))
        assertEquals(listOf("FIBONACCI OF: 10 IS: 55"), si.displayed)
        assertEquals(logHandler.getExecutedSubroutines().size, 0)
    }

    @Test
    fun executeCallToFibonacciWrittenOnTheJvm() {
        val cu = assertASTCanBeProduced("CALCFIBCAL", true)
        cu.resolve()
        val si = CollectorSystemInterface()
        val logHandler = ListLogHandler()
        si.programs["CALCFIB"] = object : JvmProgramRaw("CALCFIB", listOf(ProgramParam("ppdat", StringType(8)))) {
            override fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>): List<Value> {
                val n = params["ppdat"]!!.asString().valueWithoutPadding.asInt()
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
        execute(cu, mapOf("ppdat" to StringValue("10")), si, listOf(logHandler))
        assertEquals(listOf("FIBONACCI OF: 10 IS: 55"), si.displayed)
        assertEquals(logHandler.getExecutedSubroutines().size, 0)
    }

    @Test
    fun executeFibonacciWrittenInRpgAsProgram() {
        val cu = assertASTCanBeProduced("CALCFIB", true)
        cu.resolve()
        val si = CollectorSystemInterface()
        val rpgProgram = RpgProgram(cu)
        rpgProgram.execute(si, linkedMapOf("ppdat" to StringValue("10")))
        assertEquals(1, rpgProgram.params().size)
        assertEquals(ProgramParam("ppdat", StringType(8)), rpgProgram.params()[0])
        assertEquals(listOf("FIBONACCI OF: 10 IS: 55"), si.displayed)
    }

    @Test
    fun executeHELLOCASE() {
        assertEquals(listOf("Hello World!"), outputOf("HELLOCASE"))
    }

    @Test
    fun executeHELLOPLIST() {
        val msg = "Hello World!"
        val parms: Map<String, Value> = mapOf("msG" to StringValue(msg))
        assertEquals(listOf(msg), outputOf("HELLOPLIST", parms))
    }

    @Test
    fun executeHELLOTRIM() {
        assertEquals(listOf("Hello World!"), outputOf("HELLOTRIM"))
    }

    @Test
    fun executeHELLO1() {
        assertEquals(listOf("Hello World"), outputOf("HELLO1"))
    }

    @Test
    fun executeHELLOCHARS() {
        assertEquals(listOf("OK"), outputOf("HELLOCHARS"))
    }

    @Test
    fun executeHELLOEQU() {
        assertEquals(listOf("Cb is equal to C and Cb does not differ from C"), outputOf("HELLOEQU"))
    }

    @Test
    fun executeHELLOPAD() {
        assertEquals(listOf("X padded"), outputOf("HELLOPAD"))
    }

    @Test
    fun executeLEN() {
        assertEquals(listOf("Hello World! 23"), outputOf("LEN"))
    }

    @Test
    fun executeHELLOVARST() {
        assertEquals(listOf("Eq", "Hello-World", "Hello-World"), outputOf("HELLOVARST"))
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
    fun executeSUBDURTEST() {
        assertStartsWith(outputOf("SUBDURTEST"), "1100")
    }

    @Test
    fun executeCALCFIBCA5() {
        assertEquals(listOf("FIBONACCI OF: 10 IS: 55"), outputOf("CALCFIBCA5"))
    }

    @Test
    fun executeCAL01_callingRPGPgm() {
        assertEquals(listOf("1"), outputOf("CAL01"))
    }

    @Test
    fun executeCAL01_callingJavaPgm() {
        val si = CollectorSystemInterface()
        var javaPgmCalled = false
        si.programs["CAL02"] = object : JvmProgramRaw("CAL02", listOf(
                ProgramParam("NBR", NumberType(8, 0)))) {
            override fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>): List<Value> {
                javaPgmCalled = true
                val nbr = params["NBR"]
                if (nbr!!.asInt().value.toInt() == 0) {
                    return listOf(IntValue(1))
                } else {
                    return listOf(IntValue(2))
                }
            }
        }
        execute("CAL01", emptyMap(), si)
        assertTrue(javaPgmCalled, "Java pgm CAL02 was not called")
        assertEquals(si.displayed, listOf("1"))
    }

    @Test
    fun executeFORDOWNBY() {
        assertEquals(outputOf("FORDOWNBY"), listOf("12", "9", "6", "3"))
    }

    @Test
    fun executeMOVEFIXFIX() {
        assertEquals(outputOf("MOVEFIXFIX"), listOf("ABCDE", "56789", "", "MNOPX"))
    }

    @Test
    fun executeMOVENBRNBR() {
        assertEquals(outputOf("MOVENBRNBR"), listOf("12345", "45678", "123", "99991"))
    }

    @Test
    fun executeDOWTEST() {
        assertEquals(outputOf("DOWTEST"), listOf("COUNTER IS NOW 21"))
    }

    @Test
    fun executePLUSEQUAL() {
        assertEquals(outputOf("PLUSEQUAL"), listOf("COUNTER IS NOW 6"))
    }

    @Test
    fun executeASSIGN() {
        assertEquals(outputOf("ASSIGN"), listOf("x is now 2", "y is now 162", "z is now 12", "w is now 198359290368"))
    }

    @Test
    fun executePOWER() {
        assertEquals(outputOf("POWER"), listOf("i is now 8"))
    }

    @Test
    fun executeMultiplicationAndDivisionWithoutSpaces() {
        assertEquals(listOf("x is now 6", "y is now 2", "z is now 0"), outputOf("CALC"))
    }

    @Test
    fun executeMULTILNE() {
        assertEquals(listOf("V1x.5_"), outputOf("MULTILINE"))
    }

    @Test
    fun executeXLATEBIF() {
        assertEquals(listOf("RPG DEPT", "RPG Dept"), outputOf("XLATEBIF"))
    }

    @Test
    fun executeBIFEDITC_1() {
        assertEquals(listOf("x   123,456   123,456  1,234.56  1,234.56       .00X"), outputOf("BIFEDITC_1"))
    }

    @Test
    fun executeXLATEBIF2() {
        assertEquals(listOf("http://xxx.smaup.comuuuuuu"), outputOf("XLATEBIF2"))
    }

    @Test
    fun executeBIFEDITC_2() {
        assertEquals(listOf("x   123,456   123,456  1,234.56X",
                            "x  1,234.56          X",
                            "x  1,234.50X"),
            outputOf("BIFEDITC_2"))
    }

    @Test
    fun executeBIFEDITC_3() {
        assertEquals(listOf("x  123456  123456  1234.56X",
                            "x  1234.56      .00X",
                            "x  1234.50X"),
            outputOf("BIFEDITC_3"))
    }
    @Test
    fun executeBIFEDITC_4() {
        assertEquals(listOf("x  123456  123456  1234.56X",
                            "x  1234.56         X",
                            "x  1234.50X"),
            outputOf("BIFEDITC_4"))
    }

    @Test
    fun executeBIFEDITC_J() {
        assertEquals(listOf("x   123,456    123,456-  1,234.56 X",
                            "x  1,234.56-       .00 X",
                            "x  1,234.50 X"),
            outputOf("BIFEDITC_J"))
    }

    @Test
    fun executeBIFEDITC_Y() {
        assertEquals(listOf("x  12/34/56  12/34/56  12/34/56X",
                            "x  12/34/56   0/00/00X",
                            "x  12/34/50 12/34/5678  0/00/12X",
                            "x   1/23/45X"),
            outputOf("BIFEDITC_Y"))
    }

    @Test
    fun executeNEGATIVINI() {
        assertEquals(listOf("< 0"), outputOf("NEGATIVINI"))
    }

    @Test
    fun executeBIFEDITC_Z() {
        assertEquals(listOf("x  123456  123456  123456  123456        X"), outputOf("BIFEDITC_Z"))
    }

    @Test
    fun executeVAR01() {
        assertEquals(listOf("NOT EQ"), outputOf("VAR01"))
    }

    @Test
    fun executeARRAY01() {
        assertEquals(listOf("X-Y"), outputOf("ARRAY01"))
    }

    @Test
    fun executeCTDATA() {
        assertEquals(expected =
                    ("001\n" +
                    "d01\n" +
                    "A01\n" +
                    "c01\n" +
                    "B01\n" +
                    "b01\n" +
                    "C01\n" +
                    "901\n" +
                    "101\n" +
                    "D01\n" +
                    "H01\n" +
                    "E01\n" +
                    "201\n" +
                    "e01\n" +
                    "a01\n" +
                    "x01\n" +
                    "X01").lines(),
                    actual = outputOf("CTDATA").map(String::trim))
    }

    @Test
    fun executeARRAY02_arrayWithComments() {
        assertEquals(expected =
            ("abc\n" +
            "123\n" +
            "abc\n" +
            "123\n" +
            "abc\n" +
            "123\n" +
            "abc\n" +
            "123\n" +
            "abc\n" +
            "123\n" +
            "abc\n" +
            "xxx").lines(),
            actual = outputOf("ARRAY02").map(String::trim))
    }

    @Test
    fun executeARRAY03_arrayWithCommentsPERRCD_1() {
        assertEquals(expected =
            ("abc\n" +
            "123\n" +
            "abc\n" +
            "123\n" +
            "abc\n" +
            "123\n" +
            "abc\n" +
            "123\n" +
            "abc\n" +
            "123\n" +
            "abc\n" +
            "xxx").lines(),
            actual = outputOf("ARRAY02").map(String::trim))
    }

    @Test
    fun executeARRAY04_arrayWithCommentsAndDataReference() {
        assertEquals(expected =
            ("abc\n" +
            "123\n" +
            "abc\n" +
            "123\n" +
            "abc\n" +
            "123\n" +
            "abc\n" +
            "123\n" +
            "abc\n" +
            "123\n" +
            "abc\n" +
            "xxx").lines(),
            actual = outputOf("ARRAY04").map(String::trim))
    }

    @Test
    fun executeARRAY05NAM_namedCompileTimeArrays() {
        assertEquals(expected =
            ("100\n" +
            "100\n" +
            "100\n" +
            "100\n" +
            "100").lines(),
            actual = outputOf("ARRAY05NAM").map(String::trim))
    }

    @Test
    fun executeBIFARRAY() {
        assertEquals(listOf("001001d01d01A01A01c01c01B01B01b01b01C01C019019011011", "10"), outputOf("BIFARRAY"))
    }

    @Test
    fun executeSCANTEST() {
        assertEquals(listOf("0", "4"), outputOf("SCANTEST"))
    }

    @Test
    fun executeCLEARSUBR() {
        assertEquals(listOf("Result = 5"), outputOf("CLEARSUBR"))
    }

    @Test
    fun executeZADD() {
        assertEquals(listOf("5", "11", "88"), outputOf("ZADD"))
    }

    @Test
    fun executeLOGICAL_conditions() {
        assertEquals(listOf("A<=B", "OK"), outputOf("LOGICAL"))
    }

    // TODO implement DataStructureType coercion
    @Test @Ignore
    fun executeDSNUMERIC() {
        assertEquals(listOf("Result is: 3"), outputOf("DSNUMERIC"))
    }

    // TODO understand why this test does not pass
    @Test @Ignore
    fun executeZADDERR() {
        // TODO better error assertion
        assertFailsWith(Throwable::class) {
            execute("ZADDERR", emptyMap())
        }
    }

    @Test
    fun executeSUBSTTEST() {
        assertEquals(listOf("x)yy"), outputOf("SUBSTTEST"))
    }

    // TODO understand why this test does not pass
    @Test @Ignore
    fun executeJCODFISD() {
        val parms = mapOf("CFDS" to StringValue("LNZNLN09B63H501J"),
            "FISICA" to BooleanValue(false),
            "OMONIM" to BooleanValue(false),
            "SINTAX" to BooleanValue(false),
            "CHKDIG" to BooleanValue(false)
            )
        assertEquals(outputOf("JCODFISD", parms), emptyList<String>())
    }

    // TODO Changes in grammar needed
    @Test @Ignore
    fun executeProgramWithAVarNamedLen() {
        assertEquals(listOf("10"), outputOf("VARNAMEDLEN"))
    }

    @Test
    fun executeProgramWithRuntimeError() {
        // TODO better error assertion
        assertFailsWith(Throwable::class) {
            execute("ERROR01", emptyMap())
        }
    }
}
