package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.adaptForTestCase
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.DEFAULT_ACTIVATION_GROUP_NAME
import com.smeup.rpgparser.execution.JarikoCallback
import com.smeup.rpgparser.execution.getProgram
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

open class ActivationGroupTest : AbstractTest() {

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
        conf.adaptForTestCase(this)
        val commandLineProgram = getProgram(pgm)
        commandLineProgram.singleCall(emptyList(), conf)
    }

    /**
     * Assigned activation group name should be the default from configuration
     * */
    @Test
    fun testUnspecifiedCaller() {
        val pgm = "     H ACTGRP(*CALLER)\n" +
                "     C                   SETON                                          RT"

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
        conf.adaptForTestCase(this)
        val commandLineProgram = getProgram(pgm)
        commandLineProgram.singleCall(emptyList(), conf)
    }

    /**
     * Assigned activation group name should be from declaration
     * */
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
        ).adaptForTestCase(this)
        val commandLineProgram = getProgram(pgm)
        commandLineProgram.singleCall(emptyList(), conf)
    }

    /**
     * Assigned activation group name in main and called program should be MYGRP
     * */
    @Test
    fun testCallCaller() {
        val programName = "ACTGRP_CAL_CALLER.rpgle"
        val path = javaClass.getResource("/$programName")
        // here I set the path from where jariko will search for the rpg sources
        val rpgProgramFinders = listOf(DirRpgProgramFinder(File(path.path).parentFile))
        val conf = Configuration(
            jarikoCallback = JarikoCallback(
                getActivationGroup = {
                        executingProgramName: String, associatedActivationGroup: ActivationGroup? ->
                    println("programName: $executingProgramName")
                    println("associatedActivationGroupName: ${associatedActivationGroup?.assignedName}")
                    assertEquals("MYGRP", associatedActivationGroup?.assignedName)
                    null
                }
            ),
            defaultActivationGroupName = "MYGRP"
        ).adaptForTestCase(this)
        val commandLineProgram = getProgram(nameOrSource = programName, programFinders = rpgProgramFinders)
        commandLineProgram.singleCall(emptyList(), conf)
    }
}