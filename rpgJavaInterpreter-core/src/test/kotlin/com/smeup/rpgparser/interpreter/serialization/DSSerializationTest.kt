package com.smeup.rpgparser.interpreter.serialization

import com.smeup.rpgparser.interpreter.DataStructValue
import com.smeup.rpgparser.interpreter.Value
import kotlin.test.*
import kotlinx.serialization.decodeFromString

class DSSerializationTest {

    @Test
    fun `Json DS serialization with class discriminator`() {
        val stringSerializer =
            SerializationOption.getSerializer(false)
        val serializedDS = """
            {"$CLASS_DISCRIMINATOR_TAG":"com.smeup.rpgparser.interpreter.DataStructValue", "value":"JamesBond   7","optionalExternalLen":null}
        """.trimIndent()
        val dsValue = stringSerializer.decodeFromString<Value>(serializedDS)
        assertTrue(dsValue is DataStructValue)
    }
}
