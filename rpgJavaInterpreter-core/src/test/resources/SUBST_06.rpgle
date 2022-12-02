     V*=====================================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V*=====================================================================
     V* 23/08/22  004118  BUSFIO Creazione
     V*=====================================================================
     D AAA010          S             10                                         Str iniziale 10
      *---------------------------------------------------------------
     D* M A I N
      *---------------------------------------------------------------
      * Substring from factor 2 starting at position 2 for a length of 1.
      * The value placed in RESULT field is 'A'.
      * In this case, the result variable 'AAA001' is define inline.
      *
     C                   EVAL      AAA010='1A'
     C     1             SUBST(P)  AAA010:2      AAA001            1
      *
     C     AAA001        DSPLY
