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

import com.smeup.dbnative.DBNativeAccessConfig
import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.db.utilities.DBServer
import com.smeup.rpgparser.execute
import com.smeup.rpgparser.execution.*
import com.smeup.rpgparser.execution.SimpleReloadConfig
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.parsing.ast.EvalStmt
import com.smeup.rpgparser.parsing.ast.ProfilingAnnotationResolved
import com.smeup.rpgparser.parsing.ast.ProfilingSpanEndAnnotation
import com.smeup.rpgparser.parsing.ast.ProfilingSpanStartAnnotation
import com.smeup.rpgparser.parsing.parsetreetoast.allStatements
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import kotlin.test.*

open class ProfilingAnnotationTest : AbstractTest() {
    lateinit var smeupConfig: Configuration

    @BeforeTest
    open fun setUp() {
        if (!DBServer.isRunning()) {
            DBServer.startDB()
        }

        smeupConfig = Configuration()
        val path = javaClass.getResource("/smeup/metadata")!!.path
        val connectionConfigs = listOf(
            ConnectionConfig(
                fileName = "*",
                url = "jdbc:hsqldb:hsql://127.0.0.1:9001/mainDb",
                user = "SA",
                password = "",
                driver = "org.hsqldb.jdbc.JDBCDriver"
            )
        )
        val reloadConfig = SimpleReloadConfig(metadataPath = path, connectionConfigs = connectionConfigs)
        smeupConfig.reloadConfig = ReloadConfig(nativeAccessConfig = DBNativeAccessConfig(connectionConfigs.map {
            com.smeup.dbnative.ConnectionConfig(
                fileName = it.fileName,
                url = it.url,
                user = it.user,
                password = it.password,
                driver = it.driver,
                impl = it.impl
            )
        }), metadataProducer = { dbFile: String -> reloadConfig.getMetadata(dbFile = dbFile) })
        val dspfConfig = SimpleDspfConfig(displayFilePath = path)
        smeupConfig.dspfConfig = DspfConfig(
            metadataProducer = { displayFile -> dspfConfig.getMetadata(displayFile) },
            dspfProducer = { displayFile -> dspfConfig.dspfProducer(displayFile) })
        smeupConfig.options.profilingSupport = true
    }

    @Test
    fun executeSimpleTelemetrySpan() {
        val cu = assertASTCanBeProduced("profiling/SIMPLE_TELEMETRY_SPAN", true, withProfilingSupport = true)
        cu.resolveAndValidate()
        execute(cu, emptyMap())

        assertEquals(2, cu.resolvedProfilingAnnotations.size)
        assertSourceLineEquivalence(cu.resolvedProfilingAnnotations)

        val openAnnotations = cu.getOpenAnnotations()
        val closeAnnotations = cu.getClosedAnnotations()

        assertEquals(1, openAnnotations.size)
        assertEquals(1, closeAnnotations.size)

        assertSpanStartPositions(cu, listOf("_SPANID1" to 11), LookupMode.Annotation)
        assertSpanStartPositions(cu, listOf("_SPANID1" to 12), LookupMode.Statement)
        assertSpanEndPositions(cu, listOf(13), LookupMode.Annotation)
        assertSpanEndPositionsExhaustive(cu, listOf(12), LookupMode.Statement)
    }

    @Test
    fun executeMultipleTelemetrySpan() {
        val cu = assertASTCanBeProduced("profiling/MULTIPLE_TELEMETRY_SPAN", true, withProfilingSupport = true)
        cu.resolveAndValidate()
        execute(cu, emptyMap())

        assertEquals(6, cu.resolvedProfilingAnnotations.size)
        assertSourceLineEquivalence(cu.resolvedProfilingAnnotations)

        val openAnnotations = cu.getOpenAnnotations()
        val closeAnnotations = cu.getClosedAnnotations()

        assertEquals(3, openAnnotations.size)
        assertEquals(3, closeAnnotations.size)

        assertSpanStartPositions(
            cu, listOf("_SPANID1" to 12, "_SPANID2" to 14, "_SPANID3" to 16), LookupMode.Annotation
        )
        assertSpanStartPositions(cu, listOf("_SPANID1" to 13, "_SPANID2" to 15, "_SPANID3" to 17), LookupMode.Statement)
        assertSpanEndPositions(cu, listOf(21, 22, 18), LookupMode.Annotation)
        assertSpanEndPositionsExhaustive(cu, listOf(15, 15, 17), LookupMode.Statement)
    }

