package com.smeup.dspfparser.serialization

import com.smeup.dspfparser.linesclassifier.DSPF
import com.smeup.dspfparser.linesclassifier.DSPFSpecificationsLoader
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

internal class SerializationDeserializationTest : DSPFSpecificationsLoader("./src/test/resources/RECORDS.dspf") {
    private val json = Json { prettyPrint = true }

    @Test
    fun serialize() {
        val initial = this.specifications as DSPF
        val string = json.encodeToString(initial)
        val deserialized = json.decodeFromString<DSPF>(string)
        assertEquals(initial, deserialized)
    }
}