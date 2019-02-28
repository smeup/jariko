package com.smeup.rpgparser

import com.smeup.rpgparser.RpgParser.RContext
import me.tomassetti.kolasu.mapping.toPosition

fun RContext.toAst() = CompilationUnit(
        this.statement().mapNotNull { it.dspec() }.map { it.toAst() },
        position = this.toPosition(true))

fun RpgParser.DspecContext.toAst() = DataDefinition(
        this.ds_name().text,
        DataType.SINGLE,
        this.TO_POSITION().text.trim().toInt(),
        decimals = with(this.DECIMAL_POSITIONS().text.trim()) { if (this.isEmpty()) 0 else this.toInt() },
        position = this.toPosition(true))