    @Test
    fun executeMultipleSpanSameStatement() {
        val cu = assertASTCanBeProduced("profiling/MULTISPAN_SAME_STATEMENT", true, withProfilingSupport = true)
        cu.resolveAndValidate()
        execute(cu, emptyMap())

        assertEquals(8, cu.resolvedProfilingAnnotations.size)
        assertSourceLineEquivalence(cu.resolvedProfilingAnnotations)

        val openAnnotations = cu.getOpenAnnotations()
        val closeAnnotations = cu.getClosedAnnotations()

        assertEquals(4, openAnnotations.size)
        assertEquals(4, closeAnnotations.size)

        assertSpanStartPositions(
            cu, listOf("_SPANID1" to 11, "_SPANID2" to 12, "_SPANID3" to 13, "_SPANID4" to 14), LookupMode.Annotation
        )
        assertSpanStartPositions(
            cu, listOf("_SPANID1" to 15, "_SPANID2" to 15, "_SPANID3" to 15, "_SPANID4" to 15), LookupMode.Statement
        )
        assertSpanEndPositions(cu, listOf(16, 17, 18, 19), LookupMode.Annotation)
        assertSpanEndPositionsExhaustive(cu, listOf(15, 15, 15, 15), LookupMode.Statement)
    }

    @Test
    fun executeMultipleSpanSameStatement2() {
        val cu = assertASTCanBeProduced("profiling/MULTISPAN_SAME_STATEMENT_2", true, withProfilingSupport = true)
        cu.resolveAndValidate()
        execute(cu, emptyMap())

        assertEquals(8, cu.resolvedProfilingAnnotations.size)
        assertSourceLineEquivalence(cu.resolvedProfilingAnnotations)

        val openAnnotations = cu.getOpenAnnotations()
        val closeAnnotations = cu.getClosedAnnotations()

        assertEquals(4, openAnnotations.size)
        assertEquals(4, closeAnnotations.size)

        assertSpanStartPositions(
            cu, listOf("_SPANID1" to 11, "_SPANID2" to 12, "_SPANID3" to 13, "_SPANID4" to 14), LookupMode.Annotation
        )
        assertSpanStartPositions(
            cu, listOf("_SPANID1" to 15, "_SPANID2" to 15, "_SPANID3" to 15, "_SPANID4" to 15), LookupMode.Statement
        )
        assertSpanEndPositions(cu, listOf(16, 17, 18, 20), LookupMode.Annotation)
        assertSpanEndPositionsExhaustive(cu, listOf(15, 15, 15, 19), LookupMode.Statement)
    }

    @Test
    fun executeForSpan() {
        val cu = assertASTCanBeProduced("profiling/FOR_TELEMETRY_SPAN", true, withProfilingSupport = true)
        cu.resolveAndValidate()
        execute(cu, emptyMap())

        assertEquals(4, cu.resolvedProfilingAnnotations.size)
        assertSourceLineEquivalence(cu.resolvedProfilingAnnotations)
        assertSpanStartPositions(
            cu, listOf(
                "BEFOREFOR" to 12, "FORBODYSTART" to 14
            ), LookupMode.Annotation
        )
        assertSpanStartPositions(
            cu, listOf(
                "BEFOREFOR" to 13, "FORBODYSTART" to 15
            ), LookupMode.Statement
        )
        assertSpanEndPositions(cu, listOf(16, 18), LookupMode.Annotation)
        assertSpanEndPositionsExhaustive(cu, listOf(13, 15), LookupMode.Statement)
    }

