package com.github.rpgjavainterpreter.gatecontroller

import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.jvminterop.Size
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import com.smeup.rpgparser.rpginterop.Param
import com.smeup.rpgparser.rpginterop.RpgFacade
import com.smeup.rpgparser.rpginterop.RpgSystem
import java.io.File

data class VarElement(
    @property:Size(50) val varCd: String,
    @property:Size(1000) val varVa: String
)

data class JD_001_params(
    @property:Param("U\$FUNZ") val funz: String,
    @property:Param("U\$SVARSK") @property:Size(200) val svarsk: Array<VarElement>
)

class JD_001(javaSystemInterface: JavaSystemInterface) : RpgFacade<JD_001_params>(systemInterface = javaSystemInterface) {

    fun call(originalUrl: String, stringToReplace: String, replacement: String) {
        singleCall(JD_001_params("INZ", arrayOf(VarElement("Url", originalUrl), VarElement(stringToReplace, replacement))))
        singleCall(JD_001_params("ESE", arrayOf(VarElement("Url", originalUrl), VarElement(stringToReplace, replacement))))
        singleCall(JD_001_params("CLO", emptyArray()))
    }
}

data class JD_003_params(
    @property:Param("U\$FUNZ") val funz: String,
    @property:Param("U\$METO") @property:Size(10) val meto: String,
    @property:Param("U\$SVARSK") @property:Size(200) val svarsk: Array<VarElement>,
    @property:Param("U\$IN35") @property:Size(1) val in35: String
)

class JD_003(javaSystemInterface: JavaSystemInterface) : RpgFacade<JD_003_params>(systemInterface = javaSystemInterface) {

    fun call() {
        var flag: String = " "
        singleCall(JD_003_params("INZ", "", arrayOf(VarElement("SOCKET", "4321")), flag))
        println("Result $flag")
        singleCall(JD_003_params("ESE", "", arrayOf(VarElement("SOCKET", "4321")), flag))
        println("Result $flag")
        singleCall(JD_003_params("CLO", "", arrayOf(VarElement("SOCKET", "4321")), flag))
        println("Result $flag")
    }
}

fun main(args: Array<String>) {
    val javaSystemInterface = JavaSystemInterface().apply {
        rpgSystem = RpgSystem().apply {
            DirRpgProgramFinder(File("examples/rpg"))
        }
    }
    javaSystemInterface.addJavaInteropPackage("com.github.rpgjavainterpreter.gatecontroller")
    JD_001(javaSystemInterface).call("https://xxx.myurl.com", "x", "w")
//    JD_003(javaSystemInterface).call()
}
