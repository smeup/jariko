package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.ast.DataWrapUpChoice

class ProgramInterpreter {

    private val dataWrapUpPolicy = HashMap<RpgProgram, DataWrapUpChoice>()
    private val interpreters = HashMap<RpgProgram, InternalInterpreter>()

    fun execute(rpgProgram: RpgProgram) {
        
    }

}