package com.smeup.dspfparser.serialization

import com.smeup.dspfparser.linesclassifier.ConstantValue
import com.smeup.dspfparser.linesclassifier.DSPF
import com.smeup.dspfparser.linesclassifier.DSPFSpecificationsLoader
import com.smeup.dspfparser.linesclassifier.DSPFValue
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlin.test.Test
import kotlin.test.assertEquals

private val module = SerializersModule {
    polymorphic(DSPFValue::class) {
        subclass(ConstantValue::class)
    }
}

private val json = Json {
    prettyPrint = true
    serializersModule = module
}

internal class SerializationDeserializationTest : DSPFSpecificationsLoader("./src/test/resources/RECORDS.dspf") {
    @Test
    fun serialize() {
        val initial = this.specifications as DSPF
        val string = json.encodeToString(initial)
        val deserialized = json.decodeFromString<DSPF>(string)
        assertEquals(initial, deserialized)
    }
}