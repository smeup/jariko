     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 07/01/20  001432  BERNI  Creato
     V* 08/01/20  001432  BMA    Ricompilato
     V* 09/01/20  V5R1    BMA    Check-out 001432 in SMEDEV
     V* 13/01/20  001443  BERNI  Correzione esempi
     V* 13/01/20  V5R1    BMA    Check-out 001443 in SMEDEV
     V*=====================================================================
     D*  Pgm testing performance with array
     V*---------------------------------------------------------------------
     D ARRA1           S             20    DIM(10)                              Array 1
     D ARRA2           S             10    DIM(10)                              Array 2
     D $N              S              2  0
     D XXSTR           S             10
     D YYSTR           S             10
     D ZZSTR           S             20
     D $TIMST          S               Z   INZ
     D $TIMEN          S               Z   INZ
     D $TIMMS          S             10I 0
     D $MSG            S             52
      * Start time
     C                   TIME                    $TIMST
      * String to array
     C                   EXSR      ST2AR
      * Array to string
     C                   EXSR      AR2ST
      *
      * End time
     C                   TIME                    $TIMEN
      * Elapsed time
     C     $TIMEN        SUBDUR    $TIMST        $TIMMS:*MS
     C                   EVAL      $TIMMS=$TIMMS/1000
      * Display message
     C                   EVAL      $MSG=%TRIM(%EDITC($TIMMS:'Q'))+'ms'
     C     $MSG          DSPLY     £PDSSU
      *
     C                   SETON                                        LR
      *
      *---------------------------------------------------------------------
    RD* Routine test String to Array
      *---------------------------------------------------------------------
     C     ST2AR         BEGSR
      *
     C                   CLEAR                   XXSTR
     C                   CLEAR                   YYSTR
     C                   CLEAR                   ZZSTR
     C                   CLEAR                   $N
      * Set XXSTR , YYSTR, ZZSTR Variables
    MU* VAL1(XXSTR) VAL2('ABCDEFGHIL') COMP(EQ)
     C                   EVAL      XXSTR='ABCDEFGHIL'
    MU* VAL1(YYSTR) VAL2('MNOPQRSTOV') COMP(EQ)
     C                   EVAL      YYSTR='MNOPQRSTOV'
    MU* VAL1(ZZSTR) VAL2('ABCDEFGHILMNOPQRSTOV') COMP(EQ)
     C                   EVAL      ZZSTR=XXSTR+YYSTR
      * Use of 'MOVEL(P)
    MU* VAL1(ARRA1(1)) VAL2('ABCDEFGHIL          ') COMP(EQ)
     C                   MOVEL(P)  XXSTR         ARRA1(1)
     C                   EVAL      $MSG=%TRIMR(ARRA1(1))
     C     $MSG          DSPLY     £PDSSU
      * Use of 'EVAL'
    MU* VAL1(ARRA1(2)) VAL2('MNOPQRSTOV          ') COMP(EQ)
     C                   EVAL      ARRA1(2)=YYSTR
     C                   EVAL      $MSG=%TRIMR(ARRA1(2))
     C     $MSG          DSPLY     £PDSSU
      *
    MU* VAL1(ARRA1(3)) VAL2('ABCDEFGHILMNOPQRSTOV') COMP(EQ)
     C                   EVAL      ARRA1(3)=ZZSTR
     C                   EVAL      $MSG=%TRIMR(ARRA1(3))
     C     $MSG          DSPLY     £PDSSU
      * Use of 'MOVEL'
    MU* VAL1(ARRA1(1)) VAL2('ABCDEFGHIL          ') COMP(EQ)
    MU* VAL1(ARRA1(3)) VAL2('MNOPQRSTOVMNOPQRSTOV') COMP(EQ)
     C                   MOVEL     YYSTR         ARRA1(3)
     C                   EVAL      $MSG=%TRIMR(ARRA1(3))
     C     $MSG          DSPLY     £PDSSU
    MU* VAL1(ARRA1(01)) VAL2('ABCDEFGHILMNOPQRSTOV') COMP(EQ)
    MU* VAL1(ARRA1(02)) VAL2('ABCDEFGHILMNOPQRSTOV') COMP(EQ)
    MU* VAL1(ARRA1(03)) VAL2('ABCDEFGHILMNOPQRSTOV') COMP(EQ)
    MU* VAL1(ARRA1(04)) VAL2('ABCDEFGHILMNOPQRSTOV') COMP(EQ)
    MU* VAL1(ARRA1(05)) VAL2('ABCDEFGHILMNOPQRSTOV') COMP(EQ)
    MU* VAL1(ARRA1(06)) VAL2('ABCDEFGHILMNOPQRSTOV') COMP(EQ)
    MU* VAL1(ARRA1(07)) VAL2('ABCDEFGHILMNOPQRSTOV') COMP(EQ)
    MU* VAL1(ARRA1(08)) VAL2('ABCDEFGHILMNOPQRSTOV') COMP(EQ)
    MU* VAL1(ARRA1(09)) VAL2('ABCDEFGHILMNOPQRSTOV') COMP(EQ)
    MU* VAL1(ARRA1(10)) VAL2('ABCDEFGHILMNOPQRSTOV') COMP(EQ)
     C                   EVAL      ARRA1=ZZSTR
     C                   EVAL      $MSG=%TRIMR(ARRA1(10))
     C     $MSG          DSPLY     £PDSSU
      * Use of 'MOVEA'
    MU* VAL1(ARRA1(01)) VAL2('MNOPQRSTOVMNOPQRSTOV') COMP(EQ)
    MU* VAL1(ARRA1(02)) VAL2('ABCDEFGHILMNOPQRSTOV') COMP(EQ)
    MU* VAL1(ARRA1(03)) VAL2('ABCDEFGHILMNOPQRSTOV') COMP(EQ)
    MU* VAL1(ARRA1(04)) VAL2('ABCDEFGHILMNOPQRSTOV') COMP(EQ)
    MU* VAL1(ARRA1(05)) VAL2('ABCDEFGHILMNOPQRSTOV') COMP(EQ)
    MU* VAL1(ARRA1(06)) VAL2('ABCDEFGHILMNOPQRSTOV') COMP(EQ)
    MU* VAL1(ARRA1(07)) VAL2('ABCDEFGHILMNOPQRSTOV') COMP(EQ)
    MU* VAL1(ARRA1(08)) VAL2('ABCDEFGHILMNOPQRSTOV') COMP(EQ)
    MU* VAL1(ARRA1(09)) VAL2('ABCDEFGHILMNOPQRSTOV') COMP(EQ)
    MU* VAL1(ARRA1(10)) VAL2('ABCDEFGHILMNOPQRSTOV') COMP(EQ)
     C                   MOVEA     YYSTR         ARRA1
     C                   EVAL      $MSG=%TRIMR(ARRA1(1))+' '
     C                                 +%TRIMR(ARRA1(2))+' '
     C                                 +%TRIMR(ARRA1(3))+' '
     C                                 +%TRIMR(ARRA1(4))+' '
     C     $MSG          DSPLY     £PDSSU
      * Use of 'MOVEA(P)
    MU* VAL1(ARRA1(01)) VAL2('ABCDEFGHIL          ') COMP(EQ)
    MU* VAL1(ARRA1(02)) VAL2('                    ') COMP(EQ)
    MU* VAL1(ARRA1(03)) VAL2('                    ') COMP(EQ)
    MU* VAL1(ARRA1(04)) VAL2('                    ') COMP(EQ)
    MU* VAL1(ARRA1(05)) VAL2('                    ') COMP(EQ)
    MU* VAL1(ARRA1(06)) VAL2('                    ') COMP(EQ)
    MU* VAL1(ARRA1(07)) VAL2('                    ') COMP(EQ)
    MU* VAL1(ARRA1(08)) VAL2('                    ') COMP(EQ)
    MU* VAL1(ARRA1(09)) VAL2('                    ') COMP(EQ)
    MU* VAL1(ARRA1(10)) VAL2('                    ') COMP(EQ)
     C                   MOVEA(P)  XXSTR         ARRA1
     C                   EVAL      $MSG=%TRIMR(ARRA1(1))+' '
     C                                 +%TRIMR(ARRA1(2))+' '
     C                                 +%TRIMR(ARRA1(3))+' '
     C                                 +%TRIMR(ARRA1(4))+' '
     C     $MSG          DSPLY     £PDSSU
    MU* VAL1(ARRA2(01)) VAL2('ABCDEFGHIL') COMP(EQ)
    MU* VAL1(ARRA2(02)) VAL2('ABCDEFGHIL') COMP(EQ)
    MU* VAL1(ARRA2(03)) VAL2('ABCDEFGHIL') COMP(EQ)
    MU* VAL1(ARRA2(04)) VAL2('ABCDEFGHIL') COMP(EQ)
    MU* VAL1(ARRA2(05)) VAL2('ABCDEFGHIL') COMP(EQ)
    MU* VAL1(ARRA2(06)) VAL2('ABCDEFGHIL') COMP(EQ)
    MU* VAL1(ARRA2(07)) VAL2('ABCDEFGHIL') COMP(EQ)
    MU* VAL1(ARRA2(08)) VAL2('ABCDEFGHIL') COMP(EQ)
    MU* VAL1(ARRA2(09)) VAL2('ABCDEFGHIL') COMP(EQ)
    MU* VAL1(ARRA2(10)) VAL2('ABCDEFGHIL') COMP(EQ)
     C                   EVAL      ARRA2=ZZSTR
     C                   EVAL      $MSG=%TRIMR(ARRA2(10))
     C     $MSG          DSPLY     £PDSSU
      * Use of 'MOVEA'
    MU* VAL1(ARRA2(1)) VAL2('ABCDEFGHIL') COMP(EQ)
    MU* VAL1(ARRA2(2)) VAL2('MNOPQRSTOV') COMP(EQ)
    MU* VAL1(ARRA2(3)) VAL2('ABCDEFGHIL') COMP(EQ)
    MU* VAL1(ARRA2(4)) VAL2('ABCDEFGHIL') COMP(EQ)
    MU* VAL1(ARRA2(5)) VAL2('ABCDEFGHIL') COMP(EQ)
    MU* VAL1(ARRA2(6)) VAL2('ABCDEFGHIL') COMP(EQ)
    MU* VAL1(ARRA2(7)) VAL2('ABCDEFGHIL') COMP(EQ)
    MU* VAL1(ARRA2(8)) VAL2('ABCDEFGHIL') COMP(EQ)
    MU* VAL1(ARRA2(9)) VAL2('ABCDEFGHIL') COMP(EQ)
    MU* VAL1(ARRA2(10)) VAL2('ABCDEFGHIL') COMP(EQ)
     C                   MOVEA     ZZSTR         ARRA2
     C                   EVAL      $MSG=%TRIMR(ARRA2(1))+' '
     C                                 +%TRIMR(ARRA2(2))+' '
     C                                 +%TRIMR(ARRA2(3))+' '
     C                                 +%TRIMR(ARRA2(4))+' '
     C     $MSG          DSPLY     £PDSSU
      * Use of 'MOVEA(P)'
    MU* VAL1(ARRA2(1)) VAL2('ABCDEFGHIL') COMP(EQ)
    MU* VAL1(ARRA2(2)) VAL2('MNOPQRSTOV') COMP(EQ)
    MU* VAL1(ARRA2(3)) VAL2('          ') COMP(EQ)
    MU* VAL1(ARRA2(4)) VAL2('          ') COMP(EQ)
    MU* VAL1(ARRA2(5)) VAL2('          ') COMP(EQ)
    MU* VAL1(ARRA2(6)) VAL2('          ') COMP(EQ)
    MU* VAL1(ARRA2(7)) VAL2('          ') COMP(EQ)
    MU* VAL1(ARRA2(8)) VAL2('          ') COMP(EQ)
    MU* VAL1(ARRA2(9)) VAL2('          ') COMP(EQ)
    MU* VAL1(ARRA2(10)) VAL2('          ') COMP(EQ)
     C                   MOVEA(P)  ZZSTR         ARRA2
     C                   EVAL      $MSG=%TRIMR(ARRA2(1))+' '
     C                                 +%TRIMR(ARRA2(2))+' '
     C                                 +%TRIMR(ARRA2(3))+' '
     C                                 +%TRIMR(ARRA2(4))+' '
     C     $MSG          DSPLY     £PDSSU
      * Loop
     C                   EVAL      $N=3
      *
     C                   DO        *HIVAL
      * Check for Array's limit
     C                   EVAL      $N=$N+1
     C                   IF        $N=11
     C                   LEAVE
     C                   ENDIF
      *
     C                   EVAL      ARRA1($N)='  '+ZZSTR
     C                   ENDDO
      *
    MU* VAL1(ARRA1(5)) VAL2('  ABCDEFGHILMNOPQRST') COMP(EQ)
     C                   EVAL      $MSG=ARRA1(5)
     C     $MSG          DSPLY     £PDSSU
      *
     C                   ENDSR
      *---------------------------------------------------------------------
    RD* Routine test Array to String
      *---------------------------------------------------------------------
     C     AR2ST         BEGSR
      *
     C                   CLEAR                   XXSTR
     C                   CLEAR                   YYSTR
     C                   CLEAR                   ZZSTR
     C                   CLEAR                   $N
    MU* VAL1(ARRA1(3)) VAL2('AAAAAAAAAAAAAAAAAAAA')        COMP(EQ)
     C                   EVAL      ARRA1(3)=*ALL'A'
      * Use of 'MOVEL'
    MU* VAL1(XXSTR) VAL2('AAAAAAAAAA') COMP(EQ)
     C                   MOVEL(P)  ARRA1(3)      XXSTR
     C                   EVAL      $MSG=%TRIMR(XXSTR)
     C     $MSG          DSPLY     £PDSSU
    MU* VAL1(ARRA1(2)) VAL2('BBBBBBBBBBBBBBBBBBBB')        COMP(EQ)
     C                   EVAL      ARRA1(2)=*ALL'B'
      * Use of EVAL
    MU* VAL1(YYSTR) VAL2('BBBBBBBBBB') COMP(EQ)
     C                   EVAL      YYSTR=ARRA1(2)
     C                   EVAL      $MSG=%TRIMR(YYSTR)
     C     $MSG          DSPLY     £PDSSU
     C                   EVAL      ARRA2=*ALL'X'
     C                   EVAL      ARRA2(3)=*ALL'C'
      * Use of 'MOVEL'
    MU* VAL1(ZZSTR) VAL2('CCCCCCCCCC          ') COMP(EQ)
     C                   MOVEL(P)  ARRA2(3)      ZZSTR
     C                   EVAL      $MSG=%TRIMR(ZZSTR)
     C     $MSG          DSPLY     £PDSSU
     C                   EVAL      ARRA2(2)=*ALL'D'
      * Use of EVAL
    MU* VAL1(ZZSTR) VAL2('DDDDDDDDDD          ') COMP(EQ)
     C                   EVAL      ZZSTR=ARRA2(2)
     C                   EVAL      $MSG=%TRIMR(ZZSTR)
     C     $MSG          DSPLY     £PDSSU
     C                   EVAL      ARRA2=*ALL'X'
     C                   EVAL      ZZSTR=*ALL'Y'
      * Use of 'MOVEL'
    MU* VAL1(ZZSTR) VAL2('XXXXXXXXXXYYYYYYYYYY') COMP(EQ)
     C                   MOVEL     ARRA2(2)      ZZSTR
     C                   EVAL      $MSG=%TRIMR(ZZSTR)
     C     $MSG          DSPLY     £PDSSU
     C                   EVAL      ARRA1(2)=*ALL'B'
      * Use of MOVEA
    MU* VAL1(ARRA2(1)) VAL2('ABCDEFGHIL') COMP(EQ)
    MU* VAL1(ARRA2(2)) VAL2('          ') COMP(EQ)
    MU* VAL1(ARRA2(3)) VAL2('BBBBBBBBBB') COMP(EQ)
    MU* VAL1(ARRA2(4)) VAL2('BBBBBBBBBB') COMP(EQ)
    MU* VAL1(ARRA2(5)) VAL2('AAAAAAAAAA') COMP(EQ)
    MU* VAL1(ARRA2(6)) VAL2('AAAAAAAAAA') COMP(EQ)
    MU* VAL1(ARRA2(7)) VAL2('  ABCDEFGH') COMP(EQ)
    MU* VAL1(ARRA2(8)) VAL2('ILMNOPQRST') COMP(EQ)
    MU* VAL1(ARRA2(9)) VAL2('  ABCDEFGH') COMP(EQ)
    MU* VAL1(ARRA2(10)) VAL2('ILMNOPQRST') COMP(EQ)
     C                   MOVEA     ARRA1         ARRA2
     C                   EVAL      $MSG=%TRIMR(ARRA2(1))+' '
     C                                 +%TRIMR(ARRA2(2))+' '
     C                                 +%TRIMR(ARRA2(3))+' '
     C                                 +%TRIMR(ARRA2(4))+' '
     C     $MSG          DSPLY     £PDSSU
    MU* VAL1(ARRA1(1)) VAL2('XXXXXXXXXXXXXXXXXXXX')        COMP(EQ)
    MU* VAL1(ARRA1(2)) VAL2('XXXXXXXXXXXXXXXXXXXX')        COMP(EQ)
    MU* VAL1(ARRA1(3)) VAL2('XXXXXXXXXXXXXXXXXXXX')        COMP(EQ)
    MU* VAL1(ARRA1(4)) VAL2('XXXXXXXXXXXXXXXXXXXX')        COMP(EQ)
    MU* VAL1(ARRA1(5)) VAL2('XXXXXXXXXXXXXXXXXXXX')        COMP(EQ)
    MU* VAL1(ARRA1(6)) VAL2('XXXXXXXXXXXXXXXXXXXX')        COMP(EQ)
    MU* VAL1(ARRA1(7)) VAL2('XXXXXXXXXXXXXXXXXXXX')        COMP(EQ)
    MU* VAL1(ARRA1(8)) VAL2('XXXXXXXXXXXXXXXXXXXX')        COMP(EQ)
    MU* VAL1(ARRA1(9)) VAL2('XXXXXXXXXXXXXXXXXXXX')        COMP(EQ)
    MU* VAL1(ARRA1(10)) VAL2('XXXXXXXXXXXXXXXXXXXX')        COMP(EQ)
     C                   EVAL      ARRA1=*ALL'X'
      * Use of MOVEA
    MU* VAL1(ARRA1(1)) VAL2('ABCDEFGHIL          ')        COMP(EQ)
    MU* VAL1(ARRA1(2)) VAL2('BBBBBBBBBBBBBBBBBBBB')        COMP(EQ)
    MU* VAL1(ARRA1(3)) VAL2('AAAAAAAAAAAAAAAAAAAA')        COMP(EQ)
    MU* VAL1(ARRA1(4)) VAL2('  ABCDEFGHILMNOPQRST')        COMP(EQ)
    MU* VAL1(ARRA1(5)) VAL2('  ABCDEFGHILMNOPQRST')        COMP(EQ)
    MU* VAL1(ARRA1(6)) VAL2('XXXXXXXXXXXXXXXXXXXX')        COMP(EQ)
    MU* VAL1(ARRA1(7)) VAL2('XXXXXXXXXXXXXXXXXXXX')        COMP(EQ)
    MU* VAL1(ARRA1(8)) VAL2('XXXXXXXXXXXXXXXXXXXX')        COMP(EQ)
    MU* VAL1(ARRA1(9)) VAL2('XXXXXXXXXXXXXXXXXXXX')        COMP(EQ)
    MU* VAL1(ARRA1(10)) VAL2('XXXXXXXXXXXXXXXXXXXX')        COMP(EQ)
     C                   MOVEA     ARRA2         ARRA1
     C                   EVAL      $MSG=%TRIMR(ARRA1(1))+' '
     C                                 +%TRIMR(ARRA1(2))+' '
     C                                 +%TRIMR(ARRA1(3))+' '
     C                                 +%TRIMR(ARRA1(4))+' '
     C     $MSG          DSPLY     £PDSSU
     C                   EVAL      ARRA1=*ALL'Y'
      * Use of MOVEA(P)
    MU* VAL1(ARRA1(1)) VAL2('ABCDEFGHIL          ')        COMP(EQ)
    MU* VAL1(ARRA1(2)) VAL2('BBBBBBBBBBBBBBBBBBBB')        COMP(EQ)
    MU* VAL1(ARRA1(3)) VAL2('AAAAAAAAAAAAAAAAAAAA')        COMP(EQ)
    MU* VAL1(ARRA1(4)) VAL2('  ABCDEFGHILMNOPQRST')        COMP(EQ)
    MU* VAL1(ARRA1(5)) VAL2('  ABCDEFGHILMNOPQRST')        COMP(EQ)
    MU* VAL1(ARRA1(6)) VAL2('                    ')        COMP(EQ)
    MU* VAL1(ARRA1(7)) VAL2('                    ')        COMP(EQ)
    MU* VAL1(ARRA1(8)) VAL2('                    ')        COMP(EQ)
    MU* VAL1(ARRA1(9)) VAL2('                    ')        COMP(EQ)
    MU* VAL1(ARRA1(10)) VAL2('                    ')        COMP(EQ)
     C                   MOVEA(P)  ARRA2         ARRA1
     C                   EVAL      $MSG=%TRIMR(ARRA1(1))+' '
     C                                 +%TRIMR(ARRA1(2))+' '
     C                                 +%TRIMR(ARRA1(3))+' '
     C                                 +%TRIMR(ARRA1(4))+' '
     C     $MSG          DSPLY     £PDSSU
      *
     C                   ENDSR
