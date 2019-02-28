package com.smeup.rpgparser

import com.smeup.rpgparser.RpgParser.RContext
import me.tomassetti.kolasu.mapping.toPosition

fun RContext.toAst() = CompilationUnit(
        listOf(),
        position = this.toPosition(true))
