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


fun injectMuteAnnotationHelper( statements : List<Statement> , start : Int,
                                map: Map<Int, MuteParser.MuteLineContext>) {

    // makes a consumable list of annotation
    val mutes: MutableMap<Int, MuteParser.MuteLineContext> = map.toSortedMap()

    // Vist each statment
    statements.forEach {
        val stmt = it
        println("${stmt}")
        val toRemove = it.accept(mutes,start)

        toRemove.forEach {
            mutes.remove(it)
        }
    }
    // at the end the mutes collectiom must be empty
    // if not empty means the annotation can't be attached
    // to any statement
    print("")

}


fun CompilationUnit.injectMuteAnnotation(parseTreeRoot: RpgParser.RContext,
                                         mutes: Map<Int, MuteParser.MuteLineContext>) {
    // TODO maurizio implement a statement explorer
    injectMuteAnnotationHelper(this.main.stmts,1,mutes)
    this.subroutines.forEach {
        injectMuteAnnotationHelper(it.stmts,it.position!!.start.line,mutes)
    }


}
