package com.smeup.rpgparser.interpreter

class ProgramInterpreter(val systemInterface: SystemInterface) {

    fun execute(rpgProgram: RpgProgram, initialValues: LinkedHashMap<String, Value>) {
        rpgProgram.execute(systemInterface = systemInterface, params = initialValues)
    }
}
