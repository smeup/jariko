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

package com.smeup.rpgparser.utils

import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.Options
import com.smeup.rpgparser.execution.getProgram
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.parsing.ast.MainBody
import com.smeup.rpgparser.parsing.facade.CopyId
import com.smeup.rpgparser.parsing.facade.preprocess
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import org.apache.commons.io.FileUtils
import org.junit.Assert
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import kotlin.test.*

class MiscTest {
    @Test
    fun chunkLineTest() {
        assertEquals(
            listOf("a1", "b1", "c1", "a2", "b2", "c2"),
            listOf("a1b1c1d1", "a2b2c2xxxxxxx").chunkAs(3, 2),
        )

        assertEquals(
            listOf("a1", "b1", "a2", "b2"),
            listOf("a1b1", "a2b2").chunkAs(3, 2),
        )
    }

    @Test
    fun resizeToTest() {
        assertEquals(
            listOf("a", "b", "c"),
            listOf("a", "b", "c", "z", "z").resizeTo(3, "x"),
        )

        assertEquals(
            listOf("a", "b", "x"),
            listOf("a", "b").resizeTo(3, "x"),
        )
    }

    @Test
    fun divideAtIndexTest() {
        assertEquals(Pair("", "abcde"), "abcde".divideAtIndex(0))
        assertEquals(Pair("", "abcde"), "abcde".divideAtIndex(-1))
        assertEquals(Pair("abcde", ""), "abcde".divideAtIndex(20))
        assertEquals(Pair("abcde", ""), "abcde".divideAtIndex(4))
        assertEquals(Pair("a", "bcde"), "abcde".divideAtIndex(1))
        assertEquals(Pair("ab", "cde"), "abcde".divideAtIndex(2))
    }

    @Test
    fun repeatWithMaxSizeTest() {
        assertEquals("abcab", "abc".repeatWithMaxSize(5))
        assertEquals("aaaaa", "a".repeatWithMaxSize(5))
        assertEquals("ab", "abc".repeatWithMaxSize(2))
    }

    @Test
    fun runIfNotEmptyTest() {
        val nullSting: String? = null
        nullSting.runIfNotEmpty {
            fail("Should not be executed")
        }

        " ".runIfNotEmpty {
            fail("Should not be executed")
        }

        var result = "Lambda not invoked"
        "some string".runIfNotEmpty {
            result = "OK"
        }
        assertEquals("OK", result)
    }

    @Test
    fun compilerTest() {
        val dummyDir = "/impossibiledir12334567"
        assertEquals(
            true,
            compile(src = File(dummyDir), File(dummyDir).parentFile).isEmpty(),
        )

        val program =
            "     ** String of 50 chars:\n" +
                "     D Msg             S             50\n" +
                "     ** Implicit declaration of a number with 3 digits, 2 of them are decimals\n" +
                "     C                   clear                   X                 3 2\n" +
                "     C                   eval      x = %ABS(-1)\n" +
                "     C                   eval      Msg = 'X is ' + %CHAR(X)\n" +
                "     C                   dsply                   Msg\n" +
                "     C                   SETON                                          LR"
        val srcFile = File.createTempFile("MYPGM", ".rpgle")
        srcFile.writeText(program)
        println("Compiling $srcFile")
        val compilationResults = compile(src = srcFile, srcFile.parentFile)
        println("Compilation results: $compilationResults")
        assertNotNull(compilationResults.first().compiledFile)
        assert(compilationResults.first().error == null)
        srcFile.delete()
        compilationResults.forEach {
            it.compiledFile?.delete()
        }
    }

    @Test
    fun compileTestBadFile() {
        val program = "BADRPG\n d a a a a a aa a a a a a a"
        val srcFile = File.createTempFile("MYPGM", ".rpgle")
        srcFile.deleteOnExit()
        srcFile.writeText(program)
        println("Compiling $srcFile")

        val compilationResults =
            compile(
                src = srcFile,
                compiledProgramsDir = srcFile.parentFile,
                allowCompilationError = { _, _ -> true },
            )
        assert(compilationResults.first().compiledFile == null)
        assertNotNull(compilationResults.first().parsingError)
    }

