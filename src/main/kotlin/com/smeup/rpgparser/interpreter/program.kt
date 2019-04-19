package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.RpgParserFacade
import com.smeup.rpgparser.ast.CompilationUnit
import com.smeup.rpgparser.resolve
import java.io.InputStream

interface Program {
    fun execute(systemInterface: SystemInterface, params: Map<String, Value>)
}

class RpgProgram(val cu: CompilationUnit, val name: String = "<UNNAMED>") : Program {

    init {
        cu.resolve()
    }

    companion object {
        fun fromInputStream(inputStream: InputStream, name: String = "<UNNAMED>") : RpgProgram {
            val cu = RpgParserFacade().parseAndProduceAst(inputStream)
            return RpgProgram(cu, name)
        }
    }

    override fun execute(systemInterface: SystemInterface, params: Map<String, Value>) {
        val interpreter = Interpreter(systemInterface, programName = name)
        interpreter.execute(cu, params)
    }
}

abstract class JvmProgram(val name: String = "<UNNAMED>") : Program
