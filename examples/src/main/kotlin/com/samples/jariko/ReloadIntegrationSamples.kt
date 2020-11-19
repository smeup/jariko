package com.samples.jariko

import com.smeup.dbnative.ConnectionConfig
import com.smeup.dbnative.DBNativeAccessConfig
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.ReloadConfig
import com.smeup.rpgparser.execution.getProgram

fun callJarikoWithReload() {


    // create connection config
    val connectionsConfig = listOf(ConnectionConfig(
        fileName = "*", 
        url = "jdbc:blabla", 
        user = "user", 
        password = "password", 
        driver = "myDriverclass"
        )
    )

    // pass reloadConfig to jariko
    val config = Configuration(
        reloadConfig = ReloadConfig(
            nativeAccessConfig = DBNativeAccessConfig(connectionsConfig), 
            getMetadata = {_: String ->  null}
    ))

    // call jariko
    val inlineProgram = """
     D Z               S              1
     D Msg             S             12
     C                   EVAL      Z = 'A'
     C                   EVAL      Msg = %CHAR(Z)
     C     msg           dsply
     C                   SETON                                          RT
     """
    val commandLineProgram = getProgram(inlineProgram)
    commandLineProgram.singleCall(emptyList(), config)
}

fun main() {
    callJarikoWithReload()
}