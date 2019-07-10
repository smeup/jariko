package com.smeup.rpgparser.parsetreetoast

import com.smeup.rpgparser.MuteParser
import com.smeup.rpgparser.RpgParser
import com.smeup.rpgparser.ast.*
import com.smeup.rpgparser.facade.RpgParserFacade
import com.smeup.rpgparser.interpreter.LogEntry
import com.smeup.rpgparser.interpreter.Value
import com.strumenta.kolasu.mapping.toPosition
import com.strumenta.kolasu.model.Position
import com.strumenta.kolasu.model.ReferenceByName
import com.strumenta.kolasu.model.pos
import com.strumenta.kolasu.validation.Error
import org.apache.commons.io.input.BOMInputStream
import java.util.*

data class MuteAnnotationExecutionLogEntry(val annotation: MuteAnnotation, var result : Value) : LogEntry() {
    override fun toString(): String {
        return when(annotation) {
            is MuteComparisonAnnotation -> "executing MuteComparisonAnnotation: ${annotation.position} ${result} ${annotation.val1} ${annotation.comparison} ${annotation.val2} "
            else -> this.toString()
        }
    }
}


fun MuteParser.MuteLineContext.toAst(conf : ToAstConfiguration = ToAstConfiguration(), position: Position? = null ) : MuteAnnotation {
    val annotation = this.muteAnnotation()
    return when(annotation) {
        is MuteParser.MuteComparisonAnnotationContext -> {
            val errors = LinkedList<Error>()
            val val1 =  RpgParserFacade().createParser(BOMInputStream(("".padStart(8) + annotation.val1.text.substring(1,annotation.val1.text.lastIndex)).byteInputStream(Charsets.UTF_8)), errors, longLines = true)
                    .expression()
                    .toAst(conf)

            val val2 =  RpgParserFacade().createParser(BOMInputStream(("".padStart(8) + annotation.val2.text.substring(1,annotation.val2.text.lastIndex)).byteInputStream(Charsets.UTF_8)), errors, longLines = true)
                    .expression()
                    .toAst(conf)

            MuteComparisonAnnotation( val1, val2, Comparison.valueOf(annotation.cp.text.substring(1,annotation.cp.text.lastIndex)), position = position )

        }
        else -> TODO(this.text.toString())
    }
}


fun injectMuteAnnotationHelper( statments : List<Statement> ,
                                mutes: Map<Int, MuteParser.MuteLineContext>) {

    // Find the first statement after the mute line
    mutes.forEach { (line,mute) ->
        val followingStatement = statments.find {
            it.position!!.start.line > line
        } ?: throw NoSuchElementException("No statements after mute at line: ${line}" )
        if (followingStatement != null) {
            followingStatement.muteAnnotations.add( mute!!.toAst(  position = pos(line,mute.start.charPositionInLine,line,mute.stop.charPositionInLine)) )
        }
    }

}


fun CompilationUnit.injectMuteAnnotation(parseTreeRoot: RpgParser.RContext,
                                         mutes: Map<Int, MuteParser.MuteLineContext>) {
    // TODO implement a better statement explorer for subroutines


    this.subroutines.forEach {
        injectMuteAnnotationHelper(it.stmts,mutes)
    }

    injectMuteAnnotationHelper(this.main.stmts,mutes)
}
