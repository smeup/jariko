package com.smeup.rpgparser.utils


import org.antlr.v4.runtime.Parser
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.TerminalNode
import org.antlr.v4.runtime.tree.Trees

private fun String.encodeXmlChars() : String{
    val map = mapOf(
            '<' to "&lt;",
            '>' to "&gt;",
            '&' to "&amp;",
            '\'' to "&apos;",
            '"' to "&quot;"
    )
    return this.map { it -> if (map[it] == null) it else map[it] }.joinToString(separator = "")
}

private fun String.openTag(indent:Int) = " ".repeat(indent) + "<${this.encodeXmlChars()}>\n"
private fun String.closeTag(indent:Int) = " ".repeat(indent) + "</${this.encodeXmlChars()}>\n"
private fun String.emptyTag(indent:Int) = " ".repeat(indent) + "<${this.encodeXmlChars()}/>\n"
private fun String.leaf(indent:Int) = " ".repeat(indent) + "${this.encodeXmlChars()}\n"

fun parseTreeToXml(t: ParseTree?, parser: Parser) : String{
    fun toXmlString(t: ParseTree?, indent:Int = 0):String{
        val sb = StringBuilder()
        if (t != null){
            val nodeText = Trees.getNodeText(t, parser).trim()
            if (nodeText.isNotEmpty()) {
                if (t is TerminalNode){
                    sb.append(nodeText.leaf(indent))
                }
                else {
                    val renderedChildren = StringBuilder()
                    for (i in 0.rangeTo(t.childCount)) {
                        renderedChildren.append(toXmlString(t.getChild(i), indent + 2))
                    }
                    if (renderedChildren.isEmpty()){
                        sb.append(nodeText.emptyTag(indent))
                    }
                    else {
                        sb.append(nodeText.openTag(indent))
                        sb.append(renderedChildren)
                        sb.append(nodeText.closeTag(indent))
                    }
                }
            }
        }
        return sb.toString()
    }

    return toXmlString(t, 0)
}



