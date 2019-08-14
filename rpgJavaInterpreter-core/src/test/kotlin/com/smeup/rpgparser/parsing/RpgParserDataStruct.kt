package com.smeup.rpgparser.parsing

import com.smeup.rpgparser.assertASTCanBeProduced
import com.smeup.rpgparser.assertCanBeParsed
import com.smeup.rpgparser.execute
import com.smeup.rpgparser.parsing.parsetreetoast.resolve
import org.junit.Test


class RpgParserDataStruct {



    @Test
    fun parseSTRUCT_01() {
        val result = assertCanBeParsed("struct/STRUCT_01", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/STRUCT_01", true)
        cu.resolve()
        execute(cu, mapOf())
    }

    /**
     * Test for QUALIFIED support
     */
    @Test
    fun parseSTRUCT_02() {
        val result = assertCanBeParsed("struct/STRUCT_02", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/STRUCT_02", true)
        cu.resolve()
        execute(cu, mapOf())
    }


    @Test
    fun parseSTRUCT_03() {
        val result = assertCanBeParsed("struct/STRUCT_03", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/STRUCT_03", true)
        cu.resolve()
        execute(cu, mapOf())
    }

    @Test
    fun parseSTRUCT_04() {
        val result = assertCanBeParsed("struct/STRUCT_04", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/STRUCT_04", true)
        cu.resolve()
        execute(cu, mapOf())
    }

    /**
     * Test for TEMPLATE and LIKEDS support
     */
    @Test
    fun parseSTRUCT_05() {
        val result = assertCanBeParsed("struct/STRUCT_05", withMuteSupport = true)

        val cu = assertASTCanBeProduced("struct/STRUCT_05", true)
        cu.resolve()
        execute(cu, mapOf())

    }



}