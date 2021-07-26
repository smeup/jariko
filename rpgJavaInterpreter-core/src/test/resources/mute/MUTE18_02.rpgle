     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 23/07/21  003076  BUSFIO Creazione mute
     V* 26/07/21  003076  BUSFIO Rinominato mute
     V* ==============================================================
     D*  OBIETTIVO
     D*  Programma finalizzato ai test sulle /API: Programma
     D*  che chiama la COPY MUTE18_02A
     V* ==============================================================
      /API mute,MUTE18_02A
      * Calcolo Area triangolo
     C                   EVAL      b = 10
     C                   EVAL      h = 3
      * Eseguo subroutine in API
    MU* VAL1(AREA) VAL2(15) COMP(EQ)
     C                   EXSR      TRIARE
      * Calcolo Perimetro triangolo
     C                   EVAL      l1 = 4
     C                   EVAL      l2 = 4
      * Eseguo subroutine in API
    MU* VAL1(PERIMETER) VAL2(18) COMP(EQ)
     C                   EXSR      TRIPER
      *
     C                   SETON                                        LR
