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

package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.*
import com.smeup.rpgparser.execution.*
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.jvminterop.JvmProgramRaw
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.parsing.facade.AstCreatingException
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import com.smeup.rpgparser.utils.asInt
import org.junit.Ignore
import org.junit.Test
import org.junit.experimental.categories.Category
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlin.test.fail

open class InterpreterTest : AbstractTest() {

    @Test
    fun doubleVarDefinitionWithDifferentTypeShouldThrowAnError() {
        val systemInterface = JavaSystemInterface()

        val source = """
|     D n               S              1  0 inz(00)
|     D n               S              1  0 inz(00)
|     C                   SETON                                          LR
        """.trimMargin()

        val program = getProgram(source, systemInterface)
        assertFailsWith(AstCreatingException::class) {
            program.singleCall(listOf())
        }
    }

    @Test
    fun executeCALCFIB_initialDeclarations_dec() {
        val cu = assertASTCanBeProduced("CALCFIB_1", true)
        cu.resolveAndValidate()
        val interpreter = execute(cu, mapOf("ppdat" to StringValue("3")))
        assertIsIntValue(interpreter["NBR"], 3)
    }

    @Test
    fun executeCALCFIB_initialDeclarations_inz() {
        val cu = assertASTCanBeProduced("CALCFIB_1", true)
        cu.resolveAndValidate()

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
        cu.resolveAndValidate()
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
        cu.resolveAndValidate()
        val si = CollectorSystemInterface()
        val logHandler = ListLogHandler()
        execute(cu, mapOf("ppdat" to StringValue(input)), si, listOf(logHandler))
        assertEquals(listOf("FIBONACCI OF: ${input.padEnd(8)} IS: $output"), si.displayed)
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
        cu.resolveAndValidate()
        val si = CollectorSystemInterface()
        val logHandler = ListLogHandler()
        execute(cu, mapOf(), si, listOf(logHandler))
        assertEquals(listOf("Hello World!"), si.displayed)
        assertEquals(logHandler.getExecutedSubroutines().size, 0)
    }

    @Test
    fun executeCallToFibonacciWrittenInRpg() {
        val cu = assertASTCanBeProduced("CALCFIBCAL", true)
        cu.resolveAndValidate()
        val si = CollectorSystemInterface()
        val logHandler = ListLogHandler()
        si.programs["CALCFIB"] = rpgProgram("CALCFIB")
        execute(cu, mapOf("ppdat" to StringValue("10")), si, listOf(logHandler))
        assertEquals(listOf("FIBONACCI OF: 10       IS: 55"), si.displayed)
        assertEquals(1, logHandler.getExecutedSubroutines().size)
    }

