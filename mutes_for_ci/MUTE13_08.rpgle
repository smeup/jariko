   COP* *NOUI
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 28/10/19  001221 BMA Creato
     V*=====================================================================
     V* OBIETTIVO
     V*  Programma finalizzato ai test sulla DIV
     V*
     V*=====================================================================
     DP2               S              5P 0
     DP3               S             15P 5
     DP4               S             15P 2
     D DSP             S             50
      * Variabili
     C                   EXSR      F_DIV
      *
    MU* Type="NOXMI"
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Routine test DIV
      *---------------------------------------------------------------------
     C     F_DIV         BEGSR
      *
    MU* VAL1(P2) VAL2(5) COMP(EQ)
     C     11            DIV       2             P2
     C                   EVAL      DSP=%CHAR(P3)
     C                   DSPLY                   DSP
    MU* VAL1(P2) VAL2(2) COMP(EQ)
     C                   DIV       2             P2
     C                   EVAL      DSP=%CHAR(P3)
     C                   DSPLY                   DSP
    MU* VAL1(P2) VAL2(6) COMP(EQ)
     C     11            DIV(H)    2             P2
     C                   EVAL      DSP=%CHAR(P3)
     C                   DSPLY                   DSP
    MU* VAL1(P2) VAL2(3) COMP(EQ)
     C                   DIV(H)    2             P2
     C                   EVAL      DSP=%CHAR(P3)
     C                   DSPLY                   DSP
    MU* VAL1(P3) VAL2(5,5) COMP(EQ)
     C     11            DIV       2             P3
     C                   EVAL      DSP=%CHAR(P3)
     C                   DSPLY                   DSP
    MU* VAL1(P3) VAL2(5,5) COMP(EQ)
     C     11            DIV(H)    2             P3
     C                   EVAL      DSP=%CHAR(P3)
     C                   DSPLY                   DSP
    MU* VAL1(P4) VAL2(6,26) COMP(EQ)
     C     12,53         DIV       2             P4
     C                   EVAL      DSP=%CHAR(P4)
     C                   DSPLY                   DSP
    MU* VAL1(P4) VAL2(6,27) COMP(EQ)
     C     12,53         DIV(H)    2             P4
     C                   EVAL      DSP=%CHAR(P4)
     C                   DSPLY                   DSP
    MU* VAL1(P3) VAL2(5,44782) COMP(EQ)
     C     12,53         DIV       2,3           P3
     C                   EVAL      DSP=%CHAR(P3)
     C                   DSPLY                   DSP
    MU* VAL1(P3) VAL2(5,44783) COMP(EQ)
     C     12,53         DIV(H)    2,3           P3
     C                   EVAL      DSP=%CHAR(P3)
     C                   DSPLY                   DSP
    MU* VAL1(P3) VAL2(-5,44782) COMP(EQ)
     C     12,53         DIV       -2,3          P3
     C                   EVAL      DSP=%CHAR(P3)
     C                   DSPLY                   DSP
    MU* VAL1(P3) VAL2(-5,44783) COMP(EQ)
     C     12,53         DIV(H)    -2,3          P3
     C                   EVAL      DSP=%CHAR(P3)
     C                   DSPLY                   DSP
    MU* VAL1(P3) VAL2(5,44782) COMP(EQ)
     C     -12,53        DIV       -2,3          P3
     C                   EVAL      DSP=%CHAR(P3)
     C                   DSPLY                   DSP
    MU* VAL1(P3) VAL2(5,44783) COMP(EQ)
     C     -12,53        DIV(H)    -2,3          P3
     C                   EVAL      DSP=%CHAR(P3)
     C                   DSPLY                   DSP
    MU* VAL1(P3) VAL2(0) COMP(EQ)
     C     0             DIV(H)    -2,3          P3
     C                   EVAL      DSP=%CHAR(P3)
     C                   DSPLY                   DSP
      *
     C*                   EVAL      P2=0
     C*     12,53         DIV(H)    P2            P3
     C*                   EVAL      DSP=%CHAR(P3)
     C*                   DSPLY                   DSP
      *
     C                   ENDSR
