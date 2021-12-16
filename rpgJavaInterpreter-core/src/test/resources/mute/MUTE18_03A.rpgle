     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 23/07/21  003076  BUSFIO Creazione mute
     V* 26/07/21  003076  BUSFIO Rinominato mute
     V* ==============================================================
     D*  OBIETTIVO
     D*  Programma finalizzato ai test sulle /API: programma API per
     D*  testare MUTE18_03 restituendo una stringa concatenata con
     D*  numero
     V* ==============================================================
     D TEMP            S              2  0
     D RESULT          S             40
      *--------------------------------------------------------------*
     C     CHKTEM        BEGSR
      *
     C                   IF        TEMP>34 AND TEMP<=37
     C                   EVAL      RESULT = 'Your temperature is '
     C                             +%EDITC(TEMP:'Z') +'. You feel Well!'
     C                   ENDIF
      *
     C                   ENDSR
