package com.smeup.rpgparser.utils

import java.io.OutputStream

// TODO check encoding issues
class StringOutputStream() : OutputStream() {

    private val charList = mutableListOf<Char>()

    override fun write(b: Int) {
        charList.add(b.toChar())
    }

    override fun toString() = String(charList.toCharArray())

    val written: Boolean
        get() = charList.size > 0
}
