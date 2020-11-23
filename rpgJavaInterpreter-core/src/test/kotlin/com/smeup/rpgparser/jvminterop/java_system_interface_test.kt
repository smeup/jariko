package com.smeup.rpgparser.jvminterop

import com.smeup.rpgparser.interpreter.Program
import com.smeup.rpgparser.interpreter.ProgramParam
import com.smeup.rpgparser.interpreter.SystemInterface
import com.smeup.rpgparser.interpreter.Value
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
