package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.CollectorSystemInterface
import com.smeup.rpgparser.assertASTCanBeProduced
import com.smeup.rpgparser.execute
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.resolve
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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
        assertEquals(200, (svarsk as ArrayValue).arrayLength())
        val svarskElement = (svarsk as ArrayValue).getElement(1)
        assertEquals(blankString(1050), svarskElement)

        val svarcd = interpreter["\$\$SVARCD"]
        assertTrue(svarcd is ArrayValue)
        assertEquals(200, (svarcd as ArrayValue).arrayLength())
        val svarcdElement = (svarcd as ArrayValue).getElement(1)
        assertEquals(blankString(50), svarcdElement)

        val svarva = interpreter["\$\$SVARVA"]
        assertTrue(svarva is ArrayValue)
        assertEquals(200, (svarva as ArrayValue).arrayLength())
        val svarvaElement = (svarva as ArrayValue).getElement(1)
        assertEquals(blankString(1000), svarvaElement)
    }

    @Test
    fun executeJD_000_base() {
        val cu = assertASTCanBeProduced("JD_000_base", true)
        cu.resolve()
        val interpreter = execute(cu, mapOf(), traceMode = true)
    }

//    TODO: to solve this we should handle params being data declarations, sometimes
//    @Test
//    fun executeJD_000() {
//        val si = CollectorSystemInterface()
//        val callsToJDURL = LinkedList<Map<String, Value>>()
//        si.programs["JD_URL"] = object : JvmProgram("JD_URL", listOf(
//                ProgramParam("funz", StringType(10)),
//                ProgramParam("method", StringType(10)),
//                ProgramParam("URL", StringType(1000)))) {
//            override fun execute(systemInterface: SystemInterface, params: Map<String, Value>) : List<Value> {
//                callsToJDURL.add(params)
//                return emptyList()
//            }
//        }
//        val cu = assertASTCanBeProduced("JD_000", true)
//        cu.resolve()
//        val interpreter = execute(cu, mapOf(), systemInterface = si)
//        assertEquals(callsToJDURL.size, 1)
//        assertEquals(callsToJDURL[0]["\$\$URL"], StringValue("https://www.myurl.com".padEnd(1000, '\u0000')))
//    }

    @Test
    fun executeJD_001_plist() {
        val cu = assertASTCanBeProduced("JD_001", true)
        cu.resolve()
        val interpreter = execute(cu, mapOf(
                "U\$FUNZ" to StringValue("Foo"),
                "U\$METO" to StringValue("Bar"),
                "U\$SVARSK" to createArrayValue(StringType(1050), 200) { blankString(1050) },
                "U\$IN35" to blankString(1)))
        assertEquals(listOf("IMP0", "FIN0"), interpreter.getExecutedSubroutineNames())
        assertEquals(StringValue("Foo"), interpreter["U\$FUNZ"])
        assertEquals(StringValue("Bar"), interpreter["U\$METO"])
        assertEquals(createArrayValue(StringType(1050), 200) { blankString(1050) }, interpreter["U\$SVARSK"])
        assertEquals(StringValue("\u0000"), interpreter["U\$IN35"])
    }

    @Test
    fun executeJD_001_settingVars() {
        val cu = assertASTCanBeProduced("JD_001", true)
        cu.resolve()
        val interpreter = execute(cu, mapOf(
                "U\$FUNZ" to StringValue("Foo"),
                "U\$METO" to StringValue("Bar"),
                "U\$SVARSK" to createArrayValue(StringType(1050), 200) { blankString(1050) },
                "U\$IN35" to blankString(1)))
        assertEquals(listOf("IMP0", "FIN0"), interpreter.getExecutedSubroutineNames())
        // Initialized inside IMP0
        assertEquals(createArrayValue(StringType(1050), 200) { blankString(1050) }, interpreter["\$\$SVAR"])
    }

    @Test
    fun executeJD_001_inzFunz() {
        val cu = assertASTCanBeProduced("JD_001", true)
        cu.resolve()
        val interpreter = execute(cu, mapOf(
                "U\$FUNZ" to StringValue("INZ"),
                "U\$METO" to StringValue("Bar"),
                "U\$SVARSK" to createArrayValue(StringType(1050),200) { blankString(1050) },
                "U\$IN35" to StringValue("X")))
        assertEquals(6, interpreter.getEvaluatedExpressionsConcise().size)
        assertEquals(listOf("IMP0", "FINZ", "FIN0"), interpreter.getExecutedSubroutineNames())
        // Initialized inside IMP0
        assertEquals(createArrayValue(StringType(1050), 200) { blankString(1050) }, interpreter["\$\$SVAR"])
        // Assigned inside FINZ
        assertEquals(createArrayValue(StringType(1050), 200) { blankString(1050) }, interpreter["U\$SVARSK_INI"])
        assertEquals(StringValue(" "), interpreter["U\$IN35"])
    }

    @Test
    fun executeJD_001_complete_url_not_found() {
        val cu = assertASTCanBeProduced("JD_001", true)
        cu.resolve()
        val interpreter = execute(cu, mapOf("U\$FUNZ" to "INZ".asValue()))
        interpreter.execute(cu, mapOf("U\$FUNZ" to "EXE".asValue()), reinitialization = false)
        interpreter.execute(cu, mapOf("U\$FUNZ" to "CLO".asValue()), reinitialization = false)
        assertEquals(IntValue(0), interpreter["\$X"])
        assertEquals(StringValue("1"), interpreter["U\$IN35"])
    }

    @Test
    fun executeJD_001_complete_url_found() {
        val si = CollectorSystemInterface()
        val callsToJDURL = LinkedList<Map<String, Value>>()
        si.programs["JD_URL"] = object : JvmProgram("JD_URL", listOf(
                ProgramParam("funz", StringType(10)),
                ProgramParam("method", StringType(10)),
                ProgramParam("URL", StringType(1000)))) {
            override fun execute(systemInterface: SystemInterface, params: Map<String, Value>) : List<Value> {
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
                        0 -> "Url".padEnd(50, '\u0000') + "https://xxx.myurl.com".padEnd(1000, '\u0000')
                        1 -> "x".padEnd(50, '\u0000') + "w".padEnd(1000, '\u0000')
                        else -> "".padEnd(1050, '\u0000')
                    }.asValue()
                }), systemInterface = si)
        interpreter.traceMode = true
        interpreter.execute(cu, mapOf("U\$FUNZ" to "EXE".asValue()), reinitialization = false)
        interpreter.execute(cu, mapOf("U\$FUNZ" to "CLO".asValue()), reinitialization = false)
        assertEquals(callsToJDURL.size, 1)
        assertEquals(callsToJDURL[0]["URL"], StringValue("https://www.myurl.com".padEnd(1000, '\u0000')))
    }

    @Test
    fun executeJD_002_base() {
        val si = CollectorSystemInterface()
        val callsToListFld = LinkedList<Map<String, Value>>()
        val callsToNfyeve = LinkedList<Map<String, Value>>()
        si.programs["LISTEN_FLD"] = object : JvmProgram("LISTEN_FLD", listOf(
                ProgramParam("foldern", StringType(10)),
                ProgramParam("name", StringType(10)),
                ProgramParam("tip", StringType(10)),
                ProgramParam("ope", StringType(10)))) {
            override fun execute(systemInterface: SystemInterface, params: Map<String, Value>) : List<Value> {
                callsToListFld.add(params)
                return emptyList()
            }
        }
        si.programs["JD_NFYEVE"] = object : JvmProgram("LISTEN_FLD", listOf(
                ProgramParam("funz", StringType(10)),
                ProgramParam("meto", StringType(10)),
                ProgramParam("var", StringType(10)))) {
            override fun execute(systemInterface: SystemInterface, params: Map<String, Value>) : List<Value> {
                callsToNfyeve.add(params)
                throw InterruptForDebuggingPurposes()
            }
        }
        val cu = assertASTCanBeProduced("JD_002", true)
        cu.resolve()
        val interpreter = execute(cu, mapOf("U\$FUNZ" to "INZ".asValue()), systemInterface = si, traceMode = true)
        interpreter.execute(cu, mapOf("U\$FUNZ" to "EXE".asValue()), reinitialization = false)
        interpreter.execute(cu, mapOf("U\$FUNZ" to "CLO".asValue()), reinitialization = false)
        assertEquals(1, callsToListFld.size)
        assertEquals(1, callsToNfyeve.size)
    }

    @Test
    fun executeJD_002_checkingInputs() {
        val si = CollectorSystemInterface()
        val callsToListFld = LinkedList<Map<String, Value>>()
        val callsToNfyeve = LinkedList<Map<String, Value>>()
        si.programs["LISTEN_FLD"] = object : JvmProgram("LISTEN_FLD", listOf(
                ProgramParam("foldern", StringType(100)),
                ProgramParam("name", StringType(10)),
                ProgramParam("tip", StringType(10)),
                ProgramParam("ope", StringType(10)))) {
            override fun execute(systemInterface: SystemInterface, params: Map<String, Value>) : List<Value> {
                callsToListFld.add(params)
                return emptyList()
            }
        }
        si.programs["JD_NFYEVE"] = object : JvmProgram("LISTEN_FLD", listOf(
                ProgramParam("funz", StringType(10)),
                ProgramParam("meto", StringType(10)),
                ProgramParam("var", StringType(10)))) {
            override fun execute(systemInterface: SystemInterface, params: Map<String, Value>) : List<Value> {
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
                        0 -> "Folder".padEnd(50, '\u0000') + "my/path/to/folder".padEnd(1000, '\u0000')
                        1 -> "Mode".padEnd(50, '\u0000') + "ADD".padEnd(1000, '\u0000')
                        2 -> "Filter".padEnd(50, '\u0000') + "*.png".padEnd(1000, '\u0000')
                        else -> "".padEnd(1050, '\u0000')
                    }.asValue()
                }), systemInterface = si, traceMode = true, cycleLimit = 5)
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
        si.programs["LISTEN_FLD"] = object : JvmProgram("LISTEN_FLD", listOf(
                ProgramParam("foldern", StringType(100)),
                ProgramParam("name", StringType(10)),
                ProgramParam("tip", StringType(10)),
                ProgramParam("ope", StringType(10)))) {
            override fun execute(systemInterface: SystemInterface, params: Map<String, Value>) : List<Value> {
                callsToListFld.add(params)
                return listOf(params["foldern"]!!,
                        StringValue.padded("myFile.png", 10),
                        StringValue.padded("FILE", 10),
                        StringValue.padded("ADD", 10))
            }
        }
        si.programs["JD_NFYEVE"] = object : JvmProgram("LISTEN_FLD", listOf(
                ProgramParam("funz", StringType(10)),
                ProgramParam("meto", StringType(10)),
                ProgramParam("var", StringType(10)))) {
            override fun execute(systemInterface: SystemInterface, params: Map<String, Value>) : List<Value> {
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
                        0 -> "Folder".padEnd(50, '\u0000') + "my/path/to/folder".padEnd(1000, '\u0000')
                        1 -> "Mode".padEnd(50, '\u0000') + "ADD".padEnd(1000, '\u0000')
                        2 -> "Filter".padEnd(50, '\u0000') + "*.png".padEnd(1000, '\u0000')
                        else -> "".padEnd(1050, '\u0000')
                    }.asValue()
                }), systemInterface = si, traceMode = true, cycleLimit = 5)
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
        assertEquals(StringValue("Object name".padEnd(50, '\u0000')
                + "myFile.png".padEnd(1000, '\u0000')),
                v.getElement(1))
        assertEquals(StringValue("Object type".padEnd(50, '\u0000')
                + "FILE".padEnd(1000, '\u0000')),
                v.getElement(2))
        assertEquals(StringValue("Operation type".padEnd(50, '\u0000')
                + "ADD".padEnd(1000, '\u0000')),
                v.getElement(3))
    }

    @Test
    fun executeJD_003_base() {
        val si = CollectorSystemInterface()
        val callsToRcvsck = LinkedList<Map<String, Value>>()
//        val callsToNfyeve = LinkedList<Map<String, Value>>()
        si.programs["JD_RCVSCK"] = object : JvmProgram("LISTEN_FLD", listOf(
                ProgramParam("addr", StringType(10)),
                ProgramParam("buffer", StringType(10)),
                ProgramParam("bufferLen", StringType(10)))) {
            override fun execute(systemInterface: SystemInterface, params: Map<String, Value>) : List<Value> {
                callsToRcvsck.add(params)
                return emptyList()
            }
        }
//        si.programs["JD_NFYEVE"] = object : JvmProgram("LISTEN_FLD", listOf(
//                ProgramParam("funz", StringType(10)),
//                ProgramParam("meto", StringType(10)),
//                ProgramParam("var", StringType(10)))) {
//            override fun execute(systemInterface: SystemInterface, params: Map<String, Value>) : List<Value> {
//                callsToNfyeve.add(params)
//                throw InterruptForDebuggingPurposes()
//            }
//        }
        val cu = assertASTCanBeProduced("JD_003", true)
        cu.resolve()
        val interpreter = execute(cu, mapOf("U\$FUNZ" to "INZ".asValue()),
                systemInterface = si, traceMode = true, cycleLimit = 5)
        interpreter.execute(cu, mapOf("U\$FUNZ" to "EXE".asValue()), reinitialization = false)
        interpreter.execute(cu, mapOf("U\$FUNZ" to "CLO".asValue()), reinitialization = false)
//        assertEquals(1, callsToListFld.size)
//        assertEquals(1, callsToNfyeve.size)
    }

}