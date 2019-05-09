package com.github.rpgjavainterpreter.gatecontroller

annotation class Param(val name: String)

data class VarElement(val varCd: String, val varVa: String)

class Jd001 {

    private val traceMode = true

    fun call(originalUrl: String, stringToReplace: String, replacement: String) {
        call("INZ", arrayOf(
                VarElement("Url", originalUrl),
                VarElement(stringToReplace, replacement)))
        call("EXE", emptyArray(), reinitialization = false)
        call("CLO", emptyArray(), reinitialization = false)
    }

    fun call(@Param("U\$FUNZ") funz: String, @Param("U\$SVARSK") svarsk: Array<VarElement>,
             reinitialization: Boolean = true) {
        TODO()
    }
}

fun main(args: Array<String>) {
    Jd001().call("https://xxx.myurl.com", "x", "w")
}