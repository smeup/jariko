package com.jariko.dspf.sdk

import com.smeup.dspfparser.linesclassifier.DSPFField
import com.smeup.rpgparser.interpreter.Value
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.net.Socket

private enum class RemoteProgramState {
    EX_NOVO,
    RESUME
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
        val message = read(reader!!)
        when (message) {
            "PROGRAM_END" -> throw ProgramTerminationException()
        }
        return json.decodeFromString<List<DSPFField>>(message)
    }

    fun resume() {
        state = RemoteProgramState.RESUME
        call()
    }

    fun call() {
        try {
            server = Socket(ip, port)
            println("Connected")

            tellId()
            when (state) {
                RemoteProgramState.EX_NOVO -> {
                    tellProgramSource()
                }
                RemoteProgramState.RESUME -> {
                    send(emptyMap())
                }
            }

            while(true) {
                val fields = receive()
                val values = startVideoSession(fields)
                send(values)
            }
        } catch (e: ProgramTerminationException) {
            println(e.message)
        } catch(e: IOException) {
            println("Server connection error")
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
            else -> throw IllegalArgumentException()
        }
    } catch (e: ArrayIndexOutOfBoundsException) {
        program.call()
    }
}