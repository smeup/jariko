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

import org.junit.After
import kotlin.test.Test
import kotlin.test.assertTrue

class FeaturesFactoryTest {

    private val featuresFactory = FeaturesFactory.newInstance()

    @Test
    fun createDefaultStringType() {
        val type = featuresFactory.createStringType { StringType(10, false) }
        assertTrue(type is StringType)
    }

    @Test
    fun createUnlimitedStringType() {
        System.setProperty(FeatureFlag.UnlimitedStringTypeSwitch.getPropertyName(), "true")
        val type = featuresFactory.createStringType { StringType(10, false) }
        assertTrue(type is UnlimitedStringType)
    }

    @After
    fun tearDown() {
        System.setProperty(FeatureFlag.UnlimitedStringTypeSwitch.getPropertyName(), "")
    }
}