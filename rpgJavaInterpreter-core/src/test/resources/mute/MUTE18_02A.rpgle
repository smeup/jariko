     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 23/07/21  003076  BUSFIO Creazione mute
     V* 26/07/21  003076  BUSFIO Rinominato mute
     V* ==============================================================
     D*  OBIETTIVO
     D*  Programma finalizzato ai test sulle /API: programma API per
     D*  testare MUTE18_02 restituendo area e perimetro triangolo
     V* ==============================================================
     D b               S              2  0
     D h               S              2  0
     D l1              S              2  0
     D l2              S              2  0
     D AREA            S              3  0
     D PERIMETER       S              3  0
      *--------------------------------------------------------------*
     C     TRIARE        BEGSR
      *
     C                   EVAL      AREA = (b * h) / 2
      *
     C                   ENDSR
      *--------------------------------------------------------------*
     C     TRIPER        BEGSR
      *
     C                   EVAL      PERIMETER = b + l1 + l2
      *
     C                   ENDSR
      *--------------------------------------------------------------*