    // If the program contains a not defined variable, the compilation has to fail
    // In this case I'm trying to display MSG1 value but MSG1 is not defined
    @Test
    fun compileTestUseNotDefinedVariable() {
        val program =
            "     D MSG             S             50\n" +
                "     C                   DSPLY                   MSG1\n" +
                "     C                   SETON                                          LR"
        val srcFile = File.createTempFile("MYPGM", ".rpgle")
        srcFile.deleteOnExit()
        srcFile.writeText(program)
        println("Compiling $srcFile")
        val compilationResults = compile(src = srcFile, srcFile.parentFile)
        assert(compilationResults.first().compiledFile == null)
        assertNotNull(compilationResults.first().parsingError)
    }

    @Test
    fun compileFromInToOut() {
        val tmpDir = System.getProperty("java.io.tmpdir")
        val programName = "HELLO"
        val srcFile = File("src/test/resources/$programName.rpgle")
        val outBinFile = File("$tmpDir/$programName-out.bin").apply { deleteOnExit() }
        val outJsonFile = File("$tmpDir/$programName-out.json").apply { deleteOnExit() }

        compile(srcFile.inputStream(), outBinFile.outputStream(), Format.BIN, false)
        compile(src = srcFile, compiledProgramsDir = File(tmpDir), format = Format.BIN, muteSupport = false)
        val expectedBin = File(tmpDir, "$programName.bin").apply { deleteOnExit() }
        assertTrue(FileUtils.contentEquals(expectedBin, outBinFile))

        compile(srcFile.inputStream(), outJsonFile.outputStream(), Format.JSON, false)
        compile(src = srcFile, compiledProgramsDir = File(tmpDir), format = Format.JSON, muteSupport = false)
        val expectedJson = File(tmpDir, "$programName.json").apply { deleteOnExit() }
        assertTrue(FileUtils.contentEquals(expectedJson, outJsonFile))
    }

    @Test
    fun inputStreamPreprocess() {
        val src = """
     H/COPY QILEGEN,£INIZH     
      *---------------------------------------------------------------
     I/COPY QILEGEN,£TABB£1DS     
     I/COPY QILEGEN,£PDS     
      AFTER QILEGEN,£PDS   
      /COPY QILEGEN,£JAX_PD1            
        """
        val expected = """
********** PREPROCESSOR COPYSTART QILEGEN,£INIZH
      HELLO I AM COPY QILEGEN,£INIZH
********** PREPROCESSOR COPYEND QILEGEN,£INIZH     
      *---------------------------------------------------------------
********** PREPROCESSOR COPYSTART QILEGEN,£TABB£1DS
      HELLO I AM COPY QILEGEN,£TABB£1DS
********** PREPROCESSOR COPYEND QILEGEN,£TABB£1DS     
********** PREPROCESSOR COPYSTART QILEGEN,£PDS
      HELLO I AM COPY QILEGEN,£PDS
********** PREPROCESSOR COPYEND QILEGEN,£PDS     
      AFTER QILEGEN,£PDS   
********** PREPROCESSOR COPYSTART QILEGEN,£JAX_PD1
********** PREPROCESSOR COPYSTART QILEGEN,£JAX_PD2
      HELLO I AM COPY QILEGEN,£JAX_PD2
********** PREPROCESSOR COPYEND QILEGEN,£JAX_PD2
      AFTER QILEGEN,£PDS AND ADDING ${'$'}1${'$'}2${'$'}3
********** PREPROCESSOR COPYEND QILEGEN,£JAX_PD1   
        """
        val included =
            src.byteInputStream().preprocess(
                // recursive test
                // simulate copy £JAX_PD1 include £JAX_PD2
                findCopy = { copyId: CopyId ->
                    if (copyId.member == "£JAX_PD1") {
                        (
                            "      /COPY QILEGEN,£JAX_PD2\n" +
                                "      AFTER QILEGEN,£PDS AND ADDING $1$2$3"
                        )
                    } else {
                        "      HELLO I AM COPY ${copyId.file},${copyId.member}"
                    }
                },
            )
        println(included)
        assertEquals(expected.trim(), included.trim())
    }

