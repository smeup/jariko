package com.smeup.rpgparser

import com.smeup.rpgparser.RpgParser.RContext
import java.io.InputStream
import java.nio.charset.StandardCharsets
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// Used only to get a class to be used for getResourceAsStream
class Dummy

fun inputStreamFor(exampleName: String) : InputStream {
    return Dummy::class.java.getResourceAsStream("/$exampleName.txt")
}

fun inputStreamForCode(code: String) : InputStream {
    return code.byteInputStream(StandardCharsets.UTF_8)
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

fun assertCodeCanBeParsed(code: String) : RContext {
    val result = RpgParserFacade().parse(inputStreamForCode(code))
    assertTrue(result.correct,
            message = "Errors: ${result.errors.joinToString(separator = ", ")}")
    return result.root!!
}

fun CompilationUnit.assertDataDefinitionIsPresent(name: String, dataType: DataType, size: Int, decimal: Int = 0, arrayLength: Int = 1) {
    assertTrue(this.hasDataDefinition(name), message = "Data definition $name not found in Compilation Unit")
    val dataDefinition = this.getDataDefinition(name)
    assertEquals(dataType, dataDefinition.dataType)
    assertEquals(size, dataDefinition.size)
    assertEquals(decimal, dataDefinition.decimal)
    assertEquals(arrayLength, dataDefinition.arrayLength)
}
