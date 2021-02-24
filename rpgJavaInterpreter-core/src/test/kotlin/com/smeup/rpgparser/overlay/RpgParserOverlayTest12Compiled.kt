package com.smeup.rpgparser.overlay

import org.junit.Test

class RpgParserOverlayTest12Compiled : RpgParserOverlayTest12() {

    override fun useCompiledVersion() = true

    @Test
    override fun parseMUTE12_01_runtime() {
        super.parseMUTE12_01_runtime()
    }
}