package com.github.rpgjavainterpreter.gatecontroller

import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.jvminterop.Size
import com.smeup.rpgparser.rgpinterop.DirRpgProgramFinder
import com.smeup.rpgparser.rgpinterop.RpgSystem
import java.io.File

annotation class Param(val name: String)

data class VarElement(
        @Size(50) val varCd: String,
        @Size(1000) val varVa: String)

private fun rpgProgram(name: String) : RpgProgram {
    return RpgProgram.fromInputStream(JavaSystemInterface::class.java.getResourceAsStream("/$name.rpgle"), name)
}

class JD_001 {

    var traceMode = false

    private val programInterpreter = ProgramInterpreter(JavaSystemInterface)
    private val rpgProgram by lazy { RpgSystem.getProgram(this.javaClass.simpleName) }

    fun call(originalUrl: String, stringToReplace: String, replacement: String) {
        singleCall("INZ", arrayOf(
                VarElement("Url", originalUrl),
                VarElement(stringToReplace, replacement)))
        singleCall("EXE", emptyArray())
        singleCall("CLO", emptyArray())
    }

    fun singleCall(@Param("U\$FUNZ") funz: String,
                   @Param("U\$SVARSK") @Size(200) svarsk: Array<VarElement>) {
        val initialValues = HashMap<String, Value>()
        initialValues["U\$FUNZ"] = funz.asValue()

        initialValues["U\$SVARSK"] = createArrayValue(StringType(1050), 200) {
            if (it < svarsk.size) {
                toValue(svarsk[it])
            } else {
                blankString(1050)
            }
        }
        programInterpreter.execute(rpgProgram, initialValues, traceMode = traceMode)
    }

    private fun toValue(varElement: VarElement): Value {
        return StringValue(varElement.varCd.padEnd(50, '\u0000') + varElement.varVa.padEnd(1000, '\u0000'))
    }
}

fun main(args: Array<String>) {
    RpgSystem.addProgramFinder(DirRpgProgramFinder(File("examples/rpg")))
    JavaSystemInterface.addJavaInteropPackage("com.github.rpgjavainterpreter.gatecontroller")
    JD_001().call("https://xxx.myurl.com", "x", "w")
}
