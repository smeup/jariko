package com.jariko.dspf.sdk

import com.smeup.dspfparser.linesclassifier.DSPFField
import com.smeup.rpgparser.interpreter.Value
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.BufferedReader
import java.io.BufferedWriter
import java.net.Socket
import java.net.SocketException
import java.util.*
import kotlin.jvm.Throws

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

    private fun tellId() {
        write(writer!!, id)
    }

    private fun tellProgramSource() {
        write(writer!!, programSource)
    }

    private fun send(values: Map<String, Value>) {
       write(writer!!, json.encodeToString<Map<String, Value>>(values))
    }

    private fun receive(): List<DSPFField>{
        return json.decodeFromString<List<DSPFField>>(read(reader!!))
    }

    @Throws(SocketException::class)
    fun call() {
        try {
            server = Socket(ip, port)
            println("Connected")

            tellId()
            tellProgramSource()

            while(true) {
                val fields = receive()
                val values = startVideoSession(fields)
                send(values)
            }
        } catch (e: SerializationException) {
            println("Program ended")
        } finally {
            close()
        }
    }

    private fun close() {
        server?.close()
        println("Disconnected")
    }
}

fun main(args: Array<String>) {
    val id = try { args[0] } catch (e: ArrayIndexOutOfBoundsException) { Date().toString() }
    val ip = "localhost"
    val port = 5170
    val programSource = try { args[1] } catch (e: ArrayIndexOutOfBoundsException) { "add01.rpgle" }
    val program = RemoteProgramCaller(id, ip, port, programSource)
    program.call()
}