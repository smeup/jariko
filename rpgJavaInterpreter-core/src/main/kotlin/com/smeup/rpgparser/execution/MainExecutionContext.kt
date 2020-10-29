package com.smeup.rpgparser.execution

import com.smeup.rpgparser.interpreter.*
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * Execution context allows to propagate, in simple and safe mode, some useful informations, that could be
 * used in all phase of program execution.
 * @see MainExecutionContext#execute
 *
 * */
object MainExecutionContext {

    // default values in case jariko is not called from the command line program
    private val context: ThreadLocal<Context> by lazy { ThreadLocal<Context>() }
    private val noContextIdProvider: AtomicInteger by lazy { AtomicInteger() }
    private val noContextAttributes: MutableMap<String, Any> by lazy { mutableMapOf<String, Any>() }
    private val noConfiguration: Configuration by lazy { Configuration() }
    private val noProgramStack: Stack<RpgProgram> by lazy { Stack<RpgProgram>() }
    //

    /**
     * Call this method to execute e program in ExecutionContext environment.
     * Your program will be able to gain access to the attributes available in the entire life cycle of program execution
     * @see #getAttributes
     * @see #getConfiguration
     * @see #getMemorySliceMgr
     * */
    fun <T> execute(configuration: Configuration = Configuration(), systemInterface: SystemInterface, mainProgram: (context: Context) -> T): T {
            require(
                context.get() == null
            ) { "Context execution already created" }
        val memorySliceMgr = if (configuration.memorySliceStorage == null) {
            null
        } else {
            MemorySliceMgr(configuration.memorySliceStorage)
        }
        try {
            context.set(Context(configuration = configuration, memorySliceMgr = memorySliceMgr, systemInterface = systemInterface))
            return mainProgram.runCatching {
                invoke(context.get())
            }.onFailure {
                memorySliceMgr?.afterMainProgramInterpretation(false)
            }.onSuccess {
                memorySliceMgr?.afterMainProgramInterpretation(true)
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
    fun getMemorySliceMgr() = context.get()?.memorySliceMgr

    /**
     * @return program stack. This is an execution stack
     * */
    fun getProgramStack() = context.get()?.programStack ?: noProgramStack

    /**
     * @return Execution program name
     */
    fun getExecutionProgramName() = context.get()?.executionProgramName ?: ""

    /**
     * Set execution program name
     */
    fun setExecutionProgramName(executionProgramName: String) {
        context.get()?.executionProgramName = executionProgramName
    }

    /**
     * Logs entries
     */
    fun log(logEntry: LogEntry) {
        context.get()?.let { it.log(logEntry) }
    }
}

data class Context(
    val attributes: MutableMap<String, Any> = mutableMapOf<String, Any>(),
    val idProvider: AtomicInteger = AtomicInteger(),
    val configuration: Configuration,
    val memorySliceMgr: MemorySliceMgr? = null,
    val programStack: Stack<RpgProgram> = Stack<RpgProgram>(),
    val systemInterface: SystemInterface,
    var executionProgramName: String? = null
) {

    private val logHandlers: MutableList<InterpreterLogHandler> by lazy {
        systemInterface.getAllLogHandlers()
    }

    fun log(logEntry: LogEntry) {
        logHandlers.log(logEntry)
    }
}
