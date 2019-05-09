package com.github.rpgjavainterpreter.gatecontroller

import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.interpreter.Function

annotation class Param(val name: String)

data class VarElement(val varCd: String, val varVa: String)

private fun rpgProgram(name: String) : RpgProgram {
    return RpgProgram.fromInputStream(MySystemInterface::class.java.getResourceAsStream("/$name.rpgle"), name)
}

object MySystemInterface : SystemInterface {
    override fun display(value: String) {
        println(value)
    }

    override fun findProgram(name: String): Program? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findFunction(globalSymbolTable: SymbolTable, name: String): Function? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

class Jd001 {

    private val traceMode = true
    private val programInterpreter = ProgramInterpreter(MySystemInterface)
    private val rpgProgram = com.github.rpgjavainterpreter.gatecontroller.rpgProgram("JD_001")

    fun call(originalUrl: String, stringToReplace: String, replacement: String) {
        call("INZ", arrayOf(
                VarElement("Url", originalUrl),
                VarElement(stringToReplace, replacement)))
        call("EXE", emptyArray())
        call("CLO", emptyArray())
    }

    fun call(@Param("U\$FUNZ") funz: String, @Param("U\$SVARSK") svarsk: Array<VarElement>) {
        val initialValues = HashMap<String, Value>()
        initialValues["U\$FUNZ"] = funz.asValue()
//        initialValues["U\$SVARSK"] = ArrayValue((0 until 200).map {
//
//        }
        programInterpreter.execute(rpgProgram, initialValues)
    }
}

fun main(args: Array<String>) {
    Jd001().call("https://xxx.myurl.com", "x", "w")
}