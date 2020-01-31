package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.CollectorSystemInterface
import com.smeup.rpgparser.MockDBFile
import com.smeup.rpgparser.assertASTCanBeProduced
import com.smeup.rpgparser.execute
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import org.junit.Test
import kotlin.test.assertEquals

class DBTest {
    @Test
    fun executeCHAIN2KEYS() {
        val keysForTest = listOf("toFind1" to StringValue("ABC"), "toFind2" to StringValue("2"))
        val someDescription = StringValue("Goofy")

        val cu = assertASTCanBeProduced("db/CHAIN2KEYS")

        val f1 = DBField("KY1TST", StringType(5, false))
        val f2 = DBField("KY2TST", NumberType(2, 0))
        val f3 = DBField("DESTST", StringType(40, false))

        val mockDBInterface: DBInterface = object : DBInterface {
            override fun open(name: String): DBFile? = object : MockDBFile() {
                override fun chain(key: Value): Record = Record()
                override fun chain(keys: List<RecordField>): Record =
                    Record(RecordField("DESTST", someDescription))
            }

            override fun metadataOf(name: String): FileMetadata? = FileMetadata(name, name, listOf(f1, f2, f3))
        }

        cu.resolveAndValidate(mockDBInterface)

        val si = CollectorSystemInterface()
        si.databaseInterface = mockDBInterface

        execute(cu, keysForTest.toMap(), si)
        assertEquals(listOf("Found: ${someDescription.value}"), si.displayed)
    }

    @Test
    fun executeCHAINREADE() {
        val cu = assertASTCanBeProduced("db/CHAINREADE")

        val first = DBField("FIRSTNME", StringType(40, false))
        val last = DBField("LASTNAME", StringType(40, false))
        val mockDBInterface: DBInterface = object : DBInterface {
            var nrOfCallToEoF = 0
            override fun metadataOf(name: String): FileMetadata? = FileMetadata(name, name, listOf(first, last))
            override fun open(name: String): DBFile? = object : MockDBFile() {
                override fun chain(key: Value): Record =
                    Record(RecordField("FIRSTNME", StringValue("Giovanni")), RecordField("LASTNAME", StringValue("Boccaccio")))
                override fun readEqual(): Record =
                    Record(RecordField("FIRSTNME", StringValue("Cecco")), RecordField("LASTNAME", StringValue("Angiolieri")))
                override fun eof(): Boolean {
                    nrOfCallToEoF++
                    return nrOfCallToEoF > 1
                }
            }
        }

        cu.resolveAndValidate(mockDBInterface)
        val si = CollectorSystemInterface()
        si.databaseInterface = mockDBInterface

        execute(cu, mapOf(), si)
        assertEquals(listOf("Giovanni Boccaccio", "Cecco Angiolieri"), si.displayed)
    }
}
