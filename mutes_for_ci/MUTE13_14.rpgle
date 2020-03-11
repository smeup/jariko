   COP* *NOUI
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 11/03/20          LOMFRA Created
     V*=====================================================================
     V* OBIETTIVO
     V*  Test program for CABxx
     V*
     V*=====================================================================
     D*----------------------------------------------------------------
     D Msg             S             10
     D AAA             S              3    INZ('AAA')
     D ZZZ             S              3    INZ('ZZZ')
     **********************************************************************
     C                   EVAL      Msg = 'Test OK'
     C     AAA           CAB       AAA           SHOW                 414243
     C                   EVAL      Msg = 'Test KO'
     **********************************************************************
     C     Msg           DSPLY
     C                   GOTO      fine
     **********************************************************************
     C     SHOW          TAG
     C     Msg           DSPLY
     C     Fine          Tag
    MU* VAL1(Msg) VAL2('Test OK') COMP(EQ)
    MU* VAL1(*IN41) VAL2(*OFF) COMP(EQ)
    MU* VAL1(*IN42) VAL2(*OFF) COMP(EQ)
    MU* VAL1(*IN43) VAL2(*ON)  COMP(EQ)
     **********************************************************************
     C     'Second'      DSPLY
     C     AAA           CAB       ZZZ           CHECK2               414243
    MU* FAIL('This code should not be executed')
     C                   EVAL      Msg = 'ERROR'
     C     CHECK2        TAG
    MU* VAL1(*IN41) VAL2(*OFF) COMP(EQ)
    MU* VAL1(*IN42) VAL2(*ON)  COMP(EQ)
    MU* VAL1(*IN43) VAL2(*OFF) COMP(EQ)
     **********************************************************************
     C     'Third'       DSPLY
     C     ZZZ           CAB       AAA           CHECK3               414243
    MU* FAIL('This code should not be executed')
     C                   EVAL      Msg = 'ERROR'
     C     CHECK3        TAG
    MU* VAL1(*IN41) VAL2(*ON)  COMP(EQ)
    MU* VAL1(*IN42) VAL2(*OFF) COMP(EQ)
    MU* VAL1(*IN43) VAL2(*OFF) COMP(EQ)
     **********************************************************************
     C     'Fourth'      DSPLY
     C     ZZZ           CABGT       AAA           CHECK4
    MU* FAIL('This code should not be executed')
     C                   EVAL      Msg = 'ERROR'
     C     CHECK4        TAG
    MU* VAL1(Msg) VAL2('Test OK') COMP(EQ)
     C     Msg           DSPLY
     **********************************************************************
     C     'Fifth'       DSPLY
     C     AAA           CABLT       ZZZ           CHECK5
    MU* FAIL('This code should not be executed')
     C                   EVAL      Msg = 'ERROR'
     C     CHECK5        TAG
    MU* VAL1(Msg) VAL2('Test OK') COMP(EQ)
     C     Msg           DSPLY
     **********************************************************************
     C     'Sixth'       DSPLY
     C     AAA           CABEQ       AAA           CHECK6
    MU* FAIL('This code should not be executed')
     C                   EVAL      Msg = 'ERROR'
     C     CHECK6        TAG
    MU* VAL1(Msg) VAL2('Test OK') COMP(EQ)
     C     Msg           DSPLY
     **********************************************************************
     C                   SETON                                        LR
