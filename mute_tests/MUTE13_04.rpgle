   COP* *NOUI
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 04/10/19  001166  BERNI Creazione
     V* 16/10/19  001166  BERNI Ricompilato
     V* 16/10/19  V5R1    BMA   Check-out 001166 in SMEDEV
     V*=====================================================================
     V* OBIETTIVO
     V*  Programma finalizzato ai test sulla MOVEL
     V*
     V*=====================================================================
     D*----------------------------------------------------------------
     D AAA005          S              5
     D AAA006          S              6
     D AAA003          S              3
     D AAA010          S             10
     D DSP             S             50
      *
     D  N1             S              9  0
     D  N2             S             21  6
      *
    MU* VAL1(AAA010) VAL2('       AAA') COMP(EQ)
     C***                MOVE      'AAA'         AAA010
     C                   EVAL      AAA003='AAA'
     C                   MOVE      AAA005        AAA010
     C                   EVAL      DSP=AAA010
     C                   DSPLY                   DSP
    MU* VAL1(AAA010) VAL2('AAA       ') COMP(EQ)
     C                   EVAL      AAA003='AAA'
     C                   MOVEL     AAA003        AAA010
     C                   EVAL      DSP=AAA010
     C                   DSPLY                   DSP
    MU* VAL1(AAA010) VAL2('  AAA     ') COMP(EQ)
     C                   EVAL      AAA006='  AAA'
     C                   MOVEL     AAA006        AAA010
     C                   EVAL      DSP=AAA010
     C                   DSPLY                   DSP
    MU* VAL1(N1) VAL2(912000000) COMP(EQ)
     C                   CLEAR                   N1
     C                   MOVEL     912           N1
     C                   EVAL      DSP=%CHAR(N1)
     C                   DSPLY                   DSP
    MU* VAL1(AAA010) VAL2('ABCDE     ') COMP(EQ)
     C                   CLEAR                   AAA010
     C                   MOVEL     'ABCDE'       AAA005
     C                   MOVEL     AAA005        AAA010
     C                   EVAL      DSP=AAA010
     C                   DSPLY                   DSP
    MU* VAL1(AAA005) VAL2('RSTUV') COMP(EQ)
     C                   CLEAR                   AAA010
     C                   EVAL      AAA010='RSTUVWXYZ '
     C                   MOVEL     AAA010        AAA005
     C                   EVAL      DSP=AAA005
     C                   DSPLY                   DSP
    MU* VAL1(AAA010) VAL2('ABCDE     ') COMP(EQ)
     C                   EVAL      AAA010='RSTUVWXYZ'
     C                   EVAL      AAA005='ABCDE'
     C                   MOVEL(P)  AAA005        AAA010
     C                   EVAL      DSP=AAA010
     C                   DSPLY                   DSP
    MU* VAL1(AAA010) VAL2('ABCDEWXYZ ') COMP(EQ)
     C                   EVAL      AAA010='RSTUVWXYZ'
     C                   EVAL      AAA005='ABCDE'
     C                   MOVEL     AAA005        AAA010
     C                   EVAL      DSP=AAA010
     C                   DSPLY                   DSP
      *
     C                   SETON                                        LR
