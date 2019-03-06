package com.smeup.rpgparser

import com.smeup.rpgparser.RpgParser.RContext
import org.antlr.v4.runtime.Lexer
import org.antlr.v4.runtime.Token
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

fun assertCanBeLexed(exampleName: String, onlyVisibleTokens: Boolean = true) : List<Token> {
    val result = RpgParserFacade().lex(inputStreamFor(exampleName))
    assertTrue(result.correct,
            message = "Errors: ${result.errors.joinToString(separator = ", ")}")
    return if (onlyVisibleTokens) {
        result.root!!.filter { it.channel != Lexer.HIDDEN }
    } else {
        result.root!!
    }
}

fun assertCanBeParsed(exampleName: String) : RContext {
    val result = RpgParserFacade().parse(inputStreamFor(exampleName))
    assertTrue(result.correct,
            message = "Errors: ${result.errors.joinToString(separator = ", ")}")
    return result.root!!
}

fun assertCodeCanBeParsed(code: String) : RContext {
    val result = RpgParserFacade().parse(inputStreamForCode(code))
    assertTrue(result.correct,
            message = "Errors: ${result.errors.joinToString(separator = ", ")}")
    return result.root!!
}

fun CompilationUnit.assertDataDefinitionIsPresent(name: String, dataType: DataType, size: Int, decimals: Int = 0, arrayLength: Expression = IntLiteral(1)) {
    assertTrue(this.hasDataDefinition(name), message = "Data definition $name not found in Compilation Unit")
    val dataDefinition = this.getDataDefinition(name)
    assertEquals(dataType, dataDefinition.dataType)
    assertEquals(size, dataDefinition.size)
    assertEquals(decimals, dataDefinition.decimals)
    assertEquals(arrayLength, dataDefinition.arrayLength)
}

fun assertToken(expectedTokenType: Int, expectedTokenText: String, token: Token, trimmed: Boolean = true) {
    assertEquals(expectedTokenType, token.type)
    if (trimmed) {
        assertEquals(expectedTokenText.trim(), token.text.trim())
    } else {
        assertEquals(expectedTokenText, token.text)
    }
}