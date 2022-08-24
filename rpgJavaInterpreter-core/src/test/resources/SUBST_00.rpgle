     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 22/08/22  004118  BUSFIO Creazione
     V*=====================================================================
     D STRING          S             10                                         Str iniziale
     D RESULT          S             10                                         Risultato
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      * The SUBST operation extracts the substring from factor 2 starting
      * at position 1 for a length of 2.  The value 'AB' is placed in the
      * result field RESULT.
     C                   EVAL      STRING= 'ABCDEF'
     C     2             SUBST     STRING        RESULT
     C     RESULT        DSPLY
     C                   SETON                                        LR
