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

import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertFalse

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
}