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
import com.smeup.rpgparser.parsing.facade.CopyBlocks
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.HashMap
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds

/**
 * Execution context allows to propagate, in simple and safe mode, some useful information, that could be
 * used in all phase of program execution.
 * @see MainExecutionContext#execute
 *
 * */
object MainExecutionContext {

    // default values in case jariko is not called from the command line program
    private val context: ThreadLocal<Context> by lazy { ThreadLocal<Context>() }
    private val loggingContext: ThreadLocal<LoggingContext> by lazy { ThreadLocal<LoggingContext>() }
    private val noContextIdProvider: AtomicInteger by lazy { AtomicInteger() }
    private val noContextAttributes: MutableMap<String, Any> by lazy { mutableMapOf<String, Any>() }
    private val noConfiguration: Configuration by lazy { Configuration() }
    private val noProgramStack: Stack<RpgProgram> by lazy { Stack<RpgProgram>() }
    private val noParsingProgramStack: Stack<ParsingProgram> by lazy { Stack<ParsingProgram>() }
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
        mainProgram: (context: Context) -> T
    ): T {
        val isRootContext = context.get() == null
        if (denyRecursiveMainContextExecution) {
            require(context.get() == null) { "Context execution already created" }
        }
        val memorySliceMgr = if (isRootContext) {
            if (configuration.memorySliceStorage == null) {
                null
            } else {
                MemorySliceMgr(configuration.memorySliceStorage)
            }
        } else null
        try {
            if (isRootContext) {
                context.set(
                    Context(
                        configuration = configuration,
                        memorySliceMgr = memorySliceMgr,
                        systemInterface = systemInterface
                    )
                )
                loggingContext.set(
                    LoggingContext()
                )
            }
            return mainProgram.runCatching {
                invoke(context.get())
            }.onFailure {
                if (isRootContext) memorySliceMgr?.afterMainProgramInterpretation(false)
            }.onSuccess {
                if (isRootContext) memorySliceMgr?.afterMainProgramInterpretation(true)
            }.getOrThrow()
        } finally {
            if (isRootContext) {
                context.get()?.dbFileFactory?.close()
                context.remove()
                loggingContext.remove()
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
    fun getLoggingContext() = loggingContext.get() ?: null

    /**
     * @return a new unique identifier
     */
    fun newId(): Int {
        return if (context.get() != null) {
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
     * Logs entries
     */
    fun log(renderer: LazyLogEntry) {
        context.get()?.renderLog(renderer)
    }

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
}

data class Context(
    val attributes: MutableMap<String, Any> = mutableMapOf<String, Any>(),
    val idProvider: AtomicInteger = AtomicInteger(),
    val configuration: Configuration,
    val memorySliceMgr: MemorySliceMgr? = null,
    val programStack: Stack<RpgProgram> = Stack<RpgProgram>(),
    val systemInterface: SystemInterface,
    var executionProgramName: String? = null,
    var executionFunctionName: String? = null,
    val parsingProgramStack: Stack<ParsingProgram> = Stack<ParsingProgram>(),
    val dbFileFactory: DBFileFactory? = configuration.reloadConfig?.let {
        DBFileFactory(it.nativeAccessConfig)
    }
) {

    private val logHandlers: MutableList<InterpreterLogHandler> by lazy {
        systemInterface.getAllLogHandlers()
    }

    fun renderLog(renderer: LazyLogEntry) {
        logHandlers.renderLog(renderer)
    }
}

data class ParsingProgram(val name: String) {
    val parsingFunctionNameStack = Stack<String>()
    var copyBlocks: CopyBlocks? = null
    var sourceLines: List<String>? = null
    val attributes: MutableMap<String, Any> = mutableMapOf()
}

data class LoggingContext(
    private val timeUsageByStatement: HashMap<String, UsageMeasurement> = hashMapOf(),
    private var renderingTime: Duration = Duration.ZERO
) {

    data class UsageMeasurement(val duration: Duration, val hit: Long) {
        companion object {
            fun new(): UsageMeasurement = UsageMeasurement(
                duration = Duration.ZERO,
                hit = 0
            )
        }
    }

    fun recordRenderingDuration(executionTime: Duration) {
        renderingTime += executionTime
    }

    fun recordStatementDuration(name: String, executionTime: Duration) {
        val timeUsageEntry = timeUsageByStatement.getOrPut(name) { UsageMeasurement.new() }

        timeUsageByStatement[name] = UsageMeasurement(
            duration = timeUsageEntry.duration + executionTime,
            hit = timeUsageEntry.hit + 1
        )
    }

    fun generateTimeUsageByStatementReport(): String {
        val sorted = timeUsageByStatement
            .toList()
            .sortedBy { it.second.duration }

        val totalTime = sorted.fold(Duration.ZERO) { acc, curr -> acc + curr.second.duration }

        val format = DecimalFormat("##.#%")

        return sorted.joinToString(separator = "\n") {
            val statementName = it.first
            val duration = it.second.duration
            val timePercentage = format.format(duration / totalTime)
            val hit = it.second.hit
            val average = (duration.inWholeNanoseconds / it.second.hit).nanoseconds
            "${statementName}: $duration - $timePercentage - hit $hit times - avg. $average per usage"
        }
    }

    fun generateReport() =
        buildString {
            append('\n')
            append("Execution time is distributed as following:")
            append('\n')
            append(generateTimeUsageByStatementReport())
            append("\n\n")
            append("Of which ${getLogRenderingTime()} was used to render logs")
            append("\n")
        }

    fun getLogRenderingTime() = renderingTime
}