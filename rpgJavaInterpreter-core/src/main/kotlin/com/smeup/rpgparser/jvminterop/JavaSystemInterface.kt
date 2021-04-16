package com.smeup.rpgparser.jvminterop

import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.interpreter.Function
import com.smeup.rpgparser.parsing.ast.Api
import com.smeup.rpgparser.parsing.ast.ApiDescriptor
import com.smeup.rpgparser.parsing.ast.ApiId
import com.smeup.rpgparser.parsing.ast.MuteAnnotationExecuted
import com.smeup.rpgparser.parsing.facade.Copy
import com.smeup.rpgparser.parsing.facade.CopyId
import com.smeup.rpgparser.rpginterop.RpgSystem
import java.io.PrintStream
import java.util.*
import kotlin.reflect.KFunction1
import kotlin.reflect.full.isSubclassOf

open class JavaSystemInterface(
    private val outputStream: PrintStream,
    private val programSource: KFunction1<@ParameterName(name = "programName") String, RpgProgram>?,
    private val copySource: (copyId: CopyId) -> Copy? = { null },
    var loggingConfiguration: LoggingConfiguration? = null,
    val rpgSystem: RpgSystem = RpgSystem()
) : SystemInterface {

    override var executedAnnotationInternal: LinkedHashMap<Int, MuteAnnotationExecuted> = LinkedHashMap<Int, MuteAnnotationExecuted>()
    override var extraLogHandlers: MutableList<InterpreterLogHandler> = mutableListOf()

    override fun loggingConfiguration(): LoggingConfiguration? {
        return this.loggingConfiguration
    }

    // For calls from Java programs
    private constructor (os: PrintStream, rpgSystem: RpgSystem) : this(os, rpgSystem::getProgram, { copyId -> rpgSystem.getCopy(copyId) }, rpgSystem = rpgSystem)
    constructor (os: PrintStream) : this(os, RpgSystem())
    constructor() : this(System.out)

    private val consoleOutputList = LinkedList<String>()

    val consoleOutput: List<String>
        get() = consoleOutputList.map(String::trimEnd)

    fun clearConsole() {
        consoleOutputList.clear()
    }

    private val javaInteropPackages = LinkedList<String>()
    private val programs = HashMap<String, Program?>()
    private val copies = HashMap<CopyId, Copy?>()
    private val apiDescriptors = HashMap<ApiId, ApiDescriptor>()
    private val apis = HashMap<ApiId, Api>()
    private val functions = HashMap<String, Function>()

    fun addJavaInteropPackage(packageName: String) {
        javaInteropPackages.add(packageName)
    }

    override fun display(value: String) {
        consoleOutputList.add(value)
        outputStream.println(value)
    }

    override fun findProgram(name: String): Program? {
        return programs.computeIfAbsent(name) {
            findInPackages(name) ?: findInFileSystem(name)
        }
    }

    override fun findCopy(copyId: CopyId): Copy? {
        return copies.computeIfAbsent(copyId) {
            copySource.invoke(copyId)
        }
    }

    private fun findInFileSystem(programName: String): Program? {
        return programSource?.invoke(programName)
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

    override fun findFunction(globalSymbolTable: ISymbolTable, name: String): Function? {
        return functions.computeIfAbsent(name) {
            findFunctionInPackages(name) ?: RpgFunction.fromCurrentProgram(name)
        }
    }

    private fun findFunctionInPackages(programName: String): Function? {
        return javaInteropPackages.asSequence().map { packageName ->
            try {
                val javaClass = this.javaClass.classLoader.loadClass("$packageName.$programName")
                instantiateFunction(javaClass)
            } catch (e: Throwable) {
                null
            }
        }.filter { it != null }.firstOrNull()
    }

    open fun instantiateFunction(javaClass: Class<*>): Function? {
        return if (javaClass.kotlin.isSubclassOf(Program::class)) {
            javaClass.kotlin.constructors.filter { it.parameters.isEmpty() }.first().call() as Function
        } else {
            null
        }
    }

    override fun addExecutedAnnotation(line: Int, annotation: MuteAnnotationExecuted) {
        executedAnnotationInternal[line] = annotation
    }

    override fun getExecutedAnnotation(): LinkedHashMap<Int, MuteAnnotationExecuted> {
        return executedAnnotationInternal
    }

    override fun findApiDescriptor(apiId: ApiId): ApiDescriptor {
        return apiDescriptors.computeIfAbsent(apiId) {
            rpgSystem.findApiDescriptor(apiId)
        }
    }

    override fun findApi(apiId: ApiId): Api {
        return apis.computeIfAbsent(apiId) {
            rpgSystem.findApi(apiId)
        }
    }
}