    @Test
    fun executeDoSpan() {
        val cu = assertASTCanBeProduced("profiling/DO_TELEMETRY_SPAN", true, withProfilingSupport = true)
        cu.resolveAndValidate()
        execute(cu, emptyMap())

        assertEquals(4, cu.resolvedProfilingAnnotations.size)
        assertSourceLineEquivalence(cu.resolvedProfilingAnnotations)

        assertSpanStartPositions(
            cu, listOf(
                "BEFOREDO" to 12, "DOBODYSTART" to 14
            ), LookupMode.Annotation
        )
        assertSpanStartPositions(
            cu, listOf(
                "BEFOREDO" to 13, "DOBODYSTART" to 15
            ), LookupMode.Statement
        )
        assertSpanEndPositions(cu, listOf(16, 18), LookupMode.Annotation)
        assertSpanEndPositionsExhaustive(cu, listOf(13, 15), LookupMode.Statement)
    }

    @Test
    fun executeMonitorSpan() {
        val cu = assertASTCanBeProduced("profiling/MONITOR_TELEMETRY_SPAN", true, withProfilingSupport = true)
        cu.resolveAndValidate()
        execute(cu, emptyMap())

        assertEquals(6, cu.resolvedProfilingAnnotations.size)
        assertSourceLineEquivalence(cu.resolvedProfilingAnnotations)

        assertSpanStartPositions(
            cu, listOf(
                "BEFORESTMT" to 12, "STMTBEGIN" to 14, "STMTERROR" to 18
            ), LookupMode.Annotation
        )
        assertSpanStartPositions(
            cu, listOf(
                "BEFORESTMT" to 13, "STMTBEGIN" to 15, "STMTERROR" to 19
            ), LookupMode.Statement
        )
        assertSpanEndPositions(cu, listOf(16, 20, 22), LookupMode.Annotation)
        assertSpanEndPositionsExhaustive(cu, listOf(13, 15, 19), LookupMode.Statement)
    }

    @Test
    fun executeIfSpan() {
        val cu = assertASTCanBeProduced("profiling/IF_TELEMETRY_SPAN", true, withProfilingSupport = true)
        cu.resolveAndValidate()
        execute(cu, emptyMap())

        assertEquals(8, cu.resolvedProfilingAnnotations.size)
        assertSourceLineEquivalence(cu.resolvedProfilingAnnotations)

        assertSpanStartPositions(
            cu, listOf(
                "BEFORESTMT" to 12, "IFBODY" to 14, "ELIFBODY" to 19, "ELSEBODY" to 24
            ), LookupMode.Annotation
        )
        assertSpanStartPositions(
            cu, listOf(
                "BEFORESTMT" to 13, "IFBODY" to 15, "ELIFBODY" to 20, "ELSEBODY" to 25
            ), LookupMode.Statement
        )
        assertSpanEndPositions(cu, listOf(17, 22, 27, 29), LookupMode.Annotation)
        assertSpanEndPositionsExhaustive(cu, listOf(16, 21, 26, 13), LookupMode.Statement)
    }

    @Test
    fun executeSelectSpan() {
        val cu = assertASTCanBeProduced("profiling/SELECT_TELEMETRY_SPAN", true, withProfilingSupport = true)
        cu.resolveAndValidate()
        execute(cu, emptyMap())

        assertEquals(8, cu.resolvedProfilingAnnotations.size)
        assertSourceLineEquivalence(cu.resolvedProfilingAnnotations)

        assertSpanStartPositions(
            cu, listOf(
                "BEFORESELECT" to 12, "CONTENT1" to 15, "CONTENT2" to 19, "CONTENT3" to 23
            ), LookupMode.Annotation
        )
        assertSpanStartPositions(
            cu, listOf(
                "BEFORESELECT" to 13, "CONTENT1" to 16, "CONTENT2" to 20, "CONTENT3" to 24
            ), LookupMode.Statement
        )
        assertSpanEndPositions(cu, listOf(17, 21, 25, 27), LookupMode.Annotation)
        assertSpanEndPositionsExhaustive(cu, listOf(13, 24, 20, 16), LookupMode.Statement)
    }

