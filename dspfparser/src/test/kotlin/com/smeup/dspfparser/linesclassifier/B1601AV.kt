package com.smeup.dspfparser.linesclassifier

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class B1601AV : DSPFSpecificationsLoader("./src/test/resources/B£1601AV.dspf") {
    @Test
    fun records() {
        val first = this.specifications.records[0]
        assertEquals("FMT1", first.name)
    }

    @Test
    fun fields() {
        val fields = this.specifications.records[0].fields
        val names = fields.map { it.name }
        assertEquals(8, fields.size)
        assertTrue { names.contains("£RASDI") }
        assertTrue { names.contains("£PDSNP") }
        assertTrue { names.contains("£PDSJN") }
        assertTrue { names.contains("£PDSNU") }
        assertTrue { names.contains("W\$ARTI") }
        assertTrue { names.contains("W\$DEAR") }
        assertTrue { names.contains("W\$MESV") }
        assertTrue { names.contains("W\$RISC") }
    }

    @Test
    fun constants() {
        val constants = this.specifications.records[0].constants
        val values = constants.map { it.value }
        assertEquals(5, constants.size)
        assertTrue { values.contains(ConstantValue("**   GESTIONE ARTICOLI   **")) }
        assertTrue { values.contains(ConstantValue("Formato guida")) }
        assertTrue { values.contains(ConstantValue("Codice articolo")) }
        assertTrue { values.contains(ConstantValue("F03=Fine Lavoro")) }
        assertTrue { values.contains(ConstantValue("Selezionare un articolo")) }
    }
}
