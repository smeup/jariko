package com.smeup.dspfparser.utilities

import com.smeup.dspfparser.utils.removeNewLineAndExtend
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

internal class StringsTest {
    @Test
    fun removeNewLineAndExtend() {
        assertEquals("", "".removeNewLineAndExtend('a', 0), "Adding to empty until 0")
        assertEquals("b", "".removeNewLineAndExtend('b', 1), "Adding to empty until 1")
        assertEquals("cc", "".removeNewLineAndExtend('c', 2), "Adding to empty until 2")
        assertEquals("ddD", "dd".removeNewLineAndExtend('D', 3), "Adding to empty until 3")
        assertEquals("e", "\n".removeNewLineAndExtend('e', 1), "One new line")
        assertEquals("ffFF", "\nf\nf".removeNewLineAndExtend('F', 4), "Two new lines with others chars")
        assertEquals("gggg", "gggg".removeNewLineAndExtend('F', 4), "Unchanged without new line")
        assertEquals("ggg", "ggg\n".removeNewLineAndExtend('F', 3), "Unchanged with new line")
        assertFails("Target length is less than initial") { "hhhh".removeNewLineAndExtend('e', 3) }
        assertFails("Target length is less than initial") { "i\n".removeNewLineAndExtend('e', 0) }
    }
}
