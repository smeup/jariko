package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.*
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.logging.*
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import org.junit.Test

class InterpreterSmokeTest {

    @Test
    fun executeJD_001() {
        val cu = assertASTCanBeProduced("JD_001", true)
        cu.resolveAndValidate(DummyDBInterface)
        execute(cu, mapOf())
        // We need JD_URL in Kotlin. We want it to print its third parameter
    }

    @Test
    fun executeJD_002() {
        val cu = assertASTCanBeProduced("JD_002")
        cu.resolveAndValidate(DummyDBInterface)
        execute(cu, mapOf())
    }

    @Test
    fun executeJD_003() {
        val cu = assertASTCanBeProduced("JD_003")
        cu.resolveAndValidate(DummyDBInterface)
        execute(cu, mapOf())
    }

    @Test
    fun executeMOVEA01() {
        val cu = assertASTCanBeProduced("MOVEA01")
        cu.resolveAndValidate(DummyDBInterface)
        execute(cu, mapOf())
    }

    @Test
    fun executeCHAINHOSTS() {
        val cu = assertASTCanBeProduced("db/CHAINHOSTS")

        val hostField = DBField("HOSTNME1", StringType(255, false))
        val mockDBInterface: DBInterface = object : DBInterface {
            override fun metadataOf(name: String): FileMetadata? = FileMetadata(name, "qhosts", listOf(hostField))
            override fun open(name: String): DBFile? = object : MockDBFile() {
                override fun chain(keys: List<RecordField>): Record = TODO()
                override fun chain(key: Value): Record =
                    if (name.equals("qhosts", ignoreCase = true)) {
                        Record(RecordField(hostField.name, StringValue("loopback")))
                    } else {
                        Record()
                    }
            }
        }

        cu.resolveAndValidate(mockDBInterface)
        val si = CollectorSystemInterface(consoleLoggingConfiguration(STATEMENT_LOGGER, EXPRESSION_LOGGER))
        si.databaseInterface = mockDBInterface
        execute(cu, mapOf("ipToFind" to StringValue("127.0.0.1")), si)
    }

    @Test
    fun executeCHAIN2FILE() {
        val cu = assertASTCanBeProduced("db/CHAIN2FILE")

        val hostField = DBField("DESTST", StringType(40, false))
        val mockDBInterface: DBInterface = object : DBInterface {
            override fun metadataOf(name: String): FileMetadata? = FileMetadata(name, name, listOf(hostField))
            override fun open(name: String): DBFile? = object : MockDBFile() {
                override fun chain(key: Value): Record = Record()
            }
        }

        cu.resolveAndValidate(mockDBInterface)
        val si = CollectorSystemInterface(consoleLoggingConfiguration(STATEMENT_LOGGER, EXPRESSION_LOGGER))
        si.databaseInterface = mockDBInterface
        execute(cu, mapOf(), si)
    }
}
