package com.smeup.ftp

import java.io.Console
import java.io.File
import java.io.InputStream
import java.util.*


data class AS400SourceName(val library: String, val file: String, val member: String) {
    fun toAS400Path() = "${library}/${file}.${member}"
    fun generateLocalPathIn(destinationDir: String) = withSlash(destinationDir) + member + ".rpgle"
    private fun withSlash(destinationDir: String) = if (destinationDir.endsWith("/")) destinationDir else destinationDir + "/"
}


fun getSourceFromAS400 (server: String, user: String, password: String, source: AS400SourceName, destinationDir: String): Unit {
    val ftp = SimpleFtpClient(server, 21, user, password)

    ftp.open()

    ftp.downloadFile(source.toAS400Path(), source.generateLocalPathIn(destinationDir))

    ftp.close()
}


fun main(args: Array<String>) {
    val configuration = Configuration("ftp.properties", ConfigKeys.values())

    val source = AS400SourceName(configuration.get(ConfigKeys.LIBRARY), configuration.get(ConfigKeys.FILE) , configuration.get(ConfigKeys.MEMBER))
    getSourceFromAS400(configuration.get(ConfigKeys.SERVER), configuration.get(ConfigKeys.USER), configuration.get(ConfigKeys.PASSWORD), source, configuration.get(ConfigKeys.DESTINATION_DIRECTORY))
}

enum class ConfigKeys {
    SERVER, USER, PASSWORD, LIBRARY, FILE, MEMBER, DESTINATION_DIRECTORY
}

class Configuration <T : Enum<T>>(propertiesFileName: String, keys: Array<T>, inStream: InputStream = System.`in`) {
    private val properties = Properties()
    private val console = Scanner(inStream)

    init {
        val file = File(System.getProperty("user.dir"), propertiesFileName)
        if (file.exists()) {
            println("Reading configuration from ${file}")
            properties.load(file.inputStream())
        }
    }

    fun get(key: T): String  {
        val stringKey = asString(key)
        val value = properties.getProperty(stringKey)
        return value ?: readFromConsole(stringKey) ?: ""
    }

    private fun readFromConsole(stringKey: String) : String? {
        println("Enter ${stringKey}: ")
        return console.nextLine()
    }

    private fun asString(key: T) = key.toString().toLowerCase()
}