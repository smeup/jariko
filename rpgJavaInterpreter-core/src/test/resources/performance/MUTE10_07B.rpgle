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
     DP1               S              5P 0 INZ(1500)
     DP2               S              5P 0
     DP3               S             15P 5
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
     C                   EXSR      F_ADD
     C                   EXSR      F_CLEAR
     C                   EXSR      F_DIV
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
    MU* TIMEOUT(1000)
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Routine test ADD
      *---------------------------------------------------------------------
     C     F_ADD         BEGSR
      *
     C                   CLEAR                   P2
     C                   CLEAR                   P3
     C                   ADD       1             P2
     C                   ADD       1             P3
     C                   ADD(H)    0             P3
      *
     C                   CLEAR                   P2
     C                   CLEAR                   P3
     C                   ADD       1,003         P2
     C                   ADD       1,003         P3
     C                   ADD       P1            P2
     C                   CLEAR                   P3
     C     P2            ADD       0,12345       P3
     C                   EVAL      P3=P2+0,12345
     C     P3            ADD       P4            P5
      *
     C                   CLEAR                   P2
     C                   CLEAR                   P3
     C                   CLEAR                   P5
     C                   EVAL      P2=P1+P2
     C                   EVAL      P3=P2+0,12345
     C                   EVAL      P5=P3+P4
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test CLEAR
      *---------------------------------------------------------------------
     C     F_CLEAR       BEGSR
     C                   CLEAR                   P1
     C                   CLEAR                   P2
     C                   CLEAR                   P3
     C                   CLEAR                   P4
     C                   CLEAR                   P5
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
     C     11            DIV       2             P2
     C                   DIV       2             P2
     C     11            DIV(H)    2             P2
     C                   DIV(H)    2             P2
     C                   EVAL      P2=11/2
     C                   EVAL(H)   P2=11/2
     C     11            DIV       2             P3
     C     11            DIV(H)    2             P3
     C                   EVAL      P3=11/2
     C                   EVAL(H)   P3=11/2
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
     C     2             MULT      5             P2
     C                   MULT      5             P2
     C     2             MULT      -5            P2
     C                   MULT      -5            P2
     C                   MULT(H)   1             P2
     C     2             MULT      5,12345       P2
     C     2             MULT      -5,12345      P2
      *
     C     2             MULT      5,12345       P3
     C     2             MULT      -5,12345      P3
     C     2             MULT(H)   5,12345       P3
     C     2             MULT(H)   -5,12345      P3
      *
     C     2             MULT      5,98765       P6
     C     2             MULT      -5,98765      P6
     C     2             MULT(H)   5,98765       P6
     C     2             MULT(H)   -5,98765      P6
      *
     C                   EVAL      P2=2*5
     C                   EVAL      P2=2*(-5)
     C                   EVAL      P2=2*5,12345
     C                   EVAL      P2=2*(-5,12345)
     C                   EVAL      P3=2*5,12345
     C                   EVAL      P3=2*-5,12345
     C                   EVAL      P6=2*5,98765
     C                   EVAL      P6=2*-5,98765
     C                   EVAL(H)   P6=2*5,98765
     C                   EVAL(H)   P6=2*-5,98765
     C                   EVAL(MH)  P6=2*5,98765
     C                   EVAL(HM)  P6=2*5,98765
     C                   EVAL(MH)  P6=2*-5,98765
     C                   EVAL(RH)  P6=2*5,98765
     C                   EVAL(HR)  P6=2*5,98765
     C                   EVAL(RH)  P6=2*-5,98765
     C                   EVAL(M)   P6=2*-5,98765
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
     C     10            DIV       3             P2
     C                   MVR                     P3
     C     10            DIV       3             P5
     C                   MVR                     P6
     C     10            DIV       3             P5
     C                   MVR                     P9
      *
     C                   EVAL      P5=10
     C                   DIV       2             P5
     C                   MVR                     P6
      *
     C                   CLEAR                   P1
     C                   CLEAR                   P2
     C                   CLEAR                   P8
     C                   CLEAR                   P6
     C                   Z-ADD     44            P1
     C                   Z-ADD     6             P2
     C     P1            DIV       P2            P8
     C                   MVR                     P6
      *
     C                   CLEAR                   P1
     C                   CLEAR                   P2
     C                   CLEAR                   P3
     C                   CLEAR                   P6
     C                   Z-ADD     44            P1
     C                   Z-ADD     6             P2
     C     P1            DIV       P2            P3
     C                   MVR                     P6
      *
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
     C                   SUB       1             P2
     C                   SUB       1             P5
     C                   SUB(H)    0             P5
     C                   CLEAR                   P2
     C                   CLEAR                   P5
      *
     C                   SUB       P1            P2
     C     P2            SUB       0,12345       P3
     C     P3            SUB       P4            P5
      *
     C                   CLEAR                   P2
     C                   CLEAR                   P3
     C                   CLEAR                   P5
     C                   EVAL      P2=P2-P1
     C                   EVAL      P3=P2-0,12345
     C                   EVAL      P5=P3-P4
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test F_Z    ADD/SUB
      *---------------------------------------------------------------------
     C     F_Z           BEGSR
     C                   Z-ADD     44            P1
     C                   Z-ADD     P1            P2
     C                   Z-ADD     5,6           P2
     C                   Z-ADD(H)  5,6           P2
     C                   Z-ADD     5,6           P3
     C                   Z-ADD(H)  5,6           P3
     C                   Z-SUB     5             P2
      *
     C                   ENDSR
** TXT
Time spent
