package com.smeup.rpgparser

import com.strumenta.kolasu.model.specificProcess
import com.strumenta.kolasu.model.tryToResolve

fun CompilationUnit.resolve() {
    this.specificProcess(DataRefExpr::class.java) { dre ->
        if (!dre.variable.resolved) {
            require(dre.variable.tryToResolve(this.dataDefinitons)) {
                "Data reference not resolved: ${dre.variable.name}"
            }
        }
    }
}