package com.smeup.rpgparser.parsetreetoast

import com.smeup.rpgparser.MuteParser
import com.smeup.rpgparser.RpgParser
import com.smeup.rpgparser.ast.*
import com.smeup.rpgparser.facade.RpgParserFacade
import com.smeup.rpgparser.interpreter.DataDefinition
import com.smeup.rpgparser.interpreter.LogEntry
import com.smeup.rpgparser.interpreter.Value
import com.strumenta.kolasu.model.Position
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


fun injectMuteAnnotationToStatements( statements : List<Statement> , start : Int, end: Int,
                                map: Map<Int, MuteParser.MuteLineContext>)  : List<MuteAnnotationResolved> {

    // Consider only the annotation in the scope
    val filtered: Map<Int, MuteParser.MuteLineContext> = map.filterKeys {
        it in start..end
    }
    // makes a consumable list of annotation
    val mutesToProcess: MutableMap<Int, MuteParser.MuteLineContext> = filtered.toSortedMap()
    val mutesResolved : MutableList<MuteAnnotationResolved> = mutableListOf()

    // Vist each statment
    statements.forEach {

        val resolved  = it.accept(mutesToProcess,start,end)
        mutesResolved.addAll( resolved )

        resolved.forEach {
            mutesToProcess.remove(it.muteLine)
        }
    }
    // at the end the mutesToProcess collection should be empty
    // otherwise it means the remaining annotations can't be attached
    // to any statement
    mutesToProcess.forEach {
        print("Could not attach the annotation @line ${it.key}")
    }
    return mutesResolved

}
fun injectMuteAnnotationToDataDefinitions(definitions: List<DataDefinition>,map: Map<Int, MuteParser.MuteLineContext>) {


    if(definitions.size > 0) {
        val start : Int = definitions.first().position!!.start.line
        val end   : Int = definitions.last().position!!.end.line
        // Consider only the annotation in the scope
        val filtered: Map<Int, MuteParser.MuteLineContext> = map.filterKeys {
            it in start..end
        }
        // makes a consumable list of annotation
        val mutesToProcess: MutableMap<Int, MuteParser.MuteLineContext> = filtered.toSortedMap()

        definitions.forEach {
            val toRemove = it.accept(mutesToProcess,start,end)

            toRemove.forEach {
                mutesToProcess.remove(it)
            }
        }
    }

}


fun CompilationUnit.injectMuteAnnotation(parseTreeRoot: RpgParser.RContext,
                                         mutes: Map<Int, MuteParser.MuteLineContext>) : List<MuteAnnotationResolved>  {

    val resolved : MutableList<MuteAnnotationResolved> = mutableListOf()
    //injectMuteAnnotationHelper( this.dataDefinitions,)
    //injectMuteAnnotationToDataDefinitions(this.dataDefinitions,mutes)
    // Process the main body statements
    resolved.addAll(injectMuteAnnotationToStatements( this.main.stmts,
                                this.main.stmts.position()!!.start.line,
                                this.main.stmts.position()!!.end.line,
                                mutes))
    // Process subroutines body statements
    this.subroutines.forEach {
        resolved.addAll(injectMuteAnnotationToStatements( it.stmts,
                                    it.position!!.start.line,
                                    it.position!!.end.line,
                                    mutes))
    }

    return resolved
}


fun acceptBody(body: List<Statement>,mutes: MutableMap<Int, MuteParser.MuteLineContext>, start: Int = 0, end: Int) : MutableList<MuteAnnotationResolved> {
    val muteAttached: MutableList<MuteAnnotationResolved> = mutableListOf()

    // Process the body statements
    body.forEach {
        val toRemove = it.accept(mutes, start, end)
        toRemove.forEach {
            mutes.remove(it.muteLine)
            muteAttached.add(it)
        }
    }

    return muteAttached
}