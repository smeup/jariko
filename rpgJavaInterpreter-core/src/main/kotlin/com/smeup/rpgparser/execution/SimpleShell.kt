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

import com.smeup.rpgparser.utils.measureAndPrint
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

// TODO describe what this program does
// TODO support option to add element to rpg program finders
object SimpleShell {
    private val exitCommands = hashSetOf("exit", "quit", "signoff", "off")

    var inputStream: InputStream = System.`in`

    private val interactiveCommands = setOf("play", "run")

    private const val WELCOME_MSG = """
********************************************************        
** Welcome to Jariko Shell. Type help for suggestions **

rpg>
"""

    private const val HELP_MSG = """
You can type (@<rpgle_file_path> <args>|play|stop|exit):
 - <rpgle_file_path> followed by <args> (optional) if you want to run a program
 - play to start interactive interpretation
 - stop to stop interactive interpretation
 - exit to exit from Jariko Shell
 - help this help message
rpg>
"""
    private const val INTERACTIVE_MODE_MSG = "Interactive mode on, write or copy your code and then type run to execute program or stop if you want to return to console"

    fun repl(r: (programName: String, programArgs: List<String>) -> Unit) {
        var commandLine: String
        val console = BufferedReader(InputStreamReader(inputStream))
        var interactiveMode: Boolean? = null
        val content = StringBuilder()
        println()
        print(WELCOME_MSG.trim())
        while (true) {
            commandLine = console.readLine()
            run {
                if (commandLine.trim().isNotEmpty() && !(exitCommands + interactiveCommands).contains(commandLine.trim().toLowerCase()) &&
                    !commandLine.startsWith("@")) {
                    content.append(commandLine).append("\n")
                }
                val trimmed = commandLine.trim().toLowerCase()
                when {
                    trimmed == "play" -> {
                        interactiveMode = true
                        content.clear()
                        println(INTERACTIVE_MODE_MSG)
                    }
                    trimmed == "stop" -> {
                        interactiveMode = null
                        content.clear()
                        print("rpg>")
                    }
                    trimmed == "help" -> {
                        print(HELP_MSG.trim())
                        interactiveMode = null
                    }
                    exitCommands.contains(trimmed) -> {
                        println("Goodbye")
                        return@repl
                    }
                    trimmed.startsWith("@") -> {
                        interactiveMode = false
                    }
                    else -> if (interactiveMode == null) {
                        println("Command not recognized")
                        print("rpg>")
                    }
                }
                interactiveMode?.let {
                    if (!it) {
                        println("Executing...")
                        val args = commandLine.trim().split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        if (args.isNotEmpty()) {
                            measureAndPrint {
                                r(args[0].substring(1), args.toList().subList(1, args.size))
                            }
                            interactiveMode = null
                            print("rpg>")
                        }
                    } else {
                        if (commandLine == "run") {
                            if (content.isEmpty()) {
                                println("Type or paste your code")
                            } else {
                                println("Executing...")
                                measureAndPrint {
                                    r(content.toString(), emptyList())
                                }
                                content.clear()
                                println(INTERACTIVE_MODE_MSG)
                            }
                        }
                    }
                }
            }
        }
    }
}
