/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.execution.MainExecutionContext

class ProgramInterpreter(val systemInterface: SystemInterface) {

    fun execute(rpgProgram: RpgProgram, initialValues: LinkedHashMap<String, Value>) {
        val stackIsEmpty = MainExecutionContext.getProgramStack().isEmpty()
        // This case is when a third party library implements CallProgramHandler interface and in the handleCall lambda
        // calls the same program again.
        // In this case, we want to replace the instance created by CallStatement (last element in the stack)
        // with the one passed as parameter to this method.
        val lastIsLikeRpgProgram = !stackIsEmpty && MainExecutionContext.getProgramStack().peek() == rpgProgram
        if (lastIsLikeRpgProgram) {
            val stack = MainExecutionContext.getProgramStack()
            // replace the RpgProgram instance created in the CallStatement with the one passed as parameter
            stack[stack.lastIndex] = rpgProgram
            rpgProgram.execute(systemInterface = systemInterface, params = initialValues)
        } else {
            MainExecutionContext.getProgramStack().push(rpgProgram)
            rpgProgram.execute(systemInterface = systemInterface, params = initialValues)
            MainExecutionContext.getProgramStack().pop()
            if (MainExecutionContext.getProgramStack().isEmpty()) {
                rpgProgram.intepreterCore.onInterpretationEnd()
            }
        }
    }
}
