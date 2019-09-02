// Generated from MuteParser.g4 by ANTLR 4.7.2
package com.smeup.rpgparser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MuteParser}.
 */
public interface MuteParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MuteParser#muteLine}.
	 * @param ctx the parse tree
	 */
	void enterMuteLine(MuteParser.MuteLineContext ctx);
	/**
	 * Exit a parse tree produced by {@link MuteParser#muteLine}.
	 * @param ctx the parse tree
	 */
	void exitMuteLine(MuteParser.MuteLineContext ctx);
	/**
	 * Enter a parse tree produced by the {@code muteComparisonAnnotation}
	 * labeled alternative in {@link MuteParser#muteAnnotation}.
	 * @param ctx the parse tree
	 */
	void enterMuteComparisonAnnotation(MuteParser.MuteComparisonAnnotationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code muteComparisonAnnotation}
	 * labeled alternative in {@link MuteParser#muteAnnotation}.
	 * @param ctx the parse tree
	 */
	void exitMuteComparisonAnnotation(MuteParser.MuteComparisonAnnotationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code muteTypeAnnotation}
	 * labeled alternative in {@link MuteParser#muteAnnotation}.
	 * @param ctx the parse tree
	 */
	void enterMuteTypeAnnotation(MuteParser.MuteTypeAnnotationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code muteTypeAnnotation}
	 * labeled alternative in {@link MuteParser#muteAnnotation}.
	 * @param ctx the parse tree
	 */
	void exitMuteTypeAnnotation(MuteParser.MuteTypeAnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MuteParser#muteComparisonOperator}.
	 * @param ctx the parse tree
	 */
	void enterMuteComparisonOperator(MuteParser.MuteComparisonOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link MuteParser#muteComparisonOperator}.
	 * @param ctx the parse tree
	 */
	void exitMuteComparisonOperator(MuteParser.MuteComparisonOperatorContext ctx);
}