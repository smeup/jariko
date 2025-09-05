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

package com.smeup.rpgparser.execution

import com.smeup.dbnative.manager.DBFileFactory
import com.smeup.rpgparser.experimental.ExperimentalFeaturesFactory
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.logging.AnalyticsLoggingContext
import com.smeup.rpgparser.parsing.ast.Subroutine
import com.smeup.rpgparser.parsing.facade.CopyBlocks
import com.strumenta.kolasu.model.ReferenceByName
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * Execution context allows to propagate, in simple and safe mode, some useful information, that could be
 * used in all phase of program execution.
 * @see MainExecutionContext#execute
 *
 * */
object MainExecutionContext {
    // default values in case jariko is not called from the command line program
    private val context: ThreadLocal<Context> by lazy { ThreadLocal<Context>() }
    private val noContextIdProvider: AtomicInteger by lazy { AtomicInteger() }
    private val noContextAttributes: MutableMap<String, Any> by lazy { mutableMapOf() }
    private val noConfiguration: Configuration by lazy { Configuration() }
    private val noProgramStack: Stack<RpgProgram> by lazy { Stack<RpgProgram>() }
    private val noParsingProgramStack: Stack<ParsingProgram> by lazy { Stack<ParsingProgram>() }
    private val noProgramSubroutineStack: Stack<ReferenceByName<Subroutine>> by lazy { Stack<ReferenceByName<Subroutine>>() }
    //

    // If for some reason we have problems in MainExecutionContext.execute set this variable to true
    // in order to restore previous behaviour
    private val denyRecursiveMainContextExecution = false

    /**
     * Call this method to execute e program in execution context environment.
     * Your program will be able to gain access to the attributes available in the entire life cycle of program execution
     * @param configuration The configuration
     * @param systemInterface The system interface. If [SystemInterface.getConfiguration] is not null that
     * value has priority over the parameter configuration
     * @param mainProgram The execution logic.
     * @see getAttributes
     * @see getConfiguration
     * @see getMemorySliceMgr
     * */
    fun <T> execute(
        configuration: Configuration = Configuration(),
        systemInterface: SystemInterface,
        mainProgram: (context: Context) -> T,
    ): T {
        val isRootContext = context.get() == null
        if (denyRecursiveMainContextExecution) {
            require(context.get() == null) { "Context execution already created" }
        }
        val memorySliceMgr =
            if (isRootContext) {
                if (configuration.memorySliceStorage == null) {
                    null
                } else {
                    MemorySliceMgr(configuration.memorySliceStorage)
                }
            } else {
                null
            }
        try {
            if (isRootContext) {
                context.set(
                    Context(
                        configuration = configuration,
                        memorySliceMgr = memorySliceMgr,
                        systemInterface = systemInterface,
                    ),
                )
            }
            return mainProgram
                .runCatching {
                    val ctx = context.get()
                    val callback = ctx.configuration.jarikoCallback
                    val trace = JarikoTrace(JarikoTraceKind.MainExecutionContext)
                    callback.traceBlock(trace) { invoke(ctx) }
                }.onFailure {
                    if (isRootContext) memorySliceMgr?.afterMainProgramInterpretation(false)
                }.onSuccess {
                    if (isRootContext) memorySliceMgr?.afterMainProgramInterpretation(true)
                }.getOrThrow()
        } finally {
            if (isRootContext) {
                context.get()?.dbFileFactory?.close()
                context.remove()
            }
        }
    }

    /**
     * @return execution context attributes
     * */
    fun getAttributes(): MutableMap<String, Any> = context.get()?.attributes ?: noContextAttributes

    /**
     * @return logging context
     */
    fun getAnalyticsLoggingContext() = context.get()?.logging

    /**
     * @return a new unique identifier
     */
    fun newId(): Int =
        if (context.get() != null) {
            context.get().idProvider.getAndIncrement()
        } else {
            // In many tests, the parsing is called outside the execution context
            // It's not too wrong assume that over 32000 it can be reset idProvider
            // In this way doesn't fail the variables assignment when involved the experimental
            // symbol table
            if (noContextIdProvider.get() == 32000) {
                if (FeaturesFactory.newInstance() is ExperimentalFeaturesFactory) {
                    Exception("Reset idProvider").printStackTrace()
                }
                noContextIdProvider.set(0)
            }
            noContextIdProvider.getAndIncrement()
        }

    /**
     * @return an instance of jariko configuration.
     * First af all the configuration is searched in [SystemInterface].
     * */
    fun getConfiguration() = context.get()?.systemInterface?.getConfiguration() ?: context.get()?.configuration ?: noConfiguration

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
     * @return Execution function name
     */
    fun getExecutionFunctionName() = context.get()?.executionFunctionName ?: ""

    /**
     * Set execution function name
     */
    fun setExecutionFunctionName(executionFunctionName: String) {
        context.get()?.executionFunctionName = executionFunctionName
    }

    /**
     * @return The subroutine stack. This is an execution stack.
     */
    fun getSubroutineStack() = context.get()?.subroutineStack ?: noProgramSubroutineStack

    /**
     * Logs entries
     */
    fun log(renderer: LazyLogEntry) {
        context.get()?.renderLog(renderer)
    }

    /**
     * Get all the log handlers
     */
    val logHandlers get(): List<InterpreterLogHandler> = context.get()?.logHandlers ?: emptyList()

    /**
     * Checks if logging is enabled
     */
    val isLoggingEnabled get() = context.get()?.isLoggingEnabled ?: false

    /***
     * Get DB File Factory
     */
    fun getDBFileFactory(): DBFileFactory? = context.get()?.dbFileFactory

    /***
     * Get system interface
     */
    fun getSystemInterface(): SystemInterface? = context.get()?.systemInterface

    /**
     * Get source parsing stack.
     * */
    fun getParsingProgramStack() = context.get()?.parsingProgramStack ?: noParsingProgramStack

    /**
     * @return true if context is already created
     * */
    fun isCreated() = context.get() != null

    /**
     * @return true if error log channel is configured
     * */
    val isErrorChannelConfigured get() = context.get()?.logHandlers?.isErrorChannelConfigured() ?: false
}

data class Context(
    val attributes: MutableMap<String, Any> = mutableMapOf(),
    val idProvider: AtomicInteger = AtomicInteger(),
    val configuration: Configuration,
    val logging: AnalyticsLoggingContext = AnalyticsLoggingContext(),
    val memorySliceMgr: MemorySliceMgr? = null,
    val programStack: Stack<RpgProgram> = Stack<RpgProgram>(),
    val subroutineStack: Stack<ReferenceByName<Subroutine>> = Stack<ReferenceByName<Subroutine>>(),
    val systemInterface: SystemInterface,
    var executionProgramName: String? = null,
    var executionFunctionName: String? = null,
    val parsingProgramStack: Stack<ParsingProgram> = Stack<ParsingProgram>(),
    val dbFileFactory: DBFileFactory? =
        configuration.reloadConfig?.let {
            DBFileFactory(it.nativeAccessConfig)
        },
) {
    val logHandlers: MutableList<InterpreterLogHandler> by lazy {
        systemInterface.getAllLogHandlers()
    }

    fun renderLog(renderer: LazyLogEntry) {
        logHandlers.renderLog(renderer)
    }

    val isLoggingEnabled get() = logHandlers.isNotEmpty()
}

data class ParsingProgram(
    val name: String,
) {
    val parsingFunctionNameStack = Stack<String>()
    var copyBlocks: CopyBlocks? = null
    var sourceLines: List<String>? = null
    val attributes: MutableMap<String, Any> = mutableMapOf()
}
