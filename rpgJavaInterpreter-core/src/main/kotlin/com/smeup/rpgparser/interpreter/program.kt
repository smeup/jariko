package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.parsing.ast.*
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

    var activationGroup: ActivationGroup? = null
    private var initialized = false

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
        interpreter!!.interpretationContext = object : InterpretationContext {
            private var iDataWrapUpChoice: DataWrapUpChoice? = null
            override val currentProgramName: String
                get() = name
            override fun shouldReinitialize() = false
            override var dataWrapUpChoice: DataWrapUpChoice?
                get() = iDataWrapUpChoice
                set(value) {
                    iDataWrapUpChoice = value
                }
        }

        for (pv in params) {
            val expectedType = params().find { it.name == pv.key }!!.type
            val coercedValue = coerce(pv.value, expectedType)
            require(coercedValue.assignableTo(expectedType)) {
                "param ${pv.key} was expected to have type $expectedType. It has value: $coercedValue"
            }
        }
        if (!initialized) {
            initialized = true
            val caller = if (MainExecutionContext.getProgramStack().isNotEmpty()) {
                MainExecutionContext.getProgramStack().peek()
            } else {
                null
            }
            var activationGroupType = cu.activationGroupType()
            // if activation group is not in AST (not defined in RPG program)
            if (activationGroupType == null) {
                activationGroupType = when (caller) {
                    // for main program, which does not have a caller, activation group is fixed by config
                    null -> NamedActivationGroup(MainExecutionContext.getConfiguration().defaultActivationGroupName)
                    else -> {
                        CallerActivationGroup
                    }
                }
            }
            activationGroupType?.let {
                activationGroup = ActivationGroup(it, it.assignedName(this, caller))
            }
        }
        MainExecutionContext.getProgramStack().push(this)
        // set reinitialization to false because symboltable cleaning currently is handled directly
        // in internal interpreter before exit
        // todo i don't know whether parameter reinitialization has still sense
        interpreter.execute(this.cu, params, false)
        params.keys.forEach { params[it] = interpreter[it] }
        val changedInitialValues = params().map { interpreter[it.name] }
        // here clear symbol table if needed
        interpreter.doSomethingAfterExecution()
        MainExecutionContext.getProgramStack().pop()
        return changedInitialValues
    }

    override fun equals(other: Any?) =
            (other is RpgProgram) && other.name == name

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

/**
 * Model activation group concept.
 * @param type activation group type as from grammar
 * @param assignedName Name assigned. The assignment algorithm depends on activation group type
 * @see ActivationGroupType.assignedName
 * */
data class ActivationGroup(val type: ActivationGroupType, val assignedName: String)