package com.smeup.rpgparser.parsing

import com.smeup.rpgparser.MuteLexer
import com.smeup.rpgparser.assertASTCanBeProduced
import com.smeup.rpgparser.parsing.facade.RpgParserFacade
import com.strumenta.kolasu.model.Point
import com.strumenta.kolasu.validation.Error
import com.strumenta.kolasu.validation.ErrorType
import java.util.*
import org.antlr.v4.runtime.*
import org.apache.commons.io.input.BOMInputStream
import org.junit.Ignore
import org.junit.Test
import kotlin.test.assertEquals

class RpgParserWithMuteSupportTest {
    // Please note the 8 leading spaces
    val comparisonAnnotation = "".padStart(8) + "VAL1(array(1)) VAL2(1) COMP(EQ)"
    // val comparisonAnnotation = "".padStart(8) + "VAL1(NUMBER1) VAL2(1) COMP(EQ)"

    // Test if the lexer extracts the expected number of tokens

    // TODO understand why this test does not pass
    @Test
    @Ignore
    fun muteAnnotationsAttributionLex() {

        // val preprocessed = preprocess(comparisonAnnotation)
        val errors = LinkedList<Error>()
        val lexer = MuteLexer(ANTLRInputStream(BOMInputStream(comparisonAnnotation.byteInputStream(Charsets.UTF_8))))
        lexer.removeErrorListeners()
        lexer.addErrorListener(object : BaseErrorListener() {
            override fun syntaxError(p0: Recognizer<*, *>?, p1: Any?, line: Int, charPositionInLine: Int, errorMessage: String?, p5: RecognitionException?) {
                errors.add(Error(ErrorType.LEXICAL, errorMessage
                        ?: "unspecified", position = Point(line, charPositionInLine).asPosition))
            }
        })
        val tokens = LinkedList<Token>()
        do {
            val t = lexer.nextToken()
            if (t == null) {
                break
            } else {
                tokens.add(t)
            }
        } while (t.type != Token.EOF)
        assertEquals(7, tokens.size)
    }

    // TODO understand why this test does not pass
    // Test if the parser returns errors
    @Test
    @Ignore
    fun muteAnnotationsAttributionParse() {
        val errors = LinkedList<Error>()
        val muteParser = RpgParserFacade().createMuteParser(BOMInputStream(comparisonAnnotation.byteInputStream(Charsets.UTF_8)), errors,
                longLines = true)

        val root = muteParser.muteLine()

        assertEquals(0, errors.size)
    }

    @Test
    fun muteAnnotationsAttribution() {
        val cu = assertASTCanBeProduced("MUTE05_02",
                considerPosition = true,
                withMuteSupport = true)

        print("")
    }
}
