package com.smeup.rpgparser.utils

import org.junit.Test
import kotlin.test.assertEquals

class MiscTest {

    @Test
    fun chunkLineTest() {
        assertEquals(listOf<String>("a1", "b1", "c1", "a2", "b2", "c2"),
                     listOf<String>("a1b1c1d1", "a2b2c2xxxxxxx").chunkAs(3, 2))

        assertEquals(listOf<String>("a1", "b1", "a2", "b2"),
                     listOf<String>("a1b1", "a2b2").chunkAs(3, 2))
    }

    @Test
    fun resizeToTest() {
        assertEquals(listOf<String>("a", "b", "c"),
                     listOf<String>("a", "b", "c", "z", "z").resizeTo(3, "x"))

        assertEquals(listOf<String>("a", "b", "x"),
                     listOf<String>("a", "b").resizeTo(3, "x"))
    }
}