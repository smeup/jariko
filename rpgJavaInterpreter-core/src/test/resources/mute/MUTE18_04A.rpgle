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
     D*  Programma finalizzato ai test sulle /API: programma API per
     D*  testare MUTE18_04 restituisce il valore minore
     V* ==============================================================
     D VALUES          s              2s 0 dim(3)
     D RESULT          S              2  0
      *--------------------------------------------------------------*
     C     CHKMIN        BEGSR
      *
     C                   SORTA     VALUES
      *
     C                   EVAL      RESULT = VALUES(1)
      *
     C                   ENDSR
