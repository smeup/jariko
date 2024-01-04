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

package com.smeup.rpgparser.db

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execution.CommandLineParms
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.Options
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.logging.PERFORMANCE_LOGGER
import com.smeup.rpgparser.logging.consoleLoggingConfiguration
import org.junit.Test
open class OpenCloseTest : AbstractTest() {

    private val consoleLoggers = arrayOf(PERFORMANCE_LOGGER)
    @Test
    fun testT53_A01_P01() {
        testIfReloadConfig { reloadConfig ->
            val si = JavaSystemInterface().apply {
                loggingConfiguration = consoleLoggingConfiguration(*consoleLoggers)
            }
            executePgm(
                programName = "db/T53_A01_P01",
                params = CommandLineParms(emptyMap()),
                configuration = Configuration(reloadConfig = reloadConfig, options = Options(muteSupport = true)),
                systemInterface = si
            )
        }
    }
}
