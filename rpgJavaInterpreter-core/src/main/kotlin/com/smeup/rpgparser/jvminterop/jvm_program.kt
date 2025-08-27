package com.smeup.rpgparser.jvminterop

import com.smeup.rpgparser.interpreter.*
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.createType
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions

@Retention(AnnotationRetention.RUNTIME)
annotation class Size(
    val size: Int,
)

abstract class JvmProgramRaw(
    val name: String = "<UNNAMED JVM PROGRAM>",
    val params: List<ProgramParam>,
) : Program {
    constructor(name: String, vararg p: ProgramParam) : this(name, p.asList())

    override fun params() = params
}

/**
 * Allows to mock JvmProgram
 * @param name Program name
 * @param params Program param list
 * @param executionLogic If you want you can implement here execution logic. Default return an empty list
 * */
class JvmMockProgram(
    val name: String,
    private val params: List<ProgramParam> = emptyList(),
    private val executionLogic: (
        systemInterface: SystemInterface,
        params: LinkedHashMap<String, Value>,
    ) -> List<Value> = { _: SystemInterface, _: LinkedHashMap<String, Value> ->
        emptyList()
    },
) : Program {
    override fun params() = params

    override fun execute(
        systemInterface: SystemInterface,
        params: LinkedHashMap<String, Value>,
    ): List<Value> = executionLogic.invoke(systemInterface, params)
}

abstract class JvmProgramByReflection : Program {
    companion object {
        private val methods = HashMap<KClass<*>, KFunction<*>?>()

        fun runMethod(kClass: KClass<*>): KFunction<*>? =
            methods.computeIfAbsent(kClass) {
                val runMethods = kClass.functions.filter { it.name == "run" }.toList()
                if (runMethods.size != 1) {
                    null
                } else {
                    runMethods[0]
                }
            }
    }

    override fun params(): List<ProgramParam> {
        val f = runMethod(this::class)
        if (f == null) {
            throw RuntimeException("No run method found")
        } else {
            val params =
                f.parameters.filter {
                    it.kind != KParameter.Kind.INSTANCE &&
                        it.type != SystemInterface::class.createType()
                }
            return params.map { it.toProgramParam() }
        }
    }

    override fun execute(
        systemInterface: SystemInterface,
        params: LinkedHashMap<String, Value>,
    ): List<Value> {
        val f = runMethod(this::class)
        if (f == null) {
            throw RuntimeException("No run method found")
        } else {
            val paramsMap = HashMap<KParameter, Any?>()
            f.parameters.forEach {
                paramsMap[it] =
                    when {
                        it.kind == KParameter.Kind.INSTANCE -> this
                        it.type == SystemInterface::class.createType() -> systemInterface
                        else -> {
                            params[it.toProgramParam().name]!!.toJavaValue(it)
                        }
                    }
            }
            val result = f.callBy(paramsMap)
            // TODO process result into list of values
            return emptyList()
        }
    }
}

private fun Value.toJavaValue(parameter: KParameter): Any =
    when (parameter.type) {
        String::class.createType() -> this.asString().value
        Int::class.createType() -> this.asInt().value
        else -> TODO("We do not know how to convert a parameter of type ${parameter.type}")
    }

private fun KParameter.toProgramParam(): ProgramParam = ProgramParam(this.name!!, this.toRpgType())

private fun KParameter.toRpgType(): Type =
    when (this.type) {
        String::class.createType() -> {
            StringType(
                this.findAnnotation<Size>()?.size
                    ?: throw RuntimeException("Size annotation required for string param ${this.name}"),
                true,
            )
        }
        Int::class.createType() -> {
            NumberType(
                this.findAnnotation<Size>()?.size
                    ?: throw RuntimeException("Size annotation required for int param ${this.name}"),
                0,
            )
        }
        else -> TODO(this.type.toString())
    }
