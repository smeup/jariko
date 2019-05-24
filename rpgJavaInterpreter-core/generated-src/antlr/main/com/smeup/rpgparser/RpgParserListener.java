// Generated from RpgParser.g4 by ANTLR 4.7.1
package com.smeup.rpgparser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link RpgParser}.
 */
public interface RpgParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link RpgParser#r}.
	 * @param ctx the parse tree
	 */
	void enterR(RpgParser.RContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#r}.
	 * @param ctx the parse tree
	 */
	void exitR(RpgParser.RContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(RpgParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(RpgParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#endSource}.
	 * @param ctx the parse tree
	 */
	void enterEndSource(RpgParser.EndSourceContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#endSource}.
	 * @param ctx the parse tree
	 */
	void exitEndSource(RpgParser.EndSourceContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#endSourceHead}.
	 * @param ctx the parse tree
	 */
	void enterEndSourceHead(RpgParser.EndSourceHeadContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#endSourceHead}.
	 * @param ctx the parse tree
	 */
	void exitEndSourceHead(RpgParser.EndSourceHeadContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#endSourceLine}.
	 * @param ctx the parse tree
	 */
	void enterEndSourceLine(RpgParser.EndSourceLineContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#endSourceLine}.
	 * @param ctx the parse tree
	 */
	void exitEndSourceLine(RpgParser.EndSourceLineContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#star_comments}.
	 * @param ctx the parse tree
	 */
	void enterStar_comments(RpgParser.Star_commentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#star_comments}.
	 * @param ctx the parse tree
	 */
	void exitStar_comments(RpgParser.Star_commentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#free_comments}.
	 * @param ctx the parse tree
	 */
	void enterFree_comments(RpgParser.Free_commentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#free_comments}.
	 * @param ctx the parse tree
	 */
	void exitFree_comments(RpgParser.Free_commentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#free_linecomments}.
	 * @param ctx the parse tree
	 */
	void enterFree_linecomments(RpgParser.Free_linecommentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#free_linecomments}.
	 * @param ctx the parse tree
	 */
	void exitFree_linecomments(RpgParser.Free_linecommentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#comments}.
	 * @param ctx the parse tree
	 */
	void enterComments(RpgParser.CommentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#comments}.
	 * @param ctx the parse tree
	 */
	void exitComments(RpgParser.CommentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#dspec}.
	 * @param ctx the parse tree
	 */
	void enterDspec(RpgParser.DspecContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#dspec}.
	 * @param ctx the parse tree
	 */
	void exitDspec(RpgParser.DspecContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#dspecConstant}.
	 * @param ctx the parse tree
	 */
	void enterDspecConstant(RpgParser.DspecConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#dspecConstant}.
	 * @param ctx the parse tree
	 */
	void exitDspecConstant(RpgParser.DspecConstantContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#datatype}.
	 * @param ctx the parse tree
	 */
	void enterDatatype(RpgParser.DatatypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#datatype}.
	 * @param ctx the parse tree
	 */
	void exitDatatype(RpgParser.DatatypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword}.
	 * @param ctx the parse tree
	 */
	void enterKeyword(RpgParser.KeywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword}.
	 * @param ctx the parse tree
	 */
	void exitKeyword(RpgParser.KeywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#dspec_bif}.
	 * @param ctx the parse tree
	 */
	void enterDspec_bif(RpgParser.Dspec_bifContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#dspec_bif}.
	 * @param ctx the parse tree
	 */
	void exitDspec_bif(RpgParser.Dspec_bifContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_alias}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_alias(RpgParser.Keyword_aliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_alias}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_alias(RpgParser.Keyword_aliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_align}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_align(RpgParser.Keyword_alignContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_align}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_align(RpgParser.Keyword_alignContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_alt}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_alt(RpgParser.Keyword_altContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_alt}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_alt(RpgParser.Keyword_altContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_altseq}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_altseq(RpgParser.Keyword_altseqContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_altseq}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_altseq(RpgParser.Keyword_altseqContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_ascend}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_ascend(RpgParser.Keyword_ascendContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_ascend}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_ascend(RpgParser.Keyword_ascendContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_based}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_based(RpgParser.Keyword_basedContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_based}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_based(RpgParser.Keyword_basedContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_ccsid}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_ccsid(RpgParser.Keyword_ccsidContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_ccsid}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_ccsid(RpgParser.Keyword_ccsidContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_class}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_class(RpgParser.Keyword_classContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_class}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_class(RpgParser.Keyword_classContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_const}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_const(RpgParser.Keyword_constContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_const}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_const(RpgParser.Keyword_constContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_ctdata}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_ctdata(RpgParser.Keyword_ctdataContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_ctdata}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_ctdata(RpgParser.Keyword_ctdataContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_datfmt}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_datfmt(RpgParser.Keyword_datfmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_datfmt}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_datfmt(RpgParser.Keyword_datfmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#dateSeparator}.
	 * @param ctx the parse tree
	 */
	void enterDateSeparator(RpgParser.DateSeparatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#dateSeparator}.
	 * @param ctx the parse tree
	 */
	void exitDateSeparator(RpgParser.DateSeparatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_descend}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_descend(RpgParser.Keyword_descendContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_descend}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_descend(RpgParser.Keyword_descendContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_dim}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_dim(RpgParser.Keyword_dimContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_dim}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_dim(RpgParser.Keyword_dimContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_dtaara}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_dtaara(RpgParser.Keyword_dtaaraContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_dtaara}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_dtaara(RpgParser.Keyword_dtaaraContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_export}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_export(RpgParser.Keyword_exportContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_export}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_export(RpgParser.Keyword_exportContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_ext}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_ext(RpgParser.Keyword_extContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_ext}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_ext(RpgParser.Keyword_extContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_extfld}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_extfld(RpgParser.Keyword_extfldContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_extfld}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_extfld(RpgParser.Keyword_extfldContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_extfmt}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_extfmt(RpgParser.Keyword_extfmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_extfmt}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_extfmt(RpgParser.Keyword_extfmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_extname}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_extname(RpgParser.Keyword_extnameContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_extname}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_extname(RpgParser.Keyword_extnameContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_extpgm}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_extpgm(RpgParser.Keyword_extpgmContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_extpgm}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_extpgm(RpgParser.Keyword_extpgmContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_extproc}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_extproc(RpgParser.Keyword_extprocContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_extproc}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_extproc(RpgParser.Keyword_extprocContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_fromfile}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_fromfile(RpgParser.Keyword_fromfileContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_fromfile}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_fromfile(RpgParser.Keyword_fromfileContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_import}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_import(RpgParser.Keyword_importContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_import}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_import(RpgParser.Keyword_importContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_inz}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_inz(RpgParser.Keyword_inzContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_inz}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_inz(RpgParser.Keyword_inzContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_len}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_len(RpgParser.Keyword_lenContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_len}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_len(RpgParser.Keyword_lenContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_like}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_like(RpgParser.Keyword_likeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_like}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_like(RpgParser.Keyword_likeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_likeds}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_likeds(RpgParser.Keyword_likedsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_likeds}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_likeds(RpgParser.Keyword_likedsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_likefile}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_likefile(RpgParser.Keyword_likefileContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_likefile}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_likefile(RpgParser.Keyword_likefileContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_likerec}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_likerec(RpgParser.Keyword_likerecContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_likerec}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_likerec(RpgParser.Keyword_likerecContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_noopt}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_noopt(RpgParser.Keyword_nooptContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_noopt}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_noopt(RpgParser.Keyword_nooptContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_occurs}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_occurs(RpgParser.Keyword_occursContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_occurs}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_occurs(RpgParser.Keyword_occursContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_opdesc}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_opdesc(RpgParser.Keyword_opdescContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_opdesc}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_opdesc(RpgParser.Keyword_opdescContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_options}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_options(RpgParser.Keyword_optionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_options}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_options(RpgParser.Keyword_optionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_overlay}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_overlay(RpgParser.Keyword_overlayContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_overlay}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_overlay(RpgParser.Keyword_overlayContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_packeven}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_packeven(RpgParser.Keyword_packevenContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_packeven}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_packeven(RpgParser.Keyword_packevenContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_perrcd}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_perrcd(RpgParser.Keyword_perrcdContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_perrcd}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_perrcd(RpgParser.Keyword_perrcdContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_prefix}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_prefix(RpgParser.Keyword_prefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_prefix}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_prefix(RpgParser.Keyword_prefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_pos}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_pos(RpgParser.Keyword_posContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_pos}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_pos(RpgParser.Keyword_posContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_procptr}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_procptr(RpgParser.Keyword_procptrContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_procptr}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_procptr(RpgParser.Keyword_procptrContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_qualified}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_qualified(RpgParser.Keyword_qualifiedContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_qualified}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_qualified(RpgParser.Keyword_qualifiedContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_rtnparm}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_rtnparm(RpgParser.Keyword_rtnparmContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_rtnparm}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_rtnparm(RpgParser.Keyword_rtnparmContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_static}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_static(RpgParser.Keyword_staticContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_static}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_static(RpgParser.Keyword_staticContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_sqltype}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_sqltype(RpgParser.Keyword_sqltypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_sqltype}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_sqltype(RpgParser.Keyword_sqltypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_template}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_template(RpgParser.Keyword_templateContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_template}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_template(RpgParser.Keyword_templateContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_timfmt}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_timfmt(RpgParser.Keyword_timfmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_timfmt}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_timfmt(RpgParser.Keyword_timfmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_tofile}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_tofile(RpgParser.Keyword_tofileContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_tofile}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_tofile(RpgParser.Keyword_tofileContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_value}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_value(RpgParser.Keyword_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_value}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_value(RpgParser.Keyword_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_varying}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_varying(RpgParser.Keyword_varyingContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_varying}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_varying(RpgParser.Keyword_varyingContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_psds}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_psds(RpgParser.Keyword_psdsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_psds}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_psds(RpgParser.Keyword_psdsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_block}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_block(RpgParser.Keyword_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_block}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_block(RpgParser.Keyword_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_commit}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_commit(RpgParser.Keyword_commitContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_commit}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_commit(RpgParser.Keyword_commitContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_devid}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_devid(RpgParser.Keyword_devidContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_devid}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_devid(RpgParser.Keyword_devidContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_extdesc}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_extdesc(RpgParser.Keyword_extdescContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_extdesc}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_extdesc(RpgParser.Keyword_extdescContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_extfile}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_extfile(RpgParser.Keyword_extfileContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_extfile}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_extfile(RpgParser.Keyword_extfileContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_extind}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_extind(RpgParser.Keyword_extindContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_extind}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_extind(RpgParser.Keyword_extindContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_extmbr}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_extmbr(RpgParser.Keyword_extmbrContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_extmbr}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_extmbr(RpgParser.Keyword_extmbrContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_formlen}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_formlen(RpgParser.Keyword_formlenContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_formlen}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_formlen(RpgParser.Keyword_formlenContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_formofl}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_formofl(RpgParser.Keyword_formoflContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_formofl}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_formofl(RpgParser.Keyword_formoflContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_ignore}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_ignore(RpgParser.Keyword_ignoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_ignore}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_ignore(RpgParser.Keyword_ignoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_include}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_include(RpgParser.Keyword_includeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_include}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_include(RpgParser.Keyword_includeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_indds}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_indds(RpgParser.Keyword_inddsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_indds}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_indds(RpgParser.Keyword_inddsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_infds}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_infds(RpgParser.Keyword_infdsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_infds}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_infds(RpgParser.Keyword_infdsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_infsr}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_infsr(RpgParser.Keyword_infsrContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_infsr}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_infsr(RpgParser.Keyword_infsrContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_keyloc}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_keyloc(RpgParser.Keyword_keylocContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_keyloc}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_keyloc(RpgParser.Keyword_keylocContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_maxdev}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_maxdev(RpgParser.Keyword_maxdevContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_maxdev}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_maxdev(RpgParser.Keyword_maxdevContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_oflind}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_oflind(RpgParser.Keyword_oflindContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_oflind}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_oflind(RpgParser.Keyword_oflindContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_pass}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_pass(RpgParser.Keyword_passContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_pass}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_pass(RpgParser.Keyword_passContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_pgmname}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_pgmname(RpgParser.Keyword_pgmnameContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_pgmname}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_pgmname(RpgParser.Keyword_pgmnameContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_plist}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_plist(RpgParser.Keyword_plistContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_plist}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_plist(RpgParser.Keyword_plistContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_prtctl}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_prtctl(RpgParser.Keyword_prtctlContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_prtctl}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_prtctl(RpgParser.Keyword_prtctlContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_rafdata}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_rafdata(RpgParser.Keyword_rafdataContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_rafdata}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_rafdata(RpgParser.Keyword_rafdataContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_recno}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_recno(RpgParser.Keyword_recnoContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_recno}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_recno(RpgParser.Keyword_recnoContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_rename}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_rename(RpgParser.Keyword_renameContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_rename}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_rename(RpgParser.Keyword_renameContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_saveds}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_saveds(RpgParser.Keyword_savedsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_saveds}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_saveds(RpgParser.Keyword_savedsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_saveind}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_saveind(RpgParser.Keyword_saveindContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_saveind}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_saveind(RpgParser.Keyword_saveindContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_sfile}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_sfile(RpgParser.Keyword_sfileContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_sfile}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_sfile(RpgParser.Keyword_sfileContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_sln}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_sln(RpgParser.Keyword_slnContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_sln}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_sln(RpgParser.Keyword_slnContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_usropn}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_usropn(RpgParser.Keyword_usropnContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_usropn}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_usropn(RpgParser.Keyword_usropnContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_disk}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_disk(RpgParser.Keyword_diskContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_disk}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_disk(RpgParser.Keyword_diskContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_workstn}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_workstn(RpgParser.Keyword_workstnContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_workstn}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_workstn(RpgParser.Keyword_workstnContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_printer}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_printer(RpgParser.Keyword_printerContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_printer}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_printer(RpgParser.Keyword_printerContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_special}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_special(RpgParser.Keyword_specialContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_special}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_special(RpgParser.Keyword_specialContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_keyed}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_keyed(RpgParser.Keyword_keyedContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_keyed}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_keyed(RpgParser.Keyword_keyedContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#keyword_usage}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_usage(RpgParser.Keyword_usageContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#keyword_usage}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_usage(RpgParser.Keyword_usageContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#like_lengthAdjustment}.
	 * @param ctx the parse tree
	 */
	void enterLike_lengthAdjustment(RpgParser.Like_lengthAdjustmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#like_lengthAdjustment}.
	 * @param ctx the parse tree
	 */
	void exitLike_lengthAdjustment(RpgParser.Like_lengthAdjustmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#sign}.
	 * @param ctx the parse tree
	 */
	void enterSign(RpgParser.SignContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#sign}.
	 * @param ctx the parse tree
	 */
	void exitSign(RpgParser.SignContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#dcl_ds}.
	 * @param ctx the parse tree
	 */
	void enterDcl_ds(RpgParser.Dcl_dsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#dcl_ds}.
	 * @param ctx the parse tree
	 */
	void exitDcl_ds(RpgParser.Dcl_dsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#dcl_ds_field}.
	 * @param ctx the parse tree
	 */
	void enterDcl_ds_field(RpgParser.Dcl_ds_fieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#dcl_ds_field}.
	 * @param ctx the parse tree
	 */
	void exitDcl_ds_field(RpgParser.Dcl_ds_fieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#end_dcl_ds}.
	 * @param ctx the parse tree
	 */
	void enterEnd_dcl_ds(RpgParser.End_dcl_dsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#end_dcl_ds}.
	 * @param ctx the parse tree
	 */
	void exitEnd_dcl_ds(RpgParser.End_dcl_dsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#dcl_pr}.
	 * @param ctx the parse tree
	 */
	void enterDcl_pr(RpgParser.Dcl_prContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#dcl_pr}.
	 * @param ctx the parse tree
	 */
	void exitDcl_pr(RpgParser.Dcl_prContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#dcl_pr_field}.
	 * @param ctx the parse tree
	 */
	void enterDcl_pr_field(RpgParser.Dcl_pr_fieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#dcl_pr_field}.
	 * @param ctx the parse tree
	 */
	void exitDcl_pr_field(RpgParser.Dcl_pr_fieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#end_dcl_pr}.
	 * @param ctx the parse tree
	 */
	void enterEnd_dcl_pr(RpgParser.End_dcl_prContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#end_dcl_pr}.
	 * @param ctx the parse tree
	 */
	void exitEnd_dcl_pr(RpgParser.End_dcl_prContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#dcl_pi}.
	 * @param ctx the parse tree
	 */
	void enterDcl_pi(RpgParser.Dcl_piContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#dcl_pi}.
	 * @param ctx the parse tree
	 */
	void exitDcl_pi(RpgParser.Dcl_piContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#dcl_pi_field}.
	 * @param ctx the parse tree
	 */
	void enterDcl_pi_field(RpgParser.Dcl_pi_fieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#dcl_pi_field}.
	 * @param ctx the parse tree
	 */
	void exitDcl_pi_field(RpgParser.Dcl_pi_fieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#end_dcl_pi}.
	 * @param ctx the parse tree
	 */
	void enterEnd_dcl_pi(RpgParser.End_dcl_piContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#end_dcl_pi}.
	 * @param ctx the parse tree
	 */
	void exitEnd_dcl_pi(RpgParser.End_dcl_piContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#dcl_c}.
	 * @param ctx the parse tree
	 */
	void enterDcl_c(RpgParser.Dcl_cContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#dcl_c}.
	 * @param ctx the parse tree
	 */
	void exitDcl_c(RpgParser.Dcl_cContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#ctl_opt}.
	 * @param ctx the parse tree
	 */
	void enterCtl_opt(RpgParser.Ctl_optContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#ctl_opt}.
	 * @param ctx the parse tree
	 */
	void exitCtl_opt(RpgParser.Ctl_optContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#datatypeName}.
	 * @param ctx the parse tree
	 */
	void enterDatatypeName(RpgParser.DatatypeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#datatypeName}.
	 * @param ctx the parse tree
	 */
	void exitDatatypeName(RpgParser.DatatypeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(RpgParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(RpgParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#ifstatement}.
	 * @param ctx the parse tree
	 */
	void enterIfstatement(RpgParser.IfstatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#ifstatement}.
	 * @param ctx the parse tree
	 */
	void exitIfstatement(RpgParser.IfstatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#elseIfClause}.
	 * @param ctx the parse tree
	 */
	void enterElseIfClause(RpgParser.ElseIfClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#elseIfClause}.
	 * @param ctx the parse tree
	 */
	void exitElseIfClause(RpgParser.ElseIfClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#elseClause}.
	 * @param ctx the parse tree
	 */
	void enterElseClause(RpgParser.ElseClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#elseClause}.
	 * @param ctx the parse tree
	 */
	void exitElseClause(RpgParser.ElseClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#casestatement}.
	 * @param ctx the parse tree
	 */
	void enterCasestatement(RpgParser.CasestatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#casestatement}.
	 * @param ctx the parse tree
	 */
	void exitCasestatement(RpgParser.CasestatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#casestatementend}.
	 * @param ctx the parse tree
	 */
	void enterCasestatementend(RpgParser.CasestatementendContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#casestatementend}.
	 * @param ctx the parse tree
	 */
	void exitCasestatementend(RpgParser.CasestatementendContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#monitorstatement}.
	 * @param ctx the parse tree
	 */
	void enterMonitorstatement(RpgParser.MonitorstatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#monitorstatement}.
	 * @param ctx the parse tree
	 */
	void exitMonitorstatement(RpgParser.MonitorstatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#beginmonitor}.
	 * @param ctx the parse tree
	 */
	void enterBeginmonitor(RpgParser.BeginmonitorContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#beginmonitor}.
	 * @param ctx the parse tree
	 */
	void exitBeginmonitor(RpgParser.BeginmonitorContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#endmonitor}.
	 * @param ctx the parse tree
	 */
	void enterEndmonitor(RpgParser.EndmonitorContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#endmonitor}.
	 * @param ctx the parse tree
	 */
	void exitEndmonitor(RpgParser.EndmonitorContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#onError}.
	 * @param ctx the parse tree
	 */
	void enterOnError(RpgParser.OnErrorContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#onError}.
	 * @param ctx the parse tree
	 */
	void exitOnError(RpgParser.OnErrorContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#selectstatement}.
	 * @param ctx the parse tree
	 */
	void enterSelectstatement(RpgParser.SelectstatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#selectstatement}.
	 * @param ctx the parse tree
	 */
	void exitSelectstatement(RpgParser.SelectstatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#other}.
	 * @param ctx the parse tree
	 */
	void enterOther(RpgParser.OtherContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#other}.
	 * @param ctx the parse tree
	 */
	void exitOther(RpgParser.OtherContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#beginselect}.
	 * @param ctx the parse tree
	 */
	void enterBeginselect(RpgParser.BeginselectContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#beginselect}.
	 * @param ctx the parse tree
	 */
	void exitBeginselect(RpgParser.BeginselectContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#whenstatement}.
	 * @param ctx the parse tree
	 */
	void enterWhenstatement(RpgParser.WhenstatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#whenstatement}.
	 * @param ctx the parse tree
	 */
	void exitWhenstatement(RpgParser.WhenstatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#when}.
	 * @param ctx the parse tree
	 */
	void enterWhen(RpgParser.WhenContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#when}.
	 * @param ctx the parse tree
	 */
	void exitWhen(RpgParser.WhenContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csWHENxx}.
	 * @param ctx the parse tree
	 */
	void enterCsWHENxx(RpgParser.CsWHENxxContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csWHENxx}.
	 * @param ctx the parse tree
	 */
	void exitCsWHENxx(RpgParser.CsWHENxxContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#endselect}.
	 * @param ctx the parse tree
	 */
	void enterEndselect(RpgParser.EndselectContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#endselect}.
	 * @param ctx the parse tree
	 */
	void exitEndselect(RpgParser.EndselectContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#beginif}.
	 * @param ctx the parse tree
	 */
	void enterBeginif(RpgParser.BeginifContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#beginif}.
	 * @param ctx the parse tree
	 */
	void exitBeginif(RpgParser.BeginifContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#begindou}.
	 * @param ctx the parse tree
	 */
	void enterBegindou(RpgParser.BegindouContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#begindou}.
	 * @param ctx the parse tree
	 */
	void exitBegindou(RpgParser.BegindouContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#begindow}.
	 * @param ctx the parse tree
	 */
	void enterBegindow(RpgParser.BegindowContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#begindow}.
	 * @param ctx the parse tree
	 */
	void exitBegindow(RpgParser.BegindowContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#begindo}.
	 * @param ctx the parse tree
	 */
	void enterBegindo(RpgParser.BegindoContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#begindo}.
	 * @param ctx the parse tree
	 */
	void exitBegindo(RpgParser.BegindoContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#elseifstmt}.
	 * @param ctx the parse tree
	 */
	void enterElseifstmt(RpgParser.ElseifstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#elseifstmt}.
	 * @param ctx the parse tree
	 */
	void exitElseifstmt(RpgParser.ElseifstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#elsestmt}.
	 * @param ctx the parse tree
	 */
	void enterElsestmt(RpgParser.ElsestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#elsestmt}.
	 * @param ctx the parse tree
	 */
	void exitElsestmt(RpgParser.ElsestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csIFxx}.
	 * @param ctx the parse tree
	 */
	void enterCsIFxx(RpgParser.CsIFxxContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csIFxx}.
	 * @param ctx the parse tree
	 */
	void exitCsIFxx(RpgParser.CsIFxxContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csDOUxx}.
	 * @param ctx the parse tree
	 */
	void enterCsDOUxx(RpgParser.CsDOUxxContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csDOUxx}.
	 * @param ctx the parse tree
	 */
	void exitCsDOUxx(RpgParser.CsDOUxxContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csDOWxx}.
	 * @param ctx the parse tree
	 */
	void enterCsDOWxx(RpgParser.CsDOWxxContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csDOWxx}.
	 * @param ctx the parse tree
	 */
	void exitCsDOWxx(RpgParser.CsDOWxxContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#complexCondxx}.
	 * @param ctx the parse tree
	 */
	void enterComplexCondxx(RpgParser.ComplexCondxxContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#complexCondxx}.
	 * @param ctx the parse tree
	 */
	void exitComplexCondxx(RpgParser.ComplexCondxxContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csANDxx}.
	 * @param ctx the parse tree
	 */
	void enterCsANDxx(RpgParser.CsANDxxContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csANDxx}.
	 * @param ctx the parse tree
	 */
	void exitCsANDxx(RpgParser.CsANDxxContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csORxx}.
	 * @param ctx the parse tree
	 */
	void enterCsORxx(RpgParser.CsORxxContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csORxx}.
	 * @param ctx the parse tree
	 */
	void exitCsORxx(RpgParser.CsORxxContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#forstatement}.
	 * @param ctx the parse tree
	 */
	void enterForstatement(RpgParser.ForstatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#forstatement}.
	 * @param ctx the parse tree
	 */
	void exitForstatement(RpgParser.ForstatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#beginfor}.
	 * @param ctx the parse tree
	 */
	void enterBeginfor(RpgParser.BeginforContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#beginfor}.
	 * @param ctx the parse tree
	 */
	void exitBeginfor(RpgParser.BeginforContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#endif}.
	 * @param ctx the parse tree
	 */
	void enterEndif(RpgParser.EndifContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#endif}.
	 * @param ctx the parse tree
	 */
	void exitEndif(RpgParser.EndifContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#enddo}.
	 * @param ctx the parse tree
	 */
	void enterEnddo(RpgParser.EnddoContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#enddo}.
	 * @param ctx the parse tree
	 */
	void exitEnddo(RpgParser.EnddoContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#endfor}.
	 * @param ctx the parse tree
	 */
	void enterEndfor(RpgParser.EndforContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#endfor}.
	 * @param ctx the parse tree
	 */
	void exitEndfor(RpgParser.EndforContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#dspec_fixed}.
	 * @param ctx the parse tree
	 */
	void enterDspec_fixed(RpgParser.Dspec_fixedContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#dspec_fixed}.
	 * @param ctx the parse tree
	 */
	void exitDspec_fixed(RpgParser.Dspec_fixedContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#ds_name}.
	 * @param ctx the parse tree
	 */
	void enterDs_name(RpgParser.Ds_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#ds_name}.
	 * @param ctx the parse tree
	 */
	void exitDs_name(RpgParser.Ds_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#ospec_fixed}.
	 * @param ctx the parse tree
	 */
	void enterOspec_fixed(RpgParser.Ospec_fixedContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#ospec_fixed}.
	 * @param ctx the parse tree
	 */
	void exitOspec_fixed(RpgParser.Ospec_fixedContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#os_fixed_pgmdesc1}.
	 * @param ctx the parse tree
	 */
	void enterOs_fixed_pgmdesc1(RpgParser.Os_fixed_pgmdesc1Context ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#os_fixed_pgmdesc1}.
	 * @param ctx the parse tree
	 */
	void exitOs_fixed_pgmdesc1(RpgParser.Os_fixed_pgmdesc1Context ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#outputConditioningOnOffIndicator}.
	 * @param ctx the parse tree
	 */
	void enterOutputConditioningOnOffIndicator(RpgParser.OutputConditioningOnOffIndicatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#outputConditioningOnOffIndicator}.
	 * @param ctx the parse tree
	 */
	void exitOutputConditioningOnOffIndicator(RpgParser.OutputConditioningOnOffIndicatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#outputConditioningIndicator}.
	 * @param ctx the parse tree
	 */
	void enterOutputConditioningIndicator(RpgParser.OutputConditioningIndicatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#outputConditioningIndicator}.
	 * @param ctx the parse tree
	 */
	void exitOutputConditioningIndicator(RpgParser.OutputConditioningIndicatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#os_fixed_pgmdesc_compound}.
	 * @param ctx the parse tree
	 */
	void enterOs_fixed_pgmdesc_compound(RpgParser.Os_fixed_pgmdesc_compoundContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#os_fixed_pgmdesc_compound}.
	 * @param ctx the parse tree
	 */
	void exitOs_fixed_pgmdesc_compound(RpgParser.Os_fixed_pgmdesc_compoundContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#os_fixed_pgmdesc2}.
	 * @param ctx the parse tree
	 */
	void enterOs_fixed_pgmdesc2(RpgParser.Os_fixed_pgmdesc2Context ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#os_fixed_pgmdesc2}.
	 * @param ctx the parse tree
	 */
	void exitOs_fixed_pgmdesc2(RpgParser.Os_fixed_pgmdesc2Context ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#os_fixed_pgmfield}.
	 * @param ctx the parse tree
	 */
	void enterOs_fixed_pgmfield(RpgParser.Os_fixed_pgmfieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#os_fixed_pgmfield}.
	 * @param ctx the parse tree
	 */
	void exitOs_fixed_pgmfield(RpgParser.Os_fixed_pgmfieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#ps_name}.
	 * @param ctx the parse tree
	 */
	void enterPs_name(RpgParser.Ps_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#ps_name}.
	 * @param ctx the parse tree
	 */
	void exitPs_name(RpgParser.Ps_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#fspec}.
	 * @param ctx the parse tree
	 */
	void enterFspec(RpgParser.FspecContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#fspec}.
	 * @param ctx the parse tree
	 */
	void exitFspec(RpgParser.FspecContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#filename}.
	 * @param ctx the parse tree
	 */
	void enterFilename(RpgParser.FilenameContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#filename}.
	 * @param ctx the parse tree
	 */
	void exitFilename(RpgParser.FilenameContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#fs_parm}.
	 * @param ctx the parse tree
	 */
	void enterFs_parm(RpgParser.Fs_parmContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#fs_parm}.
	 * @param ctx the parse tree
	 */
	void exitFs_parm(RpgParser.Fs_parmContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#fs_string}.
	 * @param ctx the parse tree
	 */
	void enterFs_string(RpgParser.Fs_stringContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#fs_string}.
	 * @param ctx the parse tree
	 */
	void exitFs_string(RpgParser.Fs_stringContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#fs_keyword}.
	 * @param ctx the parse tree
	 */
	void enterFs_keyword(RpgParser.Fs_keywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#fs_keyword}.
	 * @param ctx the parse tree
	 */
	void exitFs_keyword(RpgParser.Fs_keywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#fspec_fixed}.
	 * @param ctx the parse tree
	 */
	void enterFspec_fixed(RpgParser.Fspec_fixedContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#fspec_fixed}.
	 * @param ctx the parse tree
	 */
	void exitFspec_fixed(RpgParser.Fspec_fixedContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#cspec_fixed}.
	 * @param ctx the parse tree
	 */
	void enterCspec_fixed(RpgParser.Cspec_fixedContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#cspec_fixed}.
	 * @param ctx the parse tree
	 */
	void exitCspec_fixed(RpgParser.Cspec_fixedContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#cspec_continuedIndicators}.
	 * @param ctx the parse tree
	 */
	void enterCspec_continuedIndicators(RpgParser.Cspec_continuedIndicatorsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#cspec_continuedIndicators}.
	 * @param ctx the parse tree
	 */
	void exitCspec_continuedIndicators(RpgParser.Cspec_continuedIndicatorsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#cspec_blank}.
	 * @param ctx the parse tree
	 */
	void enterCspec_blank(RpgParser.Cspec_blankContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#cspec_blank}.
	 * @param ctx the parse tree
	 */
	void exitCspec_blank(RpgParser.Cspec_blankContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#blank_spec}.
	 * @param ctx the parse tree
	 */
	void enterBlank_spec(RpgParser.Blank_specContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#blank_spec}.
	 * @param ctx the parse tree
	 */
	void exitBlank_spec(RpgParser.Blank_specContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#piBegin}.
	 * @param ctx the parse tree
	 */
	void enterPiBegin(RpgParser.PiBeginContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#piBegin}.
	 * @param ctx the parse tree
	 */
	void exitPiBegin(RpgParser.PiBeginContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#parm_fixed}.
	 * @param ctx the parse tree
	 */
	void enterParm_fixed(RpgParser.Parm_fixedContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#parm_fixed}.
	 * @param ctx the parse tree
	 */
	void exitParm_fixed(RpgParser.Parm_fixedContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#pr_parm_fixed}.
	 * @param ctx the parse tree
	 */
	void enterPr_parm_fixed(RpgParser.Pr_parm_fixedContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#pr_parm_fixed}.
	 * @param ctx the parse tree
	 */
	void exitPr_parm_fixed(RpgParser.Pr_parm_fixedContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#pi_parm_fixed}.
	 * @param ctx the parse tree
	 */
	void enterPi_parm_fixed(RpgParser.Pi_parm_fixedContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#pi_parm_fixed}.
	 * @param ctx the parse tree
	 */
	void exitPi_parm_fixed(RpgParser.Pi_parm_fixedContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#procedure}.
	 * @param ctx the parse tree
	 */
	void enterProcedure(RpgParser.ProcedureContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#procedure}.
	 * @param ctx the parse tree
	 */
	void exitProcedure(RpgParser.ProcedureContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#beginProcedure}.
	 * @param ctx the parse tree
	 */
	void enterBeginProcedure(RpgParser.BeginProcedureContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#beginProcedure}.
	 * @param ctx the parse tree
	 */
	void exitBeginProcedure(RpgParser.BeginProcedureContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#endProcedure}.
	 * @param ctx the parse tree
	 */
	void enterEndProcedure(RpgParser.EndProcedureContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#endProcedure}.
	 * @param ctx the parse tree
	 */
	void exitEndProcedure(RpgParser.EndProcedureContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#psBegin}.
	 * @param ctx the parse tree
	 */
	void enterPsBegin(RpgParser.PsBeginContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#psBegin}.
	 * @param ctx the parse tree
	 */
	void exitPsBegin(RpgParser.PsBeginContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#freeBeginProcedure}.
	 * @param ctx the parse tree
	 */
	void enterFreeBeginProcedure(RpgParser.FreeBeginProcedureContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#freeBeginProcedure}.
	 * @param ctx the parse tree
	 */
	void exitFreeBeginProcedure(RpgParser.FreeBeginProcedureContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#psEnd}.
	 * @param ctx the parse tree
	 */
	void enterPsEnd(RpgParser.PsEndContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#psEnd}.
	 * @param ctx the parse tree
	 */
	void exitPsEnd(RpgParser.PsEndContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#freeEndProcedure}.
	 * @param ctx the parse tree
	 */
	void enterFreeEndProcedure(RpgParser.FreeEndProcedureContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#freeEndProcedure}.
	 * @param ctx the parse tree
	 */
	void exitFreeEndProcedure(RpgParser.FreeEndProcedureContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#prBegin}.
	 * @param ctx the parse tree
	 */
	void enterPrBegin(RpgParser.PrBeginContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#prBegin}.
	 * @param ctx the parse tree
	 */
	void exitPrBegin(RpgParser.PrBeginContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#subroutine}.
	 * @param ctx the parse tree
	 */
	void enterSubroutine(RpgParser.SubroutineContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#subroutine}.
	 * @param ctx the parse tree
	 */
	void exitSubroutine(RpgParser.SubroutineContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#subprocedurestatement}.
	 * @param ctx the parse tree
	 */
	void enterSubprocedurestatement(RpgParser.SubprocedurestatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#subprocedurestatement}.
	 * @param ctx the parse tree
	 */
	void exitSubprocedurestatement(RpgParser.SubprocedurestatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#begsr}.
	 * @param ctx the parse tree
	 */
	void enterBegsr(RpgParser.BegsrContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#begsr}.
	 * @param ctx the parse tree
	 */
	void exitBegsr(RpgParser.BegsrContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#endsr}.
	 * @param ctx the parse tree
	 */
	void enterEndsr(RpgParser.EndsrContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#endsr}.
	 * @param ctx the parse tree
	 */
	void exitEndsr(RpgParser.EndsrContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csBEGSR}.
	 * @param ctx the parse tree
	 */
	void enterCsBEGSR(RpgParser.CsBEGSRContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csBEGSR}.
	 * @param ctx the parse tree
	 */
	void exitCsBEGSR(RpgParser.CsBEGSRContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#freeBEGSR}.
	 * @param ctx the parse tree
	 */
	void enterFreeBEGSR(RpgParser.FreeBEGSRContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#freeBEGSR}.
	 * @param ctx the parse tree
	 */
	void exitFreeBEGSR(RpgParser.FreeBEGSRContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csENDSR}.
	 * @param ctx the parse tree
	 */
	void enterCsENDSR(RpgParser.CsENDSRContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csENDSR}.
	 * @param ctx the parse tree
	 */
	void exitCsENDSR(RpgParser.CsENDSRContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#freeENDSR}.
	 * @param ctx the parse tree
	 */
	void enterFreeENDSR(RpgParser.FreeENDSRContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#freeENDSR}.
	 * @param ctx the parse tree
	 */
	void exitFreeENDSR(RpgParser.FreeENDSRContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#onOffIndicatorsFlag}.
	 * @param ctx the parse tree
	 */
	void enterOnOffIndicatorsFlag(RpgParser.OnOffIndicatorsFlagContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#onOffIndicatorsFlag}.
	 * @param ctx the parse tree
	 */
	void exitOnOffIndicatorsFlag(RpgParser.OnOffIndicatorsFlagContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#cs_controlLevel}.
	 * @param ctx the parse tree
	 */
	void enterCs_controlLevel(RpgParser.Cs_controlLevelContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#cs_controlLevel}.
	 * @param ctx the parse tree
	 */
	void exitCs_controlLevel(RpgParser.Cs_controlLevelContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#cs_indicators}.
	 * @param ctx the parse tree
	 */
	void enterCs_indicators(RpgParser.Cs_indicatorsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#cs_indicators}.
	 * @param ctx the parse tree
	 */
	void exitCs_indicators(RpgParser.Cs_indicatorsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#resultIndicator}.
	 * @param ctx the parse tree
	 */
	void enterResultIndicator(RpgParser.ResultIndicatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#resultIndicator}.
	 * @param ctx the parse tree
	 */
	void exitResultIndicator(RpgParser.ResultIndicatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#cspec_fixed_sql}.
	 * @param ctx the parse tree
	 */
	void enterCspec_fixed_sql(RpgParser.Cspec_fixed_sqlContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#cspec_fixed_sql}.
	 * @param ctx the parse tree
	 */
	void exitCspec_fixed_sql(RpgParser.Cspec_fixed_sqlContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#cspec_fixed_standard}.
	 * @param ctx the parse tree
	 */
	void enterCspec_fixed_standard(RpgParser.Cspec_fixed_standardContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#cspec_fixed_standard}.
	 * @param ctx the parse tree
	 */
	void exitCspec_fixed_standard(RpgParser.Cspec_fixed_standardContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#cspec_fixed_standard_parts}.
	 * @param ctx the parse tree
	 */
	void enterCspec_fixed_standard_parts(RpgParser.Cspec_fixed_standard_partsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#cspec_fixed_standard_parts}.
	 * @param ctx the parse tree
	 */
	void exitCspec_fixed_standard_parts(RpgParser.Cspec_fixed_standard_partsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csACQ}.
	 * @param ctx the parse tree
	 */
	void enterCsACQ(RpgParser.CsACQContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csACQ}.
	 * @param ctx the parse tree
	 */
	void exitCsACQ(RpgParser.CsACQContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csADD}.
	 * @param ctx the parse tree
	 */
	void enterCsADD(RpgParser.CsADDContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csADD}.
	 * @param ctx the parse tree
	 */
	void exitCsADD(RpgParser.CsADDContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csADDDUR}.
	 * @param ctx the parse tree
	 */
	void enterCsADDDUR(RpgParser.CsADDDURContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csADDDUR}.
	 * @param ctx the parse tree
	 */
	void exitCsADDDUR(RpgParser.CsADDDURContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csALLOC}.
	 * @param ctx the parse tree
	 */
	void enterCsALLOC(RpgParser.CsALLOCContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csALLOC}.
	 * @param ctx the parse tree
	 */
	void exitCsALLOC(RpgParser.CsALLOCContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csANDEQ}.
	 * @param ctx the parse tree
	 */
	void enterCsANDEQ(RpgParser.CsANDEQContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csANDEQ}.
	 * @param ctx the parse tree
	 */
	void exitCsANDEQ(RpgParser.CsANDEQContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csANDNE}.
	 * @param ctx the parse tree
	 */
	void enterCsANDNE(RpgParser.CsANDNEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csANDNE}.
	 * @param ctx the parse tree
	 */
	void exitCsANDNE(RpgParser.CsANDNEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csANDLE}.
	 * @param ctx the parse tree
	 */
	void enterCsANDLE(RpgParser.CsANDLEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csANDLE}.
	 * @param ctx the parse tree
	 */
	void exitCsANDLE(RpgParser.CsANDLEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csANDLT}.
	 * @param ctx the parse tree
	 */
	void enterCsANDLT(RpgParser.CsANDLTContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csANDLT}.
	 * @param ctx the parse tree
	 */
	void exitCsANDLT(RpgParser.CsANDLTContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csANDGE}.
	 * @param ctx the parse tree
	 */
	void enterCsANDGE(RpgParser.CsANDGEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csANDGE}.
	 * @param ctx the parse tree
	 */
	void exitCsANDGE(RpgParser.CsANDGEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csANDGT}.
	 * @param ctx the parse tree
	 */
	void enterCsANDGT(RpgParser.CsANDGTContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csANDGT}.
	 * @param ctx the parse tree
	 */
	void exitCsANDGT(RpgParser.CsANDGTContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csBITOFF}.
	 * @param ctx the parse tree
	 */
	void enterCsBITOFF(RpgParser.CsBITOFFContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csBITOFF}.
	 * @param ctx the parse tree
	 */
	void exitCsBITOFF(RpgParser.CsBITOFFContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csBITON}.
	 * @param ctx the parse tree
	 */
	void enterCsBITON(RpgParser.CsBITONContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csBITON}.
	 * @param ctx the parse tree
	 */
	void exitCsBITON(RpgParser.CsBITONContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csCABxx}.
	 * @param ctx the parse tree
	 */
	void enterCsCABxx(RpgParser.CsCABxxContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csCABxx}.
	 * @param ctx the parse tree
	 */
	void exitCsCABxx(RpgParser.CsCABxxContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csCABEQ}.
	 * @param ctx the parse tree
	 */
	void enterCsCABEQ(RpgParser.CsCABEQContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csCABEQ}.
	 * @param ctx the parse tree
	 */
	void exitCsCABEQ(RpgParser.CsCABEQContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csCABNE}.
	 * @param ctx the parse tree
	 */
	void enterCsCABNE(RpgParser.CsCABNEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csCABNE}.
	 * @param ctx the parse tree
	 */
	void exitCsCABNE(RpgParser.CsCABNEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csCABLE}.
	 * @param ctx the parse tree
	 */
	void enterCsCABLE(RpgParser.CsCABLEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csCABLE}.
	 * @param ctx the parse tree
	 */
	void exitCsCABLE(RpgParser.CsCABLEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csCABLT}.
	 * @param ctx the parse tree
	 */
	void enterCsCABLT(RpgParser.CsCABLTContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csCABLT}.
	 * @param ctx the parse tree
	 */
	void exitCsCABLT(RpgParser.CsCABLTContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csCABGE}.
	 * @param ctx the parse tree
	 */
	void enterCsCABGE(RpgParser.CsCABGEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csCABGE}.
	 * @param ctx the parse tree
	 */
	void exitCsCABGE(RpgParser.CsCABGEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csCABGT}.
	 * @param ctx the parse tree
	 */
	void enterCsCABGT(RpgParser.CsCABGTContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csCABGT}.
	 * @param ctx the parse tree
	 */
	void exitCsCABGT(RpgParser.CsCABGTContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csCALL}.
	 * @param ctx the parse tree
	 */
	void enterCsCALL(RpgParser.CsCALLContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csCALL}.
	 * @param ctx the parse tree
	 */
	void exitCsCALL(RpgParser.CsCALLContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csCALLB}.
	 * @param ctx the parse tree
	 */
	void enterCsCALLB(RpgParser.CsCALLBContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csCALLB}.
	 * @param ctx the parse tree
	 */
	void exitCsCALLB(RpgParser.CsCALLBContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csCALLP}.
	 * @param ctx the parse tree
	 */
	void enterCsCALLP(RpgParser.CsCALLPContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csCALLP}.
	 * @param ctx the parse tree
	 */
	void exitCsCALLP(RpgParser.CsCALLPContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csCASEQ}.
	 * @param ctx the parse tree
	 */
	void enterCsCASEQ(RpgParser.CsCASEQContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csCASEQ}.
	 * @param ctx the parse tree
	 */
	void exitCsCASEQ(RpgParser.CsCASEQContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csCASNE}.
	 * @param ctx the parse tree
	 */
	void enterCsCASNE(RpgParser.CsCASNEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csCASNE}.
	 * @param ctx the parse tree
	 */
	void exitCsCASNE(RpgParser.CsCASNEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csCASLE}.
	 * @param ctx the parse tree
	 */
	void enterCsCASLE(RpgParser.CsCASLEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csCASLE}.
	 * @param ctx the parse tree
	 */
	void exitCsCASLE(RpgParser.CsCASLEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csCASLT}.
	 * @param ctx the parse tree
	 */
	void enterCsCASLT(RpgParser.CsCASLTContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csCASLT}.
	 * @param ctx the parse tree
	 */
	void exitCsCASLT(RpgParser.CsCASLTContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csCASGE}.
	 * @param ctx the parse tree
	 */
	void enterCsCASGE(RpgParser.CsCASGEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csCASGE}.
	 * @param ctx the parse tree
	 */
	void exitCsCASGE(RpgParser.CsCASGEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csCASGT}.
	 * @param ctx the parse tree
	 */
	void enterCsCASGT(RpgParser.CsCASGTContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csCASGT}.
	 * @param ctx the parse tree
	 */
	void exitCsCASGT(RpgParser.CsCASGTContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csCAS}.
	 * @param ctx the parse tree
	 */
	void enterCsCAS(RpgParser.CsCASContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csCAS}.
	 * @param ctx the parse tree
	 */
	void exitCsCAS(RpgParser.CsCASContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csCAT}.
	 * @param ctx the parse tree
	 */
	void enterCsCAT(RpgParser.CsCATContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csCAT}.
	 * @param ctx the parse tree
	 */
	void exitCsCAT(RpgParser.CsCATContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csCHAIN}.
	 * @param ctx the parse tree
	 */
	void enterCsCHAIN(RpgParser.CsCHAINContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csCHAIN}.
	 * @param ctx the parse tree
	 */
	void exitCsCHAIN(RpgParser.CsCHAINContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csCHECK}.
	 * @param ctx the parse tree
	 */
	void enterCsCHECK(RpgParser.CsCHECKContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csCHECK}.
	 * @param ctx the parse tree
	 */
	void exitCsCHECK(RpgParser.CsCHECKContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csCHECKR}.
	 * @param ctx the parse tree
	 */
	void enterCsCHECKR(RpgParser.CsCHECKRContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csCHECKR}.
	 * @param ctx the parse tree
	 */
	void exitCsCHECKR(RpgParser.CsCHECKRContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csCLEAR}.
	 * @param ctx the parse tree
	 */
	void enterCsCLEAR(RpgParser.CsCLEARContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csCLEAR}.
	 * @param ctx the parse tree
	 */
	void exitCsCLEAR(RpgParser.CsCLEARContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csCLOSE}.
	 * @param ctx the parse tree
	 */
	void enterCsCLOSE(RpgParser.CsCLOSEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csCLOSE}.
	 * @param ctx the parse tree
	 */
	void exitCsCLOSE(RpgParser.CsCLOSEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csCOMMIT}.
	 * @param ctx the parse tree
	 */
	void enterCsCOMMIT(RpgParser.CsCOMMITContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csCOMMIT}.
	 * @param ctx the parse tree
	 */
	void exitCsCOMMIT(RpgParser.CsCOMMITContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csCOMP}.
	 * @param ctx the parse tree
	 */
	void enterCsCOMP(RpgParser.CsCOMPContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csCOMP}.
	 * @param ctx the parse tree
	 */
	void exitCsCOMP(RpgParser.CsCOMPContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csDEALLOC}.
	 * @param ctx the parse tree
	 */
	void enterCsDEALLOC(RpgParser.CsDEALLOCContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csDEALLOC}.
	 * @param ctx the parse tree
	 */
	void exitCsDEALLOC(RpgParser.CsDEALLOCContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csDEFINE}.
	 * @param ctx the parse tree
	 */
	void enterCsDEFINE(RpgParser.CsDEFINEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csDEFINE}.
	 * @param ctx the parse tree
	 */
	void exitCsDEFINE(RpgParser.CsDEFINEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csDELETE}.
	 * @param ctx the parse tree
	 */
	void enterCsDELETE(RpgParser.CsDELETEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csDELETE}.
	 * @param ctx the parse tree
	 */
	void exitCsDELETE(RpgParser.CsDELETEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csDIV}.
	 * @param ctx the parse tree
	 */
	void enterCsDIV(RpgParser.CsDIVContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csDIV}.
	 * @param ctx the parse tree
	 */
	void exitCsDIV(RpgParser.CsDIVContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csDO}.
	 * @param ctx the parse tree
	 */
	void enterCsDO(RpgParser.CsDOContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csDO}.
	 * @param ctx the parse tree
	 */
	void exitCsDO(RpgParser.CsDOContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csDOU}.
	 * @param ctx the parse tree
	 */
	void enterCsDOU(RpgParser.CsDOUContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csDOU}.
	 * @param ctx the parse tree
	 */
	void exitCsDOU(RpgParser.CsDOUContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csDOUEQ}.
	 * @param ctx the parse tree
	 */
	void enterCsDOUEQ(RpgParser.CsDOUEQContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csDOUEQ}.
	 * @param ctx the parse tree
	 */
	void exitCsDOUEQ(RpgParser.CsDOUEQContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csDOUNE}.
	 * @param ctx the parse tree
	 */
	void enterCsDOUNE(RpgParser.CsDOUNEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csDOUNE}.
	 * @param ctx the parse tree
	 */
	void exitCsDOUNE(RpgParser.CsDOUNEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csDOULE}.
	 * @param ctx the parse tree
	 */
	void enterCsDOULE(RpgParser.CsDOULEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csDOULE}.
	 * @param ctx the parse tree
	 */
	void exitCsDOULE(RpgParser.CsDOULEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csDOULT}.
	 * @param ctx the parse tree
	 */
	void enterCsDOULT(RpgParser.CsDOULTContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csDOULT}.
	 * @param ctx the parse tree
	 */
	void exitCsDOULT(RpgParser.CsDOULTContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csDOUGE}.
	 * @param ctx the parse tree
	 */
	void enterCsDOUGE(RpgParser.CsDOUGEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csDOUGE}.
	 * @param ctx the parse tree
	 */
	void exitCsDOUGE(RpgParser.CsDOUGEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csDOUGT}.
	 * @param ctx the parse tree
	 */
	void enterCsDOUGT(RpgParser.CsDOUGTContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csDOUGT}.
	 * @param ctx the parse tree
	 */
	void exitCsDOUGT(RpgParser.CsDOUGTContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csDOW}.
	 * @param ctx the parse tree
	 */
	void enterCsDOW(RpgParser.CsDOWContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csDOW}.
	 * @param ctx the parse tree
	 */
	void exitCsDOW(RpgParser.CsDOWContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csDOWEQ}.
	 * @param ctx the parse tree
	 */
	void enterCsDOWEQ(RpgParser.CsDOWEQContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csDOWEQ}.
	 * @param ctx the parse tree
	 */
	void exitCsDOWEQ(RpgParser.CsDOWEQContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csDOWNE}.
	 * @param ctx the parse tree
	 */
	void enterCsDOWNE(RpgParser.CsDOWNEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csDOWNE}.
	 * @param ctx the parse tree
	 */
	void exitCsDOWNE(RpgParser.CsDOWNEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csDOWLE}.
	 * @param ctx the parse tree
	 */
	void enterCsDOWLE(RpgParser.CsDOWLEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csDOWLE}.
	 * @param ctx the parse tree
	 */
	void exitCsDOWLE(RpgParser.CsDOWLEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csDOWLT}.
	 * @param ctx the parse tree
	 */
	void enterCsDOWLT(RpgParser.CsDOWLTContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csDOWLT}.
	 * @param ctx the parse tree
	 */
	void exitCsDOWLT(RpgParser.CsDOWLTContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csDOWGE}.
	 * @param ctx the parse tree
	 */
	void enterCsDOWGE(RpgParser.CsDOWGEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csDOWGE}.
	 * @param ctx the parse tree
	 */
	void exitCsDOWGE(RpgParser.CsDOWGEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csDOWGT}.
	 * @param ctx the parse tree
	 */
	void enterCsDOWGT(RpgParser.CsDOWGTContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csDOWGT}.
	 * @param ctx the parse tree
	 */
	void exitCsDOWGT(RpgParser.CsDOWGTContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csDSPLY}.
	 * @param ctx the parse tree
	 */
	void enterCsDSPLY(RpgParser.CsDSPLYContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csDSPLY}.
	 * @param ctx the parse tree
	 */
	void exitCsDSPLY(RpgParser.CsDSPLYContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csDUMP}.
	 * @param ctx the parse tree
	 */
	void enterCsDUMP(RpgParser.CsDUMPContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csDUMP}.
	 * @param ctx the parse tree
	 */
	void exitCsDUMP(RpgParser.CsDUMPContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csELSE}.
	 * @param ctx the parse tree
	 */
	void enterCsELSE(RpgParser.CsELSEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csELSE}.
	 * @param ctx the parse tree
	 */
	void exitCsELSE(RpgParser.CsELSEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csELSEIF}.
	 * @param ctx the parse tree
	 */
	void enterCsELSEIF(RpgParser.CsELSEIFContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csELSEIF}.
	 * @param ctx the parse tree
	 */
	void exitCsELSEIF(RpgParser.CsELSEIFContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csEND}.
	 * @param ctx the parse tree
	 */
	void enterCsEND(RpgParser.CsENDContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csEND}.
	 * @param ctx the parse tree
	 */
	void exitCsEND(RpgParser.CsENDContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csENDCS}.
	 * @param ctx the parse tree
	 */
	void enterCsENDCS(RpgParser.CsENDCSContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csENDCS}.
	 * @param ctx the parse tree
	 */
	void exitCsENDCS(RpgParser.CsENDCSContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csENDDO}.
	 * @param ctx the parse tree
	 */
	void enterCsENDDO(RpgParser.CsENDDOContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csENDDO}.
	 * @param ctx the parse tree
	 */
	void exitCsENDDO(RpgParser.CsENDDOContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csENDFOR}.
	 * @param ctx the parse tree
	 */
	void enterCsENDFOR(RpgParser.CsENDFORContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csENDFOR}.
	 * @param ctx the parse tree
	 */
	void exitCsENDFOR(RpgParser.CsENDFORContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csENDIF}.
	 * @param ctx the parse tree
	 */
	void enterCsENDIF(RpgParser.CsENDIFContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csENDIF}.
	 * @param ctx the parse tree
	 */
	void exitCsENDIF(RpgParser.CsENDIFContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csENDMON}.
	 * @param ctx the parse tree
	 */
	void enterCsENDMON(RpgParser.CsENDMONContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csENDMON}.
	 * @param ctx the parse tree
	 */
	void exitCsENDMON(RpgParser.CsENDMONContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csENDSL}.
	 * @param ctx the parse tree
	 */
	void enterCsENDSL(RpgParser.CsENDSLContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csENDSL}.
	 * @param ctx the parse tree
	 */
	void exitCsENDSL(RpgParser.CsENDSLContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csEVAL}.
	 * @param ctx the parse tree
	 */
	void enterCsEVAL(RpgParser.CsEVALContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csEVAL}.
	 * @param ctx the parse tree
	 */
	void exitCsEVAL(RpgParser.CsEVALContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csEVAL_CORR}.
	 * @param ctx the parse tree
	 */
	void enterCsEVAL_CORR(RpgParser.CsEVAL_CORRContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csEVAL_CORR}.
	 * @param ctx the parse tree
	 */
	void exitCsEVAL_CORR(RpgParser.CsEVAL_CORRContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csEVALR}.
	 * @param ctx the parse tree
	 */
	void enterCsEVALR(RpgParser.CsEVALRContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csEVALR}.
	 * @param ctx the parse tree
	 */
	void exitCsEVALR(RpgParser.CsEVALRContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csEXCEPT}.
	 * @param ctx the parse tree
	 */
	void enterCsEXCEPT(RpgParser.CsEXCEPTContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csEXCEPT}.
	 * @param ctx the parse tree
	 */
	void exitCsEXCEPT(RpgParser.CsEXCEPTContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csEXFMT}.
	 * @param ctx the parse tree
	 */
	void enterCsEXFMT(RpgParser.CsEXFMTContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csEXFMT}.
	 * @param ctx the parse tree
	 */
	void exitCsEXFMT(RpgParser.CsEXFMTContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csEXSR}.
	 * @param ctx the parse tree
	 */
	void enterCsEXSR(RpgParser.CsEXSRContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csEXSR}.
	 * @param ctx the parse tree
	 */
	void exitCsEXSR(RpgParser.CsEXSRContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csEXTRCT}.
	 * @param ctx the parse tree
	 */
	void enterCsEXTRCT(RpgParser.CsEXTRCTContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csEXTRCT}.
	 * @param ctx the parse tree
	 */
	void exitCsEXTRCT(RpgParser.CsEXTRCTContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csFEOD}.
	 * @param ctx the parse tree
	 */
	void enterCsFEOD(RpgParser.CsFEODContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csFEOD}.
	 * @param ctx the parse tree
	 */
	void exitCsFEOD(RpgParser.CsFEODContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csFOR}.
	 * @param ctx the parse tree
	 */
	void enterCsFOR(RpgParser.CsFORContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csFOR}.
	 * @param ctx the parse tree
	 */
	void exitCsFOR(RpgParser.CsFORContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csFORCE}.
	 * @param ctx the parse tree
	 */
	void enterCsFORCE(RpgParser.CsFORCEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csFORCE}.
	 * @param ctx the parse tree
	 */
	void exitCsFORCE(RpgParser.CsFORCEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csGOTO}.
	 * @param ctx the parse tree
	 */
	void enterCsGOTO(RpgParser.CsGOTOContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csGOTO}.
	 * @param ctx the parse tree
	 */
	void exitCsGOTO(RpgParser.CsGOTOContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csIF}.
	 * @param ctx the parse tree
	 */
	void enterCsIF(RpgParser.CsIFContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csIF}.
	 * @param ctx the parse tree
	 */
	void exitCsIF(RpgParser.CsIFContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csIFEQ}.
	 * @param ctx the parse tree
	 */
	void enterCsIFEQ(RpgParser.CsIFEQContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csIFEQ}.
	 * @param ctx the parse tree
	 */
	void exitCsIFEQ(RpgParser.CsIFEQContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csIFNE}.
	 * @param ctx the parse tree
	 */
	void enterCsIFNE(RpgParser.CsIFNEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csIFNE}.
	 * @param ctx the parse tree
	 */
	void exitCsIFNE(RpgParser.CsIFNEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csIFLE}.
	 * @param ctx the parse tree
	 */
	void enterCsIFLE(RpgParser.CsIFLEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csIFLE}.
	 * @param ctx the parse tree
	 */
	void exitCsIFLE(RpgParser.CsIFLEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csIFLT}.
	 * @param ctx the parse tree
	 */
	void enterCsIFLT(RpgParser.CsIFLTContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csIFLT}.
	 * @param ctx the parse tree
	 */
	void exitCsIFLT(RpgParser.CsIFLTContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csIFGE}.
	 * @param ctx the parse tree
	 */
	void enterCsIFGE(RpgParser.CsIFGEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csIFGE}.
	 * @param ctx the parse tree
	 */
	void exitCsIFGE(RpgParser.CsIFGEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csIFGT}.
	 * @param ctx the parse tree
	 */
	void enterCsIFGT(RpgParser.CsIFGTContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csIFGT}.
	 * @param ctx the parse tree
	 */
	void exitCsIFGT(RpgParser.CsIFGTContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csIN}.
	 * @param ctx the parse tree
	 */
	void enterCsIN(RpgParser.CsINContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csIN}.
	 * @param ctx the parse tree
	 */
	void exitCsIN(RpgParser.CsINContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csITER}.
	 * @param ctx the parse tree
	 */
	void enterCsITER(RpgParser.CsITERContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csITER}.
	 * @param ctx the parse tree
	 */
	void exitCsITER(RpgParser.CsITERContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csKLIST}.
	 * @param ctx the parse tree
	 */
	void enterCsKLIST(RpgParser.CsKLISTContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csKLIST}.
	 * @param ctx the parse tree
	 */
	void exitCsKLIST(RpgParser.CsKLISTContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csKFLD}.
	 * @param ctx the parse tree
	 */
	void enterCsKFLD(RpgParser.CsKFLDContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csKFLD}.
	 * @param ctx the parse tree
	 */
	void exitCsKFLD(RpgParser.CsKFLDContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csLEAVE}.
	 * @param ctx the parse tree
	 */
	void enterCsLEAVE(RpgParser.CsLEAVEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csLEAVE}.
	 * @param ctx the parse tree
	 */
	void exitCsLEAVE(RpgParser.CsLEAVEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csLEAVESR}.
	 * @param ctx the parse tree
	 */
	void enterCsLEAVESR(RpgParser.CsLEAVESRContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csLEAVESR}.
	 * @param ctx the parse tree
	 */
	void exitCsLEAVESR(RpgParser.CsLEAVESRContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csLOOKUP}.
	 * @param ctx the parse tree
	 */
	void enterCsLOOKUP(RpgParser.CsLOOKUPContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csLOOKUP}.
	 * @param ctx the parse tree
	 */
	void exitCsLOOKUP(RpgParser.CsLOOKUPContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csMHHZO}.
	 * @param ctx the parse tree
	 */
	void enterCsMHHZO(RpgParser.CsMHHZOContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csMHHZO}.
	 * @param ctx the parse tree
	 */
	void exitCsMHHZO(RpgParser.CsMHHZOContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csMHLZO}.
	 * @param ctx the parse tree
	 */
	void enterCsMHLZO(RpgParser.CsMHLZOContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csMHLZO}.
	 * @param ctx the parse tree
	 */
	void exitCsMHLZO(RpgParser.CsMHLZOContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csMLHZO}.
	 * @param ctx the parse tree
	 */
	void enterCsMLHZO(RpgParser.CsMLHZOContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csMLHZO}.
	 * @param ctx the parse tree
	 */
	void exitCsMLHZO(RpgParser.CsMLHZOContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csMLLZO}.
	 * @param ctx the parse tree
	 */
	void enterCsMLLZO(RpgParser.CsMLLZOContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csMLLZO}.
	 * @param ctx the parse tree
	 */
	void exitCsMLLZO(RpgParser.CsMLLZOContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csMONITOR}.
	 * @param ctx the parse tree
	 */
	void enterCsMONITOR(RpgParser.CsMONITORContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csMONITOR}.
	 * @param ctx the parse tree
	 */
	void exitCsMONITOR(RpgParser.CsMONITORContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csMOVE}.
	 * @param ctx the parse tree
	 */
	void enterCsMOVE(RpgParser.CsMOVEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csMOVE}.
	 * @param ctx the parse tree
	 */
	void exitCsMOVE(RpgParser.CsMOVEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csMOVEA}.
	 * @param ctx the parse tree
	 */
	void enterCsMOVEA(RpgParser.CsMOVEAContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csMOVEA}.
	 * @param ctx the parse tree
	 */
	void exitCsMOVEA(RpgParser.CsMOVEAContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csMOVEL}.
	 * @param ctx the parse tree
	 */
	void enterCsMOVEL(RpgParser.CsMOVELContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csMOVEL}.
	 * @param ctx the parse tree
	 */
	void exitCsMOVEL(RpgParser.CsMOVELContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csMULT}.
	 * @param ctx the parse tree
	 */
	void enterCsMULT(RpgParser.CsMULTContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csMULT}.
	 * @param ctx the parse tree
	 */
	void exitCsMULT(RpgParser.CsMULTContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csMVR}.
	 * @param ctx the parse tree
	 */
	void enterCsMVR(RpgParser.CsMVRContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csMVR}.
	 * @param ctx the parse tree
	 */
	void exitCsMVR(RpgParser.CsMVRContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csNEXT}.
	 * @param ctx the parse tree
	 */
	void enterCsNEXT(RpgParser.CsNEXTContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csNEXT}.
	 * @param ctx the parse tree
	 */
	void exitCsNEXT(RpgParser.CsNEXTContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csOCCUR}.
	 * @param ctx the parse tree
	 */
	void enterCsOCCUR(RpgParser.CsOCCURContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csOCCUR}.
	 * @param ctx the parse tree
	 */
	void exitCsOCCUR(RpgParser.CsOCCURContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csON_ERROR}.
	 * @param ctx the parse tree
	 */
	void enterCsON_ERROR(RpgParser.CsON_ERRORContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csON_ERROR}.
	 * @param ctx the parse tree
	 */
	void exitCsON_ERROR(RpgParser.CsON_ERRORContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#onErrorCode}.
	 * @param ctx the parse tree
	 */
	void enterOnErrorCode(RpgParser.OnErrorCodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#onErrorCode}.
	 * @param ctx the parse tree
	 */
	void exitOnErrorCode(RpgParser.OnErrorCodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csOPEN}.
	 * @param ctx the parse tree
	 */
	void enterCsOPEN(RpgParser.CsOPENContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csOPEN}.
	 * @param ctx the parse tree
	 */
	void exitCsOPEN(RpgParser.CsOPENContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csOREQ}.
	 * @param ctx the parse tree
	 */
	void enterCsOREQ(RpgParser.CsOREQContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csOREQ}.
	 * @param ctx the parse tree
	 */
	void exitCsOREQ(RpgParser.CsOREQContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csORNE}.
	 * @param ctx the parse tree
	 */
	void enterCsORNE(RpgParser.CsORNEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csORNE}.
	 * @param ctx the parse tree
	 */
	void exitCsORNE(RpgParser.CsORNEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csORLE}.
	 * @param ctx the parse tree
	 */
	void enterCsORLE(RpgParser.CsORLEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csORLE}.
	 * @param ctx the parse tree
	 */
	void exitCsORLE(RpgParser.CsORLEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csORLT}.
	 * @param ctx the parse tree
	 */
	void enterCsORLT(RpgParser.CsORLTContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csORLT}.
	 * @param ctx the parse tree
	 */
	void exitCsORLT(RpgParser.CsORLTContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csORGE}.
	 * @param ctx the parse tree
	 */
	void enterCsORGE(RpgParser.CsORGEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csORGE}.
	 * @param ctx the parse tree
	 */
	void exitCsORGE(RpgParser.CsORGEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csORGT}.
	 * @param ctx the parse tree
	 */
	void enterCsORGT(RpgParser.CsORGTContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csORGT}.
	 * @param ctx the parse tree
	 */
	void exitCsORGT(RpgParser.CsORGTContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csOTHER}.
	 * @param ctx the parse tree
	 */
	void enterCsOTHER(RpgParser.CsOTHERContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csOTHER}.
	 * @param ctx the parse tree
	 */
	void exitCsOTHER(RpgParser.CsOTHERContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csOUT}.
	 * @param ctx the parse tree
	 */
	void enterCsOUT(RpgParser.CsOUTContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csOUT}.
	 * @param ctx the parse tree
	 */
	void exitCsOUT(RpgParser.CsOUTContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csPARM}.
	 * @param ctx the parse tree
	 */
	void enterCsPARM(RpgParser.CsPARMContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csPARM}.
	 * @param ctx the parse tree
	 */
	void exitCsPARM(RpgParser.CsPARMContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csPLIST}.
	 * @param ctx the parse tree
	 */
	void enterCsPLIST(RpgParser.CsPLISTContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csPLIST}.
	 * @param ctx the parse tree
	 */
	void exitCsPLIST(RpgParser.CsPLISTContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csPOST}.
	 * @param ctx the parse tree
	 */
	void enterCsPOST(RpgParser.CsPOSTContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csPOST}.
	 * @param ctx the parse tree
	 */
	void exitCsPOST(RpgParser.CsPOSTContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csREAD}.
	 * @param ctx the parse tree
	 */
	void enterCsREAD(RpgParser.CsREADContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csREAD}.
	 * @param ctx the parse tree
	 */
	void exitCsREAD(RpgParser.CsREADContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csREADC}.
	 * @param ctx the parse tree
	 */
	void enterCsREADC(RpgParser.CsREADCContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csREADC}.
	 * @param ctx the parse tree
	 */
	void exitCsREADC(RpgParser.CsREADCContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csREADE}.
	 * @param ctx the parse tree
	 */
	void enterCsREADE(RpgParser.CsREADEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csREADE}.
	 * @param ctx the parse tree
	 */
	void exitCsREADE(RpgParser.CsREADEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csREADP}.
	 * @param ctx the parse tree
	 */
	void enterCsREADP(RpgParser.CsREADPContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csREADP}.
	 * @param ctx the parse tree
	 */
	void exitCsREADP(RpgParser.CsREADPContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csREADPE}.
	 * @param ctx the parse tree
	 */
	void enterCsREADPE(RpgParser.CsREADPEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csREADPE}.
	 * @param ctx the parse tree
	 */
	void exitCsREADPE(RpgParser.CsREADPEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csREALLOC}.
	 * @param ctx the parse tree
	 */
	void enterCsREALLOC(RpgParser.CsREALLOCContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csREALLOC}.
	 * @param ctx the parse tree
	 */
	void exitCsREALLOC(RpgParser.CsREALLOCContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csREL}.
	 * @param ctx the parse tree
	 */
	void enterCsREL(RpgParser.CsRELContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csREL}.
	 * @param ctx the parse tree
	 */
	void exitCsREL(RpgParser.CsRELContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csRESET}.
	 * @param ctx the parse tree
	 */
	void enterCsRESET(RpgParser.CsRESETContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csRESET}.
	 * @param ctx the parse tree
	 */
	void exitCsRESET(RpgParser.CsRESETContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csRETURN}.
	 * @param ctx the parse tree
	 */
	void enterCsRETURN(RpgParser.CsRETURNContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csRETURN}.
	 * @param ctx the parse tree
	 */
	void exitCsRETURN(RpgParser.CsRETURNContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csROLBK}.
	 * @param ctx the parse tree
	 */
	void enterCsROLBK(RpgParser.CsROLBKContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csROLBK}.
	 * @param ctx the parse tree
	 */
	void exitCsROLBK(RpgParser.CsROLBKContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csSCAN}.
	 * @param ctx the parse tree
	 */
	void enterCsSCAN(RpgParser.CsSCANContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csSCAN}.
	 * @param ctx the parse tree
	 */
	void exitCsSCAN(RpgParser.CsSCANContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csSELECT}.
	 * @param ctx the parse tree
	 */
	void enterCsSELECT(RpgParser.CsSELECTContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csSELECT}.
	 * @param ctx the parse tree
	 */
	void exitCsSELECT(RpgParser.CsSELECTContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csSETGT}.
	 * @param ctx the parse tree
	 */
	void enterCsSETGT(RpgParser.CsSETGTContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csSETGT}.
	 * @param ctx the parse tree
	 */
	void exitCsSETGT(RpgParser.CsSETGTContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csSETLL}.
	 * @param ctx the parse tree
	 */
	void enterCsSETLL(RpgParser.CsSETLLContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csSETLL}.
	 * @param ctx the parse tree
	 */
	void exitCsSETLL(RpgParser.CsSETLLContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csSETOFF}.
	 * @param ctx the parse tree
	 */
	void enterCsSETOFF(RpgParser.CsSETOFFContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csSETOFF}.
	 * @param ctx the parse tree
	 */
	void exitCsSETOFF(RpgParser.CsSETOFFContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csSETON}.
	 * @param ctx the parse tree
	 */
	void enterCsSETON(RpgParser.CsSETONContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csSETON}.
	 * @param ctx the parse tree
	 */
	void exitCsSETON(RpgParser.CsSETONContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csSHTDN}.
	 * @param ctx the parse tree
	 */
	void enterCsSHTDN(RpgParser.CsSHTDNContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csSHTDN}.
	 * @param ctx the parse tree
	 */
	void exitCsSHTDN(RpgParser.CsSHTDNContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csSORTA}.
	 * @param ctx the parse tree
	 */
	void enterCsSORTA(RpgParser.CsSORTAContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csSORTA}.
	 * @param ctx the parse tree
	 */
	void exitCsSORTA(RpgParser.CsSORTAContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csSQRT}.
	 * @param ctx the parse tree
	 */
	void enterCsSQRT(RpgParser.CsSQRTContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csSQRT}.
	 * @param ctx the parse tree
	 */
	void exitCsSQRT(RpgParser.CsSQRTContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csSUB}.
	 * @param ctx the parse tree
	 */
	void enterCsSUB(RpgParser.CsSUBContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csSUB}.
	 * @param ctx the parse tree
	 */
	void exitCsSUB(RpgParser.CsSUBContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csSUBDUR}.
	 * @param ctx the parse tree
	 */
	void enterCsSUBDUR(RpgParser.CsSUBDURContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csSUBDUR}.
	 * @param ctx the parse tree
	 */
	void exitCsSUBDUR(RpgParser.CsSUBDURContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csSUBST}.
	 * @param ctx the parse tree
	 */
	void enterCsSUBST(RpgParser.CsSUBSTContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csSUBST}.
	 * @param ctx the parse tree
	 */
	void exitCsSUBST(RpgParser.CsSUBSTContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csTAG}.
	 * @param ctx the parse tree
	 */
	void enterCsTAG(RpgParser.CsTAGContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csTAG}.
	 * @param ctx the parse tree
	 */
	void exitCsTAG(RpgParser.CsTAGContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csTEST}.
	 * @param ctx the parse tree
	 */
	void enterCsTEST(RpgParser.CsTESTContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csTEST}.
	 * @param ctx the parse tree
	 */
	void exitCsTEST(RpgParser.CsTESTContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csTESTB}.
	 * @param ctx the parse tree
	 */
	void enterCsTESTB(RpgParser.CsTESTBContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csTESTB}.
	 * @param ctx the parse tree
	 */
	void exitCsTESTB(RpgParser.CsTESTBContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csTESTN}.
	 * @param ctx the parse tree
	 */
	void enterCsTESTN(RpgParser.CsTESTNContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csTESTN}.
	 * @param ctx the parse tree
	 */
	void exitCsTESTN(RpgParser.CsTESTNContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csTESTZ}.
	 * @param ctx the parse tree
	 */
	void enterCsTESTZ(RpgParser.CsTESTZContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csTESTZ}.
	 * @param ctx the parse tree
	 */
	void exitCsTESTZ(RpgParser.CsTESTZContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csTIME}.
	 * @param ctx the parse tree
	 */
	void enterCsTIME(RpgParser.CsTIMEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csTIME}.
	 * @param ctx the parse tree
	 */
	void exitCsTIME(RpgParser.CsTIMEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csUNLOCK}.
	 * @param ctx the parse tree
	 */
	void enterCsUNLOCK(RpgParser.CsUNLOCKContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csUNLOCK}.
	 * @param ctx the parse tree
	 */
	void exitCsUNLOCK(RpgParser.CsUNLOCKContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csUPDATE}.
	 * @param ctx the parse tree
	 */
	void enterCsUPDATE(RpgParser.CsUPDATEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csUPDATE}.
	 * @param ctx the parse tree
	 */
	void exitCsUPDATE(RpgParser.CsUPDATEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csWHEN}.
	 * @param ctx the parse tree
	 */
	void enterCsWHEN(RpgParser.CsWHENContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csWHEN}.
	 * @param ctx the parse tree
	 */
	void exitCsWHEN(RpgParser.CsWHENContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csWHENEQ}.
	 * @param ctx the parse tree
	 */
	void enterCsWHENEQ(RpgParser.CsWHENEQContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csWHENEQ}.
	 * @param ctx the parse tree
	 */
	void exitCsWHENEQ(RpgParser.CsWHENEQContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csWHENNE}.
	 * @param ctx the parse tree
	 */
	void enterCsWHENNE(RpgParser.CsWHENNEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csWHENNE}.
	 * @param ctx the parse tree
	 */
	void exitCsWHENNE(RpgParser.CsWHENNEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csWHENLE}.
	 * @param ctx the parse tree
	 */
	void enterCsWHENLE(RpgParser.CsWHENLEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csWHENLE}.
	 * @param ctx the parse tree
	 */
	void exitCsWHENLE(RpgParser.CsWHENLEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csWHENLT}.
	 * @param ctx the parse tree
	 */
	void enterCsWHENLT(RpgParser.CsWHENLTContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csWHENLT}.
	 * @param ctx the parse tree
	 */
	void exitCsWHENLT(RpgParser.CsWHENLTContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csWHENGE}.
	 * @param ctx the parse tree
	 */
	void enterCsWHENGE(RpgParser.CsWHENGEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csWHENGE}.
	 * @param ctx the parse tree
	 */
	void exitCsWHENGE(RpgParser.CsWHENGEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csWHENGT}.
	 * @param ctx the parse tree
	 */
	void enterCsWHENGT(RpgParser.CsWHENGTContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csWHENGT}.
	 * @param ctx the parse tree
	 */
	void exitCsWHENGT(RpgParser.CsWHENGTContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csWRITE}.
	 * @param ctx the parse tree
	 */
	void enterCsWRITE(RpgParser.CsWRITEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csWRITE}.
	 * @param ctx the parse tree
	 */
	void exitCsWRITE(RpgParser.CsWRITEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csXFOOT}.
	 * @param ctx the parse tree
	 */
	void enterCsXFOOT(RpgParser.CsXFOOTContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csXFOOT}.
	 * @param ctx the parse tree
	 */
	void exitCsXFOOT(RpgParser.CsXFOOTContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csXLATE}.
	 * @param ctx the parse tree
	 */
	void enterCsXLATE(RpgParser.CsXLATEContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csXLATE}.
	 * @param ctx the parse tree
	 */
	void exitCsXLATE(RpgParser.CsXLATEContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csXML_INTO}.
	 * @param ctx the parse tree
	 */
	void enterCsXML_INTO(RpgParser.CsXML_INTOContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csXML_INTO}.
	 * @param ctx the parse tree
	 */
	void exitCsXML_INTO(RpgParser.CsXML_INTOContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csXML_SAX}.
	 * @param ctx the parse tree
	 */
	void enterCsXML_SAX(RpgParser.CsXML_SAXContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csXML_SAX}.
	 * @param ctx the parse tree
	 */
	void exitCsXML_SAX(RpgParser.CsXML_SAXContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csZ_ADD}.
	 * @param ctx the parse tree
	 */
	void enterCsZ_ADD(RpgParser.CsZ_ADDContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csZ_ADD}.
	 * @param ctx the parse tree
	 */
	void exitCsZ_ADD(RpgParser.CsZ_ADDContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csZ_SUB}.
	 * @param ctx the parse tree
	 */
	void enterCsZ_SUB(RpgParser.CsZ_SUBContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csZ_SUB}.
	 * @param ctx the parse tree
	 */
	void exitCsZ_SUB(RpgParser.CsZ_SUBContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#cs_operationExtender}.
	 * @param ctx the parse tree
	 */
	void enterCs_operationExtender(RpgParser.Cs_operationExtenderContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#cs_operationExtender}.
	 * @param ctx the parse tree
	 */
	void exitCs_operationExtender(RpgParser.Cs_operationExtenderContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#factor}.
	 * @param ctx the parse tree
	 */
	void enterFactor(RpgParser.FactorContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#factor}.
	 * @param ctx the parse tree
	 */
	void exitFactor(RpgParser.FactorContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#factorContent}.
	 * @param ctx the parse tree
	 */
	void enterFactorContent(RpgParser.FactorContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#factorContent}.
	 * @param ctx the parse tree
	 */
	void exitFactorContent(RpgParser.FactorContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#resultType}.
	 * @param ctx the parse tree
	 */
	void enterResultType(RpgParser.ResultTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#resultType}.
	 * @param ctx the parse tree
	 */
	void exitResultType(RpgParser.ResultTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#cs_fixed_comments}.
	 * @param ctx the parse tree
	 */
	void enterCs_fixed_comments(RpgParser.Cs_fixed_commentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#cs_fixed_comments}.
	 * @param ctx the parse tree
	 */
	void exitCs_fixed_comments(RpgParser.Cs_fixed_commentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#cspec_fixed_x2}.
	 * @param ctx the parse tree
	 */
	void enterCspec_fixed_x2(RpgParser.Cspec_fixed_x2Context ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#cspec_fixed_x2}.
	 * @param ctx the parse tree
	 */
	void exitCspec_fixed_x2(RpgParser.Cspec_fixed_x2Context ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#csOperationAndExtendedFactor2}.
	 * @param ctx the parse tree
	 */
	void enterCsOperationAndExtendedFactor2(RpgParser.CsOperationAndExtendedFactor2Context ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#csOperationAndExtendedFactor2}.
	 * @param ctx the parse tree
	 */
	void exitCsOperationAndExtendedFactor2(RpgParser.CsOperationAndExtendedFactor2Context ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#ispec_fixed}.
	 * @param ctx the parse tree
	 */
	void enterIspec_fixed(RpgParser.Ispec_fixedContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#ispec_fixed}.
	 * @param ctx the parse tree
	 */
	void exitIspec_fixed(RpgParser.Ispec_fixedContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#fieldRecordRelation}.
	 * @param ctx the parse tree
	 */
	void enterFieldRecordRelation(RpgParser.FieldRecordRelationContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#fieldRecordRelation}.
	 * @param ctx the parse tree
	 */
	void exitFieldRecordRelation(RpgParser.FieldRecordRelationContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#fieldIndicator}.
	 * @param ctx the parse tree
	 */
	void enterFieldIndicator(RpgParser.FieldIndicatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#fieldIndicator}.
	 * @param ctx the parse tree
	 */
	void exitFieldIndicator(RpgParser.FieldIndicatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#is_external_rec}.
	 * @param ctx the parse tree
	 */
	void enterIs_external_rec(RpgParser.Is_external_recContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#is_external_rec}.
	 * @param ctx the parse tree
	 */
	void exitIs_external_rec(RpgParser.Is_external_recContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#is_rec}.
	 * @param ctx the parse tree
	 */
	void enterIs_rec(RpgParser.Is_recContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#is_rec}.
	 * @param ctx the parse tree
	 */
	void exitIs_rec(RpgParser.Is_recContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#recordIdIndicator}.
	 * @param ctx the parse tree
	 */
	void enterRecordIdIndicator(RpgParser.RecordIdIndicatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#recordIdIndicator}.
	 * @param ctx the parse tree
	 */
	void exitRecordIdIndicator(RpgParser.RecordIdIndicatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#is_external_field}.
	 * @param ctx the parse tree
	 */
	void enterIs_external_field(RpgParser.Is_external_fieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#is_external_field}.
	 * @param ctx the parse tree
	 */
	void exitIs_external_field(RpgParser.Is_external_fieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#controlLevelIndicator}.
	 * @param ctx the parse tree
	 */
	void enterControlLevelIndicator(RpgParser.ControlLevelIndicatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#controlLevelIndicator}.
	 * @param ctx the parse tree
	 */
	void exitControlLevelIndicator(RpgParser.ControlLevelIndicatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#matchingFieldsIndicator}.
	 * @param ctx the parse tree
	 */
	void enterMatchingFieldsIndicator(RpgParser.MatchingFieldsIndicatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#matchingFieldsIndicator}.
	 * @param ctx the parse tree
	 */
	void exitMatchingFieldsIndicator(RpgParser.MatchingFieldsIndicatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#hspec_fixed}.
	 * @param ctx the parse tree
	 */
	void enterHspec_fixed(RpgParser.Hspec_fixedContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#hspec_fixed}.
	 * @param ctx the parse tree
	 */
	void exitHspec_fixed(RpgParser.Hspec_fixedContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#hs_expression}.
	 * @param ctx the parse tree
	 */
	void enterHs_expression(RpgParser.Hs_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#hs_expression}.
	 * @param ctx the parse tree
	 */
	void exitHs_expression(RpgParser.Hs_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#hs_parm}.
	 * @param ctx the parse tree
	 */
	void enterHs_parm(RpgParser.Hs_parmContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#hs_parm}.
	 * @param ctx the parse tree
	 */
	void exitHs_parm(RpgParser.Hs_parmContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#hs_string}.
	 * @param ctx the parse tree
	 */
	void enterHs_string(RpgParser.Hs_stringContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#hs_string}.
	 * @param ctx the parse tree
	 */
	void exitHs_string(RpgParser.Hs_stringContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#blank_line}.
	 * @param ctx the parse tree
	 */
	void enterBlank_line(RpgParser.Blank_lineContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#blank_line}.
	 * @param ctx the parse tree
	 */
	void exitBlank_line(RpgParser.Blank_lineContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#directive}.
	 * @param ctx the parse tree
	 */
	void enterDirective(RpgParser.DirectiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#directive}.
	 * @param ctx the parse tree
	 */
	void exitDirective(RpgParser.DirectiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#space_directive}.
	 * @param ctx the parse tree
	 */
	void enterSpace_directive(RpgParser.Space_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#space_directive}.
	 * @param ctx the parse tree
	 */
	void exitSpace_directive(RpgParser.Space_directiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#dir_copy}.
	 * @param ctx the parse tree
	 */
	void enterDir_copy(RpgParser.Dir_copyContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#dir_copy}.
	 * @param ctx the parse tree
	 */
	void exitDir_copy(RpgParser.Dir_copyContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#dir_include}.
	 * @param ctx the parse tree
	 */
	void enterDir_include(RpgParser.Dir_includeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#dir_include}.
	 * @param ctx the parse tree
	 */
	void exitDir_include(RpgParser.Dir_includeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#dir_if}.
	 * @param ctx the parse tree
	 */
	void enterDir_if(RpgParser.Dir_ifContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#dir_if}.
	 * @param ctx the parse tree
	 */
	void exitDir_if(RpgParser.Dir_ifContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#dir_elseif}.
	 * @param ctx the parse tree
	 */
	void enterDir_elseif(RpgParser.Dir_elseifContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#dir_elseif}.
	 * @param ctx the parse tree
	 */
	void exitDir_elseif(RpgParser.Dir_elseifContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#dir_else}.
	 * @param ctx the parse tree
	 */
	void enterDir_else(RpgParser.Dir_elseContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#dir_else}.
	 * @param ctx the parse tree
	 */
	void exitDir_else(RpgParser.Dir_elseContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#dir_endif}.
	 * @param ctx the parse tree
	 */
	void enterDir_endif(RpgParser.Dir_endifContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#dir_endif}.
	 * @param ctx the parse tree
	 */
	void exitDir_endif(RpgParser.Dir_endifContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#dir_define}.
	 * @param ctx the parse tree
	 */
	void enterDir_define(RpgParser.Dir_defineContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#dir_define}.
	 * @param ctx the parse tree
	 */
	void exitDir_define(RpgParser.Dir_defineContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#dir_undefine}.
	 * @param ctx the parse tree
	 */
	void enterDir_undefine(RpgParser.Dir_undefineContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#dir_undefine}.
	 * @param ctx the parse tree
	 */
	void exitDir_undefine(RpgParser.Dir_undefineContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#dir_eof}.
	 * @param ctx the parse tree
	 */
	void enterDir_eof(RpgParser.Dir_eofContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#dir_eof}.
	 * @param ctx the parse tree
	 */
	void exitDir_eof(RpgParser.Dir_eofContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#beginfree_directive}.
	 * @param ctx the parse tree
	 */
	void enterBeginfree_directive(RpgParser.Beginfree_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#beginfree_directive}.
	 * @param ctx the parse tree
	 */
	void exitBeginfree_directive(RpgParser.Beginfree_directiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#endfree_directive}.
	 * @param ctx the parse tree
	 */
	void enterEndfree_directive(RpgParser.Endfree_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#endfree_directive}.
	 * @param ctx the parse tree
	 */
	void exitEndfree_directive(RpgParser.Endfree_directiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#copyText}.
	 * @param ctx the parse tree
	 */
	void enterCopyText(RpgParser.CopyTextContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#copyText}.
	 * @param ctx the parse tree
	 */
	void exitCopyText(RpgParser.CopyTextContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#trailing_ws}.
	 * @param ctx the parse tree
	 */
	void enterTrailing_ws(RpgParser.Trailing_wsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#trailing_ws}.
	 * @param ctx the parse tree
	 */
	void exitTrailing_ws(RpgParser.Trailing_wsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#title_directive}.
	 * @param ctx the parse tree
	 */
	void enterTitle_directive(RpgParser.Title_directiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#title_directive}.
	 * @param ctx the parse tree
	 */
	void exitTitle_directive(RpgParser.Title_directiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#title_text}.
	 * @param ctx the parse tree
	 */
	void enterTitle_text(RpgParser.Title_textContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#title_text}.
	 * @param ctx the parse tree
	 */
	void exitTitle_text(RpgParser.Title_textContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op}.
	 * @param ctx the parse tree
	 */
	void enterOp(RpgParser.OpContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op}.
	 * @param ctx the parse tree
	 */
	void exitOp(RpgParser.OpContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_acq}.
	 * @param ctx the parse tree
	 */
	void enterOp_acq(RpgParser.Op_acqContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_acq}.
	 * @param ctx the parse tree
	 */
	void exitOp_acq(RpgParser.Op_acqContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_callp}.
	 * @param ctx the parse tree
	 */
	void enterOp_callp(RpgParser.Op_callpContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_callp}.
	 * @param ctx the parse tree
	 */
	void exitOp_callp(RpgParser.Op_callpContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_chain}.
	 * @param ctx the parse tree
	 */
	void enterOp_chain(RpgParser.Op_chainContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_chain}.
	 * @param ctx the parse tree
	 */
	void exitOp_chain(RpgParser.Op_chainContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_clear}.
	 * @param ctx the parse tree
	 */
	void enterOp_clear(RpgParser.Op_clearContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_clear}.
	 * @param ctx the parse tree
	 */
	void exitOp_clear(RpgParser.Op_clearContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_close}.
	 * @param ctx the parse tree
	 */
	void enterOp_close(RpgParser.Op_closeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_close}.
	 * @param ctx the parse tree
	 */
	void exitOp_close(RpgParser.Op_closeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_commit}.
	 * @param ctx the parse tree
	 */
	void enterOp_commit(RpgParser.Op_commitContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_commit}.
	 * @param ctx the parse tree
	 */
	void exitOp_commit(RpgParser.Op_commitContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_dealloc}.
	 * @param ctx the parse tree
	 */
	void enterOp_dealloc(RpgParser.Op_deallocContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_dealloc}.
	 * @param ctx the parse tree
	 */
	void exitOp_dealloc(RpgParser.Op_deallocContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_delete}.
	 * @param ctx the parse tree
	 */
	void enterOp_delete(RpgParser.Op_deleteContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_delete}.
	 * @param ctx the parse tree
	 */
	void exitOp_delete(RpgParser.Op_deleteContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_dou}.
	 * @param ctx the parse tree
	 */
	void enterOp_dou(RpgParser.Op_douContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_dou}.
	 * @param ctx the parse tree
	 */
	void exitOp_dou(RpgParser.Op_douContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_dow}.
	 * @param ctx the parse tree
	 */
	void enterOp_dow(RpgParser.Op_dowContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_dow}.
	 * @param ctx the parse tree
	 */
	void exitOp_dow(RpgParser.Op_dowContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_dsply}.
	 * @param ctx the parse tree
	 */
	void enterOp_dsply(RpgParser.Op_dsplyContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_dsply}.
	 * @param ctx the parse tree
	 */
	void exitOp_dsply(RpgParser.Op_dsplyContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_dump}.
	 * @param ctx the parse tree
	 */
	void enterOp_dump(RpgParser.Op_dumpContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_dump}.
	 * @param ctx the parse tree
	 */
	void exitOp_dump(RpgParser.Op_dumpContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_else}.
	 * @param ctx the parse tree
	 */
	void enterOp_else(RpgParser.Op_elseContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_else}.
	 * @param ctx the parse tree
	 */
	void exitOp_else(RpgParser.Op_elseContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_elseif}.
	 * @param ctx the parse tree
	 */
	void enterOp_elseif(RpgParser.Op_elseifContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_elseif}.
	 * @param ctx the parse tree
	 */
	void exitOp_elseif(RpgParser.Op_elseifContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_enddo}.
	 * @param ctx the parse tree
	 */
	void enterOp_enddo(RpgParser.Op_enddoContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_enddo}.
	 * @param ctx the parse tree
	 */
	void exitOp_enddo(RpgParser.Op_enddoContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_endfor}.
	 * @param ctx the parse tree
	 */
	void enterOp_endfor(RpgParser.Op_endforContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_endfor}.
	 * @param ctx the parse tree
	 */
	void exitOp_endfor(RpgParser.Op_endforContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_endif}.
	 * @param ctx the parse tree
	 */
	void enterOp_endif(RpgParser.Op_endifContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_endif}.
	 * @param ctx the parse tree
	 */
	void exitOp_endif(RpgParser.Op_endifContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_endmon}.
	 * @param ctx the parse tree
	 */
	void enterOp_endmon(RpgParser.Op_endmonContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_endmon}.
	 * @param ctx the parse tree
	 */
	void exitOp_endmon(RpgParser.Op_endmonContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_endsl}.
	 * @param ctx the parse tree
	 */
	void enterOp_endsl(RpgParser.Op_endslContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_endsl}.
	 * @param ctx the parse tree
	 */
	void exitOp_endsl(RpgParser.Op_endslContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_eval}.
	 * @param ctx the parse tree
	 */
	void enterOp_eval(RpgParser.Op_evalContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_eval}.
	 * @param ctx the parse tree
	 */
	void exitOp_eval(RpgParser.Op_evalContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_evalr}.
	 * @param ctx the parse tree
	 */
	void enterOp_evalr(RpgParser.Op_evalrContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_evalr}.
	 * @param ctx the parse tree
	 */
	void exitOp_evalr(RpgParser.Op_evalrContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_eval_corr}.
	 * @param ctx the parse tree
	 */
	void enterOp_eval_corr(RpgParser.Op_eval_corrContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_eval_corr}.
	 * @param ctx the parse tree
	 */
	void exitOp_eval_corr(RpgParser.Op_eval_corrContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_except}.
	 * @param ctx the parse tree
	 */
	void enterOp_except(RpgParser.Op_exceptContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_except}.
	 * @param ctx the parse tree
	 */
	void exitOp_except(RpgParser.Op_exceptContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_exfmt}.
	 * @param ctx the parse tree
	 */
	void enterOp_exfmt(RpgParser.Op_exfmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_exfmt}.
	 * @param ctx the parse tree
	 */
	void exitOp_exfmt(RpgParser.Op_exfmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_exsr}.
	 * @param ctx the parse tree
	 */
	void enterOp_exsr(RpgParser.Op_exsrContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_exsr}.
	 * @param ctx the parse tree
	 */
	void exitOp_exsr(RpgParser.Op_exsrContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_feod}.
	 * @param ctx the parse tree
	 */
	void enterOp_feod(RpgParser.Op_feodContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_feod}.
	 * @param ctx the parse tree
	 */
	void exitOp_feod(RpgParser.Op_feodContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_for}.
	 * @param ctx the parse tree
	 */
	void enterOp_for(RpgParser.Op_forContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_for}.
	 * @param ctx the parse tree
	 */
	void exitOp_for(RpgParser.Op_forContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_force}.
	 * @param ctx the parse tree
	 */
	void enterOp_force(RpgParser.Op_forceContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_force}.
	 * @param ctx the parse tree
	 */
	void exitOp_force(RpgParser.Op_forceContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_if}.
	 * @param ctx the parse tree
	 */
	void enterOp_if(RpgParser.Op_ifContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_if}.
	 * @param ctx the parse tree
	 */
	void exitOp_if(RpgParser.Op_ifContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_in}.
	 * @param ctx the parse tree
	 */
	void enterOp_in(RpgParser.Op_inContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_in}.
	 * @param ctx the parse tree
	 */
	void exitOp_in(RpgParser.Op_inContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_iter}.
	 * @param ctx the parse tree
	 */
	void enterOp_iter(RpgParser.Op_iterContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_iter}.
	 * @param ctx the parse tree
	 */
	void exitOp_iter(RpgParser.Op_iterContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_leave}.
	 * @param ctx the parse tree
	 */
	void enterOp_leave(RpgParser.Op_leaveContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_leave}.
	 * @param ctx the parse tree
	 */
	void exitOp_leave(RpgParser.Op_leaveContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_leavesr}.
	 * @param ctx the parse tree
	 */
	void enterOp_leavesr(RpgParser.Op_leavesrContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_leavesr}.
	 * @param ctx the parse tree
	 */
	void exitOp_leavesr(RpgParser.Op_leavesrContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_monitor}.
	 * @param ctx the parse tree
	 */
	void enterOp_monitor(RpgParser.Op_monitorContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_monitor}.
	 * @param ctx the parse tree
	 */
	void exitOp_monitor(RpgParser.Op_monitorContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_next}.
	 * @param ctx the parse tree
	 */
	void enterOp_next(RpgParser.Op_nextContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_next}.
	 * @param ctx the parse tree
	 */
	void exitOp_next(RpgParser.Op_nextContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_on_error}.
	 * @param ctx the parse tree
	 */
	void enterOp_on_error(RpgParser.Op_on_errorContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_on_error}.
	 * @param ctx the parse tree
	 */
	void exitOp_on_error(RpgParser.Op_on_errorContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_open}.
	 * @param ctx the parse tree
	 */
	void enterOp_open(RpgParser.Op_openContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_open}.
	 * @param ctx the parse tree
	 */
	void exitOp_open(RpgParser.Op_openContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_other}.
	 * @param ctx the parse tree
	 */
	void enterOp_other(RpgParser.Op_otherContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_other}.
	 * @param ctx the parse tree
	 */
	void exitOp_other(RpgParser.Op_otherContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_out}.
	 * @param ctx the parse tree
	 */
	void enterOp_out(RpgParser.Op_outContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_out}.
	 * @param ctx the parse tree
	 */
	void exitOp_out(RpgParser.Op_outContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_post}.
	 * @param ctx the parse tree
	 */
	void enterOp_post(RpgParser.Op_postContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_post}.
	 * @param ctx the parse tree
	 */
	void exitOp_post(RpgParser.Op_postContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_read}.
	 * @param ctx the parse tree
	 */
	void enterOp_read(RpgParser.Op_readContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_read}.
	 * @param ctx the parse tree
	 */
	void exitOp_read(RpgParser.Op_readContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_readc}.
	 * @param ctx the parse tree
	 */
	void enterOp_readc(RpgParser.Op_readcContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_readc}.
	 * @param ctx the parse tree
	 */
	void exitOp_readc(RpgParser.Op_readcContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_reade}.
	 * @param ctx the parse tree
	 */
	void enterOp_reade(RpgParser.Op_readeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_reade}.
	 * @param ctx the parse tree
	 */
	void exitOp_reade(RpgParser.Op_readeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_readp}.
	 * @param ctx the parse tree
	 */
	void enterOp_readp(RpgParser.Op_readpContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_readp}.
	 * @param ctx the parse tree
	 */
	void exitOp_readp(RpgParser.Op_readpContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_readpe}.
	 * @param ctx the parse tree
	 */
	void enterOp_readpe(RpgParser.Op_readpeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_readpe}.
	 * @param ctx the parse tree
	 */
	void exitOp_readpe(RpgParser.Op_readpeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_rel}.
	 * @param ctx the parse tree
	 */
	void enterOp_rel(RpgParser.Op_relContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_rel}.
	 * @param ctx the parse tree
	 */
	void exitOp_rel(RpgParser.Op_relContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_reset2}.
	 * @param ctx the parse tree
	 */
	void enterOp_reset2(RpgParser.Op_reset2Context ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_reset2}.
	 * @param ctx the parse tree
	 */
	void exitOp_reset2(RpgParser.Op_reset2Context ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_reset}.
	 * @param ctx the parse tree
	 */
	void enterOp_reset(RpgParser.Op_resetContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_reset}.
	 * @param ctx the parse tree
	 */
	void exitOp_reset(RpgParser.Op_resetContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_return}.
	 * @param ctx the parse tree
	 */
	void enterOp_return(RpgParser.Op_returnContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_return}.
	 * @param ctx the parse tree
	 */
	void exitOp_return(RpgParser.Op_returnContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_rolbk}.
	 * @param ctx the parse tree
	 */
	void enterOp_rolbk(RpgParser.Op_rolbkContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_rolbk}.
	 * @param ctx the parse tree
	 */
	void exitOp_rolbk(RpgParser.Op_rolbkContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_select}.
	 * @param ctx the parse tree
	 */
	void enterOp_select(RpgParser.Op_selectContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_select}.
	 * @param ctx the parse tree
	 */
	void exitOp_select(RpgParser.Op_selectContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_setgt}.
	 * @param ctx the parse tree
	 */
	void enterOp_setgt(RpgParser.Op_setgtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_setgt}.
	 * @param ctx the parse tree
	 */
	void exitOp_setgt(RpgParser.Op_setgtContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_setll}.
	 * @param ctx the parse tree
	 */
	void enterOp_setll(RpgParser.Op_setllContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_setll}.
	 * @param ctx the parse tree
	 */
	void exitOp_setll(RpgParser.Op_setllContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_sorta}.
	 * @param ctx the parse tree
	 */
	void enterOp_sorta(RpgParser.Op_sortaContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_sorta}.
	 * @param ctx the parse tree
	 */
	void exitOp_sorta(RpgParser.Op_sortaContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_test}.
	 * @param ctx the parse tree
	 */
	void enterOp_test(RpgParser.Op_testContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_test}.
	 * @param ctx the parse tree
	 */
	void exitOp_test(RpgParser.Op_testContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_unlock}.
	 * @param ctx the parse tree
	 */
	void enterOp_unlock(RpgParser.Op_unlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_unlock}.
	 * @param ctx the parse tree
	 */
	void exitOp_unlock(RpgParser.Op_unlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_update}.
	 * @param ctx the parse tree
	 */
	void enterOp_update(RpgParser.Op_updateContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_update}.
	 * @param ctx the parse tree
	 */
	void exitOp_update(RpgParser.Op_updateContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_when}.
	 * @param ctx the parse tree
	 */
	void enterOp_when(RpgParser.Op_whenContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_when}.
	 * @param ctx the parse tree
	 */
	void exitOp_when(RpgParser.Op_whenContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_write}.
	 * @param ctx the parse tree
	 */
	void enterOp_write(RpgParser.Op_writeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_write}.
	 * @param ctx the parse tree
	 */
	void exitOp_write(RpgParser.Op_writeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_xml_into}.
	 * @param ctx the parse tree
	 */
	void enterOp_xml_into(RpgParser.Op_xml_intoContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_xml_into}.
	 * @param ctx the parse tree
	 */
	void exitOp_xml_into(RpgParser.Op_xml_intoContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_xml_sax}.
	 * @param ctx the parse tree
	 */
	void enterOp_xml_sax(RpgParser.Op_xml_saxContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_xml_sax}.
	 * @param ctx the parse tree
	 */
	void exitOp_xml_sax(RpgParser.Op_xml_saxContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#search_arg}.
	 * @param ctx the parse tree
	 */
	void enterSearch_arg(RpgParser.Search_argContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#search_arg}.
	 * @param ctx the parse tree
	 */
	void exitSearch_arg(RpgParser.Search_argContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#op_code}.
	 * @param ctx the parse tree
	 */
	void enterOp_code(RpgParser.Op_codeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#op_code}.
	 * @param ctx the parse tree
	 */
	void exitOp_code(RpgParser.Op_codeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif}.
	 * @param ctx the parse tree
	 */
	void enterBif(RpgParser.BifContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif}.
	 * @param ctx the parse tree
	 */
	void exitBif(RpgParser.BifContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#optargs}.
	 * @param ctx the parse tree
	 */
	void enterOptargs(RpgParser.OptargsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#optargs}.
	 * @param ctx the parse tree
	 */
	void exitOptargs(RpgParser.OptargsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_charformat}.
	 * @param ctx the parse tree
	 */
	void enterBif_charformat(RpgParser.Bif_charformatContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_charformat}.
	 * @param ctx the parse tree
	 */
	void exitBif_charformat(RpgParser.Bif_charformatContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_dateformat}.
	 * @param ctx the parse tree
	 */
	void enterBif_dateformat(RpgParser.Bif_dateformatContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_dateformat}.
	 * @param ctx the parse tree
	 */
	void exitBif_dateformat(RpgParser.Bif_dateformatContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_timeformat}.
	 * @param ctx the parse tree
	 */
	void enterBif_timeformat(RpgParser.Bif_timeformatContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_timeformat}.
	 * @param ctx the parse tree
	 */
	void exitBif_timeformat(RpgParser.Bif_timeformatContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_editccurrency}.
	 * @param ctx the parse tree
	 */
	void enterBif_editccurrency(RpgParser.Bif_editccurrencyContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_editccurrency}.
	 * @param ctx the parse tree
	 */
	void exitBif_editccurrency(RpgParser.Bif_editccurrencyContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_lookupargs}.
	 * @param ctx the parse tree
	 */
	void enterBif_lookupargs(RpgParser.Bif_lookupargsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_lookupargs}.
	 * @param ctx the parse tree
	 */
	void exitBif_lookupargs(RpgParser.Bif_lookupargsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#durationCode}.
	 * @param ctx the parse tree
	 */
	void enterDurationCode(RpgParser.DurationCodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#durationCode}.
	 * @param ctx the parse tree
	 */
	void exitDurationCode(RpgParser.DurationCodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_timestampargs}.
	 * @param ctx the parse tree
	 */
	void enterBif_timestampargs(RpgParser.Bif_timestampargsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_timestampargs}.
	 * @param ctx the parse tree
	 */
	void exitBif_timestampargs(RpgParser.Bif_timestampargsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_tlookupargs}.
	 * @param ctx the parse tree
	 */
	void enterBif_tlookupargs(RpgParser.Bif_tlookupargsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_tlookupargs}.
	 * @param ctx the parse tree
	 */
	void exitBif_tlookupargs(RpgParser.Bif_tlookupargsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_abs}.
	 * @param ctx the parse tree
	 */
	void enterBif_abs(RpgParser.Bif_absContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_abs}.
	 * @param ctx the parse tree
	 */
	void exitBif_abs(RpgParser.Bif_absContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_addr}.
	 * @param ctx the parse tree
	 */
	void enterBif_addr(RpgParser.Bif_addrContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_addr}.
	 * @param ctx the parse tree
	 */
	void exitBif_addr(RpgParser.Bif_addrContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_alloc}.
	 * @param ctx the parse tree
	 */
	void enterBif_alloc(RpgParser.Bif_allocContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_alloc}.
	 * @param ctx the parse tree
	 */
	void exitBif_alloc(RpgParser.Bif_allocContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_bitand}.
	 * @param ctx the parse tree
	 */
	void enterBif_bitand(RpgParser.Bif_bitandContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_bitand}.
	 * @param ctx the parse tree
	 */
	void exitBif_bitand(RpgParser.Bif_bitandContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_bitnot}.
	 * @param ctx the parse tree
	 */
	void enterBif_bitnot(RpgParser.Bif_bitnotContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_bitnot}.
	 * @param ctx the parse tree
	 */
	void exitBif_bitnot(RpgParser.Bif_bitnotContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_bitor}.
	 * @param ctx the parse tree
	 */
	void enterBif_bitor(RpgParser.Bif_bitorContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_bitor}.
	 * @param ctx the parse tree
	 */
	void exitBif_bitor(RpgParser.Bif_bitorContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_bitxor}.
	 * @param ctx the parse tree
	 */
	void enterBif_bitxor(RpgParser.Bif_bitxorContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_bitxor}.
	 * @param ctx the parse tree
	 */
	void exitBif_bitxor(RpgParser.Bif_bitxorContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_char}.
	 * @param ctx the parse tree
	 */
	void enterBif_char(RpgParser.Bif_charContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_char}.
	 * @param ctx the parse tree
	 */
	void exitBif_char(RpgParser.Bif_charContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_check}.
	 * @param ctx the parse tree
	 */
	void enterBif_check(RpgParser.Bif_checkContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_check}.
	 * @param ctx the parse tree
	 */
	void exitBif_check(RpgParser.Bif_checkContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_checkr}.
	 * @param ctx the parse tree
	 */
	void enterBif_checkr(RpgParser.Bif_checkrContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_checkr}.
	 * @param ctx the parse tree
	 */
	void exitBif_checkr(RpgParser.Bif_checkrContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_date}.
	 * @param ctx the parse tree
	 */
	void enterBif_date(RpgParser.Bif_dateContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_date}.
	 * @param ctx the parse tree
	 */
	void exitBif_date(RpgParser.Bif_dateContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_days}.
	 * @param ctx the parse tree
	 */
	void enterBif_days(RpgParser.Bif_daysContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_days}.
	 * @param ctx the parse tree
	 */
	void exitBif_days(RpgParser.Bif_daysContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_dec}.
	 * @param ctx the parse tree
	 */
	void enterBif_dec(RpgParser.Bif_decContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_dec}.
	 * @param ctx the parse tree
	 */
	void exitBif_dec(RpgParser.Bif_decContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_dech}.
	 * @param ctx the parse tree
	 */
	void enterBif_dech(RpgParser.Bif_dechContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_dech}.
	 * @param ctx the parse tree
	 */
	void exitBif_dech(RpgParser.Bif_dechContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_decpos}.
	 * @param ctx the parse tree
	 */
	void enterBif_decpos(RpgParser.Bif_decposContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_decpos}.
	 * @param ctx the parse tree
	 */
	void exitBif_decpos(RpgParser.Bif_decposContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_diff}.
	 * @param ctx the parse tree
	 */
	void enterBif_diff(RpgParser.Bif_diffContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_diff}.
	 * @param ctx the parse tree
	 */
	void exitBif_diff(RpgParser.Bif_diffContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_div}.
	 * @param ctx the parse tree
	 */
	void enterBif_div(RpgParser.Bif_divContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_div}.
	 * @param ctx the parse tree
	 */
	void exitBif_div(RpgParser.Bif_divContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_editc}.
	 * @param ctx the parse tree
	 */
	void enterBif_editc(RpgParser.Bif_editcContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_editc}.
	 * @param ctx the parse tree
	 */
	void exitBif_editc(RpgParser.Bif_editcContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_editflt}.
	 * @param ctx the parse tree
	 */
	void enterBif_editflt(RpgParser.Bif_editfltContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_editflt}.
	 * @param ctx the parse tree
	 */
	void exitBif_editflt(RpgParser.Bif_editfltContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_editw}.
	 * @param ctx the parse tree
	 */
	void enterBif_editw(RpgParser.Bif_editwContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_editw}.
	 * @param ctx the parse tree
	 */
	void exitBif_editw(RpgParser.Bif_editwContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_elem}.
	 * @param ctx the parse tree
	 */
	void enterBif_elem(RpgParser.Bif_elemContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_elem}.
	 * @param ctx the parse tree
	 */
	void exitBif_elem(RpgParser.Bif_elemContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_eof}.
	 * @param ctx the parse tree
	 */
	void enterBif_eof(RpgParser.Bif_eofContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_eof}.
	 * @param ctx the parse tree
	 */
	void exitBif_eof(RpgParser.Bif_eofContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_equal}.
	 * @param ctx the parse tree
	 */
	void enterBif_equal(RpgParser.Bif_equalContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_equal}.
	 * @param ctx the parse tree
	 */
	void exitBif_equal(RpgParser.Bif_equalContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_error}.
	 * @param ctx the parse tree
	 */
	void enterBif_error(RpgParser.Bif_errorContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_error}.
	 * @param ctx the parse tree
	 */
	void exitBif_error(RpgParser.Bif_errorContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_fields}.
	 * @param ctx the parse tree
	 */
	void enterBif_fields(RpgParser.Bif_fieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_fields}.
	 * @param ctx the parse tree
	 */
	void exitBif_fields(RpgParser.Bif_fieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_float}.
	 * @param ctx the parse tree
	 */
	void enterBif_float(RpgParser.Bif_floatContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_float}.
	 * @param ctx the parse tree
	 */
	void exitBif_float(RpgParser.Bif_floatContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_found}.
	 * @param ctx the parse tree
	 */
	void enterBif_found(RpgParser.Bif_foundContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_found}.
	 * @param ctx the parse tree
	 */
	void exitBif_found(RpgParser.Bif_foundContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_graph}.
	 * @param ctx the parse tree
	 */
	void enterBif_graph(RpgParser.Bif_graphContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_graph}.
	 * @param ctx the parse tree
	 */
	void exitBif_graph(RpgParser.Bif_graphContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_handler}.
	 * @param ctx the parse tree
	 */
	void enterBif_handler(RpgParser.Bif_handlerContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_handler}.
	 * @param ctx the parse tree
	 */
	void exitBif_handler(RpgParser.Bif_handlerContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_hours}.
	 * @param ctx the parse tree
	 */
	void enterBif_hours(RpgParser.Bif_hoursContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_hours}.
	 * @param ctx the parse tree
	 */
	void exitBif_hours(RpgParser.Bif_hoursContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_int}.
	 * @param ctx the parse tree
	 */
	void enterBif_int(RpgParser.Bif_intContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_int}.
	 * @param ctx the parse tree
	 */
	void exitBif_int(RpgParser.Bif_intContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_inth}.
	 * @param ctx the parse tree
	 */
	void enterBif_inth(RpgParser.Bif_inthContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_inth}.
	 * @param ctx the parse tree
	 */
	void exitBif_inth(RpgParser.Bif_inthContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_kds}.
	 * @param ctx the parse tree
	 */
	void enterBif_kds(RpgParser.Bif_kdsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_kds}.
	 * @param ctx the parse tree
	 */
	void exitBif_kds(RpgParser.Bif_kdsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_len}.
	 * @param ctx the parse tree
	 */
	void enterBif_len(RpgParser.Bif_lenContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_len}.
	 * @param ctx the parse tree
	 */
	void exitBif_len(RpgParser.Bif_lenContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_lookup}.
	 * @param ctx the parse tree
	 */
	void enterBif_lookup(RpgParser.Bif_lookupContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_lookup}.
	 * @param ctx the parse tree
	 */
	void exitBif_lookup(RpgParser.Bif_lookupContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_lookuplt}.
	 * @param ctx the parse tree
	 */
	void enterBif_lookuplt(RpgParser.Bif_lookupltContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_lookuplt}.
	 * @param ctx the parse tree
	 */
	void exitBif_lookuplt(RpgParser.Bif_lookupltContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_lookuple}.
	 * @param ctx the parse tree
	 */
	void enterBif_lookuple(RpgParser.Bif_lookupleContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_lookuple}.
	 * @param ctx the parse tree
	 */
	void exitBif_lookuple(RpgParser.Bif_lookupleContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_lookupgt}.
	 * @param ctx the parse tree
	 */
	void enterBif_lookupgt(RpgParser.Bif_lookupgtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_lookupgt}.
	 * @param ctx the parse tree
	 */
	void exitBif_lookupgt(RpgParser.Bif_lookupgtContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_lookupge}.
	 * @param ctx the parse tree
	 */
	void enterBif_lookupge(RpgParser.Bif_lookupgeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_lookupge}.
	 * @param ctx the parse tree
	 */
	void exitBif_lookupge(RpgParser.Bif_lookupgeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_minutes}.
	 * @param ctx the parse tree
	 */
	void enterBif_minutes(RpgParser.Bif_minutesContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_minutes}.
	 * @param ctx the parse tree
	 */
	void exitBif_minutes(RpgParser.Bif_minutesContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_months}.
	 * @param ctx the parse tree
	 */
	void enterBif_months(RpgParser.Bif_monthsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_months}.
	 * @param ctx the parse tree
	 */
	void exitBif_months(RpgParser.Bif_monthsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_mseconds}.
	 * @param ctx the parse tree
	 */
	void enterBif_mseconds(RpgParser.Bif_msecondsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_mseconds}.
	 * @param ctx the parse tree
	 */
	void exitBif_mseconds(RpgParser.Bif_msecondsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_nullind}.
	 * @param ctx the parse tree
	 */
	void enterBif_nullind(RpgParser.Bif_nullindContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_nullind}.
	 * @param ctx the parse tree
	 */
	void exitBif_nullind(RpgParser.Bif_nullindContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_occur}.
	 * @param ctx the parse tree
	 */
	void enterBif_occur(RpgParser.Bif_occurContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_occur}.
	 * @param ctx the parse tree
	 */
	void exitBif_occur(RpgParser.Bif_occurContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_open}.
	 * @param ctx the parse tree
	 */
	void enterBif_open(RpgParser.Bif_openContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_open}.
	 * @param ctx the parse tree
	 */
	void exitBif_open(RpgParser.Bif_openContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_paddr}.
	 * @param ctx the parse tree
	 */
	void enterBif_paddr(RpgParser.Bif_paddrContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_paddr}.
	 * @param ctx the parse tree
	 */
	void exitBif_paddr(RpgParser.Bif_paddrContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_parms}.
	 * @param ctx the parse tree
	 */
	void enterBif_parms(RpgParser.Bif_parmsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_parms}.
	 * @param ctx the parse tree
	 */
	void exitBif_parms(RpgParser.Bif_parmsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_parmnum}.
	 * @param ctx the parse tree
	 */
	void enterBif_parmnum(RpgParser.Bif_parmnumContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_parmnum}.
	 * @param ctx the parse tree
	 */
	void exitBif_parmnum(RpgParser.Bif_parmnumContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_realloc}.
	 * @param ctx the parse tree
	 */
	void enterBif_realloc(RpgParser.Bif_reallocContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_realloc}.
	 * @param ctx the parse tree
	 */
	void exitBif_realloc(RpgParser.Bif_reallocContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_rem}.
	 * @param ctx the parse tree
	 */
	void enterBif_rem(RpgParser.Bif_remContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_rem}.
	 * @param ctx the parse tree
	 */
	void exitBif_rem(RpgParser.Bif_remContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_replace}.
	 * @param ctx the parse tree
	 */
	void enterBif_replace(RpgParser.Bif_replaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_replace}.
	 * @param ctx the parse tree
	 */
	void exitBif_replace(RpgParser.Bif_replaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_scan}.
	 * @param ctx the parse tree
	 */
	void enterBif_scan(RpgParser.Bif_scanContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_scan}.
	 * @param ctx the parse tree
	 */
	void exitBif_scan(RpgParser.Bif_scanContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_scanrpl}.
	 * @param ctx the parse tree
	 */
	void enterBif_scanrpl(RpgParser.Bif_scanrplContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_scanrpl}.
	 * @param ctx the parse tree
	 */
	void exitBif_scanrpl(RpgParser.Bif_scanrplContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_seconds}.
	 * @param ctx the parse tree
	 */
	void enterBif_seconds(RpgParser.Bif_secondsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_seconds}.
	 * @param ctx the parse tree
	 */
	void exitBif_seconds(RpgParser.Bif_secondsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_shtdn}.
	 * @param ctx the parse tree
	 */
	void enterBif_shtdn(RpgParser.Bif_shtdnContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_shtdn}.
	 * @param ctx the parse tree
	 */
	void exitBif_shtdn(RpgParser.Bif_shtdnContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_size}.
	 * @param ctx the parse tree
	 */
	void enterBif_size(RpgParser.Bif_sizeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_size}.
	 * @param ctx the parse tree
	 */
	void exitBif_size(RpgParser.Bif_sizeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_sqrt}.
	 * @param ctx the parse tree
	 */
	void enterBif_sqrt(RpgParser.Bif_sqrtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_sqrt}.
	 * @param ctx the parse tree
	 */
	void exitBif_sqrt(RpgParser.Bif_sqrtContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_status}.
	 * @param ctx the parse tree
	 */
	void enterBif_status(RpgParser.Bif_statusContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_status}.
	 * @param ctx the parse tree
	 */
	void exitBif_status(RpgParser.Bif_statusContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_str}.
	 * @param ctx the parse tree
	 */
	void enterBif_str(RpgParser.Bif_strContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_str}.
	 * @param ctx the parse tree
	 */
	void exitBif_str(RpgParser.Bif_strContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_subarr}.
	 * @param ctx the parse tree
	 */
	void enterBif_subarr(RpgParser.Bif_subarrContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_subarr}.
	 * @param ctx the parse tree
	 */
	void exitBif_subarr(RpgParser.Bif_subarrContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_subdt}.
	 * @param ctx the parse tree
	 */
	void enterBif_subdt(RpgParser.Bif_subdtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_subdt}.
	 * @param ctx the parse tree
	 */
	void exitBif_subdt(RpgParser.Bif_subdtContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_subst}.
	 * @param ctx the parse tree
	 */
	void enterBif_subst(RpgParser.Bif_substContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_subst}.
	 * @param ctx the parse tree
	 */
	void exitBif_subst(RpgParser.Bif_substContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_this}.
	 * @param ctx the parse tree
	 */
	void enterBif_this(RpgParser.Bif_thisContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_this}.
	 * @param ctx the parse tree
	 */
	void exitBif_this(RpgParser.Bif_thisContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_time}.
	 * @param ctx the parse tree
	 */
	void enterBif_time(RpgParser.Bif_timeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_time}.
	 * @param ctx the parse tree
	 */
	void exitBif_time(RpgParser.Bif_timeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_timestamp}.
	 * @param ctx the parse tree
	 */
	void enterBif_timestamp(RpgParser.Bif_timestampContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_timestamp}.
	 * @param ctx the parse tree
	 */
	void exitBif_timestamp(RpgParser.Bif_timestampContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_tlookup}.
	 * @param ctx the parse tree
	 */
	void enterBif_tlookup(RpgParser.Bif_tlookupContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_tlookup}.
	 * @param ctx the parse tree
	 */
	void exitBif_tlookup(RpgParser.Bif_tlookupContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_tlookuplt}.
	 * @param ctx the parse tree
	 */
	void enterBif_tlookuplt(RpgParser.Bif_tlookupltContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_tlookuplt}.
	 * @param ctx the parse tree
	 */
	void exitBif_tlookuplt(RpgParser.Bif_tlookupltContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_tlookuple}.
	 * @param ctx the parse tree
	 */
	void enterBif_tlookuple(RpgParser.Bif_tlookupleContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_tlookuple}.
	 * @param ctx the parse tree
	 */
	void exitBif_tlookuple(RpgParser.Bif_tlookupleContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_tlookupgt}.
	 * @param ctx the parse tree
	 */
	void enterBif_tlookupgt(RpgParser.Bif_tlookupgtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_tlookupgt}.
	 * @param ctx the parse tree
	 */
	void exitBif_tlookupgt(RpgParser.Bif_tlookupgtContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_tlookupge}.
	 * @param ctx the parse tree
	 */
	void enterBif_tlookupge(RpgParser.Bif_tlookupgeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_tlookupge}.
	 * @param ctx the parse tree
	 */
	void exitBif_tlookupge(RpgParser.Bif_tlookupgeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_trim}.
	 * @param ctx the parse tree
	 */
	void enterBif_trim(RpgParser.Bif_trimContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_trim}.
	 * @param ctx the parse tree
	 */
	void exitBif_trim(RpgParser.Bif_trimContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_triml}.
	 * @param ctx the parse tree
	 */
	void enterBif_triml(RpgParser.Bif_trimlContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_triml}.
	 * @param ctx the parse tree
	 */
	void exitBif_triml(RpgParser.Bif_trimlContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_trimr}.
	 * @param ctx the parse tree
	 */
	void enterBif_trimr(RpgParser.Bif_trimrContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_trimr}.
	 * @param ctx the parse tree
	 */
	void exitBif_trimr(RpgParser.Bif_trimrContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_ucs2}.
	 * @param ctx the parse tree
	 */
	void enterBif_ucs2(RpgParser.Bif_ucs2Context ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_ucs2}.
	 * @param ctx the parse tree
	 */
	void exitBif_ucs2(RpgParser.Bif_ucs2Context ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_uns}.
	 * @param ctx the parse tree
	 */
	void enterBif_uns(RpgParser.Bif_unsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_uns}.
	 * @param ctx the parse tree
	 */
	void exitBif_uns(RpgParser.Bif_unsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_unsh}.
	 * @param ctx the parse tree
	 */
	void enterBif_unsh(RpgParser.Bif_unshContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_unsh}.
	 * @param ctx the parse tree
	 */
	void exitBif_unsh(RpgParser.Bif_unshContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_xfoot}.
	 * @param ctx the parse tree
	 */
	void enterBif_xfoot(RpgParser.Bif_xfootContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_xfoot}.
	 * @param ctx the parse tree
	 */
	void exitBif_xfoot(RpgParser.Bif_xfootContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_xlate}.
	 * @param ctx the parse tree
	 */
	void enterBif_xlate(RpgParser.Bif_xlateContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_xlate}.
	 * @param ctx the parse tree
	 */
	void exitBif_xlate(RpgParser.Bif_xlateContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_xml}.
	 * @param ctx the parse tree
	 */
	void enterBif_xml(RpgParser.Bif_xmlContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_xml}.
	 * @param ctx the parse tree
	 */
	void exitBif_xml(RpgParser.Bif_xmlContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_years}.
	 * @param ctx the parse tree
	 */
	void enterBif_years(RpgParser.Bif_yearsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_years}.
	 * @param ctx the parse tree
	 */
	void exitBif_years(RpgParser.Bif_yearsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#bif_code}.
	 * @param ctx the parse tree
	 */
	void enterBif_code(RpgParser.Bif_codeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#bif_code}.
	 * @param ctx the parse tree
	 */
	void exitBif_code(RpgParser.Bif_codeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#free}.
	 * @param ctx the parse tree
	 */
	void enterFree(RpgParser.FreeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#free}.
	 * @param ctx the parse tree
	 */
	void exitFree(RpgParser.FreeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#c_free}.
	 * @param ctx the parse tree
	 */
	void enterC_free(RpgParser.C_freeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#c_free}.
	 * @param ctx the parse tree
	 */
	void exitC_free(RpgParser.C_freeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#control}.
	 * @param ctx the parse tree
	 */
	void enterControl(RpgParser.ControlContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#control}.
	 * @param ctx the parse tree
	 */
	void exitControl(RpgParser.ControlContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#exec_sql}.
	 * @param ctx the parse tree
	 */
	void enterExec_sql(RpgParser.Exec_sqlContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#exec_sql}.
	 * @param ctx the parse tree
	 */
	void exitExec_sql(RpgParser.Exec_sqlContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#baseExpression}.
	 * @param ctx the parse tree
	 */
	void enterBaseExpression(RpgParser.BaseExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#baseExpression}.
	 * @param ctx the parse tree
	 */
	void exitBaseExpression(RpgParser.BaseExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#indicator}.
	 * @param ctx the parse tree
	 */
	void enterIndicator(RpgParser.IndicatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#indicator}.
	 * @param ctx the parse tree
	 */
	void exitIndicator(RpgParser.IndicatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#assignmentExpression}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentExpression(RpgParser.AssignmentExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#assignmentExpression}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentExpression(RpgParser.AssignmentExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#assignOperatorExpression}.
	 * @param ctx the parse tree
	 */
	void enterAssignOperatorExpression(RpgParser.AssignOperatorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#assignOperatorExpression}.
	 * @param ctx the parse tree
	 */
	void exitAssignOperatorExpression(RpgParser.AssignOperatorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#evalExpression}.
	 * @param ctx the parse tree
	 */
	void enterEvalExpression(RpgParser.EvalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#evalExpression}.
	 * @param ctx the parse tree
	 */
	void exitEvalExpression(RpgParser.EvalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#simpleExpression}.
	 * @param ctx the parse tree
	 */
	void enterSimpleExpression(RpgParser.SimpleExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#simpleExpression}.
	 * @param ctx the parse tree
	 */
	void exitSimpleExpression(RpgParser.SimpleExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpression(RpgParser.UnaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpression(RpgParser.UnaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(RpgParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(RpgParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#indicator_expr}.
	 * @param ctx the parse tree
	 */
	void enterIndicator_expr(RpgParser.Indicator_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#indicator_expr}.
	 * @param ctx the parse tree
	 */
	void exitIndicator_expr(RpgParser.Indicator_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#function}.
	 * @param ctx the parse tree
	 */
	void enterFunction(RpgParser.FunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#function}.
	 * @param ctx the parse tree
	 */
	void exitFunction(RpgParser.FunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void enterComparisonOperator(RpgParser.ComparisonOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void exitComparisonOperator(RpgParser.ComparisonOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#assignmentOperator}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentOperator(RpgParser.AssignmentOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#assignmentOperator}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentOperator(RpgParser.AssignmentOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#assignmentOperatorIncludingEqual}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentOperatorIncludingEqual(RpgParser.AssignmentOperatorIncludingEqualContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#assignmentOperatorIncludingEqual}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentOperatorIncludingEqual(RpgParser.AssignmentOperatorIncludingEqualContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#args}.
	 * @param ctx the parse tree
	 */
	void enterArgs(RpgParser.ArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#args}.
	 * @param ctx the parse tree
	 */
	void exitArgs(RpgParser.ArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(RpgParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(RpgParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(RpgParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(RpgParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#all}.
	 * @param ctx the parse tree
	 */
	void enterAll(RpgParser.AllContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#all}.
	 * @param ctx the parse tree
	 */
	void exitAll(RpgParser.AllContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#functionName}.
	 * @param ctx the parse tree
	 */
	void enterFunctionName(RpgParser.FunctionNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#functionName}.
	 * @param ctx the parse tree
	 */
	void exitFunctionName(RpgParser.FunctionNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#multipart_identifier}.
	 * @param ctx the parse tree
	 */
	void enterMultipart_identifier(RpgParser.Multipart_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#multipart_identifier}.
	 * @param ctx the parse tree
	 */
	void exitMultipart_identifier(RpgParser.Multipart_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#indexed_identifier}.
	 * @param ctx the parse tree
	 */
	void enterIndexed_identifier(RpgParser.Indexed_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#indexed_identifier}.
	 * @param ctx the parse tree
	 */
	void exitIndexed_identifier(RpgParser.Indexed_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#opCode}.
	 * @param ctx the parse tree
	 */
	void enterOpCode(RpgParser.OpCodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#opCode}.
	 * @param ctx the parse tree
	 */
	void exitOpCode(RpgParser.OpCodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(RpgParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(RpgParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#free_identifier}.
	 * @param ctx the parse tree
	 */
	void enterFree_identifier(RpgParser.Free_identifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#free_identifier}.
	 * @param ctx the parse tree
	 */
	void exitFree_identifier(RpgParser.Free_identifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#continuedIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterContinuedIdentifier(RpgParser.ContinuedIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#continuedIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitContinuedIdentifier(RpgParser.ContinuedIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#idOrKeyword}.
	 * @param ctx the parse tree
	 */
	void enterIdOrKeyword(RpgParser.IdOrKeywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#idOrKeyword}.
	 * @param ctx the parse tree
	 */
	void exitIdOrKeyword(RpgParser.IdOrKeywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterArgument(RpgParser.ArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitArgument(RpgParser.ArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link RpgParser#symbolicConstants}.
	 * @param ctx the parse tree
	 */
	void enterSymbolicConstants(RpgParser.SymbolicConstantsContext ctx);
	/**
	 * Exit a parse tree produced by {@link RpgParser#symbolicConstants}.
	 * @param ctx the parse tree
	 */
	void exitSymbolicConstants(RpgParser.SymbolicConstantsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleTarget}
	 * labeled alternative in {@link RpgParser#target}.
	 * @param ctx the parse tree
	 */
	void enterSimpleTarget(RpgParser.SimpleTargetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleTarget}
	 * labeled alternative in {@link RpgParser#target}.
	 * @param ctx the parse tree
	 */
	void exitSimpleTarget(RpgParser.SimpleTargetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code indexedTarget}
	 * labeled alternative in {@link RpgParser#target}.
	 * @param ctx the parse tree
	 */
	void enterIndexedTarget(RpgParser.IndexedTargetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code indexedTarget}
	 * labeled alternative in {@link RpgParser#target}.
	 * @param ctx the parse tree
	 */
	void exitIndexedTarget(RpgParser.IndexedTargetContext ctx);
}