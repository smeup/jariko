package com.smeup.rpgparser

import java.io.InputStream
import kotlin.test.assertTrue

// Used only to get a class to be used for getResourceAsStream
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
    val result = RpgParserFacade().parse(inputStreamFor(exampleName))
    assertTrue(result.correct,
            message = "Errors: ${result.errors.joinToString(separator = ", ")}")
}
