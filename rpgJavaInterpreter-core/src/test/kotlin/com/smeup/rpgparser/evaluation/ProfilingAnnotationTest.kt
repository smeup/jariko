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
        smeupConfig.reloadConfig = ReloadConfig(
            nativeAccessConfig = DBNativeAccessConfig(connectionConfigs.map {
                com.smeup.dbnative.ConnectionConfig(
                    fileName = it.fileName,
                    url = it.url,
                    user = it.user,
                    password = it.password,
                    driver = it.driver,
                    impl = it.impl
                )
            }),
            metadataProducer = { dbFile: String -> reloadConfig.getMetadata(dbFile = dbFile) })
        val dspfConfig = SimpleDspfConfig(displayFilePath = path)
        smeupConfig.dspfConfig = DspfConfig(
            metadataProducer = { displayFile -> dspfConfig.getMetadata(displayFile) },
            dspfProducer = { displayFile -> dspfConfig.dspfProducer(displayFile) }
        )
        smeupConfig.options.profilingSupport = true
    }

    @Test
    fun executeSimpleTelemetrySpan() {
        val cu = assertASTCanBeProduced("profiling/SIMPLE_TELEMETRY_SPAN", true, withProfilingSupport = true)
        cu.resolveAndValidate()
        execute(cu, emptyMap())

        assertEquals(2, cu.resolvedProfilingAnnotations.size)
        val openAnnotations = cu.getOpenAnnotations()
        val closeAnnotations = cu.getClosedAnnotations()

        assertEquals(1, openAnnotations.size)
        assertEquals(1, closeAnnotations.size)

        // Assert both are attached correctly
        assertEquals(11, openAnnotations[0].profilingLine)
        assertEquals(12, openAnnotations[0].statementLine)

        assertEquals(13, closeAnnotations[0].profilingLine)
        assertEquals(12, closeAnnotations[0].statementLine)
    }

    @Test
    fun executeMultipleTelemetrySpan() {
        val cu = assertASTCanBeProduced("profiling/MULTIPLE_TELEMETRY_SPAN", true, withProfilingSupport = true)
        cu.resolveAndValidate()
        execute(cu, emptyMap())

        assertEquals(6, cu.resolvedProfilingAnnotations.size)
        val openAnnotations = cu.getOpenAnnotations()
        val closeAnnotations = cu.getClosedAnnotations()

        assertEquals(3, openAnnotations.size)
        assertEquals(3, closeAnnotations.size)

        val (openSpan1, openSpan2, openSpan3) = openAnnotations
        val (closeSpan1, closeSpan2, closeSpan3) = closeAnnotations

        assertEquals(12, openSpan1.profilingLine)
        assertEquals(13, openSpan1.statementLine)

        assertEquals(14, openSpan2.profilingLine)
        assertEquals(15, openSpan2.statementLine)

        assertEquals(16, openSpan3.profilingLine)
        assertEquals(17, openSpan3.statementLine)

        assertEquals(21, closeSpan1.profilingLine)
        assertEquals(15, closeSpan1.statementLine)

        assertEquals(22, closeSpan2.profilingLine)
        assertEquals(15, closeSpan2.statementLine)

        assertEquals(18, closeSpan3.profilingLine)
        assertEquals(17, closeSpan3.statementLine)
    }

    @Test
    fun executeMultipleSpanSameStatement() {
        val cu = assertASTCanBeProduced("profiling/MULTISPAN_SAME_STATEMENT", true, withProfilingSupport = true)
        cu.resolveAndValidate()
        execute(cu, emptyMap())

        assertEquals(8, cu.resolvedProfilingAnnotations.size)
        val openAnnotations = cu.getOpenAnnotations()
        val closeAnnotations = cu.getClosedAnnotations()

        assertEquals(4, openAnnotations.size)
        assertEquals(4, closeAnnotations.size)

        assertTrue(openAnnotations.all { it.statementLine == 15 })
        assertTrue(closeAnnotations.all { it.statementLine == 15 })
    }

    @Test
    fun executeMultipleSpanSameStatement2() {
        val cu = assertASTCanBeProduced("profiling/MULTISPAN_SAME_STATEMENT_2", true, withProfilingSupport = true)
        cu.resolveAndValidate()
        execute(cu, emptyMap())

        assertEquals(8, cu.resolvedProfilingAnnotations.size)
        val openAnnotations = cu.getOpenAnnotations()
        val closeAnnotations = cu.getClosedAnnotations()

        assertEquals(4, openAnnotations.size)
        assertEquals(4, closeAnnotations.size)

        assertTrue(openAnnotations.all { it.statementLine == 15 })
        assertEquals(3, closeAnnotations.count { it.statementLine == 15 })
        assertEquals(1, closeAnnotations.count { it.statementLine == 19 })
    }

    @Test
    fun executeForSpan() {
        val cu = assertASTCanBeProduced("profiling/FOR_TELEMETRY_SPAN", true, withProfilingSupport = true)
        cu.resolveAndValidate()
        execute(cu, emptyMap())

        assertEquals(4, cu.resolvedProfilingAnnotations.size)
        assertSpanStartPositions(
            cu, listOf(
                "BEFOREFOR" to 13,
                "FORBODYSTART" to 15
            )
        )
        assertSpanEndPositions(cu, listOf(13, 15))
    }

    @Test
    fun executeDoSpan() {
        val cu = assertASTCanBeProduced("profiling/DO_TELEMETRY_SPAN", true, withProfilingSupport = true)
        cu.resolveAndValidate()
        execute(cu, emptyMap())

        assertEquals(4, cu.resolvedProfilingAnnotations.size)
        assertSpanStartPositions(
            cu, listOf(
                "BEFOREDO" to 13,
                "DOBODYSTART" to 15
            )
        )
        assertSpanEndPositions(cu, listOf(13, 15))
    }

    @Test
    fun executeMonitorSpan() {
        val cu = assertASTCanBeProduced("profiling/MONITOR_TELEMETRY_SPAN", true, withProfilingSupport = true)
        cu.resolveAndValidate()
        execute(cu, emptyMap())

        assertEquals(6, cu.resolvedProfilingAnnotations.size)
        assertSpanStartPositions(
            cu, listOf(
                "BEFORESTMT" to 13,
                "STMTBEGIN" to 15,
                "STMTERROR" to 19
            )
        )
        assertSpanEndPositions(cu, listOf(13, 15, 19))
    }

    @Test
    fun executeIfSpan() {
        val cu = assertASTCanBeProduced("profiling/IF_TELEMETRY_SPAN", true, withProfilingSupport = true)
        cu.resolveAndValidate()
        execute(cu, emptyMap())

        assertEquals(8, cu.resolvedProfilingAnnotations.size)
        assertSpanStartPositions(
            cu, listOf(
                "BEFORESTMT" to 13,
                "IFBODY" to 15,
                "ELIFBODY" to 20,
                "ELSEBODY" to 25
            )
        )
        assertSpanEndPositions(cu, listOf(16, 21, 26, 13))
    }

    @Test
    fun executeSelectSpan() {
        val cu = assertASTCanBeProduced("profiling/SELECT_TELEMETRY_SPAN", true, withProfilingSupport = true)
        cu.resolveAndValidate()
        execute(cu, emptyMap())

        assertEquals(8, cu.resolvedProfilingAnnotations.size)
        assertSpanStartPositions(
            cu, listOf(
                "BEFORESELECT" to 13,
                "CONTENT1" to 16,
                "CONTENT2" to 20,
                "CONTENT3" to 24
            )
        )
        assertSpanEndPositions(cu, listOf(13, 24, 20, 16))
    }

    @Test
    fun executeCopyTelemetrySpan() {
        val cu = assertASTCanBeProduced("profiling/COPY_TELEMETRY_SPAN", true, withProfilingSupport = true)
        cu.resolveAndValidate()
        execute(cu, emptyMap())

        assertEquals(2, cu.resolvedProfilingAnnotations.size)
        val openAnnotations = cu.getOpenAnnotations()
        val closeAnnotations = cu.getClosedAnnotations()

        assertEquals(1, openAnnotations.size)
        assertEquals(1, closeAnnotations.size)

        // Annotations will result moved from the original expected position but will be attached to the same statement
        val eval = cu.allStatements().firstOrNull { stmt -> stmt is EvalStmt }
        assertEquals(openAnnotations[0].profilingLine + 1, openAnnotations[0].statementLine)
        assertEquals(closeAnnotations[0].profilingLine - 1, closeAnnotations[0].statementLine)
        assertEquals(closeAnnotations[0].statementLine, openAnnotations[0].statementLine)
        assertEquals(closeAnnotations[0].statementLine, eval!!.position!!.start.line)
    }

    private fun assertSpanStartPositions(cu: CompilationUnit, spans: List<Pair<String, Int>>) {
        val openAnnotations = cu.getOpenAnnotations()
        spans.forEach {
            val (name, line) = it
            val span =
                openAnnotations.find { annotation -> (annotation.source as ProfilingSpanStartAnnotation).name == name }
            assertEquals(line, span?.statementLine, "cannot find span '$name' at line '$line'")
        }
    }

    private fun assertSpanEndPositions(cu: CompilationUnit, spans: List<Int>) {
        val closedAnnotations = cu.getClosedAnnotations()
        spans.forEach { span ->
            assertTrue(closedAnnotations.any { it.statementLine == span })
        }
    }

    private fun CompilationUnit.getOpenAnnotations() = this.resolvedProfilingAnnotations.filter {
        it.source is ProfilingSpanStartAnnotation
    }

    private fun CompilationUnit.getClosedAnnotations() = this.resolvedProfilingAnnotations.filter {
        it.source is ProfilingSpanEndAnnotation
    }
}
