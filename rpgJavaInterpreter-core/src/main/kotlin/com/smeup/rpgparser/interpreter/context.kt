package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.DataWrapUpChoice
import java.nio.charset.Charset

interface InterpretationContext {
    val currentProgramName: String
    fun setDataWrapUpPolicy(dataWrapUpChoice: DataWrapUpChoice)
    fun shouldReinitialize(): Boolean
}

object DummyInterpretationContext : InterpretationContext {
    override val currentProgramName: String
        get() = "<UNSPECIFIED>"

    override fun shouldReinitialize() = false

    override fun setDataWrapUpPolicy(dataWrapUpChoice: DataWrapUpChoice) {
        // nothing to do
    }
}

class LocalizationContext(val charset: Charset = Charset.forName("Cp037"), val decedit: String = ".")
