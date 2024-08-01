package com.smeup.rpgparser.smeup

import com.smeup.rpgparser.interpreter.TimeStampValue
import com.smeup.rpgparser.parsing.parsetreetoast.AstResolutionError
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

open class MULANGT04EssentialsCodopAndBifTest : MULANGTTest() {
    /**
     * No error when there isn't a called subroutine
     * @see #270
     */
    @Test
    fun executeMU041004() {
        assertFailsWith(AstResolutionError::class) {
            "smeup/MU041004".outputOf(configuration = smeupConfig)
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

    /**
     *Assigns content of DS to a String not VARYING in EVAL
     * @see #LS24003679
     */
    @Test
    fun executeMU044013() {
        val expected = listOf("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Ae")
        assertEquals(expected, "smeup/MU044013".outputOf())
    }

    /**
     *Assigns content of DS to a String  VARYING in EVAL
     * @see #LS24003679
     */
    @Test
    fun executeMU044014() {
        val expected = listOf("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Ae")
        assertEquals(expected, "smeup/MU044014".outputOf())
    }

    /**
     * %DIFF with several DurationCodes
     * @see #LS24003282
     */
    @Test
    fun executeMUDRNRAPU00228() {
        val referenceDate = TimeStampValue.of("2024-07-10-10.25.27.921456")
        val epochDate = TimeStampValue.of("1970-01-01-00.00.00.000000")
        val referenceInstant = referenceDate.asTimeStamp().value.atZone(ZoneId.systemDefault()).toInstant()
        val epochInstant = epochDate.asTimeStamp().value.atZone(ZoneId.systemDefault()).toInstant()

        val mseconds = ChronoUnit.MICROS.between(epochInstant, referenceInstant).toString()
        val seconds = ChronoUnit.SECONDS.between(epochInstant, referenceInstant).toString()
        val minutes = ChronoUnit.MINUTES.between(epochInstant, referenceInstant).toString()
        val hours = ChronoUnit.HOURS.between(epochInstant, referenceInstant).toString()
        val days = ChronoUnit.DAYS.between(epochInstant, referenceInstant).toString()
        val months = ChronoUnit.MONTHS.between(epochDate.localDate, referenceDate.localDate).toString()
        val years = ChronoUnit.YEARS.between(epochDate.localDate, referenceDate.localDate).toString()
        val expected = listOf(
            mseconds, mseconds,
            seconds, seconds,
            minutes, minutes,
            hours, hours,
            days, days,
            months, months,
            years, years
        )
        assertEquals(expected, "smeup/MUDRNRAPU00228".outputOf())
    }

    /**
     * %DEC with dates and timestamps
     * @see #LS24003289
     */
    @Test
    fun executeMUDRNRAPU00229() {
        val expected = listOf("19700101000000000000", "19700101")
        assertEquals(expected, "smeup/MUDRNRAPU00229".outputOf())
    }
}