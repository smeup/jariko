package com.smeup.rpgparser.parsing.parsetreetoast

import com.smeup.rpgparser.interpreter.DBInterface
import com.smeup.rpgparser.interpreter.DataDefinition
import com.smeup.rpgparser.interpreter.type
import com.smeup.rpgparser.parsing.ast.*
import com.smeup.rpgparser.utils.enrichPossibleExceptionWith
import com.strumenta.kolasu.model.*
import com.strumenta.kolasu.validation.Error
import com.strumenta.kolasu.validation.ErrorType
import java.lang.RuntimeException
import java.util.*

private fun CompilationUnit.findInStatementDataDefinitions() {
    // TODO could they be also annidated?
    this.allStatements().filterIsInstance(StatementThatCanDefineData::class.java).forEach {
        this.addInStatementDataDefinitions(it.dataDefinition())
    }
}

private fun CompilationUnit.allStatements(): List<Statement> {
    val result = mutableListOf<Statement>()
    result.addAll(this.main.stmts)
    this.subroutines.forEach {
        result.addAll(it.stmts)
    }
    return result
}

private fun Node.resolveDataRefs(cu: CompilationUnit) {
    this.specificProcess(DataRefExpr::class.java) { dre ->
        if (!dre.variable.resolved) {

            if (dre.variable.name.contains('.')) {
                val ds = dre.variable.name.substring(0, dre.variable.name.indexOf("."))

                val fieldName = dre.variable.name.substring(dre.variable.name.indexOf(".") + 1)

                val resField = cu.allDataDefinitions.find { it.name.equals(fieldName, true) }
                dre.variable.referred = resField
            } else {
                require(dre.variable.tryToResolve(cu.allDataDefinitions, caseInsensitive = true)) {
                    "Data reference not resolved: ${dre.variable.name} at ${dre.position}"
                }
            }
        }
    }
}

private fun Node.resolveFunctionCalls(cu: CompilationUnit) {
    // replace FunctionCall with ArrayAccessExpr where it makes sense
    this.specificProcess(FunctionCall::class.java) { fc ->
        if (fc.args.size == 1) {
            val data = cu.allDataDefinitions.firstOrNull { it.name == fc.function.name }
            if (data != null) {
                enrichPossibleExceptionWith(fc.position) {
                    fc.replace(ArrayAccessExpr(
                            array = DataRefExpr(ReferenceByName(fc.function.name, referred = data)),
                            index = fc.args[0],
                            position = fc.position))
                }
            }
        }
    }
}

fun MuteAnnotation.resolveAndValidate(cu: CompilationUnit) {
    this.resolveDataRefs(cu)
    this.resolveFunctionCalls(cu)
}

/**
 * In case of semantic errors we could either raise exceptions or return a list of errors.
 */
fun CompilationUnit.resolveAndValidate(databaseInterface: DBInterface, raiseException: Boolean = true): List<Error> {
    this.resolve(databaseInterface)
    return this.validate(raiseException)
}

class SemanticErrorsException(val errors: List<Error>) : RuntimeException("Semantic errors found: $errors")

/**
 * In case of semantic errors we could either raise exceptions or return a list of errors.
 */
private fun CompilationUnit.validate(raiseException: Boolean = true): List<Error> {
    val errors = LinkedList<Error>()
    // TODO validate SubstExpr for assignability
    // TODO check initial value in DoStmt
    // No need to check Eval directly, we check the AssignmentExpr instead
    this.specificProcess(AssignmentExpr::class.java) {
        val targetType = it.target.type()
        val valueType = it.value.type()
        if (!targetType.canBeAssigned(valueType)) {
            errors.add(Error(ErrorType.SEMANTIC, "Invalid assignement: cannot assign ${it.value} having type $valueType to ${it.target} having type $targetType", it.position))
        }
    }
    if (errors.isNotEmpty()) {
        throw SemanticErrorsException(errors)
    }
    return errors
}

private fun CompilationUnit.resolve(databaseInterface: DBInterface) {
    this.assignParents()

    this.findInStatementDataDefinitions()

    this.databaseInterface = databaseInterface

    this.resolveDataRefs(this)

    this.specificProcess(ExecuteSubroutine::class.java) { esr ->
        if (!esr.subroutine.resolved) {
            require(esr.subroutine.tryToResolve(this.subroutines, caseInsensitive = true)) {
                "Subroutine call not resolved: ${esr.subroutine.name}"
            }
        }
    }
    this.specificProcess(QualifiedAccessExpr::class.java) { qae ->
        if (!qae.field.resolved) {
            if (qae.container is DataRefExpr) {
                val dataRef = qae.container
                val dataDefinition = dataRef.variable.referred!! as DataDefinition
                require(qae.field.tryToResolve(dataDefinition.fields, caseInsensitive = true)) {
                    "Field access not resolved: ${qae.field.name} in data definition ${dataDefinition.name}"
                }
            } else {
                TODO()
            }
        }
    }
    this.resolveFunctionCalls(this)

    // replace equality expr in For init with Assignments
    this.specificProcess(ForStmt::class.java) { fs ->
        if (fs.init is EqualityExpr) {
            val ee = fs.init as EqualityExpr
            fs.init.replace(AssignmentExpr(ee.left as AssignableExpression, ee.right, ee.position))
        }
    }

    this.specificProcess(EvalStmt::class.java) { s ->
        if (s.expression is EqualityExpr) {
            // See issue %57 during the code review
            // s.expression.replace((s.expression as EqualityExpr).toAssignment())
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
