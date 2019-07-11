package com.smeup.rpgparser

import java.io.*
import kotlin.system.measureTimeMillis


class SimpleShell {

    val exitCommands = hashSetOf<String>("exit", "quit", "signoff", "off")

    fun repl(r: (parms: Array<String>) -> Unit) {
        var commandLine: String
        val console = BufferedReader(InputStreamReader(System.`in`))

        while (true) {
            // read what the user entered
            print("rpg>")
            commandLine = console.readLine().trim()
            run {
                // if the user entered a return, just loop again
                if (exitCommands.contains(commandLine.toLowerCase())) {
                    println("Goodbye")
                    System.exit(0)
                }
                val args = commandLine.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (!args.isEmpty()) {
                    val timeElapsed = measureTimeMillis {
                        r(args)
                    }
                    println("Function executed in $timeElapsed milliseconds")
                }
            }
        }
    }
}

