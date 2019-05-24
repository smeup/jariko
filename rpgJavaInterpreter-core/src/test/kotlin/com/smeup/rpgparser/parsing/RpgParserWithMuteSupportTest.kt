package com.smeup.rpgparser.parsing

import com.smeup.rpgparser.RpgParser
import com.smeup.rpgparser.assertASTCanBeProduced
import com.smeup.rpgparser.assertCanBeParsed
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RpgParserWithMuteSupportTest {

    @Test
    fun muteAnnotationsAttribution() {
        val root = assertCanBeParsed("MUTE05_02", withMuteSupport = true)

        TODO("Verify that the mute assertions have been correctly assigned")
    }

}
