     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 18/02/20  001577  BMA Creazione
     V*=====================================================================
      *---------------------------------------------------------------
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
     D AAA010          S             10
      *---------------------------------------------------------------
    MU* VAL1(AAA010) VAL2('NOT DONE!') COMP(EQ)
     C                   EVAL      AAA010='NOT DONE!'
    MU* VAL1(*IN50) VAL2('1') COMP(EQ)
     C                   EVAL      *IN50=*ON
    MU* VAL1(*IN51) VAL2('0') COMP(EQ)
     C  N50              EVAL      *IN51=*ON
    MU* VAL1(*IN52) VAL2('1') COMP(EQ)
     C   50              EVAL      *IN52=*ON
     C   50              DO        2
     C                   EVAL      AAA010='DONE!'
     C                   ENDDO
    MU* VAL1(AAA010) VAL2('DONE!') COMP(EQ)
     C                   DSPLY                   AAA010
     C   52
     CANN50              DO        2
     C                   EVAL      AAA010='OK!'
     C                   ENDDO
    MU* VAL1(AAA010) VAL2('DONE!') COMP(EQ)
     C                   DSPLY                   AAA010
     C   52
     CORN50              DO        2
     C                   EVAL      AAA010='YES'
     C                   ENDDO
    MU* VAL1(AAA010) VAL2('YES') COMP(EQ)
     C                   DSPLY                   AAA010
      *
     C                   SETON                                        LR
      *---------------------------------------------------------------
