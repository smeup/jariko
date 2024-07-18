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
    private val jarikoThread: Thread = Thread(this)
    private val monitor = Object()

    init {
        println("ClientHandler created")
        jarikoThread.start()
    }

    private fun onExfmt(fields: List<DSPFField>, snapshot: RuntimeInterpreterSnapshot): OnExfmtResponse? {
        send(fields)
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
        }
    }

    private fun receive(): Map<String, Value> {
        try {
            val message = read(reader)
            when (message) {
                "STOP" -> throw ProgramTerminationException()
            }
//            read(reader)
            return json.decodeFromString<Map<String, Value>>(message)
        } catch (e: IOException) {
            sleep()
            println("Awake...")
            return receive()
        }
//        catch (e: SerializationException) {
//            return receive()
//        }
    }

    private fun sleep() {
        synchronized(monitor) {
            println("putting thread to sleep")
            monitor.wait()
        }
    }

    override fun run() {
        try {
            val programSource = askForProgramSource()
            val setup = CLIProgramSetup(programSource, ::onExfmt)
            val (program, configuration) = setup.create()

            program.singleCall(emptyList(), configuration)
        } catch (_: ProgramTerminationException) {

        } finally {
            onProgramEnd(this)
            jarikoThread.interrupt()
        }
    }

    fun resume() {
        synchronized(monitor) {
            println("waking up thread")
            monitor.notify()
        }
    }
}