package com.jariko.dspf.sdk

import com.smeup.dspfparser.linesclassifier.DSPFField
import com.smeup.rpgparser.interpreter.Value
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.BufferedReader
import java.io.BufferedWriter
import java.net.Socket

private enum class RemoteProgramState {
    EX_NOVO,
    RESUME,
    RESTART
}

class RemoteProgramCaller(
    private val id: String,
    private val ip: String,
    private val port: Int,
    private val programSource: String
) {
    private var server: Socket? = null
    private val reader: BufferedReader?
        get() = server?.getInputStream()?.bufferedReader()
    private val writer: BufferedWriter?
        get() = server?.getOutputStream()?.bufferedWriter()
    private var state: RemoteProgramState = RemoteProgramState.EX_NOVO

    fun resume() {
        state = RemoteProgramState.RESUME
        call()
    }

    fun restart() {
        state = RemoteProgramState.RESTART
        call()
    }

    private fun tellId() {
        write(writer!!, id)
    }

    private fun tellProgramSource() {
        write(writer!!, programSource)
    }

    private fun send(values: Map<String, Value>) {
       write(writer!!, json.encodeToString<Map<String, Value>>(values))
    }

    private fun receive(): List<DSPFField> {
        return json.decodeFromString<List<DSPFField>>(read(reader!!))
    }

    fun call() {
        try {
            server = Socket(ip, port)
            println("Connected")

            tellId()
//            tellProgramSource()
//            write(writer!!, "STOP")
            if (state == RemoteProgramState.RESTART) {
                write(writer!!, "STOP")
                throw ProgramTerminationException()
            }
            if (state == RemoteProgramState.EX_NOVO) tellProgramSource()
            if (state == RemoteProgramState.RESUME) send(emptyMap())

            while(true) {
                val fields = receive()
                val values = startVideoSession(fields)
                send(values)
            }
        } catch (e: ProgramTerminationException) {
            if (state == RemoteProgramState.RESTART) {
                state = RemoteProgramState.EX_NOVO
                call()
            }
            println(e.message)
        } finally {
            server?.close()
            println("Disconnected")
        }
    }
}

fun main(args: Array<String>) {
    val id = try { args[0] } catch (e: ArrayIndexOutOfBoundsException) { "JohnDoe" }
    val ip = "localhost"
    val port = 5170
    val programSource = try { args[1] } catch (e: ArrayIndexOutOfBoundsException) { "add01.rpgle" }

    val program = RemoteProgramCaller(id, ip, port, programSource)

    try {
        when (args[2]) {
            "--resume" -> program.resume()
            "--restart" -> program.restart()
            else -> throw IllegalArgumentException()
        }
    } catch (e: ArrayIndexOutOfBoundsException) {
        program.call()
    }
}