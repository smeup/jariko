package com.smeup.rpgparser.smeup

import com.smeup.rpgparser.parsing.parsetreetoast.AstResolutionError
import org.junit.Test
import java.time.LocalDateTime
import kotlin.test.Ignore
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFailsWith

open class MULANGT04EssentialsCodopAndBifTest : MULANGTTest() {
    /**
     * No error when there isn't a called subroutine
     * WARNING: Ignored because there are conflicts between the dependencies.
     * @see #270
     */
    @Test
    @Ignore
    fun executeMU041004() {
        assertFailsWith(AstResolutionError::class) {
            outputOf("smeup/MU041004")
        }
    }

    /**
     * TIME with inline number declarations
     */
    @Test
    fun executeT04_A80_P05() {
        val isEarly = LocalDateTime.now().hour < 10
        val suffixLength = if (isEarly) 1 else 2
        val expected = listOf(
            "A80_D1(hhmm${"s".repeat(suffixLength)}) A80_D2(hhmmssDDMM${"Y".repeat(suffixLength)}) A80_D3(hhmmssDDMMYY${"Y".repeat(suffixLength)})"
        )
        assertEquals(expected, "smeup/T04_A80_P05".outputOf())
    }
}