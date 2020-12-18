package com.jariko.samples

import com.andreapivetta.kolor.yellow
import com.smeup.dbnative.ConnectionConfig
import com.smeup.dbnative.DBNativeAccessConfig
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.ReloadConfig
import com.smeup.rpgparser.execution.getProgram
import com.smeup.rpgparser.interpreter.DataStructValue
import com.smeup.rpgparser.interpreter.StringValue
import com.smeup.rpgparser.jvminterop.JavaSystemInterface
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import java.io.File

private fun createConnectionConfig(): ConnectionConfig? {
    val url: String? = System.getenv("JRK_TEST_DB_URL")
    val user: String? = System.getenv("JRK_TEST_DB_USR")
    val password: String? = System.getenv("JRK_TEST_DB_PWD")
    val driver: String? = System.getenv("JRK_TEST_DB_DRIVER")
    return if (url != null && user != null && password != null && driver != null) {
        ConnectionConfig(
            fileName = "*",
            url = url,
            user = user,
            password = password,
            driver = driver
        )
    } else {
        null
    }
}

// todo mock java program
// example of how to pass a DS to jariko
fun execHowToPassDS() {
    val programName = "X1_X21_05M"
    val result = createConnectionConfig()?.let { connectionConfig ->

        var dataStructValue: DataStructValue? = null
        val configuration = Configuration(
            reloadConfig = ReloadConfig(DBNativeAccessConfig(listOf(connectionConfig)))
        )
        val resource = {}.javaClass.getResource("/rpg/$programName.rpgle")
        val programFinders = listOf(DirRpgProgramFinder(directory = File(resource.path).parentFile))
        getProgram(
            nameOrSource = programName,
            systemInterface = JavaSystemInterface(),
            programFinders = programFinders
        ).singleCall(
            parmsProducer = {
                // Crate DS fields
                val dataStructFields = mapOf(
                    "\$UIBPG" to StringValue("EXB"),
                    "\$UIBFU" to StringValue(programName),
                    "\$UIBME" to StringValue("MAT.CAL"),
                    "\$UIBK2" to StringValue("ERB")
                )
                mapOf("£UIBDS" to DataStructValue.createInstance(it, "£UIBDS", dataStructFields))
            },
            configuration = configuration
        )
    }
    if (result == null) {
        print("Skipped due to missing environment settings".yellow())
    }
}

fun main() {
    execHowToPassDS()
}