    @Test
    fun executeCopyTelemetrySpan() {
        val cu = assertASTCanBeProduced("profiling/COPY_TELEMETRY_SPAN", true, withProfilingSupport = true)
        cu.resolveAndValidate()
        execute(cu, emptyMap())

        assertEquals(2, cu.resolvedProfilingAnnotations.size)
        assertSourceLineEquivalence(cu.resolvedProfilingAnnotations)

        val openAnnotations = cu.getOpenAnnotations()
        val closeAnnotations = cu.getClosedAnnotations()

        assertEquals(1, openAnnotations.size)
        assertEquals(1, closeAnnotations.size)

        // Annotations will result moved from the original expected position but will be attached to the same statement
        val eval = cu.allStatements().firstOrNull { stmt -> stmt is EvalStmt }
        val evalLine = eval!!.position!!.start.line

        val (open) = openAnnotations
        val (close) = closeAnnotations

        assertEquals(open.profilingLine + 1, open.statementLine)
        assertEquals(close.profilingLine - 1, close.statementLine)
        assertEquals(open.statementLine, close.statementLine)
        assertEquals(open.statementLine, evalLine)
        assertEquals(close.statementLine, evalLine)
    }

    private enum class LookupMode {
        Statement, Annotation
    }

    private fun assertSpanStartPositions(cu: CompilationUnit, spans: List<Pair<String, Int>>, mode: LookupMode) {
        val openAnnotations = cu.getOpenAnnotations()
        spans.forEach {
            val (name, line) = it
            val span =
                openAnnotations.find { annotation -> (annotation.source as ProfilingSpanStartAnnotation).name == name }
            assertEquals(
                line, when (mode) {
                    LookupMode.Statement -> span?.statementLine
                    LookupMode.Annotation -> span?.profilingLine
                }, "cannot find span '$name' at line '$line'"
            )
        }
    }

    private fun assertSpanEndPositions(cu: CompilationUnit, spans: List<Int>, mode: LookupMode) {
        val closedAnnotations = cu.getClosedAnnotations()
        spans.forEach { span ->
            assertTrue(
                closedAnnotations.any {
                    when (mode) {
                        LookupMode.Statement -> it.statementLine == span
                        LookupMode.Annotation -> it.profilingLine == span
                    }
                }, "could not find span at line $span"
            )
        }
    }

    private fun assertSpanEndPositionsExhaustive(cu: CompilationUnit, spans: List<Int>, mode: LookupMode) {
        val closedAnnotations = cu.getClosedAnnotations()
        val toProcess = closedAnnotations.toMutableList()
        spans.forEach { span ->
            val annotation = toProcess.firstOrNull {
                when (mode) {
                    LookupMode.Statement -> it.statementLine == span
                    LookupMode.Annotation -> it.profilingLine == span
                }
            }

            assertNotNull(annotation)
            toProcess.remove(annotation)
        }

        assertEquals(0, toProcess.size)
    }

    private fun CompilationUnit.getOpenAnnotations() = this.resolvedProfilingAnnotations.filter {
        it.source is ProfilingSpanStartAnnotation
    }

    private fun CompilationUnit.getClosedAnnotations() = this.resolvedProfilingAnnotations.filter {
        it.source is ProfilingSpanEndAnnotation
    }

    private fun ProfilingAnnotationResolved.sourceLine() = this.source.position?.start?.line

    private fun assertSourceLineEquivalence(annotations: List<ProfilingAnnotationResolved>): Boolean =
        annotations.all { it.sourceLine() == it.profilingLine }
}
