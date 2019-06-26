package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.facade.RpgParserFacade
import com.smeup.rpgparser.ast.CompilationUnit
import com.smeup.rpgparser.parsetreetoast.resolve
import java.io.InputStream

data class ProgramParam(val name: String, val type: Type)

interface Program {
    fun params() : List<ProgramParam>
    fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>) : List<Value>
}

class RpgProgram(val cu: CompilationUnit, val name: String = "<UNNAMED>") : Program {
    override fun params(): List<ProgramParam> {
        val plistParams = cu.entryPlist ?: throw RuntimeException("[$name] no entry plist found")
        // TODO derive proper type from the data specification
        return plistParams.params.map {
            val type = cu.getDataDefinition(it.param.name).type
            ProgramParam(it.param.name, type)
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

    override fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>) : List<Value> {
        require(params.keys.toSet() == params().asSequence().map { it.name }.toSet()) {
            "Expected params: ${params().asSequence().map { it.name }.joinToString(", ")}"
        }
        val interpreter = InternalInterpreter(systemInterface)
        for (pv in params) {
            val expectedType = params().find { it.name == pv.key }!!.type
            val coercedValue = interpreter.coerce(pv.value, expectedType)
            require(coercedValue.assignableTo(expectedType)) {
                "param ${pv.key} was expected to have type $expectedType. It has value: $coercedValue"
            }
        }
        interpreter.execute(this.cu, params)
        return params().map { interpreter[it.name] }
    }

    override fun equals(other: Any?)
            = (other is RpgProgram) && other.name == name

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

