/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smeup.rpgparser.jvminterop

import com.smeup.rpgparser.interpreter.*

/*
  JDP_TYPES - Java Doped Procedure
  A sample of a 'doped' procedure, acting like an RPG-procedure
  but its implementation is done in kotlin and not in rpgle."
  Procedure is called by 'CALLP' and or 'EVAL' rpgle statement.
  This procedure is called by a test passing wrong type parameters:
  Expected two parameters 'String,Number' but called with 'Number,Number',
  so an exception will be thrown.
 */

class JDP_TYPES() : JavaFunction {

    override fun execute(
        systemInterface: SystemInterface,
        params: List<FunctionValue>,
        symbolTable: ISymbolTable
    ): Value {
        return IntValue.ONE
    }
}