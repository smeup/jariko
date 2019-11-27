package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.*
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.jvminterop.JvmProgramRaw
import com.smeup.rpgparser.logging.*
import com.smeup.rpgparser.parsing.parsetreetoast.resolve
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.fail
import org.junit.Ignore
import org.junit.Test
import com.smeup.rpgparser.utils.StringOutputStream
import java.io.PrintStream

class JDExamplesTest {

    @Test
    fun executeJD_000_datainit() {
        val cu = assertASTCanBeProduced("JD_000_datainit", true)
        cu.resolve()

        assertEquals("U\$SVARSK", cu.allDataDefinitions[0].name)
        assertEquals("\$\$SVARCD", cu.allDataDefinitions[1].name)
        assertEquals("\$\$SVARVA", cu.allDataDefinitions[2].name)
        assertEquals(0, (cu.allDataDefinitions[1] as FieldDefinition).startOffset)
        assertEquals(50, (cu.allDataDefinitions[1] as FieldDefinition).endOffset)
        assertEquals(50, (cu.allDataDefinitions[2] as FieldDefinition).startOffset)
        assertEquals(1050, (cu.allDataDefinitions[2] as FieldDefinition).endOffset)

        val interpreter = execute(cu, mapOf())

        val svarsk = interpreter["U\$SVARSK"]
        assertTrue(svarsk is ArrayValue)
        assertEquals(200, svarsk.arrayLength())
        val svarskElement = svarsk.getElement(1)
        assertEquals(DataStructValue(blankString(1050).value), svarskElement)

        val svarcd = interpreter["\$\$SVARCD"]
        assertTrue(svarcd is ArrayValue)
        assertEquals(200, svarcd.arrayLength())
        val svarcdElement = svarcd.getElement(1)
        assertEquals(blankString(50), svarcdElement)

        val svarva = interpreter["\$\$SVARVA"]
        assertTrue(svarva is ArrayValue)
        assertEquals(200, svarva.arrayLength())
        val svarvaElement = svarva.getElement(1)
        assertEquals(blankString(1000), svarvaElement)
    }

    @Test
    fun executeJD_000_base() {
        val cu = assertASTCanBeProduced("JD_000_base", true)
        cu.resolve()
        execute(cu, mapOf())
    }

