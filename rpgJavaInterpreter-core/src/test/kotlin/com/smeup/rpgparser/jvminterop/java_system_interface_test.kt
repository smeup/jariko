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
import com.smeup.rpgparser.interpreter.Function
import org.junit.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class JavaSystemInterfaceTest {

    @Test
    fun findsPgmsByName() {
        val javaSystemInterface = createJavaSystemInterface()
        val program = javaSystemInterface.findProgram("SomePgm")
        assertNotNull(program)
        assertTrue(program is SomePgm)
    }

    @Test
    fun findsFunctionsByName() {
        val javaSystemInterface = createJavaSystemInterface()
        var globalSymbolTable = SymbolTable()
        val function = javaSystemInterface.findFunction(globalSymbolTable = globalSymbolTable,  "SomeFunction")
        assertNotNull(function)
        assertTrue(function is SomeFunction)
    }

    @Test
    fun doesNotCrashForWrongCaseClassNames() {
        val javaSystemInterface = createJavaSystemInterface()
        val wrongCasePgm = "somepgm"
        val program = javaSystemInterface.findProgram(wrongCasePgm)
        assertNull(program)
    }

// We could do this, but with poor performances
//    @Test
//    fun findsPgmsByUpperCaseName() {
//        val javaSystemInterface = createJavaSystemInterface()
//        val program = javaSystemInterface.findProgram("SOMEPGM")
//        assertNotNull(program)
//        assertTrue(program is SomePgm)
//    }

    private fun createJavaSystemInterface(): JavaSystemInterface {
        val javaSystemInterface = JavaSystemInterface(System.out, programSource = null)
        val packageName = this.javaClass.`package`.name
        javaSystemInterface.addJavaInteropPackage(packageName)
        return javaSystemInterface
    }
}

class SomePgm() : Program {
    override fun params(): List<ProgramParam> {
        return emptyList()
    }

    override fun execute(systemInterface: SystemInterface, params: LinkedHashMap<String, Value>): List<Value> {
        return emptyList()
    }
}

class SomeFunction() : Function {
    override fun params(): List<FunctionParam> {
        return emptyList()
    }

    override fun execute(
        systemInterface: SystemInterface,
        params: List<FunctionValue>,
        symbolTable: ISymbolTable
    ): Value {
        return VoidValue
    }
}
