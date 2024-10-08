package com.smeup.rpgparser.smeup

import com.smeup.rpgparser.db.utilities.DBServer
import com.smeup.rpgparser.smeup.dbmock.MULANGTLDbMock
import org.junit.Ignore
import org.junit.Test
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

open class MULANGT02ConstAndDSpecTest : MULANGTTest() {
    @BeforeTest
    override fun setUp() {
        if (!DBServer.isRunning()) {
            DBServer.startDB()
        }

        super.setUp()
    }

    @AfterTest()
    override fun tearDown() {
        /*
         * This causes `connection exception: connection failure: java.net.SocketException: Pipe interrotta (Write failed)`
         *  during `./gradle check`
         */
//        DBServer.stopDB()
    }

    /**
     * /COPY recognized in CTDATA
     * @see #268
     */
    @Test
    fun executeMU023007() {
        val expected = listOf("/COPY in prima posizione                          ;/copy in prima posizione in minuscolo             ;Prova alla fine del testo /COPY                   ;Prova alla fine del testo in minuscolo /copy      ;Prova con /COPY in mezzo al testo                 ;Prova con /copy in mezzo al testo in minuscolo")
        assertEquals(expected, "smeup/MU023007".outputOf())
    }
    /**
     * /COPY recognized in CTDATA
     * @see #269
     */
    @Test
    fun executeMU023008() {
        val expected = listOf("Prova /COPY                                       ;Prova /COPY numero                                ;Prova /COPY 12                                    ;      /COPY QILEGEN, AAA")
        assertEquals(expected, "smeup/MU023008".outputOf())
    }
    /**
     * Data reference - DS with 2 arrays defined with overlay
     * @see #247
     */
    @Test
    fun executeT02_A40_P03() {
        val expected = listOf("CNCLICNCLIAAAABBBBBAAAABBBBBCNFORCNFORCCCCDDDDDCCCCDDDDDCNCOLCNCOLEEEEFFFFFEEEEFFFFF")
        assertEquals(expected, "smeup/T02_A40_P03".outputOf())
    }

    /**
     * Data reference - Inline definition
     * @see #250
     */
    @Test
    fun executeT02_A80_P01() {
        val expected = listOf("ABCDEFGHIJ12345")
        assertEquals(expected, "smeup/T02_A80_P01".outputOf())
    }

    /**
     * Calculation the size of DS5_FL1 from the overlaying fields
     * @see #24
     */
    @Test
    fun executeT02_A40_P05() {
        val expected = listOf("333,zz")
        assertEquals(expected, "smeup/T02_A40_P05".outputOf())
    }

    /**
     * Definition with both Like and Overlay.
     * @see #266
     */
    @Test
    fun executeT02_A40_P11() {
        val expected = listOf("CNCLICNCLICNFORCNFORCNCOLCNCOL")
        assertEquals(expected, "smeup/T02_A40_P11".outputOf())
    }

    /**
     * Definition with Like to a variable defined also with like.
     * @see #160
     */
    @Test
    fun executeT02_A50_P02() {
        val expected = listOf("A50_A3(       ) A50_A4(       )")
        assertEquals(expected, "smeup/T02_A50_P02".outputOf())
    }

    /**
     * LIKE define of field from file
     * @see #255
     */
    @Test
    fun executeT02_A50_P10() {
        val expected = listOf("A50_A10(AAA) A50_B10(BBB)")
        assertEquals(expected, "smeup/T02_A50_P10".outputOf(configuration = smeupConfig))
    }

    /**
     * Data reference - Definition both inline and file
     * @see #253
     */
    @Test
    fun executeT02_A80_P04() {
        val expected = listOf("ABCDEFGHIJ")
        assertEquals(expected, "smeup/T02_A80_P04".outputOf(configuration = smeupConfig))
    }

    /**
     * Data reference - Inline definition and with prefix External DS
     * @see #254
     */
    @Test
    fun executeT02_A80_P05() {
        val expected = listOf("ABCDEFGHIJ123.00000")
        assertEquals(expected, "smeup/T02_A80_P05".outputOf(configuration = smeupConfig))
    }

    /**
     * Inline variable with specification in D (boolean)
     * @see #253, in addition to issue
     */
    @Test
    fun executeT02_A80_P06() {
        val expected = listOf("1")
        assertEquals(expected, "smeup/T02_A80_P06".outputOf())
    }

    /**
     * ###################
     * ATOMIC TEST SECTION
     * ###################
     */

