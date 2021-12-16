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

import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.Options
import com.smeup.rpgparser.execution.getProgram
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import com.smeup.rpgparser.utils.compile
import java.io.File

data class Fun(val program: String, val function: String, val method: String) {
    constructor(program: String) : this(program, "", "")
}

fun Fun.dsEntries(): Map<String, Value> {
    return mapOf(
        "£UIBPG" to StringValue(program, false),
        "£UIBFU" to StringValue(function, false),
        "£UIBME" to StringValue(method, false)
    )
}

data class SmeupObj(val type: String, val parameter: String, val code: String)

fun SmeupObj.dsEntries(): Map<String, Value> {
    return mapOf(
        "£UIBPG" to StringValue(type, false),
        "£UIBFU" to StringValue(parameter, false),
        "£UIBME" to StringValue(code, false)
    )
}

class DopedProgram(
    val params: List<ProgramParam>,
    private val execute: (systemInterface: SystemInterface, params: LinkedHashMap<String, Value>) -> List<Value>
) : Program {

    override fun params(): List<ProgramParam> {
        return params
    }

    override fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>): List<Value> {
        return execute.invoke(systemInterface, params)
    }
}

fun AbstractDataDefinition.toProgramParam() = ProgramParam(name = name, type = type)

fun Type.toProgramParam(name: String) = ProgramParam(name = name, type = this)

private class MockSystemInterface : JavaSystemInterface() {

    lateinit var cu: CompilationUnit

    override fun findProgram(name: String): Program? {
        return when (name) {
            "B£INZ0" -> DopedProgram(params = listOf(
                StringType(10).toProgramParam("£INZFU"),
                StringType(10).toProgramParam("£INZME"),
                cu.getDataOrFieldDefinition("£INZDS").toProgramParam(),
                StringType(10).toProgramParam("£INZCO")
            )) { _, _ ->
                emptyList()
            }
            "£JAX_IMP0" -> DopedProgram(params = listOf()) { _, _ ->
                emptyList()
            }
            "£JAX_ADDO" -> DopedProgram(params = listOf(
                StringType(length = 2).toProgramParam("£JAXT1"),
                StringType(length = 10).toProgramParam("£JAXP1"),
                StringType(length = 1000).toProgramParam("£JAXK1"),
                StringType(length = 1000).toProgramParam("£JAXD1"),
                StringType(length = 1000).toProgramParam("£JAXOP"),
                StringType(length = 10).toProgramParam("£JAXEN")
            )) { _, _ ->
                emptyList()
            }
            "£JAX_FIN0" -> DopedProgram(params = listOf()) { _, _ ->
                emptyList()
            }
            else -> super.findProgram(name)
        }
    }
}

fun main() {
    val program = "TST_PGMA"
    val srcDir: File? = null
    require(srcDir != null) { "srcDir is mandatory" }
    val `fun` = Fun(program = program)
    val param = SmeupObj(type = "", parameter = "", code = "Autunno")
    // val srcDir = File("C:\\dev\\java\\smeup\\kokos\\testinstall\\kokos1-dsl\\rpg")
    val compiledProgramsDir = File(System.getProperty("java.io.tmpdir"))
    val config = Configuration().apply { options = Options().apply { dumpSourceOnExecutionError = true } }
    compile(src = srcDir, compiledProgramsDir = compiledProgramsDir, force = true, configuration = config)
    val programFinders = listOf(DirRpgProgramFinder(File(System.getProperty("java.io.tmpdir"))))
    val systemInterface = MockSystemInterface()
    getProgram(program, systemInterface = systemInterface, programFinders = programFinders).singleCall(parmsProducer = {
            compilationUnit ->
        systemInterface.cu = compilationUnit
        val datastructFiels = `fun`.dsEntries() + param.dsEntries()
        mapOf("£UIBDS" to DataStructValue.createInstance(
            compilationUnit = compilationUnit, dataStructName = "£UIBDS", values = datastructFiels)
        )
    })
}
