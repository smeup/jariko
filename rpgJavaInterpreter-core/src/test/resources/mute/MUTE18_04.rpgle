     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 23/07/21  003076  BUSFIO Creazione mute
     V* 26/07/21  003076  BUSFIO Rinominato mute
     V* ==============================================================
     D*  OBIETTIVO
     D*  Programma finalizzato ai test sulle /API: Programma
     D*  che chiama la COPY MUTE18_04A
     V* ==============================================================
      *
      /API mute,MUTE18_04A
      * Variabile
     C                   EVAL      A=25
     C                   EVAL      B=18
     C                   EVAL      C=43
      * Eseguo subroutine in API
    MU* VAL1(RESULT) VAL2(18) COMP(EQ)
     C                   EXSR      CHKMIN
      *
     C                   SETON                                        LR
