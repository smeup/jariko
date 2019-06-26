package com.smeup.rpgparser.parsing

import com.smeup.rpgparser.assertASTCanBeProduced
import org.junit.Test

class RpgParserWithMuteSupportTest {

    @Test
    fun muteAnnotationsAttribution() {
        val cu = assertASTCanBeProduced("MUTE05_02",
                considerPosition = true,
                withMuteSupport = true)

        TODO("Verify that the mute assertions have been correctly assigned")
    }

}
