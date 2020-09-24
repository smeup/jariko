package com.smeup.rpgparser.experimental

import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.interpreter.serialization.SerializationOption
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

fun MemorySliceId.fileName(ext: String = "properties") = "${this.activationGroup}.${this.programName}.$ext"

class PropertiesFileStorage(private val dir: File) : IMemorySliceStorage {

    // Choose of binary serialization
    private val serializer = SerializationOption.stringSerializer

    override fun open() {
    }

    private fun loadProperties(memorySliceId: MemorySliceId): Map<String, String> {
        val properties = Properties()
        val file = File(dir, memorySliceId.fileName())
        if (file.exists()) {
            FileInputStream(file).use {
                properties.load(it)
            }
        }
        return properties.map {
            it.key as String to it.value as String
        }.toMap()
    }

    private fun storeProperties(memorySliceId: MemorySliceId, properties: Properties) {
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val file = File(dir, memorySliceId.fileName())
        FileOutputStream(file).use {
            properties.store(it, "")
        }
    }

    override fun load(memorySliceId: MemorySliceId): Map<String, Value> {
        val properties = loadProperties(memorySliceId)
        return properties.mapValues {
            serializer.decodeFromString<Value>(it.value)
        }
    }

    override fun store(memorySliceId: MemorySliceId, values: Map<String, Value>) {
        val properties = Properties()
        values.forEach() {
            properties.setProperty(it.key, serializer.encodeToString(it.value))
        }
        storeProperties(memorySliceId, properties)
    }

    override fun beginTrans() {
    }

    override fun commitTrans() {
    }

    override fun rollbackTrans() {
    }

    override fun close() {
    }

    fun dumpPropertiesFile() {
        dir.listFiles()?.forEach {
            println("** ${it.name}")
            println(it.readText())
            println("*************************************************************************************************")
            println()
        }
    }
}