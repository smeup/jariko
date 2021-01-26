package com.smeup.rpgparser.db

import org.junit.Ignore
import org.junit.Test

class Chain2FilesDBTestCompiled() : Chain2FilesDBTest() {
    override fun useCompiledVersion() = true

    // TODO verify why this test fails
    @Test
    @Ignore("Currently test fails")
    override fun executeCHAIN2FILE() {
        super.executeCHAIN2FILE()
    }
}