package com.smeup.dspfparser.serialization

import com.smeup.dspfparser.linesclassifier.DSPFSpecifications
import com.smeup.dspfparser.linesclassifier.DSPFSpecificationsLoader
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

internal class SerializationDeserializationTest : DSPFSpecificationsLoader("./src/test/resources/D2.dspf") {
    private val json = Json { prettyPrint = true }

    @Test
    fun serialize() {
        val initial = this.specifications
        val string = json.encodeToString(initial)
        val deserialized = json.decodeFromString<DSPFSpecifications>(string)
        assertEquals(initial, deserialized)
    }
}