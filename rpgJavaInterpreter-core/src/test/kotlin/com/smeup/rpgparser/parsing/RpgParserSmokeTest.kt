/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smeup.rpgparser.parsing

import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.assertCanBeParsed
import org.junit.Test

class RpgParserSmokeTest : AbstractTest() {
    @Test
    fun parseINTTEST() {
        assertCanBeParsed("INTTEST")
    }

    @Test
    fun parseJD_001() {
        assertCanBeParsed("JD_001")
    }

    @Test
    fun parseJD_001_alt() {
        assertCanBeParsed("JD_001_alt")
    }

    @Test
    fun parseJD_001_justdirectives() {
        assertCanBeParsed("JD_001_justdirectives")
    }

    @Test
    fun parseJD_001_onedatadecl() {
        assertCanBeParsed("JD_001_onedatadecl")
    }

    @Test
    fun parseJD_001_onedatadecl_simple() {
        assertCanBeParsed("JD_001_onedatadecl_simple")
    }

    @Test
    fun parseJD_002() {
        assertCanBeParsed("JD_002")
    }

    @Test
    fun parseJD_004() {
        assertCanBeParsed("JD_004")
    }

    @Test
    fun parseJD_005() {
        assertCanBeParsed("JD_005")
    }

    @Test
    fun parseJD_006() {
        assertCanBeParsed("JD_006")
    }

    @Test
    fun parseJD_007() {
        assertCanBeParsed("JD_007")
    }

    @Test
    fun parseJD_003() {
        assertCanBeParsed("JD_003")
    }

    @Test
    fun parseJD_008() {
        assertCanBeParsed("JD_008")
    }

    @Test
    fun parsePROOF() {
        assertCanBeParsed("PROOF")
    }

    @Test
    fun parseJCODFISD() {
        assertCanBeParsed("JCODFISD")
    }

    @Test
    fun parseJCODFISS() {
        assertCanBeParsed("JCODFISS")
    }

    @Test
    fun parseJFTCPR() {
        assertCanBeParsed("JFTCPR")
    }

    @Test
    fun parseJDATWD() {
        assertCanBeParsed("JDATWD")
    }

    @Test
    fun parseJDATWDK() {
        assertCanBeParsed("JDATWDK")
    }

    @Test
    fun parseCALCFIB() {
        assertCanBeParsed("CALCFIB")
    }

    @Test
    fun parseHELLO() {
        assertCanBeParsed("HELLO", printTree = true)
    }

    @Test
    fun parseHELLO1() {
        assertCanBeParsed("HELLO1")
    }

    @Test
    fun parseJRANDOMCAL() {
        assertCanBeParsed("JRANDOMCAL")
    }

    @Test
    fun parseJRANDOMA() {
        assertCanBeParsed("JRANDOMA")
    }

    @Test
    fun parseCHAINHOSTS() {
        assertCanBeParsed("db/CHAINHOSTS")
    }

    @Test
    fun parseREADP() {
        assertCanBeParsed("db/READP")
    }

    @Test
    fun parseTIMESTDIFF() {
        assertCanBeParsed("TIMESTDIFF")
    }

    @Test
    fun parseDSEX01() {
        assertCanBeParsed("DSEX01")
    }

    @Test
    fun parseDSEX02() {
        assertCanBeParsed("DSEX02")
    }

    @Test
    fun parseMUTE05_02() {
        assertCanBeParsed("MUTE05_02")
    }

    @Test
    fun parseERRORVARST() {
        assertCanBeParsed("ERRORVARST")
    }

    @Test
    fun parseHELLOVARST() {
        assertCanBeParsed("HELLOVARST")
    }

    @Test
    fun parseASSIGNSUBS() {
        assertCanBeParsed("ASSIGNSUBS")
    }

    @Test
    fun parseABSTEST() {
        assertCanBeParsed("ABSTEST")
    }

    @Test
    fun parseCHAIN2KEYS() {
        assertCanBeParsed("db/CHAIN2KEYS")
    }

    @Test
    fun parseCHAINREADE() {
        assertCanBeParsed("db/CHAINREADE")
    }

    @Test
    fun parseCHAINREDE0() {
        assertCanBeParsed("db/CHAINREDE0")
    }

    @Test
    fun parseACTGRP_FIX() {
        assertCanBeParsed("ACTGRP_FIX")
    }

    @Test
    fun parseMUTE13_13_indicatorsWithParenthesis() {
        assertCanBeParsed("mute/MUTE13_13")
        assertASTCanBeProduced("mute/MUTE13_13", true)
    }

    @Test
    fun parseProgramWithControlLevelIndicatorFollowedByPARM() {
        // the issue was at line 2 and 3
        val pgm =
            "1    CLR                 IF        ££B£20 = '1'\n" +
                "2    CLR                 CALL      £EXCMD\n" +
                "3    CLR                 PARM      'RCLRSC'      £RCLRS            6\n" +
                "4    CLR                 PARM      6             NNN155_OLD       15 5\n" +
                "5    CLR                 ENDIF"
        val inputStream = pgm.byteInputStream()
        assertCanBeParsed(inputStream = inputStream, false)
    }
}
