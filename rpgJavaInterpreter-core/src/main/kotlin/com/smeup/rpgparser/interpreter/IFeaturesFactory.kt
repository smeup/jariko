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

import com.smeup.rpgparser.parsing.parsetreetoast.RpgType
import kotlin.reflect.full.createInstance

/**
 * Allows to enable features
 * */
interface IFeaturesFactory {
    fun createSymbolTable(): ISymbolTable

    /**
     * It allows to override the StringType creation.
     * The current implementation tests the presence of [FeatureFlag.UnlimitedStringTypeFlag] and
     * if is set to on it returns an instance of [UnlimitedStringType]
     * @param create: default creation type implementation
     * @return the instance of type created
     * */
    fun createStringType(create: () -> StringType): Type {
        return if (FeatureFlag.UnlimitedStringTypeFlag.isOn()) {
            UnlimitedStringType
        } else create.invoke()
    }
}

object FeaturesFactory {

    private fun dumpVersion() {
        versionFileContent?.let {
            println("JaRIKo - Java Rpg Interpreter written in Kotlin")
            println(it)
            println("************************************************")
        }
    }

    private val versionFileContent: String? by lazy {
        IFeaturesFactory::class.java.getResource("/META-INF/com.smeup.jariko/version.txt")
            ?.let { it.readText() }
    }

    /**
     * Retrieves the version information of Jariko.
     *
     * This function reads the version file content and parses it into a JarikoVersion object.
     * The version file content is expected to be in the format of "Version: version, Branch: branch, Revision: revision, Buildtime: buildtime".
     * If the version file content is null a default JarikoVersion object is returned with all properties set to "UNKNOWN".
     *
     * @return A JarikoVersion object representing the version information of Jariko.
     */
    fun getJarikoVersionVersion(): JarikoVersion {
        return versionFileContent?.let { content ->
            val props = mutableMapOf<String, String>()
            content.split("\n").forEach {
                if (it.startsWith("Version") || it.startsWith("Branch") || it.startsWith("Revision") || it.startsWith("Buildtime")) {
                    val (key, value) = it.split(": ")
                    props[key] = value.trim()
                }
            }
            JarikoVersion(
                version = props["Version"] ?: "",
                branch = props["Branch"] ?: "",
                revision = props["Revision"] ?: "",
                buildtime = props["Buildtime"] ?: ""
            )
        } ?: JarikoVersion()
    }

    private val factory: IFeaturesFactory by lazy {
        dumpVersion()
        val property = System.getProperty("jariko.featuresFactory", System.getProperty("featuresFactory", ""))
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
    UnlimitedStringTypeFlag;

    fun getPropertyName() = "jariko.features.$name"

    /**
     * @return true if the system property [getPropertyName] is set to "1" "on" or "true"
     * */
    fun isOn(): Boolean {
        val property = System.getProperty(getPropertyName(), "0")
        return property.lowercase().matches(Regex("1|on|true"))
    }
}

/**
 * Data class representing the version information of Jariko.
 *
 * @property version The version of Jariko. Defaults to "UNKNOWN" if not specified.
 * @property branch The branch of the source code. Defaults to "UNKNOWN" if not specified.
 * @property revision The revision of the source code. Defaults to "UNKNOWN" if not specified.
 * @property buildtime The build time of the source code. Defaults to "UNKNOWN" if not specified.
 */
data class JarikoVersion(
    val version: String = "UNKNOWN",
    val branch: String = "UNKNOWN",
    val revision: String = "UNKNOWN",
    val buildtime: String = "UNKNOWN"
)