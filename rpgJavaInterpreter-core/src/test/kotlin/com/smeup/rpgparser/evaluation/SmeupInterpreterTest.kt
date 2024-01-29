package com.smeup.rpgparser.evaluation

import com.smeup.dbnative.DBNativeAccessConfig
import com.smeup.rpgparser.AbstractTest
import com.smeup.rpgparser.execution.Configuration
import com.smeup.rpgparser.execution.ReloadConfig
import com.smeup.rpgparser.execution.SimpleReloadConfig
import org.junit.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

open class SmeupInterpreterTest : AbstractTest() {

    lateinit var smeupConfig: Configuration

    @BeforeTest
    fun setUp() {
        smeupConfig = Configuration()
        val path = javaClass.getResource("/smeup/metadata")!!.path
        val reloadConfig = SimpleReloadConfig(metadataPath = path, connectionConfigs = listOf())
        smeupConfig.reloadConfig = ReloadConfig(
            nativeAccessConfig = DBNativeAccessConfig(emptyList()),
            metadataProducer = { dbFile: String -> reloadConfig.getMetadata(dbFile = dbFile) })
    }

    @Test
    fun executeT15_A80() {
        // TODO When we will have more clear idea about the expected result, we will add the assert
        println("executeT15_A80: " + "smeup/T15_A80".outputOf())
    }

    @Test
    fun executeT15_A90() {
        // TODO When we will have more clear idea about the expected result, we will add the assert
        println("executeT15_A90: " + "smeup/T15_A90".outputOf())
    }

    @Test
    fun executeT02_A20() {
        val values = "smeup/T02_A20".outputOf()
        assertTrue(values[0].matches(Regex("A20_Z1\\(\\d{4}-\\d{2}-\\d{2}-\\d{2}\\.\\d{2}\\.\\d{2}\\.\\d{6}\\)")))
        assertEquals("A20_Z2(2003-06-27-09.25.59.123456)", values[1])
    }

    @Test
    fun executeT02_A30() {
        val len = 100
        val expected = listOf(
            buildString {
                append("AAAAA".padEnd(len, ' '))
                append("BBBBB".padEnd(len, ' '))
                append("CCCCC".padEnd(len, ' '))
                append("DDDDD".padEnd(len, ' '))
                append("EEEEE".padEnd(len, ' '))
                append("FFFFF".padEnd(len, ' '))
                append("GGGGG".padEnd(len, ' '))
                append("HHHHH".padEnd(len, ' '))
                // Here I don't padEnd because the display messages are trimmed
                append("IIIII")
            }
        )
        assertEquals(expected, "smeup/T02_A30".outputOf())
    }

    @Test
    fun executeT02_A40() {
        val expected = listOf("DS4_FL1(NNNNNFFFFFMMMMMGGGGGAAAAAZZZZZ)", "DS4_FL2(AAAAAZZZZZMMMMMGGGGGNNNNNFFFFF)")
        assertEquals(expected, "smeup/T02_A40".outputOf())
    }

    @Test
    fun executeT04_A20() {
        val expected = listOf("CALL_1(MULANGT04 , 1, MULANGTB10: MULANGT04 chiamata 1                  ) CALL_2(MULANGT04 , 3, MULANGTB10: MULANGT04 chiamata 1                  )")
        assertEquals(expected, "smeup/T04_A20".outputOf())
    }

    @Test
    fun executeT04_A40() {
        val expected = listOf("A40_P1(122469.88)A40_P2(987.22)A40_P3(123456.10)A40_P4(121028170.03)")
        assertEquals(expected, "smeup/T04_A40".outputOf())
    }

