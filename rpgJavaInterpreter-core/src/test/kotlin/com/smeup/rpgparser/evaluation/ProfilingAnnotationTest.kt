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
import com.smeup.rpgparser.parsing.ast.ProfilingSpanEndAnnotation
import com.smeup.rpgparser.parsing.ast.ProfilingSpanStartAnnotation
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
        val connectionConfigs = listOf(ConnectionConfig(
            fileName = "*",
            url = "jdbc:hsqldb:hsql://127.0.0.1:9001/mainDb",
            user = "SA",
            password = "",
            driver = "org.hsqldb.jdbc.JDBCDriver"
        ))
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
        val openAnnotations = cu.resolvedProfilingAnnotations.filter { it.source is ProfilingSpanStartAnnotation }
        val closeAnnotations = cu.resolvedProfilingAnnotations.filter { it.source is ProfilingSpanEndAnnotation }

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
        val openAnnotations = cu.resolvedProfilingAnnotations.filter { it.source is ProfilingSpanStartAnnotation }
        val closeAnnotations = cu.resolvedProfilingAnnotations.filter { it.source is ProfilingSpanEndAnnotation }

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
        val openAnnotations = cu.resolvedProfilingAnnotations.filter { it.source is ProfilingSpanStartAnnotation }
        val closeAnnotations = cu.resolvedProfilingAnnotations.filter { it.source is ProfilingSpanEndAnnotation }

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
        val openAnnotations = cu.resolvedProfilingAnnotations.filter { it.source is ProfilingSpanStartAnnotation }
        val closeAnnotations = cu.resolvedProfilingAnnotations.filter { it.source is ProfilingSpanEndAnnotation }

        assertEquals(4, openAnnotations.size)
        assertEquals(4, closeAnnotations.size)

        assertTrue(openAnnotations.all { it.statementLine == 15 })
        assertEquals(3, closeAnnotations.count { it.statementLine == 15 })
        assertEquals(1, closeAnnotations.count { it.statementLine == 19 })
    }
}
