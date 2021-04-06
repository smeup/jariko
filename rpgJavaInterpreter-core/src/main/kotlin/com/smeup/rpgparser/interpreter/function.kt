package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.parsing.ast.CompilationUnit
import com.smeup.rpgparser.parsing.ast.ReturnStmt
import com.smeup.rpgparser.parsing.parsetreetoast.resolveAndValidate

enum class ParamPassedBy {
    Reference, Value
}

data class FunctionParam(val name: String, val type: Type, val paramPassedBy: ParamPassedBy = ParamPassedBy.Reference)

data class FunctionValue(val variableName: String? = null, val value: Value)

interface Function {
    fun params(): List<FunctionParam>
    fun execute(systemInterface: SystemInterface, params: List<FunctionValue>, symbolTable: ISymbolTable): Value
}

abstract class JvmFunction(val name: String = "<UNNAMED>", val params: List<FunctionParam>) :
    Function {
    override fun params() = params
}

/**
 * This class models a generic function, "generic" because could be a procedure if return a VoidValue else
 * a function if return something else.
 * */
class RpgFunction(private val compilationUnit: CompilationUnit) : Function {

    override fun params(): List<FunctionParam> {
        val arguments = mutableListOf<FunctionParam>()
        this.compilationUnit.proceduresParamsDataDefinitions!!.forEach {
            arguments.add(FunctionParam(it.name, it.type))
        }
        return arguments
    }

    override fun execute(systemInterface: SystemInterface, params: List<FunctionValue>, symbolTable: ISymbolTable): Value {
        val interpreter: InternalInterpreter by lazy {
            InternalInterpreter(systemInterface)
        }
        // arguments are parameters expected by function
        val arguments = this.params()
        // values passed to function in format argumentName to argumentValue
        // every argumentName will be a variable scoped function
        val argumentNameToValue = mutableMapOf<String, Value>()
        arguments.forEachIndexed { index, functionParam ->
            argumentNameToValue[functionParam.name] = params[index].value
        }

        interpreter.execute(this.compilationUnit, argumentNameToValue, false)

        params.forEachIndexed { index, functionValue ->
            // if passed param contains variable name and parameter is passed by reference
            functionValue.variableName?.let { variableName ->
                if (arguments[index].paramPassedBy == ParamPassedBy.Reference) {
                    symbolTable[symbolTable.dataDefinitionByName(variableName)!!] = interpreter[arguments[index].name]
                }
            }
        }

        // If something to return to caller by 'RETURN' statement
        this.compilationUnit.main.stmts.asReversed().forEach {
            if (it is ReturnStmt) {
                val expressionEvaluation = ExpressionEvaluation(interpreter.systemInterface, LocalizationContext(), interpreter.status)
                return@execute it.expression!!.evalWith(expressionEvaluation as Evaluator)
            }
        }

        return VoidValue
    }

    init {
        compilationUnit.resolveAndValidate()
    }

    companion object {
        /**
         * Create a RpgFunction instance achieved by current program
         * @param name Function name
         * */
        fun fromCurrentProgram(name: String): RpgFunction {
            return RpgFunction(
                compilationUnit = MainExecutionContext.getProgramStack().peek().cu.procedures!!.first {
                    it.name == name
                }
            )
        }
    }
}