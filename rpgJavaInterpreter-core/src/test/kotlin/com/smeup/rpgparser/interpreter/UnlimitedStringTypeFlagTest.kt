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

import com.smeup.rpgparser.AbstractTest
import org.junit.After
import kotlin.test.Test
import kotlin.test.assertTrue

class UnlimitedStringTypeFlagTest : AbstractTest() {

    private val featuresFactory = FeaturesFactory.newInstance()

    /**
     * Assert that if UnlimitedStringTypeSwitch is default featuresFactory.createStringType returns
     * an instance of StringType
     * */
    @Test
    fun createStringType() {
        val type = featuresFactory.createStringType { StringType(10, false) }
        assertTrue(type is StringType)
    }

    /**
     * Assert that if UnlimitedStringTypeSwitch is on featuresFactory.createStringType returns
     * an instance of UnlimitedStringType
     * */
    @Test
    fun createUnlimitedStringType() {
        switchOn()
        val type = featuresFactory.createStringType { StringType(10, false) }
        assertTrue(type is UnlimitedStringType)
    }

    /**
     * Assert that if UnlimitedStringTypeSwitch is default the Msg type is StringType
     * */
    @Test
    fun msgInDSpecIsStringType() {
        assertASTCanBeProduced("HELLO").apply {
            assertTrue(getDataDefinition("Msg").type is StringType)
        }
    }

    /**
     * Assert that if UnlimitedStringTypeSwitch is on the Msg type is UnlimitedStringTupe
     * */
    @Test
    fun msgInDSpecIsUnlimitedStringType() {
        switchOn()
        assertASTCanBeProduced("HELLO").apply {
            assertTrue(getDataDefinition("Msg").type is UnlimitedStringType)
        }
    }

    /**
     * Assert that if UnlimitedStringTypeSwitch is default the Msg1 type is StringType
     * */
    @Test
    fun msg1InDSFieldIsStringType() {
        assertASTCanBeProduced("UNLIMIT_DS").apply {
            assertTrue(getDataOrFieldDefinition("Msg1").type is StringType)
        }
    }

    /**
     * Assert that if UnlimitedStringTypeSwitch is on the Msg1 type is UnlimitedStringTupe
     * */
    @Test
    fun msg1InDSFieldIsIsUnlimitedStringType() {
        switchOn()
        assertASTCanBeProduced("UNLIMIT_DS").apply {
            assertTrue(getDataOrFieldDefinition("Msg1").type is UnlimitedStringType)
        }
    }

    @After
    fun tearDown() {
        switchOff()
    }

    private fun switchOn() {
        System.setProperty(FeatureFlag.UnlimitedStringTypeFlag.getPropertyName(), "1")
    }

    private fun switchOff() {
        System.setProperty(FeatureFlag.UnlimitedStringTypeFlag.getPropertyName(), "0")
    }
}