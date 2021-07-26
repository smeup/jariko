     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 23/07/21  003076  BUSFIO Creazione mute
     V* 26/07/21  003076  BUSFIO Rinominato mute
     V* ==============================================================
     D*  OBIETTIVO
     D*  Programma finalizzato ai test sulle /API: programma chiamante
     V* ==============================================================
      * Schiera per test
     D TXT             S            100    DIM(2) CTDATA PERRCD(1)
      *
      /API mute,MUTE18_01A
      * Definizione Stringhe
     C                   EVAL      S1 = 'Prova di '
     C                   EVAL      S2 = 'concatenam'
     C                   EVAL      S3 = 'ento   '
      * Eseguo subroutine in API
    MU* VAL1(RESULT) VAL2(TXT(1)) COMP(EQ)
     C                   EXSR      CONCAT
      *
     C                   SETON                                        LR
** CTDATA TXT
Prova di  concatenamento
