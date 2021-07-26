     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 23/07/21  003076  BUSFIO Creazione mute
     V* 26/07/21  003076  BUSFIO Rinominato mute
     V* ==============================================================
     D*  OBIETTIVO
     D*  Programma finalizzato ai test sulle /API: programma API perte
     D*  MUTE18_01 che concatena stringhe
     V* ==============================================================
     D S1              S             10
     D S2              S             10
     D S3              S             10
     D RESULT          S             30
      *--------------------------------------------------------------*
     C     CONCAT        BEGSR
      *
     C                   EVAL      RESULT = S1 + S2 + S3
      *
     C                   ENDSR
      *--------------------------------------------------------------*
