package com.smeup.rpgparser.execution

import com.smeup.rpgparser.interpreter.DummyMemorySliceStorage
import com.smeup.rpgparser.interpreter.MemorySliceMgr
import java.util.concurrent.atomic.AtomicInteger

/**
 * Execution context allows to propagate, in simple and safe mode, some useful informations, that could be
 * used in all phase of program execution.
 * @see MainExecutionContext#execute
 *
 * */
object MainExecutionContext {

    private val context = ThreadLocal<Context>()
    // idProvider if missing context (i.e. main class) environment
    private val noContextIdProvider = AtomicInteger()
    // attributes if missing context (i.e. main class) environment
    private val noContextAttributes = mutableMapOf<String, Any>()
    // configuration if missing context (i.e. main class) environment
    private val noConfiguration = Configuration()
    // memorySliceMgr if missing context (i.e. main class) environment
    private val noMemorySliceMgr = MemorySliceMgr(DummyMemorySliceStorage())

    /**
     * Call this method to execute e program in ExecutionContext environment.
     * Your program will be able to gain access to the attributes available in the entire life cycle of program execution
     * @see #getAttributes
     * @see #getConfiguration
     * @see #getMemorySliceMgr
     * */
    fun <T> execute(configuration: Configuration = Configuration(), mainProgram: () -> T): T {
            require(
                context.get() == null
            ) { "Context execution already created" }
        val memorySliceStorage = configuration.memorySliceStorage ?: DummyMemorySliceStorage()
        val memorySliceMgr = MemorySliceMgr(memorySliceStorage)
        try {
            context.set(Context(configuration = configuration, memorySliceMgr = memorySliceMgr))
            return mainProgram.runCatching {
                invoke()
            }.onFailure {
                memorySliceMgr.afterMainProgramInterpretation(false)
            }.onSuccess {
                memorySliceMgr.afterMainProgramInterpretation(true)
            }.getOrThrow()
        } finally {
            context.remove()
        }
    }

    /**
     * @return execution context attributes
     * */
    fun getAttributes(): MutableMap<String, Any> = context.get()?.attributes ?: noContextAttributes

    /**
     * @return a new unique identifier
     */
    fun newId() = context.get()?.idProvider?.getAndIncrement() ?: noContextIdProvider.getAndIncrement()

    /**
     * @return an instance of jariko configuration
     * */
    fun getConfiguration() = context.get()?.configuration ?: noConfiguration

    /**
    * @return an instance of memory slice manager
    * */
    fun getMemorySliceMgr() = context.get()?.memorySliceMgr ?: noMemorySliceMgr
}

private data class Context(
    val attributes: MutableMap<String, Any> = mutableMapOf<String, Any>(),
    val idProvider: AtomicInteger = AtomicInteger(),
    val configuration: Configuration,
    val memorySliceMgr: MemorySliceMgr
)
