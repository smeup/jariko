package com.smeup.rpgparser.jvminterop

import com.smeup.rpgparser.interpreter.Function
import com.smeup.rpgparser.interpreter.Program
import com.smeup.rpgparser.interpreter.SymbolTable
import com.smeup.rpgparser.interpreter.SystemInterface
import java.util.*
import kotlin.reflect.full.isSubclassOf

object JavaSystemInterface : SystemInterface {

    private val javaInteropPackages = LinkedList<String>()
    private val programs = HashMap<String, Program?>()

    fun addJavaInteropPackage(packageName: String) {
        javaInteropPackages.add(packageName)
    }

    override fun display(value: String) {
        println(value)
    }

    override fun findProgram(programName: String): Program? {
        return programs.computeIfAbsent(programName) {
            javaInteropPackages.asSequence().map { packageName ->
                try {
                    val javaClass = this.javaClass.classLoader.loadClass("$packageName.$programName")
                    instantiateProgram(javaClass)
                } catch (e: ClassNotFoundException) {
                    null
                }

            }.filter { it != null }.firstOrNull()
        }
    }

    private fun instantiateProgram(javaClass: Class<*>): Program? {
        return if (javaClass.kotlin.isSubclassOf(Program::class)) {
            javaClass.kotlin.constructors.filter { it.parameters.isEmpty() }.first().call() as Program
        } else {
            null
        }
    }

    override fun findFunction(globalSymbolTable: SymbolTable, name: String): Function? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}