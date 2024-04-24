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
import com.smeup.rpgparser.DBPerformanceTest
import com.smeup.rpgparser.execution.CommandLineParms
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.Options
import com.smeup.rpgparser.interpreter.IntValue
import com.smeup.rpgparser.interpreter.StringValue
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.logging.LogChannel
import com.smeup.rpgparser.logging.consoleLoggingConfiguration
import org.junit.Test
import org.junit.experimental.categories.Category
import java.io.File
import java.io.PrintWriter
import java.time.LocalDateTime

/**
 * This class is for db performance test.
 * All methods should be annotated with [com.smeup.rpgparser.DBPerformanceTest]
 * */
open class MiscDBPerformanceTest : AbstractTest() {

    @Test(timeout = 3600000)
    @Category(DBPerformanceTest::class)
    open fun testEXEC_MUTE() {

        // Set if you want print output csv
        val print = true

        testIfReloadConfig { reloadConfig ->
            val current = LocalDateTime.now()
            val dateString = current.toString().replace(":", ".")
            var jrkCsvOutputDir = System.getProperty("jrkCsvOutputDir", System.getProperty("java.io.tmpdir"))
            val testPerfWriter: PrintWriter? = if (print) File("$jrkCsvOutputDir/Test-Performance_$dateString.csv").apply {
            println("Creating ${this.path}") }.printWriter() else null
            val i40d5Writer: PrintWriter? = if (print) File("$jrkCsvOutputDir/I40D5_$dateString.csv").apply {
                println("Creating ${this.path}") }.printWriter() else null
            val jrkExecMuteRepeat = System.getProperty("jrkEXEC_MUTErepeat", "1")
            println("execution mute repeat: $jrkExecMuteRepeat")
            val si = JavaSystemInterface().apply {
                loggingConfiguration = consoleLoggingConfiguration(LogChannel.PERFORMANCE)
                // capture messages and write them in CSV files
                onDisplay = { message, _ ->
                    println("Handling displayed message: $message")
                    if (!message.contains(";")) {
                        testPerfWriter?.println(message)
                        testPerfWriter?.flush()
                        i40d5Writer?.println(message)
                        i40d5Writer?.flush()
                    }
                }
            }

            val params = CommandLineParms(
                mapOf(
                    "EXEC_DB" to StringValue(reloadConfig.nativeAccessConfig.connectionsConfig[0].driver.toString()),
                    "EXEC_NM" to IntValue(jrkExecMuteRepeat.toLong())
                )
            )
            try {
                executePgm(
                    programName = "db/EXEC_MUTE",
                    params = params,
                    configuration = Configuration(reloadConfig = reloadConfig, options = Options(muteSupport = true)),
                    systemInterface = si
                )
            } finally {
                testPerfWriter?.close()
                i40d5Writer?.close()
            }
        }
    }
}