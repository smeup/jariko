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

package com.smeup.rpgparser.rpginterop

import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.MainExecutionContext
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.jvminterop.Size
import com.smeup.rpgparser.parsing.facade.dumpSource
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

annotation class Param(
    val name: String,
)

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

abstract class RpgFacade<P>(
    val programNameSource: ProgramNameSource<P> = ClassProgramName<P>(),
    val systemInterface: SystemInterface,
) {
    private var logHandlers = mutableListOf<InterpreterLogHandler>()

    private val programInterpreter = ProgramInterpreter(systemInterface.addExtraLogHandlers(logHandlers))
    private val programName by lazy { programNameSource.nameFor(this) }
    private lateinit var program: Program

    private fun configureLogHandlers() {
        logHandlers = systemInterface.getAllLogHandlers()
    }

    fun singleCall(
        params: P,
        configuration: Configuration = Configuration(),
    ): P? =
        MainExecutionContext.execute(configuration = configuration, systemInterface = systemInterface) {
            configureLogHandlers()
            it.executionProgramName = programName
            // I need to create rpgProgram inside MainExecutionContext to fix issue on experimental symbol table.
            // Issue is due to the reusing of id generator in the AST creation, for which, if I create AST outside MainExecutionContext
            // is never reinitialized, and it creates an id number (keyid associated to variable) greater than maximum allowed by experimental symbol table
            program = systemInterface.findProgram(programName)!!
            val initialValues = toInitialValues(program, params)
            when (program) {
                is RpgProgram -> programInterpreter.execute(program as RpgProgram, initialValues)
                else -> {
                    val values = program.execute(systemInterface, initialValues)
                    initialValues.keys.forEachIndexed { index, key -> initialValues[key] = values[index] }
                }
            }
            configuration.options?.apply {
                if (muteSupport) {
                    kotlin
                        .runCatching {
                            systemInterface.assertMutesSucceed(programName = programName)
                        }.onFailure { error ->
                            when (program) {
                                is RpgProgram ->
                                    (program as RpgProgram).cu.source?.apply {
                                        System.err.println(error.message)
                                        System.err.println(this.dumpSource())
                                    }
                            }
                            throw error
                        }
                }
            }
            toResults(params, initialValues)
        }

    protected open fun toResults(
        params: P,
        resultValues: LinkedHashMap<String, Value>,
    ): P {
        params!!
//        val kclass = any::class
//        val initialValues = HashMap<String, Value>()
        // TODO This is a fake implementation
//        kclass.memberProperties.forEach {
//            toRpgValue(it, it.call(params)) = resultValues[it.rpgName]
//        }
        return params
    }

    protected open fun toInitialValues(
        program: Program,
        params: P,
    ): LinkedHashMap<String, Value> {
        val any: Any = params!!
        val klass = any::class
        val initialValues = LinkedHashMap<String, Value>()
        klass.memberProperties.forEach {
            initialValues[it.rpgName] = toRpgValue(it, it.call(params))
        }
        return initialValues
    }

    private fun propertyStringValue(
        property: KProperty1<Any, *>,
        container: Any,
    ): String {
        val value = property.get(container)
        if (value is String) {
            return value
        } else {
            TODO()
        }
    }

    private fun toRpgValue(jvmValue: Any?): Value {
        return when {
            jvmValue is String -> StringValue(jvmValue)
            else -> {
                if (jvmValue is String) {
                    StringValue(jvmValue)
                } else {
                    val parts = LinkedList<String>()
                    jvmValue!!.javaClass.kotlin.memberProperties.forEach {
                        val stringLength = (it.rpgType() as StringType).length
                        parts.add(propertyStringValue(it, jvmValue).padEnd(stringLength, ' '))
                    }
                    return StringValue(parts.joinToString(separator = ""))
                }
            }
        }
    }

    private fun toRpgValue(
        property: KProperty<*>,
        jvmValue: Any?,
    ): Value {
        return when {
            property.returnType == String::class.createType() -> {
                val s = jvmValue as String
                StringValue(s)
            }
            property.returnType.classifier is KClass<*> && (property.returnType.classifier as KClass<*>).qualifiedName == "kotlin.Array" -> {
                val jvmArray = jvmValue as Array<*>
                val elementType = property.returnType.arguments[0].type!!
                val nElements =
                    property.findAnnotation<Size>()?.size ?: throw RuntimeException("Size expected for property ${property.name}")
                val rpgArray =
                    createArrayValue(elementType.toRpgType(), nElements) {
                        if (it < jvmArray.size) {
                            toRpgValue(jvmArray[it])
                        } else {
                            elementType.toRpgType().blank()
                        }
                    }
                return rpgArray
            }
            else -> {
                val classifier = property.returnType.classifier
                println((classifier as KClass<*>).qualifiedName == "kotlin.Array")
                TODO("Property $property")
            }
        }
    }
}

private fun KType.toRpgType(size: Size? = null): Type =
    when {
        this.classifier == String::class -> {
            StringType(size!!.size, false)
        }
        this.classifier is KClass<*> -> {
            val length = (this.classifier as KClass<*>).memberProperties.map { it.rpgLength() }.foldRight(0) { it, acc -> it + acc }
            StringType(length, false)
        }
        else -> TODO("$this")
    }

private fun <R> KProperty<R>.rpgLength(): Int {
    val size = this.findAnnotation<Size>()
    if (size != null) {
        return size.size
    }
    return this.returnType.toRpgType().size
}

private fun <R> KProperty<R>.rpgType(): Type {
    val size = this.findAnnotation<Size>()
    return this.returnType.toRpgType(size)
}
