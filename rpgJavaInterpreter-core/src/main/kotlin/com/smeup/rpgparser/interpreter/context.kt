package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.DataWrapUpChoice
import java.nio.charset.Charset

interface InterpretationContext {
    val currentProgramName: String
    fun setDataWrapUpPolicy(dataWrapUpChoice: DataWrapUpChoice)
    fun shouldReinitialize(): Boolean
}

class SimpleInterpretationContext(override val currentProgramName: String = "<UNSPECIFIED>") : InterpretationContext {
    override fun shouldReinitialize() = false

    override fun setDataWrapUpPolicy(dataWrapUpChoice: DataWrapUpChoice) {
        // nothing to do
    }
}

enum class DecEdit {
    COMMA,
    DOT,
    ZERO_COMMA,
    ZERO_DOT
}

class LocalizationContext(val charset: Charset = DEFAULT_CHARSET, val decedit: DecEdit = DecEdit.DOT)
