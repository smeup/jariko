package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.DataWrapUpChoice
import java.nio.charset.Charset

interface InterpretationContext {
    val currentProgramName: String
    var dataWrapUpChoice: DataWrapUpChoice?
    fun shouldReinitialize(): Boolean
}

object DummyInterpretationContext : InterpretationContext {
    override val currentProgramName: String
        get() = "<UNSPECIFIED>"

    override fun shouldReinitialize() = false

    override var dataWrapUpChoice: DataWrapUpChoice?
        get() = null
        set(value) {}
}

enum class DecEdit {
    COMMA,
    DOT,
    ZERO_COMMA,
    ZERO_DOT
}

class LocalizationContext(val charset: Charset = DEFAULT_CHARSET, val decedit: DecEdit = DecEdit.DOT)
