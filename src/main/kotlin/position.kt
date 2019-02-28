package com.smeup.rpgparser

import me.tomassetti.kolasu.model.Point
import me.tomassetti.kolasu.model.Position
import org.antlr.v4.runtime.Token

operator fun Point.plus(text: String) : Point {
    return when {
        text.isEmpty() -> this
        text.startsWith("\r\n") -> Point(line + 1, 0) + text.substring(2)
        text.startsWith("\n") || text.startsWith("\r") -> Point(line + 1, 0) + text.substring(1)
        else -> Point(line, line + 1) + text.substring(1)
    }
}

val Token.startPoint: Point
    get() = Point(this.line, this.charPositionInLine)

val Token.endPoint: Point
    get() = startPoint + this.text

val Point.asPosition: Position
    get() = Position(this, this)