package com.smeup.rpgparser.execution

import com.smeup.rpgparser.utils.measureAndPrint
import java.io.BufferedReader
import java.io.InputStreamReader

// TODO describe what this program does
// TODO support option to add element to rpg program finders
object SimpleShell {

    private val exitCommands = hashSetOf("exit", "quit", "signoff", "off")

    var inputStream = System.`in`

    fun repl(r: (programName: String, programArgs: List<String>) -> Unit) {
        var commandLine: String
        val console = BufferedReader(InputStreamReader(inputStream))

        while (true) {
            print("rpg>")
            commandLine = console.readLine().trim()
            run {
                if (exitCommands.contains(commandLine.toLowerCase())) {
                    println("Goodbye")
                    return@repl
                }
                val args = commandLine.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (args.isNotEmpty()) {
                    measureAndPrint {
                        r(args[0], args.toList().subList(1, args.size))
                    }
                }
            }
        }
    }
}
