     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 23/07/21  003076  BUSFIO Creazione mute
     V* 26/07/21  003076  BUSFIO Rinominato mute
     V* ==============================================================
     D*  OBIETTIVO
     D*  Programma finalizzato ai test sulle /API: Programma
     D*  che chiama la COPY MUTE18_03A
     V* ==============================================================
      * Schiera per test
     D TXT             S            100    DIM(2) CTDATA PERRCD(1)
      *
      /API MUTE,MUTE18_03A
      * Variabile
     C                   EVAL      TEMP=36
      * Eseguo subroutine in API
    MU* VAL1(RESULT) VAL2(TXT(1)) COMP(EQ)
     C                   EXSR      CHKTEM
      *
     C                   SETON                                        LR
** CTDATA TXT
Your temperature is 36. You feel Well!