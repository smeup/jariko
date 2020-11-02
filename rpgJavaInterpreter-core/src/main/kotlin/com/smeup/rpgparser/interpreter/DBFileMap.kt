package com.smeup.rpgparser.interpreter

import com.smeup.dbnative.file.DBFile

import java.util.*

class DBFileMap(private val dbFile: DBFile) {
    private val byFileName =
        TreeMap<String, DBFile>(String.CASE_INSENSITIVE_ORDER)
    private val byFormatName =
        TreeMap<String, DBFile>(String.CASE_INSENSITIVE_ORDER)

    fun add(fileDefinition: FileDefinition) {

        val dbFile = TODO("Insert DBFile open")

        require(dbFile != null) {
            "Cannot open ${fileDefinition.name}"
        }
        byFileName[fileDefinition.name] = dbFile
        val formatName = fileDefinition.internalFormatName
        if (formatName != null && !fileDefinition.name.equals(formatName, ignoreCase = true)) {
            byFormatName[formatName] = dbFile
        }
    }

    operator fun get(nameOrFormat: String): DBFile? = byFileName[nameOrFormat] ?: byFormatName[nameOrFormat]
}
