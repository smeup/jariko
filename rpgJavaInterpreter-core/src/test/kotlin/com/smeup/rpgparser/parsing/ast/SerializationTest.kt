package com.smeup.rpgparser.parsing.ast

import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.facade.RpgParserFacade
import org.junit.BeforeClass
import org.junit.Ignore
import org.junit.Test
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.measureTimeMillis
import kotlin.test.assertEquals

class SerializationTest {

    companion object {
        lateinit var timestamp: String
        lateinit var testFiles: Collection<File>
        @BeforeClass @JvmStatic fun setup() {
            timestamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
            val dir = File(javaClass.getResource("/performance-ast").path)
            testFiles = dir.listFiles().filter {
                it.name.endsWith(".rpgle") && it.name != ("MUTE_ERROR.rpgle")
            }
        }
    }

    private fun createAstFromSource(fileName: String, inputStream: InputStream) = RpgParserFacade().parseAndProduceAst(inputStream)

    private fun String.timestampName() = "${timestamp}_$this"

    @Test
    @Ignore
    fun probeSerializationForAllPerformanceMuteAst() {
        val errors = mutableSetOf<String?>()
        testFiles.forEach { file ->
            FileInputStream(file).use { inputStream ->
                println("Creating AST from source: ${file.name}")
                MainExecutionContext.execute(systemInterface = JavaSystemInterface()) {
                    val compilationUnit = createAstFromSource(file.name, inputStream)
                    println("Probing...")
                    kotlin.runCatching {
                        compilationUnit.encodeToString()
                    }.onFailure { error ->
                        if (errors.add(error.message)) {
                            System.err.println(error.message)
                        }
                    }.onSuccess {
                        println("Ok")
                    }
                }
            }
        }
        assertEquals(true, errors.isEmpty())
    }

    // TODO It could be useful move this test and classify it as performance
    @Test
    @Ignore
    fun comparePerformanceAst() {
        repeat(1) { it ->
            println("Round $it")
            println("File\t\tLoad from SRC\t\tLoad from BIN")
            testFiles.forEach { file ->
                FileInputStream(file).use {
                    print("${file.name}")
                    val astBySource: CompilationUnit
                    print("\t\t${measureTimeMillis { astBySource = createAstFromSource(file.name, it) }}")
                    val file = File(System.getProperty("java.io.tmpdir"), file.name.timestampName())
                    file.deleteOnExit()
                    // file.writeText(astBySource.encodeToString())
                    file.writeBytes(astBySource.encodeToByteArray())
                    val astBySerialized: CompilationUnit
                    // println("\t\t${measureTimeMillis { astBySerialized = file.readText().createCompilationUnit() }}")
                    println("\t\t${measureTimeMillis { astBySerialized = file.readBytes().createCompilationUnit() }}")
                    // assertEquals(astBySource, astBySerialized)
                }
            }
        }
    }
}