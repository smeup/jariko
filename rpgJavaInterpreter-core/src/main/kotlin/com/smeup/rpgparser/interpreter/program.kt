package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.parsing.facade.RpgParserFacade
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate
import java.io.InputStream

data class ProgramParam(val name: String, val type: Type)

infix fun Type.parm(name: String): ProgramParam = ProgramParam(name, this)

interface Program {
    fun params(): List<ProgramParam>
    fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>): List<Value>
}

class RpgProgram(val cu: CompilationUnit, dbInterface: DBInterface, val name: String = "<UNNAMED RPG PROGRAM>") : Program {

    private var systemInterface: SystemInterface? = null

    private val interpreter: InternalInterpreter by lazy {
        InternalInterpreter(this.systemInterface!!)
    }

    override fun params(): List<ProgramParam> {
        val plistParams = cu.entryPlist
        // TODO derive proper type from the data specification
        return plistParams?.params?.map {
            val type = cu.getDataDefinition(it.param.name).type
            ProgramParam(it.param.name, type)
        }
        ?: emptyList()
    }

    init {
        cu.resolveAndValidate(dbInterface)
    }

    companion object {
        fun fromInputStream(inputStream: InputStream, dbInterface: DBInterface, name: String = "<UNNAMED INPUT STREAM>"): RpgProgram {
            val cu = RpgParserFacade().parseAndProduceAst(inputStream)
            return RpgProgram(cu, dbInterface, name)
        }
    }

    override fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>): List<Value> {
        require(params.keys.toSet() == params().asSequence().map { it.name }.toSet()) {
            "Expected params: ${params().asSequence().map { it.name }.joinToString(", ")}"
        }
        this.systemInterface = systemInterface

        for (pv in params) {
            val expectedType = params().find { it.name == pv.key }!!.type
            val coercedValue = coerce(pv.value, expectedType)
            require(coercedValue.assignableTo(expectedType)) {
                "param ${pv.key} was expected to have type $expectedType. It has value: $coercedValue"
            }
        }
        interpreter!!.execute(this.cu, params)
        return params().map { interpreter!![it.name] }
    }

    override fun equals(other: Any?) =
            (other is RpgProgram) && other.name == name

    override fun hashCode(): Int {
        return name.hashCode()
    }
}
