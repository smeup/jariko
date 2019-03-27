package com.smeup.rpgparser

import com.strumenta.kolasu.model.specificProcess
import com.strumenta.kolasu.model.tryToResolve

fun CompilationUnit.resolve() {
    this.specificProcess(DataRefExpr::class.java) { dre ->
        if (!dre.variable.resolved) {
            require(dre.variable.tryToResolve(this.dataDefinitonsAndFields)) {
                "Data reference not resolved: ${dre.variable.name}"
            }
        }
    }
    this.specificProcess(ExecuteSubroutine::class.java) { esr ->
        if (!esr.subroutine.resolved) {
            require(esr.subroutine.tryToResolve(this.subroutines)) {
                "Subroutine call not resolved: ${esr.subroutine.name}"
            }
        }
    }
}