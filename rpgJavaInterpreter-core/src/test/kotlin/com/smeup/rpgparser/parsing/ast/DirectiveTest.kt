package com.smeup.rpgparser.parsing.ast

import com.smeup.rpgparser.*
import com.smeup.rpgparser.interpreter.*
import com.smeup.rpgparser.parsing.parsetreetoast.ToAstConfiguration
import com.smeup.rpgparser.parsing.parsetreetoast.resolve
import com.strumenta.kolasu.model.collectByType
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.Test as test

class DirectiveTest {

    @test fun singleDataParsing() {
        val cu = parseFragmentToCompilationUnit("H DECEDIT('.')")
        val decedits = cu.collectByType(DeceditDirective::class.java)
        assertEquals(1, decedits.size)
        assertEquals(1, cu.directives.size)
        assertEquals(".", decedits[0].format)
    }

}
