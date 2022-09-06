     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 22/08/22  004118  BUSFIO Creazione
     V*=====================================================================
     D T               S              2  0                                      Pos iniziale
     D STRING          S             10                                         Str iniziale
     D RESULT          S             10                                         Risultato
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      * The SUBST operation extracts the substring from factor 2 starting
      * at position 5 for a length of 2.  The value 'EF' is placed in the
      * result field RESULT.
     C                   EVAL      T = 5
     C                   EVAL      STRING= 'ABCDEF'
     C     2             SUBST     STRING:T      RESULT
     C     RESULT        DSPLY
     C                   SETON                                        LR
