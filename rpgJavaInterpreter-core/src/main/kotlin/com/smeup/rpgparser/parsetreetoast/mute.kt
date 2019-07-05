package com.smeup.rpgparser.parsetreetoast

import com.smeup.rpgparser.MuteParser
import com.smeup.rpgparser.RpgParser
import com.smeup.rpgparser.ast.*
import com.smeup.rpgparser.interpreter.LogEntry
import com.smeup.rpgparser.interpreter.Value
import com.strumenta.kolasu.mapping.toPosition
import com.strumenta.kolasu.model.Position
import com.strumenta.kolasu.model.ReferenceByName
import com.strumenta.kolasu.model.pos

data class MuteAnnotationExecutionLogEntry(val annotation: MuteAnnotation, var result : Value) : LogEntry() {
    override fun toString(): String {
        return when(annotation) {
            is MuteComparisonAnnotation -> "executing MuteComparisonAnnotation: ${annotation.position} ${result} ${annotation.val1} ${annotation.comparison} ${annotation.val2} "
            else -> this.toString()
        }
    }
}

fun MuteParser.LiteralContext.toAst(conf : ToAstConfiguration = ToAstConfiguration()): Expression {
    return StringLiteral(this.content?.text ?: "", toPosition(conf.considerPosition))
}

fun MuteParser.NumberContext.toAst( conf : ToAstConfiguration = ToAstConfiguration() ) : Expression {
    require(this.NumberPart().isEmpty())
    require(this.MINUS() == null)
    val text = this.NUMBER().text
    return if (text.contains('.')) {
        RealLiteral(text.toDouble(), this.toPosition(conf.considerPosition))
    } else {
        IntLiteral(text.toLong(), this.toPosition(conf.considerPosition))
    }
}

fun MuteParser.IdentifierContext.toAst( conf : ToAstConfiguration = ToAstConfiguration() ) : Expression {
    return DataRefExpr(variable = ReferenceByName(this.text), position = toPosition(conf.considerPosition))
}

fun MuteParser.ExpressionContext.toAst(conf : ToAstConfiguration = ToAstConfiguration() ) : Expression {
    return when {
        this.number() != null -> this.number().toAst(conf)
        this.identifier() != null -> this.identifier().toAst(conf)
        this.literal() != null -> this.literal().toAst(conf)
        else -> {
            TODO(this.toString())
        }
    }
}


fun MuteParser.MuteLineContext.toAst(conf : ToAstConfiguration = ToAstConfiguration(), position: Position? = null ) : MuteAnnotation {
    return when(val annotation = this.muteAnnotation()) {
        is MuteParser.MuteComparisonAnnotationContext -> {
            MuteComparisonAnnotation(annotation.val1.toAst(conf), annotation.val2.toAst(conf), Comparison.valueOf(annotation.cp.text),
                    position = position )
        }
        else -> TODO(this.text.toString())
    }
}


fun injectMuteAnnotationHelper(statments : List<Statement>,
                               mutes: Map<Int, MuteParser.MuteLineContext>) {
    statments.forEach {
        // the mute annotation must be attached to the next statement
        // TODO maurizio develop a  better strategy to find the closest next statement
        var line = it.position!!.start.line + 1
        if( line in mutes ) {
            var mute = mutes[line]
            it.muteAnnotations.add( mute!!.toAst(  position = pos(line,mute.start.charPositionInLine,line,mute.stop.charPositionInLine)) )

        }
    }
}

fun CompilationUnit.injectMuteAnnotation(parseTreeRoot: RpgParser.RContext,
                                         mutes: Map<Int, MuteParser.MuteLineContext>) {
    // TODO implement a better statement explorer for subroutines
    injectMuteAnnotationHelper(this.main.stmts,mutes)


}