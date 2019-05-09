package com.smeup.ftp

import org.apache.commons.net.PrintCommandListener
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPReply
import java.io.*


class SimpleFtpClient(private val server: String, private val port: Int, private val user: String, private val password: String) {
    private var ftp: FTPClient? = null

    @Throws(IOException::class)
    fun open() {
        ftp = FTPClient()

        ftp!!.addProtocolCommandListener(PrintCommandListener(PrintWriter(System.out), true))

        ftp!!.connect(server, port)
        val reply = ftp!!.replyCode
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp!!.disconnect()
            throw IOException("Exception in connecting to FTP Server")
        }

        ftp!!.login(user, password)
    }

    @Throws(IOException::class)
    fun close() {
        ftp!!.disconnect()
    }

    @Throws(IOException::class)
    fun putFileToPath(file: File, path: String) {
        ftp!!.storeFile(path, FileInputStream(file))
    }

    @Throws(IOException::class)
    fun downloadFile(source: String, destination: String) {
        val baos = ByteArrayOutputStream()
        ftp!!.retrieveFile(source, baos)
        val asciiString = baos.toString("ISO-8859-1")
        File(destination).writeBytes(asciiString.toByteArray(Charsets.UTF_8))
    }
}