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

@file:JvmName("StandardFeaturesFactory")
package com.smeup.rpgparser.interpreter

import java.lang.IllegalArgumentException
import kotlin.reflect.full.createInstance
import com.smeup.rpgparser.parsing.parsetreetoast.RpgType

/**
 * Allows to enable features
 * */
interface IFeaturesFactory {
    fun createSymbolTable(): ISymbolTable

    /**
     * It allows to override the StringType creation.
     * The current implementation tests the presence of [FeatureFlag.UnlimitedStringTypeSwitch] and
     * if is set to on it returns an instance of [UnlimitedStringType]
     * @param create: default creation type implementation
     * @return the instance of type created
     * */
    fun createStringType(create: () -> StringType): Type {
        return if (FeatureFlag.UnlimitedStringTypeSwitch.isOn()) {
            UnlimitedStringType
        } else create.invoke()
    }
}

object FeaturesFactory {

    private val factory: IFeaturesFactory by lazy {
        val property = System.getProperty("featuresFactory", "")
        val featuresFactoryId = if (property == "") "default" else {
            property
        }
        val featuresFactoryImpl = if (featuresFactoryId.contains('.', false)) {
            featuresFactoryId
        } else {
            val mProperty = java.util.Properties()
            IFeaturesFactory::class.java.getResource("/META-INF/com.smeup.jariko/features.properties")!!
                .openStream()!!.use {
                    mProperty.load(it)
                }
            mProperty.getProperty(featuresFactoryId)
                ?: throw IllegalArgumentException("Not found factory identified by: $featuresFactoryId")
        }

        println("------------------------------------------------------------------------------------")
        println("Creating features factory: $featuresFactoryImpl")
        println("------------------------------------------------------------------------------------")
        println("Feature flags status:")
        FeatureFlag.values().forEach { featureFlag ->
            val onOrOff = if (featureFlag.isOn()) "on" else "off"
            println(" - ${featureFlag.getPropertyName()}: $onOrOff")
        }
        println("------------------------------------------------------------------------------------")
        Class.forName(featuresFactoryImpl).kotlin.createInstance() as IFeaturesFactory
    }

    fun newInstance() = factory
}

class StandardFeaturesFactory : IFeaturesFactory {

    override fun createSymbolTable() = SymbolTable()
}

enum class FeatureFlag {

    /**
     * If "on" the alphanumeric [RpgType.ZONED] is handled like [RpgType.UNLIMITED_STRING].
     * Currently, the [RpgType.CHARACTER] is not yet handled because this cause a regression in some tests
     */
    UnlimitedStringTypeSwitch;

    fun getPropertyName() = "jariko.features.$name"

    /**
     * @return true if the system property [getPropertyName] is set to "1" "on" or "true"
     * */
    fun isOn(): Boolean {
        val property = System.getProperty(getPropertyName(), "0")
        return property.lowercase().matches(Regex("1|on|true"))
    }
}