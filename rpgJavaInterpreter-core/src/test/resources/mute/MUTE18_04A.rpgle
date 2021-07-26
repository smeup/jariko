     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 23/07/21  003076  BUSFIO Creazione mute
     V* 26/07/21  003076  BUSFIO Rinominato mute
     V* ==============================================================
     D*  OBIETTIVO
     D*  Programma finalizzato ai test sulle /API: programma API per
     D*  testare MUTE18_04 restituisce il valore minore
     V* ==============================================================
     D A               S              2  0
     D B               S              2  0
     D C               S              2  0
     D RESULT          S              2  0
      *--------------------------------------------------------------*
     C     CHKMIN        BEGSR
      *
     C                   IF        A<B
     C                   IF        A<C
     C                   EVAL      RESULT = A
     C                   ELSE
     C                   EVAL      RESULT = C
     C                   ENDIF
     C                   ELSE
     C                   IF        B<C
     C                   EVAL      RESULT = B
     C                   ELSE
     C                   EVAL      RESULT = C
     C                   ENDIF
     C                   ENDIF
      *
     C                   ENDSR
