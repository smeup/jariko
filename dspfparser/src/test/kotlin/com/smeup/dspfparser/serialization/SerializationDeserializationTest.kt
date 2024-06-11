package com.smeup.dspfparser.serialization

import com.smeup.dspfparser.linesclassifier.DSPF
import com.smeup.dspfparser.linesclassifier.DSPFSpecifications
import com.smeup.dspfparser.linesclassifier.DSPFSpecificationsLoader
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

internal class SerializationDeserializationTest : DSPFSpecificationsLoader("./src/test/resources/D2.dspf") {
    // should ignore "type" key that is produced from serialization of DSPF interface
    private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }

    @Test
    fun serialize() {
        val initial = this.specifications
        val string = json.encodeToString(initial as DSPF)
        val deserialized = json.decodeFromString<DSPFSpecifications>(string) as DSPF
        assertEquals(initial, deserialized)
    }
}