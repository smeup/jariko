package com.smeup.rpgparser.interpreter

import java.util.*

class DBFileMap(private val dbInterface: DBInterface) {
    private val byFileName =
        TreeMap<String, DBFile>(String.CASE_INSENSITIVE_ORDER)
    private val byFormatName =
        TreeMap<String, DBFile>(String.CASE_INSENSITIVE_ORDER)

    fun add(fileDefinition: FileDefinition) {
        val dbFile = dbInterface.open(fileDefinition.name)
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
