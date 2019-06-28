package com.smeup.rpgparser.jvminterop

import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.interpreter.Function
import com.smeup.rpgparser.rgpinterop.RpgSystem
import java.io.PrintStream
import java.lang.Exception
import java.util.*
import kotlin.reflect.KFunction1
import kotlin.reflect.full.isSubclassOf

open class JavaSystemInterface(private val outputStream: PrintStream,
                          private val programSource: KFunction1<@ParameterName(name = "programName") String, RpgProgram>?) : SystemInterface {

    //For calls from Java programs
    constructor (os: PrintStream) : this(os, RpgSystem::getProgram)
    constructor(): this(System.out)

    val consoleOutput = LinkedList<String>()
    private val javaInteropPackages = LinkedList<String>()
    private val programs = HashMap<String, Program?>()

    fun addJavaInteropPackage(packageName: String) {
        javaInteropPackages.add(packageName)
    }

    override fun display(value: String) {
        consoleOutput.add(value)
        outputStream.println(value)
    }

    override fun findProgram(name: String): Program? {
        return programs.computeIfAbsent(name) {
            findInPackages(name) ?: findInFileSystem(name)
        }
    }

    private fun findInFileSystem(programName: String): Program? {
        return programSource?.call(programName)
    }

    private fun findInPackages(programName: String): Program? {
        return javaInteropPackages.asSequence().map { packageName ->
            try {
                val javaClass = this.javaClass.classLoader.loadClass("$packageName.$programName")
                instantiateProgram(javaClass)
            } catch (e: Throwable) {
                null
            }

        }.filter { it != null }.firstOrNull()
    }

    open fun instantiateProgram(javaClass: Class<*>): Program? {
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