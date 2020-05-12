@file:JvmName("ExperimentalFeaturesFactory")

package com.smeup.rpgparser.experimental

import com.smeup.rpgparser.interpreter.IFeaturesFactory

class ExperimentalFeaturesFactory : IFeaturesFactory {
    override fun createSymbolTable() = SymbolTable()
}
