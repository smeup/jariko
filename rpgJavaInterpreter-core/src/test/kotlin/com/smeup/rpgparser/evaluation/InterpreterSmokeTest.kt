package com.smeup.rpgparser.evaluation

import com.smeup.rpgparser.*
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.parsetreetoast.resolve
import org.junit.Test

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

        val mockDBInterface: DBInterface = object : DBInterface {
            val hostField = DBField("HOSTNME1", StringType(255))

            override fun metadataOf(name: String): FileMetadata? = FileMetadata(name, "qhosts", listOf(hostField))

            override fun chain(name: String, key: Value): Collection<Pair<DBField, Value>>? {
                return if (name.equals("qhosts", ignoreCase = true)) {
                    listOf(hostField to StringValue("loopback"))
                } else {
                    null
                }
            }
        }

        cu.resolve(mockDBInterface)
        execute(cu, mapOf("ipToFind" to StringValue("127.0.0.1")))
    }

    @Test
    fun executeCHAIN2FILE() {
        val cu = assertASTCanBeProduced("CHAIN2FILE")

        val mockDBInterface: DBInterface = object : DBInterface {
            val hostField = DBField("DESTST", StringType(40))

            override fun metadataOf(name: String): FileMetadata? = FileMetadata(name, name, listOf(hostField))

            override fun chain(name: String, key: Value): Collection<Pair<DBField, Value>>? {
                return null
            }
        }

        cu.resolve(mockDBInterface)
        execute(cu, mapOf())
    }
}
