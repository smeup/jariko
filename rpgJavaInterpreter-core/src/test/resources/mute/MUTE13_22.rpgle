   COP* *NOUI
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 12/03/20          LOMFRN Created
     V*=====================================================================
     V* OBIETTIVO
     V*  Program to test SCAN Op Code
     V*
     V*=====================================================================
     D*----------------------------------------------------------------
     D Msg             S             10    Inz('aaaaa67AA0')
     D ResArr          S              2  0 DIM(10)
     D i               S              2  0 Inz(1)
     C     'aa'          SCAN      Msg:3         ResArr                   69
    MU* VAL1(*IN69) VAL2(*ON) COMP(EQ)
     C   69'69 on'       DSPLY
     C                   dow       ResArr(i) > 0 And i <= 10
     C     ResArr(i)     DSPLY
     C                   Eval      i += 1
     C                   Enddo
    MU* VAL1(ResArr(1)) VAL2(3) COMP(EQ)
    MU* VAL1(ResArr(2)) VAL2(4) COMP(EQ)
    MU* VAL1(ResArr(3)) VAL2(0) COMP(EQ)
     C                   Seton                                            LR
