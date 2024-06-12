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

package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.logging.ProgramUsageType
import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.parsing.facade.RpgParserFacade
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import java.io.InputStream
import kotlin.system.measureNanoTime
import kotlin.time.Duration.Companion.nanoseconds

data class ProgramParam(val name: String, val type: Type)

infix fun Type.parm(name: String): ProgramParam = ProgramParam(name, this)

interface Program {
    fun params(): List<ProgramParam>
    fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>): List<Value>
}

class RpgProgram(val cu: CompilationUnit, val name: String = "<UNNAMED RPG PROGRAM>") : Program {

    private var systemInterface: SystemInterface? = null

    private val interpreter: InternalInterpreter by lazy {
        val interpreterCore = InternalInterpreter(this.systemInterface!!)
        interpreterCore.getStatus().displayFiles = cu.displayFiles
        MainExecutionContext.getConfiguration().jarikoCallback.onInterpreterCreation(interpreterCore)
        interpreterCore
    }

    val intepreterCore: InterpreterCore by lazy {
        interpreter
    }

    lateinit var activationGroup: ActivationGroup
    private var initialized = false

    private val logHandlers: MutableList<InterpreterLogHandler> by lazy {
        systemInterface!!.getAllLogHandlers()
    }

    override fun params(): List<ProgramParam> {
        val plistParams = cu.entryPlist
        // TODO derive proper type from the data specification
        return plistParams?.params?.map {
            val type = cu.getAnyDataDefinition(it.param.name) {
                "Cannot resolve PARAM: ${it.param.name} in *ENTRY PLIST of the program: $name"
            }.type
            ProgramParam(it.param.name, type)
        }
            ?: emptyList()
    }

    init {
        cu.resolveAndValidate()
    }

    companion object {
        fun fromInputStream(inputStream: InputStream, name: String = "<UNNAMED INPUT STREAM>", sourceProgram: SourceProgram? = SourceProgram.RPGLE): RpgProgram {
            inputStream.use {
                val cu = RpgParserFacade().parseAndProduceAst(inputStream, sourceProgram)
                return RpgProgram(cu, name)
            }
        }
    }

    override fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>): List<Value> {
        val expectedKeys = params().asSequence().map { it.name }.toSet()

        if (expectedKeys.size <= params.size) {
            require(params.keys.toSet() == params().asSequence().map { it.name }.toSet()) {
                "Expected params: ${params().asSequence().map { it.name }.joinToString(", ")}"
            }
        } else {
            require(params().asSequence().map { it.name }.toSet().all { it in expectedKeys }) {
                "Expected params: ${params().asSequence().map { it.name }.joinToString(", ")}"
            }

            // Set not passed params to NullValue
            params().forEach {
                if (it.name !in params.keys) {
                    params[it.name] = NullValue
                }
            }
        }
        this.systemInterface = systemInterface
        val logSource = { LogSourceData.fromProgram(name) }
        logHandlers.renderLog(LazyLogEntry.produceStatement(logSource, "INTERPRETATION", "START"))
        val changedInitialValues: List<Value>
        val elapsed = measureNanoTime {
            interpreter.setInterpretationContext(object : InterpretationContext {
                private var iDataWrapUpChoice: DataWrapUpChoice? = null
                override val currentProgramName: String
                    get() = name
                override fun shouldReinitialize() = false
                override var dataWrapUpChoice: DataWrapUpChoice?
                    get() = iDataWrapUpChoice
                    set(value) {
                        iDataWrapUpChoice = value
                    }
            })

            for (pv in params) {
                val expectedType = params().find { it.name == pv.key }!!.type
                val coercedValue = coerce(pv.value, expectedType)
                require(coercedValue.assignableTo(expectedType)) {
                    "param ${pv.key} was expected to have type $expectedType. It has value: $coercedValue"
                }
            }
            if (!initialized) {
                initialized = true
                val caller = if (MainExecutionContext.getProgramStack().isNotEmpty()) {
                    MainExecutionContext.getProgramStack().peek()
                } else {
                    null
                }
                val activationGroupType = cu.activationGroupType() ?: when (caller) {

                        // for main program, which does not have a caller, activation group is fixed by config
                        null -> NamedActivationGroup(MainExecutionContext.getConfiguration().defaultActivationGroupName)
                        else -> {
                            CallerActivationGroup
                        }
                    }
                activationGroupType.let {
                    activationGroup = ActivationGroup(it, it.assignedName(this, caller))
                }
            }
            MainExecutionContext.getProgramStack().push(this)
            MainExecutionContext.getConfiguration().jarikoCallback.onEnterPgm(name, interpreter.getGlobalSymbolTable())
            // set reinitialization to false because symboltable cleaning currently is handled directly
            // in internal interpreter before exit
            // todo i don't know whether parameter reinitialization has still sense
            interpreter.execute(this.cu, params, false)
            MainExecutionContext.getConfiguration().jarikoCallback.onExitPgm(name, interpreter.getGlobalSymbolTable(), null)
            params.keys.forEach { params[it] = interpreter[it] }
            changedInitialValues = params().map { interpreter[it.name] }
            // here clear symbol table if needed
            interpreter.doSomethingAfterExecution()
            MainExecutionContext.getProgramStack().pop()
        }.nanoseconds
        if (MainExecutionContext.isLoggingEnabled) {
            logHandlers.renderLog(LazyLogEntry.produceStatement(logSource, "INTERPRETATION", "END"))
            logHandlers.renderLog(LazyLogEntry.producePerformanceAndUpdateAnalytics(logSource, ProgramUsageType.Interpretation, "INTERPRETATION", elapsed))
        }
        if (MainExecutionContext.getProgramStack().isEmpty()) {
            interpreter.onInterpretationEnd()
        }

        return changedInitialValues
    }

    override fun equals(other: Any?) =
            (other is RpgProgram) && other.name == name

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

/**
 * Model activation group concept.
 * @param type activation group type as from grammar
 * @param assignedName Name assigned. The assignment algorithm depends on activation group type
 * @see ActivationGroupType.assignedName
 * */
data class ActivationGroup(val type: ActivationGroupType, val assignedName: String)