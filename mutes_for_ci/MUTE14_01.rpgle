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
    MU* VAL1(*IN42) VAL2(*OFF)  COMP(EQ)
    MU* VAL1(*IN43) VAL2(*ON)  COMP(EQ)
     **********************************************************************

     C                   SETON                                        LR
