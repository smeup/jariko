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

import com.smeup.rpgparser.parsing.parsetreetoast.RpgType
import org.junit.Test
import kotlin.test.assertEquals

class TypeTest {

    @Test
    fun aSmallerIntIsAssignableToALargerInt() {
        val valueType = NumberType(1, 0)
        val targetType = NumberType(8, 0)
        assertEquals(true, targetType.canBeAssigned(valueType))
    }

    @Test
    fun twoSameNumberTypesAreAssignableToEachOther() {
        val valueType = NumberType(8, 3)
        val targetType = NumberType(8, 3)
        assertEquals(true, targetType.canBeAssigned(valueType))
    }

    @Test
    fun numberTypeLength() {
        val numberType = NumberType(entireDigits = 15, decimalDigits = 5, RpgType.ZONED)
        assertEquals(15, numberType.size)
    }
}