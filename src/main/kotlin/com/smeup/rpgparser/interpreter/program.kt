package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.RpgParserFacade
import com.smeup.rpgparser.resolve
import java.io.InputStream

interface Program {
    fun execute(systemInterface: SystemInterface, params: Map<String, Value>)
}

class RpgProgram(val inputStream: InputStream, val name: String = "<UNNAMED>") : Program {
    override fun execute(systemInterface: SystemInterface, params: Map<String, Value>) {
        val interpreter = Interpreter(systemInterface, programName = name)
        val cu = RpgParserFacade().parseAndProduceAst(inputStream)
        cu.resolve()
        interpreter.execute(cu, params)
    }
}

abstract class JvmProgram(val name: String = "<UNNAMED>") : Program
