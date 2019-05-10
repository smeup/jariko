package com.smeup.rpgparser

import com.smeup.rpgparser.ast.CompilationUnit
import com.smeup.rpgparser.facade.RpgParserFacade
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.interpreter.Function
import com.smeup.rpgparser.parsetreetoast.ToAstConfiguration
import com.smeup.rpgparser.parsetreetoast.toAst
import org.apache.commons.io.input.BOMInputStream
import java.io.InputStream

class ConsoleSystemInterface : SystemInterface {
    override fun findProgram(name: String): Program? {
        return null
    }

    override fun display(value: String) {
        println(value)
    }
    override fun findFunction(globalSymbolTable: SymbolTable, name: String): Function? {
        return null
    }
}

// Used only to get a class to be used for getResourceAsStream
class Dummy

fun inputStreamFor(exampleName: String) : InputStream {
    return BOMInputStream(Dummy::class.java.getResourceAsStream("/$exampleName.rpgle"))
}

fun runPgm(exampleName: String, parameters: List<String>) {
    val parseTreeRoot = RpgParserFacade().parse(inputStreamFor(exampleName)).root!!
    val cu = parseTreeRoot.toAst(ToAstConfiguration(true))
    val si = ConsoleSystemInterface()
    runExecution(cu, toNameValueMap(cu.dataDefinitions, parameters), si)
}

fun runExecution(cu: CompilationUnit,
                 initialValues: Map<String, Value>, systemInterface: SystemInterface? = null,
                 traceMode : Boolean = false) : InternalInterpreter {
    val interpreter = InternalInterpreter(systemInterface ?: DummySystemInterface)
    interpreter.traceMode = traceMode
    interpreter.execute(cu, initialValues)
    return interpreter
}

private fun toNameValueMap(dataDefinitions: List<DataDefinition>, parameters: List<String>) : Map<String, Value> {
    val values = parameters.map { parameter -> StringValue(parameter) }
    return dataDefinitions
            .map {dataDefinition -> dataDefinition.name }
            .zip(values)
            .toMap()
}

fun main(args : Array<String>) {
    if (args.size == 0) {
        println("Please provide the name of a .rpgle file to interpret")
        return
    }

    runPgm(args[0], args.asList().subList(1, args.size))
}

