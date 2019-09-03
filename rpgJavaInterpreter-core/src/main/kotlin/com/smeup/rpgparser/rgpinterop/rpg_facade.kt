package com.smeup.rpgparser.rgpinterop

import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.jvminterop.Size
import com.smeup.rpgparser.logging.Logger
import com.smeup.rpgparser.logging.configureLog
import com.smeup.rpgparser.logging.defaultLoggingConfiguration
import java.io.File
import java.lang.RuntimeException
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.system.measureTimeMillis

annotation class Param(val name: String)

private val <R> KProperty<R>.rpgName: String
    get() {
        val param = this.findAnnotation<Param>()
        return param?.name ?: this.name
    }

interface ProgramNameSource<P> {
    fun nameFor(rpgFacade: RpgFacade<P>): String
}

class ClassProgramName<P> : ProgramNameSource<P> {
    override fun nameFor(rpgFacade: RpgFacade<P>): String = rpgFacade.javaClass.simpleName
}

abstract class RpgFacade<P> (
    val programNameSource: ProgramNameSource<P> = ClassProgramName<P>(),
    val systemInterface: SystemInterface
) {

    private var logHandlers = mutableListOf<InterpreterLogHandler>()

    private val programInterpreter = ProgramInterpreter(systemInterface.addExtraLogHandlers(logHandlers))
    private val programName by lazy { programNameSource.nameFor(this) }
    protected val rpgProgram by lazy { RpgSystem.getProgram(programName) }

    private fun configureLogHandlers() {
        logHandlers = systemInterface.getAllLogHandlers()
    }

    fun singleCall(params: P): P? {
        configureLogHandlers()

        val initialValues = toInitialValues(params)

        logHandlers.log(StartProgramLog(programName, initialValues))
        val elapsed = measureTimeMillis {
            programInterpreter.execute(rpgProgram, initialValues)
        }
        logHandlers.log(EndProgramLog(programName, elapsed))
        return toResults(params, initialValues)
    }

    protected open fun toResults(params: P, resultValues: LinkedHashMap<String, Value>): P {
        val any: Any = params!!
        val kclass = any::class
        val initialValues = HashMap<String, Value>()
        // TODO This is a fake implementation
//        kclass.memberProperties.forEach {
//            toRpgValue(it, it.call(params)) = resultValues[it.rpgName]
//        }
        return params
    }

    protected open fun toInitialValues(params: P): LinkedHashMap<String, Value> {
        val any: Any = params!!
        val kclass = any::class
        val initialValues = LinkedHashMap<String, Value>()
        kclass.memberProperties.forEach {
            initialValues[it.rpgName] = toRpgValue(it, it.call(params))
        }
        return initialValues
    }

    private fun propertyStringValue(property: KProperty1<Any, *>, container: Any): String {
        val value = property.get(container)
        if (value is String) {
            return value
        } else {
            TODO()
        }
    }

    private fun toRpgValue(property: KType, jvmValue: Any?): Value {
        return when {
            jvmValue is String -> StringValue(jvmValue)
            else -> {
                if (jvmValue is String) {
                    StringValue(jvmValue)
                } else {
                    val parts = LinkedList<String>()
                    jvmValue!!.javaClass.kotlin.memberProperties.forEach {
                        val stringLength = (it.rpgType() as StringType).length.toInt()
                        parts.add(propertyStringValue(it, jvmValue).padEnd(stringLength, PAD_CHAR))
                    }
                    return StringValue(parts.joinToString(separator = ""))
                }
            }
        }
    }

    private fun toRpgValue(property: KProperty<*>, jvmValue: Any?): Value {
        return when {
            property.returnType == String::class.createType() -> {
                val s = jvmValue as String
                StringValue(s)
            }
            property.returnType.classifier is KClass<*> && (property.returnType.classifier as KClass<*>).qualifiedName == "kotlin.Array" -> {
                val jvmArray = jvmValue as Array<*>
                val elementType = property.returnType.arguments[0].type!!
                val nElements = property.findAnnotation<Size>()?.size ?: throw RuntimeException("Size expected for property ${property.name}")
                val rpgArray = createArrayValue(elementType.toRpgType(), nElements) {
                    if (it < jvmArray.size) {
                        toRpgValue(elementType, jvmArray[it])
                    } else {
                        blankValue(elementType.toRpgType())
                    }
                }
                return rpgArray
            }
            else -> {
                val classifier = property.returnType.classifier
                println((classifier as KClass<*>).qualifiedName == "kotlin.Array")
                val array = Array<Any>::class
                TODO("Property $property")
            }
        }
    }
}

private fun KType.toRpgType(size: Size? = null): Type {
    return when {
        this.classifier == String::class -> {
            StringType(size!!.size.toLong())
        }
        this.classifier is KClass<*> -> {
            val length = (this.classifier as KClass<*>).memberProperties.map { it.rpgLength() }.foldRight(0L) { it, acc -> it + acc }
            StringType(length)
        }
        else -> TODO("$this")
    }
}

private fun <R> KProperty<R>.rpgLength(): Long {
    val size = this.findAnnotation<Size>()
    if (size != null) {
        return size.size.toLong()
    }
    return this.returnType.toRpgType().size
}

private fun <R> KProperty<R>.rpgType(): Type {
    val size = this.findAnnotation<Size>()
    return this.returnType.toRpgType(size)
}
