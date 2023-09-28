   COP* *NOUI
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 12/03/20  001693  BMA Created
     V*=====================================================================
     V* OBIETTIVO
     V*  Test program for GOTO
     V*=====================================================================
     D Msg             S             10
     **********************************************************************
     C                   EVAL      Msg = 'Test OK'
     C                   EXSR      PIPPO
     C                   EVAL      Msg = 'Test KO'
     C     Fine          Tag
    MU* VAL1(Msg) VAL2('Test OK') COMP(EQ)
     C                   SETON                                        LR
     C     PIPPO         BEGSR
     C     Msg           DSPLY
     C                   GOTO      fine
     C                   ENDSR 