@file:JvmName("StandardFeaturesFactory")
package com.smeup.rpgparser.interpreter

import java.lang.IllegalArgumentException
import kotlin.reflect.full.createInstance

/**
 * Allows enable features
 * */
interface IFeaturesFactory {
    fun createSymbolTable(): ISymbolTable

    companion object {
        /**
         * Create a factory instance.
         * Initially it's read system property featuresFactory, if it contains a dot, featuresFactory property is
         * factory implementation class, else featuresFactory is a factory implementation id.
         * Factories implementations by ids, are searched in /META-INF/com.smeup.jariko/features.properties
         * */
        fun newInstance(): IFeaturesFactory {
            val featuresFactoryId = System.getProperty("featuresFactory", "default")
            // if id contains dot, id is an implementation
            val featuresFactoryImpl = if (featuresFactoryId.indexOf(".") != -1) {
                featuresFactoryId
            } else {
                val property = java.util.Properties()
                IFeaturesFactory::class.java.getResource("/META-INF/com.smeup.jariko/features.properties")!!
                        .openStream()!!.use {
                    property.load(it)
                }
                property.getProperty(featuresFactoryId)
                ?: throw IllegalArgumentException("Not found factory identified by: $featuresFactoryId")
            }
            return Class.forName(featuresFactoryImpl).kotlin.createInstance() as IFeaturesFactory
        }
    }
}

class StandardFeaturesFactory : IFeaturesFactory {
    override fun createSymbolTable() = SymbolTable()
}
