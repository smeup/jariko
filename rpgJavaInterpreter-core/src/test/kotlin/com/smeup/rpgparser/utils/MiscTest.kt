package com.smeup.rpgparser.utils

import org.junit.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.fail

class MiscTest {

    @Test
    fun chunkLineTest() {
        assertEquals(listOf("a1", "b1", "c1", "a2", "b2", "c2"),
            listOf("a1b1c1d1", "a2b2c2xxxxxxx").chunkAs(3, 2))

        assertEquals(listOf("a1", "b1", "a2", "b2"),
            listOf("a1b1", "a2b2").chunkAs(3, 2))
    }

    @Test
    fun resizeToTest() {
        assertEquals(listOf("a", "b", "c"),
            listOf("a", "b", "c", "z", "z").resizeTo(3, "x"))

        assertEquals(listOf("a", "b", "x"),
            listOf("a", "b").resizeTo(3, "x"))
    }

    @Test
    fun divideAtIndexTest() {
        assertEquals(Pair("", "abcde"), "abcde".divideAtIndex(0))
        assertEquals(Pair("", "abcde"), "abcde".divideAtIndex(-1))
        assertEquals(Pair("abcde", ""), "abcde".divideAtIndex(20))
        assertEquals(Pair("abcde", ""), "abcde".divideAtIndex(4))
        assertEquals(Pair("a", "bcde"), "abcde".divideAtIndex(1))
        assertEquals(Pair("ab", "cde"), "abcde".divideAtIndex(2))
    }

    @Test
    fun repeatWithMaxSizeTest() {
        assertEquals("abcab", "abc".repeatWithMaxSize(5))
        assertEquals("aaaaa", "a".repeatWithMaxSize(5))
        assertEquals("ab", "abc".repeatWithMaxSize(2))
    }

    @Test
    fun runIfNotEmptyTest() {
        val nullSting: String? = null
        nullSting.runIfNotEmpty {
            fail("Should not be executed")
        }

        " ".runIfNotEmpty {
            fail("Should not be executed")
        }

        var result = "Lambda not invoked"
        "some string".runIfNotEmpty {
            result = "OK"
        }
        assertEquals("OK", result)
    }

    @Test
    fun compilerTest() {
        val dummyDir = "/asasuj8kkagabatakapa"
        assertEquals(
            true,
            compile(src = File(dummyDir), File(dummyDir).parentFile).isEmpty()
        )

        val program = "     ** String of 50 chars:\n" +
                "     D Msg             S             50\n" +
                "     ** Implicit declaration of a number with 3 digits, 2 of them are decimals\n" +
                "     C                   clear                   X                 3 2\n" +
                "     C                   eval      x = %ABS(-1)\n" +
                "     C                   eval      Msg = 'X is ' + %CHAR(X)\n" +
                "     C                   dsply                   Msg\n" +
                "     C                   SETON                                          LR"
        val src = File.createTempFile("MYPGM", ".rpgle")
        src.writeText(program)
        println("Compiling $src")
        val compiledFiles = compile(src = src, src.parentFile)
        println("Compiled files: $compiledFiles")
        assertEquals(
            1,
            compiledFiles.size
        )
        src.delete()
        compiledFiles.forEach {
            it.delete()
        }
    }
}
