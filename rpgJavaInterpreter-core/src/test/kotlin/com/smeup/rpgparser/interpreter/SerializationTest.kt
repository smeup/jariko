package com.smeup.rpgparser.interpreter

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.*

class SerializationTest {
    @Test
    fun `values can be serialized to jSon`() {
        val numericValue: Long = 6969
        val intValue = IntValue(numericValue)
        val string = Json.encodeToString(intValue)

        println(string)
        assertTrue(string.contains("$numericValue"))

        val deserializedIntValue = Json.decodeFromString<IntValue>(string)
        assertEquals(intValue, deserializedIntValue)
    }
}
