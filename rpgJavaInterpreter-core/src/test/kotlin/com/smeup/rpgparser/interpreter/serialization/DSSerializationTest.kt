/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
            {"$CLASS_DISCRIMINATOR_TAG":"com.smeup.rpgparser.interpreter.DataStructValue", "value":{"#class":"com.smeup.rpgparser.interpreter.IndexedStringBuilder","value":"JamesBond   7","chunksSize":10},"optionalExternalLen":null}
        """.trimIndent()
        val dsValue = stringSerializer.decodeFromString<Value>(serializedDS)
        assertTrue(dsValue is DataStructValue)
    }
}
