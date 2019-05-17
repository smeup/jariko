package com.smeup.rpgparser

import com.smeup.rpgparser.ast.CompilationUnit
import com.smeup.rpgparser.facade.RpgParserFacade
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.interpreter.Function
import com.smeup.rpgparser.parsetreetoast.ToAstConfiguration
import com.smeup.rpgparser.parsetreetoast.toAst
import com.smeup.rpgparser.rgpinterop.*
import org.apache.commons.io.input.BOMInputStream
import java.io.File
import java.io.InputStream

class CommandLineParms(val parmsList: List<String>)

class CommandLineProgramNameSource(val name: String) : ProgramNameSource<CommandLineParms> {
    override fun nameFor(rpgFacade: RpgFacade<CommandLineParms>): String = name
}

class CommandLineProgram(val name: String) : RpgFacade<CommandLineParms>((CommandLineProgramNameSource(name))) {
    override fun toInitialValues(params: CommandLineParms) : Map<String, Value> {
        val values = params.parmsList.map { parameter -> StringValue(parameter) }
        return rpgProgram.params()
                .map {dataDefinition -> dataDefinition.name }
                .zip(values)
                .toMap()
    }
}

class ResourceProgramFinder(val path: String): RpgProgramFinder {
    override fun findRpgProgram(name: String): RpgProgram? {
        return RpgProgram.fromInputStream(BOMInputStream(ResourceProgramFinder::class.java.getResourceAsStream("$path$name.rpgle")))
    }
}

fun main(args : Array<String>) {
    if (args.isEmpty()) {
        println("Please provide the name of a .rpgle file to interpret")
        return
    }
    RpgSystem.addProgramFinder(DirRpgProgramFinder(File("examples/rpg")))
    RpgSystem.addProgramFinder(DirRpgProgramFinder(File(".")))
    RpgSystem.addProgramFinder(ResourceProgramFinder("/"))
    CommandLineProgram(args[0]).singleCall(CommandLineParms(args.asList().subList(1, args.size)))
}

