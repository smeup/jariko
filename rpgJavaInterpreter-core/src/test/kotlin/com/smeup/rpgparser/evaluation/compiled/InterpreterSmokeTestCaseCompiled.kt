package com.smeup.rpgparser.evaluation.compiled

import com.smeup.rpgparser.evaluation.InterpreterSmokeTestCase

class InterpreterSmokeTestCaseCompiled : InterpreterSmokeTestCase() {

    override fun useCompiledVersion() = true
}