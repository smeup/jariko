package com.smeup.rpgparser.evaluation

import com.smeup.dbnative.ConnectionConfig
import com.smeup.rpgparser.AbstractTest

open class DBPerformanceTest : AbstractTest() {

    private fun createConnectionConfig(): ConnectionConfig? {
        val url: String? = System.getenv("JRK_TEST_DB_URL")
        val user: String? = System.getenv("JRK_TEST_DB_USR")
        val password: String? = System.getenv("JRK_TEST_DB_PWD")
        val driver: String? = System.getenv("JRK_TEST_DB_DRIVER")
        return if (url != null && user != null && password != null && driver != null) {
            ConnectionConfig(
                fileName = "*",
                url = url!!,
                user = user!!,
                password = password!!,
                driver = driver
            )
        } else {
            null
        }
    }
}