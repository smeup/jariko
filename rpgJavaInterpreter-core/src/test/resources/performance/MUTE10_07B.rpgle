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
     V*  Programma finalizzato ai test su campi di tipo DECIMAL PACKED
     V*
     V*=====================================================================
     DAAA015           S             15
     DP0               S              1P 0
    MU* VAL1(P1) VAL2(1500) COMP(EQ)
     DP1               S              5P 0 INZ(1500)
     DP2               S              5P 0
     DP3               S             15P 5
    MU* VAL1(P4) VAL2(-4321,12345) COMP(EQ)
     DP4               S             15P 5 INZ(-4321,12345)
     DP5               S             15P 5
     DP6               S             12P 2
     DP7               S             11P 1
     DP8               S             10P 0
     DP9               S             21P 7
      *
     DP10              S              4P 0
     DP11              S              4P 0
     DP12              S              2P 0
     DP13              S              2P 0
     DP14              S              8P 0
      *
     DP15              S              5P 2
     DP16              S             20P 0
      *
     D TXT             S            100    DIM(10) PERRCD(1) CTDATA             _NOTXT
     D $TIMST          S               Z   INZ
     D $TIMEN          S               Z   INZ
     D $TIMMS          S             10  0
     D$MSG             S             52
      *
      * Main
     C                   EVAL      P10=*ALL'0'
      * Start Time
     C                   TIME                    $TIMST
      * Loop
     C                   DO        100000
      * Miscellaneus Subroutine
     C                   EXSR      F_BIGD
     C                   EXSR      F_ADD
     C                   EXSR      F_CLEAR
     C                   EXSR      F_DIV
     C                   EXSR      F_MOVE
     C                   EXSR      F_MULT
     C                   EXSR      F_MVR
     C                   EXSR      F_SUB
     C                   EXSR      F_Z
     C                   ENDDO
      * End time
     C                   TIME                    $TIMEN
      * Elapsed time
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
     C                   EVAL      $TIMMS=$TIMMS/1000
      * Display message
     C                   EVAL      $MSG=%trim(TXT(1))+' '+
     C                             %TRIM(%EDITC($TIMMS:'Q'))+'ms'
      *
      *
    MU* Type="NOXMI"
    MU* TIMEOUT(600)
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Routine test Big decimal
      *---------------------------------------------------------------------
     C     F_BIGD        BEGSR
      *
     C                   CLEAR                   P16
     C                   CLEAR                   AAA020           20
     C                   MOVEL     '1234567890'  AAA020
     C                   MOVE      '1234567890'  AAA020
    MU* VAL1(P16) VAL2(12345678901234567890,0) COMP(EQ)
     C                   MOVE      AAA020        P16
      *
     C                   CLEAR                   P16
    MU* VAL1(P16) VAL2(12345678901234567890,0) COMP(EQ)
     C                   EVAL      P16=12345678901234567890,0
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test ADD
      *---------------------------------------------------------------------
     C     F_ADD         BEGSR
      *
     C                   CLEAR                   P2
     C                   CLEAR                   P3
    MU* VAL1(P2) VAL2(1) COMP(EQ)
     C                   ADD       1             P2
    MU* VAL1(P3) VAL2(1) COMP(EQ)
     C                   ADD       1             P3
    MU* VAL1(P3) VAL2(1) COMP(EQ)
     C                   ADD(H)    0             P3
      *
     C                   CLEAR                   P2
     C                   CLEAR                   P3
    MU* VAL1(P2) VAL2(1) COMP(EQ)
     C                   ADD       1,003         P2
    MU* VAL1(P3) VAL2(1,003) COMP(EQ)
     C                   ADD       1,003         P3
    MU* VAL1(P2) VAL2(1501) COMP(EQ)
     C                   ADD       P1            P2
     C                   CLEAR                   P3
    MU* VAL1(P3) VAL2(1501,12345) COMP(EQ)
     C     P2            ADD       0,12345       P3
    MU* VAL1(P3) VAL2(1501,12345) COMP(EQ)
     C                   EVAL      P3=P2+0,12345
    MU* VAL1(P5) VAL2(-2820,00000) COMP(EQ)
     C     P3            ADD       P4            P5
      *
     C                   CLEAR                   P2
     C                   CLEAR                   P3
     C                   CLEAR                   P5
    MU* VAL1(P2) VAL2(1500) COMP(EQ)
     C                   EVAL      P2=P1+P2
    MU* VAL1(P3) VAL2(1500,12345) COMP(EQ)
     C                   EVAL      P3=P2+0,12345
    MU* VAL1(P5) VAL2(-2821,00000) COMP(EQ)
     C                   EVAL      P5=P3+P4
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test CLEAR
      *---------------------------------------------------------------------
     C     F_CLEAR       BEGSR
    MU* VAL1(P1) VAL2(0) COMP(EQ)
     C                   CLEAR                   P1
    MU* VAL1(P2) VAL2(0) COMP(EQ)
     C                   CLEAR                   P2
    MU* VAL1(P3) VAL2(0) COMP(EQ)
     C                   CLEAR                   P3
    MU* VAL1(P4) VAL2(0) COMP(EQ)
     C                   CLEAR                   P4
    MU* VAL1(P5) VAL2(0) COMP(EQ)
     C                   CLEAR                   P5
    MU* VAL1(P6) VAL2(0) COMP(EQ)
     C                   CLEAR                   P6
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test DIV
      *---------------------------------------------------------------------
     C     F_DIV         BEGSR
      *
     C                   CLEAR                   P2
     C                   CLEAR                   P3
     C                   CLEAR                   P5
     C                   CLEAR                   P6
    MU* VAL1(P2) VAL2(5) COMP(EQ)
     C     11            DIV       2             P2
    MU* VAL1(P2) VAL2(2) COMP(EQ)
     C                   DIV       2             P2
    MU* VAL1(P2) VAL2(6) COMP(EQ)
     C     11            DIV(H)    2             P2
    MU* VAL1(P2) VAL2(3) COMP(EQ)
     C                   DIV(H)    2             P2
    MU* VAL1(P2) VAL2(5) COMP(EQ)
     C                   EVAL      P2=11/2
    MU* VAL1(P2) VAL2(6) COMP(EQ)
     C                   EVAL(H)   P2=11/2
    MU* VAL1(P3) VAL2(5,5) COMP(EQ)
     C     11            DIV       2             P3
    MU* VAL1(P3) VAL2(5,5) COMP(EQ)
     C     11            DIV(H)    2             P3
    MU* VAL1(P3) VAL2(5,5) COMP(EQ)
     C                   EVAL      P3=11/2
    MU* VAL1(P3) VAL2(5,5) COMP(EQ)
     C                   EVAL(H)   P3=11/2
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test MOVEL
      *---------------------------------------------------------------------
     C     F_MOVE        BEGSR
      *
     C                   EVAL      P1=-9
     C                   MOVE      P1            AAA015
     C                   MOVE      '-9'          AAA015
     C                   MOVE      AAA015        P1
      *
    MU* VAL1(P1) VAL2(12345) COMP(EQ)
     C                   MOVE      '12345'       P1
      *
     C                   CLEAR                   AAA015           15
    MU* VAL1(AAA015) VAL2('12345          ') COMP(EQ)
     C                   MOVEL(P)  P1            AAA015
    MU* VAL1(AAA015) VAL2('          12345') COMP(EQ)
     C                   MOVE (P)  P1            AAA015
     C                   CLEAR                   AAA015           15
     C                   EVAL      P4=-4321,12345
    MU* VAL1(AAA015) VAL2('00000043211234N') COMP(EQ)
     C                   MOVEL(P)  P4            AAA015
     C                   CLEAR                   AAA015           15
    MU* VAL1(AAA015) VAL2('00000043211234N') COMP(EQ)
     C                   MOVE (P)  P4            AAA015
    MU* VAL1(P1) VAL2(-99999) COMP(EQ)
     C                   MOVE      *LOVAL        P1
    MU* VAL1(P1) VAL2(99999) COMP(EQ)
     C                   MOVE      *HIVAL        P1
    MU* VAL1(P3) VAL2(-9999999999,99999) COMP(EQ)
     C                   MOVE      *LOVAL        P3
    MU* VAL1(P3) VAL2(9999999999,99999) COMP(EQ)
     C                   MOVE      *HIVAL        P3
      *
     C                   EVAL      P8=123456
    MU* VAL1(P2) VAL2(23456) COMP(EQ)
     C                   MOVE      P8            P2
    MU* VAL1(P2) VAL2(1) COMP(EQ)
     C                   MOVEL     P8            P2
      *
     C                   CLEAR                   P10
     C                   CLEAR                   P11
     C                   CLEAR                   P12
     C                   CLEAR                   P13
     C                   CLEAR                   P14
    MU* VAL1(P14) VAL2(18042016) COMP(EQ)
     C                   MOVE      18042016      P14
    MU* VAL1(P10) VAL2(1804) COMP(EQ)
     C                   MOVEL     P14           P10
    MU* VAL1(P11) VAL2(2016) COMP(EQ)
     C                   MOVE      P14           P11
    MU* VAL1(P12) VAL2(18) COMP(EQ)
     C                   MOVEL     P10           P12
    MU* VAL1(P13) VAL2(04) COMP(EQ)
     C                   MOVE      P10           P13
    MU* VAL1(P10) VAL2(1818) COMP(EQ)
     C                   MOVE      P12           P10
    MU* VAL1(P10) VAL2(0418) COMP(EQ)
     C                   MOVEL     P13           P10
    MU* VAL1(P14) VAL2(18040418) COMP(EQ)
     C                   MOVE      P10           P14
    MU* VAL1(P14) VAL2(20160418) COMP(EQ)
     C                   MOVEL     P11           P14
    MU* VAL1(P3) VAL2(20160418) COMP(EQ)
     C                   MOVE(P)   '123456'      P3
    MU* VAL1(P3) VAL2(1,23456) COMP(EQ)
     C                   MOVE(P)   '123456'      P3
    MU* VAL1(P3) VAL2(123456) COMP(EQ)
     C                   MOVE(P)   '12345600000' P3
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test MULT
      *---------------------------------------------------------------------
     C     F_MULT        BEGSR
      *
     C                   CLEAR                   P2
     C                   CLEAR                   P3
     C                   CLEAR                   P5
     C                   CLEAR                   P6
    MU* VAL1(P2) VAL2(10) COMP(EQ)
     C     2             MULT      5             P2
    MU* VAL1(P2) VAL2(50) COMP(EQ)
     C                   MULT      5             P2
    MU* VAL1(P2) VAL2(-10) COMP(EQ)
     C     2             MULT      -5            P2
    MU* VAL1(P2) VAL2(50) COMP(EQ)
     C                   MULT      -5            P2
    MU* VAL1(P2) VAL2(50) COMP(EQ)
     C                   MULT(H)   1             P2
    MU* VAL1(P2) VAL2(10) COMP(EQ)
     C     2             MULT      5,12345       P2
    MU* VAL1(P2) VAL2(-10) COMP(EQ)
     C     2             MULT      -5,12345      P2
      *
    MU* VAL1(P3) VAL2(10,2469) COMP(EQ)
     C     2             MULT      5,12345       P3
    MU* VAL1(P3) VAL2(-10,2469) COMP(EQ)
     C     2             MULT      -5,12345      P3
    MU* VAL1(P3) VAL2(10,2469) COMP(EQ)
     C     2             MULT(H)   5,12345       P3
    MU* VAL1(P3) VAL2(-10,2469) COMP(EQ)
     C     2             MULT(H)   -5,12345      P3
      *
    MU* VAL1(P6) VAL2(11,97) COMP(EQ)
     C     2             MULT      5,98765       P6
    MU* VAL1(P6) VAL2(-11,97) COMP(EQ)
     C     2             MULT      -5,98765      P6
    MU* VAL1(P6) VAL2(11,98) COMP(EQ)
     C     2             MULT(H)   5,98765       P6
    MU* VAL1(P6) VAL2(-11,98) COMP(EQ)
     C     2             MULT(H)   -5,98765      P6
      *
    MU* VAL1(P2) VAL2(10) COMP(EQ)
     C                   EVAL      P2=2*5
    MU* VAL1(P2) VAL2(-10) COMP(EQ)
     C                   EVAL      P2=2*(-5)
    MU* VAL1(P2) VAL2(10) COMP(EQ)
     C                   EVAL      P2=2*5,12345
    MU* VAL1(P2) VAL2(-10) COMP(EQ)
     C                   EVAL      P2=2*(-5,12345)
    MU* VAL1(P3) VAL2(10,2469) COMP(EQ)
     C                   EVAL      P3=2*5,12345
    MU* VAL1(P3) VAL2(-10,2469) COMP(EQ)
     C                   EVAL      P3=2*-5,12345
    MU* VAL1(P6) VAL2(11,97) COMP(EQ)
     C                   EVAL      P6=2*5,98765
    MU* VAL1(P6) VAL2(-11,97) COMP(EQ)
     C                   EVAL      P6=2*-5,98765
    MU* VAL1(P6) VAL2(11,98) COMP(EQ)
     C                   EVAL(H)   P6=2*5,98765
    MU* VAL1(P6) VAL2(-11,98) COMP(EQ)
     C                   EVAL(H)   P6=2*-5,98765
    MU* VAL1(P6) VAL2(11,98) COMP(EQ)
     C                   EVAL(MH)  P6=2*5,98765
    MU* VAL1(P6) VAL2(11,98) COMP(EQ)
     C                   EVAL(HM)  P6=2*5,98765
    MU* VAL1(P6) VAL2(-11,98) COMP(EQ)
     C                   EVAL(MH)  P6=2*-5,98765
    MU* VAL1(P6) VAL2(11,98) COMP(EQ)
     C                   EVAL(RH)  P6=2*5,98765
    MU* VAL1(P6) VAL2(11,98) COMP(EQ)
     C                   EVAL(HR)  P6=2*5,98765
    MU* VAL1(P6) VAL2(-11,98) COMP(EQ)
     C                   EVAL(RH)  P6=2*-5,98765
    MU* VAL1(P6) VAL2(-11,97) COMP(EQ)
     C                   EVAL(M)   P6=2*-5,98765
    MU* VAL1(P6) VAL2(-11,97) COMP(EQ)
     C                   EVAL(R)   P6=2*-5,98765
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test MVR
      *---------------------------------------------------------------------
     C     F_MVR         BEGSR
      *
     C                   Z-ADD     44            P1
     C                   Z-ADD     6             P2
     C                   IF        %REM(P1:P2)>0
     C                   EVAL      P3=1
     C                   ENDIF
     C                   IF        %REM(11:6)>0
     C                   EVAL      P3=2
     C                   ENDIF
      *
    MU* VAL1(P3) VAL2(1) COMP(EQ)
     C     10            DIV       3             P2
     C                   MVR                     P3
    MU* VAL1(P6) VAL2(0) COMP(EQ)
     C     10            DIV       3             P5
     C                   MVR                     P6
     C     10            DIV       3             P5
     C                   MVR                     P9
      *
     C                   EVAL      P5=10
    MU* VAL1(P6) VAL2(0) COMP(EQ)
     C                   DIV       2             P5
     C                   MVR                     P6
      *
     C                   CLEAR                   P1
     C                   CLEAR                   P2
     C                   CLEAR                   P8
     C                   CLEAR                   P6
     C                   Z-ADD     44            P1
     C                   Z-ADD     6             P2
    MU* VAL1(P6) VAL2(2) COMP(EQ)
     C     P1            DIV       P2            P8
     C                   MVR                     P6
      *
     C                   CLEAR                   P1
     C                   CLEAR                   P2
     C                   CLEAR                   P3
     C                   CLEAR                   P6
     C                   Z-ADD     44            P1
     C                   Z-ADD     6             P2
    MU* VAL1(P6) VAL2(0) COMP(EQ)
     C     P1            DIV       P2            P3
     C                   MVR                     P6
      *
    MU* VAL1(P6) VAL2(5) COMP(EQ)
     C                   EVAL      P6=%REM(11:6)
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test SUB
      *---------------------------------------------------------------------
     C     F_SUB         BEGSR
     C                   CLEAR                   P2
     C                   CLEAR                   P3
     C                   CLEAR                   P5
     C                   EVAL      P1=1500
      *
    MU* VAL1(P2) VAL2(-1) COMP(EQ)
     C                   SUB       1             P2
    MU* VAL1(P5) VAL2(-1) COMP(EQ)
     C                   SUB       1             P5
    MU* VAL1(P5) VAL2(-1) COMP(EQ)
     C                   SUB(H)    0             P5
     C                   CLEAR                   P2
     C                   CLEAR                   P5
      *
    MU* VAL1(P2) VAL2(-1500) COMP(EQ)
     C                   SUB       P1            P2
    MU* VAL1(P3) VAL2(-1500,12345) COMP(EQ)
     C     P2            SUB       0,12345       P3
    MU* VAL1(P5) VAL2(2821,00000) COMP(EQ)
     C     P3            SUB       P4            P5
      *
     C                   CLEAR                   P2
     C                   CLEAR                   P3
     C                   CLEAR                   P5
    MU* VAL1(P2) VAL2(-1500) COMP(EQ)
     C                   EVAL      P2=P2-P1
    MU* VAL1(P3) VAL2(-1500,12345) COMP(EQ)
     C                   EVAL      P3=P2-0,12345
    MU* VAL1(P5) VAL2(2821,00000) COMP(EQ)
     C                   EVAL      P5=P3-P4
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test F_Z    ADD/SUB
      *---------------------------------------------------------------------
     C     F_Z           BEGSR
    MU* VAL1(P1) VAL2(44) COMP(EQ)
     C                   Z-ADD     44            P1
    MU* VAL1(P2) VAL2(44) COMP(EQ)
     C                   Z-ADD     P1            P2
    MU* VAL1(P2) VAL2(5) COMP(EQ)
     C                   Z-ADD     5,6           P2
    MU* VAL1(P2) VAL2(6) COMP(EQ)
     C                   Z-ADD(H)  5,6           P2
    MU* VAL1(P3) VAL2(5,6) COMP(EQ)
     C                   Z-ADD     5,6           P3
    MU* VAL1(P3) VAL2(5,6) COMP(EQ)
     C                   Z-ADD(H)  5,6           P3
    MU* VAL1(P2) VAL2(-5) COMP(EQ)
     C                   Z-SUB     5             P2
      *
     C                   ENDSR
** TXT
Time spent
