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

package com.jariko.samples

import com.smeup.rpgparser.execution.CallProgramHandler
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.Options
import com.smeup.rpgparser.execution.getProgram
import com.smeup.rpgparser.interpreter.IntValue
import com.smeup.rpgparser.interpreter.RpgProgram
import com.smeup.rpgparser.interpreter.SystemInterface
import com.smeup.rpgparser.interpreter.Value
import com.smeup.rpgparser.parsing.ast.Api
import com.smeup.rpgparser.parsing.ast.ApiDescriptor
import com.smeup.rpgparser.parsing.ast.ApiId
import com.smeup.rpgparser.parsing.ast.SourceProgram
import com.smeup.rpgparser.parsing.facade.Copy
import com.smeup.rpgparser.parsing.facade.CopyId
import com.smeup.rpgparser.parsing.facade.key
import com.smeup.rpgparser.rpginterop.CopyFileExtension
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import com.smeup.rpgparser.rpginterop.RpgProgramFinder
import java.io.File
import java.net.URL
// How to implements a Simple Call Program Handler that override OP_CALL call (createCallProgramHandler)
// And how to extends RpgProgramFinder to allow to Jariko to retrieve "OP_ADD" contract definition from URL (UrlRpgProgramFinder)

fun createCallProgramHandler(): CallProgramHandler {
    return CallProgramHandler(
        handleCall = { programName: String, _: SystemInterface, params: LinkedHashMap<String, Value> ->
            if (programName == "OP_ADD") {
                val a = params["A"]!!.asInt().value
                val b = params["B"]!!.asInt().value
                val c = (a + b)*2
                listOf(params["A"]!!, params["B"]!!, IntValue(c))
            } else {
                null
            }
        }
    )
}

class UrlRpgProgramFinder(val endpoint: URL) : RpgProgramFinder {

    override fun findRpgProgram(nameOrSource: String): RpgProgram? {
        // runCatching is wanted because endpoint could not have my program
        return runCatching {
            // use of source program and not bin is just because this is an example
            val pgmUrl = URL("$endpoint/$nameOrSource.rpgle")
            pgmUrl.openStream().use {
                println("Loading $nameOrSource from $pgmUrl")
                RpgProgram.fromInputStream(it, nameOrSource, SourceProgram.RPGLE)
            }
        }.onFailure {
            println(it.message)
        }.onSuccess {
        }.getOrNull()
    }

    override fun findCopy(copyId: CopyId): Copy? {
    // runCatching is wanted because endpoint could not have my program
        return runCatching {
            val pgmUrl = URL("$endpoint/${copyId.key(CopyFileExtension.rpgle)}")
            pgmUrl.openStream().use {
                println("Loading $copyId from $pgmUrl")
                Copy.fromInputStream(it)
            }
        }.onFailure {
            println(it.message)
        }.onSuccess {
        }.getOrNull()
    }

    override fun findApiDescriptor(apiId: ApiId): ApiDescriptor? {
        TODO("Not yet implemented")
    }

    override fun findApi(apiId: ApiId): Api? {
        TODO("Not yet implemented")
    }
}

fun execJariko() {

    val configuration = Configuration(
        options = Options(callProgramHandler = createCallProgramHandler())
    )
    val programFinders = listOf(
        DirRpgProgramFinder(File({ }.javaClass.getResource("/rpg").path)),
        UrlRpgProgramFinder(
            endpoint = { }.javaClass.getResource("/rpg/api"))
        // I assume that all rpgle containing api contracts will be provided by endpoint.
        // Attention: The fact that the endpoint url has "file:" as protocol is just because, is more convenient
        // for our purposes, to work with local file, but endpoint could be whatever type of protocol.
    )

    val program = getProgram(
        nameOrSource = "CALCULATOR.rpgle",
        programFinders = programFinders
    )
    println("Call CALCULATOR OP_ADD.rpgle")
    program.singleCall(
        listOf("10", "20", "")
    )
    println("Call CALCULATOR through CallProgramHandler")
    program.singleCall(
        listOf("10", "20", ""),
        // in configuration we have implemented call overriding
        // result 60 is wanted to demonstrate that we have "properly" overwritten the call statement
        configuration = configuration
    )
}

fun main() {
    execJariko()
}
