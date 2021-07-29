     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 23/07/21  003076  BUSFIO Creazione mute
     V* 26/07/21  003076  BUSFIO Rinominato mute
     V* 26/07/21  V5R1    BMA    Check-out 003076 in SMEDEV
     V* 29/07/21  003090  BUSFIO Modificato mute
     V* ==============================================================
     D*  OBIETTIVO
     D*  Programma finalizzato ai test sulle /API: Programma
     D*  che chiama la COPY MUTE18_04A
     V* ==============================================================
      *
      /API mute,MUTE18_04A
      * Variabile
     C                   EVAL      VALUES(1)=25
     C                   EVAL      VALUES(2)=18
     C                   EVAL      VALUES(3)=43
      * Eseguo subroutine in API
    MU* VAL1(RESULT) VAL2(18) COMP(EQ)
     C                   EXSR      CHKMIN
      *
     C                   SETON                                        LR
