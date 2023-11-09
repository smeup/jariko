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

package com.smeup.rpgparser.execution

import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MainExecutionContextTest {

    private lateinit var defaultErr: PrintStream
    private lateinit var byteArrayOutputStream: ByteArrayOutputStream

    @Before
    fun setup() {
        defaultErr = System.err
        byteArrayOutputStream = ByteArrayOutputStream(1024)
        val printStream = PrintStream(byteArrayOutputStream)
        System.setErr(printStream)
    }

    @After
    fun teardown() {
        System.setErr(defaultErr)
        System.err.println(byteArrayOutputStream.toByteArray())
    }

    @Test
    fun `it has never to show the message Reset idProvider with the default SymbolTable`() {
        for (i in 1..33000) {
            MainExecutionContext.newId()
        }
        assertFalse { String(byteArrayOutputStream.toByteArray()).contains("Reset idProvider") }
    }

    // I want to be sure that only the first instances of Configuration and JavaSystemInterface will be used
    @Test
    fun testFirstInstancesUsageInCaseOfRecursiveExecution() {
        val configs = listOf(
            Configuration(), Configuration()
        )
        val systemInterfaces = listOf(JavaSystemInterface(), JavaSystemInterface())
        var createConfigurationTimes = 0
        var createJavaSystemInterfaceTimes = 0
        createMainExecutionContextRecursively(
            createConfiguration = { configs[createConfigurationTimes++] },
            createJavaSystemInterface = { systemInterfaces[createJavaSystemInterfaceTimes++] },
            rootExecution = {
                assertTrue { configs[0] === MainExecutionContext.getConfiguration() }
                assertTrue { systemInterfaces[0] === MainExecutionContext.getSystemInterface() }
            },
            innerExecution = {
                // Here I assert that the first instance booth config and systemInterface must be used
                assertTrue { configs[0] === MainExecutionContext.getConfiguration() }
                assertTrue { systemInterfaces[0] === MainExecutionContext.getSystemInterface() }
            }
        )
    }

    // The MainExecutionContext must stay in created state also when inner execution throws an error
    @Test
    fun testMainExecutionCleanupInCaseOfRecursiveExecution() {
        val config = Configuration()
        val systemInterface = JavaSystemInterface()
        createMainExecutionContextRecursively(
            createConfiguration = { config },
            createJavaSystemInterface = { systemInterface },
            rootExecution = {},
            rootExecutionError = {},
            innerExecution = { error("Forced error") },
            innerExecutionError = { assertTrue { MainExecutionContext.isCreated() } }
        )
        assertFalse(MainExecutionContext.isCreated())
    }

    private fun createMainExecutionContextRecursively(
        createConfiguration: () -> Configuration,
        createJavaSystemInterface: () -> JavaSystemInterface,
        rootExecution: () -> Unit,
        rootExecutionError: (Throwable) -> Unit = { throwable -> throw throwable },
        innerExecution: () -> Unit,
        innerExecutionError: (Throwable) -> Unit = { throwable -> throw throwable }
    ) {
        kotlin.runCatching {
            MainExecutionContext.execute(
                configuration = createConfiguration(),
                systemInterface = createJavaSystemInterface(),
                mainProgram = { _ ->
                    rootExecution()
                    kotlin.runCatching {
                        MainExecutionContext.execute(
                            configuration = createConfiguration(),
                            systemInterface = createJavaSystemInterface(),
                            mainProgram = { _ -> innerExecution() }
                        )
                    }.onFailure {
                        innerExecutionError(it)
                    }.getOrThrow()
                }
            )
        }.onFailure {
            rootExecutionError(it)
        }
    }
}