package com.github.rpgjavainterpreter.gatecontroller

import com.smeup.rpgparser.interpreter.SystemInterface
import com.smeup.rpgparser.interpreter.Value
import com.smeup.rpgparser.jvminterop.JvmProgramByReflection
import com.smeup.rpgparser.jvminterop.Size

class JD_URL : JvmProgramByReflection() {

    fun run(
        systemInterface: SystemInterface,
        @Size(10) funz: String,
        @Size(10) method: String,
        @Size(1000) URL: String
    ): List<Value> {
        systemInterface.display("Invoked $funz $method, URL=$URL")
        return emptyList()
    }
}

class JD_RCVSCK : JvmProgramByReflection() {

    fun run(
        systemInterface: SystemInterface,
        @Size(10) addr: String,
        @Size(30000) buffer: String,
        @Size(5) bufferLen: Int
    ): List<Value> {
        systemInterface.display("Invoked $addr $buffer $bufferLen")
        return emptyList()
    }
}