    @Test
    fun executeJD_000_countsNrOfCalls() {
        val si = ExtendedCollectorSystemInterface()
        val callsToJDURL = LinkedList<Map<String, Value>>()
        si.programs["JD_URL"] = object : JvmProgramRaw("JD_URL", listOf(
                ProgramParam("funz", StringType(10)),
                ProgramParam("method", StringType(10)),
                ProgramParam("URL", StringType(1000)))) {
            override fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>): List<Value> {
                callsToJDURL.add(params)
                return emptyList()
            }
        }
        val cu = assertASTCanBeProduced("JD_000", true)
        cu.resolve()
        execute(cu, mapOf(), systemInterface = si, logHandlers = SimpleLogHandler.fromFlag(false))
        assertEquals(1, callsToJDURL.size)
        val urlCalled = callsToJDURL[0].get("URL")
        assertNotNull(urlCalled)
        assert(urlCalled is ArrayValue)
    }

    @Test
    fun executeJD_000() {
        assertEquals(listOf("", "", "Url", "http://xxx.smaup.com"), outputOf("JD_000"))
    }

    @Test
    fun paramsTypesOfJD_001() {
        val cu = assertASTCanBeProduced("JD_001", true)
        cu.resolve()
        val programJD_001 = RpgProgram(cu, "JD_001")
        val params = programJD_001.params()
        assertEquals(4, params.size)

        assertEquals("U\$FUNZ", params[0].name)
        assertEquals(StringType(10), params[0].type)

        assertEquals("U\$METO", params[1].name)
        assertEquals(StringType(10), params[1].type)

        assertEquals("U\$SVARSK", params[2].name)
        assertEquals(StringType(1050).toArray(200), params[2].type)

        assertEquals("U\$IN35", params[3].name)
        assertEquals(StringType(1), params[3].type)
    }

    @Test
    fun executeJD_001_plist() {
        val cu = assertASTCanBeProduced("JD_001", true)
        cu.resolve()
        val logHandler = ListLogHandler()
        val interpreter = execute(cu, mapOf(
                "U\$FUNZ" to StringValue("Foo"),
                "U\$METO" to StringValue("Bar"),
                "U\$SVARSK" to createArrayValue(StringType(1050), 200) { blankString(1050) },
                "U\$IN35" to blankString(1)),
                logHandlers = listOf(logHandler))
        assertEquals(listOf("IMP0", "FIN0"), logHandler.getExecutedSubroutineNames())
        assertEquals(StringValue("Foo"), interpreter["U\$FUNZ"])
        assertEquals(StringValue("Bar"), interpreter["U\$METO"])
        assertEquals(createArrayValue(StringType(1050), 200) { blankString(1050) }, interpreter["U\$SVARSK"])
        assertEquals(StringValue(PAD_STRING), interpreter["U\$IN35"])
    }

    @Test
    fun executeJD_001_settingVars() {
        val cu = assertASTCanBeProduced("JD_001", true)
        cu.resolve()
        val logHandler = ListLogHandler()
        val interpreter = execute(cu, mapOf(
                "U\$FUNZ" to StringValue("Foo"),
                "U\$METO" to StringValue("Bar"),
                "U\$SVARSK" to createArrayValue(StringType(1050), 200) { blankString(1050) },
                "U\$IN35" to blankString(1)),
                logHandlers = listOf(logHandler))
        assertEquals(listOf("IMP0", "FIN0"), logHandler.getExecutedSubroutineNames())
        // Initialized inside IMP0
        assertEquals(createArrayValue(StringType(1050), 200) { blankString(1050) }, interpreter["\$\$SVAR"])
    }

    @Test
    fun executeJD_001_inzFunz() {
        val cu = assertASTCanBeProduced("JD_001", true)
        cu.resolve()
        val logHandler = ListLogHandler()
        val interpreter = execute(cu, mapOf(
                "U\$FUNZ" to StringValue("INZ"),
                "U\$METO" to StringValue("Bar"),
                "U\$SVARSK" to createArrayValue(StringType(1050), 200) { blankString(1050) },
                "U\$IN35" to StringValue("X")),
                logHandlers = listOf(logHandler))
        assertEquals(1, logHandler.getEvaluatedExpressionsConcise().size)
        assertEquals(listOf("IMP0", "FINZ", "FIN0"), logHandler.getExecutedSubroutineNames())
        // Initialized inside IMP0
        assertEquals(createArrayValue(StringType(1050), 200) { blankString(1050) }, interpreter["\$\$SVAR"])
        // Assigned inside FINZ
        assertEquals(createArrayValue(StringType(1050), 200) { blankString(1050) }, interpreter["U\$SVARSK_INI"])
        assertEquals(StringValue(" "), interpreter["U\$IN35"])
    }

    @Test
    fun executeJD_001_recognizeDataDefinitions() {
        val cu = assertASTCanBeProduced("JD_001", true)
        cu.resolve()

        val unnamedDS = cu.getDataDefinition("@UNNAMED_DS_16")
        assertEquals(false, unnamedDS.type is ArrayType)
        assertEquals(210000, unnamedDS.type.size)

        val SVAR = cu.getDataOrFieldDefinition("\$\$SVAR") as FieldDefinition
        assertEquals(ArrayType(StringType(1050), 200), SVAR.type)
        assertEquals(0, SVAR.startOffset)
        assertEquals(210000, SVAR.endOffset)

        val SVARCD = cu.getDataOrFieldDefinition("\$\$SVARCD")
        assertEquals(ArrayType(StringType(50), 200), SVARCD.type)

        val SVARVA = cu.getDataOrFieldDefinition("\$\$SVARVA")
        assertEquals(ArrayType(StringType(1000), 200), SVARVA.type)
    }

    @Test
    fun executeJD_001_complete_url_not_found() {
        val cu = assertASTCanBeProduced("JD_001", true)
        cu.resolve()

        val interpreter = execute(cu, mapOf("U\$FUNZ" to "INZ".asValue()))
        interpreter.execute(cu, mapOf("U\$FUNZ" to "ESE".asValue()), reinitialization = false)
        interpreter.execute(cu, mapOf("U\$FUNZ" to "CLO".asValue()), reinitialization = false)
        assertEquals(IntValue(0), interpreter["\$X"])
        assertEquals(StringValue("1"), interpreter["U\$IN35"])
    }

    @Test
    fun executeJD_001_complete_url_found() {
        val si = CollectorSystemInterface()
        val callsToJDURL = LinkedList<Map<String, Value>>()
        si.programs["JD_URL"] = object : JvmProgramRaw("JD_URL", listOf(
                ProgramParam("funz", StringType(10)),
                ProgramParam("method", StringType(10)),
                ProgramParam("URL", ArrayType(StringType(1050), 200)))) {
            override fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>): List<Value> {
                callsToJDURL.add(params)
                return emptyList()
            }
        }
        val cu = assertASTCanBeProduced("JD_001", true)
        cu.resolve()
        val interpreter = execute(cu, mapOf(
                "U\$FUNZ" to "INZ".asValue(),
                "U\$SVARSK" to createArrayValue(StringType(1050), 200) { i ->
                    when (i) {
                        0 -> "Url".padEnd(50, PAD_CHAR) + "https://xxx.myurl.com".padEnd(1000, PAD_CHAR)
                        1 -> "x".padEnd(50, PAD_CHAR) + "w".padEnd(1000, PAD_CHAR)
                        else -> "".padEnd(1050, PAD_CHAR)
                    }.asValue()
                }), systemInterface = si)
        interpreter.execute(cu, mapOf("U\$FUNZ" to "ESE".asValue()), reinitialization = false)
        interpreter.execute(cu, mapOf("U\$FUNZ" to "CLO".asValue()), reinitialization = false)
        assertEquals(callsToJDURL.size, 1)
        val a = callsToJDURL[0]["URL"]
        if (a !is ConcreteArrayValue) {
            fail("Expected array, found $a")
        }
        val elementValue = a.getElement(1)
        assertEquals("Url", elementValue.asString().value.substring(0, 3))
        for (i in 4 until 50) {
            assertEquals(0.toByte(), elementValue.asString().value.toCharArray()[i].toByte(), "I expected 0 at $i")
        }
        assertEquals("https://www.myurl.com", elementValue.asString().value.substring(50, 71))
        for (i in 71 until 1050) {
            assertEquals(0.toByte(), elementValue.asString().value.toCharArray()[i].toByte(), "I expected 0 at $i")
        }
        assertEquals(
            StringValue("Url".padEnd(50, '\u0000') +
                "https://www.myurl.com".padEnd(1000, '\u0000')),
                elementValue.asString())
    }

    @Test
    fun executeJD_002_base() {
        val si = CollectorSystemInterface()
        val callsToListFld = LinkedList<Map<String, Value>>()
        val callsToNfyeve = LinkedList<Map<String, Value>>()
        si.programs["LISTEN_FLD"] = object : JvmProgramRaw("LISTEN_FLD", listOf(
                ProgramParam("foldern", StringType(10)),
                ProgramParam("name", StringType(10)),
                ProgramParam("tip", StringType(10)),
                ProgramParam("ope", StringType(10)))) {
            override fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>): List<Value> {
                callsToListFld.add(params)
                return emptyList()
            }
        }
        si.programs["JD_NFYEVE"] = object : JvmProgramRaw("LISTEN_FLD", listOf(
                ProgramParam("funz", StringType(10)),
                ProgramParam("meto", StringType(10)),
                ProgramParam("var", StringType(10)))) {
            override fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>): List<Value> {
                callsToNfyeve.add(params)
                throw InterruptForDebuggingPurposes()
            }
        }
        val cu = assertASTCanBeProduced("JD_002", true)
        cu.resolve()
        val interpreter = execute(cu, mapOf("U\$FUNZ" to "INZ".asValue()), systemInterface = si, logHandlers = SimpleLogHandler.fromFlag(true))
        interpreter.execute(cu, mapOf("U\$FUNZ" to "ESE".asValue()), reinitialization = false)
        interpreter.execute(cu, mapOf("U\$FUNZ" to "CLO".asValue()), reinitialization = false)
        assertEquals(1, callsToListFld.size)
        assertEquals(1, callsToNfyeve.size)
    }

    @Test
    fun executeJD_002_checkingInputs() {
        val si = CollectorSystemInterface()
        val callsToListFld = LinkedList<Map<String, Value>>()
        val callsToNfyeve = LinkedList<Map<String, Value>>()
        si.programs["LISTEN_FLD"] = object : JvmProgramRaw("LISTEN_FLD", listOf(
                ProgramParam("foldern", StringType(100)),
                ProgramParam("name", StringType(10)),
                ProgramParam("tip", StringType(10)),
                ProgramParam("ope", StringType(10)))) {
            override fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>): List<Value> {
                callsToListFld.add(params)
                if (callsToListFld.size >= 5) {
                    throw InterruptForDebuggingPurposes()
                }
                return emptyList()
            }
        }
        si.programs["JD_NFYEVE"] = object : JvmProgramRaw("LISTEN_FLD", listOf(
                ProgramParam("funz", StringType(10)),
                ProgramParam("meto", StringType(10)),
                ProgramParam("var", StringType(10)))) {
            override fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>): List<Value> {
                callsToNfyeve.add(params)
                throw InterruptForDebuggingPurposes()
            }
        }
        val cu = assertASTCanBeProduced("JD_002", true)
        cu.resolve()
        val interpreter = execute(cu, mapOf(
                "U\$FUNZ" to "INZ".asValue(),
                "U\$SVARSK" to createArrayValue(StringType(1050), 200) { i ->
                    when (i) {
                        0 -> "Folder".padEnd(50, PAD_CHAR) + "my/path/to/folder".padEnd(1000, PAD_CHAR)
                        1 -> "Mode".padEnd(50, PAD_CHAR) + "ADD".padEnd(1000, PAD_CHAR)
                        2 -> "Filter".padEnd(50, PAD_CHAR) + "*.png".padEnd(1000, PAD_CHAR)
                        else -> "".padEnd(1050, PAD_CHAR)
                    }.asValue()
                }), systemInterface = si, logHandlers = SimpleLogHandler.fromFlag(true))
        interpreter.execute(cu, mapOf("U\$FUNZ" to "EXE".asValue()), reinitialization = false)
        interpreter.execute(cu, mapOf("U\$FUNZ" to "CLO".asValue()), reinitialization = false)
        assertEquals(5, callsToListFld.size)
        assertEquals(
                mapOf(
                        "foldern" to StringValue.padded("my/path/to/folder", 1000),
                        "name" to StringValue.blank(10),
                        "tip" to StringValue.blank(10),
                        "ope" to StringValue.blank(10)
                ), callsToListFld[0])
        assertEquals(0, callsToNfyeve.size)
    }

    @Test
    fun executeJD_002_listenFldProduceOutput() {
        val si = CollectorSystemInterface()
        val callsToListFld = LinkedList<Map<String, Value>>()
        val callsToNfyeve = LinkedList<Map<String, Value>>()
        si.programs["LISTEN_FLD"] = object : JvmProgramRaw("LISTEN_FLD", listOf(
                ProgramParam("foldern", StringType(100)),
                ProgramParam("name", StringType(10)),
                ProgramParam("tip", StringType(10)),
                ProgramParam("ope", StringType(10)))) {
            override fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>): List<Value> {
                callsToListFld.add(params)
                if (callsToListFld.size >= 5) {
                    throw InterruptForDebuggingPurposes()
                }
                return listOf(params["foldern"]!!,
                        StringValue.padded("myFile.png", 10),
                        StringValue.padded("FILE", 10),
                        StringValue.padded("ADD", 10))
            }
        }
        si.programs["JD_NFYEVE"] = object : JvmProgramRaw("LISTEN_FLD", listOf(
                ProgramParam("funz", StringType(10)),
                ProgramParam("meto", StringType(10)),
                ProgramParam("var", StringType(10)))) {
            override fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>): List<Value> {
                callsToNfyeve.add(params)
                throw InterruptForDebuggingPurposes()
            }
        }
        val cu = assertASTCanBeProduced("JD_002", true)
        cu.resolve()
        val interpreter = execute(cu, mapOf(
                "U\$FUNZ" to "INZ".asValue(),
                "U\$SVARSK" to createArrayValue(StringType(1050), 200) { i ->
                    when (i) {
                        0 -> "Folder".padEnd(50, PAD_CHAR) + "my/path/to/folder".padEnd(1000, PAD_CHAR)
                        1 -> "Mode".padEnd(50, PAD_CHAR) + "ADD".padEnd(1000, PAD_CHAR)
                        2 -> "Filter".padEnd(50, PAD_CHAR) + "*.png".padEnd(1000, PAD_CHAR)
                        else -> "".padEnd(1050, PAD_CHAR)
                    }.asValue()
                }), systemInterface = si, logHandlers = SimpleLogHandler.fromFlag(true))
        interpreter.execute(cu, mapOf("U\$FUNZ" to "EXE".asValue()), reinitialization = false)
        interpreter.execute(cu, mapOf("U\$FUNZ" to "CLO".asValue()), reinitialization = false)
        assertEquals(1, callsToListFld.size)
        assertEquals(
                mapOf(
                        "foldern" to StringValue.padded("my/path/to/folder", 1000),
                        "name" to StringValue.blank(10),
                        "tip" to StringValue.blank(10),
                        "ope" to StringValue.blank(10)
                ), callsToListFld[0])
        assertEquals(1, callsToNfyeve.size)
        val v = callsToNfyeve[0]["var"] as ArrayValue
        assertEquals(StringValue("Object name".padEnd(50) +
                "myFile.png".padEnd(1000)),
                v.getElement(1))
        assertEquals(StringValue("Object type".padEnd(50) +
                "FILE".padEnd(1000)),
                v.getElement(2))
        assertEquals(StringValue("Operation type".padEnd(50) +
                "ADD".padEnd(1000)),
                v.getElement(3))
    }

    @Test
    fun executeJD_003_base() {
        val si = CollectorSystemInterface()
        val callsToRcvsck = LinkedList<Map<String, Value>>()
        si.programs["JD_RCVSCK"] = object : JvmProgramRaw("LISTEN_FLD", listOf(
                ProgramParam("addr", StringType(10)),
                ProgramParam("buffer", StringType(10)),
                ProgramParam("bufferLen", StringType(10)))) {
            override fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>): List<Value> {
                callsToRcvsck.add(params)
                if (callsToRcvsck.size >= 2) {
                    throw InterruptForDebuggingPurposes()
                }
                return emptyList()
            }
        }
        val cu = assertASTCanBeProduced("JD_003", true)
        cu.resolve()
        val interpreter = execute(cu, mapOf("U\$FUNZ" to "INZ".asValue()),
                systemInterface = si, logHandlers = SimpleLogHandler.fromFlag(true))
        interpreter.execute(cu, mapOf("U\$FUNZ" to "EXE".asValue()), reinitialization = false)
        interpreter.execute(cu, mapOf("U\$FUNZ" to "CLO".asValue()), reinitialization = false)
    }

    @Test
    fun executeJD_003() {
        val si = CollectorSystemInterface()
        val callsToRcvsck = LinkedList<Map<String, Value>>()
        val callsToNfyeve = LinkedList<Map<String, Value>>()
        si.programs["JD_RCVSCK"] = object : JvmProgramRaw("LISTEN_FLD", listOf(
                ProgramParam("addr", StringType(10)),
                ProgramParam("buffer", StringType(10)),
                ProgramParam("bufferLen", NumberType(2, 0)))) {
            override fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>): List<Value> {
                callsToRcvsck.add(params)
                if (callsToRcvsck.size >= 5) {
                    throw InterruptForDebuggingPurposes()
                }
                return listOf(params["addr"]!!, StringValue("<myxml></myxml>"), IntValue("<myxml></myxml>".length.toLong()))
            }
        }
        si.functions["P_RxELE"] = object : JvmFunction("P_RxELE", listOf(
                FunctionParam("tag", StringType(50)),
                FunctionParam("pos", StringType(50)),
                FunctionParam("index", NumberType(2, 0)),
                FunctionParam("xml", StringType(5000)))) {
            override fun execute(systemInterface: SystemInterface, params: List<Value>, symbolTable: SymbolTable): Value {
                assertEquals("Auto", params[0].asString().valueWithoutPadding)
                assertEquals("POS", params[1].asString().valueWithoutPadding)
                assertEquals(1, params[2].asInt().value)
                assertEquals("<myxml></myxml>", params[3].asString().valueWithoutPadding)
                return StringValue("<riga Targa=\"ZZ000AA\"/>")
            }
        }
        si.functions["P_RxVAL"] = object : JvmFunction("P_RxELE", listOf(
                FunctionParam("Element", StringType(500)),
                FunctionParam("AttributeName", StringType(50)))) {
            override fun execute(systemInterface: SystemInterface, params: List<Value>, symbolTable: SymbolTable): Value {
                assertEquals("<riga Targa=\"ZZ000AA\"/>", params[0].asString().valueWithoutPadding)
                assertEquals("Targa", params[1].asString().valueWithoutPadding)
                return StringValue("ZZ000AA")
            }
        }
        si.programs["JD_NFYEVE"] = object : JvmProgramRaw("LISTEN_FLD", listOf(
                ProgramParam("funz", StringType(10)),
                ProgramParam("meto", StringType(10)),
                ProgramParam("var", StringType(10)))) {
            override fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>): List<Value> {
                callsToNfyeve.add(params)
                throw InterruptForDebuggingPurposes()
            }
        }
        val cu = assertASTCanBeProduced("JD_003", true)
        cu.resolve()
        val interpreter = execute(cu, mapOf("U\$FUNZ" to "INZ".asValue(),
                "U\$SVARSK" to createArrayValue(StringType(1050), 200) { i ->
                    when (i) {
                        0 -> "SOCKET".padEnd(50, PAD_CHAR) + "addressToListenTo".padEnd(1000, PAD_CHAR)
                        else -> "".padEnd(1050, PAD_CHAR)
                    }.asValue()
                }),
                systemInterface = si, logHandlers = SimpleLogHandler.fromFlag(true))
        interpreter.execute(cu, mapOf("U\$FUNZ" to "EXE".asValue()), reinitialization = false)
        interpreter.execute(cu, mapOf("U\$FUNZ" to "CLO".asValue()), reinitialization = false)
        assertEquals(1, callsToRcvsck.size)
        assertEquals("addressToListen", callsToRcvsck[0]["addr"]!!.asString().value)
        assertEquals(1, callsToNfyeve.size)
        assertEquals("Targa".padEnd(50) + "ZZ000AA".padEnd(1000), callsToNfyeve[0]["var"]!!.asArray().getElement(2).asString().value)
    }

    @Test
    fun executeJD_003_noErrors() {
        val callsToNfyeve = LinkedList<Map<String, Value>>()
        val returnStatus = "U\$IN35"
        val parms = mapOf(
                "U\$FUNZ" to StringValue("INZ"),
                "U\$METO" to StringValue(""),
                "U\$SVARSK" to StringValue(""),
                returnStatus to StringValue(" ")
        )
        val si = CollectorSystemInterface()
        si.programs["JD_RCVSCK"] = object : JvmProgramRaw("JD_RCVSCK", listOf(
                ProgramParam("addr", StringType(10)),
                ProgramParam("buffer", StringType(10)),
                ProgramParam("bufferLen", NumberType(2, 0)))) {
            override fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>): List<Value> {
                val result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Auto Targa=\"AB123XX\" />"
                return listOf(StringValue(""), StringValue(result), IntValue(result.length.toLong()))
            }
        }
        si.functions["P_RxELE"] = object : JvmFunction("P_RxELE", listOf(
                FunctionParam("tag", StringType(50)),
                FunctionParam("pos", StringType(50)),
                FunctionParam("index", NumberType(2, 0)),
                FunctionParam("xml", StringType(5000)))) {
            override fun execute(systemInterface: SystemInterface, params: List<Value>, symbolTable: SymbolTable): Value {
                return StringValue("<Auto Targa=\"AB123XX\"/>")
            }
        }
        si.functions["P_RxVAL"] = object : JvmFunction("P_RxELE", listOf(
                FunctionParam("Element", StringType(500)),
                FunctionParam("AttributeName", StringType(50)))) {
            override fun execute(systemInterface: SystemInterface, params: List<Value>, symbolTable: SymbolTable): Value {
                return StringValue("AB123XX")
            }
        }
        si.programs["JD_NFYEVE"] = object : JvmProgramRaw("LISTEN_FLD", listOf(
                ProgramParam("funz", StringType(10)),
                ProgramParam("meto", StringType(10)),
                ProgramParam("var", StringType(10)))) {
            override fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>): List<Value> {
                callsToNfyeve.add(params)
                throw InterruptForDebuggingPurposes()
            }
        }
        execute("JD_003", parms, si)
        assertEquals(" ", parms[returnStatus]!!.value)
    }

    @Test
    fun executeJD_003_withErrors() {
        val returnStatus = "U\$IN35"
        val parms = mapOf(
                "U\$FUNZ" to StringValue("INZ"),
                "U\$METO" to StringValue(""),
                "U\$SVARSK" to StringValue(""),
                returnStatus to StringValue(" ")
        )
        val si = CollectorSystemInterface()
        si.programs["JD_RCVSCK"] = object : JvmProgramRaw("JD_RCVSCK", listOf(
                ProgramParam("addr", StringType(10)),
                ProgramParam("buffer", StringType(10)),
                ProgramParam("bufferLen", NumberType(2, 0)))) {
            override fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>): List<Value> {
                throw RuntimeException("Something went wrong")
            }
        }
        val interpreter = execute("JD_003", parms, si)
        assertEquals("1", interpreter[returnStatus].asString().value)
    }

    @Test
    fun executeJD_003_V2() {
        val callsToNfyeve = LinkedList<Map<String, Value>>()
        val targa = "AB123XX"
        val returnStatus = "U\$IN35"
        val parms = mapOf(
                "U\$FUNZ" to StringValue("INZ"),
                "U\$METO" to StringValue(""),
                "U\$SVARSK" to ConcreteArrayValue(
                        (mutableListOf(StringValue("PORT".padEnd(50) + "192.168.10.1".padEnd(1000))) +
                                MutableList(199) { StringValue("".padEnd(1050)) }).toMutableList(),
                        StringType(1050)),
                returnStatus to StringValue(" ")
        )
        val si = CollectorSystemInterface()
        si.programs["JD_RCVSCK"] = object : JvmProgramRaw("JD_RCVSCK", listOf(
                ProgramParam("addr", StringType(10)),
                ProgramParam("buffer", StringType(10)),
                ProgramParam("bufferLen", NumberType(2, 0)),
                ProgramParam("ierror", BooleanType))) {
            override fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>): List<Value> {
                val result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Auto Targa=\"${targa}\" />"
                return listOf(StringValue(""), StringValue(result), IntValue(result.length.toLong()))
            }
        }
        si.programs["JD_NFYEVE"] = object : JvmProgramRaw("LISTEN_FLD", listOf(
                ProgramParam("funz", StringType(10)),
                ProgramParam("meto", StringType(10)),
                ProgramParam("var", StringType(10)))) {
            override fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>): List<Value> {
                callsToNfyeve.add(params)
                throw InterruptForDebuggingPurposes()
            }
        }
        val logHandlers = emptyList<InterpreterLogHandler>() // listOf(EvalLogHandler(), AssignmentsLogHandler())

        execute("JD_003_V2", parms, si, logHandlers)
        assertEquals(1, callsToNfyeve.size)
        assertTrue((callsToNfyeve[0]["var"] as ConcreteArrayValue).getElement(2).asString().value.contains(targa))
        assertEquals(" ", parms[returnStatus]!!.asString().value)
    }

    // TODO understand why this test does not pass
    @Test @Ignore
    fun executeJD_003_full() {
        val callsToRcvsck = LinkedList<Map<String, Value>>()
        val callsToNfyeve = LinkedList<Map<String, Value>>()
        val targa = "AB123XX"
        val returnStatus = "U\$IN35"
        val parms = mapOf(
                "U\$FUNZ" to StringValue("INZ".padEnd(10)),
                "U\$METO" to StringValue("".padEnd(10)),
                "U\$SVARSK" to StringValue("SOCKET".padEnd(50) + "127.0.0.1"),
                returnStatus to StringValue(" ")
        )
        val si = CollectorSystemInterface(consoleLoggingConfiguration(STATEMENT_LOGGER, EXPRESSION_LOGGER, DATA_LOGGER))
        si.printOutput = true
        si.programs["JD_RCVSCK"] = object : JvmProgramRaw("JD_RCVSCK", listOf(
                ProgramParam("addr", StringType(10)),
                ProgramParam("buffer", StringType(10)),
                ProgramParam("bufferLen", NumberType(2, 0)))) {
            override fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>): List<Value> {
                callsToRcvsck.add(params)
                val result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Auto Targa=\"${targa}\" />"
                return listOf(StringValue(""), StringValue(result), IntValue(result.length.toLong()))
            }
        }
        si.programs["JD_NFYEVE"] = object : JvmProgramRaw("LISTEN_FLD", listOf(
                ProgramParam("funz", StringType(10)),
                ProgramParam("meto", StringType(10)),
                ProgramParam("var", StringType(10)))) {
            override fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>): List<Value> {
                callsToNfyeve.add(params)
                throw InterruptForDebuggingPurposes()
            }
        }
        val outputLog = StringOutputStream()
        execute("JD_003_full", parms, si, listOf(LinesLogHandler(PrintStream(outputLog))))
        assertTrue(outputLog.written, message = "No statement executed!")
        assertEquals(1, callsToRcvsck.size)
        assertEquals(1, callsToNfyeve.size)
        assertTrue((callsToNfyeve[0]["var"] as ConcreteArrayValue).getElement(1).asString().value.contains(targa))
        assertEquals(" ", parms[returnStatus]!!.value)
    }
}