    @Test
    fun executeT04_A80() {
        val actual = "smeup/T04_A80".outputOf()
        val t = LocalDateTime.now()
        val expected = listOf(
            DateTimeFormatter.ofPattern("Hmmss").format(t),
            DateTimeFormatter.ofPattern("HmmssddMMyy").format(t),
            DateTimeFormatter.ofPattern("HmmssddMMyyyy").format(t)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun executeT04_A90() {
        val expected = listOf("123.4560000000", "1234.5600000000", "123456.0000000000", "123")
        assertEquals(expected, "smeup/T04_A90".outputOf())
    }

    @Test
    fun executeT10_A20() {
        val expected = listOf("172.670-22146.863-.987000000")
        assertEquals(expected, "smeup/T10_A20".outputOf())
    }

    @Test
    fun executeT10_A70() {
        val expected = listOf("CAT_1(MR. SMITH) CAT_2(RPG/400) CAT_3(RPG/4)", "CAT_1(ABC  XYZ ) CAT_2(Mr. Smith)", "CAT_1(RPG/400   )", "CAT_1(RPGIV     )")
        assertEquals(expected, "smeup/T10_A70".outputOf())
    }

    @Test
    fun executeT16_A20() {
        val expected = listOf("(Ontario, Canada)", "(Ontario, California     )", "(Ontario, Ontario, California     )", "(Somewhere else: Ontario, Ontario, California     )")
        assertEquals(expected, "smeup/T16_A20".outputOf())
    }

    @Test
    fun executeT16_A70() {
        val expected = listOf("A70_AR1(10) A70_AR2(20) A70_DS1(30) A70_AR3(10) A70_AR4(40)")
        assertEquals(expected, "smeup/T16_A70".outputOf())
    }

    @Test
    fun executeT16_A80() {
        val expected = listOf("A80_AR3(1)(10)A80_AR3(2)(32)A80_AR3(3)(26)")
        assertEquals(expected, "smeup/T16_A80".outputOf())
    }

    @Test
    fun executeT10_A90() {
        val expected = listOf("999-9999", "A90_A4(        ) A90_A5(RPG DEPT)", "A90_A4(        ) A90_A5(RPG DEPT)")
        assertEquals(expected, "smeup/T10_A90".outputOf())
    }

    @Test
    fun executeT12_A02() {
        val expected = listOf("True")
        assertEquals(expected, "smeup/T12_A02".outputOf())
    }

    @Test
    fun executeT40_A10() {
        val expected = listOf("A10_DS_P01(BBB                 -.000000000-.000000000-0-0-0-0) A10_DS_P04(AAA                 -.000000000-.000000000-0-0-0-0)")
        assertEquals(expected, "smeup/T40_A10".outputOf(configuration = smeupConfig))
    }

    @Test
    fun executeT02_A50() {
        val expected = listOf("A50_A2(       ) A50_N1(.00) A50_N2(.00)")
        assertEquals(expected, "smeup/T02_A50".outputOf(configuration = smeupConfig))
    }

    /*
    AS400 response:

    "I_SUM(654360) I_DIF(654280) I_MUL(26172800) I_DIV(16358,00000000000000000000) P_SUM(588,04) P_DIF(413,76) P_MUL(43648,4260) P_DIV(5,74822125315584117512049) S_SUM(472,600) S_DIF(408,200) S_MUL(14180,880000) S_DIV(13,677018633540372670807) U_SUM(400) U_DIF(240) U_MUL(25600) U_DIV(4,0000000000000000000000000)"

    Jariko returns different results, but this specific use of %CHAR is declared as unsupported,
    so there is no need to handle the different type of response between AS400 and Jariko.
    */
    @Test
    fun executeT04_A70() {
        val expected = listOf(
            "I_SUM(654360) I_DIF(654280) I_MUL(26172800) I_DIV(16358.0000000000) P_SUM(588.04) P_DIF(413.76) P_MUL(43648.4260) P_DIV(5.748221253155841175120495753959146) S_SUM(472.600) S_DIF(408.200) S_MUL(14180.880000) S_DIV(13.67701863354037267080745341614907) U_SUM(400) U_DIF(240) U_MUL(25600) U_DIV(4.0000000000)"
        )
        assertEquals(expected, "smeup/T04_A70".outputOf())
    }
    @Test
    fun executeT40_A10_P07() {
        val expected = listOf(
            "1B"
        )
        assertEquals(expected, "smeup/T40_A10_P07".outputOf())
    }

    @Test
    fun executeT20_A10_P05() {
        val expected = listOf(
            "1",
            "0"
        )
        assertEquals(expected, "smeup/T20_A10_P05".outputOf())
    }

    @Test
    fun executeT02_S10_P01() {
        val expected = listOf(
            "abcdefghijklmnopqrstuvwxyzèéàòùABCDEFGHIJKLMNOPQRS        TUVWXYZEEAOUABCDEFGHIJKLMNOPQRSTUVWXYZEEAOUABCDEFGHIJKLMNOPQRSTUVWXYZEEAOUABCDEFGHIJKLMNOPQRSTUVWXYZEEAOUABCDEFGH'''*%"
        )
        assertEquals(expected, "smeup/T02_S10_P01".outputOf())
    }

    @Test
    fun executeT40_A10_P03D() {
        val expected = listOf(
            "Contenuto Post-RESET: A    -44"
        )
        assertEquals(expected, "smeup/T40_A10_P03D".outputOf())
    }

    @Test
    fun executeT15_A50() {
        val expected = listOf(
            // P1
            "A(        123.456000 /         123.456000 /       123.456000"
                    +" /       123.456000 /         123.456000   /         123.456"
                    +"000   /       123.456000   /       123.456000   /         12"
                    +"3.456000  /         123.456000  /       123.456000  /       "
                    +"123.456000  /          123.456000 /          123.456000 /   "
                    +"     123.456000 /        123.456000 / 000000123456000 /     "
                    +"  123456000) B(           .000123 /            .000123 /    "
                    +"      .000123 /          .000123 /            .000123   /   "
                    +"         .000123   /          .000123   /          .000123  "
                    +" /            .000123  /            .000123  /          .000"
                    +"123  /          .000123  /             .000123 /            "
                    +" .000123 /           .000123 /           .000123 / 000000000"
                    +"000123 /             123) C(     12,345.000000 /      12,345"
                    +".000000 /     12345.000000 /     12345.000000 /      12,345."
                    +"000000   /      12,345.000000   /     12345.000000   /     1"
                    +"2345.000000   /      12,345.000000  /      12,345.000000  / "
                    +"    12345.000000  /     12345.000000  /       12,345.000000 "
                    +"/       12,345.000000 /      12345.000000 /      12345.00000"
                    +"0 / 000012345000000 /     12345000000)"
            , // P2
            "1(     .00  123,456  123,456 1,234.56 1,234.56) 2(          "
                    +"123,456  123,456 1,234.56 1,234.56) 3(    .00 123456 123456 "
                    +"1234.56 1234.56) 4(        123456 123456 1234.56 1234.56)"
            , // P3
            "A(     .00    123,456    123,456CR 1,234.56   1,234.56CR) B("
                    +"            123,456    123,456CR 1,234.56   1,234.56CR) C(  "
                    +"  .00   123456   123456CR 1234.56   1234.56CR) D(          1"
                    +"23456   123456CR 1234.56   1234.56CR)"
            , // P4
            "K(           123,456   123,456- 1,234.56  1,234.56-) J(     "
                    +".00   123,456   123,456- 1,234.56  1,234.56-) L(    .00  123"
                    +"456  123456- 1234.56  1234.56-) M(         123456  123456- 1"
                    +"234.56  1234.56-)"
            , // P5
            "N(      .00   123,456  -123,456  1,234.56 -1,234.56) O(     "
                    +"       123,456  -123,456  1,234.56 -1,234.56) P(     .00  12"
                    +"3456 -123456  1234.56 -1234.56) Q(          123456 -123456  "
                    +"1234.56 -1234.56)"
            , // P6
            "X(0000000123456012345O0123456012345O) Y( 0/00/00 12/34/56 12"
                    +"/34/56 12/34/56 12/34/56) Z(       123456 123456 123456 1234"
                    +"56)"
            , // P8
            "        123.456000 /         123.456000 /       123.456000 /"
                    +"       123.456000 /         123.456000   /         123.45600"
                    +"0   /       123.456000   /       123.456000   /         123."
                    +"456000  /         123.456000  /       123.456000  /       12"
                    +"3.456000  /          123.456000 /          123.456000 /     "
                    +"   123.456000 /        123.456000 / 000000123456000 /       "
                    +"123456000"
            , // P9
            "        123.456000 /         123.456000 /       123.456000 /"
                    +"       123.456000 /         123.456000   /         123.45600"
                    +"0   /       123.456000   /       123.456000   /         123."
                    +"456000  /         123.456000  /       123.456000  /       12"
                    +"3.456000  /          123.456000 /          123.456000 /     "
                    +"   123.456000 /        123.456000 / 000000123456000 /       "
                    +"123456000"
        )
        assertEquals(expected, "smeup/T15_A50".outputOf())
    }

    @Test
    fun executeT02_A50_P03() {
        val expected = listOf("A50_AR1(10) A50_AR2(40)")
        assertEquals(expected, "smeup/T02_A50_P03".outputOf())
    }

    @Test
    fun executeT10_A60_P04() {
        val expected = listOf<String>("C10_P1: MULANGT102")
        assertEquals(expected, "smeup/T10_A60_P04".outputOf())

        assertFailsWith<Exception> {
            "smeup/T10_A60_P04F".outputOf()
        }
    }
}