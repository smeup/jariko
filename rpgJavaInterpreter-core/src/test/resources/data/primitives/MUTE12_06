   COP* *NOUI
     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 22/08/19  001071  BMA Creato
     V*=====================================================================
     V* OBIETTIVO
     V*  Programma finalizzato ai test su campi di tipo INDICATOR
     V*
     V*=====================================================================
      *
      * in ogni programma sono automaticamente dichiarati 99 indicatori, da *IN01 a *IN99 .
      * *ON corrisponde a '1' e *OFF a '0' .
      * un campo indicatore definito come campo stand alone è inizializzato di default a *OFF.
      * un campo indicatore all'interno di una DS deve essere inizializzato esplicitamente,
      * altrimenti è ' '.
      *
      *
    MU* VAL1(*IN35) VAL2('1') COMP(EQ)
     C                   EVAL      *IN35=*ON
    MU* VAL1(*IN35) VAL2('1') COMP(EQ)
     C                   EVAL      *IN35='1'
    MU* VAL1(*IN35) VAL2('1') COMP(EQ)
     C                   SETON                                        35
    MU* VAL1(*IN35) VAL2('1') COMP(EQ)
     C                   SETON                                          35
    MU* VAL1(*IN35) VAL2('1') COMP(EQ)
     C                   SETON                                            35
    MU* VAL1(*IN35) VAL2('0') COMP(EQ)
     C                   EVAL      *IN35=*OFF
    MU* VAL1(*IN35) VAL2('0') COMP(EQ)
     C                   EVAL      *IN35='0'
    MU* VAL1(*IN35) VAL2('0') COMP(EQ)
     C                   SETOFF                                       35
    MU* VAL1(*IN35) VAL2('0') COMP(EQ)
     C                   SETOFF                                         35
    MU* VAL1(*IN35) VAL2('0') COMP(EQ)
     C                   SETOFF                                           35
      *
    MU* VAL1(*IN35) VAL2('1') COMP(EQ)
     C                   EVAL      *IN35=*ON
    MU* VAL1(*IN50) VAL2('1') COMP(EQ)
     C                   EVAL      *IN50=*ON
    MU* VAL1(*IN10) VAL2('1') COMP(EQ)
     C                   EVAL      *IN10=*ON
      * Questa istruzione è equivalente alle 3 precedenti
      * L'ordine degli indicatori nelle 3 posizioni possibili rispetto a SETON/SETOFF è indifferente
    MU* VAL1(*IN10) VAL2('1') COMP(EQ)
    MU* VAL1(*IN50) VAL2('1') COMP(EQ)
    MU* VAL1(*IN35) VAL2('1') COMP(EQ)
     C                   SETON                                        105035
      *
    MU* VAL1(*IN35) VAL2('0') COMP(EQ)
     C                   EVAL      *IN35=*OFF
    MU* VAL1(*IN50) VAL2('0') COMP(EQ)
     C                   EVAL      *IN50=*OFF
    MU* VAL1(*IN10) VAL2('0') COMP(EQ)
     C                   EVAL      *IN10=*OFF
      * Questa istruzione è equivalente alle 3 precedenti
      * L'ordine degli indicatori nelle 3 posizioni possibili rispetto a SETON/SETOFF è indifferente
    MU* VAL1(*IN10) VAL2('0') COMP(EQ)
    MU* VAL1(*IN50) VAL2('0') COMP(EQ)
    MU* VAL1(*IN35) VAL2('0') COMP(EQ)
     C                   SETOFF                                       105035
      * Gli indicatori da 01 a 99 possono essere trattari sia come schiera che come campi singoli
      * Accendo tutti gli indicatori da 01 a 99
    MU* VAL1(*IN01) VAL2('1') COMP(EQ)
    MU* VAL1(*IN70) VAL2('1') COMP(EQ)
    MU* VAL1(*IN99) VAL2('1') COMP(EQ)
     C                   EVAL      *IN=*ON
      * La clear di un indicatore equivale a spegnerlo.
    MU* VAL1(*IN35) VAL2('0') COMP(EQ)
     C                   CLEAR                   *IN(35)
    MU* VAL1(*IN35) VAL2('0') COMP(EQ)
     C                   CLEAR                   *IN35
    MU* VAL1(*IN50) VAL2('0') COMP(EQ)
     C                   CLEAR                   *IN(50)
    MU* VAL1(*IN50) VAL2('0') COMP(EQ)
     C                   CLEAR                   *IN50
    MU* VAL1(*IN10) VAL2('0') COMP(EQ)
     C                   CLEAR                   *IN(10)
    MU* VAL1(*IN10) VAL2('0') COMP(EQ)
     C                   CLEAR                   *IN10
      * Spengo tutti gli indicatori da 01 a 99
    MU* VAL1(*IN01) VAL2('0') COMP(EQ)
    MU* VAL1(*IN70) VAL2('0') COMP(EQ)
    MU* VAL1(*IN99) VAL2('0') COMP(EQ)
     C                   EVAL      *IN=*OFF
      *
    MU* Type="NOXMI"
      * LR e RT stessi sono indicatori
     C                   SETON                                        LR
