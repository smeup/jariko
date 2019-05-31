package com.smeup.rpgparser.jvminterop

import com.smeup.rpgparser.interpreter.Function
import com.smeup.rpgparser.interpreter.Program
import com.smeup.rpgparser.interpreter.SymbolTable
import com.smeup.rpgparser.interpreter.SystemInterface
import com.smeup.rpgparser.rgpinterop.RpgSystem
import java.util.*
import kotlin.reflect.full.isSubclassOf

object JavaSystemInterface : SystemInterface {
    val consoleOutput = LinkedList<String>()
    private val javaInteropPackages = LinkedList<String>()
    private val programs = HashMap<String, Program?>()

    fun addJavaInteropPackage(packageName: String) {
        javaInteropPackages.add(packageName)
    }

    override fun display(value: String) {
        consoleOutput.add(value)
        println(value)
    }

    override fun findProgram(name: String): Program? {
        return programs.computeIfAbsent(name) {
            findInPackages(name) ?: findInFileSystem(name)
        }
    }

    private fun findInFileSystem(programName: String): Program? {
        //TODO: remove static reference!!!
        return RpgSystem.getProgram(programName)
    }

    private fun findInPackages(programName: String): Program? {
        return javaInteropPackages.asSequence().map { packageName ->
            try {
                val javaClass = this.javaClass.classLoader.loadClass("$packageName.$programName")
                instantiateProgram(javaClass)
            } catch (e: Exception) {
                null
            }

        }.filter { it != null }.firstOrNull()
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