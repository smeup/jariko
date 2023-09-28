     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 26/02/20  V5R1    FP Creato
     V* 26/02/20  001608  FP Modifiche
     V* 26/02/20  V5R1    BERNI Check-out 001608 in SMEDEV
     V* 13/03/20  001689  BMA Renamed MUTE16_01 into MUTE13_17
     V* 13/03/20  V5R1    BERNI Check-out 001689 in SMEDEV
     V* 29/03/21  002768  BERNI Aggiunti casi di test
     V* 29/03/21  V5R1    BMA   Check-out 002768 in SMEDEV
     V*=====================================================================
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
     C                   CLEAR                   FACTOR1           6
     C                   CLEAR                   FACTOR2          10
     C                   CLEAR                   RESULT           20
     C                   MOVEL(P)  'HELLO '      FACTOR1
     C                   MOVEL(P)  'WORLD!    '  FACTOR2
     C                   MOVEL(P)  *ALL'X'       RESULT
    MU* VAL1(RESULT) VAL2('HELLO WORLD!    XXXX') COMP(EQ)
     C     FACTOR1       CAT       FACTOR2       RESULT
     C                   DSPLY                   RESULT
      *
     C                   CLEAR                   FACTOR1           6
     C                   CLEAR                   FACTOR2          10
     C                   CLEAR                   RESULT           20
     C                   MOVEL(P)  'HELLO '      FACTOR1
     C                   MOVEL(P)  'WORLD!    '  FACTOR2
     C                   MOVEL(P)  *ALL'X'       RESULT
    MU* VAL1(RESULT) VAL2('HELLO   WORLD!    XX') COMP(EQ)
     C     FACTOR1       CAT       FACTOR2:3     RESULT
     C                   DSPLY                   RESULT
      *
     C                   CLEAR                   FACTOR2          10
     C                   CLEAR                   RESULT           20
     C                   MOVEL(P)  'HELLO '      FACTOR2
     C                   MOVEL(P)  *ALL'X'       RESULT
    MU* VAL1(RESULT) VAL2('XXXXXXXXXXXXXXXXXXXX') COMP(EQ)
     C                   CAT       FACTOR2       RESULT
     C                   DSPLY                   RESULT
      *
     C                   CLEAR                   FACTOR2          10
     C                   CLEAR                   RESULT           20
     C                   MOVEL(P)  'HELLO '      FACTOR2
     C                   MOVEL(P)  *ALL'X'       RESULT
    MU* VAL1(RESULT) VAL2('XXXXXXXXXXXXXXXXXXXX') COMP(EQ)
     C                   CAT       FACTOR2:3     RESULT
     C                   DSPLY                   RESULT
      *
     C                   CLEAR                   FACTOR2          10
     C                   CLEAR                   RESULT           20
     C                   MOVEL(P)  'HELLO '      FACTOR2
     C                   CLEAR                   RESULT
    MU* VAL1(RESULT) VAL2('                    ') COMP(EQ)
     C                   CAT       FACTOR2       RESULT
     C                   DSPLY                   RESULT
      *
     C                   CLEAR                   FACTOR2          10
     C                   CLEAR                   RESULT           20
     C                   MOVEL(P)  'HELLO '      FACTOR2
     C                   CLEAR                   RESULT
    MU* VAL1(RESULT) VAL2('   HELLO            ') COMP(EQ)
     C                   CAT       FACTOR2:3     RESULT
     C                   DSPLY                   RESULT
      *
     C                   CLEAR                   FACTOR2          10
     C                   CLEAR                   RESULT           20
     C                   MOVEL(P)  'HELLO '      FACTOR2
     C                   MOVEL(P)  *ALL'X'       RESULT
    MU* VAL1(RESULT) VAL2('XXXXXXXXXXXXXXXXXXXX') COMP(EQ)
     C                   CAT       FACTOR2:3     RESULT
     C                   DSPLY                   RESULT
      *
     C                   CLEAR                   FACTOR2          10
     C                   CLEAR                   RESULT           20
     C                   MOVEL(P)  'HELLO '      FACTOR2
     C                   MOVEL(P)  '012       '  RESULT
     C                   MOVE      '0123456789'  RESULT
    MU* VAL1(RESULT) VAL2('012       0123456789') COMP(EQ)
     C                   CAT       FACTOR2:3     RESULT
     C                   DSPLY                   RESULT
      *
     C                   CLEAR                   FACTOR2          10
     C                   CLEAR                   RESULT           20
     C                   MOVEL(P)  'HELLO '      FACTOR2
     C                   CLEAR                   RESULT
    MU* VAL1(RESULT) VAL2('                    ') COMP(EQ)
     C                   CAT       FACTOR2:25    RESULT
     C                   DSPLY                   RESULT
      *
     C                   CLEAR                   FACTOR2          10
     C                   CLEAR                   SHORT_RESULT      4
     C                   MOVEL(P)  'HELLO '      FACTOR2
     C                   CLEAR                   SHORT_RESULT
    MU* VAL1(SHORT_RESULT) VAL2('    ') COMP(EQ)
     C                   CAT       FACTOR2       SHORT_RESULT
     C                   DSPLY                   SHORT_RESULT
      *
     C                   CLEAR                   FACTOR2          10
     C                   CLEAR                   SHORT_RESULT      4
     C                   MOVEL(P)  'HELLO '      FACTOR2
     C                   CLEAR                   SHORT_RESULT
    MU* VAL1(SHORT_RESULT) VAL2('  HE') COMP(EQ)
     C                   CAT       FACTOR2:2     SHORT_RESULT
     C                   DSPLY                   SHORT_RESULT
      *
     C                   CLEAR                   FACTOR1           6
     C                   CLEAR                   FACTOR2          10
     C                   CLEAR                   SHORT_RESULT      4
     C                   MOVEL(P)  'HELLO '      FACTOR1
     C                   MOVEL(P)  'WORLD! '     FACTOR2
     C                   CLEAR                   SHORT_RESULT      4
    MU* VAL1(SHORT_RESULT) VAL2('HELL') COMP(EQ)
     C     FACTOR1       CAT       FACTOR2       SHORT_RESULT
     C                   DSPLY                   SHORT_RESULT
      *
     C                   CLEAR                   FACTOR1           6
     C                   CLEAR                   FACTOR2          10
     C                   CLEAR                   SHORT_RESULT      4
     C                   MOVEL(P)  'HELLO '      FACTOR1
     C                   MOVEL(P)  'WORLD! '     FACTOR2
     C                   CLEAR                   SHORT_RESULT
    MU* VAL1(SHORT_RESULT) VAL2('HELL') COMP(EQ)
     C     FACTOR1       CAT       FACTOR2:3     SHORT_RESULT
     C                   DSPLY                   SHORT_RESULT
      *
      * CAT with Factor1 and Result same variable
     C                   CLEAR                   RESULT           20
     C     RESULT        CAT       '012345':0    RESULT
    MU* VAL1(RESULT) VAL2('0123456789          ') COMP(EQ)
     C     RESULT        CAT       '6789  ':0    RESULT
     C                   DSPLY                   RESULT
      *
      * CAT with Factor1 and Result same variable
     C                   CLEAR                   LONG_RESULT      36
     C     LONG_RESULT   CAT       'ABCDEF':0    LONG_RESULT
     C     LONG_RESULT   CAT       'GHIJKL':0    LONG_RESULT
     C     LONG_RESULT   CAT       'MNOPQR':0    LONG_RESULT
     C     LONG_RESULT   CAT       'STUVWX':0    LONG_RESULT
     C     LONG_RESULT   CAT       'YZ0123':0    LONG_RESULT
    MU* VAL1(LONG_RESULT) VAL2('ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789') COMP(EQ)
     C     LONG_RESULT   CAT       '456789':0    LONG_RESULT
     C                   DSPLY                   LONG_RESULT
     C                   SETON                                        LR
      *---------------------------------------------------------------
