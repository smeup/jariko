package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.execution.MainExecutionContext

class ProgramInterpreter(val systemInterface: SystemInterface) {

    fun execute(rpgProgram: RpgProgram, initialValues: LinkedHashMap<String, Value>) {
        MainExecutionContext.getProgramStack().push(rpgProgram)
        try {
            rpgProgram.execute(systemInterface = systemInterface, params = initialValues)
        } finally {
            MainExecutionContext.getProgramStack().pop()
        }
    }
}
