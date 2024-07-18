package com.jariko.dspf.sdk

import com.smeup.dspfparser.linesclassifier.DSPFField
import com.smeup.rpgparser.interpreter.OnExfmtResponse
import com.smeup.rpgparser.interpreter.RuntimeInterpreterSnapshot
import com.smeup.rpgparser.interpreter.Value
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException

class ClientHandler(
    val id: String,
    var reader: BufferedReader,
    var writer: BufferedWriter,
    val onProgramEnd: (clientHandler: ClientHandler) -> Unit = { }
) : Runnable {

    // TODO implement a thread pool by using Executors.
    private val jarikoThread = Thread(this)
    private val monitor = Object()

    init {
        println("ClientHandler created")
        jarikoThread.start()
    }

    private fun onExfmt(fields: List<DSPFField>, snapshot: RuntimeInterpreterSnapshot): OnExfmtResponse? {
        send(fields)
        // TODO check what happens on resumed thread
        val values = receive()
        return OnExfmtResponse(snapshot, values)
    }

    private fun askForProgramSource(): String {
        try {
            return read(reader)
        } catch (e: IOException) {
            sleep()
            return askForProgramSource()
        }
    }

    private fun send(fields: List<DSPFField>) {
        try {
            write(writer, json.encodeToString<List<DSPFField>>(fields))
        } catch (e: IOException) {
            sleep()
            send(fields)
        } catch (e: SerializationException) {
            sleep()
            send(fields)
        }
    }

    private fun receive(): Map<String, Value> {
        try {
            return json.decodeFromString<Map<String, Value>>(read(reader))
        } catch (e: IOException) {
            sleep()
            // discard program source
            receive()
            return receive()
        } catch (e: SerializationException) {
            sleep()
            // discard program source
            receive()
            return receive()
        }
    }

    private fun sleep() {
        synchronized(monitor) {
            monitor.wait()
        }
    }

    override fun run() {
        val programSource = askForProgramSource()
        val setup = CLIProgramSetup(programSource, ::onExfmt)
        val (program, configuration) = setup.create()

        program.singleCall(emptyList(), configuration)
        onProgramEnd(this)
        jarikoThread.interrupt()
    }

    fun resume() {
        synchronized(monitor) {
            monitor.notify()
        }
    }
}