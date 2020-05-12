package com.smeup.rpgparser.execution

import java.util.concurrent.atomic.AtomicInteger

/**
 * Execution context allows to propagate, in simple and safe mode, some useful informations, that could be
 * used in all phase of program execution.
 * @see MainExecutionContext#execute
 *
 * */
object MainExecutionContext {

    private val context = ThreadLocal<Context>()
    // idProvider in missing context (i.e. main class) environment
    private val noContextIdProvider = AtomicInteger()
    // attributes in missing context (i.e. main class) environment
    private val noContextAttributes = mapOf<String, Any>()

    /**
     * Call this method to execute e program in ExecutionContext environment.
     * Your program will be able to gain access to attributes available in the entire life cycle of program execution
     * @see #getAttributes
     * */
    fun <T> execute(mainProgram: () -> T): T {
        try {
            require(
                context.get() == null
            ) { "Context execution already created" }
            context.set(Context())
            return mainProgram.invoke()
        } finally {
            context.remove()
        }
    }

    /**
     * @return execution context attributes
     * */
    fun getAttributes() = context.get()?.attributes ?: noContextAttributes

    /**
     * @return a new unique identifier
     */
    fun newId() = context.get()?.idProvider?.getAndIncrement() ?: noContextIdProvider.getAndIncrement()
}

private class Context {
    val attributes = mapOf<String, Any>()
    val idProvider = AtomicInteger()
}