    @Test
    fun executeCallToFibonacciWrittenOnTheJvm() {
        val cu = assertASTCanBeProduced("CALCFIBCAL", true)
        cu.resolveAndValidate()
        val si = CollectorSystemInterface()
        val logHandler = ListLogHandler()
        si.programs["CALCFIB"] = object : JvmProgramRaw("CALCFIB", listOf(ProgramParam("ppdat", StringType(8, false)))) {
            override fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>): List<Value> {
                val n = params["ppdat"]!!.asString().value.asInt()
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
        cu.resolveAndValidate()
        val si = CollectorSystemInterface()
        val rpgProgram = RpgProgram(cu)
        rpgProgram.execute(si, linkedMapOf("ppdat" to StringValue("10")))
        assertEquals(1, rpgProgram.params().size)
        assertEquals(ProgramParam("ppdat", StringType(8, false)), rpgProgram.params()[0])
        assertEquals(listOf("FIBONACCI OF: 10       IS: 55"), si.displayed)
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
        assertEquals(listOf(
            "Hello World! 23",
            "%LEN(B_01) is 1",
            "%LEN(B_02) is 20",
            "%LEN(B_03) is 0",
            "%LEN(B_03) is 1",
            "%LEN(B_03) is 5",
            "%LEN(B_04) is 0"
        ), outputOf("LEN"))
    }

    @Test
    fun executeSQRT() {
        assertEquals(listOf("489.76933346"), outputOf("SQRT"))
    }

    @Test
    fun executeROUNDING01() {
        assertEquals(listOf("34"), outputOf("ROUNDING01"))
    }

    @Test
    fun executeSETONSETOF() {
        assertEquals(listOf("Before",
                "56=off57=off",
                "After set",
                "56=on",
                "After off",
                "56=off57=off"), outputOf("SETONOF01"))
    }

    @Test @Ignore
    fun executeSETOFLF() {
        assertEquals(listOf("Before",
                            "LR Of",
                            "After set",
                            "LR Off"), outputOf("SETOFLR"))
    }

    @Test
    fun executeDCONST() {
        assertEquals(listOf("60"), outputOf("DCONST"))
    }

    // this test tries to assign in different ways a constant and
    // it expects that assignment fails
    @Test
    fun executeDCONST_assignments() {
        val eval = """
     Dy                C                   CONST(30)
     C                   Eval      y = 12            
        """
        val move = """
     Dx                C                   CONST(30)
     Dy                C                   CONST(30)
     C                   move      x             y            
        """
        val add = """
     Dy                C                   CONST(30)
     C                   add       1             y    
        """
        listOf(eval, move, add).forEach { source ->
            kotlin.runCatching {
                println("Executing:$source")
                getProgram(nameOrSource = source).singleCall(emptyList())
            }.onSuccess {
                fail(message = "Program:\n$source\ndid not have to be executed properly.")
            }.onFailure {
                assert(it.message!!.indexOf("is a const and cannot be assigned") != -1) {
                    "Exception message doesn't contain expected message but contains:\n${it.message}"
                }
            }
        }
    }

    @Test
    fun executeCALLDEFVAR() {
        assertEquals(listOf("", "R"), outputOf("CALLDEFVAR"))
    }

    @Test
    fun executeCALLDEF01_dataDefinitionsInSelect() {
        assertEquals(listOf("OK"), outputOf("CALLDEF01"))
    }

    @Test
    fun executeCALLDEF02_dataDefinitionsInNestedStatements() {
        assertEquals(listOf("OK"), outputOf("CALLDEF02"))
    }

    @Test
    fun executeHELLOVARST() {
        assertEquals(listOf("Eq", "Hello-World", "Hello-World"), outputOf("HELLOVARST"))
    }

    @Test
    fun executeVARST1() {
        assertEquals(listOf("A", "A", "A", "AA", "A"), outputOf("VARST1"))
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
    fun executeSUBDURTES2() {
        assertEquals(listOf("40208"), outputOf("SUBDURTES2"))
    }

    @Test
    fun executeDIFFTEST2() {
        assertEquals(listOf("40208"), outputOf("DIFFTEST2"))
    }

    @Test @Ignore
    fun executeDAYOFWEEK() {
        val currentDayOfWeek = SimpleDateFormat("u").format(Date())
        assertEquals(listOf(currentDayOfWeek), outputOf("DAYOFWEEK"))
    }

    @Test
    fun executeCALCFIBCA5() {
        assertEquals(listOf("FIBONACCI OF: 10       IS: 55"), outputOf("CALCFIBCA5"))
    }

    @Test
    fun executeCAL01_callingRPGPgm() {
        assertEquals(listOf("1"), outputOf("CAL01"))
    }

    @Test
    fun executeCAL03_recursive() {
        val parms = mapOf("P1" to StringValue(" "))
        assertEquals(listOf("", "HELLO"), outputOf("CAL03", parms))
    }

    @Test
    fun executeZERO() {
        assertEquals(listOf("0", "69", "0"), outputOf("ZERO"))
    }

    @Test
    fun executeZEROS1() {
        assertEquals(listOf("33"), outputOf("ZEROS1"))
    }

    @Test
    fun executeMOVEL01() {
        assertEquals(listOf("1111.1"), outputOf("MOVEL01"))
    }

    @Test
    fun executeMOVEL02() {
        assertEquals(listOf("78425"), outputOf("MOVEL02"))
    }

    @Test
    fun executeMOVEL03_zeros() {
        assertEquals(listOf("0"), outputOf("MOVEL03"))
    }

    @Test
    fun executeMOVEL04_all1() {
        assertEquals(listOf("11111"), outputOf("MOVEL04"))
    }

    @Test
    fun executeMOVEL05_allChar() {
        assertEquals(listOf("aaaaa"), outputOf("MOVEL05"))
    }

    @Test
    fun executeMOVEL06_blanks() {
        assertEquals(listOf(""), outputOf("MOVEL06"))
    }

    @Test
    fun executeMOVELDEF1_variable_definition_after_first_usage() {
        assertEquals(listOf("02"), outputOf("MOVELDEF1"))
    }

    @Test
    fun executeMOVEAMUT13() {
        assertEquals(listOf("ABCDEFGHIL         1",
                            "BBBBBBBBBBBBBBBBBBBB",
                            "AAAAAAAAAAAAAAAAAAAA",
                            "  ABCDEFGHILMNOPQRST",
                            "  ABCDEFGHILMNOPQRST",
                            "XXXXXXXXXXXXXXXXXXXX",
                            "XXXXXXXXXXXXXXXXXXXX",
                            "XXXXXXXXXXXXXXXXXXXX",
                            "XXXXXXXXXXXXXXXXXXXX",
                            "XXXXXXXXXXXXXXXXXXXX"),
            outputOf("MOVEAMUT13"))
    }

    @Test
    fun executeMOVEAP0904_p_with_chars() {
        assertEquals(listOf("ABCDEFGHIL",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""),
            outputOf("MOVEAP0904"))
    }

    @Test
    fun executeMOVEAPNBR_p_with_Numbers() {
        assertEquals(listOf("10", "11", "12", "10", "0"), outputOf("MOVEAPNBR"))
    }

    @Test
    fun executeMOVEA01() {
        assertEquals(listOf("ABCD", "ABCD", "ABCD", "CDCD", "ABCD", "ABCD"), outputOf("MOVEA01"))
    }

    @Test
    fun executeMOVEA01NBR() {
        assertEquals(listOf("1234", "1234", "56", "1234"), outputOf("MOVEA01NBR"))
    }

    @Test
    fun executeMOVEAC1_arrayToArrayOfChars() {
        assertEquals(listOf("123", "456", "789", "0DD"), outputOf("MOVEAC1"))
    }

    @Test
    fun executeMOVEAC2_arrayToSubArrayOfChars() {
        assertEquals(listOf("AAA", "123", "456", "789", "0EE"), outputOf("MOVEAC2"))
    }

    @Test
    fun executeMOVEAC4_subArrayToArrayOfChars() {
        assertEquals(listOf("345", "678", "90C", "DDD", "EEE"), outputOf("MOVEAC4"))
    }

    @Test
    fun executeMOVEAC5_subArrayToSubArrayOfChars() {
        assertEquals(listOf("AAA", "345", "678", "90D", "EEE"), outputOf("MOVEAC5"))
    }

    @Test
    fun executeMOVEAC6_subArrayToCharValue() {
        assertEquals(listOf("34567890ILMNOPQ"), outputOf("MOVEAC6"))
    }

    @Test
    fun executeMOVEAC7_arrayToCharValue() {
        assertEquals(listOf("1234567890MNOPQ"), outputOf("MOVEAC7"))
    }

    @Test
    fun executeMOVEA01B() {
        assertEquals(listOf("ABCD", "ABCD", "1234", "5678"), outputOf("MOVEA01B"))
    }

    @Test
    fun executeMOVEA02() {
        assertEquals(listOf("1234", "5678", "90CD"), outputOf("MOVEA02"))
    }

    @Test
    fun executeMOVEA03() {
        assertEquals(listOf("ABCD", "1234", "5678"), outputOf("MOVEA03"))
    }

    @Test
    fun executeMOVEA04() {
        assertEquals(listOf("ABCD", "1234", "56CD"), outputOf("MOVEA04"))
    }

    @Test
    fun executeMOVELDECLA_declaringVarsWithMOVEL() {
        assertEquals(listOf("XXXXX", "YYYYY"), outputOf("MOVELDECLA"))
    }

    @Test @Ignore
    fun executeRESULTTARG_resultAsExpressionRefenecingArray() {
        assertEquals(listOf("15.3"), outputOf("RESULTTARG"))
    }

    @Test
    fun executeXFOOT1() {
        assertEquals(listOf("15.3"), outputOf("XFOOT1"))
    }

    @Test
    fun executeXFOOT2DEF_creatingVariable() {
        assertEquals(listOf("15.3"), outputOf("XFOOT2DEF"))
    }

    @Test
    fun executeDEFINE01() {
        assertEquals(listOf("Hello"), outputOf("DEFINE01"))
    }

    @Test
    fun executeDEFINE02_defineOfVarCreatedByAnotherDefine() {
        assertEquals(listOf("Hello"), outputOf("DEFINE02"))
    }

    @Test
    fun executeMOVEA05() {
        assertEquals(listOf("ABCD", "ABCD", "ABCD", "", "", ""), outputOf("MOVEA05"))
    }

    @Test
    fun executeARRAY10() {
        assertEquals(listOf("AB  CD  EF"), outputOf("ARRAY10"))
    }

    @Test
    fun executeEVALARRAY1() {
        assertEquals(listOf("ABCDEFGHIL", "ABCDEFGHIL", "ABCDEFGHIL", "  XXXXXXXXXXXXXXXXXX"), outputOf("EVALARRAY1"))
    }

    @Test
    fun executeSTRNOTVA() {
        assertEquals(listOf("AB  CD  EF"), outputOf("STRNOTVA"))
    }

    @Test
    fun executeARRAY11() {
        assertEquals(listOf("ABCDEF", "ABCDEF"), outputOf("ARRAY11"))
    }

    @Test
    fun executeSORTANUM() {
        assertEquals(listOf("0", "1", "4", "5", "6", "8", "9"), outputOf("SORTANUM"))
    }

    @Test @Ignore
    fun executeLOOKUP_OPCODE() {
        assertEquals("""
Test 1
68 OFF
69 ON-
10    
Test 2
68 OFF
69 ON-
 3    
Test 3
68 OFF
69 OFF
 1    
Test 4
68 OFF 
69 OFF 
 1     
Test 5 
68 ON- 
69 OFF 
 9     
Test 6 
68 OFF 
69 ON- 
 7               
        """.trimIndent().lines().map(String::trim),
            outputOf("LOOKUP_OP1"))
    }

    @Test
    fun executeLOOKUP_OP2() {
        assertEquals(listOf("68 OFF", "69 OFF"), outputOf("LOOKUP_OP2"))
    }

    @Test
    fun executeLOOKUP_OP3() {
        assertEquals(listOf("68 ON-", "69 OFF"), outputOf("LOOKUP_OP3"))
    }

    @Test @Ignore
    fun executeLOOKUP_OPCODE_GoodArray() {
        assertEquals("""
Test 1
68 ON-
69 OFF
10    
Test 2
68 OFF
69 ON-
 4    
Test 3
68 OFF
69 ON-
 4    
Test 4
68 OFF
69 OFF
 1    
Test 5
68 ON-
69 OFF
 9    
Test 6
68 OFF
69 OFF
 1    
        """.trimIndent().lines().map(String::trim),
            outputOf("LOOKUP_OP9"))
    }

    @Test @Ignore
    fun executeSCANARRAY() {
        assertEquals(listOf("4"), outputOf("SCANARRAY"))
    }

    @Test
    fun executePROCEDURE1() {
        assertEquals(listOf("33"), outputOf("PROCEDURE1"))
    }

    @Test
    fun executePROCEDURE1UsingMemoryStorage() {
        val configuration = Configuration(memorySliceStorage = IMemorySliceStorage.createMemoryStorage(mutableMapOf()))
        assertEquals(listOf("33"), outputOf("PROCEDURE1", configuration = configuration))
    }

    @Test
    fun executePROCEDURE2_callAsFunction() {
        assertEquals(listOf("33"), outputOf("PROCEDURE2"))
    }

    @Test
    fun executePROCEDURE3_constExpressionWithTypeCast() {
        assertEquals(listOf("33"), outputOf("PROCEDURE3"))
    }

    @Test
    fun executePROCEDURE4_errorModifyingConstParameter() {
        // TODO Define a better exception
        assertFailsWith(Throwable::class) {
            outputOf("PROCEDURE4")
        }
    }

    @Test
    fun executePROCEDURE5_localVarNames() {
        assertEquals(listOf("33"), outputOf("PROCEDURE5"))
    }

    @Test
    fun executePROCEDURE6_shadowingOfVars() {
        assertEquals(listOf("25"), outputOf("PROCEDURE6"))
    }

    @Test
    fun executeCALLER_CALLED_pgm_with_RT() {
        assertEquals(listOf(
            "Executing CALLER",
            "x initialized at: 18",
            "x is now: 6",
            "Calling CALLED",
            "Executing CALLED",
            "x initialized at: 9",
            "x is now: 3",
            "Calling CALLED",
            "Executing CALLED",
            "x initialized at: 3",
            "x is now: 1"
            ),
            outputOf("CALLER"))
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
    fun executeDOVAR01_ModifyingEndVarAffectsDO() {
        assertEquals(outputOf("DOVAR01"), listOf("N =101", "I =96"))
    }

    @Test
    fun executeDOVAR02_ModifyingStartVarDoesntAffectDO() {
        assertEquals(outputOf("DOVAR02"), listOf("N =11", "I =6"))
    }

    @Test
    fun executeDO_TST01() {
        val si = CollectorSystemInterface().apply { printOutput = true }
        assertStartsWith(outputOf("DO_TST01", si = si), "DO_TST01(91ms) Spent")
    }

    @Test @Category(PerformanceTest::class)
    fun executeDOW_PERF() {
        assertEquals(outputOf("DOW_PERF"), listOf("COUNTER IS NOW 100000001"))
    }

    @Test @Category(PerformanceTest::class)
    fun executeDOW_LEAVE_PERF() {
        assertEquals(outputOf("DOW_LEAVE_PERF"), listOf("COUNTER IS NOW 100000001"))
    }

    @Test
    fun executePLUSEQUAL() {
        assertEquals(outputOf("PLUSEQUAL"), listOf("COUNTER IS NOW 6"))
    }

    @Test
    fun executeREMTEST() {
        assertEquals(listOf("1", "1", "-1", "-1"), outputOf("REMTEST"))
    }

    @Test
    fun executeASSIGN() {
        assertEquals(listOf("x is now 2", "y is now 162", "z is now 12", "w is now 198359290368"), outputOf("ASSIGN"))
    }

    @Test
    fun executePOWER() {
        assertEquals(listOf("i is now 8"), outputOf("POWER"))
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
    fun executeREPLACEBIF() {
        assertEquals(listOf("Pippo world!", "Hello Pippo!", "Hello Pippoorld!", "Hello Pippold!", "Hello Pippoworld!", "%20 ef", "abc%20ef"),
            outputOf("REPLACEBIF"))
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
        assertEquals(listOf("x   123,456   123,456-  1,234.56X",
                            "x  1,234.56-       .00X",
                            "x  1,234.50X"),
            outputOf("BIFEDITC_J"))
    }

    @Test
    @Ignore // we are working on DECEDIT
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
        assertEquals(listOf("0", "4", "1"), outputOf("SCANTEST"))
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
    fun executeSTARALL_ZADD() {
        assertEquals(listOf("51515"), outputOf("STARALL_ZADD"))
    }

    @Test
    fun executeSTARALL_MOVE() {
        assertEquals(listOf("WWWWWWWWWW"), outputOf("STARALL_MOVE"))
    }

    @Test
    fun executeSTARALL_EVAL() {
        assertEquals(listOf("11111"), outputOf("STARALL_EVAL"))
    }

    @Test
    fun EVALwithTypeError() {
        val systemInterface = JavaSystemInterface()

        val source = """
|     D n               S              1  0 inz(00)
|     C                   Eval      n = 'Hello World!'
|     C                   SETON                                          LR
        """.trimMargin()

        val program = getProgram(source, systemInterface)
        assertFailsWith(IllegalArgumentException::class) {
            program.singleCall(listOf())
        }
    }

    @Test
    fun executeMOVELOVAL() {
        assertEquals(listOf("99-"), outputOf("MOVELOVAL"))
    }

    @Test
    fun executeCHECK() {
        assertEquals(listOf("Wrong char at 6", "Wrong char at 7", "No wrong chars 0"), outputOf("CHECK"))
    }

    @Test
    fun executeLOGICAL_conditions() {
        assertEquals(listOf("A<=B", "OK"), outputOf("LOGICAL"))
    }

    @Test
    fun executeBOOLSTRING_conversion() {
        assertEquals(listOf("B<>1", "B=0", "0"), outputOf("BOOLSTRING"))
    }

    @Test
    fun executeDSNUMERIC() {
        assertEquals(listOf("Result is: 3"), outputOf("DSNUMERIC"))
    }

    // TODO implement DataStructureType coercion
    @Test @Ignore
    fun executeDSCHARS() {
        assertEquals(listOf("Result is: X 1Y 2"), outputOf("DSCHARS"))
    }

    @Test
    fun executeDSCHARS2() {
        assertEquals(listOf("A123456789"), outputOf("DSCHARS2"))
    }

    @Test
    fun executeDSCHARS3_assign_whole_DS() {
        assertEquals(listOf("20200901", "BERWSD"), outputOf("DSCHARS3"))
    }

    @Test
    fun executeDSCHARS4_assign_ds_subfields() {
        assertEquals(listOf("20200901", "BERWSD"), outputOf("DSCHARS4"))
    }

    @Test
    fun executeDSCHARS5_clear_ds_fields() {
        assertEquals(listOf("TEST", ""), outputOf("DSCHARS5"))
    }

    @Test
    fun executeDSCHAR6_string_to_ds_field() {
        assertEquals(listOf("1"), outputOf("DSCHARS6"))
    }

    @Test
    fun executeDS_CALLED_with_String_parameter() {
        assertEquals(listOf("James", "Bond", "007"),
            outputOf("DS_CALLED", initialValues = mapOf("P1" to StringValue("JamesBond   7"))))
    }

    @Test
    fun executeDS_CALLED_with_DS_parameter() {
        assertEquals(listOf("James", "Bond", "007"),
            outputOf("DS_CALLED", initialValues = mapOf("P1" to DataStructValue("JamesBond   7"))))
    }

    @Test
    fun executeFRSTCHRCOM_CommentInFirstChars() {
        assertEquals(listOf("Hello!"), outputOf("FRSTCHRCOM"))
    }

    @Test
    fun executeZADDERR() {
        assertFailsWith(IllegalStateException::class) {
            execute("ZADDERR", emptyMap())
        }
    }

    @Test
    fun executeZADDNOERR() {
        assertEquals(listOf("88"), outputOf("ZADDNOERR"))
    }

    @Test
    fun executeZADD_DefiningData() {
        assertEquals(listOf("88"), outputOf("ZADD2"))
    }

    @Test
    fun executeSUBSTTEST() {
        assertEquals(listOf("x)yy"), outputOf("SUBSTTEST"))
    }

    @Test
    fun executeASSIGNSUBS() {
        assertEquals(listOf("-xyz--", "-xyz", "-xyz -"), outputOf("ASSIGNSUBS"), message = "We can't handle %SUBST(X)=Y")
    }

    @Test
    fun executeABSTEST() {
        assertEquals(listOf("X is 1.23"), outputOf("ABSTEST"), message = "We can't handle %ABS(X)")
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
        assertEquals(outputOf("JCODFISD", parms), emptyList())
    }

    @Test
    fun executeReturn01() {
        assertEquals(listOf("Starting"), outputOf("RETURN01"))
    }

    @Test
    fun executeGoto01() {
        assertEquals(listOf("1", "2", "3", "4"), outputOf("GOTO01"))
    }

    @Test
    fun executeGoto02() {
        assertEquals(listOf("1", "2", "3", "4"), outputOf("GOTO02"))
    }

    @Test
    fun executeGoto02N() {
        assertEquals(listOf("1", "2", "3", "4"), outputOf("GOTO02N"))
    }

    @Test
    fun executeGotoENDSR() {
        assertEquals(listOf("1", "2", "3"), outputOf("GOTOENDSR"))
    }

    @Test
    fun executeLEAVESR() {
        assertEquals(listOf("1", "2", "3"), outputOf("LEAVESR"))
    }

    @Test
    fun executeCABLEOK() {
        assertEquals(listOf("Test OK"), outputOf("CABLEOK"))
    }

    @Test
    fun executeCABLEOK2() {
        assertEquals(listOf("Test OK"), outputOf("CABLEOK2"))
    }

    @Test
    fun executeCABLEKO() {
        assertEquals(listOf("Test KO"), outputOf("CABLEKO"))
    }

    @Test
    fun executeCABGEOK() {
        assertEquals(listOf("Test OK"), outputOf("CABGEOK"))
    }

    @Test
    fun executeCABGEOK2() {
        assertEquals(listOf("Test OK"), outputOf("CABGEOK2"))
    }

    @Test
    fun executeCABGEKO() {
        assertEquals(listOf("Test KO"), outputOf("CABGEKO"))
    }

    @Test
    fun executeCABEQOK() {
        assertEquals(listOf("Test OK"), outputOf("CABEQOK"))
    }

    @Test
    fun executeCABEQKO() {
        assertEquals(listOf("Test KO"), outputOf("CABEQKO"))
    }

    @Test
    fun executeCAB_OK() {
        assertEquals(listOf("Test OK"), outputOf("CAB_OK"))
    }

    @Test
    fun executeCAB_OKINDLower() {
        assertEquals(listOf("Test OK", "42ON"), outputOf("CAB_OKINDL"))
    }

    @Test
    fun executeCAB_OKINDGreater() {
        assertEquals(listOf("Test OK", "41ON"), outputOf("CAB_OKINDG"))
    }

    @Test
    fun executeCAB_OKINDEqual() {
        assertEquals(listOf("Test OK", "43ON"), outputOf("CAB_OKINDE"))
    }

    @Test
    fun executeERRCALLER_errorIndicatorOnCall() {
        assertEquals(listOf("10", "No Error", "Got Error"), outputOf("ERRCALLER"))
    }

    @Test @Ignore
    fun executeRTCALLER_lrIndicatorOnCall() {
        assertEquals(listOf("End LR", "End RT"), outputOf("RTCALLER"))
    }

    @Test
    fun executeProgramWithAVarNamedLen() {
        assertEquals(listOf("10"), outputOf("VARNAMEDLEN"))
    }

    @Test
    fun executeECHO() {
        assertEquals(listOf("Hello"), outputOf("ECHO", mapOf("inTxt" to StringValue("Hello"))))
    }

    @Test
    fun executeFIZZBUZZ() {
        assertEquals(listOf("7"),
            outputOf("mute/FIZZBUZZ", mapOf("NBRPAR" to StringValue("7"), "RESULT" to StringValue(""))))

        assertEquals(listOf("FIZZ"),
            outputOf("mute/FIZZBUZZ", mapOf("NBRPAR" to StringValue("3"), "RESULT" to StringValue(""))))

        assertEquals(listOf("BUZZ"),
            outputOf("mute/FIZZBUZZ", mapOf("NBRPAR" to StringValue("5"), "RESULT" to StringValue(""))))

        assertEquals(listOf("FIZZBUZZ"),
            outputOf("mute/FIZZBUZZ", mapOf("NBRPAR" to StringValue("30"), "RESULT" to StringValue(""))))
    }

    @Test
    fun executeECHO2() {
        assertEquals(listOf("Hello"), outputOf("ECHO2", mapOf("inTxt" to StringValue("Hello"))))
    }

    @Test
    fun executeCOLDFILEFN() {
        assertEquals(listOf("0", "0"), outputOf("COLDFILEFN"))
    }

    @Test
    fun executeDOU() {
        assertEquals(listOf("1", "2", "3"), outputOf("DOU", mapOf("inN" to StringValue("3"))))
        assertEquals(listOf("1"), outputOf("DOU", mapOf("inN" to StringValue("0"))))
    }

    @Test
    fun executeNOTisCaseInsensitive() {
        assertEquals(listOf("NotCondition"), outputOf("NOTCASEINS"))
    }

    @Test
    fun executeNOTDoesntNeedBrackets() {
        assertEquals(listOf("NotCondition"), outputOf("NOTBRACKET", printTree = true))
    }

    @Test
    fun executeINTEST() {
        assertEquals(listOf("910", "5602", "1234", "-910", "-5602", "-910", "12326", "-5602"), outputOf("INTTEST"))
    }

    @Test
    fun executeDECTEST() {
        assertEquals(listOf("N1=N2"), outputOf("DECTEST"))
    }

    @Test
    fun executeEDITWTEST() {
        assertEquals(listOf("x 12340", "x 012340", "x 1/12/99", "x 8:23:45"), outputOf("EDITWTEST"))
    }

    @Test
    fun executeEDITWTEST2() {
        assertEquals(listOf("x 08:23:45", "x   2.345", "x   2.345-", "x  21,4-%"), outputOf("EDITWTEST2"))
    }

    @Test
    fun executeWHEN01() {
        assertEquals(listOf("Other", "First"), outputOf("WHEN01"))
    }

    @Test
    fun executeARRAY_PARMS() {
        val parms = mapOf(
            "Arr" to StringValue("ABC".padEnd(40))
        )
        assertEquals(listOf("ABC"), outputOf("ARRAY_PARMS", parms))
    }

    @Test
    fun executeARRAY06() {
        assertEquals(listOf("A-A-A", "AB-AB-AB", "ABC -ABC -ABC -"), outputOf("ARRAY06"))
    }

    @Test
    fun executeTRIML() {
        assertEquals(listOf("Hello world!", "llo world!", "ello world!"), outputOf("TRIML"))
    }

    @Test
    fun executeTRIMR() {
        assertEquals(listOf("Hello world!", "Hello worl", "Hello world"), outputOf("TRIMR"))
    }

    @Test @Ignore
    fun executeELEM() {
        assertEquals(listOf("10", "20", "30"), outputOf("ELEM"))
    }

    @Test
    fun executeSUMDIVMULT() {
        assertEquals(listOf("20.1", "19.9", "2.0", "200.0"), outputOf("SUMDIVMULT"))
    }

    @Test @Ignore
    fun executeACTGRP_CAL() {
        assertEquals(listOf("1", "2", "3", "1", "1", "1"), outputOf("ACTGRP_CAL"))
    }

    @Test @Ignore
    fun executeCLEARDS() {
        assertEquals(listOf("0000"), outputOf("CLEARDS"))
    }

    @Test
    @Ignore
    fun executeCLEARARRAY() {
        assertEquals(
            listOf(
                "11111111111AAAAAAAAAAA",
                "                      ",
                "                      ",
                "                      ",
                "11111111111AAAAAAAAAAA",
                "22222222222BBBBBBBBBBB",
                "                      ",
                "                      ",
                "                      ",
                "22222222222BBBBBBBBBBB",
                "                      ",
                "                      ",
                "                      ",
                "22222222222BBBBBBBBBBB",
                "00000000000           ",
                "                      ",
                "                      ",
                "22222222222BBBBBBBBBBB",
                "00000000000           ",
                "44444444444DDDDDDDDDDD",
                "                      ",
                "                      ",
                "                      ",
                "                      "
            ),
            outputOf("CLEARARRAY"))
    }

    @Test
    @Ignore
    fun executeCLEARARRAY1() {
        assertEquals(
            listOf(
                " ",
                " ",
                "A",
                "B",
                "C",
                " ",
                " ",
                "A",
                " ",
                "C",
                " ",
                " ",
                " ",
                " ",
                " "
            ),
            outputOf("CLEARARRAY1"))
    }

    @Test
    @Ignore
    fun executeMOVELSTR() {
        assertEquals(
            listOf(
                "AAAA",
                "AAAAAAAA",
                "AAAAAAAAAAAA",
                "BBBBB",
                "BBBBBBBBBB",
                "BBBBBBBBBBBBBBBBBBBB",
                "CCC",
                "CCCCCCC",
                "CCCCCCCCCCCCCCCCC",
                "AAAA",
                "AAAAACCC",
                "AAAAAAAAAAAA",
                "AAAAA",
                "AAAABBBBBB",
                "AAAAAAAABBBBBBBBBBBB",
                "AAA",
                "AAAAAAA",
                "AAAAACCCCCCCCCCCC"
            ),
            outputOf("MOVELSTR")
        )
    }

    @Test
    fun executeProgramWithRuntimeError() {
        // TODO better error assertion
        assertFailsWith(Throwable::class) {
            execute("ERROR01", emptyMap())
        }
    }

    @Test
    open fun executeX1X2110() {
        val dsValue = DataStructValue.blank(30448)
        var dsDataDefinition: DataDefinition? = null
        // TODO: 03/12/2020 Move in inline function of CompilationUnit the logic to set e DS
        val configuration = Configuration(
            memorySliceStorage = IMemorySliceStorage.createMemoryStorage(mutableMapOf()),
            jarikoCallback = JarikoCallback(
                afterAstCreation = { ast: CompilationUnit ->
                    ast.dataDefinitions.forEach { dataDefinition ->
                        when (dataDefinition.type) {
                            is DataStructureType -> {
                                dsDataDefinition = dataDefinition
                                dataDefinition.fields.forEach {
                                    when (it.name) {
                                        "\$UIBPG" -> { dsValue.set(it, StringValue("EXP", false)) }
                                        "\$UIBFU" -> { dsValue.set(it, StringValue("X1X2110", false)) }
                                        "\$UIBME" -> { dsValue.set(it, StringValue("FIB", false)) }
                                        "\$UIBD1" -> { dsValue.set(it, StringValue("NUM(10)", false)) }
                                    }
                                }
                            }
                            /* no-op */
                            else -> { }
                        }
                    }
                },
                onEnterPgm = { _: String, symbolTable: ISymbolTable ->
                    symbolTable[dsDataDefinition!!] = dsValue
                }
            )
        )
        executePgmWithStringArgs(
            "X1X2110",
            listOf(""),
            configuration = configuration
        )
    }

    @Test
    fun executeFREE_HELLO() {
        assertEquals(
            expected = "Hello world, Hello world in Chinese: 你好世界, number1 * number2 = 15".split(Regex(", ")),
            actual = outputOf("FREE_HELLO"))
    }

    @Test @Ignore
    fun executeLOSER_PR() {
        executePgm("LOSER_PR")
    }

    @Test
    fun executePROCEDURE_B() {
        assertEquals(
            expected = listOf(
                "mainVar set by main",
                "sameVar set by main",
                "procVar set by proc",
                "sameVar just initialized",
                "sameVar set by proc",
                "D specs inline init!",
                "468.95",
                "mainVar changed by proc",
                "sameVar set by main"
            ),
            actual = outputOf("PROCEDURE_B")
        )
    }

    @Test
    fun executePROCEDURE_C() {
        assertEquals(
            expected = listOf("p received must be 11, is:11",
                "q received must be 22, is:22",
                "r received must be 0, is:0",
                "r=p+q must be 33, is:33",
                "s=q*2 must be 44, is:44",
                "c was *zeros, now must be 33, is:33",
                "d was *zeros, now must be 44, is:44"
            ),
            actual = outputOf("PROCEDURE_C")
        )
    }

    @Test
    fun executePROCEDURE_D() {
        assertEquals(
            expected = "33".split(Regex(",")),
            actual = outputOf("PROCEDURE_D")
        )
    }

    @Test
    fun executePROCEDURE_F() {
        assertEquals(
            expected = "99".split(Regex(",")),
            actual = outputOf("PROCEDURE_F")
        )
    }

    @Test
    fun executePROCEDURE_G() {
        assertEquals(
            expected = "99,66".split(Regex(",")),
            actual = outputOf("PROCEDURE_G")
        )
    }

    @Test
    fun executePROCEDURE_H() {
        assertEquals(
            expected = listOf("11",
                "22",
                "33",
                "0",
                "33",
                "22",
                "121"),
            actual = outputOf("PROCEDURE_H")
        )
    }

    @Test
    fun executePROCEDURE_I() {
        assertEquals(
            expected = listOf("1",
                "4"
            ),
            actual = outputOf("PROCEDURE_I")
        )
    }

    @Test
    fun executePROCEDURE_J() {
        assertEquals(
            expected = listOf("1",
                "4",
                "8",
                "9",
                "27",
                "16-",
                "16-",
                "16-"
            ),
            actual = outputOf("PROCEDURE_J")
        )
    }

    @Test
    fun executePROCEDURE_K() {
        assertEquals(
            expected = listOf("69.12",
                ".59",
                "12345          54321",
                "73.00",
                "1",
                "69.12",
                ".59",
                "12345          54321",
                "73.00",
                "1"
            ),
            actual = outputOf("PROCEDURE_K")
        )
    }

    @Test
    fun executePROCEDURE_L() {
        assertEquals(
            expected = listOf(".99",
                "1.11",
                "9.99"
            ),
            actual = outputOf("PROCEDURE_L")
        )
    }

    @Test
    fun executePROCEDURE_M() {
        assertEquals(
            expected = listOf("2.24",
                "3.36"
            ),
            actual = outputOf("PROCEDURE_M")
        )
    }

    @Test
    @Ignore
    // TODO ignored until 'DS as parameter' will be supported (maybe never?)
    fun executePROCEDURE_N() {
        assertEquals(
            expected = listOf("10.2",
                "ABCDE"
            ),
            actual = outputOf("PROCEDURE_N")
        )
    }

    @Test
    fun executePROCEDURE_O() {
        assertEquals(
            expected = listOf(
                "1.01",
                "2.04",
                "3.09",
                "1.04",
                "1.05",
                "2.22",
                "2.24",
                "2.26",
                "2.28",
                "2.30",
                "6.14",
                "1.01",
                "2.04",
                "3.09",
                "1.04",
                "1.05",
                "11.30",
                "2.22",
                "2.24",
                "2.26",
                "2.28",
                "2.30"
            ),
            actual = outputOf("PROCEDURE_O")
        )
    }

    @Test
    fun executePROCEDURE_P() {
        assertEquals(
            expected = listOf("2.04",
                "1.02",
                "1.03"
            ),
            actual = outputOf("PROCEDURE_P")
        )
    }

    @Test
    fun executePROCEDURE_Q() {
        assertFailsWith(RuntimeException::class) {
            execute("PROCEDURE_Q", emptyMap())
        }
    }

    @Test
    fun executePARMS() {
        val rpgProgramName = "PARMS"
        val cu = assertASTCanBeProduced(rpgProgramName, true)
        cu.resolveAndValidate()
        val logHandler = ListLogHandler()

        // PASS NO PARAMETERS
        var si = CollectorSystemInterface()
        si.programs[rpgProgramName] = rpgProgram(rpgProgramName)
        execute(cu, emptyMap(),
            si, listOf(logHandler))
        assertEquals(listOf("0"), si.displayed)

        // PASS ONE PARAMETER
        si = CollectorSystemInterface()
        si.programs[rpgProgramName] = rpgProgram(rpgProgramName)
        execute(cu, mapOf("P1" to StringValue("5")),
            si, listOf(logHandler))
        assertEquals(listOf("1"), si.displayed)

        // PASS TWO PARAMETERS
        si = CollectorSystemInterface()
        si.programs[rpgProgramName] = rpgProgram(rpgProgramName)
        execute(cu, mapOf("P1" to StringValue("5"),
            "P2" to StringValue("10")),
            si, listOf(logHandler))
        assertEquals(listOf("2"), si.displayed)
    }

    @Test
    fun executePROCEDURE_R() {
        assertEquals(
            expected = listOf("1.01",
                "2.04",
                "3.09",
                "1.04",
                "1.05",
                "2.21",
                "4.44",
                "6.69",
                "2.28",
                "2.30",
                "1.01",
                "1.01",
                "2.04",
                "3.09",
                "1.04",
                "1.05",
                "2.21",
                "2.21",
                "4.44",
                "6.69",
                "2.28",
                "2.30"
            ),
            actual = outputOf("PROCEDURE_R")
        )
    }

    @Test
    fun executePROCEDURE_S() {
        assertEquals(
            expected = listOf("10"),
            actual = outputOf("PROCEDURE_S")
        )
    }

    @Test
    fun executeAPIPGM1() {
        assertEquals(
            expected = "100".split(Regex(", ")),
            actual = outputOf("APIPGM1"))
    }

    @Test
    open fun executeDSOVERL() {
        assertEquals(
            expected = "AAAA,BBBB".split(","),
            actual = outputOf("DSOVERL")
        )
    }

    @Test
    fun executeDOPED_PROC() {
        assertEquals(
            expected = listOf("46",
                "12",
                "-22",
                "56",
                "56",
                "7",
                "5",
                "30"
            ),
            actual = outputOf("DOPED_PROC")
        )
    }

    @Test
    fun executeDOPED_PROC2() {
        assertFailsWith(RuntimeException::class) {
            executePgm("DOPED_PROC2")
        }
    }

    @Test
    fun executeDOPED_PROC3() {
        assertEquals(
            expected = listOf("12",
                "46",
                "56",
                "99"
            ),
            actual = outputOf("DOPED_PROC3")
        )
    }

    @Test
    fun executePERF_PROC_1() {
        val si = CollectorSystemInterface().apply { printOutput = true }
        assertStartsWith(outputOf("PERF_PROC_1", si = si), "RPG_SUM : Cycled=100001")
    }

    @Test
    fun executePERF_PROC_2() {
        val si = CollectorSystemInterface().apply { printOutput = true }
        assertStartsWith(outputOf("PERF_PROC_2", si = si), "JDP_SUM : Cycled=100001")
    }

    @Test
    fun executeJAJAX1C() {
        assertEquals(
            expected = listOf("Ahi quanto a dir qual era è cosa dura,esta selva selvaggia",
                "Lupa"
            ),
            actual = outputOf("JAJAX1C")
        )
    }

    @Test
    fun executeJAJAX1C_2() {
        assertEquals(
            expected = listOf("ERB",
                "Erbusco"
            ),
            actual = outputOf("JAJAX1C_2")
        )
    }

    @Test
    fun executeINZSR() {
        assertEquals(
            expected = listOf("HELLO IN RT", "HELLO IN LR", "HELLO IN LR"),
            actual = outputOf("INZSR")
        )
    }

    @Test
    fun executePGM_A() {
        val configuration = Configuration(
            memorySliceStorage = IMemorySliceStorage.createMemoryStorage(mutableMapOf())
        )
        assertEquals(
            listOf("Echo P1: INZ",
                "Echo P2:",
                "Echo P1: INZ",
                "Echo P2:"),
            outputOf("PGM_A", configuration = configuration))

        assertEquals(
            listOf("Echo P1: INZ",
                "Echo P2:",
                "Echo P1: INZ",
                "Echo P2:"),
            outputOf("PGM_A", configuration = configuration))
    }

    // CALL_DIVIDE calls DIVIDE and it expects a regular execution
    @Test
    fun executeCALL_DIVIDE_Ok() {
        val params = CommandLineParms(
            mapOf(
                "A" to DecimalValue(BigDecimal.valueOf(10)),
                "B" to DecimalValue(BigDecimal.valueOf(10)),
                "RESULT" to DecimalValue(BigDecimal.valueOf(0)),
                "CATCHERR" to IntValue(0)
            )
        )
        val result = executePgm(programName = "CALL_DIVIDE", params = params)
        // 10/10 = 1
        assertEquals(
            expected = BigDecimal.valueOf(1).toDouble(),
            actual = result!!.namedParams!!["RESULT"]!!.asDecimal().value.toDouble()
        )
    }

    // CALL_DIVIDE calls DIVIDE forcing an error
    // In this case CALL_DIVIDE program doesn't must handle any error
    @Test
    fun executeCALL_DIVIDE_ErrIndicatorNo() {
        val si = JavaSystemInterface()
        val paramsKo = CommandLineParms(
            mapOf(
                "A" to DecimalValue(BigDecimal.valueOf(10)),
                // Forcing error
                "B" to DecimalValue(BigDecimal.valueOf(0)),
                "RESULT" to DecimalValue(BigDecimal.valueOf(0)),
                "CATCHERR" to IntValue(0)
            )
        )
        assertFailsWith<java.lang.RuntimeException> {
            executePgm(
                programName = "CALL_DIVIDE",
                params = paramsKo,
                systemInterface = si
            )
        }
        // DSPLY must be executed just once
        assertEquals(1, si.consoleOutput.size)
    }

    // CALL_DIVIDE calls DIVIDE forcing an error
    // In this case CALL_DIVIDE program should handle the error
    @Test
    fun executeCALL_DIVIDE_ErrIndicatorYes() {
        // I create JavaSystemInterface to access to the console
        // I create Configuration and set muteSupport to true because
        // CALL_DIVIDE.rpgle contains a mute annotation
        val systemInterface = JavaSystemInterface()
        val configuration = Configuration(options = Options(muteSupport = true))
        val paramsKo = CommandLineParms(
            mapOf(
                "A" to DecimalValue(BigDecimal.valueOf(10)),
                // Forcing error
                "B" to DecimalValue(BigDecimal.valueOf(0)),
                "RESULT" to DecimalValue(BigDecimal.valueOf(0)),
                // Pass to 1 to handle error (view CALL_DIVIDE.rpgle implementation)
                "CATCHERR" to IntValue(1)
            )
        )
        executePgm(
            programName = "CALL_DIVIDE",
            params = paramsKo,
            systemInterface = systemInterface,
            configuration = configuration
        )

        // DSPLY must be executed twice
        assertEquals(2, systemInterface.consoleOutput.size)
    }

    @Test
    fun executeCOPY_INTO_COMMENTS() {
        assertEquals(listOf("Success!"), outputOf("COPY_INTO_COMMENTS"))
    }
}
