     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 05/12/19  001345  BERNI Creato
     V* 09/12/19  001345  BMA   Alcune modifiche
     V* 09/12/19  V5R1    BMA   Check-out 001345 in SMEUP_TST
     V* 11/12/19  001362  BERNI Aggiunti commenti
     V* 11/12/19  V5R1    BMA   Check-out 001362 in SMEUP_TST
     V*=====================================================================
     V* OBIETTIVO
     V*  Programma finalizzato ai test di performance su campi di tipo DECIMAL ZONED
     V*
     V*=====================================================================
     DS0               S              1S 0
    MU* VAL1(S1) VAL2(1500) COMP(EQ)
     DS1               S              5S 0 INZ(1500)
     DS2               S              5S 0 INZ(*ZEROS)
     DS3               S             15S 5 INZ(*zeROs)
    MU* VAL1(S4) VAL2(-4321,12345) COMP(EQ)
     DS4               S             15S 5 INZ(-4321,12345)
     DS5               S             15S 5
     DS6               S             12S 2
     DS7               S             11S 1
     DS8               S             10S 0
     DS9               S             21S 7
      *
     DS10              S              4S 0
     DS11              S              4S 0
     DS12              S              2S 0
     DS13              S              2S 0
     DS14              S              8S 0
     DS15              S             20S 0
      *
     D TXT             S            100    DIM(10) PERRCD(1) CTDATA             _NOTXT
     D $TIMST          S               Z   INZ
     D $TIMEN          S               Z   INZ
     D $TIMMS          S             10  0
     D$MSG             S             52
      *
      * Main
     C                   CLEAR                   S5
     C                   CLEAR                   S15
      * Start time
     C                   TIME                    $TIMST
      * Loop
     C                   DO        100000
      * Miscellaneaus subroutine
     C                   EXSR      F_BIGD
     C                   EXSR      F_ADD
     C                   EXSR      F_CLEAR
     C                   EXSR      F_DIV
     C                   EXSR      F_EVAL
     C                   EXSR      F_MOVE
     C                   EXSR      F_MULT
     C                   EXSR      F_MVR
     C                   EXSR      F_SUB
     C                   EXSR      F_Z
      *
     C                   ENDDO
      * End time
     C                   TIME                    $TIMEN
      * Elapsed time
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
    MU* VAL1($TIMMS) VAL2(1000) COMP(LT)
     C                   EVAL      $TIMMS=$TIMMS/1000
      * Display message
     C                   EVAL      $MSG=%trim(TXT(1))+' '+
     C                             %TRIM(%EDITC($TIMMS:'Q'))+'ms'
     C     $MSG          DSPLY     £PDSSU
      *
    MU* Type="NOXMI"
    MU* TIMEOUT(3500)
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Routine test ADD for big decimal variables
      *---------------------------------------------------------------------
     C     F_BIGD        BEGSR
      *
     C                   CLEAR                   S15
     C                   CLEAR                   AAA020           20
     C                   MOVEL     '1234567890'  AAA020
     C                   MOVE      '1234567890'  AAA020
    MU* VAL1(S15) VAL2(12345678901234567890,0) COMP(EQ)
     C                   MOVE      AAA020        S15
      *
     C                   CLEAR                   S15
    MU* VAL1(S15) VAL2(12345678901234567890,0) COMP(EQ)
     C                   EVAL      S15=12345678901234567890,0
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test ADD for small numeric varables
      *---------------------------------------------------------------------
     C     F_ADD         BEGSR
      *
     C                   CLEAR                   S2
     C                   CLEAR                   S3
    MU* VAL1(S2) VAL2(1) COMP(EQ)
     C                   ADD       1             S2
    MU* VAL1(S3) VAL2(1) COMP(EQ)
     C                   ADD       1             S3
    MU* VAL1(S3) VAL2(1) COMP(EQ)
     C                   ADD(H)    0             S3
      *
     C                   CLEAR                   S2
     C                   CLEAR                   S3
    MU* VAL1(S2) VAL2(1) COMP(EQ)
     C                   ADD       1,003         S2
    MU* VAL1(S3) VAL2(1,003) COMP(EQ)
     C                   ADD       1,003         S3
    MU* VAL1(S2) VAL2(1501) COMP(EQ)
     C                   ADD       S1            S2
     C                   CLEAR                   S3
    MU* VAL1(S3) VAL2(1501,12345) COMP(EQ)
     C     S2            ADD       0,12345       S3
    MU* VAL1(S3) VAL2(1501,12345) COMP(EQ)
     C                   EVAL      S3=S2+0,12345
    MU* VAL1(S5) VAL2(-2820,00000) COMP(EQ)
     C     S3            ADD       S4            S5
      *
     C                   CLEAR                   S2
     C                   CLEAR                   S3
     C                   CLEAR                   S5
    MU* VAL1(S2) VAL2(1500) COMP(EQ)
     C                   EVAL      S2=S1+S2
    MU* VAL1(S3) VAL2(1500,12345) COMP(EQ)
     C                   EVAL      S3=S2+0,12345
    MU* VAL1(S5) VAL2(-2821,00000) COMP(EQ)
     C                   EVAL      S5=S3+S4
      *
     C                   Z-ADD     1             S3
     C                   CLEAR                   S4
    MU* VAL1(S5) VAL2(0,5) COMP(EQ)
     C     -0,5          ADD       S3            S4
     C                   CLEAR                   S4
    MU* VAL1(S5) VAL2(1,5) COMP(EQ)
     C     0,5           ADD       S3            S4
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test CLEAR
      *---------------------------------------------------------------------
     C     F_CLEAR       BEGSR
    MU* VAL1(S1) VAL2(0) COMP(EQ)
     C                   CLEAR                   S1
    MU* VAL1(S2) VAL2(0) COMP(EQ)
     C                   CLEAR                   S2
    MU* VAL1(S3) VAL2(0) COMP(EQ)
     C                   CLEAR                   S3
    MU* VAL1(S4) VAL2(0) COMP(EQ)
     C                   CLEAR                   S4
    MU* VAL1(S5) VAL2(0) COMP(EQ)
     C                   CLEAR                   S5
    MU* VAL1(S6) VAL2(0) COMP(EQ)
     C                   CLEAR                   S6
    MU* VAL1(S9) VAL2(0) COMP(EQ)
     C                   CLEAR                   S9
    MU* VAL1(S15) VAL2(0) COMP(EQ)
     C                   CLEAR                   S15
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test DIV
      *---------------------------------------------------------------------
     C     F_DIV         BEGSR
      *
     C                   CLEAR                   S2
     C                   CLEAR                   S3
     C                   CLEAR                   S5
     C                   CLEAR                   S6
    MU* VAL1(S2) VAL2(5) COMP(EQ)
     C     11            DIV       2             S2
    MU* VAL1(S2) VAL2(2) COMP(EQ)
     C                   DIV       2             S2
    MU* VAL1(S2) VAL2(6) COMP(EQ)
     C     11            DIV(H)    2             S2
    MU* VAL1(S2) VAL2(3) COMP(EQ)
     C                   DIV(H)    2             S2
    MU* VAL1(S2) VAL2(5) COMP(EQ)
     C                   EVAL      S2=11/2
    MU* VAL1(S2) VAL2(6) COMP(EQ)
     C                   EVAL(H)   S2=11/2
    MU* VAL1(S3) VAL2(5,5) COMP(EQ)
     C     11            DIV       2             S3
    MU* VAL1(S3) VAL2(5,5) COMP(EQ)
     C     11            DIV(H)    2             S3
    MU* VAL1(S3) VAL2(5,5) COMP(EQ)
     C                   EVAL      S3=11/2
    MU* VAL1(S3) VAL2(5,5) COMP(EQ)
     C                   EVAL(H)   S3=11/2
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test EVAL
      *---------------------------------------------------------------------
     C     F_EVAL        BEGSR
      *
    MU* VAL1(S9) VAL2(9999999999) COMP(EQ)
     C                   EVAL      S9=9999999999
      *
    MU* VAL1(S5) VAL2(-9999999999,99999) COMP(EQ)
     C                   EVAL      S5=*LOVAL
    MU* VAL1(S5) VAL2(9999999999,99999) COMP(EQ)
     C                   EVAL      S5=*HIVAL
    MU* VAL1(S6) VAL2(-9999999999,99) COMP(EQ)
     C                   EVAL      S6=*LOVAL
    MU* VAL1(S6) VAL2(9999999999,99) COMP(EQ)
     C                   EVAL      S6=*HIVAL
    MU* VAL1(S8) VAL2(-9999999999) COMP(EQ)
     C                   EVAL      S8=*LOVAL
    MU* VAL1(S8) VAL2(9999999999) COMP(EQ)
     C                   EVAL      S8=*HIVAL
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test MOVE
      *---------------------------------------------------------------------
     C     F_MOVE        BEGSR
      *
     C                   EVAL      S1=-9
     C                   MOVE      S1            AAA015
     C                   MOVE      '-9'          AAA015
     C                   MOVE      AAA015        S1
      *
    MU* VAL1(S1) VAL2(12345) COMP(EQ)
     C                   MOVE      '12345'       S1
      *
     C                   CLEAR                   AAA015           15
    MU* VAL1(AAA015) VAL2('12345          ') COMP(EQ)
     C                   MOVEL(P)  S1            AAA015
    MU* VAL1(AAA015) VAL2('          12345') COMP(EQ)
     C                   MOVE (P)  S1            AAA015
     C                   CLEAR                   AAA015           15
     C                   EVAL      S4=-4321,12345
    MU* VAL1(AAA015) VAL2('00000043211234N') COMP(EQ)
     C                   MOVEL(P)  S4            AAA015
     C                   CLEAR                   AAA015           15
    MU* VAL1(AAA015) VAL2('00000043211234N') COMP(EQ)
     C                   MOVE (P)  S4            AAA015
    MU* VAL1(S1) VAL2(-99999) COMP(EQ)
     C                   MOVE      *LOVAL        S1
    MU* VAL1(S1) VAL2(99999) COMP(EQ)
     C                   MOVE      *HIVAL        S1
    MU* VAL1(S3) VAL2(-9999999999,99999) COMP(EQ)
     C                   MOVE      *LOVAL        S3
    MU* VAL1(S3) VAL2(9999999999,99999) COMP(EQ)
     C                   MOVE      *HIVAL        S3

     C                   EVAL      S8=123456
    MU* VAL1(S2) VAL2(23456) COMP(EQ)
     C                   MOVE      S8            S2
    MU* VAL1(S2) VAL2(1) COMP(EQ)
     C                   MOVEL     S8            S2
      *
     C                   CLEAR                   S10
     C                   CLEAR                   S11
     C                   CLEAR                   S12
     C                   CLEAR                   S13
     C                   CLEAR                   S14
    MU* VAL1(S14) VAL2(18042016) COMP(EQ)
     C                   MOVE      18042016      S14
    MU* VAL1(S10) VAL2(1804) COMP(EQ)
     C                   MOVEL     S14           S10
    MU* VAL1(S11) VAL2(2016) COMP(EQ)
     C                   MOVE      S14           S11
    MU* VAL1(S12) VAL2(18) COMP(EQ)
     C                   MOVEL     S10           S12
    MU* VAL1(S13) VAL2(04) COMP(EQ)
     C                   MOVE      S10           S13
    MU* VAL1(S10) VAL2(1818) COMP(EQ)
     C                   MOVE      S12           S10
    MU* VAL1(S10) VAL2(0418) COMP(EQ)
     C                   MOVEL     S13           S10
    MU* VAL1(S14) VAL2(18040418) COMP(EQ)
     C                   MOVE      S10           S14
    MU* VAL1(S14) VAL2(20160418) COMP(EQ)
     C                   MOVEL     S11           S14
      *
    MU* VAL1(S8) VAL2(12345) COMP(EQ)
     C                   EVAL      S8=12345
     C                   CLEAR                   AAA010           10
    MU* VAL1(S8) VAL2(0) COMP(EQ)
     C                   MOVE      AAA010        S8
     C                   MOVE(P)   ' 1 '         AAA010
    MU* VAL1(S8) VAL2(10) COMP(EQ)
     C                   MOVE      AAA010        S8
      *
     C                   CLEAR                   S9
    MU* VAL1(S9) VAL2(9999999999) COMP(EQ)
     C                   MOVE      9999999999    S9
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test MULT
      *---------------------------------------------------------------------
     C     F_MULT        BEGSR
      *
     C                   CLEAR                   S2
     C                   CLEAR                   S3
     C                   CLEAR                   S5
     C                   CLEAR                   S6
    MU* VAL1(S2) VAL2(10) COMP(EQ)
     C     2             MULT      5             S2
    MU* VAL1(S2) VAL2(50) COMP(EQ)
     C                   MULT      5             S2
    MU* VAL1(S2) VAL2(-10) COMP(EQ)
     C     2             MULT      -5            S2
    MU* VAL1(S2) VAL2(50) COMP(EQ)
     C                   MULT      -5            S2
    MU* VAL1(S2) VAL2(50) COMP(EQ)
     C                   MULT(H)   1             S2
    MU* VAL1(S2) VAL2(10) COMP(EQ)
     C     2             MULT      5,12345       S2
    MU* VAL1(S2) VAL2(-10) COMP(EQ)
     C     2             MULT      -5,12345      S2
      *
    MU* VAL1(S3) VAL2(10,2469) COMP(EQ)
     C     2             MULT      5,12345       S3
    MU* VAL1(S3) VAL2(-10,2469) COMP(EQ)
     C     2             MULT      -5,12345      S3
    MU* VAL1(S3) VAL2(10,2469) COMP(EQ)
     C     2             MULT(H)   5,12345       S3
    MU* VAL1(S3) VAL2(-10,2469) COMP(EQ)
     C     2             MULT(H)   -5,12345      S3
      *
    MU* VAL1(S6) VAL2(11,97) COMP(EQ)
     C     2             MULT      5,98765       S6
    MU* VAL1(S6) VAL2(-11,97) COMP(EQ)
     C     2             MULT      -5,98765      S6
    MU* VAL1(S6) VAL2(11,98) COMP(EQ)
     C     2             MULT(H)   5,98765       S6
    MU* VAL1(S6) VAL2(-11,98) COMP(EQ)
     C     2             MULT(H)   -5,98765      S6
      *
    MU* VAL1(S2) VAL2(10) COMP(EQ)
     C                   EVAL      S2=2*5
    MU* VAL1(S2) VAL2(-10) COMP(EQ)
     C                   EVAL      S2=2*(-5)
    MU* VAL1(S2) VAL2(10) COMP(EQ)
     C                   EVAL      S2=2*5,12345
    MU* VAL1(S2) VAL2(-10) COMP(EQ)
     C                   EVAL      S2=2*(-5,12345)
    MU* VAL1(S3) VAL2(10,2469) COMP(EQ)
     C                   EVAL      S3=2*5,12345
    MU* VAL1(S3) VAL2(-10,2469) COMP(EQ)
     C                   EVAL      S3=2*-5,12345
    MU* VAL1(S6) VAL2(11,97) COMP(EQ)
     C                   EVAL      S6=2*5,98765
    MU* VAL1(S6) VAL2(-11,97) COMP(EQ)
     C                   EVAL      S6=2*-5,98765
    MU* VAL1(S6) VAL2(11,98) COMP(EQ)
     C                   EVAL(H)   S6=2*5,98765
    MU* VAL1(S6) VAL2(-11,98) COMP(EQ)
     C                   EVAL(H)   S6=2*-5,98765
    MU* VAL1(S6) VAL2(11,98) COMP(EQ)
     C                   EVAL(MH)  S6=2*5,98765
    MU* VAL1(S6) VAL2(11,98) COMP(EQ)
     C                   EVAL(HM)  S6=2*5,98765
    MU* VAL1(S6) VAL2(-11,98) COMP(EQ)
     C                   EVAL(MH)  S6=2*-5,98765
    MU* VAL1(S6) VAL2(11,98) COMP(EQ)
     C                   EVAL(RH)  S6=2*5,98765
    MU* VAL1(S6) VAL2(11,98) COMP(EQ)
     C                   EVAL(HR)  S6=2*5,98765
    MU* VAL1(S6) VAL2(-11,98) COMP(EQ)
     C                   EVAL(RH)  S6=2*-5,98765
    MU* VAL1(S6) VAL2(-11,97) COMP(EQ)
     C                   EVAL(M)   S6=2*-5,98765
    MU* VAL1(S6) VAL2(-11,97) COMP(EQ)
     C                   EVAL(R)   S6=2*-5,98765
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test MVR
      *---------------------------------------------------------------------
     C     F_MVR         BEGSR
      *
     C                   Z-ADD     44            S1
     C                   Z-ADD     6             S2
     C                   IF        %REM(s1:s2)>0
     C                   EVAL      S3=1
     C                   ENDIF
     C                   IF        %REM(11:6)>0
     C                   EVAL      S3=2
     C                   ENDIF
      *
    MU* VAL1(S3) VAL2(1) COMP(EQ)
     C     10            DIV       3             S2
     C                   MVR                     S3
    MU* VAL1(S6) VAL2(0) COMP(EQ)
     C     10            DIV       3             S5
     C                   MVR                     S6
     C     10            DIV       3             S5
     C                   MVR                     S9
      *
     C                   EVAL      S5=10
    MU* VAL1(S6) VAL2(0) COMP(EQ)
     C                   DIV       2             S5
     C                   MVR                     S6
      *
     C                   CLEAR                   S1
     C                   CLEAR                   S2
     C                   CLEAR                   S8
     C                   CLEAR                   S6
     C                   Z-ADD     44            S1
     C                   Z-ADD     6             S2
    MU* VAL1(S6) VAL2(2) COMP(EQ)
     C     S1            DIV       S2            S8
     C                   MVR                     S6
      *
     C                   CLEAR                   S1
     C                   CLEAR                   S2
     C                   CLEAR                   S3
     C                   CLEAR                   S6
     C                   Z-ADD     44            S1
     C                   Z-ADD     6             S2
    MU* VAL1(S6) VAL2(0) COMP(EQ)
     C     S1            DIV       S2            S3
     C                   MVR                     S6
      *
    MU* VAL1(S6) VAL2(5) COMP(EQ)
     C                   EVAL      S6=%REM(11:6)
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test SUB
      *---------------------------------------------------------------------
     C     F_SUB         BEGSR
     C                   CLEAR                   S2
     C                   CLEAR                   S3
     C                   CLEAR                   S5
     C                   EVAL      S1=1500
      *
    MU* VAL1(S2) VAL2(-1) COMP(EQ)
     C                   SUB       1             S2
    MU* VAL1(S5) VAL2(-1) COMP(EQ)
     C                   SUB       1             S5
    MU* VAL1(S5) VAL2(-1) COMP(EQ)
     C                   SUB(H)    0             S5
     C                   CLEAR                   S2
     C                   CLEAR                   S5
      *
    MU* VAL1(S2) VAL2(-1500) COMP(EQ)
     C                   SUB       S1            S2
    MU* VAL1(S3) VAL2(-1500,12345) COMP(EQ)
     C     S2            SUB       0,12345       S3
    MU* VAL1(S5) VAL2(2821,00000) COMP(EQ)
     C     S3            SUB       S4            S5
      *
     C                   CLEAR                   S2
     C                   CLEAR                   S3
     C                   CLEAR                   S5
    MU* VAL1(S2) VAL2(-1500) COMP(EQ)
     C                   EVAL      S2=S2-S1
    MU* VAL1(S3) VAL2(-1500,12345) COMP(EQ)
     C                   EVAL      S3=S2-0,12345
    MU* VAL1(S5) VAL2(2821,00000) COMP(EQ)
     C                   EVAL      S5=S3-S4
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test F_Z    ADD/SUB
      *---------------------------------------------------------------------
     C     F_Z           BEGSR
    MU* VAL1(S1) VAL2(44) COMP(EQ)
     C                   Z-ADD     44            S1
    MU* VAL1(S2) VAL2(44) COMP(EQ)
     C                   Z-ADD     S1            S2
    MU* VAL1(S2) VAL2(5) COMP(EQ)
     C                   Z-ADD     5,6           S2
    MU* VAL1(S2) VAL2(6) COMP(EQ)
     C                   Z-ADD(H)  5,6           S2
    MU* VAL1(S3) VAL2(5,6) COMP(EQ)
     C                   Z-ADD     5,6           S3
    MU* VAL1(S3) VAL2(5,6) COMP(EQ)
     C                   Z-ADD(H)  5,6           S3
    MU* VAL1(S2) VAL2(-5) COMP(EQ)
     C                   Z-SUB     5             S2
      *
     C                   Z-ADD     123456        S8
    MU* VAL1(S2) VAL2(23456) COMP(EQ)
     C                   Z-ADD     S8            S2
    MU* VAL1(S2) VAL2(23456) COMP(EQ)
     C                   Z-ADD(H)  S8            S2
      *
    MU* VAL1(S2) VAL2(99999) COMP(EQ)
     C                   Z-ADD     *ALL'9'       S2
      *
     C                   ENDSR
** TXT
Time spent
