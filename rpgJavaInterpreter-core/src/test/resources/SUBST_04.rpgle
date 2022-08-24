     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 23/08/22  004118  BUSFIO Creazione
     V*=====================================================================
     D T               S              2  0                                      Pos iniziale
     D LEN             S              2  0                                      Lunghezza
     D STRING          S             15                                         Str iniziale
     D RESULT          S             10                                         Risultato
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      * The SUBST operation extracts the substring from factor 2 starting
      * at position 5. The value '123ABC' is placed in the result field RESULT.
     C                   EVAL      T = 5
     C                   EVAL      STRING = 'TEST123ABC'
     C                   SUBST     STRING:T      RESULT
     C     RESULT        DSPLY
     C                   SETON                                        LR
