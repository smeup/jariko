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
     DS1               S              5S 0 INZ(1500)
     DS2               S              5S 0 INZ(0)
     DS3               S             15S 5 INZ(0)
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
      *
     D TXT             S            100    DIM(10) PERRCD(1) CTDATA             _NOTXT
     D $TIMST          S               Z   INZ
     D $TIMEN          S               Z   INZ
     D $TIMMS          S             10  0
     D$MSG             S             52
      *
      * Main
     C                   CLEAR                   S5
      * Start time
     C                   TIME                    $TIMST
      * Loop
     C                   DO        100000
      * Miscellaneaus subroutine
     C                   EXSR      F_ADD
     C                   EXSR      F_CLEAR
     C                   EXSR      F_DIV
     C                   EXSR      F_EVAL
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
     C                   EVAL      $TIMMS=$TIMMS/1000
      * Display message
     C                   EVAL      $MSG=%trim(TXT(1))+' '+
     C                             %TRIM(%EDITC($TIMMS:'Q'))+'ms'
     C     $MSG          DSPLY     £PDSSU
      *
    MU* TIMEOUT(1000)
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Routine test ADD for small numeric varables
      *---------------------------------------------------------------------
     C     F_ADD         BEGSR
      *
     C                   CLEAR                   S2
     C                   CLEAR                   S3
     C                   ADD       1             S2
     C                   ADD       1             S3
     C                   ADD(H)    0             S3
      *
     C                   CLEAR                   S2
     C                   CLEAR                   S3
     C                   ADD       1,003         S2
     C                   ADD       1,003         S3
     C                   ADD       S1            S2
     C                   CLEAR                   S3
     C     S2            ADD       0,12345       S3
     C                   EVAL      S3=S2+0,12345
     C     S3            ADD       S4            S5
      *
     C                   CLEAR                   S2
     C                   CLEAR                   S3
     C                   CLEAR                   S5
     C                   EVAL      S2=S1+S2
     C                   EVAL      S3=S2+0,12345
     C                   EVAL      S5=S3+S4
      *
     C                   Z-ADD     1             S3
     C                   CLEAR                   S4
     C     -0,5          ADD       S3            S4
     C                   CLEAR                   S4
     C     0,5           ADD       S3            S4
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test CLEAR
      *---------------------------------------------------------------------
     C     F_CLEAR       BEGSR
     C                   CLEAR                   S1
     C                   CLEAR                   S2
     C                   CLEAR                   S3
     C                   CLEAR                   S4
     C                   CLEAR                   S5
     C                   CLEAR                   S6
     C                   CLEAR                   S9
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
     C     11            DIV       2             S2
     C                   DIV       2             S2
     C     11            DIV(H)    2             S2
     C                   DIV(H)    2             S2
     C                   EVAL      S2=11/2
     C                   EVAL(H)   S2=11/2
     C     11            DIV       2             S3
     C     11            DIV(H)    2             S3
     C                   EVAL      S3=11/2
     C                   EVAL(H)   S3=11/2
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test EVAL
      *---------------------------------------------------------------------
     C     F_EVAL        BEGSR
      *
     C                   EVAL      S9=9999999999
      *
     C                   EVAL      S5=*LOVAL
     C                   EVAL      S5=*HIVAL
     C                   EVAL      S6=*LOVAL
     C                   EVAL      S6=*HIVAL
     C                   EVAL      S8=*LOVAL
     C                   EVAL      S8=*HIVAL
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
     C     2             MULT      5             S2
     C                   MULT      5             S2
     C     2             MULT      -5            S2
     C                   MULT      -5            S2
     C                   MULT(H)   1             S2
     C     2             MULT      5,12345       S2
     C     2             MULT      -5,12345      S2
      *
     C     2             MULT      5,12345       S3
     C     2             MULT      -5,12345      S3
     C     2             MULT(H)   5,12345       S3
     C     2             MULT(H)   -5,12345      S3
      *
     C     2             MULT      5,98765       S6
     C     2             MULT      -5,98765      S6
     C     2             MULT(H)   5,98765       S6
     C     2             MULT(H)   -5,98765      S6
      *
     C                   EVAL      S2=2*5
     C                   EVAL      S2=2*(-5)
     C                   EVAL      S2=2*5,12345
     C                   EVAL      S2=2*(-5,12345)
     C                   EVAL      S3=2*5,12345
     C                   EVAL      S3=2*-5,12345
     C                   EVAL      S6=2*5,98765
     C                   EVAL      S6=2*-5,98765
     C                   EVAL(H)   S6=2*5,98765
     C                   EVAL(H)   S6=2*-5,98765
     C                   EVAL(H)   S3=2*5,12345
     C                   EVAL(H)   S3=2*-5,12345
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
     C     10            DIV       3             S2
     C                   MVR                     S3
     C     10            DIV       3             S5
     C                   MVR                     S6
     C     10            DIV       3             S5
     C                   MVR                     S9
      *
     C                   EVAL      S5=10
     C                   DIV       2             S5
     C                   MVR                     S6
      *
     C                   CLEAR                   S1
     C                   CLEAR                   S2
     C                   CLEAR                   S8
     C                   CLEAR                   S6
     C                   Z-ADD     44            S1
     C                   Z-ADD     6             S2
     C     S1            DIV       S2            S8
     C                   MVR                     S6
      *
     C                   CLEAR                   S1
     C                   CLEAR                   S2
     C                   CLEAR                   S3
     C                   CLEAR                   S6
     C                   Z-ADD     44            S1
     C                   Z-ADD     6             S2
     C     S1            DIV       S2            S3
     C                   MVR                     S6
      *
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
     C                   SUB       1             S2
     C                   SUB       1             S5
     C                   SUB(H)    0             S5
     C                   CLEAR                   S2
     C                   CLEAR                   S5
      *
     C                   SUB       S1            S2
     C     S2            SUB       0,12345       S3
     C     S3            SUB       S4            S5
      *
     C                   CLEAR                   S2
     C                   CLEAR                   S3
     C                   CLEAR                   S5
     C                   EVAL      S2=S2-S1
     C                   EVAL      S3=S2-0,12345
     C                   EVAL      S5=S3-S4
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test F_Z    ADD/SUB
      *---------------------------------------------------------------------
     C     F_Z           BEGSR
     C                   Z-ADD     44            S1
     C                   Z-ADD     S1            S2
     C                   Z-ADD     5,6           S2
     C                   Z-ADD(H)  5,6           S2
     C                   Z-ADD     5,6           S3
     C                   Z-ADD(H)  5,6           S3
     C                   Z-SUB     5             S2
      *
     C                   Z-ADD     123456        S8
     C                   Z-ADD     S8            S2
     C                   Z-ADD(H)  S8            S2
      *
     C                   Z-ADD     *ALL'9'       S2
      *
     C                   ENDSR
** TXT
Time spent
