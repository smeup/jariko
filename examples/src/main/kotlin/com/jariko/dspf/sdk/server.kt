package com.jariko.dspf.sdk

import java.io.IOException
import java.net.ServerSocket
import java.net.Socket

class Server(private val port: Int) {

    private val serverSocket = ServerSocket(port)
    private val clients = mutableListOf<ClientHandler>()
    private var alive = true

    private fun getClientHandlerById(id: String): ClientHandler? {
        return clients.find { it.id == id }
    }

    private fun onProgramEnd(client: Socket): (clientHandler: ClientHandler) -> Unit {
        return {
            try {
                clients.remove(it)
                client.close()
            } catch (e: IOException) {
                stop()
            }
        }
    }

    fun start() {
        while (alive) {
            println("Server listening on port $port...")
            val client = serverSocket.accept()
            val reader = client.getInputStream().bufferedReader(charset = Charsets.UTF_8)
            val writer = client.getOutputStream().bufferedWriter(charset = Charsets.UTF_8)
            val id = read(reader)

            println("Client sent id: $id")
            val clientHandler = getClientHandlerById(id)?.apply {
                this.reader = reader
                this.writer = writer
                println("Resuming client: ${this.id}")
                this.resume()
            } ?: ClientHandler(id, reader, writer, onProgramEnd(client)).apply {
                println("Creating client: ${this.id}")
                clients.add(this)
            }

            println("Client connected: ${clientHandler.id}")
        }
    }

    fun stop() {
        alive = false
        serverSocket.close()
    }
}

fun main(args: Array<String>) {
    detectRuntime(args)
    val server = Server(5170)
    server.start()
}