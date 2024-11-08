package com.smeup.rpgparser.smeup

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execution.Configuration
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

abstract class MockTest : AbstractTest() {
    lateinit var smeupConfig: Configuration

    @BeforeTest
    open fun setUp() {
        smeupConfig = Configuration()
    }

    @AfterTest()
    open fun tearDown() {}
}