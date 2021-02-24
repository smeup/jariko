package com.smeup.rpgparser.parsing.ast

import com.smeup.rpgparser.AbstractTest
import kotlin.test.Test

open class CopyTest : AbstractTest() {

    /**
     * We need to be able to create AST for sources containing just D specifications
     * */
    @Test
    fun `buildAstFor£MU1DSPEC`() {
        assertASTCanBeProduced("£MU1DSPEC")
    }

    /**
     * We need to be able to create AST for sources containing just C specifications
     * */
    @Test
    fun `buildAstFor£MU1CSPEC`() {
        assertASTCanBeProduced("£MU1CSPEC")
    }

    /**
     * We need to be able to create AST for sources containing /COPY directive and to verify
     * that the resulting AST contains all AST fragments provided by included copies
     * */
    @Test
    fun `buildAstFor£MU1API`() {
        val cu = assertASTCanBeProduced("£MU1API")
        // 2 ds inherited by copy inclusion
        assert(cu.dataDefinitions.size == 2)
    }

    /**
     * We need to be able to create AST for sources containing /COPY directive and to verify
     * that the resulting AST contains all AST fragments provided by included copies
     * */
    @Test
    fun `buildAstFor£MU1PGM`() {
        val cu = assertASTCanBeProduced("£MU1PGM")
        assert(cu.subroutines.size == 4)
        assert(cu.dataDefinitions.size == 2)
    }
}