      *====================================================================
      * smeup V6R1.021DV
      * Nome sorgente       : MU711004
      * Sorgente di origine : QTEMP/SRC(MU711004)
      * Esportato il        : 20240409 121031
      *====================================================================
     V* ==============================================================
     V* MODIFICHE Ril.  T Au Descrizione
     V* gg/mm/aa  nn.mm i xx Breve descrizione
     V* ==============================================================
     V* 23/03/24  MUTEST  GUAGIA Creazione
     V*=====================================================================
    O *  OBIETTIVO
    O * Testare la sostituzione di /API con /COPY
      *
    O * NB: Su AS400 per compilarli sostituire l'istruzione /API con /COPY e aggiungere
      *     la copy £HEX (compilando con il 14 non viene inclusa dal precompilatore).
      *     Una volta creato l'oggetto, rimodificare il sorgente ed esportarlo
      *
     V* ==============================================================
     D A71_01          S              1
     D A71_02          S              1
     D A71_03          S              1
      * --------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_D
      /COPY QILEGEN,£TABB£1DS
      /COPY QILEGEN,£ATRE
      /COPY QILEGEN,£PDS
      *---------------------------------------------------------------------
    RD* M A I N
      *---------------------------------------------------------------------
     C                   EVAL      £DBG_Pgm = 'MU711004'
     C                   EVAL      £DBG_Sez = 'A10'
     C                   EVAL      £DBG_Fun = '*INZ'
     C                   EXSR      £DBG
     C                   EXSR      SEZ_A10
     C                   EXSR      £DBG
     C                   EVAL      £DBG_Fun = '*END'
     C                   EXSR      £DBG
     C                   SETON                                        LR
      *---------------------------------------------------------------------
    RD* Test atomico /COPY £OAV
      *---------------------------------------------------------------------
     C     SEZ_A10       BEGSR
    OA* A£.CDOP(/API)
     D* Testare la sostituzione di /API con /COPY nel caso £C5PES (deve includere £HEX)
     C                   EVAL      £DBG_Pas='P04'
      * testo che le definizioni della £HEX ci siano
     C                   EVAL      A71_01=£HEX0E
     C                   EVAL      A71_02=£HEX3F
     C                   EVAL      A71_03=£HEXFF
     C                   EVAL      £DBG_Str='A71_01('+A71_01+') '+
     C                                      'A71_02('+A71_02+') '+
     C                                      'A71_03('+A71_03+') '
      *
     C                   ENDSR
      *---------------------------------------------------------------------
      /COPY QILEGEN,MULANG_D_C
      /API QILEGEN,£C5PES
      /COPY QILEGEN,£C5P
      /COPY QILEGEN,£ATRC
