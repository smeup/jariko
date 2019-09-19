package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.*
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.logging.*
import com.smeup.rpgparser.parsing.parsetreetoast.resolve
import org.junit.Ignore
import org.junit.Test
import java.lang.RuntimeException

class InterpreterSmokeTest {

    @Test
    fun executeJD_001() {
        val cu = assertASTCanBeProduced("JD_001", true)
        cu.resolve()
        execute(cu, mapOf())
        // We need JD_URL in Kotlin. We want it to print its third parameter
    }

    @Test
    fun executeJD_002() {
        val cu = assertASTCanBeProduced("JD_002")
        cu.resolve()
        execute(cu, mapOf())
    }

    @Test
    fun executeJD_003() {
        val cu = assertASTCanBeProduced("JD_003")
        cu.resolve()
        execute(cu, mapOf())
    }

    @Test
    fun executeCHAINHOSTS() {
        val cu = assertASTCanBeProduced("CHAINHOSTS")

        val hostField = DBField("HOSTNME1", StringType(255))
        val mockDBInterface: DBInterface = object : DBInterface {
            override fun metadataOf(name: String): FileMetadata? = FileMetadata(name, "qhosts", listOf(hostField))
            override fun open(name: String): DBFile? = object : DBFile {
                override fun eof(): Boolean = TODO("not implemented")
                override fun chain(keys: List<Pair<String, Value>>): List<Pair<String, Value>> = TODO()
                override fun chain(key: Value): List<Pair<String, Value>> =
                    if (name.equals("qhosts", ignoreCase = true)) {
                        listOf(hostField.name to StringValue("loopback"))
                    } else {
                        emptyList()
                    }
            }
        }

        cu.resolve(mockDBInterface)
        val si = CollectorSystemInterface(consoleLoggingConfiguration(STATEMENT_LOGGER, EXPRESSION_LOGGER))
        si.databaseInterface = mockDBInterface
        execute(cu, mapOf("ipToFind" to StringValue("127.0.0.1")), si)
    }

    @Test
    fun executeCHAIN2FILE() {
        val cu = assertASTCanBeProduced("CHAIN2FILE")

        val hostField = DBField("DESTST", StringType(40))
        val mockDBInterface: DBInterface = object : DBInterface {
            override fun metadataOf(name: String): FileMetadata? = FileMetadata(name, name, listOf(hostField))
            override fun open(name: String): DBFile? = object : DBFile {
                override fun eof(): Boolean = TODO("not implemented")
                override fun chain(key: Value): List<Pair<String, Value>> = emptyList()
                override fun chain(keys: List<Pair<String, Value>>): List<Pair<String, Value>> =
                    throw RuntimeException("Should not get here")
            }
        }

        cu.resolve(mockDBInterface)
        val si = CollectorSystemInterface(consoleLoggingConfiguration(STATEMENT_LOGGER, EXPRESSION_LOGGER))
        si.databaseInterface = mockDBInterface
        execute(cu, mapOf(), si)
    }

    @Test @Ignore
    fun executeCHAINREADE() {
        val cu = assertASTCanBeProduced("CHAINREADE")

        val first = DBField("FIRSTNME", StringType(40))
        val last = DBField("LASTNAME", StringType(40))
        val mockDBInterface: DBInterface = object : DBInterface {
            override fun metadataOf(name: String): FileMetadata? = FileMetadata(name, name, listOf(first, last))
            override fun open(name: String): DBFile? = object : DBFile {
                override fun eof(): Boolean = TODO("not implemented")
                override fun chain(key: Value): List<Pair<String, Value>> =
                    listOf("FIRSTNME" to StringValue("Giovanni"), "LASTNAME" to StringValue("Boccaccio"))

                override fun chain(keys: List<Pair<String, Value>>): List<Pair<String, Value>> =
                    throw RuntimeException("Should not get here")
            }
        }

        cu.resolve(mockDBInterface)
        val si = CollectorSystemInterface(consoleLoggingConfiguration(STATEMENT_LOGGER, EXPRESSION_LOGGER))
        si.databaseInterface = mockDBInterface

        execute(cu, mapOf(), si)
    }
}