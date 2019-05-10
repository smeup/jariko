package com.github.rpgjavainterpreter.gatecontroller

import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.jvminterop.JvmProgramByReflection
import com.smeup.rpgparser.jvminterop.Size

annotation class Param(val name: String)

data class VarElement(val varCd: String, val varVa: String)

private fun rpgProgram(name: String) : RpgProgram {
    return RpgProgram.fromInputStream(JavaSystemInterface::class.java.getResourceAsStream("/$name.rpgle"), name)
}


class JD_URL : JvmProgramByReflection() {

    fun run(systemInterface: SystemInterface, @Size(10) funz: String, @Size(10) method: String,
            @Size(1000) URL: String) : List<Value> {
        println("Invoked $funz $method, URL=$URL")
        return emptyList()
    }
}

class Jd001 {

    private val traceMode = true
    private val programInterpreter = ProgramInterpreter(JavaSystemInterface)
    private val rpgProgram = rpgProgram("JD_001")

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

//        initialValues["U\$SVARSK"] = createArrayValue(StringType(1050), 200) {
//            if (it < svarsk.size) {
//                toValue(svarsk[it])
//            } else {
//                blankString(1050)
//            }
//        }
        initialValues["U\$SVARSK"] = createArrayValue(StringType(1050), 200) { i ->
            when (i) {
                0 -> "Url".padEnd(50, '\u0000') + "https://xxx.myurl.com".padEnd(1000, '\u0000')
                1 -> "x".padEnd(50, '\u0000') + "w".padEnd(1000, '\u0000')
                else -> "".padEnd(1050, '\u0000')
            }.asValue()
        }
        programInterpreter.execute(rpgProgram, initialValues, traceMode = traceMode)
    }

    private fun toValue(varElement: VarElement): Value {
        return StringValue(varElement.varCd.padEnd(50, '\u0000') + varElement.varVa.padEnd(1000, '\u0000'))
    }
}

fun main(args: Array<String>) {
    JavaSystemInterface.addJavaInteropPackage("com.github.rpgjavainterpreter.gatecontroller")
    Jd001().call("https://xxx.myurl.com", "x", "w")
}