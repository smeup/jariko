     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 25/02/20  V5R1    FP Test CAT
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
     C                   SETON                                        LR
      *---------------------------------------------------------------