    /**
     * Definition with both Like and Overlay.
     * @see #266
     */
    @Test
    fun executeMU021008() {
        val expected = listOf("ABCDEFGHIJKLMNOPQRSTUVWXYZ")
        assertEquals(expected, "smeup/MU021008".outputOf(configuration = smeupConfig))
    }

    /**
     * Definition of DATE(D).
     * @see #LS24002856
     */
    @Test
    fun executeMU022501() {
        val expected = listOf("*JUL: 24/151; *ISO: 2024-05-30.")
        assertEquals(expected, "smeup/MU022501".outputOf(configuration = smeupConfig))
    }

    /**
     * Definition with both Like and Overlay.
     * @see #266
     */
    @Test
    fun executeMU024011() {
        val expected = listOf("CNCLICNCLICNFORCNFORCNCOLCNCOL")
        assertEquals(expected, "smeup/MU024011".outputOf(configuration = smeupConfig))
    }

    /**
     * Definition with Like to a variable defined also with like.
     * @see #160
     */
    @Test
    fun executeMU025002() {
        val expected = listOf("A50_A3(       ) A50_A4(       )")
        assertEquals(expected, "smeup/MU025002".outputOf(configuration = smeupConfig))
    }

    /**
     * Data definition with `Z` RPG type and resolution of inline definition,
     *  from DEFINE that uses *LIKE, from data definition of a subroutine defined in main.
     * @see #269
     */
    @Test
    fun executeMU025014() {
        val expected = listOf("A50_A14(A) A50_B14(ABCDEFGHIJ)")
        assertEquals(expected, "smeup/MU025014".outputOf(configuration = smeupConfig))
    }

    /**
     * Data definition not resolved for a specification that uses `LIKE` to a field from file. In addition,
     *  there is a DS with an `%ELEM()` built-in function to that field.
     * @see #LS24002645
     */
    @Test
    fun executeMUDRNRAPU00101() {
        MULANGTLDbMock().usePopulated {
            val expected = listOf("HELLO THERE")
            assertEquals(
                expected = expected,
                "smeup/MUDRNRAPU00101".outputOf(configuration = smeupConfig)
            )
        }
    }

    /**
     * Resolves problem od Data Reference with LIKE when in the RPG source is used an API directive.
     * @see LS24003656
     */
    @Test
    fun executeMUDRNRAPU00102() {
        val expected = listOf("HELLO THERE")
        assertEquals(expected, "smeup/MUDRNRAPU00102".outputOf(configuration = smeupConfig))
    }

    /**
     * Data definition not resolved for patterns containing the ':' in XLate factor 1
     * @see #LS24002758
     */
    @Test
    fun executeMUDRNRAPU00201() {
        val expected = listOf("ok")
        assertEquals(
            expected = expected,
            "smeup/MUDRNRAPU00201".outputOf(configuration = smeupConfig)
        )
    }

    /**
     * Instatement data definition not resolved because of CHECKR not implemented
     * @see #LS24002758
     */
    @Test
    fun executeMUDRNRAPU00204() {
        val expected = listOf("ok")
        assertEquals(
            expected = expected,
            "smeup/MUDRNRAPU00204".outputOf(configuration = smeupConfig)
        )
    }

    /**
     * Data definition where its field is initialized with the size of parent.
     * @see #LS24002756
     */
    @Test
    fun executeMU024012() {
        val expected = listOf("Size: 2")
        assertEquals(expected, "smeup/MU024012".outputOf(configuration = smeupConfig))
    }

    /**
     * Data definition where its field is initialized with the size of parent.
     * Each field has a specific position from start.
     * @see #LS24002756
     */
    @Test
    fun executeMU024013() {
        val expected = listOf("Size: 8")
        assertEquals(expected, "smeup/MU024013".outputOf(configuration = smeupConfig))
    }

    @Test
    fun executeMUDRNRAPU00202() {
        MULANGTLDbMock().usePopulated {
            val expected = listOf("ok")
            assertEquals(expected, "smeup/MUDRNRAPU00202".outputOf(configuration = smeupConfig))
        }
    }

    /**
     * Comments after API directive
     * @see #LS24002821
     */
    @Test
    fun executeMUDRNRAPU00205() {
        val expected = listOf("HELLO THERE")
        assertEquals(
            expected = expected,
            "smeup/MUDRNRAPU00205".outputOf(configuration = smeupConfig)
        )
    }

    /**
     * Data reference not resolved "UYEAR"
     * @see #LS24002831
     */
    @Test
    fun executeMUDRNRAPU00206() {
        val expected = listOf("ok")
        assertEquals(
            expected = expected,
            "smeup/MUDRNRAPU00206".outputOf(configuration = smeupConfig)
        )
    }

