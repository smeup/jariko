package com.smeup.rpgparser.interpreter

import com.smeup.dbnative.file.DBFile
import com.smeup.dbnative.manager.DBFileFactory
import com.smeup.dbnative.manager.findConnectionConfigFor

import java.util.*

class DBFileMap() {
    private val byFileName =
        TreeMap<String, DBFile>(String.CASE_INSENSITIVE_ORDER)
    private val byFormatName =
        TreeMap<String, DBFile>(String.CASE_INSENSITIVE_ORDER)

    fun add(fileDefinition: FileDefinition) {

        if (byFileName.containsKey(fileDefinition) == false) {

            //Get DBFile from Reload

            val dbFile = DBFileFactory(TODO()).open(fileDefinition.name, null)

            byFileName[fileDefinition.name] = dbFile
            val formatName = fileDefinition.internalFormatName
            if (formatName != null && !fileDefinition.name.equals(formatName, ignoreCase = true)) {
                byFormatName[formatName] = dbFile
            }
        }
    }

    operator fun get(nameOrFormat: String): DBFile? = byFileName[nameOrFormat] ?: byFormatName[nameOrFormat]
}
