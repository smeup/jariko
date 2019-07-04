package com.smeup.rpgparser

import com.smeup.rpgparser.RpgParser.*
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.ast.CompilationUnit
import com.smeup.rpgparser.ast.DataRefExpr
import com.smeup.rpgparser.ast.Expression
import com.smeup.rpgparser.facade.RpgParserFacade
import com.smeup.rpgparser.interpreter.Function
import com.smeup.rpgparser.parsetreetoast.ToAstConfiguration
import com.smeup.rpgparser.parsetreetoast.injectMuteAnnotation
import com.smeup.rpgparser.parsetreetoast.toAst
import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.ReferenceByName
import org.antlr.v4.runtime.Lexer
import org.antlr.v4.runtime.Token
import org.apache.commons.io.input.BOMInputStream
import java.io.InputStream
import java.lang.IllegalStateException
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// Used only to get a class to be used for getResourceAsStream
class Dummy

fun assertIsIntValue(value: Value, intValue: Long) {
    assertTrue(value is IntValue, "IntValue expected but found instead $value")
    assertEquals(intValue, (value as IntValue).value)
}

fun inputStreamFor(exampleName: String) : InputStream {
    val resourceStream = Dummy::class.java.getResourceAsStream("/$exampleName.rpgle")
    if (resourceStream == null) {
        throw RuntimeException("$exampleName not found")
    }
    return BOMInputStream(resourceStream)
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

fun assertCanBeParsed(exampleName: String, withMuteSupport: Boolean = false) : RContext {
    val result = RpgParserFacade()
            .apply { this.muteSupport = withMuteSupport }
            .parse(inputStreamFor(exampleName))
    assertTrue(result.correct,
            message = "Errors: ${result.errors.joinToString(separator = ", ")}")
    return result.root!!.rContext
}

fun assertASTCanBeProduced(exampleName: String, considerPosition : Boolean = false,
                           withMuteSupport: Boolean = false) : CompilationUnit {
    val parseTreeRoot = assertCanBeParsed(exampleName, withMuteSupport)
    val ast = parseTreeRoot.toAst(ToAstConfiguration(
            considerPosition = considerPosition))
    if (withMuteSupport) {
        if (!considerPosition) {
            throw IllegalStateException("Mute annotations can be injected only when retaining the position")
        }
        //ast.injectMuteAnnotation(parseTreeRoot, )
    }
    return ast
}

fun assertCodeCanBeParsed(code: String) : RContext {
    val result = RpgParserFacade().parse(inputStreamForCode(code))
    assertTrue(result.correct,
            message = "Errors: ${result.errors.joinToString(separator = ", ")}")
    return result.root!!.rContext
}

fun assertExpressionCanBeParsed(code: String) : ExpressionContext {
    val result = RpgParserFacade().parseExpression(inputStreamForCode(code))
    assertTrue(result.correct,
            message = "Errors: ${result.errors.joinToString(separator = ", ")}")
    return result.root!!
}

fun expressionAst(code: String) : Expression {
    return assertExpressionCanBeParsed(code).toAst(ToAstConfiguration(considerPosition = false))
}

fun assertStatementCanBeParsed(code: String) : StatementContext {
    val result = RpgParserFacade().parseStatement(inputStreamForCode(code))
    if (!result.correct) {
        val lexingResult = RpgParserFacade().lex(inputStreamForCode(code))
        if (lexingResult.correct) {
            println("Lexing completed successfully:")
            println("CODE <<<$code>>>")
            lexingResult.root!!.forEach {
                println(" * ${RpgLexer.VOCABULARY.getDisplayName(it.type)} '${it.text}'")
            }
        } else {
            println("Issue already at the lexical level")
        }
    }
    assertTrue(result.correct,
            message = "Errors: ${result.errors.joinToString(separator = ", ")}")
    return result.root!!
}

fun CompilationUnit.assertDataDefinitionIsPresent(name: String,
                                                  dataType: Type,
                                                  fields: List<FieldDefinition> = emptyList()) : DataDefinition {
    assertTrue(this.hasDataDefinition(name), message = "Data definition $name not found in Compilation Unit")
    val dataDefinition = this.getDataDefinition(name)
    assertEquals(dataType, dataDefinition.type)
    assertEquals(fields, dataDefinition.fields)
    return dataDefinition
}

fun assertToken(expectedTokenType: Int, expectedTokenText: String, token: Token, trimmed: Boolean = true) {
    assertEquals(expectedTokenType, token.type)
    if (trimmed) {
        val expected = expectedTokenText.trim()
        val actual = token.text.trim()
        assertEquals(expected, actual, "Expected <$expected> but got <$actual>")
    } else {
        assertEquals(expectedTokenText, token.text)
    }
}

fun dataRef(name:String) = DataRefExpr(ReferenceByName(name))

class CollectorSystemInterface : SystemInterface {
    val displayed = LinkedList<String>()
    val programs = HashMap<String, Program>()
    val functions = HashMap<String, Function>()

    override fun findProgram(name: String) = programs[name]
    override fun findFunction(globalSymbolTable: SymbolTable, name: String) = functions[name]

    override fun display(value: String) {
        displayed.add(value)
    }
}

fun execute(cu: CompilationUnit,
            initialValues: Map<String, Value>, systemInterface: SystemInterface? = null,
            traceMode : Boolean = false,
            cycleLimit: Int? = null) : InternalInterpreter {
    val interpreter = InternalInterpreter(systemInterface ?: DummySystemInterface)
    interpreter.traceMode = traceMode
    interpreter.cycleLimit = cycleLimit
    try {
        interpreter.execute(cu, initialValues)
    } catch (e: InterruptForDebuggingPurposes) {
        // nothing to do here
    }
    return interpreter
}
