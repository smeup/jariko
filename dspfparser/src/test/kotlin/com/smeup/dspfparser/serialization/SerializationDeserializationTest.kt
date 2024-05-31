package com.smeup.dspfparser.serialization

import com.smeup.dspfparser.linesclassifier.DSPFSpecifications
import com.smeup.dspfparser.linesclassifier.DSPFSpecificationsLoader
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.Test

internal class SerializationDeserializationTest : DSPFSpecificationsLoader("./src/test/resources/_d2.dspf") {
    @Test
    fun serialize() {
        assertDoesNotThrow {
            val prettyJson = Json { prettyPrint = true }
            val json = prettyJson.encodeToString(this.specifications)
            prettyJson.decodeFromString<DSPFSpecifications>(json)
        }
    }
}