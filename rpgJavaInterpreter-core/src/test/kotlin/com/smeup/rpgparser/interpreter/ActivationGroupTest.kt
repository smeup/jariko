package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.DEFAULT_ACTIVATION_GROUP_NAME
import com.smeup.rpgparser.execution.JarikoCallback
import com.smeup.rpgparser.execution.getProgram
import org.junit.Test
import kotlin.test.assertEquals

class ActivationGroupTest {

    /**
     * Assigned activation group name should be the default from configuration
     * */
    @Test
    fun testMainUnspecified() {
        val pgm = "     C                   SETON                                          RT\n"

        val conf = Configuration(
            jarikoCallback = JarikoCallback(
                getActivationGroup = {
                        _: String, associatedActivationGroup: ActivationGroup? ->
                    println("associatedActivationGroupName: ${associatedActivationGroup?.assignedName}")
                    assertEquals(Configuration().defaultActivationGroupName, associatedActivationGroup?.assignedName)
                    assertEquals(DEFAULT_ACTIVATION_GROUP_NAME, associatedActivationGroup?.assignedName)
                    null
                }
            )
        )
        val commandLineProgram = getProgram(pgm)
        commandLineProgram.singleCall(emptyList(), conf)
    }

    @Test
    fun testMainSpecified() {
        val pgm = "     H ACTGRP('MYACT')\n" +
                "     C                   SETON                                          RT"

        val conf = Configuration(
            jarikoCallback = JarikoCallback(
                getActivationGroup = {
                        _: String, associatedActivationGroup: ActivationGroup? ->
                    println("associatedActivationGroupName: ${associatedActivationGroup?.assignedName}")
                    assertEquals("MYACT", associatedActivationGroup?.assignedName)
                    null
                }
            )
        )
        val commandLineProgram = getProgram(pgm)
        commandLineProgram.singleCall(emptyList(), conf)
    }

}