    @Test
    fun dumpSrcOnAstResolvingError() {
        val pgm = """
     D MSG             S             20   
     C                   EVAL      MSG = 'Hi guy how are you?'
     C     ERR           DSPLY
        """
        val configuration = Configuration()
        configuration.options = Options()
        configuration.options.dumpSourceOnExecutionError = true
        kotlin
            .runCatching {
                getProgram(nameOrSource = pgm).singleCall(emptyList(), configuration)
            }.onFailure {
                // Exception message must contain src code
                Assert.assertTrue(it.message!!.indexOf("D MSG             S             20") > 0)
            }.onSuccess {
                Assert.fail()
            }
        // restore default
        configuration.options = Options()
        kotlin
            .runCatching {
                getProgram(nameOrSource = pgm).singleCall(emptyList(), configuration)
            }.onFailure {
                // Exception message does not must contain src code
                Assert.assertFalse(it.message!!.indexOf("D MSG             S             20") > 0)
            }.onSuccess {
                Assert.fail()
            }
    }

    @Test
    fun dumpSrcOnParsingError() {
        val pgm = """
       MSG             S             20   
     C                   EVAL      MSG = 'Hi guy how are you?'
     C     MSG           DSPLY
        """
        val configuration = Configuration()
        configuration.options = Options()
        configuration.options.dumpSourceOnExecutionError = true
        kotlin
            .runCatching {
                getProgram(nameOrSource = pgm).singleCall(emptyList(), configuration)
            }.onFailure {
                // Exception message must contain src code
                Assert.assertTrue(it.message!!.indexOf("MSG             S             20") > 0)
            }.onSuccess {
                Assert.fail()
            }
        // restore default options
        configuration.options = Options()
        kotlin
            .runCatching {
                getProgram(nameOrSource = pgm).singleCall(emptyList(), configuration)
            }.onFailure {
                // Exception message does not must contain src code
                Assert.assertFalse(it.message!!.indexOf("MSG             S             20") > 0)
            }.onSuccess {
                Assert.fail()
            }
    }

    @Test
    fun dumpSrcOnRuntimeError() {
        val pgm = """
     D VAR             S              5  0   
     C                   EVAL      VAR = 0/0
        """
        val configuration = Configuration()
        val defaultErr = System.err
        var err: ByteArrayOutputStream
        var ps: PrintStream
        try {
            err = ByteArrayOutputStream()
            ps = PrintStream(err)
            println("Redirecting stderr")
            System.setErr(ps)
            configuration.options = Options()
            configuration.options.dumpSourceOnExecutionError = true
            kotlin
                .runCatching {
                    getProgram(nameOrSource = pgm).singleCall(emptyList(), configuration)
                }.onFailure {
                    ps.flush()
                    // Error stream must contain src
                    Assert.assertTrue(err.toString().indexOf("2    D VAR             S              5  0   ") > 0)
                }.onSuccess {
                    Assert.fail()
                }
            // reset stream
            err = ByteArrayOutputStream()
            ps = PrintStream(err)
            System.setErr(ps)
            // restore default options
            configuration.options = Options()
            kotlin
                .runCatching {
                    getProgram(nameOrSource = pgm).singleCall(emptyList(), configuration)
                }.onFailure {
                    ps.flush()
                    // Error stream must not contain src
                    Assert.assertFalse(err.toString().indexOf("2    D VAR             S              5  0   ") > 0)
                }.onSuccess {
                    Assert.fail()
                }
        } finally {
            println("Restoring stderr")
            System.setErr(defaultErr)
            System.err.println("Stderr restored")
        }
    }

    /**
     * When compile error occurs, the error event must be the same of what is expected
     * when the errors occur during the interpretation of the program
     * */
    @Test
    fun onCompileErrorEventMustBeTheSame() {
        val expectedLines = listOf(9, 10)
        val dir = File("src/test/resources")
        File(dir, "ERROR35.rpgle").inputStream().use { src ->
            val errorLines = mutableListOf<Int>()
            val configuration =
                Configuration().apply {
                    jarikoCallback.onError = { errorEvent ->
                        errorEvent.sourceReference?.relativeLine?.let {
                            errorLines.add(it)
                        }
                    }
                }
            kotlin
                .runCatching {
                    compile(
                        src = src,
                        out = null,
                        format = Format.BIN,
                        programFinders = listOf(),
                        configuration = configuration,
                    )
                }.onSuccess {
                    fail("ERROR35 cannot be compiled")
                }
            assertEquals(expected = expectedLines.sorted(), actual = errorLines.sorted())
        }
    }

