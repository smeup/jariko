package com.smeup.rpgparser.db

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.db.utilities.execute
import org.hsqldb.Server
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

open class StoreTest() : AbstractTest() {

    companion object {
        lateinit var server: Server
    }

    @Before
    fun init() {
        execute(listOf(createEMPLOYEE()))
    }

    @After
    fun tearOff() {
        execute(listOf(dropEMPLOYEE()))
    }

    @Test
    fun testWrite() {
        outputOfDBPgm(
            programName = "db/WRITE01",
            metadata = listOf(createEmployeeMetadata())
        )
    }

    // TODO Waiting for evaluation about reload potential issue
    @Ignore
    @Test
    fun testUpdate() {
        outputOfDBPgm(
            programName = "db/UPDATE01",
            metadata = listOf(createEmployeeMetadata())
        )
    }
}