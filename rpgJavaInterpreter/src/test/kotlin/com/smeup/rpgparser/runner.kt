package com.smeup.rpgparser

import com.smeup.rpgparser.facade.RpgParserFacade
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.interpreter.Function
import com.smeup.rpgparser.parsetreetoast.ToAstConfiguration
import com.smeup.rpgparser.parsetreetoast.resolve
import com.smeup.rpgparser.parsetreetoast.toAst

class ConsoleSystemInterface() : SystemInterface {
    override fun findFunction(globalSymbolTable: SymbolTable, name: String): Function? {
        return null
    }

    override fun findProgram(name: String): Program? {
        return null
    }

    override fun display(value: String) {
        println(value)
    }
}

fun runPgm(exampleName: String, parameters: List<String>) {
    val parseTreeRoot = RpgParserFacade().parse(inputStreamFor(exampleName)).root!!
    val cu = parseTreeRoot.toAst(ToAstConfiguration(true))
    cu.resolve()
    val si = ConsoleSystemInterface()
    execute(cu, toNameValueMap(cu.dataDefinitions, parameters), si)
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