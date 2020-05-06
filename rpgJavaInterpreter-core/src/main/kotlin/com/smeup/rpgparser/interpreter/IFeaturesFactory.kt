@file:JvmName("StandardFeaturesFactory")
package com.smeup.rpgparser.interpreter

import java.lang.IllegalArgumentException
import kotlin.reflect.full.createInstance

/**
 * Allows enable features
 * */
interface IFeaturesFactory {
    fun createSymbolTable(): ISymbolTable
}

object FeaturesFactory {

    private val factory: IFeaturesFactory by lazy {
        var featuresFactoryId = System.getProperty("featuresFactory", "default")
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
        println("Creating features factory: $featuresFactoryImpl")
        Class.forName(featuresFactoryImpl).kotlin.createInstance() as IFeaturesFactory
    }

    fun newInstance() = factory
}

class StandardFeaturesFactory : IFeaturesFactory {
    override fun createSymbolTable() = SymbolTable()
}
