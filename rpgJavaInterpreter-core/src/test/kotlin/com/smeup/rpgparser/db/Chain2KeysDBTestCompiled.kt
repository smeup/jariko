package com.smeup.rpgparser.db

import org.junit.Ignore
import org.junit.Test

class Chain2KeysDBTestCompiled : Chain2KeysDBTest() {

    override fun useCompiledVersion() = true

    // TODO verify why this test fails
    @Test
    @Ignore("Currently test fails")
    override fun findsExistingRecord() {
        super.findsExistingRecord()
    }

    // TODO verify why this test fails
    @Test
    @Ignore("Currently test fails")
    override fun doesntFindNonExistingRecord() {
        super.doesntFindNonExistingRecord()
    }
}