package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.RpgParserFacade
import com.smeup.rpgparser.ast.CompilationUnit
import com.smeup.rpgparser.resolve
import java.io.InputStream

data class ProgramParam(val name: String, val type: Type)

interface Program {
    fun params() : List<ProgramParam>
    fun execute(systemInterface: SystemInterface, params: Map<String, Value>)
}

class RpgProgram(val cu: CompilationUnit, val name: String = "<UNNAMED>") : Program {
    override fun params(): List<ProgramParam> {
        val plistParams = cu.main.entryPlist ?: throw RuntimeException("[$name] no entry plist found")
        // TODO derive proper type from the data specification
        return plistParams.params.map {
            val type = cu.getDataDefinition(it.paramName).type
            ProgramParam(it.paramName, type)
        }
    }

    init {
        cu.resolve()
    }

    companion object {
        fun fromInputStream(inputStream: InputStream, name: String = "<UNNAMED>") : RpgProgram {
            val cu = RpgParserFacade().parseAndProduceAst(inputStream)
            return RpgProgram(cu, name)
        }
    }

    override fun execute(systemInterface: SystemInterface, paramValues: Map<String, Value>) {
        require(paramValues.keys.toSet() == params().map { it.name }.toSet()) {
            "Expected params: ${params().map { it.name }.joinToString(", ")}"
        }
        val interpreter = Interpreter(systemInterface, programName = name)
        for (pv in paramValues) {
            val expectedType = params().find { it.name == pv.key }!!.type
            val coercedValue = interpreter.coerce(pv.value, expectedType)
            require(coercedValue.assignableTo(expectedType)) {
                "param ${pv.key} was expected to have type $expectedType. It has value: $coercedValue"
            }
        }
        interpreter.execute(cu, paramValues)
    }
}

abstract class JvmProgram(val name: String = "<UNNAMED>", val params: List<ProgramParam>)
    : Program {
    override fun params() = params
}
