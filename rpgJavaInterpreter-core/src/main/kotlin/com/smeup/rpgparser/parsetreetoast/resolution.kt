package com.smeup.rpgparser.parsetreetoast

import com.smeup.rpgparser.ast.*
import com.smeup.rpgparser.com.smeup.rpgparser.utils.enrichExceptionWith
import com.strumenta.kolasu.model.*

private fun CompilationUnit.findInStatementDataDefinitions() {
    // TODO could they be also annidated?
    // TODO could they also be in subroutines?
    this.main.stmts.filterIsInstance(StatementThatCanDefineData::class.java).forEach {
        this.addInStatementDataDefinitions(it.dataDefinition())
    }
}


fun CompilationUnit.resolve() {
    this.assignParents()

    this.findInStatementDataDefinitions()

    this.specificProcess(DataRefExpr::class.java) { dre ->
        if (!dre.variable.resolved) {
            require(dre.variable.tryToResolve(this.allDataDefinitions,caseInsensitive = true)) {
                "Data reference not resolved: ${dre.variable.name} at ${dre.position}"
            }
        }
    }
    this.specificProcess(ExecuteSubroutine::class.java) { esr ->
        if (!esr.subroutine.resolved) {
            require(esr.subroutine.tryToResolve(this.subroutines,caseInsensitive = true)) {
                "Subroutine call not resolved: ${esr.subroutine.name}"
            }
        }
    }
    // replace FunctionCall with ArrayAccessExpr where it makes sense
    this.specificProcess(FunctionCall::class.java) { fc ->
        if (fc.args.size == 1) {
            val data = this.allDataDefinitions.firstOrNull { it.name == fc.function.name }
            if (data != null) {
                enrichExceptionWith(fc.position) {
                    fc.replace(ArrayAccessExpr(
                            array = DataRefExpr(ReferenceByName(fc.function.name, referred = data)),
                            index = fc.args[0],
                            position = fc.position))
                }
            }
        }
    }

    // replace equality expr in For init with Assignments
    this.specificProcess(ForStmt::class.java) { fs ->
        if (fs.init is EqualityExpr) {
            val ee = fs.init as EqualityExpr
            fs.init.replace(AssignmentExpr(ee.left as AssignableExpression, ee.right, ee.position))
        }
    }

    this.specificProcess(EvalStmt::class.java) { s ->
        if (s.expression is EqualityExpr) {
            s.expression.replace((s.expression as EqualityExpr).toAssignment())
        }
    }

    this.specificProcess(PlistParam::class.java) { pp ->
        if (!pp.param.resolved) {
            require(pp.param.tryToResolve(this.allDataDefinitions, caseInsensitive = true)) {
                "Plist Param not resolved: ${pp.param.name} at ${pp.position}"
            }
        }
    }
}

private fun EqualityExpr.toAssignment(): AssignmentExpr {
    return AssignmentExpr(
            this.left as AssignableExpression,
            this.right,
            this.position
    )
}
