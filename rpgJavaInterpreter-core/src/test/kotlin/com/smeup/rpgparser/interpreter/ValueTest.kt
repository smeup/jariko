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

package com.smeup.rpgparser.interpreter

import org.junit.Test
import kotlin.test.assertEquals

class ValueTest {

    @Test
    fun takeFromIntValue() {
        assertEquals(IntValue(2345), IntValue(1234567).take(2, 5))
    }

    @Test
    fun occurableDataStructureTypeBlank() {
        val elementSize = 20
        val occurs = 10
        val fields = listOf(
                FieldType("S1", StringType(10, false)),
                FieldType("S1", StringType(5, false))
        )
        val dataStructureType = DataStructureType(fields = fields, elementSize = elementSize)
        val dataStructBlankValue = dataStructureType.blank()
        val occurableDataStructBlankValue = OccurableDataStructureType(
            dataStructureType = dataStructureType,
            occurs = occurs)
            .blank() as OccurableDataStructValue
        for (i in 1..occurs) {
            assertEquals(dataStructBlankValue, occurableDataStructBlankValue[i])
        }
    }
}