    @Test
    fun pgmWithErrorCouldBeSerialized() {
        // Based on afterPhaseErrorContinue also pgm will be serialized

        val configuration =
            Configuration().apply {
                // I ignore every error
                options.toAstConfiguration.afterPhaseErrorContinue = { _ -> true }
            }
        javaClass.getResource("/ERROR21.rpgle").also { resource ->
            require(resource != null) { "Resource not found: /ERROR21.rpgle" }
            val path = File(resource.path).parentFile
            val programFinders = listOf(DirRpgProgramFinder(path), DirRpgProgramFinder(path))
            resource.openStream().use { inputStream ->
                compile(
                    src = inputStream,
                    out = ByteArrayOutputStream(),
                    format = Format.BIN,
                    programFinders = programFinders,
                    configuration = configuration,
                )
            }
        }
    }

    /**
     * Test to ensure that standalone data definitions are removed if they are present
     * in an unqualified data structure.
     * @see #LS25000430
     */
    @Test
    fun removeDuplicatedDataDefinition_RemoveStandaloneIfPresentInUnqualifiedDS() {
        val standaloneField1 = DataDefinition(name = "field_1", type = StringType(10), fields = emptyList())
        val standaloneField10 = DataDefinition(name = "field_10", type = StringType(10), fields = emptyList())
        val unqualifiedDS =
            DataDefinition(
                name = "UNQUALIFIED",
                type =
                    DataStructureType(
                        fields =
                            listOf(
                                FieldType("field_1", StringType(10)),
                                FieldType("field_2", StringType(10)),
                            ),
                        elementSize = 10,
                        isQualified = false,
                    ),
                fields =
                    listOf(
                        FieldDefinition(
                            name = "field_1",
                            type = StringType(10),
                            explicitStartOffset = 0,
                            explicitEndOffset = 10,
                        ),
                        FieldDefinition(
                            name = "field_2",
                            type = StringType(10),
                            explicitStartOffset = 11,
                            explicitEndOffset = 20,
                        ),
                    ),
            )
        val qualifiedDS =
            DataDefinition(
                name = "QUALIFIED",
                type =
                    DataStructureType(
                        fields =
                            listOf(
                                FieldType("field_1", StringType(10)),
                                FieldType("field_10", StringType(10)),
                            ),
                        elementSize = 10,
                        isQualified = true,
                    ),
                fields =
                    listOf(
                        FieldDefinition(
                            name = "field_1",
                            type = StringType(10),
                            explicitStartOffset = 0,
                            explicitEndOffset = 10,
                        ),
                        FieldDefinition(
                            name = "field_10",
                            type = StringType(10),
                            explicitStartOffset = 11,
                            explicitEndOffset = 20,
                        ),
                    ),
            )
        val dataDefinitions =
            listOf(
                standaloneField1,
                standaloneField10,
                unqualifiedDS,
                qualifiedDS,
            )

        val compilationUnit =
            CompilationUnit(
                fileDefinitions = emptyList(),
                dataDefinitions = dataDefinitions,
                main = MainBody(stmts = emptyList()),
                subroutines = emptyList(),
                compileTimeArrays = emptyList(),
                directives = emptyList(),
                dataAreas = mapOf(),
                position = null,
            )

        assertEquals(4, compilationUnit.dataDefinitions.size)

        val allDataDefinitions = compilationUnit.allDataDefinitions
        assertEquals(
            expected = 5,
            actual = allDataDefinitions.size,
            message = "There must be 5 data definitions because all `FieldDefinition` is added to the `allDataDefinitions` list",
        )
        assertIs<FieldDefinition>(
            value = allDataDefinitions.firstOrNull { it.name == "field_1" },
            message = "`field_1` must be a FieldDefinition",
        )
        assertNotNull(
            actual = allDataDefinitions.firstOrNull { it.name == "field_10" },
            message = "`field_10` does not must be removed because it is present both as qualified data structure field (with access only by dot notation) and `DataDefinition`",
        )
    }

    @Test
    fun getRootCause() {
        val rootCause = Exception("I'm the root cause")
        try {
            try {
                try {
                    throw rootCause
                } catch (e: Exception) {
                    throw Exception("I'm the 2nd exception", rootCause)
                }
            } catch (e: Exception) {
                throw Exception("I'm the 3rd exception", e)
            }
        } catch (e: Exception) {
            assertEquals(rootCause, getRootCause(e))
        }
    }
}