    /**
     * Additional field on an EXTNAME DS
     * @see #LS24002872
     */
    @Test
    fun executeMUDRNRAPU00207() {
        val expected = listOf("ok")
        assertEquals(
            expected = expected,
            "smeup/MUDRNRAPU00207".outputOf(configuration = smeupConfig)
        )
    }

    /**
     * DS with EXTNAME and then a field with LIKE to another of file.
     * @see #LS24002827
     */
    @Test
    fun executeMU024014() {
        val expected = listOf("A40DS1(ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ) DS1_FL1(1)(BCDEFGHIJK) DS1_FL1(2)(LMNOPQRSTU) | A40DS1(A88        LMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ) DS1_FL1(1)(88        ) DS1_FL1(2)(LMNOPQRSTU) | A40DS1(A88        00        VWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ) DS1_FL1(1)(88        ) DS1_FL1(2)(00        )")
        assertEquals(expected, "smeup/MU024014".outputOf(configuration = smeupConfig))
    }

    /**
     * DefineStmt on instatement data definitions
     * @see #LS24002930
     */
    @Test
    fun executeMUDRNRAPU00213() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00213".outputOf(configuration = smeupConfig))
    }

    /**
     * FileDefinition on metadata with empty recordFormat
     * @see #LS24002985
     */
    @Test
    fun executeMUDRNRAPU00217() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00217".outputOf(configuration = smeupConfig))
    }

    /**
     * Dynamic array based on pointer
     * @see #LS24002988
     */
    @Test
    fun executeMUDRNRAPU00218() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00218".outputOf(configuration = smeupConfig))
    }

    /**
     * Reassign value to pointer variable
     * @see #LS24003047
     */
    @Test
    fun executeMUDRNRAPU00219() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00219".outputOf(configuration = smeupConfig))
    }

    /**
     * Caller activation group with no actual caller
     * @see #LS24003137
     */
    @Test
    fun executeMUDRNRAPU00221() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00221".outputOf(configuration = smeupConfig))
    }

    /**
     * Comptime DEFINE support
     * @see #LS24003177
     */
    @Test
    fun executeMUDRNRAPU00222() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00222".outputOf(configuration = smeupConfig))
    }

    /**
     * Comptime DS with EXTNAME resolution
     * @see #LS24003185
     */
    @Test
    fun executeMUDRNRAPU00223() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00223".outputOf(configuration = smeupConfig))
    }

    /**
     * Access to an array detected as a function call by parser
     * @see #LS24003149
     */
    @Test
    fun executeMUDRNRAPU00224() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00224".outputOf(configuration = smeupConfig))
    }

    /**
     * Comptime DS with EXTNAME and comptime DEFINE support resolution in the same test
     * @see #LS24003177, #LS24003185
     */
    @Test
    fun executeMUDRNRAPU00225() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00225".outputOf(configuration = smeupConfig))
    }

    /**
     * Comptime DS with EXTNAME resolution and data structures INZ(*HIVAL)
     * @see #LS24003257
     */
    @Test
    fun executeMUDRNRAPU00226() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00226".outputOf(configuration = smeupConfig))
    }

    /**
     * Data structures INZ(*HIVAL) values test
     * @see #LS24003257
     */
    @Test
    fun executeMUDRNRAPU00227() {
        val expected = listOf("9991\uFFFF\uFFFF99999")
        assertEquals(expected, "smeup/MUDRNRAPU00227".outputOf(configuration = smeupConfig))
    }

    /**
     * LIKE on a PList with case in-sensitive lookup
     * @see #LS24003296
     */
    @Test
    fun executeMUDRNRAPU00230() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00230".outputOf(configuration = smeupConfig))
    }

    /**
     * Comptime LIKE referencing another D-Spec with LIKE
     * @see #LS24003329
     */
    @Test
    fun executeMUDRNRAPU00233() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00233".outputOf(configuration = smeupConfig))
    }

    /**
     * Comptime DEFINE support based on a comptime resolution inside a subroutine
     * @see #LS24003177
     */
    @Test
    fun executeMUDRNRAPU00231() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00231".outputOf(configuration = smeupConfig))
    }

    /**
     * Variable of type A defined with LEN keyword
     * @see #LS24003324
     */
    @Test
    fun executeMUDRNRAPU00232() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00232".outputOf(configuration = smeupConfig))
    }

    /**
     * Access to an array detected as a function call by parser in ScanExpr
     * @see #LS24003380
     */
    @Test
    fun executeMUDRNRAPU00234() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00234".outputOf(configuration = smeupConfig))
    }

    /**
     * F-spec of type 'O' that is not a printer file
     * @see #LS24003409
     */
    @Test
    fun executeMUDRNRAPU00235() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00235".outputOf(configuration = smeupConfig))
    }

    /**
     * Access to an array detected as a function call by parser in IfStmt
     * @see #LS24003380
     */
    @Test
    fun executeMUDRNRAPU00236() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00236".outputOf(configuration = smeupConfig))
    }

    /**
     * Dspec with LIKE on a field defined in an API
     * @see #LS24003456
     */
    @Test
    fun executeMUDRNRAPU00238() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00238".outputOf(configuration = smeupConfig))
    }

    /**
     * Access to an array detected as a function call by parser in SubstExpr
     * @see #LS24003380
     */
    @Test
    fun executeMUDRNRAPU00239() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00239".outputOf(configuration = smeupConfig))
    }

    @Test
    fun executeMUDRNRAPU00241() {
        val expected = listOf("0")
        assertEquals(expected, "smeup/MUDRNRAPU00241".outputOf(configuration = smeupConfig))
    }

    /**
     * Access to an array detected as a function call by parser in ArrayAccessExpr
     * @see #LS24003380
     */
    @Test
    fun executeMUDRNRAPU00243() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00243".outputOf(configuration = smeupConfig))
    }

    /**
     * Access to an array detected as a function call recursively
     * @see #LS24003753
     */
    @Test
    fun executeMUDRNRAPU00244() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00244".outputOf(configuration = smeupConfig))
    }

    /**
     * Resolution of InStatement data definitions contained in CompositeStatements
     * @see #LS24003769
     */
    @Test
    fun executeMUDRNRAPU00245() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00245".outputOf(configuration = smeupConfig))
    }

    @Test
    fun executeMUDRNRAPU00246() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00246".outputOf(configuration = smeupConfig))
    }

    /**
     * Access to an array detected as a function call in NOT expressions
     * @see #LS24003380
     */
    @Test
    fun executeMUDRNRAPU00247() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00247".outputOf(configuration = smeupConfig))
    }

    /**
     * Array declaration inside a DS with an empty INZ keyword
     * @see #LS24003783
     */
    @Test
    fun executeMUDRNRAPU00249() {
        val expected = listOf(List(99) { "0" }.toString())
        assertEquals(expected, "smeup/MUDRNRAPU00249".outputOf(configuration = smeupConfig))
    }

    /**
     * INZ of a field inside a DS declared with OCCURS keyword
     * @see #LS24003786
     */
    @Test
    fun executeMUDRNRAPU00250() {
        val expected = listOf(List(40) { ".00" }.toString())
        assertEquals(expected, "smeup/MUDRNRAPU00250".outputOf(configuration = smeupConfig))
    }

    @Test
    fun executeMUDRNRAPU01101() {
        val expected = listOf("test", "test")
        assertEquals(expected, "smeup/MUDRNRAPU01101".outputOf(configuration = smeupConfig))
    }

    @Test
    fun executeMUDRNRAPU01102() {
        val expected = listOf("test", "te", "st")
        assertEquals(expected, "smeup/MUDRNRAPU01102".outputOf(configuration = smeupConfig))
    }

    @Test
    fun executeMUDRNRAPU01103() {
        val expected = listOf("1", "0")
        assertEquals(expected, "smeup/MUDRNRAPU01103".outputOf(configuration = smeupConfig))
    }

    /**
     * Access to a DS numeric field not initialized both by parent or from itself.
     * @note This behaviour is different on AS400: a field of DS not initialized is a
     *       hexadecimal value instead `0`.
     * @see #LS24004159
     */
    @Test
    fun executeMUDRNRAPU00131() {
        val expected = listOf(".00", "0")
        assertEquals(expected, "smeup/MUDRNRAPU00131".outputOf())
    }

    /**
     * LIKE on DS field with absolute path
     * @see #LS24003324
     */
    @Test
    fun executeMUDRNRAPU00263() {
        val expected = listOf("ok")
        assertEquals(expected, "smeup/MUDRNRAPU00263".outputOf(configuration = smeupConfig))
    }

    /**
     * Verifies the sort of ds array values
     * @see #LS24004379
     */
    @Test
    fun executeMUDRNRAPU01104() {
        val expected = listOf("ORIGINAL", "3", "2", "4", "1", "5", "ORDERED", "3", "1", "2", "4", "5")
        assertEquals(expected, "smeup/MUDRNRAPU01104".outputOf(configuration = smeupConfig))
    }
}
