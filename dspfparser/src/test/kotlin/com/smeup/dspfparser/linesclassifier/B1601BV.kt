package com.smeup.dspfparser.linesclassifier

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class B1601BV : DSPFSpecificationsLoader("./src/test/resources/B£1601BV.dspf") {
    @Test
    fun records() {
        val first = this.specifications.records[0]
        assertEquals("FMT1", first.name)
    }

    @Test
    fun fields() {
        val fields = this.specifications.records[0].fields
        val names = fields.map { it.name }
        assertEquals(15, fields.size)
        assertTrue { names.contains("£RASDI") }
        assertTrue { names.contains("£PDSNP") }
        assertTrue { names.contains("£PDSJN") }
        assertTrue { names.contains("£PDSNU") }
        assertTrue { names.contains("W\$RISC") }
        assertTrue { names.contains("W\$OPZ") }
        assertTrue { names.contains("W\$NU20") }
        assertTrue { names.contains("W\$MESV") }
        assertTrue { names.contains("W\$NU22") }
        assertTrue { names.contains("I\$IN22") }
        assertTrue { names.contains("I\$IN23") }
        assertTrue { names.contains("I\$IN20") }
        assertTrue { names.contains("I\$IN25") }
        assertTrue { names.contains("W\$ALFA") }
    }

    @Test
    fun constants() {
        val constants = this.specifications.records[0].constants
        val values = constants.map { it.value }
        assertEquals(78, constants.size)
        // just some of them...
        assertTrue { values.contains(ConstantValue("* Test gestione campi sovrapposti in base ad indicatori *")) }
        assertTrue { values.contains(ConstantValue("F05=Elimin.dat.tec.")) }
        assertTrue { values.contains(ConstantValue("F16=Sorgenti")) }
        assertTrue { values.contains(ConstantValue("visualizzato")) }
        assertTrue { values.contains(ConstantValue("F07=Proteggi")) }
    }
}
