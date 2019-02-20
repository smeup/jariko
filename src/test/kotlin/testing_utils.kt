package com.smeup.rpgparser

import java.io.InputStream
import kotlin.test.assertTrue

class Dummy

fun inputStreamFor(exampleName: String) : InputStream {
    return Dummy::class.java.getResourceAsStream("/$exampleName.txt")
}

fun assertCanBeLexed(exampleName: String) {
    val result = RpgParserFacade().lex(inputStreamFor(exampleName))
    assertTrue(result.correct,
            message = "Errors: ${result.errors.joinToString(separator = ", ")}")
}

fun assertCanBeParsed(exampleName: String) {
    assertTrue(RpgParserFacade().parse(inputStreamFor(exampleName)).correct)
}