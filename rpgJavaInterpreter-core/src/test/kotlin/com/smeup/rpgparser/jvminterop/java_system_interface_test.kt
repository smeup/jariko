package com.smeup.rpgparser.jvminterop

import com.smeup.rpgparser.interpreter.Program
import com.smeup.rpgparser.interpreter.ProgramParam
import com.smeup.rpgparser.interpreter.SystemInterface
import com.smeup.rpgparser.interpreter.Value
import com.smeup.rpgparser.rgpinterop.RpgFacade
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class JavaSystemInterfaceTest {

    private val packageName: String = this.javaClass.`package`.name

    @Test
    fun findsPgmsByName() {
        val javaSystemInterface = JavaSystemInterface()
        javaSystemInterface.addJavaInteropPackage(packageName)
        val program = javaSystemInterface.findProgram("SomePgm")
        assertNotNull(program)
        assertTrue(program is SomePgm)
    }

    @Test
    fun doesNotCrashForWrongCaseClassNames() {
        val javaSystemInterface = JavaSystemInterface(programSource = null)
        javaSystemInterface.addJavaInteropPackage(packageName)
        val wrongCasePgm = "SOMEPGM"
        val program = javaSystemInterface.findProgram(wrongCasePgm)
        assertNull(program)
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
