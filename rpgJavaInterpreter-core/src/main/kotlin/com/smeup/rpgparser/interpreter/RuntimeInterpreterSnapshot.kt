package com.smeup.rpgparser.interpreter

import kotlinx.serialization.Serializable

/**
 * An instance of this object will be used by the client to inform jariko about
 * interpreter state just before the EXFMT statement execution.
 */
@Serializable
data class RuntimeInterpreterSnapshot(val id: String) {
    companion object {
        fun random(): RuntimeInterpreterSnapshot {
            return RuntimeInterpreterSnapshot("EXFMT-1234")
        }
    }
}