     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 19/02/20  001577  BMA Creazione
     V*=====================================================================
      *---------------------------------------------------------------
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      * Set off indicators 01-99
    MU* VAL1(*IN01) VAL2('0') COMP(EQ)
    MU* VAL1(*IN99) VAL2('0') COMP(EQ)
     C                   MOVEA     *ALL'0'       *IN(01)
      * Set on indicators 01-99
    MU* VAL1(*IN01) VAL2('1') COMP(EQ)
    MU* VAL1(*IN99) VAL2('1') COMP(EQ)
     C                   MOVEA     *ALL'1'       *IN(01)
      * Set off indicators 01-99
    MU* VAL1(*IN01) VAL2('0') COMP(EQ)
    MU* VAL1(*IN99) VAL2('0') COMP(EQ)
     C                   MOVEA     *ALL'0'       *IN
      * Set on indicators 20-99
    MU* VAL1(*IN01) VAL2('0') COMP(EQ)
    MU* VAL1(*IN20) VAL2('1') COMP(EQ)
    MU* VAL1(*IN99) VAL2('1') COMP(EQ)
     C                   MOVEA     *ALL'1'       *IN(20)
     C                   SETON                                        LR
      *---------------------------------------------------------------
