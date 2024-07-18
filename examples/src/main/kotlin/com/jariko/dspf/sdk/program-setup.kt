package com.jariko.dspf.sdk

import com.smeup.dspfparser.linesclassifier.DSPFField
import com.smeup.rpgparser.execution.CommandLineProgram
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.DspfConfig
import com.smeup.rpgparser.execution.JarikoCallback
import com.smeup.rpgparser.execution.SimpleDspfConfig
import com.smeup.rpgparser.execution.getProgram
import com.smeup.rpgparser.interpreter.OnExfmtResponse
import com.smeup.rpgparser.interpreter.RuntimeInterpreterSnapshot
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import java.io.File

private var isRunAsJar = false

fun detectRuntime(args: Array<String>) {
    isRunAsJar = args.isNotEmpty()
    println("Running as ${if (isRunAsJar) "jar" else "source"}")
}

class CLIProgramSetup(
    private val programSource: String,
    private var onExfmt: (
        fields: List<DSPFField>,
        snapshot: RuntimeInterpreterSnapshot) -> OnExfmtResponse? = { _, _ -> null }
) {
    private fun createDspfConfig(): DspfConfig {
        val simpleDspfConfig = if (isRunAsJar) {
            SimpleDspfConfig(".")
        } else {
            SimpleDspfConfig({ }.javaClass.getResource("/metadata")!!.path)
        }
        return DspfConfig(
            metadataProducer = simpleDspfConfig::getMetadata,
            dspfProducer = simpleDspfConfig::dspfProducer
        )
    }

    private fun createProgramFinders(): List<DirRpgProgramFinder> {
        return if (isRunAsJar) {
            listOf(DirRpgProgramFinder(File(".")))
        } else {
            listOf(DirRpgProgramFinder(File({ }.javaClass.getResource("/rpg")!!.path)))
        }
    }

    private fun createJarikoCallback(): JarikoCallback {
        val jarikoCallback = JarikoCallback()
        jarikoCallback.onExfmt = onExfmt
        return jarikoCallback
    }

    private fun createConfig(): Configuration {
        return Configuration(
            dspfConfig = createDspfConfig(),
            jarikoCallback = createJarikoCallback()
        )
    }

    fun create(): Pair<CommandLineProgram, Configuration> {
        val programSource = this.programSource
        val programFinders = this.createProgramFinders()
        val program = getProgram(programSource, programFinders = programFinders)
        val configuration = this.createConfig()

        return Pair(program, configuration)
    }
}