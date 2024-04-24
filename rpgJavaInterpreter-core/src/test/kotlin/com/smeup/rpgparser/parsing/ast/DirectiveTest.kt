package com.smeup.rpgparser.parsing.ast

import com.smeup.rpgparser.*
import com.smeup.rpgparser.interpreter.*
import com.strumenta.kolasu.model.collectByType
import kotlin.test.assertEquals
import kotlin.test.assertIs
import org.junit.Test as test

class DirectiveTest {

    @test fun singleDataParsing() {
        val cu = parseFragmentToCompilationUnit("H DECEDIT('.')")
        val decedits = cu.collectByType(DeceditDirective::class.java)
        val targetDecedit = decedits.first()
        assertIs<FormatDeceditDirective>(targetDecedit.type)
        assertEquals(1, decedits.size)
        assertEquals(1, cu.directives.size)
        assertEquals(".", (targetDecedit.type as FormatDeceditDirective).format.value)
    }
}
