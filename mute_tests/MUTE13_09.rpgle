   COP* *NOUI
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 28/10/19  001221  BMA   Creazione
     V*=====================================================================
     V* OBIETTIVO
     V*  Programma finalizzato ai test sulla ADD
     V*
     V*=====================================================================
     D*----------------------------------------------------------------
     D AAA005          S              5
     D AAA010          S             10
     D DSP             S             50
      *
     D  N1             S              9  0
     D  N2             S             21  6
      *
    MU* VAL1(N1) VAL2(10) COMP(EQ)
     C                   EVAL      N1=10
     C                   EVAL      DSP=%CHAR(N1)
     C                   DSPLY                   DSP
    MU* VAL1(N1) VAL2(15) COMP(EQ)
     C                   ADD       5             N1
     C                   EVAL      DSP='ADD       5             N1'
     C                   DSPLY                   DSP
     C                   EVAL      DSP=%CHAR(N1)
     C                   DSPLY                   DSP
    MU* VAL1(N2) VAL2(34,78) COMP(EQ)
     C                   EVAL      N2=34,78
     C                   EVAL      DSP=%CHAR(N2)
     C                   DSPLY                   DSP
    MU* VAL1(N2) VAL2(40,703456) COMP(EQ)
     C                   ADD       5,923456      N2
     C                   EVAL      DSP='ADD       5,923456      N2'
     C                   DSPLY                   DSP
     C                   EVAL      DSP=%CHAR(N2)
     C                   DSPLY                   DSP
      *
    MU* VAL1(N1) VAL2(34) COMP(EQ)
     C                   EVAL      N1=34,78
     C                   EVAL      DSP=%CHAR(N1)
     C                   DSPLY                   DSP
    MU* VAL1(N1) VAL2(39) COMP(EQ)
     C                   ADD       5,923456      N1
     C                   EVAL      DSP='ADD       5,923456      N1'
     C                   DSPLY                   DSP
     C                   EVAL      DSP=%CHAR(N1)
     C                   DSPLY                   DSP
    MU* VAL1(N1) VAL2(10) COMP(EQ)
     C                   EVAL      N1=10
     C                   EVAL      DSP=%CHAR(N1)
     C                   DSPLY                   DSP
    MU* VAL1(N1) VAL2(5) COMP(EQ)
     C                   ADD       -5            N1
     C                   EVAL      DSP='ADD       -5            N1'
     C                   DSPLY                   DSP
     C                   EVAL      DSP=%CHAR(N1)
     C                   DSPLY                   DSP
    MU* VAL1(N2) VAL2(34,78) COMP(EQ)
     C                   EVAL      N2=34,78
     C                   EVAL      DSP=%CHAR(N2)
     C                   DSPLY                   DSP
    MU* VAL1(N2) VAL2(28,856544) COMP(EQ)
     C                   ADD       -5,923456     N2
     C                   EVAL      DSP='ADD       -5,923456     N2'
     C                   DSPLY                   DSP
     C                   EVAL      DSP=%CHAR(N2)
     C                   DSPLY                   DSP
    MU* VAL1(N1) VAL2(34) COMP(EQ)
     C                   EVAL      N1=34,78
     C                   EVAL      DSP=%CHAR(N1)
     C                   DSPLY                   DSP
    MU* VAL1(N1) VAL2(28) COMP(EQ)
     C                   ADD       -5,923456     N1
     C                   EVAL      DSP='ADD       -5,923456     N1'
     C                   DSPLY                   DSP
     C                   EVAL      DSP=%CHAR(N1)
     C                   DSPLY                   DSP
      *
     C                   SETON                                        LR
